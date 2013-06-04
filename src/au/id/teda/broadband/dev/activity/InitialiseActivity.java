package au.id.teda.broadband.dev.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import au.id.teda.broadband.dev.R;
import au.id.teda.broadband.dev.helper.ConnectivityHelper;
import au.id.teda.broadband.dev.util.FontUtils;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Created by ian on 4/06/13.
 */
public class InitialiseActivity extends SherlockFragmentActivity {

    // Broadcast receiver objects
    private SyncReceiver mSyncReceiver;
    private IntentFilter filter;

    private ProgressBar mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_initialise);

        // Set font to Roboto on SDK < 11
        if (Build.VERSION.SDK_INT < 11) {
            ViewGroup godfatherView = (ViewGroup) this.getWindow().getDecorView();
            FontUtils.setRobotoFont(this, godfatherView);
        }

        getSupportActionBar().hide();


        // Setup broadcast receiver for background sync, with broadcast filter
        String BROADCAST = getString(R.string.sync_broadcast_action);
        filter = new IntentFilter(BROADCAST);
        mSyncReceiver = new SyncReceiver();

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
                Intent mi = new Intent(getBaseContext(), MainActivity.class);
                startActivity(mi);
            } else if (msg.equals(SYNC_START)){
                // Nothing to see here
            }
        }

    }


}
