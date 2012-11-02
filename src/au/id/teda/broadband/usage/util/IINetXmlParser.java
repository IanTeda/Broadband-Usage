/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package au.id.teda.broadband.usage.util;

import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses XML feeds from stackoverflow.com.
 * Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (post) in the XML feed.
 */
public class IINetXmlParser {
	
	private static final String DEBUG_TAG = "bbusage";
	
	// We don't use namespaces
    private static final String ns = null;
    private static final String FEED_TAG = "ii_feed";
    private static final String ERROR_TAG = "error";
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

    // This class represents a single data period in the XML feed.
    public static class DataPeriod {
        public final String period;
        public final String peak;
        public final String offpeak;
        public final String uploads;
        public final String freezone;

        private DataPeriod(String period, String peak, String offpeak, String uploads, String freezone) {
            this.period = period;
            this.peak = peak;
            this.offpeak = offpeak;
            this.uploads = uploads;
            this.freezone = freezone;
        }
    }
    
    
    /**
     * Method used to check for error text within XML feed.
     * @param inputStream
     * @return string with error text
     * @throws XmlPullParserException
     * @throws IOException
     */
    public String parseForError (InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeedForError(parser);
        } finally {
        	inputStream.close();
        }
    }
    
    public List<AccountInfo> parseAccountInfo(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
        	Log.d(DEBUG_TAG, "parseAccountInfo()");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeedForAccountInfo(parser);
        } finally {
        	inputStream.close();
        }
    }
    
    private String readFeedForError(XmlPullParser parser) throws XmlPullParserException, IOException {
        String error = "no errors";
        
        parser.require(XmlPullParser.START_TAG, ns, FEED_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            
            // Starts by looking for the feed entry tag
        	if (tag.equals(ERROR_TAG)) {
            	error = readError(parser);
            } else {
                skip(parser);
            }
        }
        return error;
    }
    
    private List<AccountInfo> readFeedForAccountInfo(XmlPullParser parser) throws XmlPullParserException, IOException {
        
    	List<AccountInfo> acountInfo = new ArrayList<AccountInfo>();
        
        parser.require(XmlPullParser.START_TAG, ns, FEED_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            
            // Starts by looking for the feed entry tag
        	if (tag.equals(ACCOUNT_INFO_TAG)) {
        		acountInfo.add(readAccountInfo(parser));
            } else {
                skip(parser);
            }
        }
        return acountInfo;
    }
    
    // Processes error tag.
    private String readError(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ERROR_TAG);
        String error = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, ERROR_TAG);
        return error;
    }

    private AccountInfo readAccountInfo(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, ACCOUNT_INFO_TAG);

        String plan = null;
        String product = null;
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            Log.d(DEBUG_TAG, tag);
            if (tag.equals(PLAN_TAG)) {
                plan = readPlan(parser);
            } else if (tag.equals(PRODUCT_TAG)) {
                product = readProduct(parser);
            } else {
                skip(parser);
            }
        }
        return new AccountInfo(plan, product);
    }
    
    private String readPlan(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, PLAN_TAG);
        String plan = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, PLAN_TAG);
        return plan;
    }
    
    private String readProduct(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, PRODUCT_TAG);
        String plan = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, PRODUCT_TAG);
        return plan;
    }
    
    
    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String text = null;
        if (parser.next() == XmlPullParser.TEXT) {
        	text = parser.getText();
            parser.nextTag();
        }
        
        Log.d(DEBUG_TAG, text);
        
        return text;
    }
    
    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
    	
    	Log.d(DEBUG_TAG, "skip");
    	
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
