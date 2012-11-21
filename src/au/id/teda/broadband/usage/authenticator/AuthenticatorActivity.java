package au.id.teda.broadband.usage.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.util.DownloadVolumeUsage;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
	
	/** Debug tag **/
	private static final String DEBUG_TAG = "bbusage";
	
	/** The Intent flag to confirm credentials. */
    public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";

    /** The Intent extra to store password. */
    public static final String PARAM_PASSWORD = "password";

    /** The Intent extra to store username. */
    public static final String PARAM_USERNAME = "username";
    
    /** Check if it credentials are set so we don't overright **/
    private Boolean mConfirmCredentials = false;

    /** Keep track of the login task so can cancel it if requested */
    private UserLoginTask mAuthTask = null;
    
    /** Keep track of the progress dialog so we can dismiss it */
    private ProgressDialog mProgressDialog;
    private Dialog mDialog;
    
    /** Account manager object **/
    private AccountManager mAccountManager;
    
    /** Download manager **/
    DownloadVolumeUsage mAccount;
    
    private TextView mMessage;
    private String mPassword;
    private EditText mPasswordEdit;
    private String mUsername;
    private EditText mUsernameEdit;
    
	
	@Override  
	protected void onCreate(Bundle icicle) {  
		super.onCreate(icicle);  
		this.setContentView(R.layout.authenticator_activity);
        
        mMessage = (TextView) findViewById(R.id.message_tv);
        mUsernameEdit = (EditText) findViewById(R.id.username_et);
        mPasswordEdit = (EditText) findViewById(R.id.password_et);
        
        mAccount = new DownloadVolumeUsage(this);
	}
	
    /**
     * Handles onClick event on the Submit button. Sends username/password to
     * the server for authentication. The button is configured to call
     * handleLogin() in the layout XML.
     *
     * @param view The Submit button for which this method is invoked
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
        	if (mAccount.isConnected()){
        		
        		// Set up dialog before task
        		mDialog = new Dialog(this);
        		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        		mDialog.setContentView(R.layout.progress_bar_spinner_custom);
        		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        		// Start async task
        		mAuthTask = new UserLoginTask();
	            mAuthTask.execute();
        	} else {
        		//TODO: Update to alert dialog with option for 3g check
        		//Toast.makeText(this, R.string.authenticator_activity_no_connectivity, Toast.LENGTH_SHORT).show();
        		Drawable warningImg = this.getResources().getDrawable(R.drawable.ic_warning);
        		warningImg.setBounds( 0, 0, 22, 22 );
        		mMessage.setCompoundDrawables( warningImg, null, null, null );
        		mMessage.setText(R.string.authenticator_activity_no_connectivity);
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
            final CharSequence msg = getText(R.string.login_activity_newaccount_text);
            return msg;
        }
        if (TextUtils.isEmpty(mPassword)) {
            // We have an account but no password
            return getText(R.string.login_activity_loginfail_text_pwmissing);
        }
        return null;
    }
    
    
    private void setDrawableError(){
    	// Add drawableleft error cross
		Drawable errorImg = this.getResources().getDrawable(R.drawable.ic_error);
		errorImg.setBounds( 0, 0, 22, 22 );
		mMessage.setCompoundDrawables( errorImg, null, null, null );
    }
    
    private void addAccount(String username, String password){
    	
    	String accountType = Authenticator.ACCOUNT_TYPE;
    	
    	// This is the magic that addes the account to the Android Account Manager  
    	final Account account = new Account(username, accountType);
    	
    	/**
        if (mRequestNewAccount) {
            mAccountManager.addAccountExplicitly(account, mPassword, null);
            // Set contacts sync for this account.
            ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
        } else {
            mAccountManager.setPassword(account, mPassword);
        }
        **/
    	
    	mAccountManager.addAccountExplicitly(account, password, null);  
  
    	// Now we tell our caller, could be the Android Account Manager or even our own application  
    	// that the process was successful  
  
    	final Intent intent = new Intent();  
    	intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);  
    	intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
    	this.setAccountAuthenticatorResult(intent.getExtras());  
    	this.setResult(RESULT_OK, intent);  
    	this.finish();  
  
	}  

    /**
     * This class represents an asynchronous task used to authenticate a user against XML parser
     * @author iteda
     *
     */
    public class UserLoginTask extends AsyncTask<Void, Boolean, Boolean> {
    	
    	
    	protected void onPreExecute(){
    		// Show progress dialog before executing task
    		mDialog.show();
    	}
    	

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
        	Log.d(DEBUG_TAG, "UserLoginTask.onPostExecute: " + userPassCheck);
        	
        	// Dismiss progress dialog if showing
        	if (mDialog.isShowing()) {
        		mDialog.dismiss();
        	}
        	
        	if (userPassCheck){
        		Log.d(DEBUG_TAG, "UserLoginTask.onPostExecute: " + mUsername + " / " + mPassword);
        		addAccount(mUsername, mPassword);
        	} else {
        		mMessage.setText(R.string.authenticator_activity_failure);
        		setDrawableError();
        		
        	}
        }

        @Override
        protected void onCancelled() {
        	Log.d(DEBUG_TAG, "UserLoginTask.onCancelled");
        }
    }
	
}
