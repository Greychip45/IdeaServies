package com.example.ideaservices.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ideaservices.Models.usersModel;
import com.example.ideaservices.ProfileActivity;
import com.example.ideaservices.R;

import java.util.ArrayList;

public class usersAdapter extends RecyclerView.Adapter<usersAdapter.ViewHolder> {

     Context context;
     ArrayList<usersModel> uModel;

    public usersAdapter(Context context, ArrayList<usersModel> uModel) {
        this.context = context;
        this.uModel = uModel;
    }

    @NonNull
    @Override
    public usersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull usersAdapter.ViewHolder holder, int position) {

        usersModel model = uModel.get(position);
        String providerName = model.getFirstName();


        holder.userName.setText(providerName);
        Glide.with(context).load(R.drawable.user12).placeholder(R.drawable.user12).into(holder.userProfileImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("providerName",providerName);
                i.putExtra("providerId",model.getUserId());

                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return uModel.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView userName,distance;
        private ImageView userProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.txt_user_name);
            distance = itemView.findViewById(R.id.txt_last_message);
            userProfileImage = itemView.findViewById(R.id.user_profile);

        }
    }
}
