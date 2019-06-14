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
 * YueJianAppACAboutDataModel
 */
public class YueJianAppACAboutDataModel implements Serializable {
    private String title; // 标题
    private String linkUrl; // 对应的地址

    /**
     * ACAboutDataModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACAboutDataModel(JSONObject json) {
        // 设置默认值
        this.title = ""; // 标题
        this.linkUrl = ""; // 对应的地址

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 标题
                current_field_name = "title";
                if (json.has("title")) {
                    this.title = json.getString("title");
                }
    
                // 对应的地址
                current_field_name = "link_url";
                if (json.has("link_url")) {
                    this.linkUrl = json.getString("link_url");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACAboutDataModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 标题
     */
    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }
    
    /**
     * 对应的地址
     */
    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String value) { this.linkUrl = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACAboutDataModel{" +
                "title='" + title + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                '}';
    }

    
}