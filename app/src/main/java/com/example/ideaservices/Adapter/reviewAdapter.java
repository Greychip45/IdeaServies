package com.example.ideaservices.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ideaservices.Models.reviewModel;
import com.example.ideaservices.Models.usersModel;
import com.example.ideaservices.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.ViewHolder> {

    Context context;
    ArrayList<reviewModel> rModel;
    private DatabaseReference mRef;

    public reviewAdapter(Context context, ArrayList<reviewModel> rModel) {
        this.context = context;
        this.rModel = rModel;
    }

    @NonNull
    @Override
    public reviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reviews_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull reviewAdapter.ViewHolder holder, int position) {

        reviewModel model = rModel.get(position);
        holder.reviewText.setText(model.getReviewText());
        holder.reviewDate.setText(model.getReviewDate());


        mRef = FirebaseDatabase.getInstance().getReference("Users").child(model.getSenderId());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersModel model = snapshot.getValue(usersModel.class);
                holder.providerName.setText(model.getFirstName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return rModel.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView providerName,reviewText,reviewDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            providerName = itemView.findViewById(R.id.txt_user_name);
            reviewText = itemView.findViewById(R.id.txt_review);
            reviewDate = itemView.findViewById(R.id.txt_date);

        }
    }
}
