package karenhuang.rssfeed;

import android.os.AsyncTask;
import android.webkit.WebView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * Created by karenhuang on 20/08/16.
 */
public class DownloadFeed extends AsyncTask<String, Void, String>{
    @Override
    protected String doInBackground(String... urls) {
       try {
           return loadXml(urls[0]);
       } catch (IOException e){
           return null;
       }   catch (XmlPullParserException e) {
           return null;
       }

    }
    @Override
    protected void onPostExecute(String result) {
        //setContentView(R.layout.content_main){
            //displays html string in UI via a WebView
           // WebView myWebView = (WebView) findViewById(R.id.webview);
            //myWebView.loadData(result, "text/html", null);
        }

    private String loadXml(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        //Instantiate parser
        SmhXmlParser smhParser = new SmhXmlParser();
        List<Item> items = null;

        StringBuilder displayString = new StringBuilder();
        try {
            stream = downloadUrl(urlString);
            items = smhParser.parse(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        for (Item item : items) {
            //append the link
            displayString.append("<p><a href='");
            displayString.append(item.link);
            displayString.append("'>" + item.title + "</a></p>");
            displayString.append("<p>" + item.description + "</p>");
            displayString.append("<p>" + item.creator+ "</p>");
            displayString.append("<p>" + item.date + "</p>");
        }
        return displayString.toString();
        }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();

        //Add in getResponseCode() for logging - refer to slide 32
    }

    }
