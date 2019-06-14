package com.mingquan.yuejian.vchat;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppConst;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.FastReplyAdapter;
import com.mingquan.yuejian.adapter.YueJianAppMessageAdapter;
import com.mingquan.yuejian.interf.YueJianAppINomalDialog;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACFastReplyModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPrivateInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.ui.dialog.YueJianAppEditableActionSheetDialog;
import com.mingquan.yuejian.ui.other.YueJianAppPhoneLivePrivateChat;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppInputMethodUtils;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.utils.YueJianAppUtils;
import com.mingquan.yuejian.widget.YueJianAppKBPanelConflictLayout;
import com.mingquan.yuejian.widget.YueJianAppKBRootConflictLayout;

import org.greenrobot.eventbus.EventBus;

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

public class YueJianAppVChatMessageDetailFragment extends Fragment implements View.OnClickListener {
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
    @BindView(R.id.lv_short_message)
    ListView mLvShortMessage;
    @BindView(R.id.kb_panel)
    YueJianAppKBPanelConflictLayout mPanel;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;
    private YueJianAppKBRootConflictLayout mRoot;
    private Unbinder unbinder;

    FragmentActivity mContext;
    private List<EMMessage> mChats = new ArrayList<>();
    private YueJianAppACUserPublicInfoModel mToUser;
    private YueJianAppMessageAdapter mMessageAdapter;
    private YueJianAppACUserPublicInfoModel mUser;
    private boolean canSend = true; // 防止网络差的时候重复发送消息，默认是true，点击发送按钮后为false
    private BroadcastReceiver broadcastReceiver;
    private int realKeyboardHeight;
    private ArrayList<YueJianAppACFastReplyModel> mReplies; // 快速回复短语列表
    private FastReplyAdapter mFastReplyAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = (YueJianAppKBRootConflictLayout) inflater.inflate(R.layout.yue_jian_app_fragment_vchat_message_detail, container, false);
        unbinder = ButterKnife.bind(this, mRoot);
        mContext = getActivity();

        mRoot.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        mRoot.setKBPanelConflictLayout(mPanel);
        mUser = YueJianAppAppContext.getInstance().getAcPublicUser();
        mToUser = (YueJianAppACUserPublicInfoModel) mContext.getIntent().getSerializableExtra("user");
        registerReceiver();
        initView();
        initData();
        return mRoot;
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        private int rootViewVisibleHeight;//纪录根视图的显示高度

