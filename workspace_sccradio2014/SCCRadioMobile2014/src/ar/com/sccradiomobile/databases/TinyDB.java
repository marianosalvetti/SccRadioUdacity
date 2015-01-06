package ar.com.sccradiomobile.databases;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import ar.com.sccradiomobile.storage.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Mariano Salvetti
 *
 * This class simplifies calls to SharedPreferences in a line of code.
 * It can also do more like: saving a list of Strings, ints. and Saving images.
 * All in 1 line of code!  example usage:

     TinyDB tinydb = new TinyDB(context);

     tinydb.putInt("clickCount", 2);
     tinydb.putString("userName", "john");
     tinydb.putBoolean("isUserMale", true);
     tinydb.putList("MyUsers", mUsersArray);
     tinydb.putImagePNG("DropBox/WorkImages", "MeAtlunch.png", lunchBitmap);

 These plus the corresponding get methods are all included
 This is just an exmple of how easy it is to use. there are many more usefull methods included in the class. Enjoy :)
 */

public class TinyDB {

    Context mContext;
	static SharedPreferences preferences;
	String DEFAULT_APP_IMAGEDATA_DIRECTORY;
	File mFolder = null;
	String lastImagePath = "";

	public TinyDB(Context appContext) {
		this.mContext = appContext;
		preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
	}

	public Bitmap getImage(String path) {
		Bitmap theGottenBitmap = null;
		try {
			theGottenBitmap = BitmapFactory.decodeFile(path);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return theGottenBitmap;
	}

	/**
	 * Returns the String path of the last image that was saved with this Object
	 * <p>
	 *
	 */
	public String getSavedImagePath() {
		return lastImagePath;
	}

	/**
	 * Returns the String path of the last image that was saved with this
	 * tnydbobj
	 * <p>
	 *
	 * @param theFolder
	 *            the theFolder - the folder path dir you want to save it to e.g
	 *            "DropBox/WorkImages"
	 * @param theImageName
	 *            the theImageName - the name you want to assign to the image file e.g
	 *            "MeAtlunch.png"
	 *
	 */
	public String putImagePNG(String theFolder, String theImageName,Bitmap theBitmap) {
		this.DEFAULT_APP_IMAGEDATA_DIRECTORY = theFolder;
		String mFullPath = setupFolderPath(theImageName);
		saveBitmapPNG(mFullPath, theBitmap);
		lastImagePath = mFullPath;
		return mFullPath;
	}

	private String setupFolderPath(String imageName) {
		File sdcard_path = Environment.getExternalStorageDirectory();
		mFolder = new File(sdcard_path, DEFAULT_APP_IMAGEDATA_DIRECTORY);
		if (!mFolder.exists()) {
			if (!mFolder.mkdirs()) {
                if (Constants.DEBUG) Log.e("While creatingsave path","Default Save Path Creation Error");
				// Toast("Default Save Path Creation Error");
			}
		}
		String savePath = mFolder.getPath() + '/' + imageName;
		return savePath;
	}

	private boolean saveBitmapPNG(String strFileName, Bitmap bitmap) {
		if (strFileName == null || bitmap == null)
			return false;

		boolean bSuccess1 = false;
		boolean bSuccess2;
		boolean bSuccess3;
		File saveFile = new File(strFileName);

		if (saveFile.exists()) {
			if (!saveFile.delete())
				return false;
		}

		try {
			bSuccess1 = saveFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		OutputStream out = null;
		try {
			out = new FileOutputStream(saveFile);
			bSuccess2 = bitmap.compress(CompressFormat.PNG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
			bSuccess2 = false;
		}
		try {
			if (out != null) {
				out.flush();
				out.close();
				bSuccess3 = true;
			} else
				bSuccess3 = false;

		} catch (IOException e) {
			e.printStackTrace();
			bSuccess3 = false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return (bSuccess1 && bSuccess2 && bSuccess3);
	}

	public static int getInt(String key) {

		return preferences.getInt(key, 0);
	}

	public static String getString(String key) {

		return preferences.getString(key, "");
	}

	public static void putInt(String key, int value) {

		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void putString(String key, String value) {

		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void putList(String key, ArrayList<String> marray) {

		SharedPreferences.Editor editor = preferences.edit();
		String[] mystringlist = marray.toArray(new String[marray.size()]);
		// the comma like character used below is not a comma it is the SINGLE
		// LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
		// seprating the items in the list
		editor.putString(key, TextUtils.join("‚‗‚", mystringlist));
		editor.commit();
	}

	public static ArrayList<String> getList(String key) {

		// the comma like character used below is not a comma it is the SINGLE
		// LOW-9 QUOTATION MARK unicode 201A and unicode 2017 they are used for
		// seprating the items in the list
		String[] mylist = TextUtils
				.split(preferences.getString(key, ""), "‚‗‚");
		ArrayList<String> gottenlist = new ArrayList<String>(
				Arrays.asList(mylist));
		return gottenlist;
	}

	public static void putBoolean(String key, Boolean mboolean) {

		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, mboolean);
		editor.commit();
	}

	public static Boolean getBoolean(String key) {

		return preferences.getBoolean(key, false);
	}

    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static Long getLong(String key) {
        return preferences.getLong(key, 0);
    }
}