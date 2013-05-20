package au.id.teda.broadband.dev.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatationService extends Service {
	
	//private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	private AccountAuthenticator mAccountAuthenticator;

    @Override
    public void onCreate() {
        mAccountAuthenticator = new AccountAuthenticator(this);
    }

    @Override
    public void onDestroy() {
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return mAccountAuthenticator.getIBinder();
    }
}
