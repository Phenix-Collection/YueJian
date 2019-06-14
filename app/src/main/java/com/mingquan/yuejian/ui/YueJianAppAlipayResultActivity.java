package com.mingquan.yuejian.ui;

import android.view.View;
import android.widget.TextView;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseActivity;

import butterknife.BindView;

public class YueJianAppAlipayResultActivity extends YueJianAppBaseActivity {
  @BindView(R.id.tv_alipaypay_result) TextView mAliPayResult;
  @Override
  protected int getLayoutId() {
    return R.layout.yue_jian_app_activity_alipay_result;
  }

  @Override
  public void initView() {}

  @Override
  protected boolean hasBackButton() {
    return true;
  }

  @Override
  public void initData() {
    setActionBarTitle(getString(R.string.payresult));
    if (getIntent().getIntExtra("result", 0) == 1) {
      mAliPayResult.setText("ok");
    } else {
      mAliPayResult.setText("no");
    }
  }

  @Override
  public void onClick(View v) {}
}
