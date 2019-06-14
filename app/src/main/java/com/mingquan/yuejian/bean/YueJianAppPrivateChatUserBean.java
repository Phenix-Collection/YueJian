package com.mingquan.yuejian.bean;

/**
 * Created by Administrator on 2016/4/13.
 */
public class YueJianAppPrivateChatUserBean extends YueJianAppUserBean {
  private String lastMessage;
  private String lastMessageTime;
  private boolean unreadMessage;
  private int isattention2;
  private int unReadMsgCount = 1; //未读消息数量

  public int getIsattention2() {
    return isattention2;
  }

  public void setIsattention2(int isattention2) {
    this.isattention2 = isattention2;
  }

  public boolean isUnreadMessage() {
    return unreadMessage;
  }

  public void setUnreadMessage(boolean unreadMessage) {
    this.unreadMessage = unreadMessage;
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public void setLastMessage(String lastMessage) {
    this.lastMessage = lastMessage;
  }

  public String getLastMessageTime() {
    return lastMessageTime;
  }
  public void setLastMessageTime(String lastMessageTime) {
    this.lastMessageTime = lastMessageTime;
  }

  public int getUnReadMsgCount() {
    return unReadMsgCount;
  }

  public void setUnReadMsgCount(int unReadMsgCount) {
    this.unReadMsgCount = unReadMsgCount;
  }

  @Override
  public String toString() {
    return "YueJianAppPrivateChatUserBean{"
        + "lastMessage='" + lastMessage + '\'' + ", lastMessageTime='" + lastMessageTime + '\''
        + ", unreadMessage=" + unreadMessage + ", isattention2=" + isattention2
        + ", unReadMsgCount=" + unReadMsgCount + '}';
  }
}
