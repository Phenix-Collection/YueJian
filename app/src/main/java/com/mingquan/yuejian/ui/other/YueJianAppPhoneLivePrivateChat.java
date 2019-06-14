package com.mingquan.yuejian.ui.other;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;

public class YueJianAppPhoneLivePrivateChat {
    public static EMMessage sendChatMessage(
            String content, YueJianAppACUserPublicInfoModel toUser, YueJianAppACUserPublicInfoModel mUser) {
        //创建一条文本消息,content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        if (toUser == null || Integer.parseInt(toUser.getUid()) <= 0) {
            return null;
        }
        EMMessage message = null;
        try {
            message = EMMessage.createTxtSendMessage(content, YueJianAppAppConfig.IM_ACCOUNT + toUser.getUid());
            message.setFrom(YueJianAppAppConfig.IM_ACCOUNT + mUser.getUid());
            message.setAttribute("uhead", mUser.getAvatarUrl());
            message.setAttribute("em_force_notification", true);
            message.setAttribute("uname", mUser.getName());
            //发送消息
            EMClient.getInstance().chatManager().sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 发送图片
     *
     * @param imagePath 图片本地路径
     */
    public static EMMessage sendImageMessage(String imagePath, YueJianAppACUserPublicInfoModel toUser, YueJianAppACUserPublicInfoModel mUser) {
        //创建一条文本消息,content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        if (toUser == null || Integer.parseInt(toUser.getUid()) <= 0) {
            return null;
        }

        // imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, YueJianAppAppConfig.IM_ACCOUNT + toUser.getUid());
        message.setFrom(YueJianAppAppConfig.IM_ACCOUNT + mUser.getUid());
        message.setAttribute("uhead", mUser.getAvatarUrl());
        message.setAttribute("uname", mUser.getName());
        message.setAttribute("em_force_notification", true);
        EMClient.getInstance().chatManager().sendMessage(message);
        return message;
    }

    /**
     * 发礼物
     *
     * @param content
     * @param toUser
     * @param mUser
     * @return
     */
    public static EMMessage sendChatGiftMessage(
            String content, YueJianAppACUserPublicInfoModel toUser, YueJianAppACUserPublicInfoModel mUser, String giftId, String giftIcon) {
        //创建一条文本消息,content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        if (toUser == null || toUser.getUid() == null) {
            return null;
        }

        EMMessage message = null;
        try {
            message = EMMessage.createTxtSendMessage(content, YueJianAppAppConfig.IM_ACCOUNT + toUser.getUid());
            message.setFrom(YueJianAppAppConfig.IM_ACCOUNT + mUser.getUid());
            message.setAttribute("uhead", mUser.getAvatarUrl());
            message.setAttribute("uname", mUser.getName());
            message.setAttribute("giftId", giftId);
            message.setAttribute("giftIcon", giftIcon);

            //发送消息
            EMClient.getInstance().chatManager().sendMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    /**
     * 私信发礼物
     *
     * @param content    描述内容
     * @param toUid      接收者uid
     * @param mUid       发送者uid
     * @param mAvatarUrl 发送者头像url
     * @param mName      发送者名字
     * @param giftIcon   礼物图标url
     * @return
     */
    public static EMMessage sendChatGiftMessage(String content, String toUid, String mUid, String mAvatarUrl, String mName, String giftIcon) {
        EMMessage message = null;
        try {
            message = EMMessage.createTxtSendMessage(content, YueJianAppAppConfig.IM_ACCOUNT + toUid);
            message.setFrom(YueJianAppAppConfig.IM_ACCOUNT + mUid);
            message.setAttribute("uhead", mAvatarUrl);
            message.setAttribute("uname", mName);
            message.setAttribute("giftIcon", giftIcon);
            //发送消息
            EMClient.getInstance().chatManager().sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    /**
     * 删除会话
     * true 不保留聊天记录 false保留记录
     */
    public static void deleteMessage(String username) {
        EMClient.getInstance().chatManager().deleteConversation(username, true);
    }

    /**
     * 删除当前会话的某条聊天记录
     *
     * @param e
     * @param username
     */
    public static void deleteItemMessage(EMMessage e, String username) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        conversation.removeMessage(e.getMsgId());
    }
}
