package au.id.teda.broadband.usage.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import au.id.teda.broadband.usage.R;
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

public class DownloadVolumeUsage {
	
	private static final String DEBUG_TAG = "bbusage";

	// Activity context
    private static Context context;
    
    // Activity shared preferences
    SharedPreferences sharedPrefs;

    // Connection flags.
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    public static boolean wifiOnly = true;
    
    // Error texts from XML
    private static final String AUTHENTICATION_FAILURE = "Authentication failure";
    
    private static final String WIFI_ONLY = "wifi_only";

    // Class constructor
    public DownloadVolumeUsage(Context context) {
    	DownloadVolumeUsage.context = context;

    	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
   	
    }
    
    // Checks the network connection and sets the wifiConnected and mobileConnected flags
    private void updateConnectionFlags() {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkStatus = connMgr.getActiveNetworkInfo();
        
        if (networkStatus != null && networkStatus.isConnected()) {
            wifiConnected = networkStatus.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = networkStatus.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }
    
    private InputStream bufferXmlStream(){
    	InputStream mInputStream = context.getResources().openRawResource(R.raw.authentication_error);
    	BufferedInputStream buf = new BufferedInputStream(mInputStream);
    	return buf;
    }
    
    private String urlBuilder(String username, String password){
    	Log.d(DEBUG_TAG, "urlBuilder: " + username + " / " + password);
    	String url = "https://toolbox.iinet.net.au/cgi-bin/new/volume_usage_xml.cgi?" +
				"username=" + username + 
				"&action=login" +
				"&password=" + password;
    	return url;
    }
    
    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
    	Log.d(DEBUG_TAG, "downloadUrl: " + urlString);
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
    
    public Boolean authenticate(String username, String password){
        if (isConnected()) {
        	return errorCheck(username, password);
        } else {
            return false;
        }
    	
    }
    
    private Boolean errorCheck(String username, String password){

    	String errorString = null;
    	ErrorParser mErrorParser = new ErrorParser();
    	
    	try {
    		InputStream urlStream = downloadUrl(urlBuilder(username, password));
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
    
    public void getAccountInfo() {
    	
    	AccountInfoParser mAccountInfoParser = new AccountInfoParser();
    	InputStream stream = bufferXmlStream();
    	List<AccountInfo> account = null;
      
        try {
        	account = mAccountInfoParser.parse(stream);
        } catch (XmlPullParserException e) {
			Log.e(DEBUG_TAG, "XmlPullParserException: " + e);
		} catch (IOException e) {
			Log.e(DEBUG_TAG, "IOException: " + e);
        }
        
        AccountInfoHelper mAccountInfoHelper = new AccountInfoHelper(context);
        
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

        	mAccountInfoHelper.setAccountInfo(plan, product, offpeakStartTime, offpeakEndTime, peakQuota, offpeakQuota);
        	
        }
                 
    }
    
    public void getAccountStatus(){
    	
    	AccountStatusParser mAccountStatusParser= new AccountStatusParser();
    	InputStream stream = bufferXmlStream();
    	List<AccountStatus> status = null;
    	
        try {
        	status = mAccountStatusParser.parse(stream);
        } catch (XmlPullParserException e) {
			Log.e(DEBUG_TAG, "XmlPullParserException: " + e);
		} catch (IOException e) {
			Log.e(DEBUG_TAG, "IOException: " + e);
        }
        
        AccountStatusHelper mAccountStatusHelper = new AccountStatusHelper(context);
        
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
        	
        	mAccountStatusHelper.setAccoutStatus(quotaResetDate, quotaStartDate
        			, peakDataUsed, peakIsShaped, peakSpeed
        			, offpeakDataUsed, offpeakIsShaped, offpeakSpeed
        			, uploadsDataUsed, freezoneDataUsed
        			, ipAddress, upTimeDate);
        	
        }
    	
    }
    
    public void getVolumeUsage() {
    	
    	VolumeUsageParser mVolumeUsageParser = new VolumeUsageParser();
    	InputStream stream = bufferXmlStream();
    	List<VolumeUsage> usage = null;
      
        try {
        	usage = mVolumeUsageParser.parse(stream);
        } catch (XmlPullParserException e) {
			Log.e(DEBUG_TAG, "XmlPullParserException: " + e);
		} catch (IOException e) {
			Log.e(DEBUG_TAG, "IOException: " + e);
        }
        
        // Initiate database
        VolumeUsageDailyDbAdapter mVolumeUsageDb = new VolumeUsageDailyDbAdapter(context);
        mVolumeUsageDb.open();
        
        for (VolumeUsage volumeUsage : usage) {
        	Long day = volumeUsage.day.getTimeInMillis();
        	String month = volumeUsage.month;
        	Long peak = volumeUsage.peak;
        	Long offpeak = volumeUsage.offpeak;
        	Long uploads = volumeUsage.uploads;
        	Long freezone = volumeUsage.freezone;
        	
        	mVolumeUsageDb.addEntry(day, month, peak, offpeak, uploads, freezone);
        }
        
        mVolumeUsageDb.close();
                 
    }
    
    public boolean isConnected() {
    	
    	updateConnectionFlags();
    	
        // Are we only syncing when connected through wifi
        wifiOnly = sharedPrefs.getBoolean(WIFI_ONLY, true);
    	
        if (( (!wifiOnly) && (wifiConnected || mobileConnected))
                || ( (wifiOnly) && (wifiConnected))) {
        	return true;
        } else {
        	return false;
        }
    }
    
    // Displays an error if the app is unable to load content.
    private void showConnectionError() {

    	// TODO fragment toast??
    	// The specified network connection is not available. Displays error message.
    	Toast.makeText(context, R.string.toast_no_connectivity, Toast.LENGTH_SHORT).show();
    }
    
}