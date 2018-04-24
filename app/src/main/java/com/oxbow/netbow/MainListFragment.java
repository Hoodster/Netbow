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
    GridView gridView;
    public MainListFragment() {
    
    }
    
    public static MainListFragment newInstance() {
        MainListFragment fragment = new MainListFragment();
        return fragment;
    }
    
    private void findViewById(View v) {
        
        gridView = v.findViewById(R.id.seriesList);
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
        List<Serie> s = new ArrayList<Serie>();
        Drawable d = getResources().getDrawable(R.drawable.shithappens);
        Bitmap b = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bitmapdata = stream.toByteArray();
        Serie serie = new Serie("Black Mirror","Trzy historie osadzone w przyszłości, opowiedziane w trzech odcinkach. Każda z nich opowiada o innych przykrych konsekwencjach rozwoju naszej cywilizacji, pokazując w sposób karykaturalny do czego może dojść jeśli nic się nie zmieni.",2.0, bitmapdata, "dramat", "sci-fi");
        Serie serie1 = new Serie("Black Mirror","Trzy historie osadzone w przyszłości, opowiedziane w trzech odcinkach. Każda z nich opowiada o innych przykrych konsekwencjach rozwoju naszej cywilizacji, pokazując w sposób karykaturalny do czego może dojść jeśli nic się nie zmieni. W każdej mamy do czynienia z różnym problemem społeczno-obyczajowym osadzonym w różnej futurystycznej perspektywie czasowej. W serialu zostaje poruszony między innymi problem przekraczania barier obyczajowych w imię sztuki, zgubnego wpływu nowych technologii na życie osobiste, czy podporządkowania życia regułom telewizji i portali społecznościowych, gdzie nic już nie jest prawdziwe. ",2.0, bitmapdata, "dramat", "sci-fi");
        s.add(serie);
        s.add(serie1);
        MainListAdapter adapter = new MainListAdapter(activity,s);
        gridView = view.findViewById(R.id.seriesList);
        gridView.setAdapter(adapter);
        return view;
    }
    


}
