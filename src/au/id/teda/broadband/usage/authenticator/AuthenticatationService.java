package au.id.teda.broadband.usage.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AuthenticatationService extends Service {
	
	private static final String DEBUG_TAG = "bbusage";
	
	private Authenticator mAccountAuthenticator;
	
    @Override
    public void onCreate() {
        if (Log.isLoggable(DEBUG_TAG, Log.VERBOSE)) {
            Log.v(DEBUG_TAG, "Authentication Service started.");
        }
        mAccountAuthenticator = new Authenticator(this);
    }

    @Override
    public void onDestroy() {
        if (Log.isLoggable(DEBUG_TAG, Log.VERBOSE)) {
            Log.v(DEBUG_TAG, "Authentication Service stopped.");
        }
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        if (Log.isLoggable(DEBUG_TAG, Log.VERBOSE)) {
            Log.v(DEBUG_TAG, "getBinder()...  returning the AccountAuthenticator binder for intent "
                    + intent);
        }
        return mAccountAuthenticator.getIBinder();
    }
}
