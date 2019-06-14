package com.mingquan.yuejian.huawei.push;

import android.os.Handler;
import android.os.Looper;

import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.entity.core.CommonCode;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.mingquan.yuejian.huawei.YueJianAppHMSAgent;
import com.mingquan.yuejian.huawei.common.YueJianAppApiClientMgr;
import com.mingquan.yuejian.huawei.common.YueJianAppBaseApiAgent;
import com.mingquan.yuejian.huawei.common.YueJianAppCallbackCodeRunnable;
import com.mingquan.yuejian.huawei.common.YueJianAppHMSAgentLog;
import com.mingquan.yuejian.huawei.common.YueJianAppStrUtils;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppGetTokenHandler;

/**
 * 获取token的push接口，token的结果通过广播进行接收。
 */
public class YueJianAppGetTokenApi extends YueJianAppBaseApiAgent {

    /**
     * client 无效最大尝试次数
     */
    private static final int MAX_RETRY_TIMES = 1;

    /**
     * 结果回调
     */
    private YueJianAppGetTokenHandler handler;

    /**
     * 当前剩余重试次数
     */
    private int retryTimes = MAX_RETRY_TIMES;

    /**
     * HuaweiApiClient 连接结果回调
     *
     * @param rst    结果码
     * @param client HuaweiApiClient 实例
     */
    @Override
    public void onConnect(int rst, HuaweiApiClient client) {
        if (client == null || !YueJianAppApiClientMgr.INST.isConnect(client)) {
            YueJianAppHMSAgentLog.e("client not connted");
            onPushTokenResult(rst, null);
            return;
        }

        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(client);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {
            @Override
            public void onResult(TokenResult result) {
                if (result == null) {
                    YueJianAppHMSAgentLog.e("result is null");
                    onPushTokenResult(YueJianAppHMSAgent.AgentResultCode.RESULT_IS_NULL, null);
                    return;
                }

                Status status = result.getStatus();
                if (status == null) {
                    YueJianAppHMSAgentLog.e("status is null");
                    onPushTokenResult(YueJianAppHMSAgent.AgentResultCode.STATUS_IS_NULL, null);
                    return;
                }

                int rstCode = status.getStatusCode();
                YueJianAppHMSAgentLog.d("rstCode=" + rstCode);
                // 需要重试的错误码，并且可以重试
                if ((rstCode == CommonCode.ErrorCode.SESSION_INVALID
                        || rstCode == CommonCode.ErrorCode.CLIENT_API_INVALID) && retryTimes > 0) {
                    retryTimes--;
                    connect();
                } else {
                    onPushTokenResult(rstCode, result);
                }
            }
        });
    }

    /**
     * 获取pushtoken接口调用回调
     * pushtoken通过广播下发，要监听的广播，请参见HMS-SDK开发准备中PushReceiver的注册
     * @param rstCode 结果码
     * @param result 调用获取pushtoken接口的结果
     */
    void onPushTokenResult(int rstCode, TokenResult result) {
        YueJianAppHMSAgentLog.i("getToken:callback=" + YueJianAppStrUtils.objDesc(handler) +" retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new YueJianAppCallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
        retryTimes = MAX_RETRY_TIMES;
    }

    /**
     * 获取pushtoken接口
     * pushtoken通过广播下发，要监听的广播，请参见HMS-SDK开发准备中PushReceiver的注册
     * @param handler pushtoken接口调用回调
     */
    public void getToken(YueJianAppGetTokenHandler handler) {
        YueJianAppHMSAgentLog.i("getToken:handler=" + YueJianAppStrUtils.objDesc(handler));
        this.handler = handler;
        retryTimes = MAX_RETRY_TIMES;
        connect();
    }
}
