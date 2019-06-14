package com.mingquan.yuejian.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * 一个圆角矩形的按钮 可以设置 Radius，背景色，以及点击时状态的切换
 * Created by 李建涛 on 2016/11/30.
 */

public class YueJianAppXRoundRectButton extends Button {
  private Context mContext;
  private int mEnableColor = Color.GRAY; //默认为灰色
  private int mDisableColor = Color.BLUE; //默认为蓝色
  private float mRadius;
  private OnClickListener mOnClickListener;
  private boolean mIsClickEnable = true;
  private long mDownTime = 0;
  private long mDefaultDuritionTime = 800;
  private float mWidth = 0f;
  private float mHeight = 0f;

  public YueJianAppXRoundRectButton(Context context) {
    this(context, null);
  }

  public YueJianAppXRoundRectButton(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public YueJianAppXRoundRectButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
    setClickable(true);
  }

  private void init() {
    this.setPadding(10, 10, 10, 10);
  }

  /**
   * 设置背景色，包括按钮为Enable状态的和Disable状态的
   *
   * @param enableColor
   * @param disableColor
   */
  public void setBgColor(int enableColor, int disableColor) {
    this.mEnableColor = enableColor;
    this.mDisableColor = disableColor;
    setClickable(mIsClickEnable);
  }

  public void setRadius(float radius) {
    this.mRadius = radius;
    invalidate();
  }

  /**
   * 设置间隔时间
   *
   * @param durationTime
   */
  public void setDurationTime(long durationTime) {
    this.mDefaultDuritionTime = durationTime;
  }

  /**
   * 获取两次点击事件的有效间隔时间
   *
   * @return
   */
  public long getDurationTime() {
    return mDefaultDuritionTime;
  }

  /**
   * 获取是否可点击
   *
   * @return
   */
  public boolean getClickEnabled() {
    return this.mIsClickEnable;
  }

  /**
   * 设置点击时候的效果
   *
   * @param enable true 表示当前不可点击，false 表示当前可以点击
   */
  public void setClickEnable(boolean enable) {
    GradientDrawable background = new GradientDrawable(); //创建drawable
    if (enable) {
      background.setColor(mEnableColor);
      background.setCornerRadius(mRadius);
      setBackgroundDrawable(background);
      setEnabled(true);
      this.mIsClickEnable = true;
    } else {
      background.setColor(mDisableColor);
      background.setCornerRadius(mRadius);
      setBackgroundDrawable(background);
      setEnabled(false);
      this.mIsClickEnable = false;
    }
    invalidate();
  }

  @Override
  public void setOnClickListener(OnClickListener l) {
    this.mOnClickListener = l;
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    mWidth = getWidth();
    mHeight = getHeight();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int action = event.getAction();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        mDownTime = System.currentTimeMillis();
        if (this.isEnabled()) {
          setAlpha(0.5f);
        }
        break;
      case MotionEvent.ACTION_MOVE:

        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        long upTime = System.currentTimeMillis();
        boolean isClick = (upTime - mDownTime) < 600 ? true : false;
        if (mIsClickEnable && isClick && event.getX() <= getRight() - getLeft() && event.getX() >= 0
            && event.getY() <= getBottom() - getTop() && event.getY() >= 0) {
          dealClickEvent();
        }
        setAlpha(1.0f);
    }
    return true;
  }

  /**
   * 上次点击事件的执行时间
   */
  private long mLastClickEventTime;

  /**
   * 处理点击事件
   */
  private void dealClickEvent() {
    long time = System.currentTimeMillis();
    if (time - mLastClickEventTime < mDefaultDuritionTime) { //防止连续点击事件
      return;
    }
    mLastClickEventTime = time;
    if (mOnClickListener != null) {
      mOnClickListener.onClick(this);
    }
  }
}
