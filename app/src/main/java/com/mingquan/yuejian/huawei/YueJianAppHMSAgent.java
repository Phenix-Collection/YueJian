package com.mingquan.yuejian.huawei;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.mingquan.yuejian.huawei.common.YueJianAppActivityMgr;
import com.mingquan.yuejian.huawei.common.YueJianAppApiClientMgr;
import com.mingquan.yuejian.huawei.common.YueJianAppHMSAgentLog;
import com.mingquan.yuejian.huawei.common.YueJianAppIClientConnectCallback;
import com.mingquan.yuejian.huawei.common.YueJianAppINoProguard;
import com.mingquan.yuejian.huawei.common.handler.YueJianAppConnectHandler;
import com.mingquan.yuejian.huawei.push.YueJianAppDeleteTokenApi;
import com.mingquan.yuejian.huawei.push.YueJianAppEnableReceiveNormalMsgApi;
import com.mingquan.yuejian.huawei.push.YueJianAppEnableReceiveNotifyMsgApi;
import com.mingquan.yuejian.huawei.push.YueJianAppGetPushStateApi;
import com.mingquan.yuejian.huawei.push.YueJianAppGetTokenApi;
import com.mingquan.yuejian.huawei.push.YueJianAppQueryAgreementApi;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppDeleteTokenHandler;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppEnableReceiveNormalMsgHandler;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppEnableReceiveNotifyMsgHandler;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppGetPushStateHandler;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppGetTokenHandler;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppQueryAgreementHandler;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;

/**
 * YueJianAppHMSAgent 封装入口类。 提供了HMS SDK 功能的封装，使开发者更聚焦业务的处理。
 * YueJianAppHMSAgent encapsulates the entry class. Provides a encapsulation of the HMS SDK functionality that enables developers to focus more on business processing.
 */
public final class YueJianAppHMSAgent implements YueJianAppINoProguard {

    /**
     * 基础版本 | Base version
     */
    private static final String VER_020503001 = "020503001";

    /**
     * 2.6.0 版本1                                            | 2.6.0 version 1
     * 对外：接口不变                                         | External: interface unchanged
     * 对内：HMSSDK connect 接口增加activity参数              | Internal: HMSSDK connect interface to increase activity parameters
     *      HMSSDK sign 接口增加activity参数                      | HMSSDK sign interface to increase activity parameters
     * 自身优化：                                             | Self optimization:
     *      1、增加了升级时被其他界面覆盖的处理                  | Increased handling of other interface coverage issues when upgrading
     *      2、game模块savePlayerInfo接口，去掉activity的判断    | Game Module Saveplayerinfo method to remove activity judgments
     *      3、解决错误回调成功，增加重试次数3次                 | Resolve error callback succeeded, increase retry count 3 times
	 *      4、提供了多种HMSAgent初始化方法                      | Provides a variety of hmsagent initialization methods
     *      5、初始化时增加了版本号校验                          | Increased version number checksum during initialization
     */
    private static final String VER_020600001 = "020600001";

    /**
     * 2.6.0.200                                         | 2.6.0.200
     * 自身优化：                                        | Self optimization:
     *      1、增加shell脚本用来抽取代码和编译成jar            | Add shell script to extract code and compile into jar
     *      2、示例中manifest里面升级配置错误修复              | Example manifest upgrade configuration error Repair
     *      3、抽取代码中去掉manifest文件，只留纯代码          | Remove manifest files in the extraction code, leaving only pure code
     */
    private static final String VER_020600200 = "020600200";

    /**
     * 2.6.0.302                                         | 2.6.0.302
     * 修改manifest，删除hms版本号配置；增加直接传入请求和私钥的签名方法封装
     */
    private static final String VER_020600302 = "020600302";

    /**
     * 当前版本号 | Current version number
     */
    public static final String CURVER = VER_020600302;



    public static final class AgentResultCode {

        /**
         * YueJianAppHMSAgent 成功 | success
         */
        public static final int HMSAGENT_SUCCESS = 0;

        /**
         * YueJianAppHMSAgent 没有初始化 | Hmsagent not initialized
         */
        public static final int HMSAGENT_NO_INIT = -1000;

        /**
         * 请求需要activity，但当前没有可用的activity | Request requires activity, but no active activity is currently available
         */
        public static final int NO_ACTIVITY_FOR_USE = -1001;

        /**
         * 结果为空 | Result is empty
         */
        public static final int RESULT_IS_NULL = -1002;

        /**
         * 状态为空 | Status is empty
         */
        public static final int STATUS_IS_NULL = -1003;

        /**
         * 拉起activity异常，需要检查activity有没有在manifest中配置 | Pull up an activity exception and need to check if the activity is configured in manifest
         */
        public static final int START_ACTIVITY_ERROR = -1004;

