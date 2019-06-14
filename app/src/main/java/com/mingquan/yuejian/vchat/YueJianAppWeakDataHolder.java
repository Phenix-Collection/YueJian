package com.mingquan.yuejian.vchat;

import com.mingquan.yuejian.proto.model.YueJianAppACVideoInfoModel;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Intent传递大量数据出现TransactionTooLargeException异常的解决方案
 */

public class YueJianAppWeakDataHolder {

    private static YueJianAppWeakDataHolder instance;

    public static YueJianAppWeakDataHolder getInstance() {
        if (instance == null) {
            synchronized (YueJianAppWeakDataHolder.class) {
                if (instance == null) {
                    instance = new YueJianAppWeakDataHolder();
                }
            }
        }
        return instance;
    }

    private Map<String, WeakReference<List<YueJianAppACVideoInfoModel>>> map = new HashMap<>();

    /**
     * 数据存储
     *
     * @param object
     */
    public void saveData(List<YueJianAppACVideoInfoModel> object) {
        map.put("VIDEO_INFO_LIST", new WeakReference<>(object));
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<YueJianAppACVideoInfoModel> getData() {
        WeakReference<List<YueJianAppACVideoInfoModel>> weakReference = map.get("VIDEO_INFO_LIST");
        return weakReference.get();
    }
}