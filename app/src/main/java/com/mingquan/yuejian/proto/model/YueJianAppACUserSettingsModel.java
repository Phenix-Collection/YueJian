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
 * YueJianAppACUserSettingsModel
 */
public class YueJianAppACUserSettingsModel implements Serializable {
    private int voiceVolume; // 麦克风音量
    private int accompanyVolume; // 背景音乐音量
    private int petVolume; // 宠物音量
    private boolean useBackCamera; // 是否使用后置摄像头

    /**
     * ACUserSettingsModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACUserSettingsModel(JSONObject json) {
        // 设置默认值
        this.voiceVolume = 0; // 麦克风音量
        this.accompanyVolume = 0; // 背景音乐音量
        this.petVolume = 0; // 宠物音量
        this.useBackCamera = false; // 是否使用后置摄像头

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 麦克风音量
                current_field_name = "voice_volume";
                if (json.has("voice_volume")) {
                    this.voiceVolume = json.getInt("voice_volume");
                }
    
                // 背景音乐音量
                current_field_name = "accompany_volume";
                if (json.has("accompany_volume")) {
                    this.accompanyVolume = json.getInt("accompany_volume");
                }
    
                // 宠物音量
                current_field_name = "pet_volume";
                if (json.has("pet_volume")) {
                    this.petVolume = json.getInt("pet_volume");
                }
    
                // 是否使用后置摄像头
                current_field_name = "use_back_camera";
                if (json.has("use_back_camera")) {
                    this.useBackCamera = json.getBoolean("use_back_camera");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACUserSettingsModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 麦克风音量
     */
    public int getVoiceVolume() { return voiceVolume; }
    public void setVoiceVolume(int value) { this.voiceVolume = value; }
    
    /**
     * 背景音乐音量
     */
    public int getAccompanyVolume() { return accompanyVolume; }
    public void setAccompanyVolume(int value) { this.accompanyVolume = value; }
    
    /**
     * 宠物音量
     */
    public int getPetVolume() { return petVolume; }
    public void setPetVolume(int value) { this.petVolume = value; }
    
    /**
     * 是否使用后置摄像头
     */
    public boolean getUseBackCamera() { return useBackCamera; }
    public void setUseBackCamera(boolean value) { this.useBackCamera = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACUserSettingsModel{" +
                "voiceVolume=" + voiceVolume +
                ", accompanyVolume=" + accompanyVolume +
                ", petVolume=" + petVolume +
                ", useBackCamera=" + useBackCamera +
                '}';
    }

    
}