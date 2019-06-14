package com.mingquan.yuejian.vchat;

import com.mingquan.yuejian.proto.model.YueJianAppACGiftModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;

public class YueJianAppShowGiftMessageEvent {
    private YueJianAppACGiftModel giftModel;
    private YueJianAppACUserPublicInfoModel senderModel;
    private YueJianAppACUserPublicInfoModel receiverModel;

    public YueJianAppShowGiftMessageEvent(YueJianAppACGiftModel gift, YueJianAppACUserPublicInfoModel sender, YueJianAppACUserPublicInfoModel receiver) {
        this.giftModel = gift;
        this.senderModel = sender;
        this.receiverModel = receiver;
    }

    public YueJianAppACGiftModel getGiftModel() {
        return giftModel;
    }

    public void setGiftModel(YueJianAppACGiftModel giftModel) {
        this.giftModel = giftModel;
    }

    public YueJianAppACUserPublicInfoModel getSenderModel() {
        return senderModel;
    }

    public void setSenderModel(YueJianAppACUserPublicInfoModel senderModel) {
        this.senderModel = senderModel;
    }

    public YueJianAppACUserPublicInfoModel getReceiverModel() {
        return receiverModel;
    }

    public void setReceiverModel(YueJianAppACUserPublicInfoModel receiverModel) {
        this.receiverModel = receiverModel;
    }
}