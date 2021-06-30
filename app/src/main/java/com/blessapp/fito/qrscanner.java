package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
    DatabaseReference dbref;
    String UserID;


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
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("points",data);
        dbref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                   HomeActivity.qr.setText(data);
            }
        });
/*        dbref.push().setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //FragmentHome.qr.setText("Data Inserted Successfully");
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String newpoint = dataSnapshot.child("points").getValue().toString();
                        //String getData = snapshot.child(UserID).child("points").getValue(String.class);
                        Log.d(TAG, "Get Data:" + newpoint);
                        HomeActivity.qr.setText(newpoint);
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("points", newpoint);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //HomeActivity.qr.setText("Data Inserted Successfully");
                onBackPressed();
            }
        });*/
    }

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
