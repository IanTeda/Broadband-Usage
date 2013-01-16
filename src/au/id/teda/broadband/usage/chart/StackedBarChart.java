package au.id.teda.broadband.usage.chart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.database.DailyDataDatabaseAdapter;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.ui.MainActivity;

public class StackedBarChart extends ChartBuilder {
	
	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Helper classes
	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	
    // Activity context to be used
	private Context mContext;
	
	private int MB = 1000000;
	
	private double max = 0;

	public StackedBarChart(Context context) {
		super(context);
		this.mContext = context;
		
		mAccountInfo = new AccountInfoHelper(mContext);
		mAccountStatus = new AccountStatusHelper(mContext);
	}
	
	public View getBarChartView (Cursor cursor){
		return ChartFactory.getBarChartView(mContext, 
				getStackedBarChartDataSet(cursor), 
				getStackedBarChartRenderer(), 
				Type.STACKED);
	}
	
	public double getMaxDataUsage() {
		return max;
	}
	
	protected XYMultipleSeriesDataset getStackedBarChartDataSet(Cursor cursor) {
		
		int COLUMN_INDEX_DAY = cursor.getColumnIndex(DailyDataDatabaseAdapter.DAY);
		int COLUMN_INDEX_PEAK = cursor.getColumnIndex(DailyDataDatabaseAdapter.PEAK);
		int COLUMN_INDEX_OFFPEAK = cursor.getColumnIndex(DailyDataDatabaseAdapter.OFFPEAK);
		int COLUMN_INDEX_UPLOADS = cursor.getColumnIndex(DailyDataDatabaseAdapter.UPLOADS);
		int COLUMN_INDEX_FREEZONE = cursor.getColumnIndex(DailyDataDatabaseAdapter.FREEZONE);

		// Set String value categories for graph
		CategorySeries peak = new CategorySeries(mContext.getString(R.string.chart_data_series_peak));
		CategorySeries offpeak = new CategorySeries(mContext.getString(R.string.chart_data_series_offpeak));

		// Move to first cursor entry
		cursor.moveToFirst();

		// And start adding to array
		while (cursor.isAfterLast() == false) {

			// Get peak data usage from current cursor position
			long peakUsage = ( cursor.getLong(COLUMN_INDEX_PEAK) / MB );

			// Get offpeak data usage from current cursor position
			long offpeakUsage = ( cursor.getLong(COLUMN_INDEX_OFFPEAK) / MB );

			// Make data stacked (achartengine does not do it by default).
			if (peakUsage > offpeakUsage) {
				peakUsage = peakUsage + offpeakUsage;
			} else {
				offpeakUsage = offpeakUsage + peakUsage;
			}

			// Add current cursor values to data series
			peak.add(peakUsage);
			offpeak.add(offpeakUsage);

			// Set max data usage for rendering graph
			if (max < peakUsage + offpeakUsage) {
				max = peakUsage + offpeakUsage;
			}

			cursor.moveToNext();
		}

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(peak.toXYSeries());
		dataset.addSeries(offpeak.toXYSeries());
		return dataset;
	}
	
	private XYMultipleSeriesRenderer getStackedBarChartRenderer() {
		// Set data series color
	    int[] colors = new int[] { getPeakColor(), getOffpeakColor() };
	    
	    // Load and initialise render objects
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	    //TODO: Add individual series renders
	    
	    renderer.setXAxisMin(0);
		renderer.setXAxisMax(getChartDays());
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(getMaxDataUsage());
		renderer.setLabelsColor(getLabelColor());
	    
	    // Chart render settings
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.TRANSPARENT);
	    renderer.setMarginsColor(getBackgroundColor());
	    renderer.setPanEnabled(false, false);
	    renderer.setFitLegend(true);
	    renderer.setLabelsTextSize(getLabelsTextSize(12));
	    renderer.setLegendTextSize(getLegendTextSize(12));
	    renderer.setAxesColor(getLabelColor());
	    renderer.setAntialiasing(true);
	    renderer.setBarSpacing(0.5f);
	    
	    return renderer;
	}

}
