package ar.com.sccradiomobile.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.*;

public class Utils {

    private static final String conAcentos = "áéíóú";
    private static final String sinAcentos = "aeiou";

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static void closeStreamQuietly(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            // ignore exception
        }
    }

    /**
     * Convertimos un Stream a una String y asi la mostramos en una activity
     *
     * @param is The {@code InputStream} object to read from
     * @return A {@code String} object representing the string for of the input
     * @throws java.io.IOException Thrown on read failure from the input
     */
    public static String inputStreamToString(InputStream is) {
        StringBuffer sBuffer = new StringBuffer();
        DataInputStream dataIO = new DataInputStream(is);
        String strLine = null;
        try {
            while ((strLine = dataIO.readLine()) != null) {
                sBuffer.append(strLine);
            }
            dataIO.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sBuffer.toString();
    }

    /**
     * Gets the version name of the application
     *
     * @return
     */
    public static String bundleVersion(Activity activity) {
        try {
            PackageInfo info = activity.getApplicationContext().getPackageManager().getPackageInfo(activity.getApplicationContext().getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static int getAppVersionCode() {
        return android.os.Build.VERSION.SDK_INT;
    }


    /**
     * Checks if there is network connection.
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static String removerAcentos(String s) {
        for (int i = 0; i < conAcentos.length(); i++)
            s = s.replace(conAcentos.charAt(i), sinAcentos.charAt(i));
        return s;
    }

    private static void checkStorage()
    	{
            boolean externalStorageAvailable;
            boolean externalStorageWriteable;
    		String state = Environment.getExternalStorageState();

    		if (Environment.MEDIA_MOUNTED.equals(state))
    		{
    			// We can read and write the media
    			externalStorageAvailable = externalStorageWriteable = true;
    		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
    		{
    			// We can only read the media
    			externalStorageAvailable = true;
    			externalStorageWriteable = false;
    		} else
    		{
    			// Something else is wrong. It may be one of many other states, but
    			// all we need
    			// to know is we can neither read nor write
    			externalStorageAvailable = externalStorageWriteable = false;
    		}
    	}

    /*	private static void createDir()
    	{
    		if (externalStorageAvailable && externalStorageWriteable)
    		{
    			File rememberDir = new File(Settings.getExtStorageDirectory(), Settings.getRememberDir());
    			rememberDir.mkdirs();
    		}
    	}
    */
}
