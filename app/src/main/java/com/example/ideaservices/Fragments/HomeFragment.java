package com.example.ideaservices.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ideaservices.Adapter.serviceAdapter;
import com.example.ideaservices.Models.servicesModel;
import com.example.ideaservices.databinding.FragmentHomeBinding;


import java.util.ArrayList;


public class HomeFragment extends Fragment {


    FragmentHomeBinding binding;


    private ArrayList<servicesModel> sModel = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);



        serviceAdapter sAdapter  = new serviceAdapter(getContext(),sModel);
        binding.homeRecycler.setAdapter(sAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false);
        binding.homeRecycler.setLayoutManager(layoutManager);



        sModel.add(new servicesModel("Auto Mechanic"));
        sModel.add(new servicesModel("Landscaping"));
        sModel.add(new servicesModel("Pool Service"));
        sModel.add(new servicesModel("Child Care"));
        sModel.add(new servicesModel("House Painting"));
        sModel.add(new servicesModel("Message Therapy"));
        sModel.add(new servicesModel("Graphic Design"));
        sModel.add(new servicesModel("House Moving"));
        sModel.add(new servicesModel("Computer Repair"));
        sModel.add(new servicesModel("Renovation and Interior Design"));
        sModel.add(new servicesModel("House Cleaning"));
        sModel.add(new servicesModel("Hair Dressing"));
        sModel.add(new servicesModel("Gardening"));
        sModel.add(new servicesModel("Party Planning"));
        sModel.add(new servicesModel("Lawyer"));
        sModel.add(new servicesModel("Electric Repair"));
        sModel.add(new servicesModel("Painting"));

        return binding.getRoot();


    }
}