package ar.com.sccradiomobile.home.noticias;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import ar.com.sccradiomobile.BaseActivity;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.databases.PostTable;
import ar.com.sccradiomobile.livescores.LiveScoresActivity;
import ar.com.sccradiomobile.storage.provider.PostContentProvider;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.utils.DetailImageFullScreenActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.toolbox.NetworkImageView;

public class DetalleNoticiaActivity  extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "sccradio_mobile";

    public DetalleNoticiaActivity(int titleRes) {
		super(R.string.title_section2);
		
	}
	
	public DetalleNoticiaActivity() {
		super(R.string.title_section2);
	}
	
    private TextView tituloTextView;
    // The loader's unique id. Loader ids are specific to the Activity or Fragment in which they reside.
    private static final int LOADER_NOTICIA_BY_ID = 2;
    private Long noticiaId = (long) 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
		setSlidingActionBarEnabled(true);
		setContentView(R.layout.noticia_detalle_activity);



        tituloTextView = (TextView) findViewById(R.id.titulo_noticia);
                      ActionBar actionBar = getSupportActionBar();
                      actionBar.setDisplayShowCustomEnabled(true);
                      actionBar.setIcon(R.drawable.ic_drawer);

                           View mView = getLayoutInflater().inflate(R.layout.actionbar_view, null);
                           actionBar.setCustomView(mView, new ActionBar.LayoutParams(
                                   ActionBar.LayoutParams.WRAP_CONTENT,
                                   ActionBar.LayoutParams.WRAP_CONTENT,
                                   Gravity.CENTER
                           ));
         // cambiamos el titulo
        cambiamosElTitulo(R.string.noticia_titulo_ejemplo);

	   Intent in = getIntent();
       noticiaId = in.getLongExtra(Constants.ID,0);
        if (noticiaId.intValue() > 0) {
           	// Initialize the Loader with LOADER_NOTICIA_BY_ID and callbacks this.
            // If the loader doesn't already exist, one is created. Otherwise,
            // the already created Loader is reused. In either case, the
            // LoaderManager will manage the Loader across the Activity/Fragment
            // lifecycle, will receive any new loads once they have completed,
            // and will report this new data back to the 'mCallbacks' object.
		    getSupportLoaderManager().initLoader(LOADER_NOTICIA_BY_ID, null, this);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(android.R.color.transparent);
	}


	@Override
    public void onBackPressed() {
        if ( getSlidingMenu().isMenuShowing()) {
        	getSlidingMenu().toggle();
        }
        else {
        	super.onBackPressed();
        }
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }


	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	super.onBackPressed();
            return true;
        case R.id.liveScores:
        	if (  this.getSlidingMenu().isMenuShowing()) {
        		 this.getSlidingMenu().toggle();
	        }

            Intent mainIntent = new Intent().setClass(this, LiveScoresActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            return true;

            default:
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
       	// Create a new CursorLoader with the following query parameters.
      	String[] projection = {PostTable.COLUMN_ID,PostTable.COLUMN_TITLE, PostTable.COLUMN_CONTENT, PostTable.COLUMN_EXCERPT,
                PostTable.COLUMN_PHOTO, PostTable.COLUMN_DATE };
		return new CursorLoader(this, PostContentProvider.CONTENT_URI,projection,  "_id=" + noticiaId, null, null);
     }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    		// A switch-case is useful when dealing with multiple Loaders/IDs
    		switch (loader.getId()) {
                case LOADER_NOTICIA_BY_ID:
                    // The asynchronous load is complete and the data
                    // is now available for use. Only now can we associate
                    // the queried Cursor with the SimpleCursorAdapter.
                    if (cursor != null && cursor.getCount() > 0) {
                          cursor.moveToFirst();
                          int idIndex = cursor.getColumnIndex(PostTable.COLUMN_ID);
                          int resumenIndex = -1;
                           if (!cursor.isNull( cursor.getColumnIndex( PostTable.COLUMN_EXCERPT)))
                               resumenIndex = cursor.getColumnIndex(PostTable.COLUMN_EXCERPT);

                        int contentIndex = -1;
                          if (!cursor.isNull( cursor.getColumnIndex( PostTable.COLUMN_CONTENT)))
                              contentIndex = cursor.getColumnIndex(PostTable.COLUMN_CONTENT);

                            int titleIndex = -1;
                            if (!cursor.isNull( cursor.getColumnIndex( PostTable.COLUMN_TITLE)))
                                titleIndex = cursor.getColumnIndex(PostTable.COLUMN_TITLE);

                            int urlImageIndex = 0;
                             if (!cursor.isNull( cursor.getColumnIndex( PostTable.COLUMN_PHOTO)))
                                urlImageIndex = cursor.getColumnIndex( PostTable.COLUMN_PHOTO);
                          int fechaIndex = cursor.getColumnIndex(PostTable.COLUMN_DATE);


                          noticiaId = cursor.getLong(idIndex);

                            String imageUrl = null;
                            if (urlImageIndex != 0)
                                imageUrl = cursor.getString(urlImageIndex);

                        String title = null;
                        if (titleIndex != -1)
                            title = cursor.getString(titleIndex);

                        String fecha = cursor.getString(fechaIndex);
                         String resumen = cursor.getString(resumenIndex);
                         String content = cursor.getString(contentIndex);
                          ((TextView)findViewById(R.id.titulo_noticia)).setText(Html.fromHtml(title));
                          ((TextView)findViewById(R.id.titulo_categoria)).setVisibility(View.GONE);
                          ((TextView)findViewById(R.id.subtitulo_noticia)).setText(Html.fromHtml(resumen));
                          ((TextView)findViewById(R.id.tv_fecha_noticia)).setText(fecha);
                          ((TextView)findViewById(R.id.desarrollo)).setText(Html.fromHtml(content));

                        //load image from url...
                            final NetworkImageView ivThumbnail = (NetworkImageView)findViewById(R.id.imageViewNoticia);
                            // Use of NetworkImageView
                            if (imageUrl!=null && imageUrl.length() > 0) {
                                // IMPORTANT: Use Disk Image Cache
                                ivThumbnail.setImageUrl(imageUrl, SCCRadioMobileApp.IMAGE_CACHE_MANAGER.getImageLoader());
                            } else {
                                // display placeholder or whatever you want
                                ivThumbnail.setImageResource(R.drawable.no_image);
                            }

                            final String finalImageUrl = imageUrl;
                            ivThumbnail.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {
                                              Intent intent = new Intent(DetalleNoticiaActivity.this, DetailImageFullScreenActivity.class);
                                              intent.putExtra("ORIGEN", "NOTICIAS");
                                              intent.putExtra("PATH", finalImageUrl);
                                              startActivity(intent);
                                          }
                                      }
                            );
                          // cambiamos el titulo
                        cambiamosElTitulo(title);
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
    		//mAdapter.swapCursor(null);
    	}
}
