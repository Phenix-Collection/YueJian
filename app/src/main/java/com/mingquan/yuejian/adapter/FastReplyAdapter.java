package com.mingquan.yuejian.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.proto.model.YueJianAppACFastReplyModel;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/30
 * <p>
 * 私信快速回复列表适配器
 */

public class FastReplyAdapter extends BaseAdapter {
    Activity context;

    private List<YueJianAppACFastReplyModel> replyModels = new ArrayList<>();

    public FastReplyAdapter(Activity context) {
        this.context = context;
    }

    public void setReplyList(List<YueJianAppACFastReplyModel> replyModels) {
        this.replyModels.clear();
        this.replyModels.addAll(replyModels);
        this.notifyDataSetChanged();
    }

    public void addReplyItem(YueJianAppACFastReplyModel replyModel) {
        this.replyModels.add(replyModel);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return replyModels.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.layout_fast_reply_item, null);
            holder = new ViewHolder();
            holder.tvReplyDetail = convertView.findViewById(R.id.tv_reply_item_detail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == replyModels.size()) {
            holder.tvReplyDetail.setText("添加短语");
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_17005);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tvReplyDetail.setCompoundDrawables(
                    drawable,
                    null,
                    null,
                    null);
        } else {
            YueJianAppACFastReplyModel model = replyModels.get(position);
            if (model != null) {
                holder.tvReplyDetail.setText(model.getMessage());
            }
            holder.tvReplyDetail.setCompoundDrawables(null, null, null, null);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvReplyDetail;
    }
}
