package ar.com.sccradiomobile.livescores;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.livescores.multicolumnlistadapter.JugadasListviewAdapter;
import ar.com.sccradiomobile.storage.model.livescores.DetalleJugada;
import ar.com.sccradiomobile.storage.model.livescores.DetalleJugadaDataResponse;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.storage.util.request.GsonRequest;
import ar.com.sccradiomobile.utils.Utils;
import com.actionbarsherlock.app.SherlockFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Mariano Salvetti
 * <p/>
 * En este Fragment vamos a ir mostrando las jugadas de cada partido, teniendo que ir a pegarle al Servidor
 * para traer la infor de un partido, segun su ID.
 * <p/>
 */
public class DetalleJugadasPartidoFragment extends SherlockFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String LOG_TAG = "live_Scores_Jugadas";
    /**
     * The component for the refresh of jugadas in this fragment.
     */
    SwipeRefreshLayout swipeRefresh;
    private TextView textViewTitle;
    private ListView listview;
    private JugadasListviewAdapter adapter;
    private ArrayList<String> values;

    private SoundPool soundPool;
    private int soundID;
    private boolean loaded = false;
    private boolean partidoFinalizado = false;

    /**
     * Key to insert the PARTIDO_ID into the mapping of a Bundle.
     */
    public static final String PARTIDO_ID = "partidoId";
    public static final String EQUIPO_LOCAL = "local";
    public static final String EQUIPO_VISITANTE = "visitante";

    private int partidoId;
    private String local;
    private String visitante;
    /**
     * Mock list of elements, for test only.
     */
    // private ArrayList<HashMap<String, String>> list;
    private Stack<HashMap<String, String>> pila;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static DetalleJugadasPartidoFragment newInstance(int partidoId, String local, String visitante) {
        DetalleJugadasPartidoFragment fragment = new DetalleJugadasPartidoFragment();
        Bundle args = new Bundle();
        args.putInt(PARTIDO_ID, partidoId);
        args.putString(EQUIPO_LOCAL, local);
        args.putString(EQUIPO_VISITANTE, visitante);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    public DetalleJugadasPartidoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Load parameters when the initial creation of the fragment is done
        this.partidoId = (getArguments() != null) ? getArguments().getInt(PARTIDO_ID) : -1;
        this.local = (getArguments() != null) ? getArguments().getString(EQUIPO_LOCAL) : "Atenas";
        this.visitante = (getArguments() != null) ? getArguments().getString(EQUIPO_VISITANTE) : "Regatas";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.detalle_partido_jugadas, container, false);

        // Nuevo Elemento: Swipe Refresh Layout

        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_light);

        listview = (ListView) rootView.findViewById(R.id.listView);


        textViewTitle = (TextView) rootView.findViewById(R.id.titulo_seccion);
        textViewTitle.setText(this.getSherlockActivity().getString(R.string.msg_jugadas) +"-" + this.getSherlockActivity().getString(R.string.push_to_refresh_msg));


        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundID = soundPool.load(this.getSherlockActivity().getApplicationContext(), R.raw.ping, 1);

        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });


        pila = new Stack<HashMap<String, String>>();
        populateListByPartidoId();
        adapter = new JugadasListviewAdapter(this.getSherlockActivity(), pila);
        listview.setAdapter(adapter);
        return rootView;
    }

    private void populateListByPartidoId() {
        String jornada = "1"; //TODO: usar ID de partido aca.
        jornada = (this.partidoId > 2) ? "1" : String.valueOf(partidoId);


        String url = "http://sccradio.com.ar/carpetadudo/lnb/detalle_jugada_" + jornada + ".json";
        if (Utils.isConnected(this.getSherlockActivity())) {

            Request<DetalleJugadaDataResponse> request = createRequest(jornada, url);

            SCCRadioMobileApp.REQUEST_MANAGER.addToRequestQueue(request, SCCRadioMobileApp.TAG);
        }
    }

    private Request<DetalleJugadaDataResponse> createRequest(final String jornada, String url) {
        if (Constants.DEBUG) Log.d(LOG_TAG, "request para ---->> " + jornada);
        Response.Listener<DetalleJugadaDataResponse> successListener = new Response.Listener<DetalleJugadaDataResponse>() {

            @Override
            public void onResponse(DetalleJugadaDataResponse response) {
                if (Constants.DEBUG) Log.d(LOG_TAG, " ESTADO DEL RESPONSE ---->> " + response.getEstado());

                final ArrayList<DetalleJugada> jugadas = response.getDatos().getJugadas();
                Stack<HashMap<String, String>> nuevasJugadas = parseJugadas(jugadas);
                pila.addAll(0, nuevasJugadas);
                adapter.notifyDataSetChanged();

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (Constants.DEBUG) Log.d(LOG_TAG, "Error: " + error.getMessage());

                textViewTitle.setText(getSherlockActivity().getString(R.string.msg_error_stats));
                listview.setVisibility(View.GONE);
            }
        };

        GsonRequest<DetalleJugadaDataResponse> request = new GsonRequest<DetalleJugadaDataResponse>(
                Request.Method.GET, url, DetalleJugadaDataResponse.class,
                successListener, errorListener);

        return request;
    }

    /**
     * A partir de los datos que recibimos del servidor, parseamos la informacion y
     * creamos la estructura de datos para pasar al Adapter.
     *
     * @param jugadas
     * @return Stack de Hashmaps
     */
    private Stack<HashMap<String, String>> parseJugadas(ArrayList<DetalleJugada> jugadas) {
        Stack<HashMap<String, String>> rval = new Stack<HashMap<String, String>>();

        HashMap<String, String> temp;
        DetalleJugada jugada;
        for (int i = 0; i < jugadas.size(); i++) {
            temp = new HashMap<String, String>();
            jugada = jugadas.get(i);

            temp.put(JugadasListviewAdapter.FIRST_COLUMN, jugada.getPeriodo());
            temp.put(JugadasListviewAdapter.SECOND_COLUMN, jugada.getTiempo());
            temp.put(JugadasListviewAdapter.THIRD_COLUMN, jugada.getEquipo());
            temp.put(JugadasListviewAdapter.FOURTH_COLUMN, jugada.getJugador());
            temp.put(JugadasListviewAdapter.FIFTH_COLUMN, jugada.getAccion());
            temp.put(JugadasListviewAdapter.SIXTH_COLUMN, jugada.getScore());
            rval.add(temp);
        }
        return rval;
    }

    @Override
    public void onRefresh() {
        // Reproduccion de sonido
        if (loaded) {
            soundPool.play(soundID, 0.9f, 0.9f, 1, 0, 1f);
        }

        if (partidoFinalizado) {
            if (Constants.DEBUG) Log.d(LOG_TAG, "partidoFinalizado = " + partidoFinalizado);
            // Finalizar swipeRefresh
            swipeRefresh.setRefreshing(false);
            Toast.makeText(this.getActivity(), "Partido ya finalizado ", Toast.LENGTH_LONG).show();
        } else {
            if (Constants.DEBUG) Log.d(LOG_TAG, "partidoFinalizado = " + partidoFinalizado);
            updateListaDeJugadas();
            // Finalizar swipeRefresh aca?

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Finalizar swipeRefresh
                swipeRefresh.setRefreshing(false);
            }
        }, 2000);

    }

    /**
     * Utilizando el componente Pull_To_Refresh vamos a realizar un pedido
     * al Servidor de datos de la LNB y actualizar la pila de Jugadas.
     * TODO: Validar modelo de datos y forma de paginacion..
     * TODO: No està finalizado aun.
     */
    private void updateListaDeJugadas() {
        String url = "http://sccradio.com.ar/carpetadudo/lnb/detalle_jugada_extras.json";
        if (Utils.isConnected(this.getSherlockActivity()) && !partidoFinalizado) {

            Request<DetalleJugadaDataResponse> request = createRequest("extra", url);

            SCCRadioMobileApp.REQUEST_MANAGER.addToRequestQueue(request, SCCRadioMobileApp.TAG);
        }
    }

    /**
     * Generamos una lista dura de resultados de partidos posible.
     * Onl
     */
    private void populateList() {
        pila = new Stack<HashMap<String, String>>();

        HashMap<String, String> temp = new HashMap<String, String>();
        temp.put(JugadasListviewAdapter.FIRST_COLUMN, "4");
        temp.put(JugadasListviewAdapter.SECOND_COLUMN, "0:12");
        temp.put(JugadasListviewAdapter.THIRD_COLUMN, "GECR - INDALO");
        temp.put(JugadasListviewAdapter.FOURTH_COLUMN, "ALVAREZ GONZALO (15)");
        temp.put(JugadasListviewAdapter.FIFTH_COLUMN, "2 pts. convertido");
        temp.put(JugadasListviewAdapter.SIXTH_COLUMN, "87-74");
        pila.add(temp);

        HashMap<String, String> temp1 = new HashMap<String, String>();
        temp1.put(JugadasListviewAdapter.FIRST_COLUMN, "4");
        temp1.put(JugadasListviewAdapter.SECOND_COLUMN, "0:17");
        temp1.put(JugadasListviewAdapter.THIRD_COLUMN, "GECR - INDALO");
        temp1.put(JugadasListviewAdapter.FOURTH_COLUMN, "CAVACO DIEGO (13)");
        temp1.put(JugadasListviewAdapter.FIFTH_COLUMN, "3 pts. convertido");
        temp1.put(JugadasListviewAdapter.SIXTH_COLUMN, "84-72");
        pila.add(temp1);

        HashMap<String, String> temp2 = new HashMap<String, String>();
        temp2.put(JugadasListviewAdapter.FIRST_COLUMN, "4");
        temp2.put(JugadasListviewAdapter.SECOND_COLUMN, "0:22");
        temp2.put(JugadasListviewAdapter.THIRD_COLUMN, "ATL. BOCA JRS.");
        temp2.put(JugadasListviewAdapter.FOURTH_COLUMN, "FLOWERS GARY (1)");
        temp2.put(JugadasListviewAdapter.FIFTH_COLUMN, "libre errado");
        temp2.put(JugadasListviewAdapter.SIXTH_COLUMN, "84-72");
        pila.add(temp2);

        HashMap<String, String> temp3 = new HashMap<String, String>();
        temp3.put(JugadasListviewAdapter.FIRST_COLUMN, "4");
        temp3.put(JugadasListviewAdapter.SECOND_COLUMN, "0:31");
        temp3.put(JugadasListviewAdapter.THIRD_COLUMN, "ATL. BOCA JRS.");
        temp3.put(JugadasListviewAdapter.FOURTH_COLUMN, "SAFAR SELEM ( 16 )");
        temp3.put(JugadasListviewAdapter.FIFTH_COLUMN, "robo - intercepta pase");
        temp3.put(JugadasListviewAdapter.SIXTH_COLUMN, "84-72");
        pila.add(temp3);

        HashMap<String, String> temp4 = new HashMap<String, String>();
        temp4.put(JugadasListviewAdapter.FIRST_COLUMN, "4");
        temp4.put(JugadasListviewAdapter.SECOND_COLUMN, "0:37");
        temp4.put(JugadasListviewAdapter.THIRD_COLUMN, "GECR - INDALO");
        temp4.put(JugadasListviewAdapter.FOURTH_COLUMN, "ALVAREZ GONZALO (15)");
        temp4.put(JugadasListviewAdapter.FIFTH_COLUMN, "3 pts. convertido");
        temp4.put(JugadasListviewAdapter.SIXTH_COLUMN, "84-69");
        pila.add(temp4);

        HashMap<String, String> temp5 = new HashMap<String, String>();
        temp5.put(JugadasListviewAdapter.FIRST_COLUMN, "4");
        temp5.put(JugadasListviewAdapter.SECOND_COLUMN, "0:39");
        temp5.put(JugadasListviewAdapter.THIRD_COLUMN, "ATL. BOCA JRS.");
        temp5.put(JugadasListviewAdapter.FOURTH_COLUMN, "ALVAREZ GONZALO (15)");
        temp5.put(JugadasListviewAdapter.FIFTH_COLUMN, "2 pts. errado");
        temp5.put(JugadasListviewAdapter.SIXTH_COLUMN, "84-69");
        pila.add(temp5);

        HashMap<String, String> temp6 = new HashMap<String, String>();
        temp6.put(JugadasListviewAdapter.FIRST_COLUMN, "4");
        temp6.put(JugadasListviewAdapter.SECOND_COLUMN, "0:43");
        temp6.put(JugadasListviewAdapter.THIRD_COLUMN, "ATL. BOCA JRS.");
        temp6.put(JugadasListviewAdapter.FOURTH_COLUMN, "FAGGIANO LUCAS ( 3 )");
        temp6.put(JugadasListviewAdapter.FIFTH_COLUMN, "3 pts. errado");
        temp6.put(JugadasListviewAdapter.SIXTH_COLUMN, "84-69");
        pila.add(temp6);
    }
}