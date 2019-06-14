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
 * YueJianAppACUpExtModel
 */
public class YueJianAppACUpExtModel implements Serializable {
    private int type; // 类型
    private String name; // 名称
    private String iconUrl; // 图标url
    private String runUrl; // 执行地址

    /**
     * ACUpExtModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACUpExtModel(JSONObject json) {
        // 设置默认值
        this.type = 0; // 类型
        this.name = ""; // 名称
        this.iconUrl = ""; // 图标url
        this.runUrl = ""; // 执行地址

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 类型
                current_field_name = "type";
                if (json.has("type")) {
                    this.type = json.getInt("type");
                }
    
                // 名称
                current_field_name = "name";
                if (json.has("name")) {
                    this.name = json.getString("name");
                }
    
                // 图标url
                current_field_name = "icon_url";
                if (json.has("icon_url")) {
                    this.iconUrl = json.getString("icon_url");
                }
    
                // 执行地址
                current_field_name = "run_url";
                if (json.has("run_url")) {
                    this.runUrl = json.getString("run_url");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACUpExtModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 类型
     */
    public int getType() { return type; }
    public void setType(int value) { this.type = value; }
    
    /**
     * 名称
     */
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
    
    /**
     * 图标url
     */
    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String value) { this.iconUrl = value; }
    
    /**
     * 执行地址
     */
    public String getRunUrl() { return runUrl; }
    public void setRunUrl(String value) { this.runUrl = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACUpExtModel{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", runUrl='" + runUrl + '\'' +
                '}';
    }

    
}