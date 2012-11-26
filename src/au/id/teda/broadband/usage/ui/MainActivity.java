package au.id.teda.broadband.usage.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AuthenticatorActivity;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.ui.fragments.ConfirmationDialogFragment;
import au.id.teda.broadband.usage.ui.fragments.FragmentLogInDialog;
import au.id.teda.broadband.usage.ui.fragments.FragmentLogInDialog.FragmentLogInListner;
import au.id.teda.broadband.usage.ui.fragments.ProgressDialogCircleFragment;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class MainActivity extends SherlockFragmentActivity 
	implements FragmentLogInListner  {
	
	private static final String DEBUG_TAG = "bbusage";
	
	FragmentManager mFragmentManager;
	FragmentLogInDialog fragmentLogInDialog;
	
    // The BroadcastReceiver that tracks network connectivity changes.
    // private NetworkReceiver receiver = new ConnectivityHelperNetworkReceiver();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Remove title bar 
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        
        // Set up the action bar.
        final ActionBar mActionBar = getSupportActionBar();
        // Set action bar icon for navigation
        mActionBar.setDisplayHomeAsUpEnabled(true);
        
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
                Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
        case R.id.menu_refresh:
        		Log.d(DEBUG_TAG, "onOptionsItemSelected R.id.menu_refresh");
                return true;
        default:
                return super.onOptionsItemSelected(item);
        }
    }
    
	public void onLogInClick(View button) {
		NetworkUtilities mNetworkUtilities = new NetworkUtilities(this);
		mNetworkUtilities.getXmlData();
		
		//AccountInfoHelper mAccountInfoHelper = new AccountInfoHelper(this);
		//Toast.makeText(this, "Authentication: " + mAccountInfoHelper.getPlan(), Toast.LENGTH_SHORT).show();
		
		//DownloadVolumeUsage mDownloadDataUsage = new DownloadVolumeUsage(this);
		//boolean check = mDownloadDataUsage.authCheck();
		//Toast.makeText(this, "Authentication: " + check, Toast.LENGTH_SHORT).show();
		//mDownloadDataUsage.getAccountInfo();
		//mDownloadDataUsage.getAccountStatus();
		//mDownloadDataUsage.getVolumeUsage();
		
		//Intent authenticatorActivityIntent = new Intent(this, AuthenticatorActivity.class);
		//startActivity(authenticatorActivityIntent);
		
    }
    
	@Override
	public void onFinishLogInListner(String username, String password) {
		Toast.makeText(this, "Hi, " + username + " with Password: " + password, Toast.LENGTH_SHORT).show();
	}

}