package au.id.teda.broadband.usage.ui;

import org.achartengine.GraphicalView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.authenticator.AuthenticatorActivity;
import au.id.teda.broadband.usage.chart.DoughnutChart;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.syncadapter.SyncAdapter;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class MainActivity extends SherlockFragmentActivity {
	
	public static final String DEBUG_TAG = "bbusage";
	
    /** Refresh icon reference object **/
    private static MenuItem mRefreshMenuItem;
    
    private static boolean refreshing;
    
    private static final String STATE_REFRESHING = "refresh";
    
    private static final String PREF_INITIALISED_KEY = "pref_initialise_key";
	
	private AccountAuthenticator mAccountAuthenticator;
	
	private FragmentManager mFragmentManager;
	
	private GraphicalView mDoughnutChartView;
	
	private SharedPreferences mSettings;
	
	private DoughnutChart mDoughnutChart;
	// Chart container
	private LinearLayout mChartLayoutContainer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Set up the action bar.
        final ActionBar mActionBar = getSupportActionBar();

        
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
    
    protected void onResume(){
    	super.onResume();
    	
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isAppInitialised = mSettings.getBoolean(PREF_INITIALISED_KEY, false);
    	
        // Check to see if account has been authenticated
        mAccountAuthenticator = new AccountAuthenticator(this);
        if(!mAccountAuthenticator.isAccountAuthenticated()){
        	Intent authenticator = new Intent(this, AuthenticatorActivity.class);
    		startActivity(authenticator);
    		
    	// Check to see if account has been initialised
        } else if (!isAppInitialised){
        	Intent initialise = new Intent(this, InitialiseActivity.class);
    		startActivity(initialise);
    		
    	// Else load views
        } else {
        	loadTextViews();
        	loadDoughnutChart();
        }

    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	switch (msg.what) {
        	case NetworkUtilities.HANDLER_START_ASYNC_TASK:
        		startAnimateRefreshIcon();
        		break;
        	case NetworkUtilities.HANDLER_COMPLETE_ASYNC_TASK:
        		stopAnimateRefreshIcon();
        		break;
        	}
        }
    };
    
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
        	
        		NetworkUtilities mNetworkUtilities = new NetworkUtilities(this);
        		if(mNetworkUtilities.is3gOrWifiConnected()){
        			SyncAdapter mSyncAdapter = new SyncAdapter(this, false);
        			mSyncAdapter.requestSync(handler);
        		} else {
        			noConnectivityToast();
        		}
        	
        		return true;
        default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadTextViews(){
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
    	
    	AccountInfoHelper info = new AccountInfoHelper(this);
    	AccountStatusHelper status = new AccountStatusHelper(this);
    	
    	mUsernameTV.setText(mAccountAuthenticator.getUsername());
    	mProductPlanTV.setText(info.getProductPlan());
    	mCurrentMonthTV.setText(status.getCurrentMonthString());
    	mRolloverNumberDaysTV.setText(status.getDaysSoFarString());
    	mRolloverQuotaDaysTV.setText(status.getDaysThisPeriodString());
    	mRolloverDateTV.setText(status.getStartDateString());
    	mPeakDataNumberTV.setText(status.getPeakDataUsedGbString());
    	mPeakQuotaTV.setText(info.getPeakQuotaString());
    	mPeakDataTV.setText(status.getPeakShapedString());
    	mOffpeakDataNumberTV.setText(status.getOffpeakDataUsedGbString());
    	mOffpeakQuotaTV.setText(info.getOffpeakQuotaString());
    	mOffpeakDataTV.setText(status.getOffpeakShapedString());
    	mUpTimeNumberTV.setText(status.getUpTimeDaysString());
    	mIpAddresTV.setText(status.getIpAddressStrng());
    }
    
	/**
	 * Method for loading doughnut into view
	 */
	public void loadDoughnutChart() {
		
		// Initialize layout for chart
		mChartLayoutContainer = (LinearLayout) findViewById(R.id.activity_main_chart_container);

		// Initialize chart class
		mDoughnutChart = new DoughnutChart(this);

		// Check if the chart doesn't already exist
		if (mDoughnutChartView == null) {

			// Get chart view from library
			mDoughnutChartView = (GraphicalView) mDoughnutChart.getDoughnutChartView();

			// Add chart view to layout view
			mChartLayoutContainer.addView(mDoughnutChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			
			// Get screen specs
			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			
		    // Get layout parameters
			LayoutParams params = mChartLayoutContainer.getLayoutParams();
			// Set height equal to screen width
			params.height = width;
		} else {
			// use this whenever data has changed and you want to redraw
			mDoughnutChartView.repaint();
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


}