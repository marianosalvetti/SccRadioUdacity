package ar.com.sccradiomobile.home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;
import ar.com.sccradiomobile.BaseActivity;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.home.noticias.PostFilafiloFragment;
import ar.com.sccradiomobile.home.noticias.PostNoticiasFragment;
import ar.com.sccradiomobile.storage.util.Constants;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Idea basica de como nos gustaria la Home Page, usando el Layout similar al de Facebook.
 * Es solo una prueba de concepto que debe continuar trabajandose.
 *
 * @author Mariano Salvetti
 */
public class HomePage extends BaseActivity {

    Fragment fragment1 = new PostNoticiasFragment().newInstance(Constants.CATEGORIA_TNA);
    Fragment fragment2 = new PostFilafiloFragment().newInstance(Constants.CATEGORIA_FILAFILO);

    protected static final String LOG_TAG = "home";

    public HomePage(int titleRes) {
        super(R.string.title_section0);
    }

    public HomePage() {
        super(R.string.title_section0);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        SCCRadioMobileApp.initAppRate(this);
        cambiamosElTitulo(R.string.title_section0);
        setSlidingActionBarEnabled(true);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        categoria = getIntent().getStringExtra(Constants.CATEGORIA);

        if (Constants.DEBUG) Log.d(LOG_TAG, "----> categoria: " + categoria);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (categoria.equals(Constants.CATEGORIA_TNA)) {
            ft.replace(android.R.id.content, fragment1);
        } else if (categoria.equals(Constants.CATEGORIA_FILAFILO)) {
            ft.replace(android.R.id.content, fragment2);
        }

        ft.commit();

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Por favor, haga clic en Atrás para salir", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

}