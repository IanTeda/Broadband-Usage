package au.id.teda.broadband.usage.fragments;

import android.os.Bundle;
import android.view.*;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.helper.NotificationHelper;

public class UsageSummaryAnyTimeFragment extends BaseFragment {
	
	// View inflated by fragment
	private View mFragmentView;

	// TextViews for dev
	private TextView mLayoutUsed;
	private TextView mCurrentMonth;
	private TextView mDaysNumber;
	private TextView mDaysDescription;
	private TextView mDaysSummary;
	private TextView mAnyTimeNumber;
	private TextView mAnyTimeDescription;
	private TextView mAnyTimeSummary;
	private TextView mUploadsNumber;
	private TextView mFreezoneNumber;
	private TextView mUpTimeNumber;
	private TextView mIpAddress;
	
	// Gesture detector class
	private GestureDetector mGestureDetector;
	
	// Prefrence key to store summary state
	private final static String PREF_SUMMARY_STATE_KEY = "pref_summary_state_key";
	
	/**
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_usage_summary_anytime, container, false);
		
		// Set reference for textviews
		mLayoutUsed = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_size);
    	mCurrentMonth = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_current_month);
    	mDaysNumber = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_days_number);
    	mDaysDescription = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_days_description);
    	mDaysSummary = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_days_summary);
		mAnyTimeNumber = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_anytime_number);
		mAnyTimeDescription = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_anytime_description);
		mAnyTimeSummary = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_anytime_summary);
		mUploadsNumber = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_uploads_number);
		mFreezoneNumber = (TextView) mFragmentView.findViewById(R.id.fragment_usage_summary_freezone_number);
    	mUpTimeNumber = (TextView) mFragmentView.findViewById(R.id.fragment_rollover_uptime_uptime_number);
    	mIpAddress = (TextView) mFragmentView.findViewById(R.id.fragment_rollover_uptime_description);

		// Setup the touch listener for chart
    	mFragmentView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mGestureDetector.onTouchEvent(event);
				//mGestureDetector.onTouchEvent(event);
				//return false;
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
		
		// Set gesture reference
		mGestureDetector = new GestureDetector(new MyGestureDetector());
	}
	
	/**
	 * First call in the death of fragment
	 */
	@Override
	public void onPause() {
		super.onPause();
		
		// Remember summary state
	    mEditor.putBoolean(PREF_SUMMARY_STATE_KEY, isVeiwSoFar());
	    mEditor.commit();
	}
	
	@Override
	protected void loadFragmentView(){
			
		// Used in both sofar and remaining views for phone
		if (mLayoutHelper.isLayoutPhonePort(mLayoutUsed)){
			mCurrentMonth.setText(mAccountStatus.getCurrentMonthString());
			mDaysDescription.setText(mAccountStatus.getRolloverDateString());
			mAnyTimeSummary.setText(mAccountInfo.getAnyTimeQuotaString());
			mUpTimeNumber.setText(mAccountStatus.getUpTimeDaysString());
			mIpAddress.setText(mAccountStatus.getIpAddressStrng());
		} else if (mLayoutHelper.isLayout_w1024dp(mLayoutUsed)){
		}
		
		setSummaryView();
		checkUsageStatus();
	}
	
	private void setSummaryView() {
		// Set view flipper to last view
		boolean soFar = mSettings.getBoolean(PREF_SUMMARY_STATE_KEY, true);
		if (soFar){
			setUsageSoFar();
		} else {
			setUsageToGo();
		}
	}

	private void setUsageToGo() {

		mAnyTimeNumber.setText(mAccountStatus.getAnytimeDataRemaingGbString());
		mUploadsNumber.setText(mAccountStatus.getUploadsDataUsedGbString());
		mFreezoneNumber.setText(mAccountStatus.getFreezoneDataUsedGbString());
		
		// Set this even though it is hidden. Used for switching
		mDaysSummary.setText(mContext.getString(R.string.fragment_usage_summary_days_to_go));
		
		// Only set text if loading phone layout
		if (mLayoutHelper.isLayoutPhonePort(mLayoutUsed)){
			mDaysNumber.setText(mAccountStatus.getDaysToGoString());		
			mAnyTimeDescription.setText(mAccountStatus.getAnytimeShapedRemainingString());
		}
	}

	private void setUsageSoFar() {
		
		mAnyTimeNumber.setText(mAccountStatus.getAnytimeDataUsedGbString());
		mUploadsNumber.setText(mAccountStatus.getUploadsDataUsedGbString());
		mFreezoneNumber.setText(mAccountStatus.getFreezoneDataUsedGbString());
		
		// Set this even though it is hidden. Used for switching
		mDaysSummary.setText(mContext.getString(R.string.fragment_usage_summary_days_so_far));
		
		// Only set text if loading phone layout
		if (mLayoutHelper.isLayoutPhonePort(mLayoutUsed)){
			mDaysNumber.setText(mAccountStatus.getDaysSoFarString());
			mAnyTimeDescription.setText(mAccountStatus.getAnytimeShapedUsedString());
		}
	}
	
	private boolean isVeiwSoFar(){
		CharSequence days = mDaysSummary.getText();
		CharSequence soFar = mContext.getString(R.string.fragment_usage_summary_days_so_far);
		
		if (days.equals(soFar)){
			return true;
		} else {
			return false;
		}
	}

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
	
	class MyGestureDetector extends SimpleOnGestureListener {
		
		@Override
	    public boolean onDoubleTap(MotionEvent event) {
	        if (isVeiwSoFar()){
	        	setUsageToGo();
	        } else {
	        	setUsageSoFar();
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
