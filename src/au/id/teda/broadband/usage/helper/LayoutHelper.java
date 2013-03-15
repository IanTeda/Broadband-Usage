package au.id.teda.broadband.usage.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.MainActivity;

public class LayoutHelper {
	
	// Debug tag pulled from main activity
	private static final String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// Activity context
    private static Context mContext;
    
    // Activity shared preferences
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;
    
    // Class constructor
    public LayoutHelper(Context context) {
    	this.mContext = context;
    	
    	mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
    	mEditor = mSettings.edit();
    }
	
    public boolean isLayoutPhonePort(TextView mLayoutUsed){
		CharSequence size = mLayoutUsed.getText();
		if ( size != null 
				&& size.equals(mContext.getString(R.string.size_phone_port))){
			return true;
		} else {
			return false;
		}
	}
    
    public boolean isLayoutPhoneLand(TextView mLayoutUsed){
		CharSequence size = mLayoutUsed.getText();
		if ( size != null 
				&& size.equals(mContext.getString(R.string.size_phone_land))){
			return true;
		} else {
			return false;
		}
	}
    
    public boolean isLayout_w1024dp(TextView mLayoutUsed){
		CharSequence size = mLayoutUsed.getText();
		if ( size != null 
				&& size.equals(mContext.getString(R.string.size_w1024dp))){
			return true;
		} else {
			return false;
		}
	}
    
    
	
	public int getScreenOrientation() {
	    Display getOrient = ((Activity) mContext).getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    return orientation;
	}
	
	public boolean isScreenPortrait(){
		int screen = getScreenOrientation();
		
		if (screen == Configuration.ORIENTATION_PORTRAIT){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isScreen_w1024dp(){
		float width = getScreenWidth();
		float w1024dp = 1024;
		if (width > w1024dp){
			return true;
		} else {
			return false;
		}
	}
	
	public float getScreenDensity(){
		return mContext.getResources().getDisplayMetrics().density;
	}
	
	public float getDpFromPx(float px){
		float density = getScreenDensity();
		return px / density;
	}
	
	public float getPxFromDp(float dp){
		float density = getScreenDensity();
		return dp * density;
	}
	
	public float getScreenWidth(){
		Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
	    DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);

	    float density  = mContext.getResources().getDisplayMetrics().density;
	    float dpHeight = outMetrics.heightPixels / density;
	    float dpWidth  = outMetrics.widthPixels / density;
	    
	    return dpWidth;
	}

}
