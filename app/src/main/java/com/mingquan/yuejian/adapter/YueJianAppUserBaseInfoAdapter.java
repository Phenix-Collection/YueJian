package com.mingquan.yuejian.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.proto.model.YueJianAppACUserReleationInfoModel;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;
import com.mingquan.yuejian.widget.YueJianAppStatusTextView;

import java.util.List;

/**
 *
 */
public class YueJianAppUserBaseInfoAdapter extends BaseAdapter {
    private List<YueJianAppACUserReleationInfoModel> users;
    private Activity mActivity;

    public YueJianAppUserBaseInfoAdapter(List<YueJianAppACUserReleationInfoModel> users, Activity activity) {
        this.users = users;
        mActivity = activity;
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
        final ViewHolder viewHolder;
        YueJianAppACUserReleationInfoModel model = users.get(position);
        if (convertView == null) {
            convertView = View.inflate(YueJianAppAppContext.getInstance(), R.layout.yue_jian_app_item_attention, null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = (YueJianAppAvatarView) convertView.findViewById(R.id.avatar);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.love_num = (TextView) convertView.findViewById(R.id.love_num);
            viewHolder.status = (YueJianAppStatusTextView) convertView.findViewById(R.id.stv_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.status.setStatus(model.getUser().getStatus(), model.getUser().getStatusTag());
        viewHolder.avatar.setAvatarUrl(model.getUser().getAvatarUrl());
        viewHolder.name.setText(model.getUser().getName());
        viewHolder.love_num.setText(model.getUser().getUid());
        return convertView;
    }

    class ViewHolder {
        YueJianAppAvatarView avatar;
        TextView name;
        TextView love_num;
        public YueJianAppStatusTextView status;
    }
}
