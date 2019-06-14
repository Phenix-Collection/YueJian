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
import com.mingquan.yuejian.huawei.push.handler.YueJianAppEnableReceiveNormalMsgHandler;

/**
 * 打开透传消息开关的接口。
 */
public class YueJianAppEnableReceiveNormalMsgApi extends YueJianAppBaseApiAgent {

    /**
     * 是否打开开关
     */
    boolean enable;

    /**
     * 调用接口回调
     */
    private YueJianAppEnableReceiveNormalMsgHandler handler;

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
                    onEnableReceiveNormalMsgResult(rst);
                } else {
                    // 开启/关闭透传消息
                    HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(client, enable);
                    onEnableReceiveNormalMsgResult(YueJianAppHMSAgent.AgentResultCode.HMSAGENT_SUCCESS);
                }
            }
        });
    }

    void onEnableReceiveNormalMsgResult(int rstCode) {
        YueJianAppHMSAgentLog.i("enableReceiveNormalMsg:callback=" + YueJianAppStrUtils.objDesc(handler) +" retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new YueJianAppCallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 打开/关闭透传消息
     * @param enable 打开/关闭
     */
    public void enableReceiveNormalMsg(boolean enable, YueJianAppEnableReceiveNormalMsgHandler handler) {
        YueJianAppHMSAgentLog.i("enableReceiveNormalMsg:enable=" + enable + "  handler=" + YueJianAppStrUtils.objDesc(handler));
        this.enable = enable;
        this.handler = handler;
        connect();
    }
}
