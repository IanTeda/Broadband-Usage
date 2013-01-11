package au.id.teda.broadband.usage.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.MainActivity;

import com.actionbarsherlock.app.SherlockListFragment;

public class DataTableListFragment extends SherlockListFragment {

	private static final String DEBUG_TAG = MainActivity.DEBUG_TAG;

	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(DEBUG_TAG, "DataTableListFragment.onCreate()");
		
		mContext = getSherlockActivity();

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		Log.d(DEBUG_TAG, "DataTableListFragment.onActivityCreated()");
		
		String[] presidents = { "Dwight D. Eisenhower", "John F. Kennedy",
	            "Lyndon B. Johnson", "Richard Nixon", "Gerald Ford",
	            "Jimmy Carter", "Ronald Reagan", "George H. W. Bush",
	            "Bill Clinton", "George W. Bush", "Barack Obama" };
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1, presidents);
		setListAdapter(adapter);
		
		super.onActivityCreated(savedInstanceState);
		
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		Log.d(DEBUG_TAG, "DataTableListFragment.onCreateView()");
		
		View view = inflater.inflate(R.layout.listfragment_data_table, container, false);
		 
		return view;
    }

}
