package au.id.teda.broadband.usage.helper;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AccountStatusHelper {
	
	//private static final String DEBUG_TAG = "bbusage";
	
	// Set static string values for preference keys
	private final static String QUOTA_RESET_DATE = "quotaResetDate";
	private final static String QUOTA_START_DATE = "quotaStartDate";
	private final static String PEAK_DATA_USED = "peakDataUsed";
	private final static String PEAK_SPEED = "peakSpeed";
	private final static String PEAK_IS_SHAPED = "peakIsShaped";
	private final static String OFFPEAK_DATA_USED = "offpeakDataUsed";
	private final static String OFFPEAK_SPEED = "offpeakSpeed";
	private final static String OFFPEAK_IS_SHAPED = "offpeakIsShaped";
	private final static String UPLOADS_DATA_USED = "uploadsDataUsed";
	private final static String FREEZONE_DATA_USED = "freezoneDataUsed";
	private final static String IP_ADDRESS = "ipAddress";
	private final static String UP_TIME_DATE = "upTimeDate";

	// Activity context
    private static Context mContext;
    
    // Activity shared preferences
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;
    
    // Class constructor
    public AccountStatusHelper(Context context) {
    	AccountStatusHelper.mContext = context;
    	mSettings = PreferenceManager.getDefaultSharedPreferences(context);
    	mEditor = mSettings.edit();
    }
    
    public void setAccoutStatus(long quotaResetDate, long quotaStartDate
    		, long peakDataUsed, boolean peakIsShaped, int peakSpeed
    		, long offpeakDataUsed, boolean offpeakIsShaped, int offpeakSpeed
    		, long uploadsDataUsed
    		, long freezoneDataUsed
    		, String ipAddress, long upTimeDate) {
		
    	mEditor.putLong(QUOTA_RESET_DATE, quotaResetDate);
		mEditor.putLong(QUOTA_START_DATE, quotaStartDate);
		mEditor.putLong(PEAK_DATA_USED, peakDataUsed);
		mEditor.putBoolean(PEAK_IS_SHAPED, peakIsShaped);
		mEditor.putLong(PEAK_SPEED, peakDataUsed);
		mEditor.putLong(OFFPEAK_DATA_USED, offpeakDataUsed);
		mEditor.putBoolean(OFFPEAK_IS_SHAPED, offpeakIsShaped);
		mEditor.putLong(OFFPEAK_SPEED, offpeakSpeed);
		mEditor.putLong(UPLOADS_DATA_USED, uploadsDataUsed);
		mEditor.putLong(FREEZONE_DATA_USED, freezoneDataUsed);
		mEditor.putString(IP_ADDRESS, ipAddress);
		mEditor.putLong(UP_TIME_DATE, upTimeDate);

		// Commit values to preferences
		mEditor.commit();
		
	}
    
    public boolean isStatusSet() {
		// Check to see if we have all the account status information stored
		if (isQuotaResetDateSet()
				&& isQuotaStartDateSet()
				&& isPeakDataSet()
				&& isPeakIsShapedSet()
				&& isPeakSpeedSet()
				&& isOffpeakDataSet()
				&& isOffpeakIsShapedSet()
				&& isOffpeakSpeedSet()
				&& isUploadsDataSet()
				&& isFreezoneDataSet()
				&& isUpTimeDateSet()
				&& isUpTimeDateSet()){
			
			// Looks like we have every thing, so return true
			return true;
		} else {
			
			// Dosen't seem to be all there so return false
			return false;
			
		}
	}
	
	public Calendar getQuotaResetDate(){
		long milliseconds = mSettings.getLong(QUOTA_RESET_DATE, 0);
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(milliseconds);
		return mCalendar;
	}
	
	public Calendar getQuotaStartDate(){
		long milliseconds = mSettings.getLong(QUOTA_START_DATE, 0);
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(milliseconds);
		return mCalendar;
	}

	public long getPeakDataUsed(){
		return mSettings.getLong(PEAK_DATA_USED, 0);
	}
	
	public boolean isPeakShaped(){
		return mSettings.getBoolean(PEAK_IS_SHAPED, false);
	}
	
	public int getPeakSpeed(){
		return mSettings.getInt(PEAK_SPEED, 0);
	}
	
	public long getOffpeakDataUsed(){
		return mSettings.getLong(OFFPEAK_DATA_USED, 0);
	}
	
	public boolean isOffpeakShaped(){
		return mSettings.getBoolean(OFFPEAK_IS_SHAPED, false);
	}
	
	public int getOffpeakSpeed(){
		return mSettings.getInt(OFFPEAK_SPEED, 0);
	}
	
	public long getUploadsDataUsed(){
		return mSettings.getLong(UPLOADS_DATA_USED, 0);
	}
	
	public long getFreezoneDataUsed(){
		return mSettings.getLong(FREEZONE_DATA_USED, 0);
	}
	
	public String getIpAddress(){
		return mSettings.getString(IP_ADDRESS, "");
	}
	
	public Calendar getUpTimeDate(){
		long milliseconds = mSettings.getLong(UP_TIME_DATE, 0);
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(milliseconds);
		return mCalendar;
	}
	
	public boolean isQuotaResetDateSet(){
		if (getQuotaResetDate().getTimeInMillis() > 0){
			return true;
		} 
		else {
			return false;
		}
	}
	
	public boolean isQuotaStartDateSet(){
		if (getQuotaStartDate().getTimeInMillis() > 0){
			return true;
		} 
		else {
			return false;
		}
	}
	
	public boolean isPeakDataSet(){
		if (getPeakDataUsed() > 0){
			return true;
		} 
		else {
			return false;
		}
	}
	
	public boolean isPeakIsShapedSet(){
		if (isPeakShaped()){
			return false;
		}
		else {
			return true;
		}
		
	}
	
	public boolean isPeakSpeedSet(){
		if (getPeakSpeed() > 0){
			return true;
		} 
		else {
			return false;
		}
	}
	
	public boolean isOffpeakDataSet(){
		if (getOffpeakDataUsed() > 0){
			return true;
		} 
		else {
			return false;
		}
	}
	
	public boolean isOffpeakIsShapedSet(){
		if (isOffpeakShaped()){
			return false;
		}
		else {
			return true;
		}
		
	}
	
	public boolean isOffpeakSpeedSet(){
		if (getOffpeakSpeed() > 0){
			return true;
		} 
		else {
			return false;
		}
	}
	
	public boolean isUploadsDataSet(){
		if (getUploadsDataUsed() > 0){
			return true;
		} 
		else {
			return false;
		}
	}
	
	public boolean isFreezoneDataSet(){
		if (getFreezoneDataUsed() > 0){
			return true;
		} 
		else {
			return false;
		}
	}
	
	public boolean isIpAddressSet(){
		if (getIpAddress().length() > 0){
			return true;
		} 
		else {
			return false;
		}
	}

	public boolean isUpTimeDateSet(){
		if (getUpTimeDate().getTimeInMillis() > 0){
			return true;
		} 
		else {
			return false;
		}
	}



}
