package com.mingquan.yuejian.huawei.push;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.mingquan.yuejian.huawei.YueJianAppHMSAgent;
import com.mingquan.yuejian.huawei.common.YueJianAppApiClientMgr;
import com.mingquan.yuejian.huawei.common.YueJianAppBaseApiAgent;
import com.mingquan.yuejian.huawei.common.YueJianAppCallbackCodeRunnable;
import com.mingquan.yuejian.huawei.common.YueJianAppHMSAgentLog;
import com.mingquan.yuejian.huawei.common.YueJianAppStrUtils;
import com.mingquan.yuejian.huawei.common.YueJianAppThreadUtil;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppDeleteTokenHandler;

/**
 * 删除pushtoken的接口。
 */
public class YueJianAppDeleteTokenApi extends YueJianAppBaseApiAgent {

    /**
     * 待删除的push token
     */
    private String token;

    /**
     * 调用接口回调
     */
    private YueJianAppDeleteTokenHandler handler;

    /**
     * HuaweiApiClient 连接结果回调
     *
     * @param rst    结果码
     * @param client HuaweiApiClient 实例
     */
    @Override
    public void onConnect(final int rst, final HuaweiApiClient client) {
        //需要在子线程中执行删除TOKEN操作
        YueJianAppThreadUtil.INST.excute(new Runnable() {
            @Override
            public void run() {
                //调用删除TOKEN需要传入通过getToken接口获取到TOKEN，并且需要对TOKEN进行非空判断
                if (!TextUtils.isEmpty(token)){
                    if (client == null || !YueJianAppApiClientMgr.INST.isConnect(client)) {
                        YueJianAppHMSAgentLog.e("client not connted");
                        onDeleteTokenResult(rst);
                    } else {
                        try {
                            HuaweiPush.HuaweiPushApi.deleteToken(client, token);
                            onDeleteTokenResult(YueJianAppHMSAgent.AgentResultCode.HMSAGENT_SUCCESS);
                        } catch (Exception e) {
                            YueJianAppHMSAgentLog.e("删除TOKEN失败:" + e.getMessage());
                            onDeleteTokenResult(YueJianAppHMSAgent.AgentResultCode.CALL_EXCEPTION);
                        }
                    }
                } else {
                    YueJianAppHMSAgentLog.e("删除TOKEN失败: 要删除的token为空");
                    onDeleteTokenResult(YueJianAppHMSAgent.AgentResultCode.EMPTY_PARAM);
                }
            }
        });
    }

    void onDeleteTokenResult(int rstCode) {
        YueJianAppHMSAgentLog.i("deleteToken:callback=" + YueJianAppStrUtils.objDesc(handler) +" retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new YueJianAppCallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 删除指定的pushtoken
     * 该接口只在EMUI5.1以及更高版本的华为手机上调用该接口后才不会收到PUSH消息。
     * @param token 要删除的token
     */
    public void deleteToken(String token, YueJianAppDeleteTokenHandler handler) {
        YueJianAppHMSAgentLog.i("deleteToken:token:" + YueJianAppStrUtils.objDesc(token) + " handler=" + YueJianAppStrUtils.objDesc(handler));
        this.token = token;
        this.handler = handler;
        connect();
    }
}