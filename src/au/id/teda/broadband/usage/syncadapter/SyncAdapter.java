package au.id.teda.broadband.usage.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import au.id.teda.broadband.usage.helper.NotificationHelper;
import au.id.teda.broadband.usage.ui.MainActivity;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
	
	private static final String DEBUG_TAG = "bbusage";

    private final Context mContext;
    
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		
		Log.d(DEBUG_TAG, "onPerformSync: " + authority );
		
		NetworkUtilities mNetworkUtilities = new NetworkUtilities(mContext);
		mNetworkUtilities.syncXmlData(handler);
		
		
	}
	
	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	switch (msg.what) {
        	case MainActivity.HANDLER_STOP_REFRESH_ANIMATION:
        		NotificationHelper mNotificationHelper = new NotificationHelper(mContext);
        		mNotificationHelper.showNotificaiton();
        		break;	
        	}   	
        }
    };

}
