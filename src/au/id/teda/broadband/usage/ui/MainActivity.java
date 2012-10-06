package au.id.teda.broadband.usage.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import au.id.teda.broadband.usage.R;

public class MainActivity extends Activity {
	
	//private static final String DEBUG_TAG = "bbusage";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar mActionBar = getActionBar();
        // Set action bar icon for navigation
        mActionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

}