package com.mingquan.yuejian;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.mingquan.yuejian.base.YueJianAppBaseApplication;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.ui.YueJianAppLiveLoginSelectActivity;
import com.mingquan.yuejian.ui.YueJianAppMainActivity;
import com.mingquan.yuejian.utils.YueJianAppAppUtil;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppThreadManager;
import com.taobao.sophix.SophixManager;

import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.PreferenceHelper;
import org.litepal.LitePal;

import java.io.File;
import java.lang.ref.WeakReference;

import cn.jpush.android.api.JPushInterface;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;

/**
 * 应用启动界面
 */
public class YueJianAppAppStart extends YueJianAppFullScreenModeActivity {
    private TextView mTxtVersion;
    /**
     * 需要进行检测的权限数组
     */
    private String[] needPermissions = {
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) {
            finish();
            return;
        }
        View view = View.inflate(this, R.layout.yue_jian_app_app_start, null);
        mTxtVersion = (TextView) view.findViewById(R.id.tv_version);
        setContentView(view);
        initData();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }
        ((YueJianAppAppContext) getApplication()).initWorkerThread();
    }

    /**
     * 获取数据
     */
    private void initData() {
        mTxtVersion.setText(String.format(getString(R.string.system_version), YueJianAppAppUtil.getInstance().getAppVersionName(this)));
        YueJianAppMySophixApplication.msgDisplayListener = new MyMsgDisplayListener(this);
        SophixManager.getInstance().queryAndLoadNewPatch();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    public static class MyMsgDisplayListener implements YueJianAppMySophixApplication.MsgDisplayListener {
        WeakReference<YueJianAppAppStart> mWeakReference;

        MyMsgDisplayListener(YueJianAppAppStart weakReference) {
            mWeakReference = new WeakReference<YueJianAppAppStart>(weakReference);
        }

        @Override
        public void handle(final int mode, final int code, final String info) {
            if (mWeakReference != null && mWeakReference.get() != null) {
                mWeakReference.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        YueJianAppTLog.info("sophix MyMsgDisplayListener:%s,%s,%s", mode, code, info);
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int cacheVersion =
                PreferenceHelper.readInt(YueJianAppBaseApplication.context(), "first_install", "first_install", -1);
        int currentVersion = YueJianAppTDevice.getVersionCode();
        if (cacheVersion < currentVersion) {
            PreferenceHelper.write(
                    YueJianAppBaseApplication.context(), "first_install", "first_install", currentVersion);
            cleanImageCache();
        }
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void cleanImageCache() {
        final File folder = FileUtils.getSaveFolder("phoneLive/imagecache");
        YueJianAppThreadManager.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                File[] files = folder.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        file.delete();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        YueJianAppAppUtil.getInstance().checkPermissions(
                this,
                needPermissions,
                new YueJianAppAppUtil.IPermissionsResult() {
                    @Override
                    public void passPermissons() {
                        YueJianAppAppContext.getInstance().deviceId = YueJianAppTDevice.getIMEI(YueJianAppAppStart.this);
                        YueJianAppTLog.info("device id:%s", YueJianAppAppContext.getInstance().deviceId);
                        LitePal.initialize(getApplicationContext());

                        // 屏蔽字
                        YueJianAppApiProtoHelper.sendACGetBlockWordsReq(null,
                                new YueJianAppApiProtoHelper.ACGetBlockWordsReqCallback() {
                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        YueJianAppTLog.error("errCode:" + errCode + "  errMessage:" + errMessage);
                                    }

                                    @Override
                                    public void onResponse(int version, String blockWordsUrl) {
                                        YueJianAppTLog.info("version:" + version + ",blockWordsUrl:" + blockWordsUrl);
                                        if (!String.valueOf(version).equals(getProperty("maskWorkVersion"))
                                                && !blockWordsUrl.isEmpty()) {
                                            setProperty("mask_word_url", blockWordsUrl);
                                            setProperty("maskWorkVersion", String.valueOf(version));
                                            YueJianAppAppContext.getInstance().downLoadMaskWords(blockWordsUrl);
                                        } else {
                                            YueJianAppAppConst.getInstance().getDirtyWordsFromFile();
                                        }
                                    }
                                });

                        redirectTo();
                    }

                    @Override
                    public void forbitPermissons() {
                        YueJianAppAppUtil.getInstance().showSystemPermissionsSettingDialog(
                                YueJianAppAppStart.this,
                                "有权限被禁用，须手动设置",
                                false);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        YueJianAppAppUtil.getInstance().onRequestPermissionsResult(requestCode, grantResults);

    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
        if (!YueJianAppAppContext.getInstance().isLogin()) {
            Intent intent = new Intent(this, YueJianAppLiveLoginSelectActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Intent intent = new Intent(this, YueJianAppMainActivity.class);
        startActivity(intent);
        finish();
    }
}
