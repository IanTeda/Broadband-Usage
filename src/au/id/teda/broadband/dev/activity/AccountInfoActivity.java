package au.id.teda.broadband.dev.activity;

import android.os.Bundle;
import au.id.teda.broadband.dev.R;

/**
 * Activity holder for account info fragment
 * @author Ian Teda
 *
 */
public class AccountInfoActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_account_info);
		
		// Show home (up) button on action bar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
