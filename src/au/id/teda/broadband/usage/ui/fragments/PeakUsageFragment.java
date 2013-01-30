package au.id.teda.broadband.usage.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.chart.CustomDonughtChart;


public class PeakUsageFragment extends BaseFragment {

	// View inflated by fragment
	private View mFragmentView;
			
	/**
	* Called 3rd in the fragment life cycle
	*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_peak_usage, container, false);
		
		Log.d(DEBUG_TAG, "PeakUsageFragment");
		
		return mFragmentView;
	}
	
	@Override
	protected void loadFragmentView(){
		
		// Set layout container for chart
		LinearLayout mContainer = (LinearLayout) mFragmentView.findViewById(R.id.fragment_peak_usage_donught);

		// Initialize chart class
		CustomDonughtChart mChart = new CustomDonughtChart(mContext);
		mChart.setDays(mAccountStatus.getDaysSoFar(), mAccountStatus.getDaysToGo());
		mChart.setUsage(mAccountStatus.getPeakDataUsed(), mAccountInfo.getPeakQuota());

		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		// Add chart view to layout view
		//mContainer.addView(mChart, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mContainer.addView(mChart, params);
		
		// Get screen specs
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();

		// Get layout parameters
		LayoutParams p2 = mContainer.getLayoutParams();
		// Set height equal to screen width
		p2.height = width;
			
	}

}
