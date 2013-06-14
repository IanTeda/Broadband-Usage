package au.id.teda.broadband.dev.chart;

import android.util.Log;
import au.id.teda.broadband.dev.helper.AccountInfoHelper;
import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import au.id.teda.broadband.dev.R;
import au.id.teda.broadband.dev.util.DailyVolumeUsage;

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

        for (DailyVolumeUsage volumeUsage : usage) {
            Long anytimeUsage = (volumeUsage.anytime / MB);

            // Add current cursor values to data series
            anytimeSeries.add(anytimeUsage);

            // Set max data dev for rendering graph
            if (max < anytimeUsage) {
                max = anytimeUsage * 1.05;
            }
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(anytimeSeries.toXYSeries());
        return dataset;
    }

    private XYMultipleSeriesDataset getPeakOffpeakDataset(DailyVolumeUsage[] usage) {
        // Set String value categories for graph
        CategorySeries peakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_peak));
        CategorySeries offpeakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_offpeak));

        for (DailyVolumeUsage volumeUsage : usage) {
        	Long peakUsage = (volumeUsage.peak / MB);
        	Long offpeakUsage = (volumeUsage.offpeak / MB);

			if (peakUsage > offpeakUsage) {
				peakUsage = peakUsage + offpeakUsage;
			} else {
				offpeakUsage = offpeakUsage + peakUsage;
			}

			// Add current cursor values to data series
			peakSeries.add(peakUsage);
			offpeakSeries.add(offpeakUsage);

			// Set max data dev for rendering graph
			if (max < peakUsage + offpeakUsage) {
				max = peakUsage + offpeakUsage;
			}
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(peakSeries.toXYSeries());
        dataset.addSeries(offpeakSeries.toXYSeries());
        return dataset;
    }

    private XYMultipleSeriesRenderer getAnytimeSeriesRenderer() {
        // Set data series color
        int[] colors = new int[] { getPeakColor()};

        // Load and initialise render objects
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        //TODO: Add individual series renders

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
        int[] colors = new int[] { getPeakColor(), getOffpeakColor() };

        // Load and initialise render objects
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        //TODO: Add individual series renders

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
