package au.id.teda.broadband.dev.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import au.id.teda.broadband.dev.activity.BaseActivity;
import au.id.teda.broadband.dev.util.DailyVolumeUsage;

public class DailyDataTableAdapter {
	
	private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	// Set variables for adapter
	public static final String KEY_ROWID = "_id";
	public static final String ACCOUNT = "account";
	public static final String MONTH = "month";
	public static final String DAY = "day";
    public static final String ANYTIME = "anytime";
    public static final String PEAK = "peak";
    public static final String OFFPEAK = "offpeak";
    public static final String UPLOADS = "uploads";
    public static final String FREEZONE = "freezone";
    public static final String TABLE_NAME = "daily_volume_usage";
    
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabbaseHelper;
    
	/**
	 * VolumeUsageDailyDbAdapter class constructor 
	 * @param context
	 */
    public DailyDataTableAdapter (Context context){
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
 	public Long addReplaceEntry (String userAccount, String month, long day, long anytime, long peak, long offpeak, long uploads, long freezone){
 		String comma = ", ";
 		SQLiteStatement statement = null;
 		
 		String INSERT_STATEMENT = "INSERT OR REPLACE INTO " + TABLE_NAME +
        		" (" + ACCOUNT + comma + MONTH + comma + DAY + comma 
        		+ ANYTIME + comma + PEAK + comma + OFFPEAK + comma + UPLOADS + comma + FREEZONE + ")" +
        		" VALUES (?,?,?,?,?,?,?,?)";
 		
 		statement = mDatabase.compileStatement(INSERT_STATEMENT);
 		statement.bindString(1, userAccount);
        statement.bindString(2, month);
        statement.bindString(3, Long.toString(day));
        statement.bindString(4, Long.toString(anytime));
        statement.bindString(5, Long.toString(peak));
        statement.bindString(6, Long.toString(offpeak));
        statement.bindString(7, Long.toString(uploads));
        statement.bindString(8, Long.toString(freezone));

 		// Insert the new row, returning the primary key value of the new row
 		long newRowId;
 		newRowId = statement.executeInsert();
 		statement.close();
 		
 		return newRowId;
 	}

 	/**
 	 * Return a cursor for a given period
 	 * @param period : yyyyMM string to query database
 	 * @return cursor with period values
 	 * @throws SQLException
 	 */
	public Cursor getPriodUsageCursor (String period) throws SQLException {
		
		SQLiteDatabase database = mDatabbaseHelper.getWritableDatabase();
		
		//Log.d(DEBUG_TAG, "DailyDataDBAdapter > fetchPeriodUsage(): " + period);
		String dbQuery = "SELECT * FROM " + TABLE_NAME
				+ " WHERE " + MONTH
				+ " = '" + period +"';";
		Cursor cursor = database.rawQuery(dbQuery, null);
		return cursor;
	}
	
	/**
	 * Get DailyVolumeUsage[] array for a given period(month)
	 * @param period
	 * @return DailyVolumeUsage[]
	 */
	public DailyVolumeUsage[] getDailyVolumeUsage(String period){
		
		// Open database connection
		open();

        //Log.d(DEBUG_TAG, "getDailyVolumeUsage(" + period + ")" );

		// Get cursor of values from database
		Cursor cursor = getPriodUsageCursor(period);
		
		// Intiate lista array to store cursor
		List<DailyVolumeUsage> usage = new ArrayList<DailyVolumeUsage>();
		
		// Get column numbers for use with cursor
		int COLUMN_INDEX_MONTH = cursor.getColumnIndex(MONTH);
		int COLUMN_INDEX_DAY = cursor.getColumnIndex(DAY);
        int COLUMN_INDEX_ANYTIME = cursor.getColumnIndex(ANYTIME);
		int COLUMN_INDEX_PEAK = cursor.getColumnIndex(PEAK);
		int COLUMN_INDEX_OFFPEAK = cursor.getColumnIndex(OFFPEAK);
		int COLUMN_INDEX_UPLOADS = cursor.getColumnIndex(UPLOADS);
		int COLUMN_INDEX_FREEZONE = cursor.getColumnIndex(FREEZONE);
		
		
		// Iterate cursor and store values in array list
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			
			String month = cursor.getString(COLUMN_INDEX_MONTH);
			Long day = cursor.getLong(COLUMN_INDEX_DAY);
            Long anytime = cursor.getLong(COLUMN_INDEX_ANYTIME);
			Long peak = cursor.getLong(COLUMN_INDEX_PEAK);
			Long offpeak = cursor.getLong(COLUMN_INDEX_OFFPEAK);
			Long uploads = cursor.getLong(COLUMN_INDEX_UPLOADS);
			Long freezone = cursor.getLong(COLUMN_INDEX_FREEZONE);

            /**
            Log.d(DEBUG_TAG, "DailyVolumeUsage > month:" + month +
                    ", day:" + day +
                    ", anytime:" + anytime +
                    ", peak:" + peak +
                    ", offpeak:" + offpeak +
                    ", uploads:" + uploads +
                    ", freezone:" + freezone);
             **/

            usage.add(new DailyVolumeUsage(month, day, anytime, peak, offpeak, uploads, freezone));
			
			cursor.moveToNext();
			
		}
		cursor.close();
		
		// Close database connection
		close();
		
		// Convert list array to an array of DailyVolumeUsage
		DailyVolumeUsage volumeUsage[] = usage.toArray(new DailyVolumeUsage[usage.size()]);
		
		// Return volume dev array
		return volumeUsage;
	}
}
