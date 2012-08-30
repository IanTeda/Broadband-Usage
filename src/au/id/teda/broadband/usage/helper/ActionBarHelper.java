package au.id.teda.broadband.usage.helper;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.content.Context;
import au.id.teda.broadband.usage.R;

public class ActionBarHelper {
	
	// Action bar is included in all activities that use the Theme.Holo theme
	// (or one of its descendants)
	
	// Context passed to the class
	private final Context mContext;
	// Action bar instanced passed to th class
	private final ActionBar mActionBar;

	public ActionBarHelper(Context context, ActionBar actionBar) {
		// Set context
		this.mContext = context;
		
		// Set action bar instance
		mActionBar = actionBar;
		
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
        for (int i = 0; i < getActionBarTabCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
        	mActionBar.addTab(
        			mActionBar.newTab()
                            .setText(getPageTitle(i))
                            .setTabListener((TabListener) mContext));
        }
    	
    }
    
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return mContext.getString(R.string.action_bar_tab_current).toUpperCase();
            case 1: return mContext.getString(R.string.action_bar_tab_analysis).toUpperCase();
            case 2: return mContext.getString(R.string.action_bar_tab_data_table).toUpperCase();
        }
        return null;
    }
    
    public void removeAllTabs() {
        mActionBar.removeAllTabs();
    }

}
