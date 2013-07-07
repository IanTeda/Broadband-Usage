package au.id.teda.broadband.usage.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.database.DailyDataTableAdapter;
import au.id.teda.broadband.usage.util.DailyVolumeUsage;
import au.id.teda.broadband.usage.util.DailyVolumeUsageAdapter;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class DataTableFragment extends BaseFragment {
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Daily dev array
	private DailyVolumeUsage mDailyUsageArray[];

	
	/**
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Set reference to fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_data_table, container, false);
		
		return mFragmentView;
	}
	
	/**
	 * Called 4th in the fragment life cycle
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	
	/**
	 * Called 5th in the fragment life cycle
	 */
	@Override
	public void onResume() {
		super.onResume();
	}
	
	/**
	 * Called 1st in the death of fragment
	 */
	@Override
	public void onPause() {
		super.onPause();
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

            String period;
            if (NetworkUtilities.weTesting) {
                period = NetworkUtilities.PERIOD_STRING;
            } else {
                period = mAccountStatus.getDataBaseMonthString();
            }

            // Get data array
            mDailyUsageArray = mDatabase.getDailyVolumeUsage(period);

            // Initiate adapter to be used with list view
			DailyVolumeUsageAdapter adapter = new DailyVolumeUsageAdapter(mContext, R.layout.listview_data_table_row, mDailyUsageArray);
			
			// Reference list view to be used
			ListView mListView = (ListView) mFragmentView.findViewById(R.id.fragment_data_table_listview);
			
			// Set adapter to be used with the list view
			mListView.setAdapter(adapter);

            // Hide rows based on account type
            if (mAccountInfo.isAccountAnyTime()){
                // Hide peak container
                TextView peakContainer = (TextView) mFragmentView.findViewById(R.id.listview_data_header_peak);
                peakContainer.setVisibility(View.GONE);

                // Hide offpeak container
                TextView offpeakContainer = (TextView) mFragmentView.findViewById(R.id.listview_data_header_offpeak);
                offpeakContainer.setVisibility(View.GONE);

            } else {
                // Hide anytime container
                TextView anytimeContainer = (TextView) mFragmentView.findViewById(R.id.listview_data_header_anytime);
                anytimeContainer.setVisibility(View.GONE);
            }
			
			//loadDataTable();
			
		}
	}

}
