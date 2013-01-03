package au.id.teda.broadband.usage.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.helper.NotificationHelper;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
	
	private static final String DEBUG_TAG = "bbusage";

    private final Context mContext;
    private final NetworkUtilities mNetworkUtilities;
    private static Handler mActivityHandler;
    private final NotificationHelper mNotificationHelper;
    
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        
        mNetworkUtilities = new NetworkUtilities(mContext);
        mNotificationHelper = new NotificationHelper(mContext);
    }

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		
		Log.d(DEBUG_TAG, "onPerformSync");
		mNetworkUtilities.syncXmlData(syncHandler);
	}
	
	public void requestSync(Handler handler){
		mActivityHandler = handler;
		
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
        		if (mActivityHandler != null){
        			mActivityHandler.sendEmptyMessage(NetworkUtilities.HANDLER_START_ASYNC_TASK);
        		}
        		Log.d(DEBUG_TAG, "SyncAdapter HANDLER_START_ASYNC_TASK");
        		break;
        	case NetworkUtilities.HANDLER_COMPLETE_ASYNC_TASK:
        		if (mActivityHandler != null){
        			mActivityHandler.sendEmptyMessage(NetworkUtilities.HANDLER_COMPLETE_ASYNC_TASK);
        		}
        		Log.d(DEBUG_TAG, "SyncAdapter HANDLER_COMPLETE_ASYNC_TASK");
        		mNotificationHelper.checkStatus();
        		break;
        	}
        }
    };

}
