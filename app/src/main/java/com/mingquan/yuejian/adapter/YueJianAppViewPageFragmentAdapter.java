package com.mingquan.yuejian.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

@SuppressLint("Recycle")
public class YueJianAppViewPageFragmentAdapter extends FragmentStatePagerAdapter {
  private final Context mContext;
  private final ArrayList<YueJianAppViewPageInfo> mTabs = new ArrayList<YueJianAppViewPageInfo>();

  public YueJianAppViewPageFragmentAdapter(FragmentManager fm, ViewPager pager) {
    super(fm);
    mContext = pager.getContext();
  }

  public void addTab(String title, String tag, Class<?> clss, Bundle args) {
    YueJianAppViewPageInfo viewPageInfo = new YueJianAppViewPageInfo(title, tag, clss, args);
    mTabs.add(viewPageInfo);
  }

  @Override
  public int getCount() {
    return mTabs.size();
  }

  @Override
  public int getItemPosition(Object object) {
    return PagerAdapter.POSITION_NONE;
  }

  @Override
  public Fragment getItem(int position) {
    YueJianAppViewPageInfo info = mTabs.get(position);
    if (info == null || info.clss == null)
      return null;
    return Fragment.instantiate(mContext, info.clss.getName(), info.args);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return mTabs.get(position).title;
  }
}