package com.mingquan.yuejian.hyphenate;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ConversationPresenterImpl implements ConversationPresenter {

    private ConversationView mConversationView;

    private List<EMConversation> mEMConversations;

    public ConversationPresenterImpl(ConversationView conversationView) {
        mConversationView = conversationView;
        mEMConversations = new ArrayList<>();
    }

    @Override
    public void loadAllConversations() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                final Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                mEMConversations.clear();
                mEMConversations.addAll(conversations.values());
                Collections.sort(mEMConversations, new Comparator<EMConversation>() {
                    @Override
                    public int compare(EMConversation o1, EMConversation o2) {
                        if (o2.getLastMessage() == null || o2.getLastMessage() == null) {
                            return 0;
                        } else {
                            return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
                        }
                    }
                });
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConversationView.onAllConversationsLoaded(conversations.size() == 0);
                    }
                });
            }
        });
    }

    @Override
    public List<EMConversation> getConversations() {
        return mEMConversations;
    }
}
