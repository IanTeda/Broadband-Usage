package au.id.teda.broadband.usage.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import au.id.teda.broadband.usage.ui.MainActivity;

public class CustomDonughtChart extends View {
	
	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	private int mDaysSoFar;
	private int mDaysToGo;
	private int mDaysTotal;
	
	private int mPeakUsed;
	private int mPeakRemaining;
	private int mPeakQuota;
	
	private int mOffpeakUsed;
	private int mOffpeakRemaining;
	private int mOffpeakQuota;

	public CustomDonughtChart(Context context) {
		super(context);
		Log.d(DEBUG_TAG, "CustomDonughtChart");
	}
	
	public void setDays(int daysSoFar, int daysToGo){
		mDaysSoFar = daysSoFar;
		mDaysToGo = daysToGo;
		mDaysTotal = daysSoFar + daysToGo;
	}
	
	public void setPeakUsage(int used, int quota){
		mPeakUsed = used;
		mOffpeakQuota = quota;
		mPeakRemaining = quota - used;
	}
	
	public void setOffpeakUsage(int used, int quota){
		mOffpeakUsed = used;
		mOffpeakQuota = quota;
		mOffpeakRemaining = quota - used;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		float mWidth = (float)getWidth();
		float mHeight = (float)getHeight();
		float mRadius;

		if (mWidth > mHeight){
			mRadius = mHeight/3;
		}else{
			mRadius = mWidth/3;
		}
	    
	    //Example values
	    
	    //Used to restrain canvas
		final RectF rect = new RectF();
	    rect.set(mWidth/2- mRadius, mHeight/2 - mRadius, mWidth/2 + mRadius, mHeight/2 + mRadius); 
	    
	    drawDays(canvas, rect);
	    
	    float mRadius2 = (float) (mRadius * 1);
		final RectF rect2 = new RectF();
	    rect2.set(mWidth/2 - mRadius2, mHeight/2 - mRadius2, mWidth/2 + mRadius2, mHeight/2 + mRadius2); 
	    drawPeak(canvas, rect2);
	    
	    //Used to restrain canvas	
		float mRadius3 = (float) (mRadius * 0.7);
		final RectF rect3 = new RectF();
		rect3.set(mWidth/2- mRadius3, mHeight/2 - mRadius3, mWidth/2 + mRadius3, mHeight/2 + mRadius3); 
		drawOffpeak(canvas, rect3);
	   
	    /**
	    //Set the canvas values
	    Paint paint2 = new Paint();
	    paint2.setColor(Color.BLUE);
	    paint2.setStrokeWidth(40);
	    paint2.setAntiAlias(true);
	    paint2.setStrokeCap(Paint.Cap.BUTT);
	    paint2.setStyle(Paint.Style.STROKE);
	    
	    //Used to restrain canvas	
		float mRadius2 = (float) (mRadius * 1);
		final RectF rect2 = new RectF();
	    rect2.set(mWidth/2- mRadius2, mHeight/2 - mRadius2, mWidth/2 + mRadius2, mHeight/2 + mRadius2); 
		
	    //Draw the canvas
	    canvas.drawArc(rect2, -90, 320, false, paint2);
	    
	    //Set the canvas values
	    Paint paint3 = new Paint();
	    paint3.setColor(Color.RED);
	    paint3.setStrokeWidth(40);
	    paint3.setAntiAlias(true);
	    paint3.setStrokeCap(Paint.Cap.BUTT);
	    paint3.setStyle(Paint.Style.STROKE);
	    
	    //Used to restrain canvas	
		float mRadius3 = (float) (mRadius * 0.70);
		final RectF rect3 = new RectF();
		rect3.set(mWidth/2- mRadius3, mHeight/2 - mRadius3, mWidth/2 + mRadius3, mHeight/2 + mRadius3); 
		
	    //Draw the canvas
	    canvas.drawArc(rect3, -90, 330, false, paint3);
	    **/

	}
	
