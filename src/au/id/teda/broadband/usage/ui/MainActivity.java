package au.id.teda.broadband.usage.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AuthenticatorActivity;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class MainActivity extends SherlockFragmentActivity {
	
	//private static final String DEBUG_TAG = "bbusage";
	
	private AccountInfoHelper mAccount;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Set up the action bar.
        final ActionBar mActionBar = getSupportActionBar();
        // Set action bar icon for navigation
        mActionBar.setDisplayHomeAsUpEnabled(true);

        // Check to see if account has been authenticated
        mAccount = new AccountInfoHelper(this);
        if(!mAccount.isAccountAuthenticated()){
        	Intent authenticatorActivityIntent = new Intent(this, AuthenticatorActivity.class);
    		startActivity(authenticatorActivityIntent);
        }
        
    }

    // Create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    
    // Handle options menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_settings:
                Intent settingsActivityIntent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(settingsActivityIntent);
                return true;
        case R.id.menu_refresh:
        		NetworkUtilities mNetworkUtilities = new NetworkUtilities(this);
        		mNetworkUtilities.getXmlData(item);
                return true;
        default:
                return super.onOptionsItemSelected(item);
        }
    }
    
	public void onLogInClick(View button) {
				
		//NetworkUtilities mNetworkUtilities = new NetworkUtilities(this);
		//mNetworkUtilities.getXmlData();
		
		AccountInfoHelper mAccountInfoHelper = new AccountInfoHelper(this);
		Toast.makeText(this, "Authentication: " + mAccountInfoHelper.getPlan(), Toast.LENGTH_SHORT).show();
		
		//DownloadVolumeUsage mDownloadDataUsage = new DownloadVolumeUsage(this);
		//boolean check = mDownloadDataUsage.authCheck();
		//Toast.makeText(this, "Authentication: " + check, Toast.LENGTH_SHORT).show();
		//mDownloadDataUsage.getAccountInfo();
		//mDownloadDataUsage.getAccountStatus();
		//mDownloadDataUsage.getVolumeUsage();
		
		//Intent authenticatorActivityIntent = new Intent(this, AuthenticatorActivity.class);
		//startActivity(authenticatorActivityIntent);
		
    }

}