package au.id.teda.broadband.usage.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.MainActivity;

import com.actionbarsherlock.app.SherlockFragment;

public class DataTableFragment extends SherlockFragment {

	private static final String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.listfragment_data_table, container, false);
    }

}
