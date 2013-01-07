package au.id.teda.broadband.usage.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import au.id.teda.broadband.usage.ui.MainActivity;

public class AccountInfoHelper {
	
	private static final String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// Set static string values for preference keys
	private final static String PREF_ACCOUNT_KEY = "pref_account_key";
	private final static String PREF_PLAN_KEY = "pref_plan_key";
	private final static String PREF_PRODUCT_KEY = "pref_product";
	private final static String PREF_OFFPEAK_START_KEY = "pref_offpeak_start";
	private final static String PREF_OFFPEAK_END_KEY = "pref_offpeak_end";
	private final static String PREF_PEAK_QUOTA_KEY = "pref_peak_quota";
	private final static String PREF_OFFPEAK_QUOTA_KEY = "pref_offpeak_quota";
	
	private final static long GB = 1000000000;

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
    
    public void setAccountInfo(String userAccount, String plan, String product,
			long offpeakStartTime, long offpeakEndTime, long peakQuota,
			long offpeakQuota){
		
    	mEditor.putString(PREF_ACCOUNT_KEY, userAccount);
    	mEditor.putString(PREF_PLAN_KEY, plan);
    	mEditor.putString(PREF_PRODUCT_KEY, product);
    	mEditor.putLong(PREF_OFFPEAK_START_KEY, offpeakStartTime);
    	mEditor.putLong(PREF_OFFPEAK_END_KEY, offpeakEndTime);
    	mEditor.putLong(PREF_PEAK_QUOTA_KEY, peakQuota);
    	mEditor.putLong(PREF_OFFPEAK_QUOTA_KEY, offpeakQuota);
    	
    	// Commit values to preferences
    	mEditor.commit();
	}
    
    /**
	 * Method for checking if all account information exists
	 * @return true if all data present
	 */
	public boolean isInfoSet() {
		
		// Check to see if we have all the account information stored
		if (isPlanSet()
				&& isProductSet()
				&& isOffpeakStartSet()
				&& isOffpeakEndSet()
				&& isPeakQuotaSet()
				&& isOffpeakQuotaSet()) {
			
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
		return mSettings.getString(PREF_PLAN_KEY, "");
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
		return mSettings.getString(PREF_PRODUCT_KEY, "");
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
	
	public String getProductPlan(){
		return getPlan() + " (" + getProduct() + ")";
	}
	
	/**
	 * Method for getting off peak start time
	 * @return shared preference string
	 */
	public long getOffpeakStart(){
		return mSettings.getLong(PREF_OFFPEAK_START_KEY, 0);
	}
	
	/**
	 * Check if off peak start time is set
	 * @return true of string length is greater then 0
	 */
	public boolean isOffpeakStartSet(){
		// Check if string length is greater then 0
		if (getOffpeakStart() != 0){
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
	public long getOffpeakEnd(){
		return mSettings.getLong(PREF_OFFPEAK_END_KEY, 0);
	}
	
	/**
	 * Check if off peak start time is set
	 * @return true of string length is greater then 0
	 */
	public boolean isOffpeakEndSet(){
		// Check if string length is greater then 0
		if (getOffpeakEnd() != 0){
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
	public long getPeakQuota(){
		return mSettings.getLong(PREF_PEAK_QUOTA_KEY, 0);
	}
	
	public int getPeakQuotaGb(){
		return (int) (getPeakQuota() / GB);
	}
	
	public String getPeakQuotaString(){
		return "/ " + Long.toString(getPeakQuotaGb()) + " (Gb) Peak";
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
	public long getOffpeakQuota(){
		return mSettings.getLong(PREF_OFFPEAK_QUOTA_KEY, 0);
	}
	
	public int getOffpeakQuotaGb(){
		return (int) (getOffpeakQuota() / GB);
	}
	
	public String getOffpeakQuotaString(){
		return "/ " + Long.toString(getOffpeakQuotaGb()) + " (Gb) Offpeak";
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
