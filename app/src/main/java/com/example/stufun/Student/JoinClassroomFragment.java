package com.example.stufun.Student;

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

import com.example.stufun.Model.ClassRoomModel;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class JoinClassroomFragment extends Fragment {

    private EditText jointxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_classroom, container, false);

        Button joinbtn = view.findViewById(R.id.join_class_btn);
        jointxt = view.findViewById(R.id.join_class_edit);

        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = jointxt.getText().toString();

                if(TextUtils.isEmpty(id))
                {
                    jointxt.setError("Invalid");
                    jointxt.requestFocus();
                }
                else{
                    joinclass(id);
                }
            }
        });
        return view;
    }

    private void joinclass(String id) {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("TClass");

        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference()
                            .child("Student Class");

                    final ClassRoomModel cm = snapshot.getValue(ClassRoomModel.class);

                    final HashMap<String,Object> map = new HashMap<>();

                    assert cm != null;
                    map.put("classname",cm.getClassname());
                    map.put("classcode",cm.getClasscode());
                    map.put("classsubject",cm.getClasssubject());
                    map.put("classimage","");
                    map.put("classcolorcode",cm.getClasscolorcode());
                    map.put("teachername", cm.getTeachername());
                    map.put("datecreated",cm.getDatecreated());

                    reference1.child(Prevalent.currentuser.getName()).child(cm.getClasscode()).updateChildren(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        DatabaseReference reference2 = FirebaseDatabase.getInstance()
                                                .getReference().child("Classes");

                                        reference2.child(cm.getClasscode())
                                                .child(Prevalent.currentuser.getName())
                                                .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(getActivity(), "No Such Id found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}