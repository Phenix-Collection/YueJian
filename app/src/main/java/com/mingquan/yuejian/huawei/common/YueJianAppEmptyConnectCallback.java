package com.mingquan.yuejian.huawei.common;

import com.huawei.hms.api.HuaweiApiClient;

/**
 * 连接client空回调
 */
public class YueJianAppEmptyConnectCallback implements YueJianAppIClientConnectCallback {

    private String msgPre;

    public YueJianAppEmptyConnectCallback(String msgPre){
        this.msgPre = msgPre;
    }

    /**
     * HuaweiApiClient 连接结果回调
     *
     * @param rst    结果码
     * @param client HuaweiApiClient 实例
     */
    @Override
    public void onConnect(int rst, HuaweiApiClient client) {
        YueJianAppHMSAgentLog.d(msgPre + rst);
    }
}