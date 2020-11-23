package com.example.stufun.Teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stufun.Model.DiscussionModel;
import com.example.stufun.PendingDiscussionActivity;
import com.example.stufun.Prevalent.CurrentClass;
import com.example.stufun.R;
import com.example.stufun.ViewHolder.DiscussionViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TeacherDiscussionFragment extends Fragment {

    private FloatingActionButton newQuetxt;
    private RecyclerView recyclerView;
    private String classid = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_discussion, container, false);

        newQuetxt = view.findViewById(R.id.new_que_txt);
        recyclerView = view.findViewById(R.id.teacher_discussion_recyclerview);

        classid = CurrentClass.classid;

        initalizeUI();
        showDiscussion();
        return view;
    }

    private void showDiscussion() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Discussion");

        final FirebaseRecyclerOptions<DiscussionModel> options = new FirebaseRecyclerOptions.
                Builder<DiscussionModel>().
                setQuery(reference.child(classid).orderByChild("time"),DiscussionModel.class)
                .build();

        FirebaseRecyclerAdapter<DiscussionModel, DiscussionViewHolder> adapter =
                new FirebaseRecyclerAdapter<DiscussionModel, DiscussionViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final DiscussionViewHolder holder,
                                                    int position, @NonNull DiscussionModel model) {

                        holder.questiontxt.setText(model.getQuestion());
                        holder.answertxt.setText(model.getAnswer());
                        holder.viewtxt.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(View view) {
                                if(holder.answertxt.getMaxLines()==0)
                                {
                                    holder.answertxt.setMaxLines(100);
                                    holder.viewtxt.setText("Hide Reply");
                                }
                                else{
                                    holder.answertxt.setMaxLines(0);
                                    holder.viewtxt.setText("View Reply");
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public DiscussionViewHolder onCreateViewHolder
                            (@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.discussion_viewholder,parent,false);
                        return new DiscussionViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void initalizeUI() {

        newQuetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PendingDiscussionActivity.class));
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}