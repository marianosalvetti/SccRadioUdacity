package ar.com.sccradiomobile.storage.util;

import android.app.AlarmManager;

/**
 * Set of useful constants.
 * 
 * @author Mariano Salvetti
 */
public class Constants {


	/**
	 * Constants values to schedule an update.
	 */
	public interface ScheduleUpdate {
	//	public static final long INTERVAL = 30000L;
		public static final long INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
		
		public static final long TRIGGER_AT_TIME = System.currentTimeMillis() + INTERVAL;
	}

    // Others utils constants....
    public static final String ID = "id";
    public static final String POSITION = "position";
    public static final boolean DEBUG = true;

    public static final String CATEGORIA = "categoria_configurada";
    public static final String CATEGORIA_TNA = "tna";
    public static final String CATEGORIA_FILAFILO = "pirufilafilo";

}
