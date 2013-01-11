package au.id.teda.broadband.usage.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

public class DataSummaryFragment extends SherlockFragment {
	
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	
	private SyncReceiver mSyncReceiver;
    private IntentFilter filter;
	
	private Context mContext;
	private Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set context and container activity for fragment
		//mContext = getSherlockActivity();
		//mActivity = getSherlockActivity();
		
		// Load helper classes
		mAccountInfo = new AccountInfoHelper(mContext);
		mAccountStatus = new AccountStatusHelper(mContext);
		
		// Setup broadcast receiver for background sync
        //String BROADCAST = getString(R.string.sync_broadcast_action);
        //filter = new IntentFilter(BROADCAST);
        //mSyncReceiver = new SyncReceiver();
	}

	@Override
	public void onPause() {
		super.onPause();
		//mActivity.unregisterReceiver(mSyncReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		//mActivity.registerReceiver(mSyncReceiver, filter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Fragment layout to be inflated
		View view = inflater.inflate(R.layout.fragment_data_summary, container, false);
		
		//loadView(view);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	private void loadView(View v){
		if (mAccountInfo.isInfoSet() 
    			&& mAccountStatus.isStatusSet()){
			
	    	TextView mCurrentMonthTV = (TextView) v.findViewById(R.id.fragment_data_summary_current_month_tv);
	    	TextView mRolloverNumberDaysTV = (TextView) v.findViewById(R.id.fragment_data_summary_days_number_tv);
	    	TextView mRolloverQuotaDaysTV = (TextView) v.findViewById(R.id.fragment_data_summary_days_until_tv);
	    	TextView mRolloverDateTV = (TextView) v.findViewById(R.id.fragment_data_summary_days_date_tv);
	    	TextView mPeakDataNumberTV = (TextView) v.findViewById(R.id.fragment_data_summary_peak_number_tv);
	    	TextView mPeakQuotaTV = (TextView) v.findViewById(R.id.fragment_data_summary_peak_quota_tv);
	    	TextView mPeakDataTV = (TextView) v.findViewById(R.id.fragment_data_summary_peak_used_tv);
	    	TextView mOffpeakDataNumberTV = (TextView) v.findViewById(R.id.fragment_data_summary_offpeak_number_tv);
	    	TextView mOffpeakQuotaTV = (TextView) v.findViewById(R.id.fragment_data_summary_offpeak_quota_tv);
	    	TextView mOffpeakDataTV = (TextView) v.findViewById(R.id.fragment_data_summary_offpeak_used_tv);
	    	TextView mUpTimeNumberTV = (TextView) v.findViewById(R.id.fragment_data_summary_uptime_number_tv);
	    	TextView mIpAddresTV = (TextView) v.findViewById(R.id.fragment_data_summary_uptime_ip_tv);
	    	TextView mLastSyncTV = (TextView) v.findViewById(R.id.fragment_data_summary_last_sync_tv);
	    	
	    	mCurrentMonthTV.setText(mAccountStatus.getCurrentMonthString());
	    	mRolloverNumberDaysTV.setText(mAccountStatus.getDaysSoFarString());
	    	mRolloverQuotaDaysTV.setText(mAccountStatus.getDaysThisPeriodString());
	    	mRolloverDateTV.setText(mAccountStatus.getRolloverDateString());
	    	mPeakDataNumberTV.setText(mAccountStatus.getPeakDataUsedGbString());
	    	mPeakQuotaTV.setText(mAccountInfo.getPeakQuotaString());
	    	mPeakDataTV.setText(mAccountStatus.getPeakShapedString());
	    	mOffpeakDataNumberTV.setText(mAccountStatus.getOffpeakDataUsedGbString());
	    	mOffpeakQuotaTV.setText(mAccountInfo.getOffpeakQuotaString());
	    	mOffpeakDataTV.setText(mAccountStatus.getOffpeakShapedString());
	    	mUpTimeNumberTV.setText(mAccountStatus.getUpTimeDaysString());
	    	mIpAddresTV.setText(mAccountStatus.getIpAddressStrng());
	    	mLastSyncTV.setText(mAccountStatus.getLastSyncTimeString());
			
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
            	
            } else if (msg.equals(SYNC_COMPLETE)){
            	Log.d(DEBUG_TAG, "DataSummaryFragment.SYNC_COMPLETE");
            	//loadView();
            }
        }
         
    }

}
