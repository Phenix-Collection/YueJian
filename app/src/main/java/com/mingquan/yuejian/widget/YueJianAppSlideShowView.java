package com.mingquan.yuejian.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseApplication;
import com.mingquan.yuejian.interf.YueJianAppIImageLoaderImpl;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class YueJianAppSlideShowView extends FrameLayout {
    private static final String TAG = "SlideshowView----->";
    // 使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader-1.8.6-with-sources.jar
    private YueJianAppIImageLoaderImpl mImageLoaderUtil;

    //轮播图图片数量
    private final static int IMAGE_COUNT = 2;
    //自动轮播的时间间隔
    private final static int INIT_DELAY_TIME = 1;
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private static boolean isAutoPlay = false;

    //自定义轮播图的资源
    private List<String> imageUrls;
    //跳转连接
    private List<String> imageJumps;
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;
    private List<String> mUrlList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    private Context context;
    // Handler
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem + 1);
        }

    };

    public YueJianAppSlideShowView(Context context) {
        this(context, null);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public YueJianAppSlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public YueJianAppSlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        mImageLoaderUtil = YueJianAppBaseApplication.getImageLoaderUtil();
        imageViewsList = new ArrayList<>();
        dotViewsList = new ArrayList<>();
    }

    /**
     * 设置图片源
     */
    public void setImageList(List<String> imageList) {
        imageUrls = imageList;
    }

    /**
     * 设置图片链接元
     */
    public void setUrlList(List<String> urlList) {
        imageJumps = urlList;
    }

    /**
     * 开始轮播图切换
     */
    public void startPlay() {
        stopPlay();
        initUI(context);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(
                new SlideShowTask(), INIT_DELAY_TIME, TIME_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 停止轮播图切换
     */
    private void stopPlay() {
        if (null != scheduledExecutorService) {
            scheduledExecutorService.shutdown();
        }
    }

    /**
     * 设置自动切换
     *
     * @param isAutoPlay
     */
    public void setIsAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    /**
     * 初始化Views等UI
     */
    private void initUI(final Context context) {
        if (imageUrls == null || imageUrls.size() == 0)
            return;
        LayoutInflater.from(context).inflate(R.layout.yue_jian_app_layout_slideshow, this, true);
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();
        if (dotViewsList != null) {
            dotViewsList.clear();
        }
        if (imageViewsList != null) {
            imageViewsList.clear();
        }
        // 热点个数与图片特殊相等
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView view = new ImageView(context);
            view.setTag(imageUrls.get(i));
            view.setScaleType(ScaleType.CENTER_CROP);
            imageViewsList.add(view);
            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 8;
            params.rightMargin = 8;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        viewPager.setCurrentItem(imageUrls.size() > 1 ? Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % imageUrls.size() : 0);
    }

    /**
     * 填充ViewPager的页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewsList.size() > 1 ? Integer.MAX_VALUE : imageViewsList.size();
        }

        @Override
        public Object instantiateItem(View container, final int position) {
            final int finalPosition = position % imageViewsList.size();
            ImageView imageView = imageViewsList.get(finalPosition);
            //点击跳转连接
            if (imageJumps != null && imageJumps.size() == imageUrls.size()) {
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YueJianAppUIHelper.showWebView(context, imageJumps.get(finalPosition), getResources().getString(R.string.app_name));
                    }
                });
            }
            String imgUrl = (String) imageView.getTag();
            mImageLoaderUtil.loadImageHighDefinite(
                    context,
                    imgUrl,
                    imageView,
                    null);
            ViewGroup viewParent = (ViewGroup) imageView.getParent();
            if (viewParent != null) {
                viewParent.removeView(imageView);
            }
            ((ViewGroup) container).addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1: // 手势滑动，空闲中
//                    isAutoPlay = false;
                    break;
                case 2: // 界面切换中
//                    isAutoPlay = true;
                    break;
                case 0: // 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
//                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
//                        viewPager.setCurrentItem(0);
//                    } else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) { // 当前为第一张，此时从左向右滑，则切换到最后一张
//                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
//                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int pos) {
            currentItem = pos;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos % imageViewsList.size()) {
                    dotViewsList.get(pos % dotViewsList.size()).setBackgroundResource(R.drawable.yue_jian_app_dotview_selected);
                } else {
                    dotViewsList.get(i).setBackgroundResource(R.drawable.yue_jian_app_dotview_plur);
                }
            }
        }
    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {
        @Override
        public void run() {
            synchronized (viewPager) {
//                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    /**
     * 销毁ImageView资源，回收内存
     */
    private void destoryBitmaps() {
        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }

}