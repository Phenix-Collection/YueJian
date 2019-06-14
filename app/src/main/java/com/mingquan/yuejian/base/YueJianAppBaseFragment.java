package com.mingquan.yuejian.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.YueJianAppAppManager;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.interf.YueJianAppIBaseFragment;
import com.mingquan.yuejian.interf.YueJianAppIDialogControl;
import com.mingquan.yuejian.utils.YueJianAppStatusBarUtils;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;

/**
 * 碎片基类
 */
public abstract class YueJianAppBaseFragment extends Fragment implements View.OnClickListener, YueJianAppIBaseFragment {
    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;
    public static final int STATE_PRESSNONE = 4; // 正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;

    protected LayoutInflater mInflater;

    public YueJianAppAppContext getApplication() {
        return (YueJianAppAppContext) getActivity().getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver();

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = super.onCreateView(inflater, container, savedInstanceState);
//        YueJianAppStatusBarUtils.setWindowStatusBarColor(getActivity(), YueJianAppAppContext.getInstance().getmMainColor());
        YueJianAppStatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.red);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(logoutReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int getLayoutId() {
        return 0;
    }

    protected View inflateView(int resId) {
        return this.mInflater.inflate(resId, null);
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void hideWaitDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof YueJianAppIDialogControl) {
            ((YueJianAppIDialogControl) activity).hideWaitDialog();
        }
    }

    protected ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    protected ProgressDialog showWaitDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof YueJianAppIDialogControl) {
            return ((YueJianAppIDialogControl) activity).showWaitDialog(resid);
        }
        return null;
    }

    protected ProgressDialog showWaitDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity instanceof YueJianAppIDialogControl) {
            return ((YueJianAppIDialogControl) activity).showWaitDialog(str);
        }
        return null;
    }

    @Override
    public void initView(View view) {
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
    }

    protected void requestData(boolean refresh) {
    }

    protected BroadcastReceiver logoutReceiver = null;

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(YueJianAppAppConfig.ACTION_LOGOUT);
        logoutReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(YueJianAppAppConfig.ACTION_LOGOUT)) {
                    getActivity().runOnUiThread(new Runnable() {
                        @SuppressLint("NewApi")
                        @Override
                        public void run() {
                            YueJianAppAppContext.showToastAppMsg(getActivity(), "您的账号已在其他设备登录，请重新登录!",
                                    R.color.brightyellow, 3000);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    YueJianAppAppContext.getInstance().Logout();
                                    YueJianAppUIHelper.showLoginSelectActivity(getActivity());
                                    YueJianAppAppManager.getAppManager().finishAllActivity();
                                    if (getActivity() != null)
                                        getActivity().finish();
                                }
                            }, 3000);
                        }
                    });
                }
            }
        };
        getActivity().registerReceiver(logoutReceiver, intentFilter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
