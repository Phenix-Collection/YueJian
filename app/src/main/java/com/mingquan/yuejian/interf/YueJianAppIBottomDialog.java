package com.mingquan.yuejian.interf;

import android.app.Dialog;

/**
 * Created by Administrator on 2016/3/19.
 */
public interface YueJianAppIBottomDialog {
  void cancelDialog(Dialog d);

  void determineDialog(Dialog d, Object... value);
}
