package au.id.teda.broadband.usage.chart;

import android.webkit.WebStorage;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.util.DailyVolumeUsage;

//TODO: Add uploads to chart
public class StackedBarChart extends ChartBuilder {
	
	// Debug tag pulled from main activity
	//private final static String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
    // Activity context to be used
	private Context mContext;

    // Helper classes
    private AccountInfoHelper mAccountInfo;
	
	private int MB = 1000000;
	
	private double max = 0;

	public StackedBarChart(Context context) {
		super(context);
		this.mContext = context;

        mAccountInfo = new AccountInfoHelper(mContext);

    }
	
	public View getBarChartView (DailyVolumeUsage[] usage){
		return ChartFactory.getBarChartView(mContext, 
				getStackedBarChartDataSet(usage), 
				getStackedBarChartRenderer(), 
				Type.STACKED);
	}
	
	public double getMaxDataUsage() {
		return max;
	}
	
	protected XYMultipleSeriesDataset getStackedBarChartDataSet(DailyVolumeUsage[] usage) {
        if(mAccountInfo.isAccountAnyTime()){
            return getAnytimeDataset(usage);
        } else {
            return getPeakOffpeakDataset(usage);
        }

    }

    private XYMultipleSeriesRenderer getStackedBarChartRenderer() {
        if (mAccountInfo.isAccountAnyTime()){
            return getAnytimeSeriesRenderer();
        } else {
            return getPeakOffpeakSeriesRenderer();
        }
    }

    private XYMultipleSeriesDataset getAnytimeDataset(DailyVolumeUsage[] usage) {
        // Set String value categories for graph
        CategorySeries anytimeSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_anytime));
        CategorySeries uploadsSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_upload));

        long dailyQuota = mAccountInfo.getAnyTimeQuotaDailyMb();

        for (DailyVolumeUsage volumeUsage : usage) {

            long upload = (volumeUsage.uploads / MB);
            long anytime = (volumeUsage.anytime / MB);

            long anytimeUsage = anytime - upload;
            long uploadUsage = upload;

            long anytimeUsageStaked = anytimeUsage;
            long uploadUsageStaked = anytimeUsageStaked + uploadUsage;

            // Add current cursor values to data series
            uploadsSeries.add(uploadUsageStaked);
            anytimeSeries.add(anytimeUsageStaked);

            // Set max data dev for rendering graph
            if (max < uploadUsageStaked) {
                max = uploadUsageStaked * 1.05;
            }
            if (max < dailyQuota) {
                max = dailyQuota * 1.05;
            }
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(uploadsSeries.toXYSeries());
        dataset.addSeries(anytimeSeries.toXYSeries());
        return dataset;
    }

    private XYMultipleSeriesDataset getPeakOffpeakDataset(DailyVolumeUsage[] usage) {
        // Set String value categories for graph
        CategorySeries peakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_peak));
        CategorySeries uploadsSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_upload));
        CategorySeries offpeakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_offpeak));

        long dailyQuota = mAccountInfo.getPeakQuotaDailyMb() + mAccountInfo.getOffpeakQuotaDailyMb();

        for (DailyVolumeUsage volumeUsage : usage) {

            // Get and set usage values
            long peak = (volumeUsage.peak / MB);
        	long offpeak = (volumeUsage.offpeak / MB);
            long upload = (volumeUsage.uploads / MB);

            long peakUsage = peak - peakUploadGuess(peak, offpeak, upload);
            long offpeakUsage = offpeak - offpeakUploadGuess(peak, offpeak, upload);
            long uploadUsage = upload;

            long peakUsageStaked = peakUsage;
            long offpeakUsageStaked = peakUsageStaked + offpeakUsage;
            long uploadUsageStaked = offpeakUsageStaked + uploadUsage;

			// Add current cursor values to data series
			peakSeries.add(peakUsageStaked);
			offpeakSeries.add(offpeakUsageStaked);
            uploadsSeries.add(uploadUsageStaked);

			// Set max data dev for rendering graph
			if (max < uploadUsageStaked) {
				max = uploadUsageStaked * 1.05;
			}
            if (max < dailyQuota) {
                max = dailyQuota * 1.05;
            }
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(uploadsSeries.toXYSeries());
        dataset.addSeries(offpeakSeries.toXYSeries());
        dataset.addSeries(peakSeries.toXYSeries());
        return dataset;
    }

    private XYMultipleSeriesRenderer getAnytimeSeriesRenderer() {
        // Set data series color
        int[] colors = new int[] { getUploadColor(), getPeakColor() };

        // Load and initialise render objects
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);

        renderer.setXAxisMin(0);
        renderer.setXAxisMax(getChartDays());
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(getMaxDataUsage());
        renderer.setLabelsColor(getLabelColor());
        renderer.setXLabelsColor(getLabelColor());

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

    private XYMultipleSeriesRenderer getPeakOffpeakSeriesRenderer() {
        // Set data series color
        int[] colors = new int[] { getUploadColor(), getOffpeakColor(), getPeakColor() };

        // Load and initialise render objects
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);

        renderer.setXAxisMin(0);
        renderer.setXAxisMax(getChartDays());
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(getMaxDataUsage());
        renderer.setLabelsColor(getLabelColor());
        renderer.setXLabelsColor(getLabelColor());

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
