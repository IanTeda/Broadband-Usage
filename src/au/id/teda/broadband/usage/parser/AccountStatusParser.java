package au.id.teda.broadband.usage.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class AccountStatusParser {
	
	private static final String DEBUG_TAG = "bbusage";
	
	// We don't use namespaces
	private static final String ns = null;
	
	// XML tag strings
	private static final String FEED_TAG = "ii_feed";
	private static final String VOLUME_USAGE_TAG = "volume_usage";
	private static final String OFFPEAK_START_TAG = "offpeak_start";
	private static final String OFFPEAK_END_TAG = "offpeak_end";
	private static final String QUOTA_RESET_TAG = "quota_reset";
	private static final String ANNIVERSARY_TAG = "anniversary";
	private static final String DAYS_SO_FAR_TAG = "days_so_far";
	private static final String DAYS_REMAINING_TAG = "days_remaining";
	private static final String EXPECTED_TRAFFIC_TYPES_TAG = "expected_traffic_types";
	private static final String TYPE_TAG = "type";
	private static final String CLASSIFICATION_ATT = "classification";
	private static final String PEAK_ATT = "peak";
	private static final String OFFPEAK_ATT = "offpeak";
	private static final String UPLOADS_ATT = "uploads";
	private static final String FREEZONE_ATT = "freezone";
	private static final String USED_ATT = "used";
	private static final String NAME_TAG = "name";
	private static final String QUOTA_ALLICATION_TAG = "quota_allocation";
	private static final String IS_SHAPED_TAG = "is_shaped";
	private static final String SPEED_ATT = "speed";
	
	private String mQuotaReset = null;
	private String mPeakDataUsed = null;
	private String mPeakQuota = null;
	private String mPeakSpeed = null;
	private String mPeakShaped = null;
	private String mOffpeakDataUsed = null;
	private String mOffpeakQuota = null;
	private String mOffpeakSpeed = null;
	private String mOffpeakShaped = null;
	private String mUploadsDataUsed = null;
	private String mFreezoneDataUsed = null;
	
	    
	// This class represents the account info in the XML feed.
	public static class AccountStatus {
	    public final String quotaReset;
	    public final String peakDataUsed;
	    public final String peakQuota;
	    public final String peakSpeed;
	    public final String peakShaped;
	    public final String offpeakDataUsed;
	    public final String offpeakQuota;
	    public final String offpeakSpeed;
	    public final String offpeakShaped;
	    public final String uploadsDataUsed;
	    public final String freezoneDataUsed;

	    private AccountStatus( String quotaReset
	    		, String peakDataUsed, String peakQuota, String peakSpeed, String peakShaped
	    		, String offpeakDataUsed, String offpeakQuota, String offpeakSpeed, String offpeakShaped
	    		, String uploadsDataUsed
	    		, String freezoneDataUsed) {
	    	
	        this.quotaReset = quotaReset;
	        this.peakDataUsed = peakDataUsed;
	        this.peakQuota = peakQuota;
	        this.peakSpeed = peakSpeed;
	        this.peakShaped = peakShaped;
	        this.offpeakDataUsed = offpeakDataUsed;
	        this.offpeakQuota = offpeakQuota;
	        this.offpeakSpeed = offpeakSpeed;
	        this.offpeakShaped = offpeakShaped;
	        this.uploadsDataUsed = uploadsDataUsed;
	        this.freezoneDataUsed = freezoneDataUsed;
	        
	    }
	}
	    
	public List<AccountStatus> parse (InputStream inputStream) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
	        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	        parser.setInput(inputStream, null);
	        parser.nextTag();
	        return readFeed(parser);
	    } finally {
	    	inputStream.close();
	    }
	}
	    
	private List<AccountStatus> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		List<AccountStatus> status = new ArrayList<AccountStatus>();

	    parser.require(XmlPullParser.START_TAG, ns, FEED_TAG);
	    	while (parser.next() != XmlPullParser.END_TAG) {
	    		if (parser.getEventType() != XmlPullParser.START_TAG) {
	    			continue;
	            }
	            String tagName = parser.getName();
	            Log.d(DEBUG_TAG, "readFeed: " + tagName);
	            
	            if (tagName.equals(VOLUME_USAGE_TAG)) {
	            	status.add(readVolumeUsage(parser));
	            } else {
	                skip(parser);
	            }
	        }
	        return status;
	    }
	    
	    public AccountStatus readVolumeUsage(XmlPullParser parser) throws XmlPullParserException, IOException {
	    	
		    parser.require(XmlPullParser.START_TAG, ns, VOLUME_USAGE_TAG);
	        
		    while (parser.next() != XmlPullParser.END_TAG) {
		    	if (parser.getEventType() != XmlPullParser.START_TAG) {
		    		continue;
		    	}
		    	
		    	String tagName = parser.getName();
		    	Log.d(DEBUG_TAG, "readVolumeUsage: " + tagName);
		    	
		    	if (tagName.equals(QUOTA_RESET_TAG)){
		    		mQuotaReset = readQuotaReset(parser);
		    	} else if (tagName.equals(EXPECTED_TRAFFIC_TYPES_TAG)){
		    		readExpectedTrafficTypes(parser);
		    	} else {
	                skip(parser);
	            }
		    }
		    
	        return new AccountStatus(mQuotaReset
	        		, mPeakDataUsed, mPeakQuota, mPeakSpeed, mPeakShaped
	        		, mOffpeakDataUsed, mOffpeakQuota, mOffpeakSpeed, mOffpeakShaped
	        		, mUploadsDataUsed, mFreezoneDataUsed);
	    }
	    
	    private String readQuotaReset(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	
	    	String anniversary = null;
	    	String daysSoFar = null;
	    	String daysRemaining = null;
	        
	    	parser.require(XmlPullParser.START_TAG, ns, QUOTA_RESET_TAG);
	    	
	    	while (parser.next() != XmlPullParser.END_TAG) {
		    	if (parser.getEventType() != XmlPullParser.START_TAG) {
		    		continue;
		    	}
		    	
		    	String tagName = parser.getName();
		    	
		    	if (tagName.equals(ANNIVERSARY_TAG)) {
		    		anniversary = readText(parser);
		    	} else if (tagName.equals(DAYS_SO_FAR_TAG)){
		    		daysSoFar = readText(parser);
		    	} else if (tagName.equals(DAYS_REMAINING_TAG)){
		    		daysRemaining = readText(parser);
		    	} else {
		    		skip(parser);
		    	}
	    	}
       
	        return anniversary + " " + daysSoFar + " " + daysRemaining;
	    }
	    
	    private void readExpectedTrafficTypes(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	
	    	parser.require(XmlPullParser.START_TAG, ns, EXPECTED_TRAFFIC_TYPES_TAG);

	    	while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
		    	
		    	String tagName = parser.getName();
		    	String tagAtt = parser.getAttributeValue(null, CLASSIFICATION_ATT);
		    	
		    	if (tagName.equals(TYPE_TAG)) {
		    		if (tagAtt.equals(PEAK_ATT)){
		    			mPeakDataUsed = readDataUsed(parser);
		    		} else if (tagAtt.equals(OFFPEAK_ATT)){
		    			mOffpeakDataUsed = readDataUsed(parser);
		    		} else if (tagAtt.equals(UPLOADS_ATT)){
		    			mUploadsDataUsed = readDataUsed(parser);
		    		} else if (tagAtt.equals(FREEZONE_ATT)){
		    			mFreezoneDataUsed = readDataUsed(parser);
		    		}
		    		readType(parser);
		    	} else {
		    		skip(parser);
		    		Log.d(DEBUG_TAG, "readExpectedTrafficTypes() Skip " + tagName);
		    	}

	    	}
	    	
	    }
	    
	    private void readType(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	
	    	parser.require(XmlPullParser.START_TAG, ns, TYPE_TAG);
	    	
	    	while (parser.nextTag() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
		    	
		    	String tag = parser.getName();
		    	
		    	if (tag.equals(QUOTA_ALLICATION_TAG)) {
		    		Log.d(DEBUG_TAG, "readType() In " + tag);
		    		readQuota(parser);
		    	} else {
		    		skip(parser);
		    		Log.d(DEBUG_TAG, "readType() Skip " + tag);
		    	}

	    	}
	    }
	    
	    private String readQuota(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, ns, QUOTA_ALLICATION_TAG);
	        String quota = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, QUOTA_ALLICATION_TAG);
	        Log.d(DEBUG_TAG, "readQuota " + quota	);
	        return quota;
	    }

	    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	        String text = null;
	        if (parser.next() == XmlPullParser.TEXT) {
	        	text = parser.getText();
	            parser.nextTag();
	        }
	        return text;
	    }
	    
	    private String readDataUsed(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	String dataUsed;
	    	dataUsed = parser.getAttributeValue(null, USED_ATT);
	    	return dataUsed;
	    }
	    
	    // Skips tags the parser isn't interested in. Uses depth to handle nested tags.
	    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    	
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            throw new IllegalStateException();
	        }
	        int depth = 1;
	        while (depth != 0) {
	        	switch (parser.next()) {
	            case XmlPullParser.END_TAG:
	                    depth--;
	                    break;
	            case XmlPullParser.START_TAG:
	                    depth++;
	                    break;
	            }
	        }
	    }
}
