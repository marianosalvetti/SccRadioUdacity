package ar.com.sccradiomobile.livescores;

import android.media.AudioManager;
import android.media.SoundPool;
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
import ar.com.sccradiomobile.livescores.multicolumnlistadapter.EstadisticasListviewAdapter;
import ar.com.sccradiomobile.storage.model.livescores.Estadistica;
import ar.com.sccradiomobile.storage.model.livescores.EstadisticasPartidoDataResponse;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.storage.util.request.GsonRequest;
import ar.com.sccradiomobile.utils.Utils;
import com.actionbarsherlock.app.SherlockFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mariano Salvetti
 * <p/>
 * En este Fragment cargaremos las estadisticas de cada partido, en una grilla ponemos
 * primero la info del LOCAL, y luego la info del visistante, los numeros por jugador.
 * Con el componente "Swipe to Refresh" vamos a actualizar esos valores, siempre que el partido no este finalizado.
 * <p/>
 * <p/>
 * <p/>
 * TRABAJAR ACA EL MIERCOLES Y JUEVES tal vez....
 * Para development apuntamos a:
 * http://sccradio.com.ar/carpetadudo/lnb/estadisticas_juego_1.json
 */
public class DetalleEstadisticasPartidoFragment extends SherlockFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = "live_Scores_Estadisticas";
    /**
     * The component for the refresh of jugadas in this fragment.
     */
    SwipeRefreshLayout swipeRefresh;
    private TextView textViewTitle;
    private ListView listview;
    private EstadisticasListviewAdapter adapter;
    private ArrayList<String> values;

    private SoundPool soundPool;
    private int soundID;
    private boolean loaded = false;
    private boolean partidoFinalizado = false;
    /**
     * Mock list of elements, for test only.
     */
    private List<HashMap<String, String>> mapList;
    /**
     * Key to insert the PARTIDO_ID into the mapping of a Bundle.
     */
    public static final String PARTIDO_ID = "partidoId";
    public static final String EQUIPO_LOCAL = "local";
    public static final String EQUIPO_VISITANTE = "visitante";

    int partidoId;
    String local;
    String visitante;
    String jornada = "1";

    /**
     * Instances a new fragment with a background color and an index page.
     *
     * @param partidoId id del partido a mostrar
     * @param local     team local
     * @return a new page
     */
    public static DetalleEstadisticasPartidoFragment newInstance(int partidoId, String local, String visitante) {

        // Instantiate a new fragment
        DetalleEstadisticasPartidoFragment fragment = new DetalleEstadisticasPartidoFragment();

        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(PARTIDO_ID, partidoId);
        bundle.putString(EQUIPO_LOCAL, local);
        bundle.putString(EQUIPO_VISITANTE, visitante);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);

        return fragment;

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
        //tomar los elementos a mostrar...
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.detalle_partido_estadisticas_tab, container, false);


        // Nuevo Elemento: Swipe Refresh Layout
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutEstadisticas);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_light);

        listview = (ListView) rootView.findViewById(R.id.listViewEstadisticas);

        // Show the current partidoId in the view
        TextView tvIndex = (TextView) rootView.findViewById(R.id.tvPartidoId);
        tvIndex.setText(this.getSherlockActivity().getString(R.string.push_to_refresh_msg));

        TextView tvLocal = (TextView) rootView.findViewById(R.id.titulo_local);
        tvLocal.setText(String.valueOf(this.local));

        TextView tvVisitante = (TextView) rootView.findViewById(R.id.titulo_visitante);
        tvVisitante.setText(String.valueOf(this.visitante));


        // Extra: sonido al actualizar con SwipeRefreshLayout
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundID = soundPool.load(this.getSherlockActivity().getApplicationContext(), R.raw.ping, 1);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        // Define the GRID....
        mapList = new LinkedList<HashMap<String, String>>();
        populateListByPartidoId();
        adapter = new EstadisticasListviewAdapter(this.getSherlockActivity(), mapList);
        listview.setAdapter(adapter);
        return rootView;
    }

    private void populateListByPartidoId() {
        //TODO: usar ID de partido aca.
        jornada = (this.partidoId > 3) ? "1" : String.valueOf(partidoId);

        //   int startIndex = 1 + adapter.getCount();
        // TODO Use Uri.Builder
     /*		String url = "https://picasaweb.google.com/data/feed/api/all?q="
                     + photoOf + "&thumbsize=" + thumbSize + "&max-results="
                    + RESULT_PER_PAGE + "&start-index=" + startIndex + "&alt=json";
       */
        String url = "http://ernimantel.com.ar/carpetadudo/lnb/estadisticas_juego_" + jornada + ".json";
        if (Utils.isConnected(this.getSherlockActivity())) {//&& isRunning
            //    final ProgressDialog pDialog = new ProgressDialog(this.getSherlockActivity());
            //   pDialog.setMessage("Cargando...");
            if (this.getSherlockActivity() != null && !this.getSherlockActivity().isFinishing()) {
                //        pDialog.show();
            }
            Request<EstadisticasPartidoDataResponse> request = createRequest(jornada, url);

            SCCRadioMobileApp.REQUEST_MANAGER.addToRequestQueue(request, SCCRadioMobileApp.TAG);
        }
    }

    private Request<EstadisticasPartidoDataResponse> createRequest(final String jornada, String url) {
        if (Constants.DEBUG) Log.d(LOG_TAG, "request para ---->> " + jornada);
        Response.Listener<EstadisticasPartidoDataResponse> successListener = new Response.Listener<EstadisticasPartidoDataResponse>() {

            @Override
            public void onResponse(EstadisticasPartidoDataResponse response) {
                //   Log.d(LOG_TAG, "   RESPONSE ---->> " + response.toString());
                if (Constants.DEBUG) Log.d(LOG_TAG, " ESTADO DEL RESPONSE ---->> " + response.getEstado());
                //     pDialog.hide();
                //actualizar el listado de partidos
                final ArrayList<Estadistica> estadisticasLocal = response.getDatos().getEstadisticas_local();
                final ArrayList<Estadistica> estadisticasVisitante = response.getDatos().getEstadisticas_visitante();

                List<HashMap<String, String>> nuevasEstadisticasLocal = parseEstadisticas(estadisticasLocal);
                List<HashMap<String, String>> nuevasEstadisticasVisitantes = parseEstadisticas(estadisticasVisitante);
                mapList.clear();
                mapList.addAll(0, nuevasEstadisticasVisitantes);
                mapList.addAll(0, nuevasEstadisticasLocal);
                adapter.notifyDataSetChanged();

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (Constants.DEBUG) Log.d(LOG_TAG, "Error: " + error.getMessage());
                //    pDialog.hide();
                textViewTitle.setText("Ocurrió un error al buscar resultados");
                listview.setVisibility(View.GONE);
            }
        };

        GsonRequest<EstadisticasPartidoDataResponse> request = new GsonRequest<EstadisticasPartidoDataResponse>(
                Request.Method.GET, url, EstadisticasPartidoDataResponse.class,
                successListener, errorListener);

        return request;
    }

    /**
     * A partir de los datos que recibimos del servidor, parseamos la informacion y
     * creamos la estructura de datos para pasar al Adapter.
     *
     * @param estadisticas
     * @return Stack de Hashmaps
     */
    private List<HashMap<String, String>> parseEstadisticas(ArrayList<Estadistica> estadisticas) {
        List<HashMap<String, String>> rval = new LinkedList<HashMap<String, String>>();

        HashMap<String, String> temp;
        Estadistica estadistica;
        for (int i = 0; i < estadisticas.size(); i++) {
            temp = new HashMap<String, String>();
            estadistica = estadisticas.get(i);
            //   Log.d(LOG_TAG, "--->> # " + estadistica.getNumero() + " " + estadistica.getNombre());
            temp.put(EstadisticasListviewAdapter.NRO_COLUMN, String.valueOf(estadistica.getNumero()));
            temp.put(EstadisticasListviewAdapter.JUGADOR_COLUMN, estadistica.getNombre());
            temp.put(EstadisticasListviewAdapter.PUNTOS_COLUMN, String.valueOf(estadistica.getPuntos()));

            temp.put(EstadisticasListviewAdapter.LIBRES_COLUMN, estadistica.getEstadisticaLibres());
            temp.put(EstadisticasListviewAdapter.DOBLES_COLUMN, estadistica.getEstadisticaDobles());
            temp.put(EstadisticasListviewAdapter.TRIPLES_COLUMN, estadistica.getEstadisticaTriples());
            temp.put(EstadisticasListviewAdapter.CONVERSIONES_COLUMN, estadistica.getEstadisticaConversiones());
            temp.put(EstadisticasListviewAdapter.REBOTES_TOTAL_COLUMN, estadistica.getEstadisticarRebotesTotales());
            temp.put(EstadisticasListviewAdapter.ASISTENCIAS_COLUMN, String.valueOf(estadistica.getAsistencias()));
            temp.put(EstadisticasListviewAdapter.TAPAS_COLUMN, String.valueOf(estadistica.getTapones()));
            temp.put(EstadisticasListviewAdapter.PELOTAS_PERDIDAS_COLUMN, String.valueOf(estadistica.getPelotasPerdidas()));
            temp.put(EstadisticasListviewAdapter.PELOTAS_ROBADAS_COLUMN, String.valueOf(estadistica.getPelotasRecuperadas()));
            temp.put(EstadisticasListviewAdapter.FALTAS_PERSONALES_COLUMN, String.valueOf(estadistica.getFaltasPersonales()));
            temp.put(EstadisticasListviewAdapter.FALTAS_RECIBIDAS_COLUMN, String.valueOf(estadistica.getFaltasRecibidas()));
            temp.put(EstadisticasListviewAdapter.TIEMPO_DE_JUEGO_COLUMN, String.valueOf(estadistica.getTiempoDeJuego()));
            temp.put(EstadisticasListviewAdapter.VALORACION_COLUMN, String.valueOf(estadistica.getValoracion()));
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
            updateEstadisticasDeJugadas();
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

    private void updateEstadisticasDeJugadas() {
        if (jornada.equals("1")) {
            jornada = "2";
        } else if (jornada.equals("2")) {
            jornada = "1";
        } else {
            jornada = "2";
        }
        String url = "http://ernimantel.com.ar/carpetadudo/lnb/estadisticas_juego_" + jornada + ".json";
        if (Utils.isConnected(this.getSherlockActivity()) && !partidoFinalizado) {
            //     final ProgressDialog pDialog = new ProgressDialog(this.getSherlockActivity());
            //     pDialog.setMessage("Cargando...");
            //     if(this.getSherlockActivity()!= null && !this.getSherlockActivity().isFinishing()){
            //        pDialog.show();
            //    }
            Request<EstadisticasPartidoDataResponse> request = createRequest(jornada, url);

            SCCRadioMobileApp.REQUEST_MANAGER.addToRequestQueue(request, SCCRadioMobileApp.TAG);
        }
    }


}