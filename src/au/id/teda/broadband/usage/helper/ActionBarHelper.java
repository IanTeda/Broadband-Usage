package au.id.teda.broadband.usage.helper;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.content.Context;
import au.id.teda.broadband.usage.adapter.SectionPagerAdapter;

public class ActionBarHelper {
	
	// Action bar is included in all activities that use the Theme.Holo theme
	// (or one of its descendants)
	
	// Context passed to the class
	private final Context mContext;
	
	// Action bar instanced passed to the class
	private final ActionBar mActionBar;
	
	// TabPagerAdapter
	private final SectionPagerAdapter mSectionPagerAdapter;

	public ActionBarHelper(Context context, ActionBar actionBar, SectionPagerAdapter sectionPagerAdapter) {
		// Set context
		this.mContext = context;
		
		// Set action bar instance
		mActionBar = actionBar;
		
		// Set pager
		mSectionPagerAdapter = sectionPagerAdapter;
		
		setHomeIconUp();
		
		setTabNavigationOn();
		
		loadTabs();
	}
	
    public int getActionBarTabCount() {
        return 3;
    }

    public void setHomeIconUp(){
    	// Set action bar icon for navigation
    	mActionBar.setDisplayHomeAsUpEnabled(true);
    }
    
    public void setTabNavigationOn(){
    	// Set up action bar tabs
    	mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }
    
    public void loadTabs(){
    	// For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
        	mActionBar.addTab(
        			mActionBar.newTab()
                            .setText(mSectionPagerAdapter.getPageTitle(i))
                            .setTabListener((TabListener) mContext));
        }
    	
    }
    
    public void removeAllTabs() {
        mActionBar.removeAllTabs();
    }
    
    public void setActiveTab(int position){
    	mActionBar.setSelectedNavigationItem(position);
    }
    
    public void setView(int position){
    }
}
