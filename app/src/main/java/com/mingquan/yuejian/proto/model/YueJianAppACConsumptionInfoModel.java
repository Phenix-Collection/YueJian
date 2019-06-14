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
 * YueJianAppACConsumptionInfoModel
 */
public class YueJianAppACConsumptionInfoModel implements Serializable {
    private int consumptionTime; // 收支时间
    private int consumptionType; // 消费类型(参见 ACConsumptionTypeDefine)
    private YueJianAppACUserPublicInfoModel target; // 收支目标
    private int amount; // 收支金额(正数为从目标处获得收入，负数为支出给目标)
    private String desp; // 消费描述

    /**
     * ACConsumptionInfoModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACConsumptionInfoModel(JSONObject json) {
        // 设置默认值
        this.consumptionTime = 0; // 收支时间
        this.consumptionType = 0; // 消费类型(参见 ACConsumptionTypeDefine)
        this.target = new YueJianAppACUserPublicInfoModel(null); // 收支目标
        this.amount = 0; // 收支金额(正数为从目标处获得收入，负数为支出给目标)
        this.desp = ""; // 消费描述

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 收支时间
                current_field_name = "consumption_time";
                if (json.has("consumption_time")) {
                    this.consumptionTime = json.getInt("consumption_time");
                }
    
                // 消费类型(参见 ACConsumptionTypeDefine)
                current_field_name = "consumption_type";
                if (json.has("consumption_type")) {
                    this.consumptionType = json.getInt("consumption_type");
                }
    
                // 收支目标
                current_field_name = "target";
                if (json.has("target")) {
                    this.target = new YueJianAppACUserPublicInfoModel(json.getJSONObject("target"));
        
                }
    
                // 收支金额(正数为从目标处获得收入，负数为支出给目标)
                current_field_name = "amount";
                if (json.has("amount")) {
                    this.amount = json.getInt("amount");
                }
    
                // 消费描述
                current_field_name = "desp";
                if (json.has("desp")) {
                    this.desp = json.getString("desp");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACConsumptionInfoModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 收支时间
     */
    public int getConsumptionTime() { return consumptionTime; }
    public void setConsumptionTime(int value) { this.consumptionTime = value; }
    
    /**
     * 消费类型(参见 ACConsumptionTypeDefine)
     */
    public int getConsumptionType() { return consumptionType; }
    public void setConsumptionType(int value) { this.consumptionType = value; }
    
    /**
     * 收支目标
     */
    public YueJianAppACUserPublicInfoModel getTarget() { return target; }
    public void setTarget(YueJianAppACUserPublicInfoModel value) { this.target = value; }
    
    /**
     * 收支金额(正数为从目标处获得收入，负数为支出给目标)
     */
    public int getAmount() { return amount; }
    public void setAmount(int value) { this.amount = value; }
    
    /**
     * 消费描述
     */
    public String getDesp() { return desp; }
    public void setDesp(String value) { this.desp = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACConsumptionInfoModel{" +
                "consumptionTime=" + consumptionTime +
                ", consumptionType=" + consumptionType +
                ", target=" + target +
                ", amount=" + amount +
                ", desp='" + desp + '\'' +
                '}';
    }

    
}