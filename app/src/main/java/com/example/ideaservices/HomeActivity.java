package com.example.ideaservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.example.ideaservices.Fragments.ProviderHomeFragment;
import com.example.ideaservices.Fragments.HomeFragment;
import com.example.ideaservices.Fragments.MessageFragment;
import com.example.ideaservices.Fragments.AccountFragment;
import com.example.ideaservices.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {


    ActivityHomeBinding binding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sp = getSharedPreferences("activity",MODE_PRIVATE);
        String choice = sp.getString("choice","");


        if(choice.equals("home")){
            replaceFragments(new HomeFragment());
        } else{
            replaceFragments(new ProviderHomeFragment());
        }




        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                if(choice.equals("home")){
                                    replaceFragments(new HomeFragment());
                                } else{
                                    replaceFragments(new ProviderHomeFragment());
                                }

                            }
                        }).start();

                        break;
                    case R.id.messages:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                replaceFragments(new MessageFragment());
                            }
                        }).start();

                        break;
                    case R.id.account:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                replaceFragments(new AccountFragment());
                            }
                        }).start();

                        break;
                }

                return true;
            }
        });


    }
    private void replaceFragments(Fragment fragment){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout,fragment);
        ft.commit();

    }
}