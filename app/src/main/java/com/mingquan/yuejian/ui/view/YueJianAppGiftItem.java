package com.mingquan.yuejian.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.model.YueJianAppGiftItemModel;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;
import com.mingquan.yuejian.widget.YueJianAppLoadUrlImageView;

public class YueJianAppGiftItem extends FrameLayout {
  private Context mContext;
  private YueJianAppAvatarView mAvatarView;
  private TextView mTvSenderName, mTvGiftName;
  private YueJianAppLoadUrlImageView mGiftIcon;
  private TextView mTvGiftNum;

  private YueJianAppGiftItemModel mGiftItemModel;
  private AnimatorSet mAnimatorSet;
  TranslateAnimation mIconAnim;
  ScaleAnimation mGiftNumScaleAnim;
  private int mLastActiveTime; //上一次动画数目更新的时间

  public YueJianAppGiftItem(Context context) {
    this(context, null);
  }

  public YueJianAppGiftItem(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public YueJianAppGiftItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mContext = context;
    init();
  }

  private void init() {
    initView();
    initAnim();
  }

  /**
   * 初始化动画
   */
  private void initAnim() {
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(mTvGiftNum, "scaleX", 4.0f, 0.7f, 1.1f, 1f);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(mTvGiftNum, "scaleY", 4.0f, 0.7f, 1.1f, 1f);
    ObjectAnimator translateX = ObjectAnimator.ofFloat(mTvGiftNum, "TranslationX", 1f, 180f);
    translateX.setDuration(350);
    scaleX.setDuration(350);
    scaleY.setDuration(350);
    mAnimatorSet = new AnimatorSet();
    mAnimatorSet.playTogether(scaleX, scaleY, translateX);
    mAnimatorSet.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {}
      @Override public void onAnimationEnd(Animator animation) { //动画完成后处理
      }
      @Override
      public void onAnimationCancel(Animator animation) {}
      @Override
      public void onAnimationRepeat(Animator animation) {}
    });

    mIconAnim = (TranslateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.yue_jian_app_anim_gift_icon);
    mIconAnim.setFillAfter(true);

    mGiftNumScaleAnim = (ScaleAnimation) AnimationUtils.loadAnimation(mContext, R.anim.yue_jian_app_anim_gift_num);
  }

  private void initView() {
    View view = LayoutInflater.from(mContext).inflate(R.layout.yue_jian_app_gift_item, null);
    mAvatarView = (YueJianAppAvatarView) view.findViewById(R.id.av_gift_uhead);
    mTvSenderName = (TextView) view.findViewById(R.id.tv_gift_uname);
    mTvGiftName = (TextView) view.findViewById(R.id.tv_gift_gname);
    mGiftIcon = (YueJianAppLoadUrlImageView) view.findViewById(R.id.av_gift_icon);
    mTvGiftNum = (TextView) view.findViewById(R.id.tv_gift_num);
  }

  /**
   * 设置数据
   * @param giftItemModel
   */
  public void setItemData(YueJianAppGiftItemModel giftItemModel) {
    this.mGiftItemModel = giftItemModel;
    refreshItem();
  }

  /**
   * 刷新ui
   */
  private void refreshItem() {
    mGiftIcon.setGiftLoadUrl(mGiftItemModel.getGiftIcon());
    mTvGiftNum.setText("X" + mGiftItemModel.getGiftNum());
    mTvSenderName.setText(mGiftItemModel.getSenderName());
    mTvGiftName.setText(mGiftItemModel.getGiftName());
    mAvatarView.setAvatarUrl(mGiftItemModel.getSendAvatar());
  }

  /**
   * 开始动画
   */
  public void startGiftAnim() {
    mGiftIcon.startAnimation(mIconAnim);
    mAnimatorSet.start();
  }
}
