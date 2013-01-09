package au.id.teda.broadband.usage.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
	
	//private static final String DEBUG_TAG = MainActivity.DEBUG_TAG;

    private final Context mContext;
    
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;

    }

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		
		NetworkUtilities mNetworkUtilities = new NetworkUtilities(mContext);
		mNetworkUtilities.syncXmlData();
	}

}
