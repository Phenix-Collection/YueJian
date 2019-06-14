package com.mingquan.yuejian.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.widget.ImageView;

import com.mingquan.yuejian.YueJianAppAppContext;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrato on 2016/11/16.
 */

public class YueJianAppMyAnimationDrawable {
  public static class MyFrame {
    byte[] bytes;
    int duration;
    Drawable drawable;
    boolean isReady = false;
  }

  public interface OnDrawableLoadedListener {
    void onDrawableLoaded(List<MyFrame> myFrames, long totalTime);
  }

  // 1

  /***
   * 性能更优
   * 在animation-list中设置时间
   **/
  public static void animateRawManuallyFromXML(int resourceId, final ImageView imageView,
      final Runnable onStart, final Runnable onComplete) {
    if (imageView != null) {
      loadRaw(
          resourceId, YueJianAppAppContext.getInstance().getBaseContext(), new OnDrawableLoadedListener() {
            @Override
            public void onDrawableLoaded(List<MyFrame> myFrames, long totalTime) {
              if (onStart != null) {
                onStart.run();
              }
              animateRawManually(myFrames, imageView, onComplete);
            }
          });
    }
  }

  // 2
  private static void loadRaw(final int resourceId, final Context context,
      final OnDrawableLoadedListener onDrawableLoadedListener) {
    loadFromXml(resourceId, context, onDrawableLoadedListener);
  }

  /**
   * 从Xml中获取Drawable，然后播放动画
   *
   * @param resourceId
   * @param imageView
   * @param onStart
   * @param onComplete
   * @param repeatCount
   */
  public static void animateRawRepeatFromXml(int resourceId, final ImageView imageView,
      final Runnable onStart, final Runnable onComplete, final int repeatCount) {
    loadRaw(resourceId, imageView.getContext(), new OnDrawableLoadedListener() {
      @Override
      public void onDrawableLoaded(List<MyFrame> myFrames, long totalTime) {
        if (onStart != null) {
          onStart.run();
        }
        animateRawRepeat(myFrames, imageView, repeatCount, onComplete);
      }
    });
  }

  public static void animateRawRepeatWithListener(int resourceId, final ImageView imageView,
      final Runnable onStart, final Runnable onSingleComplete, final Runnable onComplete,
      final int repeatCount) {
    loadRaw(resourceId, imageView.getContext(), new OnDrawableLoadedListener() {
      @Override
      public void onDrawableLoaded(List<MyFrame> myFrames, long totalTime) {
        if (onStart != null) {
          onStart.run();
        }
        animateRawRepeatWithSingleComplete(
            myFrames, imageView, repeatCount, onSingleComplete, onComplete);
      }
    });
  }

  public static void animateRawRepeatFromXml2(int resourceId, final ImageView imageView,
      final Runnable onStart, final Runnable onComplete, final int repeatCount) {
    loadRaw(resourceId, imageView.getContext(), new OnDrawableLoadedListener() {
      @Override
      public void onDrawableLoaded(List<MyFrame> myFrames, long totalTime) {
        if (onStart != null) {
          onStart.run();
        }
        animateRawRepeat2(myFrames, imageView, repeatCount, onComplete);
      }
    });
  }

  /**
   * 设置循环播放帧动画，可以设置一个自定义的时间长度
   *
   * @param resourceId
   * @param imageView
   * @param onStart
   * @param onComplete
   * @param timeLong
   */
  public static void animateRawRepeatWithTimeLongFromXml(int resourceId, final ImageView imageView,
      final Runnable onStart, final Runnable onComplete, final int timeLong) {
    loadRaw(resourceId, imageView.getContext(), new OnDrawableLoadedListener() {
      @Override
      public void onDrawableLoaded(List<MyFrame> myFrames, long totalTime) {
        if (onStart != null) {
          onStart.run();
        }
        animateRawRepeatWithTimeLong(myFrames, imageView, timeLong, totalTime, onComplete);
      }
    });
  }

