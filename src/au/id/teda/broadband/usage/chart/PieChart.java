package au.id.teda.broadband.usage.chart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;

public class PieChart extends ChartBuilder {
	
	private static final String DEBUG_TAG = "bbusage";
	
	private final static long GB = 1000000000;
	private final static long MB = 1000;
	
	private static final String PEAK = "Peak";
	private static final String OFFPEAK = "Offpeak";
	private static final String REMAINING = "Remaining";
	private static final String TITLE = "Data Usage";

	// Context for class
	private Context mContext;

	/**
	 * PieChart constructor
	 * @param context
	 */
	public PieChart(Context context) {
		super(context);
		mContext = context;
	}
	
	/**
	 * Public method for returning pie chart view
	 * @return pie chart view
	 */
	public GraphicalView getPieChartView() {
		return ChartFactory.getPieChartView(mContext, 
				getPieChartDataSet(),
				getPieChartRenderer());
	}
	
	/**
	 * Method for getting pie chart data set
	 * @return pie chart category series
	 */
	protected CategorySeries getPieChartDataSet() {

		// Account object and initialise
		AccountStatusHelper status = new AccountStatusHelper(mContext);
		AccountInfoHelper info = new AccountInfoHelper(mContext);

		// Get values from account
		long peak = status.getPeakDataUsed() / GB;
		long offpeak = status.getOffpeakDataUsed() / GB;
		long peakQuota = info.getPeakQuota() / MB;
		long offpeakQuota = info.getOffpeakQuota() / MB;
		long remaining = peakQuota + offpeakQuota - peak - offpeak;

		// Category series object and intialise
		CategorySeries series = new CategorySeries(TITLE);

		// Add data to category series
		series.add(PEAK, peak);
		series.add(OFFPEAK, offpeak);
		series.add(REMAINING, remaining);

		// Return category series
		return series;
	}
	
	/**
	 * Render (display) settings for pie chart
	 * @return pie chart renderer
	 */
	private DefaultRenderer getPieChartRenderer() {
		int[] colors = new int[] { getPeakColor(), getOffpeakColor(), getRemainingFillColor() };
	    DefaultRenderer renderer = buildCategoryRenderer(colors);
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.TRANSPARENT);
	    renderer.setAxesColor(getBackgroundColor());
	    renderer.setPanEnabled(false);
	    renderer.setFitLegend(true);
	    renderer.setShowLabels(false);
	    renderer.setLabelsTextSize(getLabelsTextSize());
	    renderer.setLegendTextSize(getLegendTextSize());
	    renderer.setAntialiasing(true);
	    
	    return renderer;
	}

}
