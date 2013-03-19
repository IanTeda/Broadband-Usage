package au.id.teda.broadband.usage.helper;

import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import au.id.teda.broadband.usage.R;

public class AccountInfoHelper {
	
	//private static final String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// Set static string values for preference keys
	private final static String PREF_ACCOUNT_KEY = "pref_account_key";
	private final static String PREF_PLAN_KEY = "pref_plan_key";
	private final static String PREF_PRODUCT_KEY = "pref_product";
	private final static String PREF_OFFPEAK_START_KEY = "pref_offpeak_start";
	private final static String PREF_OFFPEAK_END_KEY = "pref_offpeak_end";
	private final static String PREF_PEAK_QUOTA_KEY = "pref_peak_quota";
	private final static String PREF_OFFPEAK_QUOTA_KEY = "pref_offpeak_quota";
	
	private final static long GB = 1000000000;
	private final static long MB = 1000000;

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
	@SuppressLint("DefaultLocale")
	public String getPlan(){
		return mSettings.getString(PREF_PLAN_KEY
				, mContext.getResources().getString(R.string.fragment_product_plan_plan)).toUpperCase(Locale.getDefault());
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
	@SuppressLint("DefaultLocale")
	public String getProduct(){
		return mSettings.getString(PREF_PRODUCT_KEY
				, mContext.getResources().getString(R.string.fragment_product_plan_product)).toUpperCase(Locale.getDefault());
	}
	
	
	/**
	 * Check if product has been set
	 * @return true if product string length is greater then 0
	 */
	@SuppressLint("DefaultLocale")
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
	
	@SuppressLint("DefaultLocale")
	public String getProductPlanString(){
		return getPlan().toUpperCase(Locale.getDefault()) + " (" + getProduct().toUpperCase() + ")";
	}
	
	/**
	 * Method for getting off peak start time
	 * @return shared preference string
	 */
	public long getOffpeakStartTime(){
		return mSettings.getLong(PREF_OFFPEAK_START_KEY, 0);
	}
	
	/**
	 * Check if off peak start time is set
	 * @return true of string length is greater then 0
	 */
	public boolean isOffpeakStartSet(){
		// Check if string length is greater then 0
		if (getOffpeakStartTime() != 0){
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
	public long getOffpeakEndTime(){
		return mSettings.getLong(PREF_OFFPEAK_END_KEY, 0);
	}
	
	public String getOffpeakEndTimeString(){
		long millis = getOffpeakEndTime();
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);

		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int mins = cal.get(Calendar.MINUTE);
		
		String hoursString = "";
		if (hours < 10){
			hoursString = "0" + String.valueOf(hours);
		}
		
		String minsString = "";
		if (mins < 10){
			minsString = "0" + String.valueOf(mins);
		}
		
		String time = hoursString + ":" + minsString; 
		
		return time;
	}
	
	public String getOffpeakEndTimeAmPmString(){
		long millis = getOffpeakEndTime();
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);

		int am_pm = cal.get(Calendar.AM_PM);
		
		if (am_pm == 0){
			return "(am)";
		} else {
			return "(pm)";
		}
	}
	
	public String getOffpeakStartTimeAmPmString(){
		long millis = getOffpeakStartTime();
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);

		int am_pm = cal.get(Calendar.AM_PM);
		
		if (am_pm == 0){
			return "(am)";
		} else {
			return "(pm)";
		}
	}
	
	public String getOffpeakStartTimeString(){
		long millis = getOffpeakStartTime();
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);

		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int mins = cal.get(Calendar.MINUTE);
		
		String hoursString = "";
		if (hours < 10){
			hoursString = "0" + String.valueOf(hours);
		}
		
		String minsString = "";
		if (mins < 10){
			minsString = "0" + String.valueOf(mins);
		}
		
		String time = hoursString + ":" + minsString; 
		
