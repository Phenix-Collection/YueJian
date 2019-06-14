package com.mingquan.yuejian.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppFragmentViewPagerAdapter;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.ruffian.library.RVPIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 短视频列表页面
 */
public class YueJianAppShortVideoFragment extends Fragment {
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    Unbinder unbinder;
    @BindView(R.id.indicator)
    RVPIndicator mIndicator;
    private View ret;
    private List<String> mTitles;
    private List<Fragment> mFragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (ret == null) {
            ret = inflater.inflate(R.layout.yue_jian_app_index_like_fragment, null, false);
        }
        unbinder = ButterKnife.bind(this, ret);
        initData();

        return ret;
    }

    private void initData() {
        if (mTitles == null) {
            mTitles = new ArrayList<>();
            mTitles.add("日榜");
            mTitles.add("周榜");
            mTitles.add("月榜");
            mTitles.add(getString(R.string.newest));
            mTitles.add(getString(R.string.attention));
        }

        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(YueJianAppShortVideoChildFragment.newInstance(YueJianAppApiProtoHelper.VIDEO_TYPE_DAILY_TOP, "")); // 日榜
            mFragments.add(YueJianAppShortVideoChildFragment.newInstance(YueJianAppApiProtoHelper.VIDEO_TYPE_WEEKLY_TOP, "")); // 周榜
            mFragments.add(YueJianAppShortVideoChildFragment.newInstance(YueJianAppApiProtoHelper.VIDEO_TYPE_MONTHLY_TOP, "")); // 月榜
            mFragments.add(YueJianAppShortVideoChildFragment.newInstance(YueJianAppApiProtoHelper.VIDEO_TYPE_NEW, "")); // 最新
            mFragments.add(YueJianAppShortVideoChildFragment.newInstance(YueJianAppApiProtoHelper.VIDEO_TYPE_FOLLOWED_BROADCASTER, "")); // 关注的主播的短视频
        }

        mIndicator.setTitleList(mTitles);
        mIndicator.setViewPager(mViewpager, 0);
        mViewpager.setOffscreenPageLimit(0);
        mViewpager.setAdapter(new YueJianAppFragmentViewPagerAdapter(getChildFragmentManager(), mFragments));
        mViewpager.setCurrentItem(3);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
