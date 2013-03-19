package au.id.teda.broadband.usage.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class UptimeDatabaseAdapter {
	
	//private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	// Set variables for adapter
	public static final String KEY_ROWID = "_id";
	public static final String ACCOUNT = "account";
	public static final String START = "start_date_time";
	public static final String FINISH = "finish_date_time";
    public static final String IP = "ip_address";
    public static final String TABLE_NAME = "up_time_ip";
    
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabbaseHelper;
    
	/**
	 * VolumeUsageDailyDbAdapter class constructor 
	 * @param context
	 */
    public UptimeDatabaseAdapter (Context context){
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
 	
 	public Long addReplaceEntry (String userAccount, long start, long finish, String ip){
 		String comma = ", ";
 		SQLiteStatement statement = null;
 		
 		String INSERT_STATEMENT = "INSERT OR REPLACE INTO " + TABLE_NAME +
        		" (" + ACCOUNT + comma + START + comma + FINISH + comma + IP + ")" +
        		" VALUES (?,?,?,?)";
 		
 		statement = mDatabase.compileStatement(INSERT_STATEMENT);
 		statement.bindString(1, userAccount);
        statement.bindString(2, Long.toString(start));
        statement.bindString(3, Long.toString(finish));
        statement.bindString(4, ip);
 		
 		// Insert the new row, returning the primary key value of the new row
 		long newRowId;
 		newRowId = statement.executeInsert();
 		statement.close();
 		
 		return newRowId;
 	}
}
