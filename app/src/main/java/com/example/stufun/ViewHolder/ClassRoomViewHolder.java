package com.example.stufun.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stufun.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassRoomViewHolder extends RecyclerView.ViewHolder {

    public TextView teachernametxt,classnametxt,subjectnametxt;
    public CircleImageView classimageview;
    public CardView cardView;
    public ImageView dots;
    public TextView modifytxt;

    public ClassRoomViewHolder(@NonNull View itemView) {
        super(itemView);

        teachernametxt = itemView.findViewById(R.id.created_class_teacher_txt);
        classnametxt = itemView.findViewById(R.id.created_class_name_txt);
        subjectnametxt = itemView.findViewById(R.id.created_class_subject_txt);
        classimageview = itemView.findViewById(R.id.created_class_image);
        cardView = itemView.findViewById(R.id.classroom_card_layout);
        dots = itemView.findViewById(R.id.three_dot);
        modifytxt = itemView.findViewById(R.id.class_modify);
    }
}
