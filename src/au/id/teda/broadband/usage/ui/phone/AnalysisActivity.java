package au.id.teda.broadband.usage.ui.phone;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.MonthListFragment;

public class AnalysisActivity extends FragmentActivity {
	
	private static final String DEBUG_TAG = "bbusage";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_view);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(MonthListFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(MonthListFragment.ARG_ITEM_ID));
            
            Log.d(DEBUG_TAG, "AnalysisActivity onCreate(). ");
            
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
