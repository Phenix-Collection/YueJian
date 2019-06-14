package com.mingquan.yuejian.utils;

import android.app.Activity;
import android.content.Context;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.YueJianAppAppManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import cn.jpush.android.api.JPushInterface;

public class YueJianAppLoginUtils {
    private static YueJianAppLoginUtils loginUtils = null;

    public static YueJianAppLoginUtils getInstance() {
        if (null == loginUtils) {
            loginUtils = new YueJianAppLoginUtils();
        }
        return loginUtils;
    }

    public static void outLogin(final Context context) {
        JPushInterface.stopPush(context);
        YueJianAppAppContext.getInstance().Logout();
        YueJianAppAppManager.getAppManager().finishAllActivity();
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                YueJianAppAppContext.getInstance().mChatServer.close();
                YueJianAppAppContext.getInstance().mChatServer = null;
                YueJianAppUIHelper.showLoginSelectActivity(context);
                ((Activity)context).finish();
            }

            @Override
            public void onError(int i, String s) {
                YueJianAppTLog.error("环信退出失败");
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }
}
