package com.mingquan.yuejian.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.fragment.YueJianAppDiamondDialogFragment;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.vchat.YueJianAppAttentionDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppMyCallPriceDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppMyCollectionDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppMyInOutListDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppMyPackageDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppVChatAppointmentDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppVChatBindPhoneDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppVChatCommissionDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppVChatGiftContributionDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppVChatMessageDetailDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppVChatMyCallsDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppVChatOtherInfoDialogFragment;
import com.mingquan.yuejian.vchat.YueJianAppVChatUserTagDialogFragment;

/**
 * Created by administrato on 2016/10/27.
 */

public class YueJianAppDialogHelper {
    /**
     * 显示他人信息页面
     * Adair
     *
     * @param uid
     * @param manager
     */
    public static void showOtherInfoDialogFragment(int uid, FragmentManager manager) {

    }

    /**
     * 显示他人信息页面
     *
     * @param uid
     * @param manager
     */
    public static void showOtherInfoDialogFragment(String uid, FragmentManager manager) {

    }

    /***
     * 显示关注页面
     *
     * @param manager
     */
    public static void showAttentionDialogFragment(FragmentManager manager) {
        YueJianAppAttentionDialogFragment attentionDialog = new YueJianAppAttentionDialogFragment();
        attentionDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        attentionDialog.show(manager, "YueJianAppAttentionDialogFragment");
    }

    /**
     * 收支明细列表
     *
     * @param manager
     */
    public static void showMyDiamondListDialogFragment(FragmentManager manager) {
        YueJianAppMyInOutListDialogFragment dialogFragment = new YueJianAppMyInOutListDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        dialogFragment.show(manager, "YueJianAppMyInOutListDialogFragment");
    }

    /**
     * 充值
     *
     * @return
     */
    public static void showRechargeDialogFragment(FragmentManager fragmentManager) {
        YueJianAppDiamondDialogFragment diamondDialogFragment = new YueJianAppDiamondDialogFragment();
        diamondDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        diamondDialogFragment.show(fragmentManager, "YueJianAppDiamondDialogFragment");
    }

    /**
     * 我的钱包
     *
     * @param manager
     */
    public static void showMyPackageDialogFragment(FragmentManager manager, int diamond, int ticket) {
        YueJianAppMyPackageDialogFragment dialogFragment = new YueJianAppMyPackageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("DIAMOND", diamond);
        bundle.putInt("TICKET", ticket);
        dialogFragment.setArguments(bundle);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        dialogFragment.show(manager, "YueJianAppMyPackageDialogFragment");
    }

    /**
     * 我的通话
     *
     * @param manager
     */
    public static void showMyCallDialogFragment(FragmentManager manager) {
        YueJianAppVChatMyCallsDialogFragment dialogFragment = new YueJianAppVChatMyCallsDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        dialogFragment.show(manager, "YueJianAppVChatMyCallsDialogFragment");
    }

    /**
     * 我的时长
     *
     * @param manager
     */
    public static void showMyTimeDialogFragment(FragmentManager manager) {
        YueJianAppVChatMyCallsDialogFragment dialogFragment = new YueJianAppVChatMyCallsDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        dialogFragment.show(manager, "YueJianAppVChatMyCallsDialogFragment");
    }

    /**
     * 分成计划
     *
     * @param manager
     */
    public static void showCommissionDialogFragment(FragmentManager manager) {
        YueJianAppVChatCommissionDialogFragment dialogFragment = new YueJianAppVChatCommissionDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        dialogFragment.show(manager, "YueJianAppVChatCommissionDialogFragment");
    }

    /**
     * 预约
     *
     * @param manager
     */
    public static void showAppointmentDialogFragment(FragmentManager manager) {
        YueJianAppVChatAppointmentDialogFragment dialogFragment = new YueJianAppVChatAppointmentDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        dialogFragment.show(manager, "YueJianAppVChatAppointmentDialogFragment");
    }

    /**
     * 视频礼物榜
     *
     * @param manager
     */
    public static void showGiftContributionDialogFragment(FragmentManager manager) {
        YueJianAppVChatGiftContributionDialogFragment dialogFragment = new YueJianAppVChatGiftContributionDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        dialogFragment.show(manager, "YueJianAppVChatGiftContributionDialogFragment");
    }

    /**
     * 我的收藏
     *
     * @param manager
     */
    public static void showMyCollectionDialogFragment(FragmentManager manager) {
        YueJianAppMyCollectionDialogFragment dialogFragment = new YueJianAppMyCollectionDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        dialogFragment.show(manager, "YueJianAppMyCollectionDialogFragment");
    }

    /**
     * 绑定手机
     *
     * @param manager
     */
    public static void showBindPhoneDialogFragment(FragmentManager manager) {
        YueJianAppVChatBindPhoneDialogFragment dialogFragment = new YueJianAppVChatBindPhoneDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        dialogFragment.show(manager, "YueJianAppVChatBindPhoneDialogFragment");
    }

    /**
     * 显示V聊详情页
     *
     * @param manager
     */
    public static void showVchatOtherInfoDialogFragment(FragmentManager manager, String uid) {
        YueJianAppVChatOtherInfoDialogFragment vChatOtherInfoDialogFragment = new YueJianAppVChatOtherInfoDialogFragment();
        vChatOtherInfoDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        Bundle bundle = new Bundle();
        bundle.putString("UID", uid);
        vChatOtherInfoDialogFragment.setArguments(bundle);
        vChatOtherInfoDialogFragment.show(manager, "YueJianAppVChatOtherInfoDialogFragment");
    }

    /**
     * 显示私信详情页
     *
     * @param manager
     */
    public static void showMessageDetailDialogFragment(FragmentManager manager, YueJianAppACUserPublicInfoModel user) {
        YueJianAppVChatMessageDetailDialogFragment vChatMessageDetailDialogFragment = new YueJianAppVChatMessageDetailDialogFragment();
        vChatMessageDetailDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        Bundle bundle = new Bundle();
        bundle.putSerializable("TARGET_USER", user);
        vChatMessageDetailDialogFragment.setArguments(bundle);
        vChatMessageDetailDialogFragment.show(manager, "YueJianAppVChatMessageDetailDialogFragment");
    }

    /**
     * 显示用户标签页面
     *
     * @param manager
     */
    public static void showUserTagDialogFragment(FragmentManager manager, String targer_uid) {
        YueJianAppVChatUserTagDialogFragment vChatUserTagDialogFragment = new YueJianAppVChatUserTagDialogFragment();
        vChatUserTagDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        Bundle bundle = new Bundle();
        bundle.putString("TARGET_UID", targer_uid);
        vChatUserTagDialogFragment.setArguments(bundle);
        vChatUserTagDialogFragment.show(manager, "YueJianAppVChatUserTagDialogFragment");
    }

    /**
     * 设置视频聊天计费单价页面
     *
     * @param price
     */
    public static void showMyCallPriceDialogFragment(FragmentManager manager, int price) {
        YueJianAppMyCallPriceDialogFragment dialogFragment = new YueJianAppMyCallPriceDialogFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
        Bundle bundle = new Bundle();
        bundle.putInt("PRICE", price);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(manager, "YueJianAppMyCallPriceDialogFragment");
    }
}
