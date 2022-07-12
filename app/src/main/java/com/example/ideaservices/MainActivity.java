package com.example.ideaservices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
        boolean choice = sp.getBoolean("choice",false);

        if(choice == (true)){
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
            overridePendingTransition(0,0);
        } else {
            Intent i = new Intent(MainActivity.this, FirstActivity.class);
            startActivity(i);
            overridePendingTransition(0,0);
        }




        String[] permission = {
                Manifest.permission.READ_PHONE_NUMBERS
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, 102);
        }


    }

}