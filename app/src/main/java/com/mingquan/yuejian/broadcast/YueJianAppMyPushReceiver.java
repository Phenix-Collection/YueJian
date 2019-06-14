package com.mingquan.yuejian.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.YueJianAppLiveLoginSelectActivity;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class YueJianAppMyPushReceiver extends BroadcastReceiver {
    private static final String TAG = "YueJianAppMyPushReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        YueJianAppTLog.info("[MyReceiver] onReceive:%s, extras:%s", intent.getAction(), printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            YueJianAppTLog.info("[MyReceiver] Registration Id : %s", regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            YueJianAppTLog.info("[MyReceiver] 接收到推送消息: %s", bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String strExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            YueJianAppTLog.info("[MyReceiver] 推送消息ID: %s,是否在前台：%s", notifactionId, YueJianAppTDevice.isAppOnForeground(context));
            // 是视频聊天的通知
            if (strExtra.contains("VipChat") /*&& YueJianAppTDevice.isAppOnForeground(context)*/) {
                try {
                    JSONObject extra = new JSONObject(strExtra);
                    final String inviterInfo = extra.getString("inviter_info");
                    final String liveid = String.valueOf(extra.getInt("liveid"));
                    YueJianAppApiProtoHelper.sendACGetLiveStatusReq(null, liveid, new YueJianAppApiProtoHelper.ACGetLiveStatusReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            YueJianAppTLog.error(errMessage);
                        }

                        @Override
                        public void onResponse(int callTime, int status) {

                            if (status != YueJianAppApiProtoHelper.AUTH_STATUS_NONE) {
                                return;
                            }
                            try {
                                YueJianAppACUserPublicInfoModel userPublicInfoModel = new YueJianAppACUserPublicInfoModel(new JSONObject(inviterInfo));
                                YueJianAppUIHelper.showVideoChatActivity(YueJianAppAppContext.getInstance(), userPublicInfoModel, liveid);
                                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.cancelAll();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            String strExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            YueJianAppTLog.info("打开推送消息:%s", strExtra);
            if (YueJianAppStringUtil.isEmpty(YueJianAppAppContext.getInstance().getLoginUid())) { //已经退出登陆了
                Intent loginIntent = new Intent(YueJianAppAppContext.getInstance(), YueJianAppLiveLoginSelectActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(loginIntent);
            } else {
                if (strExtra.contains("VipChat")) {// 是视频聊天的通知
                    try {
                        JSONObject extra = new JSONObject(strExtra);
                        final String inviterInfo = extra.getString("inviter_info");
                        final String liveid = String.valueOf(extra.getInt("liveid"));
                        YueJianAppApiProtoHelper.sendACGetLiveStatusReq(null, liveid, new YueJianAppApiProtoHelper.ACGetLiveStatusReqCallback() {
                            @Override
                            public void onError(int errCode, String errMessage) {
                                YueJianAppTLog.error(errMessage);
                            }

                            @Override
                            public void onResponse(int callTime, int status) {
                                if (status != YueJianAppApiProtoHelper.AUTH_STATUS_NONE) {
                                    return;
                                }

                                try {
                                    YueJianAppACUserPublicInfoModel userPublicInfoModel = new YueJianAppACUserPublicInfoModel(new JSONObject(inviterInfo));
                                    YueJianAppUIHelper.showVideoChatActivity(YueJianAppAppContext.getInstance(), userPublicInfoModel, liveid);
                                    ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            YueJianAppTLog.info("[MyReceiver] 收到通知回调: %s", bundle.getString(JPushInterface.EXTRA_EXTRA));
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            YueJianAppTLog.info("[MyReceiver] %s, connected state change to %s", intent.getAction(), connected);
        } else {
            YueJianAppTLog.info(TAG, "[MyReceiver] Unhandled action:%s", intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:").append(key).append(", value: [").append(myKey).append(" - ").append(json.optString(myKey)).append("]");
                    }
                } catch (JSONException e) {
                    YueJianAppTLog.info("Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
