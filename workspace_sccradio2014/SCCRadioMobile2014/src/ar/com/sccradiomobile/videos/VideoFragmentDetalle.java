package ar.com.sccradiomobile.videos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import ar.com.sccradiomobile.BaseActivity;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.livescores.LiveScoresActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;


public class VideoFragmentDetalle extends BaseActivity {

    public VideoFragmentDetalle(int titleRes) {
        super(titleRes);
    }

    public VideoFragmentDetalle() {
        super(R.string.title_section1);
    }

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITULO = "title";
    public static final String EXTRA_ID_VIDEO = "id";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        setSlidingActionBarEnabled(true);

        setContentView(R.layout.video_fragment_detalle);
               ActionBar actionBar = getSupportActionBar();
               actionBar.setDisplayShowCustomEnabled(true);
               actionBar.setIcon(R.drawable.ic_drawer);

                    View mView = getLayoutInflater().inflate(R.layout.actionbar_view, null);
                    actionBar.setCustomView(mView, new ActionBar.LayoutParams(
                            ActionBar.LayoutParams.WRAP_CONTENT,
                            ActionBar.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER
                    ));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        VideoFragment videoFragment = (VideoFragment) this.getSupportFragmentManager().findFragmentById(R.id.video_fragment);

        final String video_url = getIntent().getStringExtra(EXTRA_URL);
        final String video_title = getIntent().getStringExtra(EXTRA_TITULO);
        final String video_id = getIntent().getStringExtra(EXTRA_ID_VIDEO);
        if (videoFragment != null) {
            videoFragment.updateContent(video_url,video_id,video_title);
        } else {
            Log.d("lnb_mobile", "------->> el videoFragment es nulo <<<-------------");
        }
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
}