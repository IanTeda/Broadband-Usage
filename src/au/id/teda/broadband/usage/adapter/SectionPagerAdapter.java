package au.id.teda.broadband.usage.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import au.id.teda.broadband.usage.R;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
 * sections of the app.
 */
public class SectionPagerAdapter extends FragmentPagerAdapter {

	private final Context mContext;

	public SectionPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.mContext = context;
	}

	@Override
    public Fragment getItem(int i) {
		return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return mContext.getString(R.string.action_bar_tab_current).toUpperCase();
            case 1: return mContext.getString(R.string.action_bar_tab_analysis).toUpperCase();
            case 2: return mContext.getString(R.string.action_bar_tab_data_table).toUpperCase();
        }
        return null;
    }

}