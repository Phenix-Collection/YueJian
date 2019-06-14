package com.mingquan.yuejian.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getsentry.raven.event.Event;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.bean.YueJianAppSendGiftBean;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;
import com.mingquan.yuejian.widget.YueJianAppLoadUrlImageView;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhongxf
 * @Description 礼物显示的管理类
 * @Date 2016/6/6.
 * 主要礼物逻辑：利用一个LinkedBlockingQueue来存储所有的礼物的实体类。然后利用Handler的消息机制，每隔一段时间从队列中取一次礼物出来
 * 如果取得礼物为空（队列中没有礼物），那么就延迟一段时间之后再次从队列中取出礼物
 * 如果从队列中取出的礼物不为空，则根据送礼物的人的UserId去寻找这个礼物是否正在显示，如果不在显示，则新建一个，如果正在显示，则直接修改数量
 * <p/>
 * 这个礼物View的管理类中一直存在一个定时器在沦陷礼物的容器下面的所有的礼物的View，当有礼物的View上次的更新时间超过最长显示时间，那么久就移除这个View
 * <p/>
 * 6/7实现：礼物容器中显示的礼物达到两条，并且新获取的礼物和他们两个不一样，那么需要移除一个来显示新的礼物
 * 判断所有的里面的出现的时间，然后把显示最久的先移除掉（需要考虑到线程安全）
 * <p>
 * 6/7实现：定时器的线程会更新View，在获取礼物的时候也会更新View（增加线程安全控制）
 */
@SuppressWarnings("ALL")
public class YueJianAppGiftShowManager {
  private LinkedBlockingQueue<YueJianAppSendGiftBean> queue; //礼物的队列
  private LinearLayout giftCon; //礼物的容器
  private Context cxt; //上下文

  private TranslateAnimation inAnim; //礼物View出现的动画
  private TranslateAnimation outAnim; //礼物View消失的动画
  private TranslateAnimation iconAnim; //礼物View消失的动画
  private ScaleAnimation giftNumAnim; //修改礼物数量的动画

  private final static int SHOW_GIFT_FLAG = 1; //显示礼物
  private final static int GET_QUEUE_GIFT = 0; //从队列中获取礼物
  private final static int REMOVE_GIFT_VIEW = 2; //当礼物的View显示超时，删除礼物View

