package com.example.stufun.Teacher;

import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.stufun.Model.AnnouncementModel;
import com.example.stufun.R;
import com.example.stufun.ViewHolder.AnnouncementViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class TeacherClassFragment extends Fragment {

    private RelativeLayout addannouncementlayout,settinglayout,studentlayout,discussionlayout;
    private RecyclerView recyclerView;
    private String classid = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher_class, container, false);

        assert getArguments() != null;
        classid = getArguments().getString("id");

        addannouncementlayout = view.findViewById(R.id.add_announcement_layout);
        discussionlayout = view.findViewById(R.id.discussion_layout);
        settinglayout = view.findViewById(R.id.setting_layout);
        studentlayout = view.findViewById(R.id.member_layout);
        recyclerView = view.findViewById(R.id.announcement_recyclerview);

        initalizeUI();

        showAnnouncement();
        return view;
    }

    private void showAnnouncement() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Announcement");

        FirebaseRecyclerOptions<AnnouncementModel> options =
                new FirebaseRecyclerOptions.Builder<AnnouncementModel>()
                .setQuery(databaseReference.child(classid),AnnouncementModel.class).build();

        final FirebaseRecyclerAdapter<AnnouncementModel, AnnouncementViewHolder> adapter =
                new FirebaseRecyclerAdapter<AnnouncementModel, AnnouncementViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull final AnnouncementViewHolder holder,
                                                    int position, @NonNull AnnouncementModel model)
                    {
                        holder.textView.setText(model.getText());
                        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(holder.textView.getMaxLines()==3)
                                {
                                    holder.textView.setMaxLines(50);

                                    Matrix matrix = new Matrix();
                                    holder.imageView.setScaleType(ImageView.ScaleType.MATRIX);
                                    //noinspection IntegerDivisionInFloatingPointContext
                                    matrix.postRotate((float) 180,holder.imageView.getDrawable().getBounds().width()/2
                                            ,holder.imageView.getDrawable().getBounds().height()/2);
                                    holder.imageView.setImageMatrix(matrix);
                                }
                                else
                                {
                                    holder.textView.setMaxLines(3);

                                    Matrix matrix = new Matrix();
                                    holder.imageView.setScaleType(ImageView.ScaleType.MATRIX);
                                    //noinspection IntegerDivisionInFloatingPointContext
                                    matrix.postRotate((float) 0,holder.imageView.getDrawable().getBounds().width()/2
                                            ,holder.imageView.getDrawable().getBounds().height()/2);
                                    holder.imageView.setImageMatrix(matrix);
                                }
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.announcement_viewholder,parent,false);
                        return new AnnouncementViewHolder(v);
                    }
                };
        recyclerView.setAdapter(adapter);

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                adapter.dele
//            }
//        });

        adapter.startListening();

    }

    private void initalizeUI() {

        addannouncementlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id",classid);

                Fragment fragment = new AddAnnouncementFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                        .commit();
            }
        });
        discussionlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        settinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        studentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}