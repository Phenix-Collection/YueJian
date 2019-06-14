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
 * YueJianAppACChatMessageModel
 */
public class YueJianAppACChatMessageModel implements Serializable {
    private int sendTime; // 发送时间
    private String message; // 聊天信息

    /**
     * ACChatMessageModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACChatMessageModel(JSONObject json) {
        // 设置默认值
        this.sendTime = 0; // 发送时间
        this.message = ""; // 聊天信息

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 发送时间
                current_field_name = "send_time";
                if (json.has("send_time")) {
                    this.sendTime = json.getInt("send_time");
                }
    
                // 聊天信息
                current_field_name = "message";
                if (json.has("message")) {
                    this.message = json.getString("message");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACChatMessageModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 发送时间
     */
    public int getSendTime() { return sendTime; }
    public void setSendTime(int value) { this.sendTime = value; }
    
    /**
     * 聊天信息
     */
    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACChatMessageModel{" +
                "sendTime=" + sendTime +
                ", message='" + message + '\'' +
                '}';
    }

    
}