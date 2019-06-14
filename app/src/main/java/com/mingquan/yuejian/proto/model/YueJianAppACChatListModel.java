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
 * YueJianAppACChatListModel
 */
public class YueJianAppACChatListModel implements Serializable {
    private YueJianAppACUserPublicInfoModel user; // 用户基本信息
    private YueJianAppACChatMessageModel lastMessage; // 最近的聊天记录

    /**
     * ACChatListModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACChatListModel(JSONObject json) {
        // 设置默认值
        this.user = new YueJianAppACUserPublicInfoModel(null); // 用户基本信息
        this.lastMessage = new YueJianAppACChatMessageModel(null); // 最近的聊天记录

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 用户基本信息
                current_field_name = "user";
                if (json.has("user")) {
                    this.user = new YueJianAppACUserPublicInfoModel(json.getJSONObject("user"));
        
                }
    
                // 最近的聊天记录
                current_field_name = "last_message";
                if (json.has("last_message")) {
                    this.lastMessage = new YueJianAppACChatMessageModel(json.getJSONObject("last_message"));
        
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACChatListModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 用户基本信息
     */
    public YueJianAppACUserPublicInfoModel getUser() { return user; }
    public void setUser(YueJianAppACUserPublicInfoModel value) { this.user = value; }
    
    /**
     * 最近的聊天记录
     */
    public YueJianAppACChatMessageModel getLastMessage() { return lastMessage; }
    public void setLastMessage(YueJianAppACChatMessageModel value) { this.lastMessage = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACChatListModel{" +
                "user=" + user +
                ", lastMessage=" + lastMessage +
                '}';
    }

    
}