package ar.com.sccradiomobile.videos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import ar.com.sccradiomobile.BaseActivity;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.livescores.LiveScoresActivity;
import ar.com.sccradiomobile.storage.adapter.YouTubeVideoAdapter;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.utils.DateUtils;
import com.actionbarsherlock.app.ActionBar;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Primer pantalla para los videos de Youtube.
 */
public class SCCRadioVideosActivity extends BaseActivity implements SelectionFragment.OnSelectListener {
    private static final int RESULT_PER_PAGE = 25;
    private ArrayList<Video> linksVideos = new ArrayList<Video>();
    static SelectionFragment selectionFragment;
    static YouTubeVideoAdapter<Video> adapter;
    protected static final String LOG_TAG = "lnb_tv";

    public SCCRadioVideosActivity(int titleRes) {
        super(R.string.title_section1);
    }

    public SCCRadioVideosActivity() {
        super(R.string.title_section1);
    }

    private static String url = "http://gdata.youtube.com/feeds/api/users/erniman12/uploads?v=2&alt=jsonc";


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        setSlidingActionBarEnabled(true);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setIcon(R.drawable.ic_drawer);

        View mView = getLayoutInflater().inflate(R.layout.actionbar_view, null);
        actionBar.setCustomView(mView);

        if (Constants.DEBUG) Log.d(LOG_TAG, "Starting the Youtube videos Page... ");
        if (Constants.DEBUG) Log.d(LOG_TAG, "----> categoria ya seteada: " + categoria);
        setContentView(R.layout.main_video);
        // cambiamos el titulo
        cambiamosElTitulo(R.string.title_section5);
        setSlidingActionBarEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(android.R.color.transparent);


