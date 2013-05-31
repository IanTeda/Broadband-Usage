package au.id.teda.broadband.dev.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import au.id.teda.broadband.dev.activity.BaseActivity;
import au.id.teda.broadband.dev.authenticator.AccountAuthenticator;
import au.id.teda.broadband.dev.syncadapter.DummyContentProvider;

public class ConnectivityHelper {

    private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;

    // Connection flags.
    private static boolean isWifiConnected = false;
    private static boolean isMobileConnected = false;
	
	private static Context mContext;
	
    /**
     * Class constructor
     * @param context
     */
    public ConnectivityHelper(Context context) {
    	mContext = context;
    }
    
    
    /**
     * Check network connectivity and set wifiConnected and mobileConnected flags
     */
    private void updateConnectionFlags() {

        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkStatus = connMgr.getActiveNetworkInfo();
        
        if (networkStatus != null && networkStatus.isConnected()) {
            isWifiConnected = networkStatus.getType() == ConnectivityManager.TYPE_WIFI;
            isMobileConnected = networkStatus.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            isWifiConnected = false;
            isMobileConnected = false;
        }
    }
    
    /**
     * Do we have a any type connection to the internet?
     * 
     * @return true if connection present (including WiFi settings)
     */
    public boolean isConnected() {
    	updateConnectionFlags();
    	
        if (isWifiConnected || isMobileConnected) {
        	return true;
        } else {
        	return false;
        }
    }
    
    public boolean is3gConnected() {
    	updateConnectionFlags();
    	
        if (isMobileConnected) {
        	return true;
        } else {
        	return false;
        }
    }
    
	public void requestSync(){
        AccountAuthenticator mAccountAuthenticator = new AccountAuthenticator(mContext);
		ContentResolver.requestSync(mAccountAuthenticator.getAccount(), DummyContentProvider.PROVIDER, new Bundle());
	}

}
