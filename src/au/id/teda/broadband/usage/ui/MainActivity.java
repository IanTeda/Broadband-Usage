package au.id.teda.broadband.usage.ui;

import org.achartengine.GraphicalView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AuthenticatorActivity;
import au.id.teda.broadband.usage.chart.DoughnutChart;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class MainActivity extends SherlockFragmentActivity {
	
	private static final String DEBUG_TAG = "bbusage";
	
	public static final int HANDLER_RELOAD_VIEW = 0;
	public static final int HANDLER_START_REFRESH_ANIMATION = 1;
	public static final int HANDLER_STOP_REFRESH_ANIMATION = 2;
	
    
    /** Refresh icon reference object **/
    private static MenuItem mRefreshMenuItem;
    
    private static boolean refreshing;
    
    private static final String STATE_REFRESHING = "refresh";
	
	private AccountInfoHelper mAccount;
	
	private FragmentManager mFragmentManager;
	
	private GraphicalView mDoughnutChartView;
	
	private DoughnutChart mDoughnutChart;
	// Chart container
	private LinearLayout mDoughnutChartLayout;
	
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
        } else {
        	loadTextViews();
        	loadDoughnutChart();
        }
        
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

	/**
	 *  Handler for passing messages from other classes
	 */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	switch (msg.what) {
        	case HANDLER_RELOAD_VIEW:
        		loadTextViews();
        		loadDoughnutChart();
        		break;
        	case HANDLER_START_REFRESH_ANIMATION:
        		startAnimateRefreshIcon();
        		break;
        	case HANDLER_STOP_REFRESH_ANIMATION:
        		stopAnimateRefreshIcon();
        		break;	
        	
        	}
        	
        }
    };
    
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
        		mNetworkUtilities.getXmlData(handler);
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
    	
    	mUsernameTV.setText(info.getAccountUsername());
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
		Log.d(DEBUG_TAG, "loadDoughnutChart");
		
		// Initialize layout for chart
		mDoughnutChartLayout = (LinearLayout) findViewById(R.id.activity_main_chart_container);

		// Initialize chart class
		mDoughnutChart = new DoughnutChart(this);

		// Check if the chart doesn't already exist
		if (mDoughnutChartView == null) {
			
			Log.d(DEBUG_TAG, "loadDoughnutChart > mDoughnutChartView");

			// Get chart view from library
			mDoughnutChartView = (GraphicalView) mDoughnutChart.getDoughnutChartView();

			// Add chart view to layout view
			mDoughnutChartLayout.addView(mDoughnutChartView);

		} else {
			// use this whenever data has changed and you want to redraw
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