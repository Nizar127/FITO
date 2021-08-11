package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blessapp.fito.model.coupon;
import com.blessapp.fito.model.myviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class Coupon_Redemption extends AppCompatActivity {

    TextView points;
    RecyclerView recyclerView;
    couponlistAdapter Adapter;
    DatabaseReference mBase, couponDB, checkData, theDb;
    FirebaseDatabase firebaseDatabase;
    coupon coupon;
    String userID;
    ImageView backpressBtn;

   // ImageView coupon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon__redemption);


        backpressBtn = findViewById(R.id.backpressed);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Coupon");
        String key = dbRef.child("Coupon").push().getKey();
        Log.d(TAG, "query:"+ key);
        //coupon = findViewById(R.id.couponBtn);
        points = findViewById(R.id.pointID);

        backpressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        userID = FirebaseAuth.getInstance().getUid();
        coupon = new coupon();


        mBase = FirebaseDatabase.getInstance().getReference("Coupon");
        theDb =  FirebaseDatabase.getInstance().getReference("Coupon").child(key);
        //couponDB = FirebaseDatabase.getInstance().getReference("User").child(userID);
        try{
            checkData = FirebaseDatabase.getInstance().getReference("User").child(userID);
            checkData.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.getResult().exists()){
                        Log.d(TAG, "check data:"+ checkData);
                        checkData.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Object importData = dataSnapshot.child("points").getValue();
                                if(importData != null){
                                    points.setText(importData.toString());

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });

        }catch (Exception e){
            Log.d(TAG, "Error: "+e);
        }

        Log.d(TAG, "user:"+ userID);


        //getData();


        recyclerView = findViewById(R.id.recyclerviewSystem);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
      FirebaseRecyclerOptions<coupon> options = new FirebaseRecyclerOptions.Builder<coupon>()
                //.setQuery(mBase, GenericTypeIndicator.class)
              .setQuery(mBase, new SnapshotParser<com.blessapp.fito.model.coupon>() {
                  @NonNull
                  @Override
                  public com.blessapp.fito.model.coupon parseSnapshot(@NonNull DataSnapshot snapshot) {
                      coupon coupondata = new coupon();
                      coupondata.setName(snapshot.child("Coupon_name").getValue().toString());
                      coupondata.setSponsoredName(snapshot.child("Sponsored_Name").getValue().toString());
                      coupondata.setSponsoredHighlight(snapshot.child("Coupon_Highlight").getValue().toString());
                      coupondata.setImage(snapshot.child("image").getValue().toString());
                      coupondata.setPoints(snapshot.child("Points").getValue().toString());
                      return coupondata;
                  }
              })
                .build();


        Adapter = new couponlistAdapter(options);
       // Adapter = new couponlistAdapter();
       recyclerView.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();


    }

        @Override
    protected void onStop() {
        super.onStop();
        Adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Adapter.startListening();
    }
}
