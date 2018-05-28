package com.oxbow.netbow.data;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Serie {
    public String serieId;
    public String serieTitle;
    public String originalTitle;
    public String serieDescription;
    public float serieRating;
    public Bitmap seriePoster;
    public Bitmap serieBackground;
    public String firstGenre;
    public String secondGenre;
    public String originCountry;
    public String originLanguage;
    public ArrayList<String> producers;
    public ArrayList<String> genres;
    public String homepage;
    public int episodesNumber;
    public int seriesNumber;


    public Serie() {}

    public Serie(String title, String description, float rating, Bitmap poster, String genre1, String genre2) {
        this.serieTitle = title;
        this.serieDescription = description;
        this.serieRating = rating;
        this.seriePoster = poster;
        this.firstGenre = genre1;
        this.secondGenre = genre2;
    }
    public Serie(String title, String originaltitle, String description, float rating, Bitmap poster, Bitmap background, String genre1, String genre2) {
        this.serieTitle = title;
        this.originalTitle = originaltitle;
        this.serieDescription = description;
        this.serieRating = rating;
        this.seriePoster = poster;
        this.serieBackground = background;
        this.firstGenre = genre1;
        this.secondGenre = genre2;
    }
    
    
}
