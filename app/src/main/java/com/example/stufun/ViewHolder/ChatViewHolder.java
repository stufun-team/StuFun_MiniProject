package com.example.stufun.ViewHolder;

import android.view.View;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.chat_txt);
    }
}
