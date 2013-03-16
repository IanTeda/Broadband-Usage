package au.id.teda.broadband.usage.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.TextView;
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
		
		Log.d(DEBUG_TAG, "UsageDoughnutFragment");
		
		return mFragmentView;
	}
	
	@Override
	protected void loadFragmentView() {

		loadDonughtChart();
		loadChartText();
	}
	
	private void loadDonughtChart() {
		// Set layout container for chart
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_usage_donught);
		final TextView mLayoutUsed = (TextView) mFragmentView.findViewById(R.id.fragment_usage_donught_size);
		
		// Listen for view being inflated
		ViewTreeObserver mViewTreeObserver = mContainerLayout.getViewTreeObserver();
		mViewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        @Override
	        public void onGlobalLayout() {
	        	
	        	// Get layout parameters for container
	    		LayoutParams parms = mContainerLayout.getLayoutParams();
	    		// Get reference to layout view
	    		View layout = getView();
	    		// Get height of layout
	    		int iHeight = layout.getHeight();
	    		// Get width of layout
	    		int iWidth = layout.getWidth();
	    		
	    		if (mLayoutHelper.isLayout_w1024dp(mLayoutUsed) || mLayoutHelper.isLayout_w800dp(mLayoutUsed)){
	    			// Set wdith equal to height
	    			parms.width = iHeight;
	    			
	    			// Add padding to center chart
	    			int padding = ((iWidth - iHeight) / 2);
	    			layout.setPadding(padding, 0, padding, 0);
	    			
	    		} else {
	    			// Set height equal to width
	    			parms.height = iWidth;
	    		}
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
		mContainerLayout.removeAllViews();
		mContainerLayout.addView(mChart, mChartViewParams);
	}
	
	private void loadChartText() {
		// Set layout container for chart
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_usage_donught_text_container);
		
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
		
		// Set TextView references
		final TextView mPeakPercent = (TextView) mFragmentView.findViewById(R.id.fragment_usage_donught_peak_percent);
		final TextView mOffpeakPercent = (TextView) mFragmentView.findViewById(R.id.fragment_usage_donught_offpeak_percent);

		// Set TextView values
		mPeakPercent.setText(mAccountStatus.getPeakDataUsedPercentString());
		mOffpeakPercent.setText(mAccountStatus.getOffpeakDataUsedPercentString());
	}

}
