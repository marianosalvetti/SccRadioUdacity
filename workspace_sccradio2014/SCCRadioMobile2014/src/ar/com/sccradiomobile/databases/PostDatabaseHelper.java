package ar.com.sccradiomobile.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to create the database schema.
 * 
 * @author Mariano Salvetti
 */
public class PostDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sccradiomobile.db";
	private static final int DATABASE_VERSION = 2;

	public PostDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// This method is called during creation of the database
	    PostTable.onCreate(database);

		// IMPORTANT: If we have more tables, then call its onCreate method
		// here.
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,int newVersion) {
		// Method is called during an upgrade of the database,
		// e.g. if you increase the database version manually
        PostTable.onUpgrade(database, oldVersion, newVersion);

		// IMPORTANT: If we have more tables, then call its onUpgrade method
		// here.
	}

    public static void deleteDatabase(Context context) {
    		context.deleteDatabase(DATABASE_NAME);
    	}

}
