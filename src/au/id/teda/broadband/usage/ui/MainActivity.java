package au.id.teda.broadband.usage.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.ui.fragments.FragmentLogInDialog;
import au.id.teda.broadband.usage.ui.fragments.FragmentLogInDialog.FragmentLogInListner;

public class MainActivity extends SherlockFragmentActivity implements FragmentLogInListner {
	
	private static final String DEBUG_TAG = "bbusage";
	
	FragmentManager mFragmentManager;
	FragmentLogInDialog fragmentLogInDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar mActionBar = getSupportActionBar();
        // Set action bar icon for navigation
        mActionBar.setDisplayHomeAsUpEnabled(true);
        
        // Load instance of fragments
        mFragmentManager = getSupportFragmentManager();
        fragmentLogInDialog = new FragmentLogInDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    
	public void onLogInClick(View button) {
        fragmentLogInDialog.show(mFragmentManager, "fragment_log_in");
    }
    
	@Override
	public void onFinishLogInListner(String username, String password) {
		Toast.makeText(this, "Hi, " + username + " with Password: " + password, Toast.LENGTH_SHORT).show();
	}
	
	public void onShowPasswordCheckBoxClick (View view){
		//fragmentLogInDialog.onShowPasswordCheckBoxClickMethod(view);
		Log.d(DEBUG_TAG, "onShowPasswordCheckBoxClick (" + view + " )");
	}

}