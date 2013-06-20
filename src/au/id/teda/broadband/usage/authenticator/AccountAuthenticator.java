package au.id.teda.broadband.usage.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.activity.BaseActivity;

public class AccountAuthenticator extends AbstractAccountAuthenticator {
	
	private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
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
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		return null;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		return null;
	}

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
            String authTokenType, Bundle loginOptions) {
        return null;
    }
    
    
    public Account getAccount(){
    	// Get accounts based on account type
    	Account[] accounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);
    	if (accounts.length > 0){
            return accounts[0];
        } else {
    		return null;
    	}
    }

    public String getUsername(){
    	if (getAccount() != null){
    		return getAccount().name;
    	} else {
    		return mContext.getString(R.string.fragment_product_plan_username);
    	}
    }
    
    public String getPassword(){
    	if (getAccount() != null){
    		return mAccountManager.getPassword(getAccount());
    	} else {
    		return "";
    	}
    }
    
    public boolean isAccountAuthenticated(){
        if (getAccount() != null){
        	if (getUsername().length() >0 && getPassword().length() > 0){
        		return true;
        	} else {
        		return false;
        	}
        } else {
        	return false;
        }
    }

}
