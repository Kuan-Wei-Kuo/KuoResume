package com.kuo.kuoresume.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/30.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<String> titles = new ArrayList<>();

    ArrayList<Fragment> fragments = new ArrayList<>();

    public ViewPagerAdapter(ArrayList<String> titles, ArrayList<Fragment> fragments, FragmentManager fragmentManager) {
        super(fragmentManager);

        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
