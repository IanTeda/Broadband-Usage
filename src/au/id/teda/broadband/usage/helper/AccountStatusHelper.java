package au.id.teda.broadband.usage.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class AccountStatusHelper {
	
	private static final String DEBUG_TAG = "bbusage";
	
	// Set static string values for preference keys
	private final static String ACCOUNT = "account";
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
    
    public void setAccoutStatus(String userAccount, long quotaResetDate, long quotaStartDate
    		, long peakDataUsed, boolean peakIsShaped, int peakSpeed
    		, long offpeakDataUsed, boolean offpeakIsShaped, int offpeakSpeed
    		, long uploadsDataUsed
    		, long freezoneDataUsed
    		, String ipAddress, long upTimeDate) {
		
    	mEditor.putString(ACCOUNT, userAccount);
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
	
	public int getDaysToGo(){
		// What is a day in milli seconds
		final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
		// Get current date/time
		Calendar now = Calendar.getInstance();
		// Get rollover date/time
		Calendar rollover = getQuotaResetDate();
		// Difference in milliseconds divided by day in millisecond
		int diffInDays = (int) ((rollover.getTimeInMillis() - now.getTimeInMillis())/ DAY_IN_MILLIS );
		return diffInDays;
	}
	
	public String getCurrentMonthString(){
		// How to format date
		String FORMAT_MMMM_yyyy = "MMMMM yyyy";
		
		// Set calendar to rollover date
		Calendar rollover = getQuotaResetDate();
		
		//Set up formater
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_MMMM_yyyy);
		
		// Get date value of calendar and format
		String currentMonth = sdf.format(rollover.getTime());
		return currentMonth;
	}
	
	public String getRolloverDateString(){
		// How to format date
		String FORMAT_dd_MMMM_yyyy = "dd MMMMM yyyy";
		
		// Set calendar to rollover date
		Calendar rollover = getQuotaResetDate();
		
		//Set up formater
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_dd_MMMM_yyyy);
		
		// Get date value of calendar and format

		String rolloverDate = sdf.format(rollover.getTime());
		return rolloverDate;
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
	
	public long getPeakDataUsedGb(){
		long gb = 100000000;
		long peak = getPeakDataUsed();
		return peak / gb;
	}
	
	public boolean isPeakShaped(){
		return mSettings.getBoolean(PEAK_IS_SHAPED, false);
	}
	
	public String getPeakShapedString(){
		boolean isShaped = isPeakShaped();
		if (isShaped){
			return "USED DATA (SHAPED)";
		} else {
			return "USED DATA (UNSHAPED)";
		}
	}
	
	public int getPeakSpeed(){
		return mSettings.getInt(PEAK_SPEED, 0);
	}
	
	public long getOffpeakDataUsed(){
		return mSettings.getLong(OFFPEAK_DATA_USED, 0);
	}
	
	public long getOffpeakDataUsedGb(){
		long gb = 100000000;
		long peak = getOffpeakDataUsed();
		return peak / gb;
	}
	
	public boolean isOffpeakShaped(){
		return mSettings.getBoolean(OFFPEAK_IS_SHAPED, false);
	}
	
	public String getOffpeakShapedString(){
		boolean isShaped = isOffpeakShaped();
		if (isShaped){
			return "USED DATA (SHAPED)";
		} else {
			return "USED DATA (UNSHAPED)";
		}
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
	
	public String getIpAddressStrng(){
		String ip = mSettings.getString(IP_ADDRESS, "");
		return ip + " (IP)";
	}
	
	public Calendar getUpTimeDate(){
		long milliseconds = mSettings.getLong(UP_TIME_DATE, 0);
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(milliseconds);
		return mCalendar;
	}
	
	public int getUpTimeDays(){
		// What is a day in milli seconds
		final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
		// Get current date/time
		Calendar now = Calendar.getInstance();
		// Get rollover date/time
		Calendar rollover = getUpTimeDate();
		// Difference in milliseconds divided by day in millisecond
		int diffInDays = (int) (now.getTimeInMillis() - (rollover.getTimeInMillis())/ DAY_IN_MILLIS );
		return diffInDays;
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
