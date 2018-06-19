package com.oxbow.netbow.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.oxbow.netbow.MainActivity;
import com.oxbow.netbow.R;
import com.oxbow.netbow.tmdb.TMDdConnect;

public class ProfileFragment extends Fragment {
    ImageView profileImage;
    Toolbar toolbar;
    TMDdConnect tm;
    TextView nameText;
   // Context activity;
    public ProfileFragment() {
    
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }
    
    private void findViewById(View v) {
      
        toolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_profile);
        toolbar.showOverflowMenu();
        toolbar.setTitle("");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
        profileImage = v.findViewById(R.id.profilePicture);
        nameText = v.findViewById(R.id.profileName);
        
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // activity = getActivity();
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        findViewById(view);
        FirebaseUser user = MainActivity.currentUser;
        nameText.setText(user.getDisplayName());
        try {
            profileImage.setImageBitmap(tm.loadImageFromWeb(user.getPhotoUrl().toString()));
        } catch (Exception e) {
            
        
        }
        return view;
    }
    
   
}
