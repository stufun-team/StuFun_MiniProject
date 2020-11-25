package com.example.stufun.PageAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.Chat.ChatActivity;
import com.example.stufun.Prevalent.User;
import com.example.stufun.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder> {

    private final List<User> users;
    private final Context context;
    private final boolean ischat;

    public ChatUserAdapter(Context context,List<User> users,boolean ischat)
    {
        this.context = context;
        this.users = users;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_viewholder,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = users.get(position);

        holder.textView.setText(user.getName());
        Picasso.get().load(user.getImage()).into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("rid",user.getUid());
                intent.putExtra("name",user.getName());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        public ImageView onn,off;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.user_relativeholder);
            imageView = itemView.findViewById(R.id.chat_user_profile_img);
            textView = itemView.findViewById(R.id.chat_user_name);
            onn = itemView.findViewById(R.id.seen_on);
            off = itemView.findViewById(R.id.seen_off);

        }
    }
}
