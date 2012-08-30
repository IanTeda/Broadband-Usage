package au.id.teda.broadband.usage;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import au.id.teda.broadband.usage.helper.ActionBarHelper;

public class MainActivity extends Activity implements ActionBar.TabListener {


	// Static string for debug tags
	private static final String DEBUG_TAG = "Broadband Usage";
	//private static final String INFO_TAG = MainActivity.class.getSimpleName();

	ActionBarHelper  mActionBarHelper;
	private ViewPager mPager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	   super.onCreate(savedInstanceState);
    	   setContentView(R.layout.activity_main);
    	   
    	   //setUpActionBar();
    	   
    	   // Get action bar instance
    	   ActionBar mActionBar = getActionBar();
    	   
    	   // Load action bar
    	   mActionBarHelper = new ActionBarHelper(this, mActionBar);
    	   
    	   // Load viewpager
    	   MyPagerAdapter mAdapter = new MyPagerAdapter();
    	   mPager = (ViewPager) findViewById(R.id.pager);
    	   mPager.setAdapter(mAdapter);
    	   
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
    	
    }

    @Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	
    
    }
    
    private class MyPagerAdapter extends PagerAdapter {

        public int getCount() {
                return 3;
        }

        public Object instantiateItem(View collection, int position) {

                LayoutInflater inflater = (LayoutInflater) collection.getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                int resId = 0;
                switch (position) {
                case 0:
                        resId = R.layout.current;
                        break;
                case 1:
                        resId = R.layout.anaylsis;
                        break;
                case 2:
                        resId = R.layout.data;
                        break;
                }

                View view = inflater.inflate(resId, null);

                ((ViewPager) collection).addView(view, 0);

                return view;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView((View) arg2);

        }

        @Override
        public void finishUpdate(View arg0) {
                // TODO Auto-generated method stub

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == ((View) arg1);

        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
                // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        public void startUpdate(View arg0) {
                // TODO Auto-generated method stub

        }

}
    
 
}