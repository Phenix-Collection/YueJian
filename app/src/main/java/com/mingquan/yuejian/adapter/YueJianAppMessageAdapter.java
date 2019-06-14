package com.mingquan.yuejian.adapter;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.ui.view.YueJianAppChatImageView;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.vchat.YueJianAppLargerImageDialogFragment;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;
import com.mingquan.yuejian.widget.YueJianAppPhoneLiveChatRow;
import com.mingquan.yuejian.widget.YueJianAppPhoneLiveChatRowImage;
import com.mingquan.yuejian.widget.YueJianAppPhoneLiveChatRowText;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 私信详情列表适配器
 */
public class YueJianAppMessageAdapter extends BaseAdapter {
    private List<EMMessage> mChats = new ArrayList<>();
    private LayoutInflater inflate;
    private FragmentActivity context;

    public YueJianAppMessageAdapter(FragmentActivity context) {
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    public void setChatList(List<EMMessage> mChats) {
        this.mChats = mChats;
        this.notifyDataSetChanged();
    }

    public void addMessage(EMMessage emMessage) {
        this.mChats.add(emMessage);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mChats.size();
    }

    @Override
    public Object getItem(int position) {
        return mChats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EMMessage e = mChats.get(position);
        YueJianAppPhoneLiveChatRow view = null;
        switch (e.getType()) {
            case TXT: //文本或礼物
                view = new YueJianAppPhoneLiveChatRowText(context, e, position, this);
                break;
            case IMAGE: //图片
                view = new YueJianAppPhoneLiveChatRowImage(context, e, position, this);
                break;
        }
        YueJianAppAvatarView mUhead = (YueJianAppAvatarView) view.findViewById(R.id.av_message_head);
        YueJianAppChatImageView mImage = (YueJianAppChatImageView) view.findViewById(R.id.iv_message_image);
        //点击头像进入用户详情页
        final String uid = e.getFrom().replace(YueJianAppAppConfig.IM_ACCOUNT, "");
        mUhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("120001".equals(uid)) {
                    YueJianAppTLog.info("系统消息");
                    return;
                }
                YueJianAppDialogHelper.showVchatOtherInfoDialogFragment(context.getSupportFragmentManager(), uid);
            }
        });

        if (mImage != null) {
            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EMImageMessageBody emImageMessageBody = (EMImageMessageBody) e.getBody();
                    YueJianAppLargerImageDialogFragment largerImage = new YueJianAppLargerImageDialogFragment();
                    Bundle bundle = new Bundle();
                    if (e.direct() == EMMessage.Direct.SEND) {
                        bundle.putString("imagePath", emImageMessageBody.getRemoteUrl());
                    } else {
                        bundle.putString("imagePath", emImageMessageBody.getThumbnailUrl());
                    }
                    largerImage.setArguments(bundle);
                    largerImage.setStyle(
                            DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
                    largerImage.show(context.getSupportFragmentManager(), "YueJianAppLargerImageDialogFragment");
                }
            });
        }

        //缓存的view的message很可能不是当前item的，传入当前message和position更新ui
        view.setUpView(e, position);
        return view;
    }
}
