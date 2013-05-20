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
public class UsageActivity extends BaseActivity {
    
    private UsageActivityAdapter mAdapter;
    private ViewPager mPager;
    private TitlePageIndicator mIndicator;
    
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Show home (up) button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
					
		setContentView(R.layout.activity_usage);
        
		mAdapter = new UsageActivityAdapter(getSupportFragmentManager());
		mPager = (ViewPager)findViewById(R.id.activity_usage_pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (TitlePageIndicator)findViewById(R.id.activity_usage_indicator);
		mIndicator.setViewPager(mPager);
		mIndicator.setTextColor(getResources().getColor(R.color.base_light));
		mIndicator.setSelectedBold(true);
		mIndicator.setSelectedColor(getResources().getColor(R.color.base));
		mIndicator.setFooterColor(getResources().getColor(R.color.accent));
		mIndicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Underline);

	}

    class UsageActivityAdapter extends FragmentPagerAdapter {
    	
        private final String[] TAB_TITLES = new String[] { PeakUsageFragment.PAGE_TITLE, OffpeakUsageFragment.PAGE_TITLE };
        private int mCount = TAB_TITLES.length;
    	
        public UsageActivityAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

        	switch (position) {
        	case 0:
        		return new PeakUsageFragment();
        	case 1:
        		return new OffpeakUsageFragment();
        	default:
                throw new IllegalArgumentException("not this many fragments: " + position);
           }
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	return TAB_TITLES[position % TAB_TITLES.length];
        }

        @Override
        public int getCount() {
        	return mCount;
        }
        
        public void setCount(int count) {
            if (count > 0 && count <= 10) {
                mCount = count;
                notifyDataSetChanged();
                }
            }

    }
}
