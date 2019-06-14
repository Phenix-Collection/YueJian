package com.mingquan.yuejian.model;

import java.io.Serializable;

/**
 * Created by Jiantao on 2017/6/28.
 */

public class YueJianAppGiftItemModel implements Serializable {
  private String id; //标识此GiftItem的唯一Id，用来处理连送的逻辑
  private String giftName;
  private String giftNum;
  private String giftIcon;
  private String sendUid;

  public String getSendAvatar() {
    return sendAvatar;
  }

  public void setSendAvatar(String sendAvatar) {
    this.sendAvatar = sendAvatar;
  }

  private String sendAvatar;
  private String senderName;
  private String giftId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getGiftName() {
    return giftName;
  }

  public void setGiftName(String giftName) {
    this.giftName = giftName;
  }

  public String getGiftNum() {
    return giftNum;
  }

  public void setGiftNum(String giftNum) {
    this.giftNum = giftNum;
  }

  public String getGiftIcon() {
    return giftIcon;
  }

  public void setGiftIcon(String giftIcon) {
    this.giftIcon = giftIcon;
  }

  public String getSendUid() {
    return sendUid;
  }

  public void setSendUid(String sendUid) {
    this.sendUid = sendUid;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public String getGiftId() {
    return giftId;
  }

  public void setGiftId(String giftId) {
    this.giftId = giftId;
  }

  @Override
  public String toString() {
    return "YueJianAppGiftItemModel{"
        + "id='" + id + '\'' + ", giftName='" + giftName + '\'' + ", giftNum='" + giftNum + '\''
        + ", giftIcon='" + giftIcon + '\'' + ", sendUid='" + sendUid + '\'' + ", senderName='"
        + senderName + '\'' + ", giftId='" + giftId + '\'' + '}';
  }
}
