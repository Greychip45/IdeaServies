package com.example.ideaservices.Adapter;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ideaservices.Models.requestModel;
import com.example.ideaservices.Models.usersModel;
import com.example.ideaservices.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class requestAdapter extends RecyclerView.Adapter<requestAdapter.ViewHolder> {

    ArrayList<requestModel> rModel;
    Context context;
    private DatabaseReference mRef;
    private FirebaseUser fuser;
    private int NORMAL_VIEW_TYPE = 1;
    private int COMPLETED_VIEW_TYPE = 2;


    public requestAdapter(ArrayList<requestModel> rModel, Context context) {
        this.rModel = rModel;
        this.context = context;
    }

    @NonNull
    @Override
    public requestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.service_request_view,parent,false);
            return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull requestAdapter.ViewHolder holder, int position) {

        holder.tvStatus.setVisibility(View.GONE);

        requestModel model = rModel.get(position);

        holder.dateTime.setText(model.getDateSent());
        holder.serviceDescription.setText(model.getServiceDescription());
        holder.serviceDescription.setVisibility(View.GONE);



        if(model.getServiceStatus().equalsIgnoreCase("complete")){
            holder.btnReject.setVisibility(View.GONE);
            holder.btnAccept.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.VISIBLE);

        }else if(model.getServiceStatus().equalsIgnoreCase("active")){
            holder.btnAccept.setVisibility(View.GONE);
        } else if(model.getServiceStatus().equalsIgnoreCase("rejected")){
            holder.btnReject.setVisibility(View.GONE);
        }


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

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fuser = FirebaseAuth.getInstance().getCurrentUser();
                mRef = FirebaseDatabase.getInstance().getReference("Requests").child(fuser.getUid());
                HashMap<String,Object> map = new HashMap<>();
                map.put("serviceStatus","active");
                mRef.updateChildren(map);
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fuser = FirebaseAuth.getInstance().getCurrentUser();
                mRef = FirebaseDatabase.getInstance().getReference("Requests").child(fuser.getUid());
                HashMap<String,Object> map = new HashMap<>();
                map.put("serviceStatus","rejected");
                mRef.updateChildren(map);
            }
        });

        holder.cL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = (holder.serviceDescription.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                int i = (holder.tvMore.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                TransitionManager.beginDelayedTransition(holder.cL,new AutoTransition());
                holder.serviceDescription.setVisibility(v);
                holder.tvMore.setVisibility(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rModel.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView userName,location,dateTime,serviceDescription,tvStatus,tvMore;
        ImageView userProfile;
        Button btnReject,btnAccept;
        ConstraintLayout cL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMore = itemView.findViewById(R.id.tv_more);
            tvStatus = itemView.findViewById(R.id.tv_status);
            userName = itemView.findViewById(R.id.tv_username);
            location = itemView.findViewById(R.id.tv_location);
            userProfile = itemView.findViewById(R.id.img_profile);
            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnReject = itemView.findViewById(R.id.btn_reject);
            cL = itemView.findViewById(R.id.constraitLay);
            dateTime = itemView.findViewById(R.id.tv_date_time);
            serviceDescription = itemView.findViewById(R.id.tv_request_description);

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (rModel.get(position).getServiceStatus().equals("completed")){
            return NORMAL_VIEW_TYPE;
        } else{
            return COMPLETED_VIEW_TYPE;
        }
    }
}
