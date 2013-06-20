package au.id.teda.broadband.usage.fragments;

import au.id.teda.broadband.usage.chart.StackedBarChart;
import au.id.teda.broadband.usage.chart.StackedLineChart;
import au.id.teda.broadband.usage.database.DailyDataTableAdapter;
import au.id.teda.broadband.usage.util.DailyVolumeUsage;
import au.id.teda.broadband.usage.util.DailyVolumeUsageAdapter;
import au.id.teda.broadband.usage.util.PaginationView;
import org.achartengine.GraphicalView;

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

public class DailyUsageFragment extends BaseFragment {
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Daily dev array
	private DailyVolumeUsage mDailyUsageArray[];
	
	// Gesture objects
	private GestureDetector mGestureDetector;
	private Animation mAnimSlideLeftIn;
	private Animation mAnimSlideLeftOut;
	private Animation mAnimSlideRightIn;
	private Animation mAnimSlideRightOut;
	private ViewFlipper mViewFlipper;
	
	// Layout used
	private TextView mLayoutUsed;
	
	private PaginationView mPaginationView;
    
    // Preference key for viewfliper tab location
    private final static String PREF_FLIPPER_KEY = "pref_flipper_tab_key";
	
	/**
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set reference to fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_daily_usage, container, false);
		mLayoutUsed = (TextView) mFragmentView.findViewById(R.id.fragment_daily_usage_size);
		
		if (!mLayoutHelper.isLayout_w800dp(mLayoutUsed)){
			// Set reference to view flipper
			mViewFlipper = (ViewFlipper) mFragmentView.findViewById(R.id.fragment_daily_usage_view_flipper);
			
			// Load pagination view
			mPaginationView = new PaginationView(mFragmentView, mContext);
			
			// Set pagination
			setPageNation();
		}
		
		return mFragmentView;
	}
	
	/**
	 * Called 4th in the fragment life cycle
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		loadGestures();
	}
	
	/**
	 * Called 5th in the fragment life cycle
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		// Check to see if layout used is landscape
		if (mLayoutHelper.isLayoutPhoneLand(mLayoutUsed)){
			// Build action bar title string
			String title = mContext.getResources().getString(R.string.action_bar_title) + " - " + mAccountStatus.getCurrentMonthString();
			// Set action bar title
			getSherlockActivity().getSupportActionBar().setTitle(title);
		}
	}
	
	/**
	 * Called 1st in the death of fragment
	 */
	@Override
	public void onPause() {
		super.onPause();
		
		if (!mLayoutHelper.isLayout_w800dp(mLayoutUsed)){
			// Remember ViewFliper tab position on fragment pause
		    int flipperPosition = mViewFlipper.getDisplayedChild();
		    mEditor.putInt(PREF_FLIPPER_KEY, flipperPosition);
		    mEditor.commit();
		}
	}
	
	/**
	 * Load fragment view if account status and info is set
	 */
	@Override
	protected void loadFragmentView(){
		if (mAccountInfo.isInfoSet() 
    			&& mAccountStatus.isStatusSet()){
			
	        // Get volume dev array
			DailyDataTableAdapter mDatabase = new DailyDataTableAdapter(mContext);
			String period = mAccountStatus.getDataBaseMonthString();
			mDailyUsageArray = mDatabase.getDailyVolumeUsage(period);
			
			TextView mCurrentMonthTV = (TextView) mFragmentView.findViewById(R.id.fragment_daily_usage_month_tv); 	
			mCurrentMonthTV.setText(mAccountStatus.getCurrentMonthString());
			
			if (!mLayoutHelper.isLayout_w800dp(mLayoutUsed)){
				setFlipperView();
				loadStackedBarChart();
				loadStackedLineChart();
			}
			
			loadDataTable();
			
		}
	}

	/**
	 * Set flipper view based on last stored value
	 */
	private void setFlipperView() {
		// Set view flipper to last view
		int flipperPosition = mSettings.getInt(PREF_FLIPPER_KEY, 0);
		mViewFlipper.setDisplayedChild(flipperPosition);
	}
	
	/**
	 * Load gestures and flipper animation
	 */
	private void loadGestures() {
		// Set reference to gesture detector
		mGestureDetector = new GestureDetector(new MyGestureDetector());

		// Set animation references
		mAnimSlideLeftIn = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_in);
		mAnimSlideLeftOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
		mAnimSlideRightIn = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
		mAnimSlideRightOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_out);

	}

	/**
	 * Load stacked bar chart into view
	 */
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
	
	/**
	 * Load data table into fragment view
	 */
	private void loadDataTable(){

		// Initiate adapter to be used with list view
		DailyVolumeUsageAdapter adapter = new DailyVolumeUsageAdapter(mContext, R.layout.listview_data_table_row, mDailyUsageArray);
		
		// Reference list view to be used
		ListView mListView = (ListView) mFragmentView.findViewById(R.id.fragment_daily_usage_listview);
		
		// Add list view header
		@SuppressWarnings("static-access")
		View headerView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_data_table_header, null, false);
		if (mListView.getHeaderViewsCount() == 0){
			mListView.addHeaderView(headerView);
		}
		
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
	
	/**
	 * Load stacked line chart into fragment view
	 */
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
	
	/**
	 * Set view title based on flipper tab showing
	 */
	public void setFlipperTitle() {
		
		// Set title TextView object and initialise
		TextView chartTitle = (TextView) mFragmentView.findViewById(R.id.fragment_daily_usage_title_tv);
		
		// Set title based on view flipper tab showing
		String title = getResources().getString(R.string.fragment_daily_usage_default_title);
		if (mViewFlipper.getDisplayedChild() == 0){
			title = getResources().getString(R.string.fragment_daily_usage_usage_table);
		} else if (mViewFlipper.getDisplayedChild() == 1){
			title = getResources().getString(R.string.fragment_daily_usage_bar_chart);
		} else if (mViewFlipper.getDisplayedChild() == 2){
			title = getResources().getString(R.string.fragment_daily_usage_line_chart);
			
		}
		chartTitle.setText(title);
		
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
					if (!mLayoutHelper.isLayout_w800dp(mLayoutUsed)){
						mViewFlipper.setInAnimation(mAnimSlideLeftIn);
						mViewFlipper.setOutAnimation(mAnimSlideLeftOut);
						mViewFlipper.showNext();
						setFlipperTitle();
						setPageNation();
					}
					return true;
				}
				// Else check if it is a left to right swipe
				else if (motionEvent2.getX() - motionEvent1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (!mLayoutHelper.isLayout_w800dp(mLayoutUsed)){
						mViewFlipper.setInAnimation(mAnimSlideRightIn);
						mViewFlipper.setOutAnimation(mAnimSlideRightOut);
						mViewFlipper.showPrevious();
						setFlipperTitle();
						setPageNation();
					}
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

	public void setPageNation(){
		mPaginationView.setActive(getViewFlipperTab());
	}
	
	public int getViewFlipperTab(){
		return mViewFlipper.getDisplayedChild();
	}

}
