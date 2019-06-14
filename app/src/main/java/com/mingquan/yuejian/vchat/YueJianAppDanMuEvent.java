package com.mingquan.yuejian.vchat;

import com.mingquan.yuejian.proto.model.YueJianAppACChatModel;

public class YueJianAppDanMuEvent {
    private YueJianAppACChatModel chatModel;

    public YueJianAppDanMuEvent(YueJianAppACChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public YueJianAppACChatModel getChatModel() {
        return chatModel;
    }

    public void setChatModel(YueJianAppACChatModel chatModel) {
        this.chatModel = chatModel;
    }
}