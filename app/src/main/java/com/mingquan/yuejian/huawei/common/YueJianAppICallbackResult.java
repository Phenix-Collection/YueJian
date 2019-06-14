package com.mingquan.yuejian.huawei.common;

/**
 * 回调接口
 */
public interface YueJianAppICallbackResult<R> {
    /**
     * 回调接口
     * @param rst 结果码
     * @param result 结果信息
     */
    void onResult(int rst, R result);
}
