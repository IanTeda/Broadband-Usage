package au.id.teda.broadband.usage.ui;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.adapter.SectionPagerAdapter;

public class BaseActivity extends FragmentActivity implements ActionBar.TabListener {
	
	public static final String DEBUG_TAG = "bbusage";
	
	/**
     * The {@link android.support.v4.view.PagerAdapter} that will provide layouts for each of the
     * sections.
     * */
    SectionPagerAdapter mSectionPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    
    ActionBar mActionBar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a layout for each of the three primary sections
        // of the app.
        mSectionPagerAdapter = new SectionPagerAdapter(this);

        // Set up the action bar.
        mActionBar = getActionBar();
        
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Set action bar icon for navigation
        mActionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionPagerAdapter);

        // When swiping between different sections, select the corresponding tab.
        // We can also use ActionBar.Tab#select() to do this if we have a reference to the
        // Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	mActionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
        	mActionBar.addTab(
        			mActionBar.newTab()
                            .setText(mSectionPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    
    public void removeAllTabs() {
        mActionBar.removeAllTabs();
    }

}
