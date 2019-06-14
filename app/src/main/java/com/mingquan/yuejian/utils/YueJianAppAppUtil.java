package com.mingquan.yuejian.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class YueJianAppAppUtil {
    private YueJianAppAppUtil() {
    }

    private static YueJianAppAppUtil mInstance;
    private IPermissionsResult mPermissionsResult;
    private int mRequestCode = 100;//权限请求码

    public static YueJianAppAppUtil getInstance() {
        if (mInstance == null) {
            mInstance = new YueJianAppAppUtil();
        }
        return mInstance;
    }

    /**
     * 返回当前程序版本名
     */
    public String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            //            versionCode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 获取View的宽度
     *
     * @param view
     * @return
     */
    public static int getViewWidth(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredWidth();
    }

    /**
     * 获取View的高度
     *
     * @param view
     * @return
     */
    public static int getViewHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight();
    }

    public void checkPermissions(Activity context, String[] permissions, @NonNull IPermissionsResult permissionsResult) {
        mPermissionsResult = permissionsResult;

        if (Build.VERSION.SDK_INT < 23) {//6.0才用动态权限
            YueJianAppTLog.info("check permission sdk < 23");
            permissionsResult.passPermissons();
            return;
        }

        // 创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPermissionList中
        List<String> mPermissionList = new ArrayList<>();
        //逐个判断你要的权限是否已经通过
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);// 添加还未授予的权限
            }
        }

        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(context, permissions, mRequestCode);
        } else {
            //说明权限都已经通过，可以做你想做的事情去
            YueJianAppTLog.info("check permission all pass");
            permissionsResult.passPermissons();
        }
    }

    public void checkPermissions(Activity context, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT < 23) {//6.0才用动态权限
            return;
        }
        mRequestCode = requestCode;
        // 创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPermissionList中
        List<String> mPermissionList = new ArrayList<>();
        // 逐个判断你要的权限是否已经通过
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);//添加还未授予的权限
            }
        }

        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(context, permissions, mRequestCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被禁用
            if (hasPermissionDismiss) {
                mPermissionsResult.forbitPermissons();
            } else {
                //全部权限通过，可以进行下一步操作。。。
                mPermissionsResult.passPermissons();
            }
        }
    }

    /**
     * 不再提示权限时的展示对话框
     */
    private AlertDialog mPermissionDialog;

    public void showSystemPermissionsSettingDialog(final Activity context, String message, boolean cancelable) {
        if (context.isFinishing()) {
            YueJianAppTLog.error("Activity is finishing can not show dialog");
            return;
        }
        final String mPackName = context.getPackageName();
        if (mPermissionDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            context.startActivity(intent);
                        }
                    });
            if (cancelable) {
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //关闭页面或者做其他操作
                        cancelPermissionDialog();
                    }
                });
            }
            mPermissionDialog = builder.create();
        }
        mPermissionDialog.setCancelable(cancelable);
        mPermissionDialog.show();
    }

    //关闭对话框
    private void cancelPermissionDialog() {
        if (mPermissionDialog != null) {
            mPermissionDialog.cancel();
            mPermissionDialog = null;
        }
    }

    public interface IPermissionsResult {
        void passPermissons();

        void forbitPermissons();
    }
}
