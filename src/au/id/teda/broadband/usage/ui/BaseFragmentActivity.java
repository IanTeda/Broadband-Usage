package au.id.teda.broadband.usage.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.helper.ConnectivityHelper;
import au.id.teda.broadband.usage.util.FontUtils;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class BaseFragmentActivity extends SherlockFragmentActivity {
	
	// Debug tag to be used for application
	public static final String DEBUG_TAG = "bbusage";

	// Refresh icon variables
    private static MenuItem mRefreshMenuItem;
    private static final String STATE_REFRESHING = "refresh";
    private static boolean refreshing;

    // Broadcast receiver objects
	private SyncReceiver mSyncReceiver;
    private IntentFilter filter;

    // Called 1st in the activity life cycle
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        // Set up the action bar.
        final ActionBar mActionBar = getSupportActionBar();
        
        // Set font to Roboto on SDK < 11
    	if (Build.VERSION.SDK_INT < 11) {
    	    ViewGroup godfatherView = (ViewGroup) this.getWindow().getDecorView();
    	    FontUtils.setRobotoFont(this, godfatherView);
    	}
    	
		// Setup broadcast receiver for background sync, with broadcast filter
        String BROADCAST = getString(R.string.sync_broadcast_action);
        filter = new IntentFilter(BROADCAST);
        mSyncReceiver = new SyncReceiver();
        
        // If returning from destruction start refresh icon
        if( savedInstanceState != null ) {
        	refreshing = savedInstanceState.getBoolean(STATE_REFRESHING);
        	savedInstanceState.clear();
        }
	}
	    
	// Called 2nd in the activity life cycle
    @Override
	protected void onStart() {
		super.onStart();
	}

	// Called 3rd in the activity life cycle
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	// Register sync reciever
    	registerReceiver(mSyncReceiver, filter);
    }

    // Called 1st during activity destruction
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_REFRESHING, refreshing);
        super.onSaveInstanceState(outState);
    }
    
    // Called 2nd during activity destruction
	@Override
    protected void onPause() {
        super.onPause();
        
        // Unregister broadcast receiver
        unregisterReceiver(mSyncReceiver);
	}
    
	// Called 3rd during activity destruction
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	// Called 4th during activity destruction
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// Create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.options_menu, menu);
        
        // Set object reference for refresh item
        mRefreshMenuItem = menu.findItem(R.id.menu_refresh);
        
        // Check to see if we should be animating
        if (refreshing){
        	startAnimateRefreshIcon();
        }
        return true;
    }
    
    // Handle options menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_account_info:
        		Intent accountInfoIntent = new Intent(getBaseContext(), AccountInfoActivity.class);
        		startActivity(accountInfoIntent);
        		return true;
        case R.id.menu_peak_usage:
        		Intent peakActivityIntent = new Intent(getBaseContext(), PeakUsageActivity.class);
        		startActivity(peakActivityIntent);
        		return true;
        case R.id.menu_settings:
                Intent settingsActivityIntent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(settingsActivityIntent);
                return true;
        case R.id.menu_refresh:
        		// Check for connectivity before requesting sync
        		ConnectivityHelper mNetwork = new ConnectivityHelper(this);
        		if(mNetwork.isConnected()){
        			// Request sync
        			mNetwork.requestSync();
        		} else {
        			// Toast no connectivity
        			noConnectivityToast();
        		}
        		return true;
        default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * Start the animation of the refresh icon in the action bar
     */
	public void startAnimateRefreshIcon() {
		if (mRefreshMenuItem != null){
			// Attach a rotating ImageView to the refresh item as an ActionView
			LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ImageView iv = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);
	
			// Set animation
			Animation rotation = AnimationUtils.loadAnimation(this, R.anim.clockwise_refresh);
			rotation.setRepeatCount(Animation.INFINITE);
			iv.startAnimation(rotation);
			iv.setDrawingCacheEnabled(true);
			iv.buildDrawingCache();
	
			// Start animation of image view
			mRefreshMenuItem.setActionView(iv);
			
			refreshing = true;
		}
	}
	
	/**
	 * Start stop animation of the refresh icon in the action bar
	 */
	public void stopAnimateRefreshIcon() {
		 // Stop refresh icon animation
		 if (mRefreshMenuItem != null && mRefreshMenuItem.getActionView() != null){
			 mRefreshMenuItem.getActionView().clearAnimation();
			 mRefreshMenuItem.setActionView(null);
			 
		 	refreshing = false;
		 }
	}
	
	/**
	 * Toast message on no connectivity
	 */
    private void noConnectivityToast(){
    	Toast toast = Toast.makeText(this, "No connectivity", Toast.LENGTH_LONG);
		toast.show();
    }
	
    /**
     * Broadcast reciever class to listen for background data syncs
     * @author iteda
     *
     */
	private class SyncReceiver extends BroadcastReceiver {
		
        @Override
        public void onReceive(Context context, Intent i) {
            
            String MESSAGE = getString(R.string.sync_broadcast_message);
            String SYNC_START = getString(R.string.sync_broadcast_start);
            String SYNC_COMPLETE = getString(R.string.sync_broadcast_complete);
            
            String msg = i.getStringExtra(MESSAGE);
            if (msg.equals(SYNC_START)){
        		startAnimateRefreshIcon();
            } else if (msg.equals(SYNC_COMPLETE)){
        		stopAnimateRefreshIcon();
            }
        }
         
    }

}
