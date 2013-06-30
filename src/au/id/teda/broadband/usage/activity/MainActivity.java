package au.id.teda.broadband.usage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.authenticator.AuthenticatorActivity;
import au.id.teda.broadband.usage.fragments.DataTableAnytimeFragment;
import au.id.teda.broadband.usage.fragments.StackedBarChartFragment;
import au.id.teda.broadband.usage.fragments.StackedLineChartFragment;
import au.id.teda.broadband.usage.parser.AccountStatusParser;
import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends BaseActivity {

    private MainActivityPagerAdapter mAdapter;
    private ViewPager mPager;
    private LinePageIndicator mIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check to see if account has been authenticated
        AccountAuthenticator mAccountAuthenticator = new AccountAuthenticator(this);
        if(!mAccountAuthenticator.isAccountAuthenticated()){
        	Intent authenticator = new Intent(this, AuthenticatorActivity.class);
    		startActivity(authenticator);
        }
        
       	// Don't show up button on home page action bar
       	getSupportActionBar().setHomeButtonEnabled(false);
       	// Set action bar title different to manifest label
       	getSupportActionBar().setTitle(R.string.action_bar_title);

        // Check if account is an anytime account and load layout
        if (mAccountInfo.isAccountAnyTime()){
            setContentView(R.layout.activity_main_anytime);
            // Log.d(DEBUG_TAG, "activity_main_anytime");
        } else {
            setContentView(R.layout.activity_main);
            // Log.d(DEBUG_TAG, "activity_main");
        }

        mAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.activity_main_pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (LinePageIndicator)findViewById(R.id.activity_main_pager_indicator);
        mIndicator.setViewPager(mPager);

    }

    class MainActivityPagerAdapter extends FragmentPagerAdapter {

        public MainActivityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new DataTableAnytimeFragment();
                case 1:
                    return new StackedBarChartFragment();
                case 2:
                    return new StackedLineChartFragment();
                default:
                    throw new IllegalArgumentException("not this many fragments: " + position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

}