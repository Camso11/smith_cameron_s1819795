package org.me.gcu.smith_cameron_s1819795;
//TEST

/*  Starter project for Mobile Platform Development in Semester B Session 2018/2019
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 Cameron Smith
// Student ID           s1819795
// Programme of Study   Computing
//

// Update the package name to include your Student Identifier

//import android.support.v7.app.AppCompatActivity;
//import android.support.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

//import gcu.mpd.bgsdatastarter.R;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private TextView rawDataDisplay;
    private Button startButton;
    private String result;
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    LinkedList<earthquakeInfo> alist = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        // More Code goes here
    }

    public void onClick(View aview)
    {
        startProgress();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                //
                // Throw away the first 2 header lines before parsing
                int count = 0;

                //
                //
                //
                while ((inputLine = in.readLine()) != null) {
                   if (count >= 13) {
                        result = result + inputLine;
                        //Log.e("MyTag", inputLine);
                    }
                    count++;
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }

            //
            // Now that you have the xml data you can parse it
            //

                earthquakeInfo earthquake = null;
                result = result.substring(4);
                result = result.replaceAll("geo:", "");
              //  LinkedList<earthquakeInfo> alist = null;
                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new StringReader(result));
                    int eventType = xpp.getEventType();
                    alist  = new LinkedList<earthquakeInfo>();
                    while (eventType != XmlPullParser.END_DOCUMENT)
                    {
                        //activates when start tag located
                        if(eventType == XmlPullParser.START_TAG) {
                         //   if (xpp.getName().equalsIgnoreCase("channel"))
                           // {
                             //   alist  = new LinkedList<earthquakeInfo>();
                            //}
                            //else
                                if (xpp.getName().equalsIgnoreCase("item")) {
                                    earthquake = new earthquakeInfo();
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("title"))
                                {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag","Title: " + temp);
                                    earthquake.setTitle(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("description"))
                                {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag","Description: " + temp);
                                    earthquake.setDescription(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("link"))
                                {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag","Link: " + temp);
                                    earthquake.setLink(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("pubDate"))
                                {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag","pubDate: " + temp);
                                    earthquake.setPubDate(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("category"))
                                {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag","Category: " + temp);
                                    earthquake.setCategory(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("lat"))
                                {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag","geo:lat: " + temp);
                                    earthquake.setGeoLat(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("long"))
                                {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag","geo:long: " + temp);
                                    earthquake.setGeoLong(temp);
                                }
                        }
                        else
                        if(eventType == XmlPullParser.END_TAG)
                        {
                            if (xpp.getName().equalsIgnoreCase("item"))
                            {
                             //   Log.e("MyTag","widget is " + earthquake.toString());
                                Log.e("TEST", earthquake.toString());
                                alist.add(earthquake);

                            }
                            //else
                           // if (xpp.getName().equalsIgnoreCase("channel"))
                            //{
                              //  int size;
                                //size = alist.size();
                                //Log.e("MyTag","number of earthquakes on record is: " + size);
                            //}
                        }
                        eventType = xpp.next();




                    }
                }
                catch (XmlPullParserException ae1)
                {
                    Log.e("MyTag","Parsing error" + ae1.toString());
                }
                catch (IOException ae1)
                {
                    Log.e("MyTag","IO error during parsing");
                }

            Log.e("MyTag","End xml file");
                    // Now update the TextView to display raw XML data
                    // Probably not the best way to update TextView
                    // but we are just getting started !

                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            rawDataDisplay.setText("");
                            Log.d("UI thread", "I am the UI thread");

                            for (int i = 0; i < alist.size(); i++){
                                rawDataDisplay.append(alist.get(i).toString() + "\n" + "\n");
                            }
                         //   rawDataDisplay.setText(result);
                        }
                    });
                }



        }
    }