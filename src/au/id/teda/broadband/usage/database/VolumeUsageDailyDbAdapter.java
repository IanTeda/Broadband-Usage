package au.id.teda.broadband.usage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class VolumeUsageDailyDbAdapter {
	
	//private static final String DEBUG_TAG = "bbusage";
	
	// Set variables for adapter
	public static final String KEY_ROWID = "_id";
	public static final String DAY = "day";
	public static final String MONTH = "month";
    public static final String PEAK = "peak";
    public static final String OFFPEAK = "offpeak";
    public static final String UPLOADS = "uploads";
    public static final String FREEZONE = "freezone";
    public static final String TABLE_NAME = "volume_usage_daily";
    
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabbaseHelper;
    
	// Opens database. If it cannot be opened, try to create a new. If it cannot be created, throw an exception to signal the failure
    public VolumeUsageDailyDbAdapter (Context context){
    	mDatabbaseHelper = new DatabaseHelper(context);
	}
    
    public void open() throws SQLException {
    	mDatabase = mDatabbaseHelper.getWritableDatabase();
	}
    
    public void close() {
    	mDatabbaseHelper.close();
	}
 	
 	public Long addEntry (long day, String month, long peak, long offpeak, long uploads, long freezone){
 		// Create a new map of values, where column names are the keys
 		ContentValues entry = new ContentValues();
 		entry.put(DAY, day);
 		entry.put(MONTH, month);
 		entry.put(PEAK, peak);
 		entry.put(OFFPEAK, offpeak);
 		entry.put(UPLOADS, uploads);
 		entry.put(FREEZONE, freezone);
 		
 		// Insert the new row, returning the primary key value of the new row
 		long newRowId;
 		newRowId = mDatabase.insert(TABLE_NAME, null, entry);
 		
 		return newRowId;
 	}
    
}
