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
 * YueJianAppACFastReplyModel
 */
public class YueJianAppACFastReplyModel implements Serializable {
    private int replyId; // 回复ID
    private String message; // 内容

    /**
     * ACFastReplyModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACFastReplyModel(JSONObject json) {
        // 设置默认值
        this.replyId = 0; // 回复ID
        this.message = ""; // 内容

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 回复ID
                current_field_name = "reply_id";
                if (json.has("reply_id")) {
                    this.replyId = json.getInt("reply_id");
                }
    
                // 内容
                current_field_name = "message";
                if (json.has("message")) {
                    this.message = json.getString("message");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACFastReplyModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 回复ID
     */
    public int getReplyId() { return replyId; }
    public void setReplyId(int value) { this.replyId = value; }
    
    /**
     * 内容
     */
    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACFastReplyModel{" +
                "replyId=" + replyId +
                ", message='" + message + '\'' +
                '}';
    }

    
}