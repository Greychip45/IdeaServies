package com.example.ideaservices;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ideaservices.Adapter.reviewAdapter;
import com.example.ideaservices.Models.reviewModel;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private ImageView messageBtn,addReview;
    private Button requestService;
    private TextView details;
    private ConstraintLayout constraintLayout;
    private TextView qualification,providerName;
    private RecyclerView rView;
    private ArrayList<reviewModel> rModel = new ArrayList<>();
    private int mDate,mMonth,mYear,t1Hour,t1Minute;
    private ViewGroup viewGroup;
    private DatabaseReference mRef;
    private String providerId;
    private FirebaseUser fuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        details = findViewById(R.id.details);
        addReview = findViewById(R.id.btn_add_review);
        constraintLayout = findViewById(R.id.expand_layout);
        providerName = findViewById(R.id.txt_user_name);
        qualification = findViewById(R.id.txt_qualification);
        rView = findViewById(R.id.review_recycler);
        requestService = findViewById(R.id.btn_request_service);
        viewGroup = findViewById(android.R.id.content);

        constraintLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);





        messageBtn = findViewById(R.id.img_btn_message);


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        String providedName = getIntent().getStringExtra("providerName");
        String qfn = ("qualification");
        providerId = getIntent().getStringExtra("providerId");

        providerName.setText(providedName);
        qualification.setText(qfn);

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ChatActivity.class);
                i.putExtra("providerId",providerId);
                startActivity(i);
                overridePendingTransition(0,0);
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL,false);
        rView.setLayoutManager(layoutManager);

        mRef = FirebaseDatabase.getInstance().getReference("Reviews");


        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rModel.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    reviewModel model = snapshot1.getValue(reviewModel.class);
                    rModel.add(model);
                }
                reviewAdapter rAdapter = new reviewAdapter(getApplicationContext(),rModel);
                rView.setAdapter(rAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        requestService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseDateEnTime();
            }
        });

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                View v = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.review_dialog,viewGroup,false);

                Button btnReview =v.findViewById(R.id.btn_submit);
                EditText reviewText = v.findViewById(R.id.review_text);
                RatingBar ratingBar = v.findViewById(R.id.ratingBar);

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy hh:mma");
                String dateTime = dateFormat.format(cal.getTime());



                mRef = FirebaseDatabase.getInstance().getReference("Reviews");
                btnReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<Object,String> map = new HashMap<>();


                        map.put("senderId",fuser.getUid());
                        map.put("reviewDate",dateTime);
                        map.put("review",reviewText.getText().toString());
                        map.put("rating", String.valueOf((ratingBar.getRating())));
                        mRef.push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ProfileActivity.this, "Review submitted", Toast.LENGTH_SHORT).show();
                                reviewText.setText("");
                                ratingBar.setRating(0);
                            }
                        });
                    }
                });

                builder.setCancelable(false);
                builder.setView(v);

                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(true);


            }
        });


    }

    public void expand(View view) {
        int v = (details.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
        TransitionManager.beginDelayedTransition(constraintLayout,new AutoTransition());
        details.setVisibility(v);
    }
    private void choseDateEnTime(){

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        View v = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.booking_details_card,viewGroup,false);
        builder.setCancelable(false);
        builder.setView(v);

        EditText enterDate = v.findViewById(R.id.enter_date);
        EditText enterTime = v.findViewById(R.id.enter_time);
        EditText enterDescription = v.findViewById(R.id.enter_description);
        ImageButton selectDate = v.findViewById(R.id.btn_select_date);
        ImageButton selectTime = v.findViewById(R.id.btn_select_time);
        Button btnSubmit = v.findViewById(R.id.btn_submit);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy hh:mma");
        String dateTime = dateFormat.format(cal.getTime());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef = FirebaseDatabase.getInstance().getReference("Requests");
                if(!enterDate.getText().toString().equals("") &&
                !enterTime.getText().toString().equals("") &&
                !enterDescription.getText().toString().equals("")){

                    HashMap<Object,String> map = new HashMap<>();
                    map.put("dateSent",dateTime);
                    map.put("serviceStatus","pending");
                    map.put("providerId",providerId);
                    map.put("senderId",fuser.getUid());
                    map.put("Date",enterDate.getText().toString());
                    map.put("Time",enterTime.getText().toString());
                    map.put("serviceDescription",enterDescription.getText().toString());
                    mRef.child(providerId).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ProfileActivity.this, "Request sent", Toast.LENGTH_SHORT).show();
                            enterDate.setText("");
                            enterTime.setText("");
                            enterDescription.setText("");

                        }
                    });
                }

            }
        });



        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                mDate = cal.get(Calendar.DATE);
                mMonth = cal.get(Calendar.MONTH);
                mYear = cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        enterDate.setText(date+"/"+month+"/"+year);
                    }
                },mYear,mMonth,mDate);

                datePickerDialog.setCanceledOnTouchOutside(true);
                datePickerDialog.show();

            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        t1Hour = hourOfDay;
                        t1Minute= minute;

                        Calendar cal = Calendar.getInstance();
                        cal.set(0,0,0,t1Hour,t1Minute);
                        enterTime.setText(DateFormat.format("hh:mm a",cal));
                    }
                },12,0,false);
                timePickerDialog.updateTime(t1Hour,t1Minute);
                timePickerDialog.setCanceledOnTouchOutside(true);
                timePickerDialog.show();
            }
        });

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();








    }
}