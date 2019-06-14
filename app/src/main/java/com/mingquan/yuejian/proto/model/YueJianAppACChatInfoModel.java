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
 * YueJianAppACChatInfoModel
 */
public class YueJianAppACChatInfoModel implements Serializable {
    private int chatTime; // 聊天起始时间
    private YueJianAppACUserPublicInfoModel target; // 聊天目标
    private int chatState; // 聊天状态(参见 ACCallStatusDefine)
    private int chatSeconds; // 聊天时长
    private int expense; // 聊天费用(钻石)

    /**
     * ACChatInfoModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACChatInfoModel(JSONObject json) {
        // 设置默认值
        this.chatTime = 0; // 聊天起始时间
        this.target = new YueJianAppACUserPublicInfoModel(null); // 聊天目标
        this.chatState = 0; // 聊天状态(参见 ACCallStatusDefine)
        this.chatSeconds = 0; // 聊天时长
        this.expense = 0; // 聊天费用(钻石)

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 聊天起始时间
                current_field_name = "chat_time";
                if (json.has("chat_time")) {
                    this.chatTime = json.getInt("chat_time");
                }
    
                // 聊天目标
                current_field_name = "target";
                if (json.has("target")) {
                    this.target = new YueJianAppACUserPublicInfoModel(json.getJSONObject("target"));
        
                }
    
                // 聊天状态(参见 ACCallStatusDefine)
                current_field_name = "chat_state";
                if (json.has("chat_state")) {
                    this.chatState = json.getInt("chat_state");
                }
    
                // 聊天时长
                current_field_name = "chat_seconds";
                if (json.has("chat_seconds")) {
                    this.chatSeconds = json.getInt("chat_seconds");
                }
    
                // 聊天费用(钻石)
                current_field_name = "expense";
                if (json.has("expense")) {
                    this.expense = json.getInt("expense");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACChatInfoModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 聊天起始时间
     */
    public int getChatTime() { return chatTime; }
    public void setChatTime(int value) { this.chatTime = value; }
    
    /**
     * 聊天目标
     */
    public YueJianAppACUserPublicInfoModel getTarget() { return target; }
    public void setTarget(YueJianAppACUserPublicInfoModel value) { this.target = value; }
    
    /**
     * 聊天状态(参见 ACCallStatusDefine)
     */
    public int getChatState() { return chatState; }
    public void setChatState(int value) { this.chatState = value; }
    
    /**
     * 聊天时长
     */
    public int getChatSeconds() { return chatSeconds; }
    public void setChatSeconds(int value) { this.chatSeconds = value; }
    
    /**
     * 聊天费用(钻石)
     */
    public int getExpense() { return expense; }
    public void setExpense(int value) { this.expense = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACChatInfoModel{" +
                "chatTime=" + chatTime +
                ", target=" + target +
                ", chatState=" + chatState +
                ", chatSeconds=" + chatSeconds +
                ", expense=" + expense +
                '}';
    }

    
}