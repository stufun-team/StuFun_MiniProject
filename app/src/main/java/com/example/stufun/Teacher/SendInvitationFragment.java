package com.example.stufun.Teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.R;
import com.example.stufun.Student.StudentHome;

import java.util.Objects;

public class SendInvitationFragment extends Fragment {

    String cid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send_invitation, container, false);
        assert getArguments() != null;
        cid = getArguments().getString("id");
        TextView textView = view.findViewById(R.id.class_id);
        textView.setText(cid);

        Button startBtn = view.findViewById(R.id.send_email);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
            }
        });

        Button bckbtn = view.findViewById(R.id.back_to_home);
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;
                fragment = new StudentHome();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                        .commit();
            }
        });

        return view;
    }
    @SuppressLint("IntentReset")
    protected void sendEmail() {

        String emailmsg = "Dear Sir/Mam \n"+ Prevalent.currentuser.getName() +" invited you to Join Stufun Class \n\n"+
                "Class Code : "+ cid;

        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Classroom Invitation - StuFun");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailmsg);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}