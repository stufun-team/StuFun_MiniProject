package com.example.stufun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.stufun.Chat.ChatActivity;
import com.example.stufun.Prevalent.User;
import com.example.stufun.ViewHolder.SearchViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    private EditText searchtxt;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initalizeUI();
    }

    private void initalizeUI() {

        ImageView backbtn = findViewById(R.id.back_button);
        searchtxt = findViewById(R.id.search_edit);

        recyclerView = findViewById(R.id.search_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                searchUser(searchtxt.getText().toString());
            }
        });
    }

    private void searchUser(String s) {

        if(!s.equals(""))
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Users");

            FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions
                    .Builder<User>()
                    .setQuery(reference.orderByChild("name").startAt(s).endAt("\uf8ff"),User.class)
                    .build();

            FirebaseRecyclerAdapter<User, SearchViewHolder> adapter =
                    new FirebaseRecyclerAdapter<User, SearchViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder
                                (@NonNull SearchViewHolder holder,
                                 int position, @NonNull final User model) {

                            holder.textView.setText(model.getName());
                            Picasso.get().load(model.getImage()).into(holder.imageView);

                            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(SearchActivity.this,
                                            ChatActivity.class);
                                    intent.putExtra("rid",model.getUid());
                                    intent.putExtra("name",model.getName());
                                    startActivity(intent);
                                }
                            });

                        }

                        @NonNull
                        @Override
                        public SearchViewHolder
                        onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.search_holder,parent,false);
                            return new SearchViewHolder(view);
                        }
                    };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}