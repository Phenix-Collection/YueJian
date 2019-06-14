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
 * YueJianAppACRelationModel
 */
public class YueJianAppACRelationModel implements Serializable {
    private int uid; // 用户uid
    private int targetUid; // 目标用户uid
    private boolean isFollowTarget; // 是否关注目标
    private boolean isFollowedByTarget; // 是否被目标关注
    private boolean isBlacklistTarget; // 是否拉黑目标
    private boolean isBlacklistedByTarget; // 是否被目标拉黑

    /**
     * ACRelationModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACRelationModel(JSONObject json) {
        // 设置默认值
        this.uid = 0; // 用户uid
        this.targetUid = 0; // 目标用户uid
        this.isFollowTarget = false; // 是否关注目标
        this.isFollowedByTarget = false; // 是否被目标关注
        this.isBlacklistTarget = false; // 是否拉黑目标
        this.isBlacklistedByTarget = false; // 是否被目标拉黑

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 用户uid
                current_field_name = "uid";
                if (json.has("uid")) {
                    this.uid = json.getInt("uid");
                }
    
                // 目标用户uid
                current_field_name = "target_uid";
                if (json.has("target_uid")) {
                    this.targetUid = json.getInt("target_uid");
                }
    
                // 是否关注目标
                current_field_name = "is_follow_target";
                if (json.has("is_follow_target")) {
                    this.isFollowTarget = json.getBoolean("is_follow_target");
                }
    
                // 是否被目标关注
                current_field_name = "is_followed_by_target";
                if (json.has("is_followed_by_target")) {
                    this.isFollowedByTarget = json.getBoolean("is_followed_by_target");
                }
    
                // 是否拉黑目标
                current_field_name = "is_blacklist_target";
                if (json.has("is_blacklist_target")) {
                    this.isBlacklistTarget = json.getBoolean("is_blacklist_target");
                }
    
                // 是否被目标拉黑
                current_field_name = "is_blacklisted_by_target";
                if (json.has("is_blacklisted_by_target")) {
                    this.isBlacklistedByTarget = json.getBoolean("is_blacklisted_by_target");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACRelationModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 用户uid
     */
    public int getUid() { return uid; }
    public void setUid(int value) { this.uid = value; }
    
    /**
     * 目标用户uid
     */
    public int getTargetUid() { return targetUid; }
    public void setTargetUid(int value) { this.targetUid = value; }
    
    /**
     * 是否关注目标
     */
    public boolean getIsFollowTarget() { return isFollowTarget; }
    public void setIsFollowTarget(boolean value) { this.isFollowTarget = value; }
    
    /**
     * 是否被目标关注
     */
    public boolean getIsFollowedByTarget() { return isFollowedByTarget; }
    public void setIsFollowedByTarget(boolean value) { this.isFollowedByTarget = value; }
    
    /**
     * 是否拉黑目标
     */
    public boolean getIsBlacklistTarget() { return isBlacklistTarget; }
    public void setIsBlacklistTarget(boolean value) { this.isBlacklistTarget = value; }
    
    /**
     * 是否被目标拉黑
     */
    public boolean getIsBlacklistedByTarget() { return isBlacklistedByTarget; }
    public void setIsBlacklistedByTarget(boolean value) { this.isBlacklistedByTarget = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACRelationModel{" +
                "uid=" + uid +
                ", targetUid=" + targetUid +
                ", isFollowTarget=" + isFollowTarget +
                ", isFollowedByTarget=" + isFollowedByTarget +
                ", isBlacklistTarget=" + isBlacklistTarget +
                ", isBlacklistedByTarget=" + isBlacklistedByTarget +
                '}';
    }

    
}