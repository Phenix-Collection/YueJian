/*
 * Copyright (C) 2017 QR Co. All rights reserved.
 *
 * Created by tool, DO NOT EDIT!!!
 */

package com.mingquan.yuejian.proto;

import com.mingquan.yuejian.proto.model.YueJianAppACChatModel;
import com.mingquan.yuejian.proto.model.YueJianAppACGiftModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;

public interface YueJianAppIChatProtoHandler {
    /**
     * 收到登录回应.
     * @param errorCode 错误码.
     */
    void handleCCLoginNtf(int errorCode);

    /**
     * 收到聊天回应.
     * @param errorCode 错误码.
     * @param chat 聊天信息.
     */
    void handleCCChatNtf(int errorCode, YueJianAppACChatModel chat);

    /**
     * 收到发送红心通知.
     * @param errorCode 错误码.
     * @param icon 弹出的图标名称.
     */
    void handleCCGiveHeartNtf(int errorCode, String icon);

    /**
     * 收到停止直播通知.
     * @param errorCode 错误码.
     * @param reason 关闭房间的原因.
     */
    void handleCCStopBroadcastNtf(int errorCode, String reason);

    /**
     * 收到发送礼物请通知.
     * @param errorCode 错误码.
     * @param gift 礼物的详细信息.
     * @param sender 送礼物的人.
     * @param receiver 收礼物的人.
     */
    void handleCCSendGiftNtf(int errorCode, YueJianAppACGiftModel gift, YueJianAppACUserPublicInfoModel sender, YueJianAppACUserPublicInfoModel receiver);

    /**
     * 收到更新钻石数量变化通知.
     * @param errorCode 错误码.
     * @param diamond 自己当前的钻石数量.
     */
    void handleCCUpdateDiamondNtf(int errorCode, int diamond);

    /**
     * 收到用户进入房间通知.
     * @param errorCode 错误码.
     * @param user 进入房间的用户.
     * @param rankingScore 用户的排名分数(根据此分数调整房间内用户的排位).
     */
    void handleCCUserEnterRoomNtf(int errorCode, YueJianAppACUserPublicInfoModel user, int rankingScore);

    /**
     * 收到用户离开房间通知.
     * @param errorCode 错误码.
     * @param user 离开房间的用户.
     */
    void handleCCUserLeaveRoomNtf(int errorCode, YueJianAppACUserPublicInfoModel user);

    /**
     * 收到[VCHAT]私聊开始通知.
     * @param errorCode 错误码.
     * @param roomId 房间编号.
     */
    void handleCCVipChatStartNtf(int errorCode, String roomId);

    /**
     * 收到[VCHAT]私聊结算通知.
     * @param errorCode 错误码.
     * @param broadcastSeconds 直播时长.
     * @param costedDiamond 消耗的钻石数.
     * @param experience 观众和主播增加的亲密值.
     * @param consumerUid 消费者的uid.
     */
    void handleCCVipChatResultNtf(int errorCode, int broadcastSeconds, int costedDiamond, int experience, String consumerUid);

    /**
     * 收到[VCHAT]被呼叫方拒绝接听.
     * @param errorCode 错误码.
     * @param callerUid 呼叫方的用户id.
     * @param calleeUid 被呼叫方的用户id.
     */
    void handleCCVipChatDeclineNtf(int errorCode, String callerUid, String calleeUid);

    /**
     * 收到[VCHAT]呼叫方取消呼叫.
     * @param errorCode 错误码.
     * @param callerUid 呼叫方的用户id.
     * @param calleeUid 被呼叫方的用户id.
     */
    void handleCCVipChatCancelNtf(int errorCode, String callerUid, String calleeUid);

    /**
     * 收到[VCHAT]ping回应.
     * @param errorCode 错误码.
     * @param token 口令.
     */
    void handleCCPingNtf(int errorCode, String token);
}