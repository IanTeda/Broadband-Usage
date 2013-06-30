package au.id.teda.broadband.usage.parser;

import java.io.IOException;
import java.io.InputStream;

import au.id.teda.broadband.usage.activity.BaseActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class ErrorParser {
	
	//private static final String DEBUG_TAG = BaseActivity.DEBUG_TAG;
	
    private static final String ns = null; // We don't use namespaces
    private static final String FEED_TAG = "ii_feed";
    private static final String ERROR_TAG = "error";
    
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
