package com.mingquan.yuejian;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.devspark.appmsg.AppMsg;
import com.faceunity.authpack;
import com.faceunity.wrapper.faceunity;
import com.mingquan.yuejian.agora.YueJianAppWorkerThread;
import com.mingquan.yuejian.api.remote.YueJianAppPhoneLiveApi;
import com.mingquan.yuejian.base.YueJianAppBaseApplication;
import com.mingquan.yuejian.bean.YueJianAppUserBean;
import com.mingquan.yuejian.cache.YueJianAppDataCleanManager;
import com.mingquan.yuejian.huawei.YueJianAppHMSAgent;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.YueJianAppChatProtoBuilder;
import com.mingquan.yuejian.proto.YueJianAppChatProtoHandler;
import com.mingquan.yuejian.proto.YueJianAppIChatProtoHandler;
import com.mingquan.yuejian.proto.model.YueJianAppACChatModel;
import com.mingquan.yuejian.proto.model.YueJianAppACGiftModel;
import com.mingquan.yuejian.proto.model.YueJianAppACServerConfigModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserHomepageInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPrivateInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserTagMetaDataModel;
import com.mingquan.yuejian.ui.other.YueJianAppChatServer;
import com.mingquan.yuejian.ui.other.YueJianAppPhoneLivePrivateChat;
import com.mingquan.yuejian.utils.YueJianAppMethodsCompat;
import com.mingquan.yuejian.utils.YueJianAppRom;
import com.mingquan.yuejian.utils.YueJianAppSocketHandlerUtils;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUtils;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.util.NetUtils;
import com.mob.MobSDK;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.utils.KJLoger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import cn.jpush.android.api.JPushInterface;
import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.Call;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 *
 * @version 1.0
 */
public class YueJianAppAppContext extends YueJianAppBaseApplication {
    public static final String TAG = YueJianAppAppContext.class.getSimpleName();

    public static HashMap<Integer, String> mTabIconNormalMap = new HashMap<>(5);
    public static HashMap<Integer, String> mTabIconSelectedMap = new HashMap<>(5);
    public static String mBackgroundUrl = "";
    private static YueJianAppAppContext instance;
    private int mDefaultMainColor;
    private int mMainColor;
    private int mGrayColor;
    private String loginUid;
    private boolean login;
    private String token;
    private String appChannel;
    public static String address = "上海市";
    public static String province;
    public static String country = "China";
    public String mWechatShareUrl; // 微信分享地址
    public String mChatServerUrl; // socket地址
    public String mAgreementUrl; // 服务条款地址
    public int mChargingChatFee; // 私信消息单笔收费
    public String mBroadcastPriceJson; // 连麦价格范围
    public int mCurrBuildNo; // 线上当前版本号
    public int mMinBuildNo; // 线上支持的最小版本
    public String mDownloadUrl; // 最新版本的更新地址
    public String deviceId; // 设备序列号

    public String mBroadcastShareUrl; // 分享主播地址
    public String mAgentPromoteImageUrl; // 代理推广图片地址
    public String mHuaweiToken; // 华为token
    private Socket mSocket;

    public ArrayList<YueJianAppACUserTagMetaDataModel> userTagMetaData = new ArrayList<>(); // 用户评价标签
    public ArrayList<YueJianAppACUserTagMetaDataModel> myTagMetaData = new ArrayList<>(); // 自评标签
    public String chatTime = "";
    private YueJianAppACUserPrivateInfoModel privateInfoModel;

    public float mFilterLevel = 1.0f; // 滤镜0~1 默认为1
    public float mFaceBeautyALLBlurLevel = 1.0f; // 精准美肤0关闭 1开启，默认为1
    public int mFaceShape = 3;// 脸型 0：女神 1：网红 2：自然 3：默认 4：自定义（新版美型） SDK默认为 3
    public float mFaceShapeLevel = 0.5f;// 美型程度 范围0~1 SDK默认为1
    public int mFaceBeautyItem = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appChannel = AnalyticsConfig.getChannel(this);
//    FileDownloader.init(getApplicationContext());
        init();
        initLogin();
        mDefaultMainColor = getResources().getColor(R.color.main_pure);
        mGrayColor = getResources().getColor(R.color.gray);
        mMainColor = getResources().getColor(R.color.home_pink);

