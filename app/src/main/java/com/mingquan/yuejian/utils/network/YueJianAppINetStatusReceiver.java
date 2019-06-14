package com.mingquan.yuejian.utils.network;

/**
 * 网络状态相关 来自github
 */

public interface YueJianAppINetStatusReceiver {
    void netStatusChanged(YueJianAppNetType netType);
}
