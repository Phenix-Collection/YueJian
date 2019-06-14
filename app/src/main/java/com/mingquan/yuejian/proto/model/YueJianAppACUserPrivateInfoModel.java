/*
 * Copyright (C) 2017 QR Co. All rights reserved.
 *
 * Created by tool, DO NOT EDIT!!!
 */

package com.mingquan.yuejian.proto.model;

import com.mingquan.yuejian.utils.YueJianAppRavenUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * YueJianAppACUserPrivateInfoModel
 */
public class YueJianAppACUserPrivateInfoModel implements Serializable {
    private String uid; // uid
    private int diamond; // 钻石数
    private int ticket; // 票数
    private int blacklistSize; // 黑名单中的人数
    private String followedUsersCount; // 关注的用户数量
    private int authStatus; // 审核状态(参见 ACAuthStatusDefine)
    private String authComment; // 审核评论
    private String mobile; // 手机
    private String height; // 身高(cm)
    private String weight; // 体重(kg)
    private String sign; // 星座
    private String city; // 所在城市
    private String introduction; // 个人介绍
    private int tags; // 形象标签(按位表示)
    private String cerNo; // 身份证号码
    private String imagesJson; // 图片列表(json格式)
    private boolean isBusy; // 是否开启勿扰模式
    private int price; // 视频通话每分钟价格
    private boolean isra; // 
    private boolean canFreeTrial; // 可以免费试用
    private ArrayList<YueJianAppACExtensionBarModel> extensions; // 扩展
    private boolean powp; // 
    private boolean canVideoChat; // 

    /**
     * ACUserPrivateInfoModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACUserPrivateInfoModel(JSONObject json) {
        // 设置默认值
        this.uid = ""; // uid
        this.diamond = 0; // 钻石数
        this.ticket = 0; // 票数
        this.blacklistSize = 0; // 黑名单中的人数
        this.followedUsersCount = ""; // 关注的用户数量
        this.authStatus = 0; // 审核状态(参见 ACAuthStatusDefine)
        this.authComment = ""; // 审核评论
        this.mobile = ""; // 手机
        this.height = ""; // 身高(cm)
        this.weight = ""; // 体重(kg)
        this.sign = ""; // 星座
        this.city = ""; // 所在城市
        this.introduction = ""; // 个人介绍
        this.tags = 0; // 形象标签(按位表示)
        this.cerNo = ""; // 身份证号码
        this.imagesJson = ""; // 图片列表(json格式)
        this.isBusy = false; // 是否开启勿扰模式
        this.price = 0; // 视频通话每分钟价格
        this.isra = false; // 
        this.canFreeTrial = false; // 可以免费试用
        this.extensions = new ArrayList<YueJianAppACExtensionBarModel>(); // 扩展
        this.powp = false; // 
        this.canVideoChat = false; // 

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // uid
                current_field_name = "uid";
                if (json.has("uid")) {
                    this.uid = json.getString("uid");
                }
    
                // 钻石数
                current_field_name = "diamond";
                if (json.has("diamond")) {
                    this.diamond = json.getInt("diamond");
                }
    
                // 票数
                current_field_name = "ticket";
                if (json.has("ticket")) {
                    this.ticket = json.getInt("ticket");
                }
    
                // 黑名单中的人数
                current_field_name = "blacklist_size";
                if (json.has("blacklist_size")) {
                    this.blacklistSize = json.getInt("blacklist_size");
                }
    
                // 关注的用户数量
                current_field_name = "followed_users_count";
                if (json.has("followed_users_count")) {
                    this.followedUsersCount = json.getString("followed_users_count");
                }
    
                // 审核状态(参见 ACAuthStatusDefine)
                current_field_name = "auth_status";
                if (json.has("auth_status")) {
                    this.authStatus = json.getInt("auth_status");
                }
    
                // 审核评论
                current_field_name = "auth_comment";
                if (json.has("auth_comment")) {
                    this.authComment = json.getString("auth_comment");
                }
    
                // 手机
                current_field_name = "mobile";
                if (json.has("mobile")) {
                    this.mobile = json.getString("mobile");
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
    
                // 所在城市
                current_field_name = "city";
                if (json.has("city")) {
                    this.city = json.getString("city");
                }
    
                // 个人介绍
                current_field_name = "introduction";
                if (json.has("introduction")) {
                    this.introduction = json.getString("introduction");
                }
    
                // 形象标签(按位表示)
                current_field_name = "tags";
                if (json.has("tags")) {
                    this.tags = json.getInt("tags");
                }
    
                // 身份证号码
                current_field_name = "cer_no";
                if (json.has("cer_no")) {
                    this.cerNo = json.getString("cer_no");
                }
    
                // 图片列表(json格式)
                current_field_name = "images_json";
                if (json.has("images_json")) {
                    this.imagesJson = json.getString("images_json");
                }
    
                // 是否开启勿扰模式
                current_field_name = "is_busy";
                if (json.has("is_busy")) {
                    this.isBusy = json.getBoolean("is_busy");
                }
    
                // 视频通话每分钟价格
                current_field_name = "price";
                if (json.has("price")) {
                    this.price = json.getInt("price");
                }
    
                // 
                current_field_name = "isra";
                if (json.has("isra")) {
                    this.isra = json.getBoolean("isra");
                }
    
                // 可以免费试用
                current_field_name = "can_free_trial";
                if (json.has("can_free_trial")) {
                    this.canFreeTrial = json.getBoolean("can_free_trial");
                }
    
                // 扩展
                current_field_name = "extensions";
                if (json.has("extensions")) {
                    JSONArray extensionsJson = json.getJSONArray("extensions");
                    for (int i = 0; i < extensionsJson.length(); i++) {
                        YueJianAppACExtensionBarModel model = new YueJianAppACExtensionBarModel(extensionsJson.getJSONObject(i));
                        this.extensions.add(model);
                    }
        
                }
    
                // 
                current_field_name = "powp";
                if (json.has("powp")) {
                    this.powp = json.getBoolean("powp");
                }
    
                // 
                current_field_name = "can_video_chat";
                if (json.has("can_video_chat")) {
                    this.canVideoChat = json.getBoolean("can_video_chat");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACUserPrivateInfoModel");
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
     * 钻石数
     */
    public int getDiamond() { return diamond; }
    public void setDiamond(int value) { this.diamond = value; }
    
