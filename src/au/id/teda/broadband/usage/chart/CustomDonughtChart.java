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

	public CustomDonughtChart(Context context) {
		super(context);
		Log.d(DEBUG_TAG, "CustomDonughtChart");
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		Log.d(DEBUG_TAG, "CustomDonughtChart.onDraw()");
		
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
	    
	    //Set the canvas values
		Paint paint = new Paint();
	    paint.setColor(Color.LTGRAY);
	    paint.setStrokeWidth(80);
	    paint.setAntiAlias(true);
	    paint.setStrokeCap(Paint.Cap.BUTT);
	    paint.setStyle(Paint.Style.STROKE);
	    canvas.drawArc(rect, -90, 310, false, paint);
	    	    
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

	}
}
