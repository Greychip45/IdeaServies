package com.example.ideaservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.ideaservices.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;

    private FirebaseUser mUser;
    private RadioButton radioType,radioGender;
    private ProgressDialog dialog;
    private DatabaseReference mRef;
    private String radioTypeText,radioGenderText;
    final private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.errorMessage.setVisibility(View.GONE);
        radioType = new RadioButton(RegisterActivity.this);
        radioGender = new RadioButton(RegisterActivity.this);


        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        binding.inputPhone.setText(tm.getLine1Number());

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.etFirstName.getText().toString().trim().equals("")){
                    binding.errorMessage.setText("Name cannot be empty!");
                    binding.errorMessage.setVisibility(View.VISIBLE);
                } else if(binding.etLastName.getText().toString().trim().equals("")){
                    binding.errorMessage.setText("Name cannot be empty!");
                    binding.errorMessage.setVisibility(View.VISIBLE);
                }else if(binding.etEmail.getText().toString().trim().equals("")){
                    binding.errorMessage.setText("Email cannot be empty!");
                    binding.errorMessage.setVisibility(View.VISIBLE);
                } else if(!binding.etEmail.getText().toString().trim().matches(emailPattern)){
                    binding.errorMessage.setText("Invalid email");
                    binding.errorMessage.setVisibility(View.VISIBLE);
                }
                else if(binding.inputPhone.getText().toString().trim().equals("")){
                    binding.errorMessage.setText("Phone cannot be empty!");
                    binding.errorMessage.setVisibility(View.VISIBLE);
                } else if(binding.etPassword.getText().toString().trim().equals("")){
                    binding.errorMessage.setText("Password cannot be empty!");
                    binding.errorMessage.setVisibility(View.VISIBLE);
                } else if(binding.etConfirmPassword.getText().toString().trim().equals("")){
                    binding.errorMessage.setText("Password cannot be empty!");
                    binding.errorMessage.setVisibility(View.VISIBLE);
                }else if(!binding.etPassword.getText().toString().trim()
                        .equals(binding.etConfirmPassword.getText().toString().trim())){
                    binding.errorMessage.setText("Passwords do not match");
                    binding.errorMessage.setVisibility(View.VISIBLE);
                } else{
                    performAuth();
                }

                if(radioTypeText.equals("Customer")){
                    activityChoice();

                }else{
                    activityChoice1();
                }


            }
        });
    }
    private void performAuth(){


        dialog = new ProgressDialog(this);
        dialog.setMessage("A moment...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        mAuth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etConfirmPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        dialog.dismiss();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                storeUserDataInDb();
                            }
                        }).start();
                        final Intent i = new Intent(getApplicationContext(),SProviderProfileActivity.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                binding.errorMessage.setText("An unknown error occurred");
                binding.errorMessage.setVisibility(View.VISIBLE);
            }
        });
    }
    private void storeUserDataInDb(){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        final String id = mUser.getUid();

        final String countryCode = binding.countryCodePicker.getDefaultCountryCode();
        mRef = FirebaseDatabase.getInstance().getReference("Users").child(id);

        HashMap<Object,String> hMap  =new HashMap<>();
        hMap.put("userId", (mUser).getUid());
        hMap.put("firstName",binding.etFirstName.getText().toString().trim());
        hMap.put("lastName",binding.etLastName.getText().toString().trim());
        hMap.put("emailAddress",binding.etEmail.getText().toString().trim());
        hMap.put("phone",countryCode+binding.inputPhone.getText().toString().trim());
        hMap.put("gender",radioGenderText);
        hMap.put("userType",radioTypeText);
        mRef.setValue(hMap);


    }

    public void checkButton(View view) {

        int radioId1 = binding.radioGroup.getCheckedRadioButtonId();
        int radioId2 = binding.radioGroup2.getCheckedRadioButtonId();

        radioGender = findViewById(radioId1);
        radioType = findViewById(radioId2);

        radioTypeText = radioType.getText().toString();
        radioGenderText = radioGender.getText().toString();



    }
    private void activityChoice(){
        SharedPreferences preferences = getSharedPreferences("activity",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("choice", "home");
        editor.apply();
    }
    private void activityChoice1() {
        SharedPreferences preferences = getSharedPreferences("activity", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("choice", "home1");
        editor.apply();
    }
}