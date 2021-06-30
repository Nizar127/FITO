package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class Signup extends AppCompatActivity {

    EditText mSignupname,mSignupemail, mSignuppassword;
    Button mContinuebtn;
    TextView mAlreadysignin2;
    DatabaseReference mBase;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    ProgressBar progressBar;
    String username, email, password, productRandomKey, saveCurrentDate,saveCurrentTime, USERID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mSignupname = findViewById(R.id.signupname);
        mSignupemail = findViewById(R.id.signupemail);
        mSignuppassword = findViewById(R.id.signuppassword);
        mContinuebtn = findViewById(R.id.continuebtn);
        mAlreadysignin2 = findViewById(R.id.alreadysignin2);

        mBase = FirebaseDatabase.getInstance().getReference().child("User");

        //final FirebaseAuth fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

/*
        if(fAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
*/

        mContinuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }

        });


    }

    private void SignUp() {
        username = mSignupname.getText().toString().trim();
        email = mSignupemail.getText().toString().trim();
        password = mSignuppassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Username required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password Required", Toast.LENGTH_SHORT).show();
        }

        else if(password.length() <6){
            Toast.makeText(this,"Password need to be longer", Toast.LENGTH_SHORT).show();

        } else{
            storeData();
        }
    }

    private void storeData() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //To create a unique product random key, so that it doesn't overwrite other product
        productRandomKey = saveCurrentDate + saveCurrentTime;



        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("Username",username);
        userMap.put("Email", email);
        //userMap.put("Password", password);

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    USERID = fAuth.getCurrentUser().getUid();
                    Log.d(TAG, "Get Data:" + USERID);

                    Toast.makeText(Signup.this,"User Created", Toast.LENGTH_SHORT).show();


                    //save to firebase
                    mBase.child(USERID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(Signup.this, Login.class);
                                startActivity(intent);
                                finish();
                                //loadingBar.dismiss();
                                Toast.makeText(Signup.this,"User is added successfully..", Toast.LENGTH_SHORT ).show();
                            }else{
                                //loadingBar.dismiss();
                                String message = task.getException().toString();
                                Toast.makeText(Signup.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }


        });
    }

/*    public void login(View view) {
            Intent intent = new Intent(Signup.this, Login.class);
            startActivity(intent);
    }*/
}
