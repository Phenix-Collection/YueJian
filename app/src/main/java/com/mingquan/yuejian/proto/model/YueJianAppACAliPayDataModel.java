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
 * YueJianAppACAliPayDataModel
 */
public class YueJianAppACAliPayDataModel implements Serializable {
    private String service; // service
    private String inputCharset; // 编码格式
    private String subject; // 商品标题
    private String paymentType; // 支付类型
    private String partner; // 支付宝签约账号
    private String sellerId; // 支付宝账号
    private String body; // 订单详细描述
    private String notifyUrl; // 回调地址
    private String signType; // 签名类型
    private String totalFee; // 该笔订单的资金总额(元)
    private String outTradeNo; // 订单号
    private String sign; // 签名

    /**
     * ACAliPayDataModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACAliPayDataModel(JSONObject json) {
        // 设置默认值
        this.service = "mobile.securitypay.pay"; // service
        this.inputCharset = "utf-8"; // 编码格式
        this.subject = ""; // 商品标题
        this.paymentType = "1"; // 支付类型
        this.partner = ""; // 支付宝签约账号
        this.sellerId = ""; // 支付宝账号
        this.body = ""; // 订单详细描述
        this.notifyUrl = ""; // 回调地址
        this.signType = "RSA"; // 签名类型
        this.totalFee = ""; // 该笔订单的资金总额(元)
        this.outTradeNo = ""; // 订单号
        this.sign = ""; // 签名

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // service
                current_field_name = "service";
                if (json.has("service")) {
                    this.service = json.getString("service");
                }
    
                // 编码格式
                current_field_name = "_input_charset";
                if (json.has("_input_charset")) {
                    this.inputCharset = json.getString("_input_charset");
                }
    
                // 商品标题
                current_field_name = "subject";
                if (json.has("subject")) {
                    this.subject = json.getString("subject");
                }
    
                // 支付类型
                current_field_name = "payment_type";
                if (json.has("payment_type")) {
                    this.paymentType = json.getString("payment_type");
                }
    
                // 支付宝签约账号
                current_field_name = "partner";
                if (json.has("partner")) {
                    this.partner = json.getString("partner");
                }
    
                // 支付宝账号
                current_field_name = "seller_id";
                if (json.has("seller_id")) {
                    this.sellerId = json.getString("seller_id");
                }
    
                // 订单详细描述
                current_field_name = "body";
                if (json.has("body")) {
                    this.body = json.getString("body");
                }
    
                // 回调地址
                current_field_name = "notify_url";
                if (json.has("notify_url")) {
                    this.notifyUrl = json.getString("notify_url");
                }
    
                // 签名类型
                current_field_name = "sign_type";
                if (json.has("sign_type")) {
                    this.signType = json.getString("sign_type");
                }
    
                // 该笔订单的资金总额(元)
                current_field_name = "total_fee";
                if (json.has("total_fee")) {
                    this.totalFee = json.getString("total_fee");
                }
    
                // 订单号
                current_field_name = "out_trade_no";
                if (json.has("out_trade_no")) {
                    this.outTradeNo = json.getString("out_trade_no");
                }
    
                // 签名
                current_field_name = "sign";
                if (json.has("sign")) {
                    this.sign = json.getString("sign");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACAliPayDataModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * service
     */
    public String getService() { return service; }
    public void setService(String value) { this.service = value; }
    
    /**
     * 编码格式
     */
    public String getInputCharset() { return inputCharset; }
    public void setInputCharset(String value) { this.inputCharset = value; }
    
    /**
     * 商品标题
     */
    public String getSubject() { return subject; }
    public void setSubject(String value) { this.subject = value; }
    
    /**
     * 支付类型
     */
    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String value) { this.paymentType = value; }
    
    /**
     * 支付宝签约账号
     */
    public String getPartner() { return partner; }
    public void setPartner(String value) { this.partner = value; }
    
    /**
     * 支付宝账号
     */
    public String getSellerId() { return sellerId; }
    public void setSellerId(String value) { this.sellerId = value; }
    
    /**
     * 订单详细描述
     */
    public String getBody() { return body; }
    public void setBody(String value) { this.body = value; }
    
    /**
     * 回调地址
     */
    public String getNotifyUrl() { return notifyUrl; }
    public void setNotifyUrl(String value) { this.notifyUrl = value; }
    
    /**
     * 签名类型
     */
    public String getSignType() { return signType; }
    public void setSignType(String value) { this.signType = value; }
    
    /**
     * 该笔订单的资金总额(元)
     */
    public String getTotalFee() { return totalFee; }
    public void setTotalFee(String value) { this.totalFee = value; }
    
    /**
     * 订单号
     */
    public String getOutTradeNo() { return outTradeNo; }
    public void setOutTradeNo(String value) { this.outTradeNo = value; }
    
    /**
     * 签名
     */
    public String getSign() { return sign; }
    public void setSign(String value) { this.sign = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACAliPayDataModel{" +
                "service='" + service + '\'' +
                ", inputCharset='" + inputCharset + '\'' +
                ", subject='" + subject + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", partner='" + partner + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", body='" + body + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", signType='" + signType + '\'' +
                ", totalFee='" + totalFee + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    
}