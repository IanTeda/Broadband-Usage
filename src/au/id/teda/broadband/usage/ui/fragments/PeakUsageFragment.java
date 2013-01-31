package au.id.teda.broadband.usage.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.chart.CustomDonughtChart;
import au.id.teda.broadband.usage.chart.DailyAverageChart;


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
		
		loadDonughtChart();
		loadDonughtChartText();
		
		// Set layout container for chart
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_peak_usage_daily_chart);
		
		// Set layout height based on width
		ViewTreeObserver mViewTreeObserver = mContainerLayout.getViewTreeObserver();
		// Listen for view being inflated
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
		DailyAverageChart mChart = new DailyAverageChart(mContext);
		int average = mAccountStatus.getPeakDailyAverageUsedMb();
		int quota = (int) mAccountInfo.getPeakQuotaDailyMb();
		mChart.setData(average, quota);
	
		// Set layout parameters for chart view
		LayoutParams mChartViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		// Add chart view to layout view
		mContainerLayout.addView(mChart, mChartViewParams);
		
		
	}

	private void loadDonughtChart() {
		// Set layout container for chart
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_peak_usage_donught);
		
		// Set layout height based on width
		ViewTreeObserver mViewTreeObserver = mContainerLayout.getViewTreeObserver();
		// Listen for view being inflated
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

	private void loadDonughtChartText() {
		// Set layout container for chart text
		final LinearLayout mTextContainer = (LinearLayout) mFragmentView.findViewById(R.id.fragment_peak_usage_donught_text_container);
		
		// Set layout height based on width
		ViewTreeObserver mViewTreeObserver = mTextContainer.getViewTreeObserver();
		// Listen for view being inflated
		mViewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
            	// Get layout parameters
        		LayoutParams parms = mTextContainer.getLayoutParams();
        		// Set height equal to screen width
        		parms.height = getView().getWidth();
            }
        });
		
		// Set text view references
		TextView mPeakPercent = (TextView) mFragmentView.findViewById(R.id.fragment_peak_usage_donught_text_percent);
		TextView mPeakUsed = (TextView) mFragmentView.findViewById(R.id.fragment_peak_usage_donught_text_period);
		
		// Set text in text views
		mPeakPercent.setText(mAccountStatus.getPeakDataUsedPercentString());
		mPeakUsed.setText(mContext.getString(R.string.fragment_peak_usage_used));
	}

}
