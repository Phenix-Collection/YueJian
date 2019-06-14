package com.mingquan.yuejian.huawei;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.huawei.hms.support.api.push.PushReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/2/22
 */

public class YueJianAppHuaweiPushReceiver extends PushReceiver {

    public static final String TAG = "HuaweiPushRevicer";

    public static final String ACTION_UPDATE_UI = "action.updateUI";
    public static final String ACTION_TOKEN = "action.updateToken";

    private static List<IPushCallback> pushCallbacks = new ArrayList<>();
    private static final Object CALLBACK_LOCK = new Object();

    public interface IPushCallback {
        void onReceive(Intent intent);
    }

    public static void registerPushCallback(IPushCallback callback) {
        synchronized (CALLBACK_LOCK) {
            pushCallbacks.add(callback);
        }
    }

    public static void unRegisterPushCallback(IPushCallback callback) {
        synchronized (CALLBACK_LOCK) {
            pushCallbacks.remove(callback);
        }
    }

    @Override
    public void onToken(Context context, String tokenIn, Bundle extras) {
        YueJianAppTLog.info("收到华为token：%s", tokenIn);
        String belongId = extras.getString("belongId");

        Intent intent = new Intent();
        intent.setAction(ACTION_TOKEN);
        intent.putExtra(ACTION_TOKEN, tokenIn);
        callBack(intent);

        intent = new Intent();
        intent.setAction(ACTION_UPDATE_UI);
        intent.putExtra("log", "belongId is:" + belongId + " Token is:" + tokenIn);
        callBack(intent);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            //CP可以自己解析消息内容，然后做相应的处理 | CP can parse message content on its own, and then do the appropriate processing
            String content = new String(msg, "UTF-8");
            Intent intent = new Intent();
            intent.setAction(ACTION_UPDATE_UI);
            intent.putExtra("log", "Receive a push pass message with the message:" + content);
            callBack(intent);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(ACTION_UPDATE_UI);
            intent.putExtra("log", "Receive push pass message, exception:" + e.getMessage());
            callBack(intent);
        }
        return false;
    }

    public void onEvent(Context context, Event event, Bundle extras) {
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_UI);

        int notifyId = 0;
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
        }

        String message = extras.getString(BOUND_KEY.pushMsgKey);
        intent.putExtra("log", "Received event,notifyId:" + notifyId + " msg:" + message);
        callBack(intent);
        super.onEvent(context, event, extras);
    }

    @Override
    public void onPushState(Context context, boolean pushState) {
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_UI);
        intent.putExtra("log", "The Push connection status is:" + pushState);
        callBack(intent);
    }

    private static void callBack(Intent intent) {
        synchronized (CALLBACK_LOCK) {
            for (IPushCallback callback : pushCallbacks) {
                if (callback != null) {
                    callback.onReceive(intent);
                }
            }
        }
    }
}
