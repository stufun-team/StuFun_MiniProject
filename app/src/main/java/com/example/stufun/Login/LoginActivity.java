package com.example.stufun.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stufun.HomeActivity;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.Prevalent.User;
import com.example.stufun.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText emailtxt,passwordtxt;
    private String email,password;
    private FirebaseAuth auth;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailtxt = findViewById(R.id.login_email_txt);
        passwordtxt = findViewById(R.id.login_pass_txt);
        auth = FirebaseAuth.getInstance();
        type = Objects.requireNonNull(getIntent().getExtras()).getString("type");

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        findViewById(R.id.signup_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, StudentRegister.class));
            }
        });
    }

    private void validateData() {

        email = emailtxt.getText().toString();
        password = passwordtxt.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            emailtxt.setError("Invalid ID");
            emailtxt.requestFocus();
        }
        else if(TextUtils.isEmpty(password))
        {
            passwordtxt.setError("Invalid Password");
            passwordtxt.requestFocus();
        }
        else{
            loginUser();
        }
    }

    private void loginUser() {

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            if(Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified())
                            {
                                grantAccess();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void grantAccess() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(type);

        databaseReference.
                child(Objects.requireNonNull
                        (Objects.requireNonNull(auth.getCurrentUser()).getEmail()).substring(0,4))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            Prevalent.currentuser = dataSnapshot.getValue(User.class);

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                        else
                        {
                            if(type.equals("Teacher"))
                            {
                                Prevalent.type = "Teacher";
                                Intent intent = new Intent(
                                        LoginActivity.this, TeacherRegister.class);
                                intent.putExtra("email",email);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else if(type.equals("Student")){
                                Prevalent.type = "Student";
                                Intent intent = new Intent(
                                        LoginActivity.this,
                                        StudenLoginDetailAvtivity.class);
                                intent.putExtra("email",email);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(LoginActivity.this,
                                        "Error While Processing... try after some time",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}