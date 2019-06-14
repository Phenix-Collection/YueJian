package com.mingquan.yuejian.huawei.common;

import java.io.Closeable;
import java.io.IOException;

/**
 * 工具类
 */
public final class YueJianAppIOUtils {
    public static void close(Closeable object) {
        if (object != null) {
            try {
                object.close();
            } catch (IOException e) {
                YueJianAppHMSAgentLog.d("close fail");
            }
        }
    }
}
