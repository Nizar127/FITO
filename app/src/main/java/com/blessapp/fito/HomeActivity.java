package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class HomeActivity extends AppCompatActivity {

    String userID;
    Button scannerMe;
    public static TextView qr;
    TextView powerpoint, distanceID, oil_valueID;
    BottomNavigationView bottomtabmenu;
    DatabaseReference ref, checkData, testOil, testdistance;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        qr = findViewById(R.id.qrtext);
        distanceID = findViewById(R.id.distanceID);
        oil_valueID = findViewById(R.id.oil_valueID);

        powerpoint = findViewById(R.id.pinpointID);




        //GET ID of user
       userID = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "query:"+ userID);

        ref = FirebaseDatabase.getInstance().getReference("User").child(userID);

        try{
           // checkingData();
            checkingOil();
            checkingDistance();

        }catch (Exception data){
            data.printStackTrace();
            Log.d(TAG, "Error: "+data);
            Toast.makeText(getApplicationContext(), "No data there", Toast.LENGTH_SHORT).show();
        }


        Query qrcode = ref.child(userID);
        Log.d(TAG, "query:"+ qrcode);


        scannerMe = findViewById(R.id.scanbutton);

        bottomtabmenu = findViewById(R.id.bottom_navigation);
        bottomtabmenu.setSelectedItemId(R.id.home);


        //getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, new HomeActivity()).commit();

        scannerMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, qrscanner.class);
                startActivity(intent);
            }
        });

        bottomtabmenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.points:
                        startActivity(new Intent(getApplicationContext(), PointsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.rewards:
                        startActivity(new Intent(getApplicationContext(), RewardsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profileID:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.more:
                        startActivity(new Intent(getApplicationContext(), MoreActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });


    }

    private void checkingDistance() {
        testdistance = FirebaseDatabase.getInstance().getReference("distance");
        testdistance.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    testdistance.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Object distance = snapshot.getValue();
                            if(distance != null){
                                distanceID.setText(distance.toString()+ " "+"CM");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void checkingOil() {
        testOil = FirebaseDatabase.getInstance().getReference("distance");
        testOil.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    testOil.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Object oilvalue = snapshot.getValue();
                            if(oilvalue != null){
                                oil_valueID.setText(oilvalue.toString()+ " "+"ML");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void checkingData() {
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
                                powerpoint.setText(importData.toString());
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}