        selectionFragment = (SelectionFragment) this.getSupportFragmentManager().findFragmentById(R.id.selection_fragment);
        adapter = new YouTubeVideoAdapter<Video>(SCCRadioVideosActivity.this, R.layout.youtube_video_list_item, getLinks());
        selectionFragment.setListAdapter(adapter);
        final int startIndex = 1 + adapter.getCount();
        selectionFragment.getListView().setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                new JSONParse().execute(page, totalItemsCount);
            }
        });
        selectionFragment.getListView().setDivider(null);
        selectionFragment.getListView().setDividerHeight(0);

        JSONParse myAsyncTask = new JSONParse();
        myAsyncTask.execute(1, startIndex);

    }

    @Override
    public void onBackPressed() {
        if (getSlidingMenu().isMenuShowing()) {
            getSlidingMenu().toggle();
        } else {
            super.onBackPressed();
        }
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.liveScores:
                if (this.getSlidingMenu().isMenuShowing()) {
                    this.getSlidingMenu().toggle();
                }

                Intent mainIntent = new Intent().setClass(this, LiveScoresActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onItemSelected(final int position) {
        final Video article = getLinks().get(position);

        VideoFragment videoFragment = (VideoFragment) this.getSupportFragmentManager().findFragmentById(R.id.video_fragment);
        if (videoFragment == null) {
            Intent intent = new Intent(this, VideoFragmentDetalle.class);
            intent.putExtra(VideoFragmentDetalle.EXTRA_URL, article.getUrl());
            intent.putExtra(VideoFragmentDetalle.EXTRA_TITULO, article.getTitle());
            intent.putExtra(VideoFragmentDetalle.EXTRA_ID_VIDEO, article.getId());
            startActivity(intent);
        } else {
            videoFragment.updateContent(article.getUrl(), article.getId(), article.getTitle());
        }

    }


    public ArrayList<Video> getLinks() {
        return linksVideos;
    }

    private class JSONParse extends AsyncTask<Integer, Void, Boolean> {
        private ProgressDialog pDialog;

        public JSONParse() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SCCRadioVideosActivity.this);
            pDialog.setMessage("Descargando Informacion ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... parameters) {
            int npagina = parameters[0];
            int startIndex = parameters[1];
            Log.d(LOG_TAG, "npagina: " + npagina);
            Log.d(LOG_TAG, "startIndex: " + startIndex);

            final String urlToUse = url + "&start-index=" + startIndex + "&max-results=" + RESULT_PER_PAGE;

            if (Constants.DEBUG) Log.d(LOG_TAG, "GET URL: " + urlToUse.toString());

            try {

                HttpClient client = new DefaultHttpClient();

                HttpUriRequest request = new HttpGet(urlToUse);

                HttpResponse response = client.execute(request);
                HttpEntity httpEntity = response.getEntity();
                InputStream is = httpEntity.getContent();
                String jsonString = null;
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8000);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    jsonString = sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }


                JSONObject json = new JSONObject(jsonString);
                JSONArray jsonArray = json.getJSONObject("data").getJSONArray("items");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    final String title = jsonObject.getString("title");
                    final String id = jsonObject.getString("id");
                    final String uploaded = jsonObject.getString("uploaded");
                    final String thumbUrl = jsonObject.getJSONObject("thumbnail").getString("sqDefault");

                    // Log.d("lnb_mobile", "TITLE VIDEO ==> " + title);
                    //Log.d("lnb_mobile", "uploaded VIDEO ==> " + uploaded);

                    final String dateUploaded = DateUtils.getDateFormatedYoutube(uploaded);

                    //  Log.d("lnb_mobile","Publicado el " + dateUploaded);

                    float duration = jsonObject.getInt("duration");
                    String description = jsonObject.getString("description");
                    if (id != null && duration >= 10) {
                        linksVideos.add(new Video("" + i, title, id, thumbUrl, duration, dateUploaded, description));
                    }

                }

            } catch (ClientProtocolException e) {
                //Log.e("Feck", e);
            } catch (IOException e) {
                //Log.e("Feck", e);
            } catch (JSONException e) {
                //Log.e("Feck", e);
            }

            // VIDEO_LIST = Collections.unmodifiableList(list);


            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pDialog.dismiss();

            SCCRadioVideosActivity.selectionFragment.setListAdapter(new YouTubeVideoAdapter<Video>(SCCRadioVideosActivity.this, R.layout.youtube_video_list_item, getLinks()));
            int position = adapter.getCount() - RESULT_PER_PAGE;
            selectionFragment.getListView().setSelection(position);

            TextView emptyText = (TextView) findViewById(android.R.id.empty);
            SCCRadioVideosActivity.selectionFragment.getListView().setEmptyView(emptyText);
        }
    }//end async


    /**
     * Detects when user is closer to the end of the current page and starts
     * loading the next page so the user will not have to wait (that much) for
     * the next entries.
     */
    public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
        //El minimo de elementos a tener debajo de la posicion actual del scroll antes de cargar mas
        private int visibleThreshold = 1;
        // La pagina actual
        private int currentPage = 0;
        // El total de elementos despues de la ultima carga de datos
        private int previousTotalItemCount = 0;
        //True si sigue esperando que termine de cargar el ultimo grupo de datos solicitados
        private boolean loading = true;
        // Sirve para setear la pagina inicial
        private int startingPageIndex = 0;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        public EndlessScrollListener(int visibleThreshold, int startPage) {
            this.visibleThreshold = visibleThreshold;
            this.startingPageIndex = startPage;
            this.currentPage = startPage;
        }

        /**
         * Ojo! Este metodo ocurrira muchas veces por segundo durante un desplazamiento o scroll de la
         * lista, asi que es de tener mucho cuidado lo que se escribe aqui.
         * El listener nos provee de varios parametros utiles por si necesitamos cargar mas datos
         * pero, primero debemos comprobar que una carga no este en proceso sino tendremos un pequeño
         * infierno en nuestras manos.
         */
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            if (!loading && (totalItemCount < previousTotalItemCount)) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.loading = true;
                }
            }

            if (loading) {
                if (totalItemCount > previousTotalItemCount) {
                    loading = false;
                    previousTotalItemCount = totalItemCount;
                    currentPage++;
                }
            }

            //Si hemos llegado al final de la lista y una carga de datos no esta en proceso
            //invoco el metodo onLoadMore para poder cargar mas datos.
            if (!loading
                    && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                onLoadMore(currentPage + 1, totalItemCount);
                loading = true;
            }
        }

        // Este metodo es abstracto para que el desarrollador defina la forma en que quiere
        // llamar mas informacion, como parametros recibira la pagina actual y la cantidad
        // elementos cargados en la lista hasta el momento.
        public abstract void onLoadMore(int page, int totalItemsCount);

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

    }

}
