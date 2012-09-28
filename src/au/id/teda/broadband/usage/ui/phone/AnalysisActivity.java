package au.id.teda.broadband.usage.ui.phone;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.AnalysisFragment;

public class AnalysisActivity extends FragmentActivity {
	
	private static final String DEBUG_TAG = "bbusage";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_detail);
        
        // Set up the action bar.
        final ActionBar mActionBar = getActionBar();
        // Set action bar icon for navigation
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("January 2012");
        
        
        
        if (savedInstanceState == null) {
        	
            // Add intent values to argument bundle
            Bundle arguments = new Bundle();
            arguments.putString(AnalysisFragment.ARG_ITEM_ID, 
            		getIntent().getStringExtra(AnalysisFragment.ARG_ITEM_ID));
            
            AnalysisFragment analysisFragment = new AnalysisFragment();
            analysisFragment.setArguments(arguments);
                        
            getSupportFragmentManager().beginTransaction()
            	.add(R.id.month_detail_container, analysisFragment)
            	.commit();
        }

    }	
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

}
