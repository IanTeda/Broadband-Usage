package au.id.teda.broadband.usage.ui;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.adapter.SectionPagerAdapter;
import au.id.teda.broadband.usage.ui.MonthListFragment.MonthListSelectedListner;
import au.id.teda.broadband.usage.ui.phone.AnalysisActivity;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, MonthListSelectedListner {
	
	private static final String DEBUG_TAG = "bbusage";
	
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide layouts for each of the
     * sections.
     * */
    SectionPagerAdapter mSectionPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a layout for each of the three primary sections
        // of the app.
        mSectionPagerAdapter = new SectionPagerAdapter(this);

        // Set up the action bar.
        final ActionBar mActionBar = getActionBar();
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