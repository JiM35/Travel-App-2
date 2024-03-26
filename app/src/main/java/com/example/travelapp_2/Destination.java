package com.example.travelapp_2;

public class Destination {
    private String title;
    private String location;
    private String pic; // Added field for image path
    private double score; // Added field for score

    public Destination(String title, String location, String pic, double score) {
        this.title = title;
        this.location = location;
        this.pic = pic;
        this.score = score;
    }

    public Destination(String title, String location) {
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getPic() {
        return pic;
    }

    public double getScore() {
        return score;
    }
}
