package karenhuang.rssfeed;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by karenhuang on 20/08/16.
 */
public class SmhXmlParser {
    private static final String ns = null;
    public static List<Item> entries = new ArrayList();

//Instantiate parser. In this case, the parser does nto take namespaces and uses the InputStream as unput.
// It begins the parsing process by calling nextTag() and invokes readFeed()
    public List parse(InputStream in) throws XmlPullParserException, IOException {
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
    //This method processes the feed. It looks for elements tagged <entry> for recursively processing the feed.
    //If tag does not have entry tag, it skips it. readFeed()returns a List of the entires (containing it's data members.
    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        //List entries = new ArrayList();

        parser.next();
        //parser.require(XmlPullParser.START_TAG, ns, "channel");
        //parser.require(XmlPullParser.START_TAG, ns, "link");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            //System.out.println(parser.getName());
            // Starts by looking for the entry tag
            Item item = new Item("","","","","");
            if (name.equals("item")) {

                entries.add(item.readItem(parser));
            } else {
                item.skip(parser);
            }
        }
        //MainActivity.itemList = entries;
        return entries;

    }



}
