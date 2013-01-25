package au.id.teda.broadband.usage.ui.fragments;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import com.actionbarsherlock.app.SherlockFragment;

public class ProductPlanDetailFragment extends SherlockFragment {
	
	// Debug tag pulled from main activity
	//private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
			
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_product_plan_detail, container, false);
		
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
		
		// Set reference to product plan text views
		TextView mProduct = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_product);
		TextView mPlan = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_plan);
		
		// Set text in text views
		mProduct.setText(mAccountInfo.getProduct());
		mPlan.setText(mAccountInfo.getPlan());
		
		// Set reference to peak detail 		
		TextView mPeakStart = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_start_time);
		TextView mPeakStartUnit = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_start_time_unit);
		TextView mPeakFinish = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_finish_time);
		TextView mPeakFinishUnit = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_finish_time_unit);
		TextView mPeakHours = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_hours_time);
		TextView mPeakQuotaPeriod = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_quota_data_period);
		TextView mPeakQuotaDay = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_quota_data_day);
		TextView mPeakQuotaHour = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_quota_data_hour);

		// Set peak text views
		mPeakStart.setText(mAccountInfo.getOffpeakEndTimeString());
		mPeakStartUnit.setText(mAccountInfo.getOffpeakEndTimeAmPmString());
		mPeakFinish.setText(mAccountInfo.getOffpeakStartTimeString());
		mPeakFinishUnit.setText(mAccountInfo.getOffpeakStartTimeAmPmString());
		mPeakHours.setText( mAccountInfo.getPeakHourString());
		mPeakQuotaPeriod.setText(IntUsageToString(mAccountInfo.getPeakQuotaGb()));
		mPeakQuotaDay.setText(IntUsageToString(mAccountInfo.getPeakQuotaDailyMb()));
		mPeakQuotaHour.setText(IntUsageToString(mAccountInfo.getPeakQuotaHourlyMb()));
		

		// Set reference to peak detail 		
		TextView mOffpeakStart = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_start_time);
		TextView mOffpeakStartUnit = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_start_time_unit);
		TextView mOffpeakFinish = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_finish_time);
		TextView mOffpeakFinishUnit = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_finish_time_unit);
		TextView mOffpeakHours = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_hours_time);
		TextView mOffpeakQuotaPeriod = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_quota_data_period);
		TextView mOffpeakQuotaDay = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_quota_data_day);
		TextView mOffpeakQuotaHour = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_quota_data_hour);

		// Set peak text views
		mOffpeakStart.setText(mAccountInfo.getOffpeakStartTimeString());
		mOffpeakStartUnit.setText(mAccountInfo.getOffpeakStartTimeAmPmString());
		mOffpeakFinish.setText(mAccountInfo.getOffpeakEndTimeString());
		mOffpeakFinishUnit.setText(mAccountInfo.getOffpeakEndTimeAmPmString());
		mOffpeakHours.setText( mAccountInfo.getOffpeakHourString());
		mOffpeakQuotaPeriod.setText(IntUsageToString(mAccountInfo.getOffpeakQuotaGb()));
		mOffpeakQuotaDay.setText(IntUsageToString(mAccountInfo.getOffpeakQuotaDailyMb()));
		mOffpeakQuotaHour.setText(IntUsageToString(mAccountInfo.getOffpeakQuotaHourlyMb()));
			
	}
	
	// Return formated string value for int stored in db
	private String IntUsageToString (long usage){
		NumberFormat numberFormat = new DecimalFormat("#,###");
		return numberFormat.format(usage);
		
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
