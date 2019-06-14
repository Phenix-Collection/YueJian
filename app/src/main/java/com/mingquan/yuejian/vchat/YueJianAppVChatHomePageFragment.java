package com.mingquan.yuejian.vchat;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppExtensionAdapter;
import com.mingquan.yuejian.base.YueJianAppBaseFragment;
import com.mingquan.yuejian.interf.YueJianAppINomalDialog;
import com.mingquan.yuejian.layoutmanager.YueJianAppFullyGridLayoutManager;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPrivateInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.YueJianAppMainActivity;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppLoginUtils;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;
import com.mingquan.yuejian.utils.YueJianAppUtils;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by on 2018/8/31
 * <p>
 * VChat个人首页
 */

public class YueJianAppVChatHomePageFragment extends YueJianAppBaseFragment {

    @BindView(R.id.iv_edit)
    ImageView ivEdit; // 编辑按钮
    @BindView(R.id.iv_avatar)
    YueJianAppAvatarView ivAvatar; // 头像
    @BindView(R.id.tv_name)
    TextView tvName; // 昵称
    @BindView(R.id.rtv_level)
    YueJianAppRoundTextView rtvLevel; // 等级
    @BindView(R.id.rl_attention)
    RelativeLayout rlAttention;  // 关注
    @BindView(R.id.tv_attention_num)
    TextView tvAttentionNum; // 关注数量
    @BindView(R.id.rl_diamond)
    RelativeLayout rlDiamond; // 钻石
    @BindView(R.id.tv_diamond_num)
    TextView tvDiamondNum; // 钻石数量
    @BindView(R.id.ll_my_diamond)
    LinearLayout llMyDiamond; // 我的钱包
    @BindView(R.id.ll_my_price)
    LinearLayout llMyPrice; // 我的价格
    @BindView(R.id.ll_project)
    LinearLayout llProject; // 分成计划
    @BindView(R.id.ll_auth)
    LinearLayout llAuth; // 授权认证
    @BindView(R.id.ll_upload_video)
    LinearLayout llUploadVideo; // 上传视频
    @BindView(R.id.ll_collection)
    LinearLayout llCollection; //我的收藏
    @BindView(R.id.ll_not_disturb)
    LinearLayout llNotDisturb; // 勿扰
    @BindView(R.id.imgBtn_not_disturb)
    ImageButton btnNotDisturb; // 勿扰开关

