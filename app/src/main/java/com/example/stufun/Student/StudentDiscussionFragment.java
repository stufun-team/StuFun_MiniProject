package com.example.stufun.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stufun.R;
import com.example.stufun.StudentClassFragment;
import com.example.stufun.Teacher.StudentsDetailFragment;

public class StudentDiscussionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_student_discussion, container, false);
    }

    public static class StudentClassPageAdapter extends FragmentStatePagerAdapter {

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
}