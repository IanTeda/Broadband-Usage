package au.id.teda.broadband.usage.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class AccountInfoHelper {
	
	private static final String DEBUG_TAG = "bbusage";
	
	// Set static string values for preference keys
	private final static String PLAN = "plan";
	private final static String PRODUCT = "product";
	private final static String OFF_PEAK_START = "offPeakStart";
	private final static String OFF_PEAK_END = "offPeakEnd";
	private final static String PEAK_QUOTA = "peakDataQuota";
	private final static String OFF_PEAK_QUOTA = "offPeakDataQuota";

	// Activity context
    private static Context mContext;
    
    // Activity shared preferences
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;
    
    // Class constructor
    public AccountInfoHelper(Context context) {
    	AccountInfoHelper.mContext = context;
    	mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
    	mEditor = mSettings.edit();
    }
    
    public void setAccountInfo(String plan, String product,
			long offpeakStartTime, long offpeakEndTime, long peakQuota,
			long offpeakQuota){
		
    	mEditor.putString(PLAN, plan);
    	mEditor.putString(PRODUCT, product);
    	mEditor.putLong(OFF_PEAK_START, offpeakStartTime);
    	mEditor.putLong(OFF_PEAK_END, offpeakEndTime);
    	mEditor.putLong(PEAK_QUOTA, peakQuota);
    	mEditor.putLong(OFF_PEAK_QUOTA, offpeakQuota);
    	
    	// Commit values to preferences
    	mEditor.commit();
	}
    
    /**
	 * Method for checking if all account information exists
	 * @return true if all data present
	 */
	public boolean infoExists() {
		// Check to see if we have all the account information stored
		if (getPlan().length() > 0
				&& getProduct().length() >0
				&& getOffpeakStart().length() > 0
				&& getOffpeakEnd().length() > 0
				&& getPeakQuota() > 0
				&& getOffpeakQuota() > 0){
			
			// Looks like it does so lets return true
			return true;
		} else {
			
			// Doesn't seem to be all there so return false
			return false;
		}
	}
	
	/**
	 * Method for getting account plan
	 * @return shared preference string
	 */
	public String getPlan(){
		return mSettings.getString(PLAN, "");
	}
	
	/**
	 * Check if plan has been set
	 * @return true if plan string length is greater then 0
	 */
	public boolean isPlanSet(){
		// Check plan string length is greater then 0
		if (getPlan().length() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
			
	}
	
	/**
	 * Method for getting account product
	 * @return shared preference string
	 */
	public String getProduct(){
		return mSettings.getString(PRODUCT, "");
	}
	
	
	/**
	 * Check if product has been set
	 * @return true if product string length is greater then 0
	 */
	public boolean isProductSet(){
		// Check product string length is greater then 0
		if (getProduct().length() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
			
	}
	
	/**
	 * Method for getting off peak start time
	 * @return shared preference string
	 */
	public String getOffpeakStart(){
		return mSettings.getString(OFF_PEAK_START, "");
	}
	
	/**
	 * Check if off peak start time is set
	 * @return true of string length is greater then 0
	 */
	public boolean isOffpeakStartSet(){
		// Check if string length is greater then 0
		if (getOffpeakStart().length() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}
	
	
	/**
	 * Method for getting off peak end time
	 * @return shared preference string
	 */
	public String getOffpeakEnd(){
		return mSettings.getString(OFF_PEAK_END, "");
	}
	
	/**
	 * Check if off peak start time is set
	 * @return true of string length is greater then 0
	 */
	public boolean isOffpeakEndSet(){
		// Check if string length is greater then 0
		if (getOffpeakEnd().length() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}
	
	/**
	 * Method for getting peak quota value
	 * @return shared preference Long
	 */
	public Long getPeakQuota(){
		return mSettings.getLong(PEAK_QUOTA, 0);
	}
	
	/**
	 * Method for checking if peak quota has been set
	 * @return true if long is greater then 0
	 */
	public boolean isPeakQuotaSet(){
		// Check Long value length is greater then 0
		if (getPeakQuota() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}
	
	/**
	 * Method for getting off peak quota vale
	 * @return shared preference Long
	 */
	public Long getOffpeakQuota(){
		return mSettings.getLong(OFF_PEAK_QUOTA, 0);
	}
	
	/**
	 * Method for checking if off peak quota has been set
	 * @return true if long is greater then 0
	 */
	public boolean isOffpeakQuotaSet(){
		// Check Long value length is greater then 0
		if (getOffpeakQuota() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}

}
