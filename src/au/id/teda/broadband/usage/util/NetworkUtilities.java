package au.id.teda.broadband.usage.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.actionbarsherlock.view.MenuItem;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.authenticator.AccountAuthenticator;
import au.id.teda.broadband.usage.database.VolumeUsageDailyDbAdapter;
import au.id.teda.broadband.usage.helper.AccountInfoHelper;
import au.id.teda.broadband.usage.helper.AccountStatusHelper;
import au.id.teda.broadband.usage.parser.AccountInfoParser;
import au.id.teda.broadband.usage.parser.AccountInfoParser.AccountInfo;
import au.id.teda.broadband.usage.parser.AccountStatusParser;
import au.id.teda.broadband.usage.parser.AccountStatusParser.AccountStatus;
import au.id.teda.broadband.usage.parser.ErrorParser;
import au.id.teda.broadband.usage.parser.VolumeUsageParser;
import au.id.teda.broadband.usage.parser.VolumeUsageParser.VolumeUsage;
import au.id.teda.broadband.usage.syncadapter.DummyContentProvider;
import au.id.teda.broadband.usage.ui.MainActivity;


/**
 * Class for downloading XML data.
 * Authenticate
 * Account Information
 * Account Status
 * Volume Usage
 * 
 * @author iteda
 *
 */
public class NetworkUtilities {
	
	private static final String DEBUG_TAG = "bbusage";

	/** Activity context **/
    private static Context mContext;
    
    /** Activity shared preferences **/
    SharedPreferences sharedPrefs;
    
    /** Task for downloading xml data **/
    private DownloadXmlTask mDownloadXmlTask = null;
    
    // Track AsyncTask for screen rotation
    public boolean isTaskRunning = false;
    
    private Handler mHandler;
    
    private AccountAuthenticator mAccountAuthenticator;
    
    private static String mUsername;
    
	public static final int HANDLER_START_ASYNC_TASK = 0;
	public static final int HANDLER_COMPLETE_ASYNC_TASK = 1;

    // Connection flags.
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    
    // Error texts from XML
    private static final String AUTHENTICATION_FAILURE = "Authentication failure";

    /**
     * Class constructor
     * @param context
     */
    public NetworkUtilities(Context context) {
    	// Set context based on activity context passed to constructor
    	NetworkUtilities.mContext = context;
     
    	mAccountAuthenticator = new AccountAuthenticator(mContext);

    }
    
    /**
     * Check network connectivity and set wifiConnected and mobileConnected flags
     */
    private void updateConnectionFlags() {

        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkStatus = connMgr.getActiveNetworkInfo();
        
        if (networkStatus != null && networkStatus.isConnected()) {
            wifiConnected = networkStatus.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = networkStatus.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }
	
    /**
     * Setup progress dialog and then start download task
     */
    public void syncXmlData(Handler handler){
    	
    	mUsername = mAccountAuthenticator.getUsername();
    	
		mHandler = handler;
    	
    	mDownloadXmlTask = new DownloadXmlTask();
    	mDownloadXmlTask.execute();
    }
    
    /**
     * Get xml buffered input stream
     * @return
     * @throws IOException
     */
    private UnclosableBufferedInputStream getXmlBufferedInputStream() throws IOException {
       
    	// Get input stream
        InputStream inputStream = getUrlInputStream(urlBuilder(mUsername, mAccountAuthenticator.getPassword()));
    	
    	//InputStream inputStream = mContext.getResources().openRawResource(R.raw.naked_dsl_home_5);
    	
        UnclosableBufferedInputStream  bis = new UnclosableBufferedInputStream (inputStream);
    	
    	return bis;
    }
    
    /**
     * Url builder for downloading XML
     * @param username
     * @param password
     * @return
     */
    private String urlBuilder(String username, String password){
    	String urlString = "https://toolbox.iinet.net.au/cgi-bin/new/volume_usage_xml.cgi?" +
				"username=" + username + 
				"&action=login" +
				"&password=" + password;
    	return urlString;
    }
    
    /**
     * Get URL input stream for string representation of URL
     * @param urlString
     * @return
     * @throws IOException
     */
    private InputStream getUrlInputStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }
    
