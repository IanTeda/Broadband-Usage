package au.id.teda.broadband.usage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;

public class RolloverUptimeFragment extends BaseFragment {
	
	// View inflated by fragment
	private View mFragmentView;
		
	/**
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_rollover_uptime, container, false);
		
		return mFragmentView;
	}
	
	
	@Override
	protected void loadFragmentView(){
		if (mAccountInfo.isInfoSet() 
	   			&& mAccountStatus.isStatusSet()){
			
	    	TextView mRolloverNumberDaysTV = (TextView) mFragmentView.findViewById(R.id.fragment_rollover_uptime_days_number_tv);
	    	TextView mRolloverQuotaDaysTV = (TextView) mFragmentView.findViewById(R.id.fragment_rollover_uptime_days_until_tv);
	    	TextView mRolloverDateTV = (TextView) mFragmentView.findViewById(R.id.fragment_rollover_uptime_days_date_tv);
	    	TextView mUpTimeNumberTV = (TextView) mFragmentView.findViewById(R.id.fragment_rollover_uptime_uptime_number_tv);
	    	TextView mIpAddresTV = (TextView) mFragmentView.findViewById(R.id.fragment_rollover_uptime_ip_tv);
		    		    	
	    	mRolloverNumberDaysTV.setText(mAccountStatus.getDaysSoFarString());
	    	mRolloverQuotaDaysTV.setText(mAccountStatus.getDaysThisPeriodString());
	    	mRolloverDateTV.setText(mAccountStatus.getRolloverDateString());
	    	mUpTimeNumberTV.setText(mAccountStatus.getUpTimeDaysString());
	    	mIpAddresTV.setText(mAccountStatus.getIpAddressStrng());
		}
	}
}
