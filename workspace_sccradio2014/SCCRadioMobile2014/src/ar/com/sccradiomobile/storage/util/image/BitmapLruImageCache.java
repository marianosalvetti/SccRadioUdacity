package ar.com.sccradiomobile.storage.util.image;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import ar.com.sccradiomobile.storage.util.Constants;
import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Basic "Least Recently Used" in memory cache implementation. It is fast and
 * does not block I/O. This is the recommended approach.
 * 
 * @author Mariano Salvetti
 */
public class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageCache {

	private static final String TAG = "BitmapLruImageCache";

    public static int getDefaultLruCacheSize() {
           final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
           final int cacheSize = maxMemory / 8;
           return cacheSize;
       }

   public BitmapLruImageCache() {
       this(getDefaultLruCacheSize());
   }

	public BitmapLruImageCache(int maxSize) {
		super(maxSize);
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	public Bitmap getBitmap(String url) {
        if (Constants.DEBUG)	Log.d(TAG, "Retrieved item from Mem Cache");
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
        if (Constants.DEBUG)	Log.d(TAG, "Added item to Mem Cache");
		put(url, bitmap);
	}
}
