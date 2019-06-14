package com.mingquan.yuejian.bean;

/**
 * Created by Administrator on 2016/3/30.
 */
public class YueJianAppSendGiftBean {
  private String type;
  private String action;
  private int uid;
  private int touid;
  private int giftid;
  private int giftcount;
  private int totalcoin;
  private String showid;
  private String addtime;
  private String giftname;
  private String gifticon;
  private String evensend;
  private long sendTime;
  private String avatar;
  private String receive_avatar;
  private String receive_name;
  private String nicename;
  private String level;
  private String petId;
  private String equipId;
  private String material;
  private String md5;

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public String getPetId() {
    return petId;
  }

  public void setPetId(String petId) {
    this.petId = petId;
  }

  public String getEquipId() {
    return equipId;
  }

  public void setEquipId(String equipId) {
    this.equipId = equipId;
  }

  public String getMaterial() {
    return material;
  }

  public void setMaterial(String material) {
    this.material = material;
  }

  private YueJianAppChatBean mChatBean;

  public YueJianAppChatBean getChatBean() {
    return mChatBean;
  }

  public void setChatBean(YueJianAppChatBean chatBean) {
    mChatBean = chatBean;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  private YueJianAppSendGiftBean bean;

  public YueJianAppSendGiftBean getBean() {
    return bean;
  }

  public void setBean(YueJianAppSendGiftBean bean) {
    this.bean = bean;
  }

  public String getReceive_avatar() {
    return receive_avatar;
  }

  public void setReceive_avatar(String receive_avatar) {
    this.receive_avatar = receive_avatar;
  }

  public String getReceive_name() {
    return receive_name;
  }

  @Override
  public String toString() {
    return "YueJianAppSendGiftBean{"
        + "type='" + type + '\'' + ", action='" + action + '\'' + ", uid=" + uid + ", touid="
        + touid + ", giftid=" + giftid + ", giftcount=" + giftcount + ", totalcoin=" + totalcoin
        + ", showid='" + showid + '\'' + ", addtime='" + addtime + '\'' + ", giftname='" + giftname
        + '\'' + ", gifticon='" + gifticon + '\'' + ", evensend='" + evensend + '\'' + ", sendTime="
        + sendTime + ", avatar='" + avatar + '\'' + ", receive_avatar='" + receive_avatar + '\''
        + ", receive_name='" + receive_name + '\'' + ", nicename='" + nicename + '\'' + '}';
  }

  public void setReceive_name(String receive_name) {
    this.receive_name = receive_name;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getNicename() {
    return nicename;
  }

  public void setNicename(String nicename) {
    this.nicename = nicename;
  }

  public long getSendTime() {
    return sendTime;
  }

  public void setSendTime(long sendTime) {
    this.sendTime = sendTime;
  }

  public String getEvensend() {
    return evensend;
  }

  public void setEvensend(String eventsend) {
    this.evensend = eventsend;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

  public int getGiftid() {
    return giftid;
  }

  public void setGiftid(int giftid) {
    this.giftid = giftid;
  }

  public int getTouid() {
    return touid;
  }

  public void setTouid(int touid) {
    this.touid = touid;
  }

  public int getGiftcount() {
    return giftcount;
  }

  public void setGiftcount(int giftcount) {
    this.giftcount = giftcount;
  }

  public int getTotalcoin() {
    return totalcoin;
  }

  public void setTotalcoin(int totalcoin) {
    this.totalcoin = totalcoin;
  }

  public String getShowid() {
    return showid;
  }

  public void setShowid(String showid) {
    this.showid = showid;
  }

  public String getAddtime() {
    return addtime;
  }

  public void setAddtime(String addtime) {
    this.addtime = addtime;
  }

  public String getGiftname() {
    return giftname;
  }

  public void setGiftname(String giftname) {
    this.giftname = giftname;
  }

  public String getGifticon() {
    return gifticon;
  }

  public void setGifticon(String gifticon) {
    this.gifticon = gifticon;
  }
}
