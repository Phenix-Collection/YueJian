package com.mingquan.yuejian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppConst;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseFullModeActivity;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.vchat.YueJianAppXTemplateTitle;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 修改资料
 */
public class YueJianAppEditInfoActivity extends YueJianAppBaseFullModeActivity {
  @BindView(R.id.title_layout) YueJianAppXTemplateTitle mTitleLayout;
  @BindView(R.id.edit_input) EditText mInPutText;
  @BindView(R.id.tv_prompt) TextView mPrompt;
  @BindView(R.id.btn_save) Button mBtnSave;
  @BindView(R.id.ll_back) LinearLayout mIvBack;
  @BindView(R.id.iv_editInfo_clean) ImageView mInfoClean;
  public final static String EDITKEY = "EDITKEY";
  public final static String EDITACTION = "EDITACTION";
  public final static String EDITPROMP = "EDITPROMP";
  public final static String EDITDEFAULT = "EDITDEFAULT";
  private TextView mTvTitle;
  private Intent intent;
  private String key;
  private String value;

  @Override
  protected void init(Bundle savedInstanceState) {
    super.init(savedInstanceState);
    intent = getIntent();
  }

  @Override
  public void initView() {
    mBtnSave.setOnClickListener(this);
    mIvBack.setOnClickListener(this);
    mInfoClean.setOnClickListener(this);
  }

  @Override
  public void initData() {
    if (intent != null) {
      mTvTitle.setText(intent.getStringExtra(EDITACTION));
      mTitleLayout.setTitleText(intent.getStringExtra(EDITACTION));
      mTitleLayout.setLeftBtnListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          finish();
        }
      });
      mPrompt.setText(intent.getStringExtra(EDITPROMP));
      mInPutText.setText(intent.getStringExtra(EDITDEFAULT));
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_save:
        saveInfo();
        break;
      case R.id.iv_editInfo_clean:
        mInPutText.setText("  ");
        break;
    }
  }

  /**
   * @dw 提交修改数据
   */
  private void saveInfo() {
    key = intent.getStringExtra(EDITKEY);
    value = mInPutText.getText().toString().trim();
    if (YueJianAppStringUtil.isEmpty(value)) {
      YueJianAppAppContext.showToastAppMsg(YueJianAppEditInfoActivity.this, "请填写内容!");
      return;
    }
    if (key.equals("user_nicename") && value.length() > 8) {
      YueJianAppAppContext.showToastAppMsg(YueJianAppEditInfoActivity.this, "昵称长度超过限制!");
      return;
    } else if (key.equals("signature") && value.length() > 20) {
      YueJianAppAppContext.showToastAppMsg(YueJianAppEditInfoActivity.this, "签名长度超过限制!");
      return;
    } else if (key.equals("user_nicename") && value.length() < 2) {
      YueJianAppAppContext.showToastAppMsg(YueJianAppEditInfoActivity.this, "昵称长度不少于两位!");
      return;
    }
    preSaveInfo();
  }

  /**
   * 发送前的操作
   */
  private void preSaveInfo() {
    String preSendMsg = mInPutText.getText().toString().trim();
    if (YueJianAppStringUtil.isEmpty(preSendMsg))
      return;
    if (matchKey(preSendMsg)) {
      doSavaInfo(preSendMsg);
    } else {
      YueJianAppAppContext.showToastAppMsg(this, "含有非法字符");
    }
  }

  /**
   * 保存用户信息com.
   */
  public void doSavaInfo(String value) {
    if (key.equals("user_nicename")) {
      YueJianAppApiProtoHelper.sendACUpdateNicknameReq(this,
          YueJianAppAppContext.getInstance().getLoginUid(),
          YueJianAppAppContext.getInstance().getToken(),
              YueJianAppStringUtil.encodeStr(value),
          new YueJianAppApiProtoHelper.ACUpdateNicknameReqCallback() {
            @Override
            public void onError(int errCode, String errMessage) {
              YueJianAppTLog.error("update nickname error:%s", errMessage);
              YueJianAppAppContext.showToastAppMsg(YueJianAppEditInfoActivity.this, getString(R.string.editfail));
            }

            @Override
            public void onResponse(String nickname) {
              YueJianAppAppContext.showToastAppMsg(YueJianAppEditInfoActivity.this, getString(R.string.editsuccess));
              YueJianAppACUserPublicInfoModel publicInfoModel = YueJianAppAppContext.getInstance().getAcPublicUser();
              publicInfoModel.setName(nickname);
              YueJianAppAppContext.getInstance().updateAcPublicUser(publicInfoModel);
              finish();
            }
          });
    } else if (key.equals("signature")) {
      YueJianAppApiProtoHelper.sendACUpdateSignatureReq(this,
          YueJianAppAppContext.getInstance().getLoginUid(),
          YueJianAppAppContext.getInstance().getToken(),
              YueJianAppStringUtil.encodeStr(value),
          new YueJianAppApiProtoHelper.ACUpdateSignatureReqCallback() {
            @Override
            public void onError(int errCode, String errMessage) {
              YueJianAppAppContext.showToastAppMsg(YueJianAppEditInfoActivity.this, getString(R.string.editfail));
            }

            @Override
            public void onResponse(String signature) {
              YueJianAppAppContext.showToastAppMsg(YueJianAppEditInfoActivity.this, getString(R.string.editsuccess));
              YueJianAppACUserPublicInfoModel publicInfoModel = YueJianAppAppContext.getInstance().getAcPublicUser();
              publicInfoModel.setSignature(signature);
              YueJianAppAppContext.getInstance().updateAcPublicUser(publicInfoModel);
              finish();
            }
          });
    }
  }

  private boolean matchKey(String sendMsg) {
    boolean isLegal = true;
    SpannableStringBuilder builder = new SpannableStringBuilder(sendMsg);
    int index = 0;
    while (index < sendMsg.length() - 1) {
      String word = sendMsg.substring(index, index + 1);
      if (YueJianAppAppConst.mKeyWords.containsKey(word)) {
        ArrayList<String> mKeyList = YueJianAppAppConst.mKeyWords.get(word);
        isLegal = replaceValue(mKeyList, builder);
        break;
      }
      index++;
    }
    return isLegal;
  }

  /**
   * 将关键字替换为心图标
   */
  private boolean replaceValue(ArrayList<String> mKeyList, SpannableStringBuilder builder) {
    boolean isLegal = true;
    String sendMsg = builder.toString();
    for (int i = 0; i < mKeyList.size(); i++) {
      if (sendMsg.contains(mKeyList.get(i))) {
        int index = sendMsg.indexOf(mKeyList.get(i));
        builder.replace(index, index + mKeyList.get(i).length(), "*");
        isLegal = false;
        break;
      }
    }
    return isLegal;
  }

  @Override
  protected int getLayoutId() {
    return R.layout.yue_jian_app_activity_edit;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void initActionBar(ActionBar actionBar) {
    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    actionBar.setCustomView(R.layout.yue_jian_app_view_actionbar_title);
    actionBar.getCustomView()
        .findViewById(R.id.base_title)
        .setBackgroundColor(YueJianAppAppContext.getInstance().getmMainColor());
    mTvTitle = (TextView) actionBar.getCustomView().findViewById(R.id.tv_actionBarTitle);
  }

  public void onResume() {
    super.onResume();
    MobclickAgent.onPageStart(
        "编辑资料"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
    MobclickAgent.onResume(this); //统计时长
  }

  public void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd("编辑资料"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
    // onPageEnd 在onPause 之前调用,因为 onPause
    // 中会保存信息。"SplashScreen"为页面名称，可自定义
    MobclickAgent.onPause(this);
  }
}