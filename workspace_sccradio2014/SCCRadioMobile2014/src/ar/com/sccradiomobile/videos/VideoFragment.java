package ar.com.sccradiomobile.videos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.storage.util.Constants;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;



public class VideoFragment extends SherlockFragment implements YouTubePlayer.OnInitializedListener {
	
 	public static final String API_KEY = DeveloperKeyYouTube.DEVELOPER_KEY;
    protected static final String LOG_TAG = "sccradio_tv";
	public static  String VIDEO_ID = "_9qqE9tQldI";
	
	private YouTubePlayer youTubePlayer;

	private YouTubePlayerSupportFragment youTubePlayerFragment;
	private TextView textVideoLog;
	private Button btnViewFullScreen;
	
	private static final int RQS_ErrorDialog = 1;
	
	private MyPlayerStateChangeListener myPlayerStateChangeListener;
	private MyPlaybackEventListener myPlaybackEventListener;
	private boolean isInit = true;
	String log = "";



    public void updateContent(final String url, final String id, String titulo) {

		Log.d(LOG_TAG," ------ updateContent for video id= " + url + " --------");
	    VIDEO_ID = url;

        // cambiamos el titulo
        if (titulo.length() > 45) {
            titulo = titulo.substring(0, 47) + "...";
        }

        cambiamosElTitulo(titulo);

		if (!isInit)
			youTubePlayer.cueVideo(VIDEO_ID);
		   
	       
	}
 
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.video_fragment, container, false);
	
 		youTubePlayerFragment = (YouTubePlayerSupportFragment) this.getFragmentManager().findFragmentById(R.id.youtubeplayerfragment);
		if (Constants.DEBUG) Log.d(LOG_TAG, "API_KEY: " + API_KEY);
        try {
           youTubePlayerFragment.initialize(API_KEY, this);
        } catch (NullPointerException npe) {
           if (Constants.DEBUG) Log.e(LOG_TAG, npe.toString());
        }
		
		myPlayerStateChangeListener = new MyPlayerStateChangeListener();
		myPlaybackEventListener = new MyPlaybackEventListener();
		    
		textVideoLog = (TextView)rootView.findViewById(R.id.videolog);
		btnViewFullScreen = (Button)rootView.findViewById(R.id.btnviewfullscreen);
	    btnViewFullScreen.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			youTubePlayer.setFullscreen(true);
		}});
		textVideoLog.setText("Video filmado por Diego Ivan Salvetti para SCCRadio");

		return rootView;
	}
	
	@Override
	public void onInitializationFailure(Provider provider,
			YouTubeInitializationResult result) {
		
		if (result.isUserRecoverableError()) {
			result.getErrorDialog(getActivity(), RQS_ErrorDialog).show();	
		}
	}

	@Override
	public void onInitializationSuccess(Provider provider, YouTubePlayer player,boolean wasRestored) {
		 isInit = false;
		youTubePlayer = player;
	 	youTubePlayer.setPlayerStateChangeListener(myPlayerStateChangeListener);
		youTubePlayer.setPlaybackEventListener(myPlaybackEventListener);
		
		if (!wasRestored) {
		      player.cueVideo(VIDEO_ID);
		    }

	}
    protected void cambiamosElTitulo(final String title_section) {
         ActionBar actionBar = super.getSherlockActivity().getSupportActionBar();

         TextView video = (TextView) actionBar.getCustomView().findViewById(R.id.texto_header);
         video.setVisibility(View.VISIBLE);

         video.setText(title_section );
         actionBar.setDisplayShowCustomEnabled(true);
     }
	private final class MyPlayerStateChangeListener implements PlayerStateChangeListener {
		
		private void updateLog(String prompt){
			log += 	"MyPlayerStateChangeListener" + "\n" + prompt + "\n\n=====";
		//	textVideoLog.setText(log);
		};

		@Override
		public void onAdStarted() {
			updateLog("onAdStarted()");
		}

		@Override
		public void onError(
				com.google.android.youtube.player.YouTubePlayer.ErrorReason arg0) {
			updateLog("onError(): " + arg0.toString());
		}

		@Override
		public void onLoaded(String arg0) {
			updateLog("onLoaded(): " + arg0);
		}

		@Override
		public void onLoading() {
			updateLog("onLoading()");
		}

		@Override
		public void onVideoEnded() {
			updateLog("onVideoEnded()");
		}

		@Override
		public void onVideoStarted() {
			updateLog("onVideoStarted()");
		}
		
	}
	
	private final class MyPlaybackEventListener implements PlaybackEventListener {
		
		private void updateLog(String prompt){
			log += 	"MyPlaybackEventListener" + "\n-" +  prompt + "\n\n=====";
		//	textVideoLog.setText(log);
		};

		@Override
		public void onBuffering(boolean arg0) {
			updateLog("onBuffering(): " + String.valueOf(arg0));
		}

		@Override
		public void onPaused() {
			updateLog("onPaused()");
		}

		@Override
		public void onPlaying() {
			updateLog("onPlaying()");
		}

		@Override
		public void onSeekTo(int arg0) {
			updateLog("onSeekTo(): " + String.valueOf(arg0));
		}

		@Override
		public void onStopped() {
			updateLog("onStopped()");
		}
		
	}

}