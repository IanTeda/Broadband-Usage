package au.id.teda.broadband.usage.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.ui.MainActivity;

import com.actionbarsherlock.app.SherlockFragment;

public class UsageSummaryFragment extends SherlockFragment {
	
	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Helper classes
	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	
	// Recieve sync broadcasts
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
		mFragmentView = inflater.inflate(R.layout.fragment_usage_summary, container, false);
		
		return mFragmentView;
	}
	
	/**
	 * Called 4th in the fragment life cycle
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		loadFragmentView();
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
	
	private void loadFragmentView(){
		if (mAccountInfo.isInfoSet() 
    			&& mAccountStatus.isStatusSet()){
			
			// TODO: Is view reloaded post sync?
			
			TextView mLayoutUsed = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_size);
	    	TextView mCurrentMonthTV = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_current_month_tv);
	    	TextView mPeakDataNumberTV = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_number_tv);
	    	TextView mPeakQuotaTV = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_quota_tv);
	    	TextView mPeakDataTV = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_used_tv);
	    	TextView mOffpeakDataNumberTV = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_number_tv);
	    	TextView mOffpeakQuotaTV = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_quota_tv);
	    	TextView mOffpeakDataTV = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_used_tv);
	    	TextView mUploadsDataNumberTV = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_number_tv);
	    	TextView mFreezoneDataNumberTV = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_number_tv);
	    	
	    	mCurrentMonthTV.setText(mAccountStatus.getCurrentMonthString());
	    	mPeakDataNumberTV.setText(mAccountStatus.getPeakDataUsedGbString());
	    	mOffpeakDataNumberTV.setText(mAccountStatus.getOffpeakDataUsedGbString());
	    	mUploadsDataNumberTV.setText(mAccountStatus.getUploadsDataUsedGbString());
	    	mFreezoneDataNumberTV.setText(mAccountStatus.getFreezoneDataUsedGbString());
	    	
	    	// Only set text if loading phone layout
	    	if (mLayoutUsed.getText().equals(getActivity().getString(R.string.size_phone_port))){
		    	mPeakQuotaTV.setText(mAccountInfo.getPeakQuotaString());
	    		mPeakDataTV.setText(mAccountStatus.getPeakShapedString());
		    	mOffpeakQuotaTV.setText(mAccountInfo.getOffpeakQuotaString());
		    	mOffpeakDataTV.setText(mAccountStatus.getOffpeakShapedString());
	    	}
		}
	}
	
	public class SyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent i) {
            
            String MESSAGE = getString(R.string.sync_broadcast_message);
            String SYNC_START = getString(R.string.sync_broadcast_start);
            String SYNC_COMPLETE = getString(R.string.sync_broadcast_complete);
            
            String msg = i.getStringExtra(MESSAGE);
            if (msg.equals(SYNC_START)){
            	// Nothing to do see here move along
            } else if (msg.equals(SYNC_COMPLETE)){
            	loadFragmentView();
            }
        }
         
    }

}
