package com.example.stufun.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.R;

public class StudentsViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public StudentsViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.student_name_txt);
    }
}
