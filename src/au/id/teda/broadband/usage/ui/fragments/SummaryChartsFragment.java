package au.id.teda.broadband.usage.ui.fragments;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.chart.CustomDonughtChart;
import au.id.teda.broadband.usage.chart.DoughnutChart;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.ui.MainActivity;
import au.id.teda.broadband.usage.ui.fragments.UsageSummaryFragment.SyncReceiver;

import com.actionbarsherlock.app.SherlockFragment;

public class SummaryChartsFragment extends SherlockFragment {
	
	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Helper classes
	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	
	// Recieve sync broadcasts
	private SyncReceiver mSyncReceiver;
    private IntentFilter filter;
	
    // Activity context to be used
	private Context mContext;
	
	/**
	 * Called 1st in the fragment life cycle
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Load helper classes
		mAccountInfo = new AccountInfoHelper(activity);
		mAccountStatus = new AccountStatusHelper(activity);
		
		// Set context to be used in fragment
		mContext = activity;
	}
	
	/**
	 * Called 2nd in the fragment life cycle
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Setup broadcast receiver for background sync
        String BROADCAST = getString(R.string.sync_broadcast_action);
        filter = new IntentFilter(BROADCAST);
        mSyncReceiver = new SyncReceiver();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_summary_charts, container, false);

		loadFragmentView();
		
		return mFragmentView;
	}

	/**
	 * Called 4th in the fragment life cycle
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		loadFragmentView();
	}
	
	/**
	 * Called 5th in the fragment life cycle
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		// Register broadcast receiver for background sync
		getActivity().registerReceiver(mSyncReceiver, filter);
	}
	
	/**
	 * First call in the death of fragment
	 */
	@Override
	public void onPause() {
		super.onPause();
		
		// Unregister broadcast receiver for background sync
		getActivity().unregisterReceiver(mSyncReceiver);
	}
	
	private void loadFragmentView() {
		if (mAccountInfo.isInfoSet() 
				&& mAccountStatus.isStatusSet()){
			// Set layout container for chart
			LinearLayout mChartContainer = (LinearLayout) mFragmentView.findViewById(R.id.fragment_summary_chart_donught);
	
			// Initialize chart class
			DoughnutChart mDoughnutChart = new DoughnutChart(mContext);
	
			// Get chart view from library
			GraphicalView mDoughnutChartView = (GraphicalView) mDoughnutChart.getDoughnutChartView();
	
			// Add chart view to layout view
			mChartContainer.removeAllViews();
			mChartContainer.addView(mDoughnutChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			
			// Get screen specs
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
				
			// Get layout parameters
			LayoutParams params = mChartContainer.getLayoutParams();
			// Set height equal to screen width
			params.height = width;
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
            	// Nothing to do see here move along
            } else if (msg.equals(SYNC_COMPLETE)){
            	loadFragmentView();
            }
        }
         
    }

}
