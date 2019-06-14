package com.mingquan.yuejian.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.interf.YueJianAppIImageLoaderImpl;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.concurrent.Executors;

/**
 * 继承了ImageLoaderImpl，使用单例，初始化在BaseApplication中
 */

public class YueJianAppImageLoaderUtil implements YueJianAppIImageLoaderImpl {
    private static YueJianAppImageLoaderUtil instance;
    private DisplayImageOptions userHeadBigOptions;
    private DisplayImageOptions mHighDefiniteOption; //高分辨率保存图片 无失真
    private DisplayImageOptions userHeadOptions;
    private static final String RES_DRAWABLE = "drawable://";

    public static synchronized YueJianAppImageLoaderUtil getInstance(Context context) {
        if (instance == null)
            instance = new YueJianAppImageLoaderUtil(context);
        return instance;
    }

    private YueJianAppImageLoaderUtil(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "phoneLive/image/cache");
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(context)
                        .memoryCacheExtraOptions(480, 480)
                        // 设定缓存在内存的图片大小最大为200x200
                        // max width, max height，即保存的每个缓存文件的最大长宽
                        .threadPoolSize(3)
                        .taskExecutor(Executors.newFixedThreadPool(3))
                        // AsyncTask.THREAD_POOL_EXECUTOR)
                        // .taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
                        // 线程池内加载的数量
                        .threadPriority(Thread.NORM_PRIORITY - 2)
                        .denyCacheImageMultipleSizesInMemory()
                        //                .memoryCache(new LruMemoryCache(6 * 1024 * 1024))
                        .memoryCache(new WeakMemoryCache())
                        // You can pass your own memory cache
                        // implementation/你可以通过自己的内存缓存实现
                        .memoryCacheSize(6 * 1024 * 1024)
                        .diskCache(new UnlimitedDiskCache(cacheDir))
                        //            .diskCacheFileCount(200)
                        .diskCacheSize(50 * 1024 * 1024)
                        .diskCacheExtraOptions(480, 480, null)
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // 缓存的文件数量
                        .denyCacheImageMultipleSizesInMemory()
                        // 超过2分钟的内存缓存都会被删除
                        //                .diskCache(new LimitedAgeDiskCache(cacheDir, 1000 * 60 * 3))
                        // 自定义缓存路径
                        .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        .imageDownloader(new BaseImageDownloader(
                                context, 5 * 1000, 30 * 1000)) // connectTimeout(5s),readTimeout(30s)超时时间
                        // .writeDebugLogs() // Remove for release app
                        .build(); // 开始构建
        ImageLoader.getInstance().init(config);
        DisplayImageOptions smallOptions = new DisplayImageOptions
                .Builder()
                // .showImageOnLoading(R.drawable.yue_jian_app_bg_def_small) // 设置图片在下载期间显示的图片
                // .showImageForEmptyUri(R.drawable.yue_jian_app_bg_def_small)//
                // 设置图片Uri为空或是错误的时候显示的图片
                // .showImageOnFail(R.drawable.yue_jian_app_bg_def_small) //
                // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) // 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型//
                .decodingOptions(new BitmapFactory.Options()) // 设置图片的解码配置
                .resetViewBeforeLoading(false) // 设置图片在下载前是否重置，复位
                .build();
        userHeadBigOptions =
                new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.yue_jian_app_logo) // 设置图片在下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.yue_jian_app_logo) //
                        // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.yue_jian_app_logo) // 设置图片加载/解码过程中错误时候显示的图片
                        .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                        .cacheOnDisk(false) // 设置下载的图片是否缓存在SD卡中
                        .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                        .imageScaleType(ImageScaleType.IN_SAMPLE_INT) // 设置图片以如何的编码方式显示
                        //                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//
                        //                设置图片以如何的编码方式显示
                        .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型//
                        .decodingOptions(new BitmapFactory.Options()) // 设置图片的解码配置
                        .resetViewBeforeLoading(true) // 设置图片在下载前是否重置，复位
                        .build();
        mHighDefiniteOption =
                new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.yue_jian_app_logo) // 设置图片在下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.yue_jian_app_logo) //
                        // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.yue_jian_app_logo) // 设置图片加载/解码过程中错误时候显示的图片
                        .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                        .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                        .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                        .imageScaleType(ImageScaleType.IN_SAMPLE_INT) // 设置图片以如何的编码方式显示
                        .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型//
                        .decodingOptions(new BitmapFactory.Options()) // 设置图片的解码配置
                        .displayer(new FadeInBitmapDisplayer(300))
                        .resetViewBeforeLoading(true) // 设置图片在下载前是否重置，复位
                        .build();
        userHeadOptions =
                new DisplayImageOptions.Builder()
                        .cloneFrom(smallOptions)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .showImageOnLoading(R.drawable.yue_jian_app_default_head) // 设置图片在下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.yue_jian_app_default_head) //
                        // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.yue_jian_app_default_head) // 设置图片加载/解码过程中错误时候显示的图片
                        .build();
    }

    /**
     * 加载用户头像 图片格式为有损压缩RGB-565 可以添加图片加载完成监听
     */
    @Override
    public void loadImageUserHeadBig(Context context, String url, ImageView iv) {
        if (url != null && !url.equals("")) {
            Bitmap b = ImageLoader.getInstance().getMemoryCache().get(url);
            if (b == null)
                ImageLoader.getInstance().displayImage(
                        url, iv, userHeadBigOptions); //可以设置加载完成时的监听
            else
                iv.setImageBitmap(b);
        } else
            ImageLoader.getInstance().displayImage(RES_DRAWABLE + R.drawable.yue_jian_app_logo, iv);
    }

    /**
     * 加载用户头像 适用小图 图片格式有损压缩RGB-565
     */
    @Override
    public void loadImageUserHead(Context context, String url, ImageView iv) {
        if (url != null && !url.equals("")) {
            Bitmap b = ImageLoader.getInstance().getMemoryCache().get(url);
            if (b == null) {
                ImageLoader.getInstance().displayImage(url, iv, userHeadOptions);
            } else {
                iv.setImageBitmap(b);
            }
        } else
            ImageLoader.getInstance().displayImage(RES_DRAWABLE + R.drawable.yue_jian_app_default_nouser, iv);
    }

    /**
     * 加载高清图片 图片未压缩 使用ARGB-8888格式
     */
    @Override
    public void loadImageHighDefinite(Context context, String url, ImageView iv) {
        if (url != null && !url.equals("")) {
            Bitmap b = ImageLoader.getInstance().getMemoryCache().get(url);
            if (b == null) {
                ImageLoader.getInstance().displayImage(url, iv, mHighDefiniteOption);
            } else {
                iv.setImageBitmap(b);
            }
        }
    }

    @Override
    public void loadImageHighDefinite(
            Context context, String url, ImageView iv, ImageLoadingListener listener) {
        if (url != null && !url.equals("")) {
            Bitmap b = ImageLoader.getInstance().getMemoryCache().get(url);
            if (b == null) {
                ImageLoader.getInstance().displayImage(url, iv, mHighDefiniteOption, listener); //可以设置加载完成时的监听
            } else {
                iv.setImageBitmap(b);
            }
        } else {
            ImageLoader.getInstance().displayImage(RES_DRAWABLE + R.drawable.yue_jian_app_logo, iv);
        }
    }

    @Override
    public void loadImage_glide(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).into(iv);
    }
}
