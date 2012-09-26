package au.id.teda.broadband.usage.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import au.id.teda.broadband.usage.R;

public class MonthListFragment extends ListFragment {
	
	private static final String DEBUG_TAG = "bbusage";
	
	private MonthListSelectedListner mMonthListSelectedListner;
    
	// Class callback method
	// The container Activity must implement this interface so the frag can deliver messages
    public interface MonthListSelectedListner {
    	/** Called by HeadlinesFragment when a list item is selected */
        public void onMonthSelected(int position);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int layout = android.R.layout.simple_list_item_activated_1;
        
        // Create an array adapter for the list view, using the Ipsum headlines array
        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, Ipsum.Headlines));
    }
    
    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.analysis_fragment_right) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mMonthListSelectedListner = (MonthListSelectedListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FragmentCallback");
        }
    }
    
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Log.v(DEBUG_TAG, "List item clicked " );
        
        // Notify the parent activity of selected item
        mMonthListSelectedListner.onMonthSelected(position);
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
    
}
