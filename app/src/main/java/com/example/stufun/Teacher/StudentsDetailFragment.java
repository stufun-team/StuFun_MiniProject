package com.example.stufun.Teacher;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stufun.Chat.ChatActivity;
import com.example.stufun.Prevalent.CurrentClass;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.Prevalent.User;
import com.example.stufun.R;
import com.example.stufun.ViewHolder.BlockUserViewHolder;
import com.example.stufun.ViewHolder.StudentsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class StudentsDetailFragment extends Fragment {

    private RecyclerView recyclerView;
    private String classid;
    private RecyclerView blockrecyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        classid = CurrentClass.classid;

        View view = inflater.inflate(R.layout.fragment_students_detail,
                container, false);
        recyclerView = view.findViewById(R.id.students_list_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        blockrecyclerview = view.findViewById(R.id.blockeduser_recycler);

        if(Prevalent.currentuser.getType().equals("Teacher"))
        {
            blockrecyclerview.setVisibility(View.VISIBLE);
            blockrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            TextView blockhead = view.findViewById(R.id.blockedclassmate_txt);
            blockhead.setVisibility(View.VISIBLE);
        }

        showStudents();
        showBlockUser();

        return view;
    }

    private void showBlockUser() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("BlockUser");

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(reference.child(classid).orderByChild("name"),User.class)
                        .build();

        FirebaseRecyclerAdapter<User, BlockUserViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, BlockUserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(
                            @NonNull BlockUserViewHolder holder, int position,
                            @NonNull final User model) {

                        holder.textView.setText(model.getName());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.unblock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                unblockuser(model.getUid());
                            }
                        });
                    }

                    public void unblockuser(String uid)
                    {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                .getReference();

                        databaseReference.child("BlockUser").child(CurrentClass.classid).child(uid)
                                .removeValue();

                    }

                    @NonNull
                    @Override
                    public BlockUserViewHolder onCreateViewHolder(
                            @NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.block_student_viewholder,
                                        parent,false);

                        return new BlockUserViewHolder(view);
                    }
                };
        blockrecyclerview.setAdapter(adapter);
        adapter.startListening();
    }

    private void showStudents() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Classes");

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(reference.child(classid).orderByChild("name"),User.class)
                .build();

        FirebaseRecyclerAdapter<User, StudentsViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, StudentsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(
                            @NonNull StudentsViewHolder holder, int position,
                            @NonNull final User model) {

                        holder.name.setText(model.getName());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        if(model.getUid().equals(Prevalent.currentuser.getUid()))
                        {
                            holder.chaticon.setVisibility(View.INVISIBLE);
                            holder.chaticon.setClickable(false);
                        }
                        holder.chaticon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),ChatActivity.class);
                                intent.putExtra("rid",model.getUid());
                                intent.putExtra("name",model.getName());
                                startActivity(intent);
                            }
                        });
                        if(Prevalent.currentuser.getType().equals("Teacher"))
                        {
                            holder.block.setVisibility(View.VISIBLE);
                            holder.block.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    blockuser(model.getUid(),model.getName(),model.getImage());
                                }
                            });
                        }
                    }

                    public void blockuser(String uid,String name,String image)
                    {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                .getReference();

                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("uid",uid);
                        hashMap.put("name",name);
                        hashMap.put("image",image);

                        databaseReference.child("BlockUser").child(CurrentClass.classid).child(uid)
                                .updateChildren(hashMap);

                        databaseReference.child("Student Class")
                                .child(uid).child(CurrentClass.classid)
                                .removeValue();

                        databaseReference.child("Classes").child(CurrentClass.classid)
                                .child(uid).removeValue();
                    }

                    @NonNull
                    @Override
                    public StudentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.student_viewholder,parent,false);

                        return new StudentsViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}