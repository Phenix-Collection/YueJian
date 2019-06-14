package com.mingquan.yuejian.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import java.util.Locale;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class VoiceChatViewActivity extends YueJianAppFullScreenModeActivity {

    private static final String LOG_TAG = VoiceChatViewActivity.class.getSimpleName();

    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private RtcEngine mRtcEngine;
    private String mLiveId = "chan001";
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1

        @Override
        public void onUserOffline(final int uid, final int reason) { // Tutorial Step 4
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    YueJianAppTLog.debug("on user off line");
                    onRemoteUserLeft(uid, reason);
                }
            });
        }

        @Override
        public void onUserMuteAudio(final int uid, final boolean muted) { // Tutorial Step 6
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    YueJianAppTLog.debug("on user mute audio");
                    onRemoteUserVoiceMuted(uid, muted);
                }
            });
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
            YueJianAppTLog.debug("on user joined uid:%s, elapsed:%s", uid, elapsed);
        }

        @Override
        public void onTokenPrivilegeWillExpire(String token) { // token 即将失效
            super.onTokenPrivilegeWillExpire(token);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateToken();
                }
            });
        }

        private void updateToken() {
            YueJianAppApiProtoHelper.sendACFetchAgoraTokenReq(
                    VoiceChatViewActivity.this,
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    mLiveId,
                    new YueJianAppApiProtoHelper.ACFetchAgoraTokenReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            YueJianAppTLog.error(errMessage);
                        }

                        @Override
                        public void onResponse(String token) {
                            mRtcEngine.renewToken(token);
                        }
                    });
        }

        @Override
        public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) { // 音量回调
            super.onAudioVolumeIndication(speakers, totalVolume);
            YueJianAppTLog.debug("uid:%s, volume:%s, totalVolume:%s", speakers[0].uid, speakers[0].volume, totalVolume);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_chat_view);

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {
            initAgoraEngineAndJoinChannel();
        }
    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();
        mRtcEngine.enableAudio(); // 启用音频模块
        mRtcEngine.setDefaultAudioRoutetoSpeakerphone(true); // 默认开启扬声器
        mRtcEngine.enableAudioVolumeIndication(300, 3); // 启用说话者音量提示
        mRtcEngine.setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_COMMUNICATION);
        YueJianAppApiProtoHelper.sendACFetchAgoraTokenReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                mLiveId,
                new YueJianAppApiProtoHelper.ACFetchAgoraTokenReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse(String token) {
                        joinChannel(token);
                    }
                }
        );
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i(LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);

        switch (requestCode) {
            case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel();
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                    finish();
                }
                break;
            }
        }
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
    }

    // Tutorial Step 7
    public void onLocalAudioMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
            iv.clearColorFilter();
        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }

        mRtcEngine.muteLocalAudioStream(iv.isSelected());
    }

    // Tutorial Step 5
    public void onSwitchSpeakerphoneClicked(View view) {
        ImageView iv = (ImageView) view;

        // 判断是否在启用扬声器
        YueJianAppTLog.debug("是否开启扬声器：%s", mRtcEngine.isSpeakerphoneEnabled());
        if (mRtcEngine.isSpeakerphoneEnabled()) {
            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            mRtcEngine.setEnableSpeakerphone(false);
        } else {
            iv.clearColorFilter();
            mRtcEngine.setEnableSpeakerphone(true);
        }
    }

    // Tutorial Step 3
    public void onEncCallClicked(View view) {
        finish();
    }

    // Tutorial Step 1
    private void initializeAgoraEngine() {
        YueJianAppTLog.debug("initialize agora engine");
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            YueJianAppTLog.error("%s: %s", LOG_TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    // Tutorial Step 2
    private void joinChannel(String agoraToken) {
        YueJianAppTLog.debug("join channel");
        mRtcEngine.joinChannel(agoraToken, mLiveId, "voiceChat", Integer.parseInt(YueJianAppAppContext.getInstance().getLoginUid()));
    }

    private void leaveChannel() {
        YueJianAppTLog.debug("leave channel");
        mRtcEngine.leaveChannel();
    }

    // Tutorial Step 4
    private void onRemoteUserLeft(int uid, int reason) {
        showLongToast(String.format(Locale.US, "user %d left %d", (uid & 0xFFFFFFFFL), reason));
    }

    // Tutorial Step 6
    private void onRemoteUserVoiceMuted(int uid, boolean muted) {
        showLongToast(String.format(Locale.US, "user %d muted or unmuted %b", (uid & 0xFFFFFFFFL), muted));
    }
}
