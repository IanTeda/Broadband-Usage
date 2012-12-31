package au.id.teda.broadband.usage.authenticator;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.syncadapter.DummyContentProvider;

public class AccountPreferences extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {
	
	
	private static final String DEBUG_TAG = "bbusage";
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.preferences_resources);
	}
	  
	@Override
	protected void onResume() {
		super.onResume();

		// Registers a callback to be invoked whenever a user changes a preference.
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	    
	@Override
	protected void onPause() {
		super.onPause();

		// Unregisters the listener set in onResume().
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	private static Account getAccount(AccountManager accountManager) {
	    Account[] accounts = accountManager.getAccountsByType(AccountAuthenticator.ACCOUNT_TYPE);
	    Account account;
	    if (accounts.length > 0) {
	      account = accounts[0];
	    } else {
	      account = null;
	    }
	    return account;
	  }
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		//Log.d(DEBUG_TAG, "AccountPreferences.onSharedPreferenceChanged(): " + sharedPreferences + " Key: " + key);

    	// Get accounts based on account type
		AccountManager accountManager = AccountManager.get(this);
	    Account account = getAccount(accountManager);

		ContentResolver.addPeriodicSync(account, DummyContentProvider.PROVIDER, new Bundle(), 10);
	
		Log.d(DEBUG_TAG, "onSharedPreferenceChanged");
		
		// Sets refreshDisplay to true so that when the user returns to the main
		// activity, the display refreshes to reflect the new settings.
		// NetworkActivity.refreshDisplay = true;
			
	} 

}
