package au.id.teda.broadband.usage.ui;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import au.id.teda.broadband.usage.helper.DummyContent;

public class MonthListFragment extends ListFragment {
	
	private static final String DEBUG_TAG = "bbusage";
	
	public static final String ARG_ITEM_ID = "item_id";
	
	private MonthListSelectedListner mMonthListSelectedListner;
    
	// Class callback method
	// The container Activity must implement this interface so the frag can deliver messages
	public interface MonthListSelectedListner {
    	// Called when a list item is selected
        public void onMonthSelected(String id);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setListAdapter(new ArrayAdapter<au.id.teda.broadband.usage.helper.DummyContent.DummyItem>(getActivity(),
                R.layout.simple_list_item_activated_1,
                R.id.text1,
                DummyContent.ITEMS));
    }
    
    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        //if (getFragmentManager().findFragmentById(R.id.analysis_fragment_right) != null) {
            //getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //}
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
        
        mMonthListSelectedListner.onMonthSelected(DummyContent.ITEMS.get(position).id);
        
        // Notify the parent activity of selected item
       // mMonthListSelectedListner.onMonthSelected(position);
        
        // Set the item as checked to be highlighted when in two-pane layout
        //getListView().setItemChecked(position, true);
    }
    
}
