package com.mingquan.yuejian.huawei.push;

import android.os.Handler;
import android.os.Looper;

import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.mingquan.yuejian.huawei.YueJianAppHMSAgent;
import com.mingquan.yuejian.huawei.common.YueJianAppApiClientMgr;
import com.mingquan.yuejian.huawei.common.YueJianAppBaseApiAgent;
import com.mingquan.yuejian.huawei.common.YueJianAppCallbackCodeRunnable;
import com.mingquan.yuejian.huawei.common.YueJianAppHMSAgentLog;
import com.mingquan.yuejian.huawei.common.YueJianAppStrUtils;
import com.mingquan.yuejian.huawei.common.YueJianAppThreadUtil;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppGetPushStateHandler;

/**
 * 获取push状态的接口。
 */
public class YueJianAppGetPushStateApi extends YueJianAppBaseApiAgent {

    /**
     * 调用接口回调
     */
    private YueJianAppGetPushStateHandler handler;

    /**
     * HuaweiApiClient 连接结果回调
     *
     * @param rst    结果码
     * @param client HuaweiApiClient 实例
     */
    @Override
    public void onConnect(final int rst, final HuaweiApiClient client) {
        //需要在子线程中执行获取push状态的操作
        YueJianAppThreadUtil.INST.excute(new Runnable() {
            @Override
            public void run() {
                if (client == null || !YueJianAppApiClientMgr.INST.isConnect(client)) {
                    YueJianAppHMSAgentLog.e("client not connted");
                    onGetPushStateResult(rst);
                } else {
                    HuaweiPush.HuaweiPushApi.getPushState(client);
                    onGetPushStateResult(YueJianAppHMSAgent.AgentResultCode.HMSAGENT_SUCCESS);
                }
            }
        });
    }

    void onGetPushStateResult(int rstCode) {
        YueJianAppHMSAgentLog.i("getPushState:callback=" + YueJianAppStrUtils.objDesc(handler) +" retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new YueJianAppCallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 获取push状态，push状态的回调通过广播发送。
     * 要监听的广播，请参见HMS-SDK开发准备中PushReceiver的注册
     */
    public void getPushState(YueJianAppGetPushStateHandler handler) {
        YueJianAppHMSAgentLog.i("getPushState:handler=" + YueJianAppStrUtils.objDesc(handler));
        this.handler = handler;
        connect();
    }
}
