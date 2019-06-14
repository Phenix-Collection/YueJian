package com.mingquan.yuejian.huawei.common;

/**
 * 回调线程
 */
public class YueJianAppCallbackResultRunnable<R> implements Runnable {

    private YueJianAppICallbackResult<R> handlerInner;
    private int rtnCodeInner;
    private R resultInner;

    public YueJianAppCallbackResultRunnable(YueJianAppICallbackResult<R> handler, int rtnCode, R payInfo) {
        handlerInner = handler;
        rtnCodeInner = rtnCode;
        resultInner = payInfo;
    }

    @Override
    public void run() {
        if (handlerInner != null) {
            handlerInner.onResult(rtnCodeInner, resultInner);
        }
    }
}