package au.id.teda.broadband.usage.syncadapter;

import java.util.Calendar;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.helper.NotificationHelper;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
	
	//private static final String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	public final static String PREF_LAST_SYNC_KEY = "last_sync_timestamp";

    private final Context mContext;
    
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;

    }

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		
		NetworkUtilities mNetworkUtilities = new NetworkUtilities(mContext);
		mNetworkUtilities.syncXmlData(handler);
	}
	
	public void requestSync(){
		AccountAuthenticator mAccountAuthenticator = new AccountAuthenticator(mContext);
		ContentResolver.requestSync (mAccountAuthenticator.getAccount(), DummyContentProvider.PROVIDER, new Bundle());
	}
	
	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	switch (msg.what) {
        	case NetworkUtilities.HANDLER_START_ASYNC_TASK:
        		
        		// Send broadcast sync started
        		String start = mContext.getString(R.string.sync_broadcast_start);
        		sendBroadcastMessage(start);
        		break;
        	case NetworkUtilities.HANDLER_COMPLETE_ASYNC_TASK:
        		
        		String complete = mContext.getString(R.string.sync_broadcast_complete);
        		sendBroadcastMessage(complete);
        		
        		NotificationHelper mNotificationHelper = new NotificationHelper(mContext);
        		mNotificationHelper.checkStatus();
        		
        		setSyncTimeStamp();
        		break;
        	}
        }
    };
    
    private void sendBroadcastMessage(String msg){
    	String BROADCAST = mContext.getString(R.string.sync_broadcast_action);
    	String MESSAGE = mContext.getString(R.string.sync_broadcast_message);
    	
    	Intent i = new Intent(BROADCAST);
    	i.putExtra(MESSAGE, msg);
    	mContext.sendBroadcast(i);
    }
    
    private void setSyncTimeStamp(){
    	
    	SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor mEditor = mSettings.edit();
    	
        // Get current date/time
        Calendar now = Calendar.getInstance();
        long nowInMillis = now.getTimeInMillis();
       
        // Put into shared prefferences
        mEditor.putLong(PREF_LAST_SYNC_KEY, nowInMillis);
        mEditor.commit();
    }

}
