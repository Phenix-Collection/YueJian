package com.mingquan.yuejian.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.alipay.YueJianAppAuthResult;
import com.mingquan.yuejian.api.remote.YueJianAppApiUtils;
import com.mingquan.yuejian.api.remote.YueJianAppPhoneLiveApi;
import com.mingquan.yuejian.base.YueJianAppBaseFullModeActivity;
import com.mingquan.yuejian.interf.YueJianAppINomalDialog;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppSharedPreUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppThreadManager;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class YueJianAppWebViewActivity extends YueJianAppBaseFullModeActivity implements View.OnClickListener {
    private static final String TAG = YueJianAppWebViewActivity.class.getSimpleName();
    @BindView(R.id.left_btn)
    RelativeLayout rlBack;
    @BindView(R.id.web_view)
    WebView webView;
    private String url;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
    private static final int SDK_AUTH_FLAG = 1001;
    private UIHandler mHandler;
    private Context mContext;
    private Activity mActivity;
    private String mAuthInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.yue_jian_app_activity_webview;
    }

    @JavascriptInterface
    @Override
    public void initView() {
        mContext = this;
        mActivity = this;
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != webView) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                }
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        addJavascriptInterface(); //添加JS回调方法
        WebChromeClient chromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                llTitle.setTitleText(title);
            }

            //扩展浏览器上传文件
            // 3.0++版本
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooserImpl(uploadMsg);
            }

            // 3.0--版本
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooserImpl(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooserImpl(uploadMsg);
            }

            // For Android > 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg,
                                             FileChooserParams fileChooserParams) {
                openFileChooserImplForAndroid5(uploadMsg);
                return true;
            }
        };

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                setActionBarTitle(view.getTitle());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        webView.clearHistory();
                        finish();
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return false;
                }

                if (url.startsWith("close:")) {
                    finish();
                    return false;
                }

                if (url.startsWith("login:")) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        webView.clearHistory();
                        finish();
                    }
                    return false;
                }
                view.loadUrl(url);
                return false;
            }
        });

        webView.setWebChromeClient(chromeClient);
    }

    /**
     * 加载到cookie
     */
    public void asyncCookie() {
        CookieSyncManager.createInstance(getApplicationContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        //参数 - 单独形式
        cookieManager.setCookie(url, "PEPPERTV_UID=" + YueJianAppAppContext.getInstance().getLoginUid());
        cookieManager.setCookie(url, "PEPPERTV_TOKEN=" + YueJianAppAppContext.getInstance().getToken());
        String test = cookieManager.getCookie(url);
        //最后一定要调用
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 添加js调用java方法
     */
    protected void addJavascriptInterface() {
        webView.addJavascriptInterface(new JSMethod(), "pp");
    }

    /**
     * JS方法回调接口
     */
    public class JSMethod {
        public JSMethod() {
        }

        @JavascriptInterface
        public void aliAuth(String authInfo) {
            alipayAuth(authInfo);
        }

        @JavascriptInterface
        public void sendEasemobSms(String uid, String toUid, String content) {

        }

        @JavascriptInterface
        public void jsCallMobileNativeFunction(String message) {
            try {
                JSONObject jsonMessage = new JSONObject(message);
                String methodName = jsonMessage.getString("method");
                if (methodName.equals("activity_agent_share")) { // 分享推广
                    // 弹出分享推广菜单
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            YueJianAppDialogHelp.showShareInvitationDialog(YueJianAppWebViewActivity.this);
                        }
                    });
                } else if (methodName.equals("activity_buy_vip_card")) { // 购买vip
                    int vipLevel = jsonMessage.getInt("params");
                    YueJianAppApiProtoHelper.sendACBuyVipCardReq(
                            YueJianAppWebViewActivity.this,
                            YueJianAppAppContext.getInstance().getLoginUid(),
                            YueJianAppAppContext.getInstance().getToken(),
                            vipLevel,
                            new YueJianAppApiProtoHelper.ACBuyVipCardReqCallback() {
                                @Override
                                public void onError(int errCode, String errMessage) {
                                    if (errCode == YueJianAppApiProtoHelper.ERR_NO_DIAMOND) {
                                        YueJianAppDialogHelp.showDialog(mContext, "您的钻石数不足，是否立即充值？", new YueJianAppINomalDialog() {
                                            @Override
                                            public void cancelDialog(View v, Dialog d) {
                                                d.dismiss();
                                            }

                                            @Override
                                            public void determineDialog(View v, Dialog d) {
                                                YueJianAppDialogHelper.showRechargeDialogFragment(getSupportFragmentManager());
                                                d.dismiss();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onResponse() {
                                    Toast.makeText(YueJianAppWebViewActivity.this, "购买成功！", Toast.LENGTH_SHORT).show();
                                    webView.reload();
                                }
                            });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static class UIHandler extends Handler {
        private WeakReference<YueJianAppWebViewActivity> mWeakReference;

        public UIHandler(YueJianAppWebViewActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_AUTH_FLAG: //验证结果信
                    YueJianAppAuthResult authResult = new YueJianAppAuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    String result = authResult.getResult();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000")
                            && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        final String res = authResult.getResult(); // 获取result中的信息传给后台
                        String[] mResultArray = res.split("&");
                        HashMap<String, String> resultMap = new HashMap<>();
                        /**将结果存入HashMap*/
                        for (int i = 0; i < mResultArray.length; i++) {
                            String subStr = mResultArray[i];
                            if (subStr.contains("=")) {
                                String[] subArr = subStr.split("=");
                                resultMap.put(subArr[0], subArr[1]);
                            }
                        }
                        final String uid = YueJianAppAppContext.getInstance().getLoginUid();
                        String auth_code = resultMap.get("auth_code");
                        YueJianAppPhoneLiveApi.uploadAuthInfo(uid, auth_code, new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                Log.e(TAG, "onError 认证失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG, "onResponse :" + response);
                                if (TextUtils.isEmpty(response))
                                    return;
                                //解析json,刷新WebView页面
                                if (YueJianAppApiUtils.checkSuccess(response)) {
                                    if (mWeakReference.get().webView != null) {
                                        mWeakReference.get().webView.reload();
                                    }
                                    //认证成功，将已认证信息添加在SharedPreference中
                                    YueJianAppSharedPreUtil.put(mWeakReference.get(), "has_auth" + uid, true);
                                }
                            }
                        });
                    } else {
                        // 其他状态值则为授权失败
                        Toast
                                .makeText(mWeakReference.get().mContext,
                                        "授权失败" + String.format("authCode:%s", authResult.getAuthCode()),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
            }
        }
    }

    /**
     * 处理支付宝无限账户授权，第三方登录
     */
    private void alipayAuth(final String authInfo) {
        YueJianAppThreadManager.getThreadPool().execute(new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(YueJianAppWebViewActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);
                //                Map<String, String> result = authTask.auth(authInfo);
                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            if (null != webView) {
                if (webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

    @Override
    public void initData() {
        Bundle extras = getIntent().getBundleExtra("URL_INFO");
        url = extras.getString("URL");
        String title = extras.getString("TITLE");
        webView.loadUrl(url);
        mHandler = new UIHandler(YueJianAppWebViewActivity.this);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(
                "webview"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("webview"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
        // onPageEnd 在onPause 之前调用,因为 onPause
        // 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != webView) {
            ((ViewGroup) webView.getParent()).removeAllViews();
            webView.removeAllViews();
            webView.setVisibility(View.GONE);
            webView.destroy();
            webView = null;
        }
        System.gc();
        finish();
    }
}
