package au.id.teda.broadband.usage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	// If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "iiNetUsage.db";
    
    private static final String VOLUME_USAGE_TABLE_CREATE = 
			"create table " + DailyDataDatabaseAdapter.TABLE_NAME +
			" (" + DailyDataDatabaseAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DailyDataDatabaseAdapter.ACCOUNT + " INTEGER NOT NULL, "
			+ DailyDataDatabaseAdapter.DAY + " INTEGER UNIQUE, "
			+ DailyDataDatabaseAdapter.MONTH + " TEXT NOT NULL, "
			+ DailyDataDatabaseAdapter.PEAK + " INTEGER NOT NULL, "
			+ DailyDataDatabaseAdapter.OFFPEAK + " INTEGER NOT NULL, "
			+ DailyDataDatabaseAdapter.UPLOADS + " INTEGER NOT NULL, "
			+ DailyDataDatabaseAdapter.FREEZONE + " INTEGER NOT NULL);";
    
    private static final String VOLUME_USAGE_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + DailyDataDatabaseAdapter.TABLE_NAME;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(VOLUME_USAGE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(VOLUME_USAGE_DELETE_ENTRIES);
        onCreate(db);
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
