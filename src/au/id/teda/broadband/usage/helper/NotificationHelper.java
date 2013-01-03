package au.id.teda.broadband.usage.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.MainActivity;

public class NotificationHelper {

	public static final String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	private static final String KEY_NOTIFICATION_PERIOD = "notification_period";
	private static final String KEY_NOTIFY_END_OF_PERIOD_NEAR = "notify_end_of_period_near";
	private static final String KEY_NOTIFY_END_OF_PERIOD_OVER = "notify_end_of_period_over";
	private static final String KEY_NOTIFY_PEAK_QUOTA_NEAR = "notify_peak_quota_near";
	private static final String KEY_NOTIFY_PEAK_QUOTA_OVER = "notify_peak_quota_over";
	private static final String KEY_NOTIFY_OFFPEAK_QUOTA_NEAR = "notify_offpeak_quota_near";
	private static final String KEY_NOTIFY_OFFPEAK_QUOTA_OVER = "notify_offpeak_quota_over";
	
	private static final long DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
	private static final long ONE_DAY = DAY_IN_MILLISECONDS;
	private static final long TWO_DAYS = DAY_IN_MILLISECONDS * 2;
	private static final long THREE_DAYS = DAY_IN_MILLISECONDS * 3;
	private static final long SEVEN_DAYS = DAY_IN_MILLISECONDS * 7;
	private static final long FOURTEEN_DAYS = DAY_IN_MILLISECONDS * 14;
	
	private final static long GB = 1000000000;
	private static final long ONE_GB = 1 * GB;
	private static final long TWO_GB = 2 * GB;
	private static final long FIVE_GB = 5 * GB;
	private static final long TEN_GB = 10 * GB;
	
	
	private final Context mContext;
	
    // Activity shared preferences
    private final SharedPreferences mSettings;
    private final SharedPreferences.Editor mEditor;
    
    private final AccountInfoHelper mInfo;
    private final AccountStatusHelper mStatus;
	
	public NotificationHelper(Context context) {
		mContext = context;
		
		mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
    	mEditor = mSettings.edit();
    	
    	mInfo = new AccountInfoHelper(mContext);
    	mStatus = new AccountStatusHelper(mContext);
		
		Log.d(DEBUG_TAG, "NotificationHelper");
	}
	
	public void checkStatus(){
		
		if (isEndOfPeriodNear() && !isEndOfPeriodNearNotified()) {
			Log.d(DEBUG_TAG, "End of period near");
			notifyEndOfPeriodNear();
		} else if (isNewPeriod() && !isEndOfPeriodOverNotified()){
			Log.d(DEBUG_TAG, "New period");
			notifyEndOfPeriodOver();
			resetNotificationStatus();
		} else if (isPeakQuotaNear() && !isPeakQuotaNearNotified()){
			Log.d(DEBUG_TAG, "Peak quota near");
			notifyPeakDataNear();
		} else if (isPeakQuotaOver() && !isPeakQuotaOverNotified()){
			Log.d(DEBUG_TAG, "Peak quota over");
			notifyPeakQuotaOver();
		} else if (isOffpeakQuotaNear() && !isOffpeakQuotaNearNotified()){
			Log.d(DEBUG_TAG, "Offpeak quota near");
			notifyOffpeakDataNear();
		} else if (isOffpeakQuotaOver() && !isOffpeakQuotaOverNotified()){
			Log.d(DEBUG_TAG, "Offpeak quota over");
			notifyOffpeakQuotaOver();
		}
	}
	
	private void resetNotificationStatus(){
		String period = mStatus.getCurrentMonthString();
		setNotificationPeriod(period);
		setEndOfPeriodNearNotified(false);
		setEndOfPeriodOverNotified(false);
		setPeakQuotaNearNotified(false);
		setPeakQuotaOverNotified(false);
		setOffpeakQuotaNearNotified(false);
		setOffpeakQuotaOverNotified(false);
	}
	
	private void setNotificationPeriod(String period){
		mEditor.putString(KEY_NOTIFICATION_PERIOD, period);
		mEditor.commit();
	}
	
	private String getNotificationPeriod(){
		return mSettings.getString(KEY_NOTIFICATION_PERIOD, "");
	}
	
	private boolean isEndOfPeriodNear(){
		
		String days = mSettings.getString(mContext.getString(R.string.pref_notify_days2go_array_key), "seven_days");
		long warning = daysStringtoInt(days);
		long daysToGo = mStatus.getDaysToGoMillis();
		
		Log.d(DEBUG_TAG, "isEndOfPeriodNear > warning: " + warning + " / daysToGo: " + warning);
		
		if (daysToGo > warning){
			return false;
		} else {
			return true;
		}
	}

	private void notifyEndOfPeriodNear(){
		
		// TODO: Add long message with download stats remaining
		String title = mContext.getString(R.string.notification_end_of_period_near_title);
		String message = mStatus.getDaysToGoString() + " " + mContext.getString(R.string.notification_end_of_period_near_message);
			
		showNotificaiton(title, message);
		setEndOfPeriodNearNotified(true);
	}

