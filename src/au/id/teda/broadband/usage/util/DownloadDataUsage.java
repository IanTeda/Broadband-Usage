package au.id.teda.broadband.usage.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.parser.ErrorParser;

public class DownloadDataUsage {
	
	private static final String DEBUG_TAG = "bbusage";

	// Activity context
    private static Context context;
    
    // Activity shared preferences
    SharedPreferences sharedPrefs;

    // Connection flags.
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    public static boolean wifiOnly = true;
    
    // Error texts from XML
    private static final String AUTHENTICATION_FAILURE = "Authentication failure";

    // Class constructor
    public DownloadDataUsage(Context context) {
    	DownloadDataUsage.context = context;

    	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    	
        // Are we only syncing when connected through wifi
        wifiOnly = sharedPrefs.getBoolean(context.getString(R.string.pref_key_wifi_only), true);
        
    	// Check connectivity and set flags
    	updateConnectionFlags();
    	
    }
    
    // Checks the network connection and sets the wifiConnected and mobileConnected flags
    private void updateConnectionFlags() {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkStatus = connMgr.getActiveNetworkInfo();
        
        if (networkStatus != null && networkStatus.isConnected()) {
            wifiConnected = networkStatus.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = networkStatus.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }
    
    private InputStream bufferXmlStream(){
    	InputStream mInputStream = context.getResources().openRawResource(R.raw.naked_dsl_home_5);
    	BufferedInputStream buf = new BufferedInputStream(mInputStream);
    	return buf;
    }
    
    public Boolean authCheck(){
    	String errorString = null;
    	ErrorParser mErrorParser = new ErrorParser();
    	
    	try {
			errorString = mErrorParser.parse(bufferXmlStream());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	
    	if (errorString != null 
    			&& errorString.equals(AUTHENTICATION_FAILURE)){
    		return false;
    	} else {
    		return true;
    	}
    }
    
    public void getData() {
    	
        if (( (!wifiOnly) && (wifiConnected || mobileConnected))
                || ( (wifiOnly) && (wifiConnected))) {

        } else {
        	// TODO Show wifi only status
            showConnectionError();
        }
    }
    
    // Displays an error if the app is unable to load content.
    private void showConnectionError() {

    	// TODO fragment toast??
    	// The specified network connection is not available. Displays error message.
    	Toast.makeText(context, R.string.toast_no_connectivity, Toast.LENGTH_SHORT).show();
    }
    
}