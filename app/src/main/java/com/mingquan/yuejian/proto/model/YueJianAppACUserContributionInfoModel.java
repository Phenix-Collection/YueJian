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
 * YueJianAppACUserContributionInfoModel
 */
public class YueJianAppACUserContributionInfoModel implements Serializable {
    private YueJianAppACUserPublicInfoModel user; // 用户基本信息
    private int diamond; // 贡献的钻石数

    /**
     * ACUserContributionInfoModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACUserContributionInfoModel(JSONObject json) {
        // 设置默认值
        this.user = new YueJianAppACUserPublicInfoModel(null); // 用户基本信息
        this.diamond = 0; // 贡献的钻石数

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 用户基本信息
                current_field_name = "user";
                if (json.has("user")) {
                    this.user = new YueJianAppACUserPublicInfoModel(json.getJSONObject("user"));
        
                }
    
                // 贡献的钻石数
                current_field_name = "diamond";
                if (json.has("diamond")) {
                    this.diamond = json.getInt("diamond");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACUserContributionInfoModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 用户基本信息
     */
    public YueJianAppACUserPublicInfoModel getUser() { return user; }
    public void setUser(YueJianAppACUserPublicInfoModel value) { this.user = value; }
    
    /**
     * 贡献的钻石数
     */
    public int getDiamond() { return diamond; }
    public void setDiamond(int value) { this.diamond = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACUserContributionInfoModel{" +
                "user=" + user +
                ", diamond=" + diamond +
                '}';
    }

    
}