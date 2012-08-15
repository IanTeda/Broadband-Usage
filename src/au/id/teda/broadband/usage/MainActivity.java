package au.id.teda.broadband.usage;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
	

	// Static string for debug tags
	private static final String DEBUG_TAG = MainActivity.class.getSimpleName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	   super.onCreate(savedInstanceState);
    	   setContentView(R.layout.activity_main);
    	   
    	   // Set action bar icon for navigation
    	   ActionBar actionBar = getActionBar();
    	   actionBar.setDisplayHomeAsUpEnabled(true);
    }
       
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Action bar is included in all activities that use the Theme.Holo theme
    	// (or one of its descendants)
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
}
