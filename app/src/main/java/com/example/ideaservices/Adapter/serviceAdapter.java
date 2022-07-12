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
import com.example.ideaservices.ListProvidersActivity;
import com.example.ideaservices.Models.servicesModel;
import com.example.ideaservices.R;

import java.util.ArrayList;

public class serviceAdapter extends RecyclerView.Adapter<serviceAdapter.ViewHolder> {

     Context context;
     ArrayList<servicesModel> sModel;

    public serviceAdapter(Context context, ArrayList<servicesModel> sModel) {
        this.context = context;
        this.sModel = sModel;
    }

    @NonNull
    @Override
    public serviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.service_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull serviceAdapter.ViewHolder holder, int position) {

        servicesModel model = sModel.get(position);
        String serviceName = model.getServiceName();

        holder.serviceName.setText(serviceName);
        Glide.with(context).load(model.getServiceImgUrl()).placeholder(R.drawable.ic_image).into(holder.serviceImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ListProvidersActivity.class);

                i.putExtra("serviceName",serviceName);
                i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return sModel.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView serviceName;
        private ImageView serviceImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceImage = itemView.findViewById(R.id.user_profile);
            serviceName = itemView.findViewById(R.id.txt_user_name);

        }
    }
}
