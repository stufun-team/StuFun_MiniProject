package com.example.stufun.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stufun.Notification.APIService;
import com.example.stufun.Notification.Client;
import com.example.stufun.Notification.Data;
import com.example.stufun.Notification.MyResponse;
import com.example.stufun.Notification.Sender;
import com.example.stufun.Notification.Token;
import com.example.stufun.PageAdapter.MessageAdapter;
import com.example.stufun.Model.UserChatModel;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.Prevalent.User;
import com.example.stufun.R;
import com.example.stufun.UserProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText msg_txt;
    private String rid;
    private String sid;
    private DatabaseReference reference;

    private MessageAdapter messageAdapter;
    private List<UserChatModel> mchat;

    private Uri uri;
    private String myuri = "";
    private String clicked="";
    private static final int GALLERY_PICK = 7;

    APIService apiService;
    boolean notify = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        initalizeUI();
        readMessage();
        seenMessage();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatmenu,menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.chat_clear)
            showClearDialog();
        else if(id == R.id.chat_user_profile)
            startActivity(new Intent(ChatActivity.this, UserProfileActivity.class)
                    .putExtra("rid",rid));


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK && data!=null
                && data.getData()!=null)
        {
            uri = data.getData();
            saveImageMessage();
        }
    }

    private void saveImageMessage() {

        if(clicked.equals("clicked") && !Uri.EMPTY.equals(uri))
        {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            final StorageReference fileref = storageReference.child("Chats/"+System.currentTimeMillis()+".jpg");

            fileref.putFile(uri).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    myuri = uri.toString();

                                    String currentTime = new SimpleDateFormat("HH:mm",
                                            Locale.getDefault())
                                            .format(new Date());

                                    DatabaseReference reference = FirebaseDatabase.getInstance()
                                            .getReference().child("UserChat");

                                    HashMap<String ,Object> hashMap = new HashMap<>();
                                    hashMap.put("sender",sid);
                                    hashMap.put("receiver",rid);
                                    hashMap.put("message","");
                                    hashMap.put("date",currentTime);
                                    hashMap.put("isseen",false);
                                    hashMap.put("image",myuri);
                                    hashMap.put("type","img");
                                    hashMap.put("clear","");

                                    reference.push().setValue(hashMap);
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(
                            ChatActivity.this,
                            "Error While Uploading",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void seenMessage() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("UserChat");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserChatModel chat = dataSnapshot.getValue(UserChatModel.class);
                    if (chat.getReceiver().equals(sid) && chat.getSender().equals(rid)) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initalizeUI() {

        rid = getIntent().getExtras().getString("rid");
        sid = Prevalent.currentuser.getUid();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        ImageView imageSelector = findViewById(R.id.chat_image_select);
        ImageView backBtn = findViewById(R.id.back_arrow_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
                clicked = "clicked";
            }
        });

        recyclerView = findViewById(R.id.user_chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        
        msg_txt = findViewById(R.id.chat_text);

        ImageView send_btn = findViewById(R.id.chat_send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                validatemsg();
            }
        });
    }

    private void OpenGallery() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent,"Select Image"),GALLERY_PICK);
    }

    private void validatemsg() {
        String message = msg_txt.getText().toString();
        
        if(!TextUtils.isEmpty(message))
            sendMessage(message);
    }

    private void sendMessage(final String message) {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(new Date());

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference().child("UserChat");

        HashMap<String ,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sid);
        hashMap.put("receiver",rid);
        hashMap.put("message",message);
        hashMap.put("date",currentTime);
        hashMap.put("isseen",false);
        hashMap.put("image","");
        hashMap.put("type","txt");
        hashMap.put("clear","");
        reference.push().setValue(hashMap);

        if(notify)
        {
            sendNotification(rid,Prevalent.currentuser.getName(),message);
        }
        notify = false;

    }

    private void sendNotification(String receiver, final String name, final String message) {

        Toast.makeText(this, "send Notification", Toast.LENGTH_SHORT).show();

        DatabaseReference tokenref = FirebaseDatabase.getInstance().getReference("Token");

        Query query = tokenref.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    Token token = dataSnapshot.getValue(Token.class);
                    Toast.makeText(ChatActivity.this, token.getToken(), Toast.LENGTH_SHORT).show();
                    Data data = new Data(sid,R.mipmap.ic_launcher,name + " : " + message,
                            "New Message",rid);

                    Sender sender = new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    Toast.makeText(ChatActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                                    if(response.code() == 200)
                                    {
                                        if(response.body().success != 1)
                                        {
                                            Toast.makeText(ChatActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readMessage() {
        mchat = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("UserChat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    UserChatModel userChatModel = dataSnapshot.getValue(UserChatModel.class);

                    assert userChatModel != null;
                    if(userChatModel.getReceiver().equals(sid)
                            && userChatModel.getSender().equals(rid) ||
                            userChatModel.getSender().equals(sid) &&
                                    userChatModel.getReceiver().equals(rid)){
                        if(!userChatModel.getClear().contains(sid))
                        {
                            mchat.add(userChatModel);
                        }
                    }
                    messageAdapter = new MessageAdapter(ChatActivity.this,
                            mchat);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void status(String status) {
//        reference = FirebaseDatabase.getInstance().getReference().child("Users")
//                .child(Prevalent.currentuser.getUid());
//
//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("status",status);
//
//        reference.updateChildren(hashMap);
//    }

    private void showClearDialog() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("UserChat");

        ValueEventListener listener;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserChatModel chat = dataSnapshot.getValue(UserChatModel.class);
                    if (chat.getReceiver().equals(sid) && chat.getSender().equals(rid) ||
                            chat.getReceiver().equals(rid) && chat.getSender().equals(sid)) {

                        if(!chat.getClear().contains(sid))
                        {
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("clear",chat.getClear()+sid);
                            dataSnapshot.getRef().updateChildren(hashMap);
                        }
                    }
                }
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        status("online");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        status("offline");
//        reference.removeEventListener(seenListener);
//    }
}