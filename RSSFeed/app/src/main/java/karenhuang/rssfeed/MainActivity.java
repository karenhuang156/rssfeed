package karenhuang.rssfeed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String URL = "http://www.smh.com.au/rssheadlines/top.xml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //WebView webview = new WebView(this);

        //setContentView(webView);
        //webView = (WebView) findViewById(R.id.webview);
        loadSmhFeed();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Use Asyntask to download the XML Feed from SMH.
    public void loadSmhFeed(){
        //System.out.println("loadSMHFeed");

        new DownloadFeed2().execute(URL);
        //System.out.println(URL);
    }









    private class DownloadFeed2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //System.out.println("New DL2");
                return loadXml(urls[0]);
            } catch (IOException e){
                return null;
            }   catch (XmlPullParserException e) {
                return null;
            }

        }
        @Override
        protected void onPostExecute(String result) {
                setContentView(R.layout.activity_main);
                //displays html string in UI via a WebView
            //TextView textView = (TextView) findViewById(R.id.textView);
           // textView.setText(result);
                WebView webView = (WebView) findViewById(R.id.webView);
                webView.loadData(result, "text/html", null);

        }


        private String loadXml(String urlString) throws XmlPullParserException, IOException {
            System.out.println(urlString);
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
                //displayString.append(item.title);
            }
           // System.out.println(displayString.toString());
            return displayString.toString();
        }

        // Given a string representation of a URL, sets up a connection and gets
        // an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            java.net.URL url = new URL(urlString);
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
}
