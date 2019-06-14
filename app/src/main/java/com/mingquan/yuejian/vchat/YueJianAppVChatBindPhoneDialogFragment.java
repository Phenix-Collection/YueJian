package com.mingquan.yuejian.vchat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.auth.YueJianAppEditAuthInfoActivity;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sun on 2018/9/6.
 * <p>
 * 绑定手机页面
 */

public class YueJianAppVChatBindPhoneDialogFragment extends YueJianAppBaseDialogFragment {
    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout; // top bar
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum; // 手机号输入框
    @BindView(R.id.et_auth_code)
    EditText etAuthCode; // 验证码输入框
    @BindView(R.id.btn_bind)
    Button btnBind; // 绑定按钮
    @BindView(R.id.btn_send_code)
    Button btnSendCode; // 发送验证码按钮

    private Unbinder unbinder;
    private String mPhoneNum;
    private int waitTime = 0;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_bind_phone, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEvents();
    }

    private void initEvents() {
        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mPhoneNum = s.toString().trim();
                if (mPhoneNum.length() < 11) {
                    btnSendCode.setEnabled(false);
                    return;
                }

                if (YueJianAppUtils.isMobileNO(mPhoneNum) && waitTime <= 0) {
                    btnSendCode.setEnabled(true);
                }
            }
        });

        etAuthCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 4) {
                    btnBind.setEnabled(true);
                    YueJianAppTDevice.hideSoftKeyboard(etAuthCode);
                } else {
                    btnBind.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });

        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YueJianAppApiProtoHelper.sendACBindPhoneReq(
                        getActivity(),
                        YueJianAppAppContext.getInstance().getLoginUid(),
                        YueJianAppAppContext.getInstance().getToken(),
                        mPhoneNum,
                        etAuthCode.getText().toString().trim(),
                        new YueJianAppApiProtoHelper.ACBindPhoneReqCallback() {
                            @Override
                            public void onError(int errCode, String errMessage) {
                                Toast.makeText(getActivity(), errMessage, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(int errcode) {
                                Toast.makeText(getActivity(), "绑定手机成功", Toast.LENGTH_SHORT).show();
                                if (getActivity() instanceof YueJianAppEditAuthInfoActivity) {
                                    ((YueJianAppEditAuthInfoActivity) getActivity()).setPhoneNum(mPhoneNum);
                                }
                                dismiss();
                            }
                        }
                );
            }
        });
    }

    private void sendCode() {
        waitTime = 30;
        YueJianAppApiProtoHelper.sendACGetSmsCodeReq(
                getActivity(),
                mPhoneNum,
                getActivity().getPackageName(),
                new YueJianAppApiProtoHelper.ACGetSmsCodeReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error("errCode:%s, errMessage:%s", errCode, errMessage);
                    }

                    @Override
                    public void onResponse() {
                        YueJianAppAppContext.showToastAppMsg(getActivity(), getString(R.string.codehasbeensend));
                    }
                });
        btnSendCode.setEnabled(false);
        btnSendCode.setText(String.format("(%d)", waitTime));
        handler.postDelayed(runnable, 1000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            waitTime--;
            if (btnSendCode == null) {
                return;
            }
            btnSendCode.setText(String.format("(%d)", waitTime));
            if (waitTime <= 0) {
                handler.removeCallbacks(runnable);
                btnSendCode.setText("获取验证码");
                btnSendCode.setEnabled(true);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
