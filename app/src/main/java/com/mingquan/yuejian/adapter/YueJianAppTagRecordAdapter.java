package com.mingquan.yuejian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.holder.YueJianAppRecyclerViewHolder;
import com.mingquan.yuejian.proto.model.YueJianAppACUserTagMetaDataModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserTaggingModel;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.vchat.YueJianAppLabelGroup;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;

import java.util.ArrayList;

/**
 * Created by administrator on 2018/9/29.
 * <p>
 * 用户标签记录 adapter
 */

public class YueJianAppTagRecordAdapter extends YueJianAppBaseRecyclerMutilAdapter<YueJianAppACUserTaggingModel> {


    public YueJianAppTagRecordAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public YueJianAppRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new YueJianAppRecyclerViewHolder(
                context,
                LayoutInflater.from(context).inflate(R.layout.yue_jian_app_item_tagging_record, null, false)
        );
    }

    @Override
    public void onBindViewHolder(final YueJianAppRecyclerViewHolder holder, final int position) {
        YueJianAppACUserTaggingModel model = list.get(position);

        if (model == null) {
            YueJianAppTLog.error("user tagging model == null");
            return;
        }
        ((YueJianAppAvatarView)holder.getImageView(R.id.avatar)).setAvatarUrl(model.getAvatar());
        holder.getTextView(R.id.tv_name).setText(model.getNickname());
        YueJianAppACUserTagMetaDataModel tagMetaDataModel = YueJianAppAppContext.getInstance().getUserTagMetaByTagId(model.getTagId());
        ArrayList<YueJianAppACUserTagMetaDataModel> tagList = new ArrayList<>();
        tagList.add(tagMetaDataModel);
        ((YueJianAppLabelGroup)holder.getView(R.id.label_container)).setLabels(tagList);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
