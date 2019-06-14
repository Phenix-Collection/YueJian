package com.mingquan.yuejian.agora;

import android.content.Context;
import android.util.Log;

import com.mingquan.yuejian.utils.YueJianAppTLog;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import io.agora.rtc.IRtcEngineEventHandler;

public class YueJianAppMyEngineEventHandler {
    public YueJianAppMyEngineEventHandler(Context ctx, YueJianAppEngineConfig config) {
        this.mContext = ctx;
        this.mConfig = config;
    }

    private final YueJianAppEngineConfig mConfig;

    private final Context mContext;

    private final ConcurrentHashMap<YueJianAppIAGEventHandler, Integer> mEventHandlerList = new ConcurrentHashMap<>();

    public void addEventHandler(YueJianAppIAGEventHandler handler) {
        this.mEventHandlerList.put(handler, 0);
    }

    public void removeEventHandler(YueJianAppIAGEventHandler handler) {
        this.mEventHandlerList.remove(handler);
    }

    final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        private final static String TAG = "IRtcEngineEventHandler";

        @Override
        public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
            Log.d(TAG, "onFirstRemoteVideoDecoded " + (uid & 0xFFFFFFFFL) + width + " " + height + " " + elapsed);

            Iterator<YueJianAppIAGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                YueJianAppIAGEventHandler handler = it.next();
                handler.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
            }
        }

        @Override
        public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
            Log.d(TAG, "onFirstLocalVideoFrame " + width + " " + height + " " + elapsed);
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            Iterator<YueJianAppIAGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                YueJianAppIAGEventHandler handler = it.next();
                handler.onUserJoined(uid, elapsed);
            }
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            // FIXME this callback may return times
            Iterator<YueJianAppIAGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                YueJianAppIAGEventHandler handler = it.next();
                handler.onUserOffline(uid, reason);
            }
        }

        @Override
        public void onUserMuteVideo(int uid, boolean muted) {
        }

        @Override
        public void onRtcStats(RtcStats stats) {
        }


        @Override
        public void onLeaveChannel(RtcStats stats) {

        }

        @Override
        public void onLastmileQuality(int quality) {
            Log.d(TAG, ("onLastmileQuality " + quality));
        }

        @Override
        public void onError(int err) {
            super.onError(err);
            Log.e(TAG, ("onError " + err));
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            Log.d(TAG, ("onJoinChannelSuccess " + channel + " " + uid + " " + (uid & 0xFFFFFFFFL) + " " + elapsed));

            Iterator<YueJianAppIAGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                YueJianAppIAGEventHandler handler = it.next();
                handler.onJoinChannelSuccess(channel, uid, elapsed);
            }
        }

        public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
            Log.d(TAG, ("onRejoinChannelSuccess " + channel + " " + uid + " " + elapsed));
        }

        public void onWarning(int warn) {
            Log.w(TAG, ("onWarning " + warn));
        }

        @Override
        public void onTokenPrivilegeWillExpire(String token) {
            super.onTokenPrivilegeWillExpire(token);
            YueJianAppTLog.error("token 即将失效！");
            Iterator<YueJianAppIAGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                YueJianAppIAGEventHandler handler = it.next();
                handler.onTokenPrivilegeWillExpire(token);
            }
        }
    };

}
