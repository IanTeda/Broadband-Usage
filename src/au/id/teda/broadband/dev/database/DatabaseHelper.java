package au.id.teda.broadband.dev.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	// Debug tag
	//private final String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	// If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "iiNetUsage.db";
    
    private static final String DAILY_USAGE_TABLE_CREATE = 
			"create table " + DailyDataTableAdapter.TABLE_NAME +
			" (" + DailyDataTableAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DailyDataTableAdapter.ACCOUNT + " TEXT NOT NULL, "
			+ DailyDataTableAdapter.MONTH + " TEXT NOT NULL, "
			+ DailyDataTableAdapter.DAY + " INTEGER UNIQUE, "
			+ DailyDataTableAdapter.PEAK + " INTEGER NOT NULL, "
			+ DailyDataTableAdapter.OFFPEAK + " INTEGER NOT NULL, "
			+ DailyDataTableAdapter.UPLOADS + " INTEGER NOT NULL, "
			+ DailyDataTableAdapter.FREEZONE + " INTEGER NOT NULL);";
    
    private static final String HOURLY_USAGE_TABLE_CREATE = 
			"create table " + HourlyDataTableAdapter.TABLE_NAME +
			" (" + HourlyDataTableAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ HourlyDataTableAdapter.ACCOUNT + " TEXT NOT NULL, "
			+ HourlyDataTableAdapter.DAY + " TEXT UNIQUE, "
			+ HourlyDataTableAdapter.HOUR + " TEXT NOT NULL, "
			+ HourlyDataTableAdapter.PEAK + " INTEGER NOT NULL, "
			+ HourlyDataTableAdapter.OFFPEAK + " INTEGER NOT NULL, "
			+ HourlyDataTableAdapter.UPLOADS + " INTEGER NOT NULL, "
			+ HourlyDataTableAdapter.FREEZONE + " INTEGER NOT NULL);";
    
    private static final String UP_TIME_TABLE_CREATE = 
			"create table " + UptimeTableAdapter.TABLE_NAME +
			" (" + UptimeTableAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ UptimeTableAdapter.ACCOUNT + " TEXT NOT NULL, "
			+ UptimeTableAdapter.START + " INTEGER UNIQUE, "
			+ UptimeTableAdapter.FINISH + " INTEGER NOT NULL, "
			+ UptimeTableAdapter.IP + " TEXT NOT NULL);";
    
    private static final String HISTORICAL_TABLE_CREATE = 
			"create table " + HistoricalMonthsTableAdapter.TABLE_NAME +
			" (" + HistoricalMonthsTableAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ HistoricalMonthsTableAdapter.ACCOUNT + " TEXT NOT NULL, "
			+ HistoricalMonthsTableAdapter.MONTH + " INTEGER UNIQUE); ";
    
    private static final String DAILY_USAGE_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + DailyDataTableAdapter.TABLE_NAME;
    
    private static final String HOURLY_USAGE_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + HourlyDataTableAdapter.TABLE_NAME;
    
    private static final String UP_TIME_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + UptimeTableAdapter.TABLE_NAME;
    
    private static final String HISTORICAL_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + HistoricalMonthsTableAdapter.TABLE_NAME;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DAILY_USAGE_TABLE_CREATE);
		db.execSQL(HOURLY_USAGE_TABLE_CREATE);
		db.execSQL(UP_TIME_TABLE_CREATE);
		db.execSQL(HISTORICAL_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DAILY_USAGE_DELETE_ENTRIES);
        db.execSQL(HOURLY_USAGE_DELETE_ENTRIES);
        db.execSQL(UP_TIME_DELETE_ENTRIES);
        db.execSQL(HISTORICAL_DELETE_ENTRIES);
        onCreate(db);
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
