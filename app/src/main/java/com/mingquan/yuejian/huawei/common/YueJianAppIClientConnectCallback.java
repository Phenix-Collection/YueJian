package com.mingquan.yuejian.huawei.common;

import com.huawei.hms.api.HuaweiApiClient;

/**
 * HuaweiApiClient 连接结果回调
 */
public interface YueJianAppIClientConnectCallback {
    /**
     * HuaweiApiClient 连接结果回调
     * @param rst 结果码
     * @param client HuaweiApiClient 实例
     */
    void onConnect(int rst, HuaweiApiClient client);
}
