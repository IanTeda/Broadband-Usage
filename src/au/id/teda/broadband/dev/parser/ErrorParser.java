package au.id.teda.broadband.dev.parser;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;
import au.id.teda.broadband.dev.activity.BaseActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import static au.id.teda.broadband.dev.activity.BaseActivity.*;

public class ErrorParser {
	
	private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
    private static final String ns = null; // We don't use namespaces
    private static final String FEED_TAG = "ii_feed";
    private static final String ERROR_TAG = "error";
    private static final String VOLUME_USAGE_TAG = "volume_usage";
    private static final String EXPECTED_TRAFFIC_TYPES_TAG = "expected_traffic_types";
    private static final String CLASSIFICATION_ATT = "classification";
    private static final String TYPE_TAG = "type";
    private static final String ANYTIME_ATT = "anytime";
    private static final String PEAK_ATT = "peak";
    
    /**
     * Method used to check for error text within XML feed.
     * @param inputStream
     * @return string with error text
     * @throws XmlPullParserException
     * @throws IOException
     */
    public String parse (InputStream inputStream) throws XmlPullParserException, IOException {
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
    
    /**
     * Look for error tag in stream
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private String readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        String error = "no errors";
        
        parser.require(XmlPullParser.START_TAG, ns, FEED_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            
            // Starts by looking for the feed entry tag
        	if (tagName.equals(ERROR_TAG)) {
            	error = readError(parser);
            } else if (tagName.equals(VOLUME_USAGE_TAG)){
                readVolumeUsage(parser);
            } else {
                skip(parser);
            }
        }
        return error;
    }
    
    /**
     * Process error tag
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readError(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ERROR_TAG);
        String error = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, ERROR_TAG);
        return error;
    }

    private void readVolumeUsage (XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, VOLUME_USAGE_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tagName = parser.getName();
            if (tagName.equals(EXPECTED_TRAFFIC_TYPES_TAG)){
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
            String tagAtt = parser.getAttributeValue(null, CLASSIFICATION_ATT);

            if (tagName.equals(TYPE_TAG)) {
                if (tagAtt.equals(ANYTIME_ATT)){
                    Log.d(DEBUG_TAG, "Anytime");
                } else {
                    Log.d(DEBUG_TAG, "Peak / Offpeak");
                }
            } else {
                skip(parser);
            }

        }

    }
    
    /**
     * Read text in between tags
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
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
