package com.mingquan.yuejian.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class YueJianAppUserBean implements Serializable {
  private int id = 0;
  private String user_login;
  private String user_pass;
  private String user_nicename;
  private String user_email;
  private String user_url;
  private String avatar;
  private int sex;
  private String birthday;
  private String signature;
  private String last_login_ip;
  private String last_login_time;
  private String create_time;
  private String user_activation_key;
  private String user_status;
  private String score;
  private String experience;
  private String chips;
  private String votestotal;
  private String max_exp;
  private String totalBroadcastSeconds;
  private String openid;
  private String blacknum;
  private String userType; //判断是否是管理员
  private String chatServer;
  private String country;
  private String liveType;
  private String liveTag;
  private String gold;

  public String getGold() {
    return gold;
  }

  public void setGold(String gold) {
    this.gold = gold;
  }

  public String getLiveTag() {
    return liveTag;
  }

  public void setLiveTag(String liveTag) {
    this.liveTag = liveTag;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getChatServer() {
    return chatServer;
  }

  public void setChatServer(String chatServer) {
    this.chatServer = chatServer;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }

  public String getBlacknum() {
    return blacknum;
  }

  public void setBlacknum(String blacknum) {
    this.blacknum = blacknum;
  }

  public String getTotalBroadcastSeconds() {
    return totalBroadcastSeconds;
  }

  public void setTotalBroadcastSeconds(String totalBroadcastSeconds) {
    this.totalBroadcastSeconds = totalBroadcastSeconds;
  }

  public String getVotestotal() {
    return votestotal;
  }

  public void setVotestotal(String votestotal) {
    this.votestotal = votestotal;
  }

  public String getMax_exp() {
    return max_exp;
  }

  public void setMax_exp(String max_exp) {
    this.max_exp = max_exp;
  }

  public String getExperience() {
    return experience;
  }

  public void setExperience(String experience) {
    this.experience = experience;
  }

  public String getChips() {
    return chips;
  }

  public void setChips(String chips) {
    this.chips = chips;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  private int user_type;
  private String coin;
  private String mobile;
  private String token;
  private String expiretime;
  private String votes;
  private String province;
  private String city;
  private String consumption;
  private int level;
  private int isauth; //是否已经认证
  private int isattention;
  private int attentionnum;
  private String fansnum;
  private int liverecordnum;
  private String title;
  private String nums;
  private String pk_uid;

  private List<YueJianAppPkUser> pk_users;

  public String getPk_uid() {
    return pk_uid;
  }

  public List<YueJianAppPkUser> getPk_users() {
    return pk_users;
  }

  public void setPk_users(List<YueJianAppPkUser> pk_users) {
    this.pk_users = pk_users;
  }

  public String getLiveType() {
    return liveType;
  }

  public void setLiveType(String liveType) {
    this.liveType = liveType;
  }

  private int uType;

  public String getPkUid() {
    return pk_uid;
  }

  public void setPkUid(String pkUid) {
    this.pk_uid = pkUid;
  }

  //    public String getliveType() {
  //        return liveType;
  //    }
  //
  //    public void getliveType(String type) {
  //        this.liveType = type;
  //    }

  public int getuType() {
    return uType;
  }

  public void setuType(int uType) {
    this.uType = uType;
  }

  public int getIsauth() {
    return isauth;
  }

  public void setIsauth(int isauth) {
    this.isauth = isauth;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getNums() {
    return nums;
  }

  public void setNums(String nums) {
    this.nums = nums;
  }

  public String getConsumption() {
    return consumption;
  }

  public void setConsumption(String consumption) {
    this.consumption = consumption;
  }

  public int getAttentionnum() {
    return attentionnum;
  }

  public void setAttentionnum(int attentionnum) {
    this.attentionnum = attentionnum;
  }

  public int getLiverecordnum() {
    return liverecordnum;
  }

  public void setLiverecordnum(int liverecordnum) {
    this.liverecordnum = liverecordnum;
  }

  public String getFansnum() {
    return fansnum;
  }

  public void setFansnum(String fansnum) {
    this.fansnum = fansnum;
  }

  public int getIsattention() {
    return isattention;
  }

  public void setIsattention(int isattention) {
    this.isattention = isattention;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getVotes() {
    return votes;
  }

  public void setVotes(String votes) {
    this.votes = votes;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUser_login() {
    return user_login;
  }

  public void setUser_login(String user_login) {
    this.user_login = user_login;
  }

  public String getUser_nicename() {
    return user_nicename;
  }

  public void setUser_nicename(String user_nicename) {
    this.user_nicename = user_nicename;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getLast_login_time() {
    return last_login_time;
  }

  public void setLast_login_time(String last_login_time) {
    this.last_login_time = last_login_time;
  }

  public String getUser_status() {
    return user_status;
  }

  public void setUser_status(String user_status) {
    this.user_status = user_status;
  }

  public String getUser_email() {
    return user_email;
  }

  public void setUser_email(String user_email) {
    this.user_email = user_email;
  }

  public String getUser_url() {
    return user_url;
  }

  public void setUser_url(String user_url) {
    this.user_url = user_url;
  }

  public String getUser_pass() {
    return user_pass;
  }

  public void setUser_pass(String user_pass) {
    this.user_pass = user_pass;
  }

  public int getSex() {
    return sex;
  }

  public void setSex(int sex) {
    this.sex = sex;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public String getLast_login_ip() {
    return last_login_ip;
  }

  public void setLast_login_ip(String last_login_ip) {
    this.last_login_ip = last_login_ip;
  }

  public String getCreate_time() {
    return create_time;
  }

  public void setCreate_time(String create_time) {
    this.create_time = create_time;
  }

  public String getUser_activation_key() {
    return user_activation_key;
  }

  public void setUser_activation_key(String user_activation_key) {
    this.user_activation_key = user_activation_key;
  }

  public String getScore() {
    return score;
  }

  public void setPk_uid(String pk_uid) {
    this.pk_uid = pk_uid;
  }

  public void setScore(String score) {
    this.score = score;
  }

  public int getUser_type() {
    return user_type;
  }

  public void setUser_type(int user_type) {
    this.user_type = user_type;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getCoin() {
    return coin;
  }

  public void setCoin(String coin) {
    this.coin = coin;
  }

  public String getExpiretime() {
    return expiretime;
  }

  public void setExpiretime(String expiretime) {
    this.expiretime = expiretime;
  }

  @Override
  public String toString() {
    return "YueJianAppUserBean{"
        + "id=" + id + ", user_login='" + user_login + '\'' + ", user_pass='" + user_pass + '\''
        + ", user_nicename='" + user_nicename + '\'' + ", user_email='" + user_email + '\''
        + ", user_url='" + user_url + '\'' + ", avatar='" + avatar + '\'' + ", sex=" + sex
        + ", liveType='" + liveType + '\'' + ", birthday='" + birthday + '\'' + ", signature='"
        + signature + '\'' + ", last_login_ip='" + last_login_ip + '\'' + ", last_login_time='"
        + last_login_time + '\'' + ", create_time='" + create_time + '\''
        + ", user_activation_key='" + user_activation_key + '\'' + ", user_status='" + user_status
        + '\'' + ", score='" + score + '\'' + ", experience='" + experience + '\'' + ", chips='"
        + chips + '\'' + ", votestotal='" + votestotal + '\'' + ", max_exp='" + max_exp + '\''
        + ", totalBroadcastSeconds='" + totalBroadcastSeconds + '\'' + ", openid='" + openid + '\''
        + ", blacknum='" + blacknum + '\'' + ", userType='" + userType + '\'' + ", chatServer='"
        + chatServer + '\'' + ", country='" + country + '\'' + ", user_type=" + user_type
        + ", coin='" + coin + '\'' + ", mobile='" + mobile + '\'' + ", token='" + token + '\''
        + ", expiretime='" + expiretime + '\'' + ", votes='" + votes + '\'' + ", province='"
        + province + '\'' + ", provinceBean='" + city + '\'' + ", consumption='" + consumption + '\''
        + ", level=" + level + ", isauth=" + isauth + ", isattention=" + isattention
        + ", attentionnum=" + attentionnum + ", fansnum='" + fansnum + '\''
        + ", liverecordnum=" + liverecordnum + ", title='" + title + '\'' + ", nums='" + nums + '\''
        + ", pk_uid='" + pk_uid + '\'' +", pk_users=" + pk_users
        + ", uType=" + uType + '}';
  }
}
