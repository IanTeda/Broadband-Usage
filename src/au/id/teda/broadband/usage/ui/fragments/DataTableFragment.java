package au.id.teda.broadband.usage.ui.fragments;

import com.actionbarsherlock.app.SherlockFragment;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.database.DailyDataDatabaseAdapter;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.ui.MainActivity;
import au.id.teda.broadband.usage.util.DailyVolumeUsage;
import au.id.teda.broadband.usage.util.DailyVolumeUsageAdapter;

public class DataTableFragment extends SherlockFragment {

	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Helper classes
	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	private AccountAuthenticator mAccountAuthenticator;
	
	// Receive sync broadcasts
	private SyncReceiver mSyncReceiver;
    private IntentFilter filter;
	
    // Activity context to be used
	private Context mContext;
	
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
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_data_table, container, false);
		
		setActionbarTitle();
		
		return mFragmentView;
	}

	/**
	 * 
	 */
	private void setActionbarTitle() {
		String month = mAccountStatus.getCurrentMonthString();
		String appName = getActivity().getString(R.string.app_name);
		String actionBarTitle = appName + " (" + month + ")";
		getSherlockActivity().getSupportActionBar().setTitle(actionBarTitle);
	}
	
	/**
	 * Called 4th in the fragment life cycle
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Initiate database object
		DailyDataDatabaseAdapter mDatabase = new DailyDataDatabaseAdapter(mContext);

		// Get period (month) to query database on
		String period = mAccountStatus.getDataBaseMonthString();

		// Query database and return array of data usage for the period
		DailyVolumeUsage usage[] = mDatabase.getDailyVolumeUsage(period);
		
		// Initiate adapter to be used with list view
		DailyVolumeUsageAdapter adapter = new DailyVolumeUsageAdapter(mContext, R.layout.fragment_data_table_row, usage);
		
		// Reference list view to be used
		ListView mListView = (ListView) mFragmentView.findViewById(R.id.fragment_data_table_listview);
		
		// Floating header
		View headerView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.fragment_data_table_header, null, false);
		mListView.addHeaderView(headerView);
		
		// Set adapter to be used with the list view
		mListView.setAdapter(adapter);
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
	 * First call in the death of fragment
	 */
	@Override
	public void onPause() {
		super.onPause();
		
		// Unregister broadcast receiver for background sync
		getActivity().unregisterReceiver(mSyncReceiver);
	}

	public class SyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent i) {
            
            String MESSAGE = getString(R.string.sync_broadcast_message);
            String SYNC_START = getString(R.string.sync_broadcast_start);
            String SYNC_COMPLETE = getString(R.string.sync_broadcast_complete);
            
            String msg = i.getStringExtra(MESSAGE);
            if (msg.equals(SYNC_START)){
            	// Nothing to see here move along
            } else if (msg.equals(SYNC_COMPLETE)){
            }
        }
         
    }

}
