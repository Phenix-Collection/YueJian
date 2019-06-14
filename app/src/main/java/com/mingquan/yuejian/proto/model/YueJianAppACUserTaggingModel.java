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
 * YueJianAppACUserTaggingModel
 */
public class YueJianAppACUserTaggingModel implements Serializable {
    private String uid; // 打标签用户的uid
    private String nickname; // 打标签用户的名称
    private String avatar; // 打标签用户的头像的url
    private int tagId; // 标签ID

    /**
     * ACUserTaggingModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACUserTaggingModel(JSONObject json) {
        // 设置默认值
        this.uid = ""; // 打标签用户的uid
        this.nickname = ""; // 打标签用户的名称
        this.avatar = ""; // 打标签用户的头像的url
        this.tagId = 0; // 标签ID

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 打标签用户的uid
                current_field_name = "uid";
                if (json.has("uid")) {
                    this.uid = json.getString("uid");
                }
    
                // 打标签用户的名称
                current_field_name = "nickname";
                if (json.has("nickname")) {
                    this.nickname = json.getString("nickname");
                }
    
                // 打标签用户的头像的url
                current_field_name = "avatar";
                if (json.has("avatar")) {
                    this.avatar = json.getString("avatar");
                }
    
                // 标签ID
                current_field_name = "tag_id";
                if (json.has("tag_id")) {
                    this.tagId = json.getInt("tag_id");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACUserTaggingModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 打标签用户的uid
     */
    public String getUid() { return uid; }
    public void setUid(String value) { this.uid = value; }
    
    /**
     * 打标签用户的名称
     */
    public String getNickname() { return nickname; }
    public void setNickname(String value) { this.nickname = value; }
    
    /**
     * 打标签用户的头像的url
     */
    public String getAvatar() { return avatar; }
    public void setAvatar(String value) { this.avatar = value; }
    
    /**
     * 标签ID
     */
    public int getTagId() { return tagId; }
    public void setTagId(int value) { this.tagId = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACUserTaggingModel{" +
                "uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", tagId=" + tagId +
                '}';
    }

    
}