package au.id.teda.broadband.usage.ui.phone;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.AnalysisFragment;
import au.id.teda.broadband.usage.ui.MonthListFragment;

public class AnalysisActivity extends FragmentActivity {
	
	private static final String DEBUG_TAG = "bbusage";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_detail);
        
        Log.d(DEBUG_TAG, "AnalysisActivity onCreate(). ");
        
        if (savedInstanceState == null) {
            AnalysisFragment analysis = new AnalysisFragment();
            analysis.setArguments(getIntent().getExtras());

            Log.d(DEBUG_TAG, "Intent Extras: " + getIntent().getExtras());
            
            getSupportFragmentManager().beginTransaction()
            	.add(R.id.month_detail_container, analysis)
            	.commit();
            
            /**
            MonthListFragment fragment = new MonthListFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.article, fragment)
                    .commit();
                    **/
        }

    }	

}
