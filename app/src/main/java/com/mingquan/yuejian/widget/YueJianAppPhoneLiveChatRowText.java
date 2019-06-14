package com.mingquan.yuejian.widget;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by Administrator on 2016/4/12.
 */
public class YueJianAppPhoneLiveChatRowText extends YueJianAppPhoneLiveChatRow {
    private TextView contentView;
    private YueJianAppAvatarView mUhead;
    private YueJianAppLoadUrlImageView mGiftView;

    public YueJianAppPhoneLiveChatRowText(
            Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.yue_jian_app_item_message_left
                        : R.layout.yue_jian_app_item_message_right,
                this);
    }

    @Override
    protected void onFindViewById() {
        contentView = (TextView) findViewById(R.id.tv_message_text);
        mUhead = (YueJianAppAvatarView) findViewById(R.id.av_message_head);
        mGiftView = (YueJianAppLoadUrlImageView) findViewById(R.id.iv_message_gift);
    }

    @Override
    protected void onUpdateView() {
        EMTextMessageBody body = (EMTextMessageBody) message.getBody();
        String toUid = message.getTo().replace(YueJianAppAppConfig.IM_ACCOUNT, "");
        YueJianAppTLog.info("txt chat on update view from:%s, to:%s, message:%s", message.getFrom(), toUid, body.getMessage());
        YueJianAppApiProtoHelper.sendACSaveChatMessageReq(null,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                toUid,
                YueJianAppStringUtil.encodeStr(body.getMessage()),
                new YueJianAppApiProtoHelper.ACSaveChatMessageReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        Toast.makeText(getContext().getApplicationContext(), errMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse() {
                        YueJianAppTLog.info("保存文本信息成功");
                    }
                });
    }

    @Override
    protected void onSetUpView() {
        if (contentView == null || mUhead == null || message == null) {
            return;
        }
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        // 设置内容
        contentView.setText(txtBody.getMessage());
        try {
            String giftIcon = message.getStringAttribute("giftIcon");
            if (!YueJianAppStringUtil.isEmpty(giftIcon)) {
                mGiftView.setGiftLoadUrl(giftIcon);
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

        try {
            if (message.getFrom().equals("admin")) {
                mUhead.setBackgroundResource(R.drawable.yue_jian_app_ic_launcher);
            } else {
                mUhead.setAvatarUrl(message.getStringAttribute("uhead"));
            }

        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        handleTextMessage();
    }

    private void handleTextMessage() {
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
    protected void onBubbleClick() {
    }
}
