package com.example.ideaservices.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ideaservices.ChatActivity;
import com.example.ideaservices.Models.NewChatsModel;
import com.example.ideaservices.Models.usersModel;
import com.example.ideaservices.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NewChatsAdapter extends RecyclerView.Adapter<NewChatsAdapter.ViewHolder> {

    ArrayList<NewChatsModel> nModel;
    Context context;
    private DatabaseReference mRef;

    public NewChatsAdapter(ArrayList<NewChatsModel> nModel, Context context) {
        this.nModel = nModel;
        this.context = context;
    }

    @NonNull
    @Override
    public NewChatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_chat_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewChatsAdapter.ViewHolder holder, int position) {

        NewChatsModel model = nModel.get(position);

        holder.tvTime.setText(model.getDateTime());



        mRef = FirebaseDatabase.getInstance().getReference("Users").child(model.getSenderId());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersModel model = snapshot.getValue(usersModel.class);
                holder.userName.setText(model.getFirstName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Glide.with(context).load(R.drawable.user12).placeholder(R.drawable.user12).into(holder.userProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("providerId",model.getSenderId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nModel.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView userName,lastMessage,tvTime;
        private ImageView userProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.txt_user_name);
            lastMessage = itemView.findViewById(R.id.txt_last_message);
            userProfile = itemView.findViewById(R.id.user_profile);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
