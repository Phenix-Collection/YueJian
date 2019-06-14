package com.mingquan.yuejian.utils;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.mingquan.yuejian.R;

public class YueJianAppAnimUtil {
  /**
   * 设置旋转动画
   */
  public static void startTweenRotateAnim(View view) {
    final RotateAnimation animation = new RotateAnimation(
        0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    animation.setDuration(1800); //设置动画持续时间
    animation.setInterpolator(new LinearInterpolator());
    animation.setRepeatCount(Integer.MAX_VALUE); //设置重复次数
    //        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
    view.startAnimation(animation);
  }

  public static void startObjectRotateAnim(View view) {
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, 180, 360);
    animator.setRepeatCount(Integer.MAX_VALUE);
    animator.setRepeatMode(ValueAnimator.RESTART);
    animator.setDuration(1000);
    animator.start();
  }

  public static void startShakeAnim(ObjectAnimator animator) {
    if (null != animator) {
      animator.start();
    }
  }

  public static void stopShakeAnim(View view, ObjectAnimator animator) {
    if (null == view || null == animator)
      return;
    animator.end();
    animator.cancel();
    view.clearAnimation();
  }

  public static ObjectAnimator initShakeAnim(View view, int repeatCount) {
    if (null == view)
      return null;
    ObjectAnimator animator = shake(view, 1f);
    animator.setRepeatCount(repeatCount);
    animator.setRepeatMode(ValueAnimator.REVERSE);
    return animator;
  }

  public static ObjectAnimator shake(View view, float shakeFactor) {
    PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
        Keyframe.ofFloat(0f, 1f), Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
        Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f), Keyframe.ofFloat(.5f, 1.1f),
        Keyframe.ofFloat(.6f, 1.1f), Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
        Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

    PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
        Keyframe.ofFloat(0f, 1f), Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
        Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f), Keyframe.ofFloat(.5f, 1.1f),
        Keyframe.ofFloat(.6f, 1.1f), Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
        Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

    PropertyValuesHolder pvhRotate =
        PropertyValuesHolder.ofKeyframe(View.ROTATION, Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(.1f, -3f * shakeFactor), Keyframe.ofFloat(.2f, -3f * shakeFactor),
            Keyframe.ofFloat(.3f, 3f * shakeFactor), Keyframe.ofFloat(.4f, -3f * shakeFactor),
            Keyframe.ofFloat(.5f, 3f * shakeFactor), Keyframe.ofFloat(.6f, -3f * shakeFactor),
            Keyframe.ofFloat(.7f, 3f * shakeFactor), Keyframe.ofFloat(.8f, -3f * shakeFactor),
            Keyframe.ofFloat(.9f, 3f * shakeFactor), Keyframe.ofFloat(1f, 0));

    return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).setDuration(1000);
  }

  public static ObjectAnimator nope(View view) {
    int delta = view.getResources().getDimensionPixelOffset(R.dimen.space_8);

    PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
        Keyframe.ofFloat(0f, 0), Keyframe.ofFloat(.10f, -delta), Keyframe.ofFloat(.26f, delta),
        Keyframe.ofFloat(.42f, -delta), Keyframe.ofFloat(.58f, delta),
        Keyframe.ofFloat(.74f, -delta), Keyframe.ofFloat(.90f, delta), Keyframe.ofFloat(1f, 0f));

    return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).setDuration(500);
  }

  public static void doScale(View view,float scale){
    if (scale<0){
      throw new IllegalArgumentException("scale can not less than 0");
    }
    view.setPivotX(0);
    view.setPivotY(0);
    AnimatorSet animatorSet = new AnimatorSet();//组合动画
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scale);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scale);
    animatorSet.setDuration(500);
    animatorSet.setInterpolator(new DecelerateInterpolator());
    animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
    animatorSet.start();
  }

  public static void doScaleCenter(View view,float scale){
    if (scale<0){
      throw new IllegalArgumentException("scale can not less than 0");
    }
    view.setPivotX(view.getWidth()/2);
    view.setPivotY(view.getHeight()/2);
    AnimatorSet animatorSet = new AnimatorSet();//组合动画
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scale);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scale);
    animatorSet.setDuration(500);
    animatorSet.setInterpolator(new DecelerateInterpolator());
    animatorSet.playTogether(scaleX,scaleY);//两个动画同时开始
    animatorSet.start();
  }

  public static void doScaleRight(View view,float scale){
    if (scale<0){
      throw new IllegalArgumentException("scale can not less than 0");
    }
    view.setPivotX(view.getWidth());
    view.setPivotY(view.getWidth());
    AnimatorSet animatorSet = new AnimatorSet();//组合动画
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scale);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scale);
    animatorSet.setDuration(500);
    animatorSet.setInterpolator(new DecelerateInterpolator());
    animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
    animatorSet.start();
  }

  public static void doScaleLeft(View view,float scale){
    if (scale<0){
      throw new IllegalArgumentException("scale can not less than 0");
    }
    view.setPivotX(0);
    view.setPivotY(0);
    AnimatorSet animatorSet = new AnimatorSet();//组合动画
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scale);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scale);
    animatorSet.setDuration(500);
    animatorSet.setInterpolator(new DecelerateInterpolator());
    animatorSet.play(scaleX).with(scaleY);//两个动画同时开始
    animatorSet.start();
  }

  public static void doScaleXLeft(View view,float scale,int duration){
    if (scale<0){
      throw new IllegalArgumentException("scale can not less than 0");
    }
    view.setPivotX(0);
    view.setPivotY(view.getHeight()/2);
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX",0f,scale);
    scaleX.setDuration(300);
    scaleX.start();
  }

  /**
   * 缩放动画
   * @param view
   */
  public static void scale(View view){
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(
            view, "scaleX", 1f, 0.9f, 1f, 1.1f, 1f);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(
            view, "scaleY", 1f, 0.9f, 1f, 1.1f, 1f);
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(scaleX, scaleY);
    animatorSet.setDuration(200);
    animatorSet.start();
  }
}
