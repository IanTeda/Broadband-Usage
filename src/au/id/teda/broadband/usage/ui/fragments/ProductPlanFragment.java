package au.id.teda.broadband.usage.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.ui.MainActivity;
import au.id.teda.broadband.usage.ui.fragments.DataSummaryFragment.SyncReceiver;

import com.actionbarsherlock.app.SherlockFragment;

public class ProductPlanFragment extends SherlockFragment {
	
	// Debug tag pulled from main activity
	private final static String DEBUG_TAG = MainActivity.DEBUG_TAG;
	
	// View inflated by fragment
	private View mFragmentView;
	
	// Helper classes
	private AccountInfoHelper mAccountInfo;
	private AccountStatusHelper mAccountStatus;
	private AccountAuthenticator mAccountAuthenticator;
	
	// Recieve sync broadcasts
	private SyncReceiver mSyncReceiver;
    private IntentFilter filter;
	
    // Activity context to be used
	private Context mContext;
	
	/**
	 * Called 1st in the fragment life cycle
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Load helper classes
		mAccountInfo = new AccountInfoHelper(activity);
		mAccountStatus = new AccountStatusHelper(activity);
		mAccountAuthenticator = new AccountAuthenticator(activity);
	}
	
	/**
	 * Called 2nd in the fragment life cycle
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set context for fragment. 
		// Activity extends context so we get it from there
		mContext = getActivity();
		
		// Setup broadcast receiver for background sync
        String BROADCAST = getString(R.string.sync_broadcast_action);
        filter = new IntentFilter(BROADCAST);
        mSyncReceiver = new SyncReceiver();
	}
	
	/**
	 * Called 3rd in the fragment life cycle
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set fragment layout to be inflated
		mFragmentView = inflater.inflate(R.layout.fragment_product_plan, container, false);
		
		return mFragmentView;
	}
	
	/**
	 * Called 4th in the fragment life cycle
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		loadFragmentView();
	}
	
	/**
	 * Called 5th in the fragment life cycle
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		// Register broadcast receiver for background sync
		getActivity().registerReceiver(mSyncReceiver, filter);
	}
	
	/**
	 * First call in the death of fragment
	 */
	@Override
	public void onPause() {
		super.onPause();
		
		// Unregister broadcast receiver for background sync
		getActivity().unregisterReceiver(mSyncReceiver);
	}
	
	private void loadFragmentView(){
		if (mAccountInfo.isInfoSet() 
    			&& mAccountStatus.isStatusSet()){
			
	    	TextView mUsernameTV = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_username_tv);
	    	TextView mProductPlanTV = (TextView) mFragmentView.findViewById(R.id.fragment_product_plan_product_plan_tv);
	    	
	    	mUsernameTV.setText(mAccountAuthenticator.getUsername());
	    	mProductPlanTV.setText(mAccountInfo.getProductPlan());
		}
	}
	
	public class SyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent i) {
            
            String MESSAGE = getString(R.string.sync_broadcast_message);
            String SYNC_START = getString(R.string.sync_broadcast_start);
            String SYNC_COMPLETE = getString(R.string.sync_broadcast_complete);
            
            String msg = i.getStringExtra(MESSAGE);
            if (msg.equals(SYNC_START)){
            	// Nothing to do see here move along
            } else if (msg.equals(SYNC_COMPLETE)){
            	loadFragmentView();
            }
        }
         
    }

}