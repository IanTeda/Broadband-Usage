package au.id.teda.broadband.usage.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AccountAuthenticator extends AbstractAccountAuthenticator {
	
	private static final String DEBUG_TAG = "bbusage";
	
	/** Account type String. This needs to match authenticator.xml type **/
    public static final String ACCOUNT_TYPE = "au.net.iinet.account";
    
    private static AccountManager mAccountManager;

	// Activity context
    private static Context mContext;
    
	public AccountAuthenticator(Context context) {
		super(context);
		AccountAuthenticator.mContext = context;
		
		mAccountManager = AccountManager.get(mContext);
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
            String authTokenType, String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {

		// TODO: delete?
		
		final Bundle result = new Bundle();
		final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        result.putParcelable(AccountManager.KEY_INTENT, intent);
        return result;
	}

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) {
        Log.d(DEBUG_TAG, "confirmCredentials()");
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        Log.d(DEBUG_TAG, "editProperties()");
        return null;
    }

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		Log.d(DEBUG_TAG, "getAuthToken()");
		return null;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		Log.d(DEBUG_TAG, "getAuthTokenLabel()");
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		Log.d(DEBUG_TAG, "hasFeatures()");
		return null;
	}

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
            String authTokenType, Bundle loginOptions) {
        Log.d(DEBUG_TAG, "updateCredentials()");
        return null;
    }
    
    public Account getAccount(){
    	// Get accounts based on account type
    	Account[] accounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);
        return accounts[0];
    }

    public String getUsername(){
    	return getAccount().name;
    }
    
    public String getPassword(){
    	return mAccountManager.getPassword(getAccount());
    }
    
    
    public boolean isAccountAuthenticated(){
        if (getUsername().length() >0 && getPassword().length() > 0){
        	return true;
        } else {
        	return false;
        }
    }

}
