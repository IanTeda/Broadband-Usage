package au.id.teda.broadband.usage.chart;

import org.achartengine.ChartFactory;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.util.Log;
import android.view.View;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;

/**
 * Doughnut chart class for display chart
 * @author Ian Teda
 *
 */
public class DoughnutChart extends ChartBuilder {

	//private static final String DEBUG_TAG = "bbusage";
	
	// Activity context
	private Context mContext;
	
	private final static long GB = 1000000000;
	private final static long MB = 1000;
	private final static long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

	/**
	 * DoughnutChart constructor
	 * @param context
	 */
	public DoughnutChart(Context context) {
		super(context);
		mContext = context;
	}
	
	/**
	 * Get Doughnut Chart
	 * @return doughnut chart view
	 */
	public View getDoughnutChartView() {
		return ChartFactory.getDoughnutChartView(mContext
				,getDoughnutChartDataSeries()
				,getDoughnutChartRenderer());
	}

	/**
	 * Get doughnut chart data series
	 * @return chart data series
	 */
	private MultipleCategorySeries getDoughnutChartDataSeries() {
		
		// Static string values and initialise from XML values
		final String DAYS = mContext.getResources().getString(R.string.chart_doughnut_days);
		final String DAYS_SO_FAR = mContext.getResources().getString(R.string.chart_doughnut_days_soFar);
		final String DAYS_TO_GO = mContext.getResources().getString(R.string.chart_doughnut_days_toGo);
		
		final String PEAK = mContext.getResources().getString(R.string.chart_doughnut_peak);
		final String PEAK_SO_FAR = mContext.getResources().getString(R.string.chart_doughnut_peak_soFar);
		final String PEAK_TO_GO = mContext.getResources().getString(R.string.chart_doughnut_peak_toGo);
		
		final String OFFPEAK = mContext.getResources().getString(R.string.chart_doughnut_offpeak);
		final String OFFPEAK_SO_FAR = mContext.getResources().getString(R.string.chart_doughnut_offpeak_soFar);
		final String OFFPEAK_TO_GO = mContext.getResources().getString(R.string.chart_doughnut_offpeak_toGo);
		
		// Account object and initialise
		AccountStatusHelper status = new AccountStatusHelper(mContext);
		AccountInfoHelper info = new AccountInfoHelper(mContext);

		// Peak data
		long peak = status.getPeakDataUsed() / GB;
		long peakQuota = info.getPeakQuota() / MB;
		long peakRemaining = peakQuota - peak;
		double[] peakDouble = { peak, peakRemaining }; 
		String[] peakCats = { PEAK_SO_FAR, PEAK_TO_GO };
		
		// Offpeak data
		long offpeak = status.getOffpeakDataUsed() / GB;
		long offpeakQuota = info.getPeakQuota() / MB;
		long offpeakRemaining = offpeakQuota - offpeak;
		double[] offpeakDouble = { offpeak, offpeakRemaining };
		String[] offpeakCats = { OFFPEAK_SO_FAR, OFFPEAK_TO_GO };
		
		// Days
		long daysSoFare = status.getDaysSoFar();
		long daysToGo = status.getDaysToGo();
		double[] daysDouble = { daysSoFare, daysToGo };
		String[] dayCats = { DAYS_SO_FAR, DAYS_TO_GO };
		
		// Data series and initialise
		MultipleCategorySeries series = new MultipleCategorySeries(DAYS);
		series.add(DAYS_TO_GO, dayCats, daysDouble);
		series.add(PEAK, peakCats, peakDouble );
		series.add(OFFPEAK, offpeakCats, offpeakDouble);
		
		// Return data series
		return series;
	}
	
	/**
	 * Doughnut chart display settings
	 * @return chart renderer
	 */
	private DefaultRenderer getDoughnutChartRenderer() {

	    DefaultRenderer renderer = new DefaultRenderer();
	    
	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	    //r.setColor(getPeakColor());
		renderer.addSeriesRenderer(r);
	    
		r = new SimpleSeriesRenderer();
		//r.setColor(getPeakFillColor());
		renderer.addSeriesRenderer(r);
		
	    renderer.setLabelsTextSize(getLabelsTextSize());
	    renderer.setLegendTextSize(getLegendTextSize());
		
	    renderer.setPanEnabled(false);
	    renderer.setShowLegend(false);
	    renderer.setFitLegend(true);
	    
	    //renderer.setInScroll(true);
	    
	    return renderer;
	}

}
