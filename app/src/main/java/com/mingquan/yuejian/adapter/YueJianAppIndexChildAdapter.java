package com.mingquan.yuejian.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.holder.YueJianAppRecyclerViewHolder;
import com.mingquan.yuejian.base.YueJianAppBaseApplication;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.view.YueJianAppMyRatingBar;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.widget.YueJianAppSlideShowView;
import com.mingquan.yuejian.widget.YueJianAppStatusTextView;
import com.mingquan.yuejian.xrecyclerview.YueJianAppLoadingMoreFooter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrato on 2017/3/23
 */

public class YueJianAppIndexChildAdapter extends YueJianAppBaseRecyclerMutilAdapter<YueJianAppACUserPublicInfoModel> {

    private final static int TYPE_COMMON = 0;
    private final static int TYPE_FOOTER = 1;
    private final static int TYPE_HEADER = 2;

    private YueJianAppLoadingMoreFooter mMoreFooter;
    private ArrayList<String> mImageList = new ArrayList<>();
    private ArrayList<String> mUrlList = new ArrayList<>();

    public YueJianAppLoadingMoreFooter getMoreFooter() {
        return mMoreFooter;
    }

    public YueJianAppIndexChildAdapter(Context ctx) {
        super(ctx);
        mMoreFooter = new YueJianAppLoadingMoreFooter(context, context.getResources().getColor(R.color.gray));
        mMoreFooter.setLayoutParams(
                new RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public YueJianAppRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View headView = LayoutInflater.from(context).inflate(R.layout.yue_jian_app_banner_layout, parent, false);
                headView.setLayoutParams(new LinearLayout.LayoutParams((int) YueJianAppTDevice.getScreenWidth(), ((int) (YueJianAppTDevice.getScreenWidth() / 4))));
                return new YueJianAppRecyclerViewHolder(context, headView);
            case TYPE_COMMON:
                return new YueJianAppRecyclerViewHolder(
                        context,
                        LayoutInflater.from(context).inflate(R.layout.yue_jian_app_index_child_list_item, parent, false));
            case TYPE_FOOTER:
                return new YueJianAppRecyclerViewHolder(context, mMoreFooter);
            default:
                return new YueJianAppRecyclerViewHolder(context, mMoreFooter);
        }
    }

    @Override
    public void onBindViewHolder(YueJianAppRecyclerViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                if (mImageList.size() > 0) {
                    ((YueJianAppSlideShowView) holder.getView(R.id.banner)).setImageList(mImageList);
                    ((YueJianAppSlideShowView) holder.getView(R.id.banner)).setUrlList(mUrlList);
                    ((YueJianAppSlideShowView) holder.getView(R.id.banner)).startPlay();
                }
                break;
            case TYPE_COMMON:
                final int index = mImageList.size() > 0 ? position - 1 : position;
                ((YueJianAppMyRatingBar) holder.getView(R.id.rating_bar)).setStar(list.get(index).getStar());
                YueJianAppBaseApplication.getImageLoaderUtil().loadImage_glide(
                        context,
                        list.get(index).getAvatarUrl(),
                        holder.getImageView(R.id.image_view));

                holder.getTextView(R.id.nick).setText(list.get(index).getName());

                holder.getTextView(R.id.tv_coin_time).setText(String.valueOf(list.get(index).getPrice()));
                holder.getTextView(R.id.tv_user_id).setText(String.format("ID:%s", list.get(index).getUid()));
                ((YueJianAppStatusTextView) holder.getView(R.id.status)).setStatus(list.get(index).getStatus(), list.get(index).getStatusTag());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onItemClick(v, index);
                    }
                });
                // 动态显示和隐藏
                boolean canVideoChat = false;
                if (YueJianAppAppContext.getInstance().getPrivateInfoModel() != null) {
                    canVideoChat = YueJianAppAppContext.getInstance().getPrivateInfoModel().getCanVideoChat();
                }
                holder.getTextView(R.id.sign).setText(canVideoChat ? list.get(index).getSignature() : "");
                holder.getView(R.id.ll_coin_time).setVisibility(canVideoChat ? View.VISIBLE : View.GONE);
                holder.getView(R.id.status).setVisibility(canVideoChat ? View.VISIBLE : View.GONE);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mImageList.size() > 0) {
            if (position == 0) {
                return TYPE_HEADER;
            } else if (position < list.size()) {
                return TYPE_COMMON;
            } else {
                return TYPE_FOOTER;
            }
        } else {
            if (position < list.size()) {
                return TYPE_COMMON;
            } else {
                return TYPE_FOOTER;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mImageList.size() > 0 ? list.size() + 1 : list.size();
    }

//    mBannerView.setImageList(mRollImageList);
//    mBannerView.setUrlList(mRollUrlList);

    public void setImageList(List<String> imageList) {
        this.mImageList.clear();
        this.mImageList.addAll(imageList);
    }

    public void setUrlList(List<String> urlList) {
        this.mUrlList.clear();
        this.mUrlList.addAll(urlList);
    }

}
