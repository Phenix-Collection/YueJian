package com.mingquan.yuejian.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.YueJianAppAppManager;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseFullModeActivity;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.view.YueJianAppFullScreenVideoView;
import com.mingquan.yuejian.utils.YueJianAppAppUtil;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.utils.YueJianAppUtils;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class YueJianAppLiveLoginSelectActivity extends YueJianAppBaseFullModeActivity implements PlatformActionListener {
    private String[] names = {QQ.NAME, Wechat.NAME, SinaWeibo.NAME};
    private String type;
    @BindView(R.id.tv_privacy)
    TextView mTvPrivacy;
    @BindView(R.id.txv_version)
    TextView mTvVersion;
    @BindView(R.id.loading_img)
    ImageView mLoadingImg;
    @BindView(R.id.rl_qq)
    RelativeLayout mQQ;
    @BindView(R.id.rl_sina)
    RelativeLayout mSina;
    @BindView(R.id.rl_wechat)
    RelativeLayout mWechat;
    @BindView(R.id.video_view)
    YueJianAppFullScreenVideoView mVideoView;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.yue_jian_app_activity_show_login;
    }

    @Override
    public void initView() {
        getSupportActionBar().hide();
        mTvPrivacy.setOnClickListener(this);

        if (mTvVersion != null) {
            mTvVersion.setText(String.format("Version %s", YueJianAppAppUtil.getInstance().getAppVersionName(this)));
        }
    }

    @Override
    public void initData() {
        mContext = this;
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.login_video;

        mVideoView.setVideoURI(Uri.parse(uri));
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVideoView.start();
            }
        });
        mVideoView.start();
    }

    @Override
    @OnClick({R.id.rl_qq, R.id.rl_sina, R.id.rl_wechat, R.id.rl_mobile, R.id.tv_privacy})
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.rl_qq:
                if (YueJianAppUtils.isFastClick())
                    return;
                type = "QQ";
                enableAgainClick(false);
                otherLogin(names[0]);
                break;
            case R.id.rl_sina:
                if (YueJianAppUtils.isFastClick())
                    return;
                type = "WB";
                enableAgainClick(false);
                otherLogin(names[2]);
                break;
            case R.id.rl_wechat:
                if (YueJianAppUtils.isFastClick())
                    return;
                type = "WX";
                enableAgainClick(false);
                otherLogin(names[1]);
                break;
            case R.id.rl_mobile:
                if (YueJianAppUtils.isFastClick())
                    return;
                YueJianAppUIHelper.showMobilLogin(this);
                break;
            case R.id.tv_privacy:
                if (YueJianAppUtils.isFastClick()) {
                    return;
                }
                YueJianAppUIHelper.showWebView(
                        this, YueJianAppAppContext.getInstance().mAgreementUrl, "隐私政策");
                break;
        }
    }

    private class MyRunnable implements java.lang.Runnable {

        @Override
        public void run() {
            Platform plat = null;
            switch (type) {
                case "QQ":
                    plat = ShareSDK.getPlatform(QQ.NAME);
                    break;
                case "WB":
                    plat = ShareSDK.getPlatform(SinaWeibo.NAME);
                    break;
                case "WX":
                    plat = ShareSDK.getPlatform(Wechat.NAME);
                    //判断是否安装了微信客户端
                    if (!plat.isClientValid()) {
                        mHandler.sendEmptyMessage(WECHAT_NO_CLIENT);
                        return;
                    }
                    break;
            }

            if (plat != null && plat.isAuthValid()) {
                plat.removeAccount(true);
            }
            plat.setPlatformActionListener(YueJianAppLiveLoginSelectActivity.this);
            plat.SSOSetting(false);
            plat.showUser(null);
        }
    }


    private MyRunnable mMyRunnable;

    private void otherLogin(String name) {
        if (mLoadingImg != null) {
            mLoadingImg.setVisibility(View.VISIBLE);
            animationDrawable = (AnimationDrawable) mLoadingImg.getBackground();
            animationDrawable.start();
        }
        if (mMyRunnable == null) {
            mMyRunnable = new MyRunnable();
        }

        new Thread(mMyRunnable).start();
    }


    AnimationDrawable animationDrawable = null;

    @Override
    public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
        //用户资源都保存到res
        //通过打印res数据看看有哪些数据是你想要的
        if (i == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[]{platform.getName(), hashMap};
            mHandler.sendMessage(msg);
        }
    }

    private void platformToLogin(Platform platform) {
        PlatformDb platDB = platform.getDb(); //获取数平台数据DB
        if (platDB.getUserId() == null || TextUtils.isEmpty(platDB.getUserId())) {
            return;
        }
        loginWithThirdParty(platDB);
    }

    private void loginWithThirdParty(PlatformDb platDB) {
        String mUnionid = platDB.get("unionid");
        String openId = platDB.getUserId();
        String userName = platDB.getUserName().trim();
        String icon = platDB.getUserIcon();
        YueJianAppApiProtoHelper.sendACLoginWithThirdPartyReq(this,
                type,
                openId,
                YueJianAppStringUtil.encodeStr(userName),
                icon,
                String.valueOf(YueJianAppAppContext.getInstance().getPackageInfo().versionCode),
                YueJianAppAppContext.getInstance().getAppChannel(),
                Build.MODEL + ", android " + Build.VERSION.SDK_INT,
                YueJianAppAppContext.getInstance().deviceId,
                mUnionid,
                getPackageName(),
                new YueJianAppApiProtoHelper.ACLoginWithThirdPartyReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        mHandler.sendEmptyMessage(ERROR_RESPONSE);
                    }

                    @Override
                    public void onResponse(YueJianAppACUserPublicInfoModel user, String token) {
                        Message message = Message.obtain();
                        message.obj = new Object[]{user, token};
                        message.what = SUCCESS_RESPONSE;
                        mHandler.sendMessage(message);
                    }
                });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        YueJianAppTLog.error(platform.getName() + "login onError i===:%s", i);
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        YueJianAppTLog.warn(platform.getName() + "login onCancel i===:%s", i);
        if (i == Platform.ACTION_USER_INFOR) {
            mHandler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }

    private void enableAgainClick(boolean isEnable) {
        if (mWechat != null) {
            mWechat.setEnabled(isEnable);
        }
//        mSina.setEnabled(isEnable);
//        mQQ.setEnabled(isEnable);
    }

    /**
     * 清除loading的动画
     */
    private void clearLoadingAnimation() {
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
        if (mLoadingImg != null) {
            mLoadingImg.clearAnimation();
            mLoadingImg.setVisibility(View.GONE);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(
                "登录选择"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mVideoView.start();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("登录选择"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
        // onPageEnd 在onPause 之前调用,因为 onPause
        // 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    private android.os.Handler mHandler = new MyHandler(this);
    private static final int WECHAT_NO_CLIENT = 0;
    private static final int MSG_AUTH_CANCEL = 1;
    private static final int MSG_AUTH_ERROR = 2;
    private static final int MSG_AUTH_COMPLETE = 3;
    private static final int SUCCESS_RESPONSE = 4;
    private static final int ERROR_RESPONSE = 5;

    private static class MyHandler extends android.os.Handler {
        private WeakReference<YueJianAppLiveLoginSelectActivity> mWeakReference;

        MyHandler(YueJianAppLiveLoginSelectActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            YueJianAppLiveLoginSelectActivity activity = mWeakReference.get();
            if (activity == null) return;

            switch (msg.what) {
                case MSG_AUTH_CANCEL:
                    //取消授权
                    YueJianAppAppContext.showToastAppMsg(
                            activity, "授权已取消", R.color.transparent);
                    activity.clearLoadingAnimation();
                    activity.enableAgainClick(true);
                    break;
                case MSG_AUTH_ERROR:
                    //授权失败
                    YueJianAppAppContext.showToastAppMsg(
                            activity, "授权登录失败", R.color.transparent);
                    activity.clearLoadingAnimation();
                    activity.enableAgainClick(true);
                    break;
                case MSG_AUTH_COMPLETE:
                    //授权成功
                    Object[] objs = (Object[]) msg.obj;
                    String platform = (String) objs[0];
                    activity.platformToLogin(ShareSDK.getPlatform(platform));
                    break;
                case SUCCESS_RESPONSE:
                    Object[] repObjs = (Object[]) msg.obj;
                    YueJianAppACUserPublicInfoModel user = (YueJianAppACUserPublicInfoModel) repObjs[0];
                    String token = (String) repObjs[1];
                    MobclickAgent.onProfileSignIn(activity.type, String.valueOf(user.getUid()));
                    //保存用户信息
                    YueJianAppAppContext.getInstance().saveUserInfo(user, token);
                    YueJianAppUIHelper.showMainActivity(activity.mContext);
                    activity.finish();
                    YueJianAppAppManager.getAppManager().finishAllActivity();
                    System.gc();

                    activity.clearLoadingAnimation();
                    activity.enableAgainClick(true);
                    break;
                case ERROR_RESPONSE:
                    YueJianAppAppContext.showToastAppMsg(
                            activity, "登录失败", R.color.transparent);

                    activity.clearLoadingAnimation();
                    activity.enableAgainClick(true);
                    break;
                case WECHAT_NO_CLIENT:
                    YueJianAppAppContext.showToastAppMsg(
                            activity, "请检查是否安装了微信客户端", R.color.transparent);
                    activity.clearLoadingAnimation();
                    activity.enableAgainClick(true);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
    }
}