  /**
   * 设置循环播放帧动画，可以设置一个自定义的时间长度
   *
   * @param resourceId
   * @param imageView
   * @param onStart
   * @param onComplete
   * @param timeLong
   */
  public static void animateRawRepeatWithTimeLongFromXml2(int resourceId, final ImageView imageView,
      final Runnable onStart, final Runnable onComplete, final int timeLong) {
    loadRaw(resourceId, imageView.getContext(), new OnDrawableLoadedListener() {
      @Override
      public void onDrawableLoaded(List<MyFrame> myFrames, long totalTime) {
        if (onStart != null) {
          onStart.run();
        }
        animateRawRepeatWithTimeLong2(myFrames, imageView, timeLong, totalTime, onComplete);
      }
    });
  }

  /**
   * 设置循环播放帧动画
   *
   * @param myFrames
   * @param imageView
   * @param repeatCount
   * @param onComplete
   */
  public static void animateRawRepeat2(
      List<MyFrame> myFrames, ImageView imageView, int repeatCount, Runnable onComplete) {
    //开始播放
    animateRawRepeat2(myFrames, imageView, repeatCount, onComplete, 0);
  }

  /**
   * 设置从夫播放帧动画的方法
   *
   * @param myFrames
   * @param imageView
   * @param repeatCount
   * @param onComplete
   * @param frameNumber
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  private static void animateRawRepeat2(final List<MyFrame> myFrames, final ImageView imageView,
      final int repeatCount, final Runnable onComplete, final int frameNumber) {
    try {
      final MyFrame thisFrame = myFrames.get(frameNumber % myFrames.size());
      if (thisFrame == null)
        return;
      if (frameNumber % myFrames.size() == 0) {
        thisFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
            BitmapFactory.decodeByteArray(thisFrame.bytes, 0, thisFrame.bytes.length));
      } else if ((frameNumber + 1) / myFrames.size() < repeatCount) {
        MyFrame previousFrame = myFrames.get((frameNumber - 1) % myFrames.size());
        if (previousFrame != null) {
          /**动画播放完毕后不能销毁bitmap，只需要将isReady置为false*/
          /*   ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
             previousFrame.drawable = null;*/
          previousFrame.isReady = false;
        }

      } else if ((frameNumber + 1) / myFrames.size() >= repeatCount) {
        MyFrame previousFrame = myFrames.get((frameNumber - 1) % myFrames.size());
        /**动画播放完毕后不能销毁bitmap，只需要将isReady置为false*/
        if (previousFrame.drawable != null) {
          ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
          previousFrame.drawable = null;
          previousFrame.isReady = false;
        }
      }

      if (thisFrame.drawable != null) {
        Bitmap bitmap1 = ((BitmapDrawable) thisFrame.drawable).getBitmap();
        if (bitmap1 == null)
          return;
        Bitmap bitmap2 = Bitmap.createBitmap(800, 600, Bitmap.Config.RGB_565);

        //            Canvas canvas = new Canvas(bitmap2);

        Matrix matrix = new Matrix();
        float[] values = {-1f, 0.0f, 0.0f, 0.0f, 1f, 0.0f, 0.0f, 0.0f, 1.0f};
        matrix.setValues(values);
        final Bitmap dstbmp = Bitmap.createBitmap(
            bitmap1, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);

        //            canvas.drawBitmap(dstbmp, bitmap1.getWidth(), 0, null);

        imageView.setImageBitmap(dstbmp);

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            // Make sure ImageView hasn't been changed to a different Image
            // in this time
            if ((frameNumber + 1) / myFrames.size() < repeatCount) {
              MyFrame nextFrame = myFrames.get((frameNumber + 1) % myFrames.size());
              if (nextFrame.isReady) {
                // Animate next frame
                animateRawRepeat2(myFrames, imageView, repeatCount, onComplete, frameNumber + 1);
              } else {
                nextFrame.isReady = true;
              }
            } else {
              if (onComplete != null) {
                onComplete.run();
              }
            }
          }
        }, thisFrame.duration);

        // Load next frame
        if ((frameNumber + 1) / myFrames.size() <= repeatCount) {
          new Thread(new Runnable() {
            @Override
            public void run() {
              MyFrame nextFrame = myFrames.get((frameNumber + 1) % myFrames.size());
              nextFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
                  BitmapFactory.decodeByteArray(nextFrame.bytes, 0, nextFrame.bytes.length));
              if (nextFrame.isReady) {
                // Animate next frame
                animateRawRepeat2(myFrames, imageView, repeatCount, onComplete, frameNumber + 1);
              } else {
                nextFrame.isReady = true;
              }
            }
          }).run();
        }
      }
    } catch (Exception e) {
      YueJianAppTLog.error(e.getMessage());
    }
  }

  /**
   * 设置循环播放帧动画
   *
   * @param myFrames
   * @param imageView
   * @param repeatCount
   * @param onComplete
   */
  public static void animateRawRepeat(
      List<MyFrame> myFrames, ImageView imageView, int repeatCount, Runnable onComplete) {
    //开始播放
    animateRawRepeat(myFrames, imageView, repeatCount, onComplete, 0);
  }

  public static void animateRawRepeatWithSingleComplete(List<MyFrame> myFrames, ImageView imageView,
      int repeatCount, Runnable onSingleComplete, Runnable onComplete) {
    //开始播放
    animateRawRepeatWithSingleCompleteListener(
        myFrames, imageView, repeatCount, onSingleComplete, onComplete, 0);
  }

  /**
   * 设置循环播放帧动画，设定一个时间长度
   *
   * @param myFrames
   * @param imageView
   * @param timeLong
   * @param onComplete
   */
  public static void animateRawRepeatWithTimeLong(List<MyFrame> myFrames, ImageView imageView,
      long timeLong, long totalTime, Runnable onComplete) {
    //开始播放
    animateRawRepeatSetTimeLong(myFrames, imageView, timeLong, totalTime, onComplete, 0);
  }

  public static void animateRawRepeatWithTimeLong2(List<MyFrame> myFrames, ImageView imageView,
      long timeLong, long totalTime, Runnable onComplete) {
    //开始播放
    animateRawRepeatSetTimeLong2(myFrames, imageView, timeLong, totalTime, onComplete, 0);
  }

  // 3
  private static void loadFromXml(final int resourceId, final Context context,
      final OnDrawableLoadedListener onDrawableLoadedListener) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        final ArrayList<MyFrame> myFrames = new ArrayList<MyFrame>();
        long totalTime = 0;

        XmlResourceParser parser = context.getResources().getXml(resourceId);

        try {
          int eventType = parser.getEventType();
          while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
            } else if (eventType == XmlPullParser.START_TAG) {
              if (parser.getName().equals("item")) {
                byte[] bytes = null;
                int duration = 1000;
                for (int i = 0; i < parser.getAttributeCount(); i++) {
                  if (parser.getAttributeName(i).equals("drawable")) {
                    int resId = Integer.parseInt(parser.getAttributeValue(i).substring(1));
                    bytes = YueJianAppIOUtils.toByteArray(context.getResources().openRawResource(resId));
                  } else if (parser.getAttributeName(i).equals("duration")) {
                    duration = parser.getAttributeIntValue(i, 1000);
                    totalTime += duration;
                  }
                }
                MyFrame myFrame = new MyFrame();
                myFrame.bytes = bytes;
                myFrame.duration = duration;
                myFrames.add(myFrame);
              }

            } else if (eventType == XmlPullParser.END_TAG) {
            } else if (eventType == XmlPullParser.TEXT) {
            }

            eventType = parser.next();
          }
        } catch (IOException e) {
          e.printStackTrace();
        } catch (XmlPullParserException e2) {
          // TODO: handle exception
          e2.printStackTrace();
        }

        // Run on UI Thread
        final long finalTotalTime = totalTime;
        new Handler(context.getMainLooper()).post(new Runnable() {
          @Override
          public void run() {
            if (onDrawableLoadedListener != null) {
              onDrawableLoadedListener.onDrawableLoaded(myFrames, finalTotalTime);
            }
          }
        });
      }
    }).run();
  }

  /**
   * @param myFrames
   * @param imageView
   * @param onComplete
   */
  private static void animateRawManually(
      List<MyFrame> myFrames, ImageView imageView, Runnable onComplete) {
    animateRawManually(myFrames, imageView, onComplete, 0);
  }

  /**
   * 设置重复播放帧动画的方法
   *
   * @param myFrames
   * @param imageView
   * @param repeatCount
   * @param onComplete
   * @param frameNumber
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  private static void animateRawRepeat(final List<MyFrame> myFrames, final ImageView imageView,
      final int repeatCount, final Runnable onComplete, final int frameNumber) {
    try {
      final MyFrame thisFrame = myFrames.get(frameNumber % myFrames.size());
      if (frameNumber % myFrames.size() == 0) {
        thisFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
            BitmapFactory.decodeByteArray(thisFrame.bytes, 0, thisFrame.bytes.length));
      } else if ((frameNumber + 1) / myFrames.size() < repeatCount) {
        /* if (frameNumber <= 0 || frameNumber - 1 <= 0) {
             return;
         }*/
        MyFrame previousFrame = myFrames.get((frameNumber - 1) % myFrames.size());
        /**动画播放完毕后不能销毁bitmap，只需要将isReady置为false*/
        /* ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
         previousFrame.drawable = null;*/
        previousFrame.isReady = false;
      } else if ((frameNumber + 1) / myFrames.size() >= repeatCount) {
        MyFrame previousFrame = myFrames.get((frameNumber - 1) % myFrames.size());
        /**动画播放完毕后不能销毁bitmap，只需要将isReady置为false*/
        ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
        previousFrame.drawable = null;
        previousFrame.isReady = false;
      }
      if (imageView != null) { //动画资源的释放
        imageView.setBackground(thisFrame.drawable);
      } else {
        releaseFrameResourse(myFrames);
        if (onComplete != null) {
          onComplete.run();
        }
        return;
      }
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          // Make sure ImageView hasn't been changed to a different Image
          // in this time
          if (imageView.getBackground() == thisFrame.drawable) {
            if ((frameNumber + 1) / myFrames.size() < repeatCount) {
              MyFrame nextFrame = myFrames.get((frameNumber + 1) % myFrames.size());
              if (nextFrame.isReady) {
                // Animate next frame
                animateRawRepeat(myFrames, imageView, repeatCount, onComplete, frameNumber + 1);
              } else {
                nextFrame.isReady = true;
              }
            } else {
              if (onComplete != null) {
                onComplete.run();
              }
            }
          }
        }
      }, thisFrame.duration);

      // Load next frame
      if ((frameNumber + 1) / myFrames.size() <= repeatCount) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            MyFrame nextFrame = myFrames.get((frameNumber + 1) % myFrames.size());
            nextFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
                BitmapFactory.decodeByteArray(nextFrame.bytes, 0, nextFrame.bytes.length));
            if (nextFrame.isReady) {
              // Animate next frame
              animateRawRepeat(myFrames, imageView, repeatCount, onComplete, frameNumber + 1);
            } else {
              nextFrame.isReady = true;
            }
          }
        }).run();
      }

    } catch (Exception e) { //如果抛出异常，进行资源释放操作
      releaseFrameResourse(myFrames);
      if (onComplete != null) {
        onComplete.run();
      }
      YueJianAppTLog.error(e.getMessage());
    }
  }

  /**
   * 设置重复播放帧动画的方法，方法待完善
   * 由于不知道确切的播放完成时间，故bitmap的回收不及时
   *
   * @param myFrames
   * @param imageView
   * @param timeLong
   * @param onComplete
   * @param frameNumber
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  private static void animateRawRepeatSetTimeLong2(final List<MyFrame> myFrames,
      final ImageView imageView, final long timeLong, final long totalTime,
      final Runnable onComplete, final int frameNumber) {
    try {
      final MyFrame thisFrame = myFrames.get(frameNumber % myFrames.size());
      final int repeatCount = (int) (timeLong / totalTime) + 1; //计算动画播放的次数
      if (totalTime <= 0 && onComplete != null) { //如果时间已经结束，则停止动画的播放
        onComplete.run();
        new Thread() {
          @Override
          public void run() {
            super.run();
            for (int i = 0; i < myFrames.size(); i++) {
              MyFrame frame = myFrames.get(i);
              if (frame != null && frame.drawable != null) {
                ((BitmapDrawable) myFrames.get(i).drawable).getBitmap().recycle();
              }
            }
          }
        };
        return;
      }
      if (frameNumber % myFrames.size() == 0) {
        thisFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
            BitmapFactory.decodeByteArray(thisFrame.bytes, 0, thisFrame.bytes.length));
      } else if ((frameNumber + 1) / myFrames.size() < repeatCount) {
        /* if (frameNumber <= 0 || frameNumber - 1 <= 0) {
             return;
         }*/
        MyFrame previousFrame = myFrames.get((frameNumber - 1) % myFrames.size());

        /**动画播放完毕后不能销毁bitmap，只需要将isReady置为false*/
        /* ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
         previousFrame.drawable = null;*/
        previousFrame.isReady = false;
      } else if ((frameNumber + 1) / myFrames.size() >= repeatCount) {
        MyFrame previousFrame = myFrames.get((frameNumber - 1) % myFrames.size());
        /**动画播放完毕后不能销毁bitmap，只需要将isReady置为false*/
        ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
        previousFrame.drawable = null;
        previousFrame.isReady = false;
      }

      if (thisFrame.drawable == null) {
        return;
      }
      Bitmap bitmap1 = ((BitmapDrawable) thisFrame.drawable).getBitmap();
      if (bitmap1 == null)
        return;
      Bitmap bitmap2 = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap2);
      Matrix matrix = new Matrix();
      float[] values = {-1f, 0.0f, 0.0f, 0.0f, 1f, 0.0f, 0.0f, 0.0f, 1.0f};
      matrix.setValues(values);
      System.out.println("matrix:" + matrix.toString());
      final Bitmap dstbmp =
          Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
      canvas.drawBitmap(dstbmp, bitmap1.getWidth(), 0, null);
      imageView.setImageBitmap(dstbmp);

      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          // Make sure ImageView hasn't been changed to a different Image
          // in this time

          if ((frameNumber + 1) / myFrames.size() < repeatCount) {
            MyFrame nextFrame = myFrames.get((frameNumber + 1) % myFrames.size());
            if (nextFrame.isReady) {
              // Animate next frame
              animateRawRepeatSetTimeLong2(myFrames, imageView, repeatCount,
                  totalTime - thisFrame.duration, onComplete, frameNumber + 1);
            } else {
              nextFrame.isReady = true;
            }
          } else {
            if (onComplete != null) {
              onComplete.run();
            }
          }
        }
      }, thisFrame.duration);

      // Load next frame
      if ((frameNumber + 1) / myFrames.size() <= repeatCount) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            MyFrame nextFrame = myFrames.get((frameNumber + 1) % myFrames.size());
            nextFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
                BitmapFactory.decodeByteArray(nextFrame.bytes, 0, nextFrame.bytes.length));
            if (nextFrame.isReady) {
              // Animate next frame
              animateRawRepeat2(myFrames, imageView, repeatCount, onComplete, frameNumber + 1);
            } else {
              nextFrame.isReady = true;
            }
          }
        }).run();
      }

    } catch (Exception e) {
      YueJianAppTLog.error(e.getMessage());
    }
  }

  /**
   * 设置重复播放帧动画的方法，方法待完善
   * 由于不知道确切的播放完成时间，故bitmap的回收不及时
   *
   * @param myFrames
   * @param imageView
   * @param timeLong
   * @param onComplete
   * @param frameNumber
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  private static void animateRawRepeatSetTimeLong(final List<MyFrame> myFrames,
      final ImageView imageView, final long timeLong, final long totalTime,
      final Runnable onComplete, final int frameNumber) {
    try {
      final MyFrame thisFrame = myFrames.get(frameNumber % myFrames.size());
      final int repeatCount = (int) (timeLong / totalTime) + 1; //计算动画播放的次数
      if (totalTime <= 0 && onComplete != null) { //如果时间已经结束，则停止动画的播放
        onComplete.run();
        new Thread() {
          @Override
          public void run() {
            super.run();
            for (int i = 0; i < myFrames.size(); i++) {
              MyFrame frame = myFrames.get(i);
              frame.isReady = false;
              if (frame != null && frame.drawable != null) {
                ((BitmapDrawable) myFrames.get(i).drawable).getBitmap().recycle();
              }
            }
          }
        };
        return;
      }
      if (frameNumber % myFrames.size() == 0) {
        thisFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
            BitmapFactory.decodeByteArray(thisFrame.bytes, 0, thisFrame.bytes.length));
      } else if ((frameNumber + 1) / myFrames.size() < repeatCount) {
        /* if (frameNumber <= 0 || frameNumber - 1 <= 0) {
             return;
         }*/
        MyFrame previousFrame = myFrames.get((frameNumber - 1) % myFrames.size());

        /**动画播放完毕后不能销毁bitmap，只需要将isReady置为false*/
        /* ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
         previousFrame.drawable = null;*/
        previousFrame.isReady = false;
      } else if ((frameNumber + 1) / myFrames.size() >= repeatCount) {
        MyFrame previousFrame = myFrames.get((frameNumber - 1) % myFrames.size());
        /**动画播放完毕后不能销毁bitmap，只需要将isReady置为false*/
        ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
        previousFrame.drawable = null;
        previousFrame.isReady = false;
      }

      imageView.setBackground(thisFrame.drawable);
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          // Make sure ImageView hasn't been changed to a different Image
          // in this time
          if (imageView.getBackground() == thisFrame.drawable) {
            if ((frameNumber + 1) / myFrames.size() < repeatCount) {
              MyFrame nextFrame = myFrames.get((frameNumber + 1) % myFrames.size());
              if (nextFrame.isReady) {
                // Animate next frame
                animateRawRepeatSetTimeLong(myFrames, imageView, repeatCount,
                    totalTime - thisFrame.duration, onComplete, frameNumber + 1);
              } else {
                nextFrame.isReady = true;
              }
            } else {
              if (onComplete != null) {
                onComplete.run();
              }
            }
          }
        }
      }, thisFrame.duration);

      // Load next frame
      if ((frameNumber + 1) / myFrames.size() <= repeatCount) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            MyFrame nextFrame = myFrames.get((frameNumber + 1) % myFrames.size());
            nextFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
                BitmapFactory.decodeByteArray(nextFrame.bytes, 0, nextFrame.bytes.length));
            if (nextFrame.isReady) {
              // Animate next frame
              animateRawRepeat(myFrames, imageView, repeatCount, onComplete, frameNumber + 1);
            } else {
              nextFrame.isReady = true;
            }
          }
        }).run();
      }

    } catch (Exception e) {
      YueJianAppTLog.error(e.getMessage());
    }
  }

  /**
   * 释放动画资源
   */
  public static void releaseFrameResourse(final List<MyFrame> myFrames) {
    YueJianAppThreadManager.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < myFrames.size(); i++) {
          MyFrame myFrame = myFrames.get(i);
          if (myFrame != null) {
            if (myFrame.drawable == null)
              return;
            Bitmap bitmap = ((BitmapDrawable) myFrame.drawable).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
              bitmap.recycle();
              bitmap = null;
            }
            myFrame = null;
          }
        }
      }
    });
  }

  // 5
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  private static void animateRawManually(final List<MyFrame> myFrames, final ImageView imageView,
      final Runnable onComplete, final int frameNumber) {
    try {
      final MyFrame thisFrame = myFrames.get(frameNumber);
      boolean isAnimEnd = false;
      /**
       * 如果动画结束，则释放资源
       */
      if (isAnimEnd) {
        releaseFrameResourse(myFrames);
        if (onComplete != null) {
          onComplete.run();
        }
        return;
      }
      if (frameNumber == 0) {
        thisFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
            BitmapFactory.decodeByteArray(thisFrame.bytes, 0, thisFrame.bytes.length));
      } else {
        /* if (frameNumber <= 0 || frameNumber - 1 <= 0) {
             return;
         }*/
        MyFrame previousFrame = myFrames.get(frameNumber - 1);
        ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
        previousFrame.drawable = null;
        previousFrame.isReady = false;
      }

      imageView.setBackground(thisFrame.drawable);
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          // Make sure ImageView hasn't been changed to a different Image
          // in this time
          if (imageView.getBackground() == thisFrame.drawable) {
            if (frameNumber + 1 < myFrames.size()) {
              MyFrame nextFrame = myFrames.get(frameNumber + 1);

              if (nextFrame.isReady) {
                // Animate next frame
                animateRawManually(myFrames, imageView, onComplete, frameNumber + 1);
              } else {
                nextFrame.isReady = true;
              }
            } else {
              if (onComplete != null) {
                onComplete.run();
              }
            }
          }
        }
      }, thisFrame.duration);

      // Load next frame
      if (frameNumber + 1 < myFrames.size()) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            MyFrame nextFrame = myFrames.get(frameNumber + 1);
            nextFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
                BitmapFactory.decodeByteArray(nextFrame.bytes, 0, nextFrame.bytes.length));
            if (nextFrame.isReady) {
              // Animate next frame
              animateRawManually(myFrames, imageView, onComplete, frameNumber + 1);
            } else {
              nextFrame.isReady = true;
            }
          }
        }).run();
      }
    } catch (Exception e) {
      YueJianAppTLog.error(e.getMessage());
    }
  }

  //第二种方法

  /***
   * 代码中控制时间,但不精确
   * duration = 1000;
   ****/
  public static void animateManuallyFromRawResource(int animationDrawableResourceId,
      ImageView imageView, Runnable onStart, Runnable onComplete, int duration)
      throws IOException, XmlPullParserException {
    AnimationDrawable animationDrawable = new AnimationDrawable();

    XmlResourceParser parser =
        imageView.getContext().getResources().getXml(animationDrawableResourceId);

    int eventType = parser.getEventType();
    while (eventType != XmlPullParser.END_DOCUMENT) {
      if (eventType == XmlPullParser.START_DOCUMENT) {
      } else if (eventType == XmlPullParser.START_TAG) {
        if (parser.getName().equals("item")) {
          Drawable drawable = null;
          for (int i = 0; i < parser.getAttributeCount(); i++) {
            if (parser.getAttributeName(i).equals("drawable")) {
              int resId = Integer.parseInt(parser.getAttributeValue(i).substring(1));
              byte[] bytes =
                  YueJianAppIOUtils.toByteArray(imageView.getContext().getResources().openRawResource(
                      resId)); // YueJianAppIOUtils.readBytes
              drawable = new BitmapDrawable(imageView.getContext().getResources(),
                  BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            } else if (parser.getAttributeName(i).equals("duration")) {
              duration = parser.getAttributeIntValue(i, 66);
            }
          }

          animationDrawable.addFrame(drawable, duration);
        }

      } else if (eventType == XmlPullParser.END_TAG) {
      } else if (eventType == XmlPullParser.TEXT) {
      }

      eventType = parser.next();
    }

    if (onStart != null) {
      onStart.run();
    }
    animateDrawableManually(animationDrawable, imageView, onComplete, 0);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  private static void animateDrawableManually(final AnimationDrawable animationDrawable,
      final ImageView imageView, final Runnable onComplete, final int frameNumber) {
    try {
      final Drawable frame = animationDrawable.getFrame(frameNumber);
      imageView.setBackground(frame);
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          // Make sure ImageView hasn't been changed to a different Image
          // in this time
          if (imageView.getBackground() == frame) {
            if (frameNumber + 1 < animationDrawable.getNumberOfFrames()) {
              // Animate next frame
              animateDrawableManually(animationDrawable, imageView, onComplete, frameNumber + 1);
            } else {
              // Animation complete
              if (onComplete != null) {
                onComplete.run();
              }
            }
          }
        }
      }, animationDrawable.getDuration(frameNumber));
    } catch (Exception e) {
      YueJianAppTLog.error(e.getMessage());
    }
  }

  /**
   * 设置重复播放帧动画的方法,每次循环动画时都有监听
   *
   * @param myFrames
   * @param imageView
   * @param repeatCount
   * @param onComplete
   * @param frameNumber
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  private static void animateRawRepeatWithSingleCompleteListener(final List<MyFrame> myFrames,
      final ImageView imageView, final int repeatCount, final Runnable onComplete,
      final Runnable onSingleComplete, final int frameNumber) {
    try {
      final MyFrame thisFrame = myFrames.get(frameNumber % myFrames.size());
      if (frameNumber % myFrames.size() == 0) {
        if (null != onSingleComplete) {
          onSingleComplete.run();
        }
        thisFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
            BitmapFactory.decodeByteArray(thisFrame.bytes, 0, thisFrame.bytes.length));
      } else if ((frameNumber + 1) / myFrames.size() < repeatCount) {
        /* if (frameNumber <= 0 || frameNumber - 1 <= 0) {
             return;
         }*/
        MyFrame previousFrame = myFrames.get((frameNumber - 1) % myFrames.size());
        /**动画播放完毕后不能销毁bitmap，只需要将isReady置为false*/
        /* ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
         previousFrame.drawable = null;*/
        previousFrame.isReady = false;
      } else if ((frameNumber + 1) / myFrames.size() >= repeatCount) {
        MyFrame previousFrame = myFrames.get((frameNumber - 1) % myFrames.size());
        /**动画播放完毕后不能销毁bitmap，只需要将isReady置为false*/
        ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
        previousFrame.drawable = null;
        previousFrame.isReady = false;
      }
      if (imageView != null) { //动画资源的释放
        imageView.setBackground(thisFrame.drawable);
      } else {
        releaseFrameResourse(myFrames);
        if (onComplete != null) {
          onComplete.run();
        }
        return;
      }
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          // Make sure ImageView hasn't been changed to a different Image
          // in this time
          if (imageView.getBackground() == thisFrame.drawable) {
            if ((frameNumber + 1) / myFrames.size() < repeatCount) {
              MyFrame nextFrame = myFrames.get((frameNumber + 1) % myFrames.size());
              if (nextFrame.isReady) {
                // Animate next frame
                animateRawRepeatWithSingleCompleteListener(myFrames, imageView, repeatCount,
                    onSingleComplete, onComplete, frameNumber + 1);
              } else {
                nextFrame.isReady = true;
              }
            } else {
              if (onComplete != null) {
                onComplete.run();
              }
            }
          }
        }
      }, thisFrame.duration);

      // Load next frame
      if ((frameNumber + 1) / myFrames.size() <= repeatCount) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            MyFrame nextFrame = myFrames.get((frameNumber + 1) % myFrames.size());
            nextFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(),
                BitmapFactory.decodeByteArray(nextFrame.bytes, 0, nextFrame.bytes.length));
            if (nextFrame.isReady) {
              // Animate next frame
              animateRawRepeat(myFrames, imageView, repeatCount, onComplete, frameNumber + 1);
            } else {
              nextFrame.isReady = true;
            }
          }
        }).run();
      }

    } catch (Exception e) { //如果抛出异常，进行资源释放操作
      releaseFrameResourse(myFrames);
      if (onComplete != null) {
        onComplete.run();
      }
      YueJianAppTLog.error(e.getMessage());
    }
  }
}
