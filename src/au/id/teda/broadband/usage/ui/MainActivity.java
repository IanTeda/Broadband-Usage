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
        
       	// Don't show up button on home page action bar
       	getSupportActionBar().setHomeButtonEnabled(false);
       	// Set action bar title different to manifest
       	getSupportActionBar().setTitle(R.string.action_bar_title);
        
       	setContentView(R.layout.activity_main);
        
    }

}