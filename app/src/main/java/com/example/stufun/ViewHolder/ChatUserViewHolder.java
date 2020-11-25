package com.example.stufun.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.R;

public class ChatUserViewHolder extends RecyclerView.ViewHolder {

    public TextView name;

    public ChatUserViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.chat_user_name);
    }
}
