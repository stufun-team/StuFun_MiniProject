package com.example.stufun.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stufun.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class StudentRegister extends AppCompatActivity {

    private EditText emailtxt,passwordtxt;
    private String email,password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        auth = FirebaseAuth.getInstance();

        emailtxt = findViewById(R.id.email_edit);
        passwordtxt = findViewById(R.id.password_edit);
        Button loginbtn = findViewById(R.id.send_veri_btn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInput();
            }
        });
    }

    private void validateInput(){

        email = emailtxt.getText().toString();
        password = passwordtxt.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailtxt.setError("Email Can't be Empty");
            emailtxt.requestFocus();
        }
        else if(TextUtils.isEmpty(password) || password.length()<6){
            passwordtxt.setError("Invalid password");
            passwordtxt.requestFocus();
        }
        else {
            signinuser();
        }
    }

    private void signinuser() {

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(StudentRegister.this, "Succesfully Registered", Toast.LENGTH_SHORT).show();
                                        emailtxt.setText("");
                                        passwordtxt.setText("");
                                        Intent intent = new Intent(StudentRegister.this,LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(StudentRegister.this, "Error in Registering", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(StudentRegister.this, "Error in Registering", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}