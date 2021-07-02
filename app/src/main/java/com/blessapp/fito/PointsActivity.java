package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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

public class PointsActivity extends AppCompatActivity {

    BottomNavigationView bottomtabmenu;
    CardView pointsItem, redeemBox, earnBox;
    TextView therealPoint;
    DatabaseReference ref, checkData;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String UserID = "";
    String touch = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        bottomtabmenu = findViewById(R.id.bottom_navigation);
        bottomtabmenu.setSelectedItemId(R.id.points);
        pointsItem = findViewById(R.id.pointsbox);
        redeemBox = findViewById(R.id.redeembox);
        earnBox = findViewById(R.id.earnbox);
        therealPoint = findViewById(R.id.trackpoint);
        UserID = fAuth.getCurrentUser().getUid();
        Log.d(TAG,"USER ID at Points"+ UserID);

        touch = FirebaseDatabase.getInstance().getReference("User").getKey();
        Log.d(TAG,"Test Key at Points"+ touch);



        ref = FirebaseDatabase.getInstance().getReference("User").child(UserID);
        try{
            checkData = FirebaseDatabase.getInstance().getReference("User").child(UserID);
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
                                    therealPoint.setText(importData.toString());
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
/*
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String thepoint = dataSnapshot.child("points").getValue().toString();
                therealPoint.setText(thepoint);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/

/*        ref.child(UserID).child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("Firebase", "Error getting message", task.getException());
                } else{
                    Log.d("Firebase",String.valueOf(task.getResult().getValue()));

                }
            }
        });*/


        ref.child(UserID).child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d(TAG, "rewards:"+ task);
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        Toast.makeText(PointsActivity.this,"Reading Coupon", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String pointRewards = String.valueOf(dataSnapshot.getValue());
                        Log.d(TAG, "point rewards:"+ pointRewards);

                    }
                }
            }
        });



        bottomtabmenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rewards:
                        startActivity(new Intent(getApplicationContext(), RewardsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.more:
                        startActivity(new Intent(getApplicationContext(), MoreActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.points:
                        return true;
                }
                return false;
            }
        });
    }
}
