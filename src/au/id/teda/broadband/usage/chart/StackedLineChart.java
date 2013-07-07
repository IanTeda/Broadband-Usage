package au.id.teda.broadband.usage.chart;

import android.util.Log;
import au.id.teda.broadband.usage.activity.BaseActivity;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.util.DailyVolumeUsage;
import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import au.id.teda.broadband.usage.R;

public class StackedLineChart extends ChartBuilder {

    // Debug tag pulled from main activity
    private final static String DEBUG_TAG = BaseActivity.DEBUG_TAG;

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

        if(mAccountInfo.isAccountAnyTime()){
            return getAnytimeSeriesDataset(usage);
        } else {
            return getPeakOffpeakSeriesDataset(usage);
        }
    }

    private XYMultipleSeriesRenderer getStackedLineChartRenderer() {
        if(mAccountInfo.isAccountAnyTime()){
            return getAnytimeSeriesRenderer();
        } else {
            return getPeakOffpeakSeriesRenderer();
        }
    }

    private XYMultipleSeriesDataset getAnytimeSeriesDataset(DailyVolumeUsage[] usage) {

        // Set daily average objects and initialise
        double anytimeAv = 0;

        // Set String value categories for graph
        CategorySeries anytimeSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_anytime));
        CategorySeries uploadSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_upload));
        CategorySeries anytimeQuotaSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_anytime_quota));

        // Get average daily useage
        long anytimeDailyAv = mAccountInfo.getAnyTimeQuotaDailyMb();


        for (DailyVolumeUsage volumeUsage : usage) {
            Long anytimeUsage = (volumeUsage.anytime / MB);

            accumAnytime = accumAnytime + anytimeUsage;

            // Set average daily line
            anytimeAv = anytimeAv + anytimeDailyAv;

            // Add current cursor values to data series
            anytimeSeries.add(accumAnytime);
            anytimeQuotaSeries.add(anytimeAv);

            //Log.d(DEBUG_TAG, "accumAnytime:" + accumAnytime + " anytimeAv:" + anytimeAv);

            // Set max data usage for rendering graph
            if (accumAnytime >= anytimeAv) {
                max = accumAnytime;
            } else {
                max = anytimeAv;
            }

            //Log.d(DEBUG_TAG, "Set Max:" + max);
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(anytimeSeries.toXYSeries());
        dataset.addSeries(anytimeQuotaSeries.toXYSeries());
        return dataset;
    }

    private XYMultipleSeriesDataset getPeakOffpeakSeriesDataset(DailyVolumeUsage[] usage) {

        // Set daily average objects and initialise
        long peakDailyAccum = 0;
        long offpeakDailyAccum = 0;
        long peakQuotaDailyAccum = 0;
        long offpeakQuotaDailyAccum = 0;

        // Set String value categories for graph
        CategorySeries peakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_peak));
        CategorySeries offpeakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_offpeak));
        CategorySeries peakQuotaSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_quota_peak));
        CategorySeries offpeakQuotaSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_quota_offpeak));


        // Get average daily usage
        long dailyPeakQuota = mAccountInfo.getPeakQuotaDailyMb();
        long dailyOffpeakQuota = mAccountInfo.getOffpeakQuotaDailyMb();

        for (DailyVolumeUsage volumeUsage : usage) {
            Long peakUsage = (volumeUsage.peak / MB);
            Long offpeakUsage = (volumeUsage.offpeak / MB);

            // Set daily usage
            peakDailyAccum = peakDailyAccum + peakUsage;
            offpeakDailyAccum = offpeakDailyAccum + offpeakUsage;
            peakQuotaDailyAccum = peakQuotaDailyAccum + dailyPeakQuota;
            offpeakQuotaDailyAccum = offpeakQuotaDailyAccum + dailyOffpeakQuota;

            // Make data stacked (achartengine does not do it by default).
            long peakStacked = peakDailyAccum;
            long offpeakStacked = peakStacked + offpeakDailyAccum;
            long peakQuotaStacked = peakQuotaDailyAccum;
            long offpeakQuotaStacked = peakQuotaStacked + offpeakQuotaDailyAccum;

            // Add current cursor values to data series
            peakSeries.add(peakStacked);
            offpeakSeries.add(offpeakStacked);
            peakQuotaSeries.add(peakQuotaStacked);
            offpeakQuotaSeries.add(offpeakQuotaStacked);

            // Set max data usage for rendering graph
            if (max < offpeakStacked) {
                max = offpeakStacked;
            } else if (max < offpeakQuotaStacked) {
                max = offpeakQuotaStacked;
            }

            //Log.d(DEBUG_TAG, "peakStacked:" + peakStacked + " offpeakStacked:" + offpeakStacked + " uploadStacked:" + uploadStacked + " max:" + max);
            //Log.d(DEBUG_TAG, "peakUsage:" + peakUsage + " offpeakUsage:" + offpeakUsage + " uploadUsage:" + uploadUsage);

        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(offpeakSeries.toXYSeries());
        dataset.addSeries(peakSeries.toXYSeries());
        dataset.addSeries(peakQuotaSeries.toXYSeries());
        dataset.addSeries(offpeakQuotaSeries.toXYSeries());

        return dataset;
    }

    private XYMultipleSeriesRenderer getPeakOffpeakSeriesRenderer() {
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

        // Quota daily series render settings
        r = new XYSeriesRenderer();
        r.setColor(getOffpeakTrendColor());
        r.setFillBelowLine(false);
        r.setFillPoints(false);
        r.setLineWidth(2);
        renderer.addSeriesRenderer(r);

        // Quota daily series render settings
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

    private XYMultipleSeriesRenderer getAnytimeSeriesRenderer() {

        // Set render object and initialise
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        // Set series render object
        XYSeriesRenderer r = new XYSeriesRenderer();

        // Anytime line chart
        r.setColor(getPeakColor());
        r.setFillBelowLine(true);
        r.setFillBelowLineColor(getPeakFillColor());
        r.setFillPoints(false);
        r.setLineWidth(1);
        renderer.addSeriesRenderer(r);

        // Anytime quota
        r = new XYSeriesRenderer();
        r.setColor(getPeakTrendColor());
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

    public double getMaxDataUsage() {
        return max;
    }
}