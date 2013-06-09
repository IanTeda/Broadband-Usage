package au.id.teda.broadband.dev.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import au.id.teda.broadband.dev.R;
import au.id.teda.broadband.dev.database.DailyDataTableAdapter;
import au.id.teda.broadband.dev.util.DailyVolumeUsage;
import au.id.teda.broadband.dev.util.DailyVolumeUsageAdapter;

public class DataTableAnytimeFragment extends BaseFragment {
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Daily dev array
	private DailyVolumeUsage mDailyUsageArray[];

	
	/**
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(DEBUG_TAG, "DataTableAnytimeFragment onCreate");

		// Set reference to fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_data_table_anytime, container, false);
		
		return mFragmentView;
	}
	
	/**
	 * Load fragment view if account status and info is set
	 */
	@Override
	protected void loadFragmentView(){

        Log.d(DEBUG_TAG, "DataTableAnytimeFragment loadFragmentView");

        Log.d(DEBUG_TAG, "isInfoSet:" + mAccountInfo.isInfoSet() + " | isStatusSet:" + mAccountStatus.isStatusSet());

		if (mAccountInfo.isInfoSet() 
    			&& mAccountStatus.isStatusSet()){
			
	        // Get volume array
			DailyDataTableAdapter mDatabase = new DailyDataTableAdapter(mContext);
			String period = mAccountStatus.getDataBaseMonthString();
			mDailyUsageArray = mDatabase.getDailyVolumeUsage(period);
			
			// Initiate adapter to be used with list view
			DailyVolumeUsageAdapter adapter = new DailyVolumeUsageAdapter(mContext, R.layout.listview_data_table_row, mDailyUsageArray);
			
			// Reference list view to be used
			ListView mListView = (ListView) mFragmentView.findViewById(R.id.fragment_data_table_listview);
			
			// Set adapter to be used with the list view
			mListView.setAdapter(adapter);
			
			//loadDataTable();
			
		}
	}

}
