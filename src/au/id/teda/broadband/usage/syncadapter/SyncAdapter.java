package au.id.teda.broadband.usage.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.helper.NotificationHelper;
import au.id.teda.broadband.usage.ui.MainActivity;
import au.id.teda.broadband.usage.ui.SettingsActivity;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
	
	private static final String DEBUG_TAG = MainActivity.DEBUG_TAG;

    private final Context mContext;
    private final NetworkUtilities mNetworkUtilities;
    
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        
        mNetworkUtilities = new NetworkUtilities(mContext);
    }

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		
		Log.d(DEBUG_TAG, "onPerformSync");
		mNetworkUtilities.syncXmlData(syncHandler);
	}
	
	public void requestSync(){
		AccountAuthenticator mAccountAuthenticator = new AccountAuthenticator(mContext);
		ContentResolver.requestSync (mAccountAuthenticator.getAccount(), DummyContentProvider.PROVIDER, new Bundle());
	}
	
	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler syncHandler = new Handler() {
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

}
