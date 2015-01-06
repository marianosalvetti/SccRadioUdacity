package ar.com.sccradiomobile.livescores;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.databases.TinyDB;
import ar.com.sccradiomobile.storage.model.livescores.Partido;
import ar.com.sccradiomobile.storage.model.livescores.PartidoDataResponse;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.storage.util.request.GsonRequest;
import com.actionbarsherlock.app.SherlockFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

/**
 * Created by Mariano Salvetti
 * Tenemos que continuar trabajando acà....
 */
public class MatchCenterSlide extends SherlockFragment {
    private Context context;
    ListView listView;
    TextView tvDisplayTitle;
    LinearLayout headerContainer;
    protected static final String LOG_TAG = "match_center";
    protected String categoria;
    private LiveScoresAdapter<Partido> adapter;

    public MatchCenterSlide() {
    }

    @SuppressLint("ValidFragment")
    public MatchCenterSlide(Activity context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        categoria = TinyDB.getString(Constants.CATEGORIA);

        View rootView = inflater.inflate(R.layout.home_matchcenter, container, false);
        listView = (ListView) rootView.findViewById(R.id.listPartidos);
        tvDisplayTitle = (TextView) rootView.findViewById(R.id.tituloCategoria);
        headerContainer = (LinearLayout) rootView.findViewById(R.id.header_layout_container);
        doRequestByDate("dom,23/03/2014", 1);
        categoria = TinyDB.getString(Constants.CATEGORIA);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void doRequestByDate(final String date, int jornada) {
        String url = "http://sccradio.com.ar/carpetadudo/lnb/fixture_sport.json";

        Request<PartidoDataResponse> request = createRequest(date, jornada, url);

        SCCRadioMobileApp.REQUEST_MANAGER.addToRequestQueue(request, SCCRadioMobileApp.TAG);
    }

    private Request<PartidoDataResponse> createRequest(final String date, final int jornada, String url) {

        Response.Listener<PartidoDataResponse> successListener = new Response.Listener<PartidoDataResponse>() {

            @Override
            public void onResponse(PartidoDataResponse response) {
                if (Constants.DEBUG) Log.d(LOG_TAG, response.toString());
                if (Constants.DEBUG) Log.d(LOG_TAG, " ESTADO DEL RESPONSE ---->> " + response.getEstado());


                listView.setVisibility(View.VISIBLE);
                final ArrayList<Partido> partidos = response.getDatos().getPartidos();

                assert (partidos != null);
                assert (getSherlockActivity() != null);
                assert (context != null);
                if (context == null) {
                    context = getSherlockActivity();
                }

                adapter = new LiveScoresAdapter<Partido>(context, R.layout.live_scores_item, partidos);

                listView.setAdapter(adapter);
                // ListView Item Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final int itemPosition = position;

                        Partido itemValue = (Partido) listView.getItemAtPosition(position);

                        Bundle b = new Bundle();
                        b.putLong(Constants.ID, Long.parseLong(itemValue.getPartido()));
                        b.putString("local", itemValue.getLocal());
                        b.putString("visitante", itemValue.getVisitante());

                        Intent mainIntent = new Intent().setClass(context, DetailPartidoActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mainIntent.putExtras(b);

                        startActivity(mainIntent);
                    }

                });
                listView.setVisibility((adapter.isEmpty()) ? View.GONE : View.VISIBLE);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (Constants.DEBUG) Log.d(LOG_TAG, "Error: " + error.getMessage());

                listView.setVisibility(View.GONE);
            }
        };

        GsonRequest<PartidoDataResponse> request = new GsonRequest<PartidoDataResponse>(
                Request.Method.GET, url, PartidoDataResponse.class,
                successListener, errorListener);

        return request;
    }
}
