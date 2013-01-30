package au.id.teda.broadband.usage.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;

public class ProductPlanFragment extends BaseFragment {
	
	// View inflated by fragment
	private View mFragmentView;

	/**
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_product_plan, container, false);
		
		return mFragmentView;
	}

	@Override
	protected void loadFragmentView(){
		if (mAccountInfo.isInfoSet() 
    			&& mAccountStatus.isStatusSet()){
			
	    	TextView mUsernameTV = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_username_tv);
	    	TextView mProductPlanTV = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_product_plan_tv);
	    	
	    	mUsernameTV.setText(mAccountAuthenticator.getUsername());
	    	mProductPlanTV.setText(mAccountInfo.getProductPlanString());
		}
	}

}
