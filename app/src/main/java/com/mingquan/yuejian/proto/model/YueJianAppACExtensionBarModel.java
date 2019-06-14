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
 * YueJianAppACExtensionBarModel
 */
public class YueJianAppACExtensionBarModel implements Serializable {
    private String name; // 名称
    private String content; // 内容
    private String iconUrl; // 图标
    private String linkUrl; // 链接

    /**
     * ACExtensionBarModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACExtensionBarModel(JSONObject json) {
        // 设置默认值
        this.name = ""; // 名称
        this.content = ""; // 内容
        this.iconUrl = ""; // 图标
        this.linkUrl = ""; // 链接

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 名称
                current_field_name = "name";
                if (json.has("name")) {
                    this.name = json.getString("name");
                }
    
                // 内容
                current_field_name = "content";
                if (json.has("content")) {
                    this.content = json.getString("content");
                }
    
                // 图标
                current_field_name = "icon_url";
                if (json.has("icon_url")) {
                    this.iconUrl = json.getString("icon_url");
                }
    
                // 链接
                current_field_name = "link_url";
                if (json.has("link_url")) {
                    this.linkUrl = json.getString("link_url");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACExtensionBarModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 名称
     */
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
    
    /**
     * 内容
     */
    public String getContent() { return content; }
    public void setContent(String value) { this.content = value; }
    
    /**
     * 图标
     */
    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String value) { this.iconUrl = value; }
    
    /**
     * 链接
     */
    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String value) { this.linkUrl = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACExtensionBarModel{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                '}';
    }

    
}