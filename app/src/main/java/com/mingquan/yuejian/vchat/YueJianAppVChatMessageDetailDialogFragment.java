package com.mingquan.yuejian.vchat;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppConst;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppMessageAdapter;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppEditableActionSheetDialog;
import com.mingquan.yuejian.ui.other.YueJianAppPhoneLivePrivateChat;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by sun on 2018/9/3
 * <p>
 * 私信详情页面
 */

public class YueJianAppVChatMessageDetailDialogFragment extends YueJianAppBaseDialogFragment implements View.OnClickListener {
    @BindView(R.id.tv_private_chat_title)
    TextView mTitle;
    @BindView(R.id.et_private_chat_message)
    EditText mMessageInput;
    @BindView(R.id.lv_message)
    ListView mChatMessageListView;
    @BindView(R.id.linear_bottom)
    LinearLayout mBottomSend;
    @BindView(R.id.tv_send_chat)
    TextView mTvSendChat;

    FragmentActivity mContext;
    private List<EMMessage> mChats = new ArrayList<>();
    private YueJianAppMessageAdapter mMessageAdapter;
    private YueJianAppACUserPublicInfoModel mUser;
    private YueJianAppACUserPublicInfoModel mToUser;

    private Unbinder unbinder;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_vchat_message_detail, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        mUser = YueJianAppAppContext.getInstance().getAcPublicUser();
        mToUser = (YueJianAppACUserPublicInfoModel) getArguments().getSerializable("TARGET_USER");
        initView();
        initData();
        return view;
    }

    public void initView() {
        if (!YueJianAppStringUtil.isNumeric(mToUser.getUid())) {
            mTitle.setText("系统消息");
            mBottomSend.setVisibility(View.GONE);
        } else {
            mTitle.setText(mToUser.getName());
            mBottomSend.setVisibility(View.VISIBLE);
        }
    }

    public void initData() {
        EMClient.getInstance().chatManager().addMessageListener(msgListener); // 添加消息监听
        getMessageRecord();// 获取消息记录

        mMessageAdapter = new YueJianAppMessageAdapter(mContext);
        mChatMessageListView.setAdapter(mMessageAdapter);
        mMessageAdapter.setChatList(mChats);
        if (mChats.size() > 0) {
            mChatMessageListView.setSelection(mChats.size() - 1);
        }

//        //长按删除或复制信息
//        mChatMessageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView mTvMessage = (TextView) view.findViewById(R.id.tv_message_text);
//                bottomMenu(position, mTvMessage);
//                return true;
//            }
//        });
    }

    @OnClick({R.id.tv_send_chat, R.id.et_private_chat_message, R.id.iv_private_chat_back})
    @Override
    public void onClick(View v) {
        if (YueJianAppUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_send_chat: // 发送消息按钮
                preSendMsg();
                break;
            case R.id.et_private_chat_message:

                break;
            case R.id.iv_private_chat_back: // 返回键
                dismiss();
                break;
        }
    }

    /**
     * 发送前的操作
     */
    private void preSendMsg() {
        String preSendMsg = mMessageInput.getText().toString();
        if (YueJianAppStringUtil.isEmpty(preSendMsg))
            return;
        SpannableStringBuilder builder = matchKey(preSendMsg);
        sendChatMsg(builder.toString());
    }

    private SpannableStringBuilder matchKey(String sendMsg) {
        SpannableStringBuilder builder = new SpannableStringBuilder(sendMsg);
        int index = 0;
        while (index < sendMsg.length() - 1) {
            String word = sendMsg.substring(index, index + 1);
            if (YueJianAppAppConst.mKeyWords.containsKey(word)) {
                ArrayList<String> mKeyList = YueJianAppAppConst.mKeyWords.get(word);
                builder = replaceValue(mKeyList, builder);
                sendMsg = builder.toString(); //同时替换sengMsg，减少匹配次数
            }
            index++;
        }
        return builder;
    }

    /**
     * 将关键字替换为心图标
     */
    private SpannableStringBuilder replaceValue(
            ArrayList<String> mKeyList, SpannableStringBuilder builder) {
        String sendMsg = builder.toString();
        for (int i = 0; i < mKeyList.size(); i++) {
            if (sendMsg.contains(mKeyList.get(i))) {
                int index = sendMsg.indexOf(mKeyList.get(i));
                builder.replace(index, index + mKeyList.get(i).length(), "*");
                break;
            }
        }
        return builder;
    }

    public void sendChatMsg(String msg) {
        updateChatList(YueJianAppPhoneLivePrivateChat.sendChatMessage(msg, mToUser, mUser));
        mMessageInput.setText("");
    }

    /**
     * 弹出删除/复制菜单
     */
    private void bottomMenu(final int position, final TextView tvMessage) {
        final YueJianAppEditableActionSheetDialog mDialog = new YueJianAppEditableActionSheetDialog(mContext).builder();
        TextView mDeleteTextView = new TextView(mContext);
        TextView mCopyTextView = new TextView(mContext);
        mDeleteTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mCopyTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mDialog.addSheetItem(mDeleteTextView, "删除", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        Toast.makeText(getContext().getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        EMMessage e = mChats.get(position);
                        String uid = mToUser.getUid();
                        if (YueJianAppStringUtil.isNumeric(uid)) {
                            uid += YueJianAppAppConfig.IM_ACCOUNT + uid;
                        }
                        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(uid);
                        conversation.removeMessage(e.getMsgId());

                        mChats.remove(position);
                        mMessageAdapter.notifyDataSetChanged();
                        mDialog.cancel();
                    }
                });
        if (tvMessage != null) {
            mDialog.addSheetItem(mCopyTextView, "复制", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                    new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            ClipboardManager cmb =
                                    (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            cmb.setText(tvMessage.getText());
                            Toast.makeText(getContext().getApplicationContext(), "复制成功", Toast.LENGTH_SHORT).show();
                            mDialog.cancel();
                        }
                    });
        }
        mDialog.show();
    }

    /**
     * 获取消息记录
     */
    private void getMessageRecord() {
        String target_uid = mToUser.getUid();
        if (YueJianAppStringUtil.isNumeric(target_uid)) {
            target_uid = YueJianAppAppConfig.IM_ACCOUNT + target_uid;
        }
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(target_uid);
        if (conversation != null) {
            mChats = conversation.getAllMessages();
        }
    }

    private void updateChatList(EMMessage message) {
        if (null == mMessageAdapter || null == mChatMessageListView)
            return;
        mMessageAdapter.addMessage(message);
//        mChatMessageListView.setAdapter(mMessageAdapter);
        mChatMessageListView.setSelection(mMessageAdapter.getCount() - 1);
    }

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            YueJianAppTLog.info("onMessageReceived");
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (final EMMessage message : messages) {
                        updateChatList(message);
                    }
                }
            });
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            YueJianAppTLog.info("onCmdMessageReceived");
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {
            YueJianAppTLog.info("onMessageRead");
        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {
            YueJianAppTLog.info("onMessageDeliveryed");
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            YueJianAppTLog.info("onMessageChanged");
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
