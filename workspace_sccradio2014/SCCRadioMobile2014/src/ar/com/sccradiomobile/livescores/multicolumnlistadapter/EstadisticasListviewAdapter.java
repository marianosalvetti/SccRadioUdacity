package ar.com.sccradiomobile.livescores.multicolumnlistadapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ar.com.sccradiomobile.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mariano Salvetti on 05/05/2014.
 * <p/>
 * Ver ejemplo en: http://ernimantel.com.ar/carpetadudo/lnb/detalle_jugada_extras.json
 * http://ernimantel.com.ar/carpetadudo/lnb/estadisticas_juego_1.json
 */

public class EstadisticasListviewAdapter extends BaseAdapter {
    public static final String NRO_COLUMN = "nro_columna";
    public static final String JUGADOR_COLUMN = "jugador_columna";
    public static final String PUNTOS_COLUMN = "pts_columna";
    public static final String LIBRES_COLUMN = "libres_columna";
    public static final String DOBLES_COLUMN = "dobles_colmuna";
    public static final String TRIPLES_COLUMN = "triples_columna";
    public static final String CONVERSIONES_COLUMN = "conv_columna";
    public static final String REBOTES_TOTAL_COLUMN = "rt_columna";
    public static final String ASISTENCIAS_COLUMN = "as_columna";
    public static final String TAPAS_COLUMN = "ta_columna";
    public static final String PELOTAS_ROBADAS_COLUMN = "pr_columna";
    public static final String PELOTAS_PERDIDAS_COLUMN = "pp_columna";
    public static final String FALTAS_PERSONALES_COLUMN = "fp_columna";
    public static final String FALTAS_RECIBIDAS_COLUMN = "fr_columna";
    public static final String TIEMPO_DE_JUEGO_COLUMN = "tj_columna";
    public static final String VALORACION_COLUMN = "val_columna";
    public static final String TOTALES = "totales";
    public List<HashMap<String, String>> pila;
    private Activity activity;

    public EstadisticasListviewAdapter(Activity activity, List<HashMap<String, String>> list) {
        super();
        this.activity = activity;
        this.pila = list;
    }

    @Override
    public int getCount() {
        return pila.size();
    }

