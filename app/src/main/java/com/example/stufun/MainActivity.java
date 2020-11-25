package com.example.stufun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.stufun.Login.LoginActivity;
import com.example.stufun.Login.UserSelection;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.Prevalent.User;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        },3000);
    }
    private void checkUser() {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null)
        {
            grantAccess();
        }
        else
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }



    private void grantAccess() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users");
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        databaseReference.child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Prevalent.currentuser = dataSnapshot.getValue(User.class);
                            Prevalent.type = Prevalent.currentuser.getType();

                            Intent intent = new Intent(MainActivity.this,
                                    HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                    Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(MainActivity.this,
                                    UserSelection.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                    Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("uid",auth.getUid());
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}