        YueJianAppUtils.init(this);
        setmMainColor(mMainColor);
        YueJianAppAppContext.mTabIconNormalMap.put(0, getProperty("icon_home"));
        YueJianAppAppContext.mTabIconSelectedMap.put(0, getProperty("icon_home_selected"));
        YueJianAppAppContext.mTabIconNormalMap.put(1, getProperty("icon_hot"));
        YueJianAppAppContext.mTabIconSelectedMap.put(1, getProperty("icon_hot_selected"));
        YueJianAppAppContext.mTabIconNormalMap.put(2, getProperty("icon_center"));
        YueJianAppAppContext.mTabIconSelectedMap.put(2, getProperty("icon_center_selected"));
        YueJianAppAppContext.mTabIconNormalMap.put(3, getProperty("icon_rank"));
        YueJianAppAppContext.mTabIconSelectedMap.put(3, getProperty("icon_rank_selected"));
        YueJianAppAppContext.mTabIconNormalMap.put(4, getProperty("icon_account"));
        YueJianAppAppContext.mTabIconSelectedMap.put(4, getProperty("icon_account_selected"));
        YueJianAppAppContext.mBackgroundUrl = getProperty("icon_background_url");

        requestData();

        //开发者注册的异常捕获
//    CrashHandler crashHandler = CrashHandler.getInstance();
//    crashHandler.init(getApplicationContext());
        CrashReport.initCrashReport(getApplicationContext(), YueJianAppAppConfig.BUGLY_APP_ID, YueJianAppAppConfig.DEBUG);