	private void drawDays(Canvas canvas, RectF rect){
		//Log.d(DEBUG_TAG, "mDaysSoFar:" + mDaysSoFar + " mDaysToGo:" + mDaysToGo + " = mDaysTotal:" + mDaysTotal );
		
		float soFar = ( (float) mDaysSoFar / mDaysTotal );
		float toGo = ( (float) mDaysToGo / mDaysTotal );
		
		//Log.d(DEBUG_TAG, "soFar:" + soFar + " toGo: " + toGo);
		
		int angleSoFar = (int) (soFar * 360);
		int angleTotal = 360;
		int angleToGo = (angleTotal - angleSoFar);
		
		//Log.d(DEBUG_TAG, "angleSoFar:" + angleSoFar + " angleToGo:" + angleToGo + " angleTotal:" + angleTotal);
		
		int arcStart = -90;
		int arcSoFar = arcStart + angleSoFar;
		int arcToGo = arcStart + angleTotal;
		
		//Log.d(DEBUG_TAG, "arcStart:" + arcStart + " arcSoFar:" + arcSoFar + " arcToGo:" + arcToGo);
		
		
	    //Set the canvas values
		Paint paint = new Paint();
	    paint.setColor(Color.GRAY);
	    paint.setStrokeWidth(100);
	    paint.setAntiAlias(true);
	    paint.setStrokeCap(Paint.Cap.BUTT);
	    paint.setStyle(Paint.Style.STROKE);
	    canvas.drawArc(rect, arcStart, angleSoFar, false, paint);
	    
	    Paint paint2 = new Paint();
	    paint2.setColor(Color.LTGRAY);
	    paint2.setStrokeWidth(100);
	    paint2.setAntiAlias(true);
	    paint2.setStrokeCap(Paint.Cap.BUTT);
	    paint2.setStyle(Paint.Style.STROKE);
	    canvas.drawArc(rect, arcSoFar, angleToGo, false, paint2);
	}
	
	private void drawPeak(Canvas canvas, RectF rect){
		//Log.d(DEBUG_TAG, "mPeakUsed:" + mPeakUsed + " mPeakRemaining:" + mPeakRemaining + " = mPeakQuota:" + mPeakQuota );
		
		float used = ( (float) mPeakUsed / mPeakQuota );
		float remaining = ( (float) mPeakRemaining / mPeakQuota );
		
		//Log.d(DEBUG_TAG, "used:" + used + " remaining: " + remaining);
		
		int angleUsed = (int) (used * 360);
		int angleTotal = 360;
		int angleRemaining = (angleTotal - angleUsed);
		
		//Log.d(DEBUG_TAG, "angleUsed:" + angleUsed + " angleRemaining:" + angleRemaining + " angleTotal:" + angleTotal);
		
		int arcStart = -90;
		int arcUsed = arcStart + angleUsed;
		int arcRemaining = arcStart + angleTotal;
		
		//Log.d(DEBUG_TAG, "arcStart:" + arcStart + " arcSoFar:" + arcUsed + " arcToGo:" + arcRemaining);
		
	    //Set the canvas values
		Paint paint = new Paint();
	    paint.setColor(Color.BLUE);
	    paint.setStrokeWidth(40);
	    paint.setAntiAlias(true);
	    paint.setStrokeCap(Paint.Cap.BUTT);
	    paint.setStyle(Paint.Style.STROKE);
	    canvas.drawArc(rect, arcStart, angleUsed, false, paint);
	    
	    Paint paint2 = new Paint();
	    paint2.setColor(Color.CYAN);
	    paint2.setStrokeWidth(40);
	    paint2.setAntiAlias(true);
	    paint2.setStrokeCap(Paint.Cap.BUTT);
	    paint2.setStyle(Paint.Style.STROKE);
	    canvas.drawArc(rect, arcUsed, angleRemaining, false, paint2);
			
	}
	
	private void drawOffpeak(Canvas canvas, RectF rect){
		//Log.d(DEBUG_TAG, "mOffpeakUsed:" + mOffpeakUsed + " mOffpeakRemaining:" + mOffpeakRemaining + " = mOffpeakQuota:" + mOffpeakQuota );
		
		float used = ( (float) mOffpeakUsed / mOffpeakQuota );
		float remaining = ( (float) mOffpeakRemaining / mOffpeakQuota );
		
		//Log.d(DEBUG_TAG, "used:" + used + " remaining: " + remaining);
		
		int angleUsed = (int) (used * 360);
		int angleTotal = 360;
		int angleRemaining = (angleTotal - angleUsed);
		
		//Log.d(DEBUG_TAG, "angleUsed:" + angleUsed + " angleRemaining:" + angleRemaining + " angleTotal:" + angleTotal);
		
		int arcStart = -90;
		int arcUsed = arcStart + angleUsed;
		int arcRemaining = arcStart + angleTotal;
		
		//Log.d(DEBUG_TAG, "arcStart:" + arcStart + " arcSoFar:" + arcUsed + " arcToGo:" + arcRemaining);
		
	    //Set the canvas values
		Paint paint = new Paint();
	    paint.setColor(Color.RED);
	    paint.setStrokeWidth(40);
	    paint.setAntiAlias(true);
	    paint.setStrokeCap(Paint.Cap.BUTT);
	    paint.setStyle(Paint.Style.STROKE);
	    canvas.drawArc(rect, arcStart, angleUsed, false, paint);
	    
	    Paint paint2 = new Paint();
	    paint2.setColor(Color.MAGENTA);
	    paint2.setStrokeWidth(40);
	    paint2.setAntiAlias(true);
	    paint2.setStrokeCap(Paint.Cap.BUTT);
	    paint2.setStyle(Paint.Style.STROKE);
	    canvas.drawArc(rect, arcUsed, angleRemaining, false, paint2);
			
	}
	
	
}
