package com.blessapp.fito;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.fito.model.coupon;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class couponlistAdapter extends FirebaseRecyclerAdapter<coupon, couponlistAdapter.couponViewHolder>{
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    String initialData = "";
    String currentData = "";
    String storeData = "";
    int totalData = 0;
    public couponlistAdapter(@NonNull FirebaseRecyclerOptions<coupon> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull couponViewHolder holder, int position, @NonNull coupon model) {

        holder.Name.setText(model.getName());
        //Log.d(TAG, "query:"+ model.getName());

        holder.SponsoredName.setText(model.getSponsoredName());
        holder.Points.setText(model.getPoints());
        Picasso.get().load(model.getImage()).into(holder.couponImg);
        //String postKey = articleModel.get(position).getKey();
        holder.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),couponDetailActivity.class);
                Log.d(TAG, "intent:"+ intent);
                intent.putExtra("pid", model.getPid());
                v.getContext().startActivity(intent);
                //startActivity();

            }
        });


        holder.redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence theoptions[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Are you sure you want to redeem this point?");
                builder.setItems(theoptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //if yes, then button will be disable
                            Toast.makeText(v.getContext(), "Redeem Successful", Toast.LENGTH_SHORT).show();
                            String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(UserID).child("redeem_points");
                            DatabaseReference userIntialRef = FirebaseDatabase.getInstance().getReference("User").child(UserID);
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Coupon");
                            dbRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    userIntialRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Object importData = snapshot.child("points").getValue();
                                            if(importData != null){
                                                Log.d(TAG, "check data:"+ importData);
                                                initialData = importData.toString();
                                                //therealPoint.setText(importData.toString());

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    dbRef.child(UserID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Object importsecondData = snapshot.child("points").getValue();
                                            if(importsecondData != null){
                                                Log.d(TAG, "check data:"+ importsecondData);
                                                currentData = importsecondData.toString();
                                                //therealPoint.setText(importData.toString());

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    //store new value

                                    if(Integer.parseInt(initialData) >= Integer.parseInt(currentData)){
                                        totalData = Integer.parseInt(initialData) - Integer.parseInt(currentData);
                                    }else{
                                        Toast.makeText(v.getContext(), "Your Total Point are not enough", Toast.LENGTH_SHORT).show();
                                    }
                                    storeData = String.valueOf(totalData);

                                    Object datapoints = snapshot.child("points").getValue();
                                    Object dataCoupon = snapshot.child("Coupon_name").getValue();
                                    Object dataHighlights = snapshot.child("Coupon_Highlight").getValue();
                                    Object dataTerm = snapshot.child("Coupon_Term").getValue();
                                    Object dataContact = snapshot.child("Coupon_Contact").getValue();
                                    Object dataSponsoredName = snapshot.child("Sponsored_Name").getValue();
                                    Object dataValidity = snapshot.child("Coupon_Validity").getValue();
                                    Object dataImage = snapshot.child("image").getValue();
                                    Object pid = snapshot.child("pid").getValue();


                                    HashMap<String, Object> userMap = new HashMap<>();
                                    userMap.put("PID", UserID);
                                    userMap.put("Coupon_name",dataCoupon);
                                    userMap.put("Coupon_Highlight",dataHighlights);
                                    userMap.put("Coupon_Term",dataTerm);
                                    userMap.put("Coupon_Contact",dataContact);
                                    userMap.put("Sponsored_Name",dataSponsoredName);
                                    userMap.put("image",dataImage);
                                    userMap.put("Coupon_Points",datapoints);
                                    userMap.put("Coupon_Validity",dataValidity);
                                    userMap.put("points",storeData);
                                    //userMap.put("Coupon_Points",datapoints)

                                    //update data into it
                                    userRef.child(UserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //disabled button
                                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                            builder.setTitle("Coupon To Be Redeem Has Been Add To Your Profile");
                                            //Toast.makeText(v.getContext(), "Data Has Been Uploaded", Toast.LENGTH_SHORT).show();
                                            holder.redeem.setEnabled(false);
                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                        if(which == 1){
                            Toast.makeText(v.getContext(), "Redeem Cancelled", Toast.LENGTH_SHORT).show();



                        }
                    }
                });
                builder.show();
            }
        });

    }

    @NonNull
    @Override
    public couponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_ui,parent,false);
        return new couponViewHolder(view);
    }

    class couponViewHolder extends RecyclerView.ViewHolder {

        TextView Name, SponsoredName, Points;
        //TextView couponName, sponsoredName, points;
        ImageView couponImg;
        Button redeem, detailBtn;
        public couponViewHolder(@NonNull View itemView) {
            super(itemView);

            couponImg = itemView.findViewById(R.id.couponImage);
            Name = itemView.findViewById(R.id.couponName);
            SponsoredName = itemView.findViewById(R.id.couponSponsoredName);
            Points = itemView.findViewById(R.id.textPoints);
            redeem = itemView.findViewById(R.id.redeemBtn);
            detailBtn = itemView.findViewById(R.id.viewDetailBtn);
        }
    }
}


