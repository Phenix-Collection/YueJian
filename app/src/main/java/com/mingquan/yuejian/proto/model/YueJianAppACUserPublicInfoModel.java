/*
 * Copyright (C) 2017 QR Co. All rights reserved.
 *
 * Created by tool, DO NOT EDIT!!!
 */

package com.mingquan.yuejian.proto.model;

import com.mingquan.yuejian.utils.YueJianAppRavenUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * YueJianAppACUserPublicInfoModel
 */
public class YueJianAppACUserPublicInfoModel implements Serializable {
    private String uid; // uid
    private String name; // 名称
    private int gender; // 性别
    private String avatarUrl; // 头像的url
    private int level; // 等级
    private String location; // 位置
    private String signature; // 签名
    private String country; // 国家
    private boolean isBroadcaster; // 是否是主播
    private int star; // 主播的星级评定
    private int price; // 主播的聊天价格(钻石每分钟)
    private int status; // 当前状态(参见 ACBroadcasterStatusDefine)
    private int lastLoginTime; // 最后登录时间
    private int receivingRate; // 电话接通率(百分比的分子)
    private String height; // 身高(cm)
    private String weight; // 体重(kg)
    private String sign; // 星座
    private String city; // 城市
    private int userTags; // 用户印象
    private int selfTags; // 自评形象
    private ArrayList<String> showPictures; // 展示图片
    private int followerCount; // 关注者(粉丝)数量
    private String introduction; // 个人介绍
    private String statusTag; // 当前状态标签
    private int vipTime; // vip时间
    private int receivedGift; // 收到的礼物数
    private String distanceDesp; // 距离描述

