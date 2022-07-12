package com.example.ideaservices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ideaservices.databinding.ActivitySproviderProfileBinding;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class SProviderProfileActivity extends AppCompatActivity {

    private DatabaseReference mRef;
    private FirebaseUser fuser;
    private Uri profile_uri,proof_uri;
    private StorageReference mStorage;
    private String[] items = {"Auto Mechanic","Landscaping","Pool Service","Child Care","House Painting","Message Therapy","Graphic Design",
            "House Moving","Computer Repair","Renovation and Interior Design","House Cleaning","Hair Dressing","Gardening","Party Planning","Lawyer",
            "Electric Repair","Painting"};
    private ArrayAdapter<String> adapter;


    private ActivitySproviderProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySproviderProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        adapter = new ArrayAdapter<>(this,R.layout.list_items,items);

        binding.etQualification.setAdapter(adapter);
        binding.etQualification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(SProviderProfileActivity.this, ""+item, Toast.LENGTH_SHORT).show();
            }
        });


        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserDataToDb();

            }
        });




        binding.btnSelectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(SProviderProfileActivity.this)
                        .galleryOnly()
                        .start();
            }
        });

        binding.btnSelectProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(SProviderProfileActivity.this)
                        .galleryOnly()
                        .start();
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(profile_uri == null){
            if(data.getData() != null){
                profile_uri = data.getData();
                Glide.with(SProviderProfileActivity.this).load(profile_uri)
                        .into(binding.profileView);
            } else {
                Glide.with(SProviderProfileActivity.this).load(R.drawable.ic_image)
                        .into(binding.profileView);
            }
        } else {
            if(data.getData() != null){
                proof_uri = data.getData();
                Glide.with(SProviderProfileActivity.this).load(proof_uri)
                        .into(binding.proofView);
            } else {
                Glide.with(SProviderProfileActivity.this).load(R.drawable.ic_image)
                        .into(binding.proofView);
            }
        }

    }
    private void addUserDataToDb() {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put("qualification", binding.etQualification.getText().toString());
        map.put("experience", binding.etYearsOfExperience.getText().toString());
        map.put("address", binding.etCompleteAddress.getText().toString());
        mRef.updateChildren(map);
        uploadUserImages();

    }
    private void uploadUserImages(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                mStorage = FirebaseStorage.getInstance().getReference("ProviderImages");
                final StorageReference fileReference = mStorage.child(System.currentTimeMillis()+".png");
                fileReference.putFile(profile_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {


                                fuser = FirebaseAuth.getInstance().getCurrentUser();
                                mRef = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("profileImage",task.getResult().toString());
                                mRef.updateChildren(map);

                                fileReference.putFile(proof_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {

                                                final Intent i = new Intent(SProviderProfileActivity.this,HomeActivity.class);
                                                startActivity(i);
                                                overridePendingTransition(0,0);

                                                fuser = FirebaseAuth.getInstance().getCurrentUser();
                                                mRef = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                                                HashMap<String,Object> map = new HashMap<>();
                                                map.put("proofImage",task.getResult().toString());
                                                mRef.updateChildren(map);
                                            }
                                        });
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                        binding.proofProgress.setVisibility(View.VISIBLE);
                                        double imgProgress = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                                        binding.proofProgress.setProgress((int) imgProgress);


                                    }
                                });


                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        binding.profileProgress.setVisibility(View.VISIBLE);
                        double imgProgress = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        binding.profileProgress.setProgress((int) imgProgress);

                    }
                });

            }
        }).start();

    }
}