package com.oxbow.netbow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.oxbow.netbow.adapters.MainListAdapter;
import com.oxbow.netbow.data.Serie;
import com.oxbow.netbow.tmdb.TMDdConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SerieDetails extends AppCompatActivity {
    String request = null;
    ImageView background;
    getSeries task;
    Activity activity;
    ProgressBar progressBar;
    TextView fullDescription;
    Serie serie = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  fullDescription = findViewById(R.id.fullDescription);
        progressBar = findViewById(R.id.progress);
        background = findViewById(R.id.serieBackground);
        String id = getIntent().getExtras().getString("id");
        request = "https://api.themoviedb.org/3/tv/"+id+"?api_key=99453365bfe4540972684d60cf0c1b02&language=pl";
    
        try {
            new getSeries().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            
            }
        });
        */
    }
    
  
    
    private class getSeries extends AsyncTask<Void,Void,Serie> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
    
        }
    
        @Override
        protected Serie doInBackground(Void... arg0) {
        
            TMDdConnect tmDdConnect = new TMDdConnect();
            String gsonSt = null;
            Serie serie = null;
            try {
                gsonSt = tmDdConnect.callService(request);
    
            } catch (MalformedURLException | ProtocolException e) {
                e.printStackTrace();
            }
            if (gsonSt != null) {
                try {
                    JSONObject s = new JSONObject(gsonSt);
                    String id = s.getString("id");
                    String title = s.getString("name");
                    String originaltitle = s.getString("original_name");
                    String description = s.getString("overview");
                    float rating = Float.parseFloat(String.valueOf(s.getDouble("vote_average")));
                    String poster = "https://image.tmdb.org/t/p/w500" + s.getString("poster_path");
                    String background = "https://image.tmdb.org/t/p/w500" + s.getString("backdrop_path");
                     JSONArray genres = s.getJSONArray("genres");
                     JSONObject d = genres.getJSONObject(0);
                     String firstGenre = tmDdConnect.getGenre(d.getInt("id"));
    
                    serie = new Serie();
                    serie.serieId = id;
                    serie.serieTitle = title;
                    serie.originalTitle = originaltitle;
                    serie.serieDescription = description;
                    serie.serieRating = rating;
                    serie.seriePoster = tmDdConnect.loadImageFromWeb(poster);
                    serie.serieBackground = tmDdConnect.loadImageFromWeb(background);
                       /* serie.firstGenre = firstGenre;
                        
                        if (genres.length() > 1) {
                            String secondGenre = tmDdConnect.getGenre(genres.getInt(1));
                            if (secondGenre != null)
                                serie.secondGenre = secondGenre;
                               
                        } */
    
    
                } catch (JSONException e) {
    
                }
            } else {
                Toast.makeText(getApplicationContext(), "Błąd pobierania z serwera", Toast.LENGTH_LONG).show();
    
            }
            return serie;
        }
    
        @Override
        protected void onPostExecute(Serie result) {
            progressBar.setVisibility(View.GONE);
            background.setImageBitmap(result.serieBackground);
            getSupportActionBar().setTitle(result.serieTitle);
          //  fullDescription.setText(result.serieDescription);
        }
    }
    
    private void getDominanta(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int dominantColor = palette.getDominantColor(0x000000);
                Window window = activity.getWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(dominantColor);
                }
                
            }
        });
    }
}
