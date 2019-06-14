package com.mingquan.yuejian.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseApplication;
import com.mingquan.yuejian.bean.YueJianAppPrivateChatUserBean;
import com.mingquan.yuejian.interf.YueJianAppIImageLoaderImpl;
import com.mingquan.yuejian.utils.YueJianAppTimeFormater;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;

import java.util.ArrayList;
import java.util.List;

public class YueJianAppUserBaseInfoPrivateChatAdapter extends BaseAdapter {
  private List<YueJianAppPrivateChatUserBean> users;
  private Context mContext;
  private YueJianAppIImageLoaderImpl mImageLoader;

  public YueJianAppUserBaseInfoPrivateChatAdapter(Context context,List<YueJianAppPrivateChatUserBean> users) {
    this.users = users;
    this.mContext=context;
    mImageLoader= YueJianAppBaseApplication.getImageLoaderUtil();
  }

  public int getUnReadMessageCount() {
    int count = 0;
    if (null == users || users.size() == 0) {
      return count;
    }
    for (int i = 0; i < users.size(); i++) {
      YueJianAppPrivateChatUserBean mPrivateChatBean = users.get(i);
      if (mPrivateChatBean.isUnreadMessage()) {
        count++;
      }
    }
    return count;
  }
  @Override
  public int getCount() {
    return users.size();
  }

  @Override
  public Object getItem(int position) {
    return users.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = View.inflate(YueJianAppAppContext.getInstance(), R.layout.yue_jian_app_item_private_chat, null);
      viewHolder = new ViewHolder();
      viewHolder.mUHead = (YueJianAppAvatarView) convertView.findViewById(R.id.av_userHead);
      viewHolder.mUNice = (TextView) convertView.findViewById(R.id.tv_item_uname);
      viewHolder.mULastMsg = (TextView) convertView.findViewById(R.id.tv_item_last_msg);
      viewHolder.mUnread = (TextView) convertView.findViewById(R.id.iv_unread_dot);
      viewHolder.mCurTime = (TextView) convertView.findViewById(R.id.tv_time);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    YueJianAppPrivateChatUserBean u = users.get(position);

    /**
     * 系统消息
     */
    if (u.getId() == -100) {
      viewHolder.mUHead.setImageResource(R.drawable.yue_jian_app_ic_launcher);
      viewHolder.mUNice.setText("系统消息");
      viewHolder.mULastMsg.setText(u.getLastMessage());
      viewHolder.mUnread.setVisibility(u.isUnreadMessage() ? View.VISIBLE : View.GONE);
      viewHolder.mUnread.setText("" + u.getUnReadMsgCount());
      if (u.getLastMessageTime() != null) {
        long longSec = Long.valueOf(u.getLastMessageTime());
        viewHolder.mCurTime.setText(YueJianAppTimeFormater.getInterval2(String.valueOf(longSec / 1000)));
      }
    } else { //其他消息
      mImageLoader.loadImageUserHead(mContext,u.getAvatar(),viewHolder.mUHead);
      viewHolder.mUNice.setText(u.getUser_nicename());
      viewHolder.mULastMsg.setText(u.getLastMessage());
      viewHolder.mUnread.setVisibility(u.isUnreadMessage() ? View.VISIBLE : View.GONE);
      viewHolder.mUnread.setText("" + u.getUnReadMsgCount());
      if (u.getLastMessageTime() != null) {
        long longSec = Long.valueOf(u.getLastMessageTime());
        viewHolder.mCurTime.setText(YueJianAppTimeFormater.getInterval2(String.valueOf(longSec / 1000)));
      }
    }
    return convertView;
  }

  public void setPrivateChatUserList(ArrayList<YueJianAppPrivateChatUserBean> privateChatUserList) {
    if (this.users != null) {
      this.users.clear();
      if (privateChatUserList != null) {
        this.users.addAll(privateChatUserList);
      }
    }
    notifyDataSetChanged();
  }

  private class ViewHolder {
    public YueJianAppAvatarView mUHead;
    public TextView mUNice, mULastMsg, mCurTime, mUnread;
  }
}
