package au.id.teda.broadband.usage;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity implements ActionBar.TabListener {
	

	// Static string for debug tags
	private static final String DEBUG_TAG = "Broadband Usage";
	//private static final String INFO_TAG = MainActivity.class.getSimpleName();
	
	/**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	/**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	   super.onCreate(savedInstanceState);
    	   setContentView(R.layout.activity_main);
    	   
    	   // Create the adapter that will return a fragment for each of the three primary sections
           // of the app.
           mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
    	   
           // Setup action bar
    	   setUpActionBar();
    	   
    	   // Set up the ViewPager with the sections adapter.
           mViewPager = (ViewPager) findViewById(R.id.pager);
           mViewPager.setAdapter(mSectionsPagerAdapter);
    	   
    }

	/**
	 * Method used to set up the action bar
	 * 1. Enable icon home up navigation
	 * 2. Enable tab navigation
	 * 3. Set up tabs
	 */
	private void setUpActionBar() {
		// Get action bar instance
		ActionBar actionBar = getActionBar();
    	   
		// Set action bar icon for navigation
		actionBar.setDisplayHomeAsUpEnabled(true);
    	   
		// Set up action bar tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// First tab
		actionBar.addTab(
				actionBar
					.newTab()
					.setText(R.string.action_bar_tab_current)
					.setTabListener(this) // Set MainActivity as tab listener
		);
		// Second tab
		actionBar.addTab(
				actionBar
					.newTab()
					.setText(R.string.action_bar_tab_analysis)
					.setTabListener(this) // Set MainActivity as tab listener
		);
		// Third tab
		actionBar.addTab(
				actionBar
					.newTab()
					.setText(R.string.action_bar_tab_data_table)
					.setTabListener(this) // Set MainActivity as tab listener
		);
	}
       
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Action bar is included in all activities that use the Theme.Holo theme
    	// (or one of its descendants)
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {	
    	Log.d(DEBUG_TAG, "onTabUnselected");
    }
	
    @Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	// When the given tab is selected, switch to the corresponding page in the ViewPager.
    	Log.d(DEBUG_TAG, "onTabSelected");
    }
	
    @Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	Log.d(DEBUG_TAG, "onTabReselected");
    
    }
    
 
}
