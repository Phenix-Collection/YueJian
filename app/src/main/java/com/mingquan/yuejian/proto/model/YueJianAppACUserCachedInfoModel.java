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
 * YueJianAppACUserCachedInfoModel
 */
public class YueJianAppACUserCachedInfoModel implements Serializable {
    private YueJianAppACUserPublicInfoModel publicData; // 用户的公开信息
    private YueJianAppACUserPrivateInfoModel privateData; // 用户的私有本信息
    private YueJianAppACUserSettingsModel settings; // 个人配置信息
    private YueJianAppACServerConfigModel serverConfig; // 服务器配置选项
    private String token; // 用户的私有本信息

    /**
     * ACUserCachedInfoModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACUserCachedInfoModel(JSONObject json) {
        // 设置默认值
        this.publicData = new YueJianAppACUserPublicInfoModel(null); // 用户的公开信息
        this.privateData = new YueJianAppACUserPrivateInfoModel(null); // 用户的私有本信息
        this.settings = new YueJianAppACUserSettingsModel(null); // 个人配置信息
        this.serverConfig = new YueJianAppACServerConfigModel(null); // 服务器配置选项
        this.token = ""; // 用户的私有本信息

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 用户的公开信息
                current_field_name = "public_data";
                if (json.has("public_data")) {
                    this.publicData = new YueJianAppACUserPublicInfoModel(json.getJSONObject("public_data"));
        
                }
    
                // 用户的私有本信息
                current_field_name = "private_data";
                if (json.has("private_data")) {
                    this.privateData = new YueJianAppACUserPrivateInfoModel(json.getJSONObject("private_data"));
        
                }
    
                // 个人配置信息
                current_field_name = "settings";
                if (json.has("settings")) {
                    this.settings = new YueJianAppACUserSettingsModel(json.getJSONObject("settings"));
        
                }
    
                // 服务器配置选项
                current_field_name = "server_config";
                if (json.has("server_config")) {
                    this.serverConfig = new YueJianAppACServerConfigModel(json.getJSONObject("server_config"));
        
                }
    
                // 用户的私有本信息
                current_field_name = "token";
                if (json.has("token")) {
                    this.token = json.getString("token");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACUserCachedInfoModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 用户的公开信息
     */
    public YueJianAppACUserPublicInfoModel getPublicData() { return publicData; }
    public void setPublicData(YueJianAppACUserPublicInfoModel value) { this.publicData = value; }
    
    /**
     * 用户的私有本信息
     */
    public YueJianAppACUserPrivateInfoModel getPrivateData() { return privateData; }
    public void setPrivateData(YueJianAppACUserPrivateInfoModel value) { this.privateData = value; }
    
    /**
     * 个人配置信息
     */
    public YueJianAppACUserSettingsModel getSettings() { return settings; }
    public void setSettings(YueJianAppACUserSettingsModel value) { this.settings = value; }
    
    /**
     * 服务器配置选项
     */
    public YueJianAppACServerConfigModel getServerConfig() { return serverConfig; }
    public void setServerConfig(YueJianAppACServerConfigModel value) { this.serverConfig = value; }
    
    /**
     * 用户的私有本信息
     */
    public String getToken() { return token; }
    public void setToken(String value) { this.token = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACUserCachedInfoModel{" +
                "publicData=" + publicData +
                ", privateData=" + privateData +
                ", settings=" + settings +
                ", serverConfig=" + serverConfig +
                ", token='" + token + '\'' +
                '}';
    }

    
}