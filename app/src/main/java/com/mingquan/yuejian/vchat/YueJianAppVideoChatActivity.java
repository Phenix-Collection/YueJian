package com.mingquan.yuejian.vchat;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.interf.YueJianAppINomalDialog;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;

import java.io.FileDescriptor;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * 视频聊天页面
 */
public class YueJianAppVideoChatActivity extends YueJianAppFullScreenModeActivity {

    String chatServerUrl = "http://139.224.18.21:12301";
    @BindView(R.id.remote_video_view_container)//远程video
            FrameLayout remoteVideoViewContainer;
    @BindView(R.id.local_video_view_container)//本地video
            FrameLayout localVideoViewContainer;
    @BindView(R.id.ll_close_camera)//关闭摄像头按钮布局
            LinearLayout llCloseCamera;
    @BindView(R.id.tv_call_time)//播放时长
            TextView tvCallTime;
    @BindView(R.id.ll_end_call)//结束按钮布局
            LinearLayout llEndCall;
    @BindView(R.id.ll_switch_camera)//切换摄像头按钮布局
            LinearLayout llSwitchCamera;
    @BindView(R.id.rl_vchat_bg)//连麦布局
            RelativeLayout rlVChatBG;
    @BindView(R.id.vchat_avatar)//远程头像
            YueJianAppAvatarView vchatAvatar;
    @BindView(R.id.tv_vchat_name)//远程名字
            TextView tvVchatName;
    @BindView(R.id.ll_refuse)//拒绝按钮布局
            LinearLayout llRefuse;
    @BindView(R.id.ll_vchat_accept)//接受按钮布局
            LinearLayout llVchatAccept;
    @BindView(R.id.cost_unit)
    TextView mCostUnit; // 通话价格
    private Unbinder unbinder;

    private boolean isSend = false; // 是否要发送邀请通知
    private YueJianAppACUserPublicInfoModel userInfo; // 对方用户信息
    private String mLiveId; // 房间号
    private BroadcastReceiver broadcastReceiver;

