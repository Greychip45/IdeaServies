package com.example.ideaservices.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ideaservices.Adapter.NewChatsAdapter;
import com.example.ideaservices.Models.NewChatsModel;
import com.example.ideaservices.R;
import com.example.ideaservices.databinding.FragmentMessageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageFragment extends Fragment {


    private FragmentMessageBinding binding;
    private ArrayList<NewChatsModel> nModel = new ArrayList<>();
    private FirebaseUser fuser;
    private DatabaseReference mRef;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(getLayoutInflater());

        loadUsers();

        return binding.getRoot();
    }
    private void loadUsers(){

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        binding.messageRecycler.setLayoutManager(lm);
        NewChatsAdapter nAdapter = new NewChatsAdapter(nModel,getContext());
        binding.messageRecycler.setAdapter(nAdapter);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("ChatIds");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nModel.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NewChatsModel model = dataSnapshot.getValue(NewChatsModel.class);
                        nModel.add(model);
                }
                nAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}