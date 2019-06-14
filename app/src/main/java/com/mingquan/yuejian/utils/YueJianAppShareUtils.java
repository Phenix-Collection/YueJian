package com.mingquan.yuejian.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.interf.YueJianAppICallBack;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACVideoInfoModel;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2016/4/14.
 */
public class YueJianAppShareUtils {
    private Activity mActivity;
    private YueJianAppACVideoInfoModel mVideoInfoModel;
    private YueJianAppICallBack mCallBackOperate;
    private String[] names = new String[]{QQ.NAME, WechatMoments.NAME, QZone.NAME, SinaWeibo.NAME, Wechat.NAME};
    private String name = names[0];

    public void shareVideo(Activity activity, final int viewId, final YueJianAppACVideoInfoModel videoInfoModel, YueJianAppICallBack callBackOperate) {
        mActivity = activity;
        mVideoInfoModel = videoInfoModel;
        this.mCallBackOperate = callBackOperate;
        name = getPlatformByViewId(mActivity, viewId);
        if (name == null) {
            return;
        }
        shareVideo(mActivity, name, mShareListener);
    }

    /**
     * 设置不同的渠道名
     */
    private String getPlatformByViewId(Activity activity, int viewId) {
        switch (viewId) {
            case R.id.ll_live_shar_qq:
                return names[0];
            case R.id.ll_live_shar_pyq:
                Platform pyqPlat = ShareSDK.getPlatform(WechatMoments.NAME);
                if (!pyqPlat.isClientValid()) {
                    Toast.makeText(YueJianAppAppContext.getInstance(), "请检查是否安装了微信客户端", Toast.LENGTH_SHORT).show();
                    return null;
                }
                return names[1];
            case R.id.ll_live_shar_qqzone:
                return names[2];
            case R.id.ll_live_shar_sinna:
                Platform sinaPlat = ShareSDK.getPlatform(SinaWeibo.NAME);
                if (!sinaPlat.isClientValid()) {
                    Toast.makeText(YueJianAppAppContext.getInstance(), "请检查是否安装了新浪微博客户端", Toast.LENGTH_SHORT).show();
                    return null;
                }
                return names[3];
            case R.id.ll_live_shar_wechat:
                Platform wechatPlat = ShareSDK.getPlatform(Wechat.NAME);
                if (!wechatPlat.isClientValid()) {
                    Toast.makeText(YueJianAppAppContext.getInstance(), "请检查是否安装了微信客户端", Toast.LENGTH_SHORT).show();
                    return null;
                }
                return names[4];
            default:
                return null;
        }
    }

    private PlatformActionListener mShareListener = new PlatformActionListener() {
        @Override
        public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
            YueJianAppTLog.info("分享短视频完成");
            YueJianAppApiProtoHelper.sendACShareVideoReq(null, YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(), mVideoInfoModel.getVideoId(), new YueJianAppApiProtoHelper.ACShareVideoReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            YueJianAppTLog.error("分享失败：%s", errMessage);
                        }

                        @Override
                        public void onResponse() {
                            YueJianAppTLog.info("分享成功:%s", platform.getName());
                            if (mCallBackOperate != null) {
                                mCallBackOperate.operate();
                            }
                        }
                    });
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            YueJianAppTLog.error("分享短视频错误: %s", throwable.toString());
        }

        @Override
        public void onCancel(Platform platform, int i) {
            YueJianAppTLog.info("取消分享短视频");
        }
    };

    /**
     * 分享短视频
     *
     * @param activity
     * @param name
     * @param listener
     */
    private void shareVideo(Activity activity, String name, PlatformActionListener listener) {
        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(true);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setPlatform(name);
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.yue_jian_app_ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(activity.getString(R.string.share_title));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用

        // text是分享文本，所有平台都需要这个字段
//        oks.setText(context.getString(R.string.share_detail));
        oks.setImageUrl(mVideoInfoModel.getBroadcaster().getAvatarUrl());
        oks.setUrl(mVideoInfoModel.getVideoUrl());
        oks.setSiteUrl(mVideoInfoModel.getVideoUrl());
        oks.setTitleUrl(mVideoInfoModel.getVideoUrl());
        oks.setSite(activity.getString(R.string.app_name));
        oks.setCallback(listener);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用

        // 启动分享GUI
        oks.show(activity);
    }

    /**
     * 分享推广链接
     */
    public void shareInvitation(final Activity activity, int viewId) {
        name = getPlatformByViewId(activity, viewId);
        if (name == null) {
            return;
        }

        String agentPromoteImageUrl = YueJianAppAppContext.getInstance().mAgentPromoteImageUrl;
        if (YueJianAppStringUtil.isEmpty(agentPromoteImageUrl)) {
            YueJianAppTLog.error("agentPromoteImageUrl == null || empty");
            return;
        }

        final OnekeyShare oks = new OnekeyShare();
        oks.setSilent(true);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setPlatform(name);
        oks.setImageUrl(agentPromoteImageUrl);
        Glide.with(activity).asBitmap().load(agentPromoteImageUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                oks.setImageData(resource);
                oks.setCallback(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        YueJianAppTLog.info("分享推广完成");
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        YueJianAppTLog.error("分享推广错误");
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        YueJianAppTLog.info("取消分享推广");
                    }
                });

                // 启动分享GUI
                oks.show(activity);
            }
        });
    }

    /**
     * 分享主播
     *
     * @param activity
     */
    public void shareBroadcaster(final Activity activity, int viewId) {
        name = getPlatformByViewId(activity, viewId);
        if (name == null) {
            return;
        }
        String agentPromoteImageUrl = YueJianAppAppContext.getInstance().mAgentPromoteImageUrl;
        if (YueJianAppStringUtil.isEmpty(agentPromoteImageUrl)) {
            YueJianAppTLog.error("agentPromoteImageUrl == null || empty");
            return;
        }

        final OnekeyShare oks = new OnekeyShare();
        oks.setSilent(true);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setPlatform(name);
        oks.setImageUrl(agentPromoteImageUrl);
        Glide.with(activity).asBitmap().load(agentPromoteImageUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                oks.setImageData(resource);
                oks.setCallback(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        YueJianAppTLog.info("分享推广完成");
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        YueJianAppTLog.error("分享推广错误");
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        YueJianAppTLog.info("取消分享推广");
                    }
                });

                // 启动分享GUI
                oks.show(activity);
            }
        });
    }
}
