package com.mingquan.yuejian.agora;

public interface YueJianAppIAGEventHandler {
    void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed);

    void onJoinChannelSuccess(String channel, int uid, int elapsed);

    void onUserOffline(int uid, int reason);

    void onUserJoined(int uid, int elapsed);

    void onTokenPrivilegeWillExpire(String token);
}
