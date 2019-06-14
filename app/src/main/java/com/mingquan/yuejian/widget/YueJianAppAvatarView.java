package com.mingquan.yuejian.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseApplication;
import com.mingquan.yuejian.interf.YueJianAppIImageLoaderImpl;

import org.kymjs.kjframe.utils.StringUtils;

public class YueJianAppAvatarView extends YueJianAppCircleImageView {
  public static final String AVATAR_SIZE_REG = "_[0-9]{1,3}";
  public static final String MIDDLE_SIZE = "_100";
  public static final String LARGE_SIZE = "_200";

  private int id;
  private String name;
  private Context mContext;
  private YueJianAppIImageLoaderImpl mImageLoaderUtil;

  public YueJianAppAvatarView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  public YueJianAppAvatarView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public YueJianAppAvatarView(Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    mContext = context;
    mImageLoaderUtil = YueJianAppBaseApplication.getImageLoaderUtil();
  }

  public void setUserInfo(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public void setAvatarUrl(String url) {
    if (StringUtils.isEmpty(url)) {
      setImageResource(R.drawable.yue_jian_app_null_blacklist);
      return;
    }
    // 由于头像地址默认加了一段参数需要去掉
    int end = url.indexOf('?');
    final String headUrl;
    if (end > 0) {
      headUrl = url.substring(0, end);
    } else {
      headUrl = url;
    }

    mImageLoaderUtil.loadImageUserHead(mContext, getSmallAvatar(headUrl), this);
    /* Core.getKJBitmap().display(this, headUrl, R.drawable.yue_jian_app_null_blacklist, 0, 0,
             new BitmapCallBack() {
                 @Override
                 public void onFailure(Exception e) {
                     super.onFailure(e);
                     post(new Runnable() {
                         @Override
                         public void run() {
                             setImageResource(R.drawable.yue_jian_app_null_blacklist);
                         }
                     });

                 }
             });*/
  }

  public static String getSmallAvatar(String source) {
    return source;
  }

  public static String getMiddleAvatar(String source) {
    if (source == null)
      return "";
    return source.replaceAll(AVATAR_SIZE_REG, MIDDLE_SIZE);
  }

  public static String getLargeAvatar(String source) {
    if (source == null)
      return "";
    return source.replaceAll(AVATAR_SIZE_REG, LARGE_SIZE);
  }
}
