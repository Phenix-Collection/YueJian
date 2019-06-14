package com.mingquan.yuejian.bean;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.fragment.YueJianAppPushManageFragment;
import com.mingquan.yuejian.fragment.YueJianAppSearchFragment;
import com.mingquan.yuejian.vchat.YueJianAppVChatMessageDetailFragment;

public enum YueJianAppSimpleBackPage {
  USER_PRIVATECORE_DETAIL(4, R.string.privatechat, YueJianAppVChatMessageDetailFragment.class),
  INDEX_SECREEN(5, R.string.search, YueJianAppSearchFragment.class),
  USER_PUSH_MANAGE(7, R.string.push, YueJianAppPushManageFragment.class);
  private int title;
  private Class<?> clz;
  private int value;

  YueJianAppSimpleBackPage(int value, int title, Class<?> clz) {
    this.value = value;
    this.title = title;
    this.clz = clz;
  }

  public int getTitle() {
    return title;
  }

  public void setTitle(int title) {
    this.title = title;
  }

  public Class<?> getClz() {
    return clz;
  }

  public void setClz(Class<?> clz) {
    this.clz = clz;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public static YueJianAppSimpleBackPage getPageByValue(int val) {
    for (YueJianAppSimpleBackPage p : values()) {
      if (p.getValue() == val)
        return p;
    }
    return null;
  }
}
