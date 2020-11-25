package com.example.stufun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stufun.Login.LoginActivity;
import com.example.stufun.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initalizeUI();
    }

    private void initalizeUI() {

        ImageView profileimg = findViewById(R.id.my_img);
        ImageView backbtn = findViewById(R.id.setting_back);
        TextView nametxt = findViewById(R.id.my_name);
        TextView emailtxt = findViewById(R.id.my_email);
        TextView orgtxt = findViewById(R.id.my_organization);
        TextView edittxt = findViewById(R.id.my_edit_account);
        TextView security = findViewById(R.id.my_security);
        TextView logouttxt = findViewById(R.id.my_logout);

        nametxt.setText(Prevalent.currentuser.getName());
        emailtxt.setText(Prevalent.currentuser.getEmail());
        orgtxt.setText(Prevalent.currentuser.getOrganization());

        Picasso.get().load(Prevalent.currentuser.getImage()).into(profileimg);

        edittxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,EditProfileActivity.class));
            }
        });

        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,PasswordResetActivity.class));
            }
        });

        logouttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}