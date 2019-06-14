package com.mingquan.yuejian.huawei.common;

/**
 * 回调线程
 */
public class YueJianAppCallbackCodeRunnable implements Runnable {

    private YueJianAppICallbackCode handlerInner;
    private int rtnCodeInner;

    public YueJianAppCallbackCodeRunnable(YueJianAppICallbackCode handler, int rtnCode) {
        handlerInner = handler;
        rtnCodeInner = rtnCode;
    }

    @Override
    public void run() {
        if (handlerInner != null) {
            handlerInner.onResult(rtnCodeInner);
        }
    }
}