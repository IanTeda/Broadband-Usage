package au.id.teda.broadband.usage.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
	
	private MenuItem refreshItem;
	
	FragmentManager mFragmentManager;
	FragmentLogInDialog fragmentLogInDialog;
	
    // The BroadcastReceiver that tracks network connectivity changes.
    // private NetworkReceiver receiver = new ConnectivityHelperNetworkReceiver();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportProgressBarIndeterminateVisibility(false);
        
        
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
        		refreshItem = item;
        		refresh();
                return true;
        default:
                return super.onOptionsItemSelected(item);
        }
    }
    
	public void onLogInClick(View button) {
		completeRefresh();
		
		//NetworkUtilities mNetworkUtilities = new NetworkUtilities(this);
		//mNetworkUtilities.getXmlData();
		
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
	
	 public void refresh() {
	     animateActionBarRefreshIcon();

	     //TODO trigger loading
	 }

	/**
	 * Start animation of refresh icon in action bar
	 */
	private void animateActionBarRefreshIcon() {
		// Attach a rotating ImageView to the refresh item as an ActionView
	     LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	     ImageView iv = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);

	     // Set animation
	     Animation rotation = AnimationUtils.loadAnimation(this, R.anim.clockwise_refresh);
	     rotation.setRepeatCount(Animation.INFINITE);
	     iv.startAnimation(rotation);

	     // Start animation of image view
	     refreshItem.setActionView(iv);
	     
	     this.setSupportProgressBarIndeterminateVisibility(true);
	}
	 
	 public void completeRefresh() {
		 
		 // Stop refresh icon animation
		 if (refreshItem != null && refreshItem.getActionView() != null){
			 refreshItem.getActionView().clearAnimation();
			 refreshItem.setActionView(null);
		 }
		 
		 this.setSupportProgressBarIndeterminateVisibility(false);
	}
    
	@Override
	public void onFinishLogInListner(String username, String password) {
		Toast.makeText(this, "Hi, " + username + " with Password: " + password, Toast.LENGTH_SHORT).show();
	}

}