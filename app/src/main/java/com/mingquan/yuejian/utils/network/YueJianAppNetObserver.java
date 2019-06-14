package com.mingquan.yuejian.utils.network;

/**
 *网络状态相关 来自github
 */

public class YueJianAppNetObserver {
  private static YueJianAppINetStatusReceiver sGlobalReceiver;
  private static YueJianAppINetStatusReceiver sNetStatusReceiver;

  /**
   * 注册全局回调,全局网络状态改变时,每次都会回调,主要用于设置全局网络状态使用
   * @param netStatusReceiver
   */
  public static synchronized void registerlGlobalReceiver(YueJianAppINetStatusReceiver netStatusReceiver) {
    sGlobalReceiver = netStatusReceiver;
  }

  /**
   * 获取全局回调
   * @return
   */
  public static YueJianAppINetStatusReceiver getGlobalReceiver() {
    return sGlobalReceiver;
  }

  /**
   * 取消注册的全局回调
   */
  public static void unGlobalRegister() {
    sGlobalReceiver = null;
  }

  /**
   * 注册回调
   * @param netStatusReceiver
   */
  public static synchronized void register(YueJianAppINetStatusReceiver netStatusReceiver) {
    sNetStatusReceiver = netStatusReceiver;
  }

  /**
   * 取消
   */
  public static void unregister() {
    sNetStatusReceiver = null;
  }

  public static YueJianAppINetStatusReceiver getNetStatusReceiver() {
    return sNetStatusReceiver;
  }
}
