package au.id.teda.broadband.usage.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.helper.ConnectivityHelper;
import au.id.teda.broadband.usage.helper.LayoutHelper;
import au.id.teda.broadband.usage.ui.fragments.AboutDialogFragment;
import au.id.teda.broadband.usage.util.FontUtils;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class BaseActivity extends SherlockFragmentActivity {
	
	// Debug tag to be used for application
	//public static final String DEBUG_TAG = "bbusage";

	// Refresh icon variables
    private static MenuItem mRefreshMenuItem;
    private static final String STATE_REFRESHING = "refresh";
    private static boolean refreshing;

    // Broadcast receiver objects
	private SyncReceiver mSyncReceiver;
    private IntentFilter filter;
    
    // Layout helper to determine if tablet
    private LayoutHelper mLayoutHelper;

    // Called 1st in the activity life cycle
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
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
        
        mLayoutHelper = new LayoutHelper(this);
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
    	
    	if (mLayoutHelper.isTabletDevice()){
    		getSupportMenuInflater().inflate(R.menu.options_menu_tablet, menu);
    	} else {
    		getSupportMenuInflater().inflate(R.menu.options_menu_phone, menu);
    	}
        
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
        		Intent mAccountInfoIntent = new Intent(getBaseContext(), AccountInfoActivity.class);
        		startActivity(mAccountInfoIntent);
        		return true;
        	case R.id.menu_data_usage:
        		Intent mUsageActivityIntent = new Intent(getBaseContext(), UsageActivity.class);
        		startActivity(mUsageActivityIntent);
        		return true;
        	case R.id.menu_settings:
                Intent mSettingsActivityIntent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(mSettingsActivityIntent);
                return true;
        	case R.id.menu_about:
        		FragmentManager fm = getSupportFragmentManager();
                AboutDialogFragment mDialog = new AboutDialogFragment();
                mDialog.show(fm, "dlg_edit_name");
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
        	case android.R.id.home:
	            // This is called when the Home (Up) button is pressed in the Action Bar.
	            Intent mMainActivityInetnt = new Intent(this, MainActivity.class);
	            mMainActivityInetnt.addFlags(
	                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
	                    Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(mMainActivityInetnt);
	            finish();
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
