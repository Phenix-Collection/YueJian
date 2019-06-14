package com.mingquan.yuejian.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.holder.YueJianAppRecyclerViewHolder;
import com.mingquan.yuejian.base.YueJianAppBaseApplication;
import com.mingquan.yuejian.interf.YueJianAppIImageLoaderImpl;
import com.mingquan.yuejian.proto.model.YueJianAppACUserHomepageInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACVideoInfoModel;
import com.mingquan.yuejian.vchat.YueJianAppFastBlurUtil;
import com.mingquan.yuejian.widget.YueJianAppStatusTextView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/23.
 */

public class YueJianAppVChatVideoAdapter extends YueJianAppBaseRecyclerMutilAdapter<YueJianAppACVideoInfoModel> {

    public static final int TYPE_COMMON = 1;

    private boolean isFollow;
    private boolean isShowAuthStatus = false;


    private YueJianAppIImageLoaderImpl mImageLoaderUtil = YueJianAppBaseApplication.getImageLoaderUtil();

    private YueJianAppACUserHomepageInfoModel acUserHomepageInfoModel;


    public YueJianAppVChatVideoAdapter(Context ctx) {
        super(ctx);
    }

    public void setUserInfo(YueJianAppACUserHomepageInfoModel userHomepageInfoModel) {
        acUserHomepageInfoModel = userHomepageInfoModel;
        isFollow = acUserHomepageInfoModel.getRelation().getIsFollowTarget();
    }

    @Override
    public YueJianAppRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new YueJianAppRecyclerViewHolder(context, inflater.inflate(R.layout.yue_jian_app_item_vchat_video, null));
    }

    //覆写
    public void setList(ArrayList<YueJianAppACVideoInfoModel> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyItemRangeChanged(0, list.size());
    }

    public void setShowAuthStatus(boolean isShowAuthStatus) {
        this.isShowAuthStatus = isShowAuthStatus;
    }

    public void appendList(ArrayList<YueJianAppACVideoInfoModel> list) {
        int size = this.list.size();
        this.list.addAll(list);
        notifyItemRangeChanged(size, list.size());
    }

    @Override
    public void onBindViewHolder(final YueJianAppRecyclerViewHolder holder, final int position) {
        if (getItemViewType(position) != TYPE_COMMON) {
            return;
        }
        final YueJianAppACVideoInfoModel videoInfoModel = list.get(position);
        holder.getTextView(R.id.love_num).setText(String.valueOf(videoInfoModel.getFollowerCount()));
        if (videoInfoModel.getPrice() == 0) {
            holder.getTextView(R.id.rtv_price).setVisibility(View.GONE);
        } else {
            holder.getTextView(R.id.rtv_price).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.rtv_price).setText(videoInfoModel.getPrice() + "钻石");
        }
        holder.getTextView(R.id.tv_gift_num).setText(String.valueOf(videoInfoModel.getGiftAmount()));
        mImageLoaderUtil.loadImageHighDefinite(
                context,
                videoInfoModel.getCoverUrl(),
                holder.getImageView(R.id.riv_vchat_bg),
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        if (videoInfoModel.getHasLocked()) {
                            final Bitmap finalBitmap = bitmap;
                            holder.getImageView(R.id.riv_vchat_bg).post(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap blurBitmap = YueJianAppFastBlurUtil.toBlur(finalBitmap, 10);
                                    holder.getImageView(R.id.riv_vchat_bg).setImageBitmap(blurBitmap);
                                }
                            });
                            /*new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap blurBitmap = YueJianAppFastBlurUtil.toBlur(finalBitmap, 10);
                                    holder.getImageView(R.id.riv_vchat_bg).setImageBitmap(blurBitmap);
                                }
                            }).start();*/
                        } else {
                            if (bitmap.getWidth() > bitmap.getHeight()) {
                                Matrix matrix = new Matrix();
                                matrix.setRotate(-90);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                                holder.getImageView(R.id.riv_vchat_bg).setImageBitmap(bitmap);
                            }
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
//        UiUtils.setVisibility(holder.getImageView(R.id.vchat_item_mask), videoInfoModel.getHasLocked() ? View.VISIBLE : View.GONE);
        if (isShowAuthStatus) {
            ((YueJianAppStatusTextView) holder.getTextView(R.id.tv_auth_status)).setVideoAuthStatus(videoInfoModel.getAuthStatus());
            holder.getTextView(R.id.tv_auth_status).setVisibility(View.VISIBLE);
        } else {
            holder.getTextView(R.id.tv_auth_status).setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_COMMON;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
