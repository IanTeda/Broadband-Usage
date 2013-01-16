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

public class DailyStackedBarChart extends ChartBuilder {
	
	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Helper classes
	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	
    // Activity context to be used
	private Context mContext;
	
	private int GB = 1000000;
	
	private double max = 0;

	public DailyStackedBarChart(Context context) {
		super(context);
		this.mContext = context;
		
		mAccountInfo = new AccountInfoHelper(mContext);
		mAccountStatus = new AccountStatusHelper(mContext);
	}
	
	public View getBarChartView (){
		return ChartFactory.getBarChartView(mContext, 
				getStackedBarChartDataSet(), 
				getStackedBarChartRenderer(), 
				Type.STACKED);
	}
	
	public double getMaxDataUsage() {
		return max;
	}
	
	protected XYMultipleSeriesDataset getStackedBarChartDataSet() {
		
		// Open Database
		DailyDataDatabaseAdapter mDatabase = new DailyDataDatabaseAdapter(mContext);
		mDatabase.open();

		// Get current data period
		String period = mAccountStatus.getDataBaseMonthString();

		// Retrieve cursor for given data period
		Cursor mCursor = mDatabase.getPriodUsageCursor(period);
		
		int COLUMN_INDEX_DAY = mCursor.getColumnIndex(DailyDataDatabaseAdapter.DAY);
		int COLUMN_INDEX_PEAK = mCursor.getColumnIndex(DailyDataDatabaseAdapter.PEAK);
		int COLUMN_INDEX_OFFPEAK = mCursor.getColumnIndex(DailyDataDatabaseAdapter.OFFPEAK);
		int COLUMN_INDEX_UPLOADS = mCursor.getColumnIndex(DailyDataDatabaseAdapter.UPLOADS);
		int COLUMN_INDEX_FREEZONE = mCursor.getColumnIndex(DailyDataDatabaseAdapter.FREEZONE);

		// Set String value categories for graph
		CategorySeries peakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_peak));
		CategorySeries offpeakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_offpeak));

		// Move to first cursor entry
		mCursor.moveToFirst();

		// And start adding to array
		while (mCursor.isAfterLast() == false) {

			// Get peak data usage from current cursor position
			long peakUsageLong = ( mCursor.getLong(COLUMN_INDEX_PEAK) / GB );

			// Get offpeak data usage from current cursor position
			long offpeakUsageLong = ( mCursor.getLong(COLUMN_INDEX_OFFPEAK) / GB );

			// Make data stacked (achartengine does not do it by default).
			if (peakUsageLong > offpeakUsageLong) {
				peakUsageLong = peakUsageLong + offpeakUsageLong;
			} else {
				offpeakUsageLong = offpeakUsageLong + peakUsageLong;
			}

			// Add current cursor values to data series
			peakSeries.add(peakUsageLong);
			offpeakSeries.add(offpeakUsageLong);

			// Set max data usage for rendering graph
			if (max < peakUsageLong + offpeakUsageLong) {
				max = peakUsageLong + offpeakUsageLong;
			}

			mCursor.moveToNext();
		}

		mCursor.close();
		mDatabase.close();

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(peakSeries.toXYSeries());
		dataset.addSeries(offpeakSeries.toXYSeries());
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
