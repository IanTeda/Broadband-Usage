package au.id.teda.broadband.usage.authenticator;

import android.accounts.AccountAuthenticatorActivity;
import android.os.Bundle;
import android.util.Log;
import au.id.teda.broadband.usage.R;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
	
	private static final String DEBUG_TAG = "bbusage";
	
	@Override  
	protected void onCreate(Bundle icicle) {  
		super.onCreate(icicle);  
		this.setContentView(R.layout.login_activity);
        Log.d(DEBUG_TAG, "AuthenticatorActivity.onCreate(" + icicle + ")");
	}
	
		 
}
