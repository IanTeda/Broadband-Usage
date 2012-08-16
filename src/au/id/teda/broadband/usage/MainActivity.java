package au.id.teda.broadband.usage;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class MainActivity extends Activity {
	

	// Static string for debug tags
	private static final String DEBUG_TAG = MainActivity.class.getSimpleName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	   super.onCreate(savedInstanceState);
    	   setContentView(R.layout.activity_main);
    	   
    	   // Get action bar instance
    	   ActionBar actionBar = getActionBar();
    	   
    	   // Set action bar icon for navigation
    	   actionBar.setDisplayHomeAsUpEnabled(true);
    	   
    	   // Set action bar spinner adapter
    	   SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
    			   R.array.action_bar_spinner_list,
    			   android.R.layout.simple_spinner_dropdown_item);
    	   
    	   mOnNavigationListener = new OnNavigationListener() {
    		   	// Get the same strings provided for the drop-down's ArrayAdapter
    		   String[] strings = getResources().getStringArray(R.array.action_list);

    		   @Override
    		   public boolean onNavigationItemSelected(int position, long itemId) {
	    		  
    		   }
	    	};
    	   
    	   // Allow action bar spinner navigation
    	   actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    	   // Set call backs for action bar spinner
    	   actionBar.setListNavigationCallbacks(mSpinnerAdapter, mNavigationCallback);
    	   
    }
       
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Action bar is included in all activities that use the Theme.Holo theme
    	// (or one of its descendants)
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
}
