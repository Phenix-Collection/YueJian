package com.mingquan.yuejian.huawei.common;

/**
 * 工具类
 */
public final class YueJianAppStrUtils {
    /**
     * 返回对象的描述，这里为了避免用户数据隐私的泄露，只是返回对象本身的描述 类名@hashcode
     * @param object 对象
     * @return 对象的描述
     */
    public static String objDesc(Object object) {
        return object == null ? "null" : (object.getClass().getName()+'@'+ Integer.toHexString(object.hashCode()));
    }
}
