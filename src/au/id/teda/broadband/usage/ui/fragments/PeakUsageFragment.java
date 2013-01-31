package au.id.teda.broadband.usage.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
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
		
		return mFragmentView;
	}
	
	@Override
	protected void loadFragmentView(){
		
		// Set layout container for chart
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_peak_usage_donught);
		
		// Set layout height based on width
		ViewTreeObserver mViewTreeObserver = mContainerLayout.getViewTreeObserver();
		// View isn't inflated yet so you can not get the width, this a bit of work around
		mViewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
            	// Get layout parameters
        		LayoutParams parms = mContainerLayout.getLayoutParams();
        		// Set height equal to screen width
        		parms.height = getView().getWidth();
            }
        });

		// Initialize chart class
		CustomDonughtChart mChart = new CustomDonughtChart(mContext);
		mChart.setDays(mAccountStatus.getDaysSoFar(), mAccountStatus.getDaysToGo());
		mChart.setUsage(mAccountStatus.getPeakDataUsed(), mAccountInfo.getPeakQuota());

		// Set layout parameters for chart view
		LayoutParams mChartViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		// Add chart view to layout view
		mContainerLayout.addView(mChart, mChartViewParams);
			
	}

}
