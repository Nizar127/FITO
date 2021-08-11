package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class qrscanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    ZXingScannerView scannerView;
    DatabaseReference dbref, originalCouponRef;
    String UserID;
    String addData = "";
    int TotalData = 0;
    int newData =0;
    String storeData = "";
    int intialData =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbref= FirebaseDatabase.getInstance().getReference("User").child(UserID);
        Log.d(TAG, "User ID:"+ UserID);



        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }


    @Override
    public void handleResult(Result rawResult) {
        //String data=rawResult.getText().toString();

        String data = rawResult.getText();
        Log.d(TAG, "QR Points:"+ data);
        intialData = Integer.parseInt(data);
        String newPoints = "";
        String initialPoints = "";
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("points",data);
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //DatabaseReference scannerDataRef = FirebaseDatabase.getInstance().getReference("User").child(userID).child("points");

        if(data != null){
            dbref.child("points").setValue(data);
        }else{
            dbref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    HomeActivity.qr.setText(data);
                }
            });
        }

        // scannerView.stopCamera();
        //String newPoints = "";
        //String initialPoints = "";
/*        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference scannerDataRef = FirebaseDatabase.getInstance().getReference("User").child(userID);
        scannerDataRef.child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                scannerDataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "temp data:"+snapshot);

                        Object importData = snapshot.child("points").getValue();
                        if(importData != null){
                            //if data is exist
                            Log.d(TAG, "points 1:"+ importData);
                            String initialPoint ="";
                            initialPoint = importData.toString();
                            addData = initialPoint;
                            //newData = Integer.parseInt(addData);
                            Log.d(TAG, "Big Points:"+ addData);

                        } else{
                                //if data is not exist
                            newData = 0;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        //if add data is not empty, add the previous item
    if(addData != null){
        //add data inside
        scannerView.stopCamera();
        TotalData = Integer.parseInt(addData) + Integer.parseInt(data);
        storeData = String.valueOf(TotalData);
        //once scan, close camera
        Log.d(TAG, "Total Data:"+ storeData);
        //scannerDataRef.child("points").setValue(storeData);
        //HomeActivity.qr.setText(storeData);

    }else{

        //if data is still new
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("points",data);
        scannerDataRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                HomeActivity.qr.setText(data);
            }
        });*/

    }



/*        dbref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                HomeActivity.qr.setText(data);
            }
        });*/





    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
