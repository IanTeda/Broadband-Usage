package au.id.teda.broadband.usage.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.helper.NotificationHelper;
import au.id.teda.broadband.usage.ui.MainActivity;
import com.actionbarsherlock.app.SherlockFragment;

public class UsageSummaryFragment extends SherlockFragment {
	
	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// View inflated by fragment
	private View mFragmentView;

	// TextViews for usage
	private TextView mLayoutUsed;
	private TextView mCurrentMonth;
	private TextView mDaysNumber;
	private TextView mDaysDescription;
	private TextView mDaysSummary;
	private TextView mPeakNumber;
	private TextView mPeakDescription;
	private TextView mPeakSummary;
	private TextView mOffpeakNumber;
	private TextView mOffpeakDescription;
	private TextView mOffpeakSummary;
	private TextView mUploadsNumber;
	private TextView mFreezoneNumber;
	private TextView mUpTimeNumber;
	private TextView mIpAddress;

	
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
		
		// Set reference for textviews
		mLayoutUsed = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_size);
    	mCurrentMonth = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_current_month_tv);
    	mDaysNumber = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_days_number);
    	mDaysDescription = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_days_description);
    	mDaysSummary = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_days_summary);
		mPeakNumber = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_number);
		mPeakDescription = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_description);
		mPeakSummary = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_summary);
		mOffpeakNumber = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_number);
		mOffpeakDescription = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_description);
		mOffpeakSummary = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_summary);
		mUploadsNumber = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_uploads_number);
		mFreezoneNumber = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_freezone_number);
    	mUpTimeNumber = (TextView) mFragmentView.findViewById(R.id.fragment_rollover_uptime_uptime_number);
    	mIpAddress = (TextView) mFragmentView.findViewById(R.id.fragment_rollover_uptime_description);

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
			
			setUsageSoFar();
			//setUsageToGo();
			
			// Used in both sofar and remaining views for phone
			if (isLayoutPhone(mLayoutUsed)){
				mCurrentMonth.setText(mAccountStatus.getCurrentMonthString());
				mDaysDescription.setText(mAccountStatus.getRolloverDateString());
				mPeakSummary.setText(mAccountInfo.getPeakQuotaString());
				mOffpeakSummary.setText(mAccountInfo.getOffpeakQuotaString());
				mUpTimeNumber.setText(mAccountStatus.getUpTimeDaysString());
				mIpAddress.setText(mAccountStatus.getIpAddressStrng());
			}
			
	    	//checkUsageStatus();
		}
	}

	private void setUsageToGo() {

		mPeakNumber.setText(mAccountStatus.getPeakDataRemaingGbString());
		mOffpeakNumber.setText(mAccountStatus.getOffpeakDataRemaingGbString());
		mUploadsNumber.setText(mAccountStatus.getUploadsDataUsedGbString());
		mFreezoneNumber.setText(mAccountStatus.getFreezoneDataUsedGbString());
		
		// Only set text if loading phone layout
		if (isLayoutPhone(mLayoutUsed)){
			mDaysNumber.setText(mAccountStatus.getDaysToGoString());
			mDaysSummary.setText(mContext.getString(R.string.fragment_usage_summary_days_to_go));
			mPeakDescription.setText(mAccountStatus.getPeakShapedRemainingString());
			mOffpeakDescription.setText(mAccountStatus.getOffpeakShapedRemainingString());
		} else {
			mPeakSummary.setText(mContext.getString(R.string.fragment_usage_summary_remaining_no_status));
			mOffpeakSummary.setText(mContext.getString(R.string.fragment_usage_summary_remaining_no_status));
		}
	}

	private void setUsageSoFar() {
		
		mPeakNumber.setText(mAccountStatus.getPeakDataUsedGbString());
		mOffpeakNumber.setText(mAccountStatus.getOffpeakDataUsedGbString());
		mUploadsNumber.setText(mAccountStatus.getUploadsDataUsedGbString());
		mFreezoneNumber.setText(mAccountStatus.getFreezoneDataUsedGbString());
		
		// Only set text if loading phone layout
		if (isLayoutPhone(mLayoutUsed)){
			mDaysNumber.setText(mAccountStatus.getDaysSoFarString());
			mDaysSummary.setText(mContext.getString(R.string.fragment_usage_summary_days_so_far));
			mPeakDescription.setText(mAccountStatus.getPeakShapedUsedString());
			mOffpeakDescription.setText(mAccountStatus.getOffpeakShapedUsedString());
		}
	}

	private void checkUsageStatus() {
		NotificationHelper mNotification = new NotificationHelper(mContext);
		
		//TODO: Test layout logic better
		
		if(mNotification.isPeakQuotaNear()){
			setPeakWarning();
		}
		
		if(mNotification.isPeakQuotaOver()){
			setPeakError();
		}
		
		if(mNotification.isOffpeakQuotaNear()){
			setOffpeakWarning();
		}
		
		if(mNotification.isOffpeakQuotaOver()){
			setOffpeakError();
		}
	}
	
	private void setPeakWarning(){
    	RelativeLayout mLayoutContaner = (RelativeLayout) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_container);
    	mLayoutContaner.setBackgroundDrawable((getResources().getDrawable(R.drawable.shape_flash_warning)));
	}
	
	private void setPeakError(){
		RelativeLayout mLayoutContaner = (RelativeLayout) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_container);
		mLayoutContaner.setBackgroundDrawable((getResources().getDrawable(R.drawable.shape_flash_error)));
	}
	
	private void setOffpeakWarning(){
    	RelativeLayout mLayoutContaner = (RelativeLayout) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_container);
    	mLayoutContaner.setBackgroundDrawable((getResources().getDrawable(R.drawable.shape_flash_warning)));
	}
	
	private void setOffpeakError(){
		RelativeLayout mLayoutContaner = (RelativeLayout) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_container);
		mLayoutContaner.setBackgroundDrawable((getResources().getDrawable(R.drawable.shape_flash_error)));
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
	
	private boolean isLayoutPhone(TextView mLayoutUsed){
		CharSequence size = mLayoutUsed.getText();
		if ( size != null 
				&& size.equals(getActivity().getString(R.string.size_phone_port))){
			return true;
		} else {
			return false;
		}
	}

}
