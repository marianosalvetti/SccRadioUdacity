package ar.com.sccradiomobile.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import ar.com.sccradiomobile.R;
import ar.com.sccradiomobile.SCCRadioMobileApp;
import ar.com.sccradiomobile.storage.util.Constants;
import com.android.volley.toolbox.ImageLoader;

/**
 *
 * @author Mariano Salvetti
 *
 */
public class DetailImageFullScreenActivity extends Activity {
    protected static final String LOG_TAG = "images";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);

		setContentView(R.layout.activity_imagenes);

        String origen = getIntent().getStringExtra("ORIGEN");
        String imagePath = getIntent().getStringExtra("PATH");

        if (Constants.DEBUG)	Log.d(LOG_TAG, "Starting the images Activity... origen=" + origen);
        if (Constants.DEBUG)	Log.d(LOG_TAG, "         path=" + imagePath);

        String requestUrl = null;
        int start = imagePath.lastIndexOf("/");
        int end = imagePath.indexOf("&");
        int maxWidth = 1200;
        int maxHeight = 1200;

        @SuppressLint("WrongViewCast")
        TouchImageView imageView = (TouchImageView) findViewById(R.id.imageView);
        Button btnClose = (Button)  findViewById(R.id.btnClose);
          // close button click event
         btnClose.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });

        ImageLoader mImageLoader = SCCRadioMobileApp.IMAGE_CACHE_MANAGER.getImageLoader();
        requestUrl = imagePath;


        if (Constants.DEBUG) Log.d("image", "         requestUrl=" + requestUrl);

        mImageLoader.get(requestUrl,
               ImageLoader.getImageListener(imageView,
                       R.drawable.default_image,
                       R.drawable.error),
                maxWidth,
                maxHeight
       );

	}


}