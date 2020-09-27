package com.example.zempisslepmapy;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> FragmentList = new ArrayList<>();
    private final List<String> TitleList = new ArrayList<>();

    public SectionsPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addFragmet(Fragment fragment, String title) {
        FragmentList.add(fragment);
        TitleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TitleList.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("debug", "getFragment: " + position);
        return FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

}
