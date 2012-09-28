package au.id.teda.broadband.usage.ui.phone;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.AnalysisFragment;

public class AnalysisActivity extends FragmentActivity {
	
	private static final String DEBUG_TAG = "bbusage";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_detail);
        
        Log.d(DEBUG_TAG, "AnalysisActivity onCreate(). ");
        
        if (savedInstanceState == null) {
        	
            // Add intent values to argument bundle
            Bundle arguments = new Bundle();
            arguments.putString(AnalysisFragment.ARG_ITEM_ID, 
            		getIntent().getStringExtra(AnalysisFragment.ARG_ITEM_ID));
            
            Log.d(DEBUG_TAG, "Argument Bundle: " + arguments);
            
            AnalysisFragment analysisFragment = new AnalysisFragment();
            analysisFragment.setArguments(arguments);
                        
            getSupportFragmentManager().beginTransaction()
            	.add(R.id.month_detail_container, analysisFragment)
            	.commit();
        }

    }	

}
