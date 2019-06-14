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
 * YueJianAppACGiftAnimationConfigModel
 */
public class YueJianAppACGiftAnimationConfigModel implements Serializable {
    private int fps; // 资源ID
    private int showTitle; // 是否显示标题(1显示，0不显示)
    private int fullScreen; // 是否是全屏(1全屏，0不全屏)
    private int imageWidth; // 单张图片的宽度
    private int imageHeight; // 单张图片的高度
    private int imagesCount; // 图片数量
    private ArrayList<String> animation; // 动画配置列表
    private ArrayList<String> atlas; // 图解列表
    private String background; // 背景图

    /**
     * ACGiftAnimationConfigModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACGiftAnimationConfigModel(JSONObject json) {
        // 设置默认值
        this.fps = 0; // 资源ID
        this.showTitle = 0; // 是否显示标题(1显示，0不显示)
        this.fullScreen = 0; // 是否是全屏(1全屏，0不全屏)
        this.imageWidth = 0; // 单张图片的宽度
        this.imageHeight = 0; // 单张图片的高度
        this.imagesCount = 0; // 图片数量
        this.animation = new ArrayList<String>(); // 动画配置列表
        this.atlas = new ArrayList<String>(); // 图解列表
        this.background = ""; // 背景图

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 资源ID
                current_field_name = "fps";
                if (json.has("fps")) {
                    this.fps = json.getInt("fps");
                }
    
                // 是否显示标题(1显示，0不显示)
                current_field_name = "show_title";
                if (json.has("show_title")) {
                    this.showTitle = json.getInt("show_title");
                }
    
                // 是否是全屏(1全屏，0不全屏)
                current_field_name = "full_screen";
                if (json.has("full_screen")) {
                    this.fullScreen = json.getInt("full_screen");
                }
    
                // 单张图片的宽度
                current_field_name = "image_width";
                if (json.has("image_width")) {
                    this.imageWidth = json.getInt("image_width");
                }
    
                // 单张图片的高度
                current_field_name = "image_height";
                if (json.has("image_height")) {
                    this.imageHeight = json.getInt("image_height");
                }
    
                // 图片数量
                current_field_name = "images_count";
                if (json.has("images_count")) {
                    this.imagesCount = json.getInt("images_count");
                }
    
                // 动画配置列表
                current_field_name = "animation";
                if (json.has("animation")) {
                    JSONArray animationJson = json.getJSONArray("animation");
                    for (int i = 0; i < animationJson.length(); i++) {
                        String model = animationJson.get(i).toString();
                        this.animation.add(model);
                    }
        
                }
    
                // 图解列表
                current_field_name = "atlas";
                if (json.has("atlas")) {
                    JSONArray atlasJson = json.getJSONArray("atlas");
                    for (int i = 0; i < atlasJson.length(); i++) {
                        String model = atlasJson.get(i).toString();
                        this.atlas.add(model);
                    }
        
                }
    
                // 背景图
                current_field_name = "background";
                if (json.has("background")) {
                    this.background = json.getString("background");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACGiftAnimationConfigModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 资源ID
     */
    public int getFps() { return fps; }
    public void setFps(int value) { this.fps = value; }
    
    /**
     * 是否显示标题(1显示，0不显示)
     */
    public int getShowTitle() { return showTitle; }
    public void setShowTitle(int value) { this.showTitle = value; }
    
    /**
     * 是否是全屏(1全屏，0不全屏)
     */
    public int getFullScreen() { return fullScreen; }
    public void setFullScreen(int value) { this.fullScreen = value; }
    
    /**
     * 单张图片的宽度
     */
    public int getImageWidth() { return imageWidth; }
    public void setImageWidth(int value) { this.imageWidth = value; }
    
    /**
     * 单张图片的高度
     */
    public int getImageHeight() { return imageHeight; }
    public void setImageHeight(int value) { this.imageHeight = value; }
    
    /**
     * 图片数量
     */
    public int getImagesCount() { return imagesCount; }
    public void setImagesCount(int value) { this.imagesCount = value; }
    
    /**
     * 动画配置列表
     */
    public ArrayList<String> getAnimation() { return animation; }
    public void setAnimation(ArrayList<String> value) { this.animation = value; }
    
    /**
     * 图解列表
     */
    public ArrayList<String> getAtlas() { return atlas; }
    public void setAtlas(ArrayList<String> value) { this.atlas = value; }
    
    /**
     * 背景图
     */
    public String getBackground() { return background; }
    public void setBackground(String value) { this.background = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACGiftAnimationConfigModel{" +
                "fps=" + fps +
                ", showTitle=" + showTitle +
                ", fullScreen=" + fullScreen +
                ", imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                ", imagesCount=" + imagesCount +
                ", animation=" + animation +
                ", atlas=" + atlas +
                ", background='" + background + '\'' +
                '}';
    }

    
}