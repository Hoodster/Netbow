package com.oxbow.netbow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
    ImageView background, poster;
    getSeries task;
    Activity activity;
    ProgressBar progressBar;
    TextView fullDescription, fulltitle, countryText, directorsText, episodesText, seriesText, languageText, genresText, originTitleText;
    ToggleButton switchwishlisttoggle, commentsDescription;
    CollapsingToolbarLayout collapsingToolbar;
    RatingBar rating;
    Serie serie = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        collapsingToolbar = findViewById(R.id.toolbar_layout);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        
        fullDescription = findViewById(R.id.fullDescription);
        fulltitle = findViewById(R.id.detailsTitle);

        countryText = findViewById(R.id.originCountryText);
        directorsText = findViewById(R.id.directorsText);
        episodesText = findViewById(R.id.episodesCountText);
        seriesText = findViewById(R.id.serieCountText);
        languageText = findViewById(R.id.originLanguageText);
        genresText = findViewById(R.id.genresText);
        originTitleText = findViewById(R.id.originTitleText);
        
        rating = findViewById(R.id.detailsRating);
        
        progressBar = findViewById(R.id.progress);
        
        background = findViewById(R.id.serieBackground);
        poster = findViewById(R.id.serieDetailsPoster);
        
        switchwishlisttoggle = findViewById(R.id.switchwishlist);
        commentsDescription = findViewById(R.id.commentsAndDescriptionToggle);
        commentsDescription.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    fullDescription.setVisibility(View.VISIBLE);
                } else {
                    fullDescription.setVisibility(View.GONE);
                }
        
            }
        });
        
        
        
        
        String id = getIntent().getExtras().getString("id");
        request = "https://api.themoviedb.org/3/tv/"+id+"?api_key=99453365bfe4540972684d60cf0c1b02&language=pl";
    
        try {
            new getSeries().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
                     int seriesNo = s.getInt("number_of_seasons");
                     int episodesNo = s.getInt("number_of_episodes");
                     String language = s.getJSONArray("languages").getString(0);
                     String originCountry = s.getJSONArray("origin_country").getString(0);
                     JSONArray producers = s.getJSONArray("created_by");
                     JSONArray genreslist = s.getJSONArray("genres");
                     String homepage = s.getString("homepage");
                     
                     
                     
    
                    serie = new Serie();
                    serie.serieId = id;
                    serie.serieTitle = title;
                    serie.originalTitle = originaltitle;
                    serie.serieDescription = description;
                    serie.serieRating = rating;
                    serie.seriePoster = tmDdConnect.loadImageFromWeb(poster);
                    serie.serieBackground = tmDdConnect.loadImageFromWeb(background);
                    serie.episodesNumber = episodesNo;
                    serie.seriesNumber = seriesNo;
                    serie.originLanguage = language;
                    serie.originCountry = originCountry;
                    serie.homepage = homepage;
                    ArrayList<String> producersHelper = new ArrayList<String>();
                    ArrayList<String> genresHelper = new ArrayList<String>();
                    //producers
                    for (int i = 0; i < producers.length(); i++) {
                        JSONObject producersObject =  producers.getJSONObject(i);
                        String producername = producersObject.getString("name");
                        producersHelper.add(producername);
                    }
                    
                    for (int i = 0; i < genres.length(); i++) {
                        JSONObject genresObject = genres.getJSONObject(i);
                        String genrename = genresObject.getString("name");
                        genresHelper.add(genrename);
                        
                    }
                    serie.producers = producersHelper;
                    serie.genres = genresHelper;
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Błąd pobierania z serwera", Toast.LENGTH_LONG).show();
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
            poster.setImageBitmap(result.seriePoster);
            
            getSupportActionBar().setTitle("");
            fullDescription.setText(result.serieDescription);
            fulltitle.setText(result.serieTitle);
            rating.setRating(result.serieRating / 2);
            countryText.setText(result.originCountry);
            languageText.setText(result.originLanguage);
            episodesText.setText(String.valueOf(result.episodesNumber));
            seriesText.setText(String.valueOf(result.seriesNumber));
            originTitleText.setText(result.originalTitle);
            String producersString = null, genreString = null;
            for (int i = 0; i < result.producers.size(); i++) {
                if (i == 0)
                    producersString = result.producers.get(i);
                else
                    producersString = producersString + ("\n" + result.producers.get(i));
            }
            for (int i = 0; i < result.genres.size(); i++) {
                if (i == 0)
                    genreString = result.genres.get(i);
                else
                    genreString = genreString + ("\n" + result.genres.get(i));
            }
            directorsText.setText(producersString);
            genresText.setText(genreString);
            collapsingToolbar.setTitle(result.serieTitle);

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
