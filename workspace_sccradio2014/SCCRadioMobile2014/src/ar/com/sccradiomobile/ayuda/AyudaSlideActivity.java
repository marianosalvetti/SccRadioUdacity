package ar.com.sccradiomobile.ayuda;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import ar.com.sccradiomobile.BaseActivity;
import ar.com.sccradiomobile.R;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class AyudaSlideActivity extends BaseActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    public AyudaSlideActivity(int titleRes) {
        super(R.string.title_section2);

    }

    public AyudaSlideActivity() {
        super(R.string.title_section2);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        setContentView(R.layout.ayuda_screen_slide);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(this.getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                supportInvalidateOptionsMenu();
            }
        });

        cambiamosElTitulo(R.string.title_section9);
        setSlidingActionBarEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Method move to the next page
     *
     * @param button
     */
    public void nextPage(final View button) {
        if (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1) {
            mPager.setCurrentItem(mPager.getCurrentItem() - NUM_PAGES);
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.activity_screen_slide, menu);

        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;

            case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                if (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1) {
                    mPager.setCurrentItem(mPager.getCurrentItem() - NUM_PAGES);
                } else {
                    // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                    // will do nothing.
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
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

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public SherlockFragment getItem(int position) {
            return AyudaSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}
