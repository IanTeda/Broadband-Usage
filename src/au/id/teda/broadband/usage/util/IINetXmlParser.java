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

import org.xml.sax.InputSource;
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
    private static final String AUTH_ERROR = "Authentication failure";
    
    private static final String ACCOUNT_INFO_ENTRY = "account_info";
    private static final String VOLUME_USAGE_ENTRY = "volume_usage";
    private static final String CONNECTION_ENTRY = "connections";
    
    
    private static final String DAY_HOUR_TAG = "day_hour";
    private static final String USAGE_TAG = "usage";
    
    private static final String PERIOD_KEY = "period";
    private static final String PEAK_KEY = "peak";
    private static final String OFFPEAK_KEY = "offpeak";
    private static final String	UPLOADS_KEY = "uploads";
    private static final String FREEZONE_KEY = "freezone";

    // We don't use namespaces
    
    public List<DayHour> parse(InputStream inputStream) throws XmlPullParserException, IOException {
    	
    	Log.d(DEBUG_TAG, "parse(inputStream)");
    	
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

    private List<DayHour> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
    	
        List<DayHour> usage = new ArrayList<DayHour>();
        
        parser.require(XmlPullParser.START_TAG, ns, FEED_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
            	//Log.d(DEBUG_TAG, "Continue");
                continue;
            }
            String tag = parser.getName();
            
            //Log.d(DEBUG_TAG, "readFeed() > Tag: " + tag.toString());
            
            // Starts by looking for the entry tag
        	if (tag.equals(VOLUME_USAGE_ENTRY)) {
            	Log.d(DEBUG_TAG, "In Tag: " + tag.toString());
        		return readVolumeUsage(parser);
            } else {
                skip(parser);
            }
        }
        return usage;
    }
    
    private List<DayHour> readVolumeUsage(XmlPullParser parser) throws XmlPullParserException, IOException {
    	
    	List<DayHour> usage = new ArrayList<DayHour>();
        
        parser.require(XmlPullParser.START_TAG, ns, VOLUME_USAGE_ENTRY);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            
            //Log.d(DEBUG_TAG, "readVolumeUsage() > Tag: " + tag.toString());
            
            // Starts by looking for the entry tag
            if (tag.equals(VOLUME_USAGE_ENTRY)) {
            	Log.d(DEBUG_TAG, "In Tag2: " + tag.toString());
           		return readDailyUsage(parser);
            } else {
                skip(parser);
            }
        }
        return usage;
    }
    
    private List<DayHour> readDailyUsage(XmlPullParser parser) throws XmlPullParserException, IOException {
    	
    	Log.d(DEBUG_TAG, "readUsage()");
    	
    	List<DayHour> usage = new ArrayList<DayHour>();
        
        parser.require(XmlPullParser.START_TAG, ns, VOLUME_USAGE_ENTRY);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            
            //Log.d(DEBUG_TAG, "readUsage() > Tag: " + tag.toString());
            
            // Starts by looking for the entry tag
            if (tag.equals(DAY_HOUR_TAG)) {
            	Log.d(DEBUG_TAG, "In Tag3: " + tag.toString());
           		usage.add(readUsage(parser));
            } else {
                skip(parser);
            }
        }
        return usage;
    }

    // This class represents a single entry (day) in the XML feed.
    public static class DayHour {
    	public final String period;
    	public final String peak;
        public final String offpeak;
        public final String uploads;
        public final String freezone;

        private DayHour(String period, String peak, String offpeak, String uploads, String freezone) {
        	this.period = period;
            this.peak = peak;
            this.offpeak = offpeak;
            this.uploads = uploads;
            this.freezone = freezone;
        }
    }

    // Parses the contents of an entry.
    private DayHour readUsage(XmlPullParser parser) throws XmlPullParserException, IOException {
    	
    	//Log.d(DEBUG_TAG, "readUsage()");
    	
        parser.require(XmlPullParser.START_TAG, ns, DAY_HOUR_TAG);
    	
        String period = parser.getAttributeValue(ns, PERIOD_KEY);
        String peak = null;
        String offpeak = null;
        String uploads = null;
        String freezone = null;
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            String name = parser.getAttributeName(0);
            
            if (name.equals(PEAK_KEY)){
                peak = readDataUsage(parser,name);
            }
            else if (name.equals(OFFPEAK_KEY)){
                offpeak = readDataUsage(parser,name);
            }
            else if (name.equals(UPLOADS_KEY)){
                uploads = readDataUsage(parser,name);
            }
            else if (name.equals(FREEZONE_KEY)){
            	freezone = readDataUsage(parser,name);
            }
            else{
                skip(parser);
            }

        }
        Log.d(DEBUG_TAG, period + peak + offpeak + uploads + freezone);
        
        return new DayHour(period, peak, offpeak, uploads, freezone);
    }

    // Processes title tags in the feed.
    private String readDataUsage(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
    	
    	Log.d(DEBUG_TAG, "readDataUsage(parser " + name + " )");
    	
        parser.require(XmlPullParser.START_TAG, ns, USAGE_TAG);
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, USAGE_TAG);
        return text;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        
        return result;
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
