package ar.com.sccradiomobile.livescores.multicolumnlistadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.livescores.DetallePartidoFragment;

import java.util.LinkedList;

/**
 * Created by Mariano Salvetti on 05/05/2014.
 *
  Ver ejemplo en: http://ernimantel.com.ar/carpetadudo/lnb/detalle_jugada_extras.json
 http://ernimantel.com.ar/carpetadudo/lnb/estadisticas_juego_1.json

 */

public class EstadisticasTotalesListviewAdapter extends BaseAdapter{
    public static final String FIRST_COLUMN = "Tiros Libres";
    public static final String SECOND_COLUMN= "Dobles";
    public static final String THIRD_COLUMN = "Triples";
    public static final String FOURTH_COLUMN = "Rebotes";
    public static final String FIFTH_COLUMN = "Asistencias";
    public static final String SIXTH_COLUMN = "Perdidas";
    public static final String SEVENTH_COLUMN = "Pelotas Recuperadas";
    public static final String EIGTH_COLUMN = "Tapones";
    public static final String NINTH_COLUMN = "Faltas Personales";

    public   LinkedList<DetallePartidoFragment.EstadisticaTotal> mapa;
    private Activity activity;

       public EstadisticasTotalesListviewAdapter(Activity activity,   LinkedList<DetallePartidoFragment.EstadisticaTotal> mapa) {
           super();
           this.activity = activity;
           this.mapa  = mapa;
       }

       @Override
       public int getCount() {
           return mapa.size();
       }

       @Override
       public Object getItem(int position) {
           return mapa.get(position);
       }

       @Override
       public long getItemId(int position) {
           return 0;
       }

       private class ViewHolder {
              TextView txtFirst;
              TextView txtSecond;
              TextView txtThird;
         }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder holder;
           LayoutInflater inflater = activity.getLayoutInflater();

           if (convertView == null) {
               convertView = inflater.inflate(R.layout.listview_estadisticas_totales_row, null);
               holder = new ViewHolder();
               holder.txtFirst = (TextView) convertView.findViewById(R.id.FirstText);
               holder.txtSecond = (TextView) convertView.findViewById(R.id.SecondText);
               holder.txtThird = (TextView) convertView.findViewById(R.id.ThirdText);
               convertView.setTag(holder);
           } else {
               holder = (ViewHolder) convertView.getTag();
           }

           DetallePartidoFragment.EstadisticaTotal estadistica = mapa.get(position);
           holder.txtFirst.setText(estadistica.getStat());
           holder.txtSecond.setText(estadistica.getLocal());
           holder.txtThird.setText(estadistica.getVisitante());

           return convertView;
       }
}

