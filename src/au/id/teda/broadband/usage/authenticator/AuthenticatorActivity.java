package au.id.teda.broadband.usage.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
    private Dialog mDialog;
    
    /** Account manager object **/
    private AccountManager mAccountManager;
    private String accountType;
    
    /** Download manager **/
    NetworkUtilities mAccount;
    
    private TextView mMessage;
    private String mPassword;
    private EditText mPasswordEdit;
    private String mUsername;
    private EditText mUsernameEdit;
    private CheckBox mShowPasswordCbox;
    
	@Override  
	protected void onCreate(Bundle icicle) {  
		super.onCreate(icicle);  
		this.setContentView(R.layout.activity_authenticator);
        
        mMessage = (TextView) findViewById(R.id.message_tv);
        mUsernameEdit = (EditText) findViewById(R.id.username_et);
        mPasswordEdit = (EditText) findViewById(R.id.password_et);
        mShowPasswordCbox = (CheckBox) findViewById(R.id.show_password_cbox);
        
        mAccount = new NetworkUtilities(this);
        mAccountManager = AccountManager.get(this);
        
        accountType = AccountAuthenticator.ACCOUNT_TYPE;
        
        setUsernamePasswordEditText();
        
	}

	/**
	 * Set username and password into edit text if set
	 */
	private void setUsernamePasswordEditText() {
		// Get account type and then retrieve accounts
		
        Account[] accounts = mAccountManager.getAccountsByType(accountType);
        
        // Get username and password for account
        String accountName = "";
        String accountPass = "";
        for (Account account : accounts) {
        	accountName = account.name;
        	accountPass = mAccountManager.getPassword(account);
        }
        
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
            //setDrawableWarning();
        } else {
            // Kick off a background task to perform the user login attempt.
        	if (mAccount.is3gOrWifiConnected()){
        		
        		// Set up dialog before task
        		mDialog = new Dialog(this);
        		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        		mDialog.setContentView(R.layout.progress_bar_spinner_custom);
        		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        		// Start async task
        		mAuthTask = new UserLoginTask();
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
    	// Our task is complete, so clear it out
        mAuthTask = null;
    	
    	// This is the magic that addes the account to the Android Account Manager  
    	final Account account = new Account(mUsername, accountType);
    	mAccountManager.addAccountExplicitly(account, mPassword, null);
    	ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
    	
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

    /**
     * This class represents an asynchronous task used to authenticate a user against XML parser
     * @author iteda
     *
     */
    public class UserLoginTask extends AsyncTask<Void, Boolean, Boolean> {
    	
    	/** Complete before we execute task **/
    	protected void onPreExecute(){
    		// Show progress dialog before executing task
    		mDialog.show();
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
        	
        	// Dismiss progress dialog if showing
        	if (mDialog.isShowing()) {
        		mDialog.dismiss();
        	}
        	
        	if (userPassCheck){
        		addAccount();
        	} else {
        		mMessage.setText(R.string.authenticator_activity_failure);
        		toaster(getString(R.string.authenticator_activity_failure));
        	}
        }

        @Override
        protected void onCancelled() {
        	// Nothing to see here
        }
    }
	
}
