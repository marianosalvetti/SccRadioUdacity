package ar.com.sccradiomobile.livescores;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import ar.com.sccradiomobile.BaseActivity;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.storage.util.Constants;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Created by Mariano Salvetti
 * Based on https://androidresearch.wordpress.com/2013/08/21/example-using-viewpager-with-actionbarsherlock-tabs/
 */
public class DetailPartidoActivity extends BaseActivity {
    static final int SECOND_TAB = 1;
    public static final String NOMBRE_PRIMER_TAB = "Detalles";
    public static final String NOMBRE_SEGUNDO_TAB = "Estadisticas";
    public static final String NOMBRE_TERCER_TAB = "Jugadas";

    public DetailPartidoActivity() {
        super(R.string.title_section7);
    }

    public DetailPartidoActivity(int titleRes) {
        super(R.string.title_section7);
    }

    private ActionBar actionBar;
    private ViewPager viewPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    ViewPagerPartidosAdapter pagerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setIcon(R.drawable.ic_drawer);

        View mView = getLayoutInflater().inflate(R.layout.actionbar_view, null);
        actionBar.setCustomView(mView, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        ));

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayHomeAsUpEnabled(false);


        setContentView(R.layout.detalle_partido_activity);

        //recupero los valores...
        Intent in = getIntent();
        int partidoId = (int) in.getExtras().getLong(Constants.ID, 0);
        String local = in.getExtras().getString(DetallePartidoFragment.EQUIPO_LOCAL);
        String visitante = in.getExtras().getString(DetallePartidoFragment.EQUIPO_VISITANTE);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(onPageChangeListener);
        final ViewPagerPartidosAdapter adapter = new ViewPagerPartidosAdapter(getSupportFragmentManager(), partidoId, local, visitante);
        viewPager.setAdapter(adapter);
        addActionBarTabs();
        this.pagerAdapter = adapter;

        actionBar.setDisplayHomeAsUpEnabled(true);
        slidingMenu.setSlidingEnabled(true);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    }

    private ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            actionBar.setSelectedNavigationItem(position);
        }
    };

    private void addActionBarTabs() {
        actionBar = getSupportActionBar();
        String[] tabs = {NOMBRE_PRIMER_TAB, NOMBRE_SEGUNDO_TAB, NOMBRE_TERCER_TAB};
        for (String tabTitle : tabs) {
            ActionBar.Tab tab = actionBar.newTab().setText(tabTitle).setTabListener(tabListener);
            actionBar.addTab(tab);
        }
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (tab.getPosition() == SECOND_TAB &&
                    DetailPartidoActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.cust_toast_layout, (ViewGroup) findViewById(R.id.rl_custom_toast));

                Toast toast = new Toast(getApplicationContext());
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
            }
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    };


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.liveScores:
                if (this.getSlidingMenu().isMenuShowing()) {
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
    public void onDestroy() {
        super.onDestroy();
    }

}