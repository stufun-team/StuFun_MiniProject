package com.example.stufun.ViewHolder;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.R;

public class PendingDiscussionViewHolder extends RecyclerView.ViewHolder {

    public TextView question,reply,delete;
    public EditText replytxt;
    public RelativeLayout relativeLayout;
    public TextView post;

    public PendingDiscussionViewHolder(@NonNull View itemView) {
        super(itemView);

        question = itemView.findViewById(R.id.question_pending_discussion);
        reply = itemView.findViewById(R.id.reply_pending_discussion);
        delete  =itemView.findViewById(R.id.delete_pending_discussion);
        replytxt = itemView.findViewById(R.id.edit_pending_discussion);
        relativeLayout = itemView.findViewById(R.id.pending_relative_layout);
        post = itemView.findViewById(R.id.post_discussion);
    }
}
