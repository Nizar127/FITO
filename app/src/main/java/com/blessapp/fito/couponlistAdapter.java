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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    String bigPoints = "";
    String smallPoints = "";
    String storeData = "";
    int totalData = 0;
    Boolean completeSearch = false;
    Boolean completeSearch2 = false;
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
        /*holder.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),couponDetailActivity.class);
                Log.d(TAG, "intent:"+ intent);
                String detailKey = getRef(position).getKey();
                intent.putExtra("pid", model.getPid());
                intent.putExtra("key", detailKey);
                v.getContext().startActivity(intent);
                //startActivity();

            }
        });*/


        holder.redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence theoptions[] = new CharSequence[]{
                        "Yes",
                        "No",
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Are you sure you want to redeem this point?");
                builder.setItems(theoptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //if yes, then button will be disable
                            //get data from user
                            //get data from coupon
                            //create calculation
                            //save into database
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User");
                            String userData = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    Log.d(TAG, "user data is it exist"+ task);
                                    if(task.getResult().exists()){

                                        Log.d(TAG, "user task exist:"+ task);
                                        userRef.child(userData).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Log.d(TAG, "user snapshot:"+ snapshot);
                                                Object startData = snapshot.child("points").getValue();
                                                if(startData != null){
                                                    Log.d(TAG, "yeahdata:"+ startData);
                                                    String currentPoint ="";
                                                    currentPoint = startData.toString();
                                                    initialData = currentPoint;

                                                    Calendar calendar = Calendar.getInstance();

                                                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                                    String saveCurrentDate = currentDate.format(calendar.getTime());

                                                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                                    String saveCurrentTime = currentTime.format(calendar.getTime());

                                                    //To create a unique product random key, so that it doesn't overwrite other product
                                                    String productRandomKey = saveCurrentDate + saveCurrentTime;


                                                    DatabaseReference tempdata = FirebaseDatabase.getInstance().getReference("calculateData").child(productRandomKey).child(userData);
                                                    HashMap<String, Object> usercalcMap = new HashMap<>();
                                                    usercalcMap.put("first_points",initialData);

                                                    tempdata.updateChildren(usercalcMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Log.d(TAG, "Data Inserted:"+ task);

                                                        }
                                                    });

                                                    Log.d(TAG, "latest Initial Data:"+ initialData);
                                                    //therealPoint.setText(importData.toString());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                }
                            });

                            DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference("Coupon");
                            //String key = dbRef.child("Coupon").push().getKey();
                            String key = getRef(position).getKey();
                            Log.d(TAG, "query:"+ key);
                            couponRef.child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.getResult().exists()){
                                        Log.d(TAG, "taskexist:"+ task);

                                        couponRef.child(key).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Log.d(TAG, "search coupon:"+snapshot);
                                                Object importData = snapshot.child("Points").getValue();
                                                if(importData != null){
                                                    Log.d(TAG, "importdata:"+ importData);
                                                    String initialPoint ="";
                                                    initialPoint = importData.toString();
                                                    currentData = initialPoint;

                                                    Calendar calendar = Calendar.getInstance();

                                                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                                    String saveCurrentDate = currentDate.format(calendar.getTime());

                                                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                                    String saveCurrentTime = currentTime.format(calendar.getTime());

                                                    //To create a unique product random key, so that it doesn't overwrite other product
                                                    String productRandomKey = saveCurrentDate + saveCurrentTime;

                                                    DatabaseReference tempdata = FirebaseDatabase.getInstance().getReference("calculateData").child(productRandomKey).child(userData);
                                                    HashMap<String, Object> usercalcMap = new HashMap<>();
                                                    usercalcMap.put("second_points",currentData);

                                                    tempdata.updateChildren(usercalcMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Log.d(TAG, "Data Inserted Secondary:"+ task);
                                                        }
                                                    });

                                                    Log.d(TAG, "latest current data:"+ currentData);

                                                    tempdata.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                            if(task.getResult().exists()){
                                                                Log.d(TAG, "latest data:"+ task);
                                                                tempdata.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        Log.d(TAG, "temp data:"+snapshot);
                                                                        Object firstPoints = snapshot.child("first_points").getValue();
                                                                        Object secondPoints = snapshot.child("second_points").getValue();
                                                                        if(firstPoints != null && secondPoints != null){
                                                                            Log.d(TAG, "points 1:"+ firstPoints);
                                                                            Log.d(TAG, "points 2:"+ secondPoints);

                                                                            String initialPoint ="";
                                                                            initialPoint = firstPoints.toString();
                                                                            bigPoints = initialPoint;
                                                                            Log.d(TAG, "Big Points:"+ bigPoints);


                                                                            String secondaryPoint = "";
                                                                            secondaryPoint = secondPoints.toString();
                                                                            smallPoints = secondaryPoint;
                                                                            Log.d(TAG, "Small Points:"+ smallPoints);

                                                                            if(Integer.parseInt(bigPoints) >= Integer.parseInt(smallPoints)){

                                                                                totalData = Integer.parseInt(bigPoints) - Integer.parseInt(smallPoints);
                                                                                storeData = String.valueOf(totalData);
                                                                                Log.d(TAG, "Total Data:"+ storeData);

                                                                                String theuserData = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                                Log.d(TAG, "The User Data:"+ theuserData);
                                                                                DatabaseReference userInitialRef = FirebaseDatabase.getInstance().getReference("User").child(theuserData);
                                                                                userInitialRef.child("points").setValue(storeData);

                                                                                Toast.makeText(v.getContext(), "Redeem Successful", Toast.LENGTH_SHORT).show();

                                                                                String CouponDataKey = getRef(position).getKey();
                                                                                DatabaseReference getCouponRef =FirebaseDatabase.getInstance().getReference("Coupon").child(CouponDataKey);
                                                                                getCouponRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                                        if(task.getResult().exists()){
                                                                                            getCouponRef.addValueEventListener(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                                    String getIDData = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                                                                    Object Highlight = snapshot.child("Coupon_Highlight").getValue();
                                                                                                    Object SponsoredName = snapshot.child("Sponsored_Name").getValue();
                                                                                                    Object points = snapshot.child("Points").getValue();
                                                                                                    Object Name = snapshot.child("Coupon_name").getValue();
                                                                                                    Object Contact = snapshot.child("Coupon_Contact").getValue();
                                                                                                    Object Term = snapshot.child("Coupon_Term").getValue();
                                                                                                    Object Validity = snapshot.child("Coupon_Validity").getValue();
                                                                                                    Object Image = snapshot.child("image").getValue();

                                                                                                    DatabaseReference redeemPointsRef = FirebaseDatabase.getInstance().getReference("User").child(theuserData).child("redeem_points");
                                                                                                    HashMap<String, Object> productMap = new HashMap<>();
                                                                                                    productMap.put("pid", getIDData);
                                                                                                    productMap.put("image", Image);
                                                                                                    productMap.put("Coupon_name", Name);
                                                                                                    productMap.put("Coupon_Highlight", Highlight);
                                                                                                    productMap.put("Coupon_Term", Term);
                                                                                                    productMap.put("Coupon_Contact", Contact);
                                                                                                    productMap.put("Sponsored_Name", SponsoredName);
                                                                                                    productMap.put("Coupon_Validity", Validity);
                                                                                                    productMap.put("Points", points);

                                                                                                    redeemPointsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            holder.redeem.setEnabled(false);

                                                                                                        }
                                                                                                    });
                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                });

/*                                                                                HashMap<String, Object> userReadMap = new HashMap<>();
                                                                                userReadMap.put("points",storeData);

                                                                                userInitialRef.updateChildren(userReadMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        Toast.makeText(v.getContext(), "Redeem Successful", Toast.LENGTH_SHORT).show();
                                                                                        holder.redeem.setEnabled(false);
                                                                                    }
                                                                                });*/
                                                                            }else{
                                                                                Toast.makeText(v.getContext(), "Your Points Are Not Enough", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                    }}

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });

                                                    //therealPoint.setText(importData.toString());
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
                        if(which == 1){
                            Toast.makeText(v.getContext(), "Redeem Cancelled", Toast.LENGTH_SHORT).show();

                        }
                        if(which == 2){
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Coupon");
                            //String key = dbRef.child("Coupon").push().getKey();
                            String key = getRef(position).getKey();
                            Log.d(TAG, "query:"+ key);
                            dbRef.child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.getResult().exists()){
                                        dbRef.child(key).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Object importData = snapshot.child("Points").getValue();
                                                if(importData != null){
                                                    Log.d(TAG, "importdata:"+ importData);

                                                    //therealPoint.setText(importData.toString());
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


