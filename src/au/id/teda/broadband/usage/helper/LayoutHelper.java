package au.id.teda.broadband.usage.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;

public class LayoutHelper {
	
	// Debug tag pulled from main activity
	//private static final String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// Activity context
    private static Context mContext;
    
    // Activity shared preferences
    SharedPreferences mSettings;
    SharedPreferences.Editor mEditor;
    
    // Class constructor
    public LayoutHelper(Context context) {
    	mContext = context;
    	
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
    
    public boolean isLayout_w800dp(TextView mLayoutUsed){
		CharSequence size = mLayoutUsed.getText();
		if ( size != null 
				&& size.equals(mContext.getString(R.string.size_w800dp))){
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
	    //float dpHeight = outMetrics.heightPixels / density;
	    float dpWidth  = outMetrics.widthPixels / density;
	    
	    return dpWidth;
	}
	
	/**
	 * Checks if the device is a tablet or a phone
	 * 
	 * @param activityContext
	 *            The Activity Context.
	 * @return Returns true if the device is a Tablet
	 */
	public boolean isTabletDevice() {
	    // Verifies if the Generalized Size of the device is XLARGE to be
	    // considered a Tablet
	    boolean xlarge = ((mContext.getResources().getConfiguration().screenLayout & 
	                        Configuration.SCREENLAYOUT_SIZE_MASK) == 
	                        Configuration.SCREENLAYOUT_SIZE_XLARGE);

	    // If XLarge, checks if the Generalized Density is at least MDPI
	    // (160dpi)
	    if (xlarge) {
	        DisplayMetrics metrics = new DisplayMetrics();
	        Activity activity = (Activity) mContext;
	        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

	        // MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
	        // DENSITY_TV=213, DENSITY_XHIGH=320
	        if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
	                || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
	                || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
	                || metrics.densityDpi == DisplayMetrics.DENSITY_TV
	                || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {

	            // Yes, this is a tablet!
	            return true;
	        }
	    }

	    // No, this is not a tablet!
	    return false;
	}

}
