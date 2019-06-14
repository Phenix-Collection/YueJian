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
import org.litepal.crud.DataSupport;

/**
 * YueJianAppACCachedResourceModel
 */
public class YueJianAppACCachedResourceModel extends DataSupport implements Serializable {
    private String resourceId; // 资源ID
    private String md5; // 资源的md5
    private int downloadedSize; // 已下载的文件大小
    private String filePath; // 文件保存路径
    private boolean downloadCompleted; // 是否下载完成
    private String downloadUrl; // 资源的下载地址

    /**
     * ACCachedResourceModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACCachedResourceModel(JSONObject json) {
        // 设置默认值
        this.resourceId = ""; // 资源ID
        this.md5 = ""; // 资源的md5
        this.downloadedSize = 0; // 已下载的文件大小
        this.filePath = ""; // 文件保存路径
        this.downloadCompleted = false; // 是否下载完成
        this.downloadUrl = ""; // 资源的下载地址

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 资源ID
                current_field_name = "resource_id";
                if (json.has("resource_id")) {
                    this.resourceId = json.getString("resource_id");
                }
    
                // 资源的md5
                current_field_name = "md5";
                if (json.has("md5")) {
                    this.md5 = json.getString("md5");
                }
    
                // 已下载的文件大小
                current_field_name = "downloaded_size";
                if (json.has("downloaded_size")) {
                    this.downloadedSize = json.getInt("downloaded_size");
                }
    
                // 文件保存路径
                current_field_name = "file_path";
                if (json.has("file_path")) {
                    this.filePath = json.getString("file_path");
                }
    
                // 是否下载完成
                current_field_name = "download_completed";
                if (json.has("download_completed")) {
                    this.downloadCompleted = json.getBoolean("download_completed");
                }
    
                // 资源的下载地址
                current_field_name = "download_url";
                if (json.has("download_url")) {
                    this.downloadUrl = json.getString("download_url");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACCachedResourceModel");
            extension.put("field", current_field_name);
            YueJianAppRavenUtils.logException("解析JSON数据失败", e, extension);
        }
    }
    
    /**
     * 资源ID
     */
    public String getResourceId() { return resourceId; }
    public void setResourceId(String value) { this.resourceId = value; }
    
    /**
     * 资源的md5
     */
    public String getMd5() { return md5; }
    public void setMd5(String value) { this.md5 = value; }
    
    /**
     * 已下载的文件大小
     */
    public int getDownloadedSize() { return downloadedSize; }
    public void setDownloadedSize(int value) { this.downloadedSize = value; }
    
    /**
     * 文件保存路径
     */
    public String getFilePath() { return filePath; }
    public void setFilePath(String value) { this.filePath = value; }
    
    /**
     * 是否下载完成
     */
    public boolean getDownloadCompleted() { return downloadCompleted; }
    public void setDownloadCompleted(boolean value) { this.downloadCompleted = value; }
    
    /**
     * 资源的下载地址
     */
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String value) { this.downloadUrl = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACCachedResourceModel{" +
                "resourceId='" + resourceId + '\'' +
                ", md5='" + md5 + '\'' +
                ", downloadedSize=" + downloadedSize +
                ", filePath='" + filePath + '\'' +
                ", downloadCompleted=" + downloadCompleted +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }

    
}