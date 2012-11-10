package au.id.teda.broadband.usage.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.format.DateFormat;
import android.util.Xml;

public class AccountInfoParser {
	
	//private static final String DEBUG_TAG = "bbusage";
	
	private static final String ns = null; // We don't use namespaces
	private static final String FEED_TAG = "ii_feed";
	private static final String ACCOUNT_INFO_TAG = "account_info";
	private static final String PLAN_TAG = "plan";
	private static final String PRODUCT_TAG = "product";
	private static final String VOLUME_USAGE_TAG = "volume_usage";
	private static final String OFFPEAK_START_TAG = "offpeak_start";
	private static final String OFFPEAK_END_TAG = "offpeak_end";
	private static final String EXPECTED_TRAFFIC_TYPES_TAG = "expected_traffic_types";
	private static final String TYPE_TAG = "type";
	private static final String QUOTA_ALLOCATION = "quota_allocation";
	private static final String PEAK_ATT = "peak";
	private static final String OFFPEAK_ATT = "offpeak";
	private static final String CLASSIFICATION_ATT = "classification";
	
	private String mPlan = null;
	private String mProduct = null;
	private Calendar mOffpeakStartTime = null;
	private Calendar mOffpeakEndTime = null;
	private String mPeakQuota = null;
	private String mOffpeakQuota = null;
	    
	// This class represents the account info in the XML feed.
	public static class AccountInfo {
		public final String plan;
	    public final String product;
	    public final Calendar offpeakStartTime;
	    public final Calendar offpeakEndTime;
	    public final String peakQuota;
	    public final String offpeakQuota;

	    private AccountInfo(String plan, String product
	    		, Calendar offpeakStartTime, Calendar offpeakEndTime
	    		, String peakQuota, String offpeakQuota) {
	    	this.plan = plan;
	        this.product = product;
	        this.offpeakStartTime = offpeakStartTime;
	        this.offpeakEndTime = offpeakEndTime;
	        this.peakQuota = peakQuota;
	        this.offpeakQuota = offpeakQuota;
	    }
	}
	    
	public List<AccountInfo> parse (InputStream inputStream) throws XmlPullParserException, IOException {
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
	    
	private List<AccountInfo> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<AccountInfo> accountInfo = new ArrayList<AccountInfo>();
	    parser.require(XmlPullParser.START_TAG, ns, FEED_TAG);
	    	while (parser.next() != XmlPullParser.END_TAG) {
	    		if (parser.getEventType() != XmlPullParser.START_TAG) {
	    			continue;
	            }
	            String tagName = parser.getName();
	            // Starts by looking for the account info tag
	            if (tagName.equals(ACCOUNT_INFO_TAG)) {
	            	readAccountInfo(parser);
	            } else if (tagName.equals(VOLUME_USAGE_TAG)){
	            	readVolumeUsage(parser);
	            } else {
	                skip(parser);
	            }
	            
	        }
	    	
	    	accountInfo.add(new AccountInfo(mPlan, mProduct
	    			, mOffpeakStartTime, mOffpeakEndTime
	    			, mPeakQuota, mOffpeakQuota));
	    	
	        return accountInfo;
	    }
	    
	    private void readAccountInfo(XmlPullParser parser) throws XmlPullParserException, IOException {
	        parser.require(XmlPullParser.START_TAG, ns, ACCOUNT_INFO_TAG);
	        while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
	            String name = parser.getName();
	            if (name.equals(PLAN_TAG)) {
	            	mPlan = readAccountPlan(parser);
	            } else if (name.equals(PRODUCT_TAG)) {
	            	mProduct = readAccountProduct(parser);
	            } else {
	                skip(parser);
	            }
	        }
	        
	    }
	    
	    private void readVolumeUsage(XmlPullParser parser) throws XmlPullParserException, IOException {
		    parser.require(XmlPullParser.START_TAG, ns, VOLUME_USAGE_TAG);
		    while (parser.next() != XmlPullParser.END_TAG) {
		    	if (parser.getEventType() != XmlPullParser.START_TAG) {
		    		continue;
		    	}
		    	
		    	String tagName = parser.getName();
		    	if (tagName.equals(OFFPEAK_START_TAG)){
		    		mOffpeakStartTime = getCalendarValue(readOffpeakStart(parser));
		    	} else if (tagName.equals(OFFPEAK_END_TAG)){
		    		mOffpeakEndTime = getCalendarValue(readOffpeakEnd(parser));
		    	} else if (tagName.equals(EXPECTED_TRAFFIC_TYPES_TAG)){
		    		readExpectedTrafficTypes(parser);
		    	} else {
	                skip(parser);
	            }
		    }
	    }
	    
	    private void readExpectedTrafficTypes(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	parser.require(XmlPullParser.START_TAG, ns, EXPECTED_TRAFFIC_TYPES_TAG);
	    	while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
		    	
		    	String tagName = parser.getName();
		    	if (tagName.equals(TYPE_TAG)) {
		    		readType(parser);
		    	} else {
		    		skip(parser);
		    	}
	    	}
	    	
	    }
	    
	    private void readType(XmlPullParser parser) throws IOException, XmlPullParserException {
	    	String classification = parser.getAttributeValue(null, CLASSIFICATION_ATT);
	    	parser.require(XmlPullParser.START_TAG, ns, TYPE_TAG);
	    	while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
		    	
	            String tagName = parser.getName();
	            if (tagName.equals(QUOTA_ALLOCATION)){
	            	if (classification.equals(PEAK_ATT)){
	            		mPeakQuota = readQuota(parser);
		    		} else if (classification.equals(OFFPEAK_ATT)){
		    			mOffpeakQuota = readQuota(parser);
		    		}
	            } else {
	            	skip(parser);
	            }
	    	}
	    	
	    }
	    
	    private String readQuota(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, ns, QUOTA_ALLOCATION);
	        String quota = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, QUOTA_ALLOCATION);
	        return quota;
	    }
	    
	    private String readAccountPlan(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, ns, PLAN_TAG);
	        String plan = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, PLAN_TAG);
	        return plan;
	    }

	    private String readAccountProduct(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, ns, PRODUCT_TAG);
	        String product = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, PRODUCT_TAG);
	        return product;
	    }
	    
	    private String readOffpeakStart(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, ns, OFFPEAK_START_TAG);
	        String start = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, OFFPEAK_START_TAG);
	        return start;
	    }
	    
	    private String readOffpeakEnd(XmlPullParser parser) throws IOException, XmlPullParserException {
	        parser.require(XmlPullParser.START_TAG, ns, OFFPEAK_END_TAG);
	        String end = readText(parser);
	        parser.require(XmlPullParser.END_TAG, ns, OFFPEAK_END_TAG);
	        return end;
	    }


	    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	        String text = null;
	        if (parser.next() == XmlPullParser.TEXT) {
	        	text = parser.getText();
	            parser.nextTag();
	        }
	        return text;
	    }
	    
	    private Calendar getCalendarValue(String time){
	    	SimpleDateFormat hourMintueFormat = new SimpleDateFormat("HH:mm");
	    	Calendar timeValue = Calendar.getInstance();
			try {
				timeValue.setTime(hourMintueFormat.parse(time));
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
