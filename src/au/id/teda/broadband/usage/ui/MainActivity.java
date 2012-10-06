package au.id.teda.broadband.usage.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.fragments.FragmentLogIn;
import au.id.teda.broadband.usage.ui.fragments.FragmentLogIn.FragmentLogInListner;

public class MainActivity extends Activity implements FragmentLogInListner {
	
	//private static final String DEBUG_TAG = "bbusage";
	
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
    
    @SuppressWarnings("unused")
	public void onLogInClick(View button) {
        FragmentManager fm = getFragmentManager();
        FragmentLogIn fragmentLogInDialog = new FragmentLogIn();
        fragmentLogInDialog.show(fm, "fragment_log_in");
    }
    
	@Override
	public void onFinishLogInListner(String inputText) {
		Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
	}

}