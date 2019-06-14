package com.mingquan.yuejian.api.remote;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.YueJianAppAppManager;
import com.mingquan.yuejian.utils.MD5;
import com.mingquan.yuejian.utils.YueJianAppRavenUtils;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import okhttp3.Call;

public class YueJianAppApiUtils {
    public final static int SUCCESS_CODE = 200; //成功请求到服务端
    public final static String TOKEN_TIMEOUT = "700";

    // API错误码定义
    public final static int API_ERROR_EXCEPTION = 5000; // 请求异常
    public final static int API_ERROR_NO_RESPONSE = 5001; // 未收到返回值
    public final static int API_ERROR_LACK_DATA_FIELD = 5002; // 缺少data字段
    public final static int API_ERROR_LACK_INFO_FIELD = 5003; // 缺少info字段
    public final static int API_ERROR_INVALID_JSON_DATA = 5004; // 解析json数据失败

    // 定义协议回调接口
    public interface ApiAsyncQueryCallback {
        void onError(int errCode, String errMessage);

        void onResponse(JSONObject json);
    }

    /**
     * 调用服务器的指定接口
     *
     * @param context  context
     * @param api      api的名称
     * @param params   请求参数
     * @param callback 请求结果的回调对象
     */
    public static void asyncQueryApi(final Activity context, final String api,
                                     LinkedHashMap<String, String> params, final ApiAsyncQueryCallback callback) {
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("sign", YueJianAppApiUtils.createSign(params));

        YueJianAppBasicApi.doGet(params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                callback.onError(API_ERROR_EXCEPTION, "error");
                HashMap<String, Object> extension = new HashMap<String, Object>();
                extension.put("api", api);
                YueJianAppTLog.error("doGet api:%s,exception:%s", api, e);
                YueJianAppRavenUtils.logException("请求" + api + "接口异常", e, extension);
            }

            @Override
            public void onResponse(String resStr) {
                if (resStr == null || resStr.isEmpty()) {
                    YueJianAppAppContext.showToastAppMsg(context, "网络异常");
                    callback.onError(API_ERROR_NO_RESPONSE, "网络异常");
                    return;
                }

                String current_field_name = "";
                try {
                    JSONObject resJson = new JSONObject(resStr);
                    YueJianAppTLog.info("调asyncQueryApi>>>%s", resJson.toString());
                    // 判断http请求是否成功
                    current_field_name = "ret";
                    String ret = resJson.getString("ret");
                    current_field_name = "msg";
                    String msg = resJson.getString("msg");
                    if (Integer.parseInt(ret) != SUCCESS_CODE) {
                        YueJianAppAppContext.showToastAppMsg(context, msg);
                        callback.onError(Integer.parseInt(ret), msg);
                        return;
                    }

                    // 解析服务器的返回结果
                    current_field_name = "data";
                    JSONObject dataJson = resJson.getJSONObject("data");
                    if (dataJson == null) {
                        callback.onError(API_ERROR_LACK_DATA_FIELD, "Lack data field!");
                        return;
                    }

                    // 处理token失效的错误
                    current_field_name = "code";
                    int code = dataJson.getInt("code");
                    current_field_name = "msg";
                    String msg2 = dataJson.getString("msg");
                    if (code == 700) {
                        callback.onError(code, msg2);
                        if (context != null) {
                            YueJianAppAppManager.getAppManager().finishAllActivity();
                            YueJianAppUIHelper.showLoginSelectActivity(context);
                            context.finish();
                        }
                        return;
                    } else if (code != 0) {
                        callback.onError(code, msg2);
                        return;
                    }

                    // 解析服务器返回的数据信息
                    current_field_name = "info";
                    if (!dataJson.has("info")) {
                        callback.onError(API_ERROR_LACK_INFO_FIELD, "Lack info field!");
                        return;
                    }
                    JSONObject infoJson = dataJson.optJSONObject("info");
                    //          JSONObject infoJson = dataJson.getJSONObject("info");//此处info == []
                    //          故会出现解析异常
                    if (infoJson == null) {
                        YueJianAppTLog.info("Json为空info == []  >>>API:%s", api);
                        infoJson = new JSONObject("{}");
                    }
                    callback.onResponse(infoJson);
                } catch (JSONException e) {
                    callback.onError(API_ERROR_INVALID_JSON_DATA, "解析" + api + "返回结果失败！");
                    HashMap<String, Object> extension = new HashMap<String, Object>();
                    extension.put("api", api);
                    extension.put("field", current_field_name);
                    YueJianAppRavenUtils.logException("解析" + api + "的返回结果失败", e, extension);
                }
            }
        });
    }

    /**
     * @return JsonObject
     * @dw 检测服务端请求是否有异常, 如果成功返回结果中的info json对象
     */
    public static String checkIsSuccess(String res, Activity context) {
        try {
            if (res == null || res.isEmpty()) {
                YueJianAppAppContext.showToastAppMsg(context, "网络异常");
                return null;
            }
            JSONObject resJson = new JSONObject(res);
            if (Integer.parseInt(resJson.getString("ret")) == SUCCESS_CODE) {
                JSONObject dataJson = resJson.getJSONObject("data");
                if (dataJson == null)
                    return null;
                String code = dataJson.getString("code");
                if (code.equals(TOKEN_TIMEOUT)) {
                    if (context != null) {
                        YueJianAppAppManager.getAppManager().finishAllActivity();
                        YueJianAppUIHelper.showLoginSelectActivity(context);
                        context.finish();
                    }

                    return null;
                } else if (!code.equals("0")) {
                    YueJianAppAppContext.showToastAppMsg(context, dataJson.getString("msg"));
                    return null;
                } else {
                    return dataJson.get("info").toString();
                }
            } else {
                YueJianAppAppContext.showToastAppMsg(context, resJson.getString("msg"));
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String checkIsSuccess(String res) {
        try {
            if (res == null || res.isEmpty() || TextUtils.isEmpty(res))
                return null;
            JSONObject resJson = new JSONObject(res);
            if (Integer.parseInt(resJson.getString("ret")) == SUCCESS_CODE) {
                JSONObject dataJson = resJson.getJSONObject("data");
                if (dataJson == null)
                    return null;
                String code = dataJson.getString("code");
                if (code.equals(TOKEN_TIMEOUT)) {
                    YueJianAppAppManager.getAppManager().finishAllActivity();
                    YueJianAppUIHelper.showLoginSelectActivity(YueJianAppAppContext.getInstance());
                    return null;
                } else if (!code.equals("0")) {
                    if (!TextUtils.isEmpty(dataJson.get("msg").toString())) {
                        Toast
                                .makeText(
                                        YueJianAppAppContext.getInstance(), dataJson.get("msg").toString(), Toast.LENGTH_SHORT)
                                .show();
                    }
                    return null;
                } else {
                    return dataJson.get("info").toString();
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String checkIsSuccess2(String res) {
        try {
            if (res == null)
                return "";
            JSONObject resJson = new JSONObject(res);

            if (Integer.parseInt(resJson.getString("ret")) == SUCCESS_CODE) {
                JSONObject dataJson = resJson.getJSONObject("data");
                if (dataJson == null)
                    return null;
                String code = dataJson.getString("code");
                if (code.equals(TOKEN_TIMEOUT)) {
                    YueJianAppAppManager.getAppManager().finishAllActivity();
                    YueJianAppUIHelper.showLoginSelectActivity(YueJianAppAppContext.getInstance());
                    return null;
                } else if (!code.equals("0")) {
                    return null;
                } else {
                    return dataJson.get("info").toString();
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * create sign for api
     *
     * @param map
     * @return
     */
    static String createSign(LinkedHashMap<String, String> map) {
        StringBuilder sign = new StringBuilder("");
        Set<String> set = map.keySet();
        for (String s : set) {
                sign.append(s);
                sign.append("=");
                sign.append(YueJianAppStringUtil.decodeStr(map.get(s)) + "&");
        }
        return MD5.parseStrToMd5L32(sign.substring(0, sign.length() - 1) + YueJianAppAppConfig.SIGN);
    }

    public static boolean checkSuccess(String res) {
        try {
            if (res == null || res.isEmpty() || TextUtils.isEmpty(res))
                return false;
            JSONObject resJson = new JSONObject(res);
            if (Integer.parseInt(resJson.getString("ret")) == SUCCESS_CODE) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取QQ授权信息的unionid
     *
     * @param token
     * @param callback
     */
    public static void getQQUnionId(String token, StringCallback callback) {
        OkHttpUtils.get()
                .url("https://graph.qq.com/oauth2.0/me?access_token=" + token + "&unionid=1")
                .build()
                .execute(callback);
    }

}
