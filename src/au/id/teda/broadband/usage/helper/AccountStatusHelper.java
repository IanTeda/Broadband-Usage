package au.id.teda.broadband.usage.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import au.id.teda.broadband.usage.ui.MainActivity;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class AccountStatusHelper {
	
	//private static final String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
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
	
	private final static long GB = 1000000000;
	private final static long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
	
	private final static String FORMAT_dd_MMM_YYYY = "dd-MMM-yyyy HH:mm";

	// Activity context
    private static Context mContext;
    
    // Activity shared preferences
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;
    
    AccountInfoHelper mInfo;
    
    // Class constructor
    public AccountStatusHelper(Context context) {
    	AccountStatusHelper.mContext = context;
    	mSettings = PreferenceManager.getDefaultSharedPreferences(context);
    	mEditor = mSettings.edit();
    	
    	mInfo = new AccountInfoHelper(mContext);
    }
    
    public void setAccoutStatus(String userAccount, long quotaResetDate, long quotaStartDate
    		, long peakDataUsed, boolean peakIsShaped, long peakSpeed
    		, long offpeakDataUsed, boolean offpeakIsShaped, long offpeakSpeed
    		, long uploadsDataUsed
    		, long freezoneDataUsed
    		, String ipAddress, long upTimeDate) {
    	
    	mEditor.putString(ACCOUNT, userAccount);
    	mEditor.putLong(QUOTA_RESET_DATE, quotaResetDate);
		mEditor.putLong(QUOTA_START_DATE, quotaStartDate);
		mEditor.putLong(PEAK_DATA_USED, peakDataUsed);
		mEditor.putBoolean(PEAK_IS_SHAPED, peakIsShaped);
		mEditor.putLong(PEAK_SPEED, peakSpeed);
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
	
	public long getDaysToGoMillis(){
		// Get current date/time
		Calendar now = Calendar.getInstance();
		// Get rollover date/time
		Calendar rollover = getQuotaResetDate();
		// Difference in milliseconds divided by day in millisecond
		long diffInDays = (rollover.getTimeInMillis() - now.getTimeInMillis());
		return diffInDays;
	}
	
	public int getDaysToGo(){
		int diffInDays = (int) (getDaysToGoMillis() / DAY_IN_MILLIS);
		return diffInDays;
	}
	
	public String getDaysToGoString(){

		int diffInDays = getDaysToGo();
		
		String daysToGo = Integer.toString(diffInDays);
		if (diffInDays < 10 ){
			daysToGo = "0" + daysToGo;
		}
		
		return daysToGo;
	}
	
	public String getCurrentMonthString(){
		// How to format date
		String FORMAT_MMMM_yyyy = "MMMMM yyyy";
		
		// Set calendar to rollover date
		Calendar rollover = getQuotaResetDate();
		
		//Set up formater
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_MMMM_yyyy, Locale.UK);
		
		// Get date value of calendar and format
		String currentMonth = sdf.format(rollover.getTime());
		return currentMonth.toUpperCase();
	}
	
	public String getDataBaseMonthString(){
		// How to format date
		String FORMAT_YYYYMM = "yyyyMM";
		
		// Set calendar to rollover date
		Calendar rollover = getQuotaResetDate();
		
		//Set up formater
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYYYMM, Locale.UK);
		
		return sdf.format(rollover.getTime());
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
		return rolloverDate.toUpperCase();
	}
	
	public Calendar getQuotaStartDate(){
		long milliseconds = mSettings.getLong(QUOTA_START_DATE, 0);
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(milliseconds);
		return mCalendar;
	}
	
	public int getDaysSoFar(){
		// Get current date/time
		Calendar now = Calendar.getInstance();
		
		// Get rollover date/time
		Calendar start = getQuotaStartDate();
		
		// Difference in milliseconds divided by day in millisecond
		int diffInDays = (int) ((now.getTimeInMillis() - start.getTimeInMillis()) / DAY_IN_MILLIS );
		return diffInDays;
	}
	
	public String getDaysSoFarString(){

		int diffInDays = getDaysSoFar();
		
		String daysSoGo = Integer.toString(diffInDays);
		if (diffInDays < 10 ){
			daysSoGo = "0" + daysSoGo;
		}
		
		return daysSoGo;
	}
	
	public String getStartDateString(){
		// How to format date
		String FORMAT_dd_MMMM_yyyy = "dd MMMMM yyyy";
		
		// Set calendar to rollover date
		Calendar start = getQuotaStartDate();
		
		//Set up formater
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_dd_MMMM_yyyy);
		
		// Get date value of calendar and format

		String startDate = sdf.format(start.getTime());
		return startDate.toUpperCase();
	}

	public int getDaysThisPeriod(){
		int daysToGo = getDaysToGo();
		int daysSoFar = getDaysSoFar();
		int daysThisPeriod = daysSoFar + daysToGo;
		return daysThisPeriod;
	}
	
	public String getDaysThisPeriodString(){
		String days = String.valueOf(getDaysThisPeriod());
		return "/ " + days + " days so far";
	}
	
	public long getPeakDataUsed(){
		return mSettings.getLong(PEAK_DATA_USED, 0);
	}
	
	public int getPeakDataUsedGb(){
		return (int) (mSettings.getLong(PEAK_DATA_USED, 0) / GB);
	}
	
	public String getPeakDataUsedGbString(){
		long peak = getPeakDataUsedGb();
		
		String used = Long.toString(peak);
		if (peak < 10 ){
			used = "0" + used;
		}
		
		return used;
	}
	
	public long getPeakDataRemaining(){
		long quota = mInfo.getPeakQuota();
		long used = getPeakDataUsed();
		
		return (quota - used);
	}
	
	public int getPeakDataRemaingGb(){
		return (int) ( getPeakDataRemaining() / GB );
	}
	
	public String getPeakDataRemaingGbString(){
		int data = getPeakDataRemaingGb();
		String remaining = String.valueOf(data);
		
		if (data < 10 ){
			remaining = "0" + remaining;
		}
		
		return remaining;
	}
	
	public boolean isPeakShaped(){
		return mSettings.getBoolean(PEAK_IS_SHAPED, false);
	}
	
	public String getPeakShapedUsedString(){
		boolean isShaped = isPeakShaped();
		if (isShaped){
			return "USED DATA (SHAPED)";
		} else {
			return "USED DATA (UNSHAPED)";
		}
	}
	
	public String getPeakShapedRemainingString(){
		boolean isShaped = isPeakShaped();
		if (isShaped){
			return "REMAINING DATA (SHAPED)";
		} else {
			return "REMAINING DATA (UNSHAPED)";
		}
	}
	
	public long getPeakSpeed(){
		return mSettings.getLong(PEAK_SPEED, -1);
	}
	
	public long getOffpeakDataUsed(){
		return mSettings.getLong(OFFPEAK_DATA_USED, 0);
	}
	
	public int getOffpeakDataUsedGb(){
		return (int) (mSettings.getLong(OFFPEAK_DATA_USED, 0) / GB);
	}
	
	public String getOffpeakDataUsedGbString(){
		long offpeak = getOffpeakDataUsedGb();
		
		String used = Long.toString(offpeak);
		if (offpeak < 10 ){
			used = "0" + used;
		}
		return used;
	}
	
	public long getOffpeakDataRemaining(){
		long quota = mInfo.getOffpeakQuota();
		long used = getOffpeakDataUsed();
		
		return (quota - used);
	}
	
	public int getOffpeakDataRemaingGb(){
		int remaining = (int) ( getOffpeakDataRemaining() / GB );
		return remaining ;
	}
	
	public String getOffpeakDataRemaingGbString(){
		int data = getOffpeakDataRemaingGb();
		String remaining = String.valueOf(data);

		if (data < 10 ){
			remaining = "0" + remaining;
		}
		
		return remaining;
	}
	
	public boolean isOffpeakShaped(){
		return mSettings.getBoolean(OFFPEAK_IS_SHAPED, false);
	}
	
	public String getOffpeakShapedUsedString(){
		boolean isShaped = isOffpeakShaped();
		if (isShaped){
			return "USED DATA (SHAPED)";
		} else {
			return "USED DATA (UNSHAPED)";
		}
	}
	
	public String getOffpeakShapedRemainingString(){
		boolean isShaped = isOffpeakShaped();
		if (isShaped){
			return "REMAINING DATA (SHAPED)";
		} else {
			return "REMAINING DATA (UNSHAPED)";
		}
	}
	
	public long getOffpeakSpeed(){
		return mSettings.getLong(OFFPEAK_SPEED, -1);
	}
	
	public long getUploadsDataUsed(){
		return mSettings.getLong(UPLOADS_DATA_USED, 0);
	}
	
	public int getUploadsDataUsedGb(){
		return (int) (getUploadsDataUsed() / GB);
	}
	
	public String getUploadsDataUsedGbString(){
		long offpeak = getUploadsDataUsedGb();
		
		String used = Long.toString(offpeak);
		if (offpeak < 10 ){
			used = "0" + used;
		}
		return used;
	}
	
	public long getFreezoneDataUsed(){
		return mSettings.getLong(FREEZONE_DATA_USED, 0);
	}
	
	public int getFreezoneDataUsedGb(){
		return (int) (getFreezoneDataUsed() / GB);
	}
	
	public String getFreezoneDataUsedGbString(){
		long offpeak = getFreezoneDataUsedGb();
		
		String used = Long.toString(offpeak);
		if (offpeak < 10 ){
			used = "0" + used;
		}
		return used;
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
		
		// Get current date/time
		Calendar now = Calendar.getInstance();
		
		// Get rollover date/time
		Calendar uptime = getUpTimeDate();
		
		// Difference in milliseconds divided by day in millisecond
		int diffInDays = (int) ((now.getTimeInMillis() - uptime.getTimeInMillis()) / DAY_IN_MILLIS );
		
		return diffInDays;
	}
	
	public String getUpTimeDaysString(){

		int upDays = getUpTimeDays();
		
		String days =  Integer.toString(upDays);
		if (upDays < 10 ){
			days = "0" + days;
		}
		
		return days;
	}
	
    
    private Long getLastSyncTime(){
    	return mSettings.getLong(NetworkUtilities.PREF_LAST_SYNC_KEY, 0);
    }
    
    public String getLastSyncTimeString(){
    	long lastSyncMillis = getLastSyncTime();
    	
    	Calendar lastSyncCal = Calendar.getInstance();
		lastSyncCal.setTimeInMillis(lastSyncMillis);
		
		//TODO Add local to formatter
		//Set up formatter
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_dd_MMM_YYYY);
		
		// Get date value of calendar and format
		String syncDateTime = sdf.format(lastSyncCal.getTime());
    	
		return "Synced: " + syncDateTime;
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
		if (getPeakSpeed() > -1){
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
		if (getOffpeakSpeed() > -1){
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
