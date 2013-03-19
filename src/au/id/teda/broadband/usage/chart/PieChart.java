package au.id.teda.broadband.usage.chart;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class PieChart extends ChartBuilder {
	

	// Static tag strings for logging information and debug
	//private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;

	// Context for class
	private Context mContext;
	
	private int mUploads;
	private int mDownloads;
	private int mRemaining;

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
	public View getPieChartView() {
		return ChartFactory.getPieChartView(mContext, 
				getPieChartDataSet(),
				getPieChartRenderer());
	}
	
	public void setData(int uploads, int downloads, int quota){
		mUploads = uploads;
		mDownloads = downloads;
		mRemaining = ( quota - uploads - downloads );
	}
	
	/**
	 * Method for getting pie chart data set
	 * @return pie chart category series
	 */
	protected CategorySeries getPieChartDataSet() {
		
		String UPLOADS_SERIES = "Uploads";
		String DOWNLOADS_SERIES = "Downloads";
		String REMAINING_SERIES = "Remaining";
		String CHART_TITLE = "Upload / Download";

		// Category series object and intialise
		CategorySeries series = new CategorySeries(CHART_TITLE);

		// Add data to category series
		series.add(DOWNLOADS_SERIES, mDownloads);
		series.add(UPLOADS_SERIES, mUploads);
		series.add(REMAINING_SERIES, mRemaining);
		
		// Return category series
		return series;
	}
	
	/**
	 * Render (display) settings for pie chart
	 * @return pie chart renderer
	 */
	private DefaultRenderer getPieChartRenderer() {
		//int[] colors = new int[] { getPeakColor(), getOffpeakColor(), getBackgroundColor()};
		int[] colors = new int[] { Color.RED, Color.BLUE, Color.YELLOW};
	    DefaultRenderer renderer = buildCategoryRenderer(colors);
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.TRANSPARENT);
	    renderer.setAxesColor(getBackgroundColor());
	    renderer.setPanEnabled(false);
	    renderer.setShowLabels(false);
	    renderer.setShowLegend(false);
	    renderer.setAntialiasing(true);
	    
	    return renderer;
	}

}
