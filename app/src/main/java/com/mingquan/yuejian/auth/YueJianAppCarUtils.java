package com.mingquan.yuejian.auth;

import com.mingquan.yuejian.utils.YueJianAppTLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/10/19
 */
public class YueJianAppCarUtils {
    private static final String TAG = "YueJianAppCarUtils";

    public static String carToStr(InputStream inputStream) throws IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        // 数组长度
        byte[] buffer = new byte[1024];
        // 初始长度
        int len = 0;
        // 循环
        while ((len = inputStream.read(buffer)) != -1) {
            arrayOutputStream.write(buffer, 0, len);
//            return arrayOutputStream.toString();
        }
        YueJianAppTLog.info(arrayOutputStream.toString());
        return arrayOutputStream.toString();
    }
}
