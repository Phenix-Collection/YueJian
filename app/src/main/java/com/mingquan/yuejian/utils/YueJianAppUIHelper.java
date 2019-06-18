package com.mingquan.yuejian.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.agora.YueJianAppFUDualInputToTextureExampleActivity;
import com.mingquan.yuejian.auth.YueJianAppAuthActivity;
import com.mingquan.yuejian.auth.YueJianAppEditAuthInfoActivity;
import com.mingquan.yuejian.bean.YueJianAppSimpleBackPage;
import com.mingquan.yuejian.em.YueJianAppChangInfo;
import com.mingquan.yuejian.fragment.YueJianAppDiamondDialogFragment;
import com.mingquan.yuejian.interf.YueJianAppINomalDialog;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACVideoInfoModel;
import com.mingquan.yuejian.ui.YueJianAppEditInfoActivity;
import com.mingquan.yuejian.ui.YueJianAppLiveLoginSelectActivity;
import com.mingquan.yuejian.ui.YueJianAppMainActivity;
import com.mingquan.yuejian.ui.YueJianAppMyDiamondListActivity;
import com.mingquan.yuejian.ui.YueJianAppMyInfoDetailActivity;
import com.mingquan.yuejian.ui.YueJianAppPhoneLoginActivity;
import com.mingquan.yuejian.ui.YueJianAppSelectAvatarActivity;
import com.mingquan.yuejian.ui.YueJianAppSimpleBackActivity;
import com.mingquan.yuejian.ui.YueJianAppWebViewActivity;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.vchat.YueJianAppSetVideoPriceActivity;
import com.mingquan.yuejian.vchat.YueJianAppSettingBeautyActivity;
import com.mingquan.yuejian.vchat.YueJianAppUploadVideoActivity;
import com.mingquan.yuejian.vchat.YueJianAppVChatShortVideoPlayActivity;
import com.mingquan.yuejian.vchat.YueJianAppVChatShortVideoPlayerActivity;
import com.mingquan.yuejian.vchat.YueJianAppWeakDataHolder;

import java.util.List;

/**
 * 界面帮助类
 */

public class YueJianAppUIHelper {
    /**
     * 手机登录
     *
     * @param context
     */

