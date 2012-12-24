package au.id.teda.broadband.usage.authenticator;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import au.id.teda.broadband.usage.R;

public class AccountPreferences extends PreferenceActivity {
	
	  @Override
	    public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        addPreferencesFromResource(R.xml.preferences_resources);
	    }

}
