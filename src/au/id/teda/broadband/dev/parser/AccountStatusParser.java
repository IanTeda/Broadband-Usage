package au.id.teda.broadband.dev.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class AccountStatusParser {
	
	//private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
	// We don't use namespaces
	private static final String ns = null;
	
	// XML tag strings
	private static final String FEED_TAG = "ii_feed";
	private static final String VOLUME_USAGE_TAG = "volume_usage";
	private static final String QUOTA_RESET_TAG = "quota_reset";
	private static final String DAYS_REMAINING_TAG = "days_remaining";
	private static final String DAYS_SO_FARE_TAG = "days_so_far";
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
	
	private long mQuotaResetDate;
	private long mQuotaStartDate;
	private long mPeakDataUsed;
	private int mPeakSpeed;
	private boolean mPeakIsShaped = false;
	private long mOffpeakDataUsed;
	private int mOffpeakSpeed;
	private boolean mOffpeakIsShaped = false;
	private long mUploadsDataUsed;
	private long mFreezoneDataUsed;
	private String mIpAddress = null;
	private long mUpTimeDate;
	    
	// This class represents the account info in the XML feed.
	public static class AccountStatus {
	    public final long quotaResetDate;
	    public final long quotaStartDate;
	    public final long peakDataUsed;
	    public final int peakSpeed;
	    public final boolean peakIsShaped;
	    public final long offpeakDataUsed;
	    public final int offpeakSpeed;
	    public final boolean offpeakIsShaped;
	    public final long uploadsDataUsed;
	    public final long freezoneDataUsed;
	    public final String ipAddress;
	    public final long upTimeDate;

	    private AccountStatus( long quotaResetDate, long quotaStartDate
	    		, long peakDataUsed, int peakSpeed, boolean peakIsShaped
	    		, long offpeakDataUsed, int offpeakSpeed, boolean offpeakIsShaped
	    		, long uploadsDataUsed
	    		, long freezoneDataUsed
	    		, String ipAddress, long upTimeDate) {
	    	
	    	this.quotaStartDate = quotaStartDate;
	        this.quotaResetDate = quotaResetDate;
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
	    
	    status.add( new AccountStatus(mQuotaResetDate, mQuotaStartDate
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
				readQuotaReset(parser);
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
	    
	    private void readQuotaReset(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	
	    	String daysRemaining = null;
	    	String daysSoFare = null;
	        
	    	parser.require(XmlPullParser.START_TAG, ns, QUOTA_RESET_TAG);
	    	while (parser.next() != XmlPullParser.END_TAG) {
		    	if (parser.getEventType() != XmlPullParser.START_TAG) {
		    		continue;
		    	}
		    	
		    	String tagName = parser.getName();
		    	
		    	if (tagName.equals(DAYS_REMAINING_TAG)){
		    		daysRemaining = readText(parser);
		    	} else if (tagName.equals(DAYS_SO_FARE_TAG)){
		    		daysSoFare = readText(parser);
		    	} else {
		    		skip(parser);
		    	}
	    	}
	    	
	    	Calendar quotaResetDate = Calendar.getInstance();
	    	quotaResetDate.add(Calendar.DATE, Integer.parseInt(daysRemaining) );
	    	quotaResetDate.set(Calendar.HOUR_OF_DAY, 0);
	    	mQuotaResetDate = quotaResetDate.getTimeInMillis();

	    	Calendar quotaStartDate = Calendar.getInstance();
	    	quotaStartDate.add(Calendar.DATE, ( -1 * Integer.parseInt(daysSoFare) ) );
	    	quotaStartDate.set(Calendar.HOUR_OF_DAY, 0);
	    	mQuotaStartDate = quotaStartDate.getTimeInMillis();
	    	
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
		    			mPeakDataUsed = stringToLong(readDataUsed(parser));
		    		} else if (tagAtt.equals(OFFPEAK_ATT)){
		    			mOffpeakDataUsed = stringToLong(readDataUsed(parser));
		    		} else if (tagAtt.equals(UPLOADS_ATT)){
		    			mUploadsDataUsed = stringToLong(readDataUsed(parser));
		    		} else if (tagAtt.equals(FREEZONE_ATT)){
		    			mFreezoneDataUsed = stringToLong(readDataUsed(parser));
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
	            		mPeakSpeed = stringToInt(readShapedSpeed(parser));
	            		mPeakIsShaped = readIsShaped(parser);
		    		} else if (classification.equals(OFFPEAK_ATT)){
		    			mOffpeakSpeed = stringToInt(readShapedSpeed(parser));
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
	    	Calendar upDate = getDateTimeValue(parser.getAttributeValue(null, ON_SINCE_ATT));
	    	mUpTimeDate = upDate.getTimeInMillis();
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
	    	SimpleDateFormat hourMintueFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	    	Calendar timeValue = Calendar.getInstance();
			try {
				timeValue.setTime(hourMintueFormat.parse(dateTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return timeValue;
	    }
	    
	    private Long stringToLong(String s){
	    	Long l = Long.parseLong(s);
	    	return l;
	    }
	    
	    private int stringToInt(String s){
	    	int i = Integer.parseInt(s);
	    	return i;
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
