package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blessapp.fito.model.coupon;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Coupon_Redemption extends AppCompatActivity {

    TextView points;
    RecyclerView recyclerView;
    couponlistAdapter Adapter;
    DatabaseReference mBase, couponDB;
    FirebaseDatabase firebaseDatabase;
    coupon coupon;
    String userID;
   // ImageView coupon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon__redemption);
/*        coupon[] mycoupon = new coupon[]{
          new coupon("Supreme Jacket","android.R.mipmap.ic_launcher", "200", "Supreme", "Spend RM 50 at one pump","supreme@gmail.com","Soft Great Jacker","8 September 2020","",R.drawable.background),
                new coupon("Supreme Jacket","android.R.mipmap.ic_launcher", "200", "Supreme", "Spend RM 50 at one pump","supreme@gmail.com","Soft Great Jacker","8 September 2020","",R.drawable.background),
                new coupon("Supreme Jacket","android.R.mipmap.ic_launcher", "200", "Supreme", "Spend RM 50 at one pump","supreme@gmail.com","Soft Great Jacker","8 September 2020","",R.drawable.background),
                new coupon("Supreme Jacket","android.R.mipmap.ic_launcher", "200", "Supreme", "Spend RM 50 at one pump","supreme@gmail.com","Soft Great Jacker","8 September 2020","",R.drawable.background),
                new coupon("Supreme Jacket","android.R.mipmap.ic_launcher", "200", "Supreme", "Spend RM 50 at one pump","supreme@gmail.com","Soft Great Jacker","8 September 2020","",R.drawable.background),

        };*/

        userID = FirebaseAuth.getInstance().getUid();
        coupon = new coupon();
        mBase = FirebaseDatabase.getInstance().getReference();
        couponDB = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("points");
        getData();


        //coupon = findViewById(R.id.couponBtn);
        points = findViewById(R.id.pointID);
        recyclerView = findViewById(R.id.recyclerviewSystem);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

      FirebaseRecyclerOptions<coupon> options = new FirebaseRecyclerOptions.Builder<coupon>()
                .setQuery(mBase, coupon.class)
                .build();

        Adapter = new couponlistAdapter(options);
        //Adapter = new couponAdapter(mycoupon);
        recyclerView.setAdapter(Adapter);
        Adapter.notifyDataSetChanged();


    }

    private void getData() {



/*        couponDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // String value = snapshot.getValue(String.class);
               // points.setText(value);
                if(userID.equals(snapshot.getKey())){
                    String value = snapshot.getValue(String.class);
                    points.setText(snapshot.getChildren().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Coupon_Redemption.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });*/
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
