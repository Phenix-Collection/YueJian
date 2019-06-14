package com.mingquan.yuejian;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * Sophix单独入口类，此处专门用于初始化Sophix，不应包含任何业务逻辑。
 * AndroidManifest中设置application为此类，而SophixEntry中设为原先Application类。
 * 注意原先Application里不需要再重复初始化Sophix，并且需要避免混淆原先Application类。
 */

public class YueJianAppMySophixApplication extends SophixApplication {
    private final String TAG = "YueJianAppMySophixApplication";

    public static final String appId = "25408600";
    public static final String appSecret = "2876dad6c6099926f3ca71f0e017c98e";
    public static final String rsaSecret = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDXqex7roqd1eA3BgMRn1047A9aiG/heRGlDV4Ay3CSN0fOnC5CWwAeGP0SIgy+OLtJ5ge67MBHBAeDqviJ0LfLEM8ZMoEUkCZOhfa5LidZTyHVZc7fBOhdBNZCxIJyN7JDcxHOLGablTxbnwJ0Ma/Ddoh10L461B78Exf1gCkwRtsmzcQr6aYeAYbgk6fjOdADDSJNLr86J4XIMYv6DNpQMoHTtvRXGAkqOl3v7YyCTUx7cBWjPju4X820xqfwPsOLnUVot+v4iWzcVPFsypKzQV5hTgcEswG+LEOhlVSOKEMef7lIHcs/7IkDV+xiScltPfbKMcR8wElbGKUL0Bd1AgMBAAECggEAIS4ojyLcesokE/Md18FAE3jmJ9Sj+fmpiE5VLyQdrrFIn/YRe+4KR5cqoHaSpVWPIyB8AftY3dQ138EXOgTdsiVk54iIqYRAEIa13enG7gupte5S9KcJpP4QhPc85pvBFnWGwMS+CKPeRG5jb0MRuq3q8s1p5x0pozjAz+mm7KFB2hcFoVVEXDCGdq52G2j/6OEmdmM7QHwPsu5kbtgkftIYCdqHuYXrlaPrmPxgWKgdN6jHbR8q87W2KMYwm78HC5nrQiApK5IjzhDBFiiK+NXApQG+eQOHISi2YKgTKESKyH8mLgaa9mHHd8ZgFrmAY6H31e8OG01okOv5aJk2xQKBgQDthjCb4uTCA+5V3WVvStwGeoMXSyoYeXL1zG/XT9yVLvFKr2CJZ6yj3sCywpFGT6WgY1XVmWlayAxEtPGR8MsrP2NhcYqJplurCHl3XIYmAWdRMkLOhQHNueFE5cNhxNOXMTbHzlU9qgMXKiBn0RuA2OgT2JODVvq7q1cODxqJawKBgQDocG2iwQlrCMMsXGZ9M0N50GoruJMlJdKk4QT6YLKIpBIYs5hDRp2C6O08DP1i1gxyCN57TKqaiZWgclrALwpPl6IXdC7zYHCL+MJWa1KWp1J8a5Zd+sEEXnrKF6RAnwo0HSu6emXxAqKjSvB7bKfyLST5kXJHfFyFejjPNbe6nwKBgB4+nQfsf0Z5K6sBUv414QhuJy0bJBQTuuCzlHYtNCBG5vCknj1A10nSfmUEw2zZjXR70Z0uOK0XlgBVrgDwUV6DZsssowBeD4QawyTAwlAqk5ZSORGE/DLO/XFBdHyJjIvO93O/wTjl4hjcA15U0RS6CeslA7uQosKs0Zv3rmBbAoGAQSVBlRwFR7ps+UHsd6qmfr2rBBX3J7IyF4P5oTL41tMlP0cWpcRk4Qvnca0yV0Emu89Ai84xAp8NEKr8RGDqwuP8yhYGErA02wYdcZsiypaax8RfoHdnIZJZV8rIflQUyAZk+x7S83mJm7qlK/KIH5gHhOr+3Io+ZCqdQXYMyIECgYB2p+saLHZIa6ylAM7R+DfpqC71ZKNnq3dJpNlp9YJeun9sAdPqFuNT+Iaso091D+oZojC9eMs0Dq4yGJUCVJbbYIoVBI8lmsx0UxKFITPqxuxyY6flJ+16oIk1P0MNlERanoICSTPrMSzsHa23iNDgy8XPTUaqxoRLOV9EhzCguw==";

    @Keep
    @SophixEntry(YueJianAppAppContext.class)
    static class RealApplicationStub {}

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         如果需要使用MultiDex，需要在此处调用。
        MultiDex.install(this);
        initSophix();
        Log.i("Sophix", "这里是测试的新SophixApplication");
    }

    private void initSophix() {
        final SophixManager instance = SophixManager.getInstance();
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                .setSecretMetaData(appId, appSecret, rsaSecret)
                .setEnableDebug(true)
                .setEnableFullLog()
//                .setUsingStableInit()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.d(TAG, "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            new Exception().printStackTrace();
                            Log.e(TAG, "sophix preload patch success. restart app to make effect.");
                        }

                        String msg = new StringBuilder("").append("Mode:").append(mode)
                                .append(" Code:").append(code)
                                .append(" Info:").append(info)
                                .append(" HandlePatchVersion:").append(handlePatchVersion).toString();
                        if (msgDisplayListener != null) {
                            msgDisplayListener.handle(mode, code, info);
                        } else {
                            cacheMsg.append("\n").append(msg);
                        }
                    }
                }).initialize();
    }

    public interface MsgDisplayListener {
        void handle(int mode, int code, String info);
    }

    public static MsgDisplayListener msgDisplayListener = null;
    public static StringBuilder cacheMsg = new StringBuilder();
}