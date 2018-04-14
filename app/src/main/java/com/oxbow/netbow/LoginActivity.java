package com.oxbow.netbow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {
    EditText loginLogin, loginPassword, registerLogin, registerPassword, registerName;
    String passwordEncrypted, passwordDecrypted;
Button login, register, notNetbowUser;
RelativeLayout loginLayout, registerLayout;
DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //layouts
        loginLayout = findViewById(R.id.loginContent);
        registerLayout = findViewById(R.id.registerContent);

        //edittexts
        loginLogin = findViewById(R.id.loginLogin);
        loginPassword = findViewById(R.id.passwordLogin);


        registerLogin = findViewById(R.id.registerLogin);
        registerName = findViewById(R.id.registerName);
        registerPassword = findViewById(R.id.registerPassword);

       db = FirebaseDatabase.getInstance().getReference();

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
               User e = new User(registerLogin.getText().toString(), registerName.getText().toString(), passwordEncrypted);
                db.child("users").child(registerLogin.getText().toString()).setValue(e);
                //save login as local key value
                SharedPreferences loginPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = loginPref.edit();
                editor.putString("login",registerLogin.getText().toString()).commit();

                //back to main activity
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                //clear stack
                i.putExtra("loginTemp",registerLogin.getText().toString());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
