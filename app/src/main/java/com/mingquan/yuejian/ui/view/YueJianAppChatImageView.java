package com.mingquan.yuejian.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import com.mingquan.yuejian.R;

import org.kymjs.kjframe.Core;
import org.kymjs.kjframe.bitmap.BitmapCallBack;
import org.kymjs.kjframe.utils.StringUtils;

/**
 * Created by sunzhenya
 * on 2016/12/6.
 */
public class YueJianAppChatImageView extends AppCompatImageView {
  private int mBorderRadius;
  private int mCornerPosition;
  public static final int CORNER_LEFT = 1; // left
  public static final int CORNER_RIGHT = 0; // right
  private static final int BODER_RADIUS_DEFAULT = 5;
  // private RectF mRoundRect;
  /**
   * 绘图的Paint
   */
  private Paint mBitmapPaint;
  /**
   * 3x3 矩阵，主要用于缩小放大
   */
  private Matrix mMatrix;

  /**
   * 渲染图像，使用图像为绘制图形
   */
  private BitmapShader mBitmapShader;
  private Activity aty;

  private void init(Context context) {
    aty = (Activity) context;
  }

  public YueJianAppChatImageView(Context context) {
    this(context, null);
    init(context);
  }

  public YueJianAppChatImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public YueJianAppChatImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
    this.setMaxHeight(dp2px(173));
    this.setMaxWidth(dp2px(180));
    this.setAdjustViewBounds(true);
    mMatrix = new Matrix();
    mBitmapPaint = new Paint();
    mBitmapPaint.setAntiAlias(true);
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.YueJianAppChatImageView);
    mBorderRadius = a.getDimensionPixelSize(R.styleable.YueJianAppChatImageView_border_radius,
        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BODER_RADIUS_DEFAULT,
            getResources().getDisplayMetrics()));
    mCornerPosition = a.getInt(R.styleable.YueJianAppChatImageView_corner_position, CORNER_LEFT);
    a.recycle();
  }

  public void setImageLoadUrl(String url) {
    if (StringUtils.isEmpty(url)) {
      setImageResource(R.drawable.yue_jian_app_default_pic);
      return;
    }
    int end = url.indexOf('?');
    final String headUrl;
    if (end > 0) {
      headUrl = url.substring(0, end);
    } else {
      headUrl = url;
    }

    Core.getKJBitmap().display(this, headUrl, R.drawable.yue_jian_app_default_pic, 0, 0, new BitmapCallBack() {
      @Override
      public void onFailure(Exception e) {
        super.onFailure(e);
        if (aty != null) {
          aty.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              setImageResource(R.drawable.yue_jian_app_default_pic);
            }
          });
        }
        setImageResource(R.drawable.yue_jian_app_default_pic);
      }
    });
  }

  @Override
  protected void onDraw(Canvas canvas) {
    Drawable drawable = getDrawable();
    if (drawable == null) {
      return;
    }
    Bitmap bitmap = drawableToBitamp(drawable);
    clipit(canvas, bitmap);
  }

  private void clipit(Canvas canvas, Bitmap bitmapimg) {
    mBitmapShader = new BitmapShader(bitmapimg, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    float scale = 1.0f;
    if (bitmapimg.getHeight() > dp2px(173) || bitmapimg.getWidth() > dp2px(180)) {
      if (bitmapimg.getWidth() > bitmapimg.getHeight()) {
        Log.e("error", "w>H");
        scale = dp2px(180) * 1.0f / bitmapimg.getWidth();
      } else {
        Log.e("error", "H>W");
        scale = dp2px(173) * 1.0f / bitmapimg.getHeight();
      }
    }

    // shader的变换矩阵，我们这里主要用于放大或缩小
    mMatrix.setScale(scale, scale);
    // 设置变换矩阵
    mBitmapShader.setLocalMatrix(mMatrix);
    // 设置shader
    mBitmapPaint.setShader(mBitmapShader);
    Path path = new Path();
    // right
    if (mCornerPosition == 0) {
      RectF rectF =
          new RectF(0, 0, (bitmapimg.getWidth()) * scale - 15, (bitmapimg.getHeight()) * scale);
      path.addRoundRect(rectF, Float.parseFloat(String.valueOf(mBorderRadius)),
          Float.parseFloat(String.valueOf(mBorderRadius)), Path.Direction.CW);
      path.moveTo((bitmapimg.getWidth()) * scale - 15, 30 + mBorderRadius * scale);
      path.lineTo((bitmapimg.getWidth()) * scale, 40 + mBorderRadius * scale);
      path.lineTo((bitmapimg.getWidth()) * scale - 15, 50 + mBorderRadius * scale);
      path.lineTo((bitmapimg.getWidth()) * scale - 15, 30 + mBorderRadius * scale);
      path.close();
    }
    // left
    if (mCornerPosition == 1) {
      RectF rectF =
          new RectF(15, 0, (bitmapimg.getWidth()) * scale, (bitmapimg.getHeight()) * scale);
      path.addRoundRect(rectF, Float.parseFloat(String.valueOf(mBorderRadius)),
          Float.parseFloat(String.valueOf(mBorderRadius)), Path.Direction.CW);
      path.moveTo(15, 30 + mBorderRadius);
      path.lineTo(0, 40 + mBorderRadius);
      path.lineTo(15, 50 + mBorderRadius);
      path.lineTo(15, 30 + mBorderRadius);
      path.close();
    }
    canvas.drawPath(path, mBitmapPaint);
  }

  /**
   * drawable转bitmap
   *
   * @param drawable
   * @return
   */
  private Bitmap drawableToBitamp(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      BitmapDrawable bd = (BitmapDrawable) drawable;
      return bd.getBitmap();
    }
    int w = drawable.getIntrinsicWidth();
    int h = drawable.getIntrinsicHeight();
    w = (w > 0 ? w : getDefaultWidth());
    h = (h > 0 ? h : getDefaultHeight());
    Log.e("chatimageview,w,h", String.valueOf(w) + "---" + String.valueOf(h));
    Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, w, h);
    drawable.draw(canvas);
    return bitmap;
  }

  private int defaultWidth = 35;
  private int defaultHeight = 35;

  public void setDefaultWidth(int w) {
    defaultWidth = w;
  }

  public int getDefaultWidth() {
    return defaultWidth;
  }

  public void setDefaultHeight(int h) {
    defaultHeight = h;
  }

  public int getDefaultHeight() {
    return defaultHeight;
  }

  private static final String STATE_INSTANCE = "state_instance";
  private static final String STATE_TYPE = "state_type";
  private static final String STATE_BORDER_RADIUS = "state_border_radius";

  @Override
  protected Parcelable onSaveInstanceState() {
    Bundle bundle = new Bundle();
    bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
    bundle.putInt(STATE_TYPE, mCornerPosition);
    bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
    return bundle;
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    if (state instanceof Bundle) {
      Bundle bundle = (Bundle) state;
      super.onRestoreInstanceState(((Bundle) state).getParcelable(STATE_INSTANCE));
      this.mCornerPosition = bundle.getInt(STATE_TYPE);
      this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
    } else {
      super.onRestoreInstanceState(state);
    }
  }

  public void setBorderRadius(int borderRadius) {
    int pxVal = dp2px(borderRadius);
    if (this.mBorderRadius != pxVal) {
      this.mBorderRadius = pxVal;
      invalidate();
    }
  }

  public void setCornerPosition(int type) {
    if (this.mCornerPosition != type) {
      this.mCornerPosition = type;
      if (this.mCornerPosition != CORNER_LEFT && this.mCornerPosition != CORNER_RIGHT) {
        this.mCornerPosition = CORNER_LEFT;
      }
      requestLayout();
    }
  }

  public int dp2px(int dpVal) {
    return (int) TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
  }
}
