package ar.com.sccradiomobile.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.databases.PostTable;
import ar.com.sccradiomobile.databases.TinyDB;
import ar.com.sccradiomobile.home.noticias.PostNoticiasFragment;
import ar.com.sccradiomobile.storage.model.sccradio.*;
import ar.com.sccradiomobile.storage.provider.PostContentProvider;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.storage.util.request.GsonRequest;
import ar.com.sccradiomobile.utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Getting NEWS from : http://www.sccradio.com.ar/?json=get_recent_posts
 */
public class SplashActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 2000;
    private static final String LOG_TAG = "sccradio_mobile";
    public final String NEWS_SYNC = "isNewsSync";
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_screen);

        spinner = (ProgressBar) findViewById(R.id.progressBarhome);
        spinner.setVisibility(View.VISIBLE);

    //    final boolean timeoutSync = validarLapsoUpdate();  //TODO: work here, to check every day
         final boolean timeoutSync = true;

        if (Constants.DEBUG) Log.d(LOG_TAG, "----------->starting the Splash....timeoutSync: " + timeoutSync);
        if (timeoutSync) {
            populateDB();
        } else if (Utils.isConnected(this)) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Bundle b = new Bundle();
                    b.putString(Constants.CATEGORIA, Constants.CATEGORIA_TNA);
                    Intent mainIntent = new Intent().setClass(SplashActivity.this, HomePage.class);
                    mainIntent.putExtras(b);
                    startActivity(mainIntent);
                    finish();
                }
            };
            // Simulate a long loading process on application startup.
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
        } else {
            if (Constants.DEBUG) Log.e(LOG_TAG, "======[ SIN INTERNET ]======");
            TinyDB.putBoolean(NEWS_SYNC, false);

            setContentView(R.layout.splash_screen_no_connection);

            Button btnContinuar = (Button) findViewById(R.id.button_continuar);
            btnContinuar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    manageNoConnection();
                }
            });
        }
    }

    private Boolean validarLapsoUpdate() {
        boolean rval = false;
        Boolean statusSync = TinyDB.getBoolean(NEWS_SYNC);
        if (!statusSync)
            rval = true;

        long lastUpdateTime;
        /* Get Last Update Time from Preferences */
        lastUpdateTime = TinyDB.getLong("lastUpdateTime");

        /* Should Activity Check for Updates Now? */
        if ((lastUpdateTime + (24 * 60 * 60 * 1000)) < System.currentTimeMillis()) {

            /* Save current timestamp for next Check*/
            lastUpdateTime = System.currentTimeMillis();
            TinyDB.putLong("lastUpdateTime", lastUpdateTime);


            /* Do Update */
            if (Constants.DEBUG) Log.e(LOG_TAG, "======[ Do Update ]======");
            rval = true;
        }


        return rval;

    }


    private void populateDB() {
        if (Utils.isConnected(this)) {//&& isRunning
            Request<PostDataResponse> request = createRequest();
            SCCRadioMobileApp.REQUEST_MANAGER.addToRequestQueue(request, SCCRadioMobileApp.TAG);
        } else {
            if (Constants.DEBUG) Log.e(LOG_TAG, "======[ SIN INTERNET ]======");
            TinyDB.putBoolean(NEWS_SYNC, false);
            setContentView(R.layout.splash_screen_no_connection);

            Button btnContinuar = (Button) findViewById(R.id.button_continuar);
            btnContinuar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    manageNoConnection();
                }
            });
        }
    }

    private Request<PostDataResponse> createRequest() {
        String url = PostNoticiasFragment.URL_SERVER_POST_MOBILE;
        Response.Listener<PostDataResponse> successListener = new Response.Listener<PostDataResponse>() {

            @Override
            public void onResponse(PostDataResponse response) {
                TinyDB.putBoolean(NEWS_SYNC, true);

                final ArrayList<Post> postArrayList = response.getPosts();
                if (Constants.DEBUG) Log.d(LOG_TAG, "cantidad de noticias: " + response.getPosts().size());
                if (Constants.DEBUG) Log.d(LOG_TAG, "response get Count: " + response.getCount());
                final Category category = response.getCategory();
                for (final Post post : postArrayList) {
                    post.setCategory(category);
                    if (Constants.DEBUG) Log.d(LOG_TAG, "============");
                    if (Constants.DEBUG) Log.d(LOG_TAG, post.toString());
                    if (!existePost(post))
                        addNoticia(post);
                }
                if (Constants.DEBUG) Log.d(LOG_TAG, "YA ESTAN GUARDADOS LOS POST DB");


                // Start the next activity
                Bundle b = new Bundle();
                b.putString(Constants.CATEGORIA, Constants.CATEGORIA_TNA);
                Intent mainIntent = new Intent().setClass(SplashActivity.this, HomePage.class);
                mainIntent.putExtras(b);
                startActivity(mainIntent);
                finish();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (Constants.DEBUG) Log.e(LOG_TAG, "Error: " + error.getMessage());
                TinyDB.putBoolean(NEWS_SYNC, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage(getString(R.string.progress_dialog_error_message));
                builder.setCancelable(false);
                builder.setNeutralButton(R.string.progress_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        manageNoConnection();
                    }
                });

                builder.setTitle(getString(R.string.progress_dialog_title));
                builder.create().show();
            }
        };

        GsonRequest<PostDataResponse> request = new GsonRequest<PostDataResponse>(
                Request.Method.GET, url, PostDataResponse.class,
                successListener, errorListener);

        return request;
    }

    private void addNoticia(Post post) {
        ContentValues values = new ContentValues();

        final ArrayList<Attachments> attachments = post.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            final Attachments firstPhoto = attachments.get(0);
            if (firstPhoto != null)
                values.put(PostTable.COLUMN_PHOTO, firstPhoto.getUrl());
        }
        values.put(PostTable.COLUMN_ID, post.getId());
        values.put(PostTable.COLUMN_TYPE, post.getType());
        values.put(PostTable.COLUMN_URL, post.getUrl());
        values.put(PostTable.COLUMN_STATUS, post.getStatus());
        values.put(PostTable.COLUMN_TITLE, post.getTitle());
        values.put(PostTable.COLUMN_TITLE_PLAIN, post.getTitle_plain());
        values.put(PostTable.COLUMN_CONTENT, post.getContent());
        values.put(PostTable.COLUMN_EXCERPT, post.getExcerpt());
        values.put(PostTable.COLUMN_DATE, post.getDate());


        final Category category = post.getCategory();
        if (category != null)
            values.put(PostTable.COLUMN_CATEGORIA, category.getTitle());

        final Author author = post.getAuthor();
        if (author != null) {
            final String firstName = author.getFirst_name();
            final String lastName = author.getLast_name();
            if (firstName != null && lastName != null)
                values.put(PostTable.COLUMN_AUTOR, firstName + " " + lastName);
        }


        // When you want to access data in a content provider, you use the
        // ContentResolver object in your application's Context to communicate
        // with the provider as a client. The ContentResolver object
        // communicates with the provider object, an instance of a class that
        // implements ContentProvider. The provider object receives data
        // requests from clients, performs the requested action, and returns the
        // results.
        Uri inserted = this.getApplicationContext().getContentResolver().insert(PostContentProvider.CONTENT_URI, values);
    }

    /**
     * Metodo privado que valida dentro de la DB actual si el post que es parametro existe en nuestra DB.
     *
     * @param noticia
     * @return TRUE si existe el post, false si la debemos persistir.
     */
    private boolean existePost(Post post) {
        boolean rval = false;

        Uri uriPost = PostContentProvider.CONTENT_URI;
        ContentResolver cr = this.getApplicationContext().getContentResolver();
        final String sortOrder = PostTable.COLUMN_ID + " DESC";
        Cursor cur = cr.query(uriPost, PostTable.getProjection(), null, null, sortOrder);

        if (cur.moveToFirst()) {
            int id;
            final int colId = cur.getColumnIndex(PostTable.COLUMN_ID);

            do {
                id = cur.getInt(colId);
                if ((post.getId() == id)) {
                    if (Constants.DEBUG) Log.d(LOG_TAG, "coincide ----> id: " + id);
                    rval = true;
                    break;
                }
            } while (cur.moveToNext());
        }

        if (rval) {
            if (Constants.DEBUG) Log.d(LOG_TAG, "----> si existe la noticia: " + post);
        }
        cur.close();
        return rval;
    }

    protected void manageNoConnection() {
        TinyDB.putBoolean(NEWS_SYNC, false);
        Bundle b = new Bundle();
        b.putString(Constants.CATEGORIA, Constants.CATEGORIA_TNA);
        Intent mainIntent = new Intent().setClass(SplashActivity.this, HomePage.class);
        mainIntent.putExtras(b);
        startActivity(mainIntent);
        finish();
    }
}