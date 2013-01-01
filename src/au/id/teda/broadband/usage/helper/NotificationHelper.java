package au.id.teda.broadband.usage.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.MainActivity;

public class NotificationHelper {

	public static final String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	private final Context mContext;
	
	public NotificationHelper(Context context) {
		mContext = context;
		
		Log.d(DEBUG_TAG, "NotificationHelper");
	}
	
	public void checkDaysToGo(){
		
	}
	
	public void showNotificaiton(){
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
