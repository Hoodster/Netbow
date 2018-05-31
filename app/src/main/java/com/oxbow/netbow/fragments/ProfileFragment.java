package com.oxbow.netbow.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.oxbow.netbow.R;
import com.oxbow.netbow.SerieDetails;
import com.oxbow.netbow.adapters.MainListAdapter;
import com.oxbow.netbow.data.Serie;
import com.oxbow.netbow.tmdb.TMDdConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    SearchView searchView;
    List<Serie> series = new ArrayList<>();
    GridView gridView;
    ProgressBar progressBar;
    Context activity;
    public ProfileFragment() {
    
    }
    
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }
    
    private void findViewById(View v) {
    
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_activity, container, false);
        findViewById(view);
        return view;
    }
    
   
}
