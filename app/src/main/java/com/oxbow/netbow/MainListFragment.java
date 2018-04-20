package com.oxbow.netbow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainListFragment extends Fragment {
Context activity;
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
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<Serie> s = new ArrayList<Serie>();
        Drawable d = getResources().getDrawable(R.drawable.shithappens);
        Bitmap b = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bitmapdata = stream.toByteArray();
        Serie serie = new Serie("Black Mirror","adasdasdad",2.0, bitmapdata, "dramat", "sci-fi");
        s.add(serie);
        MainListAdapter adapter = new MainListAdapter(activity,s);
        GridView v = container.findViewById(R.id.seriesList);
        v.setAdapter(adapter);
        return inflater.inflate(R.layout.main_series, container, false);
    }


}
