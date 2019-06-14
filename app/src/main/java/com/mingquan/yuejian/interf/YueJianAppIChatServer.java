package com.mingquan.yuejian.interf;

import com.mingquan.yuejian.bean.YueJianAppChatBean;
import com.mingquan.yuejian.bean.YueJianAppSendGiftBean;

/**
 * Created by Administrator on 2016/3/17.
 */
public interface YueJianAppIChatServer {
  void onConnect(boolean res);

  void onShowSendGift(YueJianAppSendGiftBean contentJson, YueJianAppChatBean chatBean, int index, String broadCastVotes);

  void onError();
}
