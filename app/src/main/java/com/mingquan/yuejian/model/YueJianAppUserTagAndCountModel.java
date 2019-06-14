package com.mingquan.yuejian.model;

import com.mingquan.yuejian.proto.model.YueJianAppACUserTagMetaDataModel;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/29
 */

public class YueJianAppUserTagAndCountModel implements Serializable {
    private YueJianAppACUserTagMetaDataModel metaDataModel;
    private int count;

    public YueJianAppACUserTagMetaDataModel getMetaDataModel() {
        return metaDataModel;
    }

    public void setMetaDataModel(YueJianAppACUserTagMetaDataModel metaDataModel) {
        this.metaDataModel = metaDataModel;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
