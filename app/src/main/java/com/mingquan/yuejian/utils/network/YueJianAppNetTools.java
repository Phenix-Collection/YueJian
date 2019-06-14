package com.mingquan.yuejian.utils.network;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络状态相关 来自github
 */

public class YueJianAppNetTools {
  /**
   * 获取连接服务
   *
   * @param context :
   * @return
   */
  private static ConnectivityManager getConnectivityMannager(Context context) {
    return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  private static Context sCtx;

  /**
   * 初始化 Context ,一般建议在 Application 中进行设置
   *
   * @param context
   */
  public static void init(Context context) {
    if (sCtx != null)
      return;

    if (context instanceof Activity) {
      sCtx = ((Activity) context).getApplication();
      return;
    }

    if (context instanceof Service) {
      sCtx = ((Service) context).getApplication();
      return;
    }
    sCtx = context.getApplicationContext();
  }

  /**
   * 判断网络是否连接
   * <br/> 在使用这个方法时,一定要先通过#YueJianAppNetTools init() 方法中进行Context 设置
   *
   * @return
   */
  public static boolean isConnected() {
    return isConnected(sCtx);
  }

  /**
   * 判断网络是否连接
   *
   * @param context
   * @return
   */
  public static boolean isConnected(Context context) {
    if (context == null) {
      throw new RuntimeException(YueJianAppNetTools.class.getSimpleName() + " # Content  is null");
    }
    init(context);
    ConnectivityManager connectivityManager = getConnectivityMannager(sCtx);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnected();
  }

  /**
   * 获取网络状态
   * <br/> 在使用这个方法时,一定要先通过#YueJianAppNetTools init() 方法中进行 Context 设置
   *
   * @return <br/> enum YueJianAppNetType
   * <br/> #NO_NET : 没有网络
   * <br/> NET_WIFI : wifi 网络
   * <br/> NET_3G_4G : 移动网络  3G 4G
   */
  public static YueJianAppNetType netType() {
    return netType(sCtx);
  }

  /**
   * 获取网络状态
   *
   * @return <br/> enum YueJianAppNetType
   * <br/> #NO_NET : 没有网络
   * <br/> NET_WIFI : wifi 网络
   * <br/> NET_3G_4G : 移动网络  3G 4G
   */
  public static YueJianAppNetType netType(Context context) {
    if (context == null) {
      throw new RuntimeException(YueJianAppNetTools.class.getSimpleName() + " # Content  is null");
    }

    init(context);
    ConnectivityManager connectivityManager = getConnectivityMannager(sCtx);

    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

    if (networkInfo == null)
      return YueJianAppNetType.NO_NET;

    int type = networkInfo.getType();
    if (type == ConnectivityManager.TYPE_MOBILE)
      return YueJianAppNetType.NET_3G_4G;

    if (type == ConnectivityManager.TYPE_WIFI)
      return YueJianAppNetType.NET_WIFI;

    return YueJianAppNetType.NO_NET;
  }

  /**
   * 获取网络状态
   */
  public static String getNetStatus(YueJianAppNetType netType) {
    String mNetStatus = "WIFI";
    switch (netType) {
      case NET_WIFI:
        mNetStatus = "WIFI";
        break;
      case NET_3G_4G:
        mNetStatus = "移动流量";
        break;
      case NO_NET:
        mNetStatus = "网络断开";
        break;
    }
    return mNetStatus;
  }
}
