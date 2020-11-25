package com.example.stufun.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.R;


public class SearchViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    public ImageView imageView;
    public RelativeLayout relativeLayout;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.search_user_name);
        imageView  =itemView.findViewById(R.id.search_user_img);
        relativeLayout  =itemView.findViewById(R.id.search_layout);
    }
}
