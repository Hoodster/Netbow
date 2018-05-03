package com.oxbow.netbow.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.oxbow.netbow.R;
import com.oxbow.netbow.adapters.MainListAdapter;
import com.oxbow.netbow.data.Serie;
import com.oxbow.netbow.imdb.TMDdConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;


public class MainListFragment extends Fragment {
Context activity;
    GridView gridView;
    ProgressBar progressBar;
    List<Serie> series = new ArrayList<Serie>();
    String url ="https://api.themoviedb.org/3/discover/tv?api_key=99453365bfe4540972684d60cf0c1b02&language=pl&sort_by=popularity.desc&include_null_first_air_dates=false&page=";
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
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
        
            }
    
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    // check if we reached the top or bottom of the list
                    View v = gridView.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        // reached the top:
                       
                    }
                } else if (totalItemCount - visibleItemCount == firstVisibleItem){
                    View v =  gridView.getChildAt(totalItemCount-1);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                     //   n=n+1;
                    //   new getSeries(n).execute();
                    }
                }
            }
        });
        new getSeries(1).execute();
        new getSeries(2).execute();
        return view;
    }
    
   private class getSeries extends AsyncTask<Void,Void,Void> {
    int p;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        public getSeries(int page) {
            super();
            p = page;
        }
       @Override
       protected Void doInBackground(Void... arg0)  {
           TMDdConnect tmDdConnect = new TMDdConnect();
           String gsonSt = null;
           Serie serie = null;
           try {
               gsonSt = tmDdConnect.callService(url + p);
              
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
                      float rating = Float.parseFloat(String.valueOf(s.getDouble("vote_average")));
                      String poster = "https://image.tmdb.org/t/p/w500" + s.getString("poster_path");
                      String background = "https://image.tmdb.org/t/p/w500" + s.getString("backdrop_path");
                      JSONArray genres = s.getJSONArray("genre_ids");
                      String firstGenre = tmDdConnect.getGenre(genres.getInt(0));
                      
                      serie = new Serie();
                      serie.serieId = id;
                      serie.serieTitle = title;
                      serie.originalTitle = originaltitle;
                      serie.serieDescription = description;
                      serie.serieRating = rating;
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
