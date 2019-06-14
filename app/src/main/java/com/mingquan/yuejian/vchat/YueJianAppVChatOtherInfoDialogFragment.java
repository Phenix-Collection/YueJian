package com.mingquan.yuejian.vchat;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.widget.HeaderScrollHelper;
import com.lzy.widget.HeaderViewPager;
import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppFragmentViewPagerAdapter;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;
import com.mingquan.yuejian.fragment.YueJianAppShortVideoChildFragment;
import com.mingquan.yuejian.interf.YueJianAppIBottomDialog;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACGiftModel;
import com.mingquan.yuejian.proto.model.YueJianAppACRelationModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserHomepageInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.ui.dialog.YueJianAppEditableActionSheetDialog;
import com.mingquan.yuejian.ui.view.YueJianAppMyRatingBar;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppShowGiftManager;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.utils.YueJianAppUtils;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;
import com.mingquan.yuejian.widget.YueJianAppSlideShowView;
import com.mingquan.yuejian.widget.YueJianAppStatusTextView;
import com.ruffian.library.RVPIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/22.
 */

public class YueJianAppVChatOtherInfoDialogFragment extends YueJianAppBaseDialogFragment {

    @BindView(R.id.index_banner)
    YueJianAppSlideShowView indexBanner; // 轮播图
    @BindView(R.id.tv_status)
    YueJianAppStatusTextView tvStatus; // 状态
    @BindView(R.id.tv_user_id)
    TextView tvUserId; // 用户id
    @BindView(R.id.tv_name)
    TextView tvName; // 主播名
    @BindView(R.id.rating_bar)
    YueJianAppMyRatingBar mRatingBar; // 星星
    @BindView(R.id.tv_fans_num)
    TextView tvFansNum; //粉丝数
    @BindView(R.id.tv_follow)
    TextView tvFollow; // 关注按钮
    @BindView(R.id.tv_signature)
    TextView tvSignature; // 签名
    @BindView(R.id.tv_expend)
    TextView tvExpend; // 费用
    @BindView(R.id.avatar_1)
    YueJianAppAvatarView avatar1;
    @BindView(R.id.avatar_2)
    YueJianAppAvatarView avatar2; //
    @BindView(R.id.avatar_3)
    YueJianAppAvatarView avatar3;
    @BindView(R.id.iv_avatar_more)
    ImageView ivAvatarMore; // 头像 更多按钮
    @BindView(R.id.gift_1)
    YueJianAppAvatarView gift1;
    @BindView(R.id.gift_2)
    YueJianAppAvatarView gift2;
    @BindView(R.id.gift_3)
    YueJianAppAvatarView gift3;
    @BindView(R.id.iv_gift_more)
    ImageView ivGiftMore; // 礼物更多按钮
    @BindView(R.id.indicator)
    RVPIndicator indicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.scrollable_layout)
    HeaderViewPager scrollableLayout; // 可滑动布局
    @BindView(R.id.iv_back)
    ImageView ivBack; // 返回按钮
    @BindView(R.id.tv_title)
    TextView tvTitle; // 顶部标题
    @BindView(R.id.top_bar_bg)
    View topBarBg;
    @BindView(R.id.tv_message)
    TextView tvMessage; // 发送私信按钮
    @BindView(R.id.tv_gift)
    TextView tvGift; // 送礼物按钮
    @BindView(R.id.iv_to_video)
    ImageView ivToVideo; // 申请视频连麦按钮
    @BindView(R.id.layout_top)
    LinearLayout layoutTop;
    @BindView(R.id.gift_tips)
    LinearLayout giftTips;
    @BindView(R.id.iv_loading)
    ImageView mIvLoading;
    @BindView(R.id.ll_expend)
    LinearLayout llExpend;
    Unbinder unbinder;

    private boolean isFollow = false; // 是否关注主播
    private String uid;
    private YueJianAppACUserHomepageInfoModel homepageInfoModel;
    private YueJianAppACUserPublicInfoModel publicInfoModel;
    private YueJianAppACRelationModel relationModel;
    private List<YueJianAppACUserPublicInfoModel> top3;
    private ArrayList<YueJianAppACGiftModel> giftModels;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    protected BroadcastReceiver broadcastReceiver = null;
    private Context mContext;
    private YueJianAppShowGiftManager mShowGiftManager;
    private Toast mToast;
    private AnimationDrawable animationDrawable;
    private YueJianAppVChatUserDataFragment userDataFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_vchat_other_info_dialog, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uid = getArguments().getString("UID");
        initView();
        initData();
        initEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver();
    }

    private void initView() {
        mContext = getActivity();
        topBarBg.setAlpha(0);
        tvTitle.setAlpha(0);
        boolean canVideoChat = YueJianAppAppContext.getInstance().getCanVideoChat();
        ivToVideo.setVisibility(canVideoChat ? View.VISIBLE : View.GONE);
        llExpend.setVisibility(canVideoChat ? View.VISIBLE : View.GONE);
        tvStatus.setVisibility(canVideoChat ? View.VISIBLE : View.GONE);
        mShowGiftManager = new YueJianAppShowGiftManager(mContext, giftTips);
        if (mTitles == null) {
            mTitles = new ArrayList<>();
            mTitles.add("资料");
            mTitles.add("视频");
        }

        indicator.setTitleList(mTitles);
        indicator.setViewPager(viewPager, 0);

        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        animationDrawable = (AnimationDrawable) mIvLoading.getBackground();
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            userDataFragment = YueJianAppVChatUserDataFragment.newInstance();
            mFragments.add(userDataFragment);
            mFragments.add(YueJianAppShortVideoChildFragment.newInstance(0, uid));
        }
        scrollableLayout.setCurrentScrollableContainer((HeaderScrollHelper.ScrollableContainer) mFragments.get(0));
        viewPager.setAdapter(new YueJianAppFragmentViewPagerAdapter(getChildFragmentManager(), mFragments));
    }

    /**
     * 广播接收者
     */
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(YueJianAppAppConfig.ACTION_CLOSE_ALL_DIALOG_FRAGMENT);
        intentFilter.addAction(YueJianAppAppConfig.ACTION_CLOSE_OTHER_INFO_DIALOG_FRAGMENT);
        intentFilter.addAction(YueJianAppAppConfig.ACTION_SEND_GIFT);
        intentFilter.addAction(YueJianAppAppConfig.ACTION_VIP_CHAT_CONNECT);
        intentFilter.addAction(YueJianAppAppConfig.ACTION_VIP_CHAT_PING);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(YueJianAppAppConfig.ACTION_CLOSE_ALL_DIALOG_FRAGMENT)
                        || intent.getAction().equals(YueJianAppAppConfig.ACTION_CLOSE_OTHER_INFO_DIALOG_FRAGMENT)) {
                    dismissAllowingStateLoss();
                } else if (intent.getAction().equals(YueJianAppAppConfig.ACTION_SEND_GIFT)) {
                    Bundle bundle = intent.getBundleExtra("GIFT_BUNDLE");
                    YueJianAppACGiftModel giftModel = (YueJianAppACGiftModel) bundle.getSerializable("GIFT_MODEL");
                    YueJianAppACUserPublicInfoModel senderModel = (YueJianAppACUserPublicInfoModel) bundle.getSerializable("SENDER_MODEL");
                    YueJianAppACUserPublicInfoModel receiverModel = (YueJianAppACUserPublicInfoModel) bundle.getSerializable("RECEIVER_MODEL");
                    mShowGiftManager.addGift(giftModel, senderModel, receiverModel);
                    mShowGiftManager.showGift();
                } else if (
                        intent.getAction().equals(YueJianAppAppConfig.ACTION_VIP_CHAT_PING) ||
                                intent.getAction().equals(YueJianAppAppConfig.ACTION_VIP_CHAT_CONNECT)) {
                    hideLoadingAnim();
                    YueJianAppUIHelper.showVideoChatActivity(mContext, publicInfoModel, "");
                }
            }
        };
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    private void initData() {
        // 获取用户信息
        YueJianAppApiProtoHelper.sendACGetUserHomepageReq(
                (Activity) mContext,
                YueJianAppAppContext.getInstance().getLoginUid(),
                uid,
                new YueJianAppApiProtoHelper.ACGetUserHomepageReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error("sendACGetUserHomepageReq:%s", errMessage);
                    }

                    @Override
                    public void onResponse(YueJianAppACUserHomepageInfoModel user) {
                        homepageInfoModel = user;
                        publicInfoModel = homepageInfoModel.getUser();
                        relationModel = homepageInfoModel.getRelation();
                        top3 = homepageInfoModel.getTop3();
                        fillUI();
                    }
                });

        // 获取礼物列表
        YueJianAppApiProtoHelper.sendACGetGiftListReq(
                (Activity) mContext,
                mContext.getPackageName(),
                new YueJianAppApiProtoHelper.ACGetGiftListReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error("sendACGetGiftListReq:%s", errMessage);
                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACGiftModel> gifts) {
                        giftModels = gifts;
                    }
                });
    }

    private void fillUI() {
        indexBanner.setImageList(publicInfoModel.getShowPictures());
        indexBanner.setIsAutoPlay(true);
        indexBanner.startPlay();
        tvSignature.setText(publicInfoModel.getIntroduction());
        tvName.setText(publicInfoModel.getName());
        tvTitle.setText(publicInfoModel.getName());
        tvStatus.setStatus(publicInfoModel.getStatus(), publicInfoModel.getStatusTag());
        tvUserId.setText(String.format("ID:%s", publicInfoModel.getUid()));
        mRatingBar.setStar(publicInfoModel.getStar());
        tvExpend.setText(String.valueOf(publicInfoModel.getPrice()));
        tvFansNum.setText(String.format("%s 粉丝", publicInfoModel.getFollowerCount()));
        isFollow = relationModel.getIsFollowTarget();
        tvFollow.setBackgroundResource(isFollow ? R.drawable.yue_jian_app_icon_04009 : R.drawable.yue_jian_app_icon_04003);
        for (int i = 0; i < top3.size(); i++) {
            switch (i) {
                case 0:
                    avatar1.setAvatarUrl(top3.get(i).getAvatarUrl());
                    break;
                case 1:
                    avatar2.setAvatarUrl(top3.get(i).getAvatarUrl());
                    break;
                case 2:
                    avatar3.setAvatarUrl(top3.get(i).getAvatarUrl());
                    break;
            }
        }

        userDataFragment.setData(publicInfoModel);
    }

    private void initEvents() {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.setCurrentScrollableContainer((HeaderScrollHelper.ScrollableContainer) mFragments.get(position));
            }
        });
        scrollableLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                float alpha = 1.0f * currentY / maxY;
                topBarBg.setAlpha(alpha);
                tvTitle.setAlpha(alpha);
                ivBack.setImageResource(alpha > 0.5 ? R.drawable.yue_jian_app_btn_back_gray : R.drawable.yue_jian_app_btn_back);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mContext.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_back, R.id.tv_follow, R.id.iv_to_video, R.id.tv_message, R.id.tv_gift, R.id.iv_menu, R.id.iv_hi})
    public void onClick(final View view) {
        if (YueJianAppUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_back:
                this.dismiss();
                break;
            case R.id.tv_follow:
                followBroadCast(view, isFollow);
                break;
            case R.id.iv_to_video:
                if (publicInfoModel == null) {
                    mToast.setText("用户信息正在加载中...");
                    mToast.show();
                    return;
                }
                YueJianAppUIHelper.showVideoChatActivity(mContext, publicInfoModel, "");
//                YueJianAppAppContext.getInstance().checkSocket();
//                showLoadingAnim();
                break;
            case R.id.tv_message:
                if (publicInfoModel == null) {
                    mToast.setText("用户信息正在加载中...");
                    mToast.show();
                    return;
                }
                YueJianAppUIHelper.showPrivateChatdetailFragment(mContext, publicInfoModel);
                break;
            case R.id.tv_gift: // 送礼物

//                mContext.startActivity(new Intent(mContext, VoiceChatViewActivity.class));
                if (giftModels == null) {
                    mToast.setText("礼物资源正在加载，请稍后。。。");
                    mToast.show();
                    return;
                }

                if (publicInfoModel == null) {
                    mToast.setText("用户信息正在加载中...");
                    mToast.show();
                    return;
                }

                YueJianAppDialogHelp.showGiftListDialog(
                        (Activity) mContext,
                        giftModels,
                        publicInfoModel,
                        0,
                        new YueJianAppIBottomDialog() {
                            @Override
                            public void cancelDialog(Dialog d) {
                                d.dismiss();
                            }

                            @Override
                            public void determineDialog(Dialog d, Object... value) {
                                d.dismiss();
                                YueJianAppDialogHelper.showRechargeDialogFragment(getActivity().getSupportFragmentManager());
                            }
                        });
                break;
            case R.id.iv_menu: // 菜单
                showMenu();
                break;
            case R.id.iv_hi: // 打招呼
                break;
        }
    }

    /**
     * 关注或取消关注
     *
     * @param btnFollow   关注按钮
     * @param isFollowing 是否正在关注
     */
    private void followBroadCast(final View btnFollow, final boolean isFollowing) {
        btnFollow.setEnabled(false);
        if (isFollowing) { // 已关注，取消关注
            YueJianAppApiProtoHelper.sendACUnfollowReq(
                    (Activity) mContext,
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    publicInfoModel.getUid(),
                    new YueJianAppApiProtoHelper.ACUnfollowReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            btnFollow.setEnabled(true);
                        }

                        @Override
                        public void onResponse() {
                            mToast.setText("已取消关注");
                            mToast.show();
                            btnFollow.setEnabled(true);
                            btnFollow.setBackgroundResource(R.drawable.yue_jian_app_icon_04003);
                            isFollow = false;
                            publicInfoModel.setFollowerCount(publicInfoModel.getFollowerCount() - 1);
                            tvFansNum.setText(String.format("%s 粉丝", publicInfoModel.getFollowerCount()));
                        }
                    });
        } else { // 未关注， 关注
            YueJianAppApiProtoHelper.sendACFollowReq(
                    (Activity) mContext,
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    publicInfoModel.getUid(),
                    new YueJianAppApiProtoHelper.ACFollowReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            btnFollow.setEnabled(true);
                        }

                        @Override
                        public void onResponse() {
                            btnFollow.setEnabled(true);
                            btnFollow.setBackgroundResource(R.drawable.yue_jian_app_icon_04009);
                            mToast.setText("已关注");
                            mToast.show();
                            isFollow = true;
                            publicInfoModel.setFollowerCount(publicInfoModel.getFollowerCount() + 1);
                            tvFansNum.setText(String.format("%s 粉丝", publicInfoModel.getFollowerCount()));
                        }
                    });
        }
    }

    private void showMenu() {
        final YueJianAppEditableActionSheetDialog mDialog = new YueJianAppEditableActionSheetDialog(mContext).builder();
        TextView mShareTextView = new TextView(mContext);
        mDialog.addSheetItem(mShareTextView, "分享", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        YueJianAppDialogHelp.showShareBroadCastDialog((Activity) mContext);
                        mDialog.cancel();
                    }
                });
        mDialog.show();
    }

    /**
     * 显示加载动画
     */
    private void showLoadingAnim() {
        mIvLoading.setVisibility(View.VISIBLE);
        animationDrawable.start();
    }

    /**
     * 隐藏加载动画
     */
    private void hideLoadingAnim() {
        mIvLoading.setVisibility(View.GONE);
        animationDrawable.stop();
        mIvLoading.clearAnimation();
    }
}
