package com.mingquan.yuejian.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.mingquan.yuejian.R;

public class YueJianAppXHorizontalProgressBar extends View {
  private static final String TAG = YueJianAppXHorizontalProgressBar.class.getSimpleName();
  private float DEFAULT_RADIUS = 0f;
  /**
   * 控件默认的高度（宽度默认为全屏）
   * 单位为dp
   */
  private int DEFAULT_HEIGHT = 80;

  private Context mContext;
  private int mWidth, mHeight; // 整个的宽和高
  private int mProgressWidth;
  private int mProgressHeight;
  private float mProgressRadius; // 进度条圆角半径
  private float mProgressValue; //第二个值
  private float mProgressLeft = 5;
  private float mProgressTop = 5;
  private float mLevelLeft = 5;
  private float mLevelTop = 5;
  private boolean mIsRoundRect;
  private Paint mBackgroundPaint;
  private Paint mLevelCirclePaint;
  private Paint mValuePaint;
  private int mDefaultBgColor = Color.GRAY;
  private int mDefaultValueColor = Color.GREEN;
  private int mProgressBackgroundColor;
  private int mProgressColor = mDefaultValueColor;
  private int mCurrExpTextColor = Color.WHITE;
  private int mTotalExpTextColor = Color.RED;
  private float mSizeTextLevel;
  private float mSizeTextExp;
  private float mDefaultProgressHeight = 30; //像素
  private int MAX = 100;
  private int mProgress;
  private int mLevelRadius = 20;
  private String mLevelText;
  private int mCurrentExp = 230;
  private int mTotalExp = 500;
  private Paint mLevelTextPaint;
  private Paint mExpLevelPaint;

  public YueJianAppXHorizontalProgressBar(Context context) {
    this(context, null);
  }

  public YueJianAppXHorizontalProgressBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public YueJianAppXHorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mContext = context;
    TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(
        attrs, R.styleable.YueJianAppXHorizontalProgressBar, defStyleAttr, 0);
    for (int i = 0; i < typedArray.getIndexCount(); i++) {
      int attr = typedArray.getIndex(i);
      switch (attr) {
        case R.styleable.YueJianAppXHorizontalProgressBar_progressColor:
          mProgressColor = typedArray.getColor(attr, mDefaultValueColor);
          break;
        case R.styleable.YueJianAppXHorizontalProgressBar_backgroundColor:
          mProgressBackgroundColor = typedArray.getColor(attr, mDefaultBgColor);
          break;
        case R.styleable.YueJianAppXHorizontalProgressBar_isRoundRect:
          mIsRoundRect = typedArray.getBoolean(attr, true);
          break;
        case R.styleable.YueJianAppXHorizontalProgressBar_radius:
          mProgressRadius = typedArray.getDimension(attr, DEFAULT_RADIUS);
          break;
        case R.styleable.YueJianAppXHorizontalProgressBar_valueTextColor:

          break;
        case R.styleable.YueJianAppXHorizontalProgressBar_showTextProgress:

          break;
      }
    }
    typedArray.recycle();
    init();
  }

  /**
   * set and get method begin
   */
  public int getmProgress() {
    return mProgress;
  }

  public void setmProgress(int progress) {
    if (progress < 0 || progress > MAX)
      return;
    this.mProgress = progress;
    refreshUI();
  }

  public int getmCurrentExp() {
    return mCurrentExp;
  }

  public int getmTotalExp() {
    return mTotalExp;
  }

  public int getMAX() {
    return MAX;
  }

  public void setMAX(int max) {
    this.MAX = max;
    refreshUI();
  }

  public float getmProgressRadius() {
    return mProgressRadius;
  }

  public void setmProgressRadius(float mProgressRadius) {
    this.mProgressRadius = mProgressRadius;
    invalidate();
  }

  public void setCurrExpTextColor(int colorRes) {
    this.mCurrExpTextColor = colorRes;
    invalidate();
  }

  public void setTotalExpColor(int colorRes) {
    this.mTotalExpTextColor = colorRes;
    invalidate();
  }

  public int getmLevelRadius() {
    return mLevelRadius;
  }

  public void setmLevelRadius(int mLevelRadius) {
    this.mLevelRadius = mLevelRadius;
  }

  public String getmLevelText() {
    return mLevelText;
  }

  public void setmLevelText(String mLevelText) {
    this.mLevelText = mLevelText;
    invalidate();
  }

  public boolean ismIsRoundRect() {
    return mIsRoundRect;
  }

  public void setmIsRoundRect(boolean mIsRoundRect) {
    this.mIsRoundRect = mIsRoundRect;
  }

  public int getProgressBackgroundColor() {
    return mProgressBackgroundColor;
  }

  public void setProgressBackgroundColor(int mBackgroundColor) {
    this.mProgressBackgroundColor = mBackgroundColor;
    invalidate();
  }
  public int getProgressColor() {
    return mProgressColor;
  }
  public void setProgressColor(int mProgressColor) {
    this.mProgressColor = mProgressColor;
    invalidate();
  }

  /**
   * 设置经验值和总经验值
   *
   * @param currExp
   * @param totalExp
   */
  public void setExp(int currExp, int totalExp) {
    if (currExp < 0 || totalExp < 0) {
      throw new IllegalArgumentException("currExp or totalExp cannot smaller than zero!");
    }
    this.mCurrentExp = currExp;
    this.mTotalExp = totalExp;
    if (mCurrentExp > mTotalExp) {
      mCurrentExp = mTotalExp;
    }
    invalidate();
  }

  /**
   * 设置经验值，必须先设置总经验
   *
   * @param currExp
   */
  public void setCurrExp(int currExp) {
    this.mCurrentExp = currExp;
    if (mCurrentExp < 0) {
      mCurrentExp = 0;
    }
    if (mCurrentExp > mTotalExp) {
      mCurrentExp = mTotalExp;
    }
    invalidate();
  }

  /**
   * 设置升级需要的总的经验值
   *
   * @param totalExp
   */
  public void setTotalExp(int totalExp) {
    if (totalExp < 0)
      return;
    this.mTotalExp = totalExp;
    invalidate();
  }

  public float getSizeTextLevel() {
    return mSizeTextLevel;
  }

  public void setSizeTextLevel(float mSizeTextLevel) {
    this.mSizeTextLevel = mSizeTextLevel;
    invalidate();
  }

  public float getSizeTextExp() {
    return mSizeTextExp;
  }

  public void setSizeTextExp(float mSizeTextExp) {
    this.mSizeTextExp = mSizeTextExp;
    invalidate();
  }

  /** set and get end */

  /**
   * 初始化
   */
  private void init() {
    setLayerType(LAYER_TYPE_SOFTWARE, null);
    mDefaultValueColor = getResources().getColor(R.color.circle_level_bg);
    mProgressBackgroundColor = mDefaultBgColor;
    mProgressColor = mDefaultValueColor;
    mCurrExpTextColor = mContext.getResources().getColor(R.color.text_color_curr_exp);
    mTotalExpTextColor = mContext.getResources().getColor(R.color.text_color_total_exp);
    mIsRoundRect = true;
    mLevelText = "LV1";
    mProgressHeight = 70; //默认
    mSizeTextExp = 35f;
    mSizeTextLevel = 45f;
    mBackgroundPaint = new Paint();
    mValuePaint = new Paint();
    mLevelTextPaint = new Paint();
    mExpLevelPaint = new Paint();
    mLevelCirclePaint = new Paint();
    initTextPaint(mExpLevelPaint);
    initPain(mBackgroundPaint);
    initPain(mValuePaint);
    initPain(mLevelCirclePaint);
    initTextPaint(mLevelTextPaint);
  }

  private void initTextPaint(Paint paint) {
    paint.setFlags(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(Color.WHITE);
  }

  /**
   * 初始化画笔
   *
   * @param paint
   */
  private void initPain(Paint paint) {
    paint.setStyle(Paint.Style.FILL_AND_STROKE);
    paint.setAntiAlias(true); // 抗锯齿效果
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    int width = 0;
    int height = 0;

    if (widthMode == MeasureSpec.EXACTLY) {
      //一般是设置了明确的值或者是MATCH_PARENT
      width = widthSize;
    }
    if (heightMode == MeasureSpec.EXACTLY) {
      //一般是设置了明确的值或者是MATCH_PARENT
      height = heightSize;
    } else {
      height = dp2px(20);
    }
    //调用父类的方法
    setMeasuredDimension(width, height);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    calculateDimension();
    super.onLayout(changed, left, top, right, bottom);
  }

  /**
   * calculate width and height
   */
  private void calculateDimension() {
    mWidth = getWidth();
    mHeight = getHeight();
    mLevelRadius = (mHeight - 10) / 2;
    mProgressLeft = mLevelLeft + mLevelRadius * 2;
    mProgressTop = mLevelTop + mLevelRadius - mProgressHeight / 2;
    mProgressWidth = mWidth - 10 - mLevelRadius * 2; //进度条的宽度
    mProgressRadius = mProgressHeight / 2;
    mProgressValue = 0; //初始的进度条值
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawColor(Color.WHITE);
    this.mProgressValue = mCurrentExp * mProgressWidth / mTotalExp;
    mBackgroundPaint.setColor(mProgressBackgroundColor);
    mValuePaint.setColor(mProgressColor);
    drawRect(canvas, mBackgroundPaint, mProgressLeft - mProgressRadius, mProgressTop,
        mProgressLeft + mProgressWidth, mProgressTop + mProgressHeight); //预留5个像素的边距
    drawRect(canvas, mValuePaint, mProgressLeft - mProgressRadius + 1, mProgressTop + 1,
        mProgressLeft + mProgressValue - 1, mProgressTop + mProgressHeight - 1); //相当于padding=1;
    drawLevelCircle(canvas, mLevelCirclePaint, mLevelLeft, mLevelTop, mLevelRadius);
    drawLevelText(canvas, mLevelTextPaint, mLevelText, mSizeTextLevel);
    drawExpText(canvas, mExpLevelPaint, String.valueOf(mCurrentExp), String.valueOf(mTotalExp),
        mSizeTextExp);
  }

  /**
   * 画等级的字体
   */
  private void drawLevelText(Canvas canvas, Paint textPaint, String level, float textSize) {
    if (TextUtils.isEmpty(level))
      return;
    // FontMetrics对象
    textPaint.setTextSize(textSize);
    textPaint.setFakeBoldText(true);
    textPaint.measureText(level);

    Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
    float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
    float offY = fontTotalHeight / 2 - fontMetrics.bottom;
    float textWidth = textPaint.measureText(level, 0, level.length());
    canvas.drawText(level, mLevelLeft + mLevelRadius - textWidth / 2,
        mLevelTop + mLevelRadius + offY, textPaint);
  }

  /**
   * 画经验的字体
   */
  private void drawExpText(
      Canvas canvas, Paint textPaint, String currExp, String totalExp, float textSize) {
    if (TextUtils.isEmpty(currExp))
      return;
    // FontMetrics对象
    textPaint.setFakeBoldText(true);
    textPaint.measureText(currExp);
    textPaint.setTextSize(textSize);
    textPaint.setColor(mCurrExpTextColor);
    Paint.FontMetrics mCurrExpFontMetrics = textPaint.getFontMetrics();
    float mCurrExpTotalHeight = mCurrExpFontMetrics.bottom - mCurrExpFontMetrics.top;
    float mCurrExpOffY = mCurrExpTotalHeight / 2 - mCurrExpFontMetrics.bottom;
    float mCurrExpWidth = textPaint.measureText(currExp, 0, currExp.length());
    canvas.drawText(currExp, mProgressLeft + mProgressWidth / 2 - mCurrExpWidth,
        mProgressTop + mProgressHeight / 2 + mCurrExpOffY, textPaint);

    String totalExpText = String.format(mContext.getString(R.string.exp_formatter), totalExp);
    textPaint.setColor(mTotalExpTextColor);
    textPaint.measureText(totalExpText);
    Paint.FontMetrics mTotalExpFontMetrics = textPaint.getFontMetrics();
    float mTotalExpHeight = mTotalExpFontMetrics.bottom - mTotalExpFontMetrics.top;
    float mTotalExpOffY = mTotalExpHeight / 2 - mTotalExpFontMetrics.bottom;
    float mTotalExpWidth = textPaint.measureText(totalExpText, 0, totalExpText.length());
    canvas.drawText(totalExpText, mProgressLeft + (mProgressWidth / 2),
        mProgressTop + mProgressHeight / 2 + mTotalExpOffY, textPaint);
  }

  /**
   * 画左边的等级图标
   */
  private void drawLevelCircle(
      Canvas canvas, Paint mValuePaint, float mLevelStart, float mLevelTop, int mLevelRadius) {
    mValuePaint.setColor(mProgressColor);
    mValuePaint.setShadowLayer(5, 3, 3, Color.DKGRAY);
    canvas.drawCircle(
        mLevelStart + mLevelRadius, mLevelTop + mLevelRadius, mLevelRadius, mValuePaint);
  }

  /**
   * 清空画布
   *
   * @param canvas
   */
  public void clearCanvas(Canvas canvas) {
    Paint paint = new Paint();
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    canvas.drawPaint(paint);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    invalidate();
  }

  /**
   * 重新绘制
   */
  private void refreshUI() {
    this.mProgressValue = (mProgress * mProgressWidth) / MAX;
    invalidate();
  }

  /**
   * 绘制矩形
   *
   * @param canvas
   * @param paint
   * @param left   左
   * @param top    上
   * @param right  右
   * @param bottom 下
   */
  private void drawRect(
      Canvas canvas, Paint paint, float left, float top, float right, float bottom) {
    RectF rectF = new RectF(left, top, right, bottom);
    if (mIsRoundRect) {
      canvas.drawRoundRect(rectF, mProgressRadius, mProgressRadius, paint);
    } else {
      canvas.drawRect(rectF, paint);
    }
  }

  /**
   * dp 2 px
   *
   * @param dpVal
   */
  protected int dp2px(int dpVal) {
    return (int) TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
  }

  /**
   * sp 2 px
   *
   * @param spVal
   * @return
   */
  protected int sp2px(int spVal) {
    return (int) TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
  }
}
