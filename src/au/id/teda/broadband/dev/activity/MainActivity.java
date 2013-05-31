package au.id.teda.broadband.dev.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import au.id.teda.broadband.dev.R;
import au.id.teda.broadband.dev.authenticator.AccountAuthenticator;
import au.id.teda.broadband.dev.authenticator.AuthenticatorActivity;

public class MainActivity extends BaseActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check to see if account has been authenticated
        AccountAuthenticator mAccountAuthenticator = new AccountAuthenticator(this);
        if(!mAccountAuthenticator.isAccountAuthenticated()){
        	Intent authenticator = new Intent(this, AuthenticatorActivity.class);
    		startActivity(authenticator);
        }
        
       	// Don't show up button on home page action bar
       	getSupportActionBar().setHomeButtonEnabled(false);
       	// Set action bar title different to manifest label
       	getSupportActionBar().setTitle(R.string.action_bar_title);

        // Check if account is an anytime account and load layout
        if (mAccountInfo.isAccountAnyTime()){
            Log.d(DEBUG_TAG, "activity_main_anytime");
            setContentView(R.layout.activity_main_anytime);
        } else {
            Log.d(DEBUG_TAG, "activity_main");
            setContentView(R.layout.activity_main);
        }
        
    }

}