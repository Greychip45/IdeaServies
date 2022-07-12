package com.example.ideaservices.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ideaservices.R;
import com.example.ideaservices.ShowRequestsActivity;
import com.example.ideaservices.databinding.FragmentProviderHomeBinding;


public class ProviderHomeFragment extends Fragment {

    FragmentProviderHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProviderHomeBinding.inflate(inflater, container, false);

        binding.layoutActiveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent i = new Intent(getContext(), ShowRequestsActivity.class);
                i.putExtra("loadType","active");
                startActivity(i);
            }
        });

        binding.layoutPendingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent i = new Intent(getContext(), ShowRequestsActivity.class);
                i.putExtra("loadType","pending");
                startActivity(i);
            }
        });

        binding.layoutCompleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent i = new Intent(getContext(), ShowRequestsActivity.class);
                i.putExtra("loadType","complete");
                startActivity(i);
            }
        });

        binding.layoutRejectedRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent i = new Intent(getContext(), ShowRequestsActivity.class);
                i.putExtra("loadType","rejected");
                startActivity(i);
            }
        });

        return binding.getRoot();
    }
}