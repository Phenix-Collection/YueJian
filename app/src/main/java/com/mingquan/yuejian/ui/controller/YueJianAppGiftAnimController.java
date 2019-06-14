package com.mingquan.yuejian.ui.controller;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.mingquan.yuejian.R;

/**
 * Controller for gift animation,Singleton Pattern
 * Created by Jiantao on 2017/6/30.
 */

public class YueJianAppGiftAnimController {
  private static YueJianAppGiftAnimController mGiftAnimController;
  private TranslateAnimation mGiftInAnim;
  TranslateAnimation mGiftOutAnim;
  TranslateAnimation mGiftIconAnim;

  private YueJianAppGiftAnimController(Context context) {}

  /**
   * initialize common gift animation
   * @param ctx
   */
  public void createCommonAnim(Context ctx) {
    mGiftInAnim = (TranslateAnimation) AnimationUtils.loadAnimation(ctx, R.anim.yue_jian_app_anim_gift_in);
    mGiftOutAnim = (TranslateAnimation) AnimationUtils.loadAnimation(ctx, R.anim.yue_jian_app_anim_gift_out);
    mGiftIconAnim = (TranslateAnimation) AnimationUtils.loadAnimation(ctx, R.anim.yue_jian_app_anim_gift_icon);
  }

  public static YueJianAppGiftAnimController getInstance(Context context) {
    if (null == mGiftAnimController) {
      synchronized (YueJianAppGiftAnimController.class) {
        mGiftAnimController = new YueJianAppGiftAnimController(context);
      }
    }
    return mGiftAnimController;
  }

  public void startGiftInAnim(View view, Animation.AnimationListener listener) {
    mGiftInAnim.setAnimationListener(listener);
    view.startAnimation(mGiftInAnim);
  }
}
