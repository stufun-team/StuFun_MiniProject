package com.example.stufun;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddAnnouncementFragment extends Fragment {

    private EditText announcement;
    private Button button;
    private String classid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_announcement, container, false);

        announcement = view.findViewById(R.id.add_announcement_edit);
        button = view.findViewById(R.id.add_announcement_btn);
        assert getArguments() != null;
        classid = getArguments().getString("id");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

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
                            Toast.makeText(getActivity(), "Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}