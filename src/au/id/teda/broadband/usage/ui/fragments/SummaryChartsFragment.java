package au.id.teda.broadband.usage.ui.fragments;

import org.achartengine.GraphicalView;

import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.chart.DoughnutChart;

public class SummaryChartsFragment extends BaseFragment {
	
	// View inflated by fragment
	private View mFragmentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_summary_charts, container, false);
		
		return mFragmentView;
	}

	@Override
	protected void loadFragmentView() {
		loadDoughnutChart();
	}

	private void loadDoughnutChart() {
		if (mAccountInfo.isInfoSet() 
				&& mAccountStatus.isStatusSet()){
			// Set layout container for chart
			LinearLayout mChartContainer = (LinearLayout) mFragmentView.findViewById(R.id.fragment_summary_chart_donught);
	
			// Initialize chart class
			DoughnutChart mDoughnutChart = new DoughnutChart(mContext);
	
			// Get chart view from library
			GraphicalView mDoughnutChartView = (GraphicalView) mDoughnutChart.getDoughnutChartView();
	
			// Add chart view to layout view
			mChartContainer.removeAllViews();
			mChartContainer.addView(mDoughnutChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			
			// Get screen specs
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
				
			// Get layout parameters
			LayoutParams params = mChartContainer.getLayoutParams();
			// Set height equal to screen width
			params.height = width;
		}
	}

}
