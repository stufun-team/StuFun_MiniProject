package com.example.stufun;

import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.stufun.Model.AnnouncementModel;
import com.example.stufun.ViewHolder.AnnouncementViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentClassFragment extends Fragment {

    private RecyclerView recyclerView;
    private String classid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_student_class,
                container, false);

        classid = getArguments().getString("id");
        recyclerView = view.findViewById(R.id.student_announcement_reyclerview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

        adapter.startListening();
    }
}