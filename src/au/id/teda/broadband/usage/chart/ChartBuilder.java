package au.id.teda.broadband.usage.chart;

import java.util.List;

import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.util.Log;

import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.activity.BaseActivity;

public class ChartBuilder {

	//protected static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;

	private Context mContext;

	//private double maxDataUsage = 0;
    long accumAnytime;
	long accumPeak;
	long accumPeakStacked;
	long accumOffpeak = 0;

	// Color values for focus and alternate
	public final int colorBackgroundLight;
	public final int colorBackground;
	public final int colorBase;
	public final int colorBaseLight;
	public final int colorBaseLighter;
	public final int colorBaseDark;
	public final int colorBaseDarker;
	public final int colorAccent;

    public final int iPeakColor;
    public final int iOffpeakColor;
    public final int iUploadColor;



    private final String xAxes;
	
	//private final static int LEGEND_TEXT_SIZE = 18;
	//private final static int LABEL_TEXT_SIZE = 12;

	// Chart builder constructor
	public ChartBuilder(Context context) {
		mContext = context;

		colorBase = mContext.getResources().getColor(R.color.chart_base);
		colorBaseLight = mContext.getResources().getColor(R.color.chart_base_light);
		colorBaseLighter = mContext.getResources().getColor(R.color.chart_base_lighter);
		colorBaseDark = mContext.getResources().getColor(R.color.chart_base_dark);
		colorBaseDarker = mContext.getResources().getColor(R.color.chart_base_darker);
		colorBackgroundLight = mContext.getResources().getColor(R.color.background_alt_light);
		colorBackground = mContext.getResources().getColor(R.color.background_alt);
		colorAccent = mContext.getResources().getColor(R.color.accent);

        iPeakColor = mContext.getResources().getColor(R.color.chart_peak);
        iOffpeakColor = mContext.getResources().getColor(R.color.chart_offpeak);
        iUploadColor = mContext.getResources().getColor(R.color.chart_upload);


        // Chart strings
		xAxes = mContext.getResources().getString(R.string.fragment_daily_usage_chart_x_title);

	}
	
	public int getAccentColor() {
		return colorAccent;
	}

	public int getPeakColor() {
		return iPeakColor;
	}
	
	public int getPeakFillColor(){
		return colorBaseLight;
	}
	
	public int getPeakTrendColor(){
		return colorBase;
	}
	
	public int getOffpeakColor() {
		return iOffpeakColor;
	}
	
	public int getOffpeakFillColor(){
		return colorBaseLighter;
	}
	
	public int getOffpeakTrendColor(){
		return colorBase;
	}

    public int getUploadColor() {
        return iUploadColor;
    }

    public int getUploadFillColor(){
        return colorBaseLight;
    }

    public int getUploadTrendColor(){
        return colorBase;
    }
	
	public int getLabelColor() {
		return colorBaseDarker;
	}
	
	public int getBackgroundColor() {
		return colorBackground;
	}
	
	public int getBackgroundAltColor() {
		return colorBackgroundLight;
	}
	
	/**
	 * Calculate pixel value for dip
	 * @param dip value to be converted
	 * @return pixel value of dip for current screen density
	 */
	protected float getPixelDip(int dip){
		final float scale = mContext.getResources().getDisplayMetrics().density;
		float pixels = (dip * scale + 0.5f);
		return pixels;
	}

	public float getLegendTextSize(int dip){
		return getPixelDip(dip);
	}
	
	public float getLabelsTextSize(int dip){
		return getPixelDip(dip);
	}

	/**
	 * Get string X-Axis from XML
	 * 
	 * @return String value
	 */
	public String getXAxes() {
		return xAxes;
	}

	/**
	 * Builds a bar multiple series renderer to use the provided colors.
	 * 
	 * @param colors the series renderers colors
	 * @return the bar multiple series renderer
	 */
	protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(25);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	/**
	 * Builds an XY multiple series renderer.
	 * 
	 * @param colors the series rendering colors
	 * @param styles the series point styles
	 * @return the XY multiple series renderers
	 */
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
			PointStyle[] styles) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 20, 30, 15, 20 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
	}

	/**
	 * Builds a bar multiple series dataset using the provided values.
	 * 
	 * @param titles the series titles
	 * @param values the values
	 * @return the XY multiple bar dataset
	 */
	protected XYMultipleSeriesDataset buildBarDataset(String[] titles,
			List<double[]> values) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			double[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}

	/**
	 * Builds a category renderer to use the provided colors.
	 * 
	 * @param colors the colors
	 * @return the category renderer
	 */
	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}


	/**
	 * Method for returning the number of calendar days for this period
	 * 
	 * @return
	 */
	protected int getChartDays() {
		AccountStatusHelper status = new AccountStatusHelper(mContext);

		int daysSoFare = status.getDaysSoFar();
		int daysToGo = status.getDaysToGo();

		int chartDays = daysSoFare + daysToGo + 1;

		return chartDays;
	}

    // Calculate peak upload based on percentage value between peak & offpeak
    public long peakUploadGuess(Long peak, Long offpeak, Long upload){
        // Calculate floating point percentage of peak upload
        float percentage = (float) peak / (float) (peak + offpeak);

        // Cacluate peak upload from percentage
        float peakUpload = upload * percentage;

        // Cast to long and return peak upload
        return  (long) peakUpload;
    }

    // Calculate offpeak upload based on percentage value between peak & offpeak
    public long offpeakUploadGuess(Long peak, Long offpeak, Long upload){

        // Calculate floating point percentage of peak upload
        float percentage = (float) peak / (float) (peak + offpeak);

        // Cacluate peak upload from percentage
        float offpeakUpload = upload * percentage;

        // Cast to long and return peak upload
        return (long) offpeakUpload;
    }
	

}
