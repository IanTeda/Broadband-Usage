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
    private static final String PERIOD_TAG = "period";
    private static final String PEAK_TAG = "peak";
    private static final String OFFPEAK_TAG = "offpeak";
    private static final String	UPLOADS_TAG = "uploads";
    private static final String FREEZONE_TAG = "freezone";

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
            
            Log.d(DEBUG_TAG, "readFeed() > Tag: " + tag.toString());
            
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
            
            Log.d(DEBUG_TAG, "readVolumeUsage() > Tag: " + tag.toString());
            
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
            
            Log.d(DEBUG_TAG, "readUsage() > Tag: " + tag.toString());
            
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
    	
    	Log.d(DEBUG_TAG, "readUsage()");
    	
        String period = null;
        String peak = null;
        String offpeak = null;
        String uploads = null;
        String freezone = null;

        parser.require(XmlPullParser.START_TAG, ns, DAY_HOUR_TAG);
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            String tag = parser.getName();
            
            Log.d(DEBUG_TAG, "readUsage() > Tag: " + tag.toString());
            
            if (tag.equals(PERIOD_TAG)) {
            	//period = readPeriod(parser);
            	period = "period2";
            } else if (tag.equals(PEAK_TAG)) {
                //peak = readPeakData(parser);
            	peak = "peak2";
            } else if (tag.equals(OFFPEAK_TAG)) {
                //offpeak = readOffpeakData(parser);
            	offpeak = "offpeak2";
            } else if (tag.equals(UPLOADS_TAG)) {
                //uploads = readUploadsData(parser);
            	uploads = "uploads2";
            } else if (tag.equals(FREEZONE_TAG)) {
                //freezone = readFreezoneData(parser);
            	freezone = "freezone2";
            } else {
                skip(parser);
            }
        }
        return new DayHour(period, peak, offpeak, uploads, freezone);
    }

    // Processes title tags in the feed.
    private String readPeriod(XmlPullParser parser) throws IOException, XmlPullParserException {
    	
    	Log.d(DEBUG_TAG, "IINetXmlParser.readPeriod()");
    	
        parser.require(XmlPullParser.START_TAG, ns, PERIOD_TAG);
        String period = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, PERIOD_TAG);
        return period;
    }

    // Processes summary tags in the feed.
    private String readPeakData(XmlPullParser parser) throws IOException, XmlPullParserException {
    	
    	Log.d(DEBUG_TAG, "IINetXmlParser.readPeakData()");
    	
        parser.require(XmlPullParser.START_TAG, ns, PEAK_TAG);
        String peak = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, PEAK_TAG);
        return peak;
    }
    
    private String readOffpeakData(XmlPullParser parser) throws IOException, XmlPullParserException {
    	
    	Log.d(DEBUG_TAG, "IINetXmlParser.readOffpeakData()");
    	
        parser.require(XmlPullParser.START_TAG, ns, OFFPEAK_TAG);
        String peak = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, OFFPEAK_TAG);
        return peak;
    }
    
    private String readUploadsData(XmlPullParser parser) throws IOException, XmlPullParserException {
    	
    	Log.d(DEBUG_TAG, "IINetXmlParser.readUploadsData()");
    	
        parser.require(XmlPullParser.START_TAG, ns, UPLOADS_TAG);
        String uploads = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, UPLOADS_TAG);
        return uploads;
    }
    
    private String readFreezoneData(XmlPullParser parser) throws IOException, XmlPullParserException {
    	
    	Log.d(DEBUG_TAG, "IINetXmlParser.readFreezoneData()");
    	
        parser.require(XmlPullParser.START_TAG, ns, FREEZONE_TAG);
        String freezone = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, FREEZONE_TAG);
        return freezone;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        
        Log.d(DEBUG_TAG, "IINetXmlParser.readText( " + result + " )");
        
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
