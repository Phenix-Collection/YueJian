package com.mingquan.yuejian.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.YueJianAppAppManager;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.utils.YueJianAppUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @dw登录
 */
public class YueJianAppPhoneLoginActivity extends YueJianAppFullScreenModeActivity {
    @BindView(R.id.et_loginphone)
    EditText mEtUserPhone;
    @BindView(R.id.et_logincode)
    EditText mEtCode;
    @BindView(R.id.btn_send_code)
    Button mBtnSendCode;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    private int waitTime = 0;
    private String mUserName = "";
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_login);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        mBtnSendCode.setEnabled(false);
        mBtnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        mEtUserPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim();
                if (YueJianAppUtils.isMobileNO(phone) && waitTime <= 0) {
                    mBtnSendCode.setEnabled(true);
                } else {
                    mBtnSendCode.setEnabled(false);
                }
            }
        });

        mEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 4) {
                    mBtnLogin.setEnabled(false);
                } else {
                    mBtnLogin.setEnabled(true);
                }
            }
        });
    }

    private void sendCode() {
        mUserName = mEtUserPhone.getText().toString();
        if (!mUserName.equals("") && mUserName.length() == 11) {
            waitTime = 30;
            YueJianAppApiProtoHelper.sendACGetSmsCodeReq(
                    this,
                    mUserName,
                    getPackageName(),
                    new YueJianAppApiProtoHelper.ACGetSmsCodeReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            YueJianAppTLog.error("errCode::" + errCode + "  errMessage::" + errMessage);
                        }

                        @Override
                        public void onResponse() {
                            YueJianAppAppContext.showToastAppMsg(YueJianAppPhoneLoginActivity.this, getString(R.string.codehasbeensend));
                            mBtnSendCode.setEnabled(false);
                            mBtnSendCode.setText("(" + waitTime + ")");
                            handler.postDelayed(runnable, 1000);
                        }
                    });
        } else {
            YueJianAppAppContext.showToastAppMsg(
                    YueJianAppPhoneLoginActivity.this, getString(R.string.plasecheckyounumiscorrect));
        }
    }

    private void doLogin() {

        if (prepareForLogin()) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        sendLoginWithPhone();
    }

    private void sendLoginWithPhone() {
        mBtnLogin.setEnabled(false);
        mUserName = mEtUserPhone.getText().toString();
        String password = mEtCode.getText().toString();
        YueJianAppApiProtoHelper.sendACLoginWithPhoneReq(
                this,
                mUserName,
                password,
                String.valueOf(YueJianAppAppContext.getInstance().getPackageInfo().versionCode),
                YueJianAppAppContext.getInstance().getAppChannel(),
                Build.MODEL + ", android " + Build.VERSION.SDK_INT,
                YueJianAppAppContext.getInstance().deviceId,
                getPackageName(),
                new YueJianAppApiProtoHelper.ACLoginWithPhoneReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error("ACLoginWithPhone errorCode:%s,errMessage:%s", errCode, errMessage);
                        YueJianAppAppContext.showToast(errMessage);
                        mBtnLogin.setEnabled(true);
                    }

                    @Override
                    public void onResponse(YueJianAppACUserPublicInfoModel user, String token) {
                        mBtnLogin.setEnabled(true);
                        MobclickAgent.onProfileSignIn(String.valueOf(user.getUid()));
                        //保存用户信息
                        YueJianAppAppContext.getInstance().saveUserInfo(user, token);
                        handler.removeCallbacks(runnable);
                        YueJianAppAppManager.getAppManager().finishAllActivity();
                        YueJianAppUIHelper.showMainActivity(YueJianAppPhoneLoginActivity.this);
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        handler.removeCallbacks(runnable);
    }

    private boolean prepareForLogin() {
        if (!YueJianAppTDevice.hasInternet()) {
            YueJianAppAppContext.showToastShort(R.string.tip_no_internet);
            return true;
        }
        if (mEtUserPhone.length() == 0) {
            mEtUserPhone.setError("请输入手机号码");
            mEtUserPhone.requestFocus();
            return true;
        }

        if (mEtCode.length() == 0) {
            mEtCode.setError("请输入验证码");
            mEtCode.requestFocus();
            return true;
        }

        return false;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            waitTime--;
            mBtnSendCode.setText("(" + waitTime + ")");
            if (waitTime <= 0) {
                handler.removeCallbacks(runnable);
                mBtnSendCode.setText("发送验证码");
                mBtnSendCode.setEnabled(true);
                return;
            }
            handler.postDelayed(this, 1000);
        }
    };


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(
                "手机登录"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("手机登陆"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
        // onPageEnd 在onPause 之前调用,因为 onPause
        // 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