    private RtcEngine mRtcEngine;// Tutorial Step 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_video_chat);
        unbinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        userInfo = (YueJianAppACUserPublicInfoModel) intent.getSerializableExtra("user_public_info");
        isSend = intent.getBooleanExtra("is_send", false);
        mLiveId = intent.getStringExtra("live_id");
        registerBroadcast();
        initData();
        initAgoraEngine();
    }

    private void initData() {
        mCostUnit.setText("每分钟支付" + userInfo.getPrice() + getString(R.string.unit));
        vchatAvatar.setAvatarUrl(userInfo.getAvatarUrl());
        tvVchatName.setText(userInfo.getName());
        if (isSend) {
            sendInviteVChat();
        } else {
            playSound("connecting.mp3", true);
        }
    }

    /**
     * 初始化声网
     */
    private void initAgoraEngine() {
        initializeAgoraEngine();     // Tutorial Step 1
        setupVideoProfile();         // Tutorial Step 2
        setupLocalVideo();           // Tutorial Step 3
    }

    /**
     * 发送邀请私聊
     */
    private void sendInviteVChat() {
        YueJianAppApiProtoHelper.sendACInviteVipChatReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                userInfo.getUid(),
                true,
                new YueJianAppApiProtoHelper.ACInviteVipChatReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse(final String liveid) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLiveId = liveid;
                                YueJianAppUiUtils.setVisibility(llVchatAccept, View.GONE);
                                YueJianAppUiUtils.setVisibility(rlVChatBG, View.VISIBLE);
                                playSound("ring.mp3", true);
                            }
                        });
                    }
                }
        );
    }

    /**
     * 发送挂断请求
     */
    private void sendQuitVipChat() {
        YueJianAppApiProtoHelper.sendACQuitVipChatReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                mLiveId,
                new YueJianAppApiProtoHelper.ACQuitVipChatReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse(int broadcastSeconds, int costedDiamond, int experience, String consumerUid) {

                    }
                });
    }

    private MediaPlayer mMediaPlayer;

    /**
     * 播放铃声
     *
     * @param fileName  文件名
     * @param isLooping 是否循环播放
     */
    public void playSound(String fileName, boolean isLooping) {
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
                mMediaPlayer.setLooping(isLooping);
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
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
    }

    /**
     * 点击接听按钮，发送接受VChat操作
     *
     * @param view
     */
    public void onAcceptVChat(View view) {
        YueJianAppApiProtoHelper.sendACAcceptVipChatReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                userInfo.getUid(),
                mLiveId,
                new YueJianAppApiProtoHelper.ACAcceptVipChatReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                });
    }

    /**
     * 点击拒绝按钮，发送拒绝VChat操作
     *
     * @param view
     */
    public void onRefuseVChat(View view) {
        if (isSend) { // 连麦的发起者, 执行取消操作
            YueJianAppApiProtoHelper.sendACCancelVipChatReq(this,
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    userInfo.getUid(),
                    mLiveId,
                    new YueJianAppApiProtoHelper.ACCancelVipChatReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {

                        }

                        @Override
                        public void onResponse() {

                        }
                    });
        } else {
            YueJianAppApiProtoHelper.sendACDeclineVipChatReq(
                    this,
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    userInfo.getUid(),
                    mLiveId,
                    new YueJianAppApiProtoHelper.ACDeclineVipChatReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {

                        }

                        @Override
                        public void onResponse() {

                        }
                    }
            );
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
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (YueJianAppAppConfig.ACTION_VIP_CHAT_START.equals(intent.getAction())) { // 开始连麦
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeSound();
                            joinChannel(mLiveId);
                            YueJianAppUiUtils.setVisibility(rlVChatBG, View.GONE);
                            if (mHandler != null) {
                                mHandler.sendEmptyMessage(0);
                            }
                        }
                    });
                    YueJianAppUiUtils.setVisibility(llVchatAccept, View.GONE);
                    YueJianAppUiUtils.setVisibility(llRefuse, View.GONE);
                    YueJianAppUiUtils.setVisibility(rlVChatBG, View.GONE);
                } else if (YueJianAppAppConfig.ACTION_VIP_CHAT_RESULT.equals(intent.getAction())) { // 连麦结束
                    leaveChannel();

                    final int broadcastSeconds = intent.getIntExtra("BROADCAST_SECONDS", 0);
                    final int costedDiamond = intent.getIntExtra("COST_DIAMOND", 0);
                    final int experience = intent.getIntExtra("EXPERIENCE", 0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            YueJianAppDialogHelp.showUserVChatResultDialog(
                                    getLayoutInflater(),
                                    YueJianAppVideoChatActivity.this,
                                    userInfo,
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
                    });
                } else if (YueJianAppAppConfig.ACTION_VIP_CHAT_DECLINE.equals(intent.getAction())) { // 接收者拒绝连麦
                    YueJianAppTLog.info("接收者拒绝连麦");
                    String tips;
                    if (isSend) { //我是邀请者
                        tips = String.format("%s拒绝了你的连麦请求", userInfo.getName());
                    } else {
                        tips = String.format("您拒绝了%s的连麦请求", userInfo.getName());
                    }
                    Toast.makeText(getApplicationContext(), tips, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (YueJianAppAppConfig.ACTION_VIP_CHAT_CANCEL.equals(intent.getAction())) { // 发起者取消连麦
                    YueJianAppTLog.info("发起者取消连麦");
                    String tips;
                    if (isSend) { //我是邀请者
                        tips = String.format("您取消了与%s的连麦请求", userInfo.getName());
                    } else {
                        tips = String.format("%s取消了与您的连麦请求", userInfo.getName());
                    }
                    Toast.makeText(getApplicationContext(), tips, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    int time = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(Message msg) {
            time += 1;
            if (tvCallTime != null) {
                tvCallTime.setText("已耗时: " + YueJianAppStringUtil.getMinuteSecondBySecond(time));
            }
            if (null != mHandler) {
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
            super.handleMessage(msg);
        }
    };


    // Tutorial Step 1
    private void initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(YueJianAppAppContext.getInstance(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) { // Tutorial Step 5
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(int uid, int reason) { // 远程已离线
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft();
                }
            });
        }

        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) { // 其他用户已停发/已重发视频流回调
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVideoMuted(uid, muted);
                }
            });
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {//加入频道成功
            super.onJoinChannelSuccess(channel, uid, elapsed);
        }
    };

    // Tutorial Step 2
    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_720P, false);
    }

    // Tutorial Step 3
    private void setupLocalVideo() {
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        localVideoViewContainer.addView(surfaceView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, 0));
    }

    // Tutorial Step 4
    private void joinChannel(String roomId) {
        mRtcEngine.joinChannel(getString(R.string.agora_app_id), roomId, "Extra Optional Data", 0);
        YueJianAppUiUtils.setVisibility(rlVChatBG, View.GONE);
    }

    // Tutorial Step 5
    private void setupRemoteVideo(int uid) {
        if (remoteVideoViewContainer.getChildCount() >= 1) {
            return;
        }

        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        remoteVideoViewContainer.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));

        surfaceView.setTag(uid); // for mark purpose
    }

    // Tutorial Step 6
    private void leaveChannel() {
        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
            mRtcEngine = null;
        }
        RtcEngine.destroy();
    }

    // Tutorial Step 7
    private void onRemoteUserLeft() {
        remoteVideoViewContainer.removeAllViews();
    }

    // Tutorial Step 10
    private void onRemoteUserVideoMuted(int uid, boolean muted) {
        SurfaceView surfaceView = (SurfaceView) remoteVideoViewContainer.getChildAt(0);

        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
    }

    // 关闭摄像头
    public void onLocalAudioMuteClicked(View view) {
        LinearLayout ll = (LinearLayout) view;
        mRtcEngine.muteLocalVideoStream(ll.isSelected());
        ll.setSelected(!ll.isSelected());

    }

    // 切换摄像头
    public void onSwitchCameraClicked(View view) {
        if (mRtcEngine != null) {
            mRtcEngine.switchCamera();
        }
    }

    // 挂断
    public void onEncCallClicked(View view) {
        sendQuitVipChat();
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeSound();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler.removeMessages(0);
        unregisterReceiver(broadcastReceiver);
        leaveChannel();
        unbinder.unbind();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            YueJianAppDialogHelp.showConfirmDialog(getLayoutInflater(), this, "确定退出连麦吗", new YueJianAppINomalDialog() {
                @Override
                public void cancelDialog(View v, Dialog d) {
                    d.dismiss();
                }

                @Override
                public void determineDialog(View v, Dialog d) {
                    d.dismiss();
                    sendQuitVipChat();
                }
            });
        }
        return super.onKeyDown(keyCode, event);
    }
}
