package au.id.teda.broadband.usage.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.database.DailyDataTableAdapter;
import au.id.teda.broadband.usage.util.DailyVolumeUsage;
import au.id.teda.broadband.usage.util.DailyVolumeUsageAdapter;

public class DataTableFragment extends BaseFragment {
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Daily usage array
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
			
	        // Get volume usage array
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
