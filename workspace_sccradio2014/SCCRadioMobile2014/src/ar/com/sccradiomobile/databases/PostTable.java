package ar.com.sccradiomobile.databases;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ar.com.sccradiomobile.storage.util.Constants;

/**
 * Table of Post.
 * <p>
 * IMPORTANT: Have one separate class per table as best practice. This way we
 * are prepared in case our database schema grows.
 * 
 * @author Mariano Salvetti
 */
public class PostTable {

	public static final String TABLE_POST = "post";
	public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TITLE_PLAIN = "title_plain";
    public static final String COLUMN_CONTENT= "content";
	public static final String COLUMN_EXCERPT = "excerpt";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_AUTOR = "author";
	public static final String COLUMN_CATEGORIA = "categoria";

	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_POST
			+ "(" + COLUMN_ID + " INTEGER PRIMARY KEY , "
            + COLUMN_TYPE + " TEXT NOT NULL, "
            + COLUMN_PHOTO + " TEXT NULL, "
            + COLUMN_URL + " TEXT NOT NULL, "
            + COLUMN_STATUS + " TEXT NOT NULL, "
            + COLUMN_TITLE + " TEXT NOT NULL, "
            + COLUMN_TITLE_PLAIN + " TEXT NOT NULL, "
            + COLUMN_CONTENT + " TEXT NULL, "
            + COLUMN_EXCERPT + " TEXT NULL, "
            + COLUMN_AUTOR + " TEXT NULL, "
            + COLUMN_CATEGORIA + " TEXT NULL, "
            + COLUMN_DATE + " TEXT NOT NULL);";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if (Constants.DEBUG)	Log.d("sccradio_mobile", "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy the old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);
		onCreate(database);
	}

    private static final String[] projection = new String[]{PostTable.COLUMN_ID, PostTable.COLUMN_TYPE,
            PostTable.COLUMN_PHOTO,PostTable.COLUMN_URL,PostTable.COLUMN_STATUS,
            PostTable.COLUMN_TITLE,PostTable.COLUMN_TITLE_PLAIN,PostTable.COLUMN_CONTENT,
            PostTable.COLUMN_EXCERPT,PostTable.COLUMN_AUTOR,PostTable.COLUMN_DATE, PostTable.COLUMN_CATEGORIA};

     public static String[] getProjection() {
            return projection;
     }
}