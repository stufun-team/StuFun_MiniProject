package com.example.stufun.Teacher;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stufun.Prevalent.CurrentClass;
import com.example.stufun.R;
import com.example.stufun.Student.StudentHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class AddAnnouncementFragment extends Fragment {

    private EditText announcement;
    private String classid="";
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_announcement, container, false);


        return view;
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
                            Toast.makeText(getActivity(), "Successfully", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new TeacherClass();
                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity())
                                    .getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                                    .commit();
                        }
                    }
                });
    }
}