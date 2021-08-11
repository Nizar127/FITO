package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.blessapp.fito.model.couponUsed;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class redeemActivity extends AppCompatActivity {

    TextView displayed;
    TextView pointMore;
    RecyclerView recyclerViewMore;
    CouponUsedAdapter couponAdapter;
    couponUsed couponUse;
    DatabaseReference mBase, couponDB, checkData;
    FirebaseDatabase firebaseDatabase;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        //coupon = findViewById(R.id.couponBtn);
        pointMore = findViewById(R.id.pinpointID);

        userID = FirebaseAuth.getInstance().getUid();
        //coupon = new coupon();
        couponUse = new couponUsed();

        mBase = FirebaseDatabase.getInstance().getReference("User").child(userID).child("redeem_points");
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
                                    pointMore.setText(importData.toString());

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

        displayed = findViewById(R.id.morewords);

        recyclerViewMore = findViewById(R.id.recyclerviewMore);
        recyclerViewMore.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<couponUsed> options = new FirebaseRecyclerOptions.Builder<couponUsed>()
                .setQuery(mBase, couponUsed.class)
                .build();


        couponAdapter = new CouponUsedAdapter(options);
        recyclerViewMore.setAdapter(couponAdapter);
        couponAdapter.notifyDataSetChanged();

    }
    @Override
    protected void onStart() {
        super.onStart();
        couponAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        couponAdapter.stopListening();
    }
}
