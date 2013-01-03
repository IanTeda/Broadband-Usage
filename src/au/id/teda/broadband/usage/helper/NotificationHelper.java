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
		String period = mStatus.getCurrentMonthString();
		
		if (isNewPeriod(period)){
			resetNotificationStatus(period);
		}
	}
	
	private boolean isNewPeriod(String period){
		if (period.equals(getNotificationPeriod())){
			return false;
		} else {
			return true;
		}
		
	}
	
	private void resetNotificationStatus(String period){
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
	
	private void setEndOfPeriodNearNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_END_OF_PERIOD_NEAR, flag);
		mEditor.commit();
	}
	
	private boolean isEndOfPeriodNear(){
		int daysToGo = mStatus.getDaysToGo();
		return false;
	}
	
	private boolean isEndOfPeriodNearNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_END_OF_PERIOD_NEAR, false);
	}
	
	private void setEndOfPeriodOverNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_END_OF_PERIOD_OVER, flag);
		mEditor.commit();
	}
	
	private boolean isEndOfPeriodOverNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_END_OF_PERIOD_OVER, false);
	}
	
	private void setPeakQuotaNearNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_PEAK_QUOTA_NEAR, flag);
		mEditor.commit();
	}
	
	private boolean isPeakQuotaNearNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_PEAK_QUOTA_NEAR, false);
	}
	
	private void setPeakQuotaOverNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_PEAK_QUOTA_OVER, flag);
		mEditor.commit();
	}
	
	private boolean isPeakQuotaOverNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_PEAK_QUOTA_OVER, false);
	}
	
	private void setOffpeakQuotaNearNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_NEAR, flag);
		mEditor.commit();
	}
	
	private boolean isOffpeakQuotaNearNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_NEAR, false);
	}
	
	private void setOffpeakQuotaOverNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_OVER, flag);
		mEditor.commit();
	}
	
	private boolean isOffpeakQuotaOverNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_OVER, false);
	}
	
	private void showNotificaiton(){
		Log.d(DEBUG_TAG, "NotificationHelper.showNotificaiton()");
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
		mBuilder.setSmallIcon(R.drawable.ic_iinet_logo);
		mBuilder.setContentTitle("My notification");
		mBuilder.setContentText("Hello World!");
		
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
