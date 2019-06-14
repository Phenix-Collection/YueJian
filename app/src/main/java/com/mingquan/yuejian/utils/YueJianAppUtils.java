package com.mingquan.yuejian.utils;

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by administrato on 2016/8/19.
 */
public class YueJianAppUtils {
    private static long lastClickTime;
    public static int tabsPosition = 0;

    private static Context context;

    private YueJianAppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        YueJianAppUtils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null)
            return context;
        throw new NullPointerException("u should init first");
    }


    /**
     * 防止在短时间内重复点击
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 600) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 状态，检测头像是否更换！如果更换，则重新虚化背景
     */
    public static boolean IS_IMG_CHANGED = false;
    public static boolean IS_IMG_FIRST_LOAD = true;

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1]\\d{10}";
        if (TextUtils.isEmpty(mobiles) || mobiles.length() != 11) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null)
            return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
