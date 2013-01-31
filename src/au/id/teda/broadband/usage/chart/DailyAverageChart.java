package au.id.teda.broadband.usage.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import au.id.teda.broadband.usage.helper.LayoutHelper;
import au.id.teda.broadband.usage.ui.BaseActivity;

public class DailyAverageChart extends View {
	
	// Debug tag pulled from main activity
	//private final static String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	private int mAverageUsage;
	private int mAverageQuota;
	
	private int usageColor;
	private int usageColorAlt;
	private int accentColor;
	
	private LayoutHelper mLayoutHelper;
	
	private Paint mDailyPaint = new Paint();
	private Paint mAveragePaint = new Paint();
	private Paint mLinePaint = new Paint();
	
	private int PADDING = 4;
	private int LINE_WIDTH = 1;
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
	
	public void setData(int average, int quota){
		mAverageUsage = average;
		mAverageQuota = quota;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// Get view width and height
		float width = (float)getWidth();
		float height = (float)getHeight();
		
		float left = 0 + padding;
		float top = 0 + padding;
		float right = width - padding;
		float bottom = height - padding;
				
		mDailyPaint.setColor(usageColorAlt);
		mDailyPaint.setStrokeWidth(height);
		mDailyPaint.setAntiAlias(true);
		mDailyPaint.setStrokeCap(Paint.Cap.BUTT);
		mDailyPaint.setStyle(Paint.Style.STROKE);
	    canvas.drawRect(left, top, right, bottom, mDailyPaint);
	    
		float leftAverage = left;
		float topAverage = top;
		float rightAverage = mAverageUsage * right / (mAverageQuota * 2);
		float bottomAverage = bottom;
	    
	    mAveragePaint.setColor(usageColor);
	    mAveragePaint.setStrokeWidth(height);
	    mAveragePaint.setAntiAlias(true);
	    mAveragePaint.setStrokeCap(Paint.Cap.BUTT);
	    mAveragePaint.setStyle(Paint.Style.STROKE);
	    canvas.drawRect(leftAverage, topAverage, rightAverage, bottomAverage, mAveragePaint);
	    
		float leftLine = (width / 2) - (lineWidth / 2);
		float topLine = 0;
		float rightLine = (width / 2) + (lineWidth / 2);
		float bottomLine = height;
	    
	    mLinePaint.setColor(accentColor);
	    mLinePaint.setStrokeWidth(lineWidth);
	    mLinePaint.setAntiAlias(true);
	    mLinePaint.setStrokeCap(Paint.Cap.BUTT);
	    mLinePaint.setStyle(Paint.Style.STROKE);
	    canvas.drawRect(leftLine, topLine, rightLine, bottomLine, mLinePaint);
		
	}
}
