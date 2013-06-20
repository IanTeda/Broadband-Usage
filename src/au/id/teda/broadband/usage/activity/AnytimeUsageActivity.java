package au.id.teda.broadband.usage.activity;

import android.os.Bundle;
import au.id.teda.broadband.usage.R;

/**
 * Activity holder for PeakUsageFragment
 * @author Ian Teda
 *
 */
public class AnytimeUsageActivity extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Show home (up) button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_anytime_usage);

	}
}
