package com.mingquan.yuejian.huawei.common;

/**
 * API 实现类基类，用于处理公共操作
 * 目前实现的是client的连接及回调
 */
public abstract class YueJianAppBaseApiAgent implements YueJianAppIClientConnectCallback {
    protected void connect() {
        YueJianAppHMSAgentLog.d("connect");
        YueJianAppApiClientMgr.INST.connect(this, true);
    }
}
