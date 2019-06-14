/*
 * Copyright (C) 2017 QR Co. All rights reserved.
 *
 * Created by tool, DO NOT EDIT!!!
 */

package com.mingquan.yuejian.proto.model;

import com.mingquan.yuejian.utils.YueJianAppRavenUtils;

import java.io.Serializable;
import java.util.HashMap;

import org.json.JSONObject;

/**
 * YueJianAppACVideoInfoModel
 */
public class YueJianAppACVideoInfoModel implements Serializable {
    private int videoId; // 短视频Id
    private YueJianAppACUserPublicInfoModel broadcaster; // 主播信息
    private String coverUrl; // 封面地址
    private String videoUrl; // 视频地址
    private int followerCount; // 关注数量
    private boolean hasLocked; // 是否解锁
    private boolean isFollowing; // 是否关注
    private int price; // 短视频价格
    private int watchCount; // 累计观看次数
    private int giftAmount; // 累计收取的礼物金额
    private String shareUrl; // 视频分享地址
    private int shareCount; // 视频累计的分享数量
    private int authStatus; // 是否通过审核(参见 ACAuthStatusDefine)

    /**
     * ACVideoInfoModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACVideoInfoModel(JSONObject json) {
        // 设置默认值
        this.videoId = 0; // 短视频Id
        this.broadcaster = new YueJianAppACUserPublicInfoModel(null); // 主播信息
        this.coverUrl = ""; // 封面地址
        this.videoUrl = ""; // 视频地址
        this.followerCount = 0; // 关注数量
        this.hasLocked = false; // 是否解锁
        this.isFollowing = false; // 是否关注
        this.price = 0; // 短视频价格
        this.watchCount = 0; // 累计观看次数
        this.giftAmount = 0; // 累计收取的礼物金额
        this.shareUrl = ""; // 视频分享地址
        this.shareCount = 0; // 视频累计的分享数量
        this.authStatus = 0; // 是否通过审核(参见 ACAuthStatusDefine)

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 短视频Id
                current_field_name = "video_id";
                if (json.has("video_id")) {
                    this.videoId = json.getInt("video_id");
                }
    
                // 主播信息
                current_field_name = "broadcaster";
                if (json.has("broadcaster")) {
                    this.broadcaster = new YueJianAppACUserPublicInfoModel(json.getJSONObject("broadcaster"));
        
                }
    
                // 封面地址
                current_field_name = "cover_url";
                if (json.has("cover_url")) {
                    this.coverUrl = json.getString("cover_url");
                }
    
                // 视频地址
                current_field_name = "video_url";
                if (json.has("video_url")) {
                    this.videoUrl = json.getString("video_url");
                }
    
                // 关注数量
                current_field_name = "follower_count";
                if (json.has("follower_count")) {
                    this.followerCount = json.getInt("follower_count");
                }
    
                // 是否解锁
                current_field_name = "has_locked";
                if (json.has("has_locked")) {
                    this.hasLocked = json.getBoolean("has_locked");
                }
    
                // 是否关注
                current_field_name = "is_following";
                if (json.has("is_following")) {
                    this.isFollowing = json.getBoolean("is_following");
                }
    
                // 短视频价格
                current_field_name = "price";
                if (json.has("price")) {
                    this.price = json.getInt("price");
                }
    
                // 累计观看次数
                current_field_name = "watch_count";
                if (json.has("watch_count")) {
                    this.watchCount = json.getInt("watch_count");
                }
    
                // 累计收取的礼物金额
                current_field_name = "gift_amount";
                if (json.has("gift_amount")) {
                    this.giftAmount = json.getInt("gift_amount");
                }
    
                // 视频分享地址
                current_field_name = "share_url";
                if (json.has("share_url")) {
                    this.shareUrl = json.getString("share_url");
                }
    
                // 视频累计的分享数量
                current_field_name = "share_count";
                if (json.has("share_count")) {
                    this.shareCount = json.getInt("share_count");
                }
    
                // 是否通过审核(参见 ACAuthStatusDefine)
                current_field_name = "auth_status";
                if (json.has("auth_status")) {
                    this.authStatus = json.getInt("auth_status");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACVideoInfoModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 短视频Id
     */
    public int getVideoId() { return videoId; }
    public void setVideoId(int value) { this.videoId = value; }
    
    /**
     * 主播信息
     */
    public YueJianAppACUserPublicInfoModel getBroadcaster() { return broadcaster; }
    public void setBroadcaster(YueJianAppACUserPublicInfoModel value) { this.broadcaster = value; }
    
    /**
     * 封面地址
     */
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String value) { this.coverUrl = value; }
    
    /**
     * 视频地址
     */
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String value) { this.videoUrl = value; }
    
    /**
     * 关注数量
     */
    public int getFollowerCount() { return followerCount; }
    public void setFollowerCount(int value) { this.followerCount = value; }
    
    /**
     * 是否解锁
     */
    public boolean getHasLocked() { return hasLocked; }
    public void setHasLocked(boolean value) { this.hasLocked = value; }
    
    /**
     * 是否关注
     */
    public boolean getIsFollowing() { return isFollowing; }
    public void setIsFollowing(boolean value) { this.isFollowing = value; }
    
    /**
     * 短视频价格
     */
    public int getPrice() { return price; }
    public void setPrice(int value) { this.price = value; }
    
    /**
     * 累计观看次数
     */
    public int getWatchCount() { return watchCount; }
    public void setWatchCount(int value) { this.watchCount = value; }
    
    /**
     * 累计收取的礼物金额
     */
    public int getGiftAmount() { return giftAmount; }
    public void setGiftAmount(int value) { this.giftAmount = value; }
    
    /**
     * 视频分享地址
     */
    public String getShareUrl() { return shareUrl; }
    public void setShareUrl(String value) { this.shareUrl = value; }
    
    /**
     * 视频累计的分享数量
     */
    public int getShareCount() { return shareCount; }
    public void setShareCount(int value) { this.shareCount = value; }
    
    /**
     * 是否通过审核(参见 ACAuthStatusDefine)
     */
    public int getAuthStatus() { return authStatus; }
    public void setAuthStatus(int value) { this.authStatus = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACVideoInfoModel{" +
                "videoId=" + videoId +
                ", broadcaster=" + broadcaster +
                ", coverUrl='" + coverUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", followerCount=" + followerCount +
                ", hasLocked=" + hasLocked +
                ", isFollowing=" + isFollowing +
                ", price=" + price +
                ", watchCount=" + watchCount +
                ", giftAmount=" + giftAmount +
                ", shareUrl='" + shareUrl + '\'' +
                ", shareCount=" + shareCount +
                ", authStatus=" + authStatus +
                '}';
    }

    
}