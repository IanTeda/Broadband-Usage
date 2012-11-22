package au.id.teda.broadband.usage.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.util.DownloadVolumeUsage;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
	
	/** Debug tag **/
	private static final String DEBUG_TAG = "bbusage";
	
    /** Check if it credentials are set so we don't overright **/
    private Boolean mConfirmCredentials = false;

    /** Keep track of the login task so can cancel it if requested */
    private UserLoginTask mAuthTask = null;
    
    /** Keep track of the progress dialog so we can dismiss it */
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
        mAccountManager = AccountManager.get(this);
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
    
    private void addAccount(){
    	// Our task is complete, so clear it out
        mAuthTask = null;
    	
    	// This is the magic that addes the account to the Android Account Manager  
    	final Account account = new Account(mUsername, Authenticator.ACCOUNT_TYPE);
    	mAccountManager.addAccountExplicitly(account, mPassword, null);  
  
	}
    
	public void onClickShowPassword(View view) {
		if (((CheckBox) view).isChecked()) {
			// Show password if check box checked
			mPasswordEdit.setTransformationMethod(null);
		} else {
			// Else hide password
			mPasswordEdit.setTransformationMethod(new PasswordTransformationMethod());
		}
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
        	
        	// Dismiss progress dialog if showing
        	if (mDialog.isShowing()) {
        		mDialog.dismiss();
        	}
        	
        	if (userPassCheck){
        		addAccount();
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
