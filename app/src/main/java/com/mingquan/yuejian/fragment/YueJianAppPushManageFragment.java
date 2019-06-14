package com.mingquan.yuejian.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.umeng.analytics.MobclickAgent;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseFragment;
import com.mingquan.yuejian.utils.YueJianAppSharedPreUtil;

import butterknife.ButterKnife;
import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/4/23.
 */
public class YueJianAppPushManageFragment extends YueJianAppBaseFragment {
  @BindView(R.id.ib_push_manage_start_or_close) ImageButton mBtnStartOrClosePush;
  private final String IS_OPEN_PUSH = "isOpenPush";
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.yue_jian_app_fragment_push_manage, null);
    ButterKnife.bind(this, view);
    initData();
    initView(view);
    return view;
  }

  @Override
  public void initData() {}

  @Override
  public void initView(View view) {
    boolean isOpenPush = YueJianAppSharedPreUtil.getBoolean(getActivity(), IS_OPEN_PUSH);
    changeBtnUI(isOpenPush, mBtnStartOrClosePush);
    mBtnStartOrClosePush.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startOrClosePush();
      }
    });
  }

  private void changeBtnUI(boolean startOrClose, ImageButton ib) {
    ib.setImageResource(startOrClose ? R.drawable.yue_jian_app_global_switch_on : R.drawable.yue_jian_app_global_switch_off);
  }

  private void startOrClosePush() {
    boolean isOpenPush = YueJianAppSharedPreUtil.getBoolean(getActivity(), IS_OPEN_PUSH);
    YueJianAppSharedPreUtil.put(getActivity(), IS_OPEN_PUSH, isOpenPush ? false : true);
    changeBtnUI(isOpenPush ? false : true, mBtnStartOrClosePush);
    if (isOpenPush) {
      JPushInterface.stopPush(getActivity());
    } else {
      JPushInterface.resumePush(getActivity());
    }
  }
  public void onResume() {
    super.onResume();
    MobclickAgent.onPageStart(
        "推送管理"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
    MobclickAgent.onResume(getActivity()); //统计时长
  }
  public void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd("推送管理"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
    // onPageEnd 在onPause 之前调用,因为 onPause
    // 中会保存信息。"SplashScreen"为页面名称，可自定义
    MobclickAgent.onPause(getActivity());
  }
}
