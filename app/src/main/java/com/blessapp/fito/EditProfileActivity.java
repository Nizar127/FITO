package com.blessapp.fito;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    Button editDone;
    ImageView backbtn;
    EditText nameEdit;
    CircleImageView editImg;
    String UserID, downloadImgUrl;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    StorageReference editImgRef;
    Uri imgUri;
    String nameEditTxt;
    DatabaseReference mBase;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        
        editDone = findViewById(R.id.editProfileBtn);
        editImg = findViewById(R.id.mainImgEdit);
        nameEdit = findViewById(R.id.fullNameEdit);
        backbtn = findViewById(R.id.backhomebtn);

        UserID = fAuth.getCurrentUser().getUid();
        mBase = FirebaseDatabase.getInstance().getReference("User");

        editImgRef = FirebaseStorage.getInstance().getReference().child("User Images");

        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(imgUri).setAspectRatio(1,1).start(EditProfileActivity.this);
            }
        });


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        editDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try{
                    UpdateData();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();

            editImg.setImageURI(imgUri);
        }else {
            Toast.makeText(this, "Error occurred, please try again.", Toast.LENGTH_SHORT).show();

        }
    }

    private void UpdateData() {
        nameEditTxt = nameEdit.getText().toString().trim();

        if(imgUri == null){
            Toast.makeText(this, "User image required", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(nameEditTxt)){
            Toast.makeText(this, "Product image required", Toast.LENGTH_SHORT).show();
        } else{
            StoreUser(nameEditTxt);
        }
    }

    private void StoreUser(String nameEditTxt) {

        final StorageReference filePath = editImgRef.child(imgUri.getLastPathSegment() + UserID + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imgUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(EditProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditProfileActivity.this, "User Image uploaded", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImgUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImgUrl = task.getResult().toString();

                            Toast.makeText(EditProfileActivity.this, "User image url received", Toast.LENGTH_SHORT).show();

                            SaveData(nameEditTxt);
                        }
                    }
                });

            }
        });
    }

    private void SaveData(String nameEditTxt) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("Username", nameEditTxt);
        userMap.put("ImageUser",downloadImgUrl);

        mBase.child(UserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull  Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();

                    Toast.makeText(EditProfileActivity.this,"New Doctor Inserted", Toast.LENGTH_SHORT).show();
                }else{
                    String message = task.getException().toString();
                    Toast.makeText(EditProfileActivity.this,"Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}