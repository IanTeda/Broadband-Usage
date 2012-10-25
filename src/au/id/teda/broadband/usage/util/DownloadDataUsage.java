package au.id.teda.broadband.usage.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import au.id.teda.broadband.usage.R;
import au.id.teda.broadband.usage.util.IINetXmlParser.DayHour;

public class DownloadDataUsage {
	
	private static final String DEBUG_TAG = "bbusage";

    private static final String URL = "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";
	
	Context mContext;

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;

    // The user's current network preference setting.
    public static boolean wifiOnly = true;

    // Class constructor
    public DownloadDataUsage(Context context) {
    	
    	//Log.d(DEBUG_TAG, "DownloadDataUsage( "+ context + " )");
    	
    	// Set context from activity that called helper
    	this.mContext = context;
    	
    	// Gets the user's network preference settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        
        // Retrieves a string value for the preferences. The second parameter
        // is the default value to use if a preference value is not found.
        wifiOnly = sharedPrefs.getBoolean(mContext.getString(R.string.pref_key_wifi_only), true);
    	
    	// Check connectivity and set flags
    	updateConnectedFlags();
    	
    }
    
    // Checks the network connection and sets the wifiConnected and mobileConnected
    // variables accordingly.
    private void updateConnectedFlags() {
    	
    	//Log.d(DEBUG_TAG, "updateConnectedFlags()");
    	
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
    
    // Uses AsyncTask subclass to download the XML feed from stackoverflow.com.
    // This avoids UI lock up. To prevent network operations from
    // causing a delay that results in a poor user experience, always perform
    // network operations on a separate thread from the UI.
    public void getData() {
    	
    	//Log.d(DEBUG_TAG, "getData()");
    	
        if (( (!wifiOnly) && (wifiConnected || mobileConnected))
                || ( (wifiOnly) && (wifiConnected))) {
            // AsyncTask subclass
            new DownloadXmlTask().execute(URL);
        } else {
        	// TODO Show wifi only status
            showError();
        }
    }
    
    // Displays an error if the app is unable to load content.
    private void showError() {

    	// TODO fragment toast??
    	// The specified network connection is not available. Displays error message.
    	Toast.makeText(mContext, R.string.toast_no_connectivity, Toast.LENGTH_SHORT).show();
    }
    
    // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
        	//Log.d(DEBUG_TAG, "DownloadXmlTask.doInBackground( "+ urls + " )");
        	
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return Resources.getSystem().getString(R.string.toast_no_connectivity);
            } catch (XmlPullParserException e) {
                return Resources.getSystem().getString(R.string.toast_error_xml);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            
        	//Log.d(DEBUG_TAG, "onPostExecute()");
        	
        }
    }
    
    // Uploads XML from stackoverflow.com, parses it, and combines it with
    // HTML markup. Returns HTML string.
    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
    	
    	//Log.d(DEBUG_TAG, "loadXmlFromNetwork(" + urlString + ")");
    	
        InputStream stream = null;
        IINetXmlParser mXmlParser = new IINetXmlParser();
        
        List<DayHour> entries = null;
        String title = null;
        String url = null;
        String summary = null;
        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

        // Checks whether the user set the preference to include summary text
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean pref = sharedPrefs.getBoolean("summaryPref", false);

        try {
			// Load & parse development XML file
        	InputStream streamRaw = mContext.getResources().openRawResource(R.raw.naked_dsl_home_5);
        	//Log.d(DEBUG_TAG, "loadXmlFromNetwork().try stream: " + streamRaw);
        	entries = mXmlParser.parse(streamRaw);

        	// Load stream and parse entry
            //stream = downloadUrl(urlString);
            //entries = stackOverflowXmlParser.parse(stream);
        // Makes sure that the InputStream is closed after the app is finished using it.
        } finally {
        	//Log.d(DEBUG_TAG, "loadXmlFromNetwork().finally");
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        String estring = null;
        
        for (DayHour entry : entries) {
        	Log.d(DEBUG_TAG, "loadXmlFromNetwork().for Entry");
        	Log.d(DEBUG_TAG, "Period: " + entry.period 
        			+ " + Peak: " + entry.peak 
        			+ " + Offpeak: " + entry.offpeak
        			+ " + Uploads: " + entry.uploads
        			+ " + Freezone: " + entry.freezone);
        	estring = entry.period + entry.peak  + entry.offpeak + entry.uploads + entry.freezone;
        }
        return estring;
    }
    
    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
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
    *
    * This BroadcastReceiver intercepts the android.net.ConnectivityManager.CONNECTIVITY_ACTION,
    * which indicates a connection change. It checks whether the type is TYPE_WIFI.
    * If it is, it checks whether Wi-Fi is connected and sets the wifiConnected flag in the
    * main activity accordingly.
    *
    */
   public class NetworkReceiver extends BroadcastReceiver {
	   
       @Override
       public void onReceive(Context context, Intent intent) {
    	   
           ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
           NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

           // Checks the user prefs and the network connection. Based on the result, decides
           // whether to refresh the display or keep the current display.
           // If the userpref is Wi-Fi only, checks to see if the device has a Wi-Fi connection.
           if (wifiOnly 
        		   && networkInfo != null
                   && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
               // If device has its Wi-Fi connection, sets refreshDisplay
               // to true. This causes the display to be refreshed when the user
               // returns to the app.
               refreshDisplay = true;
               // Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();

               // If the setting is ANY network and there is a network connection
               // (which by process of elimination would be mobile), sets refreshDisplay to true.
           } else if (!wifiOnly 
        		   && networkInfo != null) {

               // Otherwise, the app can't download content--either because there is no network
               // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
               // is no Wi-Fi connection.
               // Sets refreshDisplay to false.
           } else {
               Toast.makeText(mContext, R.string.toast_no_connectivity, Toast.LENGTH_SHORT).show();
           }
       }
   }
    
    
    
    
    
    
    
    
    
    

    
    
    
}
