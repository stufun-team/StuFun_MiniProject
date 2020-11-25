package com.example.stufun.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stufun.PageAdapter.StudentHomePageAdapter;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.R;
import com.google.android.material.tabs.TabLayout;

public class StudentHome extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Prevalent.currentfragment = "home";

        String pos = "0";
        try{
            pos = getArguments().getString("no");
        }
        catch (Exception ignored){}


        final View view = inflater.inflate(R.layout.fragment_student_home2, container, false);

        TabLayout tabLayout = view.findViewById(R.id.student_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Classroom"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = view.findViewById(R.id.student_viewpager);

        final StudentHomePageAdapter adapter = new StudentHomePageAdapter(
                getActivity().getSupportFragmentManager(),tabLayout.getTabCount()
        );

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

        viewPager.setCurrentItem(Integer.parseInt(pos));

        return view;
    }
}