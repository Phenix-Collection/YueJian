/*
 * Copyright (C) 2017 QR Co. All rights reserved.
 *
 * Created by tool, DO NOT EDIT!!!
 */

package com.mingquan.yuejian.proto;

import org.json.JSONObject;
import io.socket.client.Socket;

public class YueJianAppChatProtoBuilder {
    // 频道定义
    private static final String CHAT_CHANNEL_NAME = "game";

    // 协议号定义
    private static final int PID_CCLoginRpt = 11;  // 登录请求
    private static final int PID_CCSendHearBeatRpt = 13;  // 发送心跳包请求
    private static final int PID_CCChatRpt = 18;  // 聊天请求
    private static final int PID_CCGiveHeartRpt = 20;  // 发送红心请求
    private static final int PID_CCPingRpt = 313;  // [VCHAT]ping请求

    /**
     * 发送登录请求.
     * @param socket The client socket of socket.io.
     * @param uid 用户id.
     * @param token 用户验证口令.
     */
    public static void sendCCLoginRpt(Socket socket, String uid, String token) {
        if (socket == null) {
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("proto_id", PID_CCLoginRpt);
            if (uid != "") { json.put("uid", uid); }
            if (token != "") { json.put("token", token); }
            socket.emit(CHAT_CHANNEL_NAME, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送发送心跳包请求.
     * @param socket The client socket of socket.io.
     */
    public static void sendCCSendHearBeatRpt(Socket socket) {
        if (socket == null) {
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("proto_id", PID_CCSendHearBeatRpt);
            socket.emit(CHAT_CHANNEL_NAME, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送聊天请求.
     * @param socket The client socket of socket.io.
     * @param channel 聊天频道(参见 ACChatChannelTypeDefine).
     * @param content 发送的内容.
     * @param extraImageUrl 附加图片地址.
     */
    public static void sendCCChatRpt(Socket socket, int channel, String content, String extraImageUrl) {
        if (socket == null) {
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("proto_id", PID_CCChatRpt);
            if (channel != 0) { json.put("channel", channel); }
            if (content != "") { json.put("content", content); }
            if (extraImageUrl != "") { json.put("extra_image_url", extraImageUrl); }
            socket.emit(CHAT_CHANNEL_NAME, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送发送红心请求.
     * @param socket The client socket of socket.io.
     */
    public static void sendCCGiveHeartRpt(Socket socket) {
        if (socket == null) {
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("proto_id", PID_CCGiveHeartRpt);
            socket.emit(CHAT_CHANNEL_NAME, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送[VCHAT]ping请求.
     * @param socket The client socket of socket.io.
     * @param token 口令.
     */
    public static void sendCCPingRpt(Socket socket, String token) {
        if (socket == null) {
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("proto_id", PID_CCPingRpt);
            if (token != "") { json.put("token", token); }
            socket.emit(CHAT_CHANNEL_NAME, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}