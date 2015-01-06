package ar.com.sccradiomobile.livescores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.storage.model.livescores.Partido;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Mariano Salvetti
 * Adatper para mostrar la informacion de cada partido en vivo, que traemos desde el Servidor.
 */
public class LiveScoresAdapter<T> extends ArrayAdapter<Partido> {
    Context context;
    int layoutResourceId;

    ArrayList<Partido> data = null;

    public LiveScoresAdapter(Context context, int layoutResourceId, ArrayList<Partido> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PartidoHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PartidoHolder();
            holder.escudo_local = (NetworkImageView) row.findViewById(R.id.escudo_local);
            holder.escudo_visitante = (NetworkImageView) row.findViewById(R.id.escudo_visitante);
            holder.txtLocal = (TextView) row.findViewById(R.id.txtLocal);
            holder.txtVisitante = (TextView) row.findViewById(R.id.txtVisitante);
            holder.estado_partido = (TextView) row.findViewById(R.id.estado_partido);
            holder.txtPuntosLocal = (TextView) row.findViewById(R.id.txtPuntosLocal);
            holder.txtPuntosVisitante = (TextView) row.findViewById(R.id.txtPuntosVisitante);
            holder.gana_local =  (ImageView) row.findViewById(R.id.gana_local);
            holder.gana_visitante =  (ImageView) row.findViewById(R.id.gana_visitante);
            row.setTag(holder);
        } else {
            holder = (PartidoHolder) row.getTag();
        }

        final Partido partido = data.get(position);
        holder.txtLocal.setText(partido.getLocal());
        final String puntosLocal = partido.getQ4().getLocal();
        holder.txtPuntosLocal.setText(puntosLocal);
        holder.txtVisitante.setText(partido.getVisitante());
        final String puntosVisitante = partido.getQ4().getVisitante();
        holder.txtPuntosVisitante.setText(puntosVisitante);

        if (partido.isFinalizado())
            holder.estado_partido.setText(context.getString(R.string.msg_end_game));
        else
            holder.estado_partido.setText(context.getString(R.string.msg_play_game));

        if (Integer.parseInt(puntosLocal)> Integer.parseInt(puntosVisitante)) {
            holder.gana_local.setVisibility(View.VISIBLE);
            holder.gana_visitante.setVisibility(View.GONE);
        }   else  if (Integer.parseInt(puntosLocal) < Integer.parseInt(puntosVisitante)){
            holder.gana_local.setVisibility(View.GONE);
            holder.gana_visitante.setVisibility(View.VISIBLE);
        }   else   {
            holder.gana_local.setVisibility(View.GONE);
            holder.gana_visitante.setVisibility(View.GONE);
        }

        final String imageUrlLocal = partido.getEscudo_local();
        if (imageUrlLocal.length() > 0) {
            holder.escudo_local.setImageUrl(imageUrlLocal, SCCRadioMobileApp.IMAGE_CACHE_MANAGER.getImageLoader());
        } else {
            holder.escudo_local.setImageResource(R.drawable.no_image);
        }

        final String imageUrlVisitante = partido.getEscudo_visitante();
        if (imageUrlVisitante.length() > 0) {
            holder.escudo_visitante.setImageUrl(imageUrlVisitante, SCCRadioMobileApp.IMAGE_CACHE_MANAGER.getImageLoader());
        } else {
            holder.escudo_visitante.setImageResource(R.drawable.no_image);
        }
        return row;
    }

    static class PartidoHolder {
        TextView estado_partido;
        NetworkImageView escudo_local;
        TextView txtLocal;
        NetworkImageView escudo_visitante;
        TextView txtVisitante;

        TextView txtPuntosLocal;
        TextView txtPuntosVisitante;

        ImageView gana_local;
        ImageView gana_visitante;
    }
}