    /**
     * 票数
     */
    public int getTicket() { return ticket; }
    public void setTicket(int value) { this.ticket = value; }
    
    /**
     * 黑名单中的人数
     */
    public int getBlacklistSize() { return blacklistSize; }
    public void setBlacklistSize(int value) { this.blacklistSize = value; }
    
    /**
     * 关注的用户数量
     */
    public String getFollowedUsersCount() { return followedUsersCount; }
    public void setFollowedUsersCount(String value) { this.followedUsersCount = value; }
    
    /**
     * 审核状态(参见 ACAuthStatusDefine)
     */
    public int getAuthStatus() { return authStatus; }
    public void setAuthStatus(int value) { this.authStatus = value; }
    
    /**
     * 审核评论
     */
    public String getAuthComment() { return authComment; }
    public void setAuthComment(String value) { this.authComment = value; }
    
    /**
     * 手机
     */
    public String getMobile() { return mobile; }
    public void setMobile(String value) { this.mobile = value; }
    
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
     * 所在城市
     */
    public String getCity() { return city; }
    public void setCity(String value) { this.city = value; }
    
    /**
     * 个人介绍
     */
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String value) { this.introduction = value; }
    
    /**
     * 形象标签(按位表示)
     */
    public int getTags() { return tags; }
    public void setTags(int value) { this.tags = value; }
    
    /**
     * 身份证号码
     */
    public String getCerNo() { return cerNo; }
    public void setCerNo(String value) { this.cerNo = value; }
    
    /**
     * 图片列表(json格式)
     */
    public String getImagesJson() { return imagesJson; }
    public void setImagesJson(String value) { this.imagesJson = value; }
    
    /**
     * 是否开启勿扰模式
     */
    public boolean getIsBusy() { return isBusy; }
    public void setIsBusy(boolean value) { this.isBusy = value; }
    
    /**
     * 视频通话每分钟价格
     */
    public int getPrice() { return price; }
    public void setPrice(int value) { this.price = value; }
    
    /**
     * 
     */
    public boolean getIsra() { return isra; }
    public void setIsra(boolean value) { this.isra = value; }
    
    /**
     * 可以免费试用
     */
    public boolean getCanFreeTrial() { return canFreeTrial; }
    public void setCanFreeTrial(boolean value) { this.canFreeTrial = value; }
    
    /**
     * 扩展
     */
    public ArrayList<YueJianAppACExtensionBarModel> getExtensions() { return extensions; }
    public void setExtensions(ArrayList<YueJianAppACExtensionBarModel> value) { this.extensions = value; }
    
    /**
     * 
     */
    public boolean getPowp() { return powp; }
    public void setPowp(boolean value) { this.powp = value; }
    
    /**
     * 
     */
    public boolean getCanVideoChat() { return canVideoChat; }
    public void setCanVideoChat(boolean value) { this.canVideoChat = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACUserPrivateInfoModel{" +
                "uid='" + uid + '\'' +
                ", diamond=" + diamond +
                ", ticket=" + ticket +
                ", blacklistSize=" + blacklistSize +
                ", followedUsersCount='" + followedUsersCount + '\'' +
                ", authStatus=" + authStatus +
                ", authComment='" + authComment + '\'' +
                ", mobile='" + mobile + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", sign='" + sign + '\'' +
                ", city='" + city + '\'' +
                ", introduction='" + introduction + '\'' +
                ", tags=" + tags +
                ", cerNo='" + cerNo + '\'' +
                ", imagesJson='" + imagesJson + '\'' +
                ", isBusy=" + isBusy +
                ", price=" + price +
                ", isra=" + isra +
                ", canFreeTrial=" + canFreeTrial +
                ", extensions=" + extensions +
                ", powp=" + powp +
                ", canVideoChat=" + canVideoChat +
                '}';
    }

    
}