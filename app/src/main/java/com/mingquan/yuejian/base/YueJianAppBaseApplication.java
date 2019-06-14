package com.mingquan.yuejian.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppConst;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.interf.YueJianAppIImageLoaderImpl;
import com.mingquan.yuejian.utils.YueJianAppImageLoaderUtil;
import com.mingquan.yuejian.utils.network.YueJianAppNetObserver;
import com.mingquan.yuejian.utils.network.YueJianAppINetStatusReceiver;
import com.mingquan.yuejian.utils.network.YueJianAppNetType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class YueJianAppBaseApplication extends MultiDexApplication {
  private static String PREF_NAME = "huanlejiao.pref";
  private static final String TAG = YueJianAppBaseApplication.class.getSimpleName();
  static Context _context;
  static Resources _resource;
  private static String lastToast = "";
  private static long lastToastTime;
  private static YueJianAppIImageLoaderImpl mImageLoaderUtil;
  public static YueJianAppNetType sNetType;
  private YueJianAppAppConst mAppConst;

  private final static int MIN_HEAP_SIZE = 512 * 1024 * 1024;

  @Override
  public void onCreate() {
    super.onCreate();
    setMinHeapSize(MIN_HEAP_SIZE);
    //设置最小heap内存为6MB大小
    _context = getApplicationContext();
    _resource = _context.getResources();

    mImageLoaderUtil = YueJianAppImageLoaderUtil.getInstance(getApplicationContext());
    mAppConst = YueJianAppAppConst.getInstance();
    mAppConst.init(_context);

    YueJianAppNetObserver.registerlGlobalReceiver(new YueJianAppINetStatusReceiver() {
      @Override
      public void netStatusChanged(YueJianAppNetType netType) {
        Log.w(TAG,
            "***********************  current network status  *********************" + netType);
        sNetType = netType;
      }
    });
  }

  public static void setMinHeapSize(long size) {
    try {
      Class<?> cls = Class.forName("dalvik.system.VMRuntime");
      Method getRuntime = cls.getMethod("getRuntime");
      Object obj = getRuntime.invoke(null); // obj就是Runtime
      if (obj == null) {
        System.err.println("obj is null");
      } else {
        System.out.println(obj.getClass().getName());
        Class<?> runtimeClass = obj.getClass();
        Method setMinimumHeapSize = runtimeClass.getMethod("setMinimumHeapSize", long.class);
        setMinimumHeapSize.invoke(obj, size);
      }

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  public static synchronized YueJianAppBaseApplication context() {
    return (YueJianAppBaseApplication) _context;
  }

  public static Resources resources() {
    return _resource;
  }

  /**
   * 获取图片加载工具类
   *
   * @return
   */
  public static YueJianAppIImageLoaderImpl getImageLoaderUtil() {
    return mImageLoaderUtil;
  }

  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
  public static void apply(SharedPreferences.Editor editor) {
      editor.apply();
  }

  public static void set(String key, int value) {
    SharedPreferences.Editor editor = getPreferences().edit();
    editor.putInt(key, value);
    apply(editor);
  }

  public static void set(String key, boolean value) {
    SharedPreferences.Editor editor = getPreferences().edit();
    editor.putBoolean(key, value);
    apply(editor);
  }

  public static void set(String key, String value) {
    SharedPreferences.Editor editor = getPreferences().edit();
    editor.putString(key, value);
    apply(editor);
  }

  public static boolean get(String key, boolean defValue) {
    return getPreferences().getBoolean(key, defValue);
  }

  public static String get(String key, String defValue) {
    return getPreferences().getString(key, defValue);
  }

  public static int get(String key, int defValue) {
    return getPreferences().getInt(key, defValue);
  }

  public static long get(String key, long defValue) {
    return getPreferences().getLong(key, defValue);
  }

  public static float get(String key, float defValue) {
    return getPreferences().getFloat(key, defValue);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static SharedPreferences getPreferences() {
    SharedPreferences pre = context().getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS);
    return pre;
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public static SharedPreferences getPreferences(String prefName) {
    return context().getSharedPreferences(prefName, Context.MODE_MULTI_PROCESS);
  }

  public static String string(int id) {
    return _resource.getString(id);
  }

  public static String string(int id, Object... args) {
    return _resource.getString(id, args);
  }

  public static void showToast(int message) {
    showToast(message, Toast.LENGTH_LONG, 0);
  }

  public static void showToast(String message) {
    showToast(message, Toast.LENGTH_LONG, 0, Gravity.BOTTOM);
  }

  public static void showToast(int message, int icon) {
    showToast(message, Toast.LENGTH_LONG, icon);
  }

  public static void showToast(String message, int icon) {
    showToast(message, Toast.LENGTH_LONG, icon, Gravity.BOTTOM);
  }

  public static void showToastShort(int message) {
    showToast(message, Toast.LENGTH_SHORT, 0);
  }

  public static void showToastShort(String message) {
    showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
  }

  public static void showToastShort(String message, int gravity) {
    showToast(message, Toast.LENGTH_SHORT, 0, gravity);
  }

  public static void showToastShort(int message, Object... args) {
    showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM, args);
  }

  public static void showToast(int message, int duration, int icon) {
    showToast(message, duration, icon, Gravity.BOTTOM);
  }

  public static void showCenterToast(int message) {
    showToast(message, Toast.LENGTH_LONG, 0, Gravity.CENTER);
  }

  public static void showToast(int message, int duration, int icon, int gravity) {
    showToast(context().getString(message), duration, icon, gravity);
  }

  public static void showToast(int message, int duration, int icon, int gravity, Object... args) {
    showToast(context().getString(message, args), duration, icon, gravity);
  }

  public static void showToast(String message, int duration, int icon, int gravity) {
    if (message != null && !message.equalsIgnoreCase("")) {
      long time = System.currentTimeMillis();
      if (!message.equalsIgnoreCase(lastToast) || Math.abs(time - lastToastTime) > 2000) {
        View view = LayoutInflater.from(context()).inflate(R.layout.yue_jian_app_view_toast, null);
        ((TextView) view.findViewById(R.id.title_tv)).setText(message);
        if (icon != 0) {
          ((ImageView) view.findViewById(R.id.icon_iv)).setImageResource(icon);
          ((ImageView) view.findViewById(R.id.icon_iv)).setVisibility(View.VISIBLE);
        }
        Toast toast = new Toast(context());
        int xOffSet = 0;
        int yOffset = 0;
        if (gravity == Gravity.CENTER) {
          yOffset = -200;
        }
        toast.setView(view);
        toast.setGravity(gravity, xOffSet, yOffset);
        toast.setDuration(duration);
        toast.show();
        lastToast = message;
        lastToastTime = System.currentTimeMillis();
      }
    }
  }
}
