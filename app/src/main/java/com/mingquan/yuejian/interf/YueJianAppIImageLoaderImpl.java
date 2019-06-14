package com.mingquan.yuejian.interf;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public interface YueJianAppIImageLoaderImpl {
  void loadImageUserHeadBig(Context context, String url, ImageView iv);
  void loadImageUserHead(Context context, String url, ImageView iv);
  void loadImageHighDefinite(Context context, String url, ImageView iv);
  void loadImageHighDefinite(
          Context context, String url, ImageView iv, ImageLoadingListener listener);
  void loadImage_glide(
          Context context, String url, ImageView iv);

}
