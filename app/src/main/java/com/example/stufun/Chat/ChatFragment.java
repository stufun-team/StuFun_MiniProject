package com.example.stufun.Chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stufun.Notification.Token;
import com.example.stufun.PageAdapter.ChatUserAdapter;
import com.example.stufun.Model.UserChatModel;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.Prevalent.User;
import com.example.stufun.R;
import com.example.stufun.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton floatsearch;
    private String myid;

    private ChatUserAdapter chatUserAdapter;
    private List<User> users;
    private List<String> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.chat_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        myid = Prevalent.currentuser.getUid();

        floatsearch = view.findViewById(R.id.search_floatingbar);
        floatsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        showUsers();

        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }


    private void showUsers() {
        users = new ArrayList<>();
        list = new ArrayList<>();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("UserChat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    UserChatModel userChatModel = dataSnapshot.getValue(UserChatModel.class);

                    if(userChatModel.getSender().equals(myid))
                    {
                        String fid = userChatModel.getReceiver();
                        if(!list.contains(fid)){
                            list.add(fid);

                            DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                    .getReference().child("Users");

                            reference1.child(fid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists())
                                    {
                                        User user = snapshot.getValue(User.class);
                                        users.add(user);
                                        chatUserAdapter = new ChatUserAdapter(getActivity(),
                                                users,true);
                                        recyclerView.setAdapter(chatUserAdapter);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                    if(userChatModel.getReceiver().equals(myid))
                    {
                        String fid = userChatModel.getSender();
                        if(!list.contains(fid)){
                            list.add(fid);

                            DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                    .getReference().child("Users");

                            reference1.child(fid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists())
                                    {
                                        User user = snapshot.getValue(User.class);
                                        users.add(user);
                                        chatUserAdapter = new ChatUserAdapter(getActivity(),
                                                users,true);
                                        recyclerView.setAdapter(chatUserAdapter);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void updateToken(String token)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Token");

        Token token1 = new Token(token);
        reference.child(Prevalent.currentuser.getUid()).setValue(token1);
    }
}