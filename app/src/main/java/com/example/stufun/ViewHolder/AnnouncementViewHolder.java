package com.example.stufun.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.R;

public class AnnouncementViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    public RelativeLayout relativeLayout;
    public ImageView imageView;

    public AnnouncementViewHolder(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.announcement_txt);
        relativeLayout = itemView.findViewById(R.id.expand_announce);
        imageView = itemView.findViewById(R.id.expand_announce_icon);

    }
}
