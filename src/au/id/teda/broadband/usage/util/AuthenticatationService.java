package au.id.teda.broadband.usage.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return new AccountAuthenticator(this).getIBinder();
	}

}
