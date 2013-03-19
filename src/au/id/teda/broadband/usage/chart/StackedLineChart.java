package au.id.teda.broadband.usage.chart;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.util.DailyVolumeUsage;

public class StackedLineChart extends ChartBuilder {

	// Debug tag pulled from main activity
	//private final static String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	// Helper classes
	private AccountInfoHelper mAccountInfo;
	
    // Activity context to be used
	private Context mContext;
	
	private int MB = 1000000;
	
	private double max = 0;
	

	public StackedLineChart(Context context) {
		super(context);
		this.mContext = context;
		
		mAccountInfo = new AccountInfoHelper(mContext);
	}
	
	public View getChartView(DailyVolumeUsage[] usage) {
		return ChartFactory.getLineChartView(mContext, 
				getStackedLineChartDataSet(usage),
				getStackedLineChartRenderer());
	}
	
	protected XYMultipleSeriesDataset getStackedLineChartDataSet(DailyVolumeUsage[] usage) {
		
		// Set daily average objects and initialise
		double peakAv = 0;
		double offpeakAv = 0;
		double offPeakAvStacked = 0;

		// Set String value categories for graph
		CategorySeries peakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_peak));
		CategorySeries offpeakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_offpeak));
		CategorySeries peakQuotaSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_peak_quota));
		CategorySeries offpeakQuotaSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_offpeak_quota));
		
		// Get average daily useage
		long peakDailyAv = mAccountInfo.getPeakQuotaDailyMb();
		long offpeakDailyAv = mAccountInfo.getOffpeakQuotaDailyMb();
		
		
        for (DailyVolumeUsage volumeUsage : usage) {
        	Long peakUsage = (volumeUsage.peak / MB);
        	Long offpeakUsage = (volumeUsage.offpeak / MB);
        	
        	accumPeak = accumPeak + peakUsage;
        	accumOffpeak = accumOffpeak + offpeakUsage;
        	
			// Set average daily line
			peakAv = peakAv + peakDailyAv;
			offpeakAv = offpeakAv + offpeakDailyAv;
			offPeakAvStacked = peakAv + offpeakAv;

			// Make data stacked (achartengine does not do it by default).
			accumPeakStacked = accumPeak + accumOffpeak;

			// Add current cursor values to data series
			peakSeries.add(accumPeakStacked);
			offpeakSeries.add(accumOffpeak);
			peakQuotaSeries.add(peakAv);
			offpeakQuotaSeries.add(offPeakAvStacked);

			// Set max data usage for rendering graph
			if (max < (accumPeak + accumOffpeak)) {
				max = accumPeak + accumOffpeak;
			}
        }

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(peakSeries.toXYSeries());
		dataset.addSeries(offpeakSeries.toXYSeries());
		dataset.addSeries(peakQuotaSeries.toXYSeries());
		dataset.addSeries(offpeakQuotaSeries.toXYSeries());
		return dataset;
	}
	
	public double getMaxDataUsage() {
		long max = mAccountInfo.getPeakQuotaMb() + mAccountInfo.getOffpeakQuotaMb();
		return max;
	}

	private XYMultipleSeriesRenderer getStackedLineChartRenderer() {
		// Set render object and initialise
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

	    // Set series render object
	    XYSeriesRenderer r = new XYSeriesRenderer();
	    
	    // Peak series render settings
	    r.setColor(getPeakColor());
	    r.setFillBelowLine(true);
	    r.setFillBelowLineColor(getPeakFillColor());
	    r.setFillPoints(false);
	    r.setLineWidth(1);
	    renderer.addSeriesRenderer(r);
	    
	    // Offpeak series render settings
	    r = new XYSeriesRenderer();
	    r.setColor(getOffpeakColor());
	    r.setFillBelowLine(true);
	    r.setFillBelowLineColor(getOffpeakFillColor());
	    r.setFillPoints(false);
	    r.setLineWidth(1);
	    renderer.addSeriesRenderer(r);
	    
	    // Peak daily series render settings
	    r = new XYSeriesRenderer();
	    r.setColor(getPeakTrendColor());
	    r.setFillBelowLine(false);
	    r.setFillPoints(false);
	    r.setLineWidth(2);
	    renderer.addSeriesRenderer(r);
	    
	    // Offpeak daily series render settings
	    r = new XYSeriesRenderer();
	    r.setColor(getOffpeakTrendColor());
	    r.setFillBelowLine(false);
	    r.setFillPoints(false);
	    r.setLineWidth(2);
	    renderer.addSeriesRenderer(r);
	    
	    // Graph render settings
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.TRANSPARENT);
	    renderer.setMarginsColor(getBackgroundColor());
	    renderer.setPanEnabled(false, false);
	    renderer.setShowLegend(true);
	    renderer.setFitLegend(true);
	    renderer.setLabelsTextSize(getLabelsTextSize(12));
	    renderer.setLegendTextSize(getLabelsTextSize(12));
	    renderer.setAxesColor(getLabelColor());
	    renderer.setAntialiasing(true);
	    renderer.setXAxisMin(0);
		renderer.setXAxisMax(getChartDays());
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(getMaxDataUsage());
		renderer.setAxesColor(getLabelColor());
		renderer.setLabelsColor(getLabelColor());
		renderer.setXLabelsColor(getLabelColor());
	    
	    // Set point size to 0 to hide
	    renderer.setPointSize(0f);

	    return renderer;
	}
}
