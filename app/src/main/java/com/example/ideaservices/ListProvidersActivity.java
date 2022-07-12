package com.example.ideaservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ideaservices.Adapter.usersAdapter;
import com.example.ideaservices.Models.usersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListProvidersActivity extends AppCompatActivity {

    private ArrayList<usersModel> uModel = new ArrayList<>();
    private RecyclerView rView;
    private TextView providersNear;
    private DatabaseReference mRef;
    private FirebaseUser fuser;
    private ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_providers);

        providersNear = findViewById(R.id.providers_near);

        String serviceName = getIntent().getStringExtra("serviceName");
        providersNear.setText("Providers in your city");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Users");

        rView = findViewById(R.id.provider_recycler);


        pBar = new ProgressBar(ListProvidersActivity.this);
        isLoading(true);

        usersAdapter uAdapter = new usersAdapter(getApplicationContext(),uModel);
        rView.setAdapter(uAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2,RecyclerView.VERTICAL,false);
        rView.setLayoutManager(layoutManager);


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uModel.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    usersModel model = snapshot1.getValue(usersModel.class);
                    if(!model.getUserType().equalsIgnoreCase("Customer") && model.getUserId() != fuser.getUid()
                     && model.getQualification().equalsIgnoreCase(serviceName)){
                        uModel.add(model);
                    }
                    uAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        if(uModel.size() > 0){
            isLoading(false);
        }

    }
    private void isLoading(boolean isLoading){
        if(isLoading){
            pBar.setVisibility(View.VISIBLE);
        }else{
            pBar.setVisibility(View.GONE);
        }
    }
}