	private void setEndOfPeriodNearNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_END_OF_PERIOD_NEAR, flag);
		mEditor.commit();
	}
	
	private boolean isEndOfPeriodNearNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_END_OF_PERIOD_NEAR, false);
	}
	
	private boolean isNewPeriod(){
		String period = mStatus.getCurrentMonthString();
		if (period.equals(getNotificationPeriod())){
			return false;
		} else {
			return true;
		}
	}

	private void setEndOfPeriodOverNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_END_OF_PERIOD_OVER, flag);
		mEditor.commit();
	}
	
	private void notifyEndOfPeriodOver(){
		String title = mContext.getString(R.string.notification_end_of_period_over_title);
		String message = mContext.getString(R.string.notification_end_of_period_over_message);
		showNotificaiton(title, message);
		setEndOfPeriodOverNotified(true);
	}
	
	private boolean isEndOfPeriodOverNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_END_OF_PERIOD_OVER, false);
	}
	
	private boolean isPeakQuotaNear(){
		String value = mSettings.getString(mContext.getString(R.string.pref_notify_peak_near_array_key), "five_gb");
		long warning = gbStringToLong(value);
		long used = mStatus.getPeakDataUsed();
		
		if (used > warning){
			return true;
		} else {
			return false;
		}
	}
	
	private void notifyPeakDataNear(){
		
		// TODO: Add long message with download stats remaining
		String title = mContext.getString(R.string.notification_peak_data_near_title);
		String message = mStatus.getPeakDataRemaingGbString() + " " + mContext.getString(R.string.notification_peak_data_near_message);
			
		showNotificaiton(title, message);
		setPeakQuotaNearNotified(true);
	}
	
	private void setPeakQuotaNearNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_PEAK_QUOTA_NEAR, flag);
		mEditor.commit();
	}
	
	private boolean isPeakQuotaNearNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_PEAK_QUOTA_NEAR, false);
	}
	
	private boolean isPeakQuotaOver(){
		Long quota = mInfo.getPeakQuota();
		long used = mStatus.getPeakDataUsed();
		
		if (used > quota){
			return true;
		} else {
			return false;
		}
	}
	
	private void notifyPeakQuotaOver(){
		String title = mContext.getString(R.string.notification_peak_data_over_title);
		String message = mContext.getString(R.string.notification_peak_data_over_message);
		showNotificaiton(title, message);
		setPeakQuotaOverNotified(true);
	}
	
	private void setPeakQuotaOverNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_PEAK_QUOTA_OVER, flag);
		mEditor.commit();
	}
	
	private boolean isPeakQuotaOverNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_PEAK_QUOTA_OVER, false);
	}
	
	
	private boolean isOffpeakQuotaNear(){
		String value = mSettings.getString(mContext.getString(R.string.pref_notify_offpeak_near_array_key), "five_gb");
		long warning = gbStringToLong(value);
		long used = mStatus.getOffpeakDataUsed();
		
		if (used > warning){
			return true;
		} else {
			return false;
		}
	}
	
	private void notifyOffpeakDataNear(){
		
		// TODO: Add long message with download stats remaining
		String title = mContext.getString(R.string.notification_offpeak_data_near_title);
		String message = mStatus.getOffpeakDataRemaingGbString() + " " + mContext.getString(R.string.notification_offpeak_data_near_message);
			
		showNotificaiton(title, message);
		setOffpeakQuotaNearNotified(true);
	}
	
	private void setOffpeakQuotaNearNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_NEAR, flag);
		mEditor.commit();
	}
	
	private boolean isOffpeakQuotaNearNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_NEAR, false);
	}
	
	private boolean isOffpeakQuotaOver(){
		Long quota = mInfo.getOffpeakQuota();
		long used = mStatus.getOffpeakDataUsed();
		
		if (used > quota){
			return true;
		} else {
			return false;
		}
	}
	
	private void notifyOffpeakQuotaOver(){
		String title = mContext.getString(R.string.notification_offpeak_data_over_title);
		String message = mContext.getString(R.string.notification_offpeak_data_over_message);
		showNotificaiton(title, message);
		setOffpeakQuotaOverNotified(true);
	}
	
	private void setOffpeakQuotaOverNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_OVER, flag);
		mEditor.commit();
	}
	
	private boolean isOffpeakQuotaOverNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_OVER, false);
	}
	
	private long daysStringtoInt(String value){
      
		if (value.equals("one_day")){
			return ONE_DAY;
		} else if (value.equals("two_days")){
			return TWO_DAYS;
		} else if (value.equals("three_days")){
			return THREE_DAYS;
		} else if (value.equals("seven_days")){
			return SEVEN_DAYS;
		} else if (value.equals("fourteen_days")){
			return FOURTEEN_DAYS;
		} else {
			return SEVEN_DAYS;
		}
		
	}
	
	private long gbStringToLong(String value){
		if (value.equals("one_gb")){
			return ONE_GB;
		} else if (value.equals("two_gb")){
			return TWO_GB;
		} else if (value.equals("five_gb")){
			return FIVE_GB;
		} else if (value.equals("ten_gb")){
			return TEN_GB;
		} else {
			return FIVE_GB;
		}
	}
	
	private void showNotificaiton(String title, String message){
		Log.d(DEBUG_TAG, "NotificationHelper.showNotificaiton()");
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
		mBuilder.setSmallIcon(R.drawable.ic_iinet_logo);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(message);
		
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(mContext, MainActivity.class);
		
		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		
		// Sets an ID for the notification
		int mNotificationId = 001;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
		
	}

}
