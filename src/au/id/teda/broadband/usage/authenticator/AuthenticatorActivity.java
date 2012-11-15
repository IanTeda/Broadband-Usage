package au.id.teda.broadband.usage.authenticator;


import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.parser.ErrorParser;
import au.id.teda.broadband.usage.util.DownloadVolumeUsage;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
	
	public static final String PARAM_AUTHTOKEN_TYPE = "auth.token";  
    public static final String PARAM_CREATE = "create";  
  
    public static final int REQ_CODE_CREATE = 1;  
  
    public static final int REQ_CODE_UPDATE = 2;  
  
    public static final String EXTRA_REQUEST_CODE = "req.code";  
  
    public static final int RESP_CODE_SUCCESS = 0;  
  
    public static final int RESP_CODE_ERROR = 1;  
  
    public static final int RESP_CODE_CANCEL = 2;  
	
	@Override  
	protected void onCreate(Bundle icicle) {  
		super.onCreate(icicle);  
		this.setContentView(R.layout.login_activity);  
	}
	
	public void onCancelClick(View v) {  
		this.finish();  
	} 
	
	public void onSaveClick(View v) {  
        TextView tvUsername;  
        TextView tvPassword;  
        String username;  
        String password;  
        boolean hasErrors = false;  
  
        tvUsername = (TextView) this.findViewById(R.id.username_edit);  
        tvPassword = (TextView) this.findViewById(R.id.password_edit);  
  
        tvUsername.setBackgroundColor(Color.WHITE);  
        tvPassword.setBackgroundColor(Color.WHITE);  
  
        username = tvUsername.getText().toString();  
        password = tvPassword.getText().toString();  
  
        if (username.length() < 3) {  
            hasErrors = true;  
            tvUsername.setBackgroundColor(Color.MAGENTA);  
        }  
        if (password.length() < 3) {  
            hasErrors = true;  
            tvPassword.setBackgroundColor(Color.MAGENTA);  
        }  
  
        if (hasErrors) {  
            return;  
        }  
          
        // Now that we have done some simple "client side" validation it  
        // is time to check with the server  
  
        // ... perform some network activity here  
  
        // finished  
  
        String accountType = this.getIntent().getStringExtra(PARAM_AUTHTOKEN_TYPE);  
        if (accountType == null) {  
            accountType = AccountAuthenticator.ACCOUNT_TYPE;  
        }  
  
        AccountManager accMgr = AccountManager.get(this);  
  
        if (hasErrors) {  
  
            // Handle errors  
  
        } else {  
  
            // This is the magic that addes the account to the Android Account Manager  
            final Account account = new Account(username, accountType);  
            accMgr.addAccountExplicitly(account, password, null);  
  
            // Now we tell our caller, could be the Andreoid Account Manager or even our own application  
            // that the process was successful  
  
            final Intent intent = new Intent();  
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);  
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);  
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, accountType);  
            this.setAccountAuthenticatorResult(intent.getExtras());  
            this.setResult(RESULT_OK, intent);  
            this.finish();  
  
        }  
    } 
	 
}
