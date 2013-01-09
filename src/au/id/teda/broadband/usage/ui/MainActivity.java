package au.id.teda.broadband.usage.ui;

import org.achartengine.GraphicalView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.authenticator.AuthenticatorActivity;
import au.id.teda.broadband.usage.chart.DoughnutChart;
import au.id.teda.broadband.usage.cursoradapter.DailyDataTableCursorAdapter;
import au.id.teda.broadband.usage.database.DailyDataDatabaseAdapter;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.helper.ConnectivityHelper;
import au.id.teda.broadband.usage.syncadapter.SyncAdapter;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class MainActivity extends SherlockFragmentActivity {
	
	public static final String DEBUG_TAG = "bbusage";
	
    /** Refresh icon reference object **/
    private static MenuItem mRefreshMenuItem;
    
    private static boolean refreshing;
    
    private static final String STATE_REFRESHING = "refresh";
	
	private AccountAuthenticator mAccountAuthenticator;
	
	private FragmentManager mFragmentManager;
	
	private GraphicalView mDoughnutChartView;
	
	private SharedPreferences mSettings;
	
	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	
	private SyncReceiver mSyncReceiver;
    private IntentFilter filter;
	
	private DoughnutChart mDoughnutChart;
	// Chart container
	private LinearLayout mChartLayoutContainer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        if (isScreenPortrait()){
        	setContentView(R.layout.activity_main);
        }
        
        // Set up the action bar.
        final ActionBar mActionBar = getSupportActionBar();
        
    	mAccountInfo = new AccountInfoHelper(this);
    	mAccountStatus = new AccountStatusHelper(this);

        String BROADCAST = getString(R.string.sync_broadcast_action);
        filter = new IntentFilter(BROADCAST);
        mSyncReceiver = new SyncReceiver();
        
        if( savedInstanceState != null ) {
        	refreshing = savedInstanceState.getBoolean(STATE_REFRESHING);
        	savedInstanceState.clear();
        }
        
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_REFRESHING, refreshing);
        super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        unregisterReceiver(mSyncReceiver);
     }
    
    protected void onResume(){
    	super.onResume();
    	
    	registerReceiver(mSyncReceiver, filter);
    	
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        
        // Check to see if account has been authenticated
        mAccountAuthenticator = new AccountAuthenticator(this);
        if(!mAccountAuthenticator.isAccountAuthenticated()){
        	Intent authenticator = new Intent(this, AuthenticatorActivity.class);
    		startActivity(authenticator);
    	// Else load views
        } else if (isScreenPortrait()) {
        	loadTextViews();
        	loadDoughnutChart();
        } else {
        	Log.d(DEBUG_TAG, "Landscape");
        	loadListView();
        }

    }
    
    private void noConnectivityToast(){
    	Toast toast = Toast.makeText(this, "No connectivity", Toast.LENGTH_LONG);
		toast.show();
    }
    
    // Create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.options_menu, menu);
        // Set object reference for refresh item
        mRefreshMenuItem = menu.findItem(R.id.menu_refresh);
        if (refreshing){
        	startAnimateRefreshIcon();
        }
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
        	
        		ConnectivityHelper mNetwork = new ConnectivityHelper(this);
        		if(mNetwork.isConnected()){
        			mNetwork.requestSync();
        		} else {
        			noConnectivityToast();
        		}
        	
        		return true;
        default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    public void loadListView(){
    	
    	// Load and open database
		DailyDataDatabaseAdapter adapter = new DailyDataDatabaseAdapter(this);
		adapter.open();
		
		// Get current data month
		AccountStatusHelper mStatus = new AccountStatusHelper(this);
		String month = mStatus.getCurrentMonthString();
		
		// Get cursor for current month
		Cursor cursor = adapter.getPriodUsageCursor(month);
		startManagingCursor(cursor); // TODO: Is this needed??
		
		ListView mListView = (ListView)findViewById(android.R.id.list);
		
		/**
		setListAdapter(new DailyDataTableCursorAdapter(this, cursor));
		
		
    	ListView mListView = (ListView)findViewById(android.R.id.list);
    	
    	
		

		
		
		adapter.close();
    	
    	
    	
    	mListView.setAdapter(adapter);
    	mListView.setOnItemClickListener(new OnItemClickListener()
    	{
    	     @Override
    	     public void onItemClick(AdapterView<?> a, View v,int position, long id) 
    	     {
    	          Toast.makeText(getBaseContext(), "Click", Toast.LENGTH_LONG).show();
    	      }
    	});
    	**/
    }

    public void loadTextViews(){
    	
    	if (mAccountInfo.isInfoSet() 
    			&& mAccountStatus.isStatusSet()
    			&& isScreenPortrait()){
    	
	    	TextView mUsernameTV = (TextView) findViewById(R.id.activity_main_username_tv);
	    	TextView mProductPlanTV = (TextView) findViewById(R.id.activity_main_product_plan_tv);
	    	TextView mCurrentMonthTV = (TextView) findViewById(R.id.activity_main_current_month_tv);
	    	TextView mRolloverNumberDaysTV = (TextView) findViewById(R.id.activity_main_days_number_tv);
	    	TextView mRolloverQuotaDaysTV = (TextView) findViewById(R.id.activity_main_days_until_tv);
	    	TextView mRolloverDateTV = (TextView) findViewById(R.id.activity_main_days_date_tv);
	    	TextView mPeakDataNumberTV = (TextView) findViewById(R.id.activity_main_peak_number_tv);
	    	TextView mPeakQuotaTV = (TextView) findViewById(R.id.activity_main_peak_quota_tv);
	    	TextView mPeakDataTV = (TextView) findViewById(R.id.activity_main_peak_used_tv);
	    	TextView mOffpeakDataNumberTV = (TextView) findViewById(R.id.activity_main_offpeak_number_tv);
	    	TextView mOffpeakQuotaTV = (TextView) findViewById(R.id.activity_main_offpeak_quota_tv);
	    	TextView mOffpeakDataTV = (TextView) findViewById(R.id.activity_main_offpeak_used_tv);
	    	TextView mUpTimeNumberTV = (TextView) findViewById(R.id.activity_main_uptime_number_tv);
	    	TextView mIpAddresTV = (TextView) findViewById(R.id.activity_main_uptime_ip_tv);
	    	TextView mLastSyncTV = (TextView) findViewById(R.id.activity_main_last_sync_tv);
	    	
	    	mUsernameTV.setText(mAccountAuthenticator.getUsername());
	    	mProductPlanTV.setText(mAccountInfo.getProductPlan());
	    	mCurrentMonthTV.setText(mAccountStatus.getCurrentMonthString());
	    	mRolloverNumberDaysTV.setText(mAccountStatus.getDaysSoFarString());
	    	mRolloverQuotaDaysTV.setText(mAccountStatus.getDaysThisPeriodString());
	    	mRolloverDateTV.setText(mAccountStatus.getStartDateString());
	    	mPeakDataNumberTV.setText(mAccountStatus.getPeakDataUsedGbString());
	    	mPeakQuotaTV.setText(mAccountInfo.getPeakQuotaString());
	    	mPeakDataTV.setText(mAccountStatus.getPeakShapedString());
	    	mOffpeakDataNumberTV.setText(mAccountStatus.getOffpeakDataUsedGbString());
	    	mOffpeakQuotaTV.setText(mAccountInfo.getOffpeakQuotaString());
	    	mOffpeakDataTV.setText(mAccountStatus.getOffpeakShapedString());
	    	mUpTimeNumberTV.setText(mAccountStatus.getUpTimeDaysString());
	    	mIpAddresTV.setText(mAccountStatus.getIpAddressStrng());
	    	mLastSyncTV.setText(mAccountStatus.getLastSyncTimeString());
    	}
    }
    
	/**
	 * Method for loading doughnut into view
	 */
	public void loadDoughnutChart() {
		
		if (mAccountInfo.isInfoSet() 
				&& mAccountStatus.isStatusSet()
				&& isScreenPortrait()){
			// Initialize layout for chart
			mChartLayoutContainer = (LinearLayout) findViewById(R.id.activity_main_chart_container);
	
			// Initialize chart class
			mDoughnutChart = new DoughnutChart(this);
	
			// Get chart view from library
			mDoughnutChartView = (GraphicalView) mDoughnutChart.getDoughnutChartView();
	
			// Add chart view to layout view
			mChartLayoutContainer.removeAllViews();
			mChartLayoutContainer.addView(mDoughnutChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			
			// Get screen specs
			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
				
			// Get layout parameters
			LayoutParams params = mChartLayoutContainer.getLayoutParams();
			// Set height equal to screen width
			params.height = width;
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
	
	public class SyncReceiver extends BroadcastReceiver {
		
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
            	loadTextViews();
            	loadDoughnutChart();
            }
        }
         
    }
	
	public int getScreenOrientation() {
	    Display getOrient = getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    return orientation;
	}
	
	public boolean isScreenPortrait(){
		int screen = getScreenOrientation();
		
		if (screen == Configuration.ORIENTATION_PORTRAIT){
			return true;
		} else {
			return false;
		}
	}


}