package com.mingquan.yuejian.utils;

import android.util.Log;

import com.mingquan.yuejian.YueJianAppAppConfig;

/**
 * Created by Administrator on 2017/11/8.
 * 日志输出类
 */

public class YueJianAppTLog {
    public static final String LOG_TAG = "yue_jian_log";
    public static final boolean DEBUG = YueJianAppAppConfig.LOG_DEBUG;

    public static void debug(String format, Object...params) {
        if (!DEBUG) { return; }
        Log.d(LOG_TAG, String.format(format, params));
    }

    public static void info(String format, Object...params) {
        if (!DEBUG) { return; }
        Log.i(LOG_TAG, String.format(format, params));
    }

    public static void warn(String format, Object...params) {
        if (!DEBUG) { return; }
        Log.w(LOG_TAG, String.format(format, params));
    }

    public static void error(String format, Object...params) {
        if (!DEBUG) { return; }
        Log.e(LOG_TAG, String.format(format, params));
    }
}

