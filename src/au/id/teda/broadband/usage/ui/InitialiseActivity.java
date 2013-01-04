package au.id.teda.broadband.usage.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.syncadapter.SyncAdapter;
import au.id.teda.broadband.usage.util.NetworkUtilities;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class InitialiseActivity extends SherlockFragmentActivity {
	
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
    
    private static final String PREF_INITIALISEd_KEY = "pref_initialise_key";
    
    private static ProgressBar mProgressBar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialise);
        
        mProgressBar = (ProgressBar) findViewById(R.id.activity_initialise_progress);
        
        SyncAdapter mSyncAdapter = new SyncAdapter(this, false);
		mSyncAdapter.requestSync(handler);

    }
    
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	switch (msg.what) {
        	case NetworkUtilities.HANDLER_START_ASYNC_TASK:
        		Log.d(DEBUG_TAG, "InitialiseActivity.HANDLER_START_ASYNC_TASK");
        		startProgressBar();
        		break;
        	case NetworkUtilities.HANDLER_COMPLETE_ASYNC_TASK:
        		Log.d(DEBUG_TAG, "InitialiseActivity.HANDLER_COMPLETE_ASYNC_TASK");
        		setInitialised(true);
        		finish();
        		break;
        	}
        }
    };
    
    private void setInitialised(boolean flag){
    	SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(this);
    	SharedPreferences.Editor mEditor = mSettings.edit();
    	mEditor.putBoolean(PREF_INITIALISEd_KEY, flag);
    	mEditor.commit();
    }
    
    private void startProgressBar(){
    	mProgressBar.setIndeterminate(true);
    }
    
    private void completeProgressBar(){
    	mProgressBar.setVisibility(View.GONE);
    }

}
