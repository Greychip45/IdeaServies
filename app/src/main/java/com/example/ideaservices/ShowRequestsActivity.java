package com.example.ideaservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.ideaservices.Adapter.requestAdapter;
import com.example.ideaservices.Models.NewChatsModel;
import com.example.ideaservices.Models.requestModel;
import com.example.ideaservices.databinding.ActivityShowRequestsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowRequestsActivity extends AppCompatActivity {

    private ArrayList<requestModel> nModel = new ArrayList<>();

    private ActivityShowRequestsBinding binding;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mRef = FirebaseDatabase.getInstance().getReference("Requests");


        LinearLayoutManager lm = new LinearLayoutManager(ShowRequestsActivity.this);
        binding.requestsRecycler.setLayoutManager(lm);
        requestAdapter rAdapter = new requestAdapter(nModel,ShowRequestsActivity.this);
        binding.requestsRecycler.setAdapter(rAdapter);




        String loadType = getIntent().getStringExtra("loadType");

        switch (loadType){
            case "active":

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nModel.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            requestModel model = snapshot1.getValue(requestModel.class);
                            if(model.getServiceStatus().equalsIgnoreCase("active")){
                                nModel.add(model);
                            }

                        }
                        rAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


                break;
            case "pending":

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nModel.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            requestModel model = snapshot1.getValue(requestModel.class);
                            if(model.getServiceStatus().equalsIgnoreCase("pending")){
                                nModel.add(model);
                            }


                        }
                        rAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                break;
            case "complete":

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nModel.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            requestModel model = snapshot1.getValue(requestModel.class);
                            if(model.getServiceStatus().equalsIgnoreCase("complete")){
                                nModel.add(model);
                            }

                        }

                        rAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "rejected":
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nModel.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            requestModel model = snapshot1.getValue(requestModel.class);
                            if(model.getServiceStatus().equalsIgnoreCase("rejected")){
                                nModel.add(model);
                            }

                        }
                        rAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }
    }

}