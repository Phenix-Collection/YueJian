package com.mingquan.yuejian.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.utils.YueJianAppTDevice;

import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.utils.StringUtils;

/**
 * Created by Administrator on 2016/3/14.
 */
public class YueJianAppLoadUrlImageView extends AppCompatImageView {
  private Activity aty;
  public YueJianAppLoadUrlImageView(Context context) {
    super(context);
    init(context);
  }

  public YueJianAppLoadUrlImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  private void init(Context context) {
    aty = (Activity) context;
  }
  public YueJianAppLoadUrlImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
  public void setImageLoadUrl(String url) {
    if (StringUtils.isEmpty(url)) {
      setImageResource(0);
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

    Core.getKJBitmap().display(
        this, headUrl, 0, 0, 0, new BitmapCallBack() {
          @Override
          public void onFailure(Exception e) {
            super.onFailure(e);
            if (aty != null) {
              aty.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  setImageResource(0);
                }
              });
            }
            setImageResource(0);
          }
        });
  }

  public void setBackLoadUrl(String url) {
    if (StringUtils.isEmpty(url)) {
      setBackgroundResource(0);
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

    Core.getKJBitmap().display(this, headUrl, 0,
        (int) YueJianAppTDevice.getScreenWidth(), (int) YueJianAppTDevice.getScreenHeight(), new BitmapCallBack() {
          @Override
          public void onFailure(Exception e) {
            super.onFailure(e);
            if (aty != null) {
              aty.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  setBackgroundResource(0);
                }
              });
            }
            setBackgroundResource(0);
          }
        });
  }

  public void setGiftLoadUrl(String url) {
    if (StringUtils.isEmpty(url)) {
      setImageResource(0);
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

    Core.getKJBitmap().display(
        this, headUrl, R.drawable.yue_jian_app_bg_round_transparent, 0, 0, new BitmapCallBack() {
          @Override
          public void onFailure(Exception e) {
            super.onFailure(e);
            if (aty != null) {
              aty.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  setImageResource(R.drawable.yue_jian_app_bg_round_transparent);
                }
              });
            }
            setImageResource(R.drawable.yue_jian_app_bg_round_transparent);
          }
        });
  }
}
