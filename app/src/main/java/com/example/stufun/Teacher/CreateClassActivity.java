package com.example.stufun.Teacher;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
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

import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;


public class CreateClassActivity extends Fragment {


    private EditText cnametxt, csubjecttxt;
    private Button submitbtn;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_class_activity, container, false);

        cnametxt = view.findViewById(R.id.add_class_name);
        csubjecttxt = view.findViewById(R.id.add_class_subject);
        submitbtn = view.findViewById(R.id.add_class_btn);
        progressDialog = new ProgressDialog(getContext());
        initalizeUI();
        return view;
    }

    private void initalizeUI() {


        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                validate();
            }
        });
    }

    private void validate() {

        String cname = cnametxt.getText().toString();
        String csubject = csubjecttxt.getText().toString();

        if(TextUtils.isEmpty(cname))
        {
            cnametxt.setError("Can't be empty");
            cnametxt.requestFocus();
        }
        else if(TextUtils.isEmpty(csubject))
        {
            cnametxt.setError("Can't be empty");
            cnametxt.requestFocus();
        }
        else{
            createClassroom(cname,csubject);
            progressDialog.setTitle("Creating Classroom");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

    private void createClassroom(String cname,String csubject) {

        final DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference().child("Teacher Classroom");

        Random random = new Random();

        final String id = String.valueOf(random.nextInt(999)+1000);
        int color = Color.argb(255,random.nextInt(256)
                ,random.nextInt(256),random.nextInt(256));

        int randomimg = random.nextInt(3);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String crdate = sdf.format(new Date());

        final HashMap<String,Object> map = new HashMap<>();
        map.put("classname",cname);
        map.put("classcode",id);
        map.put("classsubject",csubject);
        map.put("classimage",randomimg);
        map.put("classcolorcode",color);
        map.put("teachername", Prevalent.currentuser.getName());
        map.put("datecreated",crdate);


        databaseReference.child(Prevalent.currentuser.getUid()).child(id).updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {

                            DatabaseReference reference =
                                    FirebaseDatabase.getInstance().getReference().child("TClass");
                            reference.child(id).updateChildren(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(getActivity(),
                                                        "Successful",
                                                        Toast.LENGTH_SHORT).show();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("id",id);
                                                Fragment fragment = new SendInvitationFragment();
                                                fragment.setArguments(bundle);
                                                FragmentManager fragmentManager =
                                                        Objects.requireNonNull(getActivity()).
                                                                getSupportFragmentManager();
                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.fragment_container,fragment)
                                                        .commit();
                                                progressDialog.cancel();
                                            }
                                            else {
                                                progressDialog.cancel();
                                                Toast.makeText(getActivity(),
                                                        "Error", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        }
                        else
                        {
                            progressDialog.cancel();
                            Toast.makeText(getActivity(),
                                    "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}