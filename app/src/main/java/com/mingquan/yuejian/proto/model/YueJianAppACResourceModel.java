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
 * YueJianAppACResourceModel
 */
public class YueJianAppACResourceModel implements Serializable {
    private String resourceId; // 资源ID
    private int resourceType; // 资源类型(参见 ACResourceTypeDefine)
    private String md5; // 资源的md5
    private String url; // 资源的下载地址
    private int priority; // 资源下载的优先级(数字越大优先级越高)
    private boolean loadAsap; // 是否进入APP后就立刻下载
    private int fileSize; // 文件大小
    private boolean isZipfile; // 是否是zip格式的文件
    private String unzippedDirName; // 解压后的目录名

    /**
     * ACResourceModel的构造函数
     * @param json json数据源
     */
    public YueJianAppACResourceModel(JSONObject json) {
        // 设置默认值
        this.resourceId = ""; // 资源ID
        this.resourceType = 0; // 资源类型(参见 ACResourceTypeDefine)
        this.md5 = ""; // 资源的md5
        this.url = ""; // 资源的下载地址
        this.priority = 0; // 资源下载的优先级(数字越大优先级越高)
        this.loadAsap = false; // 是否进入APP后就立刻下载
        this.fileSize = 0; // 文件大小
        this.isZipfile = false; // 是否是zip格式的文件
        this.unzippedDirName = ""; // 解压后的目录名

        // 解析JSON数据
        String current_field_name = "";
        try {
            if (json != null) {
                // 资源ID
                current_field_name = "resource_id";
                if (json.has("resource_id")) {
                    this.resourceId = json.getString("resource_id");
                }
    
                // 资源类型(参见 ACResourceTypeDefine)
                current_field_name = "resource_type";
                if (json.has("resource_type")) {
                    this.resourceType = json.getInt("resource_type");
                }
    
                // 资源的md5
                current_field_name = "md5";
                if (json.has("md5")) {
                    this.md5 = json.getString("md5");
                }
    
                // 资源的下载地址
                current_field_name = "url";
                if (json.has("url")) {
                    this.url = json.getString("url");
                }
    
                // 资源下载的优先级(数字越大优先级越高)
                current_field_name = "priority";
                if (json.has("priority")) {
                    this.priority = json.getInt("priority");
                }
    
                // 是否进入APP后就立刻下载
                current_field_name = "load_asap";
                if (json.has("load_asap")) {
                    this.loadAsap = json.getBoolean("load_asap");
                }
    
                // 文件大小
                current_field_name = "file_size";
                if (json.has("file_size")) {
                    this.fileSize = json.getInt("file_size");
                }
    
                // 是否是zip格式的文件
                current_field_name = "is_zipfile";
                if (json.has("is_zipfile")) {
                    this.isZipfile = json.getBoolean("is_zipfile");
                }
    
                // 解压后的目录名
                current_field_name = "unzipped_dir_name";
                if (json.has("unzipped_dir_name")) {
                    this.unzippedDirName = json.getString("unzipped_dir_name");
                }
    
            }
        } catch (Exception e) {
          HashMap<String, Object> extension = new HashMap<>();
            extension.put("model", "YueJianAppACResourceModel");
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
     * 资源类型(参见 ACResourceTypeDefine)
     */
    public int getResourceType() { return resourceType; }
    public void setResourceType(int value) { this.resourceType = value; }
    
    /**
     * 资源的md5
     */
    public String getMd5() { return md5; }
    public void setMd5(String value) { this.md5 = value; }
    
    /**
     * 资源的下载地址
     */
    public String getUrl() { return url; }
    public void setUrl(String value) { this.url = value; }
    
    /**
     * 资源下载的优先级(数字越大优先级越高)
     */
    public int getPriority() { return priority; }
    public void setPriority(int value) { this.priority = value; }
    
    /**
     * 是否进入APP后就立刻下载
     */
    public boolean getLoadAsap() { return loadAsap; }
    public void setLoadAsap(boolean value) { this.loadAsap = value; }
    
    /**
     * 文件大小
     */
    public int getFileSize() { return fileSize; }
    public void setFileSize(int value) { this.fileSize = value; }
    
    /**
     * 是否是zip格式的文件
     */
    public boolean getIsZipfile() { return isZipfile; }
    public void setIsZipfile(boolean value) { this.isZipfile = value; }
    
    /**
     * 解压后的目录名
     */
    public String getUnzippedDirName() { return unzippedDirName; }
    public void setUnzippedDirName(String value) { this.unzippedDirName = value; }
    

    @Override
    public String toString() {
        return "YueJianAppACResourceModel{" +
                "resourceId='" + resourceId + '\'' +
                ", resourceType=" + resourceType +
                ", md5='" + md5 + '\'' +
                ", url='" + url + '\'' +
                ", priority=" + priority +
                ", loadAsap=" + loadAsap +
                ", fileSize=" + fileSize +
                ", isZipfile=" + isZipfile +
                ", unzippedDirName='" + unzippedDirName + '\'' +
                '}';
    }

    
}