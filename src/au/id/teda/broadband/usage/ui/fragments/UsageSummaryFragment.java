package au.id.teda.broadband.usage.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.helper.NotificationHelper;
import au.id.teda.broadband.usage.ui.MainActivity;
import au.id.teda.broadband.usage.ui.fragments.DailyUsageFragment.MyGestureDetector;

import com.actionbarsherlock.app.SherlockFragment;

public class UsageSummaryFragment extends SherlockFragment {
	
	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Gesture objects
	private GestureDetector mGestureDetector;
	private Animation mAnimSlideLeftIn;
	private Animation mAnimSlideLeftOut;
	private Animation mAnimSlideRightIn;
	private Animation mAnimSlideRightOut;
	private ViewFlipper mViewFlipper;
	
	private TextView mLayoutUsed;
	private TextView mCurrentMonthTV;
	
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
		mFragmentView = inflater.inflate(R.layout.fragment_usage_summary, container, false);
		
		mLayoutUsed = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_size);
    	mCurrentMonthTV = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_current_month_tv);
    	
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
	
	private void loadGestures() {
		// Set reference for ViewFlipper layout
		mViewFlipper = (ViewFlipper) mFragmentView.findViewById(R.id.fragment_usage_summary_view_flipper);

		// Set reference to gesture detector
		mGestureDetector = new GestureDetector(new MyGestureDetector());

		// Set animation references
		mAnimSlideLeftIn = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_in);
		mAnimSlideLeftOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
		mAnimSlideRightIn = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_in);
		mAnimSlideRightOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_out);

	}
	
	private void loadFragmentView(){
		if (mAccountInfo.isInfoSet() 
    			&& mAccountStatus.isStatusSet()){
			
			// TODO: Is view reloaded post sync?
			
			//setViewRemaining();

	    	setViewSoFar();
	    	
	    	checkUsageStatus();
	    	
			// Setup the touch listener for layout
	    	LinearLayout mLayout = (LinearLayout) mFragmentView.findViewById(R.id.fragment_usage_summary_container);
	    	mLayout.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					Log.d(DEBUG_TAG, "onTouch");
					
					mGestureDetector.onTouchEvent(event);
					return false;
				}
			});
	    	
		}
	}

	/**
	 * 
	 */
	private void setViewRemaining() {
		
		TextView peak = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_remaining_number_tv);
		TextView peakDescription = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_remaining_description_tv);
		TextView peakSummary = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_remaining_summary_tv);
		TextView offpeak = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_remaining_number_tv);
		TextView offpeakDescription = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_remaining_description_tv);
		TextView offpeakSummary = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_remaining_summary_tv);
		TextView uploads = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_uploads_remaining_number_tv);
		TextView freezone = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_freezone_remaining_number_tv);
		
		
		peak.setText(mAccountStatus.getPeakDataRemaingGbString());
		offpeak.setText(mAccountStatus.getOffpeakDataRemaingGbString());
		uploads.setText(mAccountStatus.getUploadsDataUsedGbString());
		freezone.setText(mAccountStatus.getFreezoneDataUsedGbString());
		
		// Only set text if loading phone layout
		if (isLayoutPhone(mLayoutUsed)){
			mCurrentMonthTV.setText(mAccountStatus.getCurrentMonthString());
			peakDescription.setText(mAccountInfo.getPeakQuotaString());
			peakSummary.setText(mAccountStatus.getPeakShapedRemainingString());
			offpeakDescription.setText(mAccountInfo.getOffpeakQuotaString());
			offpeakSummary.setText(mAccountStatus.getOffpeakShapedRemainingString());
		} else {
			peakSummary.setText(mContext.getString(R.string.fragment_usage_summary_remaining_no_status));
			offpeakSummary.setText(mContext.getString(R.string.fragment_usage_summary_remaining_no_status));
		}
	}

	/**
	 * 
	 */
	private void setViewSoFar() {
		
		TextView peak = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_used_number_tv);
		TextView peakDescription = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_used_description_tv);
		TextView peakSummary = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_used_summary_tv);
		TextView offpeak = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_used_number_tv);
		TextView offpeakDescription = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_used_description_tv);
		TextView offpeakSummary = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_used_summary_tv);
		TextView uploads = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_uploads_used_number_tv);
		TextView freezone = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_freezone_used_number_tv);
		
		peak.setText(mAccountStatus.getPeakDataUsedGbString());
		offpeak.setText(mAccountStatus.getOffpeakDataUsedGbString());
		uploads.setText(mAccountStatus.getUploadsDataUsedGbString());
		freezone.setText(mAccountStatus.getFreezoneDataUsedGbString());
		
		// Only set text if loading phone layout
		if (isLayoutPhone(mLayoutUsed)){
			mCurrentMonthTV.setText(mAccountStatus.getCurrentMonthString());
			peakDescription.setText(mAccountInfo.getPeakQuotaString());
			peakSummary.setText(mAccountStatus.getPeakShapedUsedString());
			offpeakDescription.setText(mAccountInfo.getOffpeakQuotaString());
			offpeakSummary.setText(mAccountStatus.getOffpeakShapedUsedString());
		}
	}

	/**
	 * 
	 */
	private void checkUsageStatus() {
		NotificationHelper mNotification = new NotificationHelper(mContext);
		
		//TODO: Test layout logic better
		
		if(mNotification.isPeakQuotaNear()){
			setPeakWarning();
		}
		
		if(mNotification.isPeakQuotaOver()){
			setPeakError();
		}
		
		if(mNotification.isOffpeakQuotaNear()){
			setOffpeakWarning();
		}
		
		if(mNotification.isOffpeakQuotaOver()){
			setOffpeakError();
		}
	}
	
	private void setPeakWarning(){
    	RelativeLayout mLayoutContaner = (RelativeLayout) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_container);
    	mLayoutContaner.setBackgroundDrawable((getResources().getDrawable(R.drawable.shape_flash_warning)));
	}
	
	private void setPeakError(){
		RelativeLayout mLayoutContaner = (RelativeLayout) mFragmentView.findViewById(R.id.fragment_usage_summary_peak_container);
		mLayoutContaner.setBackgroundDrawable((getResources().getDrawable(R.drawable.shape_flash_error)));
	}
	
	private void setOffpeakWarning(){
    	RelativeLayout mLayoutContaner = (RelativeLayout) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_container);
    	mLayoutContaner.setBackgroundDrawable((getResources().getDrawable(R.drawable.shape_flash_warning)));
	}
	
	private void setOffpeakError(){
		RelativeLayout mLayoutContaner = (RelativeLayout) mFragmentView.findViewById(R.id.fragment_usage_summary_offpeak_container);
		mLayoutContaner.setBackgroundDrawable((getResources().getDrawable(R.drawable.shape_flash_error)));
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
	
	private boolean isLayoutPhone(TextView mLayoutUsed){
		CharSequence size = mLayoutUsed.getText();
		if ( size != null 
				&& size.equals(getActivity().getString(R.string.size_phone_port))){
			return true;
		} else {
			return false;
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
						Log.d(DEBUG_TAG, "Swipe left");
						mViewFlipper.setInAnimation(mAnimSlideLeftIn);
						mViewFlipper.setOutAnimation(mAnimSlideLeftOut);
						mViewFlipper.showNext();
					return true;
				}
				// Else check if it is a left to right swipe
				else if (motionEvent2.getX() - motionEvent1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
						Log.d(DEBUG_TAG, "Swipe right");
						mViewFlipper.setInAnimation(mAnimSlideRightIn);
						mViewFlipper.setOutAnimation(mAnimSlideRightOut);
						mViewFlipper.showPrevious();
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
