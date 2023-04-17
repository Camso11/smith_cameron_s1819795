package org.me.gcu.smith_cameron_s1819795;




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
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
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

import androidx.appcompat.app.ActionBar;
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
    private Button detailedButton;
    private String result;
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    LinkedList<earthquakeInfo> alist = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab1 = getSupportActionBar();
        ab1.hide();
        // Set up the raw links to the graphical components
        descriptionDisplay = (ListView) findViewById(R.id.list);
        startButton = (Button)findViewById(R.id.startButton);
        detailedButton = (Button)findViewById(R.id.detailedButton);
        strongButton = (Button)findViewById(R.id.strongButton);
        deepestButton = (Button)findViewById(R.id.deepestButton);
        shallowestButton = (Button)findViewById(R.id.shallowestButton);
        nearestButton = (Button)findViewById(R.id.nearestButton);
        dateButton = (Button)findViewById(R.id.dateButton);
        detailedButton.setOnClickListener(this);
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
//displays more detailed info when an arraylist item is clicked
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
    //onClick method tells system what to do in the event of each button being pressed
    public void onClick(View aview)
    {
        //when start button is pressed, runs a check to see if the device is connected to the internet before attempting to download the data
        if (aview == startButton) {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info !=null && info.isConnected()){
                startProgress();
            }
            else{
                //error message for when there is no network connection
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("| Error |").setMessage("No internet connection. Please connect to the internet and try again.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

            }
        }
        //the following else if statements call the relevant method when their respective button is pressed.
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
            directional();
        }
        else
        if (aview == dateButton) {
            date();
        }
        else
            if (aview == detailedButton){

                detailed();
            }
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    }

    //method for locating and displaying the strongest quake on record
    public void strongest()
    {
        //returns an error if the user attempts to run method before downloading the data
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
           //iterates through the arrayList to find the earthquake with the highest magnitude
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
           //outputs the result of the for loop
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
    //method for finding and displaying the closest earthquake to the north. east, south and west of GLasgow.
    public void directional()
    {
        //returns an error if the user attempts to run method before downloading the data
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
            double gLat = 55.86515;
            double gLong = -4.25763;
            double northern = 90;
            int northI = 0;
            double southern = -90;
            int southI = 0;
            double eastern = 180;
            int eastI = 0;
            double western = -180;
            int westI = 0;
            String message = "";
            String tempStr2 = "";
            double tempLat = 0;
            double tempLong = 0;
//iterates through the arrayList and locates the closest earthquake in each direction.
            for (int i = 0; i < alist.size(); i++){
                tempLat = Double.parseDouble(alist.get(i).getGeoLat());
                tempLong = Double.parseDouble(alist.get(i).getGeoLong());

                //finds nearest northern quake
                if(tempLat > gLat && tempLat <= northern) {
                    northI = i;
                    northern = tempLat;
                }
                //finds nearest southern quake
                if(tempLat < gLat && tempLat >= southern) {
                        southI = i;
                        southern = tempLat;

                }
                //finds nearest eastern quake
                if(tempLong > gLong && tempLong <= eastern) {
                    eastI = i;
                    eastern = tempLong;
                    }
                //finds nearest western quake
                if(tempLong < gLong && tempLong >= western) {
                    westI = i;
                    western = tempLong;

                    }
            }
            //outputs the results of the above loop
            message = "Closest Earthquake to the North:\n" + alist.get(northI).detailedDescription() + "\n \n" + "Closest Earthquake to the East:\n" + alist.get(eastI).detailedDescription() + "\n \n" +"Closest Earthquake to the South:\n" + alist.get(southI).detailedDescription() + "\n \n" + "Closest Earthquake to the West:\n" + alist.get(westI).detailedDescription();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("| Nearest Earthquakes to Glasgow |").setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        }
    }
