package au.id.teda.broadband.usage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	// If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "iiNetUsage.db";
    
    private static final String DAILY_USAGE_TABLE_CREATE = 
			"create table " + DailyDataDatabaseAdapter.TABLE_NAME +
			" (" + DailyDataDatabaseAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DailyDataDatabaseAdapter.ACCOUNT + " TEXT NOT NULL, "
			+ DailyDataDatabaseAdapter.MONTH + " TEXT NOT NULL, "
			+ DailyDataDatabaseAdapter.DAY + " INTEGER UNIQUE, "
			+ DailyDataDatabaseAdapter.PEAK + " INTEGER NOT NULL, "
			+ DailyDataDatabaseAdapter.OFFPEAK + " INTEGER NOT NULL, "
			+ DailyDataDatabaseAdapter.UPLOADS + " INTEGER NOT NULL, "
			+ DailyDataDatabaseAdapter.FREEZONE + " INTEGER NOT NULL);";
    
    private static final String HOURLY_USAGE_TABLE_CREATE = 
			"create table " + HourlyDataDatabaseAdapter.TABLE_NAME +
			" (" + HourlyDataDatabaseAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ HourlyDataDatabaseAdapter.ACCOUNT + " TEXT NOT NULL, "
			+ HourlyDataDatabaseAdapter.DAY + " TEXT UNIQUE, "
			+ HourlyDataDatabaseAdapter.HOUR + " TEXT NOT NULL, "
			+ HourlyDataDatabaseAdapter.PEAK + " INTEGER NOT NULL, "
			+ HourlyDataDatabaseAdapter.OFFPEAK + " INTEGER NOT NULL, "
			+ HourlyDataDatabaseAdapter.UPLOADS + " INTEGER NOT NULL, "
			+ HourlyDataDatabaseAdapter.FREEZONE + " INTEGER NOT NULL);";
    
    private static final String UP_TIME_TABLE_CREATE = 
			"create table " + UptimeDatabaseAdapter.TABLE_NAME +
			" (" + UptimeDatabaseAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ UptimeDatabaseAdapter.ACCOUNT + " TEXT NOT NULL, "
			+ UptimeDatabaseAdapter.START + " INTEGER UNIQUE, "
			+ UptimeDatabaseAdapter.FINISH + " INTEGER NOT NULL, "
			+ UptimeDatabaseAdapter.IP + " TEXT NOT NULL);";
    
    private static final String DAILY_USAGE_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + DailyDataDatabaseAdapter.TABLE_NAME;
    
    private static final String HOURLY_USAGE_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + HourlyDataDatabaseAdapter.TABLE_NAME;
    
    private static final String UP_TIME_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + UptimeDatabaseAdapter.TABLE_NAME;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DAILY_USAGE_TABLE_CREATE);
		db.execSQL(HOURLY_USAGE_TABLE_CREATE);
		db.execSQL(UP_TIME_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DAILY_USAGE_DELETE_ENTRIES);
        db.execSQL(HOURLY_USAGE_DELETE_ENTRIES);
        db.execSQL(UP_TIME_DELETE_ENTRIES);
        onCreate(db);
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
