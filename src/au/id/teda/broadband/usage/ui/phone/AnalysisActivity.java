package au.id.teda.broadband.usage.ui.phone;

import android.os.Bundle;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.AnalysisFragment;
import au.id.teda.broadband.usage.ui.BaseActivity;

public class AnalysisActivity extends BaseActivity {
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_detail);
        
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

}
