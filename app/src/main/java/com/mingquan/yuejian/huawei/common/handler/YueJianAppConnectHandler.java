package com.mingquan.yuejian.huawei.common.handler;

/**
 * HuaweiApiClient 连接结果回调
 */
public interface YueJianAppConnectHandler {
    /**
     * HuaweiApiClient 连接结果回调
     * @param rst 结果码
     */
    void onConnect(int rst);
}
