package com.mingquan.yuejian.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;

import java.util.Date;

public abstract class YueJianAppPhoneLiveChatRow extends LinearLayout {
  protected Context context;
  protected Activity activity;
  protected EMMessage message;
  protected int position;
  protected BaseAdapter adapter;
  protected LayoutInflater inflater;
  private YueJianAppAvatarView mUhead;
  protected EMCallBack messageSendCallback;
  protected TextView percentageView;

  public YueJianAppPhoneLiveChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
    super(context);
    this.context = context;
    this.activity = (Activity) context;
    this.message = message;
    this.position = position;
    this.adapter = adapter;
    inflater = LayoutInflater.from(context);

    initView();
  }

  private void initView() {
    onInflatView();
    mUhead = (YueJianAppAvatarView) findViewById(R.id.av_message_head);
    onFindViewById();
  }

  public void setUpView(EMMessage message, int position) {
    this.message = message;
    this.position = position;
    setUpBaseView();
    onSetUpView();
  }

  private void setUpBaseView() {
    TextView timestamp = (TextView) findViewById(R.id.timestamp);
    if (timestamp != null) {
      if (position == 0) {
        timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
        timestamp.setVisibility(View.VISIBLE);
      } else {
        EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
        if (prevMessage != null
            && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
          timestamp.setVisibility(View.GONE);
        } else {
          timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
          timestamp.setVisibility(View.VISIBLE);
        }
      }
    }
  }
  /**
   * 设置消息发送callback
   */
  protected void setMessageSendCallback() {
    if (messageSendCallback == null) {
      messageSendCallback = new EMCallBack() {

        @Override
        public void onSuccess() {
          YueJianAppTLog.info("发送成功");
          updateView();
        }

        @Override
        public void onProgress(final int progress, String status) {
          YueJianAppTLog.info("发送中:%s", progress);
        }

        @Override
        public void onError(int code, String error) {
          YueJianAppTLog.info("发送失败:%s", error);
//          updateView();
        }
      };
    }
    message.setMessageStatusCallback(messageSendCallback);
  }
  protected void updateView() {
    activity.runOnUiThread(new Runnable() {
      public void run() {
        onUpdateView();
      }
    });
  }

  /**
   * 填充layout
   */
  protected abstract void onInflatView();

  /**
   * 查找chatrow里的控件
   */
  protected abstract void onFindViewById();

  /**
   * 消息状态改变，刷新listview
   */
  protected abstract void onUpdateView();

  /**
   * 设置更新控件属性
   */
  protected abstract void onSetUpView();

  /**
   * 聊天气泡被点击事件
   */
  protected abstract void onBubbleClick();
}
