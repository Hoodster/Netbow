package com.oxbow.netbow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oxbow.netbow.fragments.MainListFragment;
import com.oxbow.netbow.fragments.ProfileFragment;
import com.oxbow.netbow.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public static FirebaseUser currentUser;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("ResourceType")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    manager.beginTransaction()
                            .replace(R.id.frame_container, new MainListFragment())
                            .addToBackStack(null)
                            .commit();
                    return true;
                case R.id.navigation_search:
                    manager.beginTransaction()
                            .replace(R.id.frame_container, new SearchFragment())
                            .addToBackStack(null)
                            .commit();
                    return true;
                case R.id.navigation_recommended:
                    return true;
                case R.id.navigation_profile:
                    manager.beginTransaction()
                            .replace(R.id.frame_container, new ProfileFragment())
                            .addToBackStack(null)
                            .commit();
                    return true;
                
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavViewHelp.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
    

    @Override
    protected void onResume()
    {
        super.onResume();
    }

}
