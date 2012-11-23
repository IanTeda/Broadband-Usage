package au.id.teda.broadband.usage.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
	
	private static final String DEBUG_TAG = "bbusage";
	
    private final AccountManager mAccountManager;

    private final Context mContext;
    
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		// TODO Auto-generated method stub
		String mUsername = account.name;
		String mPassword = mAccountManager.getPassword(account);
		Log.d(DEBUG_TAG, "SyncAdapter.onPerformSync: " + mUsername + " / " + mPassword); 
		
	}

}
