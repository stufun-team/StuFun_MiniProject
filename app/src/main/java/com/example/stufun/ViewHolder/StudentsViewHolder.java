package com.example.stufun.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.R;

public class StudentsViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public ImageView imageView;
    public ImageView chaticon;
    public ImageView block;

    public StudentsViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.student_name_txt);
        imageView = itemView.findViewById(R.id.student_detail_img);
        chaticon = itemView.findViewById(R.id.student_detail_chat_img);
        block = itemView.findViewById(R.id.student_detail_blockuser);

    }
}
