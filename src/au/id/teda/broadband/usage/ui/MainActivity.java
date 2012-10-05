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
import au.id.teda.broadband.usage.adapter.ActionbarSpinnerAdapter;
import au.id.teda.broadband.usage.adapter.SectionPagerAdapter;
import au.id.teda.broadband.usage.listener.NavigationListener;
import au.id.teda.broadband.usage.ui.MonthListFragment.MonthListSelectedListner;
import au.id.teda.broadband.usage.ui.phone.AnalysisActivity;

public class MainActivity extends FragmentActivity {
	
	// Testing
	
	private static final String DEBUG_TAG = "bbusage";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar mActionBar = getActionBar();
        // Set action bar icon for navigation
        mActionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

}