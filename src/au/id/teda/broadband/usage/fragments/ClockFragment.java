package au.id.teda.broadband.usage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.util.CustomDigitalClock;


public class ClockFragment extends BaseFragment {

	// View inflated by fragment
	private View mFragmentView;
			
	/**
	* Called 3rd in the fragment life cycle
	*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_clock, container, false);
		
		return mFragmentView;
	}
	
	@Override
	protected void loadFragmentView(){
			
		// Set clock ticking
		@SuppressWarnings("unused")
        CustomDigitalClock dc = (CustomDigitalClock) mFragmentView.findViewById(R.id.fragment_clock_digital);
			
		// Set current data period
		TextView mCurrentlyUsing = (TextView) mFragmentView.findViewById(R.id.fragment_clock_currently_using);
		mCurrentlyUsing.setText(mAccountInfo.getPeriodString());

        if (mAccountInfo.isAccountAnyTime()){
            mCurrentlyUsing.setVisibility(View.GONE);
        }
		
		// Set Username
		TextView mUsername = (TextView) mFragmentView.findViewById(R.id.fragment_clock_username);
		mUsername.setText(mAccountAuthenticator.getUsername());
	}

}
