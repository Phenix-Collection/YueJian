package com.mingquan.yuejian.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.fragment.YueJianAppDiamondDialogFragment;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACAliPayDataModel;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import java.util.LinkedHashMap;
import java.util.Set;

public class YueJianAppAliPayDiamondForFragment {
  public static final String TAG = "alipay-sdk";

  private static final int SDK_PAY_FLAG = 1;

  private static final int SDK_CHECK_FLAG = 2;

  private static final int ALIPAY = 3;
  private Activity mPayActivity;
  private YueJianAppDiamondDialogFragment mPayFragment;
  private String rechargeNum;

  public YueJianAppAliPayDiamondForFragment(YueJianAppDiamondDialogFragment fragment) {
    this.mPayFragment = fragment;
    this.mPayActivity = fragment.getActivity();
  }

  public void initPay(int money, String num, String itemId) {
    rechargeNum = num;
    YueJianAppApiProtoHelper.sendACPayWithAliReq(
            mPayActivity,
            YueJianAppAppContext.getInstance().getLoginUid(),
            YueJianAppAppContext.getInstance().getToken(),
            itemId,
            money,
            0,
            new YueJianAppApiProtoHelper.ACPayWithAliReqCallback() {
              @Override
              public void onError(int errCode, String errMessage) {

              }

              @Override
              public void onResponse(String orderInfo) {
                YueJianAppTLog.info("Order Info：%s", orderInfo);
                Message msg = new Message();
                msg.what = ALIPAY;
                msg.obj = orderInfo;
                mHandler.sendMessage(msg);
              }
            });
  }

  private void AldiaoYong(final String payInfo) {
    Runnable payRunnable = new Runnable() {
      @Override
      public void run() {
        // 构造PayTask 对象
        PayTask alipay = new PayTask(mPayActivity);
        // 调用支付接口
        String result = alipay.pay(payInfo, true);
        Message msg = new Message();
        msg.what = SDK_PAY_FLAG;
        msg.obj = result;
        mHandler.sendMessage(msg);
      }
    };

    Thread payThread = new Thread(payRunnable);
    payThread.start();
  }

  @SuppressLint("HandlerLeak")
  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case SDK_PAY_FLAG: {
          YueJianAppResult resultObj = new YueJianAppResult((String) msg.obj);
          String resultStatus = resultObj.toString();
          // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
          if (TextUtils.equals(resultObj.getResultStatus(), "9000")) {
            YueJianAppAppContext.showToastAppMsg(mPayActivity, "支付成功");
            mPayFragment.rechargeResult(rechargeNum);
            YueJianAppTLog.info("充值数量：%s", rechargeNum);
          } else {
            // 判断resultStatus 为非“9000”则代表可能支付失败
            // “8000”
            // 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultObj.getResultStatus(), "8000")) {
              YueJianAppAppContext.showToastAppMsg(mPayActivity, "支付结果确认中");
            } else {
              YueJianAppAppContext.showToastAppMsg(mPayActivity, "支付失败");
            }
          }
          break;
        }
        case SDK_CHECK_FLAG: {
          Toast.makeText(mPayActivity, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
          break;
        }
        case ALIPAY: {
          AldiaoYong((String) msg.obj);
          break;
        }
        default:
          break;
      }
    }
  };

  public String getOrderInfo(final YueJianAppACAliPayDataModel model) {
    StringBuilder info = new StringBuilder();
    final LinkedHashMap map = new LinkedHashMap() {
      {
        put("_input_charset", "\"" + model.getInputCharset() + "\"");
        put("body", "\"" + model.getBody() + "\"");
        put("notify_url", "\"" + model.getNotifyUrl() + "\"");
        put("out_trade_no", "\"" + model.getOutTradeNo() + "\"");
        put("partner", "\"" + model.getPartner() + "\"");
        put("payment_type", "\"" + model.getPaymentType() + "\"");
        put("seller_id", "\"" + model.getSellerId() + "\"");
        put("service", "\"" + model.getService() + "\"");
        put("subject", "\"" + model.getSubject() + "\"");
        put("total_fee", "\"" + model.getTotalFee() + "\"");
        put("sign", "\"" + model.getSign() + "\"");
        put("sign_type", "\"" + model.getSignType() + "\"");
      }
    };
    Set<String> set = map.keySet();
    for (String key : set) {
      info.append(key + "=" + map.get(key));
      info.append("&");
    }
    info.substring(0, info.length() - 1);
    return info.substring(0, info.length() - 1);
  }

  /**
   * sign the order info. 对订单信息进行签名
   *
   * @param content 待签名订单信息
   */
  /*public String sign(String content) {
    return YueJianAppSignUtils.sign(content, YueJianAppKeys.PRIVATE);
  }*/
}
