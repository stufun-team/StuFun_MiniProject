package com.example.stufun.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.R;

public class DiscussionViewHolder extends RecyclerView.ViewHolder {

    public TextView questiontxt,answertxt,viewtxt;

    public DiscussionViewHolder(@NonNull View itemView) {
        super(itemView);

        questiontxt = itemView.findViewById(R.id.discuss_question);
        answertxt = itemView.findViewById(R.id.discuss_reply);
        viewtxt = itemView.findViewById(R.id.view_more);

    }
}
