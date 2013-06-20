package au.id.teda.broadband.usage.fragments;

import au.id.teda.broadband.usage.database.DailyDataTableAdapter;
import org.achartengine.GraphicalView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.chart.StackedLineChart;
import au.id.teda.broadband.usage.util.DailyVolumeUsage;

public class StackedLineChartFragment extends BaseFragment {
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Daily dev array
	private DailyVolumeUsage mDailyUsageArray[];
	
	/**
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Set reference to fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_stacked_line_chart, container, false);
		return mFragmentView;
	}
	
	/**
	 * Called 4th in the fragment life cycle
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * Called 5th in the fragment life cycle
	 */
	@Override
	public void onResume() {
		super.onResume();
	}
	
	/**
	 * Called 1st in the death of fragment
	 */
	@Override
	public void onPause() {
		super.onPause();
	}
	
	/**
	 * Load fragment view if account status and info is set
	 */
	@Override
	protected void loadFragmentView() {
        //Log.d(DEBUG_TAG, "isInfoSet:" + mAccountInfo.isInfoSet() + " isStatusSet:" + mAccountStatus.isStatusSet());

        if (mAccountInfo.isInfoSet() && mAccountStatus.isStatusSet()) {
			
	        // Get volume dev array
			DailyDataTableAdapter mDatabase = new DailyDataTableAdapter(mContext);
			String period = mAccountStatus.getDataBaseMonthString();

            //String period = "200903"; // Used for testing old xml feeds
            //Log.d(DEBUG_TAG, "Period:" + period);
			mDailyUsageArray = mDatabase.getDailyVolumeUsage(period);
			
			// Set layout container for chart
			LinearLayout mChartContainer = (LinearLayout) mFragmentView.findViewById(R.id.fragment_stacked_line_chart_container);

			// Initialize chart class
			StackedLineChart mLineChart = new StackedLineChart(mContext);
			
			// Get chart view from library
			GraphicalView mLineChartView = (GraphicalView) mLineChart.getChartView(mDailyUsageArray);

			// Add chart view to layout view
			mChartContainer.removeAllViews();
			mChartContainer.addView(mLineChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
	}

}
