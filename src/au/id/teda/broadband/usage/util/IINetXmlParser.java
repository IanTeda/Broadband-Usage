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
	
    private static final String ns = null;
    
    private static final String FEED_TAG = "ii_feed";
    private static final String ERROR_TAG = "error";
    private static final String AUTH_ERROR = "Authentication failure";
    private static final String ACCOUNT_INFO_ENTRY = "account_info";
    
    private static final String ENTRY_TAG = "day_hour";
    private static final String PERIOD_TAG = "period";
    private static final String PEAK_TAG = "peak";
    private static final String OFFPEAK_TAG = "offpeak";
    private static final String	UPLOADS_TAG = "uploads";
    private static final String FREEZONE_TAG = "freezone";

    // We don't use namespaces
    
    public List<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
    	
        List<Entry> usage = new ArrayList<Entry>();

        parser.require(XmlPullParser.START_TAG, ns, FEED_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            
            // Starts by looking for the entry tag
            if (name.equals(ENTRY_TAG)) {
            	usage.add(readUsage(parser));
            } else {
                skip(parser);
            }
        }
        return usage;
    }

    // This class represents a single entry (day) in the XML feed.
    public static class Entry {
    	public final String period;
    	public final String peak;
        public final String offpeak;
        public final String uploads;
        public final String freezone;

        private Entry(String period, String peak, String offpeak, String uploads, String freezone) {
        	this.period = period;
            this.peak = peak;
            this.offpeak = offpeak;
            this.uploads = uploads;
            this.freezone = freezone;
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private Entry readUsage(XmlPullParser parser) throws XmlPullParserException, IOException {
    	
        parser.require(XmlPullParser.START_TAG, ns, ENTRY_TAG);
        
        String period = null;
        String peak = null;
        String offpeak = null;
        String uploads = null;
        String freezone = null;
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(PERIOD_TAG)) {
            	period = readPeriod(parser);
            } else if (name.equals(PEAK_TAG)) {
                peak = readPeakData(parser);
            } else if (name.equals(OFFPEAK_TAG)) {
                offpeak = readOffpeakData(parser);
            } else if (name.equals(UPLOADS_TAG)) {
                uploads = readUploadsData(parser);
            } else if (name.equals(FREEZONE_TAG)) {
                freezone = readFreezoneData(parser);
            } else {
                skip(parser);
            }
        }
        return new Entry(period, peak, offpeak, uploads, freezone);
    }

    // Processes title tags in the feed.
    private String readPeriod(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, PERIOD_TAG);
        String period = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, PERIOD_TAG);
        return period;
    }

    // Processes summary tags in the feed.
    private String readPeakData(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, PEAK_TAG);
        String peak = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, PEAK_TAG);
        return peak;
    }
    
    private String readOffpeakData(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, OFFPEAK_TAG);
        String peak = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, OFFPEAK_TAG);
        return peak;
    }
    
    private String readUploadsData(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, UPLOADS_TAG);
        String uploads = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, UPLOADS_TAG);
        return uploads;
    }
    
    private String readFreezoneData(XmlPullParser parser) throws IOException, XmlPullParserException {
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
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
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
