package au.id.teda.broadband.usage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class VolumeUsageDbHelper extends SQLiteOpenHelper {
	
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VolumeUsage.db";
    
    public static final String VOLUME_USAGE_TABLE_CREATE = 
			"create table " + VolumeUsageDatabaseAdapter.DATABASE_TABLE +
			" (" + VolumeUsageDatabaseAdapter.KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ VolumeUsageDatabaseAdapter.PERIOD + " INTEGER UNIQUE, "
			+ VolumeUsageDatabaseAdapter.MONTH + " TEXT NOT NULL, "
			+ VolumeUsageDatabaseAdapter.PEAK + " INTEGER NOT NULL, "
			+ VolumeUsageDatabaseAdapter.OFFPEAK + " INTEGER NOT NULL, "
			+ VolumeUsageDatabaseAdapter.UPLOAD + " INTEGER NOT NULL, "
			+ VolumeUsageDatabaseAdapter.FREEZONE + " INTEGER NOT NULL);";
    
    public static final String VOLUME_USAGE_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + VolumeUsageDatabaseAdapter.DATABASE_TABLE;

	public VolumeUsageDbHelper(Context context) {
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
