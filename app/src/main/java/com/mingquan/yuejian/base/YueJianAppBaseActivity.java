package com.mingquan.yuejian.base;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.YueJianAppAppManager;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.interf.YueJianAppIBaseView;
import com.mingquan.yuejian.interf.YueJianAppIDialogControl;
import com.mingquan.yuejian.ui.dialog.YueJianAppCommonToast;
import com.mingquan.yuejian.ui.view.YueJianAppToastDialog;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppInputMethodUtils;
import com.mingquan.yuejian.utils.YueJianAppSharedPreUtil;
import com.mingquan.yuejian.utils.YueJianAppStatusBarUtils;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * baseActionBar Activity
 */
public abstract class YueJianAppBaseActivity extends AppCompatActivity implements YueJianAppIDialogControl, View.OnClickListener, YueJianAppIBaseView {
  private static final String TAG = YueJianAppBaseActivity.class.getSimpleName();

  private boolean _isVisible;
  private ProgressDialog _waitDialog;
  protected LayoutInflater mInflater;
  protected ActionBar mActionBar;
  private TextView mTvActionTitle;
  private TextView mTvTitle;
  protected TextView mTvRight;
  protected ImageView mImgMore;
  private View mActionBarView;
  private ConnectivityManager mConnectivityManager;
  private NetworkInfo mNetWorkInfo;
  private YueJianAppToastDialog mToastDialog;
  /**
   * 全局的加载框对象，已经完成初始化.
   */
  public ProgressDialog mProgressDialog;
  /**
   * 加载框的文字说明.
   */
  private String mProgressMessage = "请稍候...";

  private BroadcastReceiver logoutReceiver = null;
  private Unbinder unbinder;

