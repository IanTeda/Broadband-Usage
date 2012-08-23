package au.id.teda.broadband.usage.adapter;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.MainActivity.DummySectionFragment;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
 * sections of the app.
 */
public class SectionPagerAdapter extends FragmentPagerAdapter {

	public SectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
    public Fragment getItem(int i) {
        Fragment fragment = new DummySectionFragment();
        Bundle args = new Bundle();
        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return Resources.getSystem().getString(R.string.action_bar_tab_current).toUpperCase();
            case 1: return Resources.getSystem().getString(R.string.action_bar_tab_analysis).toUpperCase();
            case 2: return Resources.getSystem().getString(R.string.action_bar_tab_data_table).toUpperCase();
        }
        return null;
    }

}
