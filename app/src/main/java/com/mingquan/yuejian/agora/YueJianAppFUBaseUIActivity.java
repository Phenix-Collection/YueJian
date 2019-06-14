package com.mingquan.yuejian.agora;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.interf.YueJianAppIBottomDialog;
import com.mingquan.yuejian.interf.YueJianAppINomalDialog;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACGiftModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppSharedPreUtil;
import com.mingquan.yuejian.utils.YueJianAppShowGiftManager;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base Acitivity, Handle events from FU or users
 */

public abstract class YueJianAppFUBaseUIActivity extends YueJianAppAgoraBaseActivity implements View.OnClickListener, View.OnTouchListener {

    protected RelativeLayout localContainer;
    protected RelativeLayout remoteContainer;
    protected GLSurfaceView localVideoView;
    protected SurfaceView remoteSurfaceView;
    private LinearLayout llRefuse; // 拒绝按钮布局
    private LinearLayout llVChatAccept; // 接受按钮布局
    private RelativeLayout rlVChatBG; // 连麦布局
    private YueJianAppAvatarView vChatAvatar; // 远程头像
    private ImageView iconVip; // vip图标
    private TextView tvVChatName; // 远程名字
    private TextView tvCameraStatus; // 摄像头状态
    private TextView tvCallTime; // 播放时长
    private TextView costUnit; // 通话价格
    private ImageView ivMask; // 小窗口蒙板
    private ImageView ivMysteryStatus; // 神秘隐身状态
    private TextView tvGift; // 送礼物按钮
    private LinearLayout giftTips; // 礼物提示区域
    private TextView tvBuildId; // app版本名
    private TextView tvTargetUid; // 对方uid
    ImageView ivOpenAudio; // 开启音频权限
    ImageView ivOpenCamera; // 开启摄像头权限
    private ImageView ivLoading; // 加载动画
    private AnimationDrawable animationDrawable; // 加载动画
    private boolean isConnecting = false; // 是否连麦中

    private YueJianAppACUserPublicInfoModel targetUserInfo; // 对方用户信息
    protected YueJianAppACUserPublicInfoModel mUserInfo; // 自己的用户信息
    private String agoraToken; // 声网加房间的token
    protected String mLiveId; // 房间号
    private BroadcastReceiver broadcastReceiver;
    private MediaPlayer mMediaPlayer;
    private YueJianAppShowGiftManager mShowGiftManager;
    private Toast mToast;

    private HashMap<View, int[]> mTouchPointMap = new HashMap<>();
    private ArrayList<YueJianAppACGiftModel> giftModels;
    RelativeLayout.LayoutParams layoutParamsBig;
    RelativeLayout.LayoutParams layoutParamsSmall;
    private boolean isSender; // 是否为呼叫方
    private boolean isExpend = false; // 是否为扣费方
    private boolean isHaveRemindRecharge = false; // 是否已经过提示充值
    private boolean isHaveRemindExit = false; // 是否已经过提示结束通话
    private boolean isWaitingCancel = false; // 是否等待重连socket后取消连麦
    private boolean isWaitingAccept = false; // 是否等待重连socket后接受连麦
    private boolean isWaitingDecline = false; // 是否等待重连socket后拒绝连麦

