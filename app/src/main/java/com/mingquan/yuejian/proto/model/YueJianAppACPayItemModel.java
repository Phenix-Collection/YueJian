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
 * YueJianAppACPayItemModel
 */
public class YueJianAppACPayItemModel implements Serializable {
    private String itemId; // 商品id
    private int price; // 价格（RMB）
    private int value; // 购买的钻石数量
    private int gifts; // 赠送的钻石数量
    private String desp; // 商品描述
    private String imageUrl; // 展示图片地址
    private ArrayList<YueJianAppACUpExtModel> upExt; // 扩展信息

    /**
     * ACPayItemModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACPayItemModel(JSONObject json) {
        // 设置默认值
        this.itemId = ""; // 商品id
        this.price = 0; // 价格（RMB）
        this.value = 0; // 购买的钻石数量
        this.gifts = 0; // 赠送的钻石数量
        this.desp = ""; // 商品描述
        this.imageUrl = ""; // 展示图片地址
        this.upExt = new ArrayList<YueJianAppACUpExtModel>(); // 扩展信息

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 商品id
                current_field_name = "item_id";
                if (json.has("item_id")) {
                    this.itemId = json.getString("item_id");
                }
    
                // 价格（RMB）
                current_field_name = "price";
                if (json.has("price")) {
                    this.price = json.getInt("price");
                }
    
                // 购买的钻石数量
                current_field_name = "value";
                if (json.has("value")) {
                    this.value = json.getInt("value");
                }
    
                // 赠送的钻石数量
                current_field_name = "gifts";
                if (json.has("gifts")) {
                    this.gifts = json.getInt("gifts");
                }
    
                // 商品描述
                current_field_name = "desp";
                if (json.has("desp")) {
                    this.desp = json.getString("desp");
                }
    
                // 展示图片地址
                current_field_name = "image_url";
                if (json.has("image_url")) {
                    this.imageUrl = json.getString("image_url");
                }
    
                // 扩展信息
                current_field_name = "up_ext";
                if (json.has("up_ext")) {
                    JSONArray upExtJson = json.getJSONArray("up_ext");
                    for (int i = 0; i < upExtJson.length(); i++) {
                        YueJianAppACUpExtModel model = new YueJianAppACUpExtModel(upExtJson.getJSONObject(i));
                        this.upExt.add(model);
                    }
        
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACPayItemModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 商品id
     */
    public String getItemId() { return itemId; }
    public void setItemId(String value) { this.itemId = value; }
    
    /**
     * 价格（RMB）
     */
    public int getPrice() { return price; }
    public void setPrice(int value) { this.price = value; }
    
    /**
     * 购买的钻石数量
     */
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    
    /**
     * 赠送的钻石数量
     */
    public int getGifts() { return gifts; }
    public void setGifts(int value) { this.gifts = value; }
    
    /**
     * 商品描述
     */
    public String getDesp() { return desp; }
    public void setDesp(String value) { this.desp = value; }
    
    /**
     * 展示图片地址
     */
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String value) { this.imageUrl = value; }
    
    /**
     * 扩展信息
     */
    public ArrayList<YueJianAppACUpExtModel> getUpExt() { return upExt; }
    public void setUpExt(ArrayList<YueJianAppACUpExtModel> value) { this.upExt = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACPayItemModel{" +
                "itemId='" + itemId + '\'' +
                ", price=" + price +
                ", value=" + value +
                ", gifts=" + gifts +
                ", desp='" + desp + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", upExt=" + upExt +
                '}';
    }

    
}