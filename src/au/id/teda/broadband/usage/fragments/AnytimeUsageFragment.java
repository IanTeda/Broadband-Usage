package au.id.teda.broadband.usage.fragments;

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
import au.id.teda.broadband.usage.chart.PieChart;
import org.achartengine.GraphicalView;


public class AnytimeUsageFragment extends BaseFragment {

	// BaseFragment used to load the standard methods
	
	// View inflated by fragment
	private View mFragmentView;

	/**
	* Called 3rd in the fragment life cycle
	*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_anytime_usage, container, false);
		
		return mFragmentView;
	}
	
	@Override
	protected void loadFragmentView(){
		
		loadDonughtChart();
		loadDonughtChartText();
		loadDailyAverageChart();
		loadPieChart();
		
	}

	private void loadPieChart() {
		// Set layout container for chart
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_anytime_upload_download_chart);

		// Initialize chart class
		PieChart mPieChart = new PieChart(mContext);
		
		// Set chart values
		int uploads =  (mAccountStatus.getUploadsDataUsedGb() / 2);
		int downloads = mAccountStatus.getAnytimeDataUsedGb();
		int quota = mAccountInfo.getAnyTimeQuotaGb();
		mPieChart.setData(uploads, downloads, quota);
	
		// Set layout parameters for chart view
		LayoutParams mChartViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		// Add chart view to layout view
		GraphicalView mGraphicalView = (GraphicalView) mPieChart.getPieChartView();
		mContainerLayout.removeAllViews();
		mContainerLayout.addView(mGraphicalView, mChartViewParams);
		
		// Set text view references
		TextView mUploadData = (TextView) mFragmentView.findViewById(R.id.fragment_anytime_upload_download_upload_number);
		TextView mDownloadData = (TextView) mFragmentView.findViewById(R.id.fragment_anytime_upload_download_download_number);
		
		mUploadData.setText(mAccountStatus.getUploadsDataUsedGbString());
		mDownloadData.setText(mAccountStatus.getAnytimeDataUsedLessUploadsGbString());
	}

	private void loadDonughtChart() {
		// Set layout container for chart
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_anytime_usage_donught);
		
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
		CustomDonughtChart mChart = new CustomDonughtChart(mContext);
		mChart.setDays(mAccountStatus.getDaysSoFar(), mAccountStatus.getDaysToGo());
		mChart.setUsage(mAccountStatus.getAnytimeDataUsed(), mAccountInfo.getAnyTimeQuota());
	
		// Set layout parameters for chart view
		LayoutParams mChartViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		// Add chart view to layout view
		mContainerLayout.removeAllViews();
		mContainerLayout.addView(mChart, mChartViewParams);
	}

	private void loadDonughtChartText() {
		// Set layout container for chart text
		final LinearLayout mTextContainer = (LinearLayout) mFragmentView.findViewById(R.id.fragment_anytime_usage_donught_text_container);

		// Listen for view being inflated
		ViewTreeObserver mViewTreeObserver = mTextContainer.getViewTreeObserver();
		mViewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
            	// Get layout parameters
        		LayoutParams parms = mTextContainer.getLayoutParams();
        		// Set height equal to layout width
        		parms.height = getView().getWidth();
            }
        });
		
		// Set text view references
		TextView mAnytimePercent = (TextView) mFragmentView.findViewById(R.id.fragment_anytime_usage_donught_text_percent);
		TextView mAnytimeUsed = (TextView) mFragmentView.findViewById(R.id.fragment_anytime_usage_donught_text_period);
		
		// Set text in text views
		mAnytimePercent.setText(mAccountStatus.getAnytimeDataUsedPercentString());
		mAnytimeUsed.setText(mContext.getString(R.string.fragment_anytime_usage_used));
	}

	private void loadDailyAverageChart() {
		// Set layout container for chart
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_anytime_usage_daily_chart);
		
		// Initialize chart class
		DailyAverageChart mChart = new DailyAverageChart(mContext);
		int average = mAccountStatus.getAnytimeDailyAverageUsedMb();
		int quota = (int) mAccountInfo.getAnyTimeQuotaDailyMb();
		
		// Check to make sure info status is set so we don't get divid by zero erros
		if (!mAccountInfo.isInfoSet() || !mAccountStatus.isStatusSet()){
			average = 2;
			quota = 1;
		}
		
		// Set chart data
		mChart.setData(average, quota);
	
		// Set layout parameters for chart view
		LayoutParams mChartViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		// Add chart view to layout view
		mContainerLayout.addView(mChart, mChartViewParams);
		
		// Set text view references
		TextView mDailyAnytime = (TextView) mFragmentView.findViewById(R.id.fragment_anytime_usage_daily_number);
		TextView mDailyAnytimeVariation = (TextView) mFragmentView.findViewById(R.id.fragment_anytime_usage_daily_description_right);
		
		mDailyAnytime.setText(IntUsageToString(mAccountStatus.getAnytimeDailyAverageUsedMb()));
		mDailyAnytimeVariation.setText(IntUsageToString(mAccountStatus.getAnytimeAverageVariationMb()));
	}
}
