package com.example.stufun.Login;

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
import android.widget.Toast;

import com.example.stufun.Teacher.HomeActivity;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class TeacherRegister extends AppCompatActivity {

    private EditText nametxt,orgtxt;
    private ImageView profileimg;
    private Uri uri;
    private String myuri = "";
    private static final int GALLERY_PICK = 7;
    private String checker="";
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        initalizeUI();
    }

    private void initalizeUI() {
        
        email = Objects.requireNonNull(getIntent().getExtras())
                .getString("email");
        nametxt  = findViewById(R.id.teacher_login_name);
        orgtxt = findViewById(R.id.teacher_organization_name);
        profileimg = findViewById(R.id.login_image_select);
        Button submitbtn = findViewById(R.id.teacher_create_user);

        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                openGallery();
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    private void openGallery() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent,"Select Image"),GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK && data!=null
                && data.getData()!=null)
        {
            uri = data.getData();
            profileimg.setImageURI(uri);
        }
    }

    private void validateData() {

        String name = nametxt.getText().toString();
        String organization = orgtxt.getText().toString();

        if(TextUtils.isEmpty(name)){
            nametxt.setError("Invalid name");
            nametxt.requestFocus();
        }
        else if(TextUtils.isEmpty(organization)){
            nametxt.setError("Invalid Organization");
            nametxt.requestFocus();
        }
        else{
            registerUser(name,organization);
        }
    }

    private void registerUser(final String name, final String organization) {

        if (checker.equals("clicked")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            StorageReference fileref = storageReference.child("Users/"+email.substring(0,4)+".jpg");

            fileref.putFile(uri).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getUploadSessionUri();

                    assert downloadUri != null;
                    myuri = downloadUri.toString();
                    
                    DatabaseReference reference =
                            FirebaseDatabase.getInstance().getReference()
                            .child("Teacher");

                    HashMap<String, Object> hashMap = 
                            new HashMap<>();
                    hashMap.put("name",name);
                    hashMap.put("organization",organization);
                    hashMap.put("image",myuri);
                    hashMap.put("email",email);
                    
                    reference.child(email.substring(0,4)).updateChildren(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(TeacherRegister.this,
                                                "Successfully data Uploaded",
                                                Toast.LENGTH_SHORT)
                                                .show();

                                        Prevalent.currentuser.setEmail(email);
                                        Prevalent.currentuser.setName(name);
                                        Prevalent.currentuser.setImage(myuri);
                                        Prevalent.currentuser.setOrganization(organization);

                                        Intent intent = new Intent(
                                                TeacherRegister.this,
                                                HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(
                                                TeacherRegister.this,
                                                "Error in Uploading",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(
                            TeacherRegister.this, 
                            "Error While Uploading", 
                            Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
        }
    }
}