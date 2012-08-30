package au.id.teda.broadband.usage.listener;

import android.app.Activity;
import android.app.Fragment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class ActionBarTabsListener <T extends Fragment> implements ActionBar.TabListener {
	
	// Static string for debug tags
	private static final String DEBUG_TAG = "Broadband Usage";
	//private static final String INFO_TAG = ActionBarTabsListener.class.getSimpleName();
	
    private Fragment mFragment;
    private final Activity mActivity;
    private final String mTag;
    private ViewPager mPager;
    
    /** Constructor used each time a new tab is created.
     * @param activity  The host Activity, used to instantiate the fragment
     * @param tag  The identifier tag for the fragment
     * @param clz  The fragment's Class, used to instantiate the fragment
     */
   public ActionBarTabsListener(Activity activity, String tag, ViewPager pager) {
       mActivity = activity;
       mTag = tag;
       mPager = pager;
   }

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "onTabSelected");
		mPager.setCurrentItem(Integer.parseInt(mTag));
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "onTabUnselected");
	}


	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, "onTabReselected");
		
	}

}
