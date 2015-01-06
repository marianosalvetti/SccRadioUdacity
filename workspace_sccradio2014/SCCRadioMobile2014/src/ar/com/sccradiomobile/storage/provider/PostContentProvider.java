package ar.com.sccradiomobile.storage.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import ar.com.sccradiomobile.databases.PostDatabaseHelper;
import ar.com.sccradiomobile.databases.PostTable;

import java.util.Arrays;
import java.util.HashSet;


/**
 * Custom {@link android.content.ContentProvider} that encapsulates the data, and provides
 * mechanisms for defining data security.
 *
 * @author Mariano Salvetti
 */
public class PostContentProvider extends ContentProvider {

	private PostDatabaseHelper databaseHelper;

	// Used for the UriMatcher
	private static final int POSTS = 30;
	private static final int POST_ID = 40;

	private static final String AUTHORITY = "ar.com.sccradiomobile.provider";

	private static final String BASE_PATH = "posts";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/posts";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/post";

	/**
	 * {@link android.content.UriMatcher} to determine what is requested to this
	 * {@link android.content.ContentProvider}.
	 */
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, POSTS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", POST_ID);
	}

	@Override
	public boolean onCreate() {
		databaseHelper = new PostDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {

		// Using SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// check if the caller has requested a column which does not exists
		checkColumns(projection);

		// Set the table
		queryBuilder.setTables(PostTable.TABLE_POST);

		int uriType = sURIMatcher.match(uri);

		switch (uriType) {
            case POSTS:
                break;
            case POST_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(PostTable.COLUMN_ID + "="+ uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		Cursor cursor = queryBuilder.query(db, projection, selection,selectionArgs, null, null, sortOrder);

		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);

		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		long id = 0;

		switch (uriType) {
            case POSTS:
                id = db.insert(PostTable.TABLE_POST, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// Notify that the content has been modified
		getContext().getContentResolver().notifyChange(uri, null);

		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);

		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		int rowsUpdated = 0;

		switch (uriType) {
            case POSTS:
                rowsUpdated = db.update(PostTable.TABLE_POST, values, selection,selectionArgs);
                break;

            case POST_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(PostTable.TABLE_POST, values,
                            PostTable.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = db.update(PostTable.TABLE_POST, values,
                            PostTable.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// Notify that the content has been modified
		getContext().getContentResolver().notifyChange(uri, null);

		return rowsUpdated;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);

		SQLiteDatabase db = databaseHelper.getWritableDatabase();

		int rowsDeleted = 0;

		switch (uriType) {
            case POSTS:
                rowsDeleted = db.delete(PostTable.TABLE_POST, selection,selectionArgs);
                break;
            case POST_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(PostTable.TABLE_POST,PostTable.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = db.delete(PostTable.TABLE_POST,
                            PostTable.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// Notify that the content has been modified
		getContext().getContentResolver().notifyChange(uri, null);

		return rowsDeleted;
	}

	/**
	 * Validate that a query only requests valid columns.
	 * 
	 * @param projection
	 */
	private void checkColumns(String[] projection) {
		String[] available = { PostTable.COLUMN_ID, PostTable.COLUMN_TYPE,
		            PostTable.COLUMN_PHOTO,PostTable.COLUMN_URL,PostTable.COLUMN_STATUS,
		            PostTable.COLUMN_TITLE,PostTable.COLUMN_TITLE_PLAIN,PostTable.COLUMN_CONTENT,
		            PostTable.COLUMN_EXCERPT,PostTable.COLUMN_AUTOR,PostTable.COLUMN_DATE,PostTable.COLUMN_CATEGORIA};
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			// check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection.");
			}
		}
	}
}
