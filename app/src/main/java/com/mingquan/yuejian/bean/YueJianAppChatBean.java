package com.mingquan.yuejian.bean;

import android.text.SpannableStringBuilder;

/**
 * Created by Administrator on 2016/3/16.
 */
public class YueJianAppChatBean extends YueJianAppUserBean {
  private SpannableStringBuilder userNick;
  private SpannableStringBuilder sendChatMsg;
  private SpannableStringBuilder msgShow;
  private int type;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public SpannableStringBuilder getUserNick() {
    return userNick;
  }

  public void setUserNick(SpannableStringBuilder userNick) {
    this.userNick = userNick;
  }

  public SpannableStringBuilder getSendChatMsg() {
    return sendChatMsg;
  }

  public void setSendChatMsg(SpannableStringBuilder sendChatMsg) {
    this.sendChatMsg = sendChatMsg;
  }
}
