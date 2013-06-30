package au.id.teda.broadband.usage.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.authenticator.AuthenticatorActivity;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.helper.ConnectivityHelper;
import au.id.teda.broadband.usage.util.FontUtils;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Created by ian on 4/06/13.
 *
 *  Initialisation app so we don't show peak/offpeak layout with anytime accounts
 *  on MainActivity load.
 */
public class InitialiseActivity extends SherlockFragmentActivity {

    // Broadcast receiver objects
    private SyncReceiver mSyncReceiver;
    private IntentFilter filter;

    private ProgressBar mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout to load
        setContentView(R.layout.activity_initialise);

        // Set font to Roboto on SDK < 11
        if (Build.VERSION.SDK_INT < 11) {
            ViewGroup godfatherView = (ViewGroup) this.getWindow().getDecorView();
            FontUtils.setRobotoFont(this, godfatherView);
        }

        // Set action bar title
        getSupportActionBar().setTitle(getString(R.string.action_bar_title_initialise));


        // Setup broadcast receiver for background sync, with broadcast filter
        String BROADCAST = getString(R.string.sync_broadcast_action);
        filter = new IntentFilter(BROADCAST);
        mSyncReceiver = new SyncReceiver();

        // Setup indeterminate progress bar
        mProgress = (ProgressBar) findViewById(R.id.activity_initialise_progressBar);
        mProgress.isIndeterminate();
        mProgress.setVisibility(View.VISIBLE);

        //TODO: Will this trigger two syncs on first load?
        ConnectivityHelper mNetwork = new ConnectivityHelper(this);
        mNetwork.requestSync();

    }

    // Called 3rd in the activity life cycle
    @Override
    protected void onResume(){
        super.onResume();

        // Register sync reciever
        registerReceiver(mSyncReceiver, filter);

        // Check to see if info and status has been set
        AccountInfoHelper mInfo = new AccountInfoHelper(this);
        AccountStatusHelper mStatus = new AccountStatusHelper(this);
        // Make sure we don't get stuck if info and status is set
        if(mInfo.isInfoSet() && mStatus.isStatusSet()){
            startMainActivity();
        }
    }

    // Called 2nd during activity destruction
    @Override
    protected void onPause() {
        super.onPause();

        // Unregister broadcast receiver
        unregisterReceiver(mSyncReceiver);
    }

    /**
     * Broadcast reciever class to listen for background data syncs
     * @author iteda
     *
     */
    private class SyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent i) {

            String MESSAGE = getString(R.string.sync_broadcast_message);
            String SYNC_START = getString(R.string.sync_broadcast_start);
            String SYNC_COMPLETE = getString(R.string.sync_broadcast_complete);

            String msg = i.getStringExtra(MESSAGE);
            if (msg.equals(SYNC_COMPLETE)){
                // Start MainActivity after sync complete
                startMainActivity();
            } else if (msg.equals(SYNC_START)){
                // Nothing to see here
            }
        }

    }

    private void startMainActivity() {
        Intent mi = new Intent(getBaseContext(), MainActivity.class);
        startActivity(mi);
    }


}