  private Timer timer; //轮询礼物容器的所有的子View判断是否超过显示的最长时间
  Typeface tfs = null;

  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      YueJianAppTLog.error("msg.what:" + msg.what);
      switch (msg.what) {
        case SHOW_GIFT_FLAG: //如果是处理显示礼物的消息
          YueJianAppSendGiftBean showVo = (YueJianAppSendGiftBean) msg.obj;
          if (showVo == null)
            return;
          String userId = showVo.getUid() + "_" + showVo.getGiftid();
          int num = showVo.getGiftcount();
          View giftView = giftCon.findViewWithTag(userId);
          giftNumAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
              handler.sendEmptyMessage(GET_QUEUE_GIFT);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
          });
          if (giftView == null) { //获取的礼物的实体，判断送的人不在显示
            //首先需要判断下Gift ViewGroup下面的子View是否超过两个
            int count = giftCon.getChildCount();
            if (count >= 2) { //如果正在显示的礼物的个数超过两个，那么就移除最后一次更新时间比较长的
              View giftView1 = giftCon.getChildAt(0);
              TextView nameTv1 = (TextView) giftView1.findViewById(R.id.tv_gift_uname);
              long lastTime1 = (long) nameTv1.getTag();
              View giftView2 = giftCon.getChildAt(1);
              TextView nameTv2 = (TextView) giftView2.findViewById(R.id.tv_gift_uname);
              long lastTime2 = (long) nameTv2.getTag();
              Message rmMsg = new Message();
              if (lastTime1 > lastTime2) { //如果第二个View显示的时间比较长
                rmMsg.obj = 1;
              } else { //如果第一个View显示的时间长
                rmMsg.obj = 0;
              }
              rmMsg.what = REMOVE_GIFT_VIEW;
              handler.sendMessage(rmMsg);
            }
            //获取礼物的View的布局
            giftView = LayoutInflater.from(cxt).inflate(R.layout.yue_jian_app_gift_item, null);
            giftView.setTag(userId);
            //显示礼物的数量
            final TextView giftNum = (TextView) giftView.findViewById(R.id.tv_gift_num);
            initTyps(giftNum);
            giftNum.setTag(num);
            giftNum.setText("X" + num);

            TextView tv = (TextView) giftView.findViewById(R.id.tv_gift_uname);
            tv.setText(showVo.getNicename());
            TextView gtv = (TextView) giftView.findViewById(R.id.tv_gift_gname);
            gtv.setText("送了 " + showVo.getGiftname());
            final YueJianAppLoadUrlImageView icon =
                (YueJianAppLoadUrlImageView) giftView.findViewById(R.id.av_gift_icon);
            icon.setImageLoadUrl(showVo.getGifticon());
            ((YueJianAppAvatarView) giftView.findViewById(R.id.av_gift_uhead))
                .setAvatarUrl(showVo.getAvatar());
            tv.setTag(System.currentTimeMillis());
            //将礼物的View添加到礼物的ViewGroup中
            giftCon.addView(giftView);
            if (giftView != null) {
              giftView.startAnimation(inAnim); //播放礼物View出现的动画
            }
            inAnim.setAnimationListener(new Animation.AnimationListener() {
              @Override
              public void onAnimationStart(Animation animation) {}

              @Override
              public void onAnimationEnd(Animation animation) {
                icon.startAnimation(iconAnim);
                giftNum.startAnimation(giftNumAnim);
                showGiftNumAnim(giftNum);
              }

              @Override
              public void onAnimationRepeat(Animation animation) {}
            });
          } else { //如果送的礼物正在显示（只是修改下数量）
            //显示礼物的数量
            final TextView giftNum = (TextView) giftView.findViewById(R.id.tv_gift_num);
            initTyps(giftNum);
            int showNum = (int) giftNum.getTag() + num;
            giftNum.setText("X" + (showNum));
            giftNum.setTag(showNum);
            TextView tv = (TextView) giftView.findViewById(R.id.tv_gift_uname);
            tv.setTag(System.currentTimeMillis());
            //            giftNum.startAnimation(giftNumAnim);
            showGiftNumAnim(giftNum);
          }
          break;
        case GET_QUEUE_GIFT: //如果是从队列中获取礼物实体的消息
          YueJianAppSendGiftBean vo = queue.poll();
          if (vo != null) { //如果从队列中获取的礼物不为空，那么就将礼物展示在界面上
            Message giftMsg = new Message();
            giftMsg.obj = vo;
            giftMsg.what = SHOW_GIFT_FLAG;
            handler.sendMessage(giftMsg);
          } else {
            handler.sendEmptyMessageDelayed(
                GET_QUEUE_GIFT, 1000); //如果这次从队列中获取的消息是礼物是空的，则一秒之后重新获取
          }
          isEnd = false;
          queue.remove(vo);
          break;

        case REMOVE_GIFT_VIEW:
          final int index = (int) msg.obj;
          View removeView = giftCon.getChildAt(index);
          outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
              if (giftCon != null) {
                giftCon.removeViewAt(index);
              }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
          });
          if (removeView != null)
            removeView.startAnimation(outAnim);
          break;
        default:
          break;
      }
    }
  };

  private void initTyps(TextView giftNum) {
    if (tfs == null) {
      tfs = Typeface.createFromAsset(cxt.getAssets(), "fonts/dinnot.ttf");
    }
    if (tfs != null && giftNum != null) {
      giftNum.setTypeface(tfs);
    }
  }

  private void showGiftNumAnim(View v) {
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 4.0f, 0.7f, 1.1f, 1f);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 4.0f, 0.7f, 1.1f, 1f);
    ObjectAnimator translateX = ObjectAnimator.ofFloat(v, "TranslationX", 1f, 180f);

    AnimatorSet animatorSet = new AnimatorSet();
    AnimatorSet animatorSet2 = new AnimatorSet();
    animatorSet.playTogether(scaleX, scaleY);

    animatorSet2.play(translateX);
    animatorSet2.setDuration(350);
    animatorSet2.start();
    animatorSet.setDuration(700);
    animatorSet.start();
  }

  ScheduledExecutorService executors = null;

  public YueJianAppGiftShowManager(Context cxt, final LinearLayout giftCon) {
    this.cxt = cxt;
    this.giftCon = giftCon;
    queue = new LinkedBlockingQueue<YueJianAppSendGiftBean>();
    inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(cxt, R.anim.yue_jian_app_anim_gift_in);
    outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(cxt, R.anim.yue_jian_app_anim_gift_out);
    iconAnim = (TranslateAnimation) AnimationUtils.loadAnimation(cxt, R.anim.yue_jian_app_anim_gift_icon);
    iconAnim.setFillAfter(true);
    giftNumAnim = (ScaleAnimation) AnimationUtils.loadAnimation(cxt, R.anim.yue_jian_app_anim_gift_num);

    if (executors == null) {
      executors = Executors.newScheduledThreadPool(5);
    }
    executors.scheduleWithFixedDelay(new Runnable() {
      @Override
      public void run() {
        int count = giftCon.getChildCount();
        for (int i = 0; i < count; i++) {
          View view = giftCon.getChildAt(i);
          TextView name = (TextView) view.findViewById(R.id.tv_gift_uname);
          long nowtime = System.currentTimeMillis();
          long upTime = (long) name.getTag();
          if ((nowtime - upTime) >= 4000) {
            Message msg = new Message();
            msg.obj = i;
            msg.what = REMOVE_GIFT_VIEW;
            handler.sendMessage(msg);
          }
        }
      }
    }, 1, 1, TimeUnit.SECONDS);
  }

  private boolean isEnd = true;

  //开始显示礼物
  public void showGift() {
    if (isEnd) {
      handler.sendEmptyMessage(GET_QUEUE_GIFT); //轮询队列获取礼物
    }
    isEnd = false;
  }

  //放入礼物到队列
  /*public boolean addGift(YueJianAppSendGiftBean vo) {
      boolean add = false;
      try {
          add = queue.add(vo);
      } catch (Exception e) {
          YueJianAppRavenUtils.captureMessage(cxt, e, YueJianAppRavenUtils.ERROR, YueJianAppRavenUtils.DEBUG, Event.Level.ERROR,
  null);
      }
      return add;
  }*/

  public void addGift(YueJianAppSendGiftBean vo) {
    try {
      queue.put(vo);
    } catch (Exception e) {
      YueJianAppRavenUtils.captureMessage(
          cxt, e, YueJianAppRavenUtils.ERROR, YueJianAppRavenUtils.DEBUG, Event.Level.ERROR, null);
    }
  }
}
