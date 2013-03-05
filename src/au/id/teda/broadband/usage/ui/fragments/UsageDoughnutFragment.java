package au.id.teda.broadband.usage.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.chart.CustomDonughtChartStacked;

public class UsageDoughnutFragment extends BaseFragment {
	// BaseFragment used to load the standard methods
	
	// View inflated by fragment
	private View mFragmentView;
	
	/**
	* Called 3rd in the fragment life cycle
	*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_usage_doughnut, container, false);
		
		return mFragmentView;
	}
	
	@Override
	protected void loadFragmentView() {

		loadDonughtChart();

	}
	
	private void loadDonughtChart() {
		// Set layout container for chart
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_usage_donught);
		
		// Listen for view being inflated
		ViewTreeObserver mViewTreeObserver = mContainerLayout.getViewTreeObserver();
		mViewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        @Override
	        public void onGlobalLayout() {
	        	// Get layout parameters
	    		LayoutParams parms = mContainerLayout.getLayoutParams();
	    		
    			// Set height equal to parent layout width
    			parms.height = getView().getWidth();
	        }
	    });
	
		// Initialize chart class
		CustomDonughtChartStacked mChart = new CustomDonughtChartStacked(mContext);
		mChart.setDays(mAccountStatus.getDaysSoFar(), mAccountStatus.getDaysToGo());
		mChart.setPeakUsage(mAccountStatus.getPeakDataUsed(), mAccountInfo.getPeakQuota());
		mChart.setOffpeakUsage(mAccountStatus.getOffpeakDataUsed(), mAccountInfo.getOffpeakQuota());
	
		// Set layout parameters for chart view
		LayoutParams mChartViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		// Add chart view to layout view
		mContainerLayout.addView(mChart, mChartViewParams);
	}

}
