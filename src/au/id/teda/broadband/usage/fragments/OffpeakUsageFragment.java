package au.id.teda.broadband.usage.fragments;

import au.id.teda.broadband.usage.chart.ChartBuilder;
import au.id.teda.broadband.usage.chart.PieChart;
import org.achartengine.GraphicalView;

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


public class OffpeakUsageFragment extends BaseFragment {

	// BaseFragment used to load the standard methods
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Fragment page title
	public static String PAGE_TITLE = "OFFPEAK";

    private ChartBuilder mChartBuilder;

    /**
	* Called 3rd in the fragment life cycle
	*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_offpeak_usage, container, false);

        mChartBuilder = new ChartBuilder(mContext);

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
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_offpeak_upload_download_chart);

		// Initialize chart class
		PieChart mPieChart = new PieChart(mContext);

        // Set chart values
        long upload =  (mAccountStatus.getUploadsDataUsed());
        long peak = mAccountStatus.getOffpeakDataUsed();
        long offpeak = mAccountStatus.getPeakDataUsed();
        long quota = mAccountInfo.getPeakQuota();

        // Guestimate peak upload value
        long offpeakUpload = mChartBuilder.peakUploadGuess(peak, offpeak, upload);

        // Calculate offpeak less estimate upload in offpeak
        long download = offpeak - offpeakUpload;

        // Set data in pie chart
        mPieChart.setData(upload, download, quota);
	
		// Set layout parameters for chart view
		LayoutParams mChartViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		// Add chart view to layout view
		GraphicalView mGraphicalView = (GraphicalView) mPieChart.getPieChartView();
		mContainerLayout.removeAllViews();
		mContainerLayout.addView(mGraphicalView, mChartViewParams);
		
		// Set text view references
		TextView mUploadData = (TextView) mFragmentView.findViewById(R.id.fragment_offpeak_upload_download_upload_number);
		TextView mDownloadData = (TextView) mFragmentView.findViewById(R.id.fragment_offpeak_upload_download_download_number);

        mUploadData.setText(dataToGbString(offpeakUpload));
        mDownloadData.setText(dataToGbString(download));
	}

	private void loadDonughtChart() {
		// Set layout container for chart
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_offpeak_usage_donught);

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
		mChart.setUsage(mAccountStatus.getOffpeakDataUsed(), mAccountInfo.getOffpeakQuota());
	
		// Set layout parameters for chart view
		LayoutParams mChartViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		// Add chart view to layout view
		mContainerLayout.removeAllViews();
		mContainerLayout.addView(mChart, mChartViewParams);
	}

	private void loadDonughtChartText() {
		// Set layout container for chart text
		final LinearLayout mTextContainer = (LinearLayout) mFragmentView.findViewById(R.id.fragment_offpeak_usage_donught_text_container);

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
		TextView mOffpeakPercent = (TextView) mFragmentView.findViewById(R.id.fragment_offpeak_usage_donught_text_percent);
		TextView mOffpeakUsed = (TextView) mFragmentView.findViewById(R.id.fragment_offpeak_usage_donught_text_period);
		
		// Set text in text views
		mOffpeakPercent.setText(mAccountStatus.getOffpeakDataUsedPercentString());
		mOffpeakUsed.setText(mContext.getString(R.string.fragment_offpeak_usage_used));
	}

	private void loadDailyAverageChart() {
		// Set layout container for chart
		final LinearLayout mContainerLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_offpeak_usage_daily_chart);
		
		// Initialize chart class
		DailyAverageChart mChart = new DailyAverageChart(mContext);
		int average = mAccountStatus.getOffpeakDailyAverageUsedMb();
		int quota = (int) mAccountInfo.getOffpeakQuotaDailyMb();
		
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
		TextView mDailyPeak = (TextView) mFragmentView.findViewById(R.id.fragment_offpeak_usage_daily_number);
		TextView mDailyPeakVariation = (TextView) mFragmentView.findViewById(R.id.fragment_offpeak_usage_daily_description_right);
		
		mDailyPeak.setText(IntUsageToString(mAccountStatus.getOffpeakDailyAverageUsedMb()));
		//TODO: Update method below
		mDailyPeakVariation.setText(IntUsageToString(mAccountStatus.getOffpeakAverageVariationMb()));
	}

}
