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
 * YueJianAppACUserTagModel
 */
public class YueJianAppACUserTagModel implements Serializable {
    private int tagId; // 标签id
    private int count; // 标签数量

    /**
     * ACUserTagModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACUserTagModel(JSONObject json) {
        // 设置默认值
        this.tagId = 0; // 标签id
        this.count = 0; // 标签数量

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 标签id
                current_field_name = "tag_id";
                if (json.has("tag_id")) {
                    this.tagId = json.getInt("tag_id");
                }
    
                // 标签数量
                current_field_name = "count";
                if (json.has("count")) {
                    this.count = json.getInt("count");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACUserTagModel");
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
     * 标签数量
     */
    public int getCount() { return count; }
    public void setCount(int value) { this.count = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACUserTagModel{" +
                "tagId=" + tagId +
                ", count=" + count +
                '}';
    }

    
}