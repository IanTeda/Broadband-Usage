package au.id.teda.broadband.usage.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import au.id.teda.broadband.usage.activity.BaseActivity;
import au.id.teda.broadband.usage.util.NetworkUtilities;

@SuppressLint("DefaultLocale") // For some reason cannot remove the lint warning
public class AccountStatusHelper {
	
	private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	// Set static string values for preference keys
	private final static String ACCOUNT = "account";
	private final static String QUOTA_RESET_DATE = "quotaResetDate";
	private final static String QUOTA_START_DATE = "quotaStartDate";
    private final static String ANYTIME_DATA_USED = "anyTimeDataUsed";
    private final static String ANYTIME_SPEED = "anyTimeSpeed";
    private final static String ANYTIME_IS_SHAPED = "anyTimeIsShaped";
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
	private final static long MB = 1000000;
	private final static long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
	
	//private final static String FORMAT_dd_MMM_YYYY = "dd-MMM-yyyy HH:mm";
	private final static String FORMAT_EEE_dd_MMM_YYYY = "EEE, dd-MMM-yyyy HH:mm";

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
            , long anyTimeDataUsed, boolean anyTimeIsShaped, long anyTimeSpeed
    		, long peakDataUsed, boolean peakIsShaped, long peakSpeed
    		, long offpeakDataUsed, boolean offpeakIsShaped, long offpeakSpeed
    		, long uploadsDataUsed
    		, long freezoneDataUsed
    		, String ipAddress, long upTimeDate) {

        mEditor.putString(ACCOUNT, userAccount);
    	mEditor.putLong(QUOTA_RESET_DATE, quotaResetDate);
		mEditor.putLong(QUOTA_START_DATE, quotaStartDate);
        mEditor.putLong(ANYTIME_DATA_USED, anyTimeDataUsed);
        mEditor.putBoolean(ANYTIME_IS_SHAPED, anyTimeIsShaped);
        mEditor.putLong(ANYTIME_SPEED, anyTimeSpeed);
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

        Log.d(DEBUG_TAG, "uptime:" + upTimeDate);

		// Commit values to preferences
		mEditor.commit();
		
	}
    
    public boolean isStatusSet() {

/**
        Log.d(DEBUG_TAG, "" + mInfo.isAccountAnyTime()
                + isQuotaResetDateSet()
                + isQuotaStartDateSet()
                + isAnytimeDataSet()
                + isAnytimeIsShapedSet()
                + isAnytimeSpeedSet()
                + isUploadsDataSet()
                + isFreezoneDataSet()
                + isUpTimeDateSet());
 **/


        if (mInfo.isAccountAnyTime()
                && isQuotaResetDateSet()
                && isQuotaStartDateSet()
                && isAnytimeDataSet()
                && isAnytimeIsShapedSet()
                && isAnytimeSpeedSet()
                && isUploadsDataSet()
                && isFreezoneDataSet()){

            return true;

        } else if (!mInfo.isAccountAnyTime()
                && isQuotaResetDateSet()
                && isQuotaStartDateSet()
                && isPeakDataSet()
                && isPeakIsShapedSet()
                && isPeakSpeedSet()
                && isOffpeakDataSet()
                && isOffpeakIsShapedSet()
                && isOffpeakSpeedSet()
                && isUploadsDataSet()
                && isFreezoneDataSet()){

            return true;

        } else {

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
		if (diffInDays < 0){
			daysToGo = "00";
		} else if (diffInDays < 10 ){
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
		return currentMonth.toUpperCase(Locale.getDefault());
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
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_dd_MMMM_yyyy, Locale.getDefault());
		
		// Get date value of calendar and format
		String rolloverDate = sdf.format(rollover.getTime());
		return rolloverDate.toUpperCase(Locale.getDefault());
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
		
		String daysSoFar = Integer.toString(diffInDays);
		if (diffInDays < 10 ){
			daysSoFar = "0" + daysSoFar;
		} else if (diffInDays > 31){
			daysSoFar = "00";
		}
		
		return daysSoFar;
	}
	
	public String getStartDateString(){
		// How to format date
		String FORMAT_dd_MMMM_yyyy = "dd MMMMM yyyy";
		
		// Set calendar to rollover date
		Calendar start = getQuotaStartDate();
		
		//Set up formater
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_dd_MMMM_yyyy, Locale.getDefault());
		
		// Get date value of calendar and format

		String startDate = sdf.format(start.getTime());
		return startDate.toUpperCase(Locale.getDefault());
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

    /**
     *  Start any time methods
     */

    public long getAnytimeDataUsed(){
        return mSettings.getLong(ANYTIME_DATA_USED, 0);
    }

    public long getAnytimeDataUsedMb(){
        return (getAnytimeDataUsed() / MB);
    }

    public int getAnytimeDataUsedGb(){
        int data = (int) (getAnytimeDataUsed() / GB);
        return data;
    }

    public int getAnytimeDailyAverageUsedMb(){
        long used = getAnytimeDataUsedMb();
        long days = getDaysSoFar();
        int average = (int) (used / days);
        return average;
    }

    public int getAnytimeAverageVariationMb(){
        long quota = mInfo.getAnyTimeQuotaDailyMb();
        long daily = getAnytimeDailyAverageUsedMb();
        int variation = (int) (daily - quota);
        return variation;
    }

    public int getAnytimeDataUsedLessUploadsGb(){
        int download = (int) getAnytimeDataUsedGb();
        int upload = getUploadsDataUsedGb();
        return (download - upload);
    }

    public String getAnytimeDataUsedGbString(){
        long data = getAnytimeDataUsedGb();

        String used = Long.toString(data);
        if (data < 10 ){
            used = "0" + used;
        }

        return used;
    }

    public String getAnytimeDataUsedLessUploadsGbString(){
        long data = getAnytimeDataUsedLessUploadsGb();

        String used = Long.toString(data);
        if (data < 10 ){
            used = "0" + used;
        }

        return used;
    }

    public int getAnytimeDataUsedPercent(){
        long used = getAnytimeDataUsed();
        long quota = mInfo.getAnyTimeQuota();
        int percent = (int) ((used * 100.0f) / quota);
        return percent;
    }

    public String getAnytimeDataUsedPercentString(){
        int percent = getAnytimeDataUsedPercent();
        return String.valueOf(percent) + "%";
    }

    public long getAnytimeDataRemaining(){
        long quota = mInfo.getAnyTimeQuota();
        long used = getAnytimeDataUsed();

        return (quota - used);
    }

    public int getAnytimeDataRemaingGb(){
        return (int) ( getAnytimeDataRemaining() / GB );
    }

    public String getAnytimeDataRemaingGbString(){
        int data = getAnytimeDataRemaingGb();
        String remaining = String.valueOf(data);

        if (data < 10 ){
            remaining = "0" + remaining;
        }

        return remaining;
    }

    public boolean isAnytimeShaped(){
        return mSettings.getBoolean(ANYTIME_IS_SHAPED, false);
    }

    public String getAnytimeShapedUsedString(){
        boolean isShaped = isAnytimeShaped();
        if (isShaped){
            return "USED DATA (SHAPED)";
        } else {
            return "USED DATA (UNSHAPED)";
        }
    }

    public String getAnytimeShapedRemainingString(){
        boolean isShaped = isAnytimeShaped();
        if (isShaped){
            return "REMAINING DATA (SHAPED)";
        } else {
            return "REMAINING DATA (UNSHAPED)";
        }
    }

    public long getAnytimeSpeed(){
        return mSettings.getLong(ANYTIME_SPEED, 0);
    }

    /**
     *  Start Peak Data Methods
     */
	public long getPeakDataUsed(){
		return mSettings.getLong(PEAK_DATA_USED, 0);
	}
	
	public int getPeakDailyAverageUsedMb(){
		long used = getPeakDataUsed() / MB;
		long days = getDaysSoFar();
		int average = (int) (used / days);
		return average;
	}
	
	public int getPeakAverageVariationMb(){
		long quota = mInfo.getPeakQuotaDailyMb();
		long daily = getPeakDailyAverageUsedMb();
		int variation = (int) (daily - quota);
		return variation;
	}
	
	public int getPeakDataUsedGb(){
		return (int) (mSettings.getLong(PEAK_DATA_USED, 0) / GB);
	}
	
	public int getPeakDataUsedLessUploadsGb(){
		int download = getPeakDataUsedGb();
		int upload = getUploadsDataUsedGb();
		return (download - upload);
	}
	
	public String getPeakDataUsedGbString(){
		long peak = getPeakDataUsedGb();
		
		String used = Long.toString(peak);
		if (peak < 10 ){
			used = "0" + used;
		}
		
		return used;
	}
	
	public String getPeakDataUsedLessUploadsGbString(){
		long peak = getPeakDataUsedLessUploadsGb();
		
		String used = Long.toString(peak);
		if (peak < 10 ){
			used = "0" + used;
		}
		
		return used;
	}
	
	public int getPeakDataUsedPercent(){
		long used = getPeakDataUsed();
		long quota = mInfo.getPeakQuota();
		int percent = (int) ((used * 100.0f) / quota);
		return percent;
	}
	
	public String getPeakDataUsedPercentString(){
		int percent = getPeakDataUsedPercent();
		return String.valueOf(percent) + "%";
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
		return mSettings.getLong(PEAK_SPEED, 0);
	}

    /**
     *  Start off peak
     */
	
	public long getOffpeakDataUsed(){
		return mSettings.getLong(OFFPEAK_DATA_USED, 0);
	}
	
	public int getOffpeakDailyAverageUsedMb(){
		long used = getOffpeakDataUsed() / MB;
		long days = getDaysSoFar();
		int average = (int) (used / days);
		return average;
	}
	
	public int getOffpeakAverageVariationMb(){
		long quota = mInfo.getOffpeakQuotaDailyMb();
		long daily = getOffpeakDailyAverageUsedMb();
		int variation = (int) (daily - quota);
		return variation;
	}
	
	public int getOffpeakDataUsedGb(){
		return (int) (mSettings.getLong(OFFPEAK_DATA_USED, 0) / GB);
	}
	
	public int getOffpeakDataUsedLessUploadsGb(){
		int download = getOffpeakDataUsedGb();
		int upload = getUploadsDataUsedGb();
		return (download - upload);
	}
	
	public int getOffpeakDataUsedPercent(){
		long used = getOffpeakDataUsed();
		long quota = mInfo.getOffpeakQuota();
		int percent = (int) ((used * 100.0f) / quota);
		return percent;
	}
	
	public String getOffpeakDataUsedPercentString(){
		int percent = getOffpeakDataUsedPercent();
		return String.valueOf(percent) + "%";
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
	
	public String getOffpeakDataUsedLessUploadsGbString(){
		long peak = getOffpeakDataUsedLessUploadsGb();
		
		String used = Long.toString(peak);
		if (peak < 10 ){
			used = "0" + used;
		}
		
		return used;
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
		return mSettings.getLong(OFFPEAK_SPEED, 0);
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
		long milliseconds = mSettings.getLong(UP_TIME_DATE, -1);
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
		if (upDays < 0){
			days = "00";
		} else if (upDays < 10 ){
			days = "0" + days;
		} else if (upDays > 31){
			days = "00";
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
		
		//Set up formatter
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_EEE_dd_MMM_YYYY, Locale.getDefault());
		
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

    public boolean isAnytimeDataSet(){
        if (getAnytimeDataUsed() > 0){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isAnytimeIsShapedSet(){
        if (isAnytimeShaped()){
            return false;
        }
        else {
            return true;
        }

    }

    public boolean isAnytimeSpeedSet(){
        if (getAnytimeSpeed() > -1){
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

    private Calendar getCalendarFromMillis(long milliseconds){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliseconds);
        return cal;
    }



}
