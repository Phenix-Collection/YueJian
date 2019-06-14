package com.mingquan.yuejian;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.mingquan.yuejian.utils.YueJianAppTLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 */
public class YueJianAppAppConfig {
    public static final boolean DEBUG = false; // TODO 打包时 将DEBUG 修改为false即可
    public static final boolean LOG_DEBUG = true; // TODO 打包时 将DEBUG 修改为false即可

    //默认配置为线上
    public static String MAIN_URL = "https://api2.papatv.net/public/";
    public static String SIGN = "E^vQAA44U(r6aT7eNWU!HbSP)+ht+fK5!mhebhnJ3E@KddsCzW";
    public static String UPLOADAUTHINFO = "http://m.peppertv.cn/apps/authorize/aliauth";

    static {
        YueJianAppTLog.info("DEBUG:%s", DEBUG);
        if (DEBUG) { //线下 测试
            MAIN_URL = "http://test.vchat.peppertv.cn/public/";
            SIGN = "UC@cgbQt9yCQKKDNJWsuaXpbGBqd8ofowNQnrHhWmRYzgkobFE";
            UPLOADAUTHINFO = "http://m.test.tv.lmx0536.cn/apps/authorize/aliauth";
        } else { //线上配置 正式版
            MAIN_URL = "https://api2.papatv.net/public/";
            SIGN = "E^vQAA44U(r6aT7eNWU!HbSP)+ht+fK5!mhebhnJ3E@KddsCzW";
            UPLOADAUTHINFO = "http://m.peppertv.cn/apps/authorize/aliauth";
        }
    }

    private final static String APP_CONFIG = "config";
    public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";
    public static final String KEY_TWEET_DRAFT = "KEY_TWEET_DRAFT";
    public static final String KEY_NOTE_DRAFT = "KEY_NOTE_DRAFT";
    public static final String KEY_FIRST_START = "KEY_FIRST_START";
    public static final String MobKey = "24ee9070f4ff6";
    public static final String MobSecret = "31b27d91132024556a04340230996e9a";
//  public static final String MobKey = "228de3da33bd0";
//  public static final String MobSecret = "a37a1e4933f8fc56d2c1d848ff05daff";
    public static final String WCHAT_APP_ID = "wx3a4a3e4c8501bbee";
    public static final String WCHAT_APP_SECRET = "59ffa85c4e9df5b4818f6782df2a6f0c";
//    public static final String WCHAT_APP_ID = "wx3a4a3e4c8501bbee";
//    public static final String WCHAT_APP_SECRET = "efa2f6e1d806cbd9b8e2aaad401d6bca";
    public static final String TALKING_DATA_ID = "A0117081AA2A4A13B8A79B61A59290A2";
    public static final String HAS_ALERT_NETWORK_CHANGE = "has_alert_network_change";
    public static final String ACTION_LOGOUT = "com.mingquan.yuejian.logout";
    public static final String ACTION_RECHARGE_DIAMOND = "com.mingquan.yuejian.recharge.diamond";
    public static final String ACTION_PRIVATE_MESSAGE = "com.mingquan.yuejian.message";
    public static final String ACTION_REFRESH_BROADCASTER_LIST = "refresh_broadcaster_list";
    public static final String ACTION_CLOSE_ALL_DIALOG_FRAGMENT = "com.mingquan.yuejian.close.all_dialog_fragment";
    public static final String ACTION_CLOSE_OTHER_INFO_DIALOG_FRAGMENT = "com.mingquan.yuejian.close.other_info_dialog_fragment";
    public static final String ACTION_VIP_CHAT_START = "vip.chat.start";
    public static final String ACTION_VIP_CHAT_RESULT = "vip.chat.result";
    public static final String ACTION_VIP_CHAT_DECLINE = "vip.chat.Decline";
    public static final String ACTION_VIP_CHAT_CANCEL = "vip.chat.cancel";
    public static final String ACTION_VIP_CHAT_PING = "vip.chat.ping";
    public static final String ACTION_VIP_CHAT_CONNECT = "vip.chat.connect";
    public static final String ACTION_ENTER_ROOM = "vip.chat.enter.room";
    public static final String ACTION_SEND_GIFT = "send.gift";

    public static final String IM_ACCOUNT = YueJianAppAppConfig.DEBUG ? "pt" : "formal"; // 注册环信账号的前缀
    public static final String BUGLY_APP_ID = "c72765c0b7"; // bugly appid
    public static final String HUA_WEI_APP_ID = "100476819"; // 华为appid

    /**
     * Dialog的类型.
     */
    public static final int DIALOG_PROGRESS = 0;

    // 默认存放图片的路径
    public final static String APP_ROOT_DIR =
            Environment.getExternalStorageDirectory() + File.separator + "yue_jian" + File.separator;

    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = APP_ROOT_DIR + "live_img" + File.separator;
    /**
     * 默认缓存图片的路径
     */
    public final static String DEFAULT_CACHE_IMAGE_PATH = APP_ROOT_DIR + "cache_image";

    public final static String DEFAULT_CACHE_PATH = APP_ROOT_DIR + "cache_data";

    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = APP_ROOT_DIR + "download" + File.separator;

    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_GIFT_PATH =
            APP_ROOT_DIR + "download" + File.separator + "gift" + File.separator;

    public final static String DEFAULT_SAVE_USER_INFO_PATH = APP_ROOT_DIR + "user" + File.separator;
    private Context mContext;
    private static YueJianAppAppConfig appConfig;

    public static YueJianAppAppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            synchronized (YueJianAppAppConfig.class) {
                if (null == appConfig) {
                    appConfig = new YueJianAppAppConfig();
                    appConfig.mContext = context;
                }
            }
        }
        return appConfig;
    }

    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取files目录下的config
            // fis = activity.openFileInput(APP_CONFIG);
            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在files目录下
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public void set(Properties ps) {
        Properties props = get();

        props.putAll(ps);
        setProps(props);
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        if (null == props || key == null)
            return;
        for (String k : key) props.remove(k);
        setProps(props);
    }

    /**
     * 友盟统计
     */
    public static final String EVENT_BROACAST_TIME = "EVENT_BROACAST_TIME";
    public static final String EVENT_FOLLOWING = "EVENT_FOLLOWING";
    public static final String EVENT_BANNED = "EVENT_BANNED";
    public static final String EVENT_SHARE = "EVENT_SHARE";
    public static final String EVENT_SEND_GIFT = "EVENT_SEND_GIFT";
    public static final String EVENT_RECHARGE = "EVENT_RECHARGE";
    public static final String EVENT_REPORT = "EVENT_REPORT";
    public static final String EVENT_EXCHANGE = "EVENT_EXCHANGE";
}
