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
        	//Log.d(DEBUG_TAG, "getItem Position:" + position);
        	Fragment mFragment = new Fragment();

        	switch (position) {
        	case 0:
        		mFragment = PeakUsageFragment.newInstance(CONTENT[position % CONTENT.length]);
        		break;
        	case 1:
        		mFragment = OffpeakUsageFragment.newInstance(CONTENT[position % CONTENT.length]);
        		break;
           }

           return mFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	Log.d(DEBUG_TAG, "getPageTitle Position:" + position);
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }
}
