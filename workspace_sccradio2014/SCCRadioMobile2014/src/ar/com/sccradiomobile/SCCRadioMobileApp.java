package ar.com.sccradiomobile;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap.CompressFormat;
import ar.com.sccradiomobile.databases.TinyDB;
import ar.com.sccradiomobile.storage.util.image.ImageCacheManager;
import ar.com.sccradiomobile.storage.util.image.ImageCacheManager.CacheType;
import ar.com.sccradiomobile.storage.util.request.RequestManager;
import ar.com.sccradiomobile.utils.AppRate;

/**
 * Main entry point of the app.
 * 
 * @author Mariano Salvetti
 */
public class SCCRadioMobileApp extends Application {

	public static final String TAG = "SCCRadioMobileApp";

	// Handle requests
	public static RequestManager REQUEST_MANAGER;

	// Handle image cache
	public static ImageCacheManager IMAGE_CACHE_MANAGER;

	// Constants for image cache
	private static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10; // 10 MB
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100; // PNG is lossless so
														// quality is ignored
														// but must be provided
    private static TinyDB tinydb ;
    private static AppRate appRate;

	@Override
	public void onCreate() {
		super.onCreate();

		REQUEST_MANAGER = new RequestManager(this);
		
		IMAGE_CACHE_MANAGER = new  ImageCacheManager(this,
				this.getPackageCodePath(), DISK_IMAGECACHE_SIZE,
				DISK_IMAGECACHE_COMPRESS_FORMAT, DISK_IMAGECACHE_QUALITY,
				CacheType.DISK); // Use Disk based LRU Image cache implementation.

        tinydb = new TinyDB(this);

	}

	@Override
    public void onTerminate() {
        super.onTerminate();
    }
	

    public static TinyDB getTinyDB() {
        return tinydb;
    }

    public static AppRate getAppRate() {
           return appRate;
       }

    public static void initAppRate(Activity hostActivit) {
           appRate = new AppRate(hostActivit);
       }
}
