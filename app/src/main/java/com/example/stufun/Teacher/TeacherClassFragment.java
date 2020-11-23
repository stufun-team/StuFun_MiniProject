package com.example.stufun.Teacher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stufun.AddAnnouncementActivity;
import com.example.stufun.Chat.ChatActivity;
import com.example.stufun.Model.AnnouncementModel;
import com.example.stufun.Prevalent.CurrentClass;
import com.example.stufun.R;
import com.example.stufun.ViewHolder.AnnouncementViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class TeacherClassFragment extends Fragment {

    private RecyclerView recyclerView;
    private String classid = "";
    private FloatingActionButton floatingActionButton;
    private int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher_class, container, false);

        classid = CurrentClass.classid;

        recyclerView = view.findViewById(R.id.announcement_recyclerview);
        floatingActionButton = view.findViewById(R.id.float_add_announcement);


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
                    public AnnouncementViewHolder onCreateViewHolder(
                            @NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.announcement_viewholder,
                                        parent,false);
                        return new AnnouncementViewHolder(v);
                    }
                };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int pos = viewHolder.getAdapterPosition();
            showDialog(pos);
        }
    };

    private void showDialog(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete Announcement");

        builder.setMessage("Are you sure want to delete Announcement");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAnnouncement(pos);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAnnouncement();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteAnnouncement(final int id)
    {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Announcement");
        final ValueEventListener listener;
        listener = reference.child(CurrentClass.classid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot :snapshot.getChildren())
                {
                    if(i == id)
                    {
                        AnnouncementModel announcementModel = dataSnapshot.getValue(AnnouncementModel.class);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                .child("Announcement");

                        databaseReference.child(CurrentClass.classid).child(announcementModel.getId())
                                .removeValue();

                    }
                    reference.removeEventListener(this);
                    i+=1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initalizeUI() {

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddAnnouncementActivity.class));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}