  @Override
  protected void onDestroy() {
    super.onDestroy();

    if (mToastDialog != null) {
      mToastDialog.dismiss();
    }
    unregisterReceiver(logoutReceiver);
    YueJianAppTDevice.hideSoftKeyboard(getCurrentFocus());
    YueJianAppInputMethodUtils.fixInputMethodManagerLeak(this);

    unbinder.unbind();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    YueJianAppAppManager.getAppManager().addActivity(this);
    YueJianAppStatusBarUtils.setWindowStatusBarColor(this, R.color.white);
    if (!hasActionBar()) {
      supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    onBeforeSetContentLayout();
    if (getLayoutId() != 0) {
      setContentView(getLayoutId());
    }
    mActionBar = getSupportActionBar();
    mInflater = getLayoutInflater();
    if (hasActionBar()) {
      initActionBar(mActionBar);
    }

    // 通过注解绑定控件
    unbinder = ButterKnife.bind(this);
    init(savedInstanceState);
//    init(this);
    initView();
    initData();
    _isVisible = true;
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    logoutReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
          mConnectivityManager =
              (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
          mNetWorkInfo = mConnectivityManager.getActiveNetworkInfo();
          if (mNetWorkInfo != null && mNetWorkInfo.isAvailable()) {
            /////////////网络连接
            String name = mNetWorkInfo.getTypeName();
            if (mNetWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
              YueJianAppSharedPreUtil.put(YueJianAppBaseActivity.this, YueJianAppAppConfig.HAS_ALERT_NETWORK_CHANGE, false);
              /////WiFi网络
            } else if (mNetWorkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
              /////有线网络
            } else if (mNetWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
              /////////3g网络
              YueJianAppTLog.warn("切换到移动网络");
              if (null != mToastDialog) {
                if (!YueJianAppSharedPreUtil.getBoolean(
                        YueJianAppBaseActivity.this, YueJianAppAppConfig.HAS_ALERT_NETWORK_CHANGE, false)) {
                  mToastDialog.setTitle(getString(R.string.net_work_change));
                  mToastDialog.setTimeLong(10 * 1000);
                  mToastDialog.show();
                  YueJianAppSharedPreUtil.put(YueJianAppBaseActivity.this, YueJianAppAppConfig.HAS_ALERT_NETWORK_CHANGE, true);
                }
              }
            }
          } else {
            ////////网络断开
          }
        }
      }
    };
    initToastDialog();
    registerReceiver(logoutReceiver, intentFilter);
  }

  private void initToastDialog() {
    mToastDialog = new YueJianAppToastDialog(this);
  }

  /**
   * 描述：显示进度框.
   *
   * @param message the message
   */
  public void showProgressDialog(String message) {
    if (YueJianAppAppManager.activityIsDestroyed(this)) {
      return;
    }
    // 创建一个显示进度的Dialog
    if (!TextUtils.isEmpty(message)) {
      mProgressMessage = message;
    }
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressDialog(this);
      // 设置点击屏幕Dialog不消失
      mProgressDialog.setCanceledOnTouchOutside(false);
    }
    mProgressDialog.setMessage(mProgressMessage);
    this.showDialog(YueJianAppAppConfig.DIALOG_PROGRESS);
  }

  @Override
  public Dialog onCreateDialog(int id) {
    Dialog dialog = null;
    switch (id) {
      case YueJianAppAppConfig.DIALOG_PROGRESS:
        if (mProgressDialog == null) {
          Log.i(TAG, "Dialog方法调用错误,请调用showProgressDialog()!");
        }
        return mProgressDialog;
    }
    return dialog;
  }

  /**
   * 描述：移除进度框.
   */
  public void removeProgressDialog() {
    this.removeDialog(YueJianAppAppConfig.DIALOG_PROGRESS);
  }

  @TargetApi(19)
  private void setTranslucentStatus(boolean on) {
    Window win = getWindow();
    WindowManager.LayoutParams winParams = win.getAttributes();
    final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    if (on) {
      winParams.flags |= bits;
    } else {
      winParams.flags &= ~bits;
    }
    win.setAttributes(winParams);
  }

  protected void onBeforeSetContentLayout() {}

  protected boolean hasActionBar() {
    return true;
  }

  protected int getLayoutId() {
    return 0;
  }

  protected View inflateView(int resId) {
    return mInflater.inflate(resId, null);
  }

  protected int getActionBarTitle() {
    return R.string.app_name;
  }

  protected boolean hasBackButton() {
    return false;
  }

  protected void init(Bundle savedInstanceState) {}

  //    protected void initActionBar(ActionBar actionBar) {
  //        if (actionBar == null)
  //            return;
  //        if (hasBackButton()) {
  //            mActionBar.setDisplayHomeAsUpEnabled(true);
  //            mActionBar.setHomeButtonEnabled(true);
  //        } else {
  //            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
  //            actionBar.setDisplayUseLogoEnabled(false);
  //            int titleRes = getActionBarTitle();
  //            if (titleRes != 0) {
  //                actionBar.setTitle(titleRes);
  //            }
  //        }
  //    }

  protected void initActionBar(ActionBar actionBar) {
    if (actionBar == null)
      return;
    mActionBarView = getLayoutInflater().inflate(
        R.layout.yue_jian_app_view_actionbar_title, new LinearLayout(getApplicationContext()), false);
    ((RelativeLayout) mActionBarView.findViewById(R.id.base_title))
        .setBackgroundColor(YueJianAppAppContext.getInstance().getmMainColor());
    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    ActionBar.LayoutParams params = new ActionBar.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    actionBar.setCustomView(mActionBarView, params);
    mTvTitle = (TextView) actionBar.getCustomView().findViewById(R.id.tv_actionBarTitle);
    mTvTitle.setText(getActionBarTitle());
    mTvRight = (TextView) actionBar.getCustomView().findViewById(R.id.tv_text);
    mImgMore = (ImageView) actionBar.getCustomView().findViewById(R.id.iv_more);
    mTvRight.setVisibility(View.GONE);
    YueJianAppUiUtils.setVisibility(mImgMore, View.GONE);
    LinearLayout mLlBack = (LinearLayout) actionBar.getCustomView().findViewById(R.id.ll_back);
    mLlBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  public void setActionBarTitle(int resId) {
    if (resId != 0) {
      setActionBarTitle(getString(resId));
    }
  }

  public void setActionBarTitle(String title) {
    if (YueJianAppStringUtil.isEmpty(title)) {
      title = getString(R.string.app_name);
    }
    if (hasActionBar() && mActionBar != null) {
      if (mTvActionTitle != null) {
        mTvActionTitle.setText(title);
      }
      if (mTvTitle != null) {
        mTvTitle.setText(title);
      }
      mActionBar.setTitle(title);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        break;

      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  public void showToast(int msgResid, int icon, int gravity) {
    showToast(getString(msgResid), icon, gravity);
  }

  public void showToast2(String msg) {
    AppMsg.makeText(this, msg, new AppMsg.Style(1000, R.drawable.yue_jian_app_toast_background)).show();
  }

  public void showToast(String message, int icon, int gravity) {
    YueJianAppCommonToast toast = new YueJianAppCommonToast(this);
    toast.setMessage(message);
    toast.setMessageIc(icon);
    toast.setLayoutGravity(gravity);
    toast.show();
  }

  @Override
  public ProgressDialog showWaitDialog() {
    return showWaitDialog(R.string.loading);
  }

  @Override
  public ProgressDialog showWaitDialog(int resid) {
    return showWaitDialog(getString(resid));
  }

  @Override
  public ProgressDialog showWaitDialog(String message) {
    if (_isVisible) {
      if (_waitDialog == null) {
        _waitDialog = YueJianAppDialogHelp.getWaitDialog(this, message);
      }
        _waitDialog.setCanceledOnTouchOutside(false);
        _waitDialog.show();
      return _waitDialog;
    }
    return null;
  }

  @Override
  public void hideWaitDialog() {
    if (_isVisible && _waitDialog != null) {
      try {
        _waitDialog.dismiss();
        _waitDialog = null;
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  @Override
  public boolean onMenuOpened(int featureId, Menu menu) {
    // setOverflowIconVisible(featureId, menu);
    return super.onMenuOpened(featureId, menu);
  }
}