    /**
     * Parse XML based on username and password, for authentication failure error.
     * 
     * @param username
     * @param password
     * @return boolean false if error tag found in XML parse
     */
    public Boolean authenticate(String username, String password){
    	String errorString = null;
    	ErrorParser mErrorParser = new ErrorParser();
    	
    	try {
    		InputStream urlStream = getUrlInputStream(urlBuilder(username, password));
			errorString = mErrorParser.parse(urlStream);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	
    	if (errorString != null 
    			&& errorString.equals(AUTHENTICATION_FAILURE)){
    		return false;
    	} else {
    		return true;
    	}
    }
    
    /**
     * Parse XML input stream for account information and then set shared prefs
     * @param stream
     */
    private void setAccountInfo(UnclosableBufferedInputStream stream) {
       	AccountInfoParser mAccountInfoParser = new AccountInfoParser();
    	List<AccountInfo> account = null;

        try {
        	account = mAccountInfoParser.parse(stream);
        } catch (XmlPullParserException e) {
			Log.e(DEBUG_TAG, "XmlPullParserException: " + e);
		} catch (IOException e) {
			Log.e(DEBUG_TAG, "IOException: " + e);
        }
        
        AccountInfoHelper mAccountInfoHelper = new AccountInfoHelper(mContext);
        
        
        String plan = null;
        String product = null;
    	long offpeakStartTime;
    	long offpeakEndTime;
    	long peakQuota;
    	long offpeakQuota;
        for (AccountInfo accountInfo : account) {
        	
        	plan = accountInfo.plan;
        	product = accountInfo.product;
        	offpeakStartTime = accountInfo.offpeakStartTime;
        	offpeakEndTime = accountInfo.offpeakEndTime;
        	peakQuota = accountInfo.offpeakQuota;
        	offpeakQuota = accountInfo.offpeakQuota;
        	
        	mAccountInfoHelper.setAccountInfo(mUsername, plan, product, offpeakStartTime, offpeakEndTime, peakQuota, offpeakQuota);
        	
        }
                 
    }
    
    /**
     * Parse XML input stream for account status and then set shared prefs
     * @param stream
     */
    private void setAccountStatus(UnclosableBufferedInputStream stream){
    	AccountStatusParser mAccountStatusParser= new AccountStatusParser();
    	List<AccountStatus> status = null;
    	
        try {
        	status = mAccountStatusParser.parse(stream);
        } catch (XmlPullParserException e) {
			Log.e(DEBUG_TAG, "XmlPullParserException: " + e);
		} catch (IOException e) {
			Log.e(DEBUG_TAG, "IOException: " + e);
        }
        
        AccountStatusHelper mAccountStatusHelper = new AccountStatusHelper(mContext);
        
        long quotaResetDate;
        long quotaStartDate;
	    long peakDataUsed;
	    long offpeakDataUsed;
	    long uploadsDataUsed;
	    long freezoneDataUsed;
	    int peakSpeed;
		boolean peakIsShaped;
		int offpeakSpeed;
		boolean offpeakIsShaped;
		String ipAddress;
		long upTimeDate;
		
        for (AccountStatus accountStatus : status) {
        	quotaResetDate = accountStatus.quotaResetDate;
        	quotaStartDate = accountStatus.quotaStartDate;
        	peakDataUsed = accountStatus.peakDataUsed;
        	offpeakDataUsed = accountStatus.offpeakDataUsed;
        	uploadsDataUsed = accountStatus.uploadsDataUsed;
        	freezoneDataUsed = accountStatus.freezoneDataUsed;
        	peakSpeed = accountStatus.peakSpeed;
        	peakIsShaped = accountStatus.peakIsShaped;
        	offpeakSpeed = accountStatus.offpeakSpeed;
        	offpeakIsShaped = accountStatus.offpeakIsShaped;
        	ipAddress = accountStatus.ipAddress;
        	upTimeDate = accountStatus.upTimeDate;
        	
        	mAccountStatusHelper.setAccoutStatus(mUsername, quotaResetDate, quotaStartDate
        			, peakDataUsed, peakIsShaped, peakSpeed
        			, offpeakDataUsed, offpeakIsShaped, offpeakSpeed
        			, uploadsDataUsed, freezoneDataUsed
        			, ipAddress, upTimeDate);

        }
    	
    }
    
    /**
     * Parse XML input stream for volume usage and then added to database
     * @param stream
     */
    private void setVolumeUsage(UnclosableBufferedInputStream stream) {
    	
    	VolumeUsageParser mVolumeUsageParser = new VolumeUsageParser();
    	List<VolumeUsage> usage = null;
      
        try {
        	usage = mVolumeUsageParser.parse(stream);
        } catch (XmlPullParserException e) {
			Log.e(DEBUG_TAG, "XmlPullParserException: " + e);
		} catch (IOException e) {
			Log.e(DEBUG_TAG, "IOException: " + e);
        }
        
        // Initiate database
        VolumeUsageDailyDbAdapter mVolumeUsageDb = new VolumeUsageDailyDbAdapter(mContext);
        mVolumeUsageDb.open();
        
        for (VolumeUsage volumeUsage : usage) {
        	Long day = volumeUsage.day.getTimeInMillis();
        	String month = volumeUsage.month;
        	Long peak = volumeUsage.peak;
        	Long offpeak = volumeUsage.offpeak;
        	Long uploads = volumeUsage.uploads;
        	Long freezone = volumeUsage.freezone;
        	
        	mVolumeUsageDb.addReplaceEntry(mUsername, day, month, peak, offpeak, uploads, freezone);
        }
        
        mVolumeUsageDb.close();
                 
    }
    
    /**
     * Do we have a any type connection to the internet?
     * 
     * @return true if connection present (including WiFi settings)
     */
    public boolean is3gOrWifiConnected() {
    	updateConnectionFlags();
    	
        if (wifiConnected || mobileConnected) {
        	return true;
        } else {
        	return false;
        }
    }
    
    public boolean is3gConnected() {
    	updateConnectionFlags();
    	
        if (mobileConnected) {
        	return true;
        } else {
        	return false;
        }
    }
   
    /**
     * Clear the task object
     * TODO: Do i need to do this?
     */
    private void closeTask(){
    	// Our task is complete, so clear it out
    	mDownloadXmlTask = null;
    }
    
    
    /**
     * AsyncTask for downloading and parsing XML data
     * @author iteda
     *
     */
    private class DownloadXmlTask extends AsyncTask<Void, Void, Void> {

    	/** Complete before we execute task **/
    	protected void onPreExecute(){
    		if (mHandler != null){
    			mHandler.sendEmptyMessage(HANDLER_START_ASYNC_TASK);
    		}
    	}
    	
    	
		@Override
		protected Void doInBackground(Void... params) {
			try {
				UnclosableBufferedInputStream stream = getXmlBufferedInputStream();
				setAccountInfo(stream);
				setAccountStatus(stream);
				setVolumeUsage(stream);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			if (mHandler != null){
			// Stop animation of refresh icon
				mHandler.sendEmptyMessage(HANDLER_COMPLETE_ASYNC_TASK);
			}
			//TODO: Do I need to do this?
			closeTask();
        }
    	
    }
    
    /**
     * Class of buffer input stream
     * @author iteda
     *
     */
    private class UnclosableBufferedInputStream extends BufferedInputStream {

        public UnclosableBufferedInputStream(InputStream in) {
        	super(in);
        	super.mark(Integer.MAX_VALUE);
        }

        @Override
        public void close() throws IOException {
        	super.reset();
        }
    }
    
}