    protected boolean isRemoteJoined = false; // 判断对方是否进入房间了
    protected String mEffectFileName = YueJianAppEffectAndFilterSelectAdapter.EFFECT_ITEM_FILE_NAME[0];
    private boolean isNeedEffectItem = false; // 是否需要贴纸（马赛克）
    protected boolean setMysteryMode = true; // 设置神秘模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_base);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 0.7f;
        getWindow().setAttributes(params);
        layoutParamsBig = new RelativeLayout.LayoutParams((int) YueJianAppTDevice.getScreenWidth(), (int) YueJianAppTDevice.getScreenHeight());
        layoutParamsSmall = new RelativeLayout.LayoutParams((int) YueJianAppTDevice.getScreenWidth() / 5, (int) YueJianAppTDevice.getScreenHeight() / 5);
        layoutParamsSmall.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParamsSmall.setMargins(0, (int) getResources().getDimension(R.dimen.space_15), (int) getResources().getDimension(R.dimen.space_15), 0);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        isNeedEffectItem = YueJianAppSharedPreUtil.getBoolean(getApplicationContext(), "mosaic", false);
        mEffectFileName = isNeedEffectItem ? YueJianAppEffectAndFilterSelectAdapter.EFFECT_ITEM_FILE_NAME[1] : YueJianAppEffectAndFilterSelectAdapter.EFFECT_ITEM_FILE_NAME[0];
        Intent intent = getIntent();
        targetUserInfo = (YueJianAppACUserPublicInfoModel) intent.getSerializableExtra("user_public_info");
        agoraToken = intent.getStringExtra("agora_token");
        isSender = intent.getBooleanExtra("is_sender", true);
        mLiveId = intent.getStringExtra("live_id");
        mUserInfo = YueJianAppAppContext.getInstance().getAcPublicUser();
        localContainer = (RelativeLayout) findViewById(R.id.local_video_view_container);
        remoteContainer = (RelativeLayout) findViewById(R.id.remote_video_view_container);
        localVideoView = (GLSurfaceView) findViewById(R.id.local_video_view);
        llRefuse = (LinearLayout) findViewById(R.id.ll_refuse);
        llVChatAccept = (LinearLayout) findViewById(R.id.ll_vchat_accept);
        rlVChatBG = (RelativeLayout) findViewById(R.id.rl_vchat_bg);
        vChatAvatar = (YueJianAppAvatarView) findViewById(R.id.vchat_avatar);
        iconVip = findViewById(R.id.iv_icon_vip);
        tvCallTime = (TextView) findViewById(R.id.tv_call_time);
        tvVChatName = (TextView) findViewById(R.id.tv_vchat_name);
        tvCameraStatus = (TextView) findViewById(R.id.tv_camera_status);
        ivMask = (ImageView) findViewById(R.id.iv_mask);
        ivMysteryStatus = (ImageView) findViewById(R.id.iv_mystery_status);
        costUnit = (TextView) findViewById(R.id.cost_unit);
        tvGift = (TextView) findViewById(R.id.tv_gift);
        giftTips = (LinearLayout) findViewById(R.id.gift_tips);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        ivOpenCamera = (ImageView) findViewById(R.id.iv_open_camera);
        ivOpenAudio = (ImageView) findViewById(R.id.iv_open_audio);
        tvBuildId = (TextView) findViewById(R.id.tv_build_id);
        tvTargetUid = (TextView) findViewById(R.id.tv_target_uid);

