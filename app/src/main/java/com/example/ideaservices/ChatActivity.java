package com.example.ideaservices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ideaservices.Adapter.messageAdapter;
import com.example.ideaservices.Models.messageModel;
import com.example.ideaservices.databinding.ActivityChatBinding;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private ArrayList<messageModel> mModel = new ArrayList<>();
    private DatabaseReference mRef;
    private FirebaseUser fuser;
    private String dateTime;
    private String providerId;
    private ActivityChatBinding binding;
    private ProgressBar progressBar;
    private Uri uri;
    private StorageReference mStorage;
    private String dbKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        progressBar = findViewById(R.id.progress_bar);
        progressBar = new ProgressBar(getApplicationContext());
        progressBar = new ProgressBar(ChatActivity.this);
        binding.card.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.GONE);



        binding.imgBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.card.setVisibility(View.GONE);
                uri = null;
            }
        });

        binding.textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(ChatActivity.this)
                        .crop()
                        .start();
            }
        });


        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        binding.chatRecycler.setLayoutManager(lm);
        lm.setStackFromEnd(true);
        binding.chatRecycler.setHasFixedSize(true);

        providerId = getIntent().getStringExtra("providerId");




        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy hh:mma");
                dateTime = dateFormat.format(cal.getTime());
                if(uri != null){
                    sendImage();
                } else {
                    sendMessage(dateTime);
                }

                dbKey = null;
            }
        });
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        readMessages(fuser.getUid(),providerId);



    }
    private  void readMessages(String senderId,String receiverId){

        mRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mModel.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    messageModel model = snapshot1.getValue(messageModel.class);
                    if (model.getReceiverId().equals(senderId) && model.getSenderId().equals(receiverId) ||
                            model.getReceiverId().equals(receiverId) && model.getSenderId().equals(senderId)) {
                        isLoading(false);
                        binding.progressBar.setVisibility(View.GONE);
                        mModel.add(model);
                    }

                }
                messageAdapter ma = new messageAdapter(getApplicationContext(),mModel);
                binding.chatRecycler.setAdapter(ma);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void sendMessage(String dateTime){
        fuser =  FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Chats");
        HashMap<String,Object> map = new HashMap<>();
        map.put("message",binding.etMessage.getText().toString());
        map.put("dateTime",dateTime);
        map.put("senderId",fuser.getUid());
        map.put("receiverId",providerId);
        dbKey = mRef.push().getKey();
        mRef.child(dbKey).setValue(map);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mRef = FirebaseDatabase.getInstance().getReference("Users").child(providerId).child("ChatIds");
                HashMap<String,Object> newChat = new HashMap<>();
                newChat.put("senderId",fuser.getUid());
                newChat.put("dateTime",dateTime);
                mRef.updateChildren(newChat);

                mRef = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("ChatIds");
                HashMap<String,Object> onNewChat = new HashMap<>();
                onNewChat.put("senderId",providerId);
                onNewChat.put("dateTime",dateTime);
                mRef.updateChildren(onNewChat);
            }
        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uri = data.getData();
        if(data.getData() != null)  {
            Glide.with(ChatActivity.this).load(uri)
                    .fitCenter()
                    .into(binding.imageView);
            binding.card.setVisibility(View.VISIBLE);
        } else {
            binding.card.setVisibility(View.GONE);
        }
    }
    private void sendImage(){
        mRef = FirebaseDatabase.getInstance().getReference("Chats");
        mStorage = FirebaseStorage.getInstance().getReference("ChatImages");
        final StorageReference fileReference = mStorage.child(System.currentTimeMillis()+".png");
        fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sendMessage(dateTime);
                binding.card.setVisibility(View.GONE);
                fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("sentImage",task.getResult().toString());
                        mRef.child(dbKey).updateChildren(map);

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                final double progress = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.progressBar.setProgress((int) progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, "You might be offline", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void isLoading(boolean isLoading){
        if(isLoading == true){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.GONE);
        }
    }

}