package au.id.teda.broadband.usage.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AccountStatusHelper {
	
	private static final String DEBUG_TAG = "bbusage";
	
	// Set static string values for preference keys
	private final static String LAST_UPDATE = "lastUpdate";
	private final static String CURRENT_DATA_PERIOD = "currentDataPeriod";
	private final static String CURRENT_ANNIVERSARY = "currentAnniversary";
	private final static String CURRENT_DAYS_SO_FAR = "currentDaysSoFare";
	private final static String CURRENT_DAYS_TO_GO = "currentDaysToGo";
	private final static String CURRENT_PEAK_SHAPED = "currentPeakShaped";
	private final static String CURRENT_OFF_PEAK_SHAPED = "currentOffPeakShaped";
	private final static String CURRENT_PEAK_USED = "currentPeakUsed";
	private final static String CURRENT_OFF_PEAK_USED = "currentOffPeakUsed";
	private final static String CURRENT_UPLOAD_USED = "currentUploadUsed";
	private final static String CURRENT_FREEZONE_USED = "currentFreezoneUsed";
	private final static String CURRENT_PEAK_SHAPED_SPEED = "currentPeakShapedSpeed";
	private final static String CURRENT_OFF_PEAK_SHAPED_SPEED = "currentOffPeakShapedSpeed";
	private final static String CURRENT_UP_TIME = "currentUpTime";
	private final static String CURRENT_IP = "CurrentIp";
	
	// Activity context
    private static Context context;
    
    // Activity shared preferences
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor = mSettings.edit();
    
    // Class constructor
    public AccountStatusHelper(Context context) {
    	AccountStatusHelper.context = context;
    	mSettings = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public void setAccoutStatus(long systemDateTime, String period,
    		long anniversary, long days_so_far, long days_to_go,
			int peak_shaped, int offpeak_shaped, long peak_used,
			long offpeak_used, long upload_used, long freezone_used,
			long peak_shaped_speed, long offpeak_shaped_speed, long uptime,
			String ip_address) {
		
		mEditor.putLong(LAST_UPDATE, systemDateTime);
		mEditor.putString(CURRENT_DATA_PERIOD, period);
		mEditor.putLong(CURRENT_ANNIVERSARY, anniversary);
		mEditor.putLong(CURRENT_DAYS_SO_FAR, days_so_far);
		mEditor.putLong(CURRENT_DAYS_TO_GO, days_to_go);
		mEditor.putInt(CURRENT_PEAK_SHAPED, peak_shaped);
		mEditor.putInt(CURRENT_OFF_PEAK_SHAPED, offpeak_shaped);
		mEditor.putLong(CURRENT_PEAK_USED, peak_used);
		mEditor.putLong(CURRENT_OFF_PEAK_USED, offpeak_used);
		mEditor.putLong(CURRENT_UPLOAD_USED, upload_used);
		mEditor.putLong(CURRENT_FREEZONE_USED, freezone_used);
		mEditor.putLong(CURRENT_PEAK_SHAPED_SPEED, peak_shaped_speed);
		mEditor.putLong(CURRENT_OFF_PEAK_SHAPED_SPEED, offpeak_shaped_speed);
		mEditor.putLong(CURRENT_UP_TIME, uptime);
		mEditor.putString(CURRENT_IP, ip_address);
		// Commit values to preferences
		mEditor.commit();
		
	}
    
    public boolean isStatusSet() {
		// Check to see if we have all the account status information stored
		if (getLastUpdate() > 0
				&& getCurrentDataPeriod().length() > 0
				&& getCurrentAnniversary() >= 0
				&& getCurrentDaysToGo() >= 0
				&& getCurrentDaysSoFar() >= 0
				&& getCurrentPeakShaped() >= 0
				&& getCurrentOffpeakShaped() >= 0
				&& getCurrentPeakUsed() >= 0
				&& getCurrentOffpeakUsed() >= 0
				&& getCurrentUploadUsed() >= 0
				&& getCurrentFreezoneUsed() >= 0
				&& getCurrentPeakShapedSpeed() >= 0
				&& getCurrentOffpeakShapedSpeed() >= 0
				&& getCurrentUpTime() >= 0
				&& getCurrentIp().length() > 0){
			
			// Looks like we have every thing, so return true
			return true;
		} else {
			
			// Dosen't seem to be all there so return false
			return false;
			
		}
	}
    
    /**
	 * Method for getting last update
	 * @return Long value of last update date
	 */
	public Long getLastUpdate(){
		return mSettings.getLong(LAST_UPDATE, 0);
	}
	
	/**
	 * Method for getting current data period
	 * @return String value in shared preference
	 */
	public String getCurrentDataPeriod(){
		return mSettings.getString(CURRENT_DATA_PERIOD, "");
	}
	
	/**
	 * Method for getting current roll over (anniversary) date
	 * @return Long value of anniversary date
	 */
	public Long getCurrentAnniversary(){
		return mSettings.getLong(CURRENT_ANNIVERSARY, 0);
	}
	
	/**
	 * Method for getting current days to go
	 * @return Long value of days to go in current period
	 */
	public Long getCurrentDaysToGo(){
		return mSettings.getLong(CURRENT_DAYS_TO_GO, 0);
	}
	
	/**
	 * Method for getting current days so far
	 * @return Long value of days so far in current period
	 */
	public Long getCurrentDaysSoFar(){
		return mSettings.getLong(CURRENT_DAYS_SO_FAR, 0);
	}
	
	/**
	 * Method for getting current peak shape speed
	 * @return int value of peak shaped
	 */
	public int getCurrentPeakShaped(){
		return mSettings.getInt(CURRENT_PEAK_SHAPED, 0);
	}
	
	/**
	 * Method for checking if current off peak is shaped
	 * @return int value of off peak shaped
	 */
	public int getCurrentOffpeakShaped(){
		return mSettings.getInt(CURRENT_OFF_PEAK_SHAPED, 0);
	}
	
	/**
	 * Method for checking if current peak is shaped
	 * @return Long value of current peak data used
	 */
	public Long getCurrentPeakUsed(){
		return mSettings.getLong(CURRENT_PEAK_USED, 0);
	}
	
	/**
	 * Method for getting current off peak data used to date
	 * @return Long value of current off peak data used
	 */
	public Long getCurrentOffpeakUsed(){
		return mSettings.getLong(CURRENT_OFF_PEAK_USED, 0);
	}
	
	/**
	 * Method for geting current uploaded data used to date
	 * @return Long value of current upload data used
	 */
	public Long getCurrentUploadUsed(){
		return mSettings.getLong(CURRENT_UPLOAD_USED, 0);
	}

	/**
	 * Method for getting current freezone date used to date
	 * @return Long value of current Freezone data used
	 */
	public Long getCurrentFreezoneUsed(){
		return mSettings.getLong(CURRENT_FREEZONE_USED, 0);
	}
	
	/**
	 * Method for getting current peak shaped speed
	 * @return Long value of current peak shaped speed
	 */
	public Long getCurrentPeakShapedSpeed(){
		return mSettings.getLong(CURRENT_PEAK_SHAPED_SPEED, 0);
	}
	
	/**
	 * Method for getting current off peak shaped speed
	 * @return Long value of current off peak shaped speed
	 */
	public Long getCurrentOffpeakShapedSpeed(){
		return mSettings.getLong(CURRENT_OFF_PEAK_SHAPED_SPEED, 0);
	}

	/**
	 * Method for getting current up time of broadband connection
	 * @return Long value of up time date
	 */
	public Long getCurrentUpTime(){
		return mSettings.getLong(CURRENT_UP_TIME, 0);
	}
	
	/**
	 * Method for returning current IP address of broadband connection
	 * @return String value of current IP Address
	 */
	public String getCurrentIp(){
		return mSettings.getString(CURRENT_IP, "");
	}
    
    /**
	 * Method for checking if current data period has been set
	 * @return true if String length is greater then 0
	 */
	public boolean isCurrentDataPeriodSet(){
		// Check Current Data Period string length is greater then 0
		if (getCurrentDataPeriod().length() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}

	/**
	 * Method for checking if anniversary has been set
	 * @return true if String length is greater then 0
	 */
	public boolean isCurrentAnniversarySet(){
		// Check Current Anniversary Long is greater then 0
		if (getCurrentAnniversary() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}

	/**
	 * Method for checking if days to go has been set
	 * @return true if days Long is greater then 0
	 */
	public boolean isCurrentDaysToGoSet(){
		// Check Current Days to Go is greater then 0
		if (getCurrentDaysToGo() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}

	/**
	 * Method for checking if days to go has been set
	 * @return true if days is greater then 0
	 */
	public boolean isCurrentDaysSoFarSet(){
		// Check Current Days so Far is greater then 0
		if (getCurrentDaysSoFar() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}

	/**
	 * Method for checking if peak period is shaped
	 * @return true if currently shaped
	 */
	public boolean isCurrentPeakShaped(){
		// Check Int value for peak shaped
		if (getCurrentPeakShaped() == 0){
			// Int value is 0 therefore un-shaped so return false
			return false;
		}
		// Else Int value is 1
		else {
			// Therefore we are shaped so return true
			return true;
		}
		
	}

	/**
	 * Method for checking if peak period is shaped
	 * @return true if currently shaped
	 */
	public boolean isCurrentOffpeakShaped(){
		// Check Int value for offpeak shaped
		if (getCurrentOffpeakShaped() == 0){
			// Int value is 0 therefore un-shaped so return false
			return false;
		}
		// Else Int value is 1
		else {
			// Therefore we are shaped so return true
			return true;
		}
		
	}

	/**
	 * Method to check if current peak data used is set
	 * @return true if Long value is greater then 0
	 */
	public boolean isCurrentPeakUsedSet(){
		// Check Current Peak Used Long is greater then 0
		if (getCurrentPeakUsed() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}

	/**
	 * Method to check if current off peak data used is set
	 * @return true if Long value is greater then 0
	 */
	public boolean isCurrentOffpeakUsedSet(){
		// Check Current Peak Used Long is greater then 0
		if (getCurrentOffpeakUsed() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}

	/**
	 * Method for checking if connection up time has been set
	 * @return true if long is greater then 0
	 */
	public boolean isCurrentUpTimeSet(){
		// Check Current Data Period length is greater then 0
		if (getCurrentUpTime() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}

	/**
	 * Method for checking if current ip has been set
	 * @return true if string is greater then 0
	 */
	public boolean isCurrentIpSet(){
		// Check Current IP string length is greater then 0
		if (getCurrentIp().length() > 0){
			// Looks like it is so return true
			return true;
		} 
		else {
			// Else it must be 0 and not set so return false
			return false;
		}
	}

}
