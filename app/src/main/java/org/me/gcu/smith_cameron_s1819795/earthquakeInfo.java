package org.me.gcu.smith_cameron_s1819795;
//
// Name                 Cameron Smith
// Student ID           s1819795
// Programme of Study   Computing
//
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class earthquakeInfo {
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String category;
    private String geoLat;
    private String geoLong;
    private String depth;
    private String magnitude;
    private String formattedDate;
    private Date parseDate;
    //constructor method
    public earthquakeInfo() {
    title = "";
    description = "";
    link = "";
    pubDate = "";
    category = "";
    geoLat = "";
    geoLong = "";
    depth = "";
    magnitude = "";
    parseDate = null;
    formattedDate = "";
    }

    //Setter methods for the class
    public void setTitle(String newTitle){
        title = newTitle;
    }
    public void setDescription(String newDescription){

        description = newDescription;
        String[] split = description.split(";");
        depth = split[3];
        magnitude = split[4];
    }
    public void setLink(String newLink){
        link = newLink;
    }
    public void setPubDate(String newPubDate){
        pubDate = newPubDate;
        SimpleDateFormat i = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.UK);
        SimpleDateFormat o = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        try {
            Date temp = i.parse(pubDate);
            formattedDate = o.format(temp);
            parseDate = o.parse(formattedDate);
        }catch (ParseException e){
            e.printStackTrace();
        }

    }
    public void setCategory(String newCategory){
        category = newCategory;
    }
    public void setGeoLat(String newGeoLat){
        geoLat = newGeoLat;
    }
    public void setGeoLong(String newGeoLong){
        geoLong = newGeoLong;
    }
    //getter methods for class
    public String getTitle(){
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public Date getParseDate() {return parseDate;}

    public String getCategory() {
        return category;
    }

    public String getGeoLat() {
        return geoLat;
    }

    public String getGeoLong() {
        return geoLong;
    }

    public String getDepth() {return depth;}

    public String getMagnitude() {return magnitude;}

    public String getFormattedDate() {return formattedDate;}

    //detailed string output method
    public String detailedDescription()
    {
        return
                "Title=" + title + "\n" + "Description=" + description + "\n" +"Link=" + link + "\n" +"Date=" + pubDate + "\n" +"Category=" + category + "\n" +"Latitude=" + geoLat + "\n" +"Longitude=" + geoLong ;
    }
//toString method (only outputs title)
    @Override
    public java.lang.String toString() {
        return
                "title=" + title;
    }
}

