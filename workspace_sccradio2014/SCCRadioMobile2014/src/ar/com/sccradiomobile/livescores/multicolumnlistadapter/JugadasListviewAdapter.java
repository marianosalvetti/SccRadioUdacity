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
import java.util.Stack;

/**
 * Created by Mariano Salvetti on 05/05/2014.
 *
  Ver ejemplo en: http://ernimantel.com.ar/carpetadudo/lnb/detalle_jugada_extras.json
 */

public class JugadasListviewAdapter extends BaseAdapter{
    public static final String FIRST_COLUMN = "first";
    public static final String SECOND_COLUMN= "second";
    public static final String THIRD_COLUMN = "third";
    public static final String FOURTH_COLUMN = "fourth";
    public static final String FIFTH_COLUMN = "fifth";
    public static final String SIXTH_COLUMN = "sixth";
    public static final String CONVERTIDO = "convertido";
    public Stack<HashMap<String, String>> pila;
    private Activity activity;

       public JugadasListviewAdapter(Activity activity, Stack<HashMap<String,String>> list) {
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
              TextView txtFirst;
              TextView txtSecond;
              TextView txtThird;
              TextView txtFourth;
              TextView txtFifth;
              TextView txtSixth;
         }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder holder;
           LayoutInflater inflater = activity.getLayoutInflater();

           if (convertView == null) {
               convertView = inflater.inflate(R.layout.listview_jornada_row, null);
               holder = new ViewHolder();
               holder.txtFirst = (TextView) convertView.findViewById(R.id.FirstText);
               holder.txtSecond = (TextView) convertView.findViewById(R.id.SecondText);
               holder.txtThird = (TextView) convertView.findViewById(R.id.ThirdText);
               holder.txtFourth = (TextView) convertView.findViewById(R.id.FourthText);
               holder.txtFifth = (TextView) convertView.findViewById(R.id.FifthText);
               holder.txtSixth = (TextView) convertView.findViewById(R.id.SixthText);
               convertView.setTag(holder);
           } else {
               holder = (ViewHolder) convertView.getTag();
           }

           HashMap<String, String> map = pila.get(position);
           holder.txtFirst.setText(map.get(FIRST_COLUMN));
           holder.txtSecond.setText(map.get(SECOND_COLUMN));
           holder.txtThird.setText(map.get(THIRD_COLUMN));
           holder.txtFourth.setText(map.get(FOURTH_COLUMN));

           final String accion = map.get(FIFTH_COLUMN);
           holder.txtFifth.setText(accion);
           holder.txtSixth.setText(map.get(SIXTH_COLUMN));


           if (accion.contains(CONVERTIDO)) {
             holder.txtFirst.setTypeface(Typeface.DEFAULT_BOLD);
             holder.txtSecond.setTypeface(Typeface.DEFAULT_BOLD);
             holder.txtThird.setTypeface(Typeface.DEFAULT_BOLD);
             holder.txtFourth.setTypeface(Typeface.DEFAULT_BOLD);
             holder.txtFifth.setTypeface(Typeface.DEFAULT_BOLD);
             holder.txtSixth.setTypeface(Typeface.DEFAULT_BOLD);
           } else {
              holder.txtFirst.setTypeface(Typeface.DEFAULT);
              holder.txtSecond.setTypeface(Typeface.DEFAULT);
              holder.txtThird.setTypeface(Typeface.DEFAULT);
              holder.txtFourth.setTypeface(Typeface.DEFAULT);
              holder.txtFifth.setTypeface(Typeface.DEFAULT);
              holder.txtSixth.setTypeface(Typeface.DEFAULT);
           }
           return convertView;
       }
}

