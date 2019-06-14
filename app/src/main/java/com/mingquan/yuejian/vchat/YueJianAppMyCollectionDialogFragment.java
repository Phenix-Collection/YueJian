package com.mingquan.yuejian.vchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppFragmentViewPagerAdapter;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;
import com.mingquan.yuejian.fragment.YueJianAppShortVideoChildFragment;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.ruffian.library.RVPIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sun on 2018/9/4.
 * <p>
 * 我的收藏列表页面
 */

public class YueJianAppMyCollectionDialogFragment extends YueJianAppBaseDialogFragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.indicator)
    RVPIndicator mIndicator;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    Unbinder unbinder;
    private List<String> mTitles;
    private List<Fragment> mFragments;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_my_collection, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    public void initData() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (mTitles == null) {
            mTitles = new ArrayList<>();
            mTitles.add("我喜欢的");
            mTitles.add("我购买的");
        }

        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(YueJianAppShortVideoChildFragment.newInstance(YueJianAppApiProtoHelper.VIDEO_TYPE_FOLLOWED, ""));
            mFragments.add(YueJianAppShortVideoChildFragment.newInstance(YueJianAppApiProtoHelper.VIDEO_TYPE_PURCHASED, ""));
        }

        //设置 title
        mIndicator.setTitleList(mTitles);
        mIndicator.setViewPager(mViewPager, 0);
        mViewPager.setCurrentItem(0);
        mViewPager.setAdapter(new YueJianAppFragmentViewPagerAdapter(getChildFragmentManager(), mFragments));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

