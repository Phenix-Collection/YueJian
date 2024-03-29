package com.mingquan.yuejian.auth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mingquan.yuejian.R;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

public class YueJianAppGridImageAdapter2 extends RecyclerView.Adapter<YueJianAppGridImageAdapter2.ViewHolder> {
    private static final int TYPE_CAMERA = 1;
    private static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private List<LocalMedia> list = new ArrayList<>();
    private int selectMax = 9;
    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;

    public interface onAddPicClickListener {
        void onAddPicClick();
    }

    public YueJianAppGridImageAdapter2(Context context, onAddPicClickListener mOnAddPicClickListener) {
        Context context1 = context;
        mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public void setList(List<LocalMedia> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        LinearLayout llDel;
        TextView tvDuration;

        public ViewHolder(View view) {
            super(view);
            mImg = (ImageView) view.findViewById(R.id.fiv);
            llDel = (LinearLayout) view.findViewById(R.id.ll_del);
            tvDuration = (TextView) view.findViewById(R.id.tv_duration);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.yue_jian_app_item_filter_image,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private boolean isShowAddItem(int position) {
        return position == list.size();
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setImageResource(R.drawable.yue_jian_app_add_img);
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAddPicClickListener.onAddPicClick();
                }
            });
        } else {
            LocalMedia media = list.get(position);
            String path = media.getCutPath();
            Log.i("裁剪地址::", media.getCutPath());
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.color.vchat_f6f6f6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(viewHolder.itemView.getContext())
                    .load(path)
                    .apply(options)
                    .into(viewHolder.mImg);
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = viewHolder.getAdapterPosition();
                        mItemClickListener.onItemClick(adapterPosition, v);
                    }
                });
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int adapterPosition = viewHolder.getAdapterPosition();
                        mItemLongClickListener.onItemLongClick(adapterPosition, v);
                        return true;
                    }
                });
            }
        }
    }

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

}
