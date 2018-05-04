package com.oxbow.netbow.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.support.v7.widget.SearchView;
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

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchFragment extends Fragment {
    SearchView searchView;
    List<Serie> series = new ArrayList<>();
    GridView gridView;
    ProgressBar progressBar;
    Context activity;
    public SearchFragment() {
    
    }
    
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }
    
    private void findViewById(View v) {
        searchView = v.findViewById(R.id.seriesSeachView);
        gridView = v.findViewById(R.id.searchResults);
        progressBar = v.findViewById(R.id.progessResults);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_activity,container,false);
        findViewById(view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                String textquery = newText.replace(" ","%20");
                String query = "https://api.themoviedb.org/3/search/tv?api_key=99453365bfe4540972684d60cf0c1b02&language=pl&query="+textquery+"&page=1";
                new getResults(query).execute();
                return false;
            }
    
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }
    private class getResults extends AsyncTask<Void,Void,Void> {
        String url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        public getResults(String url) {
            super();
            this.url = url;
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
                    series.clear();
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
