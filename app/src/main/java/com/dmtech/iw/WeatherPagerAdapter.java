package com.dmtech.iw;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class WeatherPagerAdapter extends FragmentStatePagerAdapter {

    private List<WeatherFragment> fragments = new ArrayList<>();

    public WeatherPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void setFragments(List<WeatherFragment> fs) {
        this.fragments.clear();
        this.fragments.addAll(fs);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
