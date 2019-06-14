package com.mingquan.yuejian.bean;

import android.graphics.BitmapRegionDecoder;

import com.mingquan.yuejian.proto.model.YueJianAppACGiftAnimationConfigModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jiantao on 2017/1/19.
 */

public class YueJianAppSurfaceFrameBean {
  class config {}

  private String resourceId;
  private String md5;
  public List<String> mJsonList;
  public List<String> mPicList;
  private int mAnimAction;
  private String bgFilePath;
  public boolean mHasResLoaded = false;
  public boolean mHasBitmapDecoded = false;
  public YueJianAppACGiftAnimationConfigModel animationConfigModel;
  public static HashMap<String, BitmapRegionDecoder> mBitmapRegionDecoderHashMap = new HashMap<>();
  public HashMap<String, List<YueJianAppFrameBean>> mFrameBeanMap = new HashMap<>();
  public List<YueJianAppFrameBean> mFrameBeanList = new ArrayList<>();

  public YueJianAppSurfaceFrameBean(String resourceId) {
    this.resourceId = resourceId;
    mFrameBeanMap.put(resourceId, mFrameBeanList);
  }

  public void setAnimAction(int animAction) {
    this.mAnimAction = animAction;
  }

  public int getAnimAction() {
    return mAnimAction;
  }

  public String getBgFilePath() {
    return bgFilePath;
  }

  public void setBgFilePath(String bgFilePath) {
    this.bgFilePath = bgFilePath;
  }

  /**
   * 获取动画的资源
   *
   * @param resourceId
   */
  public List<YueJianAppFrameBean> getAnimFrameList(String resourceId) {
    return mFrameBeanMap.get(resourceId);
  }

  public String getResourceId() {
    return resourceId;
  }

  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public boolean getHasResLoaded() {
    return mHasResLoaded;
  }

  public boolean getHasBitmapDecoded() {
    return mHasBitmapDecoded;
  }

  public void setHasResLoaded(boolean hasResLoaded) {
    this.mHasResLoaded = hasResLoaded;
  }

  public void setHasBitmapDecoded(boolean hasBitmapDecoded) {
    this.mHasBitmapDecoded = hasBitmapDecoded;
  }

  public List<String> getJsonList() {
    return mJsonList;
  }

  public void setJsonList(List<String> mJsonList) {
    this.mJsonList = mJsonList;
  }

  public List<String> getPicList() {
    return mPicList;
  }

  public void setPicList(List<String> mPicList) {
    this.mPicList = mPicList;
  }

  public YueJianAppACGiftAnimationConfigModel getAnimationConfigModel() {
    return animationConfigModel;
  }

  public void setAnimationConfigModel(YueJianAppACGiftAnimationConfigModel animationConfigModel) {
    this.animationConfigModel = animationConfigModel;
  }
}
