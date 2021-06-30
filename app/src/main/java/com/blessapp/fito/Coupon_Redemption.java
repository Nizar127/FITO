package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

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



        //coupon = findViewById(R.id.couponBtn);
        points = findViewById(R.id.pointID);

        userID = FirebaseAuth.getInstance().getUid();
        coupon = new coupon();
        mBase = FirebaseDatabase.getInstance().getReference("Coupon");
        couponDB = FirebaseDatabase.getInstance().getReference("User").child(userID);
        couponDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String thepoint = dataSnapshot.child("points").getValue().toString();
                points.setText(thepoint);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d(TAG, "user:"+ userID);


        //getData();


        recyclerView = findViewById(R.id.recyclerviewSystem);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

      FirebaseRecyclerOptions<coupon> options = new FirebaseRecyclerOptions.Builder<coupon>()
                .setQuery(mBase, coupon.class)
                .build();


        //Adapter = new couponlistAdapter(options);
        //Adapter = new couponAdapter(mycoupon);
      //  recyclerView.setAdapter(Adapter);
        //Adapter.notifyDataSetChanged();


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<coupon> options =
            new FirebaseRecyclerOptions.Builder<coupon>()
            .setQuery(mBase, coupon.class)
            .build();

        FirebaseRecyclerAdapter<coupon, myviewholder> adapter =
                new FirebaseRecyclerAdapter<coupon, myviewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull com.blessapp.fito.model.coupon model) {
                       // holder.Sponsored.
                    }

                    @NonNull
                    @Override
                    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_ui,parent,false);
                        myviewholder holder = new myviewholder(view);
                        return holder;
                    }

                };





    }

    /*    @Override
    protected void onStop() {
        super.onStop();
        Adapter.stopListening();
    }*/

}
