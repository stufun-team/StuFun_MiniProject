package com.example.stufun.Teacher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stufun.PageAdapter.PageAdapter;
import com.example.stufun.Prevalent.CurrentClass;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;


public class TeacherClass extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        Prevalent.currentfragment = "frag";

        TextView textView = view.findViewById(R.id.class_teacher_head);
        textView.setText(CurrentClass.classname);


        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_announcement_24));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_question_answer_24));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_group_24));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = view.findViewById(R.id.view_pager);
        final PageAdapter adapter = new PageAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                ,tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }
}