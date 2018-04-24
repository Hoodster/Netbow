package com.oxbow.netbow.data;

import android.util.Base64;

public class Serie {
    public String serieName;
    public String serieDescription;
    public double serieRating;
    public byte[] serieImage;
    public String firstCategory;
    public String secondCategory;


    public Serie() {}

    public Serie(String name, String description, double rating, byte[] image, String firstCat, String secondCat) {
        this.serieName = name;
        this.serieDescription = description;
        this.serieRating = rating;
        this.serieImage = image;
        this.firstCategory = firstCat;
        this.secondCategory = secondCat;
    }
}
