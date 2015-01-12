package ar.com.sccradiomobile.images;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.databases.FeedTable;
import ar.com.sccradiomobile.livescores.LiveScoresActivity;
import ar.com.sccradiomobile.storage.RequestActivity;
import ar.com.sccradiomobile.storage.adapter.FeedAdapter;
import ar.com.sccradiomobile.storage.model.picasa.PicasaResponse;
import ar.com.sccradiomobile.storage.provider.FeedContentProvider;
import ar.com.sccradiomobile.storage.receiver.MyAlarmReceiver;
import ar.com.sccradiomobile.storage.receiver.MyAlarmReceiver.OnScheduleUpdateListener;
import ar.com.sccradiomobile.storage.util.Constants;
import ar.com.sccradiomobile.storage.util.request.GsonRequest;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.Request;
import com.android.volley.VolleyError;

 


/**
 * Executes a request to get photos from Picasa dynamically.
 * <p>
 * Implements the "Endless" List UI design pattern :). See
 * {@link EndlessScrollListener}.
 * <p>
 * Implements Polling pattern. This is NOT a best practice.
 * 
 * 
 * @author Mariano Salvetti
 */
public class DynamicPhotoListActivity extends RequestActivity<PicasaResponse>
		implements OnScheduleUpdateListener,
		LoaderManager.LoaderCallbacks<Cursor> {

	public DynamicPhotoListActivity() {
		super(R.string.title_section2);
	}
	
	public DynamicPhotoListActivity(int titleRes) {
		super(R.string.title_section2);
	}

	private static final String TAG = "SCCRadioMobileApp";
	private static final int RESULT_PER_PAGE = 20;
	private ListView mListView;
	private FeedAdapter mAdapter;
	private boolean mHasData = false;
	private boolean mErrorOcurred = false;

	// The loader's unique id. Loader ids are specific to the Activity or
	// Fragment in which they reside.
	private static final int LOADER_ID = 1;

	private BroadcastReceiver mAlarmReceiver;
	private boolean isRequestPending;
	private boolean needToScroll;

	public static final String ACTION_UPDATE_DATA = "ar.com.sccradiomobile.action.UPDATE_DATA"; //change to ar.com.sccradiomobile

	@Override
	protected void onCreateImpl(Bundle savedInstanceState) {
		setSlidingActionBarEnabled(true);
		 
        if (Constants.DEBUG) Log.d("sccradio", "Starting the Noticias Dynamic List... ");
		setContentView(R.layout.activity_dynamic_list);

		mListView = (ListView) findViewById(R.id.lstFeed);

		// Set List empty view
		mListView.setEmptyView(findViewById(android.R.id.empty));

		// Scroll listener
		mListView.setOnScrollListener(new EndlessScrollListener());

		cambiamosElTitulo(R.string.title_section4);
		setSlidingActionBarEnabled(true);


		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		/**
		 * here we have the solution for the navigation icon
		 */
		getSupportActionBar().setIcon(android.R.color.transparent);


        if (Constants.DEBUG)  Log.d("sccradio", "comenzando con el retrieveLocalFeeds... ");
		retrieveLocalFeeds();
	}

	/**
	 * Gets Feeds that are stored locally.
	 */
	private void retrieveLocalFeeds() {

		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] { FeedTable.COLUMN_TITLE, FeedTable.COLUMN_IMAGE_URL , FeedTable.COLUMN_DESCRIPTION};
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.tvTitle , R.id.description};

		// Initialize the adapter. Note that we pass a 'null' Cursor as the
		// third argument. We will pass the adapter a Cursor only when the
		// data has finished loading for the first time (i.e. when the
		// LoaderManager delivers the data to onLoadFinished). Also note
		// that we have passed the '0' flag as the last argument. This
		// prevents the adapter from registering a ContentObserver for the
		// Cursor (the CursorLoader will do this for us!).
		mAdapter = new FeedAdapter(this, R.layout.row_dynamic_picassa_list_element, null, from,to, 0);

		// Associate the (now empty) adapter with the ListView.
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			 /*   Toast.makeText(getApplicationContext(),
			      "Tap on Image " + (position +1), Toast.LENGTH_LONG)
			      .show();
			  */
			  }
		 
			}); 
		
		// Initialize the Loader with LOADER_ID and callbacks this.
		// If the loader doesn't already exist, one is created. Otherwise,
		// the already created Loader is reused. In either case, the
		// LoaderManager will manage the Loader across the Activity/Fragment
		// lifecycle, will receive any new loads once they have completed,
		// and will report this new data back to the 'mCallbacks' object.
		getSupportLoaderManager().initLoader(LOADER_ID, null, this);

	}

	@Override
	public void onResume() {
		super.onResume();
		
		isRunning = true;

		if (!mHasData && !mErrorOcurred) {

			// Execute the request
			// if (!isRequestPending) {
			// performRequest();
			// isRequestPending = true;
			// }
		}

		// Register the receiver
		mAlarmReceiver = new MyAlarmReceiver(this);
		IntentFilter intentFilter = new IntentFilter(ACTION_UPDATE_DATA);
		this.registerReceiver(mAlarmReceiver, intentFilter);

		// After receiver registering, we can schedule the update.
		scheduleAlarmReceiver();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isRunning = false;
		this.unregisterReceiver(mAlarmReceiver);

	}

	// Schedule AlarmManager to invoke MyAlarmReceiver and cancel any existing
	// current PendingIntent.
	private void scheduleAlarmReceiver() {
		AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

		Intent i = new Intent(DynamicPhotoListActivity.ACTION_UPDATE_DATA);

		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0); // PendingIntent.FLAG_CANCEL_CURRENT

		// Use inexact repeating which is easier on battery (system can phase
		// events and not wake at exact times)
		alarmMgr.setInexactRepeating(AlarmManager.RTC,
				Constants.ScheduleUpdate.TRIGGER_AT_TIME, // first poll
				Constants.ScheduleUpdate.INTERVAL, pi);
	}

	@Override
	public void onScheduleUpdate() {
		if (!isRequestPending) {
			performRequest();
			isRequestPending = true;
			needToScroll = true;
		}
	}

	@Override
	protected Request<PicasaResponse> createRequest() {
		String photoOf = "sccradio"; // search criteria
		int thumbSize = 160;
		int startIndex = 1 + mAdapter.getCount();

		// TODO Use Uri.Builder
		String url = "https://picasaweb.google.com/data/feed/api/all?q="
				+ photoOf + "&thumbsize=" + thumbSize + "&max-results="
				+ RESULT_PER_PAGE + "&start-index=" + startIndex + "&alt=json";


		if (Constants.DEBUG) Log.d(TAG, "GET URL: " + url.toString());
		GsonRequest<PicasaResponse> request = new GsonRequest<PicasaResponse>(
				Request.Method.GET, url, PicasaResponse.class, this, this);

		return request;
	}

	@Override
	public void onResponse(PicasaResponse response) {
        if (Constants.DEBUG) Log.d(TAG, "GET success!: " + response.toString());

		mProgressDialog.dismiss();

		isRequestPending = false;

		// Add feeds to the adapter
		mAdapter.addAll(response);

		if (needToScroll) {
			int position = mAdapter.getCount() - RESULT_PER_PAGE;
			mListView.setSelection(position);
			needToScroll = false;
		}		
	}

	@Override
	public void onErrorResponse(VolleyError error) {
        if (Constants.DEBUG) Log.d(TAG, "GET error: " + error.getMessage());
		mProgressDialog.dismiss();
		mErrorOcurred = true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// Create a new CursorLoader with the following query parameters.
		String[] projection = { FeedTable.COLUMN_ID, FeedTable.COLUMN_TITLE,FeedTable.COLUMN_IMAGE_URL,FeedTable.COLUMN_DESCRIPTION};
		return new CursorLoader(this, FeedContentProvider.CONTENT_URI,projection, null, null, null);

	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// A switch-case is useful when dealing with multiple Loaders/IDs
		switch (loader.getId()) {
		case LOADER_ID:
			// The asynchronous load is complete and the data
			// is now available for use. Only now can we associate
			// the queried Cursor with the SimpleCursorAdapter.
			mAdapter.swapCursor(cursor);

			if (mAdapter.getCount() == 0) {
				performRequest();
			}
			break;
		}
		// The listview now displays the queried data.
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// For whatever reason, the Loader's data is now unavailable.
		// Remove any references to the old data by replacing it with
		// a null Cursor.
		mAdapter.swapCursor(null);
	}

	/**
	 * Detects when user is closer to the end of the current page and starts
	 * loading the next page so the user will not have to wait (that much) for
	 * the next entries.
	 */
	public class EndlessScrollListener implements AbsListView.OnScrollListener {
		// How many entries earlier to start loading next page
		private int visibleThreshold = 5;
		private int currentPage = 0;
		private int previousTotal = 0;
		private boolean loading = true;

		public EndlessScrollListener() {
		}

		public EndlessScrollListener(int visibleThreshold) {
			this.visibleThreshold = visibleThreshold;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (loading) {
				if (totalItemCount > previousTotal) {
					loading = false;
					previousTotal = totalItemCount;
					currentPage++;
				}
			}
			if (!loading
					&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				// I load the next page of gigs using a background task,
				// but you can call any function here.
				if (!isRequestPending) {
					loading = true;
					performRequest();
					isRequestPending = true;
				}

			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		public int getCurrentPage() {
			return currentPage;
		}
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