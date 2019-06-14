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
 * YueJianAppACWeixinPayDataModel
 */
public class YueJianAppACWeixinPayDataModel implements Serializable {
    private String appid; // 开放平台的appid
    private String wxPackage; // package
    private String partnerid; // 微信商户id
    private String noncestr; // 随机字符串
    private String prepayid; // 微信预支付订单号
    private int timestamp; // 时间戳
    private String sign; // 签名

    /**
     * ACWeixinPayDataModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACWeixinPayDataModel(JSONObject json) {
        // 设置默认值
        this.appid = ""; // 开放平台的appid
        this.wxPackage = ""; // package
        this.partnerid = ""; // 微信商户id
        this.noncestr = ""; // 随机字符串
        this.prepayid = ""; // 微信预支付订单号
        this.timestamp = 0; // 时间戳
        this.sign = ""; // 签名

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 开放平台的appid
                current_field_name = "appid";
                if (json.has("appid")) {
                    this.appid = json.getString("appid");
                }
    
                // package
                current_field_name = "wx_package";
                if (json.has("wx_package")) {
                    this.wxPackage = json.getString("wx_package");
                }
    
                // 微信商户id
                current_field_name = "partnerid";
                if (json.has("partnerid")) {
                    this.partnerid = json.getString("partnerid");
                }
    
                // 随机字符串
                current_field_name = "noncestr";
                if (json.has("noncestr")) {
                    this.noncestr = json.getString("noncestr");
                }
    
                // 微信预支付订单号
                current_field_name = "prepayid";
                if (json.has("prepayid")) {
                    this.prepayid = json.getString("prepayid");
                }
    
                // 时间戳
                current_field_name = "timestamp";
                if (json.has("timestamp")) {
                    this.timestamp = json.getInt("timestamp");
                }
    
                // 签名
                current_field_name = "sign";
                if (json.has("sign")) {
                    this.sign = json.getString("sign");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACWeixinPayDataModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 开放平台的appid
     */
    public String getAppid() { return appid; }
    public void setAppid(String value) { this.appid = value; }
    
    /**
     * package
     */
    public String getWxPackage() { return wxPackage; }
    public void setWxPackage(String value) { this.wxPackage = value; }
    
    /**
     * 微信商户id
     */
    public String getPartnerid() { return partnerid; }
    public void setPartnerid(String value) { this.partnerid = value; }
    
    /**
     * 随机字符串
     */
    public String getNoncestr() { return noncestr; }
    public void setNoncestr(String value) { this.noncestr = value; }
    
    /**
     * 微信预支付订单号
     */
    public String getPrepayid() { return prepayid; }
    public void setPrepayid(String value) { this.prepayid = value; }
    
    /**
     * 时间戳
     */
    public int getTimestamp() { return timestamp; }
    public void setTimestamp(int value) { this.timestamp = value; }
    
    /**
     * 签名
     */
    public String getSign() { return sign; }
    public void setSign(String value) { this.sign = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACWeixinPayDataModel{" +
                "appid='" + appid + '\'' +
                ", wxPackage='" + wxPackage + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", timestamp=" + timestamp +
                ", sign='" + sign + '\'' +
                '}';
    }

    
}