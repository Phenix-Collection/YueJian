package com.mingquan.yuejian.hyphenate;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.vchat.YueJianAppMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ConversationFragment extends HyphenateBaseFragment implements ConversationView {

    public static final String TAG = "ConversationFragment";
    @BindView(R.id.rv_private_chat)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_empty_private_chat)
    LinearLayout llEmptyPrivateChat;
    Unbinder unbinder;
    FragmentActivity mContext;
    private ConversationAdapter mConversationAdapter;
    private ConversationPresenter mConversationPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.yue_jian_app_fragment_vchat_conversation_list;
    }

    @Override
    protected void init() {
        super.init();
        mContext = getActivity();
        mConversationPresenter = new ConversationPresenterImpl(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mConversationAdapter = new ConversationAdapter(mContext, mConversationPresenter.getConversations());
        mRecyclerView.setAdapter(mConversationAdapter);
        mConversationPresenter.loadAllConversations();
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onAllConversationsLoaded(boolean isEmpty) {
        llEmptyPrivateChat.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        mConversationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        mConversationAdapter.notifyDataSetChanged();
    }

    private EMMessageListenerAdapter mEMMessageListenerAdapter = new EMMessageListenerAdapter() {

        @Override
        public void onMessageReceived(List<EMMessage> list) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast(getString(R.string.receive_new_message));
                    mConversationPresenter.loadAllConversations();
                    if (mRecyclerView != null) {
                        mRecyclerView.scrollToPosition(0);
                    }
                }
            });
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(YueJianAppMessageEvent event) {
        if (event.getMessage().equals(YueJianAppAppConfig.ACTION_PRIVATE_MESSAGE)) {
            mConversationPresenter.loadAllConversations();
            if (mRecyclerView != null) {
                mRecyclerView.scrollToPosition(0);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListenerAdapter);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
