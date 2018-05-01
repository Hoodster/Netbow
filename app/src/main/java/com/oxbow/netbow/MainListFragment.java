package com.oxbow.netbow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.oxbow.netbow.adapters.MainListAdapter;
import com.oxbow.netbow.data.Serie;
import com.oxbow.netbow.imdb.TMDdConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;


public class MainListFragment extends Fragment {
Context activity;
    GridView gridView;
    ProgressBar progressBar;
    List<Serie> series = new ArrayList<Serie>();
    String url ="https://api.themoviedb.org/3/discover/tv?api_key=99453365bfe4540972684d60cf0c1b02&language=pl&sort_by=popularity.desc&include_null_first_air_dates=false";
    public MainListFragment() {
    
    }
    
    public static MainListFragment newInstance() {
        MainListFragment fragment = new MainListFragment();
        return fragment;
    }
    
    private void findViewById(View v) {
        
        gridView = v.findViewById(R.id.seriesList);
        progressBar = v.findViewById(R.id.progessSerieList);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_series,container,false);
        findViewById(view);
        new getSeries().execute();
        return view;
    }
    
   private class getSeries extends AsyncTask<Void,Void,Void> {
    
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
       @Override
       protected Void doInBackground(Void... arg0)  {
           TMDdConnect tmDdConnect = new TMDdConnect();
           String gsonSt = null;
           Serie serie = null;
           try {
               gsonSt = tmDdConnect.callService(url);
           } catch (MalformedURLException | ProtocolException e) {
               e.printStackTrace();
           }
           if (gsonSt != null) {
               try {
                   JSONObject jsonObject = new JSONObject(gsonSt);
                   JSONArray results = jsonObject.getJSONArray("results");
                   for (int i = 0; i < results.length(); i ++) {
                       JSONObject s = results.getJSONObject(i);
                      String id = s.getString("id");
                      String title = s.getString("name");
                      String originaltitle = s.getString("original_name");
                      String description = s.getString("overview");
                      double rating = s.getDouble("vote_average");
                      String poster = "https://image.tmdb.org/t/p/w500" + s.getString("poster_path");
                      String background = "https://image.tmdb.org/t/p/w500" + s.getString("backdrop_path");
                      JSONArray genres = s.getJSONArray("genre_ids");
                      String firstGenre = tmDdConnect.getGenre(genres.getInt(0));
                      
                      serie = new Serie();
                      serie.serieId = id;
                      serie.serieTitle = title;
                      serie.originalTitle = originaltitle;
                      serie.serieDescription = description;
                      serie.serieRating =rating;
                      serie.seriePoster = tmDdConnect.loadImageFromWeb(poster);
                      serie.serieBackground = tmDdConnect.loadImageFromWeb(background);
                      serie.firstGenre = firstGenre;
                       if (genres.length() > 1) {
                           String secondGenre = tmDdConnect.getGenre(genres.getInt(1));
                           serie.secondGenre = secondGenre;
                       }
                      series.add(serie);
                   }
               } catch (final JSONException e) {}
               
           } else {
               Toast.makeText(activity, "Błąd pobierania z serwera", Toast.LENGTH_LONG).show();
           }
           return null;
       }
       
       @Override
       protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
           MainListAdapter adapter = new MainListAdapter(activity,series);
           gridView.setAdapter(adapter);
       }
   }
}
