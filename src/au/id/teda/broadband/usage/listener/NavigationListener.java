package au.id.teda.broadband.usage.listener;

import android.app.ActionBar.OnNavigationListener;
import android.util.Log;

public class NavigationListener implements OnNavigationListener {
	
	private static final String DEBUG_TAG = "bbusage";

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		
		Log.v(DEBUG_TAG, "Spinner selected");
		
		return false;
	}

}
