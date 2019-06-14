package com.mingquan.yuejian.api.remote;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Set;
public class YueJianAppBasicApi {
  private LinkedHashMap<String, Object> mMap;
  public YueJianAppBasicApi(LinkedHashMap<String, Object> map) {
    mMap = map;
  }

  /**
   * get
   *
   * @param map
   * @return
   */
  public static void doGet(LinkedHashMap<String, String> map, Callback callback) {
    GetBuilder builder = OkHttpUtils.get().url(YueJianAppAppConfig.MAIN_URL);
    if (null == map || map.isEmpty()) {
      builder.build().execute(callback);
      return;
    }
    Set<String> set = map.keySet();
    if (set.isEmpty()) {
      builder.build().execute(callback);
      return;
    }
    for (String key : set) {
      builder.addParams(key, map.get(key));
    }
    builder.build().execute(callback);
    builder.build().cancel();
  }

  /**
   * get
   *
   * @param map
   * @return
   */
  public static void doGet(LinkedHashMap<String, String> map, StringCallback callback, String url) {
    GetBuilder builder = OkHttpUtils.get().url(url);
    if (null == map || map.isEmpty()) {
      builder.build().execute(callback);
      return;
    }
    Set<String> set = map.keySet();
    if (null == set || set.isEmpty()) {
      builder.build().execute(callback);
      return;
    }
    for (String key : set) {
      builder.addParams(key, map.get(key));
    }
    builder.build().execute(callback);
    builder.build().cancel();
  }

  /**
   * post file
   *
   * @param map
   * @param callback
   */
  public static void postFile(LinkedHashMap<String, Object> map, StringCallback callback) {
    PostFormBuilder builder = OkHttpUtils.post();
    builder.addFile("file", String.valueOf(map.get("type")), (File) map.get("file"));
    if (null == map || map.isEmpty()) {
      builder.build().execute(callback);
      return;
    }
    Set<String> set = map.keySet();
    if (null == set || set.isEmpty()) {
      builder.build().execute(callback);
      return;
    }
    for (String key : set) {
      builder.addParams(key, String.valueOf(map.get(key)));
    }
    builder.url(String.valueOf(map.get("url"))).build().execute(callback);
    builder.build().cancel();
  }
}
