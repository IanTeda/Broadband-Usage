package au.id.teda.broadband.usage.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;

public class FooterFragment extends BaseFragment  {
		
	// View inflated by fragment
	private View mFragmentView;
		
	/**
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_footer, container, false);
			
		return mFragmentView;
	}
		
	@Override
	protected void loadFragmentView(){

        // Set last sync time
        TextView mLastSyncTV = (TextView) mFragmentView.findViewById(R.id.fragment_footer_last_sync_tv);
        mLastSyncTV.setText(mAccountStatus.getLastSyncTimeString());


        // Check if account is an anytime account and load layout
        TextView mDataPeriodTV = (TextView) mFragmentView.findViewById(R.id.fragment_footer_data_period_tv);
        if (mAccountInfo.isAccountAnyTime()){
            // Is any time so hide data period
            mDataPeriodTV.setVisibility(View.GONE);
        } else {
            // Set data period
            mDataPeriodTV.setText(Html.fromHtml(mAccountInfo.getDataPeriodString()));
        }


	}
}
