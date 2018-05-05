package com.oxbow.netbow.tmdb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class TMDdConnect {
    private static final String TAG = TMDdConnect.class.getSimpleName();
    
    public TMDdConnect() {
    
    }
    
    //Method sends URL service request
    public String callService(String reqUrl) throws MalformedURLException, ProtocolException {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
    
            InputStream input = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(input);
        } catch (IOException ignored) { }
        return response;
    }
    
    private String convertStreamToString(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader((new InputStreamReader(input)));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            input.close();
        }
        return sb.toString();
    }
    
    //Method sets genre based on its ID from The Movie Database
    public String getGenre(int id) {
        String genre = null;
        switch (id) {
            case 28:
                genre = "akcja";
                break;
            case 12:
                genre = "przygodowy";
                break;
            case 16:
                genre = "animacja";
                break;
            case 35:
                genre = "komedia";
                break;
            case 80:
                genre = "kryminał";
                break;
            case 99:
                genre = "dokument";
                break;
            case 18:
                genre = "dramat";
                break;
            case 10751:
                genre = "familijny";
                break;
            case 14:
                genre = "fantasy";
                break;
            case 36:
                genre = "historyczny";
                break;
            case 27:
                genre = "horror";
                break;
            case 10402:
                genre = "muzyczny";
                break;
            case 9648:
                genre = "tajemnica";
                break;
            case 10749:
                genre = "romans";
                break;
            case 878:
                genre = "sci-fi";
                break;
            case 10770:
                genre = "film tv";
                break;
            case 53:
                genre = "thriller";
                break;
            case 10752:
                genre = "wojenny";
                break;
            case 37:
                genre = "western";
                break;
                
        }
        return genre;
    }
    
    //Method is getting an image from URL jpg and turns it into bitmap
    public Bitmap loadImageFromWeb(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream is = connection.getInputStream();
            Bitmap b = BitmapFactory.decodeStream(is);
            return b;
        } catch (Exception e) {
            return null;
        }
    }
    
    //When overview has more than 214 characters app is looking forward closest dot and ends overview with sentence which dot has ended.
    public String cutOverview(String overview) {
        String newOverview = overview;
        
        if (overview.length() > 214) {
          int count = overview.lastIndexOf(".",214);
          newOverview = overview.substring(0, count + 1);
        }
        return newOverview;
    }
}
