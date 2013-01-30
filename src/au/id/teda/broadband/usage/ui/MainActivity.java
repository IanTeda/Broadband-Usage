package au.id.teda.broadband.usage.ui;

import android.content.Intent;
import android.os.Bundle;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.authenticator.AuthenticatorActivity;

public class MainActivity extends BaseActivity {
	
	private AccountAuthenticator mAccountAuthenticator;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check to see if account has been authenticated
        mAccountAuthenticator = new AccountAuthenticator(this);
        if(!mAccountAuthenticator.isAccountAuthenticated()){
        	Intent authenticator = new Intent(this, AuthenticatorActivity.class);
    		startActivity(authenticator);
        }
        
       	setContentView(R.layout.activity_main);
        
    }

}