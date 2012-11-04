package au.id.teda.broadband.usage.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
	private static final String FREEZIBE_ATT = "freezone";
	private static final String USED_ATT = "used";
	private static final String NAME_TAG = "name";
	private static final String QUOTA_ALLICATION_TAG = "quota_allocation";
	private static final String IS_SHAPED_TAG = "is_shaped";
	private static final String SPEED_ATT = "speed";
	    
	// This class represents the account info in the XML feed.
	public static class AccountStatus {
		public final String offpeakStart;
	    public final String offpeakEnd;
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

	    private AccountStatus( String offpeakStart, String offpeakEnd
	    		, String quotaReset
	    		, String peakDataUsed, String peakQuota, String peakSpeed, String peakShaped
	    		, String offpeakDataUsed, String offpeakQuota, String offpeakSpeed, String offpeakShaped
	    		, String uploadsDataUsed
	    		, String freezoneDataUsed) {
	    	
	    	this.offpeakStart = offpeakStart;
	        this.offpeakEnd = offpeakEnd;
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
	            String name = parser.getName();
	            // Starts by looking for the account info tag
	            if (name.equals(VOLUME_USAGE_TAG)) {
	            	status.add(readVolumeUsage(parser));
	            } else {
	                skip(parser);
	            }
	        }
	        return status;
	    }
	    
	    private AccountStatus readVolumeUsage(XmlPullParser parser) throws XmlPullParserException, IOException {
	    	Log.d(DEBUG_TAG, "readVolumeUsage");
	    	
	        parser.require(XmlPullParser.START_TAG, ns, VOLUME_USAGE_TAG);
	        
			String offpeakStart = null;
		    String offpeakEnd = null;
		    String quotaReset = null;
		    String peakDataUsed = "test";
		    String peakQuota = "test";
		    String peakSpeed = "test";
		    String peakShaped = "test";
		    String offpeakDataUsed = "test";
		    String offpeakQuota = "test";
		    String offpeakSpeed = "test";
		    String offpeakShaped = "test";
		    String uploadsDataUsed = "test";
		    String freezoneDataUsed = "test";

	        while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
	            String name = parser.getName();
	            Log.d(DEBUG_TAG, name.toString());
	            if (name.equals(OFFPEAK_START_TAG)) {
	            	offpeakStart = readOffpeakStart(parser);
	            } else if (name.equals(OFFPEAK_END_TAG)) {
	            	offpeakEnd = readOffpeakEnd(parser);
	            } else if (name.equals(QUOTA_RESET_TAG)){
	            	quotaReset = readQuotaReset(parser);
	            } else if (name.equals(EXPECTED_TRAFFIC_TYPES_TAG)){
	            	//peakDataUsed = readPeakDataUsed(parser);
	            	//offpeakDataUsed = readOffpeakDataUsed(parser);
	            	//uploadsDataUsed = readUploadsDataUsed(parser);
	            	//freezoneDataUsed = readFreezoneDataUsed(parser);
	            } else {
	                skip(parser);
	            }
	        }
	        
	        return new AccountStatus(offpeakStart, offpeakEnd, quotaReset
	        		, peakDataUsed, peakQuota, peakSpeed, peakShaped
	        		, offpeakDataUsed, offpeakQuota, offpeakSpeed, offpeakShaped
	        		, uploadsDataUsed, freezoneDataUsed);
	    }
	    
	    private String readOffpeakStart(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	Log.d(DEBUG_TAG, "readOffpeakStart");
	        parser.require(XmlPullParser.START_TAG, ns, OFFPEAK_START_TAG);
	        String offpeakStart = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, OFFPEAK_START_TAG);
	        return offpeakStart;
	    }

	    private String readOffpeakEnd(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	Log.d(DEBUG_TAG, "readOffpeakEnd");
	        parser.require(XmlPullParser.START_TAG, ns, OFFPEAK_END_TAG);
	        String offpeakEnd = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, OFFPEAK_END_TAG);
	        return offpeakEnd;
	    }
	    
	    private String readQuotaReset(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	Log.d(DEBUG_TAG, "readQuotaReset");
	    	
	    	String anniversary = null;
	    	String daysSoFar = null;
	    	String daysRemaining = null;
	        
	    	parser.require(XmlPullParser.START_TAG, ns, QUOTA_RESET_TAG);
	    	String tag = parser.getName();
	    	if (tag.equals(ANNIVERSARY_TAG)) {
	    		anniversary = readText(parser);
	    	} else if (tag.equals(DAYS_SO_FAR_TAG)){
	    		daysSoFar = readText(parser);
	    	} else if (tag.equals(DAYS_REMAINING_TAG)){
	    		daysRemaining = readText(parser);
	    	}
	        parser.require(XmlPullParser.END_TAG, ns, QUOTA_RESET_TAG);
	        
	        return anniversary;
	    }
	    
	    private String readPeakDataUsed(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	Log.d(DEBUG_TAG, "readPeakDataUsed");
	    	String peakData = null;
	    	
	    	parser.require(XmlPullParser.START_TAG, ns, EXPECTED_TRAFFIC_TYPES_TAG);
	    	String tag = parser.getName();
	    	String relType = parser.getAttributeValue(null, CLASSIFICATION_ATT);
	    	
	        if (tag.equals(TYPE_TAG)) {
	            if (relType.equals(PEAK_ATT)) {
	            	peakData = parser.getAttributeValue(null, USED_ATT);
	                parser.nextTag();
	            }
	        }
	        parser.require(XmlPullParser.END_TAG, ns, EXPECTED_TRAFFIC_TYPES_TAG);
	        return peakData;

	    }

	    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	        String text = null;
	        if (parser.next() == XmlPullParser.TEXT) {
	        	text = parser.getText();
	            parser.nextTag();
	        }
	        return text;
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
