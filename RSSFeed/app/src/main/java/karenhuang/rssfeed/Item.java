package karenhuang.rssfeed;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by karenhuang on 20/08/16.
 */
public class Item {
    public final String title;
    public final String link;
    public final String description;
    public final String creator;
    public final String pubDate;
    private static final String ns = null;



    public Item (String title, String link, String description, String creator, String pubDate){
        this.title = title;
        this.link = link;
        this.description = description;
        this.creator = creator;
        this.pubDate = pubDate;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    public Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String link = null;
        String description = null;
        String creator = null;
        String pubDate = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            System.out.println("is not end tag");
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                System.out.println("not start tag");
                continue;
            }

            String name = parser.getName();
             if (name.equals("title")) {
                 title = readTitle(parser);
                 System.out.println(title);
             } else if (name.equals("link")) {
                link = readLink(parser);
                System.out.println(link);
            } else if (name.equals("description")) {
                description = readDescription(parser);
                System.out.println(description);
            } else if (name.equals("dc:creator")) {
                creator = readCreator(parser);
                System.out.println(creator);
            } else if (name.equals("pubDate")) {
                pubDate = readDate(parser);

            } else {
                skip(parser);
            }

        }
        return new Item(title,link, description, creator, pubDate);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes link tags in the feed.
    //For the link tag, the parser extracts data for links by first determining if the link is the kind it's interested in.
    // Then it uses parser.getAttributeValue() to extract the link's value.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
       /* String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
           if (relType.equals("alternate")){
               link = parser.getAttributeValue(null, "href");
                link = readText(parser);
                parser.nextTag();
            } //***************************************************************************************
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }*/
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }


    // Processes description tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }
    // Processes creator tags in the feed
    private String readCreator(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "dc:creator");
        String creator = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "dc:creator");
        return creator;
    }
    // Processes date tags in the feed
    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return date;
    }

    // For the tags title, link, description, creator, date and extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    //Skips tags that are not needed. It throws an exception if current tag is not START_TAG.
    //It consumes the START_TAG and all events up to and including the matching END_TAG.
    //To do this it keeps track of the nesting depth ie. start tag will increment and end tags will decrement 1.
    public void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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
