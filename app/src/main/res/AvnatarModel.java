package com.example.stufun.Model;


import com.example.stufun.R;

import java.util.ArrayList;

public class AvnatarModel {
    ArrayList<Integer> images;

    public AvnatarModel() {
        images = new ArrayList<>();
        images.add(R.drawable.class_1);
        images.add(R.drawable.class_2);
        images.add(R.drawable.class_3);
        images.add(R.drawable.class_4);
    }
    public int getImage(int i)
    {
        return images.get(i);
    }
}
