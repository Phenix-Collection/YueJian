package com.mingquan.yuejian.utils;

import com.mingquan.yuejian.bean.YueJianAppChatBean;
import com.mingquan.yuejian.bean.YueJianAppSendGiftBean;
import com.mingquan.yuejian.interf.YueJianAppIChatServer;

/**
 * Created by administrato on 2017/5/3.
 */

public class YueJianAppSocketHandlerUtils implements YueJianAppIChatServer {

  @Override
  public void onConnect(boolean res) {

  }

  @Override
  public void onShowSendGift(
          YueJianAppSendGiftBean contentJson, YueJianAppChatBean chatBean, int index, String broadCastVotes) {

  }

  @Override
  public void onError() {}

}
