package au.id.teda.broadband.usage.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.cursoradapter.DailyDataTableCursorAdapter;
import au.id.teda.broadband.usage.database.DailyDataDatabaseAdapter;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.parser.VolumeUsageParser;
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
		
		return mFragmentView;
	}
	
	/**
	 * Called 4th in the fragment life cycle
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		DailyDataDatabaseAdapter mDatabase = new DailyDataDatabaseAdapter(mContext);
		String period = mAccountStatus.getDataBaseMonthString();
		
		/**
		DailyVolumeUsage weather_data[] = new DailyVolumeUsage[]
		        {
		            new DailyVolumeUsage(1,"Month",245648454,3,5,6),
		            new DailyVolumeUsage(1,"Month",2,3,5,6),
		            new DailyVolumeUsage(1,"Month",2,3,5,6),
		            new DailyVolumeUsage(1,"Month",2,3,5,6),
		            new DailyVolumeUsage(1,"Month",2,3,5,6),
		        };
		**/
		
		DailyVolumeUsage weather_data[] = mDatabase.getDailyVolumeUsage(period);
		
		Log.d(DEBUG_TAG, String.valueOf(weather_data.length));
		
		DailyVolumeUsageAdapter adapter = new DailyVolumeUsageAdapter(mContext, 
                R.layout.fragment_data_table_row, weather_data);
		
		ListView mListView = (ListView) mFragmentView.findViewById(R.id.fragment_data_table_listview);
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
