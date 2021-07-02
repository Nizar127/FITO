package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class couponDetailActivity extends AppCompatActivity {

    TextView cpnName, cpnSponsoredName, cpnTerm, cpnContact, cpnPoints, cpnHighlights, cpnvalidity;
    ImageView cpnImage;
    DatabaseReference ref;
    FirebaseAuth fAuth;
    String userID, getCouponID;
    //private Users mUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);

        cpnName = findViewById(R.id.cpnName);
        cpnSponsoredName = findViewById(R.id.cpnSponsoredName);
        cpnTerm = findViewById(R.id.cpnSponsoredTerm);
        cpnContact = findViewById(R.id.cpnSponsoredContact);
        cpnPoints = findViewById(R.id.cpnPoints);
        cpnHighlights = findViewById(R.id.cpnSponsoredHighlight);
        cpnvalidity = findViewById(R.id.cpnValidity);
        cpnImage = findViewById(R.id.cpnImg);

        getCouponID = getIntent().getStringExtra("pid");
        Log.d(TAG, "Coupon ID:"+ getCouponID);


        getDetails(getCouponID);

        fAuth = FirebaseAuth.getInstance();

        userID = fAuth.getCurrentUser().getUid();

/*        ref = FirebaseDatabase.getInstance().getReference("Coupon");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object Points = snapshot.child("Points").getValue();
                Log.d(TAG, "check data:"+ Points);
                Object Highlight = snapshot.child("Coupon_Highlight").getValue();
                Object SponsoredName = snapshot.child("Sponsored_Name").getValue();
                Object Name = snapshot.child("Coupon_name").getValue();
                Object Contact = snapshot.child("Coupon_Contact").getValue();
                Object Term = snapshot.child("Coupon_Term").getValue();
                Object Validity = snapshot.child("Coupon_Validity").getValue();
                Object Image = snapshot.child("image").getValue();

                if(Points != null && Highlight != null && SponsoredName != null && Name != null && Contact != null && Term != null && Validity != null && Image != null){
                    cpnContact.setText(Contact.toString());
                    cpnName.setText(Name.toString());
                    cpnHighlights.setText(Highlight.toString());
                    cpnSponsoredName.setText(SponsoredName.toString());
                    cpnPoints.setText(Points.toString());
                    cpnTerm.setText(Term.toString());
                    cpnvalidity.setText(Validity.toString());
                    Picasso.get().load(Image.toString()).into(cpnImage);
                }
                //cpnImage.setImageResource(Image).;



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    private void getDetails(String getCouponID) {
       DatabaseReference jav = FirebaseDatabase.getInstance().getReference("Coupon");
        Log.d(TAG, "jav:"+ jav);

        jav.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d(TAG, "coupon:"+ getCouponID);

                if(task.getResult().exists()){
                    jav.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Object Points = dataSnapshot.child("pid").child("Points").getValue();
                            Log.d(TAG, "Points:"+ Points);
                            Object Highlight = dataSnapshot.child("Coupon_Highlight").getValue();
                            Object SponsoredName = dataSnapshot.child("Sponsored_Name").getValue();
                            Object Name = dataSnapshot.child("Coupon_name").getValue();
                            Object Contact = dataSnapshot.child("Coupon_Contact").getValue();
                            Object Term = dataSnapshot.child("Coupon_Term").getValue();
                            Object Validity = dataSnapshot.child("Coupon_Validity").getValue();
                            Object Image = dataSnapshot.child("image").getValue();

                            if(Points != null && Highlight != null && SponsoredName != null && Name != null && Contact != null && Term != null && Validity != null && Image != null){
                                cpnContact.setText(Contact.toString());
                                cpnName.setText(Name.toString());
                                cpnHighlights.setText(Highlight.toString());
                                cpnSponsoredName.setText(SponsoredName.toString());
                                cpnPoints.setText(Points.toString());
                                cpnTerm.setText(Term.toString());
                                cpnvalidity.setText(Validity.toString());
                                Picasso.get().load(Image.toString()).into(cpnImage);
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
