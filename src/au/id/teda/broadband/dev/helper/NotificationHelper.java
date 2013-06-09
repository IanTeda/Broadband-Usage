package au.id.teda.broadband.dev.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import au.id.teda.broadband.dev.R;
import au.id.teda.broadband.dev.activity.MainActivity;

// TODO: Improve multiply notification (i.e. show as one)
public class NotificationHelper {

	//public static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	private static final String KEY_NOTIFICATION_PERIOD = "notification_period";
	private static final String KEY_NOTIFY_END_OF_PERIOD_NEAR = "notify_end_of_period_near";
	private static final String KEY_NOTIFY_END_OF_PERIOD_OVER = "notify_end_of_period_over";
    private static final String KEY_NOTIFY_ANYTIME_QUOTA_NEAR = "notify_anytime_quota_near";
    private static final String KEY_NOTIFY_ANYTIME_QUOTA_OVER = "notify_anytime_quotat_over";
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
		this.mContext = context;
		
		mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
    	mEditor = mSettings.edit();
    	
    	mInfo = new AccountInfoHelper(mContext);
    	mStatus = new AccountStatusHelper(mContext);
	}
	
	public void checkStatus(){
		
		//resetNotificationStatus();
		
		if (isEndOfPeriodNear() 
				&& !isEndOfPeriodNearNotified()
				&& showEndOfPeriodNearNotification()) {

			notifyEndOfPeriodNear();
		}
		
		if (isNewPeriod() 
				&& !isEndOfPeriodOverNotified()
				&& showEndOfPeriodOverNotification()){

			notifyEndOfPeriodOver();
			resetNotificationStatus();
		}

        if (isAnyTimeQuotaNear()
                && !isAnyTimeQuotaNearNotified()
                && showAnyTimeQuotaNearNotification()
                && mInfo.isAccountAnyTime()){

            notifyAnyTimeDataNear();
        }

        if (isAnyTimeQuotaOver()
                && !isAnyTimeQuotaOverNotified()
                && showAnyTimeQuotaOverNotification()
                && mInfo.isAccountAnyTime()){

            notifyAnyTimeQuotaOver();
        }

        if (isPeakQuotaNear()
				&& !isPeakQuotaNearNotified()
				&& showPeakQuotaNearNotification()
                && !mInfo.isAccountAnyTime()){

			notifyPeakDataNear();
		}
		
		if (isPeakQuotaOver() 
				&& !isPeakQuotaOverNotified()
				&& showPeakQuotaOverNotification()
                && !mInfo.isAccountAnyTime()){

			notifyPeakQuotaOver();
		} 
		
		if (isOffpeakQuotaNear() 
				&& !isOffpeakQuotaNearNotified()
				&& showOffpeakQuotaNearNotification()
                && !mInfo.isAccountAnyTime()){

			notifyOffpeakDataNear();
		}
		
		if (isOffpeakQuotaOver() 
				&& !isOffpeakQuotaOverNotified()
				&& showOffpeakQuotaOverNotification()
                && !mInfo.isAccountAnyTime()){
			
			notifyOffpeakQuotaOver();
		}
	}
	
	private void resetNotificationStatus(){
		
		String period = mStatus.getCurrentMonthString();
		setNotificationPeriod(period);
		setEndOfPeriodNearNotified(false);
		setEndOfPeriodOverNotified(false);
        setAnyTimeQuotaNearNotified(false);
        setAnyTimeQuotaOverNotified(false);
		setPeakQuotaNearNotified(false);
		setPeakQuotaOverNotified(false);
		setOffpeakQuotaNearNotified(false);
		setOffpeakQuotaOverNotified(false);
	}

    /**
     * Start period notification methods
     */

	private void setNotificationPeriod(String period){
		mEditor.putString(KEY_NOTIFICATION_PERIOD, period);
		mEditor.commit();
	}
	
	private String getNotificationPeriod(){
		return mSettings.getString(KEY_NOTIFICATION_PERIOD, "");
	}
	
	private String getEndPeriodNearDays(){
		return mSettings.getString(mContext.getString(R.string.pref_notify_days2go_array_key), "seven_days");
	}
	
	private boolean isEndOfPeriodNear(){
		
		String days = getEndPeriodNearDays();
		long warning = daysStringtoMillis(days);
		long daysToGo = mStatus.getDaysToGoMillis();
		
		if (daysToGo > warning){
			return false;
		} else {
			return true;
		}
	}
	
	private boolean showEndOfPeriodNearNotification(){
		String days = getEndPeriodNearDays();
		if (days.equals("never")){
			return false;
		} else {
			return true;
		}
	}

	private void notifyEndOfPeriodNear(){
		
		// TODO: Add long message with download stats remaining
		String title = mContext.getString(R.string.notification_end_of_period_near_title);
		String message = mStatus.getDaysToGoString() + " " + mContext.getString(R.string.notification_end_of_period_near_message);
			
		showNotificaiton(title, message, 1);
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

	public void setEndOfPeriodOverNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_END_OF_PERIOD_OVER, flag);
		mEditor.commit();
	}
	
	private boolean showEndOfPeriodOverNotification(){
		return mSettings.getBoolean(mContext.getString(R.string.pref_notify_new_period_checkbox_key), true);
	}
	
	private void notifyEndOfPeriodOver(){
		String title = mContext.getString(R.string.notification_end_of_period_over_title);
		String message = mContext.getString(R.string.notification_end_of_period_over_message);
		showNotificaiton(title, message, 2);
		setEndOfPeriodOverNotified(true);
	}
	
	private boolean isEndOfPeriodOverNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_END_OF_PERIOD_OVER, false);
	}

    /**
     *  Start anytime notification methods
     */

    private String getAnyTimeQuotaNearWarning(){
        return mSettings.getString(mContext.getString(R.string.pref_notify_anytime_near_array_key), "five_gb");
    }

    public boolean isAnyTimeQuotaNear(){
        String value = getAnyTimeQuotaNearWarning();
        long warning = gbStringToLong(value);
        long remaining = mStatus.getAnyTimeDataRemaining();

        if (remaining < warning && remaining != 0){
            return true;
        } else {
            return false;
        }
    }

    private boolean showAnyTimeQuotaNearNotification(){
        String value = getAnyTimeQuotaNearWarning();

        if (value.equals("never")){
            return false;
        } else {
            return true;
        }
    }

    private void notifyAnyTimeDataNear(){

        // TODO: Add long message with download stats remaining
        String title = mContext.getString(R.string.notification_peak_data_near_title);
        String message = mStatus.getAnyTimeDataRemaingGbString() + " " + mContext.getString(R.string.notification_anytime_data_near_message);

        showNotificaiton(title, message, 3);
        setAnyTimeQuotaNearNotified(true);
    }

    private void setAnyTimeQuotaNearNotified(boolean flag){
        mEditor.putBoolean(KEY_NOTIFY_ANYTIME_QUOTA_NEAR, flag);
        mEditor.commit();
    }

    private boolean isAnyTimeQuotaNearNotified(){
        return mSettings.getBoolean(KEY_NOTIFY_ANYTIME_QUOTA_NEAR, false);
    }

    public boolean isAnyTimeQuotaOver(){
        long quota = mInfo.getAnyTimeQuota();
        long used = mStatus.getAnyTimeDataUsed();

        if (used > quota){
            return true;
        } else {
            return false;
        }
    }

    private boolean showAnyTimeQuotaOverNotification(){
        return mSettings.getBoolean(mContext.getString(R.string.pref_notify_anytime_over_checkbox_key), true);
    }

    private void notifyAnyTimeQuotaOver(){
        String title = mContext.getString(R.string.notification_anytime_data_over_title);
        String message = mStatus.getDaysToGoString() + " " + mContext.getString(R.string.notification_anytime_data_over_message);
        showNotificaiton(title, message, 4);
        setAnyTimeQuotaOverNotified(true);
    }

    private void setAnyTimeQuotaOverNotified(boolean flag){
        mEditor.putBoolean(KEY_NOTIFY_ANYTIME_QUOTA_OVER, flag);
        mEditor.commit();
    }

    private boolean isAnyTimeQuotaOverNotified(){
        return mSettings.getBoolean(KEY_NOTIFY_ANYTIME_QUOTA_OVER, false);
    }

    /**
     *  Start peak notification methods
     */

	private String getPeakQuotaNearWarning(){
		return mSettings.getString(mContext.getString(R.string.pref_notify_peak_near_array_key), "five_gb");
	}
	
	public boolean isPeakQuotaNear(){
		String value = getPeakQuotaNearWarning();
		long warning = gbStringToLong(value);
		long remaining = mStatus.getPeakDataRemaining();
		
		if (remaining < warning && remaining != 0){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean showPeakQuotaNearNotification(){
		String value = getPeakQuotaNearWarning();
		
		if (value.equals("never")){
			return false;
		} else {
			return true;
		}
	}
	
	private void notifyPeakDataNear(){
		
		// TODO: Add long message with download stats remaining
		String title = mContext.getString(R.string.notification_peak_data_near_title);
		String message = mStatus.getPeakDataRemaingGbString() + " " + mContext.getString(R.string.notification_peak_data_near_message);
			
		showNotificaiton(title, message, 3);
		setPeakQuotaNearNotified(true);
	}
	
	private void setPeakQuotaNearNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_PEAK_QUOTA_NEAR, flag);
		mEditor.commit();
	}
	
	private boolean isPeakQuotaNearNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_PEAK_QUOTA_NEAR, false);
	}
	
	public boolean isPeakQuotaOver(){
		long quota = mInfo.getPeakQuota();
		long used = mStatus.getPeakDataUsed();
		
		if (used > quota){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean showPeakQuotaOverNotification(){
		return mSettings.getBoolean(mContext.getString(R.string.pref_notify_peak_over_checkbox_key), true);
	}
	
	private void notifyPeakQuotaOver(){
		String title = mContext.getString(R.string.notification_peak_data_over_title);
		String message = mStatus.getDaysToGoString() + " " + mContext.getString(R.string.notification_peak_data_over_message);
		showNotificaiton(title, message, 4);
		setPeakQuotaOverNotified(true);
	}
	
	private void setPeakQuotaOverNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_PEAK_QUOTA_OVER, flag);
		mEditor.commit();
	}
	
	private boolean isPeakQuotaOverNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_PEAK_QUOTA_OVER, false);
	}

    /**
     *  Start off peak notification methods
     */
	
	private String getOffpeakQuotaWarning(){
		return mSettings.getString(mContext.getString(R.string.pref_notify_offpeak_near_array_key), "five_gb"); 
	}
	
	public boolean isOffpeakQuotaNear(){
		String value = getOffpeakQuotaWarning();
		long warning = gbStringToLong(value);
		long remaining = mStatus.getOffpeakDataRemaining();
				
		if (remaining < warning && remaining != 0){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean showOffpeakQuotaNearNotification(){
		String value = getOffpeakQuotaWarning();
		
		if (value.equals("never")){
			return false;
		} else {
			return true;
		}
	}
	
	private void notifyOffpeakDataNear(){
		
		// TODO: Add long message with download stats remaining
		String title = mContext.getString(R.string.notification_offpeak_data_near_title);
		String message = mStatus.getOffpeakDataRemaingGbString() + " " + mContext.getString(R.string.notification_offpeak_data_near_message);
			
		showNotificaiton(title, message, 5);
		setOffpeakQuotaNearNotified(true);
	}
	
	private void setOffpeakQuotaNearNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_NEAR, flag);
		mEditor.commit();
	}
	
	private boolean isOffpeakQuotaNearNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_NEAR, false);
	}
	
	public boolean isOffpeakQuotaOver(){
		long quota = mInfo.getOffpeakQuota();
		long used = mStatus.getOffpeakDataUsed();
		
		if (used > quota){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean showOffpeakQuotaOverNotification(){
		return mSettings.getBoolean(mContext.getString(R.string.pref_notify_offpeak_over_checkbox_key), true);
	}
	
	private void notifyOffpeakQuotaOver(){
		String title = mContext.getString(R.string.notification_offpeak_data_over_title);
		String message = mStatus.getDaysToGoString() + " " + mContext.getString(R.string.notification_offpeak_data_over_message);
		showNotificaiton(title, message, 6);
		setOffpeakQuotaOverNotified(true);
	}
	
	private void setOffpeakQuotaOverNotified(boolean flag){
		mEditor.putBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_OVER, flag);
		mEditor.commit();
	}
	
	private boolean isOffpeakQuotaOverNotified(){
		return mSettings.getBoolean(KEY_NOTIFY_OFFPEAK_QUOTA_OVER, false);
	}

    /**
     * Start helper methods
     */

	private long daysStringtoMillis(String value){
      
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
	
	private void showNotificaiton(String title, String message, int id){
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
		mBuilder.setSmallIcon(R.drawable.ic_notification);
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
		int mNotificationId = id;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());
	}

}
