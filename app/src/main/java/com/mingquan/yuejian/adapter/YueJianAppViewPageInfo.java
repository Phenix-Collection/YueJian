package com.mingquan.yuejian.adapter;

import android.os.Bundle;

public final class YueJianAppViewPageInfo {
  public final String tag;
  public final Class<?> clss;
  public final Bundle args;
  public final String title;

  public YueJianAppViewPageInfo(String _title, String _tag, Class<?> _class, Bundle _args) {
    title = _title;
    tag = _tag;
    clss = _class;
    args = _args;
  }
}