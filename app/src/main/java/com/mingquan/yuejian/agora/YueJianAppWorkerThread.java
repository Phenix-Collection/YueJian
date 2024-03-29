package com.mingquan.yuejian.agora;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import java.io.File;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.mediaio.IVideoSink;
import io.agora.rtc.mediaio.IVideoSource;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class YueJianAppWorkerThread extends Thread {
    private final static String TAG = "YueJianAppWorkerThread";

    private final Context mContext;

    private static final int ACTION_WORKER_THREAD_QUIT = 0X1010; // quit this thread

    private static final int ACTION_WORKER_JOIN_CHANNEL = 0X2010;

    private static final int ACTION_WORKER_LEAVE_CHANNEL = 0X2011;

    private static final int ACTION_WORKER_CONFIG_ENGINE = 0X2012;

    private static final int ACTION_WORKER_CLOSE_CAMERA = 0X2013;

    private static final class WorkerThreadHandler extends Handler {

        private YueJianAppWorkerThread mWorkerThread;

        WorkerThreadHandler(YueJianAppWorkerThread thread) {
            this.mWorkerThread = thread;
        }

        public void release() {
            mWorkerThread = null;
        }

        @Override
        public void handleMessage(Message msg) {
            if (this.mWorkerThread == null) {
                Log.w(TAG, ("handler is already released! " + msg.what));
                return;
            }

            switch (msg.what) {
                case ACTION_WORKER_THREAD_QUIT:
                    mWorkerThread.exit();
                    break;
                case ACTION_WORKER_JOIN_CHANNEL:
                    String[] data = (String[]) msg.obj;
                    mWorkerThread.joinChannel(data[0], data[1], msg.arg1);
                    break;
                case ACTION_WORKER_LEAVE_CHANNEL:
                    String channel = (String) msg.obj;
                    mWorkerThread.leaveChannel(channel);
                    break;
                case ACTION_WORKER_CLOSE_CAMERA:
                    boolean isClose = (boolean) msg.obj;
                    mWorkerThread.closeCamera(isClose);
                    break;
                case ACTION_WORKER_CONFIG_ENGINE:
                    Object[] configData = (Object[]) msg.obj;
                    mWorkerThread.configEngine((int) configData[0], (int) configData[1]);
                    break;
            }
        }
    }

    private WorkerThreadHandler mWorkerHandler;

    private boolean mReady;

    public final void waitForReady() {
        while (!mReady) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "wait for " + YueJianAppWorkerThread.class.getSimpleName());
        }
    }

    @Override
    public void run() {
        Log.d(TAG, "start to run");
        Looper.prepare();

        mWorkerHandler = new WorkerThreadHandler(this);

        ensureRtcEngineReadyLock();

        mReady = true;

        // enter thread looper
        Looper.loop();
    }

    private RtcEngine mRtcEngine;

    public final void joinChannel(String token, String channel, int uid) {
        if (Thread.currentThread() != this) {
            Log.w(TAG, ("joinChannel() - worker thread asynchronously " + channel + " " + uid));
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_JOIN_CHANNEL;
            envelop.obj = new String[]{token, channel};
            envelop.arg1 = uid;
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        ensureRtcEngineReadyLock();
        mRtcEngine.joinChannel(token, channel, "AgoraWithBeauty", uid);
        mEngineConfig.mChannel = channel;

        Log.d(TAG, ("joinChannel " + token + " " + channel + " " + uid));
    }

    public final void leaveChannel(String channel) {
        if (Thread.currentThread() != this) {
            Log.w(TAG, ("leaveChannel() - worker thread asynchronously " + channel));
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_LEAVE_CHANNEL;
            envelop.obj = channel;
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }

        int clientRole = mEngineConfig.mClientRole;
        mEngineConfig.reset();
        Log.d(TAG, ("leaveChannel " + channel + " " + clientRole));
    }

    /**
     * 关闭摄像头
     *
     * @param isClose
     */
    final void closeCamera(boolean isClose) {
        if (Thread.currentThread() != this) {
            YueJianAppTLog.info("closeCamera() - worker thread asynchronously isClose: %s", isClose);
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_CLOSE_CAMERA;
            envelop.obj = isClose;
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        if (mRtcEngine != null) {
            mRtcEngine.muteLocalVideoStream(isClose);
        }
        YueJianAppTLog.info("closeCamera() isClose: %s", isClose);
    }

    private YueJianAppEngineConfig mEngineConfig;

    public final YueJianAppEngineConfig getEngineConfig() {
        return mEngineConfig;
    }

    private final YueJianAppMyEngineEventHandler mEngineEventHandler;

    public final void configEngine(int cRole, int vProfile) {
        if (Thread.currentThread() != this) {
            Log.d(TAG, "configEngine() - worker thread asynchronously " + cRole + " " + vProfile);
            Message envelop = new Message();
            envelop.what = ACTION_WORKER_CONFIG_ENGINE;
            envelop.obj = new Object[]{cRole, vProfile};
            mWorkerHandler.sendMessage(envelop);
            return;
        }

        ensureRtcEngineReadyLock();
        mEngineConfig.mClientRole = cRole;
        mEngineConfig.mVideoProfile = vProfile;

//        mRtcEngine.setVideoProfile(mEngineConfig.mVideoProfile, true);
        VideoEncoderConfiguration videoEncoderConfiguration = new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_1280x720,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
        mRtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration);

        mRtcEngine.setClientRole(cRole);
        Log.d(TAG, "configEngine " + cRole + " " + mEngineConfig.mVideoProfile);
    }

    public static String getDeviceID(Context context) {
        // XXX according to the API docs, this value may change after factory reset
        // use Android id as device id
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void setVideoSource(IVideoSource source) {
        mRtcEngine.setVideoSource(source);
    }

    public void setLocalVideoRenderer(IVideoSink renderer) {
        mRtcEngine.setLocalVideoRenderer(renderer);
    }

    private RtcEngine ensureRtcEngineReadyLock() {
        if (mRtcEngine == null) {
            String appId = mContext.getString(R.string.agora_app_id);
            if (TextUtils.isEmpty(appId)) {
                throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
            }
            try {
                mRtcEngine = RtcEngine.create(mContext, appId, mEngineEventHandler.mRtcEventHandler);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
                throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
            }
            // set channel profile mode as CHANNEL_PROFILE_LIVE_BROADCASTING or CHANNEL_PROFILE_COMMUNICATION
            mRtcEngine.setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            // mRtcEngine.setChannelProfile(YueJianAppAgora_Constants.CHANNEL_PROFILE_COMMUNICATION);
            mRtcEngine.enableVideo();
            mRtcEngine.setLogFile(Environment.getExternalStorageDirectory()
                    + File.separator + mContext.getPackageName() + "/log/agora-rtc.log");
            mRtcEngine.enableDualStreamMode(true);
        }
        return mRtcEngine;
    }

    public YueJianAppMyEngineEventHandler eventHandler() {
        return mEngineEventHandler;
    }

    public RtcEngine getRtcEngine() {
        return mRtcEngine;
    }

    /**
     * call this method to exit
     * should ONLY call this method when this thread is running
     */
    public final void exit() {
        if (Thread.currentThread() != this) {
            Log.w(TAG, "exit() - exit app thread asynchronously");
            mWorkerHandler.sendEmptyMessage(ACTION_WORKER_THREAD_QUIT);
            return;
        }

        mReady = false;

        // TODO should remove all pending(read) messages

        Log.d(TAG, "exit() > start");

        // exit thread looper
        Looper.myLooper().quit();

        mWorkerHandler.release();

        Log.d(TAG, "exit() > end");
    }

    public YueJianAppWorkerThread(Context context) {
        this.mContext = context;

        this.mEngineConfig = new YueJianAppEngineConfig();

        this.mEngineConfig.mUid = YueJianAppAgora_Constants.UID;

        this.mEngineEventHandler = new YueJianAppMyEngineEventHandler(mContext, this.mEngineConfig);
    }
}
