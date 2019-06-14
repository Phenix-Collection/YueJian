package com.mingquan.yuejian.em;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.fragment.YueJianAppBroadcastFragment;
import com.mingquan.yuejian.fragment.YueJianAppShortVideoFragment;
import com.mingquan.yuejian.hyphenate.ConversationFragment;
import com.mingquan.yuejian.vchat.YueJianAppVChatHomePageFragment;

/**
 * Created by Administrator on 2016/3/9.
 */
public enum YueJianAppMainTab {
    INDEX(0, R.drawable.yue_jian_app_tab_home, 0, YueJianAppBroadcastFragment.class),
    LIKE(1, R.drawable.yue_jian_app_tab_like, 1, YueJianAppShortVideoFragment.class),
    //  MESSAGE(2, R.drawable.yue_jian_app_tab_message, 2, YueJianAppVChatEMConversationListFragment.class),
    MESSAGE(2, R.drawable.yue_jian_app_tab_message, 2, ConversationFragment.class),
    //  MESSAGE(2, R.drawable.yue_jian_app_tab_message, 2, VChatMessageListFragment.class),
    INFO(3, R.drawable.yue_jian_app_tab_info, 3, YueJianAppVChatHomePageFragment.class);

    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;

    YueJianAppMainTab(int idx, int resIcon, int resName, Class<?> clz) {
        this.idx = idx;
        this.resIcon = resIcon;
        this.resName = resName;
        this.clz = clz;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
