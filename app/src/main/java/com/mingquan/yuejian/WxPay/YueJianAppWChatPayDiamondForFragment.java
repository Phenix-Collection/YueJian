package com.mingquan.yuejian.WxPay;

import android.app.Activity;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.fragment.YueJianAppDiamondDialogFragment;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACWeixinPayDataModel;

/**
 * Created by Administrator on 2016/4/14.
 */
public class YueJianAppWChatPayDiamondForFragment {
  IWXAPI msgApi;
  private Activity mPayActivity;
  private YueJianAppDiamondDialogFragment mPayFragment;

  String nums = "";
  public YueJianAppWChatPayDiamondForFragment(YueJianAppDiamondDialogFragment fragment) {
    mPayFragment = fragment;
    this.mPayActivity = fragment.getActivity();
    msgApi = WXAPIFactory.createWXAPI(mPayActivity, null);
    // 将该app注册到微信
    msgApi.registerApp(YueJianAppAppConfig.WCHAT_APP_ID);
  }

  /**
   * @param price 价格
   * @param num   数量
   * @dw 初始化微信支付
   */
  public void initPay(String price, String num, String itemId) {
    nums = num;
    YueJianAppApiProtoHelper.sendACPayWithWeixinReq(mPayActivity, "" + YueJianAppAppContext.getInstance().getLoginUid(),
        YueJianAppAppContext.getInstance().getToken(), itemId, Integer.parseInt(price), 0,
        new YueJianAppApiProtoHelper.ACPayWithWeixinReqCallback() {
          @Override
          public void onError(int errCode, String errMessage) {}

          @Override
          public void onResponse(YueJianAppACWeixinPayDataModel data) {
            callWxPay(data);
          }
        });
  }

  private void callWxPay(YueJianAppACWeixinPayDataModel info) {
    try {
      PayReq req = new PayReq();
      req.appId = info.getAppid();
      req.partnerId = info.getPartnerid();
      req.prepayId = info.getPrepayid(); //预支付会话ID
      req.packageValue = "Sign=WXPay";
      req.nonceStr = info.getNoncestr();
      req.timeStamp = String.valueOf(info.getTimestamp());
      req.sign = info.getSign();
      if (msgApi.sendReq(req)) {
        YueJianAppAppContext.showToastAppMsg(mPayActivity, "微信支付");
      } else {
        YueJianAppAppContext.showToastAppMsg(mPayActivity, "请查看您是否安装微信");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
