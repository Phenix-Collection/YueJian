package com.mingquan.yuejian.vchat;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.mingquan.yuejian.utils.YueJianAppTLog;

import java.util.List;

public class YueJianAppVideoLayoutAdapter extends PagerAdapter {

    private List<View> mViews;

    public YueJianAppVideoLayoutAdapter(List<View> views) {
        this.mViews = views;
    }


    public void setViews(List<View> mViews) {
        this.mViews = mViews;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViews.get(position));
    }
}