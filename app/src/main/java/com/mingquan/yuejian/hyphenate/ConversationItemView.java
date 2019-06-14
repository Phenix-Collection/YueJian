package com.mingquan.yuejian.hyphenate;

import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppEditableActionSheetDialog;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.vchat.YueJianAppMessageEvent;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationItemView extends RelativeLayout {
    public static final String TAG = "ConversationItemView";
    @BindView(R.id.av_userHead)
    YueJianAppAvatarView mAVAvatar;
    @BindView(R.id.iv_icon_vip)
    ImageView ivIconVip;
    @BindView(R.id.iv_unread_dot)
    TextView mUnreadCount;
    @BindView(R.id.tv_time)
    TextView mTimestamp;
    @BindView(R.id.tv_item_uname)
    TextView mUserName;
    @BindView(R.id.tv_item_last_msg)
    TextView mLastMessage;
    @BindView(R.id.conversation_item_container)
    RelativeLayout mConversationItemContainer;
    private FragmentActivity mContext;

    public ConversationItemView(FragmentActivity context) {
        this(context, null);
    }

    public ConversationItemView(FragmentActivity context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.yue_jian_app_item_private_chat, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(final EMConversation emConversation) {
        updateLastMessage(emConversation);
        updateUnreadCount(emConversation);
        YueJianAppApiProtoHelper.sendACGetUserPublicInfoReq(null,
                emConversation.conversationId().replace(YueJianAppAppConfig.IM_ACCOUNT, ""),
                new YueJianAppApiProtoHelper.ACGetUserPublicInfoReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse(final YueJianAppACUserPublicInfoModel user) {
                        mUserName.setText(user.getName());
                        mAVAvatar.setAvatarUrl(user.getAvatarUrl());
                        ivIconVip.setVisibility(user.getVipTime() > System.currentTimeMillis() / 1000 ?
                                View.VISIBLE : View.GONE);
                        mConversationItemContainer.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                YueJianAppUIHelper.showPrivateChatdetailFragment(mContext, user);
                            }
                        });
                        mConversationItemContainer.setOnLongClickListener(new OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                deleteConversation(emConversation);
                                return false;
                            }
                        });
                    }
                }
        );
    }

    private void updateLastMessage(EMConversation emConversation) {
        EMMessage emMessage = emConversation.getLastMessage();
        if (emMessage.getBody() instanceof EMTextMessageBody) {
            mLastMessage.setText(((EMTextMessageBody) emMessage.getBody()).getMessage());
        } else {
            mLastMessage.setText(getContext().getString(R.string.last_message_image));
        }
        mTimestamp.setText(DateUtils.getTimestampString(new Date(emMessage.getMsgTime())));
    }

    private void updateUnreadCount(EMConversation emConversation) {
        int unreadMsgCount = emConversation.getUnreadMsgCount();
        if (unreadMsgCount > 0) {
            mUnreadCount.setVisibility(VISIBLE);
            mUnreadCount.setText(String.valueOf(unreadMsgCount));
        } else {
            mUnreadCount.setVisibility(GONE);
        }
    }

    /**
     * 删除消息
     *
     */
    private void deleteConversation(final EMConversation emConversation) {
        final YueJianAppEditableActionSheetDialog mDialog = new YueJianAppEditableActionSheetDialog(mContext).builder();
        TextView mDeleteTextView = new TextView(mContext);
        mDialog.addSheetItem(mDeleteTextView, "删除", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        YueJianAppAppContext.showToastAppMsg(mContext, "删除成功");
                        EMClient.getInstance().chatManager().deleteConversation(emConversation.conversationId(), true);
                        EventBus.getDefault().post(new YueJianAppMessageEvent(YueJianAppAppConfig.ACTION_PRIVATE_MESSAGE)); // 更新底部导航栏小红点状态
                        mDialog.cancel();
                    }
                });
        mDialog.show();
    }
}
