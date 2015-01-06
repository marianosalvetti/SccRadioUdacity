package ar.com.sccradiomobile.livescores;

import android.annotation.SuppressLint;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.livescores.multicolumnlistadapter.EstadisticasTotalesListviewAdapter;
import ar.com.sccradiomobile.storage.model.livescores.Estadistica;
import ar.com.sccradiomobile.storage.model.livescores.EstadisticasPartidoDataContainer;
import ar.com.sccradiomobile.storage.model.livescores.EstadisticasPartidoDataResponse;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.storage.util.request.GsonRequest;
import ar.com.sccradiomobile.utils.RobotoTextView;
import ar.com.sccradiomobile.utils.Utils;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Mariano Salvetti
 * <p/>
 * En este Fragment cargaremos todos los detalles de un partido especidifo, teninedo en cuenta si ha finalizado o no.
 * TODO: por construir, es una parte importante de la App.
 */
public class DetallePartidoFragment extends SherlockFragment {


    private static final String LOG_TAG = "live_Scores_Detalle";
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

    private RobotoTextView tvEstadoPartido;
    private TextView tvLocal;
    private TextView tvLocalPuntos;
    private TextView tvVisitante;
    private TextView tvVisitantePuntos;
    private TextView columnLocal;
    private TextView columnVisitante;

    private NetworkImageView escudo_local;
    private NetworkImageView escudo_visitante;

    // para los totales por equipo...
    /**
     * The component for the refresh of jugadas in this fragment.
     */
    SwipeRefreshLayout swipeRefresh;
    private ListView listview;
    private EstadisticasTotalesListviewAdapter adapter;
    private ArrayList<String> values;

    private SoundPool soundPool;
    private int soundID;
    private boolean loaded = false;
    private boolean partidoFinalizado = false;
    /**
     * Mock list of elements, for test only.
     */
    private LinkedList<EstadisticaTotal> mapList;

    /**
     * Instances a new fragment with a background color and an index page.
     *
     * @param partidoId id del partido a mostrar
     * @param local     team local
     * @return a new page
     */
    public static DetallePartidoFragment newInstance(int partidoId, String local, String visitante) {

        // Instantiate a new fragment
        DetallePartidoFragment fragment = new DetallePartidoFragment();

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

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //tomar los elementos a mostrar y luego vamos con los componentes...
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.detalle_partido_tab, container, false);

        ActionBar actionBar = super.getSherlockActivity().getSupportActionBar();
        TextView partido = (TextView) actionBar.getCustomView().findViewById(R.id.texto_header);
        partido.setVisibility(View.VISIBLE);
        partido.setText(String.valueOf(this.local) + " vs " + String.valueOf(this.visitante));
        actionBar.setDisplayShowCustomEnabled(true);

        listview = (ListView) rootView.findViewById(R.id.listViewEstadisticasEquipos);

        mapList = new LinkedList<EstadisticaTotal>();

        // Show the current partidoId in the view
        tvEstadoPartido = (RobotoTextView) rootView.findViewById(R.id.tvPartidoId);
        tvEstadoPartido.setText(String.valueOf(this.partidoId));

        columnLocal = (TextView) rootView.findViewById(R.id.SecondColumLocalText);
        tvLocal = (TextView) rootView.findViewById(R.id.tv_local);
        tvLocal.setText(String.valueOf(this.local));
        columnLocal.setText(String.valueOf(this.local));

        tvLocalPuntos = (TextView) rootView.findViewById(R.id.tv_local_puntos);
        tvLocalPuntos.setText(" ");

        columnVisitante = (TextView) rootView.findViewById(R.id.ThirdColumnVisitanteText);
        tvVisitante = (TextView) rootView.findViewById(R.id.tv_visitante);
        tvVisitante.setText(String.valueOf(this.visitante));
        columnVisitante.setText(String.valueOf(this.visitante));

        tvVisitantePuntos = (TextView) rootView.findViewById(R.id.tv_visitante_puntos);
        tvVisitantePuntos.setText(" ");

        escudo_local = (NetworkImageView) rootView.findViewById(R.id.escudo_local);
        escudo_local.setImageResource(R.drawable.no_image);
        escudo_visitante = (NetworkImageView) rootView.findViewById(R.id.escudo_visitante);
        escudo_visitante.setImageResource(R.drawable.no_image);

        populateScreenByPartidoId();
        adapter = new EstadisticasTotalesListviewAdapter(this.getSherlockActivity(), mapList);

