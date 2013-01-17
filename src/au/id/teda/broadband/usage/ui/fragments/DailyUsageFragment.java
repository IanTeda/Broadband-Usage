package au.id.teda.broadband.usage.ui.fragments;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
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
import android.widget.TextView;
import android.widget.ViewFlipper;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.chart.StackedBarChart;
import au.id.teda.broadband.usage.chart.StackedLineChart;
import au.id.teda.broadband.usage.database.DailyDataDatabaseAdapter;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.ui.MainActivity;
import au.id.teda.broadband.usage.ui.fragments.FragmentFooter.SyncReceiver;

import com.actionbarsherlock.app.SherlockFragment;

public class DailyUsageFragment extends SherlockFragment {
	
	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Gesture objects
	private GestureDetector myGestureDetector;
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	private ViewFlipper myViewFlipper;
	
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
	}
	
	/**
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_daily_usage, container, false);
		
		mFragmentView.setOnTouchListener(new OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent event) {
		    	
		    	Log.d(DEBUG_TAG, "onTouch");
		    	
				if (myGestureDetector.onTouchEvent(event)){
					return true;
				} else {
					return false;
				}
		    }
		});
		
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
			
			loadStackedBarChart();
			loadStackedLineChart();
		}
	}
	
	private void loadGestures() {
		// Set reference for ViewFlipper layout
		myViewFlipper = (ViewFlipper) mFragmentView.findViewById(R.id.fragment_daily_usage_view_flipper);

		// Set reference to gesture detector
		myGestureDetector = new GestureDetector(new MyGestureDetector());

		// Set animation references
		slideLeftIn = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_out);

		// Set the touch listener for the main view to be our custom gesture
		// listener
		myViewFlipper.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				myGestureDetector.onTouchEvent(event);
				return false;
			}
		});
	}

	private void loadStackedBarChart() {
		// Set layout container for chart
		LinearLayout mChartContainer = (LinearLayout) mFragmentView.findViewById(R.id.fragment_daily_usage_bar_chart_container);

		// Initialise chart class
		StackedBarChart mBarChart = new StackedBarChart(mContext);
		
		// Open Database
		DailyDataDatabaseAdapter mDatabase = new DailyDataDatabaseAdapter(mContext);
		mDatabase.open();

		// Get current data period
		String period = mAccountStatus.getDataBaseMonthString();

		// Retrieve cursor for given data period
		Cursor mCursor = mDatabase.getPriodUsageCursor(period);
		
		// Get chart view from library
		GraphicalView mBarChartView = (GraphicalView) mBarChart.getBarChartView(mCursor);

		// Add chart view to layout view
		mChartContainer.removeAllViews();
		mChartContainer.addView(mBarChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		// Get screen specs
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
			
		// Get layout parameters
		LayoutParams params = mChartContainer.getLayoutParams();
		// Set height equal to screen width
		params.height = width;
		
		mCursor.close();
		mDatabase.close();
		
		// Setup the touch listener for chart
		mBarChartView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				myGestureDetector.onTouchEvent(event);
				return false;
			}
		});
	}
	
	private void loadStackedLineChart() {
		// Set layout container for chart
		LinearLayout mChartContainer = (LinearLayout) mFragmentView.findViewById(R.id.fragment_daily_usage_line_chart_container);

		// Initialise chart class
		StackedLineChart mLineChart = new StackedLineChart(mContext);
		
		// Open Database
		DailyDataDatabaseAdapter mDatabase = new DailyDataDatabaseAdapter(mContext);
		mDatabase.open();

		// Get current data period
		String period = mAccountStatus.getDataBaseMonthString();

		// Retrieve cursor for given data period
		Cursor mCursor = mDatabase.getPriodUsageCursor(period);
		
		// Get chart view from library
		GraphicalView mLineChartView = (GraphicalView) mLineChart.getChartView(mCursor);

		// Add chart view to layout view
		mChartContainer.removeAllViews();
		mChartContainer.addView(mLineChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		// Get screen specs
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
			
		// Get layout parameters
		LayoutParams params = mChartContainer.getLayoutParams();
		// Set height equal to screen width
		params.height = width;
		
		mCursor.close();
		mDatabase.close();
		
		// Setup the touch listener for chart
		mLineChartView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				myGestureDetector.onTouchEvent(event);
				return false;
			}
		});
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
					myViewFlipper.setInAnimation(slideLeftIn);
					myViewFlipper.setOutAnimation(slideLeftOut);
					myViewFlipper.showNext();
					//setPageNation();
					//setChartTitle();
					return true;
				}
				// Else check if it is a left to right swipe
				else if (motionEvent2.getX() - motionEvent1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					myViewFlipper.setInAnimation(slideRightIn);
					myViewFlipper.setOutAnimation(slideRightOut);
					myViewFlipper.showPrevious();
					//setPageNation();
					//setChartTitle();
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
