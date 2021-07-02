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
    TextView powerpoint;
    BottomNavigationView bottomtabmenu;
    DatabaseReference ref, checkData;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        qr = findViewById(R.id.qrtext);

        powerpoint = findViewById(R.id.pinpointID);



        //GET ID of user
       userID = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "query:"+ userID);

        ref = FirebaseDatabase.getInstance().getReference("User").child(userID);
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

        }catch (Exception e){
            Log.d(TAG, "Error: "+e);
        }






        Query qrcode = ref.child(userID);
        Log.d(TAG, "query:"+ qrcode);

/*       qrcode.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                    String fed = snapshot.child(userID).getKey();
                    if(fed.equals(userID)){
                        String itemQR = snapshot.child("points").getValue(String.class);
                        qr.setText(itemQR);
                        Intent intent = new Intent(getApplicationContext(), RewardsActivity.class);
                        intent.putExtra("points", itemQR);
                    }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });*/

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
}
