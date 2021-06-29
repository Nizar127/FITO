package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RewardsActivity extends AppCompatActivity {

    BottomNavigationView bottomtabmenu;
    Button rewardsBtn;
    ImageView couponBtn;
    CardView earnBox, redeemBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        bottomtabmenu = findViewById(R.id.bottom_navigation);
        bottomtabmenu.setSelectedItemId(R.id.frag_rewards);
        couponBtn = findViewById(R.id.couponBtn);

        earnBox = findViewById(R.id.earnbox);
        redeemBox = findViewById(R.id.redeembox);

        couponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardsActivity.this, Coupon_Redemption.class);
                startActivity(intent);
            }
        });

        //rewardsBtn = findViewById(R.id);

        bottomtabmenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.frag_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.frag_points:
                        startActivity(new Intent(getApplicationContext(), PointsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.frag_more:
                        startActivity(new Intent(getApplicationContext(), MoreActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.frag_rewards:
                        return true;
                }
                return false;
            }
        });
    }


}
