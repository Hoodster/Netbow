package com.oxbow.netbow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oxbow.netbow.data.User;

import java.nio.charset.StandardCharsets;


public class LoginActivity extends AppCompatActivity {
    EditText loginLogin, loginPassword, registerLogin, registerPassword, registerName;
    String passwordEncrypted, passwordDecrypted;
    CoordinatorLayout coordinatorLayout;
Button login, register, notNetbowUser;
RelativeLayout loginLayout, registerLayout;
DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        //layouts
        loginLayout = findViewById(R.id.loginContent);
        registerLayout = findViewById(R.id.registerContent);

        //edittexts
        loginLogin = findViewById(R.id.loginLogin);
        loginPassword = findViewById(R.id.passwordLogin);


        registerLogin = findViewById(R.id.registerLogin);
        registerPassword = findViewById(R.id.registerPassword);

       db = FirebaseDatabase.getInstance().getReference("users");

        //change register or login layout and button text
        notNetbowUser = findViewById(R.id.changeView);
        notNetbowUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginLayout.getVisibility() != View.VISIBLE) {
                    loginLayout.setVisibility(View.VISIBLE);
                    registerLayout.setVisibility(View.GONE);
                    notNetbowUser.setText("nie masz konta?");
                } else {
                    loginLayout.setVisibility(View.GONE);
                    registerLayout.setVisibility(View.VISIBLE);
                    notNetbowUser.setText("zaloguj się");
                }
            }
        });

        register = findViewById(R.id.tryRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onClick(View view) {
                //encode password to Base64
                byte[] passwordCode = registerPassword.getText().toString().getBytes();
                passwordEncrypted = Base64.encodeToString(passwordCode,Base64.DEFAULT);

                //write to database
               User e = new User(registerLogin.getText().toString(), passwordEncrypted);
                db.child(registerLogin.getText().toString()).setValue(e);


                //save login as local key value
                SharedPreferences loginPref = getSharedPreferences("loginPrefs",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = loginPref.edit();
                editor.putString("login",registerLogin.getText().toString()).commit();
                editor.commit();

                //back to main activity
               Intent i = new Intent(LoginActivity.this, MainActivity.class);
                //clear stack
               i.putExtra("loginTemp",registerLogin.getText().toString());
               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(i);
            }
        });

        login = findViewById(R.id.tryLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String password = "";
                        String typedpassword = loginPassword.getText().toString();
                        User user = dataSnapshot.child(loginLogin.getText().toString()).getValue(User.class);
                        //if login is valid decode its password
                        if (user != null) {
                            byte[] bytepassword = Base64.decode(user.password, Base64.DEFAULT);
                            password = new String(bytepassword, StandardCharsets.UTF_8);
                        } else {
                            Snackbar.make(coordinatorLayout,"Czas najwyższy zapłacić za ten serwer...", Snackbar.LENGTH_LONG).show();
                        }

                        //password validation
                        if (typedpassword.equals(password)) {
                            //START save login info in app's preferences
                            SharedPreferences loginPref = getSharedPreferences("loginPrefs",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = loginPref.edit();
                            editor.putString("login", loginLogin.getText().toString());
                            editor.commit();
                            //END
                            Snackbar.make(coordinatorLayout, "Zalogowałeś się jako " + loginLogin.getText().toString() + ".",Snackbar.LENGTH_LONG).show();
                            //back to main activity
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            //clear stack
                            i.putExtra("loginTemp", loginLogin.getText().toString());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);

                        } else {
                           Snackbar.make(coordinatorLayout, "Nie ten login lub hasło, zią.", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(coordinatorLayout,"Następnym razem będzie lepiej, wierzę w Ciebie.",Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

    }
}
