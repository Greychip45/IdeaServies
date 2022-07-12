package com.example.ideaservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.example.ideaservices.Models.usersModel;
import com.example.ideaservices.databinding.ActivityEmailLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmailLoginActivity extends AppCompatActivity {

    private ActivityEmailLoginBinding binding;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    final private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private DatabaseReference mRef;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.errorText.setVisibility(View.GONE);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("A moment...");

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.etEmail.getText().toString().trim().equals("")){
                    binding.errorText.setText("Email cannot be empty");
                    binding.errorText.setVisibility(View.VISIBLE);
                } else if(!binding.etEmail.getText().toString().trim().matches(emailPattern)){
                    binding.errorText.setText("Invalid email");
                    binding.errorText.setVisibility(View.VISIBLE);
                } else if(binding.etPassword.getText().toString().trim().equals("")){
                    binding.errorText.setText("Password cannot be empty");
                    binding.errorText.setVisibility(View.VISIBLE);
                }  else{
                    login();
                }
            }
        });


        binding.phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                overridePendingTransition(0,0);

            }
        });
    }
    private void login(){
        pDialog.show();

        mAuth.signInWithEmailAndPassword(binding.etEmail.getText().toString().trim(),binding.etPassword.getText().toString().trim())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pDialog.dismiss();
                        mUser = FirebaseAuth.getInstance().getCurrentUser();
                        mRef = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        usersModel model = snapshot.getValue(usersModel.class);
                                        userType = model.getUserType();
                                        if(userType.equals("Customer")){
                                            activityChoice();
                                        } else{
                                            activityChoice1();
                                        }

                                        final Intent i = new Intent(EmailLoginActivity.this,HomeActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(0,0);
                                    }
                                }).start();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                binding.errorText.setText("A fatal exception occurred");
                                binding.errorText.setVisibility(View.VISIBLE);
                                pDialog.dismiss();
                            }
                        });
                    }
                });
    }
    private void activityChoice(){
        SharedPreferences preferences = getSharedPreferences("activity",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("choice", "home");
        editor.apply();

        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor etr = pref.edit();
        etr.putBoolean("choice",true);
        etr.apply();

    }
    private void activityChoice1(){
        SharedPreferences preferences = getSharedPreferences("activity",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("choice", "home1");
        editor.apply();

        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor etr = pref.edit();
        etr.putBoolean("choice",true);
        etr.apply();

    }



}