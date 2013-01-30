package au.id.teda.broadband.usage.ui;

import android.os.Bundle;
import android.util.Log;
import au.id.teda.broadband.usage.R;

/**
 * Activity holder for PeakUsageFragment
 * @author Ian Teda
 *
 */
public class PeakUsageActivity extends BaseActivity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.activity_peak_usage);
			
		Log.d(DEBUG_TAG, "PeakUsageActivity");
	}
}
