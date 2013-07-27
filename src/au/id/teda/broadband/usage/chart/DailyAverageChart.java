package au.id.teda.broadband.usage.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import au.id.teda.broadband.usage.activity.BaseActivity;
import au.id.teda.broadband.usage.helper.LayoutHelper;

public class DailyAverageChart extends View {
	
	// Debug tag pulled from main activity
	//private final static String DEBUG_TAG = BaseActivity.DEBUG_TAG;

    // Percentage value of current data usage
	private double fDataUsedPercentage;

	private int usageColor;
	private int accentColor;
	
	private LayoutHelper mLayoutHelper;
	
	private Paint mAveragePaint = new Paint();
	private Paint mLinePaint = new Paint();
	
	private int LINE_WIDTH = 2;
	private float lineWidth;
	
	public DailyAverageChart(Context context) {
		super(context);
		
		// Get chart colors
		ChartBuilder mChartBuilder = new ChartBuilder(context);
		usageColor = mChartBuilder.getPeakColor();
		accentColor = mChartBuilder.getAccentColor();
		
		// Calculate chart dp's
		mLayoutHelper = new LayoutHelper(context);
		lineWidth = mLayoutHelper.getPxFromDp(LINE_WIDTH);
	}
	
	public void setData(int data, int quota){

        int max = quota * 2;

        // Check to see if we have zero values
		if (quota == 0 || data == 0){
			fDataUsedPercentage = 0.01;
		} else {
            // Calculate percentage of usage
            fDataUsedPercentage = (double) data / (double) quota;
        }

    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// Get width and height of canvas and cast to float
		float width = (float) getWidth();
		float height = (float) getHeight();

        // Calculate co-ordinates for data usage rectangle
		float fLeftData = 0;
		float fTopData = 0;
		float fRightData = (float) ((width / 2) * fDataUsedPercentage);
		float fBottomData = height;

        // Draw data usage rectangle
        mAveragePaint.setColor(usageColor);
	    mAveragePaint.setStyle(Paint.Style.FILL);
	    canvas.drawRect(fLeftData, fTopData, fRightData, fBottomData, mAveragePaint);

        // Calculate co-ordinates for average line
		float leftLine = (float) ((width * 0.5));
		float topLine = 0;
		float bottomLine = height;

        // Draw the average line
        mLinePaint.setColor(accentColor);
        mLinePaint.setStrokeWidth(lineWidth);
        canvas.drawLine(leftLine, topLine, leftLine, bottomLine, mLinePaint);
		
	}
}
