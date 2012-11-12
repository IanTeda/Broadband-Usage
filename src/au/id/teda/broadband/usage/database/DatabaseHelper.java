package au.id.teda.broadband.usage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VolumeUsage.db";
    
    public static final String VOLUME_USAGE_CREATE_ENTRIES = 
			"create table " + VolumeUsageDataBaseAdapter.DATABASE_TABLE +
			" (" + VolumeUsageDataBaseAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ VolumeUsageDataBaseAdapter.PERIOD + " INTEGER UNIQUE, "
			+ VolumeUsageDataBaseAdapter.MONTH + " TEXT NOT NULL, "
			+ VolumeUsageDataBaseAdapter.PEAK + " INTEGER NOT NULL, "
			+ VolumeUsageDataBaseAdapter.OFFPEAK + " INTEGER NOT NULL, "
			+ VolumeUsageDataBaseAdapter.UPLOAD + " INTEGER NOT NULL, "
			+ VolumeUsageDataBaseAdapter.FREEZONE + " INTEGER NOT NULL);";
    
    public static final String VOLUME_USAGE_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + VolumeUsageDataBaseAdapter.DATABASE_TABLE;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(VOLUME_USAGE_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(VOLUME_USAGE_DELETE_ENTRIES);
        onCreate(db);
	}

}