        initFuRenderer();
    }

    private void requestData() {
        // 用户标签
        YueJianAppApiProtoHelper.sendACGetUserTagMetaDataReq(
                null,
                getPackageName(),
                new YueJianAppApiProtoHelper.ACGetUserTagMetaDataReqCallback() {

                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACUserTagMetaDataModel> tags) {
                        userTagMetaData = tags;
                        for (YueJianAppACUserTagMetaDataModel model : tags) {
                            userTagMap.put(model.getTagId(), model);
                        }
                    }
                });

        // 自己的标签
        YueJianAppApiProtoHelper.sendACGetSelfTagMetaDataReq(
                null,
                getPackageName(),
                new YueJianAppApiProtoHelper.ACGetSelfTagMetaDataReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACUserTagMetaDataModel> tags) {
                        myTagMetaData = tags;
                        for (YueJianAppACUserTagMetaDataModel model : tags) {
                            myTagMap.put(model.getTagId(), model);
                        }
                    }
                });
        // 获取配置信息
        getConfig();
    }

    /**
     * 加载faceunity底层数据包
     */
    public void initFuRenderer() {
        try {
            //获取faceunity SDK版本信息
            YueJianAppTLog.info("fu sdk version %s", faceunity.fuGetVersion());
            int len;
            /**
             * fuSetup faceunity初始化
             * 其中 v3.bundle：人脸识别数据文件，缺少该文件会导致系统初始化失败；
             *      authpack：用于鉴权证书内存数组。若没有,请咨询support@faceunity.com
             * 首先调用完成后再调用其他FU API
             */
            InputStream v3 = getAssets().open("v3.bundle");
            byte[] v3Data = new byte[v3.available()];
            len = v3.read(v3Data);
            YueJianAppTLog.info("v3 len:%s", len);
            v3.close();
            faceunity.fuSetup(v3Data, null, authpack.A());

            /**
             * 加载优化表情跟踪功能所需要加载的动画数据文件anim_model.bundle；
             * 启用该功能可以使表情系数及avatar驱动表情更加自然，减少异常表情、模型缺陷的出现。该功能对性能的影响较小。
             * 启用该功能时，通过 fuLoadAnimModel 加载动画模型数据，加载成功即可启动。该功能会影响通过fuGetFaceInfo获取的expression表情系数，以及通过表情驱动的avatar模型。
             * 适用于使用Animoji和avatar功能的用户，如果不是，可不加载
             */
            InputStream animModel = getAssets().open("anim_model.bundle");
            byte[] animModelData = new byte[animModel.available()];
            len = animModel.read(animModelData);
            YueJianAppTLog.info("anim_model len:%s", len);
            animModel.close();
            faceunity.fuLoadAnimModel(animModelData);

            InputStream beauty = getAssets().open("face_beautification.bundle");
            byte[] itemData = new byte[beauty.available()];
            len = beauty.read(itemData);
            YueJianAppTLog.info("beautification len:%s", len);
            beauty.close();
            mFaceBeautyItem = faceunity.fuCreateItemFromPackage(itemData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<Integer, YueJianAppACUserTagMetaDataModel> userTagMap = new HashMap<>();
    private Map<Integer, YueJianAppACUserTagMetaDataModel> myTagMap = new HashMap<>();

    public ArrayList<YueJianAppACUserTagMetaDataModel> getUserTagMetaData() {
        return userTagMetaData;
    }

    public ArrayList<YueJianAppACUserTagMetaDataModel> getMyTagMetaData() {
        return myTagMetaData;
    }

    /**
     * 根据tag_id 获取用户评价标签model
     *
     * @return
     */
    public YueJianAppACUserTagMetaDataModel getUserTagMetaByTagId(int tag_id) {
        return userTagMap.get(tag_id);
    }

    /**
     * 根据tag_id 获取个人形象标签model
     *
     * @param tag_id
     * @return
     */
    public YueJianAppACUserTagMetaDataModel getMyUserTagMetaByTagId(int tag_id) {
        return myTagMap.get(tag_id);
    }

    /**
     * 获取渠道名
     *
     * @return
     */
    public String getAppChannel() {
        return appChannel;
    }

    public YueJianAppChatServer mChatServer;

    public void getConfig() {
        YueJianAppApiProtoHelper.sendACGetConfigReq(null, getPackageName(), new YueJianAppApiProtoHelper.ACGetConfigReqCallback() {
            @Override
            public void onError(int errCode, String errMessage) {
                YueJianAppTLog.error(errMessage);
            }

            @Override
            public void onResponse(YueJianAppACServerConfigModel config) {
                YueJianAppTLog.info("server config model : %s", config.toString());
                mChatServerUrl = config.getChatServer();
                mWechatShareUrl = config.getWechatShareUrl();
                mAgreementUrl = config.getAgreementUrl();
                mChargingChatFee = config.getChargingChatFee();
                mBroadcastPriceJson = config.getBroadcastPriceJson();
                mCurrBuildNo = config.getACurrentBuildNo();
                mMinBuildNo = config.getAMinBuildNo();
                mDownloadUrl = config.getADownloadUrl();
//                connChatServer();
            }
        });
    }

    public void connChatServer() {
        if (mChatServer != null) {
            return;
        }
        try {
            mChatServer = new YueJianAppChatServer(new YueJianAppSocketHandlerUtils(), this, 0, mChatServerUrl);
            YueJianAppChatProtoHandler mChatProtoHandler = YueJianAppChatProtoHandler.getInstance();
            mChatProtoHandler.dispatchProto(mChatServer.mSocket, new YueJianAppIChatProtoHandler() {
                @Override
                public void handleCCLoginNtf(int errorCode) {
                    onConnectRes(errorCode);
                    Intent intent = new Intent(YueJianAppAppConfig.ACTION_VIP_CHAT_CONNECT);
                    sendBroadcast(intent);
                }

                @Override
                public void handleCCChatNtf(int errorCode, YueJianAppACChatModel chat) {

                }

                @Override
                public void handleCCGiveHeartNtf(int errorCode, String icon) {

                }

                @Override
                public void handleCCStopBroadcastNtf(int errorCode, String reason) {

                }

                @Override
                public void handleCCSendGiftNtf(int errorCode, YueJianAppACGiftModel gift, YueJianAppACUserPublicInfoModel sender, YueJianAppACUserPublicInfoModel receiver) {
                    YueJianAppTLog.info("handleCCSendGiftNtf gift:%s, sender:%s, receiver:%s", gift, sender, receiver);
                    if (sender.getUid().equals(loginUid)) { // 如果是自己给对方送礼物，就给对方再发一条私信
                        YueJianAppPhoneLivePrivateChat.sendChatGiftMessage(
                                String.format("送给你一个%s", gift.getName()),
                                receiver.getUid(),
                                loginUid,
                                getAcPublicUser().getAvatarUrl(),
                                getAcPublicUser().getName(),
                                gift.getIcon());
                    }
                    Intent intent = new Intent(YueJianAppAppConfig.ACTION_SEND_GIFT);
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("GIFT_MODEL", gift);
                    bundle.putSerializable("SENDER_MODEL", sender);
                    bundle.putSerializable("RECEIVER_MODEL", receiver);
                    intent.putExtra("GIFT_BUNDLE", bundle);
                    sendBroadcast(intent);
                }

                @Override
                public void handleCCUpdateDiamondNtf(int errorCode, int diamond) {
                    YueJianAppTLog.info("handleCCUpdateDiamondNtf diamond:%s", diamond);
                }

                @Override
                public void handleCCUserEnterRoomNtf(int errorCode, YueJianAppACUserPublicInfoModel user, int rankingScore) {

                }

                @Override
                public void handleCCUserLeaveRoomNtf(int errorCode, YueJianAppACUserPublicInfoModel user) {

                }

                @Override
                public void handleCCVipChatStartNtf(int errorCode, String roomId) { // 连麦开始
                    YueJianAppTLog.info("vip chat start room : %s", roomId);
                    Intent intent = new Intent(YueJianAppAppConfig.ACTION_VIP_CHAT_START);
                    intent.putExtra("ROOM_ID", roomId);
//                    sendBroadcast(intent);
                }

                @Override
                public void handleCCVipChatResultNtf(int errorCode, int broadcastSeconds, int costedDiamond, int experience, String consumerUid) {
                    YueJianAppTLog.info("vip chat start broadcastSeconds : %s, costedDiamond: %s, experience:%s",
                            broadcastSeconds,
                            costedDiamond,
                            experience,
                            consumerUid);
                    Intent intent = new Intent(YueJianAppAppConfig.ACTION_VIP_CHAT_RESULT);
                    intent.putExtra("BROADCAST_SECONDS", broadcastSeconds);
                    intent.putExtra("COST_DIAMOND", costedDiamond);
                    intent.putExtra("EXPERIENCE", experience);
                    intent.putExtra("CONSUMER_UID", consumerUid);
//                    sendBroadcast(intent);
                }

                @Override
                public void handleCCVipChatDeclineNtf(int errorCode, String callerUid, String calleeUid) { // 主播拒绝接听
                    YueJianAppTLog.info("vip chat decline callerUid : %s, calleeUid: %s", callerUid, calleeUid);
                    Intent intent = new Intent(YueJianAppAppConfig.ACTION_VIP_CHAT_DECLINE);
//                    sendBroadcast(intent);
                }

                @Override
                public void handleCCVipChatCancelNtf(int errorCode, String callerUid, String calleeUid) { // 连麦发起者取消连麦
                    YueJianAppTLog.info("vip chat cancel callerUid : %s, calleeUid: %s", callerUid, calleeUid);
                    Intent intent = new Intent(YueJianAppAppConfig.ACTION_VIP_CHAT_CANCEL);
//                    sendBroadcast(intent);
                }

                @Override
                public void handleCCPingNtf(int errorCode, String token) {
                    YueJianAppTLog.info("ping token:%s", token);
                    if (chatTime.equals(token)) {
                        Intent intent = new Intent(YueJianAppAppConfig.ACTION_VIP_CHAT_PING);
//                        sendBroadcast(intent);
                    }
                }
            });
            mChatServer.connectSocketServer(
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    Handler mHandler = new Handler();

    // 连接结果
    public void onConnectRes(int errorCode) {
        if (errorCode == 0) {
            YueJianAppTLog.info("socket 连接成功");
            //开启定时发送心跳包
            mChatServer.heartbeat(mHandler);
        } else {
            YueJianAppTLog.info("socket 连接失败 errCode:%s", errorCode);
        }
    }

    /**
     * 检查重连socket
     */
    public void checkSocket() {
        if (mChatServer == null) {
            if (YueJianAppStringUtil.isEmpty(mChatServerUrl)) {
                YueJianAppApiProtoHelper.sendACGetConfigReq(null, getPackageName(), new YueJianAppApiProtoHelper.ACGetConfigReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse(YueJianAppACServerConfigModel config) {
                        YueJianAppTLog.info("server config model : %s", config.toString());
                        mChatServerUrl = config.getChatServer();
                        mWechatShareUrl = config.getWechatShareUrl();
                        mAgreementUrl = config.getAgreementUrl();
                        mChargingChatFee = config.getChargingChatFee();
                        mBroadcastPriceJson = config.getBroadcastPriceJson();
                        mCurrBuildNo = config.getACurrentBuildNo();
                        mMinBuildNo = config.getAMinBuildNo();
                        mDownloadUrl = config.getADownloadUrl();
                        connChatServer();
                    }
                });
            } else {
                connChatServer();
            }
        } else {
            if (!mChatServer.mSocket.connected()) { // socket断开重连
                mChatServer.mSocket.disconnect();
                mChatServer.mSocket.connect();
            } else {
                chatTime = String.valueOf(System.currentTimeMillis());
                YueJianAppChatProtoBuilder.sendCCPingRpt(mChatServer.mSocket, chatTime);
            }
        }
    }

    /**
     * 下载屏蔽字列表
     *
     * @param maskWordUrl
     */
    public void downLoadMaskWords(String maskWordUrl) {
        YueJianAppTLog.info("下载屏蔽字列表");
        if (YueJianAppStringUtil.isEmpty(maskWordUrl))
            return;
        String fileName = "";
        if (maskWordUrl.contains("/"))
            ;
        {
            fileName = maskWordUrl.substring(maskWordUrl.lastIndexOf("/"));
            YueJianAppTLog.info("屏蔽字文件名：" + fileName);
        }
        final String finalFileName = fileName;
        YueJianAppPhoneLiveApi.downloadMaskWord(
                maskWordUrl, new FileCallBack(YueJianAppAppConfig.DEFAULT_SAVE_FILE_PATH, finalFileName) {
                    @Override
                    public void inProgress(float progress, long total) {
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e(TAG, "下载屏蔽字列表失败");
                        YueJianAppAppConst.getInstance().getDirtyWordsFromAsserts();
                    }

                    @Override
                    public void onResponse(File response) {
                        setProperty("mask_file_name", finalFileName);
                        YueJianAppTLog.error("file:" + response.getName());
                        YueJianAppAppConst.getInstance().getDirtyWordsFromFile();
                    }
                });
    }

    /**
     * 初始化操作
     */
    private void init() {
        if (YueJianAppRom.isEmui()) {
            YueJianAppHMSAgent.init(this); // 华为推送
        }
        MobSDK.init(this, YueJianAppAppConfig.MobKey, YueJianAppAppConfig.MobSecret);
        MobclickAgent.setDebugMode(false);
        KJLoger.openDebutLog(false);
        //环信初始化
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
        EMClient.getInstance().init(context(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(false);
        setGlobalListeners();
        //初始化jpush
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

        // talkingData 相关
        TCAgent.LOG_ON = true;
        TCAgent.init(this, YueJianAppAppConfig.TALKING_DATA_ID, appChannel);
        TCAgent.setReportUncaughtExceptions(true);
    }

    /**
     * 返回Socket
     *
     * @param url 根据后台返回的地址
     * @return
     */
    public Socket getSocket(String url) {
        try {
            mSocket = IO.socket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return mSocket;
    }

    protected void setGlobalListeners() {
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            YueJianAppTLog.info("连接聊天服务器成功！");
        }

        @Override
        public void onDisconnected(final int error) {
            if (error == EMError.USER_REMOVED) {
                // 显示帐号已经被移除
                YueJianAppTLog.warn("显示帐号已经被移除");
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                // 显示帐号在其他设备登陆
                YueJianAppTLog.warn("error" + error);
//        YueJianAppTLog.warn("您的账号已在其他设备登录，请重新登录");
                //当账号在其他地方登录的时候，通知下线
//        cleanLoginInfo();//TODO
       /* Intent intent = new Intent();
        intent.setAction(YueJianAppAppConfig.ACTION_LOGOUT);
        sendBroadcast(intent);*/
            } else {
                if (NetUtils.hasNetwork(YueJianAppAppContext.getInstance())) {
                    //连接不到聊天服务器
                    YueJianAppTLog.warn("连接不到聊天服务器");
                } else {
                    //当前网络不可用，请检查网络设置
                    YueJianAppTLog.warn("当前网络不可用，请检查网络设置");
                }
            }
        }
    }

    public int getmMainColor() {
        return mMainColor;
    }

    public void setmMainColor(int mMainColor) {
        this.mMainColor = mMainColor;
    }

    public int getmGrayColor() {
        return mGrayColor;
    }

    public void setmGrayColor(int mGrayColor) {
        this.mGrayColor = mGrayColor;
    }

    private void initLogin() {
        YueJianAppUserBean user = getLoginUser();
        if (null != user && user.getId() > 0) {
            login = true;
            loginUid = String.valueOf(user.getId());
            token = user.getToken();
        } else {
            this.cleanLoginInfo();
        }
    }

    public int getmDefaultMainColor() {
        return mDefaultMainColor;
    }

    public void setmDefaultMainColor(int mDefaultMainColor) {
        this.mDefaultMainColor = mDefaultMainColor;
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static YueJianAppAppContext getInstance() {
        return instance;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        YueJianAppAppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return YueJianAppAppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        YueJianAppAppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = YueJianAppAppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        YueJianAppAppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(YueJianAppAppConfig.CONF_APP_UNIQUEID);
        if (YueJianAppStringUtil.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(YueJianAppAppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    public void saveUserInfo(final YueJianAppACUserPublicInfoModel user, final String token) {
        this.loginUid = user.getUid();
        this.token = token;
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getUid()));
                if (!YueJianAppStringUtil.isEmpty(user.getName())) {
                    setProperty("user.name", user.getName());
                }
                setProperty("user.token", token);
                if (!YueJianAppStringUtil.isEmpty(user.getSignature())) {
                    setProperty("user.sign", user.getSignature());
                }
                if (user.getAvatarUrl() != null || !YueJianAppStringUtil.isEmpty(user.getAvatarUrl())) {
                    setProperty("user.avatar", user.getAvatarUrl());
                }
                if (user.getLocation() != null || !YueJianAppStringUtil.isEmpty(user.getLocation())) {
                    setProperty("user.provinceBean", user.getLocation());
                }
                setProperty("user.sex", String.valueOf(user.getGender()));
                if (user.getSignature() != null || !YueJianAppStringUtil.isEmpty(user.getSignature())) {
                    setProperty("user.signature", user.getSignature());
                }
                setProperty("user.level", String.valueOf(user.getLevel()));
                setProperty("user.isBroadcaster", String.valueOf(user.getIsBroadcaster()));
            }
        });
    }

    public void setPrivateInfo(YueJianAppACUserPrivateInfoModel privateInfoModel) {
        this.privateInfoModel = privateInfoModel;
    }

    public YueJianAppACUserPrivateInfoModel getPrivateInfoModel() {
        return privateInfoModel;
    }

    public boolean getCanVideoChat() {
        if (privateInfoModel == null) {
            return false;
        } else {
            return privateInfoModel.getCanVideoChat();
        }
    }

    /**
     * 更新 公有信息
     *
     * @param user
     */
    public void updateAcPublicUser(final YueJianAppACUserPublicInfoModel user) {
        if (null == user) {
            return;
        }
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getUid()));
                if (user.getName() != null || !YueJianAppStringUtil.isEmpty(user.getName())) {
                    setProperty("user.name", user.getName());
                }
                if (user.getSignature() != null || !YueJianAppStringUtil.isEmpty(user.getSignature())) {
                    setProperty("user.sign", user.getSignature());
                }
                if (user.getAvatarUrl() != null || !YueJianAppStringUtil.isEmpty(user.getAvatarUrl())) {
                    setProperty("user.avatar", user.getAvatarUrl());
                }
                if (user.getLocation() != null || !YueJianAppStringUtil.isEmpty(user.getLocation())) {
                    setProperty("user.provinceBean", user.getLocation());
                }
                setProperty("user.sex", String.valueOf(user.getGender()));
                if (user.getSignature() != null || !YueJianAppStringUtil.isEmpty(user.getSignature())) {
                    setProperty("user.signature", user.getSignature());
                }
                setProperty("user.level", String.valueOf(user.getLevel()));
                setProperty("user.isBroadcaster", String.valueOf(user.getIsBroadcaster()));
                setProperty("user.status", String.valueOf(user.getStatus()));
                setProperty("user.vipTime", String.valueOf(user.getVipTime()));
            }
        });
    }

    public void updateACUserHomepageInfoModel(YueJianAppACUserHomepageInfoModel model) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(new File(YueJianAppAppConfig.DEFAULT_SAVE_USER_INFO_PATH)));
            //            oos.write(mPersonMike);
            oos.writeObject(model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得登录用户的信息
     *
     * @return
     */
    public YueJianAppUserBean getLoginUser() {
        YueJianAppUserBean user = new YueJianAppUserBean();
        try {
            user.setId(YueJianAppStringUtil.toInt(getProperty("user.uid"), 0));
            user.setGold(getProperty("user.gold"));
            user.setAvatar(getProperty("user.avatar"));
            user.setUser_nicename(getProperty("user.name"));
            user.setSignature(getProperty("user.sign"));
            user.setToken(getProperty("user.token"));
            user.setCity(getProperty("user.provinceBean"));
            user.setCoin(getProperty("user.coin"));
            String sex = getProperty("user.sex");
            user.setSex(Integer.parseInt(sex == null ? "0" : sex));
            user.setSignature(getProperty("user.signature"));
            user.setAvatar(getProperty("user.avatar"));
            String level = getProperty("user.level");
            user.setLevel(Integer.parseInt(level == null ? "0" : level));
            user.setChips(getProperty("user.chips"));
            user.setExperience(getProperty("user.experience"));
            user.setMax_exp(getProperty("user.max_exp"));
            String isauth = getProperty("user.isauth");
            user.setIsauth(Integer.parseInt(isauth == null ? "0" : isauth));
            user.setVotestotal(getProperty("user.votestotal"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 替换掉的方法
     * 获取登录人信息
     *
     * @return
     */
    public YueJianAppACUserPublicInfoModel getAcPublicUser() {
        YueJianAppACUserPublicInfoModel user = new YueJianAppACUserPublicInfoModel(null);
        try {
            user.setUid(getProperty("user.uid"));
            user.setAvatarUrl(getProperty("user.avatar"));
            user.setName(getProperty("user.name"));
            user.setSignature(getProperty("user.sign"));
            user.setLocation(getProperty("user.provinceBean"));
            String sex = getProperty("user.sex");
            user.setGender(
                    Integer.parseInt(sex == null ? String.valueOf(YueJianAppApiProtoHelper.GENDER_MALE) : sex));
            user.setSignature(getProperty("user.signature"));
            String level = getProperty("user.level");
            user.setLevel(Integer.parseInt(level == null ? "0" : level));
            boolean isBroadcaster = Boolean.parseBoolean(getProperty("user.isBroadcaster"));
            user.setIsBroadcaster(isBroadcaster);
            String status = getProperty("user.status");
            user.setStatus(Integer.parseInt(status == null ? "0" : status));
            String vipTime = getProperty("user.vipTime");
            user.setVipTime(Integer.parseInt(YueJianAppStringUtil.isEmpty(vipTime) ? "0" : vipTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = "";
        this.login = false;
        removeProperty("user.uid", "user.gold", "user.votestotal", "user.chips", "user.experience",
                "user.max_exp", "user.token", "user.game_token", "user.name", "user.avatar", "user.sign",
                "user.provinceBean", "user.coin", "user.sex", "user.signature", "user.level");
    }

    public String getLoginUid() {
        return loginUid;
    }

    public String getToken() {
        return token;
    }

    public boolean isLogin() {
        return login;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        cleanLoginInfo();
        this.login = false;
        this.loginUid = "";
        this.token = "";
    }

    /**
     * 清除保存的缓存
     */ /*
   public void cleanCookie() {
       removeProperty(YueJianAppAppConfig.CONF_COOKIE);
   }

   /**
    * 清除app缓存
    */
    public void clearAppCache() {
        YueJianAppDataCleanManager.cleanDatabases(this);
        // 清除数据缓存
        YueJianAppDataCleanManager.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            YueJianAppDataCleanManager.cleanCustomCache(YueJianAppMethodsCompat.getExternalCacheDir(this));
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
        Core.getKJBitmap().cleanCache();
    }


    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public static String getTweetDraft() {
        return getPreferences().getString(YueJianAppAppConfig.KEY_TWEET_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setTweetDraft(String draft) {
        set(YueJianAppAppConfig.KEY_TWEET_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static String getNoteDraft() {
        return getPreferences().getString(YueJianAppAppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setNoteDraft(String draft) {
        set(YueJianAppAppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static boolean isFristStart() {
        return getPreferences().getBoolean(YueJianAppAppConfig.KEY_FIRST_START, true);
    }

    public static void setFristStart(boolean frist) {
        set(YueJianAppAppConfig.KEY_FIRST_START, frist);
    }

    public static void showToastAppMsg(Activity context, String msg) {
        if (context == null) {
            return;
        }
        AppMsg.makeText(context, msg, new AppMsg.Style(1000, R.drawable.yue_jian_app_toast_background)).show();
    }

    public static void showToastAppMsg(Activity context, String msg, int resID) {
        AppMsg.makeText(context, msg, new AppMsg.Style(1000, resID)).show();
    }

    public static void showToastAppMsg(Activity context, String msg, int resID, int duration) {
        AppMsg.makeText(context, msg, new AppMsg.Style(duration, resID)).show();
    }

    /********************************************声网、faceUnity相关*******************************/
    private YueJianAppWorkerThread mWorkerThread;

    public synchronized void initWorkerThread() {
        YueJianAppTLog.info("initWorkerThread");
        if (mWorkerThread == null) {
            mWorkerThread = new YueJianAppWorkerThread(getApplicationContext());
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }

    public synchronized YueJianAppWorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
    }

    private UploadManager uploadManager;

    public UploadManager getUploadManager() {
        if (uploadManager == null) {
//            Recorder recorder = null;
//            try {
//                recorder = new FileRecorder();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            KeyGenerator keyGen = new KeyGenerator(){
//                public String gen(String key, File file){
//                    // 不必使用url_safe_base64转换，uploadManager内部会处理
//                    // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
//                    return key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
//                }
//            };

            Configuration config = new Configuration.Builder()
                    .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                    .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                    .connectTimeout(10)           // 链接超时。默认10秒
//                    .useHttps(true)               // 是否使用https上传域名
                    .responseTimeout(60)          // 服务器响应超时。默认60秒
//                    .recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
//                    .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                    .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                    .build();
            uploadManager = new UploadManager(config);
        }
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        return uploadManager;
    }
}
