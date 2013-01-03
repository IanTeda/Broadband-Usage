package au.id.teda.broadband.usage.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.syncadapter.DummyContentProvider;
import au.id.teda.broadband.usage.ui.SettingsActivity;
import au.id.teda.broadband.usage.util.NetworkUtilities;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
	
	/** Debug tag **/
	private static final String DEBUG_TAG = "bbusage";
    
    /** The Intent flag to confirm credentials. */
    public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";
    
    /** The Intent extra to store password. */
    public static final String PARAM_PASSWORD = "password";

    /** The Intent extra to store username. */
    public static final String PARAM_USERNAME = "username";

    /** Keep track of the login task so can cancel it if requested */
    private UserLoginTask mAuthTask = null;
    
    /** Keep track of the progress dialog so we can dismiss it */
    private Dialog mDialog = null;
    
    private boolean mDialogShowing;
    
    /** Account manager object **/
    private static AccountManager mAccountManager;
    private static String accountType;
    private static AccountAuthenticator mAccountAuthenticator;
    
    /** Download manager **/
    static NetworkUtilities mAccount;
    
    private static TextView mMessage;
    private static String mPassword;
    private EditText mPasswordEdit;
    private static String mUsername;
    private EditText mUsernameEdit;
    private CheckBox mShowPasswordCbox;
    
	@Override  
	protected void onCreate(Bundle icicle) {  
		super.onCreate(icicle);  
		setContentView(R.layout.activity_authenticator);

        /** Set up account instance **/
		mAccountAuthenticator = new AccountAuthenticator(this);
        mAccount = new NetworkUtilities(this);
        mAccountManager = AccountManager.get(this);
        accountType = AccountAuthenticator.ACCOUNT_TYPE;

		/** Set up activity view **/
        mMessage = (TextView) findViewById(R.id.message_tv);
        mUsernameEdit = (EditText) findViewById(R.id.username_et);
        mPasswordEdit = (EditText) findViewById(R.id.password_et);
        mShowPasswordCbox = (CheckBox) findViewById(R.id.show_password_cbox);
        setUsernamePasswordEditText();
        
        /** Check if there has been a screen orientation change **/
		Object retained = getLastNonConfigurationInstance(); 
        if ( retained instanceof UserLoginTask ) { 
                // Associate orientation new activity with async task 
                mAuthTask = (UserLoginTask) retained; 
                mAuthTask.setActivity(this);
                // Show dialog again
                showMyDialog();
        }
       
	}
	
	/** After a screen orientation change, this method is invoked. **/
    @Override
    public Object onRetainNonConfigurationInstance() {
    	// Using state save task so destroy old task
    	if (mAuthTask != null){
    		mAuthTask.detach();
    	}
    	return(mAuthTask);
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
	}
    
    protected void onPause(){
    	super.onPause();
    	// Dismiss dialog so we don't get window leaks
    	dismissMyDialog();
    }
    
    /** Activity method called once asyncTask complete **/
    private void onTaskCompleted(boolean userPassCheck) { 
    	
    	// Tidy up task
    	mAuthTask = null;
    	
        dismissMyDialog();
        
    	if (userPassCheck){
    		addAccount();
    	} else {
    		showAuthenticatinFailure();
    	}
    }
    
    
    private void showMyDialog(){
    	// Set up dialog
		mDialog = new Dialog(this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_indeterminate_progress_spinner);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		mDialog.show();
		
		// Set display boolean to true
		mDialogShowing = true;
    }
    
    private void dismissMyDialog(){
    	if (mDialogShowing) {
    		mDialog.dismiss();
    		mDialogShowing = false;
    	}
    }

	/**
	 * Set username and password into edit text if set
	 */
	private void setUsernamePasswordEditText() {
        // Get username and password for account
        String accountName = mAccountAuthenticator.getUsername();
        String accountPass = mAccountAuthenticator.getPassword();

        // If we get something set edit text values
        if (accountName.length()>0 && accountPass.length() >0){
        	mUsernameEdit.setText(accountName);
        	mPasswordEdit.setText(accountPass);
        	mMessage.setText(R.string.authenticator_activity_account_set);
        }
	}
	
	/**
	 * On click event for button
	 * @param view
	 */
    public void onClickAddAccount(View view) {
    	
    	// Get username and password from edit_text's
        mUsername = mUsernameEdit.getText().toString();
        mPassword = mPasswordEdit.getText().toString();
        
        // Check if we like what was put into the username and password et's
        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
            mMessage.setText(getMessage());
        } else {
        	
            // Kick off a background task to perform the user login attempt.
        	if (mAccount.is3gOrWifiConnected()){
        		// Start async task
        		mAuthTask = new UserLoginTask(this);
	            mAuthTask.execute();
	            
        	} else {
        		// Set message text to connection error
        		mMessage.setText(R.string.authenticator_activity_no_connectivity);
        		toaster(getString(R.string.authenticator_activity_no_connectivity));
        	}
        }
    }

    
    /**
     * Returns the message to be displayed at the top of the login dialog box.
     */
    private CharSequence getMessage() {
        if (TextUtils.isEmpty(mUsername)) {
            // If no username, then we ask the user to log in using an
            // appropriate service.
            return getText(R.string.authenticator_activity_no_username);
        }
        if (TextUtils.isEmpty(mPassword)) {
            // We have an account but no password
            return getText(R.string.authenticator_activity_no_password);
        }
        return null;
    }
    
    /**
     * Add account to manager
     */
    private void addAccount(){
    	// This is the magic that adds the account to the Android Account Manager  
    	final Account account = new Account(mUsername, accountType);
    	mAccountManager.addAccountExplicitly(account, mPassword, null);
    	// Account is Syncable
    	ContentResolver.setIsSyncable(account, DummyContentProvider.PROVIDER, 1);
    	// Sync account automatically
    	ContentResolver.setSyncAutomatically(account, DummyContentProvider.PROVIDER, true);
    	// Sync every day as default
    	ContentResolver.addPeriodicSync(account, DummyContentProvider.PROVIDER, new Bundle(), SettingsActivity.TWENTY_FOUR_HOURS);
    	// Complete first sync
    	ContentResolver.requestSync (account, DummyContentProvider.PROVIDER, new Bundle());
    	
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        
        finish();
	}
    
    /**
     * On click method for showing or hiding password
     * @param view
     */
	public void onClickShowPassword(View view) {
		if (mShowPasswordCbox.isChecked()) {
			// Show password if check box checked
			mPasswordEdit.setTransformationMethod(null);
			// Move to cursor to end of edit text
			mPasswordEdit.setSelection(mPasswordEdit.getText().length());
		} else {
			// Else hide password
			mPasswordEdit.setTransformationMethod(new PasswordTransformationMethod());
			// Move to cursor to end of edit text
			mPasswordEdit.setSelection(mPasswordEdit.getText().length());
		}
	}
	
	/**
	 * Show a toast method
	 * @param message
	 */
	private void toaster(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	private void showAuthenticatinFailure() {
		mMessage.setText(R.string.authenticator_activity_failure);
		toaster(getString(R.string.authenticator_activity_failure));
	}

    /**
     * This class represents an asynchronous task used to authenticate a user against XML parser
     * @author iteda
     *
     */
    static class UserLoginTask extends AsyncTask<Void, Boolean, Boolean> {
    	
    	private AuthenticatorActivity activity;
    	private boolean completed;
    	
    	UserLoginTask(AuthenticatorActivity activity){
    		this.activity = activity;
    	}
    	
    	/** Complete before we execute task **/
    	protected void onPreExecute(){
    		// Show progress in UI thread dialog before executing task
    		activity.showMyDialog();
    	}
    	
    	/** Task to be complete **/
        @Override
        protected Boolean doInBackground(Void... params) {
            // We do the actual work of authenticating the user in DownloadVolumeUsage class.
            try {
            	return mAccount.authenticate(mUsername, mPassword);
            } catch (Exception ex) {
                Log.e(DEBUG_TAG, "UserLoginTask.doInBackground: failed to authenticate");
                Log.i(DEBUG_TAG, ex.toString());
                return false;
            }
        	
        }

        @Override
        protected void onPostExecute(Boolean userPassCheck) {
        	completed = true;
        	notifyActivityTaskCompleted(userPassCheck);
        }

        @Override
        protected void onCancelled() {
        	// Nothing to see here
        }
        
        void detach(){
        	activity = null;
        }
        
        
        
        void setActivity(AuthenticatorActivity activity) {
          this.activity = activity;
          if (completed){
        	  notifyActivityTaskCompleted(false); 
          }
        }
        
        /**
         * Helper method to notify the activity that this task was completed.
         * @param userPassCheck 
         */
        private void notifyActivityTaskCompleted(Boolean userPassCheck) { 
                if ( null != activity ) { 
                	activity.onTaskCompleted(userPassCheck); 
                } 
        }         
    }
	
}
