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

        // Set daily accum objects and initialise
        long anytimeDailyAccum = 0;
        long uploadDailyAccum = 0;
        long quotaDailyAccum = 0;

        // Set String value categories for graph
        CategorySeries anytimeSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_anytime));
        CategorySeries uploadSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_upload));
        CategorySeries anytimeQuotaSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_anytime_quota));

        // Get average daily useage
        long quotaDaily = mAccountInfo.getAnyTimeQuotaDailyMb();


        for (DailyVolumeUsage volumeUsage : usage) {
            // Get values from array
            Long anytime = (volumeUsage.anytime / MB);
            Long upload  = (volumeUsage.uploads / MB);

            // Take off upload usage
            Long anytimeUsage = anytime - upload;
            Long uploadUsage = upload;

            // Calculate accumalitive values
            anytimeDailyAccum = anytimeDailyAccum + anytimeUsage;
            uploadDailyAccum = uploadDailyAccum + uploadUsage;
            quotaDailyAccum = quotaDailyAccum + quotaDaily;

            // Calculate stacked values
            long anytimeStacked = anytimeDailyAccum;
            long uploadStacked = anytimeDailyAccum + uploadDailyAccum;

            // Add current cursor values to data series
            anytimeSeries.add(anytimeStacked);
            uploadSeries.add(uploadStacked);
            anytimeQuotaSeries.add(quotaDailyAccum);

            //Log.d(DEBUG_TAG, "anytimeUsage:" + anytimeUsage + " uploadUsage:" + uploadUsage + " quotaDaily:" + quotaDaily);
            //Log.d(DEBUG_TAG, "anytimeDailyAccum:" + anytimeDailyAccum + " uploadDailyAccum:" + uploadDailyAccum + " quotaDailyAccum:" + quotaDailyAccum);

            // Set max data usage for rendering graph
            if (max <= uploadDailyAccum) {
                max = uploadDailyAccum * 1.05;
            }

            if (max <= quotaDailyAccum ){
                max = quotaDailyAccum * 1.05;
            }
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(uploadSeries.toXYSeries());
        dataset.addSeries(anytimeSeries.toXYSeries());
        dataset.addSeries(anytimeQuotaSeries.toXYSeries());
        return dataset;
    }

    private XYMultipleSeriesDataset getPeakOffpeakSeriesDataset(DailyVolumeUsage[] usage) {

        // Set daily accum objects and initialise
        long peakDailyAccum = 0;
        long offpeakDailyAccum = 0;
        long uploadDailyAccum = 0;
        long quotaDailyAccum = 0;

        // Set String value categories for graph
        CategorySeries peakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_peak));
        CategorySeries offpeakSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_offpeak));
        CategorySeries uploadSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_upload));
        CategorySeries quotaSeries = new CategorySeries(mContext.getString(R.string.chart_data_series_quota));


        // Get average daily usage
        long dailyQuota = mAccountInfo.getPeakQuotaDailyMb() + mAccountInfo.getOffpeakQuotaDailyMb();

        for (DailyVolumeUsage volumeUsage : usage) {
            // Get values from array & take off upload guess
            Long peak = (volumeUsage.peak / MB);
            Long offpeak = (volumeUsage.offpeak / MB);
            Long upload = (volumeUsage.uploads / MB);
            Long quota = dailyQuota;

            // Calculate usage values
            Long peakUsage = peak - peakUploadGuess(peak, offpeak, upload);
            Long offpeakUsage = offpeak - offpeakUploadGuess(peak, offpeak, upload);
            Long uploadUsage = upload;

            // Set daily accumulated usage for stacking chart
            peakDailyAccum = peakDailyAccum + peakUsage;
            offpeakDailyAccum = offpeakDailyAccum + offpeakUsage;
            uploadDailyAccum = uploadDailyAccum + uploadUsage;
            quotaDailyAccum = quotaDailyAccum + quota;

            // Make data stacked for chart.
            long peakStacked = peakDailyAccum;
            long offpeakStacked = peakStacked + offpeakDailyAccum;
            long uploadStacked = offpeakStacked + uploadDailyAccum;

            // Add current cursor values to data series
            peakSeries.add(peakStacked);
            offpeakSeries.add(offpeakStacked);
            uploadSeries.add(uploadStacked);
            quotaSeries.add(quotaDailyAccum);

            // Set max data usage for rendering graph
            if (max < uploadStacked) {
                max = uploadStacked * 1.05;
            }

            if (max < quotaDailyAccum) {
                max = quotaDailyAccum * 1.05;
            }

            //Log.d(DEBUG_TAG, "dailyQuota:" + dailyQuota + " | quotaDailyAccum:" + quotaDailyAccum);
            //Log.d(DEBUG_TAG, "peakStacked:" + peakStacked + " offpeakStacked:" + offpeakStacked + " uploadStacked:" + uploadStacked + " max:" + max);
            //Log.d(DEBUG_TAG, "peakUsage:" + peakUsage + " offpeakUsage:" + offpeakUsage + " uploadUsage:" + uploadUsage);

        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(uploadSeries.toXYSeries());
        dataset.addSeries(offpeakSeries.toXYSeries());
        dataset.addSeries(peakSeries.toXYSeries());
        dataset.addSeries(quotaSeries.toXYSeries());


        return dataset;
    }

    private XYMultipleSeriesRenderer getPeakOffpeakSeriesRenderer() {

        // Set render object and initialise
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        // Set series render object
        XYSeriesRenderer r = new XYSeriesRenderer();

        /** Cannot get this one to work for depreciated call below
         * FillOutsideLine fill = FillOutsideLine.BOUNDS_BELOW;
         * fill.setColor(Color.MAGENTA);
         * xyRenderer.addFillOutsideLine(fill);
         */

        // Upload series render settings
        r.setColor(getUploadColor());
        r.setFillBelowLine(true);
        r.setFillBelowLineColor(getUploadColor());
        r.setFillPoints(false);
        r.setLineWidth(0);
        renderer.addSeriesRenderer(r);

        // Offpeak series render settings
        r = new XYSeriesRenderer();
        r.setColor(getOffpeakColor());
        r.setFillBelowLine(true);
        r.setFillBelowLineColor(getOffpeakColor());
        r.setFillPoints(false);
        r.setLineWidth(1);
        renderer.addSeriesRenderer(r);

        // peak series render settings
        r = new XYSeriesRenderer();
        r.setColor(getPeakColor());
        r.setFillBelowLine(true);
        r.setFillBelowLineColor(getPeakColor());
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
        XYSeriesRenderer r;

        // Anytime line chart
        r = new XYSeriesRenderer();
        r.setColor(getPeakColor());
        r.setFillBelowLine(true);
        r.setFillBelowLineColor(getUploadColor());
        r.setFillPoints(false);
        r.setLineWidth(0);
        renderer.addSeriesRenderer(r);

        // Upload line chart
        r = new XYSeriesRenderer();
        r.setColor(getPeakColor());
        r.setFillBelowLine(true);
        r.setFillBelowLineColor(getPeakColor());
        r.setFillPoints(false);
        r.setLineWidth(0);
        renderer.addSeriesRenderer(r);

        // Quota line
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