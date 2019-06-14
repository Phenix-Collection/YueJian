package com.mingquan.yuejian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.holder.YueJianAppRecyclerViewHolder;
import com.mingquan.yuejian.proto.model.YueJianAppACExtensionBarModel;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;

/**
 * Created by Administrator on 2019/3/29
 */

public class YueJianAppExtensionAdapter extends YueJianAppBaseRecyclerMutilAdapter<YueJianAppACExtensionBarModel> {
    public YueJianAppExtensionAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public YueJianAppRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new YueJianAppRecyclerViewHolder(
                context,
                LayoutInflater.from(context).inflate(R.layout.yue_jian_app_item_extension, null, false)
        );
    }

    @Override
    public void onBindViewHolder(YueJianAppRecyclerViewHolder holder, final int position) {
        final YueJianAppACExtensionBarModel model = list.get(position);
        holder.getTextView(R.id.tv_item_title).setText(model.getName());
        holder.getTextView(R.id.tv_item_content).setText(model.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YueJianAppStringUtil.isEmpty(model.getLinkUrl())) {
                    YueJianAppTLog.error("url is empty");
                    return;
                }
                YueJianAppUIHelper.showWebView(context, model.getLinkUrl(), "");
            }
        });
    }
}
