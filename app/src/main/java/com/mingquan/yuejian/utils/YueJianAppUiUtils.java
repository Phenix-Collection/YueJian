package com.mingquan.yuejian.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Adair on 2017/2/16.
 */

public class YueJianAppUiUtils {
  /**
   * set visibility for view
   *
   * @param view       View
   * @param visibleTag visible status
   */
  public static void setVisibility(View view, int visibleTag) {
    if (null != view && view.getVisibility() != visibleTag) {
      view.setVisibility(visibleTag);
    }
  }

  /**
   * set background color
   *
   * @param view
   * @param resId
   */
  public static void setImageBackgroundResource(ImageView view, int resId) {
    if (null != view) {
      view.setBackgroundResource(resId);
    }
  }

  /**
   * set view text
   *
   * @param view
   * @param s
   */
  public static void setViewText(View view, String s) {
    if (null != view) {
      if (view instanceof TextView) {
        ((TextView) view).setText(s);
      } else if (view instanceof EditText) {
        ((EditText) view).setText(s);
      }
    }
  }

  /**
   * set text size
   *
   * @param view
   * @param textSize
   */
  public static void setTextView(TextView view, int textSize) {
    if (null != view) {
      view.setTextSize(textSize);
    }
  }

  /**
   * post visible
   *
   * @param view
   * @param v
   */
  public static void postVisible(final View view, final int v) {
    if (null != view) {
      view.post(new Runnable() {
        @Override
        public void run() {
          setVisibility(view, v);
        }
      });
    }
  }

  /**
   * 展示View向上滑动动画
   *
   * @param view
   * @param listener
   */
  public static void showViewUpAnim(View view, Animator.AnimatorListener listener) {
    if (null == view)
      return;
    view.setVisibility(View.VISIBLE);
    ObjectAnimator animator =
        ObjectAnimator.ofFloat(view, "translationY", (YueJianAppTDevice.dpToPixel(220)), 0);
    animator.setInterpolator(new AccelerateInterpolator());
    animator.setDuration(300);
    if (null != listener) {
      animator.addListener(listener);
    }
    animator.start();
  }

  public static void showViewUpAnimQuick(
      View view, float transUpY, Animator.AnimatorListener listener) {
    if (null == view)
      return;
    view.setVisibility(View.VISIBLE);
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, -transUpY);
    animator.setInterpolator(new AccelerateInterpolator());
    animator.setDuration(10);
    if (null != listener) {
      animator.addListener(listener);
    }
    animator.start();
  }

  /**
   * 显示自上而下的动画
   *
   * @param view
   * @param transDownY
   * @param listener
   */
  public static void showViewDownAnimQuick(
      View view, float transDownY, Animator.AnimatorListener listener) {
    if (null == view)
      return;
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, transDownY);
    animator.setInterpolator(new AccelerateInterpolator());
    animator.setDuration(0);
    if (null != listener) {
      animator.addListener(listener);
    }
    animator.start();
  }

  /**
   * View自上而下消失动画
   *
   * @param view
   * @param listener
   */
  public static void showViewDownAnim(View view, Animator.AnimatorListener listener) {
    if (null == view)
      return;
    ObjectAnimator animator =
        ObjectAnimator.ofFloat(view, "translationY", 0, YueJianAppTDevice.dpToPixel(220));
    animator.setInterpolator(new AccelerateInterpolator());
    animator.setDuration(300);
    if (null != listener) {
      animator.addListener(listener);
    }
    animator.start();
  }

  /**
   * 展示View向上滑动动画 消失时间 50ms
   *
   * @param view
   * @param listener
   */
  public static void showViewUpImmediatelyAnim(View view, Animator.AnimatorListener listener) {
    if (null == view)
      return;
    view.setVisibility(View.VISIBLE);
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, 0);
    animator.setInterpolator(new AccelerateInterpolator());
    animator.setDuration(50);
    if (null != listener) {
      animator.addListener(listener);
    }
    animator.start();
  }

  /**
   * View自上而下消失动画  消失时间 50ms
   *
   * @param view
   * @param listener
   */
  public static void showViewDownImmediatelyAnim(View view, Animator.AnimatorListener listener) {
    if (null == view)
      return;
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, 0);
    animator.setInterpolator(new AccelerateInterpolator());
    animator.setDuration(50);
    if (null != listener) {
      animator.addListener(listener);
    }
    animator.start();
  }

  /**
   * 展示View向上滑动动画
   *
   * @param view
   * @param listener
   */
  public static void showViewUpAnimWithHeight(View view, Animator.AnimatorListener listener) {
    if (null == view)
      return;
    int height = YueJianAppAppUtil.getViewHeight(view);
    view.setVisibility(View.VISIBLE);
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, -height);
    animator.setInterpolator(new AccelerateInterpolator());
    animator.setDuration(30);
    if (null != listener) {
      animator.addListener(listener);
    }
    animator.start();
  }

  /**
   * View自上而下消失动画
   *
   * @param view
   * @param listener
   */
  public static void showViewDownAnimWithHeight(View view, Animator.AnimatorListener listener) {
    if (null == view)
      return;
    int height = YueJianAppAppUtil.getViewHeight(view);
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, height);
    animator.setInterpolator(new AccelerateInterpolator());
    animator.setDuration(300);
    if (null != listener) {
      animator.addListener(listener);
    }
    animator.start();
  }

  /**
   * Y 轴方向的移动动画
   * @param view
   * @param startY
   * @param endY
   * @param listener
   */
  public static void doTranslateYAnim(
      View view, int startY, int endY, Animator.AnimatorListener listener) {
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", startY, endY);
    animator.setInterpolator(new AccelerateInterpolator());
    animator.setDuration(300);
    if (null != listener) {
      animator.addListener(listener);
    }
    animator.start();
  }
}
