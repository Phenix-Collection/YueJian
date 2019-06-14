package com.mingquan.yuejian.hyphenate;

import com.hyphenate.chat.EMConversation;

import java.util.List;

public interface ConversationPresenter {

    void loadAllConversations();

    List<EMConversation> getConversations();
}
