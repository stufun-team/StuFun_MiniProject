package com.example.stufun.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.R;

import org.w3c.dom.Text;

public class BlockUserViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView textView;
    public ImageView unblock;

    public BlockUserViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.block_student_detail_img);
        textView = itemView.findViewById(R.id.block_student_name_txt);
        unblock = itemView.findViewById(R.id.block_student_detail_unblock);
    }
}
