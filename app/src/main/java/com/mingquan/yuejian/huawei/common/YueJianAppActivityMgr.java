package com.mingquan.yuejian.huawei.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity Management Class
 * 此类注册了activity的生命周期监听，用来获取最新的activity给后续逻辑处理使用 | This class registers the life cycle monitoring of the activity to obtain the latest activity for subsequent logical processing using
 */
public final class YueJianAppActivityMgr implements Application.ActivityLifecycleCallbacks {

    /**
     * 单实例 | Single Instance
     */
    public static final YueJianAppActivityMgr INST = new YueJianAppActivityMgr();

    private static final Object LOCK_LASTACTIVITIES = new Object();

    /**
     * 应用程序 | application
     */
    private Application application;

    /**
     * 最新的activity列表，如果没有则为空列表 | Latest list of activity, if no, empty list
     */
    private List<Activity> curActivities = new ArrayList<Activity>();

    /**
     * activity onResume Event Monitoring
     */
    private List<YueJianAppIActivityResumeCallback> resumeCallbacks = new ArrayList<YueJianAppIActivityResumeCallback>();

    /**
     * activity onPause Event Monitoring
     */
    private List<YueJianAppIActivityPauseCallback> pauseCallbacks = new ArrayList<YueJianAppIActivityPauseCallback>();

    /**
     * activity onDestroyed Event Monitoring
     */
    private List<YueJianAppIActivityDestroyedCallback> destroyedCallbacks = new ArrayList<YueJianAppIActivityDestroyedCallback>();

    /**
     * 私有构造方法 | Private construction methods
     * 防止外面直接创建实例 | Prevent external instances from being created directly
     */
    private YueJianAppActivityMgr(){}

    /**
     * 初始化方法 | Initialization method
     * @param app 应用程序 | application
     */
    public void init(Application app, Activity initActivity) {
        YueJianAppHMSAgentLog.d("init");

        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(this);
        }

        application = app;
        setCurActivity(initActivity);
        app.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 释放资源，一般不需要调用 | Frees resources, and generally does not need to call
     */
    public void release() {
        YueJianAppHMSAgentLog.d("release");
        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(this);
        }

