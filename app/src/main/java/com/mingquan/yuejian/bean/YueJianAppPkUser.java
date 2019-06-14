package com.mingquan.yuejian.bean;

import java.io.Serializable;

/**
 * Created by administrato on 2016/9/29.
 */
public class YueJianAppPkUser implements Serializable {
  private String user_nicename;
  private String avatar;
  private String level;

  public String getUser_nicename() {
    return user_nicename;
  }

  public void setUser_nicename(String user_nicename) {
    this.user_nicename = user_nicename;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  @Override
  public String toString() {
    return "YueJianAppPkUser{"
        + "user_nicename='" + user_nicename + '\'' + ", avatar='" + avatar + '\'' + ", level='"
        + level + '\'' + '}';
  }
}