        listview.setAdapter(adapter);
        return rootView;
    }

    private void populateScreenByPartidoId() {
        jornada = (this.partidoId > 3) ? "1" : String.valueOf(partidoId);

        String url = "http://sccradio.com.ar/carpetadudo/lnb/estadisticas_juego_" + jornada + ".json";
        if (Utils.isConnected(this.getSherlockActivity())) {//&& isRunning
            Request<EstadisticasPartidoDataResponse> request = createRequest(jornada, url);
            SCCRadioMobileApp.REQUEST_MANAGER.addToRequestQueue(request, SCCRadioMobileApp.TAG);
        }
    }

    private Request<EstadisticasPartidoDataResponse> createRequest(final String jornada, String url) {
        Response.Listener<EstadisticasPartidoDataResponse> successListener = new Response.Listener<EstadisticasPartidoDataResponse>() {

            @Override
            public void onResponse(EstadisticasPartidoDataResponse response) {
                final EstadisticasPartidoDataContainer responseDatos = response.getDatos();
                tvLocal.setText(responseDatos.getLocal());
                tvLocalPuntos.setText(responseDatos.getPuntosLocal());
                tvVisitante.setText(responseDatos.getVisitante());
                tvVisitantePuntos.setText(responseDatos.getPuntosVisitante());
                parseResponse(responseDatos);


            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (Constants.DEBUG) Log.d(LOG_TAG, "Error: " + error.getMessage());

                tvEstadoPartido.setText(getSherlockActivity().getString(R.string.msg_error_stats));
                tvLocal.setVisibility(View.GONE);
                tvLocalPuntos.setVisibility(View.GONE);
                tvVisitante.setVisibility(View.GONE);
                tvVisitantePuntos.setVisibility(View.GONE);

            }
        };

        GsonRequest<EstadisticasPartidoDataResponse> request = new GsonRequest<EstadisticasPartidoDataResponse>(
                Request.Method.GET, url, EstadisticasPartidoDataResponse.class,
                successListener, errorListener);

        return request;
    }

    private void parseResponse(EstadisticasPartidoDataContainer datos) {
        final String imageUrlLocal = datos.getEscudoLocal();
        if (imageUrlLocal.length() > 0) {
            escudo_local.setImageUrl(imageUrlLocal, SCCRadioMobileApp.IMAGE_CACHE_MANAGER.getImageLoader());
        } else {
            escudo_local.setImageResource(R.drawable.no_image);
        }

        final String imageUrlVisitante = datos.getEscudoVisitante();
        if (imageUrlVisitante.length() > 0) {
            escudo_visitante.setImageUrl(imageUrlVisitante, SCCRadioMobileApp.IMAGE_CACHE_MANAGER.getImageLoader());
        } else {
            escudo_visitante.setImageResource(R.drawable.no_image);
        }

        if (datos.isFinalizado())
            tvEstadoPartido.setText(getSherlockActivity().getString(R.string.msg_end_game));
        else
            tvEstadoPartido.setText(getSherlockActivity().getString(R.string.msg_play_game));

        final ArrayList<Estadistica> estadisticasLocal = datos.getEstadisticas_local();
        final ArrayList<Estadistica> estadisticasVisitante = datos.getEstadisticas_visitante();

        HashMap<String, String> totalesLocal = parseEstadisticas(estadisticasLocal);
        HashMap<String, String> totalesVisitantes = parseEstadisticas(estadisticasVisitante);
        LinkedList<EstadisticaTotal> totales = generateTotales(totalesLocal, totalesVisitantes);

        mapList.clear();

        mapList.addAll(totales);

        adapter.notifyDataSetChanged();


    }

    private LinkedList<EstadisticaTotal> generateTotales(HashMap<String, String> totalesLocal, HashMap<String, String> totalesVisitantes) {
        LinkedList<EstadisticaTotal> rval = new LinkedList<EstadisticaTotal>();

        for (String key : totalesLocal.keySet()) {
            EstadisticaTotal obj = new EstadisticaTotal();
            obj.setStat(key);
            obj.setLocal(totalesLocal.get(key));
            obj.setVisitante(totalesVisitantes.get(key));
            rval.add(obj);
        }

        return rval;
    }

    /**
     * A partir de los datos que recibimos del servidor, parseamos la informacion en busqueda de los totales
     * de cada equipo y creamos la estructura de datos para pasar al Adapter.
     *
     * @param estadisticas
     * @return Lista de Hashmaps
     */
    private HashMap<String, String> parseEstadisticas(ArrayList<Estadistica> estadisticas) {
        String totales = "totales";
        HashMap<String, String> temp = new HashMap<String, String>();
        Estadistica estadistica;
        for (int i = 0; i < estadisticas.size(); i++) {

            estadistica = estadisticas.get(i);
            if (estadistica.getNombre().contains(totales)) {
                temp.put(EstadisticasTotalesListviewAdapter.FIRST_COLUMN, estadistica.getLibresConvertidos() + "-" + estadistica.getLibresLanzados() + "(" + estadistica.getPorcentajeLibres() + "%)");
                temp.put(EstadisticasTotalesListviewAdapter.SECOND_COLUMN, estadistica.getDoblesConvertidos() + "-" + estadistica.getDoblesLanzados() + "(" + estadistica.getPorcentajeDobles() + "%)");
                temp.put(EstadisticasTotalesListviewAdapter.THIRD_COLUMN, estadistica.getTriplesConvertidos() + "-" + estadistica.getTriplesLanzados() + "(" + estadistica.getPorcentajeTriples() + "%)");
                temp.put(EstadisticasTotalesListviewAdapter.FOURTH_COLUMN, String.valueOf(estadistica.getRebotesTotales()));
                temp.put(EstadisticasTotalesListviewAdapter.FIFTH_COLUMN, String.valueOf(estadistica.getAsistencias()));
                temp.put(EstadisticasTotalesListviewAdapter.SIXTH_COLUMN, String.valueOf(estadistica.getPelotasPerdidas()));
                temp.put(EstadisticasTotalesListviewAdapter.SEVENTH_COLUMN, String.valueOf(estadistica.getPelotasRecuperadas()));
                temp.put(EstadisticasTotalesListviewAdapter.EIGTH_COLUMN, String.valueOf(estadistica.getTapones()));
                temp.put(EstadisticasTotalesListviewAdapter.NINTH_COLUMN, String.valueOf(estadistica.getFaltasPersonales()));

                break;
            }

        }

      /*
        for (String key : temp.keySet()) {
            Log.d(LOG_TAG, "Key = " + key + " - " + temp.get(key));
        }
        */
        return temp;
    }


    public class EstadisticaTotal {
        private String stat;
        private String local;
        private String visitante;

        @Override
        public String toString() {
            return "--->>EstadisticaTotal{" +
                    "stat='" + stat + '\'' +
                    ", local='" + local + '\'' +
                    ", visitante='" + visitante + '\'' +
                    '}';
        }

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }

        public String getVisitante() {
            return visitante;
        }

        public void setVisitante(String visitante) {
            this.visitante = visitante;
        }
    }
}