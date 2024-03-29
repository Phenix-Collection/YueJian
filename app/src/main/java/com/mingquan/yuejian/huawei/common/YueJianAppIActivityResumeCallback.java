package com.mingquan.yuejian.huawei.common;

import android.app.Activity;

/**
 * Activity onResume 事件回调接口
 */
public interface YueJianAppIActivityResumeCallback {

    /**
     * Activity onResume回调
     * @param activity 发生 onResume 事件的activity
     */
    void onActivityResume(Activity activity);
}
