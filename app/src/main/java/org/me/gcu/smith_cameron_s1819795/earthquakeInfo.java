package org.me.gcu.smith_cameron_s1819795;

public class earthquakeInfo {
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String category;
    private String geoLat;
    private String geoLong;

    //constructor method
    public earthquakeInfo() {
    title = "";
    description = "";
    link = "";
    pubDate = "";
    category = "";
    geoLat = "";
    geoLong = "";
    }
    public earthquakeInfo(String titleIn,String descriptionIn, String linkIn, String pubDateIn, String categoryIn, String geoLatIn, String geoLongIn) {
        title = titleIn;
        description = descriptionIn;
        link = linkIn;
        pubDate = pubDateIn;
        category = categoryIn;
        geoLat = geoLatIn;
        geoLong = geoLongIn;
    }
    //Setter methods for the class
    public void setTitle(String newTitle){
        title = newTitle;
    }
    public void setDescription(String newDescription){
        description = newDescription;
    }
    public void setLink(String newLink){
        link = newLink;
    }
    public void setPubDate(String newPubDate){
        pubDate = newPubDate;
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

    public String getCategory() {
        return category;
    }

    public String getGeoLat() {
        return geoLat;
    }

    public String getGeoLong() {
        return geoLong;
    }


    @Override
    public java.lang.String toString() {
        return "earthquakeInfo{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", category='" + category + '\'' +
                ", geoLat='" + geoLat + '\'' +
                ", geoLong='" + geoLong + '\'' +
                '}';
    }
}

