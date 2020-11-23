package com.example.stufun.Teacher;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.stufun.Model.AvnatarModel;
import com.example.stufun.Model.ClassRoomModel;
import com.example.stufun.Prevalent.CurrentClass;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.R;
import com.example.stufun.ViewHolder.ClassRoomViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private FloatingActionButton createround;
    private Button createsquare;
    private RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.teacher_classroom_recyclerview);
        createround = view.findViewById(R.id.create_classroom_round);
        createsquare = view.findViewById(R.id.create_classroom_square);
        reference = FirebaseDatabase.getInstance().getReference().child("Teacher Classroom");
        relativeLayout = view.findViewById(R.id.no_class_layout);

        initalizeUI();

        showClass();
        return view;
    }

    private void showClass() {

        FirebaseRecyclerOptions<ClassRoomModel> options = new
                FirebaseRecyclerOptions.Builder<ClassRoomModel>()
                .setQuery(reference.child(Prevalent.currentuser.getUid()),
                        ClassRoomModel.class).build();

        FirebaseRecyclerAdapter<ClassRoomModel, ClassRoomViewHolder> adapter =
                new FirebaseRecyclerAdapter<ClassRoomModel, ClassRoomViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(
                            @NonNull final ClassRoomViewHolder holder, int position,
                            @NonNull final ClassRoomModel model) {



                        relativeLayout.setVisibility(View.INVISIBLE);
                        holder.classnametxt.setText(model.getClassname());
                        holder.subjectnametxt.setText(model.getClasssubject());
                        holder.teachernametxt.setText(model.getTeachername());

                        holder.dots.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(holder.modifytxt.getVisibility() == View.GONE)
                                {
                                    holder.modifytxt.setVisibility(View.VISIBLE);
                                }
                                else {
                                    holder.modifytxt.setVisibility(View.GONE);
                                }
                            }
                        });

                        holder.modifytxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteClass(model.getClasscode());
                            }
                        });

                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CurrentClass.classid = model.getClasscode();
                                CurrentClass.classname = model.getClassname();
                                Fragment fragment = new TeacherClass();
                                FragmentManager fragmentManager =
                                        Objects.requireNonNull(getActivity())
                                                .getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                                        .commit();
                            }
                        });
                        AvnatarModel avnatarModel = new AvnatarModel();

                        try{
                            holder.cardView.setCardBackgroundColor(model.getClasscolorcode());
                            Picasso.get().load(avnatarModel.getImage(model.getClassimage()))
                                    .into(holder.classimageview);
                        }
                        catch (Exception ignored){
                        }
                    }

                    public void deleteClass(String classcode)
                    {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                .getReference();

                        databaseReference.child("Teacher Classroom")
                                .child(Prevalent.currentuser.getUid()).child(classcode)
                                .removeValue();

                        databaseReference.child("TClass").child(classcode).removeValue();
                    }

                    @NonNull
                    @Override
                    public ClassRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.created_class_viewholder,parent,
                                        false);

                        return new ClassRoomViewHolder(view);

                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void initalizeUI() {

        createsquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CreateClassActivity();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                        .commit();
            }
        });

        createround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CreateClassActivity();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                        .commit();

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


}