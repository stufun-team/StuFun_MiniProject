package com.example.stufun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.Prevalent.User;
import com.example.stufun.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserSelection extends AppCompatActivity {

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.student_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSelection.this,LoginActivity.class);
                intent.putExtra("type","Student");
                startActivity(intent);
            }
        });
        findViewById(R.id.teacher_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSelection.this,LoginActivity.class);
                intent.putExtra("type","Teacher");
                startActivity(intent);
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//        if(auth.getCurrentUser()!=null)
//            loginUser();
//    }

    private void loginUser() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Teacher");

        databaseReference.
                child(Objects.requireNonNull
                        (Objects.requireNonNull(auth.getCurrentUser()).getEmail()).substring(0,4))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            Prevalent.currentuser = dataSnapshot.getValue(User.class);

                            Intent intent = new Intent(UserSelection.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                        else
                        {
                            Intent intent = new Intent(UserSelection.this,TeacherRegister.class);
                            intent.putExtra("email",auth.getCurrentUser().getEmail());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}