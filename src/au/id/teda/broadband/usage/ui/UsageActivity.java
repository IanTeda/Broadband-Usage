package au.id.teda.broadband.usage.ui;

import com.viewpagerindicator.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.fragments.OffpeakUsageFragment;
import au.id.teda.broadband.usage.ui.fragments.PeakUsageFragment;

/**
 * Activity holder for PeakUsageFragment
 * @author Ian Teda
 *
 */
public class UsageActivity extends BaseActivity {

    private static final String[] CONTENT = new String[] { "Peak", "Offpeak" };
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		setContentView(R.layout.activity_usage);
		Log.d(DEBUG_TAG, "UsageActivity");
        
		FragmentPagerAdapter adapter = new UsageAdapter(getSupportFragmentManager());
        //FragmentPagerAdapter adapter = new UsageAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
			
	}

    class UsageAdapter extends FragmentPagerAdapter {
        public UsageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
        	Fragment f = new Fragment();

        	switch (position) {
        	case 0:
        			f = PeakUsageFragment.newInstance(position);
        			break;
        	case 1:
        			f = OffpeakUsageFragment.newInstance(position);
        			break;

           }

           return f;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }
}
