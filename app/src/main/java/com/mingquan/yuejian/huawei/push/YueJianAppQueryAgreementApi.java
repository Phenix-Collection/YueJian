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
import com.mingquan.yuejian.huawei.push.handler.YueJianAppQueryAgreementHandler;

/**
 * 获取push协议展示的接口。
 */
public class YueJianAppQueryAgreementApi extends YueJianAppBaseApiAgent {

    /**
     * 调用接口回调
     */
    private YueJianAppQueryAgreementHandler handler;

    /**
     * HuaweiApiClient 连接结果回调
     *
     * @param rst    结果码
     * @param client HuaweiApiClient 实例
     */
    @Override
    public void onConnect(final int rst, final HuaweiApiClient client) {
        //需要在子线程中执行获取push协议展示的操作
        YueJianAppThreadUtil.INST.excute(new Runnable() {
            @Override
            public void run() {
                if (client == null || !YueJianAppApiClientMgr.INST.isConnect(client)) {
                    YueJianAppHMSAgentLog.e("client not connted");
                    onQueryAgreementResult(rst);
                } else {
                    HuaweiPush.HuaweiPushApi.queryAgreement(client);
                    onQueryAgreementResult(YueJianAppHMSAgent.AgentResultCode.HMSAGENT_SUCCESS);
                }
            }
        });
    }

    void onQueryAgreementResult(int rstCode) {
        YueJianAppHMSAgentLog.i("queryAgreement:callback=" + YueJianAppStrUtils.objDesc(handler) +" retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new YueJianAppCallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 请求push协议展示
     */
    public void queryAgreement(YueJianAppQueryAgreementHandler handler) {
        YueJianAppHMSAgentLog.i("queryAgreement:handler=" + YueJianAppStrUtils.objDesc(handler));
        this.handler = handler;
        connect();
    }
}
