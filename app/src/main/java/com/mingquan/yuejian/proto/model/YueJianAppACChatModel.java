/*
 * Copyright (C) 2017 QR Co. All rights reserved.
 *
 * Created by tool, DO NOT EDIT!!!
 */

package com.mingquan.yuejian.proto.model;

import com.mingquan.yuejian.utils.YueJianAppRavenUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * YueJianAppACChatModel
 */
public class YueJianAppACChatModel implements Serializable {
    private YueJianAppACUserPublicInfoModel sender; // 发送者的基本信息
    private int channel; // 聊天频道(参见 ACChatChannelTypeDefine)
    private String content; // 发送的内容
    private String extraImageUrl; // 附加图片地址

    /**
     * ACChatModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACChatModel(JSONObject json) {
        // 设置默认值
        this.sender = new YueJianAppACUserPublicInfoModel(null); // 发送者的基本信息
        this.channel = 0; // 聊天频道(参见 ACChatChannelTypeDefine)
        this.content = ""; // 发送的内容
        this.extraImageUrl = ""; // 附加图片地址

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 发送者的基本信息
                current_field_name = "sender";
                if (json.has("sender")) {
                    this.sender = new YueJianAppACUserPublicInfoModel(json.getJSONObject("sender"));
        
                }
    
                // 聊天频道(参见 ACChatChannelTypeDefine)
                current_field_name = "channel";
                if (json.has("channel")) {
                    this.channel = json.getInt("channel");
                }
    
                // 发送的内容
                current_field_name = "content";
                if (json.has("content")) {
                    this.content = json.getString("content");
                }
    
                // 附加图片地址
                current_field_name = "extra_image_url";
                if (json.has("extra_image_url")) {
                    this.extraImageUrl = json.getString("extra_image_url");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACChatModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 发送者的基本信息
     */
    public YueJianAppACUserPublicInfoModel getSender() { return sender; }
    public void setSender(YueJianAppACUserPublicInfoModel value) { this.sender = value; }
    
    /**
     * 聊天频道(参见 ACChatChannelTypeDefine)
     */
    public int getChannel() { return channel; }
    public void setChannel(int value) { this.channel = value; }
    
    /**
     * 发送的内容
     */
    public String getContent() { return content; }
    public void setContent(String value) { this.content = value; }
    
    /**
     * 附加图片地址
     */
    public String getExtraImageUrl() { return extraImageUrl; }
    public void setExtraImageUrl(String value) { this.extraImageUrl = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACChatModel{" +
                "sender=" + sender +
                ", channel=" + channel +
                ", content='" + content + '\'' +
                ", extraImageUrl='" + extraImageUrl + '\'' +
                '}';
    }

    
}