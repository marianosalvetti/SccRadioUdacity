package ar.com.sccradiomobile.storage.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.videos.Video;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;


/**
 * Custom {@link android.support.v4.widget.SimpleCursorAdapter} to show feed.
 * <p>
 * Use Image Cache to save and load images from disk.
 *
 * @author Mariano Salvetti
 */
public class YouTubeVideoAdapter<T> extends ArrayAdapter<Video> {
    public static final int MAX_CHARS = 35;
    Context context;
    int layoutResourceId;
    private Activity activity;
    ArrayList<Video> data = null;

    public YouTubeVideoAdapter(Activity activity, int layoutResourceId, ArrayList<Video> data) {
        super(activity, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.data = data;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        VideoHolder holder = null;

        LayoutInflater inflater =  LayoutInflater.from(context);

        if (row == null) {
            row = inflater.inflate(R.layout.youtube_video_list_item, parent, false);

            holder = new VideoHolder();
            holder.title = (TextView) row.findViewById(R.id.titleVideo);
            holder.thumbImg = (NetworkImageView) row.findViewById(R.id.thumbImg);
            holder.duracion = (TextView) row.findViewById(R.id.duration);
            holder.fecha = (TextView) row.findViewById(R.id.fecha);
            holder.description = (TextView) row.findViewById(R.id.descripcionVideo);

            row.setTag(holder);
        } else {
            holder = (VideoHolder) row.getTag();
        }

        final Video  partido = data.get(position);
        holder.title.setText(partido.getTitle());
        holder.fecha.setText(partido.getDateUploaded());
        final String partidoDescription = partido.getDescription();
     /*   if (partidoDescription.length() >= MAX_CHARS) {
            holder.description.setText(partidoDescription.substring(0,MAX_CHARS));
        } else{
            holder.description.setText(partidoDescription);
        } */
        holder.description.setText(partidoDescription);
        holder.fecha.setVisibility(View.GONE);
        holder.duracion.setText(String.valueOf(partido.getDuracionDetalle()));

       final String imageUrl = partido.getThumbUrl();
       if (imageUrl.length() > 0) {
            holder.thumbImg.setImageUrl(imageUrl, SCCRadioMobileApp.IMAGE_CACHE_MANAGER.getImageLoader());

       } else {
            holder.thumbImg.setImageResource(R.drawable.no_image);
       }
        return row;
    }

    static class VideoHolder {
        TextView title;
        TextView duracion;
        TextView fecha;
        TextView description;
        NetworkImageView thumbImg;

    }
}
