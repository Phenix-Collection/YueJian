package com.mingquan.yuejian.em;

/**
 * Created by Administrator on 2016/3/11.
 */
public enum YueJianAppChangInfo {
  CHANG_NICK("user_nicename"),
  CHANG_SIGN("signature");
  String action;
  YueJianAppChangInfo(String action) {
    this.action = action;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }
}
