package au.id.teda.broadband.usage.ui;

import android.content.Intent;
import android.util.Log;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.MonthListFragment.MonthListSelectedListner;
import au.id.teda.broadband.usage.ui.phone.AnalysisActivity;

public class MainActivity extends BaseActivity implements MonthListSelectedListner {
	
	@Override
	public void onMonthSelected(String id) {
		// The user selected the month from the MonthListFragment
		
		// Capture the article fragment from the activity layout
		AnalysisFragment analysisFrag = (AnalysisFragment) getSupportFragmentManager()
						.findFragmentById(R.id.analysis_fragment_right);
		
		// Check if we are on the tablet or phone
		if (analysisFrag != null) {
			Log.d(DEBUG_TAG, "I am a tablet");
			
			// Call a method in the ArticleFragment to update its content
        	analysisFrag.updateAnalysisView(id);
			
		} else {
			Log.d(DEBUG_TAG, "I am a phone");
			
			// Load the activity for the phone
            Intent detailIntent = new Intent(this, AnalysisActivity.class);
            detailIntent.putExtra(AnalysisFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
			
		}
	}
}