        @Override
        public void onGlobalLayout() {
            //获取当前根视图在屏幕上显示的大小
            Rect r = new Rect();
            mRoot.getWindowVisibleDisplayFrame(r);
            int visibleHeight = r.height();
            if (rootViewVisibleHeight == 0) {
                rootViewVisibleHeight = visibleHeight;
                return;
            }

            //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
            if (rootViewVisibleHeight == visibleHeight) {
                return;
            }
            //根视图显示高度变小超过200，可以看作软键盘显示了
            if (rootViewVisibleHeight - visibleHeight > 200) {
                realKeyboardHeight = rootViewVisibleHeight - visibleHeight;
                mLvShortMessage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, realKeyboardHeight));
                rootViewVisibleHeight = visibleHeight;
                mRoot.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
                return;
            }

            //根视图显示高度变大超过200，可以看作软键盘隐藏了
            if (visibleHeight - rootViewVisibleHeight > 200) {
                rootViewVisibleHeight = visibleHeight;
            }
        }
    };

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(YueJianAppAppConfig.ACTION_VIP_CHAT_PING);
        intentFilter.addAction(YueJianAppAppConfig.ACTION_VIP_CHAT_CONNECT);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(YueJianAppAppConfig.ACTION_VIP_CHAT_PING)) {
                    YueJianAppUIHelper.showVideoChatActivity(mContext, mToUser, "");
                } else if (intent.getAction().equals(YueJianAppAppConfig.ACTION_VIP_CHAT_CONNECT)) {
                    YueJianAppUIHelper.showVideoChatActivity(mContext, mToUser, "");
                }
            }
        };
        mContext.registerReceiver(broadcastReceiver, intentFilter);
    }

    public void initView() {
        boolean isCustomer = mToUser.getUid().equals("120001");
        String hint = isCustomer ? "欢迎咨询官方客服" : String.format("发送一条消息收费%s钻石", YueJianAppAppContext.getInstance().mChargingChatFee);
        // 主播不显示消费提示语
        if (YueJianAppAppContext.getInstance().getPrivateInfoModel() != null &&
                YueJianAppAppContext.getInstance().getPrivateInfoModel().getAuthStatus() != YueJianAppApiProtoHelper.AUTH_STATUS_CERTIFIED) {
            mMessageInput.setHint(hint);
        }

        mTitle.setText(mToUser.getName());
        flVideo.setVisibility(YueJianAppAppContext.getInstance().getCanVideoChat() ? View.VISIBLE : View.GONE);
    }

    public void initData() {
        EMClient.getInstance().chatManager().addMessageListener(msgListener); // 添加消息监听
        getMessageRecord();// 获取消息记录

        mMessageAdapter = new YueJianAppMessageAdapter(mContext);
        mMessageAdapter.setChatList(mChats);
        mChatMessageListView.setAdapter(mMessageAdapter);
        mChatMessageListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mChatMessageListView.setSelection(mChats.size());
        if (mChats.size() > 0) {
            mChatMessageListView.setSelection(mChats.size() - 1);
        }
        mChatMessageListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mPanel.getVisibility() == View.VISIBLE) {
                    mPanel.hide();
                } else {
                    YueJianAppInputMethodUtils.closeKeyBoard(mContext, mMessageInput);
                }
                return false;
            }
        });

        mReplies = new ArrayList<>();
        mFastReplyAdapter = new FastReplyAdapter(mContext);
        mLvShortMessage.setAdapter(mFastReplyAdapter);
        updateReplyList();

        mLvShortMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mReplies.size()) { // 添加item
                    YueJianAppDialogHelp.showEditReplyItemDialog(mContext, new YueJianAppINomalDialog() {
                        @Override
                        public void cancelDialog(View v, Dialog d) {
                            d.dismiss();
                            if (mPanel.getVisibility() == View.GONE) {
                                mMessageInput.requestFocus();
                                mRoot.setIsTouchClose(true);
                                YueJianAppInputMethodUtils.closeKeyBoard(mContext, mMessageInput);
                            }
                        }

                        @Override
                        public void determineDialog(View v, Dialog d) {
                            d.dismiss();
                            String item = ((EditText) v).getText().toString().trim();
                            addReplyItem(item);
                            if (mPanel.getVisibility() == View.GONE) {
//                                mPanel.show();
                                mMessageInput.requestFocus();
                                mRoot.setIsTouchClose(true);
                                YueJianAppInputMethodUtils.closeKeyBoard(mContext, mMessageInput);
                            }
                        }
                    });
                } else { // 显示在编辑框
                    TextView tvItemDetail = view.findViewById(R.id.tv_reply_item_detail);
                    String itemDetail = tvItemDetail.getText().toString().trim();
                    if (YueJianAppStringUtil.isEmpty(itemDetail)) {
                        return;
                    }
                    mMessageInput.setText(itemDetail);
                    mMessageInput.setSelection(mMessageInput.getText().toString().length());
                }
            }
        });

        mLvShortMessage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position != mReplies.size()) {
                    YueJianAppDialogHelp.showDialog(mContext, "确定删除此条短语吗？", new YueJianAppINomalDialog() {
                        @Override
                        public void cancelDialog(View v, Dialog d) {
                            d.dismiss();
                        }

                        @Override
                        public void determineDialog(View v, Dialog d) {
                            deleteReplyItem(mReplies.get(position).getReplyId());
                            d.dismiss();
                        }
                    });
                }
                return true;
            }
        });
    }

    /**
     * 获取消息记录
     */
    private void getMessageRecord() {
        String target_uid = mToUser.getUid();
        target_uid = YueJianAppAppConfig.IM_ACCOUNT + target_uid;
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(target_uid, null, true);
        conversation.markAllMessagesAsRead();
        int count = conversation.getAllMessages().size();
        if (count < conversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = conversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            conversation.loadMoreMsgFromDB(msgId, 20 - count);
        }
        mChats = conversation.getAllMessages();
    }

    private void updateReplyList() {
        YueJianAppApiProtoHelper.sendACFastReplyListReq(
                mContext,
                mUser.getUid(),
                YueJianAppAppContext.getInstance().getToken(),
                new YueJianAppApiProtoHelper.ACFastReplyListReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACFastReplyModel> replies) {
                        mReplies.clear();
                        mReplies.addAll(replies);
                        mFastReplyAdapter.setReplyList(replies);
                    }
                });
    }

    private void addReplyItem(String item) {
        YueJianAppApiProtoHelper.sendACAddFastReplyReq(
                mContext,
                mUser.getUid(),
                YueJianAppAppContext.getInstance().getToken(),
                item,
                new YueJianAppApiProtoHelper.ACAddFastReplyReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppAppContext.showToastAppMsg(mContext, errMessage);
                    }

                    @Override
                    public void onResponse(int replyId) {
                        YueJianAppTLog.info("添加reply id:%s", replyId);
                        updateReplyList();
                    }
                }
        );
    }

    private void deleteReplyItem(int replyId) {
        YueJianAppApiProtoHelper.sendACDeleteFastReplyReq(
                mContext,
                mUser.getUid(),
                YueJianAppAppContext.getInstance().getToken(),
                replyId,
                new YueJianAppApiProtoHelper.ACDeleteFastReplyReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppAppContext.showToastAppMsg(mContext, errMessage);
                    }

                    @Override
                    public void onResponse() {
                        YueJianAppAppContext.showToastAppMsg(mContext, "短语删除成功！");
                        updateReplyList();
                    }
                }
        );
    }

    @OnClick({R.id.tv_send_chat,
            R.id.et_private_chat_message,
            R.id.iv_private_chat_back,
            R.id.fl_video,
            R.id.fl_camera,
            R.id.fl_picture,
            R.id.fl_short_message})
    public void onClick(View v) {
        if (YueJianAppUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_send_chat: // 发送消息按钮
                sendChatMsg();
                break;
            case R.id.et_private_chat_message:

                break;
            case R.id.iv_private_chat_back: // 返回键
                mContext.onBackPressed();
                break;
            case R.id.fl_video: // 视频聊天
                YueJianAppUIHelper.showVideoChatActivity(mContext, mToUser, "");
                break;
            case R.id.fl_camera: // 拍照
                openCamera();
                break;
            case R.id.fl_picture: // 相册
                openGallery();
                break;
            case R.id.fl_short_message: // 短语
                // 短语面板隐藏并且软键盘隐藏时 显示面板
                if (mPanel.getVisibility() == View.GONE && !YueJianAppInputMethodUtils.isShowKeyboard(mContext, mMessageInput)) {
                    mPanel.show();
                    return;
                }

                if (mPanel.getVisibility() == View.GONE) {
                    mRoot.setIsTouchClose(true);
                    YueJianAppInputMethodUtils.closeKeyBoard(mContext, mMessageInput);
                } else {
                    YueJianAppInputMethodUtils.openKeyBoard(mContext, mMessageInput);
                }

                break;
        }
    }

    /**
     * 打开相册
     */
    private void openGallery() {
        PictureSelector.create(YueJianAppVChatMessageDetailFragment.this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .maxSelectNum(1)
                .isCamera(false)
                .previewImage(true)// 是否可预览图片
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .rotateEnabled(false) // 裁剪是否可旋转图片
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 打开摄像头
     */
    private void openCamera() {
        PictureSelector.create(YueJianAppVChatMessageDetailFragment.this)
                .openCamera(PictureMimeType.ofImage())
                .previewImage(true)// 是否可预览图片
                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .rotateEnabled(false) // 裁剪是否可旋转图片
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        List<LocalMedia> images;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    images = PictureSelector.obtainMultipleResult(data);
                    sendImageMsg(images.get(0).getPath());
                    break;
            }
        }
    }

    /**
     * 发送图片消息
     */
    private void sendImageMsg(final String imagePath) {
        if (!canSend) {
            return;
        }
        canSend = false;
        YueJianAppApiProtoHelper.sendACChargingChatReq(
                mContext,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                mToUser.getUid(),
                new YueJianAppApiProtoHelper.ACChargingChatReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        canSend = true;
                        if (errCode == YueJianAppApiProtoHelper.ERR_NO_DIAMOND) {
                            YueJianAppDialogHelp.showDialog(mContext, "您的钻石数不足以发送消息，是否立即充值？", new YueJianAppINomalDialog() {
                                @Override
                                public void cancelDialog(View v, Dialog d) {
                                    d.dismiss();
                                }

                                @Override
                                public void determineDialog(View v, Dialog d) {
                                    YueJianAppDialogHelper.showRechargeDialogFragment(getActivity().getSupportFragmentManager());
                                    d.dismiss();
                                }
                            });
                        } else {
                            YueJianAppAppContext.showToastAppMsg(mContext, errMessage);
                        }
                    }

                    @Override
                    public void onResponse(int diamond) {
                        canSend = true;
                        YueJianAppTLog.info("剩余钻石" + diamond);
                        YueJianAppACUserPrivateInfoModel privateInfoModel = YueJianAppAppContext.getInstance().getPrivateInfoModel();
                        privateInfoModel.setDiamond(diamond);
                        YueJianAppAppContext.getInstance().setPrivateInfo(privateInfoModel);
                        EMMessage message = YueJianAppPhoneLivePrivateChat.sendImageMessage(imagePath, mToUser, mUser);
                        updateChatList(message);
                    }
                });
    }

    private void updateChatList(EMMessage message) {
        if (null == mMessageAdapter || null == mChatMessageListView)
            return;
        mMessageAdapter.addMessage(message);
        mMessageAdapter.notifyDataSetChanged();
        mChatMessageListView.setSelection(mMessageAdapter.getCount());
        if (!message.getFrom().equals(YueJianAppAppConfig.IM_ACCOUNT + mUser.getUid())) {
            EMClient.getInstance().chatManager().getConversation(message.getFrom()).markAllMessagesAsRead();
        }
    }

    /**
     * 发送文本消息
     */
    private void sendChatMsg() {
        String txt = mMessageInput.getText().toString();
        if (YueJianAppStringUtil.isEmpty(txt)) {
            YueJianAppAppContext.showToastAppMsg(mContext, "发送内容不能为空！！！");
            return;
        }

        final String finalTxt = matchKey(txt);

        if (!canSend) {
            return;
        }

        canSend = false;
        // 发一条消息支付5钻石

        YueJianAppApiProtoHelper.sendACChargingChatReq(
                mContext,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                mToUser.getUid(),
                new YueJianAppApiProtoHelper.ACChargingChatReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        canSend = true;
                        if (errCode == YueJianAppApiProtoHelper.ERR_NO_DIAMOND) {
                            YueJianAppDialogHelp.showDialog(mContext, "您的钻石数不足以发送消息，是否立即充值？", new YueJianAppINomalDialog() {
                                @Override
                                public void cancelDialog(View v, Dialog d) {
                                    d.dismiss();
                                }

                                @Override
                                public void determineDialog(View v, Dialog d) {
                                    YueJianAppDialogHelper.showRechargeDialogFragment(getActivity().getSupportFragmentManager());
                                    d.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onResponse(int diamond) {
                        canSend = true;
                        YueJianAppTLog.info("剩余钻石" + diamond);
                        YueJianAppACUserPrivateInfoModel privateInfoModel = YueJianAppAppContext.getInstance().getPrivateInfoModel();
                        privateInfoModel.setDiamond(diamond);
                        YueJianAppAppContext.getInstance().setPrivateInfo(privateInfoModel);
                        updateChatList(YueJianAppPhoneLivePrivateChat.sendChatMessage(finalTxt, mToUser, mUser));
                        mMessageInput.setText("");
                    }
                });
    }

    /**
     * 匹配不文明关键字
     *
     * @param sendMsg
     * @return
     */
    private String matchKey(String sendMsg) {
        SpannableStringBuilder builder = new SpannableStringBuilder(sendMsg);
        int index = 0;
        while (index < sendMsg.length() - 1) {
            String word = sendMsg.substring(index, index + 1);
            if (YueJianAppAppConst.mKeyWords.containsKey(word)) {
                ArrayList<String> mKeyList = YueJianAppAppConst.mKeyWords.get(word);
                builder = replaceValue(mKeyList, builder);
                sendMsg = builder.toString(); //同时替换sendMsg，减少匹配次数
            }
            index++;
        }
        return builder.toString();
    }

    /**
     * 将关键字替换为"*"
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

    /**
     * 弹出删除、复制菜单
     */
    private void showDeleteCopyMenu(final int position, final TextView tvMessage) {
        final YueJianAppEditableActionSheetDialog mDialog = new YueJianAppEditableActionSheetDialog(mContext).builder();
        TextView mDeleteTextView = new TextView(mContext);
        TextView mCopyTextView = new TextView(mContext);
        mDialog.addSheetItem(mDeleteTextView, "删除", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        YueJianAppAppContext.showToastAppMsg(mContext, "删除成功");
                        EMMessage e = mChats.get(position);
                        YueJianAppPhoneLivePrivateChat.deleteItemMessage(e, YueJianAppAppConfig.IM_ACCOUNT + mToUser.getUid());

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
                            YueJianAppAppContext.showToastAppMsg(mContext, "复制成功");
                            mDialog.cancel();
                        }
                    });
        }
        mDialog.show();
    }

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (final EMMessage message : messages) {
                        if (message.getFrom().equals(YueJianAppAppConfig.IM_ACCOUNT + mToUser.getUid())) {
                            updateChatList(message);
                        }
                    }
                }
            });
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {
        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != msgListener) {
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        }
        mContext.unregisterReceiver(broadcastReceiver);
        EventBus.getDefault().post(new YueJianAppMessageEvent(YueJianAppAppConfig.ACTION_PRIVATE_MESSAGE)); // 更新底部导航栏小红点状态
        EventBus.getDefault().post(new YueJianAppMessageEvent("REFRESH_CONVERSATION_LIST"));
        unbinder.unbind();
    }
}
