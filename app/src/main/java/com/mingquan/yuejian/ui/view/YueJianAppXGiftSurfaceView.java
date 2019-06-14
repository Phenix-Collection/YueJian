package com.mingquan.yuejian.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.bean.YueJianAppSurfaceFrameBean;
import com.mingquan.yuejian.bean.YueJianAppFrameBean;
import com.mingquan.yuejian.em.YueJianAppAnimType;
import com.mingquan.yuejian.proto.model.YueJianAppACGiftAnimationConfigModel;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 宠物特效类，采用SurfaceView绘制
 * Created by 李建涛 on 2017/1/6.
 */

public class YueJianAppXGiftSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
  private static final String TAG = YueJianAppXGiftSurfaceView.class.getSimpleName();
  private RectF mClickRect = new RectF();
  private SurfaceHolder mHolder;
  private AnimThread mAnimThread;
  private long lastClickTime;
  private Paint mPaint;
  private int mWidth = 512;
  private int mHeight = 512;
  private String mResourceId;
  private boolean mIsStart = false;
  private int rate = 15;
  private int index = 0;
  private Random rand;
  private Context mContext;
  private Paint mBgPaint;
  private YueJianAppAnimType mAnimType;
  private YueJianAppSurfaceFrameBean mSurfaceFrameBean;
  private float mClickRatio;
  private float mPetOffset;
  private int leftOffset;
  private int topOffset;

  private Handler handler = new Handler() {
    public void handleMessage(android.os.Message msg) {}
  };
  private BitmapFactory.Options options;
  private float mScaleX = 1;
  private float mScaleY;
  private Bitmap heart;
  private Bitmap mBgBitmap;
  private Bitmap mScaleBgBitmap;

  public YueJianAppXGiftSurfaceView(Context context) {
    super(context);
    initClickArea();
    init(context);
  }

  public YueJianAppXGiftSurfaceView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initClickArea();
    init(context);
  }

  public YueJianAppXGiftSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initClickArea();
    init(context);
  }

  private void initClickArea() {
    mWidth = 10;
    mHeight = 10;
    mClickRatio = 1 / 3;
    /**设置默认的宽高*/
    resetClickArea();
  }

  /**
   * 设置控件参数
   */
  public void setSize(int width, int height) {
    if (width <= 0 || height <= 0)
      return;
    this.mWidth = width;
    this.mHeight = height;
    resetLayoutParams();
  }

  public float calculateScale(int width, int height) { //计算缩放的方法
    float scaleX = YueJianAppTDevice.getScreenWidth() / width;
    float scaleY = YueJianAppTDevice.getScreenHeight() / height;
    return scaleX > scaleY ? scaleY : scaleX;
  }

  public float calculateScale() { //计算缩放的方法
    if (null == mSurfaceFrameBean || null == mSurfaceFrameBean.getAnimationConfigModel()) {
      return 1.0f;
    }
    int width = mSurfaceFrameBean.getAnimationConfigModel().getImageWidth();
    int height = mSurfaceFrameBean.getAnimationConfigModel().getImageHeight();
    if (width <= 0 || height <= 0) {
      return 1.0f;
    }
    float scaleX = YueJianAppTDevice.getScreenWidth() / width;
    float scaleY = YueJianAppTDevice.getScreenHeight() / height;
    return scaleX > scaleY ? scaleX : scaleY;
  }

  private void resetLayoutParams() {
    if (mWidth <= 0 || mHeight <= 0)
      return;
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
    params.width = (int) (mWidth * mScaleX);
    params.height = (int) (mHeight * mScaleX);
    params.addRule(Gravity.CENTER);
    setLayoutParams(params);
  }

  /**
   * 设置方法系数
   *
   * @param scale
   */
  public void setScale(float scale) {
    this.mScaleX = scale;
    resetLayoutParams();
  }

  /**
   * 设置点击区域
   */
  public void setCilckArea(RectF rectF) {
    mClickRect = rectF;
  }

  /**
   * 设置点击可点击区域
   *
   * @param clickRatio 点击参数 范围是 0 到 1,如果设为三分之一，表示控件在1/3*mWidth 到 2/3 *
   * mWidth, 1/3 *mHeight 到 2/3 * mHeight 区域内可以点击
   */
  public void setClickArea(float clickRatio) {
    if (clickRatio >= 1 || clickRatio <= 0) {
      return;
    }
    this.mClickRatio = clickRatio;
    resetClickArea();
  }

  /**
   * 设置左偏移和上偏移
   *
   * @param leftOffset
   * @param topOffset
   */
  public void setOffset(int leftOffset, int topOffset) {
    this.leftOffset = leftOffset;
    this.topOffset = topOffset;
    invalidate();
  }

  /**
   * 改变点击区域
   */
  private void resetClickArea() {
    mClickRect.set(mWidth * mClickRatio, mHeight * mClickRatio, mWidth * (1 - mClickRatio),
        mHeight * (1 - mClickRatio));
  }



  /**
   * /**
   * 设置动画资源
   */
  public void setAnimResource(YueJianAppSurfaceFrameBean surfaceFrameBean) {
    if (null == surfaceFrameBean) {
      return;
    }
    this.mSurfaceFrameBean = surfaceFrameBean;
    YueJianAppACGiftAnimationConfigModel configModel = surfaceFrameBean.getAnimationConfigModel();
    if (null != configModel) {
      YueJianAppTLog.error("surfaceFrameBean.getAnimationConfigModel().getImageWidth()=="
          + surfaceFrameBean.getAnimationConfigModel().getImageWidth());
      mWidth = configModel.getImageWidth();
      mHeight = configModel.getImageHeight();
      rate = 1000 / configModel.getFps();
      int fullScreen = configModel.getFullScreen();
      if (fullScreen == 1) {
        mScaleX = calculateScale();
      } else {
        mScaleX = 1.0f;
      }
      resetLayoutParams();
      setRate((int) (rate * 0.6));
    }
    /* if (!TextUtils.isEmpty(mSurfaceFrameBean.getBgFilePath())){
       mBgBitmap = BitmapFactory.decodeFile(mSurfaceFrameBean.getBgFilePath());
     }else{//防止下一个动画没有背景 必须将背景置为null
       mBgBitmap=null;
       mScaleBgBitmap=null;
     }*/
  }

  @SuppressLint("NewApi")
  private void init(Context context) {
    mContext = context;
    mHolder = getHolder();
    mHolder.addCallback(this);
    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    rand = new Random();
    heart = BitmapFactory.decodeResource(getResources(), R.drawable.yue_jian_app_ico_heart_05);
    options = new BitmapFactory.Options();
    options.inPreferredConfig = Bitmap.Config.RGB_565;
    //    calculateScale();
    setZOrderOnTop(true);
    //    setZOrderMediaOverlay(true);
    getHolder().setFormat(PixelFormat.TRANSLUCENT);
    getPaint();
  }
  private boolean shouldPlayAnim=true;

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    //        handler.sendEmptyMessageDelayed(0, 1000 * (4 + rand.nextInt(4)));
    //        mAnimThread = new AnimThread();
    Log.e(TAG,"surfaceCreated 重新播放动画");
    if (!TextUtils.isEmpty(mResourceId)&&shouldPlayAnim) {
      resetAnim(mResourceId, YueJianAppAnimType.Single);
    }
  }

  @Override
  protected void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Log.e(TAG,"onConfigurationChanged ");
    if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) { //横屏
      shouldPlayAnim=false;
    }else{
      shouldPlayAnim=true;
    }
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    Log.e(TAG,"surfaceChanged ");

  }

  @Override
  public void onScreenStateChanged(int screenState) {
    super.onScreenStateChanged(screenState);
    Log.e(TAG,"onScreenStateChanged");
    Log.e(TAG,"screenState:"+screenState);
    if (screenState== View.SCREEN_STATE_OFF){
      shouldPlayAnim=false;
    }else{
      shouldPlayAnim=true;
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    Log.e(TAG,"surfaceDestroyed");
    shouldPlayAnim=false;
    if (mAnimThread != null) {
      mAnimThread.isRun = false;
      mAnimThread.stopThread();
      mAnimThread = null;
    }
  }

  //2017年12月8日16:12:49 不拦截事件
  @Override
  public boolean onFilterTouchEventForSecurity(MotionEvent event) {
    return false;
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    mWidth = getWidth();
    mHeight = getHeight();
  }

  public interface OnAnimEndListener { void onAnimEnd(); }

  public AnimThread getAnimThread() {
    return mAnimThread;
  }

  public class AnimThread extends Thread {
    public boolean isRun = true;
    public OnAnimEndListener mAnimEndListener;
    public void setOnAnimEndListener(OnAnimEndListener listener) {
      this.mAnimEndListener = listener;
    }

    @Override
    public void run() {
      super.run();
      SurfaceHolder holder = mHolder;
      while (isRun) {
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
          SystemClock.sleep(1000);
          YueJianAppTLog.error("YueJianAppXGiftSurfaceView canvas==" + canvas);
          continue;
        }
        synchronized (AnimThread.class) {
          List<YueJianAppFrameBean> mFrameList = null;
          Bitmap bmp = null;
          mFrameList = mSurfaceFrameBean.getAnimFrameList(mResourceId);
          if (mFrameList != null && mFrameList.size() > 0) {
            if (drawAnim(holder, canvas, mFrameList, bmp))
              continue;
          }
          holder.unlockCanvasAndPost(canvas); //解锁画布
          if (mFrameList != null && index < mFrameList.size()) {
            index++;
            if (index >= mFrameList.size()) {
              switch (mAnimType) {
                case Recycle:
                  index = 0;
                  break;
                case Single:
                  index = 0; // Jiantao stopAnim()
                  clearScreen();
                  isRun = false;
                  if (this.mAnimEndListener != null) {
                    mAnimEndListener.onAnimEnd();
                  }
                  break;
              }
            }
          } else {
            stopThread();
          }
        }
        SystemClock.sleep(rate);
      }
    }

    private void clearScreen() {
      Canvas canvas = mHolder.lockCanvas(null);
      canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // 清除画布
      mHolder.unlockCanvasAndPost(canvas);
    }

    public void stopThread() {
      isRun = false;
      try {
        join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        mAnimThread = null;
      }
    }
  }

  /**
   * 切换动画
   */
  private synchronized void switchPlayAnim(String resourceId, YueJianAppAnimType animType) {
    this.mResourceId = resourceId;
    this.mAnimType = animType;
  }

  /**
   * 绘制Bitmap
   *
   * @param holder
   * @param canvas
   * @param mFrameList
   * @param bmp
   * @return
   */
  private boolean drawAnim(
      SurfaceHolder holder, Canvas canvas, List<YueJianAppFrameBean> mFrameList, Bitmap bmp) {
    if (index >= mFrameList.size()) {
      index = 0;
    }
    YueJianAppFrameBean mFrameBean = mFrameList.get(index);
    if (mFrameBean.frame.w > 0) {
      Rect rect = new Rect(mFrameBean.frame.x, mFrameBean.frame.y,
          mFrameBean.frame.x + mFrameBean.frame.w, mFrameBean.frame.y + mFrameBean.frame.h);
      bmp = getBitmap(mFrameBean, bmp, rect);
      if (null == bmp) {
        YueJianAppTLog.error("*********************bitmap 为空");
      }
      if (bmp == null) {
        holder.unlockCanvasAndPost(canvas);
        return true;
      }
      canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
      //            canvas.drawBitmap(scaleBitmap(bmp, scale), mFrameBean.spriteSourceSize.x,
      //            mFrameBean.spriteSourceSize.y, mPaint);
      //            canvas.drawBitmap(scaleBitmap(bmp, mScaleX), -(2 * mFrameBean.spriteSourceSize.x
      //            + mFrameBean.spriteSourceSize.w * mScaleX) / 2 + mWidth / 2, -(2 *
      //            mFrameBean.spriteSourceSize.y + mFrameBean.spriteSourceSize.h * mScaleX) / 2 +
      //            mWidth / 2, mPaint);
      //            mPaint.setColor(Color.GREEN);
      //      mPetOffset = (mWidth * mScaleX - mWidth) / 2;
      // SurfaceView 的 背景图 暂时去掉
      /* if (null!=mScaleBgBitmap){
         canvas.drawBitmap(mScaleBgBitmap,0,0,mPaint);
         canvas.save();
       }*/
      canvas.drawBitmap(scaleBitmap(bmp, mScaleX), mFrameBean.spriteSourceSize.x * mScaleX,
          mFrameBean.spriteSourceSize.y * mScaleX, mPaint);
      mClickRect.set(mFrameBean.spriteSourceSize.x * mScaleX,
          mFrameBean.spriteSourceSize.y * mScaleX,
          mFrameBean.spriteSourceSize.x * mScaleX + mFrameBean.spriteSourceSize.w * mScaleX,
          mFrameBean.spriteSourceSize.y * mScaleX + mFrameBean.spriteSourceSize.h * mScaleX);
      //            canvas.drawRect(mClickRect, mBgPaint);
      /**drawBitmap after*/
      // 李建涛 暂时注释掉 此操作适用于car和castle，meteor动画
      //          canvas.drawBitmap(scaleBitmap(bmp), mFrameBean.spriteSourceSize.x * scale,
      //          mFrameBean.spriteSourceSize.y * scale, mPaint);
      // 调试 画一个心在上边
      /*if (index>12){
        canvas.drawBitmap(scaleBitmap(heart,mScaleX),
                mFrameBean.spriteSourceSize.x * mScaleX+mFrameBean.spriteSourceSize.w/2,
                mFrameBean.spriteSourceSize.y * mScaleX+mFrameBean.spriteSourceSize.h/2, mPaint);
      }*/
      if (!bmp.isRecycled()) {
        bmp.recycle();
      }
    }
    return false;
  }

  /**
   * 获取将要绘制的Bitmap
   *
   * @param mFrameBean
   * @param bmp
   * @param rect
   * @return
   */
  private Bitmap getBitmap(YueJianAppFrameBean mFrameBean, Bitmap bmp, Rect rect) {
    Set set = mSurfaceFrameBean.mBitmapRegionDecoderHashMap.keySet();
    Iterator iter = set.iterator();
    while (iter.hasNext()) {
      String key = (String) iter.next();
      if (key.endsWith(mFrameBean.getImageSource())) {
        if (mSurfaceFrameBean.mBitmapRegionDecoderHashMap.get(key) != null) {
          //          YueJianAppTLog.info("mSurfaceFrameBean.mBitmapRegionDecoderHashMap.get(key)");
          if (rect != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            bmp =
                mSurfaceFrameBean.mBitmapRegionDecoderHashMap.get(key).decodeRegion(rect, options);
          } else {
            YueJianAppTLog.info("rect为空了。");
          }
        } else {
          YueJianAppTLog.info("mSurfaceFrameBean.mBitmapRegionDecoderHashMap.get(key)为空了。");
        }
        break;
      }
    }
    return bmp;
  }

  public void getPaint() {
    mBgPaint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
    mBgPaint.setColor(Color.GREEN);
    mBgPaint.setStrokeJoin(Paint.Join.ROUND);
    mBgPaint.setStrokeCap(Paint.Cap.ROUND);
    mBgPaint.setAntiAlias(true);
    mBgPaint.setStrokeWidth(3);
  }

  private float mClickX;
  private float mClickY;
  private float mUpX;
  private float mUpY;
  private long mTimeDown;
  private long mTimeUp;
  private boolean isClickEvent = false;

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        isClickEvent = false;
        mClickX = event.getX();
        mClickY = event.getY();
        if (mClickRect.contains(mClickX, mClickY)) {
          mTimeDown = System.currentTimeMillis();
          return true;
        } else {
          return super.onTouchEvent(event);
        }
      //                break;
      case MotionEvent.ACTION_MOVE:
        break;
      case MotionEvent.ACTION_UP:
        mUpX = event.getX();
        mUpY = event.getY();
        mTimeUp = System.currentTimeMillis();
        break;
    }
    /**解析出点击事件*/
    if (mTimeUp - mTimeDown <= 550 && Math.abs(mUpX - mClickX) < 15
        && Math.abs(mUpY - mClickY) < 15) {
      isClickEvent = true;
    }
    if (isClickEvent) {
      dealClickEvent();
    }
    return true;
  }

  /**
   * 处理点击事件
   */
  private void dealClickEvent() {
    long time = System.currentTimeMillis();
    if (time - lastClickTime < 1000) { //防止连续点击事件
      return;
    }
    lastClickTime = time;
    if (mOnPetClickListener != null) {
      mOnPetClickListener.onPetClick();
    }
  }

  private OnPetClickListener mOnPetClickListener;

  /**
   * 宠物点击事件
   */
  public interface OnPetClickListener { void onPetClick(); }

  public void setOnPetClickListener(OnPetClickListener listener) {
    this.mOnPetClickListener = listener;
  }

  /**
   * Bitmap的缩放
   *
   * @param bmp
   * @return
   */
  public Bitmap scaleBitmap(Bitmap bmp, float scale) {
    //        float scale = YueJianAppTDevice.getScreenWidth() / bmp.getWidth();
    //    if (this.mCurrAnimState == AnimBean.RED_CAR) {
    //      scale = mScaleX;
    //    }
    // 定义矩阵对象
    Matrix matrix = new Matrix();
    matrix.postScale(scale, scale);
    // bmp.getWidth(), bmp.getHeight()分别表示缩放后的位图宽高
    Bitmap dstbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    return dstbmp;
  }

  public synchronized void setRate(int rate) {
    if (rate <= 0) {
      this.rate = 20;
    }
    this.rate = rate;
  }

  public String getCurrResourceId() {
    return this.mResourceId;
  }

  public synchronized void setmCurrAnimState(String resourceId) {
    this.mResourceId = resourceId;
    this.mAnimType = YueJianAppAnimType.Recycle; //默认是循环
  }

  public void setUid(int uid) {}

  private int uid = 0;

  /**
   * 开始动画
   *
   * @param resourceId
   * @param animType
   */
  public synchronized void startAnim(String resourceId, YueJianAppAnimType animType, int uid) {
    if (TextUtils.isEmpty(resourceId))
      return; //如果当前正在播放相同的动画 则返回
    if (mSurfaceFrameBean == null)
      return;
    this.mAnimType = animType;
    this.mResourceId = resourceId;
    this.uid = uid;
    this.index = 0;
    if (mAnimThread != null) {
      mAnimThread.stopThread(); //停止上一个线程
    }

    if (null != mBgBitmap) {
      mScaleBgBitmap = scaleBitmap(mBgBitmap, mScaleX);
    }
    mAnimThread = new AnimThread(); //开启新的线程
    mAnimThread.start();
  }

  /**
   * 开始动画
   *
   * @param resourceId
   * @param animType
   */
  public synchronized void startAnim(String resourceId, YueJianAppAnimType animType) {
    /*  if (state <= mCurrAnimState)
      return;*/ //如果当前正在播放相同的动画 则返回
    if (mSurfaceFrameBean == null)
      return;
    this.mAnimType = animType;
    this.mResourceId = resourceId;
    this.index = 0;
    if (mAnimThread != null) {
      mAnimThread.stopThread(); //停止上一个线程
    }
    mAnimThread = new AnimThread(); //开启新的线程
    mAnimThread.start();
  }

  /**
   * 播放动画 用于页面跳转后返回 重新播放动画
   */
  private synchronized void resetAnim(String resourceId, YueJianAppAnimType animType) {
    if (mSurfaceFrameBean == null)
      return;
    this.mAnimType = animType;
    this.mResourceId = resourceId;
    this.index = 0;
    if (mAnimThread != null) {
      mAnimThread.stopThread(); //停止上一个线程
    }
    mAnimThread = new AnimThread(); //开启新的线程
    mAnimThread.start();
  }

  public synchronized void setRunning(boolean isrunning) {}

  public synchronized void addIndex() {
    this.index++;
  }
}