    @Override
    public Object getItem(int position) {
        return pila.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txtNro;
        TextView txtJugador;
        TextView txtPuntos;
        TextView txtLibres;
        TextView txtDobles;
        TextView txtTriples;
        TextView txtConversiones;
        TextView txtRebotes;
        TextView txtAsistencias;
        TextView txtTapas;
        TextView txtPelotasRobadas;
        TextView txtPelotasPerdidas;
        TextView txtFaltasPersonales;
        TextView txtFaltasRecibidas;
        TextView txtTiempoDeJuego;
        TextView txtValoracion;
        TextView line_empty;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_estadistica_row, null);
            holder = new ViewHolder();
            holder.txtNro = (TextView) convertView.findViewById(R.id.nro_columna);
            holder.txtJugador = (TextView) convertView.findViewById(R.id.jugador_columna);
            holder.txtPuntos = (TextView) convertView.findViewById(R.id.pts_columna);
            holder.txtLibres = (TextView) convertView.findViewById(R.id.libres_columna);
            holder.txtDobles = (TextView) convertView.findViewById(R.id.dobles_colmuna);
            holder.txtTriples = (TextView) convertView.findViewById(R.id.triples_columna);
            holder.txtConversiones = (TextView) convertView.findViewById(R.id.conv_columna);
            holder.txtRebotes = (TextView) convertView.findViewById(R.id.rt_columna);
            holder.txtAsistencias = (TextView) convertView.findViewById(R.id.as_columna);
            holder.txtTapas = (TextView) convertView.findViewById(R.id.ta_columna);
            holder.txtPelotasRobadas = (TextView) convertView.findViewById(R.id.pr_columna);
            holder.txtPelotasPerdidas = (TextView) convertView.findViewById(R.id.pp_columna);
            holder.txtFaltasPersonales = (TextView) convertView.findViewById(R.id.fp_columna);
            holder.txtFaltasRecibidas = (TextView) convertView.findViewById(R.id.fr_columna);
            holder.txtTiempoDeJuego = (TextView) convertView.findViewById(R.id.tj_columna);
            holder.txtValoracion = (TextView) convertView.findViewById(R.id.val_columna);
            holder.line_empty = (TextView) convertView.findViewById(R.id.line_empty);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = pila.get(position);
        holder.txtNro.setText(map.get(NRO_COLUMN));
        holder.txtJugador.setText(map.get(JUGADOR_COLUMN));
        holder.txtPuntos.setText(map.get(PUNTOS_COLUMN));
        holder.txtLibres.setText(map.get(LIBRES_COLUMN));
        holder.txtDobles.setText(map.get(DOBLES_COLUMN));
        holder.txtTriples.setText(map.get(TRIPLES_COLUMN));
        holder.txtConversiones.setText(map.get(CONVERSIONES_COLUMN));
        holder.txtRebotes.setText(map.get(REBOTES_TOTAL_COLUMN));
        holder.txtAsistencias.setText(map.get(ASISTENCIAS_COLUMN));
        holder.txtTapas.setText(map.get(TAPAS_COLUMN));
        holder.txtPelotasRobadas.setText(map.get(PELOTAS_ROBADAS_COLUMN));
        holder.txtPelotasPerdidas.setText(map.get(PELOTAS_PERDIDAS_COLUMN));
        holder.txtFaltasPersonales.setText(map.get(FALTAS_PERSONALES_COLUMN));
        holder.txtFaltasRecibidas.setText(map.get(FALTAS_RECIBIDAS_COLUMN));
        holder.txtTiempoDeJuego.setText(map.get(TIEMPO_DE_JUEGO_COLUMN));
        holder.txtValoracion.setText(map.get(VALORACION_COLUMN));


        final String jugador = map.get(JUGADOR_COLUMN);

        if (jugador.contains(TOTALES)) {
            holder.txtNro.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtNro.setText("");
            holder.txtJugador.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtJugador.setText(jugador.toUpperCase());
            holder.txtPuntos.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtLibres.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtDobles.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtTriples.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtRebotes.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtAsistencias.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtTapas.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtPelotasRobadas.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtPelotasPerdidas.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtFaltasPersonales.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtFaltasRecibidas.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtTiempoDeJuego.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtValoracion.setTypeface(Typeface.DEFAULT_BOLD);

            holder.line_empty.setVisibility(View.VISIBLE);

        } else {
            holder.txtNro.setTypeface(Typeface.DEFAULT);
            holder.txtJugador.setTypeface(Typeface.DEFAULT);
            holder.txtPuntos.setTypeface(Typeface.DEFAULT);
            holder.txtLibres.setTypeface(Typeface.DEFAULT);
            holder.txtDobles.setTypeface(Typeface.DEFAULT);
            holder.txtTriples.setTypeface(Typeface.DEFAULT);
            holder.txtRebotes.setTypeface(Typeface.DEFAULT);
            holder.txtAsistencias.setTypeface(Typeface.DEFAULT);
            holder.txtTapas.setTypeface(Typeface.DEFAULT);
            holder.txtPelotasRobadas.setTypeface(Typeface.DEFAULT);
            holder.txtPelotasPerdidas.setTypeface(Typeface.DEFAULT);
            holder.txtFaltasPersonales.setTypeface(Typeface.DEFAULT);
            holder.txtFaltasRecibidas.setTypeface(Typeface.DEFAULT);
            holder.txtTiempoDeJuego.setTypeface(Typeface.DEFAULT);
            holder.txtValoracion.setTypeface(Typeface.DEFAULT);
            holder.line_empty.setVisibility(View.GONE);

        }
        return convertView;
    }
}

