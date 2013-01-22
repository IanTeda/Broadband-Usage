package au.id.teda.broadband.usage.ui.fragments;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.chart.StackedBarChart;
import au.id.teda.broadband.usage.chart.StackedLineChart;
import au.id.teda.broadband.usage.database.DailyDataDatabaseAdapter;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.ui.MainActivity;
import au.id.teda.broadband.usage.util.DailyVolumeUsage;
import au.id.teda.broadband.usage.util.DailyVolumeUsageAdapter;

import com.actionbarsherlock.app.SherlockFragment;

public class DailyUsageFragment extends SherlockFragment {
	
	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Daily usage array
	private DailyVolumeUsage mDailyUsageArray[];
	
	// Gesture objects
	private GestureDetector mGestureDetector;
	private Animation mAnimSlideLeftIn;
	private Animation mAnimSlideLeftOut;
	private Animation mAnimSlideRightIn;
	private Animation mAnimSlideRightOut;
	private ViewFlipper mViewFlipper;
	
	// Key for view state saving
	private static final String VIEW_FLIPPER_NUMBER = "flipper_number";
	
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
	}
	
	/**
	 * Called 2nd in the fragment life cycle
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set context for fragment. 
		// Activity extends context so we get it from there
		mContext = getActivity();
		
		// Setup broadcast receiver for background sync
        String BROADCAST = getString(R.string.sync_broadcast_action);
        filter = new IntentFilter(BROADCAST);
        mSyncReceiver = new SyncReceiver();
        
		// If orientation has changed
		if (savedInstanceState != null) {		
			// Restore ViewFlipper position
			int flipperPosition = savedInstanceState.getInt(VIEW_FLIPPER_NUMBER);
	        mViewFlipper.setDisplayedChild(flipperPosition);
	    }
	}
	
	/**
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_daily_usage, container, false);
		
        // Get Volume Usage array
		DailyDataDatabaseAdapter mDatabase = new DailyDataDatabaseAdapter(mContext);
		String period = mAccountStatus.getDataBaseMonthString();
		mDailyUsageArray = mDatabase.getDailyVolumeUsage(period);
		
		return mFragmentView;
	}
	
	/**
	 * Called 4th in the fragment life cycle
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		loadFragmentView();
		loadGestures();
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
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		
		// Remember ViewFliper tab position during orientation change
	    int position = mViewFlipper.getDisplayedChild();
	    savedInstanceState.putInt(VIEW_FLIPPER_NUMBER, position);
	    
	    //TODO: Save flipper number for reload of app (remember what one they like)
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
	
	private void loadFragmentView(){
		if (mAccountInfo.isInfoSet() 
    			&& mAccountStatus.isStatusSet()){
			
			TextView mCurrentMonthTV = (TextView) mFragmentView.findViewById(R.id.fragment_daily_usage_month_tv); 	
			mCurrentMonthTV.setText(mAccountStatus.getCurrentMonthString());
			
			loadDataTable();
			loadStackedBarChart();
			loadStackedLineChart();
		}
	}
	
	private void loadGestures() {
		// Set reference for ViewFlipper layout
		mViewFlipper = (ViewFlipper) mFragmentView.findViewById(R.id.fragment_daily_usage_view_flipper);

		// Set reference to gesture detector
		mGestureDetector = new GestureDetector(new MyGestureDetector());

		// Set animation references
		mAnimSlideLeftIn = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_in);
		mAnimSlideLeftOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
		mAnimSlideRightIn = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
		mAnimSlideRightOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_out);

	}

	private void loadStackedBarChart() {
		// Set layout container for chart
		LinearLayout mChartContainer = (LinearLayout) mFragmentView.findViewById(R.id.fragment_daily_usage_bar_chart_container);

		// Initialise chart class
		StackedBarChart mBarChart = new StackedBarChart(mContext);
		
		// Get chart view from library
		GraphicalView mBarChartView = (GraphicalView) mBarChart.getBarChartView(mDailyUsageArray);

		// Add chart view to layout view
		mChartContainer.removeAllViews();
		mChartContainer.addView(mBarChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		// Setup the touch listener for chart
		mBarChartView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				return false;
			}
		});
	}
	
	private void loadDataTable(){

		// Initiate adapter to be used with list view
		DailyVolumeUsageAdapter adapter = new DailyVolumeUsageAdapter(mContext, R.layout.listview_data_table_row, mDailyUsageArray);
		
		// Reference list view to be used
		ListView mListView = (ListView) mFragmentView.findViewById(R.id.fragment_daily_usage_listview);
		
		// Floating header
		View headerView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_data_table_header, null, false);
		mListView.addHeaderView(headerView);
		
		// Set adapter to be used with the list view
		mListView.setAdapter(adapter);
		
		// Setup the touch listener for chart
		mListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				return false;
			}
		});
	}
	
	private void loadStackedLineChart() {
		// Set layout container for chart
		LinearLayout mChartContainer = (LinearLayout) mFragmentView.findViewById(R.id.fragment_daily_usage_line_chart_container);

		// Initialise chart class
		StackedLineChart mLineChart = new StackedLineChart(mContext);
		
		// Get chart view from library
		GraphicalView mLineChartView = (GraphicalView) mLineChart.getChartView(mDailyUsageArray);

		// Add chart view to layout view
		mChartContainer.removeAllViews();
		mChartContainer.addView(mLineChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		// Setup the touch listener for chart
		mLineChartView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				return false;
			}
		});
	}
	
	public void setChartTitle() {
		
		// Set title TextView object and initialise
		TextView chartTitle = (TextView) mFragmentView.findViewById(R.id.fragment_daily_usage_title_tv);
		
		// Check if ViewFlipper tab is at 1 (Bar Chart)
		if (mViewFlipper.getDisplayedChild() == 0){
			String title = getResources().getString(R.string.fragment_daily_usage_bar_chart);
			chartTitle.setText(title);
		}
		// Else check if ViewFilipper is at 2 (Pie Chart)
		else if (mViewFlipper.getDisplayedChild() == 1){
			String title = getResources().getString(R.string.fragment_daily_usage_line_chart);
			chartTitle.setText(title);
		}
		else {
			String title = getResources().getString(R.string.fragment_daily_usage_chart);
			chartTitle.setText(title);
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
	
	class MyGestureDetector extends SimpleOnGestureListener {
		
		// Gesture static int values to detect fling
		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;
		
		@Override
		public boolean onFling(MotionEvent motionEvent1,
				MotionEvent motionEvent2, float velocityX, float velocityY) {
			try {
				// Check to see if swipe is to short
				if (Math.abs(motionEvent1.getY() - motionEvent2.getY()) > SWIPE_MAX_OFF_PATH) {
					return false;
				}
				// Check if it is a right to left swipe
				if (motionEvent1.getX() - motionEvent2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(mAnimSlideLeftIn);
					mViewFlipper.setOutAnimation(mAnimSlideLeftOut);
					mViewFlipper.showNext();
					setChartTitle();
					return true;
				}
				// Else check if it is a left to right swipe
				else if (motionEvent2.getX() - motionEvent1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(mAnimSlideRightIn);
					mViewFlipper.setOutAnimation(mAnimSlideRightOut);
					mViewFlipper.showPrevious();
					setChartTitle();
					return true;
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}

		// It is necessary to return true from onDown for the onFling event to
		// register
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

	}


}
