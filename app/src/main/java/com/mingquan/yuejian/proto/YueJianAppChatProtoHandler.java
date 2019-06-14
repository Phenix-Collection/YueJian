/*
 * Copyright (C) 2017 QR Co. All rights reserved.
 *
 * Created by tool, DO NOT EDIT!!!
 */

package com.mingquan.yuejian.proto;

import com.mingquan.yuejian.proto.model.YueJianAppACChatModel;
import com.mingquan.yuejian.proto.model.YueJianAppACGiftModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;

import java.util.HashMap;

import org.json.JSONObject;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import com.mingquan.yuejian.utils.YueJianAppRavenUtils;

public class YueJianAppChatProtoHandler {
    // 频道定义
    private static final String CHAT_CHANNEL_NAME = "game";

    // 协议号定义
    private static final int PID_CCLoginNtf = 12;  // 登录回应
    private static final int PID_CCChatNtf = 19;  // 聊天回应
    private static final int PID_CCGiveHeartNtf = 21;  // 发送红心通知
    private static final int PID_CCStopBroadcastNtf = 23;  // 停止直播通知
    private static final int PID_CCSendGiftNtf = 25;  // 发送礼物请通知
    private static final int PID_CCUpdateDiamondNtf = 28;  // 更新钻石数量变化通知
    private static final int PID_CCUserEnterRoomNtf = 29;  // 用户进入房间通知
    private static final int PID_CCUserLeaveRoomNtf = 30;  // 用户离开房间通知
    private static final int PID_CCVipChatStartNtf = 307;  // [VCHAT]私聊开始通知
    private static final int PID_CCVipChatResultNtf = 310;  // [VCHAT]私聊结算通知
    private static final int PID_CCVipChatDeclineNtf = 311;  // [VCHAT]被呼叫方拒绝接听
    private static final int PID_CCVipChatCancelNtf = 312;  // [VCHAT]呼叫方取消呼叫
    private static final int PID_CCPingNtf = 314;  // [VCHAT]ping回应

    private static YueJianAppChatProtoHandler sInstance;
    private YueJianAppIChatProtoHandler mHandler;

    public static synchronized YueJianAppChatProtoHandler getInstance() {
        if (sInstance == null) {
            sInstance = new YueJianAppChatProtoHandler();
        }
        return sInstance;
    }

    /**
     * 监听指定的聊天频道.
     * @param socket The client socket of socket.io.
     * @param handler 协议回调处理对象.
     */
    public void dispatchProto(Socket socket, YueJianAppIChatProtoHandler handler) {
        this.mHandler = handler;
        socket.on(CHAT_CHANNEL_NAME, channelGameListener);
    }

    private final Emitter.Listener channelGameListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String res = args[0].toString();
            if (res.isEmpty()) {
                return;
            }