		return time;
	}
	
	public int getPeakHours(){
		long offpeakEnd = getOffpeakEndTime();
		long offpeakStart = getOffpeakStartTime();
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(offpeakEnd);
		int start = cal1.get(Calendar.HOUR_OF_DAY);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(offpeakStart);
		int end = cal2.get(Calendar.HOUR_OF_DAY);

		if (end > start){
			return (24 - ( end - start));
		} else {
			return (24 - ( start - end));
		}
	}
	
	public String getPeakHourString(){
		int hours = getPeakHours();
		return String.valueOf(hours);
	}
	
	public int getOffpeakHours(){
		return (24 - getPeakHours());
	}
	
	public String getOffpeakHourString(){
		int hours = getOffpeakHours();
		return String.valueOf(hours);
	}
	
	/**
	 * Check if off peak start time is set
	 * @return true of string length is greater then 0
	 */
	public boolean isOffpeakEndSet(){
		// Check if string length is greater then 0
		if (getOffpeakEndTime() != 0){
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
	
	public long getPeakQuotaMb(){
		return (getPeakQuota() / MB);
	}
	
	public long getPeakQuotaDailyMb(){
		long quota = getPeakQuotaMb() * 12;
		long days = 360;
		
		if (days > 0 && quota > 0){
			return ( quota / days);
		} else {
			return 0;
		}
	}
	
	public long getPeakQuotaHourlyMb(){
		long quota = getPeakQuotaDailyMb();
		long hours = getPeakHours();
		
		if (hours > 0 && quota > 0){
			return ( quota / hours);
		} else {
			return 0;
		}
	}
	
	public long getPeakQuotaDailyGb(){
		long quota = getPeakQuotaGb() * 12;
		long days = 360;
		return ( quota / days);
	}
	
	public String getPeakQuotaString(){
		long quota = getPeakQuotaGb();
		
		if (quota < 10){
			return "/ 0" + Long.toString(quota) + " (Gb) Peak";
		} else {
			return "/ " + Long.toString(quota) + " (Gb) Peak";
		}
		
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
	
	public long getOffpeakQuotaMb(){
		return (getOffpeakQuota() / MB);
	}
	
	public long getOffpeakQuotaDailyMb(){
		long quota = getOffpeakQuotaMb() * 12;
		long days = 360;
		
		if (days > 0 && quota > 0){
			return ( quota / days);
		} else {
			return 0;
		}
	}
	
	public long getOffpeakQuotaHourlyMb(){
		long quota = getOffpeakQuotaDailyMb();
		long hours = getOffpeakHours();
		
		if (hours > 0 && quota > 0){
			return ( quota / hours);
		} else {
			return 0;
		}
	}
	
	public long getOffpeakQuotaDailyGb(){
		long quota = getOffpeakQuotaGb() * 12;
		long days = 360;
		return ( quota / days);
	}
	
	public String getOffpeakQuotaString(){
		
		long quota = getOffpeakQuotaGb();
		
		if (quota < 10){
			return "/ 0" + Long.toString(quota) + " (Gb) Offpeak";
		} else {
			return "/ " + Long.toString(quota) + " (Gb) Offpeak";
		}
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
	
	public boolean isNowOffpeakTime(){
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(getOffpeakStartTime());
		
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(getOffpeakEndTime());
		
		Calendar now = Calendar.getInstance();

		int hourStart = start.get(Calendar.HOUR_OF_DAY);
		int hourEnd = end.get(Calendar.HOUR_OF_DAY);
		int hourNow = now.get(Calendar.HOUR_OF_DAY);
		
		if (hourNow > hourStart && hourNow < hourEnd){
			return true;
		} else {
			return false;
		}	
	}
	
	public String getDataPeriodString(){
		if (isNowOffpeakTime()){
			return "Currently: <b>Offpeak</b>";
		} else {
			return "Currently: <b>Peak</b>";
		}
	}
	
	public String getPeriodString(){
		if (isNowOffpeakTime()){
			return "OFFPEAK";
		} else {
			return "PEAK";
		}
	}

}