        /**
         * onActivityResult 回调结果错误 | Onactivityresult Callback Result Error
         */
        public static final int ON_ACTIVITY_RESULT_ERROR = -1005;

        /**
         * 重复请求 | Duplicate Request
         */
        public static final int REQUEST_REPEATED = -1006;

        /**
         * 连接client 超时 | Connect Client Timeout
         */
        public static final int APICLIENT_TIMEOUT = -1007;

        /**
         * 调用接口异常 | Calling an interface exception
         */
        public static final int CALL_EXCEPTION = -1008;

        /**
         * 接口参数为空 | Interface parameter is empty
         */
        public static final int EMPTY_PARAM = -1009;
    }

    private YueJianAppHMSAgent(){}

    private static boolean checkSDKVersion(Context context){
        long sdkMainVerL = HuaweiApiAvailability.HMS_SDK_VERSION_CODE/1000;
        long agentMainVerL = Long.parseLong(CURVER)/1000;
        if (sdkMainVerL != agentMainVerL) {
            String errMsg = "error: YueJianAppHMSAgent major version code ("+agentMainVerL+") does not match HMSSDK major version code ("+sdkMainVerL+")";
            YueJianAppHMSAgentLog.e(errMsg);
            Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * 初始化方法，传入第一个界面的activity   | Initialization method, passing in the first interface activity
     * @param activity 当前界面             | Current interface
     * @return true：成功 false：失败        | True: Success false: Failed
     */
    public static boolean init(Activity activity) {
        return init(null, activity);
    }

    /**
     * 初始化方法，建议在Application onCreate里面调用    | Initialization method, it is recommended to call in creator OnCreate
     * @param app 应用程序                              | Application
     * @return true：成功 false：失败                   | True: Success false: Failed
     */
    public static boolean init(Application app) {
        return init(app, null);
    }

    /**
     * 初始化方法，建议在Application onCreate里面调用 | Initialization method, it is recommended to call in creator OnCreate
     * @param app 应用程序 | Application
     * @param activity 当前界面 | Current activity
     * @return true：成功 false：失败 | True: Success false: Failed
     */
    public static boolean init(Application app, Activity activity) {

        Application appTmp = app;
        Activity activityTmp = activity;

        // 两个参数都为null，直接抛异常 | Two parameters are null, throwing exceptions directly
        if (appTmp == null && activityTmp == null) {
            YueJianAppHMSAgentLog.e("the param of method YueJianAppHMSAgent.init can not be null !!!");
            return false;
        }

        // 如果application实例为null，则从activity里面取 | If the creator instance is null, it is taken from the activity
        if (appTmp == null) {
            appTmp = activityTmp.getApplication();
        }

        // 如果application实例仍然为null，抛异常 | Throws an exception if the creator instance is still null
        if (appTmp == null) {
            YueJianAppHMSAgentLog.e("the param of method YueJianAppHMSAgent.init app can not be null !!!");
            return false;
        }

        // activity 已经失效，则赋值null | Assignment NULL if activity has been invalidated
        if (activityTmp != null && activityTmp.isFinishing()) {
            activityTmp = null;
        }

        // 检查HMSAgent 和 HMSSDK 版本匹配关系 | Check hmsagent and HMSSDK version matching relationships
        if (!checkSDKVersion(appTmp)) {
            return false;
        }

        YueJianAppHMSAgentLog.i("init YueJianAppHMSAgent " + CURVER + " with hmssdkver " + HuaweiApiAvailability.HMS_SDK_VERSION_CODE);

        // 初始化activity管理类 | Initializing Activity Management Classes
        YueJianAppActivityMgr.INST.init(appTmp, activityTmp);

        // 初始化HuaweiApiClient管理类 | Initialize Huaweiapiclient Management class
        YueJianAppApiClientMgr.INST.init(appTmp);

        return true;
    }

    /**
     * 释放资源，这里一般不需要调用 | Frees resources, which are generally not required to call
     */
    public static void destroy() {
        YueJianAppHMSAgentLog.i("destroy YueJianAppHMSAgent");
        YueJianAppActivityMgr.INST.release();
        YueJianAppApiClientMgr.INST.release();
    }

    /**
     * 连接HMS SDK， 可能拉起界面(包括升级引导等)，建议在第一个界面进行连接。 | Connecting to the HMS SDK may pull up the activity (including upgrade guard, etc.), and it is recommended that you connect in the first activity.
     * 此方法可以重复调用，没必要为了只调用一次做复杂处理 | This method can be called repeatedly, and there is no need to do complex processing for only one call at a time
     * 方法为异步调用，调用结果在主线程回调 | Method is called asynchronously, and the result is invoked in the main thread callback
     * @param activity 当前界面的activity， 不能传空 | Activity of the current activity, cannot be empty
     * @param callback 连接结果回调 | Connection Result Callback
     */
    public static void connect(Activity activity, final YueJianAppConnectHandler callback) {
        YueJianAppHMSAgentLog.i("start connect");
        YueJianAppApiClientMgr.INST.connect(new YueJianAppIClientConnectCallback() {
            @Override
            public void onConnect(final int rst, HuaweiApiClient client) {
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onConnect(rst);
                        }
                    });
                }
            }
        }, true);
    }

    /**
     * 检查本应用的升级 | Check for upgrades to this application
     * @param activity 上下文 | context
     */
    public static void checkUpdate (final Activity activity) {
        YueJianAppHMSAgentLog.i("start checkUpdate");
        YueJianAppApiClientMgr.INST.connect(new YueJianAppIClientConnectCallback() {
            @Override
            public void onConnect(int rst, HuaweiApiClient client) {
                Activity activityCur = YueJianAppActivityMgr.INST.getLastActivity();

                if (activityCur != null && client != null) {
                    client.checkUpdate(activityCur);
                } else if (activity != null && client != null){
                    client.checkUpdate(activity);
                } else {
                    // 跟SE确认：activity 为 null ， 不处理 | Activity is null and does not need to be processed
                    YueJianAppHMSAgentLog.e("no activity to checkUpdate");
                }
            }
        }, true);
    }





    /**
     * push接口封装 | Push interface Encapsulation
     */
    public static final class Push {
        /**
         * 获取pushtoken接口 | Get Pushtoken method
         * pushtoken通过广播下发，要监听的广播，请参见HMS-SDK开发准备中PushReceiver的注册 | Pushtoken Broadcast issued, to listen to the broadcast, see HMS-SDK Development Preparation Pushreceiver Registration
         * @param handler pushtoken接口调用回调（结果会在主线程回调） | getToken method Call callback (result will be callback in main thread)
         */
        public static void getToken(YueJianAppGetTokenHandler handler){
            new YueJianAppGetTokenApi().getToken(handler);
        }

        /**
         * 删除指定的pushtoken | Deletes the specified Pushtoken
         * 该接口只在EMUI5.1以及更高版本的华为手机上调用该接口后才不会收到PUSH消息。 | The method will not receive a push message until it is invoked on EMUI5.1 and later Huawei handsets.
         * @param token 要删除的token | Token to delete
         * @param handler 方法调用结果回调（结果会在主线程回调） | Method call result Callback (result will be callback on main thread)
         */
        public static void deleteToken(String token, YueJianAppDeleteTokenHandler handler){
            new YueJianAppDeleteTokenApi().deleteToken(token, handler);
        }

        /**
         * 获取push状态，push状态的回调通过广播发送。 | Gets the push state, and the push state callback is sent by broadcast.
         * 要监听的广播，请参见HMS-SDK开发准备中PushReceiver的注册 | To listen for broadcasts, see Pushreceiver Registration in HMS-SDK development preparation
         * @param handler 方法调用结果回调（结果会在主线程回调） | Method call result Callback (result will be callback on main thread)
         */
        public static void getPushState(YueJianAppGetPushStateHandler handler){
            new YueJianAppGetPushStateApi().getPushState(handler);
        }

        /**
         * 打开/关闭通知栏消息 | Turn on/off notification bar messages
         * @param enable 打开/关闭 | Turn ON/off
         * @param handler 方法调用结果回调（结果会在主线程回调） | Method call result Callback (result will be callback on main thread)
         */
        public static void enableReceiveNotifyMsg(boolean enable, YueJianAppEnableReceiveNotifyMsgHandler handler){
            new YueJianAppEnableReceiveNotifyMsgApi().enableReceiveNotifyMsg(enable, handler);
        }

        /**
         * 打开/关闭透传消息 | Turn on/off the pass message
         * @param enable 打开/关闭 | Turn ON/off
         * @param handler 方法调用结果回调（结果会在主线程回调） | Method call result Callback (result will be callback on main thread)
         */
        public static void enableReceiveNormalMsg(boolean enable, YueJianAppEnableReceiveNormalMsgHandler handler){
            new YueJianAppEnableReceiveNormalMsgApi().enableReceiveNormalMsg(enable, handler);
        }

        /**
         * 请求push协议展示 | Request Push Protocol Display
         * @param handler 方法调用结果回调（结果会在主线程回调）| Method call result Callback (result will be callback on main thread)
         */
        public static void queryAgreement(YueJianAppQueryAgreementHandler handler){
            new YueJianAppQueryAgreementApi().queryAgreement(handler);
        }
    }
}
