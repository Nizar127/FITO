package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.blessapp.fito.model.coupon;
import com.blessapp.fito.model.couponUsed;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class MoreActivity extends AppCompatActivity {

    BottomNavigationView bottomtabmenu;
    TextView displayed;
    TextView pointMore;
    RecyclerView recyclerViewMore;
    CouponUsedAdapter couponAdapter;
    couponUsed couponUsed;
    DatabaseReference mBase, couponDB, checkData;
    FirebaseDatabase firebaseDatabase;
    com.blessapp.fito.model.coupon coupon;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        bottomtabmenu = findViewById(R.id.bottom_navigation);
        bottomtabmenu.setSelectedItemId(R.id.more);

        //coupon = findViewById(R.id.couponBtn);
        pointMore = findViewById(R.id.pinpointID);

        userID = FirebaseAuth.getInstance().getUid();
        //coupon = new coupon();
        couponUsed = new couponUsed();

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
                .setQuery(mBase, new SnapshotParser<com.blessapp.fito.model.couponUsed>() {
                    @NonNull
                    @Override
                    public com.blessapp.fito.model.couponUsed parseSnapshot(@NonNull DataSnapshot snapshot) {
                        couponUsed coupondata = new couponUsed();
                        //couponUsed coupondata = new couponUsed();
                        coupondata.setName(snapshot.child("Coupon_name").getValue().toString());
                        coupondata.setSponsoredName(snapshot.child("Sponsored_Name").getValue().toString());
                        coupondata.setImage(snapshot.child("image").getValue().toString());
                        coupondata.setPoints(snapshot.child("Points").getValue().toString());
                        return coupondata;
                    }
                })
                .build();

        couponAdapter = new CouponUsedAdapter(options);
        recyclerViewMore.setAdapter(couponAdapter);
        couponAdapter.notifyDataSetChanged();


        bottomtabmenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.points:
                        startActivity(new Intent(getApplicationContext(), PointsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rewards:
                        startActivity(new Intent(getApplicationContext(), RewardsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.more:
                        return true;
                }
                return false;
            }
        });
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
