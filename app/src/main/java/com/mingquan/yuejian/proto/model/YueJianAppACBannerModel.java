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
 * YueJianAppACBannerModel
 */
public class YueJianAppACBannerModel implements Serializable {
    private String imageUrl; // banner对应的图片地址
    private String linkUrl; // banner对应的链接地址

    /**
     * ACBannerModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACBannerModel(JSONObject json) {
        // 设置默认值
        this.imageUrl = ""; // banner对应的图片地址
        this.linkUrl = ""; // banner对应的链接地址

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // banner对应的图片地址
                current_field_name = "image_url";
                if (json.has("image_url")) {
                    this.imageUrl = json.getString("image_url");
                }
    
                // banner对应的链接地址
                current_field_name = "link_url";
                if (json.has("link_url")) {
                    this.linkUrl = json.getString("link_url");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACBannerModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * banner对应的图片地址
     */
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String value) { this.imageUrl = value; }
    
    /**
     * banner对应的链接地址
     */
    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String value) { this.linkUrl = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACBannerModel{" +
                "imageUrl='" + imageUrl + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                '}';
    }

    
}