package au.id.teda.broadband.usage.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    // Class constructor
    public DownloadVolumeUsage(Context context) {
    	DownloadVolumeUsage.context = context;

    	sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    	
        // Are we only syncing when connected through wifi
        wifiOnly = sharedPrefs.getBoolean(context.getString(R.string.pref_key_wifi_only), true);
        
    	// Check connectivity and set flags
    	updateConnectionFlags();
    	
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
    	InputStream mInputStream = context.getResources().openRawResource(R.raw.naked_dsl_home_5);
    	BufferedInputStream buf = new BufferedInputStream(mInputStream);
    	return buf;
    }
    
    public Boolean authCheck(){
    	String errorString = null;
    	ErrorParser mErrorParser = new ErrorParser();
    	
    	try {
			errorString = mErrorParser.parse(bufferXmlStream());
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
        
        Calendar quotaReset;
	    String peakDataUsed;
	    String offpeakDataUsed;
	    String uploadsDataUsed;
	    String freezoneDataUsed;
	    String peakSpeed;
		boolean peakIsShaped;
		String offpeakSpeed;
		boolean offpeakIsShaped;
		String ipAddress;
		Calendar upTimeDate;
		
        for (AccountStatus accountStatus : status) {
        	quotaReset = accountStatus.quotaReset;
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
        	
        	Log.d(DEBUG_TAG, "Quota Reset: " + quotaReset.getTime());
        	Log.d(DEBUG_TAG, "Peak Data Used: " + peakDataUsed);
        	Log.d(DEBUG_TAG, "Offpeak Data Used: " + offpeakDataUsed);
        	Log.d(DEBUG_TAG, "Uploads Data Used: " + uploadsDataUsed);
        	Log.d(DEBUG_TAG, "Freezone Data Used: " + freezoneDataUsed);
        	Log.d(DEBUG_TAG, "Peak Is Shaped: " + peakIsShaped);
        	Log.d(DEBUG_TAG, "Peak Shaped Speed: " + peakSpeed);
        	Log.d(DEBUG_TAG, "Offpeak Is Shaped: " + offpeakIsShaped);
        	Log.d(DEBUG_TAG, "Offpeak Shaped Speed: " + offpeakSpeed);
        	Log.d(DEBUG_TAG, "Ip Address: " + ipAddress);
        	Log.d(DEBUG_TAG, "Uptime: " + upTimeDate.getTime());
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
    
    public void getData() {
    	
        if (( (!wifiOnly) && (wifiConnected || mobileConnected))
                || ( (wifiOnly) && (wifiConnected))) {

        } else {
        	// TODO Show wifi only status
            showConnectionError();
        }
    }
    
    // Displays an error if the app is unable to load content.
    private void showConnectionError() {

    	// TODO fragment toast??
    	// The specified network connection is not available. Displays error message.
    	Toast.makeText(context, R.string.toast_no_connectivity, Toast.LENGTH_SHORT).show();
    }
    
}