//method for finding the deepest earthquake on record
    public void deepest()
    {
        //returns an error if the user attempts to run method before downloading the data
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
            //iterates through the arrayList and locates the deepest earthquake
            for (int i = 0; i < alist.size(); i++){
                tempStr = alist.get(i).getDepth();
                String[] split = tempStr.split(":");
                tempStr2 = split[1];
                tempStr2 = tempStr2.replaceAll("[km]","");
                tempStr2 = tempStr2.replaceAll("\\s","");
                tempInt =  Integer.parseInt(tempStr2);
             //generates the output string using the records of the deepest quake(s)
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
            //outputs results
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
//method for finding the shallowest earthquake on record
    public void shallowest()
    {
        //returns an error if the user attempts to run method before downloading the data

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

            String output = "";
            int shallowest = 999;
            int shallowI = 0;
            String tempStr = "";
            String tempStr2 = "";
            int tempInt = 0;
            //iterates through the arrayList and locates the shalllowest earthquake

            for (int i = 0; i < alist.size(); i++){
                tempStr = alist.get(i).getDepth();
                String[] split = tempStr.split(":");
                tempStr2 = split[1];
                tempStr2 = tempStr2.replaceAll("[km]","");
                tempStr2 = tempStr2.replaceAll("\\s","");

                tempInt =  Integer.parseInt(tempStr2);
                //generates the output string using the records of the shallowest quake(s)
                if (tempInt < shallowest)
                {
                    shallowest = tempInt;
                    shallowI = i;
                    output = "Earthquake " + alist.get(shallowI).getDepth() + "\n" +"\n" + alist.get(shallowI).detailedDescription();
                }  else
                if (tempInt == shallowest){
                    shallowI = i;
                    output = output + "Earthquake " + alist.get(shallowI).getDepth() + "\n" +"\n" + alist.get(shallowI).detailedDescription();

                }
            }
            //outputs results
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
    //searches for earthquakes that happened on a date matching the user input
    public void date()
    {
        //returns an error if the user attempts to run method before downloading the data
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
//sets the view for the alert window to allow user to input search date
            AlertDialog.Builder search = new AlertDialog.Builder(this);
            search.setTitle("Please enter the date you want to search for:");
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.date_search, null);
            search.setView(view);
            EditText date1 = view.findViewById(R.id.date1);

            search.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                @Override
              //runs method when search button is pressed
                public void onClick(DialogInterface dialog, int which) {
                    String q1 = date1.getText().toString();
                    Date d1 = parseIn(q1);
                    //returns an error if the user attempts to run method with an incorrectly formatted date

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
                    //searches for earthquakes that match the search date
                    else {
                        String output = "";
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        Boolean found = false;
                        String tempStr = "";
                        String tempStr2 = "";
                        double tempDbl = 0.0;
                        for (int i = 0; i < alist.size(); i++) {
                            tempStr = alist.get(i).getMagnitude();
                            String[] split = tempStr.split(":");
                            tempStr2 = split[1];
                            tempDbl =  Double.parseDouble(tempStr2);
                        if (q1.equals(alist.get(i).getFormattedDate()))
                       //colour codes results based on magnitude
                        {
                            if (tempDbl <= 2.5){
                                output = "Earthquake on date " + alist.get(i).getFormattedDate() + "\n" +  alist.get(i).detailedDescription() + "\n" + "\n";
                                SpannableString weakQuake= new SpannableString(output);
                                weakQuake.setSpan(new BackgroundColorSpan(Color.GREEN), 0, output.length(), 0);
                                builder.append(weakQuake);

                            } else if (tempDbl > 2.5 && tempDbl <= 4.0) {
                                output = "Earthquake on date " + alist.get(i).getFormattedDate() + "\n" +  alist.get(i).detailedDescription() + "\n" + "\n";
                                SpannableString midQuake= new SpannableString(output);
                                midQuake.setSpan(new BackgroundColorSpan(Color.YELLOW), 0, output.length(), 0);
                                builder.append(midQuake);

                            } else if (tempDbl > 4.0) {
                                output = "Earthquake on date " + alist.get(i).getFormattedDate() + "\n" +  alist.get(i).detailedDescription() + "\n" + "\n";
                                SpannableString StrongQuake= new SpannableString(output);
                                StrongQuake.setSpan(new BackgroundColorSpan(Color.RED), 0, output.length(), 0);
                                builder.append(StrongQuake);
                            }
                         //   output = output + "Earthquake on date " + alist.get(i).getFormattedDate() + "\n" +  alist.get(i).detailedDescription() + "\n" + "\n";
                            found = true;
                        }

                        }
                        //displays output if no results are found
                        if (found == false) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setTitle("| Error |").setMessage("No earthquakes found on date " + q1).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog a2 = alert.create();
                            a2.show();
                        }
                        else
                            //displays output if results are found
                        if (found == true) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setTitle("Results").setMessage(builder).setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
    //method for detailed search algorithm
    public void detailed()
    {
        //returns an error if the user attempts to run method before downloading the data
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
//assigns view to alert box to allow user input of search query
            AlertDialog.Builder search = new AlertDialog.Builder(this);
            search.setTitle("Please enter your search information");
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.detailed_search, null);
            search.setView(view);
            EditText date1 = view.findViewById(R.id.date1);
            EditText info = view.findViewById(R.id.info);
            search.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                //runs search when button pressed
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String q1 = date1.getText().toString();
                     String q2 = info.getText().toString();
                    Date d1 = parseIn(q1);
                    //throws error if date is formatted wrong
                    if (d1 == null){
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
                        //searches for earthquakes matching search parameters
                        for (int i = 0; i < alist.size(); i++) {
                            if (q1.equals(alist.get(i).getFormattedDate()) && alist.get(i).detailedDescription().contains(q2))
                            {
                                output = output + "Earthquake on date " + alist.get(i).getFormattedDate() + "\n" +  alist.get(i).detailedDescription() + "\n" + "\n";
                                found = true;
                            }

                        }
                        //displays output if no results are found
                        if (found == false) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setTitle("| Error |").setMessage("No earthquakes found on date " + q1).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog a2 = alert.create();
                            a2.show();
                        }
                        else
                            //displays output when results are found
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
    //method for parsing the date from a String type to a Date type
    private Date parseIn(String input){
        //sets format for the date object
        SimpleDateFormat o = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        try {
            //translates String input
            Date temp = o.parse(input);
            return temp;
        }catch (ParseException e){
            return null;
        }

    }

    //(UNUSED) method for checking if a date falls within a certain range
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
                                //creates new earthquake object
                                if (xpp.getName().equalsIgnoreCase("item")) {
                                    earthquake = new earthquakeInfo();
                                }
                                else
                                    //the following statements assign values to the newly created objects based on tags
                                if (xpp.getName().equalsIgnoreCase("title"))
                                {
                                    String temp = xpp.nextText();
                                    Log.e("MyTag","Title: " + temp);
                                    earthquake.setTitle(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("description"))
                                {
                                    String temp = xpp.nextText();
                                    Log.e("MyTag","Description: " + temp);
                                    earthquake.setDescription(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("link"))
                                {
                                    String temp = xpp.nextText();
                                    Log.e("MyTag","Link: " + temp);
                                    earthquake.setLink(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("pubDate"))
                                {
                                    String temp = xpp.nextText();
                                    Log.e("MyTag","pubDate: " + temp);
                                    earthquake.setPubDate(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("category"))
                                {
                                    String temp = xpp.nextText();
                                    Log.e("MyTag","Category: " + temp);
                                    earthquake.setCategory(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("lat"))
                                {
                                    String temp = xpp.nextText();
                                    Log.e("MyTag","geo:lat: " + temp);
                                    earthquake.setGeoLat(temp);
                                }
                                else
                                if (xpp.getName().equalsIgnoreCase("long"))
                                {
                                    String temp = xpp.nextText();
                                    Log.e("MyTag","geo:long: " + temp);
                                    earthquake.setGeoLong(temp);
                                }
                        }
                        else
                            //adds earthquake to ArrayList upon finding end tag
                        if(eventType == XmlPullParser.END_TAG)
                        {
                            if (xpp.getName().equalsIgnoreCase("item"))
                            {
                             //   Log.e("MyTag","widget is " + earthquake.toString());
                                Log.e("TEST", earthquake.toString());
                              //adds new earthquakeInfo object to the Arraylist
                                alist.add(earthquake);

                            }

                        }
                        //iterates pullparser
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


                   descriptionDisplay.post(new Runnable() {
                        public void run() {
                            Log.d("UI thread", "I am the UI thread");
//arrayadapter sets created to display arrayList in the listView
                            ArrayAdapter adapter = new ArrayAdapter<earthquakeInfo>(MainActivity.this, R.layout.activity_listview, alist);
                            descriptionDisplay.setAdapter(adapter);


                        }
                    });
                }



        }
    }