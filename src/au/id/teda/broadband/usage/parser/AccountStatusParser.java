package au.id.teda.broadband.usage.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class AccountStatusParser {
	
	//private static final String DEBUG_TAG = "bbusage";
	
	// We don't use namespaces
	private static final String ns = null;
	
	// XML tag strings
	private static final String FEED_TAG = "ii_feed";
	private static final String VOLUME_USAGE_TAG = "volume_usage";
	private static final String QUOTA_RESET_TAG = "quota_reset";
	private static final String DAYS_REMAINING_TAG = "days_remaining";
	private static final String EXPECTED_TRAFFIC_TYPES_TAG = "expected_traffic_types";
	private static final String TYPE_TAG = "type";
	private static final String CLASSIFICATION_ATT = "classification";
	private static final String PEAK_ATT = "peak";
	private static final String OFFPEAK_ATT = "offpeak";
	private static final String UPLOADS_ATT = "uploads";
	private static final String FREEZONE_ATT = "freezone";
	private static final String USED_ATT = "used";
	private static final String IS_SHAPED_TAG = "is_shaped";
	private static final String SPEED_ATT = "speed";
	private static final String CONNECTIONS_TAG = "connections";
	private static final String IP_TAG = "ip";
	private static final String ON_SINCE_ATT = "on_since";
	
	private Calendar mQuotaReset = null;
	private String mPeakDataUsed = null;
	private String mPeakSpeed = null;
	private boolean mPeakIsShaped = false;
	private String mOffpeakDataUsed = null;
	private String mOffpeakSpeed = null;
	private boolean mOffpeakIsShaped = false;
	private String mUploadsDataUsed = null;
	private String mFreezoneDataUsed = null;
	private String mIpAddress = null;
	private Calendar mUpTimeDate = null;
	    
	// This class represents the account info in the XML feed.
	public static class AccountStatus {
	    public final Calendar quotaReset;
	    public final String peakDataUsed;
	    public final String peakSpeed;
	    public final boolean peakIsShaped;
	    public final String offpeakDataUsed;
	    public final String offpeakSpeed;
	    public final boolean offpeakIsShaped;
	    public final String uploadsDataUsed;
	    public final String freezoneDataUsed;
	    public final String ipAddress;
	    public final Calendar upTimeDate;

	    private AccountStatus( Calendar quotaReset
	    		, String peakDataUsed, String peakSpeed, boolean peakIsShaped
	    		, String offpeakDataUsed, String offpeakSpeed, boolean offpeakIsShaped
	    		, String uploadsDataUsed
	    		, String freezoneDataUsed
	    		, String ipAddress, Calendar upTimeDate) {
	    	
	        this.quotaReset = quotaReset;
	        this.peakDataUsed = peakDataUsed;
	        this.peakSpeed = peakSpeed;
	        this.peakIsShaped = peakIsShaped;
	        this.offpeakDataUsed = offpeakDataUsed;
	        this.offpeakSpeed = offpeakSpeed;
	        this.offpeakIsShaped = offpeakIsShaped;
	        this.uploadsDataUsed = uploadsDataUsed;
	        this.freezoneDataUsed = freezoneDataUsed;
	        this.ipAddress = ipAddress;
	        this.upTimeDate = upTimeDate;
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
	    	if (tagName.equals(VOLUME_USAGE_TAG)) {
	    		readVolumeUsage(parser);
	    	} else if (tagName.equals(CONNECTIONS_TAG)){
	    		readConnectionInfo(parser);
	    	} else {
	    		skip(parser);
	    	}
	    }
	    
	    status.add( new AccountStatus(mQuotaReset
	        		, mPeakDataUsed, mPeakSpeed, mPeakIsShaped
	        		, mOffpeakDataUsed, mOffpeakSpeed, mOffpeakIsShaped
	        		, mUploadsDataUsed, mFreezoneDataUsed
	        		, mIpAddress, mUpTimeDate));
	    
	    return status;
	}
	    
	private void readVolumeUsage (XmlPullParser parser) throws XmlPullParserException, IOException {
	    	
		parser.require(XmlPullParser.START_TAG, ns, VOLUME_USAGE_TAG);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
		    	
			String tagName = parser.getName();
			if (tagName.equals(QUOTA_RESET_TAG)){
				mQuotaReset = readQuotaReset(parser);
			} else if (tagName.equals(EXPECTED_TRAFFIC_TYPES_TAG)){
				readExpectedTrafficTypes(parser);
			} else {
				skip(parser);
			}
		}
	}
	    
	    public void readConnectionInfo(XmlPullParser parser) throws XmlPullParserException, IOException {

		    parser.require(XmlPullParser.START_TAG, ns, CONNECTIONS_TAG);
		    while (parser.next() != XmlPullParser.END_TAG) {
		    	if (parser.getEventType() != XmlPullParser.START_TAG) {
		    		continue;
		    	}
		    	
		    	String tagName = parser.getName();
		    	
		    	if (tagName.equals(IP_TAG)){
		    		readIpInfo(parser);
		    	} else {
	                skip(parser);
	            }
		    }
	    }
	    
	    private Calendar readQuotaReset(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	
	    	String daysRemaining = null;
	        
	    	parser.require(XmlPullParser.START_TAG, ns, QUOTA_RESET_TAG);
	    	while (parser.next() != XmlPullParser.END_TAG) {
		    	if (parser.getEventType() != XmlPullParser.START_TAG) {
		    		continue;
		    	}
		    	
		    	String tagName = parser.getName();
		    	
		    	if (tagName.equals(DAYS_REMAINING_TAG)){
		    		daysRemaining = readText(parser);
		    	} else {
		    		skip(parser);
		    	}
	    	}
	    	Calendar now = Calendar.getInstance();
	    	now.set(Calendar.HOUR_OF_DAY, 0);
	    	now.add(Calendar.DATE, Integer.parseInt(daysRemaining) );
	        return now;
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
		    	}

	    	}
	    	
	    }
	    
	    private void readType(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	
	    	String classification = parser.getAttributeValue(null, CLASSIFICATION_ATT);
	    	
	    	parser.require(XmlPullParser.START_TAG, ns, TYPE_TAG);
	    	while (parser.nextTag() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
		    	
	            String tagName = parser.getName();
	            if (tagName.equals(IS_SHAPED_TAG)){
	            	if (classification.equals(PEAK_ATT)){
	            		mPeakSpeed = readShapedSpeed(parser);
	            		mPeakIsShaped = readIsShaped(parser);
		    		} else if (classification.equals(OFFPEAK_ATT)){
		    			mOffpeakSpeed = readShapedSpeed(parser);
		    			mOffpeakIsShaped = readIsShaped(parser);
		    		}
	            } else {
	            	skip(parser);
	            }

	    	}
	    }
	    
	    private boolean readIsShaped(XmlPullParser parser) throws IOException, XmlPullParserException {
	        boolean flag = false;
	    	parser.require(XmlPullParser.START_TAG, ns, IS_SHAPED_TAG);
	        String shaped = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, IS_SHAPED_TAG);
	        if (shaped == "true"){
	        	flag = true;
	        } else {
	        	flag = false;
	        }
	        return flag;
	    }
	    
	    private void readIpInfo(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	mUpTimeDate = getDateTimeValue(parser.getAttributeValue(null, ON_SINCE_ATT));
	    	parser.require(XmlPullParser.START_TAG, ns, IP_TAG);
	        mIpAddress = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, IP_TAG);
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
	    
	    private String readShapedSpeed(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	String shapedSpeed;
	    	shapedSpeed = parser.getAttributeValue(null, SPEED_ATT);
	    	return shapedSpeed;
	    }
	    
	    private Calendar getDateTimeValue(String dateTime){
	    	SimpleDateFormat hourMintueFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	Calendar timeValue = Calendar.getInstance();
			try {
				timeValue.setTime(hourMintueFormat.parse(dateTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return timeValue;
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
