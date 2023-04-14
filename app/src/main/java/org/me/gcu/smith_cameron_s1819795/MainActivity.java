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
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

//import gcu.mpd.bgsdatastarter.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{
    private TextView rawDataDisplay;
    private ListView descriptionDisplay;
    private Button startButton;
    private Button strongButton;
    private Button deepestButton;
    private Button shallowestButton;
    private Button dateButton;
    private Button nearestButton;
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
        descriptionDisplay = (ListView) findViewById(R.id.list);
        startButton = (Button)findViewById(R.id.startButton);
        strongButton = (Button)findViewById(R.id.strongButton);
        deepestButton = (Button)findViewById(R.id.deepestButton);
        shallowestButton = (Button)findViewById(R.id.shallowestButton);
        nearestButton = (Button)findViewById(R.id.nearestButton);
        dateButton = (Button)findViewById(R.id.dateButton);
        dateButton.setOnClickListener(this);
        deepestButton.setOnClickListener(this);
        nearestButton.setOnClickListener(this);
        shallowestButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
        strongButton.setOnClickListener(this);
        descriptionDisplay.setOnItemClickListener(this);
        // More Code goes here
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("| Earthquake Info |").setMessage(alist.get(position).detailedDescription()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }
    public void onClick(View aview)
    {
        if (aview == startButton) {
            startProgress();
        }
        else
        if (aview == strongButton) {
           strongest();
        }
        else
        if (aview == deepestButton) {
            deepest();
        }
        else
        if (aview == shallowestButton) {
            shallowest();
        }
        else
        if (aview == nearestButton) {
            shallowest();
        }
        else
        if (aview == dateButton) {
            date();
        }
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //
    public void strongest()
    {
        if (alist == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("| Error |").setMessage("Please download the data before searching").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        }
        else {
            Double strongest = 1.1;
            int strongestI = 0;
           String tempStr = "";
           String tempStr2 = "";
           Double tempDbl = 1.1;
           for (int i = 0; i < alist.size(); i++){
               tempStr = alist.get(i).getMagnitude();
               String[] split = tempStr.split(":");
               tempStr2 = split[1];
               tempDbl =  Double.parseDouble(tempStr2);
               if (tempDbl > strongest)
               {
                   strongest = tempDbl;
                   strongestI = i;
               }
            }
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("| Strongest Earthquake |").setMessage(alist.get(strongestI).detailedDescription() + "\n" + alist.get(strongestI).getMagnitude()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        }
    }

    public void deepest()
    {
        if (alist == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("| Error |").setMessage("Please download the data before searching").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        }
        else {
            int deepest = 0;
            int deepI = 0;
            String tempStr = "";
            String tempStr2 = "";
            int tempInt = 0;
            String output = "";
            for (int i = 0; i < alist.size(); i++){
                tempStr = alist.get(i).getDepth();
                String[] split = tempStr.split(":");
                tempStr2 = split[1];
                tempStr2 = tempStr2.replaceAll("[km]","");
                tempStr2 = tempStr2.replaceAll("\\s","");
                tempInt =  Integer.parseInt(tempStr2);
                if (tempInt > deepest)
                {
                    deepest = tempInt;
                    deepI = i;
                    output = "Earthquake " + alist.get(deepI).getDepth() + "\n" +"\n" + alist.get(deepI).detailedDescription();
                }
                else
                    if (tempInt == deepest){
                        deepI = i;
                        output = output + "Earthquake " + alist.get(deepI).getDepth() + "\n" +"\n" + alist.get(deepI).detailedDescription();

                    }
            }
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("| Deepest Earthquake |").setMessage(output).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        }
    }

    public void shallowest()
    {
        if (alist == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("| Error |").setMessage("Please download the data before searching").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        }
        else {
            int shallowest = 999;
            int shallowI = 0;
            String tempStr = "";
            String tempStr2 = "";
            int tempInt = 0;
            for (int i = 0; i < alist.size(); i++){
                tempStr = alist.get(i).getDepth();
                String[] split = tempStr.split(":");
                tempStr2 = split[1];
                tempStr2 = tempStr2.replaceAll("[km]","");
                tempStr2 = tempStr2.replaceAll("\\s","");

                tempInt =  Integer.parseInt(tempStr2);
                if (tempInt < shallowest)
                {
                    shallowest = tempInt;
                    shallowI = i;
                }
            }
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("| Deepest Earthquake |").setMessage(alist.get(shallowI).detailedDescription() + "\n" + alist.get(shallowI).getDepth()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        }
    }
    public void date()
    {
        if (alist == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("| Error |").setMessage("Please download the data before searching").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        }
        else {

            AlertDialog.Builder search = new AlertDialog.Builder(this);
            search.setTitle("Please enter the range of dates you want to search between");
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.date_search, null);
            search.setView(view);
            EditText date1 = view.findViewById(R.id.date1);

            search.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String q1 = date1.getText().toString();
                   // String q2 = date2.getText().toString();
                    Date d1 = parseIn(q1);
                 //   Date d2 = parseIn(q2);
                    if (d1 == null ){
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("| Error |").setMessage("Please enter the date in the correct format.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog a2 = alert.create();
                        a2.show();

                    }
                    else {
                        String output = "";
                        Boolean found = false;
                        for (int i = 0; i < alist.size(); i++) {
                        if (d1.equals(alist.get(i).getFormattedDate()))
                        {
                            output = output + "Earthquake on date " + alist.get(i).getFormattedDate() + "\n" +  alist.get(i).detailedDescription() + "\n" + "\n";
                            found = true;
                        }

                        }
                        if (found == false) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setTitle("| Error |").setMessage("No earthquakes" + alist.get(2).getFormattedDate()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog a2 = alert.create();
                            a2.show();
                        }
                        else
                        if (found == true) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setTitle("Results").setMessage(output).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog a2 = alert.create();
                            a2.show();
                        }
                        }

                }
            });
            search.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = search.create();
            dialog.show();
        }
    }
    private Date parseIn(String input){
        SimpleDateFormat o = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        try {
            Date temp = o.parse(input);
            return temp;
        }catch (ParseException e){
            return null;
        }

    }

    private boolean dateCheck (Date input, Date input2, Date search) {
       // return input.compareTo(search) * search.compareTo(input2) >= 0;
        //return search.after(input) && search.before(input2);
        if ( search.equals(input) ||
                search.equals(input2) ||
                (search.after(input) && search.before(input2) )){
            return true;
        }
        else {
            return false;
        }

    }

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
                            Log.d("UI thread", "I am the UI thread");

                            ArrayAdapter adapter = new ArrayAdapter<earthquakeInfo>(MainActivity.this, R.layout.activity_listview, alist);
                            descriptionDisplay.setAdapter(adapter);

                           // for (int i = 0; i < alist.size(); i++){
                                //rawDataDisplay.append(alist.get(i).getDescription() + "\n" + "\n");
                            //}
                            //   rawDataDisplay.setText(result);
                        }
                    });
                }



        }
    }