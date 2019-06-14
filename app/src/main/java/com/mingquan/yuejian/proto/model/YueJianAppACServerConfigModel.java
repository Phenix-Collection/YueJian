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
 * YueJianAppACServerConfigModel
 */
public class YueJianAppACServerConfigModel implements Serializable {
    private String wechatShareUrl; // 微信分享地址
    private String chatServer; // 聊天服务器的地址
    private String agreementUrl; // 服务条款地址
    private int chargingChatFee; // 收费聊天的单笔费用(单位为钻石)
    private String broadcastPriceJson; // 直播价格(json格式)
    private int iMinBuildNo; // i最低要求的build号
    private int iCurrentBuildNo; // i当前build号
    private String iDownloadUrl; // i下载地址
    private int aMinBuildNo; // a最低要求的build号
    private int aCurrentBuildNo; // a当前build号
    private String aDownloadUrl; // a下载地址

    /**
     * ACServerConfigModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACServerConfigModel(JSONObject json) {
        // 设置默认值
        this.wechatShareUrl = ""; // 微信分享地址
        this.chatServer = ""; // 聊天服务器的地址
        this.agreementUrl = ""; // 服务条款地址
        this.chargingChatFee = 0; // 收费聊天的单笔费用(单位为钻石)
        this.broadcastPriceJson = ""; // 直播价格(json格式)
        this.iMinBuildNo = 0; // i最低要求的build号
        this.iCurrentBuildNo = 0; // i当前build号
        this.iDownloadUrl = ""; // i下载地址
        this.aMinBuildNo = 0; // a最低要求的build号
        this.aCurrentBuildNo = 0; // a当前build号
        this.aDownloadUrl = ""; // a下载地址

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 微信分享地址
                current_field_name = "wechat_share_url";
                if (json.has("wechat_share_url")) {
                    this.wechatShareUrl = json.getString("wechat_share_url");
                }
    
                // 聊天服务器的地址
                current_field_name = "chat_server";
                if (json.has("chat_server")) {
                    this.chatServer = json.getString("chat_server");
                }
    
                // 服务条款地址
                current_field_name = "agreement_url";
                if (json.has("agreement_url")) {
                    this.agreementUrl = json.getString("agreement_url");
                }
    
                // 收费聊天的单笔费用(单位为钻石)
                current_field_name = "charging_chat_fee";
                if (json.has("charging_chat_fee")) {
                    this.chargingChatFee = json.getInt("charging_chat_fee");
                }
    
                // 直播价格(json格式)
                current_field_name = "broadcast_price_json";
                if (json.has("broadcast_price_json")) {
                    this.broadcastPriceJson = json.getString("broadcast_price_json");
                }
    
                // i最低要求的build号
                current_field_name = "i_min_build_no";
                if (json.has("i_min_build_no")) {
                    this.iMinBuildNo = json.getInt("i_min_build_no");
                }
    
                // i当前build号
                current_field_name = "i_current_build_no";
                if (json.has("i_current_build_no")) {
                    this.iCurrentBuildNo = json.getInt("i_current_build_no");
                }
    
                // i下载地址
                current_field_name = "i_download_url";
                if (json.has("i_download_url")) {
                    this.iDownloadUrl = json.getString("i_download_url");
                }
    
                // a最低要求的build号
                current_field_name = "a_min_build_no";
                if (json.has("a_min_build_no")) {
                    this.aMinBuildNo = json.getInt("a_min_build_no");
                }
    
                // a当前build号
                current_field_name = "a_current_build_no";
                if (json.has("a_current_build_no")) {
                    this.aCurrentBuildNo = json.getInt("a_current_build_no");
                }
    
                // a下载地址
                current_field_name = "a_download_url";
                if (json.has("a_download_url")) {
                    this.aDownloadUrl = json.getString("a_download_url");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACServerConfigModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 微信分享地址
     */
    public String getWechatShareUrl() { return wechatShareUrl; }
    public void setWechatShareUrl(String value) { this.wechatShareUrl = value; }
    
    /**
     * 聊天服务器的地址
     */
    public String getChatServer() { return chatServer; }
    public void setChatServer(String value) { this.chatServer = value; }
    
    /**
     * 服务条款地址
     */
    public String getAgreementUrl() { return agreementUrl; }
    public void setAgreementUrl(String value) { this.agreementUrl = value; }
    
    /**
     * 收费聊天的单笔费用(单位为钻石)
     */
    public int getChargingChatFee() { return chargingChatFee; }
    public void setChargingChatFee(int value) { this.chargingChatFee = value; }
    
    /**
     * 直播价格(json格式)
     */
    public String getBroadcastPriceJson() { return broadcastPriceJson; }
    public void setBroadcastPriceJson(String value) { this.broadcastPriceJson = value; }
    
    /**
     * i最低要求的build号
     */
    public int getIMinBuildNo() { return iMinBuildNo; }
    public void setIMinBuildNo(int value) { this.iMinBuildNo = value; }
    
    /**
     * i当前build号
     */
    public int getICurrentBuildNo() { return iCurrentBuildNo; }
    public void setICurrentBuildNo(int value) { this.iCurrentBuildNo = value; }
    
    /**
     * i下载地址
     */
    public String getIDownloadUrl() { return iDownloadUrl; }
    public void setIDownloadUrl(String value) { this.iDownloadUrl = value; }
    
    /**
     * a最低要求的build号
     */
    public int getAMinBuildNo() { return aMinBuildNo; }
    public void setAMinBuildNo(int value) { this.aMinBuildNo = value; }
    
    /**
     * a当前build号
     */
    public int getACurrentBuildNo() { return aCurrentBuildNo; }
    public void setACurrentBuildNo(int value) { this.aCurrentBuildNo = value; }
    
    /**
     * a下载地址
     */
    public String getADownloadUrl() { return aDownloadUrl; }
    public void setADownloadUrl(String value) { this.aDownloadUrl = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACServerConfigModel{" +
                "wechatShareUrl='" + wechatShareUrl + '\'' +
                ", chatServer='" + chatServer + '\'' +
                ", agreementUrl='" + agreementUrl + '\'' +
                ", chargingChatFee=" + chargingChatFee +
                ", broadcastPriceJson='" + broadcastPriceJson + '\'' +
                ", iMinBuildNo=" + iMinBuildNo +
                ", iCurrentBuildNo=" + iCurrentBuildNo +
                ", iDownloadUrl='" + iDownloadUrl + '\'' +
                ", aMinBuildNo=" + aMinBuildNo +
                ", aCurrentBuildNo=" + aCurrentBuildNo +
                ", aDownloadUrl='" + aDownloadUrl + '\'' +
                '}';
    }

    
}