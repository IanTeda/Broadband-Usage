package au.id.teda.broadband.usage.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import au.id.teda.broadband.usage.helper.LayoutHelper;

public class CustomDonughtChartStacked extends View {
	
	// Debug tag pulled from main activity
	//private final static String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	private int mDaysSoFar;
	private int mDaysToGo;
	private int mDaysTotal;
	
	private long mPeakUsed;
	private long mPeakRemaining;
	private long mPeakQuota;
	
	private long mOffpeakUsed;
	private long mOffpeakRemaining;
	private long mOffpeakQuota;
	
	private int backgroundWidth;
	private int usageWidth;
	
	private int backgroundColor;
	private int backgroundColorAlt;
	private int usageColor;
	private int usageColorAlt;
	
	private int BACKGROUND_PX = 30;
	private int USAGE_PX = 25;
	private int PADDING = 4;
	
	private LayoutHelper mLayoutHelper;
	
	private final RectF mRectF;

	public CustomDonughtChartStacked(Context context) {
		super(context);
		//Log.d(DEBUG_TAG, "CustomDonughtChart");
		
		mRectF = new RectF();
		
		// Get chart colors
		ChartBuilder mChartBuilder = new ChartBuilder(context);
		backgroundColor = mChartBuilder.getBackgroundColor();
		backgroundColorAlt = mChartBuilder.getBackgroundAltColor();
		usageColor = mChartBuilder.getPeakColor();
		usageColorAlt = mChartBuilder.getOffpeakColor();
		
		// get chart widths
		mLayoutHelper = new LayoutHelper(context);
		backgroundWidth = (int) mLayoutHelper.getPxFromDp(BACKGROUND_PX);
		usageWidth = (int) mLayoutHelper.getPxFromDp(USAGE_PX);
	}
	
	public void setDays(int daysSoFar, int daysToGo){
		mDaysSoFar = daysSoFar;
		mDaysToGo = daysToGo;
		mDaysTotal = daysSoFar + daysToGo;
	}
	
	public void setPeakUsage(long peak, long quota){
		mPeakUsed = peak;
		mPeakQuota = quota;
		mPeakRemaining = quota - peak;
	}
	
	public void setOffpeakUsage(long offpeak, long quota){
		mOffpeakUsed = offpeak;
		mOffpeakQuota = quota;
		mOffpeakRemaining = quota - offpeak;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// Get view width and height
		float width = (float)getWidth();
		float height = (float)getHeight();
		
		drawPeak(canvas, width, height);
		drawOffpeak(canvas, width, height);

	}

	private void drawPeak(Canvas canvas, float width, float height) {
		// Calculate line width based on stroke width
		int lineWidth = (backgroundWidth/2);
		
		float left = 0 + lineWidth + mLayoutHelper.getPxFromDp(PADDING);
		float top = 0 + lineWidth + mLayoutHelper.getPxFromDp(PADDING);
		float right = width - lineWidth - mLayoutHelper.getPxFromDp(PADDING);
		float bottom = height - lineWidth - mLayoutHelper.getPxFromDp(PADDING);
	    
	    // Used to restrain canvas
	    mRectF.set(left, top, right, bottom); 
	    
	    // Draw days
	    drawDays(canvas, mRectF);
	    // Draw usage
	    drawPeakUsage(canvas, mRectF);
	}
	
	private void drawOffpeak(Canvas canvas, float width, float height) {
		// Calculate line width based on stroke width
		int lineWidth = (backgroundWidth/2);
		
		float left = backgroundWidth + lineWidth + mLayoutHelper.getPxFromDp(PADDING);
		float top = backgroundWidth + lineWidth + mLayoutHelper.getPxFromDp(PADDING);
		float right = width - lineWidth - backgroundWidth - mLayoutHelper.getPxFromDp(PADDING);
		float bottom = height - lineWidth - backgroundWidth - mLayoutHelper.getPxFromDp(PADDING);
	    
	    // Used to restrain canvas
	    mRectF.set(left, top, right, bottom); 
	    
	    // Draw days
	    drawDays(canvas, mRectF);
	    // Draw usage
	    drawOffpeakUsage(canvas, mRectF);
	}
	
	private void drawDays(Canvas canvas, RectF rect){
		
		// Calculate percentage ratios
		float soFar = ( (float) mDaysSoFar / mDaysTotal );
		//float toGo = ( (float) mDaysToGo / mDaysTotal );
		
		// Calculate angle based on percentage ratios
		int angleSoFar = (int) (soFar * 360);
		int angleTotal = 360;
		int angleToGo = (angleTotal - angleSoFar);
		
		// Calculate arc lengths based on angles
		int arcStart = -90;
		int arcSoFar = arcStart + angleSoFar;
		//int arcToGo = arcStart + angleTotal;
		
	    //Set the canvas values
		Paint paint = new Paint();
	    paint.setColor(backgroundColor);
	    paint.setStrokeWidth(backgroundWidth);
	    paint.setAntiAlias(true);
	    paint.setStrokeCap(Paint.Cap.BUTT);
	    paint.setStyle(Paint.Style.STROKE);
	    canvas.drawArc(rect, arcStart, angleSoFar, false, paint);
	    
	    Paint paint2 = new Paint();
	    paint2.setColor(backgroundColorAlt);
	    paint2.setStrokeWidth(backgroundWidth);
	    paint2.setAntiAlias(true);
	    paint2.setStrokeCap(Paint.Cap.BUTT);
	    paint2.setStyle(Paint.Style.STROKE);
	    canvas.drawArc(rect, arcSoFar, angleToGo, false, paint2);
	}
	
	private void drawPeakUsage(Canvas canvas, RectF rect){
		
		// Calculate percentage ratios
		float used = ( (float) mPeakUsed / mPeakQuota );
		//float remaining = ( (float) mPeakRemaining / mPeakQuota );

		// Calculate angle based on percentage ratios
		int angleUsed = (int) (used * 360);
		int angleTotal = 360;
		//int angleRemaining = (angleTotal - angleUsed);
		
		// Calculate arc lengths based on angles
		int arcStart = -90;
		//int arcUsed = arcStart + angleUsed;
		//int arcRemaining = arcStart + angleTotal;
		
		
	    //Set the canvas values
		Paint paint = new Paint();
	    paint.setColor(usageColor);
	    paint.setStrokeWidth(usageWidth);
	    paint.setAntiAlias(true);
	    paint.setStrokeCap(Paint.Cap.BUTT);
	    paint.setStyle(Paint.Style.STROKE);
	    canvas.drawArc(rect, arcStart, angleUsed, false, paint);
	}
	
	private void drawOffpeakUsage(Canvas canvas, RectF rect){
		
		// Calculate percentage ratios
		float used = ( (float) mOffpeakUsed / mOffpeakQuota );
		//float remaining = ( (float) mOffpeakRemaining / mOffpeakQuota );

		// Calculate angle based on percentage ratios
		int angleUsed = (int) (used * 360);
		//int angleTotal = 360;
		//int angleRemaining = (angleTotal - angleUsed);
		
		// Calculate arc lengths based on angles
		int arcStart = -90;
		//int arcUsed = arcStart + angleUsed;
		//int arcRemaining = arcStart + angleTotal;
		
		
	    //Set the canvas values
		Paint paint = new Paint();
	    paint.setColor(usageColorAlt);
	    paint.setStrokeWidth(usageWidth);
	    paint.setAntiAlias(true);
	    paint.setStrokeCap(Paint.Cap.BUTT);
	    paint.setStyle(Paint.Style.STROKE);
	    canvas.drawArc(rect, arcStart, angleUsed, false, paint);
	}
	
}
