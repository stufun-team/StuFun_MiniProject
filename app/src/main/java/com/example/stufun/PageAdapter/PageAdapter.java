package com.example.stufun.PageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.stufun.Teacher.CreateClassActivity;
import com.example.stufun.Teacher.StudentsDetailFragment;
import com.example.stufun.Teacher.TeacherClassFragment;
import com.example.stufun.Teacher.TeacherDiscussionFragment;

public class PageAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;
    public PageAdapter(FragmentManager fm,int noOfTabs)
    {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new TeacherClassFragment();
            case 1:
                return new TeacherDiscussionFragment();
            case 2:
                return new StudentsDetailFragment();
            default:
                //noinspection ConstantConditions
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
