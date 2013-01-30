package au.id.teda.broadband.usage.ui;

import android.os.Bundle;
import au.id.teda.broadband.usage.R;

public class PeakUsageActivity extends BaseActivity {
	
	/**
	 * Activity holder for PeakUsageFragment info fragment
	 * @author Ian Teda
	 *
	 */
	public class AccountInfoActivity extends BaseActivity {
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_peak_usage);
		}
	}

}
