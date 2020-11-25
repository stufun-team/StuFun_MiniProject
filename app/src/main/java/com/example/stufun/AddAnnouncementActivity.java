package com.example.stufun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stufun.Prevalent.CurrentClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddAnnouncementActivity extends AppCompatActivity {

    private EditText announcement;
    private String classid="";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        Toolbar toolbar = findViewById(R.id.add_announce_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        announcement = findViewById(R.id.add_announcement_edit);
        Button button = findViewById(R.id.add_announcement_btn);
        classid = CurrentClass.classid;
        progressDialog = new ProgressDialog(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void validate() {
        String announce = announcement.getText().toString();

        if(TextUtils.isEmpty(announce)){
            announcement.setError("Can't be Empty");
            announcement.requestFocus();
        }
        else
        {
            registerAnnouncement(announce);
            progressDialog.setTitle("Submitting Detail");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    private void registerAnnouncement(String announce) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Announcement");
        String id = System.currentTimeMillis()+"";

        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("id",id);
        hashMap.put("text",announce);

        reference.child(classid).child(id).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.cancel();
                            Toast.makeText(AddAnnouncementActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });
    }
}