    @BindView(R.id.ll_clear_cache)
    LinearLayout llClearCache; // 清理缓存
    @BindView(R.id.ll_safety)
    LinearLayout llSafety; // 账户安全
    @BindView(R.id.ll_logout)
    LinearLayout llLogout; // 退出登录
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.tv_my_price) // 聊天价格
            TextView tvMyPrice;

    @BindView(R.id.tvAuthTitle)
    TextView tvAuthTitle;
    @BindView(R.id.tvAuth)
    TextView tvAuth;
    @BindView(R.id.tv_build_id)
    TextView tvBuildId;
    @BindView(R.id.rv_extension)
    RecyclerView rvExtension;
    @BindView(R.id.iv_icon_vip)
    ImageView ivIconVip; // vip角标
    @BindView(R.id.ll_my_package)
    LinearLayout llMyPackage;
    @BindView(R.id.ll_my_call)
    LinearLayout llMyCall;
    @BindView(R.id.ll_my_time)
    LinearLayout llMyTime;
    @BindView(R.id.ll_invitation)
    LinearLayout llInvitation;
    @BindView(R.id.ll_appointment)
    LinearLayout llAppointment;
    @BindView(R.id.ll_beauty)
    LinearLayout llBeauty;
    private Activity mContext;
    private FragmentManager mFragmentManager;
    private YueJianAppACUserPublicInfoModel mUserPublicInfo;
    private YueJianAppACUserPrivateInfoModel mUserPrivateInfo;
    private Toast mToast;
    private YueJianAppExtensionAdapter extensionAdapter;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mFragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_vchat_home_page, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvBuildId.setText(YueJianAppTDevice.getVersionName());
        extensionAdapter = new YueJianAppExtensionAdapter(mContext);
        YueJianAppFullyGridLayoutManager manager = new YueJianAppFullyGridLayoutManager(mContext, 1, LinearLayoutManager.VERTICAL, false);
        rvExtension.setAdapter(extensionAdapter);
        rvExtension.setLayoutManager(manager);
        initView(view);
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
        ((YueJianAppMainActivity) mContext).showTabHost(); // 当MainActivity底部导航栏正在执行隐藏动画时，使其执行显示动画
        if (EventBus.getDefault().isRegistered(this)) {
            return;
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!EventBus.getDefault().isRegistered(this)) {
            return;
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(YueJianAppMessageEvent event) {
        if (event.getMessage().equals("REFRESH")) {
            requestData();
        }
    }

    /**
     * 获取用户信息
     */
    private void requestData() {
        YueJianAppApiProtoHelper.sendACGetUserPrivateInfoReq(
                mContext,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                new YueJianAppApiProtoHelper.ACGetUserPrivateInfoReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse(YueJianAppACUserPrivateInfoModel user) {
                        YueJianAppTLog.info("privateInfoModel:%s", user);
                        mUserPrivateInfo = user;
                        if (mUserPrivateInfo.getCanVideoChat()) {
                            YueJianAppUiUtils.setVisibility(rvExtension, View.VISIBLE);
                            YueJianAppUiUtils.setVisibility(llMyCall, View.VISIBLE);
                            YueJianAppUiUtils.setVisibility(llMyTime, View.VISIBLE);
                            YueJianAppUiUtils.setVisibility(llBeauty, View.VISIBLE);
                            YueJianAppUiUtils.setVisibility(llAuth, View.VISIBLE);
                        } else {
                            YueJianAppUiUtils.setVisibility(rvExtension, View.GONE);
                            YueJianAppUiUtils.setVisibility(llMyCall, View.GONE);
                            YueJianAppUiUtils.setVisibility(llMyTime, View.GONE);
                            YueJianAppUiUtils.setVisibility(llBeauty, View.GONE);
                            YueJianAppUiUtils.setVisibility(llAuth, View.GONE);
                        }
                        YueJianAppAppContext.getInstance().setPrivateInfo(mUserPrivateInfo);
                        YueJianAppApiProtoHelper.sendACGetUserPublicInfoReq(
                                mContext,
                                YueJianAppAppContext.getInstance().getLoginUid(),
                                new YueJianAppApiProtoHelper.ACGetUserPublicInfoReqCallback() {
                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        YueJianAppTLog.error("get public info error:%s", errMessage);
                                    }

                                    @Override
                                    public void onResponse(YueJianAppACUserPublicInfoModel user) {
                                        mUserPublicInfo = user;
                                        YueJianAppAppContext.getInstance().updateAcPublicUser(user);
                                        fillUI();
                                    }
                                });
                    }
                });
    }

    private void fillUI() {
        if (!YueJianAppStringUtil.isEmpty(mUserPrivateInfo.getFollowedUsersCount())) {
            tvAttentionNum.setText(mUserPrivateInfo.getFollowedUsersCount());
        }
        ivAvatar.setAvatarUrl(mUserPublicInfo.getAvatarUrl());
        YueJianAppUiUtils.setVisibility(ivIconVip, mUserPublicInfo.getVipTime() > System.currentTimeMillis() / 1000 ? View.VISIBLE : View.GONE);
        tvName.setText(mUserPublicInfo.getName());
        rtvLevel.setText(MessageFormat.format("LV.{0}", mUserPublicInfo.getLevel()));
        tvDiamondNum.setText(String.valueOf(mUserPrivateInfo.getDiamond()));
        tvUserId.setText(MessageFormat.format("ID:{0}", mUserPrivateInfo.getUid()));
        btnNotDisturb.setBackgroundResource(mUserPrivateInfo.getIsBusy() ? R.drawable.yue_jian_app_global_switch_on : R.drawable.yue_jian_app_global_switch_off);
        llMyPrice.setVisibility(View.GONE);
        llUploadVideo.setVisibility(View.GONE);
        switch (mUserPrivateInfo.getAuthStatus()) {
            case YueJianAppApiProtoHelper.AUTH_STATUS_NONE:
                tvAuth.setText("马上认证");
                break;
            case YueJianAppApiProtoHelper.AUTH_STATUS_WAITING:
                tvAuth.setText("审核中");
                break;
            case YueJianAppApiProtoHelper.AUTH_STATUS_REJECTED:
                tvAuth.setText("审核被拒绝");
                break;
            case YueJianAppApiProtoHelper.AUTH_STATUS_CERTIFIED:
                tvAuth.setText("修改资料");
                llUploadVideo.setVisibility(View.VISIBLE);
                llMyPrice.setVisibility(mUserPrivateInfo.getCanVideoChat() ? View.VISIBLE : View.GONE);
                tvMyPrice.setText(String.format("%s 钻石/分钟", mUserPrivateInfo.getPrice()));
                break;
        }
        extensionAdapter.setList(mUserPrivateInfo.getExtensions());
    }

    @OnClick({
            R.id.iv_edit,
            R.id.iv_avatar,
            R.id.rl_attention,
            R.id.rl_diamond,
            R.id.ll_my_package,
            R.id.ll_my_price,
            R.id.ll_my_diamond,
            R.id.ll_project,
            R.id.ll_my_call,
            R.id.ll_my_time,
            R.id.ll_invitation,
            R.id.ll_appointment,
            R.id.ll_auth,
            R.id.ll_upload_video,
            R.id.ll_beauty,
            R.id.ll_collection,
            R.id.ll_not_disturb,
            R.id.imgBtn_not_disturb,
            R.id.ll_clear_cache,
            R.id.ll_safety,
            R.id.ll_logout,
    })
    @Override
    public void onClick(View v) {
        if (YueJianAppUtils.isFastClick())
            return;
        final int id = v.getId();
        switch (id) {
            case R.id.iv_edit:
            case R.id.iv_avatar:
                if (mUserPrivateInfo == null) {
                    YueJianAppTLog.error("mUserPrivateInfo == null");
                    mToast.setText("正在加载数据，请稍后...");
                    mToast.show();
                    return;
                }
                if (mUserPrivateInfo.getAuthStatus() == YueJianAppApiProtoHelper.AUTH_STATUS_CERTIFIED) { // 主播跳转编辑认证资料页面
                    YueJianAppUIHelper.showEditAuthinfoActivity(mContext);
                } else { // 普通用户跳转编辑头像页面
                    YueJianAppUIHelper.showMyInfoDetailActivity(mContext);
                }
                break;
            case R.id.rl_attention: // 关注列表
                YueJianAppDialogHelper.showAttentionDialogFragment(mFragmentManager);
                break;
            case R.id.rl_diamond: // 充值
                YueJianAppDialogHelper.showRechargeDialogFragment(mFragmentManager);
                break;
            case R.id.ll_my_package: // 我的钱包
                if (mUserPrivateInfo == null) {
                    YueJianAppTLog.error("mUserPrivateInfo == null");
                    mToast.setText("正在加载数据，请稍后...");
                    mToast.show();
                    return;
                }
                YueJianAppDialogHelper.showMyPackageDialogFragment(mFragmentManager, mUserPrivateInfo.getDiamond(), mUserPrivateInfo.getTicket());
                break;
            case R.id.ll_my_price: // 我的价格
                if (mUserPrivateInfo == null) {
                    YueJianAppTLog.error("mUserPrivateInfo == null");
                    mToast.setText("正在加载数据，请稍后...");
                    mToast.show();
                    return;
                }
                YueJianAppDialogHelper.showMyCallPriceDialogFragment(mFragmentManager, mUserPrivateInfo.getPrice());
                break;
            case R.id.ll_my_diamond: // 我的钻石
                YueJianAppDialogHelper.showMyDiamondListDialogFragment(mFragmentManager);
                break;
            case R.id.ll_project: // 分成计划
                YueJianAppDialogHelper.showCommissionDialogFragment(mFragmentManager);
                break;
            case R.id.ll_my_call: // 我的通话
                YueJianAppDialogHelper.showMyCallDialogFragment(mFragmentManager);
                break;
            case R.id.ll_my_time: // 我的时长
                // TODO: 2018/12/19
                break;
            case R.id.ll_invitation: // 邀请
                YueJianAppUIHelper.showWebView(mContext, YueJianAppAppContext.getInstance().mWechatShareUrl, "邀请");
                break;
            case R.id.ll_appointment: // 预约
                YueJianAppDialogHelper.showAppointmentDialogFragment(mFragmentManager);
                break;
            case R.id.ll_auth: // 申请认证 成为大V
                if (mUserPrivateInfo == null) {
                    YueJianAppTLog.error("mUserPrivateInfo == null");
                    mToast.setText("正在加载数据，请稍后...");
                    mToast.show();
                    return;
                }

                switch (mUserPrivateInfo.getAuthStatus()) {
                    case YueJianAppApiProtoHelper.AUTH_STATUS_NONE:// 未提交审核
                    case YueJianAppApiProtoHelper.AUTH_STATUS_REJECTED:// 审核被拒绝
                        YueJianAppUIHelper.showAuthActivity(mContext);
                        break;
                    case YueJianAppApiProtoHelper.AUTH_STATUS_WAITING: // 审核中
                        mToast.setText("正在审核中。。。");
                        mToast.show();
                        break;
                    case YueJianAppApiProtoHelper.AUTH_STATUS_CERTIFIED: // 审核通过
                        YueJianAppUIHelper.showEditAuthinfoActivity(mContext);
                }
                break;
            case R.id.ll_upload_video: // 上传视频
                YueJianAppUIHelper.showUploadVideoActivity(mContext);
                break;
            case R.id.ll_beauty: // 设置美颜
                YueJianAppUIHelper.showSettingBeautyActivity(mContext);
                break;
            case R.id.ll_collection: // 我的收藏
                YueJianAppDialogHelper.showMyCollectionDialogFragment(mFragmentManager);
                break;
            case R.id.ll_not_disturb: // 勿扰
            case R.id.imgBtn_not_disturb: // 勿扰
                startOrClosePush();
                break;
            case R.id.ll_clear_cache: // 清理缓存
                llClearCache.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mToast.setText("清理完成！");
                        mToast.show();
                    }
                }, 1000);
                break;
            case R.id.ll_safety: // 账户安全
                if (mUserPrivateInfo == null) {
                    YueJianAppTLog.error("mUserPrivateInfo == null");
                    mToast.setText("正在加载数据，请稍后...");
                    mToast.show();
                    return;
                }
                if (YueJianAppStringUtil.isEmpty(mUserPrivateInfo.getMobile())) {
                    YueJianAppDialogHelper.showBindPhoneDialogFragment(mFragmentManager);
                } else {
                    mToast.setText("已经绑定手机号！");
                    mToast.show();
                    return;
                }
                break;
            case R.id.ll_logout: // 安全退出
                YueJianAppDialogHelp.showDialog(mContext, getString(R.string.determineloginout), new YueJianAppINomalDialog() {
                    @Override
                    public void cancelDialog(View v, Dialog d) {
                        d.dismiss();
                    }

                    @Override
                    public void determineDialog(View v, Dialog d) {
                        d.dismiss();
                        YueJianAppLoginUtils.outLogin(mContext);
                    }
                });
                break;
        }
    }

    private void startOrClosePush() {
        if (mUserPrivateInfo == null) {
            YueJianAppTLog.error("YueJianAppVChatHomePageFragment mUserPrivateInfo == null");
            return;
        }
        // 如果是认证主播,开启勿扰状态, 关闭通知状态,弹出开启通知的对话框
        if (mUserPrivateInfo.getAuthStatus() == YueJianAppApiProtoHelper.AUTH_STATUS_CERTIFIED
                && mUserPrivateInfo.getIsBusy()
                && !YueJianAppTDevice.isNotificationEnabled(mContext)
                ) {
            YueJianAppDialogHelp.showDialog(mContext, "打开通知，不要错过任何重要信息哦", new YueJianAppINomalDialog() {
                @Override
                public void cancelDialog(View v, Dialog d) {
                    d.dismiss();
                }

                @Override
                public void determineDialog(View v, Dialog d) {
                    YueJianAppTDevice.goToNotifySettings(mContext);
                    d.dismiss();
                }
            });
        }
        YueJianAppApiProtoHelper.sendACSetBroadcastModeReq(
                mContext,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                !mUserPrivateInfo.getIsBusy(),
                new YueJianAppApiProtoHelper.ACSetBroadcastModeReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        Toast.makeText(mContext, errMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse() {
                        mUserPrivateInfo.setIsBusy(!mUserPrivateInfo.getIsBusy());
                        btnNotDisturb.setBackgroundResource(mUserPrivateInfo.getIsBusy() ? R.drawable.yue_jian_app_global_switch_on : R.drawable.yue_jian_app_global_switch_off);

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
