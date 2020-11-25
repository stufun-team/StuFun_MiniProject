package com.example.stufun.PageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.stufun.Student.StudentDiscussionFragment;
import com.example.stufun.Student.StudentClassFragment;
import com.example.stufun.Teacher.StudentsDetailFragment;

public class StudentClassPageAdapter extends FragmentStatePagerAdapter {

    private int noOfTabs;
    public StudentClassPageAdapter(FragmentManager fm, int noOfTabs)
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
                return new StudentClassFragment();
            case 1:
                return new StudentDiscussionFragment();
            case 2:
                return new StudentsDetailFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
