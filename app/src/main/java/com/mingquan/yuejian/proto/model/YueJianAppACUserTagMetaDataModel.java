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
 * YueJianAppACUserTagMetaDataModel
 */
public class YueJianAppACUserTagMetaDataModel implements Serializable {
    private int tagId; // 标签id
    private String name; // 标签名称
    private String color; // 标签颜色(#RRGGBB)

    /**
     * ACUserTagMetaDataModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACUserTagMetaDataModel(JSONObject json) {
        // 设置默认值
        this.tagId = 0; // 标签id
        this.name = ""; // 标签名称
        this.color = ""; // 标签颜色(#RRGGBB)

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 标签id
                current_field_name = "tag_id";
                if (json.has("tag_id")) {
                    this.tagId = json.getInt("tag_id");
                }
    
                // 标签名称
                current_field_name = "name";
                if (json.has("name")) {
                    this.name = json.getString("name");
                }
    
                // 标签颜色(#RRGGBB)
                current_field_name = "color";
                if (json.has("color")) {
                    this.color = json.getString("color");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACUserTagMetaDataModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 标签id
     */
    public int getTagId() { return tagId; }
    public void setTagId(int value) { this.tagId = value; }
    
    /**
     * 标签名称
     */
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
    
    /**
     * 标签颜色(#RRGGBB)
     */
    public String getColor() { return color; }
    public void setColor(String value) { this.color = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACUserTagMetaDataModel{" +
                "tagId=" + tagId +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    
}