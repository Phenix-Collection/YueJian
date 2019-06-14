package com.mingquan.yuejian.auth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseApplication;
import com.mingquan.yuejian.proto.model.YueJianAppACVideoInfoModel;
import com.mingquan.yuejian.widget.YueJianAppStatusTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YueJianAppGridVideoAdapter extends RecyclerView.Adapter<YueJianAppGridVideoAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<YueJianAppACVideoInfoModel> list = new ArrayList<>();
    private Context context;
    public YueJianAppGridVideoAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setList(List<YueJianAppACVideoInfoModel> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.video_item_layout)
        RelativeLayout videoItemLayout;
        @BindView(R.id.iv_add)
        ImageView ivAdd;
//        @BindView(R.id.rtv_price)
//        RoundTextView price;
        @BindView(R.id.rtv_price)
        TextView price;
        @BindView(R.id.riv_vchat_bg)
        ImageView rivVchatBg;
        @BindView(R.id.tv_gift_num)
        TextView giftNum;
        @BindView(R.id.tv_auth_status)
        YueJianAppStatusTextView authStatus;
        @BindView(R.id.love_num)
        TextView loveNum;
        @BindView(R.id.vchat_item_mask)
        ImageView vchatItemMask;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.yue_jian_app_item_vchat_video,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (position == 0) {
            viewHolder.videoItemLayout.setVisibility(View.GONE);
            viewHolder.ivAdd.setVisibility(View.VISIBLE);
        } else {
            YueJianAppACVideoInfoModel model = list.get(position - 1);
            viewHolder.videoItemLayout.setVisibility(View.VISIBLE);
            viewHolder.ivAdd.setVisibility(View.GONE);
            YueJianAppBaseApplication.getImageLoaderUtil().loadImageHighDefinite(context, model.getCoverUrl(), viewHolder.rivVchatBg);
            if (model.getPrice() == 0) {
                viewHolder.price.setVisibility(View.GONE);
            } else {
                viewHolder.price.setVisibility(View.VISIBLE);
                viewHolder.price.setText(String.format("%d钻石", model.getPrice()));
            }
            viewHolder.giftNum.setText(String.valueOf(model.getGiftAmount()));
            viewHolder.loveNum.setText(String.valueOf(model.getFollowerCount()));
            viewHolder.authStatus.setVideoAuthStatus(model.getAuthStatus());
        }
        //itemView 的点击事件
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    mItemClickListener.onItemClick(adapterPosition, v);
                }
            });
        }
    }

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