            int proto_id = 0;
            int errorCode = 0;
            try {
                JSONObject json = new JSONObject(res);
                if (!json.has("proto_id")) {
                    return;
                }

                proto_id = json.getInt("proto_id");
                errorCode = json.getInt("error_code");
                JSONObject data = json.getJSONObject("data");
                switch (proto_id) {
                    case PID_CCLoginNtf:
                        handleCCLoginNtf(errorCode, data);
                        break;

                    case PID_CCChatNtf:
                        handleCCChatNtf(errorCode, data);
                        break;

                    case PID_CCGiveHeartNtf:
                        handleCCGiveHeartNtf(errorCode, data);
                        break;

                    case PID_CCStopBroadcastNtf:
                        handleCCStopBroadcastNtf(errorCode, data);
                        break;

                    case PID_CCSendGiftNtf:
                        handleCCSendGiftNtf(errorCode, data);
                        break;

                    case PID_CCUpdateDiamondNtf:
                        handleCCUpdateDiamondNtf(errorCode, data);
                        break;

                    case PID_CCUserEnterRoomNtf:
                        handleCCUserEnterRoomNtf(errorCode, data);
                        break;

                    case PID_CCUserLeaveRoomNtf:
                        handleCCUserLeaveRoomNtf(errorCode, data);
                        break;

                    case PID_CCVipChatStartNtf:
                        handleCCVipChatStartNtf(errorCode, data);
                        break;

                    case PID_CCVipChatResultNtf:
                        handleCCVipChatResultNtf(errorCode, data);
                        break;

                    case PID_CCVipChatDeclineNtf:
                        handleCCVipChatDeclineNtf(errorCode, data);
                        break;

                    case PID_CCVipChatCancelNtf:
                        handleCCVipChatCancelNtf(errorCode, data);
                        break;

                    case PID_CCPingNtf:
                        handleCCPingNtf(errorCode, data);
                        break;


                    default: {
                    }
                }
            } catch (Exception e) {
                HashMap<String, Object> extension = new HashMap<>();
                extension.put("proto_id", proto_id);
                extension.put("errorCode", errorCode);
                YueJianAppRavenUtils.logException("监听聊天服务器" + CHAT_CHANNEL_NAME + "返回结果失败!", e, extension);
            }
        }
    };

    /**
     * 收到登录回应.
     * @param json 协议数据.
     */
    private void handleCCLoginNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            mHandler.handleCCLoginNtf(errorCode);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCLoginNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCLoginNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到聊天回应.
     * @param json 协议数据.
     */
    private void handleCCChatNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 聊天信息
            current_field_name = "chat";
            YueJianAppACChatModel chat = new YueJianAppACChatModel(null);
            if (json.has("chat")) {
                chat = new YueJianAppACChatModel(json.getJSONObject("chat"));
            }
            
        
            mHandler.handleCCChatNtf(errorCode, chat);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCChatNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCChatNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到发送红心通知.
     * @param json 协议数据.
     */
    private void handleCCGiveHeartNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 弹出的图标名称
            current_field_name = "icon";
            String icon = "";
            if (json.has("icon")) {
                icon = json.getString("icon");
            }
            
            mHandler.handleCCGiveHeartNtf(errorCode, icon);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCGiveHeartNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCGiveHeartNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到停止直播通知.
     * @param json 协议数据.
     */
    private void handleCCStopBroadcastNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 关闭房间的原因
            current_field_name = "reason";
            String reason = "";
            if (json.has("reason")) {
                reason = json.getString("reason");
            }
            
            mHandler.handleCCStopBroadcastNtf(errorCode, reason);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCStopBroadcastNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCStopBroadcastNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到发送礼物请通知.
     * @param json 协议数据.
     */
    private void handleCCSendGiftNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 礼物的详细信息
            current_field_name = "gift";
            YueJianAppACGiftModel gift = new YueJianAppACGiftModel(null);
            if (json.has("gift")) {
                gift = new YueJianAppACGiftModel(json.getJSONObject("gift"));
            }
            
        
            // 送礼物的人
            current_field_name = "sender";
            YueJianAppACUserPublicInfoModel sender = new YueJianAppACUserPublicInfoModel(null);
            if (json.has("sender")) {
                sender = new YueJianAppACUserPublicInfoModel(json.getJSONObject("sender"));
            }
            
        
            // 收礼物的人
            current_field_name = "receiver";
            YueJianAppACUserPublicInfoModel receiver = new YueJianAppACUserPublicInfoModel(null);
            if (json.has("receiver")) {
                receiver = new YueJianAppACUserPublicInfoModel(json.getJSONObject("receiver"));
            }
            
        
            mHandler.handleCCSendGiftNtf(errorCode, gift, sender, receiver);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCSendGiftNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCSendGiftNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到更新钻石数量变化通知.
     * @param json 协议数据.
     */
    private void handleCCUpdateDiamondNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 自己当前的钻石数量
            current_field_name = "diamond";
            int diamond = 0;
            if (json.has("diamond")) {
                diamond = json.getInt("diamond");
            }
            
            mHandler.handleCCUpdateDiamondNtf(errorCode, diamond);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCUpdateDiamondNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCUpdateDiamondNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到用户进入房间通知.
     * @param json 协议数据.
     */
    private void handleCCUserEnterRoomNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 进入房间的用户
            current_field_name = "user";
            YueJianAppACUserPublicInfoModel user = new YueJianAppACUserPublicInfoModel(null);
            if (json.has("user")) {
                user = new YueJianAppACUserPublicInfoModel(json.getJSONObject("user"));
            }
            
        
            // 用户的排名分数(根据此分数调整房间内用户的排位)
            current_field_name = "ranking_score";
            int rankingScore = 0;
            if (json.has("ranking_score")) {
                rankingScore = json.getInt("ranking_score");
            }
            
            mHandler.handleCCUserEnterRoomNtf(errorCode, user, rankingScore);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCUserEnterRoomNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCUserEnterRoomNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到用户离开房间通知.
     * @param json 协议数据.
     */
    private void handleCCUserLeaveRoomNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 离开房间的用户
            current_field_name = "user";
            YueJianAppACUserPublicInfoModel user = new YueJianAppACUserPublicInfoModel(null);
            if (json.has("user")) {
                user = new YueJianAppACUserPublicInfoModel(json.getJSONObject("user"));
            }
            
        
            mHandler.handleCCUserLeaveRoomNtf(errorCode, user);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCUserLeaveRoomNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCUserLeaveRoomNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到[VCHAT]私聊开始通知.
     * @param json 协议数据.
     */
    private void handleCCVipChatStartNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 房间编号
            current_field_name = "room_id";
            String roomId = "";
            if (json.has("room_id")) {
                roomId = json.getString("room_id");
            }
            
            mHandler.handleCCVipChatStartNtf(errorCode, roomId);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCVipChatStartNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCVipChatStartNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到[VCHAT]私聊结算通知.
     * @param json 协议数据.
     */
    private void handleCCVipChatResultNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 直播时长
            current_field_name = "broadcast_seconds";
            int broadcastSeconds = 0;
            if (json.has("broadcast_seconds")) {
                broadcastSeconds = json.getInt("broadcast_seconds");
            }
            
            // 消耗的钻石数
            current_field_name = "costed_diamond";
            int costedDiamond = 0;
            if (json.has("costed_diamond")) {
                costedDiamond = json.getInt("costed_diamond");
            }
            
            // 观众和主播增加的亲密值
            current_field_name = "experience";
            int experience = 0;
            if (json.has("experience")) {
                experience = json.getInt("experience");
            }
            
            // 消费者的uid
            current_field_name = "consumer_uid";
            String consumerUid = "";
            if (json.has("consumer_uid")) {
                consumerUid = json.getString("consumer_uid");
            }
            
            mHandler.handleCCVipChatResultNtf(errorCode, broadcastSeconds, costedDiamond, experience, consumerUid);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCVipChatResultNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCVipChatResultNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到[VCHAT]被呼叫方拒绝接听.
     * @param json 协议数据.
     */
    private void handleCCVipChatDeclineNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 呼叫方的用户id
            current_field_name = "caller_uid";
            String callerUid = "";
            if (json.has("caller_uid")) {
                callerUid = json.getString("caller_uid");
            }
            
            // 被呼叫方的用户id
            current_field_name = "callee_uid";
            String calleeUid = "";
            if (json.has("callee_uid")) {
                calleeUid = json.getString("callee_uid");
            }
            
            mHandler.handleCCVipChatDeclineNtf(errorCode, callerUid, calleeUid);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCVipChatDeclineNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCVipChatDeclineNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到[VCHAT]呼叫方取消呼叫.
     * @param json 协议数据.
     */
    private void handleCCVipChatCancelNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 呼叫方的用户id
            current_field_name = "caller_uid";
            String callerUid = "";
            if (json.has("caller_uid")) {
                callerUid = json.getString("caller_uid");
            }
            
            // 被呼叫方的用户id
            current_field_name = "callee_uid";
            String calleeUid = "";
            if (json.has("callee_uid")) {
                calleeUid = json.getString("callee_uid");
            }
            
            mHandler.handleCCVipChatCancelNtf(errorCode, callerUid, calleeUid);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCVipChatCancelNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCVipChatCancelNtf返回结果失败!", e, extension);
        }
    }

    /**
     * 收到[VCHAT]ping回应.
     * @param json 协议数据.
     */
    private void handleCCPingNtf(int errorCode, JSONObject json) {
        String current_field_name = "";
        try {
            // 口令
            current_field_name = "token";
            String token = "";
            if (json.has("token")) {
                token = json.getString("token");
            }
            
            mHandler.handleCCPingNtf(errorCode, token);
        } catch (Exception e) {
            HashMap<String, Object> extension = new HashMap<>();
            extension.put("proto", "CCPingNtf");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析" + CHAT_CHANNEL_NAME + ".CCPingNtf返回结果失败!", e, extension);
        }
    }
}