package com.mingquan.yuejian.utils;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * 屏幕相关 状态栏，标题栏高度
 * Created by Jiantao on 2016/10/18.
 */
public class YueJianAppDisplayUtils {
  /**
   * 获取状态栏高度  方法一
   *
   * @param context
   * @return
   */
  public static int getStatusBarHeight1(Context context) {
    int statusBarHeight = -1;
    //获取status_bar_height资源的ID
    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      //根据资源ID获取响应的尺寸值
      statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
    }
    return statusBarHeight;
  }

  /**
   * 获取状态栏的高度 方法二
   *
   * @param context
   * @return
   */
  public static int getStatusBarHeight2(Context context) {
    try {
      Class<?> c = Class.forName("com.android.internal.R$dimen");
      Object obj = c.newInstance();
      Field field = c.getField("status_bar_height");
      int x = Integer.parseInt(field.get(obj).toString());
      return context.getResources().getDimensionPixelSize(x);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
}
