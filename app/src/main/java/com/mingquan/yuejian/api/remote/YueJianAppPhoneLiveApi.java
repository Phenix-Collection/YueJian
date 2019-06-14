package com.mingquan.yuejian.api.remote;

import android.os.Build;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.LinkedHashMap;

/**
 * 接口获取
 */
public class YueJianAppPhoneLiveApi {

  /**
   * @param uidList 用户id字符串 以逗号分割
   * @dw 批量获取用户信息
   */
  public static void getMultiBaseInfo(
      int action, int uid, String uidList, StringCallback callback) {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    map.put("service", "User.getMultiBaseInfo");
    map.put("uids", uidList);
    map.put("type", String.valueOf(action));
    map.put("uid", String.valueOf(uid));
    map.put("timestamp", String.valueOf(System.currentTimeMillis()));
    map.put("sign", YueJianAppApiUtils.createSign(map));
    YueJianAppBasicApi.doGet(map, callback);
  }

  /**
   * @param uid   当前用户id
   * @param ucuid to uid
   * @dw 获取用户信息私聊专用
   */

  public static void getPmUserInfo(int uid, int ucuid, StringCallback callback) {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    map.put("service", "User.getPmUserInfo");
    map.put("uid", String.valueOf(uid));
    map.put("ucuid", String.valueOf(ucuid));
    map.put("timestamp", String.valueOf(System.currentTimeMillis()));
    map.put("sign", YueJianAppApiUtils.createSign(map));
    YueJianAppBasicApi.doGet(map, callback);
  }

  // token是否过期
  public static void tokenIsOutTime(String uid, String token, StringCallback callback) {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    map.put("service", "User.iftoken");
    map.put("uid", uid);
    map.put("token", token);
    map.put("phone",
        "手机型号: " + Build.MODEL + ",SDK版本:" + Build.VERSION.SDK
            + ",系统版本:" + Build.VERSION.RELEASE);
    map.put("version",
        YueJianAppAppContext.getInstance().getPackageInfo().versionName + ","
            + YueJianAppAppContext.getInstance().getPackageInfo().versionCode);
    map.put("timestamp", String.valueOf(System.currentTimeMillis()));
    map.put("sign", YueJianAppApiUtils.createSign(map));
    YueJianAppBasicApi.doGet(map, callback);
  }

  /**
   * @param uid 用户id
   * @dw 获取用户余额
   */
  public static void getUserDiamondsNum(int uid, String token, StringCallback callback) {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    map.put("service", "User.getUserPrivateInfo");
    map.put("uid", String.valueOf(uid));
    map.put("token", token);
    map.put("timestamp", String.valueOf(System.currentTimeMillis()));
    map.put("sign", YueJianAppApiUtils.createSign(map));
    YueJianAppBasicApi.doGet(map, callback);
  }

  /**
   * @param maskWordUrl 下载地址
   * @dw 下载屏蔽字文件
   */
  public static void downloadMaskWord(String maskWordUrl, FileCallBack fileCallBack) {
    OkHttpUtils.get().url(maskWordUrl).build().execute(fileCallBack);
  }

  /**
   * @dw 检查新版本
   */
  public static void checkUpdate(String channel, StringCallback callback) {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    //        map.put("service", "User.getVersion");
    map.put("service", "Config.getAndroidVersion");
    map.put("channel", channel);
    YueJianAppBasicApi.doGet(map, callback);
  }

  /**
   * @param apkUrl 下载地址
   * @dw 下载最新apk
   */
  public static void getNewVersionApk(String apkUrl, FileCallBack fileCallBack) {
    OkHttpUtils.post().url(apkUrl).build().execute(fileCallBack);
  }

  public static void uploadAuthInfo(String uid, String auth_code, StringCallback callback) {
    GetBuilder builder = OkHttpUtils.get().url(YueJianAppAppConfig.UPLOADAUTHINFO);
    builder.addParams("uid", uid);
    builder.addParams("auth_code", auth_code);
    builder.build().execute(callback);
  }
}
