package com.example.stufun.Teacher;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stufun.Model.StudentsModel;
import com.example.stufun.R;
import com.example.stufun.ViewHolder.StudentsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StudentsDetailFragment extends Fragment {

    private RecyclerView recyclerView;
    private String classid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        assert getArguments() != null;
        classid = getArguments().getString("id");
        View view = inflater.inflate(R.layout.fragment_students_detail,
                container, false);
        recyclerView = view.findViewById(R.id.students_list_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        showStudents();
        return view;
    }

    private void showStudents() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Class");

        FirebaseRecyclerOptions<StudentsModel> options =
                new FirebaseRecyclerOptions.Builder<StudentsModel>()
                .setQuery(reference.child(classid),StudentsModel.class)
                .build();

        FirebaseRecyclerAdapter<StudentsModel, StudentsViewHolder> adapter =
                new FirebaseRecyclerAdapter<StudentsModel, StudentsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(
                            @NonNull StudentsViewHolder holder, int position,
                            @NonNull StudentsModel model) {

                        holder.name.setText(model.getName());
                        holder.name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
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