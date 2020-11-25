package com.example.stufun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stufun.Model.PendingDiscussionModel;
import com.example.stufun.Prevalent.CurrentClass;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.ViewHolder.AnnouncementViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AskQuestionActivity extends AppCompatActivity {
    private ImageView send_btn;
    private EditText question_txt;
    private RecyclerView recyclerView;
    private String classid;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        send_btn = findViewById(R.id.ask_img);
        question_txt = findViewById(R.id.ask_edit);
        recyclerView = findViewById(R.id.student_pending_discussion);
        progressDialog = new ProgressDialog(this);

        initalizeUI();

        showQuestions();
    }

    private void initalizeUI() {


        Toolbar toolbar = findViewById(R.id.ask_que_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
                progressDialog.setTitle("Submitting Detail");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });

        classid = CurrentClass.classid;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void validate() {
        String question = question_txt.getText().toString();
        if(!TextUtils.isEmpty(question))
            saveQuestion(question);
    }

    private void saveQuestion(String question) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Discussion Pending");

        String id = System.currentTimeMillis()+"";
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("question",question);
        hashMap.put("student", Prevalent.currentuser.getUid());
        hashMap.put("id",id);

        reference.child(classid).child(id).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        progressDialog.cancel();
                        question_txt.setText("");
                        if(task.isSuccessful())
                            Toast.makeText(AskQuestionActivity.this,
                                    "Successfully uploaded", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AskQuestionActivity.this,
                                    "Error While Upload", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showQuestions() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Discussion Pending");

        final FirebaseRecyclerOptions<PendingDiscussionModel> options =
                new FirebaseRecyclerOptions.Builder<PendingDiscussionModel>().
                        setQuery(reference.child(classid).orderByChild("student")
                                .equalTo(Prevalent.currentuser.getUid()),PendingDiscussionModel.class)
                        .build();

        FirebaseRecyclerAdapter<PendingDiscussionModel, AnnouncementViewHolder> adapter =
                new FirebaseRecyclerAdapter<PendingDiscussionModel, AnnouncementViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final AnnouncementViewHolder holder,
                                                    int position, @NonNull PendingDiscussionModel model) {

                        holder.textView.setText(model.getQuestion());
                        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(holder.textView.getMaxLines()==3)
                                {
                                    holder.textView.setMaxLines(50);

                                    Matrix matrix = new Matrix();
                                    holder.imageView.setScaleType(ImageView.ScaleType.MATRIX);

                                    //noinspection IntegerDivisionInFloatingPointContext
                                    matrix.postRotate((float) 180,holder.imageView
                                                    .getDrawable().getBounds().width()/2
                                            ,holder.imageView.getDrawable()
                                                    .getBounds().height()/2);
                                    holder.imageView.setImageMatrix(matrix);
                                }
                                else
                                {
                                    holder.textView.setMaxLines(3);

                                    Matrix matrix = new Matrix();
                                    holder.imageView.setScaleType(ImageView.ScaleType.MATRIX);
                                    //noinspection IntegerDivisionInFloatingPointContext
                                    matrix.postRotate((float) 0,holder.imageView
                                                    .getDrawable().getBounds().width()/2
                                            ,holder.imageView.getDrawable()
                                                    .getBounds().height()/2);
                                    holder.imageView.setImageMatrix(matrix);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AnnouncementViewHolder onCreateViewHolder
                            (@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.announcement_viewholder,parent,false);
                        return new AnnouncementViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}