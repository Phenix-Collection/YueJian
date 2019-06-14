package com.mingquan.yuejian.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.em.YueJianAppChangInfo;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppEditableActionSheetDialog;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.utils.YueJianAppUtils;
import com.mingquan.yuejian.vchat.YueJianAppXTemplateTitle;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/3/9.
 */
public class YueJianAppMyInfoDetailActivity extends YueJianAppFullScreenModeActivity implements View.OnClickListener {
    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout;
    @BindView(R.id.rl_userHead)
    RelativeLayout mRlUserHead;
    @BindView(R.id.rl_userNick)
    RelativeLayout mRlUserNick;
    @BindView(R.id.rl_userSign)
    RelativeLayout mRlUserSign;
    @BindView(R.id.rl_userSex)
    RelativeLayout mRlUserSex;
    @BindView(R.id.tv_userNick)
    TextView mUserNick;
    @BindView(R.id.tv_sign)
    TextView mUserSign;
    @BindView(R.id.av_userHead)
    YueJianAppAvatarView mUserHead;
    @BindView(R.id.tv_sex)
    TextView mSex;

    private YueJianAppACUserPublicInfoModel mUser;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_myinfo_detail);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRlUserNick.setOnClickListener(this);
        mRlUserSign.setOnClickListener(this);
        mRlUserHead.setOnClickListener(this);
        mRlUserSex.setOnClickListener(this);
        mUserHead.setOnClickListener(this); //增加头像事件监听
    }

    @Override
    public void onClick(View v) {
        if (mUser != null) {
            switch (v.getId()) {
                case R.id.rl_userNick:
                    YueJianAppUIHelper.showEditInfoActivity(this, "修改昵称", getString(R.string.editnickpromp),
                            mUser.getName(), YueJianAppChangInfo.CHANG_NICK);
                    break;
                case R.id.rl_userSign:
                    YueJianAppUIHelper.showEditInfoActivity(this, "修改签名", getString(R.string.editsignpromp),
                            mUser.getSignature(), YueJianAppChangInfo.CHANG_SIGN);
                    break;
                case R.id.rl_userHead:
                    YueJianAppUIHelper.showSelectAvatar(this, mUser.getAvatarUrl());
                    break;
                case R.id.rl_userSex:
                    if (YueJianAppUtils.isFastClick()) {
                        return;
                    }
                    showSelectSex();
                    break;
                case R.id.av_userHead:
                    YueJianAppUIHelper.showSelectAvatar(this, mUser.getAvatarUrl()); //选择头像
                    break;
            }
        }
    }

    /**
     * 弹出选择性别窗口
     */
    private void showSelectSex() {
        final YueJianAppEditableActionSheetDialog mDialog = new YueJianAppEditableActionSheetDialog(this).builder();
        TextView manTextView = new TextView(this);
        TextView womenTextView = new TextView(this);
        mDialog.addSheetItem(manTextView, "男", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mSex.setText("男");
                        YueJianAppApiProtoHelper.sendACUpdateGenderReq(YueJianAppMyInfoDetailActivity.this,
                                YueJianAppAppContext.getInstance().getLoginUid(),
                                YueJianAppAppContext.getInstance().getToken(), YueJianAppApiProtoHelper.GENDER_MALE,
                                new YueJianAppApiProtoHelper.ACUpdateGenderReqCallback() {
                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        YueJianAppAppContext.showToastAppMsg(YueJianAppMyInfoDetailActivity.this, "修改失败");
                                    }

                                    @Override
                                    public void onResponse(int gender) {
                                        mUser.setGender(gender);
                                        YueJianAppAppContext.getInstance().updateAcPublicUser(mUser);
                                    }
                                });
                        mDialog.cancel();
                    }
                });
        mDialog.addSheetItem(womenTextView, "女", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mSex.setText("女");
                        YueJianAppApiProtoHelper.sendACUpdateGenderReq(YueJianAppMyInfoDetailActivity.this,
                                YueJianAppAppContext.getInstance().getLoginUid(),
                                YueJianAppAppContext.getInstance().getToken(), YueJianAppApiProtoHelper.GENDER_FEMALE,
                                new YueJianAppApiProtoHelper.ACUpdateGenderReqCallback() {
                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        YueJianAppAppContext.showToastAppMsg(YueJianAppMyInfoDetailActivity.this, "修改失败");
                                    }

                                    @Override
                                    public void onResponse(int gender) {
                                        mUser.setGender(gender);
                                        YueJianAppAppContext.getInstance().updateAcPublicUser(mUser);
                                    }
                                });
                        mDialog.cancel();
                    }
                });
        mDialog.show();
    }

    @Override
    protected void onStart() {
        mUser = YueJianAppAppContext.getInstance().getAcPublicUser();
        YueJianAppTLog.error("mUser:" + mUser);
        fillUI();
        super.onStart();
    }

    private void fillUI() {
        mUserNick.setText(mUser.getName());
        mUserSign.setText(mUser.getSignature());
        mUserHead.setAvatarUrl(mUser.getAvatarUrl());
        mSex.setText(mUser.getGender() == YueJianAppApiProtoHelper.GENDER_MALE ? "男" : "女");
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(
                "个人中心"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人中心"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
        // onPageEnd 在onPause 之前调用,因为 onPause
        // 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
