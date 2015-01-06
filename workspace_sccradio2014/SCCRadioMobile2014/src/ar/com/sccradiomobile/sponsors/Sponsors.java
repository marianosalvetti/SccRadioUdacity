package ar.com.sccradiomobile.sponsors;


import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import ar.com.sccradiomobile.BaseActivity;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.livescores.LiveScoresActivity;
import com.actionbarsherlock.view.MenuItem;

/**
 * Created by Mariano Salvetti
 */
public class Sponsors extends BaseActivity {

    public Sponsors() {
    		super(R.string.title_section8);

    	}

	public Sponsors(int titleRes) {
		super(R.string.title_section8);

	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        setSlidingActionBarEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setContentView(R.layout.sponsors_web_layout);
          // cambiamos el titulo
        cambiamosElTitulo(R.string.title_section8);

        WebView engine = (WebView) findViewById(R.id.web_engine);
        String url = "file:///android_asset/sponsors/main_sponsors.html";
        engine.loadUrl(url);


        cambiamosElTitulo(R.string.title_section8);
      		setSlidingActionBarEnabled(true);
      		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

  
    protected int getLayoutResourceId() {
        return R.layout.activity_live_scores;
    }

    @Override
    public void onBackPressed() {
       if (this.getSlidingMenu().isMenuShowing()) {
           this.getSlidingMenu().toggle();
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
              if (  this.getSlidingMenu().isMenuShowing()) {
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
}
