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
import au.id.teda.broadband.usage.ui.fragments.FragmentLogInDialog;
import au.id.teda.broadband.usage.ui.fragments.FragmentLogInDialog.FragmentLogInListner;
import au.id.teda.broadband.usage.util.DownloadDataUsage;

public class MainActivity extends SherlockFragmentActivity implements FragmentLogInListner {
	
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
        
        // Load instance of fragments
        mFragmentManager = getSupportFragmentManager();
        fragmentLogInDialog = new FragmentLogInDialog();
        
        // Register BroadcastReceiver to track connection changes.
        /**
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
		**/
	
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
		// Show log in fragment
        // fragmentLogInDialog.show(mFragmentManager, "fragment_log_in");
		
		DownloadDataUsage mDownloadDataUsage = new DownloadDataUsage(this);
		boolean check = mDownloadDataUsage.authCheck();
		Toast.makeText(this, "Authentication: " + check, Toast.LENGTH_SHORT).show();
    }
    
	@Override
	public void onFinishLogInListner(String username, String password) {
		Toast.makeText(this, "Hi, " + username + " with Password: " + password, Toast.LENGTH_SHORT).show();
	}

}