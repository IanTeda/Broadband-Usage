package au.id.teda.broadband.dev.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import au.id.teda.broadband.dev.R;
import au.id.teda.broadband.dev.fragments.OffpeakUsageFragment;
import au.id.teda.broadband.dev.fragments.PeakUsageFragment;
import com.viewpagerindicator.TitlePageIndicator;

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
