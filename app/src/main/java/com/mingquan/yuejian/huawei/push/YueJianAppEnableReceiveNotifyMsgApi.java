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
import com.mingquan.yuejian.huawei.push.handler.YueJianAppEnableReceiveNotifyMsgHandler;

/**
 * 打开自呈现消息开关的接口。
 */
public class YueJianAppEnableReceiveNotifyMsgApi extends YueJianAppBaseApiAgent {

    /**
     * 是否打开开关
     */
    boolean enable;

    /**
     * 调用接口回调
     */
    private YueJianAppEnableReceiveNotifyMsgHandler handler;

    /**
     * HuaweiApiClient 连接结果回调
     *
     * @param rst    结果码
     * @param client HuaweiApiClient 实例
     */
    @Override
    public void onConnect(final int rst, final HuaweiApiClient client) {
        //需要在子线程中执行开关的操作
        YueJianAppThreadUtil.INST.excute(new Runnable() {
            @Override
            public void run() {
                if (client == null || !YueJianAppApiClientMgr.INST.isConnect(client)) {
                    YueJianAppHMSAgentLog.e("client not connted");
                    onEnableReceiveNotifyMsgResult(rst);
                } else {
                    // 开启/关闭自呈现消息
                    HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(client, enable);
                    onEnableReceiveNotifyMsgResult(YueJianAppHMSAgent.AgentResultCode.HMSAGENT_SUCCESS);
                }
            }
        });
    }

    void onEnableReceiveNotifyMsgResult(int rstCode) {
        YueJianAppHMSAgentLog.i("enableReceiveNotifyMsg:callback=" + YueJianAppStrUtils.objDesc(handler) +" retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new YueJianAppCallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 打开/关闭自呈现消息
     * @param enable 打开/关闭
     */
    public void enableReceiveNotifyMsg(boolean enable, YueJianAppEnableReceiveNotifyMsgHandler handler) {
        YueJianAppHMSAgentLog.i("enableReceiveNotifyMsg:enable=" + enable + " handler=" + YueJianAppStrUtils.objDesc(handler));
        this.enable = enable;
        this.handler = handler;
        connect();
    }
}
