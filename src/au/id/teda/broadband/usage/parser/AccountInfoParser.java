package au.id.teda.broadband.usage.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class AccountInfoParser {
	
	private static final String DEBUG_TAG = "bbusage";
	
	private static final String ns = null; // We don't use namespaces
	private static final String FEED_TAG = "ii_feed";
	private static final String ACCOUNT_INFO_TAG = "account_info";
	private static final String PLAN_TAG = "plan";
	private static final String PRODUCT_TAG = "product";
	    
	// This class represents the account info in the XML feed.
	public static class AccountInfo {
		public final String plan;
	    public final String product;

	    private AccountInfo(String plan, String product) {
	    	this.plan = plan;
	        this.product = product;
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
		List<AccountInfo> account = new ArrayList<AccountInfo>();

	    parser.require(XmlPullParser.START_TAG, ns, FEED_TAG);
	    	while (parser.next() != XmlPullParser.END_TAG) {
	    		if (parser.getEventType() != XmlPullParser.START_TAG) {
	    			continue;
	            }
	            String name = parser.getName();
	            // Starts by looking for the account info tag
	            if (name.equals(ACCOUNT_INFO_TAG)) {
	            	account.add(readAccountInfo(parser));
	            } else {
	                skip(parser);
	            }
	        }
	        return account;
	    }
	    
	    private AccountInfo readAccountInfo(XmlPullParser parser) throws XmlPullParserException, IOException {
	        parser.require(XmlPullParser.START_TAG, ns, ACCOUNT_INFO_TAG);
	        
	        String plan = null;
	        String product = null;
	        while (parser.next() != XmlPullParser.END_TAG) {
	            if (parser.getEventType() != XmlPullParser.START_TAG) {
	                continue;
	            }
	            String name = parser.getName();
	            if (name.equals(PLAN_TAG)) {
	                plan = readAccountPlan(parser);
	            } else if (name.equals(PRODUCT_TAG)) {
	                product = readAccountProduct(parser);
	            } else {
	                skip(parser);
	            }
	        }
	        return new AccountInfo(plan, product);
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