    /**
     * ACUserPublicInfoModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACUserPublicInfoModel(JSONObject json) {
        // 设置默认值
        this.uid = ""; // uid
        this.name = ""; // 名称
        this.gender = 1; // 性别
        this.avatarUrl = ""; // 头像的url
        this.level = 1; // 等级
        this.location = ""; // 位置
        this.signature = ""; // 签名
        this.country = ""; // 国家
        this.isBroadcaster = false; // 是否是主播
        this.star = 0; // 主播的星级评定
        this.price = 0; // 主播的聊天价格(钻石每分钟)
        this.status = 0; // 当前状态(参见 ACBroadcasterStatusDefine)
        this.lastLoginTime = 0; // 最后登录时间
        this.receivingRate = 0; // 电话接通率(百分比的分子)
        this.height = ""; // 身高(cm)
        this.weight = ""; // 体重(kg)
        this.sign = ""; // 星座
        this.city = ""; // 城市
        this.userTags = 0; // 用户印象
        this.selfTags = 0; // 自评形象
        this.showPictures = new ArrayList<String>(); // 展示图片
        this.followerCount = 0; // 关注者(粉丝)数量
        this.introduction = ""; // 个人介绍
        this.statusTag = ""; // 当前状态标签
        this.vipTime = 0; // vip时间
        this.receivedGift = 0; // 收到的礼物数
        this.distanceDesp = ""; // 距离描述

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // uid
                current_field_name = "uid";
                if (json.has("uid")) {
                    this.uid = json.getString("uid");
                }
    
                // 名称
                current_field_name = "name";
                if (json.has("name")) {
                    this.name = json.getString("name");
                }
    
                // 性别
                current_field_name = "gender";
                if (json.has("gender")) {
                    this.gender = json.getInt("gender");
                }
    
                // 头像的url
                current_field_name = "avatar_url";
                if (json.has("avatar_url")) {
                    this.avatarUrl = json.getString("avatar_url");
                }
    
                // 等级
                current_field_name = "level";
                if (json.has("level")) {
                    this.level = json.getInt("level");
                }
    
                // 位置
                current_field_name = "location";
                if (json.has("location")) {
                    this.location = json.getString("location");
                }
    
                // 签名
                current_field_name = "signature";
                if (json.has("signature")) {
                    this.signature = json.getString("signature");
                }
    
                // 国家
                current_field_name = "country";
                if (json.has("country")) {
                    this.country = json.getString("country");
                }
    
                // 是否是主播
                current_field_name = "is_broadcaster";
                if (json.has("is_broadcaster")) {
                    this.isBroadcaster = json.getBoolean("is_broadcaster");
                }
    
                // 主播的星级评定
                current_field_name = "star";
                if (json.has("star")) {
                    this.star = json.getInt("star");
                }
    
                // 主播的聊天价格(钻石每分钟)
                current_field_name = "price";
                if (json.has("price")) {
                    this.price = json.getInt("price");
                }
    
                // 当前状态(参见 ACBroadcasterStatusDefine)
                current_field_name = "status";
                if (json.has("status")) {
                    this.status = json.getInt("status");
                }
    
                // 最后登录时间
                current_field_name = "last_login_time";
                if (json.has("last_login_time")) {
                    this.lastLoginTime = json.getInt("last_login_time");
                }
    
                // 电话接通率(百分比的分子)
                current_field_name = "receiving_rate";
                if (json.has("receiving_rate")) {
                    this.receivingRate = json.getInt("receiving_rate");
                }
    
                // 身高(cm)
                current_field_name = "height";
                if (json.has("height")) {
                    this.height = json.getString("height");
                }
    
                // 体重(kg)
                current_field_name = "weight";
                if (json.has("weight")) {
                    this.weight = json.getString("weight");
                }
    
                // 星座
                current_field_name = "sign";
                if (json.has("sign")) {
                    this.sign = json.getString("sign");
                }
    
                // 城市
                current_field_name = "city";
                if (json.has("city")) {
                    this.city = json.getString("city");
                }
    
                // 用户印象
                current_field_name = "user_tags";
                if (json.has("user_tags")) {
                    this.userTags = json.getInt("user_tags");
                }
    
                // 自评形象
                current_field_name = "self_tags";
                if (json.has("self_tags")) {
                    this.selfTags = json.getInt("self_tags");
                }
    
                // 展示图片
                current_field_name = "show_pictures";
                if (json.has("show_pictures")) {
                    JSONArray showPicturesJson = json.getJSONArray("show_pictures");
                    for (int i = 0; i < showPicturesJson.length(); i++) {
                        String model = showPicturesJson.get(i).toString();
                        this.showPictures.add(model);
                    }
        
                }
    
                // 关注者(粉丝)数量
                current_field_name = "follower_count";
                if (json.has("follower_count")) {
                    this.followerCount = json.getInt("follower_count");
                }
    
                // 个人介绍
                current_field_name = "introduction";
                if (json.has("introduction")) {
                    this.introduction = json.getString("introduction");
                }
    
                // 当前状态标签
                current_field_name = "status_tag";
                if (json.has("status_tag")) {
                    this.statusTag = json.getString("status_tag");
                }
    
                // vip时间
                current_field_name = "vip_time";
                if (json.has("vip_time")) {
                    this.vipTime = json.getInt("vip_time");
                }
    
                // 收到的礼物数
                current_field_name = "received_gift";
                if (json.has("received_gift")) {
                    this.receivedGift = json.getInt("received_gift");
                }
    
                // 距离描述
                current_field_name = "distance_desp";
                if (json.has("distance_desp")) {
                    this.distanceDesp = json.getString("distance_desp");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACUserPublicInfoModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * uid
     */
    public String getUid() { return uid; }
    public void setUid(String value) { this.uid = value; }
    
    /**
     * 名称
     */
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
    
    /**
     * 性别
     */
    public int getGender() { return gender; }
    public void setGender(int value) { this.gender = value; }
    
