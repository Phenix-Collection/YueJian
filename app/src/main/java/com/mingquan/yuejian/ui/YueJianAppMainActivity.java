package com.mingquan.yuejian.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.em.YueJianAppMainTab;
import com.mingquan.yuejian.huawei.YueJianAppHMSAgent;
import com.mingquan.yuejian.huawei.YueJianAppHuaweiPushReceiver;
import com.mingquan.yuejian.huawei.common.handler.YueJianAppConnectHandler;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppDeleteTokenHandler;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppEnableReceiveNormalMsgHandler;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppEnableReceiveNotifyMsgHandler;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppGetPushStateHandler;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppGetTokenHandler;
import com.mingquan.yuejian.huawei.push.handler.YueJianAppQueryAgreementHandler;
import com.mingquan.yuejian.interf.YueJianAppIBottomDialog;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACChatModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPrivateInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.view.YueJianAppDanMuView;
import com.mingquan.yuejian.utils.YueJianAppAppUtil;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppRom;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.vchat.YueJianAppDanMuEvent;
import com.mingquan.yuejian.vchat.YueJianAppMessageEvent;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class YueJianAppMainActivity extends YueJianAppFullScreenModeActivity implements TabHost.OnTabChangeListener, YueJianAppHuaweiPushReceiver.IPushCallback {

    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;
    @BindView(R.id.tab_host_layout)
    RelativeLayout tabHostLayout;
    @BindView(R.id.red_point)
    TextView redPoint;
    @BindView(R.id.dan_mu)
    YueJianAppDanMuView danMuView;
    private Context mContext;

    private TranslateAnimation animation;
    private TextView[] tabTitles = new TextView[4];
    private boolean isVisible = true;
    // 高德地图
    private AMapLocationClient mLocationClient = null;
    /**
     * 需要进行检测的权限数组
     */
    private String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_main);
        ButterKnife.bind(this);
        mContext = this;

        initViews();
        initData();
    }

    private void initViews() {
        mTabHost.setup(mContext, getSupportFragmentManager(), R.id.fragment_container);
        mTabHost.getTabWidget().setShowDividers(0);
        ((RelativeLayout.LayoutParams) redPoint.getLayoutParams()).rightMargin = (int) (YueJianAppTDevice.getScreenWidth() / 4 * 1.25);
        initTabs();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initTabs() {
        YueJianAppMainTab[] tabs = YueJianAppMainTab.values();
        String[] titles = getResources().getStringArray(R.array.tab_bottom_titles);
        for (int i = 0; i < tabs.length; i++) {
            YueJianAppMainTab mainTab = tabs[i];
            TabHost.TabSpec tab = mTabHost.newTabSpec(String.valueOf(mainTab.getResName()));
            View indicator = LayoutInflater.from(getApplicationContext()).inflate(R.layout.yue_jian_app_bottom_tab_indicator, null);
            ImageView tabImg = (ImageView) indicator.findViewById(R.id.tab_bottom_img);
            final TextView tabTitle = (TextView) indicator.findViewById(R.id.tab_bottom_tv);
            tabTitle.setText(titles[i]);
            tabTitle.setTextColor(i == 0 ? getResources().getColor(R.color.vchat_ea4579) : getResources().getColor(R.color.black));
            tabTitles[i] = tabTitle;
            tabImg.setImageDrawable(getResources().getDrawable(mainTab.getResIcon()));
            tab.setIndicator(indicator);
            tab.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return new View(mContext);
                }
            });

            mTabHost.addTab(tab, mainTab.getClz(), null);
        }
        mTabHost.setOnTabChangedListener(this);
        /*mTabHost.getTabWidget().getChildTabViewAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabHost.setCurrentTab(0);
                EventBus.getDefault().post(new YueJianAppMessageEvent(YueJianAppAppConfig.ACTION_REFRESH_BROADCASTER_LIST));
            }
        });*/
        mTabHost.setCurrentTab(0);
    }

    private void initData() {
        //检查token是否过期
        checkTokenIsOutTime();

        //注册极光推送
        registerJPush();
        //登录环信
        registerAndLoginIM();

        getPublicInfo();

        getPrivateInfo();

        getAgentPromoteUrl();

        getBroadcasterShareUrl();

        YueJianAppAppContext.getInstance().checkSocket();

        // 华为设备，获取华为token
        if (YueJianAppRom.isEmui()) {
            YueJianAppHuaweiPushReceiver.registerPushCallback(YueJianAppMainActivity.this);
            YueJianAppHMSAgent.connect(YueJianAppMainActivity.this, new YueJianAppConnectHandler() {
                @Override
                public void onConnect(int rst) {
                    getHuaweiPushToken();
                }
            });
        }

        YueJianAppAppUtil.getInstance().checkPermissions(
                this,
                needPermissions,
                new YueJianAppAppUtil.IPermissionsResult() {
                    @Override
                    public void passPermissons() {
                        YueJianAppTLog.info("main activity 定位权限被允许");
                        initAmap();
                    }

                    @Override
                    public void forbitPermissons() {
                        YueJianAppTLog.info("main activity 定位权限被禁用");
                        YueJianAppAppUtil.getInstance().showSystemPermissionsSettingDialog(
                                YueJianAppMainActivity.this,
                                "定位权限被禁用, 手动开启?",
                                true);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        YueJianAppAppUtil.getInstance().onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (messageListener != null) {
            EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        }

        YueJianAppHuaweiPushReceiver.unRegisterPushCallback(this);
    }

    private void loginIM(String pwd, final boolean reLogin) {
        YueJianAppTLog.info("login im");
        EMClient.getInstance().login(
                YueJianAppAppConfig.IM_ACCOUNT + YueJianAppAppContext.getInstance().getLoginUid(), pwd,
                new EMCallBack() { //回调
                    @Override
                    public void onSuccess() {
                        YueJianAppTLog.info("im login success");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                YueJianAppTLog.info(
                                        "主页登录聊天服务器成功" + YueJianAppAppConfig.IM_ACCOUNT + YueJianAppAppContext.getInstance().getLoginUid());
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                        if (204 == code) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    YueJianAppAppContext.showToastAppMsg(YueJianAppMainActivity.this, "聊天服务器登录失败,请重新登录");
                                }
                            });
                        }
                        if (reLogin) {
                            loginIM("peppertv/" + YueJianAppAppContext.getInstance().getLoginUid(), false);
                        }
                        YueJianAppTLog.warn("主页登录聊天服务器失败"
                                + "code:" + code + "    MESSAGE:" + message);
                    }
                });
    }

    /**
     * 登录或注册环信即时聊天
     */
    private void registerAndLoginIM() {
        if (!EMClient.getInstance().isConnected()) {
            //没有登录
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().createAccount(
                                YueJianAppAppConfig.IM_ACCOUNT + YueJianAppAppContext.getInstance().getLoginUid(),
                                "peppertv/" + YueJianAppAppContext.getInstance().getLoginUid());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //注册成功,开始登陆
                                loginIM("peppertv/" + YueJianAppAppContext.getInstance().getLoginUid(), true);
                            }
                        });

                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        if (e.getErrorCode() == EMError.USER_ALREADY_EXIST) {
                            //用户已存在，直接登录
                            loginIM("peppertv/" + YueJianAppAppContext.getInstance().getLoginUid(), true);
                        }
                    }
                }
            }).start();
        }

        onMessageUpdate(); // 更新小红点状态

        if (messageListener != null) {
            EMClient.getInstance().chatManager().addMessageListener(messageListener);
        }
    }

    private EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            YueJianAppTLog.info("main activity onMessageReceived list size:%s", list.size());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onMessageUpdate();
                }
            });
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {
        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {
        }
    };

    private void getPublicInfo() {
        YueJianAppApiProtoHelper.sendACGetUserPublicInfoReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                new YueJianAppApiProtoHelper.ACGetUserPublicInfoReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse(YueJianAppACUserPublicInfoModel user) {
                        YueJianAppAppContext.getInstance().updateAcPublicUser(user);
                    }
                });
    }

    /**
     * 获取用户私有信息
     */
    private void getPrivateInfo() {
        YueJianAppApiProtoHelper.sendACGetUserPrivateInfoReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                new YueJianAppApiProtoHelper.ACGetUserPrivateInfoReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse(final YueJianAppACUserPrivateInfoModel user) {
                        YueJianAppAppContext.getInstance().setPrivateInfo(user);
                        int currVersionCode = YueJianAppTDevice.getVersionCode();

                        if (!user.getIsra() && currVersionCode < YueJianAppAppContext.getInstance().mCurrBuildNo) { // 不是测试号并且当前版本号比线上版本低
                            YueJianAppTDevice.updateVersion(
                                    YueJianAppMainActivity.this,
                                    YueJianAppAppContext.getInstance().mDownloadUrl,
                                    currVersionCode < YueJianAppAppContext.getInstance().mMinBuildNo);
                            return;
                        }

                        // 勿扰未开启,通知未开启时,弹出开启通知的提示
                        if (user.getIsBusy()) {
                            return;
                        }

                        showFreeTrial(user);
                    }
                });
    }

    /**
     * 显示体验视频通话对话框
     */
    private void showFreeTrial(YueJianAppACUserPrivateInfoModel user) {
        if (!user.getCanFreeTrial()) {
            return;
        }

        // 可以免费体验视频通话
//        YueJianAppAppContext.getInstance().setCanFreeTrial(false);
        YueJianAppDialogHelp.showToVideoDialog(YueJianAppMainActivity.this, new YueJianAppIBottomDialog() {
            @Override
            public void cancelDialog(Dialog d) {
                d.dismiss();
            }

            @Override
            public void determineDialog(final Dialog d, Object... value) {
                YueJianAppApiProtoHelper.sendACGetBroadcastListReq(
                        YueJianAppMainActivity.this,
                        YueJianAppApiProtoHelper.BROADCASTER_RECOMMEND,
                        10,
                        0,
                        YueJianAppAppContext.getInstance().getLoginUid(),
                        YueJianAppApiProtoHelper.GENDER_NONE,
                        new YueJianAppApiProtoHelper.ACGetBroadcastListReqCallback() {
                            @Override
                            public void onError(int errCode, String errMessage) {
                                YueJianAppTLog.error(errMessage);
                                d.dismiss();
                            }

                            @Override
                            public void onResponse(ArrayList<YueJianAppACUserPublicInfoModel> broadcasts, int nextOffset) {
                                List<YueJianAppACUserPublicInfoModel> models = new ArrayList<>();
                                d.dismiss();
                                if (broadcasts.size() <= 0) {
                                    return;
                                }
                                for (YueJianAppACUserPublicInfoModel model : broadcasts) {
                                    if (model.getStatus() == YueJianAppApiProtoHelper.BROADCASTER_STATUS_ONLINE) {
                                        models.add(model);
                                    }
                                }
                                int randomIndex = (int) (Math.random() * models.size());
                                YueJianAppUIHelper.showVideoChatActivity(YueJianAppMainActivity.this, models.get(randomIndex), "");
                            }
                        });
            }
        });
    }

    /**
     * 获取代理推广地址
     */
    private void getAgentPromoteUrl() {
        YueJianAppApiProtoHelper.sendACGetAgentPromotePosterUrlReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                new YueJianAppApiProtoHelper.ACGetAgentPromotePosterUrlReqCallback() {

                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse(String imageUrl) {
                        YueJianAppTLog.info("imageUrl:%s", imageUrl);
                        YueJianAppAppContext.getInstance().mAgentPromoteImageUrl = imageUrl;
                        Glide.with(YueJianAppMainActivity.this).load(imageUrl).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                    }
                });
    }

    /**
     * 获取主播分享地址
     */
    private void getBroadcasterShareUrl() {
        YueJianAppApiProtoHelper.sendACGetBroadcasterShareUrlReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                new YueJianAppApiProtoHelper.ACGetBroadcasterShareUrlReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse(String shareUrl) {
                        YueJianAppTLog.info("shareUrl:%s", shareUrl);
                        YueJianAppAppContext.getInstance().mBroadcastShareUrl = shareUrl;
                    }
                }
        );
    }

    /**
     * @dw 注册极光推送
     */
    private void registerJPush() {
        JPushInterface.resumePush(this);
        JPushInterface.setAlias(
                this, YueJianAppAppContext.getInstance().getLoginUid() + "PUSH", new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                    }
                });
    }

    /**
     * @dw 检查token是否过期
     */
    private void checkTokenIsOutTime() {
        YueJianAppApiProtoHelper.sendACUpdateAppStateReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                String.valueOf(YueJianAppTDevice.getVersionCode()),
                Build.MODEL + ", android " + Build.VERSION.SDK_INT,
                YueJianAppAppContext.getInstance().deviceId,
                new YueJianAppApiProtoHelper.ACUpdateAppStateReqCallback() {

                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse() {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(Object event) {
        // 私信
        if (event instanceof YueJianAppMessageEvent && ((YueJianAppMessageEvent) event).getMessage().equals(YueJianAppAppConfig.ACTION_PRIVATE_MESSAGE)) {
            onMessageUpdate();
        }
        // 弹幕
        else if (event instanceof YueJianAppDanMuEvent) {
            showDanMu(((YueJianAppDanMuEvent) event).getChatModel());
        }
    }


    /**
     * 更新环信未读消息
     */
    public void onMessageUpdate() {
        int messageCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (messageCount > 0) {
            redPoint.setVisibility(View.VISIBLE);
            redPoint.setText(String.valueOf(messageCount));
        } else {
            redPoint.setVisibility(View.GONE);
        }
    }

    /**
     * 显示弹幕
     *
     * @param chatModel
     */
    private void showDanMu(YueJianAppACChatModel chatModel) {
        danMuView.addDanMuHorn(chatModel.getSender(), chatModel.getContent());
    }

    @Override
    public void onTabChanged(String tabId) {
        int id = Integer.valueOf(tabId);
        for (int i = 0; i < tabTitles.length; i++) {
            tabTitles[i].setTextColor(i == id ? getResources().getColor(R.color.vchat_ea4579) : getResources().getColor(R.color.black));
        }
    }

    /**
     * 隐藏底部导航栏
     */
    public void hideTabHost() {
        if (!isVisible) {
            return;
        }

        isVisible = false;
        animation = new TranslateAnimation(0, 0, 0, YueJianAppTDevice.dpToPixel(60));
        animation.setDuration(500);
        animation.setFillAfter(true);
        tabHostLayout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 显示底部导航栏
     */
    public void showTabHost() {
        if (isVisible) {
            return;
        }

        isVisible = true;
        animation = new TranslateAnimation(0, 0, YueJianAppTDevice.dpToPixel(60), 0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        tabHostLayout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 设置底部导航栏是否显示
     */
    public void setTabHostIsVisible(boolean isShow) {
        if (isVisible && !isShow) {
            animation = new TranslateAnimation(0, 0, YueJianAppTDevice.dpToPixel(55), 0);
        } else if (!isVisible && isShow) {
            animation = new TranslateAnimation(0, 0, 0, YueJianAppTDevice.dpToPixel(55));
        }
        isVisible = isShow;
        animation.setDuration(500);
        animation.setFillAfter(true);
        tabHostLayout.startAnimation(animation);
    }

    @Override
    public void onReceive(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            if (b != null && YueJianAppHuaweiPushReceiver.ACTION_TOKEN.equals(action)) {
                YueJianAppAppContext.getInstance().mHuaweiToken = b.getString(YueJianAppHuaweiPushReceiver.ACTION_TOKEN);
                YueJianAppTLog.info("huawei push onReceive token: %s", YueJianAppAppContext.getInstance().mHuaweiToken);
                EMClient.getInstance().sendHMSPushTokenToServer(YueJianAppAppConfig.HUA_WEI_APP_ID, YueJianAppAppContext.getInstance().mHuaweiToken);
            } else if (b != null && YueJianAppHuaweiPushReceiver.ACTION_UPDATE_UI.equals(action)) {
                String log = b.getString("log");
                YueJianAppTLog.info("huawei push onReceive log: %s", log);
            }
        }
    }

    /**
     * 获取华为推送token
     */
    private void getHuaweiPushToken() {
        YueJianAppHMSAgent.Push.getToken(new YueJianAppGetTokenHandler() {
            @Override
            public void onResult(int rtnCode) {
            }
        });
    }

    /**
     * 删除华为推送token
     */
    private void deleteHuaweiPushToken() {
        YueJianAppHMSAgent.Push.deleteToken(YueJianAppAppContext.getInstance().mHuaweiToken, new YueJianAppDeleteTokenHandler() {
            @Override
            public void onResult(int rst) {
                YueJianAppTLog.info("delete hua wei push token :%s", rst);
            }
        });
    }

    /**
     * 获取华为推送状态
     */
    private void getHuaweiPushStatus() {
        YueJianAppHMSAgent.Push.getPushState(new YueJianAppGetPushStateHandler() {
            @Override
            public void onResult(int rst) {
                YueJianAppTLog.info("get hua wei push status :%s", rst);
            }
        });
    }

    /**
     * 设置是否接收普通透传消息（华为）
     *
     * @param enable 是否开启
     */
    private void setReceiveNormalMsg(boolean enable) {
        YueJianAppHMSAgent.Push.enableReceiveNormalMsg(enable, new YueJianAppEnableReceiveNormalMsgHandler() {
            @Override
            public void onResult(int rst) {
                YueJianAppTLog.info("set receive normal msg :%s", rst);
            }
        });
    }

    /**
     * 设置接收通知消息（华为）
     *
     * @param enable 是否开启
     */
    private void setReceiveNotifyMsg(boolean enable) {
        YueJianAppHMSAgent.Push.enableReceiveNotifyMsg(enable, new YueJianAppEnableReceiveNotifyMsgHandler() {
            @Override
            public void onResult(int rst) {
                YueJianAppTLog.info("set receive notify msg :%s", rst);
            }
        });
    }

    /**
     * 显示华为推送协议
     */
    private void showHuaweiPushAgreement() {
        YueJianAppHMSAgent.Push.queryAgreement(new YueJianAppQueryAgreementHandler() {
            @Override
            public void onResult(int rst) {
                YueJianAppTLog.info("show hua wei push agreement :%s", rst);
            }
        });
    }

    private void initAmap() {
        LocationManager systemService = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (systemService != null) {
            boolean GPSEnabled = systemService.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (GPSEnabled) {
                //初始化定位
                mLocationClient = new AMapLocationClient(getApplicationContext());
                //设置定位回调监听
                mLocationClient.setLocationListener(mLocationListener);

                //初始化定位参数
                AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
                //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
                //设置是否返回地址信息（默认返回地址信息）
                mLocationOption.setNeedAddress(true);
                //设置是否只定位一次,默认为false
                mLocationOption.setOnceLocation(true);
                //设置是否强制刷新WIFI，默认为强制刷新
                mLocationOption.setWifiActiveScan(true);
                //设置是否允许模拟位置,默认为false，不允许模拟位置
                mLocationOption.setMockEnable(false);
                //设置定位间隔,单位毫秒,默认为2000ms
                mLocationOption.setInterval(2000);
                //给定位客户端对象设置定位参数
                mLocationClient.setLocationOption(mLocationOption);
                //启动定位
                mLocationClient.startLocation();
            }
        }
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            CoordinateConverter converter = new CoordinateConverter(YueJianAppMainActivity.this);
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType(); //获取当前定位结果来源，如网络定位结果，详见定位类型表
                    double latitude = amapLocation.getLatitude(); //获取纬度
                    double longitude = amapLocation.getLongitude(); //获取经度
                    amapLocation.getAccuracy(); //获取精度信息
                    //返回true代表当前位置在大陆、港澳地区，反之不在。
                    //第一个参数为纬度，第二个为经度，纬度和经度均为高德坐标系。
                    boolean isAMapDataAvailable = converter.isAMapDataAvailable(latitude, longitude);
                    YueJianAppApiProtoHelper.sendACUpdateGpsLocationReq(
                            YueJianAppMainActivity.this,
                            YueJianAppAppContext.getInstance().getLoginUid(),
                            YueJianAppAppContext.getInstance().getToken(),
                            String.valueOf(longitude),
                            String.valueOf(latitude),
                            new YueJianAppApiProtoHelper.ACUpdateGpsLocationReqCallback() {
                                @Override
                                public void onError(int errCode, String errMessage) {
                                    YueJianAppAppContext.showToastAppMsg(YueJianAppMainActivity.this, errMessage);
                                }

                                @Override
                                public void onResponse() {
                                    YueJianAppTLog.info("update gps location success!");
                                }
                            }
                    );
                    YueJianAppApiProtoHelper.sendACUpdateLocationReq(
                            YueJianAppMainActivity.this,
                            YueJianAppAppContext.getInstance().getLoginUid(),
                            YueJianAppAppContext.getInstance().getToken(),
                            "China",
                            amapLocation.getProvince(),
                            amapLocation.getCity(),
                            new YueJianAppApiProtoHelper.ACUpdateLocationReqCallback() {
                                @Override
                                public void onError(int errCode, String errMessage) {
                                    YueJianAppTLog.error(errMessage);
                                    stopLocation();
                                }

                                @Override
                                public void onResponse(String country_, String province_, String city_) {
                                    YueJianAppTLog.info("update location success ! location:%s, province:%s, city:%s", country_, province_, city_);
                                    stopLocation();
                                }
                            });
                } else {
                    YueJianAppTLog.error("location ErrCode:%s, errInfo:%s", amapLocation.getErrorCode(), amapLocation.getErrorInfo());
                }
            } else {
                YueJianAppTLog.warn("定位未开启");
            }
        }
    };

    //停止定位
    private void stopLocation() {
        YueJianAppTLog.info("停止定位");
        mLocationClient.stopLocation();
        mLocationClient.onDestroy(); //销毁定位客户端。
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static Boolean isExit = false;

    /**
     * 双击退出
     */
    private void exitBy2Click() {
        Timer tExit;
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            // 按返回键执行home键的操作
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }
}