        clearCurActivities();
        clearActivitResumeCallbacks();
        clearActivitPauseCallbacks();
        application = null;
    }

    /**
     * 注册activity onResume事件回调 | Registering an Activity Onresume event Callback
     * @param callback activity onResume事件回调 | Activity Onresume Event Callback
     */
    public void registerActivitResumeEvent(YueJianAppIActivityResumeCallback callback) {
        YueJianAppHMSAgentLog.d("registerOnResume:" + YueJianAppStrUtils.objDesc(callback));
        resumeCallbacks.add(callback);
    }

    /**
     * 反注册activity onResume事件回调 | unregistration Activity Onresume Event Callback
     * @param callback 已经注册的 activity onResume事件回调 | Registered Activity Onresume Event callback
     */
    public void unRegisterActivitResumeEvent(YueJianAppIActivityResumeCallback callback) {
        YueJianAppHMSAgentLog.d("unRegisterOnResume:" + YueJianAppStrUtils.objDesc(callback));
        resumeCallbacks.remove(callback);
    }

    /**
     * 注册activity onPause 事件回调 | Registering an Activity OnPause event Callback
     * @param callback activity onPause 事件回调 | Activity OnPause Event Callback
     */
    public void registerActivitPauseEvent(YueJianAppIActivityPauseCallback callback) {
        YueJianAppHMSAgentLog.d("registerOnPause:" + YueJianAppStrUtils.objDesc(callback));
        pauseCallbacks.add(callback);
    }

    /**
     * 反注册activity onPause事件回调 | unregistration activity OnPause Event Callback
     * @param callback 已经注册的 activity onPause事件回调 | Registered Activity OnPause Event callback
     */
    public void unRegisterActivitPauseEvent(YueJianAppIActivityPauseCallback callback) {
        YueJianAppHMSAgentLog.d("unRegisterOnPause:" + YueJianAppStrUtils.objDesc(callback));
        pauseCallbacks.remove(callback);
    }

    /**
     * 注册activity onDestroyed 事件回调 | Registering an Activity ondestroyed event Callback
     * @param callback activity onDestroyed 事件回调 | Activity Ondestroyed Event Callback
     */
    public void registerActivitDestroyedEvent(YueJianAppIActivityDestroyedCallback callback) {
        YueJianAppHMSAgentLog.d("registerOnDestroyed:" + YueJianAppStrUtils.objDesc(callback));
        destroyedCallbacks.add(callback);
    }

    /**
     * 反注册activity onDestroyed 事件回调 | unregistration Activity ondestroyed Event Callback
     * @param callback 已经注册的 activity onDestroyed事件回调 | Registered Activity ondestroyed Event callback
     */
    public void unRegisterActivitDestroyedEvent(YueJianAppIActivityDestroyedCallback callback) {
        YueJianAppHMSAgentLog.d("unRegisterOnDestroyed:" + YueJianAppStrUtils.objDesc(callback));
        destroyedCallbacks.remove(callback);
    }

    /**
     * 清空 activity onResume事件回调 | Clear Activity Onresume Event callback
     */
    public void clearActivitResumeCallbacks() {
        YueJianAppHMSAgentLog.d("clearOnResumeCallback");
        resumeCallbacks.clear();
    }

    /**
     * 清空 activity onPause 事件回调 | Clear Activity OnPause Event callback
     */
    public void clearActivitPauseCallbacks() {
        YueJianAppHMSAgentLog.d("clearOnPauseCallback");
        pauseCallbacks.clear();
    }

    /**
     * 获取最新的activity | Get the latest activity
     * @return 最新的activity | Latest activity
     */
    public Activity getLastActivity() {
        return getLastActivityInner();
    }

    /**
     * activity onCreate 监听回调 | Activity OnCreate Listener Callback
     * @param activity 发生onCreate事件的activity | Activity that occurs OnCreate events
     * @param savedInstanceState 缓存状态数据 | Cached state data
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        YueJianAppHMSAgentLog.d("onCreated:" + YueJianAppStrUtils.objDesc(activity));
        setCurActivity(activity);
    }

    /**
     * activity onStart 监听回调 | Activity OnStart Listener Callback
     * @param activity 发生onStart事件的activity | Activity that occurs OnStart events
     */
    @Override
    public void onActivityStarted(Activity activity) {
        YueJianAppHMSAgentLog.d("onStarted:" + YueJianAppStrUtils.objDesc(activity));
        setCurActivity(activity);
    }

    /**
     * activity onResume 监听回调 | Activity Onresume Listener Callback
     * @param activity 发生onResume事件的activity | Activity that occurs Onresume events
     */
    @Override
    public void onActivityResumed(Activity activity) {
        YueJianAppHMSAgentLog.d("onResumed:" + YueJianAppStrUtils.objDesc(activity));
        setCurActivity(activity);

        List<YueJianAppIActivityResumeCallback> tmdCallbacks = new ArrayList<YueJianAppIActivityResumeCallback>(resumeCallbacks);
        for (YueJianAppIActivityResumeCallback callback : tmdCallbacks) {
            callback.onActivityResume(activity);
        }
    }

    /**
     * activity onPause 监听回调 | Activity OnPause Listener Callback
     * @param activity 发生onPause事件的activity | Activity that occurs OnPause events
     */
    @Override
    public void onActivityPaused(Activity activity) {
        YueJianAppHMSAgentLog.d("onPaused:" + YueJianAppStrUtils.objDesc(activity));
        List<YueJianAppIActivityPauseCallback> tmdCallbacks = new ArrayList<YueJianAppIActivityPauseCallback>(pauseCallbacks);
        for (YueJianAppIActivityPauseCallback callback : tmdCallbacks) {
            callback.onActivityPause(activity);
        }
    }

    /**
     * activity onStop 监听回调 | Activity OnStop Listener Callback
     * @param activity 发生onStop事件的activity | Activity that occurs OnStop events
     */
    @Override
    public void onActivityStopped(Activity activity) {
        YueJianAppHMSAgentLog.d("onStopped:" + YueJianAppStrUtils.objDesc(activity));
    }

    /**
     * activity onSaveInstanceState 监听回调 | Activity Onsaveinstancestate Listener Callback
     * @param activity 发生 onSaveInstanceState 事件的activity | Activity that occurs onsaveinstancestate events
     */
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    /**
     * activity onDestroyed 监听回调 | Activity Ondestroyed Listener Callback
     * @param activity 发生 onDestroyed 事件的activity | Activity that occurs ondestroyed events
     */
    @Override
    public void onActivityDestroyed(Activity activity) {
        YueJianAppHMSAgentLog.d("onDestroyed:" + YueJianAppStrUtils.objDesc(activity));
        removeActivity(activity);

        // activity onDestroyed 事件回调 | Activity Ondestroyed Event Callback
        List<YueJianAppIActivityDestroyedCallback> tmdCallbacks = new ArrayList<YueJianAppIActivityDestroyedCallback>(destroyedCallbacks);
        for (YueJianAppIActivityDestroyedCallback callback : tmdCallbacks) {
            callback.onActivityDestroyed(activity, getLastActivityInner());
        }
    }

    /**
     * 移除当前activity | Remove Current Activity
     * @param curActivity 要移除的activity | Activity to remove
     */
    private void removeActivity(Activity curActivity) {
        synchronized (LOCK_LASTACTIVITIES) {
            curActivities.remove(curActivity);
        }
    }

    /**
     * 设置最新的activity | Set up the latest activity
     * @param curActivity 最新的activity | Latest activity
     */
    private void setCurActivity(Activity curActivity) {
        synchronized (LOCK_LASTACTIVITIES) {
            int idxCurActivity = curActivities.indexOf(curActivity);
            if (idxCurActivity == -1) {
                curActivities.add(curActivity);
            } else if (idxCurActivity < curActivities.size()-1){
                curActivities.remove(curActivity);
                curActivities.add(curActivity);
            }
        }
    }

    /**
     * 获取最新的activity，如果没有则返回null | Gets the latest activity and returns null if not
     * @return 最新的activity | Latest activity
     */
    private Activity getLastActivityInner(){
        synchronized (LOCK_LASTACTIVITIES) {
            if (curActivities.size() > 0) {
                return curActivities.get(curActivities.size()-1);
            } else {
                return null;
            }
        }
    }

    /**
     * 清理activities | Clean activities
     */
    private void clearCurActivities(){
        synchronized (LOCK_LASTACTIVITIES) {
            curActivities.clear();
        }
    }
}