    public static void showMobilLogin(Context context) {
        Intent intent = new Intent(context, YueJianAppPhoneLoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 登陆选择
     *
     * @param context
     */
    public static void showLoginSelectActivity(Context context) {
        if (context == null)
            return;
        Intent intent = new Intent(context, YueJianAppLiveLoginSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 首页
     *
     * @param context
     */
    public static void showMainActivity(Context context) {
        Intent intent = new Intent(context, YueJianAppMainActivity.class);
        //        Intent intent = new Intent(context, IndexActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 我的详细资料
     *
     * @param context
     */
    public static void showMyInfoDetailActivity(Context context) {
        Intent intent = new Intent(context, YueJianAppMyInfoDetailActivity.class);
        context.startActivity(intent);
    }

    /**
     * 编辑资料
     *
     * @param context
     */
    public static void showEditInfoActivity(YueJianAppMyInfoDetailActivity context, String action,
                                            String prompt, String defaultStr, YueJianAppChangInfo changInfo) {
        Intent intent = new Intent(context, YueJianAppEditInfoActivity.class);
        intent.putExtra(YueJianAppEditInfoActivity.EDITACTION, action);
        intent.putExtra(YueJianAppEditInfoActivity.EDITDEFAULT, defaultStr);
        intent.putExtra(YueJianAppEditInfoActivity.EDITPROMP, prompt);
        intent.putExtra(YueJianAppEditInfoActivity.EDITKEY, changInfo.getAction());
        context.startActivity(intent);
//    context.overridePendingTransition(R.anim.yue_jian_app_activity_open_start, 0);
    }

    public static void showSelectAvatar(YueJianAppMyInfoDetailActivity context, String avatar) {
        Intent intent = new Intent(context, YueJianAppSelectAvatarActivity.class);
        intent.putExtra("uhead", avatar);
        context.startActivity(intent);
//    context.overridePendingTransition(R.anim.yue_jian_app_activity_open_start, 0);
    }

    /**
     * 我的钻石
     *
     * @return
     */
    public static void showMyDiamonds(Context context, Bundle bundle) {
        Intent intent = new Intent(context, YueJianAppMyDiamondListActivity.class);
        intent.putExtra("USERINFO", bundle);
        context.startActivity(intent);
    }

    /**
     * 我的钻石DialogFragment
     *
     * @return
     */
    public static void showMyDiamondsDialogFragment(FragmentManager fragmentManager, Bundle bundle) {
        YueJianAppDiamondDialogFragment diamondDialogFragment = new YueJianAppDiamondDialogFragment();
        diamondDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        diamondDialogFragment.setArguments(bundle);
        diamondDialogFragment.show(fragmentManager, "YueJianAppDiamondDialogFragment");
    }

    /**
     * 视频聊天页面
     *
     * @param context
     * @param user
     * @param liveId  连麦的房间号
     */
    public static void showVideoChatActivity(
            final Context context,
            YueJianAppACUserPublicInfoModel user,
            String liveId) {
        boolean canVideoChat = YueJianAppAppContext.getInstance().getPrivateInfoModel().getCanVideoChat();
        if (!canVideoChat) {
            YueJianAppTLog.error("can video chat == false");
            return;
        }

        if (YueJianAppAppContext.getInstance().getLoginUid().equals(user.getUid())) {
            Toast.makeText(context, "不能跟自己连麦", Toast.LENGTH_SHORT).show();
            return;
        }

        if (YueJianAppStringUtil.isEmpty(liveId) && user.getUid().equals("120001")) { // 不能给客服打电话
            Toast.makeText(context, "不能跟客服连麦", Toast.LENGTH_SHORT).show();
            return;
        }

        // 自己不是主播，对方是主播，检查钻石是否足够
        if (!YueJianAppAppContext.getInstance().getAcPublicUser().getIsBroadcaster()
                && user.getIsBroadcaster()
                && YueJianAppAppContext.getInstance().getPrivateInfoModel().getDiamond() < user.getPrice()) {
            YueJianAppDialogHelp.showDialog(context, "钻石不足，是否立即充值", new YueJianAppINomalDialog() {
                @Override
                public void cancelDialog(View v, Dialog d) {
                    d.dismiss();
                }

                @Override
                public void determineDialog(View v, Dialog d) {
                    YueJianAppDialogHelper.showRechargeDialogFragment(((FragmentActivity) context).getSupportFragmentManager());
                    d.dismiss();
                }
            });
            return;
        }

        if (YueJianAppStringUtil.isEmpty(liveId)) {
            inviteVipChat(context, user);
        } else {
            fetchAgoraToken(context, user, false, liveId);
        }
    }

    /**
     * 发送邀请连麦
     */
    private static void inviteVipChat(
            final Context context,
            final YueJianAppACUserPublicInfoModel user) {
        YueJianAppApiProtoHelper.sendACInviteVipChatReq(
                null,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                user.getUid(),
                !YueJianAppAppConfig.DEBUG,
                new YueJianAppApiProtoHelper.ACInviteVipChatReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                        Toast.makeText(context, errMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String liveid) {
                        fetchAgoraToken(context, user, true, liveid);
                    }
                }
        );
    }

    /**
     * 获取声网token
     *
     * @param context
     * @param user
     * @param isSender
     * @param liveId
     */
    private static void fetchAgoraToken(
            final Context context,
            final YueJianAppACUserPublicInfoModel user,
            final boolean isSender,
            final String liveId) {
        YueJianAppApiProtoHelper.sendACFetchAgoraTokenReq(
                null,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                liveId,
                new YueJianAppApiProtoHelper.ACFetchAgoraTokenReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse(String token) {
                        Intent intent = new Intent(context, YueJianAppFUDualInputToTextureExampleActivity.class);
                        intent.putExtra("user_public_info", user);
                        intent.putExtra("agora_token", token);
                        intent.putExtra("is_sender", isSender);
                        intent.putExtra("live_id", liveId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                });
    }

    public static void showShortVideoActivity(Context context, YueJianAppACVideoInfoModel videoInfoModel) {
        Intent intent = new Intent(context, YueJianAppVChatShortVideoPlayActivity.class);
        intent.putExtra("videoInfo", videoInfoModel);
        context.startActivity(intent);
    }

    public static void showShortVideoPlayerActivity(Context context, List<YueJianAppACVideoInfoModel> videoInfoModels, int position) {
        YueJianAppWeakDataHolder.getInstance().saveData(videoInfoModels);
        Intent intent = new Intent(context, YueJianAppVChatShortVideoPlayerActivity.class);
        intent.putExtra("POSITION", position);
        context.startActivity(intent);
    }

    //私信详情
    public static void showPrivateChatdetailFragment(Context context, YueJianAppACUserPublicInfoModel user) {
        Intent intent = new Intent(context, YueJianAppSimpleBackActivity.class);
        intent.putExtra("user", user);
        intent.putExtra(YueJianAppSimpleBackActivity.BUNDLE_KEY_PAGE, YueJianAppSimpleBackPage.USER_PRIVATECORE_DETAIL.getValue());
        context.startActivity(intent);
    }

    //搜索
    public static void showSearch(Context context) {
        Intent intent = new Intent(context, YueJianAppSimpleBackActivity.class);
        intent.putExtra(YueJianAppSimpleBackActivity.BUNDLE_KEY_PAGE, YueJianAppSimpleBackPage.INDEX_SECREEN.getValue());
        context.startActivity(intent);
    }

    //打开网页
    public static void showWebView(Context context, String url, String title) {
        Intent intent = new Intent(context, YueJianAppWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("TITLE", title);
        intent.putExtra("URL_INFO", bundle);
        context.startActivity(intent);
    }

    /**
     * 我的钻石列表页面
     *
     * @param context
     */
    public static void showMyDiamondListActivity(Context context) {
        Intent intent = new Intent(context, YueJianAppMyDiamondListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 认证
     *
     * @param context
     */
    public static void showAuthActivity(Context context) {
        Intent intent = new Intent(context, YueJianAppAuthActivity.class);
        context.startActivity(intent);
    }

    /**
     * 编辑认证资料页面
     *
     * @param context
     */
    public static void showEditAuthinfoActivity(Context context) {
        Intent intent = new Intent(context, YueJianAppEditAuthInfoActivity.class);
        context.startActivity(intent);
    }

    /**
     * 上传头像页面
     *
     * @param context
     */
    public static void showUploadVideoActivity(Context context) {
        Intent intent = new Intent(context, YueJianAppUploadVideoActivity.class);
        context.startActivity(intent);
    }

    /**
     * 设置短视频价格页面
     *
     * @param context
     */
    public static void showSetVideoPriceActivity(Activity context, int videoId) {
        Intent intent = new Intent(context, YueJianAppSetVideoPriceActivity.class);
        intent.putExtra("VIDEO_ID", videoId);
        context.startActivityForResult(intent, 100);
    }

    /**
     * 设置美颜页面
     *
     * @param context
     */
    public static void showSettingBeautyActivity(Context context) {
        Intent intent = new Intent(context, YueJianAppSettingBeautyActivity.class);
        context.startActivity(intent);
    }
}
