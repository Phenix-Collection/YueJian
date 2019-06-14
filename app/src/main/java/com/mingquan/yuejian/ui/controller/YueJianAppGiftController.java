package com.mingquan.yuejian.ui.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.getsentry.raven.event.Event;
import com.mingquan.yuejian.model.YueJianAppGiftItemModel;
import com.mingquan.yuejian.ui.view.YueJianAppGiftItem;
import com.mingquan.yuejian.utils.YueJianAppRavenUtils;

import java.lang.ref.WeakReference;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * YueJianAppGiftController , used for normal gift animation in PepperTV
 */

public class YueJianAppGiftController {
  private static final String TAG = YueJianAppGiftController.class.getSimpleName();
  private static YueJianAppGiftController mGiftController;
  private static Context mContext;
  private ViewGroup mGiftContainer;
  private static LinkedBlockingQueue<YueJianAppGiftItemModel> mGiftBlockingQueue;
  private static final int REMOVE_GIFT_ITEM = 0;
  private static final int GET_NEXT_GIFT_MODEL = 2;
  private static final int ADD_GIFT_ITEM = 4;
  private static UIHandler mHandler;
  private final YueJianAppGiftAnimController mGiftAnimController;

  private YueJianAppGiftController(Context context) {
    mContext = context;
    mGiftBlockingQueue = new LinkedBlockingQueue<>();
    mGiftAnimController = YueJianAppGiftAnimController.getInstance(context);
    mHandler = new UIHandler(this);
  }

  public static YueJianAppGiftController getInstance(Context context) {
    if (null == mGiftController) {
      synchronized (YueJianAppGiftController.class) {
        if (null == mGiftController) {
          mGiftController = new YueJianAppGiftController(context);
        }
      }
    }
    return mGiftController;
  }

  /**
   * 设置GiftContainer 礼物动画的容器
   *
   * @param giftContainer
   */
  public void setGiftContainer(ViewGroup giftContainer) {
    this.mGiftContainer = giftContainer;
  }

  /**
   * add YueJianAppGiftItemModel to the gift blocking queue
   *
   * @param giftItemModel
   */
  public static void addGiftItemMode(YueJianAppGiftItemModel giftItemModel) {
    try {
      mGiftBlockingQueue.put(giftItemModel);
      mHandler.sendEmptyMessage(GET_NEXT_GIFT_MODEL); //获取数据并播放动画
    } catch (InterruptedException e) {
      if (null != giftItemModel) {
        Log.e(TAG, giftItemModel.toString());
      }
      YueJianAppRavenUtils.captureMessage(
          mContext, e, YueJianAppRavenUtils.ERROR, YueJianAppRavenUtils.DEBUG, Event.Level.ERROR, null);
    }
  }

  private static class UIHandler extends Handler {
    private WeakReference<YueJianAppGiftController> mWeakReference;
    public UIHandler(YueJianAppGiftController weakReference) {
      this.mWeakReference = new WeakReference<>(weakReference);
    }

    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case REMOVE_GIFT_ITEM:

          break;
        case GET_NEXT_GIFT_MODEL:
          mWeakReference.get().takeGiftFromQueueAndPlayAnimation();
          break;
        case ADD_GIFT_ITEM:

          break;
      }
    }
  };

  /**
   * get gift from the blocking queue,and play animation
   */
  private void takeGiftFromQueueAndPlayAnimation() {
    YueJianAppGiftItemModel giftItemModel = mGiftBlockingQueue.poll();
    if (null == giftItemModel) {
      Log.e(TAG, "Oops, there is no gift in the gift blocking queue!");
      return;
    }

    int count = mGiftContainer.getChildCount();
    if (count
        >= 2) { //如果当前两个GiftItem的的数目都在更新，等待当前动画播放完成，或者移除数量不再更新的礼物动画GiftItem
      // 1.如果礼物和赠送者都相同，则找到相对应的GiftItem并更新其数量，做数量更新动画
      // 2.重新创建一个GiftItem并播放动画

    } else {
      addGiftItemView(giftItemModel);
    }
  }

  /**
   * create a YueJianAppGiftItem Object and add to the GiftContainer
   *
   * @param giftItemModel
   */
  private void addGiftItemView(YueJianAppGiftItemModel giftItemModel) {
    final YueJianAppGiftItem giftItem = new YueJianAppGiftItem(mContext);
    giftItem.setItemData(giftItemModel);
    mGiftContainer.addView(giftItem);

    mGiftAnimController.createCommonAnim(mContext);
    mGiftAnimController.startGiftInAnim(giftItem, new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        Log.e(TAG, "GiftInAnim start");
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        Log.e(TAG, "GiftInAnim onAnimationEnd");
        giftItem.startGiftAnim();
      }

      @Override
      public void onAnimationRepeat(Animation animation) {
        Log.e(TAG, "GiftInAnim onAnimationRepeat");
      }
    });
  }
}
