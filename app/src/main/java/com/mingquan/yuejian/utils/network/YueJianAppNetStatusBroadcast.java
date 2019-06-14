package com.mingquan.yuejian.utils.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 网络状态相关
 */

public class YueJianAppNetStatusBroadcast extends BroadcastReceiver {
    private static final String TAG = "YueJianAppNetStatusBroadcast";
    public static final String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    /**
     * 有些时候网络改变时会调用两次,使用这个变了记录上一次改变时的网络状态,如果和上一次相同,则不向下通知
     */
    private static YueJianAppNetType sNetTypeCache = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        YueJianAppNetTools.init(context);
        Log.i(TAG, "Cache Net :" + sNetTypeCache);
        if (YueJianAppNetTools.netType() == sNetTypeCache) {
            return;
        }
        Log.i(TAG, "Changed net :" + YueJianAppNetTools.netType());
        if (intent.getAction().equals(NET_CHANGE_ACTION)) {
            sNetTypeCache = YueJianAppNetTools.netType();
            if (sNetTypeCache.equals(YueJianAppNetType.NO_NET)) {
                return;
            }
            /*if (AppContext.getInstance().mChatServerUrl == null && !AppContext.getInstance().mChatServer.mSocket.connected()) {
                AppContext.getInstance().getConfig();
            }*/
        }
    }
}
