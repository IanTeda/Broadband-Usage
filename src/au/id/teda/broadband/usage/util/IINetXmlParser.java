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
import au.id.teda.broadband.usage.util.CopyOfIINetXmlParser.DayHour;

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
    
    
    public String errorCheck (InputStream inputStream) throws XmlPullParserException, IOException {
    	
    	Log.d(DEBUG_TAG, "parse(inputStream)");
    	
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
    
    private String readFeedForError(XmlPullParser parser) throws XmlPullParserException, IOException {
    	
        String error = null;
        
        parser.require(XmlPullParser.START_TAG, ns, FEED_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            
            // Starts by looking for the feed entry tag
        	if (tag.equals(ERROR_TAG)) {
            	Log.d(DEBUG_TAG, "In Tag: " + tag.toString());
            	error = readError(parser);
            } else {
                skip(parser);
            }
        }
        return error;
    }
    
    // Processes title tags in the feed.
    private String readError(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ERROR_TAG);
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, ERROR_TAG);
    	Log.d(DEBUG_TAG, "readError() > " + text);
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
    
    
    /**
     * This class represents an error entry in the xml
     * @author iteda
     *
     */
    public static class ErrorEntry {
    	public final String error;
    	
    	private ErrorEntry (String error){
    		this.error = error;
    	}
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
