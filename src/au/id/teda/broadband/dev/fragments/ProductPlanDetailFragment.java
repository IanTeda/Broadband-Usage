package au.id.teda.broadband.dev.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import au.id.teda.broadband.dev.R;

public class ProductPlanDetailFragment extends BaseFragment {

	// View inflated by fragment
	private View mFragmentView;

	/**
	* Called 3rd in the fragment life cycle
	*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_product_plan_detail, container, false);

        RelativeLayout mAnytimeContainer = (RelativeLayout) mFragmentView.findViewById(R.id.fragment_product_plan_anytime_container);
        RelativeLayout mPeakContainer = (RelativeLayout) mFragmentView.findViewById(R.id.fragment_product_plan_peak_container);
        RelativeLayout mOffpeakContainer = (RelativeLayout) mFragmentView.findViewById(R.id.fragment_product_plan_offpeak_container);


        if (mAccountInfo.isAccountAnyTime()){
            mAnytimeContainer.setVisibility(View.VISIBLE);
            mPeakContainer.setVisibility(View.GONE);
            mOffpeakContainer.setVisibility(View.GONE);
        } else {
            mAnytimeContainer.setVisibility(View.GONE);
            mPeakContainer.setVisibility(View.VISIBLE);
            mOffpeakContainer.setVisibility(View.VISIBLE);
        }
		
		return mFragmentView;
	}
	
	@Override
	protected void loadFragmentView(){
		
		// Set reference to product plan text views
		TextView mProduct = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_product);
		TextView mPlan = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_plan);
		
		// Set text in text views
		mProduct.setText(mAccountInfo.getProduct());
		mPlan.setText(mAccountInfo.getPlan());

        // Set reference to anytime detail
        TextView mAnytimeQuotaPeriod = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_anytime_quota_data_period);
        TextView mAnytimeQuotaDay = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_anytime_quota_data_day);
        TextView mAnytimeQuotaHour = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_anytime_quota_data_hour);

        // Set anytime text views
        mAnytimeQuotaPeriod.setText(IntUsageToString(mAccountInfo.getAnyTimeQuotaGb()));
        mAnytimeQuotaDay.setText(IntUsageToString(mAccountInfo.getAnyTimeQuotaDailyMb()));
        mAnytimeQuotaHour.setText(IntUsageToString(mAccountInfo.getAnyTimeQuotaHourlyMb()));
		
		// Set reference to peak detail 		
		TextView mPeakStart = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_start_time);
		TextView mPeakStartUnit = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_start_time_unit);
		TextView mPeakFinish = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_finish_time);
		TextView mPeakFinishUnit = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_finish_time_unit);
		TextView mPeakHours = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_hours_time);
		TextView mPeakQuotaPeriod = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_quota_data_period);
		TextView mPeakQuotaDay = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_quota_data_day);
		TextView mPeakQuotaHour = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_peak_quota_data_hour);

		// Set peak text views
		mPeakStart.setText(mAccountInfo.getOffpeakEndTimeString());
		mPeakStartUnit.setText(mAccountInfo.getOffpeakEndTimeAmPmString());
		mPeakFinish.setText(mAccountInfo.getOffpeakStartTimeString());
		mPeakFinishUnit.setText(mAccountInfo.getOffpeakStartTimeAmPmString());
		mPeakHours.setText( mAccountInfo.getPeakHourString());
		mPeakQuotaPeriod.setText(IntUsageToString(mAccountInfo.getPeakQuotaGb()));
		mPeakQuotaDay.setText(IntUsageToString(mAccountInfo.getPeakQuotaDailyMb()));
		mPeakQuotaHour.setText(IntUsageToString(mAccountInfo.getPeakQuotaHourlyMb()));

		// Set reference to peak detail 		
		TextView mOffpeakStart = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_start_time);
		TextView mOffpeakStartUnit = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_start_time_unit);
		TextView mOffpeakFinish = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_finish_time);
		TextView mOffpeakFinishUnit = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_finish_time_unit);
		TextView mOffpeakHours = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_hours_time);
		TextView mOffpeakQuotaPeriod = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_quota_data_period);
		TextView mOffpeakQuotaDay = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_quota_data_day);
		TextView mOffpeakQuotaHour = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_detail_offpeak_quota_data_hour);

		// Set peak text views
		mOffpeakStart.setText(mAccountInfo.getOffpeakStartTimeString());
		mOffpeakStartUnit.setText(mAccountInfo.getOffpeakStartTimeAmPmString());
		mOffpeakFinish.setText(mAccountInfo.getOffpeakEndTimeString());
		mOffpeakFinishUnit.setText(mAccountInfo.getOffpeakEndTimeAmPmString());
		mOffpeakHours.setText( mAccountInfo.getOffpeakHourString());
		mOffpeakQuotaPeriod.setText(IntUsageToString(mAccountInfo.getOffpeakQuotaGb()));
		mOffpeakQuotaDay.setText(IntUsageToString(mAccountInfo.getOffpeakQuotaDailyMb()));
		mOffpeakQuotaHour.setText(IntUsageToString(mAccountInfo.getOffpeakQuotaHourlyMb()));
			
	}

}
