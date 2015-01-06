package ar.com.sccradiomobile.utils;

/**
 * Created by Mariano Salvetti
 *
 * A lot of method that we use in different sections.
 */

import android.content.Context;
import android.util.Log;
import ar.com.sccradiomobile.BuildConfig;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public final class DateUtils {

	/**
     * Time zone to use when formatting all session times. To always use the
     * phone local time, use {@link TimeZone#getDefault()}.
     */
	public static final TimeZone CONFERENCE_TIME_ZONE = TimeZone.getTimeZone("America/Los_Angeles");
	public static final String PM_VALUE = "PM";
	public static final String AM_VALUE = "AM";
	public static final String STRING_SPACE = " ";
	public static final String FORMAT_DAYTIME_SERVER = "MM/dd/yyyy hh:mm a";

	public static final int SECOND_MILLIS = 1000;
	public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
	public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
	public static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static final long sAppLoadTime = System.currentTimeMillis();
    private static final DateFormat dateFormatYouTube = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat outputFormatYouTube = new SimpleDateFormat("dd/M/yyyy");
	/**
	 * @param dateParameter
	 * @param dayToUse
	 * @return the date to use , but in Millis (long)
	 */
	public static final long getDateToUseInMillis(String dateParameter, String dayToUse) {
		long startMillis;
		if (dateParameter.contains(DateUtils.PM_VALUE))
			dateParameter = dateParameter.replace(DateUtils.PM_VALUE, STRING_SPACE + DateUtils.PM_VALUE);
		else if (dateParameter.contains(DateUtils.AM_VALUE))
			dateParameter = dateParameter.replace(DateUtils.AM_VALUE, STRING_SPACE + DateUtils.AM_VALUE);
		String starTimeToUse = dayToUse + STRING_SPACE + dateParameter;
//		Log.d(logTag," |---> Date and Time to Use  = " + starTimeToUse );
		startMillis = DateUtils.stringCalendarDateToDateGTM(starTimeToUse,DateUtils.FORMAT_DAYTIME_SERVER).getTime();
		return startMillis;
	}

	/**
	 * Parse any string date with given format or default "yyyy-MM-dd'T'HH:mm:ss" into a Date element
	 * if there is any error in the parser process this method launch an exception,
	 * the returned element has GTM time
	 * and returns null.
	 * @param dateString date as string
	 * @param format String format of given String date
	 * @return GTM Date corresponding to the given String date
	 */
	public static Date stringCalendarDateToDateGTM(String dateString, String format) {
		Date date = null;
		SimpleDateFormat dformat;
		if (format==null)
			dformat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		else
			dformat = new SimpleDateFormat(format);
		try {
			date = dformat.parse(dateString);
		} catch (ParseException parseException) {
			parseException.printStackTrace();
			return null;
		}
		return date;
	}

	/**
	 * Parse any string date with given or default "yyyy-MM-dd'T'HH:mm:ss" format into a Date element
	 * if there is any error in the parser process this method launch an exception,
	 * the returned element has device Locale time
	 * and returns null.
	 * @param dateString date as string
	 * @param format String format of given String date
	 * @return Locale Date corresponding to the given String date
	 */
	public static Date stringCalendarDateToDateLocale(String dateString, String format) {
		Date date = null;
		SimpleDateFormat dformat;
		if (format==null)
			dformat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		else
			dformat = new SimpleDateFormat(format);
		try {
				date = dformat.parse(dateString);

		} catch (ParseException parseException) {
			parseException.printStackTrace();
			return null;
		}

		TimeZone tm = TimeZone.getDefault();
	//	TimeZone tm = TimeZone.getTimeZone("America/Los_Angeles");
		tm.setID(Locale.getDefault().getISO3Country());
		date = new Date(date.getTime()+tm.getOffset(date.getTime()));

		return date;
	}

	/**
	 * Parse any date into given string or default "yyyy-MM-dd'T'HH:mm:ss" format
	 * @param date Date object we want to transform into String
	 * @param format String format of desired String date
	 * @return String date corresponding with given String Date object
	 */
	public static String dateToStringCalendarDate(Date date, String format){
		SimpleDateFormat dformat;
		if (format==null)
			dformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		else
			dformat = new SimpleDateFormat(format);
		if (date != null) {
			return dformat.format(date);
		} else {
			return "NOT VALID STRING FORMAT";
		}
	}


    public static long getCurrentTime(final Context context) {
        if (BuildConfig.DEBUG) {
            return context.getSharedPreferences("mock_data", Context.MODE_PRIVATE)
                    .getLong("mock_current_time", System.currentTimeMillis())
                    + System.currentTimeMillis() - sAppLoadTime;
        } else {
            return System.currentTimeMillis();
        }
    }

    public static String getDateFormatedYoutube(String uploaded) {

           String dateUploaded = null;
           String dateString = uploaded.replace(".000Z", " ");
           Date date = null;
           try {
                 date = dateFormatYouTube.parse(dateString);
           } catch (ParseException e) {
               Log.d("lnb_mobile", "ParseException = " + e.toString());
           }

           dateUploaded  = outputFormatYouTube.format(date);
           return dateUploaded;
       }
}
