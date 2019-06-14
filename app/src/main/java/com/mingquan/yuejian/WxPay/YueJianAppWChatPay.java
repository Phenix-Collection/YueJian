package com.mingquan.yuejian.WxPay;

import com.mingquan.yuejian.ui.YueJianAppMyDiamondListActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACWeixinPayDataModel;

/**
 * Created by Administrator on 2016/4/14.
 */
public class YueJianAppWChatPay {
  IWXAPI msgApi;
  // appid
  // wx28dba92c1487ba79
  final String appId = "wx28dba92c1487ba79";
  private YueJianAppMyDiamondListActivity context;
  String nums = "";
  public YueJianAppWChatPay(YueJianAppMyDiamondListActivity context) {
    this.context = context;
    msgApi = WXAPIFactory.createWXAPI(context, null);
    // 将该app注册到微信】
    msgApi.registerApp(appId);
  }

  /**
   * @param price 价格
   * @param num   数量
   * @dw 初始化微信支付
   */
  public void initPay(String price, String num, String itemId) {
    nums = num;
    /* PhoneLiveApi.wxPay(YueJianAppAppContext.getInstance().getLoginUid(), price, new StringCallback() {
         @Override
         public void onError(Call call, Exception e) {

         }

         @Override
         public void onResponse(String response) {
             String res = ApiUtils.checkIsSuccess(response, context);
             if (null == res) return;
             callWxPay(res);

         }
     });*/
    YueJianAppApiProtoHelper.sendACPayWithWeixinReq(context, "" + YueJianAppAppContext.getInstance().getLoginUid(),
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

  private void callWxPay(/*String res*/ YueJianAppACWeixinPayDataModel info) {
    try {
      /*JSONObject signInfo = new JSONObject(res);
       PayReq req = new PayReq();
       req.appId = signInfo.getString("appid");
       req.partnerId = signInfo.getString("partnerid");
       req.prepayId = signInfo.getString("prepayid");//预支付会话ID
       req.packageValue = "Sign=WXPay";
       req.nonceStr = signInfo.getString("noncestr");
       req.timeStamp = signInfo.getString("timestamp");
       req.sign = signInfo.getString("sign");
       if (msgApi.sendReq(req)) {
           YueJianAppAppContext.showToastAppMsg(context, "微信支付");
       } else {
           YueJianAppAppContext.showToastAppMsg(context, "请查看您是否安装微信");
       }*/
      PayReq req = new PayReq();
      req.appId = info.getAppid();
      req.partnerId = info.getPartnerid();
      req.prepayId = info.getPrepayid(); //预支付会话ID
      req.packageValue = "Sign=WXPay";
      req.nonceStr = info.getNoncestr();
      req.timeStamp = String.valueOf(info.getTimestamp());
      req.sign = info.getSign();
      if (msgApi.sendReq(req)) {
        YueJianAppAppContext.showToastAppMsg(context, "微信支付");
      } else {
        YueJianAppAppContext.showToastAppMsg(context, "请查看您是否安装微信");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
