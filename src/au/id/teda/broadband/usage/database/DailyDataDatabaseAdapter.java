package au.id.teda.broadband.usage.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DailyDataDatabaseAdapter {
	
	//private static final String DEBUG_TAG = "bbusage";
	
	// Set variables for adapter
	public static final String KEY_ROWID = "_id";
	public static final String ACCOUNT = "account";
	public static final String DAY = "day";
	public static final String MONTH = "month";
    public static final String PEAK = "peak";
    public static final String OFFPEAK = "offpeak";
    public static final String UPLOADS = "uploads";
    public static final String FREEZONE = "freezone";
    public static final String TABLE_NAME = "volume_usage_daily";
    
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabbaseHelper;
    
	/**
	 * VolumeUsageDailyDbAdapter class constructor 
	 * @param context
	 */
    public DailyDataDatabaseAdapter (Context context){
    	mDatabbaseHelper = new DatabaseHelper(context);
	}
    
    /**
     * Open database. If it cannot be opened, try to create a new. 
     * If it cannot be created, throw an exception to signal the failure
     * @throws SQLException
     */
    public void open() throws SQLException {
    	mDatabase = mDatabbaseHelper.getWritableDatabase();
	}
    
    /**
     * Close database
     */
    public void close() {
    	mDatabbaseHelper.close();
	}
 	
    /**
     * Add entry or replace if exists
     * @param userAccount
     * @param day
     * @param month
     * @param peak
     * @param offpeak
     * @param uploads
     * @param freezone
     * @return Row ID 
     */
 	public Long addReplaceEntry (String userAccount, long day, String month, long peak, long offpeak, long uploads, long freezone){
 		String comma = ", ";
 		SQLiteStatement statement = null;
 		
 		String INSERT_STATEMENT = "INSERT OR REPLACE INTO " + TABLE_NAME +
        		" (" + ACCOUNT + comma + DAY + comma + MONTH + comma 
        		+ PEAK + comma + OFFPEAK + comma + UPLOADS + comma + FREEZONE + ")" +
        		" VALUES (?,?,?,?,?,?,?)";
 		
 		statement = mDatabase.compileStatement(INSERT_STATEMENT);
 		statement.bindString(1, userAccount);
        statement.bindString(2, Long.toString(day));
        statement.bindString(3, month);
        statement.bindString(4, Long.toString(peak));
        statement.bindString(5, Long.toString(offpeak));
        statement.bindString(6, Long.toString(uploads));
        statement.bindString(7, Long.toString(freezone));
 		
 		// Insert the new row, returning the primary key value of the new row
 		long newRowId;
 		newRowId = statement.executeInsert();
 		statement.close();
 		
 		return newRowId;
 	}
 	
	// Return a cursor for a given period
	public Cursor getPriodUsageCursor (String period) throws SQLException {
		
		SQLiteDatabase database = mDatabbaseHelper.getWritableDatabase();
		
		//Log.d(DEBUG_TAG, "DailyDataDBAdapter > fetchPeriodUsage(): " + period);
		String dbQuery = "SELECT * FROM " + TABLE_NAME
				+ " WHERE " + MONTH
				+ " = '" + period +"';";
		Cursor cursor = database.rawQuery(dbQuery, null);
		return cursor;
	}
    
}