    /**
     * 头像的url
     */
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String value) { this.avatarUrl = value; }
    
    /**
     * 等级
     */
    public int getLevel() { return level; }
    public void setLevel(int value) { this.level = value; }
    
    /**
     * 位置
     */
    public String getLocation() { return location; }
    public void setLocation(String value) { this.location = value; }
    
    /**
     * 签名
     */
    public String getSignature() { return signature; }
    public void setSignature(String value) { this.signature = value; }
    
    /**
     * 国家
     */
    public String getCountry() { return country; }
    public void setCountry(String value) { this.country = value; }
    
    /**
     * 是否是主播
     */
    public boolean getIsBroadcaster() { return isBroadcaster; }
    public void setIsBroadcaster(boolean value) { this.isBroadcaster = value; }
    
    /**
     * 主播的星级评定
     */
    public int getStar() { return star; }
    public void setStar(int value) { this.star = value; }
    
    /**
     * 主播的聊天价格(钻石每分钟)
     */
    public int getPrice() { return price; }
    public void setPrice(int value) { this.price = value; }
    
    /**
     * 当前状态(参见 ACBroadcasterStatusDefine)
     */
    public int getStatus() { return status; }
    public void setStatus(int value) { this.status = value; }
    
    /**
     * 最后登录时间
     */
    public int getLastLoginTime() { return lastLoginTime; }
    public void setLastLoginTime(int value) { this.lastLoginTime = value; }
    
    /**
     * 电话接通率(百分比的分子)
     */
    public int getReceivingRate() { return receivingRate; }
    public void setReceivingRate(int value) { this.receivingRate = value; }
    
    /**
     * 身高(cm)
     */
    public String getHeight() { return height; }
    public void setHeight(String value) { this.height = value; }
    
    /**
     * 体重(kg)
     */
    public String getWeight() { return weight; }
    public void setWeight(String value) { this.weight = value; }
    
    /**
     * 星座
     */
    public String getSign() { return sign; }
    public void setSign(String value) { this.sign = value; }
    
    /**
     * 城市
     */
    public String getCity() { return city; }
    public void setCity(String value) { this.city = value; }
    
    /**
     * 用户印象
     */
    public int getUserTags() { return userTags; }
    public void setUserTags(int value) { this.userTags = value; }
    
    /**
     * 自评形象
     */
    public int getSelfTags() { return selfTags; }
    public void setSelfTags(int value) { this.selfTags = value; }
    
    /**
     * 展示图片
     */
    public ArrayList<String> getShowPictures() { return showPictures; }
    public void setShowPictures(ArrayList<String> value) { this.showPictures = value; }
    
    /**
     * 关注者(粉丝)数量
     */
    public int getFollowerCount() { return followerCount; }
    public void setFollowerCount(int value) { this.followerCount = value; }
    
    /**
     * 个人介绍
     */
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String value) { this.introduction = value; }
    
    /**
     * 当前状态标签
     */
    public String getStatusTag() { return statusTag; }
    public void setStatusTag(String value) { this.statusTag = value; }
    
    /**
     * vip时间
     */
    public int getVipTime() { return vipTime; }
    public void setVipTime(int value) { this.vipTime = value; }
    
    /**
     * 收到的礼物数
     */
    public int getReceivedGift() { return receivedGift; }
    public void setReceivedGift(int value) { this.receivedGift = value; }
    
    /**
     * 距离描述
     */
    public String getDistanceDesp() { return distanceDesp; }
    public void setDistanceDesp(String value) { this.distanceDesp = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACUserPublicInfoModel{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", level=" + level +
                ", location='" + location + '\'' +
                ", signature='" + signature + '\'' +
                ", country='" + country + '\'' +
                ", isBroadcaster=" + isBroadcaster +
                ", star=" + star +
                ", price=" + price +
                ", status=" + status +
                ", lastLoginTime=" + lastLoginTime +
                ", receivingRate=" + receivingRate +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", sign='" + sign + '\'' +
                ", city='" + city + '\'' +
                ", userTags=" + userTags +
                ", selfTags=" + selfTags +
                ", showPictures=" + showPictures +
                ", followerCount=" + followerCount +
                ", introduction='" + introduction + '\'' +
                ", statusTag='" + statusTag + '\'' +
                ", vipTime=" + vipTime +
                ", receivedGift=" + receivedGift +
                ", distanceDesp='" + distanceDesp + '\'' +
                '}';
    }

    
    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if((o == null) || (o.getClass() != this.getClass())) return false;
        YueJianAppACUserPublicInfoModel other= (YueJianAppACUserPublicInfoModel) o;
        return this.getUid().equals(other.getUid());
    }
}