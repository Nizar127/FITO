package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
    LinearLayout redeemClick;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        bottomtabmenu = findViewById(R.id.bottom_navigation);
        bottomtabmenu.setSelectedItemId(R.id.more);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        redeemClick = findViewById(R.id.redeemSystem);

        redeemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreActivity.this, redeemActivity.class);
                startActivity(intent);
            }
        });

        //coupon = findViewById(R.id.couponBtn);






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
                    case R.id.profileID:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.more:
                        return true;
                }
                return false;
            }
        });
    }


}
