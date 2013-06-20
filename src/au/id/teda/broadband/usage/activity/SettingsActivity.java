package au.id.teda.broadband.usage.activity;

import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.widget.CheckBox;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.syncadapter.DummyContentProvider;

// For +3.0 this should be preference fragment

public class SettingsActivity extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {
	
	//private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	public static final long HALF_HOUR = 60 * 30;
	public static final long HOUR = HALF_HOUR * 2;
	public static final long THREE_HOURS = HOUR * 3;
	public static final long SIX_HOURS = THREE_HOURS * 2;
	public static final long TWELVE_HOURS = SIX_HOURS * 2;
	public static final long TWENTY_FOUR_HOURS = TWELVE_HOURS * 2;
	
	// Activity shared preferences
    SharedPreferences mSettings;
    private AccountInfoHelper mInfo;


    private ListPreference mFreqListPreference;
    private ListPreference mDaysToGoListPreference;
    private ListPreference mAnyTimeNearPreference;
    private ListPreference mPeakNearPreference;
    private ListPreference mOffpeakNearPreference;

    private CheckBox mAnyTimeOverCheckBox;
    private CheckBox mPeakOverCheckBox;
    private CheckBox mOffpeakOverCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loads the XML preferences file.
        addPreferencesFromResource(R.xml.preferences);
        
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        
        mFreqListPreference = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_sync_freq_key));
        mDaysToGoListPreference = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_notify_days2go_array_key));
        mAnyTimeNearPreference = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_notify_anytime_near_array_key));
        mPeakNearPreference = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_notify_peak_near_array_key));
        mOffpeakNearPreference = (ListPreference) getPreferenceScreen().findPreference(this.getString(R.string.pref_notify_offpeak_near_array_key));
    
        // Show home (up) button on action bar
     	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Preference mAnytimeCheckBox = findPreference(this.getString(R.string.pref_notify_anytime_over_checkbox_key));
        Preference mPeakCheckBox = findPreference(this.getString(R.string.pref_notify_peak_over_checkbox_key));
        Preference mOffpeakCheckBox = findPreference(this.getString(R.string.pref_notify_offpeak_over_checkbox_key));

        PreferenceScreen preferenceScreen = getPreferenceScreen();

        mInfo = new AccountInfoHelper(this);
        if (mInfo.isAccountAnyTime()){
            preferenceScreen.removePreference(mPeakCheckBox);
            preferenceScreen.removePreference(mPeakNearPreference);
            preferenceScreen.removePreference(mOffpeakCheckBox);
            preferenceScreen.removePreference(mOffpeakNearPreference);
        } else {
            preferenceScreen.removePreference(mAnytimeCheckBox);
            preferenceScreen.removePreference(mAnyTimeNearPreference);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Registers a callback to be invoked whenever a user changes a preference.
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        
        setListPreferenceSummaries();
    }

	private void setListPreferenceSummaries() {
		mFreqListPreference.setSummary(mFreqListPreference.getEntry());
        mDaysToGoListPreference.setSummary(mDaysToGoListPreference.getEntry());
        mAnyTimeNearPreference.setSummary(mAnyTimeNearPreference.getEntry());
        mPeakNearPreference.setSummary(mPeakNearPreference.getEntry());
        mOffpeakNearPreference.setSummary(mOffpeakNearPreference.getEntry());
	}
    
    @Override
    protected void onPause() {
        super.onPause();

        // Unregisters the listener set in onResume().
        // It's best practice to unregister listeners when your app isn't using them to cut down on
        // unnecessary system overhead. You do this in onPause().
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		setListPreferenceSummaries();
		
		if (key.equals(this.getString(R.string.pref_sync_freq_key))){
			setSyncFrequency(key);
			mFreqListPreference.setSummary(mFreqListPreference.getEntry());
		}
		
		
	}
	
    // Handle options menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case android.R.id.home:
	            // This is called when the Home (Up) button is pressed in the Action Bar.
	            Intent mMainActivityInetnt = new Intent(this, MainActivity.class);
	            mMainActivityInetnt.addFlags(
	                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
	                    Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(mMainActivityInetnt);
	            finish();
	            return true;
        	default:
                return super.onOptionsItemSelected(item);
        }
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
	
	/**
	 * @param key
	 */
	private void setSyncFrequency(String key) {
		long freq;
		String pref_value = mSettings.getString(key, "twenty_four_hour");
		if (pref_value.equals("half_hour")){
			freq = HALF_HOUR;
		} else if (pref_value.equals("hour")){
			freq = HOUR;
		} else if (pref_value.equals("three_hours")){
			freq = THREE_HOURS;
		} else if (pref_value.equals("six_hours")){
			freq = SIX_HOURS;
		} else if (pref_value.equals("twelve_hours")){
			freq = TWELVE_HOURS;
		} else {
			freq = TWENTY_FOUR_HOURS;
		}
		
		// Get accounts based on account type
		AccountManager accountManager = AccountManager.get(this);
		Account account = getAccount(accountManager);

		ContentResolver.addPeriodicSync(account, DummyContentProvider.PROVIDER, new Bundle(), freq);
	} 
	
}
