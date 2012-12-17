package au.id.teda.broadband.usage.chart;

import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;

public class ChartBuilder {

	private static final String DEBUG_TAG = "bbusage";

	private Context mContext;

	private static final String PEAK = "Peak";
	private static final String OFFPEAK = "Offpeak";
	private static final String REMAINING = "Remaining";
	private static final String TITLE = "Data Usage";

	private double maxDataUsage = 0;
	long accumPeak;
	long accumPeakStacked;
	long accumOffpeak = 0;

	// Color values for focus and alternate
	private final int peakColor;
	private final int peakFillColor;
	private final int peakTrendColor;
	private final int offpeakColor;
	private final int offpeakFillColor;
	private final int offpeakTrendColor;
	private final int remainingColor;
	private final int remainingFillColor;
	private final int axesColor;
	private final int labelColor;
	private final int backgroundColor;

	private final String xAxes;
	
	private final static int legendTextSize = 18;
	private final static int labelsTextSize = 12;

	// Chart builder constructor
	public ChartBuilder(Context context) {
		mContext = context;

		// Chart colours
		peakColor = mContext.getResources().getColor(R.color.chart_peak_color);
		peakFillColor = mContext.getResources().getColor(R.color.chart_peak_fill_color);
		peakTrendColor = mContext.getResources().getColor(R.color.chart_peak_trend_color);
		offpeakColor = mContext.getResources().getColor(R.color.chart_offpeak_color);
		offpeakFillColor = mContext.getResources().getColor(R.color.chart_offpeak_fill_color);
		offpeakTrendColor = mContext.getResources().getColor(R.color.chart_offpeak_trend_color);
		remainingColor = mContext.getResources().getColor(R.color.chart_remaining_color);
		remainingFillColor = mContext.getResources().getColor(R.color.chart_remaining_fill_color);
		axesColor = mContext.getResources().getColor(R.color.chart_axes_color);
		labelColor = mContext.getResources().getColor(R.color.chart_label_color);
		backgroundColor = mContext.getResources().getColor(R.color.background);

		// Chart strings
		xAxes = mContext.getResources().getString(R.string.chart_x_title);

	}
	
	/**
	 * Calculate pixel value for dip
	 * @param dip value to be converted
	 * @return pixel value of dip for current screen density
	 */
	protected int getPixelDip(int dip){
		final float scale = mContext.getResources().getDisplayMetrics().density;
		int pixels = (int) (dip * scale + 0.5f);
		return pixels;
	}
	
	public int getLegendTextSize(){
		return getPixelDip(legendTextSize);
	}
	
	public int getLabelsTextSize(){
		return getPixelDip(labelsTextSize);
	}


	/**
	 * Get peak color from XML
	 * 
	 * @return int color value
	 */
	public int getPeakColor() {
		return peakColor;
	}
	
	public int getPeakTrendColor(){
		return peakTrendColor;
	}
	
	public int getOffpeakTrendColor(){
		return offpeakTrendColor;
	}

	/**
	 * Get offpeak color from XML
	 * 
	 * @return int color value
	 */
	public int getOffpeakColor() {
		return offpeakColor;
	}

	/**
	 * Get chart axes color from XML
	 * 
	 * @return int color value
	 */
	public int getAxesColor() {
		return axesColor;
	}

	/**
	 * Get chart labal color from XML
	 * 
	 * @return int color value
	 */
	public int getLabelColor() {
		return labelColor;
	}

	/**
	 * Get chart background color from XML
	 * 
	 * @return int color value
	 */
	public int getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Get peak fill color from XML
	 * 
	 * @return int color value
	 */
	public int getPeakFillColor() {
		return peakFillColor;
	}

	/**
	 * Get offpeak fill color from XML
	 * 
	 * @return int color value
	 */
	public int getOffpeakFillColor() {
		return offpeakFillColor;
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
	 * Get remaining color from XML
	 * 
	 * @return int color value
	 */
	public int getRemainingColor() {
		return remainingColor;
	}
	
	/**
	 * Get remaining fill color from XML
	 * 
	 * @return int color value
	 */
	public int getRemainingFillColor() {
		return remainingFillColor;
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

		int chartDays = daysSoFare + daysToGo;

		return chartDays;
	}

	

}
