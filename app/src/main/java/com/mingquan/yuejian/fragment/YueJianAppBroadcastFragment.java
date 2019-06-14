package com.mingquan.yuejian.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppFragmentViewPagerAdapter;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;
import com.ruffian.library.RVPIndicator;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 主播列表页面
 */

public class YueJianAppBroadcastFragment extends Fragment {

    @BindView(R.id.tv_ignore)
    TextView tvIgnore;
    @BindView(R.id.tv_open_notify)
    TextView tvOpenNotify;
    @BindView(R.id.ll_notify)
    LinearLayout llNotify;
    private View ret;
    @BindView(R.id.app_indicator)
    RVPIndicator mIndicator;
    @BindView(R.id.child_viewpager)
    ViewPager mPager;
    private Context mContext;
    private ArrayList<Fragment> mFragList;
    private Unbinder unbind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (ret == null) {
            ret = inflater.inflate(R.layout.yue_jian_app_index_fragment, null, false);

        }
        unbind = ButterKnife.bind(this, ret);

        initViews();
        initData();
        return ret;
    }

    private void initViews() {
        if (!YueJianAppTDevice.isNotificationEnabled(mContext)) {
            YueJianAppUiUtils.setVisibility(llNotify, View.VISIBLE);
        } else {
            YueJianAppUiUtils.setVisibility(llNotify, View.GONE);
        }

        tvIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YueJianAppUiUtils.setVisibility(llNotify, View.GONE);
            }
        });

        tvOpenNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YueJianAppTDevice.goToNotifySettings(mContext);
                YueJianAppUiUtils.setVisibility(llNotify, View.GONE);
            }
        });
    }

    private void initData() {
        mFragList = new ArrayList<>();
        String[] tabTitles = getResources().getStringArray(R.array.tab_top_titles);

        for (int i = 0; i < tabTitles.length; i++) {
            mFragList.add(YueJianAppBroadcastChildFragment.newInstance(i));
        }

        mIndicator.setTitleList(Arrays.asList(tabTitles));
        mPager.setOffscreenPageLimit(0);
        mPager.setAdapter(new YueJianAppFragmentViewPagerAdapter(getChildFragmentManager(), mFragList));
        mIndicator.setViewPager(mPager, 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @OnClick(R.id.home_search)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_search:
                YueJianAppUIHelper.showSearch(mContext);
                break;
        }
    }
}
