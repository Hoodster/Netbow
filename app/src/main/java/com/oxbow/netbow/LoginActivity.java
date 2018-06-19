package com.oxbow.netbow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oxbow.netbow.data.User;

import java.nio.charset.StandardCharsets;


public class LoginActivity extends AppCompatActivity {
    EditText loginLogin, loginPassword, registerLogin, registerPassword, registerName;
    CoordinatorLayout coordinatorLayout;
Button login, register, notNetbowUser, fullRegister;
ImageView logo;
private FirebaseAuth auth;
RelativeLayout loginLayout, registerLayout, nameLayout;
DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    
        auth.getInstance();
        
        logo = findViewById(R.id.welcomeLogo);
    
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
    
        //layouts
        loginLayout = findViewById(R.id.loginContent);
        registerLayout = findViewById(R.id.registerContent);
        nameLayout = findViewById(R.id.nameInsert);
    
        //edittexts
        loginLogin = findViewById(R.id.loginLogin);
        loginPassword = findViewById(R.id.passwordLogin);
        
        registerLogin = findViewById(R.id.registerLogin);
        registerPassword = findViewById(R.id.registerPassword);
        
        registerName = findViewById(R.id.newUserName);
        registerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        
            }
    
            @Override
            public void onTextChanged(CharSequence st, int i, int i1, int i2) {
                if (st.length() > 0)
                    fullRegister.setVisibility(View.VISIBLE);
                else
                    fullRegister.setVisibility(View.GONE);
            }
    
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        
        fullRegister = findViewById(R.id.finalRegister);
        fullRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewUserName(registerName.getText().toString());
            }
        });
    
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
                auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(registerLogin.getText().toString(), registerPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerLayout.setVisibility(View.GONE);
                            logo.setVisibility(View.GONE);
                            notNetbowUser.setVisibility(View.GONE);
                            nameLayout.setVisibility(View.VISIBLE);
                            registerName.requestFocus();
                        }
                    }
                });
            }
        });
        
        login = findViewById(R.id.tryLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(loginLogin.getText().toString(), loginPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(coordinatorLayout, "Zalogowałeś się jako " + loginLogin.getText().toString() + ".",Snackbar.LENGTH_LONG).show();
                            //back to main activity
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            //clear stack
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    }
                });
            }
        });
    }
        
        @Override
        protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    
    protected void setNewUserName(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                    
                }
            });
        }
    }
}
