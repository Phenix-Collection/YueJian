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
 * YueJianAppACUserHomepageInfoModel
 */
public class YueJianAppACUserHomepageInfoModel implements Serializable {
    private YueJianAppACUserPublicInfoModel user; // 用户基本信息
    private ArrayList<YueJianAppACUserPublicInfoModel> top3; // 贡献榜前三名
    private YueJianAppACRelationModel relation; // 和目标用户之间的关系

    /**
     * ACUserHomepageInfoModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACUserHomepageInfoModel(JSONObject json) {
        // 设置默认值
        this.user = new YueJianAppACUserPublicInfoModel(null); // 用户基本信息
        this.top3 = new ArrayList<YueJianAppACUserPublicInfoModel>(); // 贡献榜前三名
        this.relation = new YueJianAppACRelationModel(null); // 和目标用户之间的关系

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 用户基本信息
                current_field_name = "user";
                if (json.has("user")) {
                    this.user = new YueJianAppACUserPublicInfoModel(json.getJSONObject("user"));
        
                }
    
                // 贡献榜前三名
                current_field_name = "top3";
                if (json.has("top3")) {
                    JSONArray top3Json = json.getJSONArray("top3");
                    for (int i = 0; i < top3Json.length(); i++) {
                        YueJianAppACUserPublicInfoModel model = new YueJianAppACUserPublicInfoModel(top3Json.getJSONObject(i));
                        this.top3.add(model);
                    }
        
                }
    
                // 和目标用户之间的关系
                current_field_name = "relation";
                if (json.has("relation")) {
                    this.relation = new YueJianAppACRelationModel(json.getJSONObject("relation"));
        
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACUserHomepageInfoModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 用户基本信息
     */
    public YueJianAppACUserPublicInfoModel getUser() { return user; }
    public void setUser(YueJianAppACUserPublicInfoModel value) { this.user = value; }
    
    /**
     * 贡献榜前三名
     */
    public ArrayList<YueJianAppACUserPublicInfoModel> getTop3() { return top3; }
    public void setTop3(ArrayList<YueJianAppACUserPublicInfoModel> value) { this.top3 = value; }
    
    /**
     * 和目标用户之间的关系
     */
    public YueJianAppACRelationModel getRelation() { return relation; }
    public void setRelation(YueJianAppACRelationModel value) { this.relation = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACUserHomepageInfoModel{" +
                "user=" + user +
                ", top3=" + top3 +
                ", relation=" + relation +
                '}';
    }

    
}