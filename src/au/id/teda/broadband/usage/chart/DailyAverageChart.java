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
	private final static String DEBUG_TAG = BaseActivity.DEBUG_TAG;

    // Percentage value of current data usage
	private double fDataUsedPercentage;

    // Quota usage
    private double fQuotaUsage;

    // Max usage to show on graph
    private double fMaxUsage;
	
	private int usageColor;
	private int usageColorAlt;
	private int accentColor;
	
	private LayoutHelper mLayoutHelper;
	
	private Paint mDailyPaint = new Paint();
	private Paint mAveragePaint = new Paint();
	private Paint mLinePaint = new Paint();
	
	private int PADDING = 4;
	private int LINE_WIDTH = 2;
	private float padding;
	private float lineWidth;
	
	public DailyAverageChart(Context context) {
		super(context);
		
		// Get chart colors
		ChartBuilder mChartBuilder = new ChartBuilder(context);
		usageColor = mChartBuilder.getPeakColor();
		usageColorAlt = mChartBuilder.getBackgroundAltColor();
		accentColor = mChartBuilder.getAccentColor();
		
		// Calculate chart dp's
		mLayoutHelper = new LayoutHelper(context);
		padding = mLayoutHelper.getPxFromDp(PADDING);
		lineWidth = mLayoutHelper.getPxFromDp(LINE_WIDTH);
	}
	
	public void setData(int data, int quota){

        Log.d(DEBUG_TAG, "data:" + data + " quota:" + quota);

        fQuotaUsage = quota;
        fMaxUsage = fQuotaUsage * 2;

		// Check to see if we have zero values
		if (quota == 0 || data == 0){
			fDataUsedPercentage = 0.01;
		} else if (data == quota) {
			fDataUsedPercentage = 0.5;
		} else if (data < quota){
            fDataUsedPercentage = ( fMaxUsage / (fQuotaUsage - data) - 2) / 100;
        } else {
            fDataUsedPercentage = (100 - (fMaxUsage/(data - fQuotaUsage)) + 2) / 100;
        }

        Log.d(DEBUG_TAG, "fDataUsedPercentage:" + fDataUsedPercentage + " fMaxUsage:" + fMaxUsage + " fQuotaUsage:" + fQuotaUsage);

    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// Get width and height of canvas and cast to float
		float width = (float) getWidth();
		float height = (float) getHeight();

        // Calculate co-ordinates for background rectangle
		float left = 0 + padding;
		float top = 0 + padding;
		float right = width - padding;
		float bottom = height - padding;

        Log.d(DEBUG_TAG, "left:" + left + " right:" + right);

        // Draw background rectangle
		mDailyPaint.setColor(usageColorAlt);
		mDailyPaint.setStyle(Paint.Style.FILL);
	    canvas.drawRect(left, top, right, bottom, mDailyPaint);

        // Calculate co-ordinates for data usage rectangle
		float fLeftData = left;
		float fTopData = top;
		float fRightData = (float) ((width) * fDataUsedPercentage);
		float fBottomData = bottom;

        Log.d(DEBUG_TAG, "fLeftData:" + fLeftData + " fRightData:" + fRightData);

        // Draw data usage rectangle
        mAveragePaint.setColor(usageColor);
	    mAveragePaint.setStyle(Paint.Style.FILL);
	    canvas.drawRect(fLeftData, fTopData, fRightData, fBottomData, mAveragePaint);
	    
		float leftLine = (float) ((width * 0.5));
		float topLine = 0;
		float rightLine = (float) ((width * 0.5));
		float bottomLine = height;

        Log.d(DEBUG_TAG, "leftLine:" + leftLine + " rightLine:" + rightLine);

        // Draw the average line
        mLinePaint.setColor(accentColor);
        mLinePaint.setStrokeWidth(lineWidth);
        canvas.drawLine(leftLine, topLine, leftLine, bottomLine, mLinePaint);
		
	}
}
