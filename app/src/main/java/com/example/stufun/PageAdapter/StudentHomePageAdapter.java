package com.example.stufun.PageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.stufun.Chat.ChatFragment;
import com.example.stufun.Prevalent.Prevalent;
import com.example.stufun.Student.StudentHomeFragment;
import com.example.stufun.Teacher.HomeFragment;

public class StudentHomePageAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;
    public StudentHomePageAdapter(FragmentManager fm, int noOfTabs)
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
                if(Prevalent.type.equals("Teacher"))
                    return new HomeFragment();
                else
                    return new StudentHomeFragment();
            case 1:
                return new ChatFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
