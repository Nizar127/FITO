package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextView signupBtn;
    EditText emailInput, passwordInput;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.loginemail);
        passwordInput = findViewById(R.id.loginpassword);
        loginBtn = findViewById(R.id.loginsignin);
        signupBtn = findViewById(R.id.signUpBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Login.this, Signup.class);
                //startActivity(intent);
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
                Toast.makeText(Login.this,"login WORKING", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void LoginUser() {
        //String username = inputUserName.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        int passLength = password.length();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        }

        else{
            AllowAccessToAccount(email, password);
        }
    }

    private void AllowAccessToAccount(String email, String password) {

        //allow access is use the firebase auth but we need to update the status the logged on status
        //if login, change the logged on to Available..
        // put authlistener to user all the time so user  just need to login once
        // if user not active for 2 days straight, sign user out and change status to Not Available
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(Login.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(Login.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}
