package ar.com.sccradiomobile.storage.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.databases.PostTable;
import ar.com.sccradiomobile.storage.model.sccradio.Attachments;
import ar.com.sccradiomobile.storage.model.sccradio.Author;
import ar.com.sccradiomobile.storage.model.sccradio.Category;
import ar.com.sccradiomobile.storage.model.sccradio.Post;
import ar.com.sccradiomobile.storage.provider.PostContentProvider;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.storage.util.image.FeedImageView;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;


/**
 * Custom {@link android.support.v4.widget.SimpleCursorAdapter} to show feed.
 * <p/>
 * Use Image Cache to save and load images from disk.
 *
 * @author Mariano Salvetti
 */
public class PostAdapter extends SimpleCursorAdapter {

    public static final String NOTICIAS_TNA = "mobile";
    public static final String CATEGORIA_FILAFILO = "pirufilafilo";
    private Context mContext;
    private int mLayout;
    private String categoria;

    private static final String LOG_TAG = "sccradio_mobile";

    public PostAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, String categoria) {
        super(context, layout, c, from, to, flags);
        mContext = context;
        mLayout = layout;
        this.categoria = categoria;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(mLayout, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views
        String title = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_TITLE));
        String resumen = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_EXCERPT));
        String cat = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_CATEGORIA));

        if (Constants.DEBUG) Log.d(LOG_TAG, "======[ " + resumen + " ]======");
        if (Constants.DEBUG) Log.d(LOG_TAG, "======[cat: " + cat + " ]======");

        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(Html.fromHtml(title));

        TextView tvResumen = (TextView) view.findViewById(R.id.txtResumen);
        tvResumen.setText(Html.fromHtml(resumen));

        TextView tvFecha = (TextView) view.findViewById(R.id.fechaNoticia);

        final String textTimeStamp = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_DATE));
        tvFecha.setText(textTimeStamp);

        NetworkImageView profilePic = (NetworkImageView) view.findViewById(R.id.profilePic);
        profilePic.setImageUrl("http://m.c.lnkd.licdn.com/mpr/pub/image-UT47FzxCG7oxJsJy336htwj-wj7p95-eUbeCtKFTwLh_9o4LUT4Cb1UCw1449-xuPEeE/diego-salvetti.jpg", SCCRadioMobileApp.IMAGE_CACHE_MANAGER.getImageLoader());
        //   profilePic.setVisibility(View.GONE);

        final FeedImageView ivThumbnail = (FeedImageView) view.findViewById(R.id.ivThumbnail);
        ivThumbnail.setVisibility(View.VISIBLE);
        String imageUrl = cursor.getString(cursor.getColumnIndex(PostTable.COLUMN_PHOTO));
    //    if (Constants.DEBUG) Log.d(LOG_TAG, "======[ " + imageUrl + " ]======");


        if (imageUrl != null && imageUrl.length() > 0) {
            ivThumbnail.setImageUrl(imageUrl, SCCRadioMobileApp.IMAGE_CACHE_MANAGER.getImageLoader());
            ivThumbnail.setVisibility(View.VISIBLE);
            ivThumbnail.setResponseObserver(new FeedImageView.ResponseObserver() {
                @Override
                public void onError() {
                    if (Constants.DEBUG) Log.d(LOG_TAG, "======[ onError ]======");
                }

                @Override
                public void onSuccess() {
                    if (Constants.DEBUG) Log.d(LOG_TAG, "======[ onSuccess ]======");
                }
            });
        } else {
            ivThumbnail.setVisibility(View.GONE);
        }


    }

    public void addNoticia(Post post) {
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
        if (category != null) {
            values.put(PostTable.COLUMN_CATEGORIA, category.getTitle());
            if (Constants.DEBUG) Log.d(LOG_TAG, "======[ Categoria " +  category.getTitle() + " ]======");
        }

        final Author author = post.getAuthor();
        if (author != null) {
            final String firstName = author.getFirst_name();
            final String lastName = author.getLast_name();
            if (firstName != null && lastName != null)
                values.put(PostTable.COLUMN_AUTOR, firstName + " " + lastName);
        }


        Uri inserted = mContext.getContentResolver().insert(PostContentProvider.CONTENT_URI, values);
    }


    public void addAllNoticias(ArrayList<Post> noticiaArrayList) {

        if (noticiaArrayList != null) {
            for (final Post post : noticiaArrayList) {
                if (!existePost(post))
                    addNoticia(post);
            }
        }
    }

    /**
     * Metodo privado que valida dentro de la DB actual si el post que es parametro existe en nuestra DB.
     *
     * @param noticia
     * @return TRUE si existe el post, false si la debemos persistir.
     */
    public boolean existePost(Post post) {
        boolean rval = false;

        Uri uriPost = PostContentProvider.CONTENT_URI;
        ContentResolver cr = mContext.getContentResolver();
        final String sortOrder = PostTable.COLUMN_ID + " ASC";
        String selection = null;
        String[] selectionArgs = null;
        Cursor cur = cr.query(uriPost, PostTable.getProjection(),selection,selectionArgs, sortOrder);

        if (cur.moveToFirst()) {
            int id;
            final int colId = cur.getColumnIndex(PostTable.COLUMN_ID);
            do {
                id = cur.getInt(colId);
                if (Constants.DEBUG) Log.d(LOG_TAG, "encontramos ----> id: " + id);
                if ((post.getId() == id)) {
                    if (Constants.DEBUG) Log.d(LOG_TAG, "coincide ----> id: " + id);
                    rval = true;  //si existe la noticia en cuestion.
                    break;
                }
            } while (cur.moveToNext());
        }

        if (rval) {
            if (Constants.DEBUG) Log.d(LOG_TAG, "----> si existe la noticia: " + post.getId());
        }
        cur.close();
        return rval;
    }
}
