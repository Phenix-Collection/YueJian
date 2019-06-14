package com.mingquan.yuejian.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.Unbinder;

/**
 * 微信支付的回调类
 */
public class YueJianAppWXPayEntryActivity extends Activity implements IWXAPIEventHandler {
  private static final String TAG = "MicroMsg.SDKSample.YueJianAppWXPayEntryActivity";

  @BindView(R.id.ll_back) LinearLayout mLayBack;
  @BindView(R.id.lay_title) RelativeLayout mLayTitle;
  @BindView(R.id.tv_title) TextView mTitle;

  @BindView(R.id.tv_hint) TextView mTvHint;

  private IWXAPI api;
  private Unbinder unbinder;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.yue_jian_app_activity_wx_result);
    unbinder = ButterKnife.bind(this);
    api = WXAPIFactory.createWXAPI(this, YueJianAppAppConfig.WCHAT_APP_ID); //参数，微信的appId
//    api = WXAPIFactory.createWXAPI(this, "wx3a4a3e4c8501bbee"); //参数，微信的appId
    api.handleIntent(getIntent(), this);
    initView();
  }

  private void initView() {
    mLayTitle.setBackgroundColor(YueJianAppAppContext.getInstance().getmMainColor());
    mTvHint.setTextColor(YueJianAppAppContext.getInstance().getmMainColor());
    mTitle.setText("充值结果");
    mLayBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    api.handleIntent(intent, this);
  }

  @Override
  public void onReq(BaseReq req) {}

  @Override
  public void onResp(BaseResp resp) {
    // 李建涛 支付成功后的通知
    if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
      switch (resp.errCode) {
        case -2: //支付取消
          mTvHint.setText("支付取消");
          break;
        case 0: //充值成功
          //          YueJianAppTLog.error("YueJianAppWXPayEntryActivity 微信充值成功");
          YueJianAppTLog.error(resp.toString());
          mTvHint.setText("充值成功");
          Intent intent = new Intent(YueJianAppAppConfig.ACTION_RECHARGE_DIAMOND);
          sendBroadcast(intent);
          break;
        default:
          break;
      }

      finish(); //关闭页面
      //发送发送广播 提示充值情况
      /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_tip);
        builder.setMessage(getString(R.string.pay_result_callback_msg,
        String.valueOf(resp.errCode)));
        builder.show();*/
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }
}