package ar.com.sccradiomobile.home.noticias;

/**
 * Created by Mariano Salvetti on 09/06/2014.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.databases.PostTable;
import ar.com.sccradiomobile.storage.adapter.PostAdapter;
import ar.com.sccradiomobile.storage.model.sccradio.Post;
import ar.com.sccradiomobile.storage.model.sccradio.PostDataResponse;
import ar.com.sccradiomobile.storage.provider.PostContentProvider;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.storage.util.request.GsonRequest;
import ar.com.sccradiomobile.utils.Utils;
import com.actionbarsherlock.app.SherlockFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class PostFilafiloFragment extends SherlockFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "sccradio_mobileFilafilo";
    /**
     * Key to insert the PARTIDO_ID into the mapping of a Bundle.
     */
    private static final String CATEGORIA_ID = "categoria";
    private static String categoria = Constants.CATEGORIA_FILAFILO;

    private static final int RESULT_PER_PAGE = 20;
    private ListView mListView;
    private PostAdapter mAdapter;

    private boolean isRunning = true;
    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private static final int LOADER_ALL_NOTICIAS_ID = 2;

    private boolean needToScroll;

    public static final String ACTION_UPDATE_DATA = "ar.com.sccradiomobile.action.UPDATE_DATA";
    public static final String URL_SERVER_POST_HOME = "http://www.sccradio.com.ar/api/core/get_category_posts/?slug=home-recent-posts";
    public static final String URL_SERVER_POST_MOBILE = "http://www.sccradio.com.ar/api/core/get_category_posts/?slug=mobile";
    public static final String URL_SERVER_POST_PIRU_FILAFILO = "http://www.sccradio.com.ar/api/core/get_category_posts/?slug=pirufilafilo";
    /**
     * Instances a new fragment with a background color and an index page.
     *
     * @param categoria id de la categoria a mostrar
     * @return a new page
     */
    public static PostFilafiloFragment newInstance(String categoria) {

        // Instantiate a new fragment
        PostFilafiloFragment fragment = new PostFilafiloFragment();

        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putString(Constants.CATEGORIA, categoria);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        categoria = (getArguments() != null) ? getArguments().getString(Constants.CATEGORIA) : Constants.CATEGORIA_FILAFILO;

        if (Constants.DEBUG) Log.d(LOG_TAG, "----> categoria: " + categoria);
    }

    @Override
    public void onResume() {
        super.onResume();

        isRunning = true;

    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_dynamic_list, container, false);
        mListView = (ListView) rootView.findViewById(R.id.lstFeed);

        // Set List empty view
        mListView.setEmptyView(rootView.findViewById(android.R.id.empty));

        // Scroll listener
     //   mListView.setOnScrollListener(new EndlessScrollListener());

      //  setupActionBar();

        if (Constants.DEBUG) Log.d(LOG_TAG, "comenzando con el recuperarNoticiasLocales desde el Fragment... ");

        recuperarNoticiasLocales();
        return rootView;
    }



    private void populateScreenByCategoriaId() {
        if (Constants.DEBUG) Log.d(LOG_TAG, "---->populateScreenByCategoriaId: " + categoria);

        if (Utils.isConnected(this.getSherlockActivity()) && isRunning) {//&& isRunning
            final ProgressDialog pDialog = new ProgressDialog(this.getSherlockActivity());
            pDialog.setMessage("Cargando...");
            pDialog.show();
            Request<PostDataResponse> request = createRequest(pDialog);

            SCCRadioMobileApp.REQUEST_MANAGER.addToRequestQueue(request, SCCRadioMobileApp.TAG);
        }
    }


    private Request<PostDataResponse> createRequest(final ProgressDialog pDialog) {
        int startIndex = 1 + mAdapter.getCount();

        if (Constants.DEBUG) Log.d(LOG_TAG, "----> url: " + URL_SERVER_POST_PIRU_FILAFILO);
        Response.Listener<PostDataResponse> successListener = new Response.Listener<PostDataResponse>() {

            @Override
            public void onResponse(PostDataResponse response) {
                if (Constants.DEBUG) Log.d(LOG_TAG, " ESTADO DEL RESPONSE ---->> " + response.getStatus());

                pDialog.dismiss();
                final ArrayList<Post> noticiaArrayList = response.getPosts();

                if (Constants.DEBUG) Log.d(LOG_TAG, "cantidad de posts: " + response.getCount());

                if (noticiaArrayList != null) {

                    mAdapter.addAllNoticias(noticiaArrayList);

                    if (needToScroll) {
                        int position = mAdapter.getCount() - RESULT_PER_PAGE;
                        mListView.setSelection(position);
                        needToScroll = false;
                    }
                    if (Constants.DEBUG) Log.d(LOG_TAG, "guardadas las noticias en DB");
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (Constants.DEBUG) Log.e(LOG_TAG, "Error: " + error.getMessage());
                pDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage(getString(R.string.progress_dialog_error_message));
                builder.setCancelable(false);
                builder.setNeutralButton(R.string.progress_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                    }
                });

                builder.setTitle(getString(R.string.progress_dialog_title));
                builder.create().show();
            }
        };

        GsonRequest<PostDataResponse> request = new GsonRequest<PostDataResponse>(
                Request.Method.GET, URL_SERVER_POST_PIRU_FILAFILO, PostDataResponse.class,
                successListener, errorListener);

        return request;
    }


    /**
     * Gets Feeds that are stored locally.
     */
    private void recuperarNoticiasLocales() {
        String[] from = new String[]{PostTable.COLUMN_TITLE, PostTable.COLUMN_CONTENT, PostTable.COLUMN_PHOTO, PostTable.COLUMN_DATE, PostTable.COLUMN_EXCERPT, PostTable.COLUMN_CATEGORIA};
        int[] to = new int[]{R.id.tvTitle};

        mAdapter = new PostAdapter(getSherlockActivity(), R.layout.feed_item, null, from, to, 0, PostAdapter.CATEGORIA_FILAFILO);

        // Associate the (now empty) adapter with the ListView.
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putLong(Constants.ID, id);

                Intent mainIntent = new Intent().setClass(getSherlockActivity(), DetalleNoticiaActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.putExtras(b);
                startActivity(mainIntent);
            }

        });

        getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_ALL_NOTICIAS_ID, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Create a new CursorLoader with the following query parameters.
        String[] projection = {PostTable.COLUMN_ID, PostTable.COLUMN_TITLE, PostTable.COLUMN_CONTENT, PostTable.COLUMN_PHOTO, PostTable.COLUMN_DATE, PostTable.COLUMN_EXCERPT, PostTable.COLUMN_CATEGORIA};

      //  final String selection = null;
      //  final String[] selectionArgs = null;

        String selection = PostTable.COLUMN_CATEGORIA + "=?";
        String[] selectionArgs = {categoria};


        final String sortOrder = PostTable.COLUMN_ID + " DESC";
     //   final String sortOrder = null;
        return new CursorLoader(getSherlockActivity(),
                PostContentProvider.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // A switch-case is useful when dealing with multiple Loaders/IDs
        switch (loader.getId()) {
            case LOADER_ALL_NOTICIAS_ID:
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorAdapter.
                mAdapter.swapCursor(cursor);

                if (mAdapter.getCount() == 0) {
                    populateScreenByCategoriaId();
                    // performRequest();
                }
                break;
        }
        // The listview now displays the queried data.
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        mAdapter.swapCursor(null);
    }

    /**
     * Detects when user is closer to the end of the current page and starts
     * loading the next page so the user will not have to wait (that much) for
     * the next entries.
     */
    public class EndlessScrollListener implements AbsListView.OnScrollListener {
        // How many entries earlier to start loading next page
        private int visibleThreshold = 5;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading
                    && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
           /*     if (!isRequestPending) {
                    loading = true;
                    populateScreenByCategoriaId();
                    // performRequest();
                    isRequestPending = true;
                }
                  */
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        public int getCurrentPage() {
            return currentPage;
        }
    }
}