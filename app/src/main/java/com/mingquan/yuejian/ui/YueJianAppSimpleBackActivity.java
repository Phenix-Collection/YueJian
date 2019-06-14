package com.mingquan.yuejian.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.YueJianAppAppManager;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseActivity;
import com.mingquan.yuejian.bean.YueJianAppSimpleBackPage;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;

import java.lang.ref.WeakReference;

public class YueJianAppSimpleBackActivity extends YueJianAppBaseActivity {
  public final static String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
  public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";
  private static final String TAG = "FLAG_TAG";
  protected WeakReference<Fragment> mFragment;
  protected int mPageValue = -1;

  @Override
  protected int getLayoutId() {
    return R.layout.yue_jian_app_activity_simple_fragment;
  }

  @Override
  public void initView() {}

  @Override
  public void initData() {
    registerReceiver();
  }

  protected BroadcastReceiver logoutReceiver = null;
  private void registerReceiver() {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(YueJianAppAppConfig.ACTION_LOGOUT);
    logoutReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(YueJianAppAppConfig.ACTION_LOGOUT)) {
          runOnUiThread(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
              YueJianAppAppContext.showToastAppMsg(YueJianAppSimpleBackActivity.this,
                  "您的账号已在其他设备登录，请重新登录!", R.color.brightyellow, 3000);
              new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  YueJianAppAppContext.getInstance().Logout();
                  YueJianAppUIHelper.showLoginSelectActivity(YueJianAppSimpleBackActivity.this);
                  YueJianAppAppManager.getAppManager().finishAllActivity();
                  finish();
                }
              }, 3000);
            }
          });
        }
      }
    };
    registerReceiver(logoutReceiver, intentFilter);
  }

  @Override
  public void onClick(View v) {}

  @Override
  protected boolean hasActionBar() {
    return false;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    super.init(savedInstanceState);
    if (mPageValue == -1) {
      mPageValue = getIntent().getIntExtra(BUNDLE_KEY_PAGE, 0);
    }
    initFromIntent(mPageValue, getIntent());
  }

  protected void initFromIntent(int pageValue, Intent data) {
    if (data == null) {
      throw new RuntimeException("you must provide a page info to display");
    }
    YueJianAppSimpleBackPage page = YueJianAppSimpleBackPage.getPageByValue(pageValue);
    if (page == null) {
      throw new IllegalArgumentException("can not find page by value:" + pageValue);
    }

    setActionBarTitle(page.getTitle());

    try {
      Fragment fragment = (Fragment) page.getClz().newInstance();

      Bundle args = data.getBundleExtra(BUNDLE_KEY_ARGS);

      if (args != null) {
        fragment.setArguments(args);
      }

      FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
      trans.replace(R.id.container, fragment, TAG);
      trans.commitAllowingStateLoss();

      mFragment = new WeakReference<Fragment>(fragment);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException("generate fragment error. by value:" + pageValue);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    try {
      unregisterReceiver(logoutReceiver);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
