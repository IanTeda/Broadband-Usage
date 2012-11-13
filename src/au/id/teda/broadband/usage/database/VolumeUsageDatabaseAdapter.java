package au.id.teda.broadband.usage.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class VolumeUsageDatabaseAdapter {
	
	private static final String DEBUG_TAG = "bbusage";
	
	// Set variables for adapter
	public static final String KEY_ROWID = "_id";
	public static final String PERIOD ="period";
	public static final String MONTH = "month";
    public static final String PEAK = "peak";
    public static final String OFFPEAK = "offpeak";
    public static final String UPLOAD = "upload";
    public static final String FREEZONE = "freezone";
    public static final String DATABASE_TABLE = "volume_usage_daily";
    
    private SQLiteDatabase mDatabase;
    private VolumeUsageDbHelper mDbHelper;
    
	// Opens database. If it cannot be opened, try to create a new. If it cannot be created, throw an exception to signal the failure
    public VolumeUsageDatabaseAdapter (Context context){
    	mDbHelper = new VolumeUsageDbHelper(context);
	}
    
    public void open() throws SQLException {
    	mDatabase = mDbHelper.getWritableDatabase();
	}
    
    public void close() {
    	mDbHelper.close();
	}
 	
 	
    
}
