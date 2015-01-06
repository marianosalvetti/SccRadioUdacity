package ar.com.sccradiomobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import ar.com.sccradiomobile.livescores.LiveScoresActivity;
import ar.com.sccradiomobile.livescores.MatchCenterSlide;
import ar.com.sccradiomobile.slidemenu.SlidingMenuFragment;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Clase Base de toda la Arquitectura, ya que hereda de SlidingFragmentActivity, y que a su vez, esta
 * ultima hereda de SherlockFragmentActivity.
 *
 * @author Mariano Salvetti
 */
public class BaseActivity extends SlidingFragmentActivity {

    private int mTitleRes;
    protected SlidingMenuFragment mFrag;
    protected MatchCenterSlide matchCenterFrag;
    protected SlidingMenu slidingMenu;
    public static int checkoutResultcode = 0;
    protected boolean doubleBackToExitPressedOnce = false;
    protected static final String LOG_TAG = "sccradio_base";
    protected String categoria;

    public BaseActivity(int titleRes) {
        mTitleRes = titleRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mFrag = new SlidingMenuFragment();
            t.replace(R.id.menu_frame, mFrag);
            t.commit();
        } else {
            mFrag = (SlidingMenuFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }


        // customize the SlidingMenu
        customizeSlideMenu(savedInstanceState);

        // add the custom view to the action bar
        setupActionBar();

    }

    /**
     * Inicializamos y configuramos el menu lateral de la aplicacion
     *
     * @param savedInstanceState
     */
    private void customizeSlideMenu(Bundle savedInstanceState) {
        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        mFrag.setSlidingMenu(slidingMenu);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);

        getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
        getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            matchCenterFrag = new MatchCenterSlide(this);
            t.replace(R.id.menu_frame_two, matchCenterFrag, "matchcenter");
            t.commit();
        }

        slidingMenu.setSlidingEnabled(true);
    }

    /**
     * Inicializamos y configuramos la Barra de Acion Superior de la aplicacion
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_drawer);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
        actionBar.setHomeButtonEnabled(true);

        View mView = getLayoutInflater().inflate(R.layout.actionbar_view, null);
        actionBar.setCustomView(mView);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                return true;
            case R.id.liveScores:
                if (this.getSlidingMenu().isMenuShowing()) {
                    this.getSlidingMenu().toggle();
                }
                Intent mainIntent = new Intent().setClass(BaseActivity.this, LiveScoresActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            this.getSlidingMenu().toggle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static void showAlert(String object, String ok, final Runnable okRunnable, String cancel, final Runnable cancelRunnable, final Activity ctx) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(ok, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (okRunnable != null)
                    okRunnable.run();
                if (checkoutResultcode == -1)
                    ctx.finish();

            }
        });
        if (cancelRunnable != null) {
            alertDialog.setNegativeButton(cancel, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancelRunnable.run();
                }
            });
        }
        alertDialog.setMessage(object);
        if (!ctx.isFinishing()) {
            ctx.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    alertDialog.show();
                }
            });
        }
    }

    public void showAlert(String object, String ok, final Runnable okRunnable, String cancel, final Runnable cancelRunnable) {
        showAlert(object, ok, okRunnable, cancel, cancelRunnable, this);
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

    protected void cambiamosElTitulo(final int title_section) {
        ActionBar actionBar = this.getSupportActionBar();


        TextView partido = (TextView) actionBar.getCustomView().findViewById(R.id.texto_header);
        partido.setVisibility(View.VISIBLE);

        partido.setText(this.getResources().getString(title_section));
        actionBar.setDisplayShowCustomEnabled(true);
    }

    protected void cambiamosElTitulo(final String title_section) {
        ActionBar actionBar = this.getSupportActionBar();

        TextView partido = (TextView) actionBar.getCustomView().findViewById(R.id.texto_header);
        partido.setVisibility(View.VISIBLE);

        partido.setText(title_section);
        actionBar.setDisplayShowCustomEnabled(true);
    }
}
