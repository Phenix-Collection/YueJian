package com.mingquan.yuejian.widget;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.ui.view.YueJianAppChatImageView;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by sunzhenya
 * on 2016/11/25.
 */
public class YueJianAppPhoneLiveChatRowImage extends YueJianAppPhoneLiveChatRow {
  private YueJianAppAvatarView mUhead;
  //    private YueJianAppLoadUrlImageView mImage;
  private YueJianAppChatImageView mImage;

  public YueJianAppPhoneLiveChatRowImage(
      Context context, EMMessage message, int position, BaseAdapter adapter) {
    super(context, message, position, adapter);
  }

  @Override
  protected void onInflatView() {
    inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE
            ? R.layout.yue_jian_app_item_message_left_image
            : R.layout.yue_jian_app_item_message_right_image,
        this);
  }

  @Override
  protected void onFindViewById() {
    mUhead = (YueJianAppAvatarView) findViewById(R.id.av_message_head);
    mImage = (YueJianAppChatImageView) findViewById(R.id.iv_message_image);
  }

  @Override
  protected void onUpdateView() {
    EMImageMessageBody body = (EMImageMessageBody) message.getBody();
    String toUid = message.getTo().replace(YueJianAppAppConfig.IM_ACCOUNT, "");
    YueJianAppTLog.info("image chat on update view from:%s, to:%s, imagePath:%s", message.getFrom(), toUid, body.getRemoteUrl());
    YueJianAppApiProtoHelper.sendACSaveChatMessageReq(null,
            YueJianAppAppContext.getInstance().getLoginUid(),
            YueJianAppAppContext.getInstance().getToken(),
            toUid,
            YueJianAppStringUtil.encodeStr(body.getRemoteUrl()),
            new YueJianAppApiProtoHelper.ACSaveChatMessageReqCallback() {
              @Override
              public void onError(int errCode, String errMessage) {
                Toast.makeText(getContext().getApplicationContext(), errMessage, Toast.LENGTH_SHORT).show();
              }

              @Override
              public void onResponse() {
                YueJianAppTLog.info("保存图片信息成功");
              }
            });
  }

  @Override
  protected void onSetUpView() {
    if (mUhead == null || message == null)
      return;
    EMImageMessageBody imageBody = (EMImageMessageBody) message.getBody();
    try {
      if (message.getFrom().equals("admin")) {
        mUhead.setBackgroundResource(R.drawable.yue_jian_app_ic_launcher);
      } else {
        mUhead.setAvatarUrl(message.getStringAttribute("uhead"));
      }
      if (message.direct() == EMMessage.Direct.SEND) {
        if (!imageBody.getThumbnailUrl().isEmpty()) {
          mImage.setImageLoadUrl(imageBody.getThumbnailUrl());
        } else {
          if (!imageBody.getRemoteUrl().isEmpty()) {
            mImage.setImageLoadUrl(imageBody.getRemoteUrl());
          } else {
            mImage.setImageLoadUrl(imageBody.getLocalUrl());
          }
        }
      } else {
        mImage.setImageLoadUrl(imageBody.getThumbnailUrl());
      }

    } catch (HyphenateException e) {
      e.printStackTrace();
    }
    handleTextMessage();
  }

  protected void handleTextMessage() {
    if (message.direct() == EMMessage.Direct.SEND) {
      setMessageSendCallback();
      switch (message.status()) {
        case CREATE:
          // 发送消息
          //                sendMsgInBackground(message);
          break;
        case SUCCESS: // 发送成功
          break;
        case FAIL: // 发送失败
          break;
        case INPROGRESS: // 发送中
          break;
        default:
          break;
      }
    } else {
      if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
        try {
          EMClient.getInstance().chatManager().ackMessageRead(
              message.getFrom(), message.getMsgId());
        } catch (HyphenateException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  protected void onBubbleClick() {}
}
