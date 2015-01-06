package ar.com.sccradiomobile.livescores;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import ar.com.sccradiomobile.BaseActivity;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.storage.model.livescores.Partido;
import ar.com.sccradiomobile.storage.model.livescores.PartidoDataResponse;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.storage.util.request.GsonRequest;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Idea basica de como  puede ser la actividad para los ImagenesActivity
 *
 * @author Mariano Salvetti
 */
public class LiveScoresActivity extends BaseActivity implements OnClickListener {

    private static final String LOG_TAG = "live_Scores";
    private Button btnChangeDate;
    private static final int DATE_DIALOG_ID = 999;
    private TextView tvDisplayDate;
    private TextView tvNoDate;
    private int year;
    private int month;
    private int day;
    private ListView listView;
    private boolean existenFechas = true;
    private LiveScoresAdapter<Partido> adapter;

    public LiveScoresActivity(int titleRes) {
        super(R.string.title_section7);
    }

    public LiveScoresActivity() {
        super(R.string.title_section7);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
     protected void onStart() {
       super.onStart();
     }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        setSlidingActionBarEnabled(true);
        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        setContentView(R.layout.activity_live_scores);

        listView = (ListView) findViewById(R.id.listResultados);
        listView.setEmptyView(findViewById(android.R.id.empty));
        tvDisplayDate = (TextView) findViewById(R.id.tvDisplayDate);
        btnChangeDate = (Button) findViewById(R.id.btn_date);
        btnChangeDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);

            }

        });

        setCurrentDateOnView();

        if (existenFechas) {

            setBasicListOnView();
            setBasicListHeaderOnViewFromDB();
        } else {
            btnChangeDate.setVisibility(View.GONE);
            tvDisplayDate.setVisibility(View.GONE);
            tvNoDate = (TextView) findViewById(R.id.no_dates);
            tvNoDate.setVisibility(View.VISIBLE);

        }

        cambiamosElTitulo(R.string.title_section7);
        setSlidingActionBarEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                  case android.R.id.home:
                    super.onBackPressed();
                    return true;

                  default:
                     return super.onOptionsItemSelected(item);
            }
        }


    private void setBasicListHeaderOnViewFromDB() {
        // Get ListView object from xml
        LinearLayout ll_View = (LinearLayout) findViewById(R.id.ll_scroll_dates);

        List<Map<String, String>> data = new CreateMockbuttons().invoke();
        int count = 1;
        for (final Map<String, String> element : data) {
            final Button a = new Button(this);

            final String text = element.get("title");
            final String date = element.get("date");
            a.setText(Html.fromHtml("<font color='red'>"+text+"</font><br/><font color='blue'>"+date+"</font>"));
            a.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final int finalCount = count;
            a.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doRequestByDate(date, finalCount);
                }
            });
            ll_View.addView(a);
            count++;
        }

    }

    private void doRequestByDate(final String date, final int jornada) {
        String url = "http://sccradio.com.ar/carpetadudo/lnb/jornada_"+ jornada +".json";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading));
        if(this != null && !this.isFinishing()){
            pDialog.show();
        }
        Request<PartidoDataResponse>  request = createRequest(date, jornada, url, pDialog);

        SCCRadioMobileApp.REQUEST_MANAGER.addToRequestQueue(request, SCCRadioMobileApp.TAG);
    }

    private Request<PartidoDataResponse> createRequest(final String date, final int jornada, String url, final ProgressDialog pDialog) {

        Response.Listener<PartidoDataResponse> successListener = new Response.Listener<PartidoDataResponse>() {

            @Override
            public void onResponse(PartidoDataResponse response) {
                if (Constants.DEBUG)    Log.d(LOG_TAG, response.toString());
                if (Constants.DEBUG)    Log.d(LOG_TAG, " ESTADO DEL RESPONSE ---->> " + response.getEstado());
                pDialog.hide();
                //actualizar fecha y luego el listado
                tvDisplayDate.setText(date);
                listView.setVisibility(View.VISIBLE);
                final ArrayList<Partido> partidos = response.getDatos().getPartidos();

                adapter = new LiveScoresAdapter<Partido>(LiveScoresActivity.this,R.layout.live_scores_item, partidos);

                listView.setAdapter(adapter);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (Constants.DEBUG)    Log.d(LOG_TAG, "Error: " + error.getMessage());
                pDialog.hide();
                tvDisplayDate.setText("Sin resultados para el " + date);
                listView.setVisibility(View.GONE);
            }
        };

        GsonRequest<PartidoDataResponse> request = new GsonRequest<PartidoDataResponse>(
                Request.Method.GET, url, PartidoDataResponse.class,
                successListener, errorListener);

        return request;
    }

    private List<Map<String, String>> createMockbuttons() {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Map<String, String> datum = new HashMap<String, String>(2);
        datum.put("title", "Titulo 1");
        String dateStr = "23/03/2014";
        SimpleDateFormat curFormater = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        Date dateObj = Calendar.getInstance().getTime();
        try {
            dateObj = curFormater.parse(dateStr);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        String newDateStr = postFormater.format(dateObj);
        datum.put("date", newDateStr);
        data.add(datum);

        Map<String, String> datum2 = new HashMap<String, String>(2);
        datum2.put("title", "Titulo 2");
        String dateStr2 = "24/03/2014";
        Date dateObj2 = new Date();
        try {
            dateObj2 = curFormater.parse(dateStr2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newDateStr = postFormater.format(dateObj2);
        datum2.put("date", newDateStr);
        data.add(datum2);

        Map<String, String> datum3 = new HashMap<String, String>(2);
        datum3.put("title", "Titulo 3");
        String dateStr3 = "30/03/2014";
        Date dateObj3 = new Date();
        try {
            dateObj3 = curFormater.parse(dateStr3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newDateStr = postFormater.format(dateObj3);
        datum3.put("date", newDateStr);
        data.add(datum3);
        return data;
    }


    private void setBasicListOnView() {

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                final int itemPosition = position;
                // ListView Clicked item value
                Partido itemValue = (Partido) listView.getItemAtPosition(position);

                Bundle b = new Bundle();
                b.putLong(Constants.ID, Long.parseLong(itemValue.getPartido()));
                b.putString("local",itemValue.getLocal());
                b.putString("visitante",itemValue.getVisitante());
                Intent mainIntent = new Intent().setClass(getApplicationContext(), DetailPartidoActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.putExtras(b);

                startActivity(mainIntent);
            }

        });

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
    public void onClick(View v) {
        Toast.makeText(this, "Refresh values for this date", Toast.LENGTH_LONG).show();
    }

    // display current date
    public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.tvDisplayDate);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month + 1).append("-").append(year).append(" "));

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tvDisplayDate.setText(new StringBuilder()
                    .append(day).append("/").append(month + 1).append("/").append(year).append(" "));

        }
    };

    /**
     * Creacion de datos MOCK's para el header con scroll sobre las JORNADAS del Torneo.
     */
    private class CreateMockbuttons {
        public List<Map<String, String>> invoke() {
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("title", "Jornada 1");
            String dateStr = "23/03/2014";
            SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
            Date dateObj = new Date();
            try {
                dateObj = curFormater.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat postFormater = new SimpleDateFormat("EE,dd/MM/yyyy");
            String newDateStr = postFormater.format(dateObj);
            datum.put("date", newDateStr);
            if (Constants.DEBUG)  Log.d("lnb_mobile","------->>>> usando = " + newDateStr);
            data.add(datum);

            Map<String, String> datum2 = new HashMap<String, String>(2);
           datum2.put("title", "Jornada 2");
           String dateStr2 = "26/03/2014";
           Date dateObj2 = new Date();
           try {
               dateObj2= curFormater.parse(dateStr2);
           } catch (ParseException e) {
               e.printStackTrace();
           }
           newDateStr = postFormater.format(dateObj2);
           datum2.put("date", newDateStr);
           data.add(datum2);

            Map<String, String> datum3 = new HashMap<String, String>(2);
            datum3.put("title", "Jornada 3");
            String dateStr3 = "30/03/2014";
            Date dateObj3 = new Date();
            try {
                dateObj3 = curFormater.parse(dateStr3);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newDateStr = postFormater.format(dateObj3);
            datum3.put("date", newDateStr);
            data.add(datum3);

            Map<String, String> datum4 = new HashMap<String, String>(2);
            datum4.put("title", "Jornada 4");
            String dateStr4 = "05/04/2014";
            Date dateObj4 = new Date();
            try {
                dateObj4 = curFormater.parse(dateStr4);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newDateStr = postFormater.format(dateObj4);
            datum4.put("date", newDateStr);
            data.add(datum4);

            Map<String, String> datum5 = new HashMap<String, String>(2);
            datum5.put("title", "Jornada 5");
            String dateStr5 = "06/04/2014";
            Date dateObj5 = new Date();
            try {
                dateObj5 = curFormater.parse(dateStr5);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newDateStr = postFormater.format(dateObj5);
            datum5.put("date", newDateStr);
            data.add(datum5);

            Map<String, String> datum6 = new HashMap<String, String>(2);
            datum6.put("title", "Jornada 6");
            String dateStr6 = "13/04/2014";
            Date dateObj6 = new Date();
            try {
                dateObj6 = curFormater.parse(dateStr6);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newDateStr = postFormater.format(dateObj6);
            datum6.put("date", newDateStr);
            data.add(datum6);

            Map<String, String> datum7 = new HashMap<String, String>(2);
            datum7.put("title", "Jornada 7");
            String dateStr7 = "14/04/2014";
            Date dateObj7 = new Date();
            try {
                dateObj7 = curFormater.parse(dateStr7);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newDateStr = postFormater.format(dateObj7);
            datum7.put("date", newDateStr);
            data.add(datum7);

            Map<String, String> datum8 = new HashMap<String, String>(2);
            datum8.put("title", "Jornada 8");
            String dateStr8 = "15/04/2014";
            Date dateObj8 = new Date();
            try {
                dateObj8 = curFormater.parse(dateStr8);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newDateStr = postFormater.format(dateObj8);
            datum8.put("date", newDateStr);
            data.add(datum8);

            Map<String, String> datum9 = new HashMap<String, String>(2);
            datum9.put("title", "Jornada 9");
            String dateStr9 = "21/04/2014";
            Date dateObj9 = new Date();
            try {
                dateObj9 = curFormater.parse(dateStr9);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newDateStr = postFormater.format(dateObj9);
            datum9.put("date", newDateStr);
            data.add(datum9);

            Map<String, String> datum10 = new HashMap<String, String>(2);
            datum10.put("title", "Jornada 10");
            String dateStr10 = "22/04/2014";
            Date dateObj10 = new Date();
            try {
                dateObj10 = curFormater.parse(dateStr10);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newDateStr = postFormater.format(dateObj10);
            datum10.put("date", newDateStr);
            data.add(datum10);
            return data;
        }
    }
}