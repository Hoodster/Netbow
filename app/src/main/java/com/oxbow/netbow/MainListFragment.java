package com.oxbow.netbow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.oxbow.netbow.adapters.MainListAdapter;
import com.oxbow.netbow.data.Serie;

import java.util.List;


public class MainListFragment extends Fragment {

    public MainListFragment() {
        // Required empty public constructor
    }

    public static MainListFragment newInstance() {
        MainListFragment fragment = new MainListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<Serie> s = null;

        Serie serie = new Serie("Black Mirror","adasdasdad",2.0, null, "dramat", "sci-fi");
        s.add(serie);
        MainListAdapter adapter = new MainListAdapter(getContext(),s);
        GridView v = container.findViewById(R.id.seriesList);
        v.setAdapter(adapter);
        return inflater.inflate(R.layout.main_series, container, false);
    }


}
