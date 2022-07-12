package com.example.ideaservices.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
        import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

import com.example.ideaservices.Models.messageModel;
import com.example.ideaservices.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.ViewHolder> {

     Context context;
     ArrayList<messageModel> mModel;
     private int SENDER_VIEW_TYPE = 1;
     private int RECEIVER_VIEW_TYPE = 2;
     private FirebaseUser fuser;



    public messageAdapter(Context context, ArrayList<messageModel> mModel) {
        this.context = context;
        this.mModel = mModel;

    }

    @NonNull
    @Override
    public messageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        if(viewType == SENDER_VIEW_TYPE){
            v = LayoutInflater.from(context).inflate(R.layout.chat_item_sender, parent, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.chat_item_receiver, parent, false);
        }
        return new ViewHolder(v);

    }

    @Override
    public int getItemViewType(int position) {

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mModel.get(position).getSenderId().equals(fuser.getUid())){
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull messageAdapter.ViewHolder holder, int position) {

        messageModel model = mModel.get(position);

        holder.textMessage.setText(model.getMessage());
        holder.timeSent.setText(model.getDateTime());




    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
         ImageView textImage;
         TextView textMessage,timeSent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           textImage = itemView.findViewById(R.id.textImage);
           textMessage = itemView.findViewById(R.id.txt_msg);
           timeSent = itemView.findViewById(R.id.txt_time);
           textImage = itemView.findViewById(R.id.textImage);


        }

    }


}