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
 * YueJianAppACGiftModel
 */
public class YueJianAppACGiftModel implements Serializable {
    private int giftId; // 礼物id
    private String name; // 礼物名称
    private int giftCount; // 礼物数量
    private String icon; // 礼物图标
    private int price; // 礼物的钻石价格
    private int giftAnimationType; // 礼物动画类型(参见 ACGiftAnimationTypeDefine)
    private int exp; // 礼物对应的经验
    private String resourceId; // 资源ID

    /**
     * ACGiftModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACGiftModel(JSONObject json) {
        // 设置默认值
        this.giftId = 0; // 礼物id
        this.name = ""; // 礼物名称
        this.giftCount = 0; // 礼物数量
        this.icon = ""; // 礼物图标
        this.price = 0; // 礼物的钻石价格
        this.giftAnimationType = 0; // 礼物动画类型(参见 ACGiftAnimationTypeDefine)
        this.exp = 0; // 礼物对应的经验
        this.resourceId = ""; // 资源ID

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 礼物id
                current_field_name = "gift_id";
                if (json.has("gift_id")) {
                    this.giftId = json.getInt("gift_id");
                }
    
                // 礼物名称
                current_field_name = "name";
                if (json.has("name")) {
                    this.name = json.getString("name");
                }
    
                // 礼物数量
                current_field_name = "gift_count";
                if (json.has("gift_count")) {
                    this.giftCount = json.getInt("gift_count");
                }
    
                // 礼物图标
                current_field_name = "icon";
                if (json.has("icon")) {
                    this.icon = json.getString("icon");
                }
    
                // 礼物的钻石价格
                current_field_name = "price";
                if (json.has("price")) {
                    this.price = json.getInt("price");
                }
    
                // 礼物动画类型(参见 ACGiftAnimationTypeDefine)
                current_field_name = "gift_animation_type";
                if (json.has("gift_animation_type")) {
                    this.giftAnimationType = json.getInt("gift_animation_type");
                }
    
                // 礼物对应的经验
                current_field_name = "exp";
                if (json.has("exp")) {
                    this.exp = json.getInt("exp");
                }
    
                // 资源ID
                current_field_name = "resource_id";
                if (json.has("resource_id")) {
                    this.resourceId = json.getString("resource_id");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACGiftModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 礼物id
     */
    public int getGiftId() { return giftId; }
    public void setGiftId(int value) { this.giftId = value; }
    
    /**
     * 礼物名称
     */
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
    
    /**
     * 礼物数量
     */
    public int getGiftCount() { return giftCount; }
    public void setGiftCount(int value) { this.giftCount = value; }
    
    /**
     * 礼物图标
     */
    public String getIcon() { return icon; }
    public void setIcon(String value) { this.icon = value; }
    
    /**
     * 礼物的钻石价格
     */
    public int getPrice() { return price; }
    public void setPrice(int value) { this.price = value; }
    
    /**
     * 礼物动画类型(参见 ACGiftAnimationTypeDefine)
     */
    public int getGiftAnimationType() { return giftAnimationType; }
    public void setGiftAnimationType(int value) { this.giftAnimationType = value; }
    
    /**
     * 礼物对应的经验
     */
    public int getExp() { return exp; }
    public void setExp(int value) { this.exp = value; }
    
    /**
     * 资源ID
     */
    public String getResourceId() { return resourceId; }
    public void setResourceId(String value) { this.resourceId = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACGiftModel{" +
                "giftId=" + giftId +
                ", name='" + name + '\'' +
                ", giftCount=" + giftCount +
                ", icon='" + icon + '\'' +
                ", price=" + price +
                ", giftAnimationType=" + giftAnimationType +
                ", exp=" + exp +
                ", resourceId='" + resourceId + '\'' +
                '}';
    }

    
}