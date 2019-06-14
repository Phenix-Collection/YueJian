/*
 * Copyright (C) 2017 QR Co. All rights reserved.
 *
 * Created by tool, DO NOT EDIT!!!
 */

package com.mingquan.yuejian.proto;

import android.app.Activity;

import com.mingquan.yuejian.api.remote.YueJianAppApiUtils;
import com.mingquan.yuejian.proto.model.YueJianAppACBannerModel;
import com.mingquan.yuejian.proto.model.YueJianAppACChatInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACChatListModel;
import com.mingquan.yuejian.proto.model.YueJianAppACChatMessageModel;
import com.mingquan.yuejian.proto.model.YueJianAppACConsumptionInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACFastReplyModel;
import com.mingquan.yuejian.proto.model.YueJianAppACGiftModel;
import com.mingquan.yuejian.proto.model.YueJianAppACPayItemModel;
import com.mingquan.yuejian.proto.model.YueJianAppACResourceModel;
import com.mingquan.yuejian.proto.model.YueJianAppACServerConfigModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserHomepageInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPrivateInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserReleationInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserTagMetaDataModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserTagModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserTaggingModel;
import com.mingquan.yuejian.proto.model.YueJianAppACVideoInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACWeixinPayDataModel;
import com.mingquan.yuejian.utils.YueJianAppRavenUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class YueJianAppApiProtoHelper {
    // 错误码定义
    public static final int ERR_NO_ERROR = 0;  // 没有错误
    public static final int ERR_SYSTEM_ERROR = 1;  // 系统错误
    public static final int ERR_NEED_BIND_PHONE = 2;  // 需要绑定手机
    public static final int ERR_INVALID_PHONE_NUMBER = 9;  // 无效的手机号码
    public static final int ERR_INVALID_SMS_CODE = 10;  // 无效的验证码
    public static final int ERR_DUPLICATE_BIND_PHONE = 50;  // 重复绑定手机
    public static final int ERR_DUPLICATE_BIND_WX_UNIONID = 51;  // 重复绑定微信unionid
    public static final int ERR_INVALID_LIVEID = 52;  // 无效的LiveId
    public static final int ERR_INVALID_USER_TAG = 53;  // 无效的用户标签Id
    public static final int ERR_INVALID_VIDEO_ID = 54;  // 无效的视频Id
    public static final int ERR_UNLOCKED_VIDEO = 55;  // 视频未锁定，不需要购买
    public static final int ERR_DUPLICATE_BUY_VIDEO = 56;  // 已经购买过该视频
    public static final int ERR_UNFOLLOWED_VIDEO = 57;  // 未关注该视频
    public static final int ERR_DUPLICATE_FOLLOW_VIDEO = 58;  // 重复关注视频
    public static final int ERR_TARGET_IS_BUSY = 59;  // 对方正在通话中
    public static final int ERR_DUPLICATE_UPLOAD_AUTH_DATA = 60;  // 不可以重复提交审核资料
    public static final int ERR_INVALID_PRICE = 61;  // 无效的价格
    public static final int ERR_INVALID_FILE_TYPE = 62;  // 无效的文件类型
    public static final int ERR_INVALID_UPLOAD_FILE_KEY = 63;  // 无效的上传文件key
    public static final int ERR_UNAUTHORIZED = 64;  // 尚未认证
    public static final int ERR_CANNOT_SEND_MESSAGE_TO_WATCHER = 65;  // 不可以给用户发送私信
    public static final int ERR_CANNOT_FIND_PHOTO_RECORD = 66;  // 未找到照片记录
    public static final int ERR_INVALID_AUTH_STATUS = 67;  // 审核状态有误
    public static final int ERR_TARGET_IS_IN_THE_CALL = 68;  // 对方正在通话中
    public static final int ERR_NOT_LOGIN = 69;  // 尚未登录
    public static final int ERR_LACK_REPORT_REASON = 70;  // 缺少举报的理由
    public static final int ERR_REPORT_TOO_OFTEN = 71;  // 举报过于滤频繁
    public static final int ERR_DUPLICATE_REPORT = 72;  // 已经举报过该主播
    public static final int ERR_INVALID_PAY_AMOUNT = 73;  // 无效的充值金额
    public static final int ERR_DUPLICATE_CALL = 74;  // 重复拨打
    public static final int ERR_BEEN_BANNED = 75;  // 已被对方屏蔽
    public static final int ERR_NOT_SUPPORT = 76;  // 不支持
    public static final int ERR_ALREADY_LIFELONG_VIP = 77;  // 已经是终身VIP了
    public static final int ERR_TOO_MUCH_FAST_REPLY = 78;  // 快速回复数已达上限
    public static final int ERR_FAST_REPLY_TOO_LONG = 79;  // 快速回复消息过长
    public static final int ERR_CANNOT_FIND_PROFILE_RECORD = 80;  // 未找到资料审核记录
    public static final int ERR_DUPLICATE_TAKE_DAILY_LOGIN_REWARD = 3;  // 已经领取过今天的奖励了！
    public static final int ERR_MAX_WATCH_REWARD_COUNT = 4;  // 已经最大的领取上限！
    public static final int ERR_CANNOT_GET_WATCH_REWARD_IN_ADVANCE = 5;  // 不可以提前领取奖励！
    public static final int ERR_DUPLICATE_TAKE_QUEST_REWARD = 6;  // 不可重复领取任务奖励!
    public static final int ERR_INVALID_QUEST_TYPE = 7;  // 无效的任务类型!
    public static final int ERR_QUEST_NOT_COMPLETED = 8;  // 任务没有完成!
    public static final int ERR_SUSPENDED_ACCOUNT = 11;  // 账号被冻结
    public static final int ERR_REGISTER_FAILED = 12;  // 注册失败
    public static final int ERR_UPDATE_TOKEN_FAILED = 13;  // 更新token失败
    public static final int ERR_THIRD_PARTY_AUTH_FAILED = 14;  // 第三方授权错误
    public static final int ERR_NO_PATCH_FILE = 15;  // 没有更新文件
    public static final int ERR_INVALID_NICKNAME = 16;  // 无效的昵称
    public static final int ERR_USER_NOT_EXIST = 17;  // 用户不存在
    public static final int ERR_CANNOT_MUTE_BROADCASTER = 18;  // 不可以禁言主播
    public static final int ERR_PERMISSION_DENIED = 19;  // 权限不足
    public static final int ERR_BIND_WECHAT_FAILED = 20;  // 绑定微信失败
    public static final int ERR_CANNOT_FOLLOW_SELF = 21;  // 不可以关注自己
    public static final int ERR_INVALID_RANK_TYPE = 22;  // 无效的排行榜类型
    public static final int ERR_UNAUTHORIZED_ACCOUNT = 23;  // 账号未通过认证
    public static final int ERR_INVALID_GIFT_TYPE = 24;  // 无效的礼物类型
    public static final int ERR_INVALID_GIFT_ID = 25;  // 无效的礼物ID
    public static final int ERR_BROADCASTER_DONOT_HAS_PET = 26;  // 主播没有宠物
    public static final int ERR_NO_DIAMOND = 27;  // 余额不足
    public static final int ERR_HTTP_REQUEST_FAILED = 28;  // http请求失败
    public static final int ERR_INVALID_UID = 29;  // 无效的用户id
    public static final int ERR_INVALID_PARAMETERS = 31;  // 无效的参数
    public static final int ERR_DUPLICATE_CONFIRM = 35;  // 重复审核
    public static final int ERR_DUPLICATE_CLOSE_ROOM = 36;  // 不可以重复关闭房间
    public static final int ERR_INVALID_INVITE_CODE = 37;  // 无效的邀请码
    public static final int ERR_DUPLICATE_BIND_INVITE_CODE = 38;  // 重复绑定邀请码
    public static final int ERR_INVALID_TOKEN = 39;  // 无效的token
    public static final int ERR_UNAVAILABLE = 40;  // 暂未开放
    public static final int ERR_INVALID_AVATAR_FRAME_ID = 41;  // 无效的头像框ID
    public static final int ERR_NOT_HAVE_ITEM = 42;  // 未拥有指定的道具
    public static final int ERR_INVALID_ACHIEVEMENT_ID = 43;  // 无效的成就ID
    public static final int ERR_NOT_COMPLETE_ACHIEVEMENT = 44;  // 成就未完成
    public static final int ERR_DUPLICATE_GET_REWARD = 45;  // 不可以重复领取奖励
    public static final int ERR_TARGET_IS_OFFLINE = 46;  // 目标不在线了
    public static final int ERR_ONLY_RUN_IN_DEBUG_MODE = 47;  // 只能在测试模式下执行
    public static final int ERR_INVALID_REQUEST = 48;  // 非法请求
    public static final int ERR_INVALID_SIGN = 49;  // 签名验证失败
    
    // 审核状态定义
    public static final int AUTH_STATUS_NONE = 0;  // 未提交审核
    public static final int AUTH_STATUS_WAITING = 1;  // 审核中
    public static final int AUTH_STATUS_REJECTED = 2;  // 审核失败
    public static final int AUTH_STATUS_CERTIFIED = 3;  // 审核通过
    
    // 直播模式
    public static final int BROADCAST_MODE_SHOW = 0;  // 普通直播
    public static final int BROADCAST_MODE_GAME = 1;  // 游戏直播
    public static final int BROADCAST_MODE_GAME_WITH_IMAGE = 2;  // 静态图片的游戏直播
    public static final int BROADCAST_MODE_SHOW_WITH_VIDEO = 4;  // 静态视频直播
    public static final int BROADCAST_MODE_VIP = 8;  // VIP直播
    public static final int BROADCAST_MODE_PC = 16;  // PC直播
    public static final int BROADCAST_MODE_OFFLINE = 32;  // 离线中
    public static final int BROADCAST_MODE_ALL = 255;  // 所有模式
    
    // 主播分类定义
    public static final int BROADCASTER_RECOMMEND = 1;  // 推荐
    public static final int BROADCASTER_NEWBIE = 2;  // 新人
    public static final int BROADCASTER_CATEGORY_STAR3 = 3;  // 三星
    public static final int BROADCASTER_CATEGORY_STAR4 = 4;  // 四星
    public static final int BROADCASTER_CATEGORY_STAR5 = 5;  // 五星
    public static final int BROADCASTER_CATEGORY_NEARBY = 6;  // 附近
    public static final int BROADCASTER_CATEGORY_SCAN = 7;  // 扫描
    
    // 直播状态定义
    public static final int BROADCASTER_STATUS_OFFLINE = 0;  // 离线(未使用)
    public static final int BROADCASTER_STATUS_ONLINE = 1;  // 在线
    public static final int BROADCASTER_STATUS_CALLING = 2;  // 通话中
    public static final int BROADCASTER_STATUS_BUSY = 3;  // 勿扰
    
    // 呼叫状态定义
    public static final int CALL_STATUS_NONE = 0;  // 未接听
    public static final int CALL_STATUS_CALLING = 1;  // 通话中
    public static final int CALL_STATUS_DECLINE = 2;  // 被呼叫人拒绝通话
    public static final int CALL_STATUS_CANCEL = 3;  // 发起人取消
    public static final int CALL_STATUS_END = 4;  // 通话结束
    public static final int CALL_STATUS_SHUTDOWN = 5;  // 系统关闭
    
    // 聊天频道定义
    public static final int CHAT_CHANNEL_PUBLIC = 1;  // 公共频道
    public static final int CHAT_CHANNEL_BARRAGE = 2;  // 用户弹幕
    public static final int CHAT_CHANNEL_HORN = 4;  // 用户喇叭
    public static final int CHAT_CHANNEL_TOP = 8;  // 置顶信息
    public static final int CHAT_CHANNEL_SYSTEM = 16;  // 系统公告
    
    // 消费类型定义
    public static final int DIAMOND_INPUT_CHARGE = 1;  // 充值
    public static final int DIAMOND_INPUT_ACTIVITY_AGENT_INVITE_USER = 6;  // 代理邀请用户
    public static final int DIAMOND_INPUT_ACTIVITY_BE_INVITED_BY_AGENT = 7;  // 接受代理的邀请
    public static final int DIAMOND_INPUT_ACTIVITY_BE_INVITED_BY_PARTNER = 8;  // 接受合伙人的邀请
    public static final int DIAMOND_INPUT_ACTIVITY_FREE_TRIAL = 9;  // 免费试用活动赠送的钻石
    public static final int DIAMOND_INPUT_ADMIN = 98;  // GM充值
    public static final int DIAMOND_INPUT_TESTING = 99;  // 测试收入
    public static final int DIAMOND_OUTPUT_BUY_VIDEO = 102;  // 购买短视频
    public static final int DIAMOND_OUTPUT_VIDEO_CHAT = 103;  // 和主播视频电话
    public static final int DIAMOND_OUTPUT_SEND_GIFT = 104;  // 赠送礼物
    public static final int DIAMOND_OUTPUT_SEND_MESSAGE = 105;  // 给主播发送私信
    public static final int DIAMOND_OUTPUT_BUY_VIP_CARD = 106;  // 购买vip卡片
    public static final int DIAMOND_OUTPUT_TESTING = 199;  // 测试支出
    public static final int TICKET_INPUT_SELL_VIDEO = 1002;  // 出售短视频
    public static final int TICKET_INPUT_VIDEO_CHAT = 1003;  // 和观众视频电话
    public static final int TICKET_INPUT_RECEIVE_GIFT = 1004;  // 收到礼物
    public static final int TICKET_INPUT_RECEIVE_MESSAGE = 1005;  // 收到私信
    public static final int TICKET_INPUT_TESTING = 1099;  // 测试收入
    public static final int DEVKEEP_COIN_OUTPUT_BUY_VIDEO = 202;  // 购买短视频
    public static final int DEVKEEP_COIN_OUTPUT_VIDEO_CHAT = 203;  // 和主播视频电话
    public static final int DEVKEEP_COIN_OUTPUT_SEND_GIFT = 204;  // 赠送礼物
    public static final int DEVKEEP_COIN_OUTPUT_SEND_MESSAGE = 205;  // 给主播发送私信
    public static final int DEVKEEP_TICKET_INPUT_SELL_VIDEO = 2002;  // 出售短视频
    public static final int DEVKEEP_TICKET_INPUT_VIDEO_CHAT = 2003;  // 和观众视频电话
    public static final int DEVKEEP_TICKET_INPUT_RECEIVE_GIFT = 2004;  // 收到礼物
    public static final int DEVKEEP_TICKET_INPUT_RECEIVE_MESSAGE = 2005;  // 收到私信
    public static final int TICKET_OUTPUT_TESTING = 1199;  // 测试支出
    
    // 用户日常统计类型
    public static final int DAILY_STAT_CHARGE_COUNT = 1;  // 充值(次数)
    public static final int DAILY_STAT_CHARGE_AMOUNT = 2;  // 充值(金额RMB)
    public static final int DAILY_STAT_BIND_PHONE = 3;  // 绑定手机(次数)
    public static final int DAILY_STAT_SHARE_BROADCAST = 4;  // 分享直播(次数)
    public static final int DAILY_STAT_DONATE_GOLD = 5;  // 打赏主播游戏币(游戏币数量)
    public static final int DAILY_STAT_SEND_GIFT = 6;  // 赠送礼物(钻石数)
    public static final int DAILY_STAT_FEED_PET_COUNT = 7;  // 喂食宠物(次数)
    public static final int DAILY_STAT_GAME_WIN = 100;  // 玩任意游戏胜利的次数
    public static final int DAILY_STAT_GAME1_WIN = 101;  // 玩三个魔蛋胜利的次数
    
    // 用户设备定义
    public static final int DEVICE_IOS = 0;  // iOS
    public static final int DEVICE_ANDROID = 1;  // Android
    
    // 性别类型
    public static final int GENDER_NONE = 0;  // 未设置性别
    public static final int GENDER_MALE = 1;  // 男性
    public static final int GENDER_FEMALE = 2;  // 女性
    
    // 礼物动画类型
    public static final int GIFT_ANIMATION_SIMPLE = 0;  // 普通连发礼物的动画
    public static final int GIFT_ANIMATION_2D_FIXED = 1;  // 序列帧动画(播放参数固定，资源动态下载)
    
    // 货币类型
    public static final int MONEY_TYPE_DIAMOND = 0;  // 钻石
    public static final int MONEY_TYPE_GOLD = 1;  // 游戏币
    
    // 支付类型[ANDROID ONLY]
    public static final int PAY_TYPE_ALI = 1;  // 支付宝支付[ANDROID ONLY]
    public static final int PAY_TYPE_WECHAT = 2;  // 微信支付[ANDROID ONLY]
    public static final int PAY_TYPE_APPLE = 3;  // 苹果支付
    public static final int PAY_TYPE_ALIF2F_QR = 4;  // 支付宝当面付(扫码)[ANDROID ONLY]
    public static final int PAY_TYPE_WECHAT_QR = 5;  // 微信扫码支付[ANDROID ONLY]
    public static final int PAY_TYPE_WECHAT_MP = 6;  // 微信公众平台[ANDROID ONLY]
    public static final int PAY_TYPE_ALI_WAP = 7;  // 支付宝手机网站支付[ANDROID ONLY]
    
    // 个人排行榜类型
    public static final int PERSON_RANKING_TOTAL = 0;  // 日榜
    public static final int PERSON_RANKING_DAILY = 1;  // 总榜
    
    // 排行榜分类定义
    public static final int RANKING_DAY = 0;  // 日榜
    public static final int RANKING_MONTH = 1;  // 月榜
    public static final int RANKING_TOTAL = 2;  // 总榜
    
    // 下载资源类型
    public static final int RESOURCE_GIFT = 1;  // 礼物资源
    
    // 过滤类型定义[SERVER ONLY]
    public static final int STERILIZE_NONE = 0;  // 不处理
    public static final int STERILIZE_WORK_TIME = 1;  // 工作时间
    public static final int STERILIZE_WHOLE_DAY = 2;  // 全天
    
    // 上传文件的类型定义
    public static final int UPLOAD_FILE_AUTH_IMAGE = 1;  // 主播认证图片
    public static final int UPLOAD_FILE_VIDEO = 2;  // 短视频(包含举报内容)
    public static final int UPLOAD_FILE_AVATAR = 3;  // 头像
    
    // 用户行为类型
    public static final int USER_ACTION_CREATE_ACCOUNT = 1;  // 创建账号
    public static final int USER_ACTION_UPLOAD_HEAD_IMAGE = 2;  // 上传头像
    public static final int USER_ACTION_BUY_DIAMOND = 3;  // 购买钻石(消耗的人民币， 获得的钻石)
    public static final int USER_ACTION_BUY_GOLD = 4;  // 购买游戏币(消耗的人民币， 获得的钻石)
    public static final int USER_ACTION_FOLLOW = 5;  // 关注(目标uid)
    public static final int USER_ACTION_UNFOLLOW = 6;  // 取消关注(目标uid)
    public static final int USER_ACTION_BANNED = 7;  // 拉黑(目标uid)
    public static final int USER_ACTION_UNBANNED = 8;  // 取消拉黑(目标uid)
    public static final int USER_ACTION_BIND_PHONE = 9;  // 绑定手机(手机号)
    public static final int USER_ACTION_SHARE_BROADCAST = 10;  // 分享直播(目标uid, channel)
    public static final int USER_ACTION_BUG_PET = 11;  // 购买宠物(宠物id，消耗的钻石数)
    public static final int USER_ACTION_FEED_PET = 12;  // 喂食宠物(宠物id, 猫粮id, 消耗的金币)
    public static final int USER_ACTION_GAME_WIN = 13;  // 玩游戏获胜(游戏id, 主播uid, 赢得的游戏币)
    public static final int USER_ACTION_GAME_LOSE = 14;  // 玩游戏失败(游戏id, 主播uid, 损失的游戏币)
    public static final int USER_ACTION_DONATE_GOLD = 101;  // 打赏主播游戏币(目标uid, 失去的游戏币)
    public static final int USER_ACTION_RECEIVE_DONATE_GOLD = 102;  // 收到打赏的游戏币(获得的游戏币, 送礼物人的uid)
    public static final int USER_ACTION_SEND_GIFT = 103;  // 赠送礼物(目标uid, 礼物ID, 消耗的钻石)
    public static final int USER_ACTION_RECEIVE_GIFT = 104;  // 收到礼物(礼物ID, 获得的辣票, 送礼物人的uid)
    public static final int USER_ACTION_SEND_OFFLINE_GIFT = 105;  // 赠送私信礼物(目标uid, 礼物ID, 消耗的钻石)
    public static final int USER_ACTION_RECEIVE_OFFLINE_GIFT = 106;  // 收到离线礼物(礼物ID, 获得的辣票, 送礼物人的uid)
    public static final int USER_ACTION_SEND_GOLD = 107;  // 赠送游戏币(目标uid, 失去的游戏币)
    public static final int USER_ACTION_RECEIVE_GOLD = 108;  // 收取游戏币(获得的游戏币, 送礼物人的uid)
    public static final int USER_ACTION_SEND_EGG = 109;  // 给主播送魔蛋(目标uid, 消耗的钻石)
    public static final int USER_ACTION_RECEIVE_EGG = 110;  // 收到魔蛋礼物(开出的礼物id，获得的辣票, 送礼物人的id)
    
    // 用户状态定义
    public static final int USER_STATUS_NORMAL = 1;  // 正常
    public static final int USER_STATUS_BANNED = 2;  // 被禁止
    
    // [VCHAT]短视频类型定义
    public static final int VIDEO_TYPE_NEW = 1;  // 最新
    public static final int VIDEO_TYPE_DAILY_TOP = 2;  // 日榜
    public static final int VIDEO_TYPE_WEEKLY_TOP = 3;  // 周榜
    public static final int VIDEO_TYPE_MONTHLY_TOP = 4;  // 月榜
    public static final int VIDEO_TYPE_FOLLOWED_BROADCASTER = 5;  // 关注的主播的视频
    public static final int VIDEO_TYPE_FOLLOWED = 6;  // 自己关注的视频
    public static final int VIDEO_TYPE_PURCHASED = 7;  // 已购的视频
    
    // VIP卡类型定义
    public static final int VIP_CARD_WEEKLY = 1;  // 周卡
    public static final int VIP_CARD_MONTHLY = 2;  // 月卡
    public static final int VIP_CARD_QUARTERLY = 3;  // 季卡
    public static final int VIP_CARD_LIFELONG = 4;  // 终身卡
    
    // 定义协议回调接口
    public interface ACCreateApplePayOrderIdReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String orderId, String verifyUrl);
    }
    public interface ACVerifyApplePayReceiptReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(int diamond);
    }
    public interface ACPayWithWeixinReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(YueJianAppACWeixinPayDataModel data);
    }
    public interface ACDiamondItemListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACPayItemModel> items);
    }
    public interface ACBindPhoneReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(int errcode);
    }
    public interface ACGetSmsCodeReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACLoginWithPhoneReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(YueJianAppACUserPublicInfoModel user, String token);
    }
    public interface ACLoginWithThirdPartyReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(YueJianAppACUserPublicInfoModel user, String token);
    }
    public interface ACUpdateAppStateReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACGetConfigReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(YueJianAppACServerConfigModel config);
    }
    public interface ACGetAdvertisementPageReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String imageUrl, String linkUrl);
    }
    public interface ACGetBannerListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACBannerModel> banners);
    }
    public interface ACGetBlockWordsReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(int version, String blockWordsUrl);
    }
    public interface ACGetBroadcastListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACUserPublicInfoModel> broadcasts, int nextOffset);
    }
    public interface ACGetGiftListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACGiftModel> gifts);
    }
    public interface ACGetFollowingBroadcastListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACUserPublicInfoModel> broadcasts);
    }
    public interface ACIsFollowingReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(boolean isFollowing);
    }
    public interface ACFollowReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACUnfollowReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACGetFollowedUserListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACUserReleationInfoModel> users);
    }
    public interface ACGetFollowerListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACUserReleationInfoModel> users);
    }
    public interface ACInsertToBlacklistReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACRemoveFromBlacklistReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACGetBlacklistReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACUserPublicInfoModel> users);
    }
    public interface ACSearchUserReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACUserReleationInfoModel> users);
    }
    public interface ACGetUserPublicInfoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(YueJianAppACUserPublicInfoModel user);
    }
    public interface ACGetUserPrivateInfoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(YueJianAppACUserPrivateInfoModel user);
    }
    public interface ACGetUserHomepageReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(YueJianAppACUserHomepageInfoModel user);
    }
    public interface ACUpdateGenderReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(int gender);
    }
    public interface ACUpdateNicknameReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String nickname);
    }
    public interface ACUpdateSignatureReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String signature);
    }
    public interface ACUpdateLocationReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String country, String province, String city);
    }
    public interface ACBindWechatReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACPayWithAliReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String orderInfo);
    }
    public interface ACGetResourceListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACResourceModel> resources);
    }
    public interface ACGetVideoListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACVideoInfoModel> videos, int nextOffset);
    }
    public interface ACGetConsumptionInfoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACConsumptionInfoModel> consumptions, int nextOffset);
    }
    public interface ACGetChatLogReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACChatInfoModel> logs);
    }
    public interface ACFollowVideoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACUnfollowVideoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACBuyVideoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACSendGiftReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(YueJianAppACGiftModel gift);
    }
    public interface ACShareVideoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACInviteVipChatReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String liveid);
    }
    public interface ACAcceptVipChatReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACDeclineVipChatReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACQuitVipChatReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(int broadcastSeconds, int costedDiamond, int experience, String consumerUid);
    }
    public interface ACCancelVipChatReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACGetUserTagsBriefReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACUserTagModel> tags);
    }
    public interface ACGetUserTagsDetailReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACUserTaggingModel> tags, int nextOffset);
    }
    public interface ACTagUserReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACGetUserTagMetaDataReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACUserTagMetaDataModel> tags);
    }
    public interface ACGetBroadcasterVideoListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACVideoInfoModel> videos, int nextOffset);
    }
    public interface ACUploadAuthInfoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACGetSelfTagMetaDataReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACUserTagMetaDataModel> tags);
    }
    public interface ACUploadVideoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(YueJianAppACVideoInfoModel video);
    }
    public interface ACSetVideoPriceReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACDeleteVideoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACUploadAvatarReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String avatarUrl);
    }
    public interface ACGetQiniuUploadTokenReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String uploadToken);
    }
    public interface ACSetBroadcastModeReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACSetBroadcastPriceReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACChargingChatReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(int diamond);
    }
    public interface ACKpiQueryReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(int weekOnlineSeconds, int weekCallSeconds, int todayOnlineSeconds, int todayCallSeconds);
    }
    public interface ACFetchAgoraTokenReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String token);
    }
    public interface ACGetAgentPromotePosterUrlReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String imageUrl);
    }
    public interface ACGetBroadcasterShareUrlReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String shareUrl);
    }
    public interface ACUpdateGpsLocationReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACGetLiveStatusReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(int callTime, int status);
    }
    public interface ACVipChatStartReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACReportComplaintsReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACGetVipChatInfoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(int status, int broadcastSeconds, int costedDiamond, int experience, String consumerUid);
    }
    public interface ACGetLastVipChatLiveInfoReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(String liveid, YueJianAppACUserPublicInfoModel inviter, int status, int broadcastSeconds, int costedDiamond, int experience, String consumerUid);
    }
    public interface ACSaveChatMessageReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACReceivedGiftListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACGiftModel> gifts);
    }
    public interface ACChatListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACChatListModel> items);
    }
    public interface ACChatHistoryReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACChatMessageModel> messages, int nextOffset);
    }
    public interface ACDeleteChatHistoryReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACBuyVipCardReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }
    public interface ACFastReplyListReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(ArrayList<YueJianAppACFastReplyModel> replies);
    }
    public interface ACAddFastReplyReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse(int replyId);
    }
    public interface ACDeleteFastReplyReqCallback {
        void onError(int errCode, String errMessage);
        void onResponse();
    }

    /**
     * 请求创建苹果订单请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param itemId 购买的商品id.
     * @param money 商品的价格（RMB）.
     * @param itemType 商品类型(0: 钻石， 1：游戏币).
     */
    public static void sendACCreateApplePayOrderIdReq(final Activity context, String uid, String token, String itemId, int money, int itemType, final ACCreateApplePayOrderIdReqCallback callback) {
        String api_name = "tv.createApplePayOrderId";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("item_id", itemId);
        params.put("money", String.valueOf(money));
        params.put("item_type", String.valueOf(itemType));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 购买商品的订单号
                    current_field_name = "info.order_id";
                    String orderId = "";
                    if (json.has("order_id")) {
                        orderId = json.getString("order_id");
                    }
                
                    // 验证回调地址
                    current_field_name = "info.verify_url";
                    String verifyUrl = "";
                    if (json.has("verify_url")) {
                        verifyUrl = json.getString("verify_url");
                    }
                
                    callback.onResponse(orderId, verifyUrl);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACCreateApplePayOrderIdReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACCreateApplePayOrderIdReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.createApplePayOrderId接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[POST]验证支付回执请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param receipt 支付回执.
     * @param orderId 购买商品的订单号.
     */
    public static void sendACVerifyApplePayReceiptReq(final Activity context, String uid, String token, String receipt, String orderId, final ACVerifyApplePayReceiptReqCallback callback) {
        String api_name = "tv.verifyApplePayReceipt";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("receipt", receipt);
        params.put("order_id", orderId);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 当前用户拥有的钻石
                    current_field_name = "info.diamond";
                    int diamond = 0;
                    if (json.has("diamond")) {
                        diamond = json.getInt("diamond");
                    }
                
                    callback.onResponse(diamond);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACVerifyApplePayReceiptReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACVerifyApplePayReceiptReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.verifyApplePayReceipt接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求微信支付请求[ANDROID ONLY].
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param itemId 购买的商品id.
     * @param money 商品的价格（RMB）.
     * @param itemType 商品类型(0: 钻石， 1：游戏币).
     */
    public static void sendACPayWithWeixinReq(final Activity context, String uid, String token, String itemId, int money, int itemType, final ACPayWithWeixinReqCallback callback) {
        String api_name = "tv.payWithWeixin";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("item_id", itemId);
        params.put("money", String.valueOf(money));
        params.put("item_type", String.valueOf(itemType));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 支付数据
                    current_field_name = "info.data";
                    YueJianAppACWeixinPayDataModel data = new YueJianAppACWeixinPayDataModel(json.getJSONObject("data"));
            
                    callback.onResponse(data);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACPayWithWeixinReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACPayWithWeixinReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.payWithWeixin接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求游戏币钻石列表请求.
     * @param type 类型.
     * @param uid 自己的uid.
     */
    public static void sendACDiamondItemListReq(final Activity context, int type, String uid, final ACDiamondItemListReqCallback callback) {
        String api_name = "tv.diamondItemList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("type", String.valueOf(type));
        params.put("uid", uid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 钻石商品列表
                    current_field_name = "info.items";
                    ArrayList<YueJianAppACPayItemModel> items = new ArrayList<>();
                    if (json.has("items")) {
                        JSONArray itemsJson = json.getJSONArray("items");
                        for (int i = 0; i < itemsJson.length(); i++) {
                            YueJianAppACPayItemModel model = new YueJianAppACPayItemModel(itemsJson.getJSONObject(i));
                            items.add(model);
                        }
                    }
            
                    callback.onResponse(items);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACDiamondItemListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACDiamondItemListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.diamondItemList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求绑定手机请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param phone 手机号.
     * @param sms 验证码.
     */
    public static void sendACBindPhoneReq(final Activity context, String uid, String token, String phone, String sms, final ACBindPhoneReqCallback callback) {
        String api_name = "tv.bindPhone";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("phone", phone);
        params.put("sms", sms);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 错误码
                    current_field_name = "info.errcode";
                    int errcode = 0;
                    if (json.has("errcode")) {
                        errcode = json.getInt("errcode");
                    }
                
                    callback.onResponse(errcode);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACBindPhoneReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACBindPhoneReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.bindPhone接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求短信验证码请求.
     * @param phoneNumber 电话号码.
     * @param bundleId 应用的bundle_id.
     */
    public static void sendACGetSmsCodeReq(final Activity context, String phoneNumber, String bundleId, final ACGetSmsCodeReqCallback callback) {
        String api_name = "tv.getSmsCode";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("phone_number", phoneNumber);
        params.put("bundle_id", bundleId);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetSmsCodeReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetSmsCodeReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getSmsCode接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求手机账号登录请求.
     * @param phoneNumber 手机号码.
     * @param smsCode 短信验证码.
     * @param appVersion 应用版本号.
     * @param channel 当前所属渠道.
     * @param deviceModel 设备型号.
     * @param deviceId 设备识别号.
     * @param bundleId 应用的bundle_id.
     */
    public static void sendACLoginWithPhoneReq(final Activity context, String phoneNumber, String smsCode, String appVersion, String channel, String deviceModel, String deviceId, String bundleId, final ACLoginWithPhoneReqCallback callback) {
        String api_name = "tv.loginWithPhone";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("phone_number", phoneNumber);
        params.put("sms_code", smsCode);
        params.put("app_version", appVersion);
        params.put("channel", channel);
        params.put("device_model", deviceModel);
        params.put("device_id", deviceId);
        params.put("bundle_id", bundleId);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 用户列表
                    current_field_name = "info.user";
                    YueJianAppACUserPublicInfoModel user = new YueJianAppACUserPublicInfoModel(json.getJSONObject("user"));
            
                    // token
                    current_field_name = "info.token";
                    String token = "";
                    if (json.has("token")) {
                        token = json.getString("token");
                    }
                
                    callback.onResponse(user, token);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACLoginWithPhoneReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACLoginWithPhoneReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.loginWithPhone接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求第三方账号登录请求.
     * @param platform 第三方平台标识.
     * @param openid 第三方账号.
     * @param nickname 用户昵称.
     * @param avatarUrl 用户头像地址.
     * @param appVersion 应用版本号.
     * @param channel 当前所属渠道.
     * @param deviceModel 设备型号.
     * @param deviceId 设备识别号.
     * @param unionid 第三方平台的统一账号.
     * @param bundleId 应用的bundle_id.
     */
    public static void sendACLoginWithThirdPartyReq(final Activity context, String platform, String openid, String nickname, String avatarUrl, String appVersion, String channel, String deviceModel, String deviceId, String unionid, String bundleId, final ACLoginWithThirdPartyReqCallback callback) {
        String api_name = "tv.loginWithThirdParty";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("platform", platform);
        params.put("openid", openid);
        params.put("nickname", nickname);
        params.put("avatar_url", avatarUrl);
        params.put("app_version", appVersion);
        params.put("channel", channel);
        params.put("device_model", deviceModel);
        params.put("device_id", deviceId);
        params.put("unionid", unionid);
        params.put("bundle_id", bundleId);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 用户列表
                    current_field_name = "info.user";
                    YueJianAppACUserPublicInfoModel user = new YueJianAppACUserPublicInfoModel(json.getJSONObject("user"));
            
                    // token
                    current_field_name = "info.token";
                    String token = "";
                    if (json.has("token")) {
                        token = json.getString("token");
                    }
                
                    callback.onResponse(user, token);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACLoginWithThirdPartyReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACLoginWithThirdPartyReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.loginWithThirdParty接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求更新app状态请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param appVersion 应用版本号.
     * @param deviceModel 设备型号.
     * @param deviceId 设备识别号.
     */
    public static void sendACUpdateAppStateReq(final Activity context, String uid, String token, String appVersion, String deviceModel, String deviceId, final ACUpdateAppStateReqCallback callback) {
        String api_name = "tv.updateAppState";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("app_version", appVersion);
        params.put("device_model", deviceModel);
        params.put("device_id", deviceId);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACUpdateAppStateReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACUpdateAppStateReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.updateAppState接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求获取配置信息请求.
     * @param bundleId 应用的bundle_id.
     */
    public static void sendACGetConfigReq(final Activity context, String bundleId, final ACGetConfigReqCallback callback) {
        String api_name = "tv.getConfig";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("bundle_id", bundleId);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 服务器配置选项
                    current_field_name = "info.config";
                    YueJianAppACServerConfigModel config = new YueJianAppACServerConfigModel(json.getJSONObject("config"));
            
                    callback.onResponse(config);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetConfigReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetConfigReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getConfig接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求获取广告页请求.
     * @param bundleId 应用的bundle_id.
     */
    public static void sendACGetAdvertisementPageReq(final Activity context, String bundleId, final ACGetAdvertisementPageReqCallback callback) {
        String api_name = "tv.getAdvertisementPage";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("bundle_id", bundleId);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 图片地址
                    current_field_name = "info.image_url";
                    String imageUrl = "";
                    if (json.has("image_url")) {
                        imageUrl = json.getString("image_url");
                    }
                
                    // 链接地址
                    current_field_name = "info.link_url";
                    String linkUrl = "";
                    if (json.has("link_url")) {
                        linkUrl = json.getString("link_url");
                    }
                
                    callback.onResponse(imageUrl, linkUrl);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetAdvertisementPageReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetAdvertisementPageReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getAdvertisementPage接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求Banner列表请求.
     * @param bundleId 应用的bundle_id.
     * @param uid 自己的uid.
     */
    public static void sendACGetBannerListReq(final Activity context, String bundleId, String uid, final ACGetBannerListReqCallback callback) {
        String api_name = "tv.getBannerList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("bundle_id", bundleId);
        params.put("uid", uid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // Banner列表
                    current_field_name = "info.banners";
                    ArrayList<YueJianAppACBannerModel> banners = new ArrayList<>();
                    if (json.has("banners")) {
                        JSONArray bannersJson = json.getJSONArray("banners");
                        for (int i = 0; i < bannersJson.length(); i++) {
                            YueJianAppACBannerModel model = new YueJianAppACBannerModel(bannersJson.getJSONObject(i));
                            banners.add(model);
                        }
                    }
            
                    callback.onResponse(banners);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetBannerListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetBannerListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getBannerList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求获取屏蔽词列表请求.
     */
    public static void sendACGetBlockWordsReq(final Activity context, final ACGetBlockWordsReqCallback callback) {
        String api_name = "tv.getBlockWords";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 屏蔽词表版本号
                    current_field_name = "info.version";
                    int version = 0;
                    if (json.has("version")) {
                        version = json.getInt("version");
                    }
                
                    // 屏蔽词表下载地址
                    current_field_name = "info.block_words_url";
                    String blockWordsUrl = "";
                    if (json.has("block_words_url")) {
                        blockWordsUrl = json.getString("block_words_url");
                    }
                
                    callback.onResponse(version, blockWordsUrl);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetBlockWordsReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetBlockWordsReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getBlockWords接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求直播列表请求.
     * @param category 主播类型(参见 ACBroadcasterCategoryDefine).
     * @param count 请求的数量(服务器控制上限).
     * @param offset 请求偏移(首次调用时传0).
     * @param uid 自己的uid.
     * @param gender 主播的性别(参见 ACGenderTypeDefine).
     */
    public static void sendACGetBroadcastListReq(final Activity context, int category, int count, int offset, String uid, int gender, final ACGetBroadcastListReqCallback callback) {
        String api_name = "tv.getBroadcastList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("category", String.valueOf(category));
        params.put("count", String.valueOf(count));
        params.put("offset", String.valueOf(offset));
        params.put("uid", uid);
        params.put("gender", String.valueOf(gender));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 直播列表
                    current_field_name = "info.broadcasts";
                    ArrayList<YueJianAppACUserPublicInfoModel> broadcasts = new ArrayList<>();
                    if (json.has("broadcasts")) {
                        JSONArray broadcastsJson = json.getJSONArray("broadcasts");
                        for (int i = 0; i < broadcastsJson.length(); i++) {
                            YueJianAppACUserPublicInfoModel model = new YueJianAppACUserPublicInfoModel(broadcastsJson.getJSONObject(i));
                            broadcasts.add(model);
                        }
                    }
            
                    // 下一次的请求偏移
                    current_field_name = "info.next_offset";
                    int nextOffset = 0;
                    if (json.has("next_offset")) {
                        nextOffset = json.getInt("next_offset");
                    }
                
                    callback.onResponse(broadcasts, nextOffset);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetBroadcastListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetBroadcastListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getBroadcastList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求礼物列表请求.
     * @param bundleId 应用的bundle_id.
     */
    public static void sendACGetGiftListReq(final Activity context, String bundleId, final ACGetGiftListReqCallback callback) {
        String api_name = "tv.getGiftList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("bundle_id", bundleId);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 礼物列表
                    current_field_name = "info.gifts";
                    ArrayList<YueJianAppACGiftModel> gifts = new ArrayList<>();
                    if (json.has("gifts")) {
                        JSONArray giftsJson = json.getJSONArray("gifts");
                        for (int i = 0; i < giftsJson.length(); i++) {
                            YueJianAppACGiftModel model = new YueJianAppACGiftModel(giftsJson.getJSONObject(i));
                            gifts.add(model);
                        }
                    }
            
                    callback.onResponse(gifts);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetGiftListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetGiftListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getGiftList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求关注的直播列表请求.
     * @param targetUid 关注者的uid.
     */
    public static void sendACGetFollowingBroadcastListReq(final Activity context, String targetUid, final ACGetFollowingBroadcastListReqCallback callback) {
        String api_name = "tv.getFollowingBroadcastList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 直播列表
                    current_field_name = "info.broadcasts";
                    ArrayList<YueJianAppACUserPublicInfoModel> broadcasts = new ArrayList<>();
                    if (json.has("broadcasts")) {
                        JSONArray broadcastsJson = json.getJSONArray("broadcasts");
                        for (int i = 0; i < broadcastsJson.length(); i++) {
                            YueJianAppACUserPublicInfoModel model = new YueJianAppACUserPublicInfoModel(broadcastsJson.getJSONObject(i));
                            broadcasts.add(model);
                        }
                    }
            
                    callback.onResponse(broadcasts);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetFollowingBroadcastListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetFollowingBroadcastListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getFollowingBroadcastList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求是否正在关注指定主播请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 测试对象的uid.
     */
    public static void sendACIsFollowingReq(final Activity context, String uid, String token, String targetUid, final ACIsFollowingReqCallback callback) {
        String api_name = "tv.isFollowing";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 是否关注
                    current_field_name = "info.is_following";
                    boolean isFollowing = false;
                    if (json.has("is_following")) {
                        isFollowing = json.getBoolean("is_following");
                    }
                
                    callback.onResponse(isFollowing);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACIsFollowingReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACIsFollowingReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.isFollowing接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求关注主播请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 关注对象的uid.
     */
    public static void sendACFollowReq(final Activity context, String uid, String token, String targetUid, final ACFollowReqCallback callback) {
        String api_name = "tv.follow";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACFollowReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACFollowReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.follow接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求取消关注主播请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 取消关注对象的uid.
     */
    public static void sendACUnfollowReq(final Activity context, String uid, String token, String targetUid, final ACUnfollowReqCallback callback) {
        String api_name = "tv.unfollow";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACUnfollowReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACUnfollowReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.unfollow接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求获取关注的用户列表请求.
     * @param uid 自己的uid.
     * @param targetUid 任意主播的uid.
     */
    public static void sendACGetFollowedUserListReq(final Activity context, String uid, String targetUid, final ACGetFollowedUserListReqCallback callback) {
        String api_name = "tv.getFollowedUserList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 关注的用户列表
                    current_field_name = "info.users";
                    ArrayList<YueJianAppACUserReleationInfoModel> users = new ArrayList<>();
                    if (json.has("users")) {
                        JSONArray usersJson = json.getJSONArray("users");
                        for (int i = 0; i < usersJson.length(); i++) {
                            YueJianAppACUserReleationInfoModel model = new YueJianAppACUserReleationInfoModel(usersJson.getJSONObject(i));
                            users.add(model);
                        }
                    }
            
                    callback.onResponse(users);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetFollowedUserListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetFollowedUserListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getFollowedUserList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求获取粉丝列表请求.
     * @param uid 自己的uid.
     * @param targetUid 查询对象的uid.
     */
    public static void sendACGetFollowerListReq(final Activity context, String uid, String targetUid, final ACGetFollowerListReqCallback callback) {
        String api_name = "tv.getFollowerList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 粉丝列表
                    current_field_name = "info.users";
                    ArrayList<YueJianAppACUserReleationInfoModel> users = new ArrayList<>();
                    if (json.has("users")) {
                        JSONArray usersJson = json.getJSONArray("users");
                        for (int i = 0; i < usersJson.length(); i++) {
                            YueJianAppACUserReleationInfoModel model = new YueJianAppACUserReleationInfoModel(usersJson.getJSONObject(i));
                            users.add(model);
                        }
                    }
            
                    callback.onResponse(users);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetFollowerListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetFollowerListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getFollowerList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求将用户加入黑名单请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 处理对象的uid.
     */
    public static void sendACInsertToBlacklistReq(final Activity context, String uid, String token, String targetUid, final ACInsertToBlacklistReqCallback callback) {
        String api_name = "tv.insertToBlacklist";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACInsertToBlacklistReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACInsertToBlacklistReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.insertToBlacklist接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求从黑名单中移除用户请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 处理对象的uid.
     */
    public static void sendACRemoveFromBlacklistReq(final Activity context, String uid, String token, String targetUid, final ACRemoveFromBlacklistReqCallback callback) {
        String api_name = "tv.removeFromBlacklist";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACRemoveFromBlacklistReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACRemoveFromBlacklistReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.removeFromBlacklist接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求获取黑名单请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     */
    public static void sendACGetBlacklistReq(final Activity context, String uid, String token, final ACGetBlacklistReqCallback callback) {
        String api_name = "tv.getBlacklist";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 拉黑用户列表
                    current_field_name = "info.users";
                    ArrayList<YueJianAppACUserPublicInfoModel> users = new ArrayList<>();
                    if (json.has("users")) {
                        JSONArray usersJson = json.getJSONArray("users");
                        for (int i = 0; i < usersJson.length(); i++) {
                            YueJianAppACUserPublicInfoModel model = new YueJianAppACUserPublicInfoModel(usersJson.getJSONObject(i));
                            users.add(model);
                        }
                    }
            
                    callback.onResponse(users);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetBlacklistReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetBlacklistReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getBlacklist接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求查找用户请求.
     * @param myUid 自己的uid.
     * @param searchText 搜索的关键字.
     */
    public static void sendACSearchUserReq(final Activity context, String myUid, String searchText, final ACSearchUserReqCallback callback) {
        String api_name = "tv.searchUser";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("my_uid", myUid);
        params.put("search_text", searchText);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 用户列表
                    current_field_name = "info.users";
                    ArrayList<YueJianAppACUserReleationInfoModel> users = new ArrayList<>();
                    if (json.has("users")) {
                        JSONArray usersJson = json.getJSONArray("users");
                        for (int i = 0; i < usersJson.length(); i++) {
                            YueJianAppACUserReleationInfoModel model = new YueJianAppACUserReleationInfoModel(usersJson.getJSONObject(i));
                            users.add(model);
                        }
                    }
            
                    callback.onResponse(users);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACSearchUserReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACSearchUserReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.searchUser接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求用户开放信息请求.
     * @param targetUid 查询用户的uid.
     */
    public static void sendACGetUserPublicInfoReq(final Activity context, String targetUid, final ACGetUserPublicInfoReqCallback callback) {
        String api_name = "tv.getUserPublicInfo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 用户的开放信息
                    current_field_name = "info.user";
                    YueJianAppACUserPublicInfoModel user = new YueJianAppACUserPublicInfoModel(json.getJSONObject("user"));
            
                    callback.onResponse(user);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetUserPublicInfoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetUserPublicInfoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getUserPublicInfo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求用户私有信息请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     */
    public static void sendACGetUserPrivateInfoReq(final Activity context, String uid, String token, final ACGetUserPrivateInfoReqCallback callback) {
        String api_name = "tv.getUserPrivateInfo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 用户的私有信息
                    current_field_name = "info.user";
                    YueJianAppACUserPrivateInfoModel user = new YueJianAppACUserPrivateInfoModel(json.getJSONObject("user"));
            
                    callback.onResponse(user);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetUserPrivateInfoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetUserPrivateInfoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getUserPrivateInfo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求用户主页请求.
     * @param uid 自己的uid.
     * @param targetUid 查询用户的uid.
     */
    public static void sendACGetUserHomepageReq(final Activity context, String uid, String targetUid, final ACGetUserHomepageReqCallback callback) {
        String api_name = "tv.getUserHomepage";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 用户的主页信息
                    current_field_name = "info.user";
                    YueJianAppACUserHomepageInfoModel user = new YueJianAppACUserHomepageInfoModel(json.getJSONObject("user"));
            
                    callback.onResponse(user);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetUserHomepageReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetUserHomepageReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getUserHomepage接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求更新性别请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param gender 性别(参见 ACGenderTypeDefine).
     */
    public static void sendACUpdateGenderReq(final Activity context, String uid, String token, int gender, final ACUpdateGenderReqCallback callback) {
        String api_name = "tv.updateGender";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("gender", String.valueOf(gender));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 当前的性别(参见 ACGenderTypeDefine)
                    current_field_name = "info.gender";
                    int gender = 0;
                    if (json.has("gender")) {
                        gender = json.getInt("gender");
                    }
                
                    callback.onResponse(gender);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACUpdateGenderReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACUpdateGenderReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.updateGender接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求更新昵称请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param nickname 新的昵称.
     */
    public static void sendACUpdateNicknameReq(final Activity context, String uid, String token, String nickname, final ACUpdateNicknameReqCallback callback) {
        String api_name = "tv.updateNickname";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("nickname", nickname);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 当前的昵称
                    current_field_name = "info.nickname";
                    String nickname = "";
                    if (json.has("nickname")) {
                        nickname = json.getString("nickname");
                    }
                
                    callback.onResponse(nickname);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACUpdateNicknameReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACUpdateNicknameReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.updateNickname接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求更新签名请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param signature 新的签名.
     */
    public static void sendACUpdateSignatureReq(final Activity context, String uid, String token, String signature, final ACUpdateSignatureReqCallback callback) {
        String api_name = "tv.updateSignature";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("signature", signature);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 当前的签名
                    current_field_name = "info.signature";
                    String signature = "";
                    if (json.has("signature")) {
                        signature = json.getString("signature");
                    }
                
                    callback.onResponse(signature);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACUpdateSignatureReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACUpdateSignatureReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.updateSignature接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求更新位置请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param country 国家.
     * @param province 省份.
     * @param city 城市.
     */
    public static void sendACUpdateLocationReq(final Activity context, String uid, String token, String country, String province, String city, final ACUpdateLocationReqCallback callback) {
        String api_name = "tv.updateLocation";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("country", country);
        params.put("province", province);
        params.put("city", city);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 当前的国家
                    current_field_name = "info.country";
                    String country = "";
                    if (json.has("country")) {
                        country = json.getString("country");
                    }
                
                    // 当前的省份
                    current_field_name = "info.province";
                    String province = "";
                    if (json.has("province")) {
                        province = json.getString("province");
                    }
                
                    // 当前的城市
                    current_field_name = "info.city";
                    String city = "";
                    if (json.has("city")) {
                        city = json.getString("city");
                    }
                
                    callback.onResponse(country, province, city);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACUpdateLocationReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACUpdateLocationReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.updateLocation接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求绑定微信请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param unionId 微信unionid.
     */
    public static void sendACBindWechatReq(final Activity context, String uid, String token, String unionId, final ACBindWechatReqCallback callback) {
        String api_name = "tv.bindWechat";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("union_id", unionId);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACBindWechatReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACBindWechatReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.bindWechat接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求支付宝支付请求[ANDROID ONLY].
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param itemId 购买的商品id.
     * @param money 商品的价格（RMB）.
     * @param itemType 商品类型(0: 钻石， 1：游戏币).
     */
    public static void sendACPayWithAliReq(final Activity context, String uid, String token, String itemId, int money, int itemType, final ACPayWithAliReqCallback callback) {
        String api_name = "tv.payWithAli";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("item_id", itemId);
        params.put("money", String.valueOf(money));
        params.put("item_type", String.valueOf(itemType));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 支付数据
                    current_field_name = "info.order_info";
                    String orderInfo = "";
                    if (json.has("order_info")) {
                        orderInfo = json.getString("order_info");
                    }
                
                    callback.onResponse(orderInfo);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACPayWithAliReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACPayWithAliReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.payWithAli接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求获取资源列表请求.
     */
    public static void sendACGetResourceListReq(final Activity context, final ACGetResourceListReqCallback callback) {
        String api_name = "tv.getResourceList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 资源列表
                    current_field_name = "info.resources";
                    ArrayList<YueJianAppACResourceModel> resources = new ArrayList<>();
                    if (json.has("resources")) {
                        JSONArray resourcesJson = json.getJSONArray("resources");
                        for (int i = 0; i < resourcesJson.length(); i++) {
                            YueJianAppACResourceModel model = new YueJianAppACResourceModel(resourcesJson.getJSONObject(i));
                            resources.add(model);
                        }
                    }
            
                    callback.onResponse(resources);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetResourceListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetResourceListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getResourceList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取短视频列表请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param videoType 短视频分类(参见 ACVideoTypeDefine).
     * @param count 请求的记录数量.
     * @param offset 请求偏移(首次调用时传0).
     */
    public static void sendACGetVideoListReq(final Activity context, String uid, String token, int videoType, int count, int offset, final ACGetVideoListReqCallback callback) {
        String api_name = "tv.getVideoList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("video_type", String.valueOf(videoType));
        params.put("count", String.valueOf(count));
        params.put("offset", String.valueOf(offset));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 短视频列表
                    current_field_name = "info.videos";
                    ArrayList<YueJianAppACVideoInfoModel> videos = new ArrayList<>();
                    if (json.has("videos")) {
                        JSONArray videosJson = json.getJSONArray("videos");
                        for (int i = 0; i < videosJson.length(); i++) {
                            YueJianAppACVideoInfoModel model = new YueJianAppACVideoInfoModel(videosJson.getJSONObject(i));
                            videos.add(model);
                        }
                    }
            
                    // 下一次的请求偏移
                    current_field_name = "info.next_offset";
                    int nextOffset = 0;
                    if (json.has("next_offset")) {
                        nextOffset = json.getInt("next_offset");
                    }
                
                    callback.onResponse(videos, nextOffset);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetVideoListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetVideoListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getVideoList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取收支信息请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param count 请求的记录数量.
     * @param offset 请求偏移(首次调用时传0).
     */
    public static void sendACGetConsumptionInfoReq(final Activity context, String uid, String token, int count, int offset, final ACGetConsumptionInfoReqCallback callback) {
        String api_name = "tv.getConsumptionInfo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("count", String.valueOf(count));
        params.put("offset", String.valueOf(offset));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 收支列表
                    current_field_name = "info.consumptions";
                    ArrayList<YueJianAppACConsumptionInfoModel> consumptions = new ArrayList<>();
                    if (json.has("consumptions")) {
                        JSONArray consumptionsJson = json.getJSONArray("consumptions");
                        for (int i = 0; i < consumptionsJson.length(); i++) {
                            YueJianAppACConsumptionInfoModel model = new YueJianAppACConsumptionInfoModel(consumptionsJson.getJSONObject(i));
                            consumptions.add(model);
                        }
                    }
            
                    // 下一次的请求偏移
                    current_field_name = "info.next_offset";
                    int nextOffset = 0;
                    if (json.has("next_offset")) {
                        nextOffset = json.getInt("next_offset");
                    }
                
                    callback.onResponse(consumptions, nextOffset);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetConsumptionInfoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetConsumptionInfoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getConsumptionInfo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取通话记录请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     */
    public static void sendACGetChatLogReq(final Activity context, String uid, String token, final ACGetChatLogReqCallback callback) {
        String api_name = "tv.getChatLog";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 聊天记录列表
                    current_field_name = "info.logs";
                    ArrayList<YueJianAppACChatInfoModel> logs = new ArrayList<>();
                    if (json.has("logs")) {
                        JSONArray logsJson = json.getJSONArray("logs");
                        for (int i = 0; i < logsJson.length(); i++) {
                            YueJianAppACChatInfoModel model = new YueJianAppACChatInfoModel(logsJson.getJSONObject(i));
                            logs.add(model);
                        }
                    }
            
                    callback.onResponse(logs);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetChatLogReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetChatLogReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getChatLog接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]关注短视频请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param videoId 短视频Id.
     */
    public static void sendACFollowVideoReq(final Activity context, String uid, String token, int videoId, final ACFollowVideoReqCallback callback) {
        String api_name = "tv.followVideo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("video_id", String.valueOf(videoId));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACFollowVideoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACFollowVideoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.followVideo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]取消关注短视频请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param videoId 短视频Id.
     */
    public static void sendACUnfollowVideoReq(final Activity context, String uid, String token, int videoId, final ACUnfollowVideoReqCallback callback) {
        String api_name = "tv.unfollowVideo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("video_id", String.valueOf(videoId));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACUnfollowVideoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACUnfollowVideoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.unfollowVideo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]购买短视频请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param videoId 短视频Id.
     */
    public static void sendACBuyVideoReq(final Activity context, String uid, String token, int videoId, final ACBuyVideoReqCallback callback) {
        String api_name = "tv.buyVideo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("video_id", String.valueOf(videoId));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACBuyVideoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACBuyVideoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.buyVideo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求发送礼物请请求.
     * @param uid 送礼物人的uid.
     * @param token 送礼物人的token.
     * @param targetUid 收礼物人的uid.
     * @param giftId 发送的礼物编号.
     * @param giftCount 发送的礼物数量.
     * @param videoId 视频ID.
     */
    public static void sendACSendGiftReq(final Activity context, String uid, String token, String targetUid, int giftId, int giftCount, int videoId, final ACSendGiftReqCallback callback) {
        String api_name = "tv.sendGift";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);
        params.put("gift_id", String.valueOf(giftId));
        params.put("gift_count", String.valueOf(giftCount));
        params.put("video_id", String.valueOf(videoId));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 礼物的详细信息
                    current_field_name = "info.gift";
                    YueJianAppACGiftModel gift = new YueJianAppACGiftModel(json.getJSONObject("gift"));
            
                    callback.onResponse(gift);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACSendGiftReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACSendGiftReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.sendGift接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]分享短视频请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param videoId 短视频Id.
     */
    public static void sendACShareVideoReq(final Activity context, String uid, String token, int videoId, final ACShareVideoReqCallback callback) {
        String api_name = "tv.shareVideo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("video_id", String.valueOf(videoId));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACShareVideoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACShareVideoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.shareVideo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]发起私聊请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 目标用户id.
     * @param isProductEnv 是否是生产环境.
     */
    public static void sendACInviteVipChatReq(final Activity context, String uid, String token, String targetUid, boolean isProductEnv, final ACInviteVipChatReqCallback callback) {
        String api_name = "tv.inviteVipChat";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);
        params.put("is_product_env", isProductEnv ? "1" : "0");

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 直播id
                    current_field_name = "info.liveid";
                    String liveid = "";
                    if (json.has("liveid")) {
                        liveid = json.getString("liveid");
                    }
                
                    callback.onResponse(liveid);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACInviteVipChatReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACInviteVipChatReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.inviteVipChat接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]被呼叫人同意私聊请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 发起请求的用户id.
     * @param liveid 直播id.
     */
    public static void sendACAcceptVipChatReq(final Activity context, String uid, String token, String targetUid, String liveid, final ACAcceptVipChatReqCallback callback) {
        String api_name = "tv.acceptVipChat";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);
        params.put("liveid", liveid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACAcceptVipChatReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACAcceptVipChatReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.acceptVipChat接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]被呼叫人拒绝私聊请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 发起请求的用户id.
     * @param liveid 直播id.
     */
    public static void sendACDeclineVipChatReq(final Activity context, String uid, String token, String targetUid, String liveid, final ACDeclineVipChatReqCallback callback) {
        String api_name = "tv.declineVipChat";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);
        params.put("liveid", liveid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACDeclineVipChatReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACDeclineVipChatReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.declineVipChat接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]退出私聊请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param liveid 直播id.
     */
    public static void sendACQuitVipChatReq(final Activity context, String uid, String token, String liveid, final ACQuitVipChatReqCallback callback) {
        String api_name = "tv.quitVipChat";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("liveid", liveid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 直播时长
                    current_field_name = "info.broadcast_seconds";
                    int broadcastSeconds = 0;
                    if (json.has("broadcast_seconds")) {
                        broadcastSeconds = json.getInt("broadcast_seconds");
                    }
                
                    // 消耗的钻石数
                    current_field_name = "info.costed_diamond";
                    int costedDiamond = 0;
                    if (json.has("costed_diamond")) {
                        costedDiamond = json.getInt("costed_diamond");
                    }
                
                    // 观众和主播增加的亲密值
                    current_field_name = "info.experience";
                    int experience = 0;
                    if (json.has("experience")) {
                        experience = json.getInt("experience");
                    }
                
                    // 消费者的uid
                    current_field_name = "info.consumer_uid";
                    String consumerUid = "";
                    if (json.has("consumer_uid")) {
                        consumerUid = json.getString("consumer_uid");
                    }
                
                    callback.onResponse(broadcastSeconds, costedDiamond, experience, consumerUid);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACQuitVipChatReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACQuitVipChatReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.quitVipChat接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]发起人取消私聊请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 发起请求的用户id.
     * @param liveid 直播id.
     */
    public static void sendACCancelVipChatReq(final Activity context, String uid, String token, String targetUid, String liveid, final ACCancelVipChatReqCallback callback) {
        String api_name = "tv.cancelVipChat";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);
        params.put("liveid", liveid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACCancelVipChatReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACCancelVipChatReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.cancelVipChat接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]用户标签摘要请求.
     * @param uid 用户的uid.
     */
    public static void sendACGetUserTagsBriefReq(final Activity context, String uid, final ACGetUserTagsBriefReqCallback callback) {
        String api_name = "tv.getUserTagsBrief";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 标签列表
                    current_field_name = "info.tags";
                    ArrayList<YueJianAppACUserTagModel> tags = new ArrayList<>();
                    if (json.has("tags")) {
                        JSONArray tagsJson = json.getJSONArray("tags");
                        for (int i = 0; i < tagsJson.length(); i++) {
                            YueJianAppACUserTagModel model = new YueJianAppACUserTagModel(tagsJson.getJSONObject(i));
                            tags.add(model);
                        }
                    }
            
                    callback.onResponse(tags);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetUserTagsBriefReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetUserTagsBriefReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getUserTagsBrief接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]用户标签详情请求.
     * @param uid 用户的uid.
     * @param count 请求的数量(服务器控制上限).
     * @param offset 请求偏移(首次调用时传0).
     */
    public static void sendACGetUserTagsDetailReq(final Activity context, String uid, int count, int offset, final ACGetUserTagsDetailReqCallback callback) {
        String api_name = "tv.getUserTagsDetail";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("count", String.valueOf(count));
        params.put("offset", String.valueOf(offset));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 标签详情列表
                    current_field_name = "info.tags";
                    ArrayList<YueJianAppACUserTaggingModel> tags = new ArrayList<>();
                    if (json.has("tags")) {
                        JSONArray tagsJson = json.getJSONArray("tags");
                        for (int i = 0; i < tagsJson.length(); i++) {
                            YueJianAppACUserTaggingModel model = new YueJianAppACUserTaggingModel(tagsJson.getJSONObject(i));
                            tags.add(model);
                        }
                    }
            
                    // 下一次的请求偏移
                    current_field_name = "info.next_offset";
                    int nextOffset = 0;
                    if (json.has("next_offset")) {
                        nextOffset = json.getInt("next_offset");
                    }
                
                    callback.onResponse(tags, nextOffset);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetUserTagsDetailReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetUserTagsDetailReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getUserTagsDetail接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]给用户打标签请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 目标用户id.
     * @param tagId 标签id.
     */
    public static void sendACTagUserReq(final Activity context, String uid, String token, String targetUid, int tagId, final ACTagUserReqCallback callback) {
        String api_name = "tv.tagUser";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);
        params.put("tag_id", String.valueOf(tagId));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACTagUserReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACTagUserReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.tagUser接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取用户标签的元数据请求.
     * @param bundleId 应用的bundle_id.
     */
    public static void sendACGetUserTagMetaDataReq(final Activity context, String bundleId, final ACGetUserTagMetaDataReqCallback callback) {
        String api_name = "tv.getUserTagMetaData";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("bundle_id", bundleId);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 标签列表
                    current_field_name = "info.tags";
                    ArrayList<YueJianAppACUserTagMetaDataModel> tags = new ArrayList<>();
                    if (json.has("tags")) {
                        JSONArray tagsJson = json.getJSONArray("tags");
                        for (int i = 0; i < tagsJson.length(); i++) {
                            YueJianAppACUserTagMetaDataModel model = new YueJianAppACUserTagMetaDataModel(tagsJson.getJSONObject(i));
                            tags.add(model);
                        }
                    }
            
                    callback.onResponse(tags);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetUserTagMetaDataReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetUserTagMetaDataReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getUserTagMetaData接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取指定主播的短视频列表请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 目标用户id.
     * @param count 请求的数量(服务器控制上限).
     * @param offset 请求偏移(首次调用时传0).
     */
    public static void sendACGetBroadcasterVideoListReq(final Activity context, String uid, String token, String targetUid, int count, int offset, final ACGetBroadcasterVideoListReqCallback callback) {
        String api_name = "tv.getBroadcasterVideoList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);
        params.put("count", String.valueOf(count));
        params.put("offset", String.valueOf(offset));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 短视频列表
                    current_field_name = "info.videos";
                    ArrayList<YueJianAppACVideoInfoModel> videos = new ArrayList<>();
                    if (json.has("videos")) {
                        JSONArray videosJson = json.getJSONArray("videos");
                        for (int i = 0; i < videosJson.length(); i++) {
                            YueJianAppACVideoInfoModel model = new YueJianAppACVideoInfoModel(videosJson.getJSONObject(i));
                            videos.add(model);
                        }
                    }
            
                    // 下一次的请求偏移
                    current_field_name = "info.next_offset";
                    int nextOffset = 0;
                    if (json.has("next_offset")) {
                        nextOffset = json.getInt("next_offset");
                    }
                
                    callback.onResponse(videos, nextOffset);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetBroadcasterVideoListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetBroadcasterVideoListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getBroadcasterVideoList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]上传主播认证请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param nickname 用户昵称.
     * @param mobile 手机.
     * @param height 身高(cm).
     * @param weight 体重(kg).
     * @param constellation ).
     * @param city 所在城市.
     * @param introduction 个人介绍.
     * @param tags 形象标签(按位表示).
     * @param signature 个性签名.
     * @param realName 用户真实姓名.
     * @param cerNo 身份证号码.
     * @param imagesJson 图片列表(json格式).
     */
    public static void sendACUploadAuthInfoReq(final Activity context, String uid, String token, String nickname, String mobile, int height, int weight, int constellation, String city, String introduction, int tags, String signature, String realName, String cerNo, String imagesJson, final ACUploadAuthInfoReqCallback callback) {
        String api_name = "tv.uploadAuthInfo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("nickname", nickname);
        params.put("mobile", mobile);
        params.put("height", String.valueOf(height));
        params.put("weight", String.valueOf(weight));
        params.put("constellation", String.valueOf(constellation));
        params.put("city", city);
        params.put("introduction", introduction);
        params.put("tags", String.valueOf(tags));
        params.put("signature", signature);
        params.put("real_name", realName);
        params.put("cer_no", cerNo);
        params.put("images_json", imagesJson);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACUploadAuthInfoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACUploadAuthInfoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.uploadAuthInfo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取用户自评标签的元数据请求.
     * @param bundleId 应用的bundle_id.
     */
    public static void sendACGetSelfTagMetaDataReq(final Activity context, String bundleId, final ACGetSelfTagMetaDataReqCallback callback) {
        String api_name = "tv.getSelfTagMetaData";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("bundle_id", bundleId);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 标签列表
                    current_field_name = "info.tags";
                    ArrayList<YueJianAppACUserTagMetaDataModel> tags = new ArrayList<>();
                    if (json.has("tags")) {
                        JSONArray tagsJson = json.getJSONArray("tags");
                        for (int i = 0; i < tagsJson.length(); i++) {
                            YueJianAppACUserTagMetaDataModel model = new YueJianAppACUserTagMetaDataModel(tagsJson.getJSONObject(i));
                            tags.add(model);
                        }
                    }
            
                    callback.onResponse(tags);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetSelfTagMetaDataReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetSelfTagMetaDataReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getSelfTagMetaData接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]上传短视频请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param videoKey 上传视频的文件key.
     * @param coverKey 视频封面的文件key.
     * @param price 视频定价.
     */
    public static void sendACUploadVideoReq(final Activity context, String uid, String token, String videoKey, String coverKey, int price, final ACUploadVideoReqCallback callback) {
        String api_name = "tv.uploadVideo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("video_key", videoKey);
        params.put("cover_key", coverKey);
        params.put("price", String.valueOf(price));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 上传的短视频
                    current_field_name = "info.video";
                    YueJianAppACVideoInfoModel video = new YueJianAppACVideoInfoModel(json.getJSONObject("video"));
            
                    callback.onResponse(video);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACUploadVideoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACUploadVideoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.uploadVideo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]设置视频价格请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param videoId 短视频Id.
     * @param price 视频定价.
     */
    public static void sendACSetVideoPriceReq(final Activity context, String uid, String token, int videoId, int price, final ACSetVideoPriceReqCallback callback) {
        String api_name = "tv.setVideoPrice";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("video_id", String.valueOf(videoId));
        params.put("price", String.valueOf(price));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACSetVideoPriceReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACSetVideoPriceReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.setVideoPrice接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]删除短视频请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param videoId 短视频Id.
     */
    public static void sendACDeleteVideoReq(final Activity context, String uid, String token, int videoId, final ACDeleteVideoReqCallback callback) {
        String api_name = "tv.deleteVideo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("video_id", String.valueOf(videoId));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACDeleteVideoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACDeleteVideoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.deleteVideo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]上传头像请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param avatarKey 上传头像的文件key.
     */
    public static void sendACUploadAvatarReq(final Activity context, String uid, String token, String avatarKey, final ACUploadAvatarReqCallback callback) {
        String api_name = "tv.uploadAvatar";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("avatar_key", avatarKey);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 头像的下载地址
                    current_field_name = "info.avatar_url";
                    String avatarUrl = "";
                    if (json.has("avatar_url")) {
                        avatarUrl = json.getString("avatar_url");
                    }
                
                    callback.onResponse(avatarUrl);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACUploadAvatarReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACUploadAvatarReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.uploadAvatar接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取文件上传token请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param fileType 上传文件的类型(参见 ACUploadFileTypeDefine).
     */
    public static void sendACGetQiniuUploadTokenReq(final Activity context, String uid, String token, int fileType, final ACGetQiniuUploadTokenReqCallback callback) {
        String api_name = "tv.getQiniuUploadToken";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("file_type", String.valueOf(fileType));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 上传图片的token
                    current_field_name = "info.upload_token";
                    String uploadToken = "";
                    if (json.has("upload_token")) {
                        uploadToken = json.getString("upload_token");
                    }
                
                    callback.onResponse(uploadToken);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetQiniuUploadTokenReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetQiniuUploadTokenReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getQiniuUploadToken接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]设置勿扰模式请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param isBusy 是否为勿扰模式.
     */
    public static void sendACSetBroadcastModeReq(final Activity context, String uid, String token, boolean isBusy, final ACSetBroadcastModeReqCallback callback) {
        String api_name = "tv.setBroadcastMode";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("is_busy", isBusy ? "1" : "0");

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACSetBroadcastModeReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACSetBroadcastModeReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.setBroadcastMode接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]设置直播价格请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param price 视频通话每分钟价格.
     */
    public static void sendACSetBroadcastPriceReq(final Activity context, String uid, String token, int price, final ACSetBroadcastPriceReqCallback callback) {
        String api_name = "tv.setBroadcastPrice";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("price", String.valueOf(price));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACSetBroadcastPriceReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACSetBroadcastPriceReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.setBroadcastPrice接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]收费聊天请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 目标的uid.
     */
    public static void sendACChargingChatReq(final Activity context, String uid, String token, String targetUid, final ACChargingChatReqCallback callback) {
        String api_name = "tv.chargingChat";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 剩余的钻石数
                    current_field_name = "info.diamond";
                    int diamond = 0;
                    if (json.has("diamond")) {
                        diamond = json.getInt("diamond");
                    }
                
                    callback.onResponse(diamond);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACChargingChatReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACChargingChatReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.chargingChat接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]KPI考核请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     */
    public static void sendACKpiQueryReq(final Activity context, String uid, String token, final ACKpiQueryReqCallback callback) {
        String api_name = "tv.kpiQuery";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 本周在线时长(秒)
                    current_field_name = "info.week_online_seconds";
                    int weekOnlineSeconds = 0;
                    if (json.has("week_online_seconds")) {
                        weekOnlineSeconds = json.getInt("week_online_seconds");
                    }
                
                    // 本周通话时长(秒)
                    current_field_name = "info.week_call_seconds";
                    int weekCallSeconds = 0;
                    if (json.has("week_call_seconds")) {
                        weekCallSeconds = json.getInt("week_call_seconds");
                    }
                
                    // 今日累计在线时长(秒)
                    current_field_name = "info.today_online_seconds";
                    int todayOnlineSeconds = 0;
                    if (json.has("today_online_seconds")) {
                        todayOnlineSeconds = json.getInt("today_online_seconds");
                    }
                
                    // 今日累计通话时长(秒)
                    current_field_name = "info.today_call_seconds";
                    int todayCallSeconds = 0;
                    if (json.has("today_call_seconds")) {
                        todayCallSeconds = json.getInt("today_call_seconds");
                    }
                
                    callback.onResponse(weekOnlineSeconds, weekCallSeconds, todayOnlineSeconds, todayCallSeconds);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACKpiQueryReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACKpiQueryReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.kpiQuery接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取声网token请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param channelName 声网频道名称.
     */
    public static void sendACFetchAgoraTokenReq(final Activity context, String uid, String token, String channelName, final ACFetchAgoraTokenReqCallback callback) {
        String api_name = "tv.fetchAgoraToken";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("channel_name", channelName);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 声网的token
                    current_field_name = "info.token";
                    String token = "";
                    if (json.has("token")) {
                        token = json.getString("token");
                    }
                
                    callback.onResponse(token);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACFetchAgoraTokenReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACFetchAgoraTokenReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.fetchAgoraToken接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取代理推广海报地址请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     */
    public static void sendACGetAgentPromotePosterUrlReq(final Activity context, String uid, String token, final ACGetAgentPromotePosterUrlReqCallback callback) {
        String api_name = "tv.getAgentPromotePosterUrl";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 图片地址
                    current_field_name = "info.image_url";
                    String imageUrl = "";
                    if (json.has("image_url")) {
                        imageUrl = json.getString("image_url");
                    }
                
                    callback.onResponse(imageUrl);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetAgentPromotePosterUrlReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetAgentPromotePosterUrlReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getAgentPromotePosterUrl接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取主播分享地址请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     */
    public static void sendACGetBroadcasterShareUrlReq(final Activity context, String uid, String token, final ACGetBroadcasterShareUrlReqCallback callback) {
        String api_name = "tv.getBroadcasterShareUrl";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 分享地址
                    current_field_name = "info.share_url";
                    String shareUrl = "";
                    if (json.has("share_url")) {
                        shareUrl = json.getString("share_url");
                    }
                
                    callback.onResponse(shareUrl);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetBroadcasterShareUrlReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetBroadcasterShareUrlReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getBroadcasterShareUrl接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]更新GPS位置信息请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param longitude GPS定位经度.
     * @param latitude GPS定位纬度.
     */
    public static void sendACUpdateGpsLocationReq(final Activity context, String uid, String token, String longitude, String latitude, final ACUpdateGpsLocationReqCallback callback) {
        String api_name = "tv.updateGpsLocation";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("longitude", longitude);
        params.put("latitude", latitude);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACUpdateGpsLocationReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACUpdateGpsLocationReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.updateGpsLocation接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]查询通话状态的请求.
     * @param liveid 直播id.
     */
    public static void sendACGetLiveStatusReq(final Activity context, String liveid, final ACGetLiveStatusReqCallback callback) {
        String api_name = "tv.getLiveStatus";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("liveid", liveid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 拨打时间
                    current_field_name = "info.call_time";
                    int callTime = 0;
                    if (json.has("call_time")) {
                        callTime = json.getInt("call_time");
                    }
                
                    // 拨打状态(参见 ACCallStatusDefine)
                    current_field_name = "info.status";
                    int status = 0;
                    if (json.has("status")) {
                        status = json.getInt("status");
                    }
                
                    callback.onResponse(callTime, status);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetLiveStatusReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetLiveStatusReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getLiveStatus接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]私聊开始通知请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param liveid 直播id.
     */
    public static void sendACVipChatStartReq(final Activity context, String uid, String token, String liveid, final ACVipChatStartReqCallback callback) {
        String api_name = "tv.vipChatStart";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("liveid", liveid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACVipChatStartReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACVipChatStartReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.vipChatStart接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]举报投诉主播请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 举报的主播id.
     * @param title 举报标题.
     * @param desp 详情描述.
     * @param imageUrl 举报图片地址.
     */
    public static void sendACReportComplaintsReq(final Activity context, String uid, String token, String targetUid, String title, String desp, String imageUrl, final ACReportComplaintsReqCallback callback) {
        String api_name = "tv.reportComplaints";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);
        params.put("title", title);
        params.put("desp", desp);
        params.put("image_url", imageUrl);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACReportComplaintsReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACReportComplaintsReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.reportComplaints接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]查询私聊信息请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param liveid 直播id.
     */
    public static void sendACGetVipChatInfoReq(final Activity context, String uid, String token, String liveid, final ACGetVipChatInfoReqCallback callback) {
        String api_name = "tv.getVipChatInfo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("liveid", liveid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 拨打状态(参见 ACCallStatusDefine)
                    current_field_name = "info.status";
                    int status = 0;
                    if (json.has("status")) {
                        status = json.getInt("status");
                    }
                
                    // 直播时长
                    current_field_name = "info.broadcast_seconds";
                    int broadcastSeconds = 0;
                    if (json.has("broadcast_seconds")) {
                        broadcastSeconds = json.getInt("broadcast_seconds");
                    }
                
                    // 消耗的钻石数
                    current_field_name = "info.costed_diamond";
                    int costedDiamond = 0;
                    if (json.has("costed_diamond")) {
                        costedDiamond = json.getInt("costed_diamond");
                    }
                
                    // 观众和主播增加的亲密值
                    current_field_name = "info.experience";
                    int experience = 0;
                    if (json.has("experience")) {
                        experience = json.getInt("experience");
                    }
                
                    // 消费者的uid
                    current_field_name = "info.consumer_uid";
                    String consumerUid = "";
                    if (json.has("consumer_uid")) {
                        consumerUid = json.getString("consumer_uid");
                    }
                
                    callback.onResponse(status, broadcastSeconds, costedDiamond, experience, consumerUid);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetVipChatInfoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetVipChatInfoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getVipChatInfo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取最近的私聊信息请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     */
    public static void sendACGetLastVipChatLiveInfoReq(final Activity context, String uid, String token, final ACGetLastVipChatLiveInfoReqCallback callback) {
        String api_name = "tv.getLastVipChatLiveInfo";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 直播id
                    current_field_name = "info.liveid";
                    String liveid = "";
                    if (json.has("liveid")) {
                        liveid = json.getString("liveid");
                    }
                
                    // 邀请者信息
                    current_field_name = "info.inviter";
                    YueJianAppACUserPublicInfoModel inviter = new YueJianAppACUserPublicInfoModel(json.getJSONObject("inviter"));
            
                    // 拨打状态(参见 ACCallStatusDefine)
                    current_field_name = "info.status";
                    int status = 0;
                    if (json.has("status")) {
                        status = json.getInt("status");
                    }
                
                    // 直播时长
                    current_field_name = "info.broadcast_seconds";
                    int broadcastSeconds = 0;
                    if (json.has("broadcast_seconds")) {
                        broadcastSeconds = json.getInt("broadcast_seconds");
                    }
                
                    // 消耗的钻石数
                    current_field_name = "info.costed_diamond";
                    int costedDiamond = 0;
                    if (json.has("costed_diamond")) {
                        costedDiamond = json.getInt("costed_diamond");
                    }
                
                    // 观众和主播增加的亲密值
                    current_field_name = "info.experience";
                    int experience = 0;
                    if (json.has("experience")) {
                        experience = json.getInt("experience");
                    }
                
                    // 消费者的uid
                    current_field_name = "info.consumer_uid";
                    String consumerUid = "";
                    if (json.has("consumer_uid")) {
                        consumerUid = json.getString("consumer_uid");
                    }
                
                    callback.onResponse(liveid, inviter, status, broadcastSeconds, costedDiamond, experience, consumerUid);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACGetLastVipChatLiveInfoReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACGetLastVipChatLiveInfoReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.getLastVipChatLiveInfo接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]上报聊天信息请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 目标的uid.
     * @param message 用户发送的聊天内容.
     */
    public static void sendACSaveChatMessageReq(final Activity context, String uid, String token, String targetUid, String message, final ACSaveChatMessageReqCallback callback) {
        String api_name = "tv.saveChatMessage";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);
        params.put("message", message);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACSaveChatMessageReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACSaveChatMessageReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.saveChatMessage接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]收到的礼物列表请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 目标的uid.
     */
    public static void sendACReceivedGiftListReq(final Activity context, String uid, String token, String targetUid, final ACReceivedGiftListReqCallback callback) {
        String api_name = "tv.receivedGiftList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 礼物列表
                    current_field_name = "info.gifts";
                    ArrayList<YueJianAppACGiftModel> gifts = new ArrayList<>();
                    if (json.has("gifts")) {
                        JSONArray giftsJson = json.getJSONArray("gifts");
                        for (int i = 0; i < giftsJson.length(); i++) {
                            YueJianAppACGiftModel model = new YueJianAppACGiftModel(giftsJson.getJSONObject(i));
                            gifts.add(model);
                        }
                    }
            
                    callback.onResponse(gifts);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACReceivedGiftListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACReceivedGiftListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.receivedGiftList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]请求聊天列表请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     */
    public static void sendACChatListReq(final Activity context, String uid, String token, final ACChatListReqCallback callback) {
        String api_name = "tv.chatList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 聊天列表
                    current_field_name = "info.items";
                    ArrayList<YueJianAppACChatListModel> items = new ArrayList<>();
                    if (json.has("items")) {
                        JSONArray itemsJson = json.getJSONArray("items");
                        for (int i = 0; i < itemsJson.length(); i++) {
                            YueJianAppACChatListModel model = new YueJianAppACChatListModel(itemsJson.getJSONObject(i));
                            items.add(model);
                        }
                    }
            
                    callback.onResponse(items);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACChatListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACChatListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.chatList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]查询聊天记录的请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 目标的uid.
     * @param count 请求的数量.
     * @param offset 请求偏移(首次调用时传0).
     */
    public static void sendACChatHistoryReq(final Activity context, String uid, String token, String targetUid, int count, int offset, final ACChatHistoryReqCallback callback) {
        String api_name = "tv.chatHistory";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);
        params.put("count", String.valueOf(count));
        params.put("offset", String.valueOf(offset));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 最近的聊天记录
                    current_field_name = "info.messages";
                    ArrayList<YueJianAppACChatMessageModel> messages = new ArrayList<>();
                    if (json.has("messages")) {
                        JSONArray messagesJson = json.getJSONArray("messages");
                        for (int i = 0; i < messagesJson.length(); i++) {
                            YueJianAppACChatMessageModel model = new YueJianAppACChatMessageModel(messagesJson.getJSONObject(i));
                            messages.add(model);
                        }
                    }
            
                    // 下一次的请求偏移
                    current_field_name = "info.next_offset";
                    int nextOffset = 0;
                    if (json.has("next_offset")) {
                        nextOffset = json.getInt("next_offset");
                    }
                
                    callback.onResponse(messages, nextOffset);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACChatHistoryReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACChatHistoryReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.chatHistory接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]删除聊天记录的请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param targetUid 目标的uid.
     */
    public static void sendACDeleteChatHistoryReq(final Activity context, String uid, String token, String targetUid, final ACDeleteChatHistoryReqCallback callback) {
        String api_name = "tv.deleteChatHistory";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("target_uid", targetUid);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACDeleteChatHistoryReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACDeleteChatHistoryReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.deleteChatHistory接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]购买VIP卡的请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param cardType vip卡类型(参见 ACVipCardDefine).
     */
    public static void sendACBuyVipCardReq(final Activity context, String uid, String token, int cardType, final ACBuyVipCardReqCallback callback) {
        String api_name = "tv.buyVipCard";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("card_type", String.valueOf(cardType));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACBuyVipCardReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACBuyVipCardReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.buyVipCard接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]获取快速回复列表请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     */
    public static void sendACFastReplyListReq(final Activity context, String uid, String token, final ACFastReplyListReqCallback callback) {
        String api_name = "tv.fastReplyList";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 快速回复列表
                    current_field_name = "info.replies";
                    ArrayList<YueJianAppACFastReplyModel> replies = new ArrayList<>();
                    if (json.has("replies")) {
                        JSONArray repliesJson = json.getJSONArray("replies");
                        for (int i = 0; i < repliesJson.length(); i++) {
                            YueJianAppACFastReplyModel model = new YueJianAppACFastReplyModel(repliesJson.getJSONObject(i));
                            replies.add(model);
                        }
                    }
            
                    callback.onResponse(replies);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACFastReplyListReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACFastReplyListReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.fastReplyList接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]增加快速回复请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param message 新增的快捷回复内容.
     */
    public static void sendACAddFastReplyReq(final Activity context, String uid, String token, String message, final ACAddFastReplyReqCallback callback) {
        String api_name = "tv.addFastReply";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("message", message);

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    // 回复ID
                    current_field_name = "info.reply_id";
                    int replyId = 0;
                    if (json.has("reply_id")) {
                        replyId = json.getInt("reply_id");
                    }
                
                    callback.onResponse(replyId);
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACAddFastReplyReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACAddFastReplyReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.addFastReply接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }

    /**
     * 请求[VCHAT]删除快速回复请求.
     * @param uid 自己的uid.
     * @param token 自己的token.
     * @param replyId 待删除的快捷回复的ID.
     */
    public static void sendACDeleteFastReplyReq(final Activity context, String uid, String token, int replyId, final ACDeleteFastReplyReqCallback callback) {
        String api_name = "tv.deleteFastReply";

        // 构造请求参数列表
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("service", api_name);
        params.put("uid", uid);
        params.put("token", token);
        params.put("reply_id", String.valueOf(replyId));

        // 请求接口
        YueJianAppApiUtils.asyncQueryApi(context, api_name, params, new YueJianAppApiUtils.ApiAsyncQueryCallback() {
            @Override
            public void onResponse(JSONObject json) {
                String current_field_name = "";
                try {
                    callback.onResponse();
                } catch (Exception e) {
                    callback.onError(YueJianAppApiUtils.API_ERROR_INVALID_JSON_DATA, "解析ACDeleteFastReplyReqAPI接口的返回结果失败");

                    HashMap<String, Object> extension = new HashMap<>();
                    extension.put("api", "ACDeleteFastReplyReq");
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析tv.deleteFastReply接口的返回结果失败", e, extension);
                }
            }

            @Override
            public void onError(int errCode, String errMessage) {
                callback.onError(errCode, errMessage);
            }
        });
    }
}