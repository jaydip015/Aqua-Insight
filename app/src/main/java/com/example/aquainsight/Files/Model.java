package com.example.aquainsight.Files;

public class Model {
    String Address;
    String Headline;
    Float latitude;
    Float longitude;
    String ImgLink;
    String Issue;
    int Reviewed;

    public Model(String address, String headline, Float latitude, Float longitude, String imgLink, String issue, int reviewed) {
        Address = address;
        Headline = headline;
        this.latitude = latitude;
        this.longitude = longitude;
        ImgLink = imgLink;
        Issue = issue;
        Reviewed = reviewed;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getHeadline() {
        return Headline;
    }

    public void setHeadline(String headline) {
        Headline = headline;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getImgLink() {
        return ImgLink;
    }

    public void setImgLink(String imgLink) {
        ImgLink = imgLink;
    }

    public String getIssue() {
        return Issue;
    }

    public void setIssue(String issue) {
        Issue = issue;
    }

    public int getReviewed() {
        return Reviewed;
    }

    public void setReviewed(int reviewed) {
        Reviewed = reviewed;
    }
}