        registerBroadcast();
        initView();
    }

    private void initView() {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append("每分钟支付");
        stringBuilder.append(String.valueOf(targetUserInfo.getPrice()));

        stringBuilder.append("钻石");
        stringBuilder.setSpan(
                new ForegroundColorSpan(YueJianAppAppContext.getInstance().getResources().getColor(R.color.color_FFAD37)),
                3,
                5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        costUnit.setText(stringBuilder);
        vChatAvatar.setAvatarUrl(targetUserInfo.getAvatarUrl());
        iconVip.setVisibility(targetUserInfo.getVipTime() > System.currentTimeMillis() / 1000 ? View.VISIBLE : View.GONE);
        tvVChatName.setText(targetUserInfo.getName());
        if (isSender) {
            YueJianAppUiUtils.setVisibility(llVChatAccept, View.GONE);
            YueJianAppUiUtils.setVisibility(rlVChatBG, View.VISIBLE);
            playSound("ring.mp3");
        } else {
            playSound("connecting.mp3");
        }
        mHandler.sendEmptyMessage(1);
        mShowGiftManager = new YueJianAppShowGiftManager(this, giftTips);
        setIsExpend();
        YueJianAppUiUtils.setVisibility(tvGift, isExpend ? View.VISIBLE : View.GONE);
        animationDrawable = (AnimationDrawable) ivLoading.getBackground();
        tvBuildId.setText(String.format("version:%s", YueJianAppTDevice.getVersionName()));
        tvTargetUid.setText(String.format("ID:%s", targetUserInfo.getUid()));

        if (mUserInfo.getVipTime() > System.currentTimeMillis() / 1000) {
            if (isNeedEffectItem) {
                ivMysteryStatus.setBackgroundResource(R.drawable.yue_jian_app_vchat_mystery_opened);
            } else {
                ivMysteryStatus.setBackgroundResource(R.drawable.yue_jian_app_vchat_mystery_unopen);
            }
        } else {
            if (isNeedEffectItem) {
                isNeedEffectItem = false;
                YueJianAppSharedPreUtil.put(getApplicationContext(), "mosaic", false);
            }
            ivMysteryStatus.setBackgroundResource(R.drawable.yue_jian_app_vchat_mystery_unopen);
        }
        if (mUserInfo.getIsBroadcaster()) { // 对主播隐藏神秘模式功能
            YueJianAppUiUtils.setVisibility(ivMysteryStatus, View.GONE);
        }
    }

    /**
     * 本地在前 远程放大
     */
    protected void setLocalTop() {
        YueJianAppTLog.info("远程全屏");
        remoteContainer.removeAllViews();
        localContainer.setLayoutParams(layoutParamsSmall);
        remoteContainer.setLayoutParams(layoutParamsBig);
        remoteSurfaceView.setZOrderOnTop(false);
        remoteSurfaceView.setZOrderMediaOverlay(false);
        localVideoView.setZOrderOnTop(true);
        localVideoView.setZOrderMediaOverlay(true);
        remoteContainer.addView(remoteSurfaceView);
        localVideoView.setClickable(true);
        remoteSurfaceView.setClickable(false);
    }

    /**
     * 远程在前 本地放大
     */
    protected void setRemoteTop() {
        YueJianAppTLog.info("本地全屏");
        remoteContainer.removeAllViews();
        localContainer.setLayoutParams(layoutParamsBig);
        remoteContainer.setLayoutParams(layoutParamsSmall);
        localVideoView.setZOrderOnTop(false);
        localVideoView.setZOrderMediaOverlay(false);
        remoteSurfaceView.setZOrderOnTop(true);
        remoteSurfaceView.setZOrderMediaOverlay(true);
        remoteContainer.addView(remoteSurfaceView);
        remoteSurfaceView.setClickable(true);
        localVideoView.setClickable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 获取礼物列表
        YueJianAppApiProtoHelper.sendACGetGiftListReq(this, getPackageName(), new YueJianAppApiProtoHelper.ACGetGiftListReqCallback() {
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

    /**
     * 拖动小窗口的触摸事件
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int last_X = (int) event.getRawX();
                int last_Y = (int) event.getRawY();
                mTouchPointMap.put(v, new int[]{last_X, last_Y});
                break;
            case MotionEvent.ACTION_MOVE:
                int[] lastPoint = mTouchPointMap.get(v);
                if (lastPoint != null) {
                    int dx = (int) event.getRawX() - lastPoint[0];
                    int dy = (int) event.getRawY() - lastPoint[1];

                    int left = (int) v.getX() + dx;
                    int top = (int) v.getY() + dy;
                    v.setX(left);
                    v.setY(top);
                    lastPoint[0] = (int) event.getRawX();
                    lastPoint[1] = (int) event.getRawY();

                    mTouchPointMap.put(v, lastPoint);
                    v.getParent().requestLayout();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close_camera: // 关闭摄像头
                v.setSelected(!v.isSelected());
                worker().closeCamera(v.isSelected());
                tvCameraStatus.setText(v.isSelected() ? "打开摄像头" : "关闭摄像头");
                tvCameraStatus.setTextColor(v.isSelected() ? getResources().getColor(R.color.vchat_ea4579) : getResources().getColor(R.color.white));
                localVideoView.setVisibility(v.isSelected() ? View.GONE : View.VISIBLE);
                break;
            case R.id.ll_switch_camera: // 切换摄像头
                onCameraChange();
                break;
            case R.id.ll_vchat_accept: // 接听
                YueJianAppTLog.info("接受VChat操作");
                YueJianAppApiProtoHelper.sendACAcceptVipChatReq(
                        YueJianAppFUBaseUIActivity.this,
                        YueJianAppAppContext.getInstance().getLoginUid(),
                        YueJianAppAppContext.getInstance().getToken(),
                        targetUserInfo.getUid(),
                        mLiveId,
                        new YueJianAppApiProtoHelper.ACAcceptVipChatReqCallback() {
                            @Override
                            public void onError(int errCode, String errMessage) {
                                YueJianAppTLog.error(errMessage);
                                mToast.setText(errMessage);
                                mToast.show();
                                finish();
                            }

                            @Override
                            public void onResponse() {
                                if (isConnecting) {
                                    return;
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        isConnecting = true;
                                        closeSound();
                                        worker().joinChannel(agoraToken, mLiveId, Integer.parseInt(YueJianAppAppContext.getInstance().getLoginUid()));
                                        YueJianAppUiUtils.setVisibility(rlVChatBG, View.GONE);
                                        if (mHandler != null) {
                                            mHandler.sendEmptyMessage(0);
                                        }
                                        remoteContainer.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!isRemoteJoined) {
                                                    sendQuitVipChat();
                                                }
                                            }
                                        }, 30000);
                                        YueJianAppUiUtils.setVisibility(llVChatAccept, View.GONE);
                                        YueJianAppUiUtils.setVisibility(llRefuse, View.GONE);
                                        YueJianAppUiUtils.setVisibility(rlVChatBG, View.GONE);
                                    }
                                });
                            }
                        });
                break;
            case R.id.ll_refuse: // 拒绝或取消
                YueJianAppTLog.info("拒绝VChat操作");
                cancelOrDecline();
                break;
            case R.id.ll_end_call: // 结束通话
                YueJianAppTLog.info("挂断操作");
                sendQuitVipChat();
                break;
            case R.id.tv_gift:
                if (giftModels == null) {
                    mToast.setText("礼物资源正在加载，请稍后。。。");
                    mToast.show();
                    return;
                }

                if (targetUserInfo == null) {
                    mToast.setText("用户信息正在加载中...");
                    mToast.show();
                    return;
                }

                YueJianAppDialogHelp.showGiftListDialog(this, giftModels, targetUserInfo, 0, new YueJianAppIBottomDialog() {
                    @Override
                    public void cancelDialog(Dialog d) {
                        d.dismiss();
                    }

                    @Override
                    public void determineDialog(Dialog d, Object... value) {
                        d.dismiss();
                        YueJianAppDialogHelper.showRechargeDialogFragment(getSupportFragmentManager());
                    }
                });
            case R.id.iv_open_audio:
                break;
            case R.id.iv_open_camera:
                break;
            case R.id.iv_mystery_status:
                if (mUserInfo.getVipTime() > System.currentTimeMillis() / 1000) {
                    isNeedEffectItem = !isNeedEffectItem;
                    YueJianAppSharedPreUtil.put(getApplicationContext(), "mosaic", isNeedEffectItem);
                    ivMysteryStatus.setBackgroundResource(isNeedEffectItem ?
                            R.drawable.yue_jian_app_vchat_mystery_opened :
                            R.drawable.yue_jian_app_vchat_mystery_unopen);
                    mEffectFileName = isNeedEffectItem ?
                            YueJianAppEffectAndFilterSelectAdapter.EFFECT_ITEM_FILE_NAME[1] :
                            YueJianAppEffectAndFilterSelectAdapter.EFFECT_ITEM_FILE_NAME[0];
                    setMysteryMode = true;
                } else {
                    mToast.setText("需要VIP特权，才能开启隐身模式");
                    mToast.show();
                }
                break;
        }
    }

    /**
     * 取消或拒绝接听
     */
    private void cancelOrDecline() {
        if (isSender) { // 连麦的发起者, 执行取消操作
            YueJianAppApiProtoHelper.sendACCancelVipChatReq(this,
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    targetUserInfo.getUid(),
                    mLiveId,
                    new YueJianAppApiProtoHelper.ACCancelVipChatReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            YueJianAppTLog.error(errMessage);
                            finish();
                        }

                        @Override
                        public void onResponse() {
                            String tips = String.format("您取消了与%s的连麦请求", targetUserInfo.getName());
                            mToast.setText(tips);
                            mToast.show();
                            finish();
                        }
                    });
        } else { // 连麦的接听者，执行拒绝接听的操作
            YueJianAppApiProtoHelper.sendACDeclineVipChatReq(
                    this,
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    targetUserInfo.getUid(),
                    mLiveId,
                    new YueJianAppApiProtoHelper.ACDeclineVipChatReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            YueJianAppTLog.error(errMessage);
                            finish();
                        }

                        @Override
                        public void onResponse() {
                            String tips = String.format("您拒绝了与%s的连麦请求", targetUserInfo.getName());
                            mToast.setText(tips);
                            mToast.show();
                            finish();
                        }
                    }
            );
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            YueJianAppDialogHelp.showDialog(
                    this,
                    "确定要退出视频聊天吗？",
                    new YueJianAppINomalDialog() {
                        @Override
                        public void cancelDialog(View v, Dialog d) {
                            d.dismiss();
                        }

                        @Override
                        public void determineDialog(View v, Dialog d) {
                            if (isConnecting) { // 正在连麦
                                sendQuitVipChat();
                            } else {
                                cancelOrDecline();
                            }
                            d.dismiss();
                        }
                    }
            );
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 发送挂断请求
     */
    protected void sendQuitVipChat() {
        YueJianAppTLog.info("end time %s", System.currentTimeMillis());
        if (mHandler != null) {
            mHandler.removeMessages(0);
            mHandler = null;
        }
        YueJianAppApiProtoHelper.sendACQuitVipChatReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                mLiveId,
                new YueJianAppApiProtoHelper.ACQuitVipChatReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                        finish();
                    }

                    @Override
                    public void onResponse(
                            final int broadcastSeconds,
                            final int costedDiamond,
                            final int experience,
                            final String consumerUid) {
                        worker().leaveChannel(mLiveId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (consumerUid.equals(YueJianAppAppContext.getInstance().getLoginUid())) {
                                    YueJianAppDialogHelp.showUserVChatResultDialog(
                                            getLayoutInflater(),
                                            YueJianAppFUBaseUIActivity.this,
                                            targetUserInfo,
                                            broadcastSeconds,
                                            costedDiamond,
                                            experience,
                                            new YueJianAppINomalDialog() {
                                                @Override
                                                public void cancelDialog(View v, Dialog d) {
                                                    d.dismiss();
                                                    finish();
                                                }

                                                @Override
                                                public void determineDialog(View v, Dialog d) {
                                                    d.dismiss();
                                                    finish();
                                                }
                                            });
                                } else {
                                    YueJianAppDialogHelp.showBroadcasterVChatResultDialog(
                                            getLayoutInflater(),
                                            YueJianAppFUBaseUIActivity.this,
                                            targetUserInfo,
                                            broadcastSeconds,
                                            costedDiamond,
                                            experience,
                                            new YueJianAppINomalDialog() {
                                                @Override
                                                public void cancelDialog(View v, Dialog d) {
                                                    d.dismiss();
                                                    finish();
                                                }

                                                @Override
                                                public void determineDialog(View v, Dialog d) {
                                                    d.dismiss();
                                                    finish();
                                                }
                                            });
                                }
                            }
                        });
                    }
                });
    }

    /**
     * 播放铃声
     *
     * @param fileName 文件名
     */
    public void playSound(String fileName) {
        YueJianAppTLog.info("播放铃声");
        fileName = "sounds/" + fileName;
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        try {
            if (!mMediaPlayer.isPlaying()) {
                AssetManager a = getAssets();
                a.open(fileName);

                AssetFileDescriptor assetFileDescriptor =
                        a.openFd(fileName);
                FileDescriptor fd = assetFileDescriptor.getFileDescriptor();
                mMediaPlayer.setDataSource(
                        fd, assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                mMediaPlayer.prepare();
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭铃声
     */
    private void closeSound() {
        YueJianAppTLog.info("停止播放铃声");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
    }

    /**
     * 注册广播接收者
     */
    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(YueJianAppAppConfig.ACTION_VIP_CHAT_START);
        intentFilter.addAction(YueJianAppAppConfig.ACTION_VIP_CHAT_RESULT);
        intentFilter.addAction(YueJianAppAppConfig.ACTION_VIP_CHAT_DECLINE);
        intentFilter.addAction(YueJianAppAppConfig.ACTION_VIP_CHAT_CANCEL);
        intentFilter.addAction(YueJianAppAppConfig.ACTION_VIP_CHAT_PING); // 验证socket连接成功
        intentFilter.addAction(YueJianAppAppConfig.ACTION_VIP_CHAT_CONNECT); // socket重连成功
        intentFilter.addAction(YueJianAppAppConfig.ACTION_SEND_GIFT);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (YueJianAppAppConfig.ACTION_VIP_CHAT_START.equals(intent.getAction())) { // 开始连麦
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isConnecting = true;
                            closeSound();
                            worker().joinChannel(agoraToken, mLiveId, Integer.parseInt(YueJianAppAppContext.getInstance().getLoginUid()));
                            YueJianAppUiUtils.setVisibility(rlVChatBG, View.GONE);
                            if (mHandler != null) {
                                mHandler.sendEmptyMessage(0);
                            }
                        }
                    });
                    YueJianAppUiUtils.setVisibility(llVChatAccept, View.GONE);
                    YueJianAppUiUtils.setVisibility(llRefuse, View.GONE);
                    YueJianAppUiUtils.setVisibility(rlVChatBG, View.GONE);
                } else if (YueJianAppAppConfig.ACTION_VIP_CHAT_RESULT.equals(intent.getAction())) { // 连麦结束
                    isConnecting = false;
                    worker().leaveChannel(mLiveId);

                    final int broadcastSeconds = intent.getIntExtra("BROADCAST_SECONDS", 0);
                    final int costedDiamond = intent.getIntExtra("COST_DIAMOND", 0);
                    final int experience = intent.getIntExtra("EXPERIENCE", 0);
                    final String consumerUid = intent.getStringExtra("CONSUMER_UID");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (consumerUid.equals(YueJianAppAppContext.getInstance().getLoginUid())) {
                                YueJianAppDialogHelp.showUserVChatResultDialog(
                                        getLayoutInflater(),
                                        YueJianAppFUBaseUIActivity.this,
                                        targetUserInfo,
                                        broadcastSeconds,
                                        costedDiamond,
                                        experience,
                                        new YueJianAppINomalDialog() {
                                            @Override
                                            public void cancelDialog(View v, Dialog d) {
                                                d.dismiss();
                                                finish();
                                            }

                                            @Override
                                            public void determineDialog(View v, Dialog d) {
                                                d.dismiss();
                                                finish();
                                            }
                                        });
                            } else {
                                YueJianAppDialogHelp.showBroadcasterVChatResultDialog(
                                        getLayoutInflater(),
                                        YueJianAppFUBaseUIActivity.this,
                                        targetUserInfo,
                                        broadcastSeconds,
                                        costedDiamond,
                                        experience,
                                        new YueJianAppINomalDialog() {
                                            @Override
                                            public void cancelDialog(View v, Dialog d) {
                                                d.dismiss();
                                                finish();
                                            }

                                            @Override
                                            public void determineDialog(View v, Dialog d) {
                                                d.dismiss();
                                                finish();
                                            }
                                        });
                            }
                        }
                    });
                } else if (YueJianAppAppConfig.ACTION_VIP_CHAT_DECLINE.equals(intent.getAction())) { // 接收者拒绝连麦
                    YueJianAppTLog.info("接收者拒绝连麦");
                    String tips;
                    if (isSender) { //我是邀请者
                        tips = String.format("%s拒绝了你的连麦请求", targetUserInfo.getName());
                    } else {
                        tips = String.format("您拒绝了%s的连麦请求", targetUserInfo.getName());
                    }
                    mToast.setText(tips);
                    mToast.show();
                    finish();
                } else if (YueJianAppAppConfig.ACTION_VIP_CHAT_CANCEL.equals(intent.getAction())) { // 发起者取消连麦
                    YueJianAppTLog.info("发起者取消连麦");
                    String tips;
                    if (isSender) { //我是邀请者
                        tips = String.format("您取消了与%s的连麦请求", targetUserInfo.getName());
                    } else {
                        tips = String.format("%s取消了与您的连麦请求", targetUserInfo.getName());
                    }
                    mToast.setText(tips);
                    mToast.show();
                    finish();
                } else if (YueJianAppAppConfig.ACTION_SEND_GIFT.equals(intent.getAction())) { // 显示礼物队列
                    YueJianAppTLog.info("显示礼物队列");
                    Bundle bundle = intent.getBundleExtra("GIFT_BUNDLE");
                    YueJianAppACGiftModel giftModel = (YueJianAppACGiftModel) bundle.getSerializable("GIFT_MODEL");
                    YueJianAppACUserPublicInfoModel senderModel = (YueJianAppACUserPublicInfoModel) bundle.getSerializable("SENDER_MODEL");
                    YueJianAppACUserPublicInfoModel receiverModel = (YueJianAppACUserPublicInfoModel) bundle.getSerializable("RECEIVER_MODEL");
                    mShowGiftManager.addGift(giftModel, senderModel, receiverModel);
                    mShowGiftManager.showGift();
                } else if (YueJianAppAppConfig.ACTION_VIP_CHAT_CONNECT.equals(intent.getAction())) {
                    if (isWaitingAccept) {
                        isWaitingAccept = false;
                        YueJianAppApiProtoHelper.sendACAcceptVipChatReq(
                                YueJianAppFUBaseUIActivity.this,
                                YueJianAppAppContext.getInstance().getLoginUid(),
                                YueJianAppAppContext.getInstance().getToken(),
                                targetUserInfo.getUid(),
                                mLiveId,
                                new YueJianAppApiProtoHelper.ACAcceptVipChatReqCallback() {
                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        YueJianAppTLog.error(errMessage);
                                        mToast.setText(errMessage);
                                        mToast.show();
                                    }

                                    @Override
                                    public void onResponse() {
                                    }
                                });
                    }

                    if (isWaitingCancel) {
                        isWaitingCancel = false;
                        YueJianAppApiProtoHelper.sendACCancelVipChatReq(YueJianAppFUBaseUIActivity.this,
                                YueJianAppAppContext.getInstance().getLoginUid(),
                                YueJianAppAppContext.getInstance().getToken(),
                                targetUserInfo.getUid(),
                                mLiveId,
                                new YueJianAppApiProtoHelper.ACCancelVipChatReqCallback() {
                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        YueJianAppTLog.error(errMessage);
                                        finish();
                                    }

                                    @Override
                                    public void onResponse() {
                                    }
                                });
                    }

                    if (isWaitingDecline) {
                        isWaitingDecline = false;
                        YueJianAppApiProtoHelper.sendACDeclineVipChatReq(
                                YueJianAppFUBaseUIActivity.this,
                                YueJianAppAppContext.getInstance().getLoginUid(),
                                YueJianAppAppContext.getInstance().getToken(),
                                targetUserInfo.getUid(),
                                mLiveId,
                                new YueJianAppApiProtoHelper.ACDeclineVipChatReqCallback() {
                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        YueJianAppTLog.error(errMessage);
                                        finish();
                                    }

                                    @Override
                                    public void onResponse() {

                                    }
                                }
                        );
                    }
                } else if (YueJianAppAppConfig.ACTION_VIP_CHAT_PING.equals(intent.getAction())) {
                    if (isWaitingAccept) {
                        isWaitingAccept = false;
                        YueJianAppApiProtoHelper.sendACAcceptVipChatReq(
                                YueJianAppFUBaseUIActivity.this,
                                YueJianAppAppContext.getInstance().getLoginUid(),
                                YueJianAppAppContext.getInstance().getToken(),
                                targetUserInfo.getUid(),
                                mLiveId,
                                new YueJianAppApiProtoHelper.ACAcceptVipChatReqCallback() {
                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        YueJianAppTLog.error(errMessage);
                                        mToast.setText(errMessage);
                                        mToast.show();
                                    }

                                    @Override
                                    public void onResponse() {
                                    }
                                });
                    }

                    if (isWaitingCancel) {
                        isWaitingCancel = false;
                        YueJianAppApiProtoHelper.sendACCancelVipChatReq(YueJianAppFUBaseUIActivity.this,
                                YueJianAppAppContext.getInstance().getLoginUid(),
                                YueJianAppAppContext.getInstance().getToken(),
                                targetUserInfo.getUid(),
                                mLiveId,
                                new YueJianAppApiProtoHelper.ACCancelVipChatReqCallback() {
                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        YueJianAppTLog.error(errMessage);
                                        finish();
                                    }

                                    @Override
                                    public void onResponse() {
                                    }
                                });
                    }

                    if (isWaitingDecline) {
                        isWaitingDecline = false;
                        YueJianAppApiProtoHelper.sendACDeclineVipChatReq(
                                YueJianAppFUBaseUIActivity.this,
                                YueJianAppAppContext.getInstance().getLoginUid(),
                                YueJianAppAppContext.getInstance().getToken(),
                                targetUserInfo.getUid(),
                                mLiveId,
                                new YueJianAppApiProtoHelper.ACDeclineVipChatReqCallback() {
                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        YueJianAppTLog.error(errMessage);
                                        finish();
                                    }

                                    @Override
                                    public void onResponse() {

                                    }
                                }
                        );
                    }
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private int seconds = 0; // 计时器
    private int fetchVipChatInfoTimes = 0; // 调用 api次数
    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: // 计时器
                    seconds += 1;
                    if (tvCallTime != null) {
                        tvCallTime.setText(YueJianAppStringUtil.getMinuteSecondBySecond(seconds));
                    }
                    checkDiamond();
                    if (null != mHandler) {
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
                case 1: // 循环调用getVipChatInfo api
                    fetchVipChatInfoTimes++;
                    if (fetchVipChatInfoTimes > 60 && mHandler != null) {
                        mHandler.removeMessages(1);
                        return;
                    }
                    YueJianAppApiProtoHelper.sendACGetVipChatInfoReq(
                            YueJianAppFUBaseUIActivity.this,
                            YueJianAppAppContext.getInstance().getLoginUid(),
                            YueJianAppAppContext.getInstance().getToken(),
                            mLiveId,
                            new YueJianAppApiProtoHelper.ACGetVipChatInfoReqCallback() {
                                @Override
                                public void onError(int errCode, String errMessage) {
                                    YueJianAppTLog.error(errMessage);
                                }

                                @Override
                                public void onResponse(int status, int broadcastSeconds, int costedDiamond, int experience, String consumerUid) {
                                    YueJianAppTLog.info("get vip chat info status:%s, seconds:%s, costDiamond:%s, experience:%s, consumerUid:%s",
                                            status,
                                            broadcastSeconds,
                                            costedDiamond,
                                            experience,
                                            consumerUid);
                                    if (status == YueJianAppApiProtoHelper.CALL_STATUS_CALLING) { // 开始连麦
                                        if (isConnecting) {
                                            return;
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                isConnecting = true;
                                                closeSound();
                                                worker().joinChannel(agoraToken, mLiveId, Integer.parseInt(YueJianAppAppContext.getInstance().getLoginUid()));
                                                YueJianAppUiUtils.setVisibility(rlVChatBG, View.GONE);
                                                if (mHandler != null) {
                                                    mHandler.sendEmptyMessage(0);
                                                }
                                                YueJianAppUiUtils.setVisibility(llVChatAccept, View.GONE);
                                                YueJianAppUiUtils.setVisibility(llRefuse, View.GONE);
                                                YueJianAppUiUtils.setVisibility(rlVChatBG, View.GONE);
                                                remoteContainer.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (!isRemoteJoined) {
                                                            sendQuitVipChat();
                                                        }
                                                    }
                                                }, 30000);
                                            }
                                        });
                                    } else if (status == YueJianAppApiProtoHelper.CALL_STATUS_DECLINE) { // 接听方拒绝接听
                                        if (isSender) {
                                            mToast.setText(String.format("%s拒绝了与您的连麦请求", targetUserInfo.getName()));
                                            mToast.show();
                                            finish();
                                        }
                                    } else if (status == YueJianAppApiProtoHelper.CALL_STATUS_CANCEL) { // 拨打方取消
                                        mToast.setText(String.format("%s取消了与您的连麦请求", targetUserInfo.getName()));
                                        mToast.show();
                                        finish();
                                    } else {
                                        if (mHandler != null) {
                                            mHandler.sendEmptyMessageDelayed(1, 1000);
                                        }
                                    }
                                }
                            });
                    break;
            }
        }
    };

    /**
     * 检查是否需要弹充值对话框
     * 或 退出房间
     */
    private void checkDiamond() {
        if (!isExpend) {
            return;
        }

        if (targetUserInfo == null) {
            return;
        }

        if (targetUserInfo.getPrice() == 0) {
            return;
        }
        // 当前钻石数
        int currDiamond = YueJianAppAppContext.getInstance().getPrivateInfoModel().getDiamond();
        // 可通话时长
        int canCallSeconds = currDiamond * 60 / targetUserInfo.getPrice() + 5 - seconds;
        if ((canCallSeconds <= 120) && !isHaveRemindRecharge) { // 钻石数不足通话两分钟且未提示时，提示去充值
            isHaveRemindRecharge = true;
            YueJianAppDialogHelp.showDialog(this, "当前钻石不多了，是否立即充值？", new YueJianAppINomalDialog() {
                @Override
                public void cancelDialog(View v, Dialog d) {
                    d.dismiss();
                }

                @Override
                public void determineDialog(View v, Dialog d) {
                    YueJianAppDialogHelper.showRechargeDialogFragment(getSupportFragmentManager());
                    d.dismiss();
                }
            });
        } else if (canCallSeconds > 180 && isHaveRemindRecharge) { // (充值后)钻石数大于可通话三分钟，且提示过
            isHaveRemindRecharge = false;
        }

        // 钻石不足
        if (canCallSeconds <= 0 && !isHaveRemindExit) {
            isHaveRemindExit = true;
            mToast.setText("钻石数量不足");
            mToast.show();
            sendQuitVipChat();
        }
    }

    /**
     * 判断是否为扣费方
     */
    private void setIsExpend() {
        if (mUserInfo.getIsBroadcaster()) { // 双方都是主播，呼叫方扣费
            if (isSender && targetUserInfo.getIsBroadcaster()) {
                isExpend = true;
            }
        } else {
            isExpend = true;
        }
    }

    /**
     * 显示加载动画
     */
    private void showLoadingAnim() {
        ivLoading.setVisibility(View.VISIBLE);
        animationDrawable.start();
    }

    /**
     * 隐藏加载动画
     */
    private void hideLoadingAnim() {
        ivLoading.setVisibility(View.GONE);
        animationDrawable.stop();
        ivLoading.clearAnimation();
    }

    /**
     * Camera switched
     */
    abstract protected void onCameraChange();

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        closeSound();
        if (mHandler != null) {
            mHandler.removeMessages(0);
            mHandler.removeMessages(1);
            mHandler = null;
        }
        unregisterReceiver(broadcastReceiver);
        worker().leaveChannel(mLiveId);
    }
}
