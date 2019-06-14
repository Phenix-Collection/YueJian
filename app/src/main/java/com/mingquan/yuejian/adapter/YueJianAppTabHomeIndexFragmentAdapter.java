package com.mingquan.yuejian.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by admin on 2017/4/11.
 */

public class YueJianAppTabHomeIndexFragmentAdapter extends FragmentPagerAdapter {

    private final String[] titles;
    private Context context;
    private List<Fragment> fragments;

    public YueJianAppTabHomeIndexFragmentAdapter(Context context, List<Fragment> fragments, String[] titles, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}