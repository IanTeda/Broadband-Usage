package au.id.teda.broadband.usage.fragments;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.activity.BaseActivity;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.helper.LayoutHelper;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class BaseFragment extends SherlockFragment {

	protected final static String DEBUG_TAG = BaseActivity.DEBUG_TAG;
			
	// Helper classes
	protected AccountInfoHelper mAccountInfo;
	protected AccountStatusHelper mAccountStatus;
	protected AccountAuthenticator mAccountAuthenticator;
	protected LayoutHelper mLayoutHelper;
	
	// Activity shared preferences
	protected SharedPreferences mSettings;
	protected SharedPreferences.Editor mEditor;
			
	// Receive sync broadcasts
	protected SyncReceiver mSyncReceiver;
	protected IntentFilter filter;
			
	// Activity context to be used
	protected Context mContext;

    protected int GB = 1000000000;


    /**
	 * Called 1st in the fragment life cycle
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
				
		// Load helper classes
		mAccountInfo = new AccountInfoHelper(activity);
		mAccountStatus = new AccountStatusHelper(activity);
		mAccountAuthenticator = new AccountAuthenticator(activity);
		mLayoutHelper = new LayoutHelper(activity);
		
		// Set up shared preferences
		mSettings = PreferenceManager.getDefaultSharedPreferences(activity);
    	mEditor = mSettings.edit();
	}
			
	/**
	 * Called 2nd in the fragment life cycle
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		// Set context for fragment. 
		// Activity extends context so we get it from there
		mContext = getActivity();
				
		// Setup broadcast receiver for background sync
		String BROADCAST = getString(R.string.sync_broadcast_action);
		filter = new IntentFilter(BROADCAST);
		mSyncReceiver = new SyncReceiver();
	}
			
	/**
	 * Called 4th in the fragment life cycle
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        if (mAccountInfo.isInfoSet() && mAccountStatus.isStatusSet()) {
		    loadFragmentView();
        }
	}
			
	/**
	 * Called 5th in the fragment life cycle
	 */
	@Override
	public void onResume() {
		super.onResume();
				
		// Register broadcast receiver for background sync
		getActivity().registerReceiver(mSyncReceiver, filter);
	}
			
	/**
	* Called 1st in the death of fragment
	*/
	@Override
	public void onPause() {
		super.onPause();
				
		// Unregister broadcast receiver for background sync
		getActivity().unregisterReceiver(mSyncReceiver);
	}
	
	// Return formated string value for int stored in db
	protected String IntUsageToString (long usage){
		NumberFormat numberFormat = new DecimalFormat("#,###");
		return numberFormat.format(usage);
		
	}


    protected String dataToGbString(long data){
        long dataGb = (data / GB);

        String used = Long.toString(dataGb);
        if (dataGb < 10 ){
            used = "0" + used;
        }
        return used;
    }
			
	protected abstract void loadFragmentView();
			
	private class SyncReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent i) {
		            
			String MESSAGE = getString(R.string.sync_broadcast_message);
			String SYNC_START = getString(R.string.sync_broadcast_start);
			String SYNC_COMPLETE = getString(R.string.sync_broadcast_complete);
		            
			String msg = i.getStringExtra(MESSAGE);
			if (msg.equals(SYNC_START)){
				// Nothing to do see here move along
			} else if (msg.equals(SYNC_COMPLETE)){
                if (mAccountInfo.isInfoSet() && mAccountStatus.isStatusSet()) {
                    loadFragmentView();
                }
            }
		}
		         
	}

}
