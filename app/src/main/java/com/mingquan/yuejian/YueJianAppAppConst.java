package com.mingquan.yuejian;

import android.content.Context;
import android.text.TextUtils;

import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppThreadManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 国家以及关键字的静态HashMap
 * Created by Jiantao on 2016/12/7.
 */
public class YueJianAppAppConst {
  private static final String TAG = YueJianAppAppConst.class.getSimpleName();
  private Context mContext;
  private static YueJianAppAppConst mAppConst = new YueJianAppAppConst();
  public static int PAGE_NUM_FIFTY = 50;
  public static int PAGE_NUM_TWENTY = 20;

  private YueJianAppAppConst() {
    //需要在Application类中初始化
  }

  public void init(Context context) {
    this.mContext = context;
    getDirtyWordsFromAsserts();
  }
  public static YueJianAppAppConst getInstance() {
    return mAppConst;
  }

  /**
   * 下载更新完成之后调用，对关键字的Map进行更新
   */
  public void updateKeyWords() {
    getDirtyWordsFromFile();
  }

  public final static HashMap<String, String> mCountryMap = new HashMap<String, String>() {
    {
      put("Canada", "🇨🇦");
      put("Guinea-Bissau", "🇬🇼");
      put("Lithuania", "🇱🇹");
      put("Cambodia", "🇰🇭");
      put("Ceuta & Melilla", "🇪🇦");
      put("Aruba", "🇦🇼");
      put("Swaziland", "🇸🇿");
      put("Argentina", "🇦🇷");
      put("Bolivia", "🇧🇴");
      put("Cameroon", "🇨🇲");
      put("Burkina Faso", "🇧🇫");
      put("Turkmenistan", "🇹🇲");
      put("Bahrain", "🇧🇭");
      put("Saudi Arabia", "🇸🇦");
      put("Rwanda", "🇷🇼");
      put("Japan", "🇯🇵");
      put("Wallis & Futuna", "🇼🇫");
      put("American Samoa", "🇦🇸");
      put("Northern Mariana Islands", "🇲🇵");
      put("Slovenia", "🇸🇮");
      put("Guatemala", "🇬🇹");
      put("Kuwait", "🇰🇼");
      put("Ascension Island", "🇦🇨");
      put("Jordan", "🇯🇴");
      put("St. Lucia", "🇱🇨");
      put("Dominica", "🇩🇲");
      put("Liberia", "🇱🇷");
      put("Maldives", "🇲🇻");
      put("Jamaica", "🇯🇲");
      put("Trinidad & Tobago", "🇹🇹");
      put("Oman", "🇴🇲");
      put("St. Kitts & Nevis", "🇰🇳");
      put("Martinique", "🇲🇶");
      put("Christmas Island", "🇨🇽");
      put("French Guiana", "🇬🇫");
      put("Niue", "🇳🇺");
      put("Monaco", "🇲🇨");
      put("England", "🇽🇪");
      put("St. Barthélemy", "🇧🇱");
      put("Yemen", "🇾🇪");
      put("European Union", "🇪🇺");
      put("Jersey", "🇯🇪");
      put("Bahamas", "🇧🇸");
      put("Albania", "🇦🇱");
      put("Samoa", "🇼🇸");
      put("Macau", "🇲🇴");
      put("Ethiopia", "🇪🇹");
      put("Norfolk Island", "🇳🇫");
      put("United Arab Emirates", "🇦🇪");
      put("Guam", "🇬🇺");
      put("Kosovo", "🇽🇰");
      put("India", "🇮🇳");
      put("Azerbaijan", "🇦🇿");
      put("Lesotho", "🇱🇸");
      put("São Tomé & Príncipe", "🇸🇹");
      put("Kenya", "🇰🇪");
      put("South Korea", "🇰🇷");
      put("Tajikistan", "🇹🇯");
      put("Turkey", "🇹🇷");
      put("Afghanistan", "🇦🇫");
      put("Bangladesh", "🇧🇩");
      put("Mauritania", "🇲🇷");
      put("Solomon Islands", "🇸🇧");
      put("Svalbard & Jan Mayen", "🇸🇯");
      put("San Marino", "🇸🇲");
      put("Mongolia", "🇲🇳");
      put("France", "🇫🇷");
      put("Bermuda", "🇧🇲");
      put("Namibia", "🇳🇦");
      put("Somalia", "🇸🇴");
      put("Peru", "🇵🇪");
      put("Laos", "🇱🇦");
      put("Nauru", "🇳🇷");
      put("Seychelles", "🇸🇨");
      put("Norway", "🇳🇴");
      put("Malawi", "🇲🇼");
      put("Cook Islands", "🇨🇰");
      put("Benin", "🇧🇯");
      put("Réunion", "🇷🇪");
      put("Libya", "🇱🇾");
      put("Cuba", "🇨🇺");
      put("Montenegro", "🇲🇪");
      put("Togo", "🇹🇬");
      put("Pitcairn Islands", "🇵🇳");
      //            put("China", "🇨🇳");
      put("Armenia", "🇦🇲");
      put("Dominican Republic", "🇩🇴");
      put("St. Pierre & Miquelon", "🇵🇲");
      put("French Polynesia", "🇵🇫");
      put("Côte D’Ivoire", "🇨🇮");
      put("Ghana", "🇬🇭");
      put("Tonga", "🇹🇴");
      put("Indonesia", "🇮🇩");
      put("New Zealand", "🇳🇿");
      put("Western Sahara", "🇪🇭");
      put("St. Helena", "🇸🇭");
      put("Finland", "🇫🇮");
      put("Central African Republic", "🇨🇫");
      put("New Caledonia", "🇳🇨");
      put("Mauritius", "🇲🇺");
      put("Liechtenstein", "🇱🇮");
      put("Belarus", "🇧🇾");
      put("St. Martin", "🇲🇫");
      put("Mali", "🇲🇱");
      put("Vatican ProvinceBean", "🇻🇦");
      put("Russia", "🇷🇺");
      put("Bulgaria", "🇧🇬");
      put("United States", "🇺🇸");
      put("Romania", "🇷🇴");
      put("Angola", "🇦🇴");
      put("French Southern Territories", "🇹🇫");
      put("Cayman Islands", "🇰🇾");
      put("South Africa", "🇿🇦");
      put("Tokelau", "🇹🇰");
      put("Cyprus", "🇨🇾");
      put("Caribbean Netherlands", "🇧🇶");
      put("Sweden", "🇸🇪");
      put("Qatar", "🇶🇦");
      put("Antigua & Barbuda", "🇦🇬");
      put("South Georgia & South Sandwich Islands", "🇬🇸");
      put("Austria", "🇦🇹");
      put("Vietnam", "🇻🇳");
      put("Clipperton Island", "🇨🇵");
      put("Uganda", "🇺🇬");
      put("Åland Islands", "🇦🇽");
      put("Canary Islands", "🇮🇨");
      put("Hungary", "🇭🇺");
      put("Niger", "🇳🇪");
      put("Wales", "🇽🇼");
      put("Brazil", "🇧🇷");
      put("Falkland Islands", "🇫🇰");
      put("Faroe Islands", "🇫🇴");
      put("Guinea", "🇬🇳");
      put("Panama", "🇵🇦");
      put("Scotland", "🇽🇸");
      put("Costa Rica", "🇨🇷");
      put("Luxembourg", "🇱🇺");
      put("Cape Verde", "🇨🇻");
      put("Andorra", "🇦🇩");
      put("Chad", "🇹🇩");
      put("British Virgin Islands", "🇻🇬");
      put("Gibraltar", "🇬🇮");
      put("Ireland", "🇮🇪");
      put("Pakistan", "🇵🇰");
      put("Palau", "🇵🇼");
      put("Nigeria", "🇳🇬");
      put("Ukraine", "🇺🇦");
      put("Ecuador", "🇪🇨");
      put("Czech Republic", "🇨🇿");
      put("Brunei", "🇧🇳");
      put("Australia", "🇦🇺");
      put("Iran", "🇮🇷");
      put("Algeria", "🇩🇿");
      put("El Salvador", "🇸🇻");
      put("Tuvalu", "🇹🇻");
      put("Congo - Kinshasa", "🇨🇩");
      put("Gambia", "🇬🇲");
      put("Turks & Caicos Islands", "🇹🇨");
      put("Marshall Islands", "🇲🇭");
      put("Chile", "🇨🇱");
      put("Puerto Rico", "🇵🇷");
      put("Belgium", "🇧🇪");
      put("Kiribati", "🇰🇮");
      put("Haiti", "🇭🇹");
      put("Belize", "🇧🇿");
      put("Hong Kong", "🇭🇰");
      put("Sierra Leone", "🇸🇱");
      put("Georgia", "🇬🇪");
      put("Denmark", "🇩🇰");
      put("Philippines", "🇵🇭");
      put("Moldova", "🇲🇩");
      put("Morocco", "🇲🇦");
      put("Heard & McDonald Islands", "🇭🇲");
      put("Malta", "🇲🇹");
      put("Guernsey", "🇬🇬");
      put("Thailand", "🇹🇭");
      put("Switzerland", "🇨🇭");
      put("Grenada", "🇬🇩");
      put("Congo - Brazzaville", "🇨🇬");
      put("U.S. Outlying Islands", "🇺🇲");
      put("U.S. Virgin Islands", "🇻🇮");
      put("Isle of Man", "🇮🇲");
      put("Portugal", "🇵🇹");
      put("Estonia", "🇪🇪");
      put("Uruguay", "🇺🇾");
      put("Curaçao", "🇨🇼");
      put("Equatorial Guinea", "🇬🇶");
      put("Lebanon", "🇱🇧");
      put("Tristan Da Cunha", "🇹🇦");
      put("Uzbekistan", "🇺🇿");
      put("Tunisia", "🇹🇳");
      put("Djibouti", "🇩🇯");
      put("Greenland", "🇬🇱");
      put("Timor-Leste", "🇹🇱");
      put("Spain", "🇪🇸");
      put("Colombia", "🇨🇴");
      put("Burundi", "🇧🇮");
      put("Slovakia", "🇸🇰");
      put("Taiwan", "🇹🇼");
      put("Fiji", "🇫🇯");
      put("Barbados", "🇧🇧");
      put("Cocos Islands", "🇨🇨");
      put("Madagascar", "🇲🇬");
      put("Italy", "🇮🇹");
      put("Bhutan", "🇧🇹");
      put("Sudan", "🇸🇩");
      put("Palestinian Territories", "🇵🇸");
      put("Nepal", "🇳🇵");
      put("Micronesia", "🇫🇲");
      put("Bosnia & Herzegovina", "🇧🇦");
      put("Netherlands", "🇳🇱");
      put("Tanzania", "🇹🇿");
      put("Suriname", "🇸🇷");
      put("Anguilla", "🇦🇮");
      put("Venezuela", "🇻🇪");
      put("United Nations", "🇺🇳");
      put("Israel", "🇮🇱");
      put("Malaysia", "🇲🇾");
      put("Iceland", "🇮🇸");
      put("Zambia", "🇿🇲");
      put("Senegal", "🇸🇳");
      put("Papua New Guinea", "🇵🇬");
      put("St. Vincent & Grenadines", "🇻🇨");
      put("Zimbabwe", "🇿🇼");
      put("Germany", "🇩🇪");
      put("Vanuatu", "🇻🇺");
      put("Diego Garcia", "🇩🇬");
      put("Mozambique", "🇲🇿");
      put("Kazakhstan", "🇰🇿");
      put("Poland", "🇵🇱");
      put("Eritrea", "🇪🇷");
      put("Kyrgyzstan", "🇰🇬");
      put("Mayotte", "🇾🇹");
      put("British Indian Ocean Territory", "🇮🇴");
      put("Iraq", "🇮🇶");
      put("Montserrat", "🇲🇸");
      put("Mexico", "🇲🇽");
      put("Macedonia", "🇲🇰");
      put("North Korea", "🇰🇵");
      put("Paraguay", "🇵🇾");
      put("Latvia", "🇱🇻");
      put("South Sudan", "🇸🇸");
      put("Guyana", "🇬🇾");
      put("Croatia", "🇭🇷");
      put("Syria", "🇸🇾");
      put("Guadeloupe", "🇬🇵");
      put("Sint Maarten", "🇸🇽");
      put("Honduras", "🇭🇳");
      put("Myanmar", "🇲🇲");
      put("Bouvet Island", "🇧🇻");
      put("Egypt", "🇪🇬");
      put("Nicaragua", "🇳🇮");
      put("Singapore", "🇸🇬");
      put("Serbia", "🇷🇸");
      put("Comoros", "🇰🇲");
      put("United Kingdom", "🇬🇧");
      put("Antarctica", "🇦🇶");
      put("Greece", "🇬🇷");
      put("Sri Lanka", "🇱🇰");
      put("Gabon", "🇬🇦");
      put("Botswana", "🇧🇼");
    }
  };

  public final static HashMap<String, ArrayList<String>> mKeyWords =
      new HashMap<String, ArrayList<String>>() {
        {
          put("團", new ArrayList<String>() {
            {
              add("團員馬爾汀");
              add("團長戈登");
            }
          });
          put("麇", new ArrayList<String>() {
            { add("麇"); }
          });
          put("歇", new ArrayList<String>() {
            { add("歇逼"); }
          });
          put("麥", new ArrayList<String>() {
            { add("麥克斯"); }
          });
          put("麦", new ArrayList<String>() {
            { add("麦角酸"); }
          });
          put("地", new ArrayList<String>() {
            {
              add("地下教會");
              add("地藏");
            }
          });
          put("圮", new ArrayList<String>() {
            { add("圮綦"); }
          });
          put("圣", new ArrayList<String>() {
            { add("圣泉学淫"); }
          });
          put("麻", new ArrayList<String>() {
            {
              add("麻醉乙醚");
              add("麻醉钢枪");
              add("麻醉枪");
            }
          });
          put("正", new ArrayList<String>() {
            {
              add("正義黨論壇");
              add("正見網");
            }
          });
          put("劬", new ArrayList<String>() {
            { add("劬"); }
          });
          put("亂", new ArrayList<String>() {
            {
              add("亂倫");
              add("亂交");
              add("亂輪");
            }
          });
          put("聯", new ArrayList<String>() {
            { add("聯總"); }
          });
          put("二", new ArrayList<String>() {
            {
              add("二乙基酰胺发抡");
              add("二穴中出");
              add("二逼");
              add("二奶");
              add("二屄");
              add("二B");
            }
          });
          put("于", new ArrayList<String>() {
            { add("于幼軍"); }
          });
          put("互", new ArrayList<String>() {
            {
              add("互舔淫穴");
              add("互淫");
            }
          });
          put("五", new ArrayList<String>() {
            {
              add("五星红旗");
              add("五月天");
            }
          });
          put("亚", new ArrayList<String>() {
            {
              add("亚洲情色网");
              add("亚洲有码");
              add("亚洲淫娃");
              add("亚洲性虐");
              add("亚洲色");
              add("亚情");
            }
          });
          put("棬", new ArrayList<String>() {
            { add("棬"); }
          });
          put("劌", new ArrayList<String>() {
            { add("劌"); }
          });
          put("交", new ArrayList<String>() {
            {
              add("交换夫妻");
              add("交媾");
              add("交配");
            }
          });
          put("亮", new ArrayList<String>() {
            {
              add("亮穴");
              add("亮屄");
            }
          });
          put("人", new ArrayList<String>() {
            {
              add("人民之聲論壇");
              add("人民內情真相");
              add("人妻做爱");
              add("人体摄影");
              add("人妻交换");
              add("人妻自拍");
              add("人妻榨乳");
              add("人民真實");
              add("人妻熟女");
              add("人妻色诱");
              add("人權");
              add("人屠");
            }
          });
          put("聖", new ArrayList<String>() {
            {
              add("聖射手");
              add("聖戰");
            }
          });
          put("鴿", new ArrayList<String>() {
            { add("鴿派"); }
          });
          put("鴰", new ArrayList<String>() {
            { add("鴰"); }
          });
          put("鴟", new ArrayList<String>() {
            { add("鴟"); }
          });
          put("糝", new ArrayList<String>() {
            { add("糝"); }
          });
          put("糞", new ArrayList<String>() {
            { add("糞便"); }
          });
          put("柔", new ArrayList<String>() {
            { add("柔阴术"); }
          });
          put("死", new ArrayList<String>() {
            {
              add("死全家");
              add("死GM");
              add("死GD");
              add("死gd");
              add("死gm");
            }
          });
          put("凵", new ArrayList<String>() {
            { add("凵"); }
          });
          put("糴", new ArrayList<String>() {
            { add("糴"); }
          });
          put("k", new ArrayList<String>() {
            { add("kao"); }
          });
          put("柯", new ArrayList<String>() {
            {
              add("柯建銘");
              add("柯賜海");
            }
          });
          put("释", new ArrayList<String>() {
            {
              add("释迦牟尼");
              add("释欲");
            }
          });
          put("柴", new ArrayList<String>() {
            { add("柴玲"); }
          });
          put("妓", new ArrayList<String>() {
            {
              add("妓院");
              add("妓女");
              add("妓");
            }
          });
          put("妖", new ArrayList<String>() {
            { add("妖媚熟母"); }
          });
          put("妈", new ArrayList<String>() {
            {
              add("妈卖妈屁");
              add("妈白勺");
              add("妈的");
              add("妈批");
              add("妈比");
              add("妈逼");
              add("妈祖");
              add("妈B");
            }
          });
          put("襆", new ArrayList<String>() {
            { add("襆"); }
          });
          put("妹", new ArrayList<String>() {
            {
              add("妹妹骚图");
              add("妹妹阴毛");
            }
          });
          put("妳", new ArrayList<String>() {
            {
              add("妳老母的");
              add("妳娘的");
              add("妳马的");
              add("妳妈的");
            }
          });
          put("無", new ArrayList<String>() {
            {
              add("無界瀏覽器");
              add("無碼電影");
              add("無碼H漫");
              add("無毛穴");
            }
          });
          put("麴", new ArrayList<String>() {
            { add("麴"); }
          });
          put("闃", new ArrayList<String>() {
            { add("闃"); }
          });
          put("疯", new ArrayList<String>() {
            { add("疯狗"); }
          });
          put("跨", new ArrayList<String>() {
            { add("跨下呻吟"); }
          });
          put("跽", new ArrayList<String>() {
            { add("跽"); }
          });
          put("闞", new ArrayList<String>() {
            { add("闞"); }
          });
          put("闥", new ArrayList<String>() {
            { add("闥"); }
          });
          put("疆", new ArrayList<String>() {
            { add("疆独"); }
          });
          put("闷", new ArrayList<String>() {
            { add("闷骚"); }
          });
          put("疖", new ArrayList<String>() {
            { add("疖"); }
          });
          put("犬", new ArrayList<String>() {
            { add("犬"); }
          });
          put("A", new ArrayList<String>() {
            {
              add("ATAN的移动石");
              add("Atan的移动石");
              add("AV电影下载");
              add("Administrator");
              add("Abianwansui");
              add("AIORT墓地");
              add("Alafate");
              add("Aluoyue");
              add("ASSHOLE");
              add("AI滋");
              add("Admin");
              add("Abian");
              add("A片");
              add("AIDS");
              add("Aids");
              add("AV");
            }
          });
          put("汆", new ArrayList<String>() {
            { add("汆"); }
          });
          put("遺", new ArrayList<String>() {
            { add("遺精"); }
          });
          put("汔", new ArrayList<String>() {
            { add("汔"); }
          });
          put("遲", new ArrayList<String>() {
            { add("遲鈍的圖騰"); }
          });
          put("带", new ArrayList<String>() {
            { add("带套肛交"); }
          });
          put("汩", new ArrayList<String>() {
            { add("汩"); }
          });
          put("紧", new ArrayList<String>() {
            {
              add("紧缚凌辱");
              add("紧穴");
            }
          });
          put("帳", new ArrayList<String>() {
            { add("帳號"); }
          });
          put("達", new ArrayList<String>() {
            {
              add("達夫警衛兵");
              add("達夫侍從");
              add("達癩");
            }
          });
          put("遗", new ArrayList<String>() {
            { add("遗精"); }
          });
          put("鸠", new ArrayList<String>() {
            {
              add("鸠屎");
              add("鸠");
            }
          });
          put("道", new ArrayList<String>() {
            { add("道教"); }
          });
          put("月", new ArrayList<String>() {
            {
              add("月经不调");
              add("月经");
              add("月經");
            }
          });
          put("朝", new ArrayList<String>() {
            { add("朝天穴"); }
          });
          put("朐", new ArrayList<String>() {
            { add("朐"); }
          });
          put("粉", new ArrayList<String>() {
            {
              add("粉飾太平");
              add("粉红穴");
              add("粉屄");
              add("粉穴");
            }
          });
          put("木", new ArrayList<String>() {
            {
              add("木子論壇");
              add("木耳");
            }
          });
          put("本", new ArrayList<String>() {
            {
              add("本土无码");
              add("本?拉登");
              add("本拉登");
            }
          });
          put("精", new ArrayList<String>() {
            {
              add("精液榨取");
              add("精液浴");
              add("精液");
              add("精子");
            }
          });
          put("粥", new ArrayList<String>() {
            {
              add("粥健康");
              add("粥永康");
            }
          });
          put("回", new ArrayList<String>() {
            {
              add("回族人吃猪肉");
              add("回民都是猪");
              add("回民吃猪肉");
              add("回民暴動");
              add("回民暴动");
              add("回民是猪");
              add("回良玉");
              add("回教");
            }
          });
          put("囝", new ArrayList<String>() {
            { add("囝"); }
          });
          put("撩", new ArrayList<String>() {
            { add("撩人"); }
          });
          put("囅", new ArrayList<String>() {
            { add("囅"); }
          });
          put("撸", new ArrayList<String>() {
            {
              add("撸管子");
              add("撸");
            }
          });
          put("国", new ArrayList<String>() {
            { add("国产AV"); }
          });
          put("撅", new ArrayList<String>() {
            { add("撅起大白腚"); }
          });
          put("l", new ArrayList<String>() {
            {
              add("lunlidianyingxiazai.1174.net.cn");
              add("lunlidianying.1274.net.cn");
              add("lianggezhongguo");
              add("lamusifeierde");
              add("langyou.info");
              add("laichangxing");
              add("luoronghuan");
              add("lll.xaoh.cn");
              add("lxsm888.com");
              add("lyx-game.cn");
              add("lichangchun");
              add("luoxialu.cn");
              add("lordren.com");
              add("lybbs.info");
              add("liubocheng");
              add("likeko.com");
              add("l2jsom.cn");
              add("lidashi");
              add("liening");
              add("linken");
              add("luogan");
              add("liusi");
              add("ligen");
            }
          });
          put("撒", new ArrayList<String>() {
            {
              add("撒切尔");
              add("撒尿");
            }
          });
          put("鹱", new ArrayList<String>() {
            { add("鹱"); }
          });
          put("撖", new ArrayList<String>() {
            { add("撖"); }
          });
          put("鹾", new ArrayList<String>() {
            { add("鹾"); }
          });
          put("撟", new ArrayList<String>() {
            { add("撟"); }
          });
          put("鹺", new ArrayList<String>() {
            { add("鹺"); }
          });
          put("掰", new ArrayList<String>() {
            {
              add("掰穴皮卡丘");
              add("掰穴打洞");
              add("掰穴");
            }
          });
          put("破", new ArrayList<String>() {
            {
              add("破处");
              add("破鞋");
            }
          });
          put("掯", new ArrayList<String>() {
            { add("掯"); }
          });
          put("掮", new ArrayList<String>() {
            { add("掮"); }
          });
          put("推", new ArrayList<String>() {
            {
              add("推女郎");
              add("推油");
            }
          });
          put("排", new ArrayList<String>() {
            { add("排泄"); }
          });
          put("砍", new ArrayList<String>() {
            { add("砍翻一條街"); }
          });
          put("掄", new ArrayList<String>() {
            { add("掄功"); }
          });
          put("掎", new ArrayList<String>() {
            { add("掎"); }
          });
          put("蹉", new ArrayList<String>() {
            { add("蹉"); }
          });
          put("蹕", new ArrayList<String>() {
            { add("蹕"); }
          });
          put("W", new ArrayList<String>() {
            {
              add("WEB战牌");
              add("WEB牌戰");
              add("WSTAIJI");
              add("WANGCE");
              add("WEBZEN");
            }
          });
          put("髭", new ArrayList<String>() {
            { add("髭"); }
          });
          put("髑", new ArrayList<String>() {
            { add("髑屙民"); }
          });
          put("蹣", new ArrayList<String>() {
            { add("蹣"); }
          });
          put("劐", new ArrayList<String>() {
            { add("劐"); }
          });
          put("高", new ArrayList<String>() {
            {
              add("高校骚乱");
              add("高丽棒子");
              add("高麗棒子");
              add("高潮集锦");
              add("高潮白浆");
              add("高清性愛");
              add("高校暴乱");
              add("高级逼");
              add("高潮");
            }
          });
          put("逼", new ArrayList<String>() {
            {
              add("逼你老母");
              add("逼樣");
              add("逼痒");
              add("逼奸");
              add("逼样");
              add("逼毛");
              add("逼");
            }
          });
          put("連", new ArrayList<String>() {
            {
              add("連方瑀");
              add("連勝德");
              add("連惠心");
              add("連勝文");
              add("連戰");
            }
          });
          put("造", new ArrayList<String>() {
            {
              add("造愛");
              add("造反");
            }
          });
          put("巨", new ArrayList<String>() {
            {
              add("巨乳俏女医");
              add("巨鐵角哈克");
              add("巨炮兵团");
              add("巨槌騎兵");
              add("巨乳素人");
              add("巨乳");
              add("巨奶");
              add("巨骚");
              add("巨屌");
            }
          });
          put("蔣", new ArrayList<String>() {
            {
              add("蔣中正");
              add("蔣介石");
            }
          });
          put("蔡", new ArrayList<String>() {
            {
              add("蔡崇國");
              add("蔡啓芳");
              add("蔡文胜");
              add("蔡英文");
            }
          });
          put("巡", new ArrayList<String>() {
            { add("巡查"); }
          });
          put("巧", new ArrayList<String>() {
            { add("巧淫奸戏"); }
          });
          put("工", new ArrayList<String>() {
            {
              add("工商时报");
              add("工自聯");
            }
          });
          put("巹", new ArrayList<String>() {
            { add("巹"); }
          });
          put("巴", new ArrayList<String>() {
            {
              add("巴倫侍從");
              add("巴倫坦");
            }
          });
          put("覘", new ArrayList<String>() {
            { add("覘"); }
          });
          put("B", new ArrayList<String>() {
            {
              add("BlowJobs");
              add("BASTARD");
              add("Biao子");
              add("BanName");
              add("BIGNEWS");
              add("BLOWJOB");
              add("BIAO子");
              add("BAO皮");
              add("BBC、");
              add("Baichi");
              add("Bao皮");
              add("BAICHI");
              add("BIAOZI");
              add("BAOPI");
              add("Baopi");
              add("BOXUN");
              add("BI样");
              add("BITCH");
              add("Bi样");
              add("Bitch");
              add("B样");
            }
          });
          put("要", new ArrayList<String>() {
            {
              add("要色色");
              add("要射了");
            }
          });
          put("奇", new ArrayList<String>() {
            { add("奇淫宝鉴"); }
          });
          put("奁", new ArrayList<String>() {
            { add("奁"); }
          });
          put("偬", new ArrayList<String>() {
            { add("偬"); }
          });
          put("好", new ArrayList<String>() {
            {
              add("好色cc");
              add("好嫩");
            }
          });
          put("她", new ArrayList<String>() {
            {
              add("她妈的金日成");
              add("她妈");
              add("她媽");
              add("她娘");
            }
          });
          put("奸", new ArrayList<String>() {
            {
              add("奸夫淫妇");
              add("奸淫电车");
              add("奸一奸");
              add("奸暴");
              add("奸他");
              add("奸淫");
              add("奸幼");
              add("奸你");
              add("奸情");
              add("奸她");
              add("奸");
            }
          });
          put("覿", new ArrayList<String>() {
            { add("覿"); }
          });
          put("奴", new ArrayList<String>() {
            {
              add("奴隸魔族士兵");
              add("奴隷调教");
              add("奴畜抄");
            }
          });
          put("奶", new ArrayList<String>() {
            {
              add("奶大屄肥");
              add("奶挺臀翘");
              add("奶奶的");
              add("奶娘");
              add("奶頭");
              add("奶子");
              add("奶");
            }
          });
          put("女", new ArrayList<String>() {
            {
              add("女主人羅姬馬莉");
              add("女马白勺");
              add("女马ㄉ");
              add("女马的");
              add("女优");
              add("女幹");
              add("女屄");
              add("女干");
              add("女尻");
            }
          });
          put("親", new ArrayList<String>() {
            {
              add("親民黨");
              add("親美");
              add("親日");
            }
          });
          put("奩", new ArrayList<String>() {
            { add("奩"); }
          });
          put("覯", new ArrayList<String>() {
            { add("覯"); }
          });
          put("覡", new ArrayList<String>() {
            { add("覡"); }
          });
          put("仆", new ArrayList<String>() {
            { add("仆街"); }
          });
          put("肮", new ArrayList<String>() {
            { add("肮脏美学"); }
          });
          put("仂", new ArrayList<String>() {
            { add("仂"); }
          });
          put("肥", new ArrayList<String>() {
            { add("肥逼"); }
          });
          put("格", new ArrayList<String>() {
            {
              add("格雷(關卡排名管理者)");
              add("格魯(城鎮移動)");
              add("格魯");
            }
          });
          put("根", new ArrayList<String>() {
            { add("根正苗红"); }
          });
          put("核", new ArrayList<String>() {
            {
              add("核工业基地");
              add("核潜艇");
              add("核武器");
            }
          });
          put("他", new ArrayList<String>() {
            {
              add("他母親");
              add("他嗎的");
              add("他妈的");
              add("他祖宗");
              add("他马的");
              add("他媽的");
              add("他母亲");
              add("他媽");
              add("他爹");
              add("他干");
              add("他妈");
              add("他娘");
            }
          });
          put("肷", new ArrayList<String>() {
            { add("肷"); }
          });
          put("桊", new ArrayList<String>() {
            { add("桊"); }
          });
          put("以", new ArrayList<String>() {
            { add("以茎至洞"); }
          });
          put("肉", new ArrayList<String>() {
            {
              add("肉棍干骚妇");
              add("肉淫器吞精");
              add("肉感炮友");
              add("肉丝裤袜");
              add("肉棍子");
              add("肉蒲团");
              add("肉便器");
              add("肉逼");
              add("肉壶");
              add("肉棍");
              add("肉箫");
              add("肉唇");
              add("肉具");
              add("肉壁");
              add("肉缝");
              add("肉欲");
              add("肉茎");
              add("肉棒");
              add("肉穴");
              add("肉沟");
              add("肉洞");
            }
          });
          put("剛", new ArrayList<String>() {
            {
              add("剛比樣子");
              add("剛比");
            }
          });
          put("肘", new ArrayList<String>() {
            { add("肘永康"); }
          });
          put("前", new ArrayList<String>() {
            { add("前凸后翘"); }
          });
          put("m", new ArrayList<String>() {
            {
              add("meiguiqingren.1274.net.cn");
              add("meideweijiefu");
              add("mgjzybj.com");
              add("minghuinews");
              add("mpceggs.cn");
              add("moyi520.cn");
              add("mayingjiu");
              add("maozedong");
              add("mohanmode");
              add("mosuolini");
              add("my3q.com");
              add("mm美图");
              add("majiajue");
              add("minghui");
              add("mandela");
              add("makesi");
              add("mokeer");
              add("making");
              add("master");
            }
          });
          put("籜", new ArrayList<String>() {
            { add("籜"); }
          });
          put("躅", new ArrayList<String>() {
            { add("躅"); }
          });
          put("骡", new ArrayList<String>() {
            { add("骡干"); }
          });
          put("躑", new ArrayList<String>() {
            { add("躑"); }
          });
          put("躓", new ArrayList<String>() {
            { add("躓"); }
          });
          put("躒", new ArrayList<String>() {
            { add("躒"); }
          });
          put("骨", new ArrayList<String>() {
            { add("骨獅"); }
          });
          put("躜", new ArrayList<String>() {
            { add("躜"); }
          });
          put("躚", new ArrayList<String>() {
            { add("躚"); }
          });
          put("骑", new ArrayList<String>() {
            {
              add("骑他");
              add("骑她");
              add("骑你");
            }
          });
          put("躦", new ArrayList<String>() {
            { add("躦"); }
          });
          put("磧", new ArrayList<String>() {
            { add("磧"); }
          });
          put("骚", new ArrayList<String>() {
            {
              add("骚妇露逼");
              add("骚穴怒放");
              add("骚浪美女");
              add("骚浪人妻");
              add("骚女叫春");
              add("骚B熟女");
              add("骚妇掰B");
              add("骚姐姐");
              add("骚姨妈");
              add("骚妹妹");
              add("骚妻");
              add("骚妹");
              add("骚乳");
              add("骚妈");
              add("骚屄");
              add("骚穴");
              add("骚货");
              add("骚水");
              add("骚女");
              add("骚逼");
              add("骚母");
              add("骚洞");
              add("骚B");
              add("骚");
            }
          });
          put("身", new ArrayList<String>() {
            { add("身份生成器"); }
          });
          put("躪", new ArrayList<String>() {
            { add("躪"); }
          });
          put("骆", new ArrayList<String>() {
            { add("骆海坚"); }
          });
          put("罟", new ArrayList<String>() {
            { add("罟"); }
          });
          put("网", new ArrayList<String>() {
            { add("网管"); }
          });
          put("杂", new ArrayList<String>() {
            { add("杂种"); }
          });
          put("杀", new ArrayList<String>() {
            { add("杀人犯"); }
          });
          put("来", new ArrayList<String>() {
            {
              add("来爽我");
              add("来插我");
              add("来干");
            }
          });
          put("罴", new ArrayList<String>() {
            { add("罴"); }
          });
          put("東", new ArrayList<String>() {
            {
              add("東西南北論壇");
              add("東部地下水路");
              add("東土耳其斯坦");
              add("東南西北論談");
              add("東方紅時空");
              add("東突獨立");
              add("東突暴動");
              add("東條英機");
              add("東院看守");
              add("東北獨立");
              add("東方時空");
              add("東洋屄");
              add("東亞");
              add("東條");
              add("東社");
              add("東升");
            }
          });
          put("許", new ArrayList<String>() {
            {
              add("許信良");
              add("許財利");
              add("許家屯");
            }
          });
          put("瑪", new ArrayList<String>() {
            {
              add("瑪麗亞");
              add("瑪雅");
            }
          });
          put("訾", new ArrayList<String>() {
            { add("訾"); }
          });
          put("開", new ArrayList<String>() {
            {
              add("開放雜志");
              add("開苞");
            }
          });
          put("訐", new ArrayList<String>() {
            { add("訐"); }
          });
          put("閿", new ArrayList<String>() {
            { add("閿"); }
          });
          put("閹", new ArrayList<String>() {
            { add("閹狗"); }
          });
          put("閻", new ArrayList<String>() {
            { add("閻明複"); }
          });
          put("援", new ArrayList<String>() {
            {
              add("援助交易");
              add("援交薄码");
              add("援交自拍");
              add("援交妹");
              add("援交");
            }
          });
          put("C", new ArrayList<String>() {
            {
              add("CND刊物和论坛");
              add("CHINESENEWSNET");
              add("Clockgemstone");
              add("CLOCKGEMSTONE");
              add("CHINALIBERAL");
              add("Carrefour");
              add("Crestbone");
              add("CRESTBONE");
              add("CREADERS");
              add("CC小雪");
              add("CNN/NHK");
              add("CHINAMZ");
              add("CAO你");
              add("CHA你");
              add("Client");
              add("C-SPAN");
              add("CAOBI");
              add("CAOB");
              add("CAO");
              add("CND");
              add("CS");
              add("Cs");
            }
          });
          put("揭", new ArrayList<String>() {
            { add("揭批書"); }
          });
          put("糁", new ArrayList<String>() {
            { add("糁"); }
          });
          put("餼", new ArrayList<String>() {
            { add("餼"); }
          });
          put("插", new ArrayList<String>() {
            {
              add("插入内射");
              add("插穴手淫");
              add("插那嗎逼");
              add("插你爺爺");
              add("插那嗎比");
              add("插穴止痒");
              add("插那嗎B");
              add("插后庭");
              add("插你妈");
              add("插阴茎");
              add("插深些");
              add("插你媽");
              add("插逼");
              add("插他");
              add("插进");
              add("插阴");
              add("插我");
              add("插妳");
              add("插暴");
              add("插你");
              add("插比");
              add("插她");
              add("插穴");
              add("插Gm");
              add("插GM");
              add("插gM");
              add("插gm");
              add("插b");
              add("插");
            }
          });
          put("蕆", new ArrayList<String>() {
            { add("蕆"); }
          });
          put("淋", new ArrayList<String>() {
            { add("淋病"); }
          });
          put("釣", new ArrayList<String>() {
            { add("釣魚島"); }
          });
          put("淩", new ArrayList<String>() {
            { add("淩鋒"); }
          });
          put("金", new ArrayList<String>() {
            {
              add("金融邮报(台湾股网)");
              add("金鳞岂是池中物");
              add("金槍不倒");
              add("金融时报");
              add("金毛穴");
              add("金正日");
              add("金山桥");
              add("金正恩");
              add("金日成");
              add("金堯如");
              add("金澤辰");
            }
          });
          put("淫", new ArrayList<String>() {
            {
              add("淫の方程式");
              add("淫乱诊所");
              add("淫水爱液");
              add("淫声浪语");
              add("淫师荡母");
              add("淫欲日本");
              add("淫水涟涟");
              add("淫水丝袜");
              add("淫水四溅");
              add("淫亂潮吹");
              add("淫妻交换");
              add("淫水横溢");
              add("淫妇自慰");
              add("淫丝荡袜");
              add("淫兽学园");
              add("淫欲世家");
              add("淫语连连");
              add("淫荡贵妇");
              add("淫水横流");
              add("淫水翻騰");
              add("淫乱熟女");
              add("淫乱工作");
              add("淫肉诱惑");
              add("淫女炮图");
              add("淫色贴图");
              add("淫战群P");
              add("淫东方");
              add("淫驴屯");
              add("淫告白");
              add("淫浆");
              add("淫流");
              add("淫奴");
              add("淫腔");
              add("淫汁");
              add("淫液");
              add("淫女");
              add("淫痴");
              add("淫叫");
              add("淫虫");
              add("淫欲");
              add("淫母");
              add("淫賤");
              add("淫亂");
              add("淫荡");
              add("淫洞");
              add("淫秽");
              add("淫穴");
              add("淫糜");
              add("淫浪");
              add("淫毛");
              add("淫妹");
              add("淫店");
              add("淫货");
              add("淫图");
              add("淫样");
              add("淫色");
              add("淫穢");
              add("淫妻");
              add("淫书");
              add("淫貨");
              add("淫湿");
              add("淫姐");
              add("淫乱");
              add("淫逼");
              add("淫娃");
              add("淫贱");
              add("淫靡");
              add("淫蕩");
              add("淫妞");
              add("淫河");
              add("淫魔");
              add("淫妇");
              add("淫虐");
              add("淫水");
              add("淫情");
              add("淫蜜");
              add("淫B");
              add("淫");
            }
          });
          put("嶠", new ArrayList<String>() {
            { add("嶠"); }
          });
          put("蕩", new ArrayList<String>() {
            {
              add("蕩婦");
              add("蕩妹");
            }
          });
          put("采", new ArrayList<String>() {
            { add("采花堂"); }
          });
          put("釃", new ArrayList<String>() {
            { add("釃"); }
          });
          put("野", new ArrayList<String>() {
            {
              add("野外性交");
              add("野鶏");
            }
          });
          put("混", new ArrayList<String>() {
            {
              add("混亂的圖騰");
              add("混 沌决");
            }
          });
          put("嶴", new ArrayList<String>() {
            { add("嶴"); }
          });
          put("系", new ArrayList<String>() {
            {
              add("系统公告");
              add("系统消息");
              add("系统讯息");
              add("系統公告");
              add("系统");
              add("系統");
            }
          });
          put("蕺", new ArrayList<String>() {
            { add("蕺"); }
          });
          put("鸌", new ArrayList<String>() {
            { add("鸌"); }
          });
          put("鸟", new ArrayList<String>() {
            {
              add("鸟你");
              add("鸟gM");
              add("鸟Gm");
              add("鸟gm");
              add("鸟GM");
            }
          });
          put("鸨", new ArrayList<String>() {
            { add("鸨"); }
          });
          put("嚴", new ArrayList<String>() {
            {
              add("嚴家其");
              add("嚴家祺");
            }
          });
          put("擊", new ArrayList<String>() {
            {
              add("擊傷的圖騰");
              add("擊倒圖騰");
            }
          });
          put("鸡", new ArrayList<String>() {
            {
              add("鸡巴暴胀");
              add("鸡叭");
              add("鸡芭");
              add("鸡鸡");
              add("鸡奸");
              add("鸡歪");
              add("鸡掰");
              add("鸡巴");
              add("鸡Ｙ");
              add("鸡８");
              add("鸡吧");
              add("鸡八");
              add("鸡Y");
            }
          });
          put("鸦", new ArrayList<String>() {
            {
              add("鸦片液");
              add("鸦片渣");
            }
          });
          put("操", new ArrayList<String>() {
            {
              add("操你八辈祖宗");
              add("操你妈屄");
              add("操穴喷水");
              add("操那嗎比");
              add("操你爺爺");
              add("操那嗎逼");
              add("操B指南");
              add("操那嗎B");
              add("操你妈");
              add("操母狗");
              add("操７８");
              add("操逼毛");
              add("操女也");
              add("操他妈");
              add("操妳妈");
              add("操她妈");
              add("操七八");
              add("操你媽");
              add("操ＧＹ");
              add("操人也");
              add("操你");
              add("操烂");
              add("操他");
              add("操林");
              add("操我");
              add("操屄");
              add("操穴");
              add("操逼");
              add("操死");
              add("操她");
              add("操尼");
              add("操鶏");
              add("操肿");
              add("操爽");
              add("操黑");
              add("操妳");
              add("操射");
              add("操比");
              add("操蛋");
              add("操妻");
              add("操gm");
              add("操XX");
              add("操78");
              add("操Gm");
              add("操gM");
              add("操GM");
              add("操GY");
              add("操b");
              add("操");
            }
          });
          put("嚳", new ArrayList<String>() {
            { add("嚳"); }
          });
          put("刿", new ArrayList<String>() {
            { add("刿"); }
          });
          put("胸", new ArrayList<String>() {
            {
              add("胸濤乳浪");
              add("胸推");
            }
          });
          put("能", new ArrayList<String>() {
            { add("能樣"); }
          });
          put("胯", new ArrayList<String>() {
            { add("胯下呻吟"); }
          });
          put("列", new ArrayList<String>() {
            { add("列宁"); }
          });
          put("分", new ArrayList<String>() {
            {
              add("分隊長施蒂文");
              add("分裂中国");
              add("分裂祖国");
            }
          });
          put("切", new ArrayList<String>() {
            { add("切七"); }
          });
          put("桃", new ArrayList<String>() {
            { add("桃园蜜洞"); }
          });
          put("雠", new ArrayList<String>() {
            { add("雠"); }
          });
          put("雪", new ArrayList<String>() {
            { add("雪腿玉胯"); }
          });
          put("挨", new ArrayList<String>() {
            { add("挨球"); }
          });
          put("雷", new ArrayList<String>() {
            { add("雷尼亞"); }
          });
          put("零", new ArrayList<String>() {
            { add("零八宪章"); }
          });
          put("項", new ArrayList<String>() {
            {
              add("項懷誠");
              add("項小吉");
            }
          });
          put("集", new ArrayList<String>() {
            {
              add("集体性爱");
              add("集体淫");
            }
          });
          put("雅", new ArrayList<String>() {
            { add("雅蠛蝶"); }
          });
          put("碛", new ArrayList<String>() {
            { add("碛"); }
          });
          put("按", new ArrayList<String>() {
            { add("按摩棒"); }
          });
          put("頊", new ArrayList<String>() {
            { add("頊"); }
          });
          put("持", new ArrayList<String>() {
            { add("持不同政見"); }
          });
          put("雙", new ArrayList<String>() {
            { add("雙十節"); }
          });
          put("雜", new ArrayList<String>() {
            { add("雜種"); }
          });
          put("車", new ArrayList<String>() {
            {
              add("車侖女幹");
              add("車侖");
            }
          });
          put("驶", new ArrayList<String>() {
            {
              add("驶你老母");
              add("驶你老师");
              add("驶你娘");
              add("驶你爸");
              add("驶你母");
              add("驶你公");
            }
          });
          put("D", new ArrayList<String>() {
            {
              add("DAJIYUAN");
              add("DICK");
              add("DAMN");
              add("Dick");
            }
          });
          put("軍", new ArrayList<String>() {
            { add("軍妓"); }
          });
          put("碎", new ArrayList<String>() {
            {
              add("碎片製造商人蘇克");
              add("碎片製造商人馬克");
            }
          });
          put("马", new ArrayList<String>() {
            {
              add("马卖马屁");
              add("马拉戈壁");
              add("马勒戈壁");
              add("马祖日报");
              add("马英九");
              add("马加爵");
              add("马白勺");
              add("马的");
            }
          });
          put("贝", new ArrayList<String>() {
            { add("贝肉"); }
          });
          put("碡", new ArrayList<String>() {
            { add("碡"); }
          });
          put("軺", new ArrayList<String>() {
            { add("軺"); }
          });
          put("驏", new ArrayList<String>() {
            { add("驏"); }
          });
          put("熟", new ArrayList<String>() {
            {
              add("熟女乱伦");
              add("熟女颜射");
              add("熟妇人妻");
              add("熟妇骚器");
              add("熟女护士");
              add("熟妇");
              add("熟母");
              add("熟女");
            }
          });
          put("太", new ArrayList<String>() {
            {
              add("太子党");
              add("太阳报");
              add("太陽報");
              add("太监");
              add("太監");
            }
          });
          put("夫", new ArrayList<String>() {
            {
              add("夫妻床上激情自拍");
              add("夫妻俱乐部");
              add("夫妻自拍");
              add("夫妻乱交");
              add("夫妻多p");
              add("夫妻3p");
            }
          });
          put("天", new ArrayList<String>() {
            {
              add("天安門錄影帶");
              add("天安門屠殺");
              add("天眼日报社");
              add("天天干贴图");
              add("天下杂志");
              add("天天情色");
              add("天皇陛下");
              add("天天干");
              add("天閹");
            }
          });
          put("夯", new ArrayList<String>() {
            { add("夯"); }
          });
          put("觏", new ArrayList<String>() {
            { add("觏"); }
          });
          put("失", new ArrayList<String>() {
            { add("失禁"); }
          });
          put("觇", new ArrayList<String>() {
            { add("觇"); }
          });
          put("处", new ArrayList<String>() {
            {
              add("处女开包");
              add("处女");
              add("处男");
            }
          });
          put("觶", new ArrayList<String>() {
            { add("觶"); }
          });
          put("熱", new ArrayList<String>() {
            {
              add("熱站政論網");
              add("熱比婭");
            }
          });
          put("觳", new ArrayList<String>() {
            { add("觳"); }
          });
          put("外", new ArrayList<String>() {
            {
              add("外交與方略");
              add("外交論壇");
              add("外—挂");
              add("外专局");
              add("外　挂");
              add("外交部");
              add("外汇局");
              add("外\\挂");
              add("外-挂");
              add("外/挂");
              add("外_挂");
              add("外挂");
              add("外遇");
            }
          });
          put("多", new ArrayList<String>() {
            {
              add("多人性愛");
              add("多人轮");
            }
          });
          put("夜", new ArrayList<String>() {
            {
              add("夜話紫禁城");
              add("夜情");
            }
          });
          put("詵", new ArrayList<String>() {
            { add("詵"); }
          });
          put("镇", new ArrayList<String>() {
            { add("镇压"); }
          });
          put("詿", new ArrayList<String>() {
            { add("詿"); }
          });
          put("詘", new ArrayList<String>() {
            { add("詘"); }
          });
          put("詈", new ArrayList<String>() {
            { add("詈"); }
          });
          put("镳", new ArrayList<String>() {
            { add("镳"); }
          });
          put("長", new ArrayList<String>() {
            { add("長官沙塔特"); }
          });
          put("出", new ArrayList<String>() {
            {
              add("出售此号");
              add("出售枪支");
              add("出售手枪");
              add("出售假币");
              add("出售神符");
            }
          });
          put("ㄆ", new ArrayList<String>() {
            { add("ㄆ"); }
          });
          put("凸", new ArrayList<String>() {
            {
              add("凸女也");
              add("凸人也");
              add("凸肉优");
              add("凸她");
              add("凸妳");
              add("凸你");
              add("凸我");
              add("凸他");
            }
          });
          put("脱", new ArrayList<String>() {
            { add("脱内裤"); }
          });
          put("ㄌ", new ArrayList<String>() {
            { add("ㄌ"); }
          });
          put("ㄍ", new ArrayList<String>() {
            { add("ㄍ"); }
          });
          put("凱", new ArrayList<String>() {
            {
              add("凱奧勒尼什");
              add("凱爾雷斯");
              add("凱特切爾");
              add("凱爾本");
            }
          });
          put("ㄏ", new ArrayList<String>() {
            { add("ㄏ"); }
          });
          put("ㄈ", new ArrayList<String>() {
            { add("ㄈ"); }
          });
          put("ㄉ", new ArrayList<String>() {
            { add("ㄉ"); }
          });
          put("榱", new ArrayList<String>() {
            { add("榱"); }
          });
          put("ㄋ", new ArrayList<String>() {
            { add("ㄋ"); }
          });
          put("ㄔ", new ArrayList<String>() {
            { add("ㄔ"); }
          });
          put("ㄕ", new ArrayList<String>() {
            { add("ㄕ"); }
          });
          put("ㄖ", new ArrayList<String>() {
            { add("ㄖ"); }
          });
          put("ㄗ", new ArrayList<String>() {
            { add("ㄗ"); }
          });
          put("ㄐ", new ArrayList<String>() {
            { add("ㄐ"); }
          });
          put("ㄑ", new ArrayList<String>() {
            { add("ㄑ"); }
          });
          put("ㄒ", new ArrayList<String>() {
            { add("ㄒ"); }
          });
          put("ㄓ", new ArrayList<String>() {
            { add("ㄓ"); }
          });
          put("ㄜ", new ArrayList<String>() {
            { add("ㄜ"); }
          });
          put("ㄝ", new ArrayList<String>() {
            { add("ㄝ"); }
          });
          put("ㄞ", new ArrayList<String>() {
            { add("ㄞ"); }
          });
          put("ㄟ", new ArrayList<String>() {
            { add("ㄟ"); }
          });
          put("ㄘ", new ArrayList<String>() {
            { add("ㄘ"); }
          });
          put("ㄙ", new ArrayList<String>() {
            { add("ㄙ"); }
          });
          put("ㄚ", new ArrayList<String>() {
            { add("ㄚ"); }
          });
          put("ㄛ", new ArrayList<String>() {
            { add("ㄛ"); }
          });
          put("ㄤ", new ArrayList<String>() {
            { add("ㄤ"); }
          });
          put("ㄥ", new ArrayList<String>() {
            { add("ㄥ"); }
          });
          put("ㄦ", new ArrayList<String>() {
            { add("ㄦ"); }
          });
          put("ㄧ", new ArrayList<String>() {
            { add("ㄧ"); }
          });
          put("ㄠ", new ArrayList<String>() {
            { add("ㄠ"); }
          });
          put("ㄡ", new ArrayList<String>() {
            { add("ㄡ"); }
          });
          put("ㄢ", new ArrayList<String>() {
            { add("ㄢ"); }
          });
          put("ㄣ", new ArrayList<String>() {
            { add("ㄣ"); }
          });
          put("ㄨ", new ArrayList<String>() {
            { add("ㄨ"); }
          });
          put("ㄩ", new ArrayList<String>() {
            { add("ㄩ"); }
          });
          put("凌", new ArrayList<String>() {
            { add("凌辱"); }
          });
          put("转", new ArrayList<String>() {
            { add("转法轮"); }
          });
          put("轮", new ArrayList<String>() {
            {
              add("轮奸内射");
              add("轮流干");
              add("轮盘赌");
              add("轮暴");
              add("轮功");
              add("轮干");
              add("轮奸");
              add("轮操");
            }
          });
          put("Z", new ArrayList<String>() {
            {
              add("ZHENGJIANWANG");
              add("ZHENSHANREN");
              add("ZHUANFALUN");
              add("ZHENGJIAN");
            }
          });
          put("缔", new ArrayList<String>() {
            { add("缔顺"); }
          });
          put("悝", new ArrayList<String>() {
            { add("悝"); }
          });
          put("跫", new ArrayList<String>() {
            { add("跫"); }
          });
          put("缵", new ArrayList<String>() {
            { add("缵"); }
          });
          put("轹", new ArrayList<String>() {
            { add("轹"); }
          });
          put("噦", new ArrayList<String>() {
            { add("噦"); }
          });
          put("搴", new ArrayList<String>() {
            { add("搴"); }
          });
          put("噴", new ArrayList<String>() {
            {
              add("噴你");
              add("噴精");
            }
          });
          put("搋", new ArrayList<String>() {
            { add("搋"); }
          });
          put("搖", new ArrayList<String>() {
            { add("搖頭丸"); }
          });
          put("搞", new ArrayList<String>() {
            { add("搞栗棒"); }
          });
          put("搛", new ArrayList<String>() {
            { add("搛"); }
          });
          put("誶", new ArrayList<String>() {
            { add("誶"); }
          });
          put("销", new ArrayList<String>() {
            { add("销魂洞"); }
          });
          put("瓠", new ArrayList<String>() {
            { add("瓠"); }
          });
          put("锖", new ArrayList<String>() {
            { add("锖"); }
          });
          put("锪", new ArrayList<String>() {
            { add("锪"); }
          });
          put("舔", new ArrayList<String>() {
            {
              add("舔屁眼");
              add("舔鸡巴");
              add("舔西");
              add("舔脚");
              add("舔逼");
              add("舔屄");
              add("舔B");
            }
          });
          put("誄", new ArrayList<String>() {
            { add("誄"); }
          });
          put("隱", new ArrayList<String>() {
            { add("隱者之路"); }
          });
          put("隹", new ArrayList<String>() {
            { add("隹"); }
          });
          put("换", new ArrayList<String>() {
            {
              add("换妻大会");
              add("换妻杂交");
              add("换妻");
            }
          });
          put("隐", new ArrayList<String>() {
            { add("隐窝窝"); }
          });
          put("p", new ArrayList<String>() {
            {
              add("prettyirene.net");
              add("pcikchina.com");
              add("parke888.com");
              add("pengdehuai");
              add("pengliyuan");
              add("pkmmo.com");
              add("peacehall");
              add("pk200.com");
              add("petgirl");
              add("playboy");
              add("paper64");
              add("pujing");
              add("ppt.cc");
              add("pussy");
              add("porn");
            }
          });
          put("醴", new ArrayList<String>() {
            { add("醴"); }
          });
          put("醵", new ArrayList<String>() {
            { add("醵"); }
          });
          put("洗", new ArrayList<String>() {
            {
              add("洗肠射尿");
              add("洗脑班");
              add("洗腦");
            }
          });
          put("洛", new ArrayList<String>() {
            { add("洛克菲爾特"); }
          });
          put("薅", new ArrayList<String>() {
            { add("薅"); }
          });
          put("薛", new ArrayList<String>() {
            { add("薛偉"); }
          });
          put("薟", new ArrayList<String>() {
            { add("薟"); }
          });
          put("鄧", new ArrayList<String>() {
            { add("鄧笑貧"); }
          });
          put("薑", new ArrayList<String>() {
            { add("薑春雲"); }
          });
          put("醮", new ArrayList<String>() {
            { add("醮"); }
          });
          put("跞", new ArrayList<String>() {
            { add("跞"); }
          });
          put("灝", new ArrayList<String>() {
            { add("灝"); }
          });
          put("蘧", new ArrayList<String>() {
            { add("蘧"); }
          });
          put("士", new ArrayList<String>() {
            { add("士兵管理員瓦爾臣"); }
          });
          put("妩", new ArrayList<String>() {
            { add("妩媚挑逗"); }
          });
          put("蘭", new ArrayList<String>() {
            { add("蘭迪"); }
          });
          put("灌", new ArrayList<String>() {
            { add("灌满精液"); }
          });
          put("灄", new ArrayList<String>() {
            { add("灄"); }
          });
          put("壹", new ArrayList<String>() {
            {
              add("壹周刊(台湾)");
              add("壹周刊");
              add("壹电视");
            }
          });
          put("蘇", new ArrayList<String>() {
            {
              add("蘇特勤護衛兵");
              add("蘇特勤魔法師");
              add("蘇特勒守護兵");
              add("蘇盈貴");
              add("蘇紹智");
              add("蘇菲爾");
              add("蘇貞昌");
              add("蘇曉康");
              add("蘇南成");
              add("蘇特勤");
              add("蘇拉");
            }
          });
          put("灵", new ArrayList<String>() {
            {
              add("灵仙真佛宗");
              add("灵灵派");
              add("灵灵教");
            }
          });
          put("灭", new ArrayList<String>() {
            { add("灭族"); }
          });
          put("火", new ArrayList<String>() {
            {
              add("火辣图片");
              add("火辣写真");
              add("火棒");
            }
          });
          put("蘚", new ArrayList<String>() {
            { add("蘚鮑"); }
          });
          put("龍", new ArrayList<String>() {
            {
              add("龍火之心");
              add("龍虎豹");
            }
          });
          put("摩", new ArrayList<String>() {
            {
              add("摩门教");
              add("摩洛客");
            }
          });
          put("嘬", new ArrayList<String>() {
            { add("嘬"); }
          });
          put("龜", new ArrayList<String>() {
            {
              add("龜兒子");
              add("龜孫子");
              add("龜公");
              add("龜頭");
              add("龜投");
            }
          });
          put("摸", new ArrayList<String>() {
            {
              add("摸你鶏巴");
              add("摸咪咪");
              add("摸阴蒂");
            }
          });
          put("龟", new ArrayList<String>() {
            {
              add("龟儿子");
              add("龟孙子");
              add("龟头");
              add("龟公");
            }
          });
          put("龙", new ArrayList<String>() {
            {
              add("龙 虎 门");
              add("龙新民");
              add("龙虎");
            }
          });
          put("摶", new ArrayList<String>() {
            { add("摶"); }
          });
          put("嘸", new ArrayList<String>() {
            { add("嘸"); }
          });
          put("嘏", new ArrayList<String>() {
            { add("嘏"); }
          });
          put("摇", new ArrayList<String>() {
            {
              add("摇头丸");
              add("摇头玩");
            }
          });
          put("F", new ArrayList<String>() {
            {
              add("Freetibet 全能神");
              add("FEELMISTONE");
              add("Feelmistone");
              add("FREECHINA");
              add("Freetibet");
              add("FALUNDAFA");
              add("FREENET");
              add("FREEDOM");
              add("FA轮");
              add("FALUN");
              add("FUCK");
              add("FALU");
              add("Fuck");
              add("FKU");
              add("Fku");
              add("FLG");
            }
          });
          put("冰", new ArrayList<String>() {
            {
              add("冰毒卖枪支弹药");
              add("冰毒");
              add("冰後");
              add("冰粉");
            }
          });
          put("冱", new ArrayList<String>() {
            { add("冱"); }
          });
          put("腹", new ArrayList<String>() {
            { add("腹黑"); }
          });
          put("腡", new ArrayList<String>() {
            { add("腡"); }
          });
          put("槧", new ArrayList<String>() {
            { add("槧"); }
          });
          put("写", new ArrayList<String>() {
            { add("写真"); }
          });
          put("军", new ArrayList<String>() {
            {
              add("军国主义");
              add("军长发威");
              add("军妓");
            }
          });
          put("腚", new ArrayList<String>() {
            { add("腚"); }
          });
          put("冈", new ArrayList<String>() {
            {
              add("冈村秀树");
              add("冈村宁次");
            }
          });
          put("册", new ArrayList<String>() {
            {
              add("册那娘餓比");
              add("册那");
            }
          });
          put("再", new ArrayList<String>() {
            { add("再奸"); }
          });
          put("冁", new ArrayList<String>() {
            { add("冁"); }
          });
          put("内", new ArrayList<String>() {
            {
              add("内幕第28期");
              add("内射美妇");
              add("内射");
              add("内幕");
            }
          });
          put("輅", new ArrayList<String>() {
            { add("輅"); }
          });
          put("筇", new ArrayList<String>() {
            { add("筇"); }
          });
          put("騷", new ArrayList<String>() {
            {
              add("騷包");
              add("騷棒");
              add("騷逼");
              add("騷貨");
              add("騷卵");
              add("騷鶏");
              add("騷棍");
              add("騷浪");
              add("騷B");
            }
          });
          put("屁", new ArrayList<String>() {
            { add("屁眼"); }
          });
          put("騭", new ArrayList<String>() {
            { add("騭"); }
          });
          put("輕", new ArrayList<String>() {
            { add("輕舟快訊"); }
          });
          put("輪", new ArrayList<String>() {
            {
              add("輪大");
              add("輪功");
              add("輪奸");
            }
          });
          put("1", new ArrayList<String>() {
            {
              add("1234chengren.1249.net.cn");
              add("1999亚太新新闻");
              add("11xp.1243.net.cn");
              add("1000scarf.com");
              add("17173yxdl.cn");
              add("16dy-图库");
              add("17youle.com");
              add("17173dl.net");
              add("13888wg.com");
              add("158le.com");
              add("1qmsj.com");
              add("166578.cn");
              add("18900.com");
              add("173at.com");
              add("1100y.com");
              add("15wy.com");
              add("1t1t.com");
              add("13ml.net");
              add("10mb.cn");
              add("131.com");
              add("16大");
              add("18摸");
              add("18禁");
            }
          });
          put("筹", new ArrayList<String>() {
            { add("筹码"); }
          });
          put("騎", new ArrayList<String>() {
            { add("騎你"); }
          });
          put("輳", new ArrayList<String>() {
            { add("輳"); }
          });
          put("翘", new ArrayList<String>() {
            {
              add("翘臀嫩穴");
              add("翘臀嫩逼");
              add("翘臀");
            }
          });
          put("情", new ArrayList<String>() {
            {
              add("情色艺术天空");
              add("情色五月");
              add("情色天崖");
              add("情色导航");
              add("情色文学");
              add("情色");
              add("情婦");
              add("情獸");
            }
          });
          put("芰", new ArrayList<String>() {
            { add("芰"); }
          });
          put("想", new ArrayList<String>() {
            { add("想上你"); }
          });
          put("惹", new ArrayList<String>() {
            {
              add("惹火自拍");
              add("惹火身材");
            }
          });
          put("q", new ArrayList<String>() {
            {
              add("qingsewuyuetian.1174.net.cn");
              add("quannengshenjiao");
              add("qixingnet.com");
              add("qiangjian");
              add("qqsg.org");
              add("qgqm.org");
              add("qtoy.com");
            }
          });
          put("翮", new ArrayList<String>() {
            { add("翮"); }
          });
          put("翥", new ArrayList<String>() {
            { add("翥"); }
          });
          put("虢", new ArrayList<String>() {
            { add("虢"); }
          });
          put("ㄅ", new ArrayList<String>() {
            { add("ㄅ"); }
          });
          put("墨", new ArrayList<String>() {
            { add("墨索里尼"); }
          });
          put("ㄇ", new ArrayList<String>() {
            { add("ㄇ"); }
          });
          put("西", new ArrayList<String>() {
            {
              add("西藏314事件");
              add("西藏之声");
              add("西藏自由");
              add("西哈努克");
              add("西藏独立");
              add("西藏分裂");
              add("西洋美女");
              add("西藏天葬");
              add("西藏獨");
              add("西藏国");
              add("西藏");
            }
          });
          put("瀵", new ArrayList<String>() {
            { add("瀵"); }
          });
          put("虎", new ArrayList<String>() {
            {
              add("虎门");
              add("虎骑");
            }
          });
          put("虐", new ArrayList<String>() {
            {
              add("虐奴");
              add("虐待");
            }
          });
          put("處", new ArrayList<String>() {
            { add("處女膜"); }
          });
          put("虛", new ArrayList<String>() {
            {
              add("虛無的飽食者");
              add("虛弱圖騰");
            }
          });
          put("ㄎ", new ArrayList<String>() {
            { add("ㄎ"); }
          });
          put("諾", new ArrayList<String>() {
            { add("諾姆"); }
          });
          put("諼", new ArrayList<String>() {
            { add("諼"); }
          });
          put("諶", new ArrayList<String>() {
            { add("諶"); }
          });
          put("鋝", new ArrayList<String>() {
            { add("鋝"); }
          });
          put("ㄊ", new ArrayList<String>() {
            { add("ㄊ"); }
          });
          put("璩", new ArrayList<String>() {
            {
              add("璩美凤");
              add("璩");
            }
          });
          put("諞", new ArrayList<String>() {
            { add("諞"); }
          });
          put("諑", new ArrayList<String>() {
            { add("諑"); }
          });
          put("論", new ArrayList<String>() {
            { add("論壇管理員"); }
          });
          put("諗", new ArrayList<String>() {
            { add("諗"); }
          });
          put("諏", new ArrayList<String>() {
            { add("諏"); }
          });
          put("酹", new ArrayList<String>() {
            { add("酹"); }
          });
          put("藏", new ArrayList<String>() {
            {
              add("藏青会");
              add("藏妇会");
              add("藏独");
            }
          });
          put("崗", new ArrayList<String>() {
            { add("崗哨士兵"); }
          });
          put("G", new ArrayList<String>() {
            {
              add("GAMEMASTER");
              add("GameMaster");
              add("Gruepin");
              add("GRUEPIN");
              add("GAN你");
              add("G巴");
              add("G八");
              add("G掰");
              add("G叭");
              add("G芭");
              add("GCD");
              add("Gm");
              add("GM");
            }
          });
          put("酱", new ArrayList<String>() {
            { add("酱猪媳"); }
          });
          put("流", new ArrayList<String>() {
            {
              add("流蜜汁");
              add("流淫");
              add("流氓");
            }
          });
          put("酥", new ArrayList<String>() {
            {
              add("酥胸诱惑");
              add("酥痒");
              add("酥穴");
            }
          });
          put("测", new ArrayList<String>() {
            {
              add("测绘局");
              add("测试");
              add("测拿");
            }
          });
          put("海", new ArrayList<String>() {
            {
              add("海洛因");
              add("海洋局");
            }
          });
          put("浴", new ArrayList<String>() {
            {
              add("浴室乱伦");
              add("浴室自拍");
              add("浴尿");
            }
          });
          put("藪", new ArrayList<String>() {
            { add("藪"); }
          });
          put("酒", new ArrayList<String>() {
            { add("酒店援交"); }
          });
          put("浪", new ArrayList<String>() {
            {
              add("浪叫");
              add("浪女");
              add("浪妇");
              add("浪臀");
              add("浪穴");
            }
          });
          put("酆", new ArrayList<String>() {
            { add("酆"); }
          });
          put("群", new ArrayList<String>() {
            {
              add("群奸乱交");
              add("群奸轮射");
              add("群交亂舞");
              add("群魔色舞");
              add("群阴会");
              add("群交");
              add("群奸");
              add("群P");
            }
          });
          put("思", new ArrayList<String>() {
            { add("思科羅"); }
          });
          put("脚", new ArrayList<String>() {
            { add("脚交"); }
          });
          put("羼", new ArrayList<String>() {
            { add("羼"); }
          });
          put("羆", new ArrayList<String>() {
            { add("羆"); }
          });
          put("羅", new ArrayList<String>() {
            {
              add("羅文嘉");
              add("羅福助");
              add("羅志明");
              add("羅禮詩");
              add("羅幹");
            }
          });
          put("羋", new ArrayList<String>() {
            { add("羋"); }
          });
          put("美", new ArrayList<String>() {
            {
              add("美女吞精");
              add("美臀嫰穴");
              add("美乳美穴");
              add("美体艳姿");
              add("美乳斗艳");
              add("美女走光");
              add("美女成人");
              add("美女高潮");
              add("美女祼聊");
              add("美少妇");
              add("美利坚");
              add("美国佬");
              add("美骚妇");
              add("美乳");
              add("美国");
              add("美穴");
            }
          });
          put("总", new ArrayList<String>() {
            {
              add("总理");
              add("总局");
            }
          });
          put("性", new ArrayList<String>() {
            {
              add("性感诱惑");
              add("性虎色网");
              add("性感肉丝");
              add("性爱擂台");
              add("性交无码");
              add("性战擂台");
              add("性愛圖片");
              add("性感乳娘");
              add("性愛插穴");
              add("性交自拍");
              add("性感妖娆");
              add("性交吞精");
              add("性爱图库");
              add("性高潮");
              add("性之站");
              add("性交图");
              add("性虐待");
              add("性交课");
              add("性骚扰");
              add("性無能");
              add("性奴会");
              add("性交易");
              add("性饥渴");
              add("性无能");
              add("性乐");
              add("性爱");
              add("性佣");
              add("性欲");
              add("性虐");
              add("性虎");
              add("性息");
              add("性感");
              add("性愛");
              add("性事");
              add("性病");
              add("性奴");
              add("性交");
            }
          });
          put("2", new ArrayList<String>() {
            {
              add("21世纪中国基金会");
              add("2y7v.cnhwxvote.cn");
              add("208.43.198.56");
              add("222se图片");
              add("2feiche.cn");
              add("202333.com");
            }
          });
          put("羞", new ArrayList<String>() {
            { add("羞耻母"); }
          });
          put("斫", new ArrayList<String>() {
            { add("斫"); }
          });
          put("齔", new ArrayList<String>() {
            { add("齔"); }
          });
          put("斧", new ArrayList<String>() {
            { add("斧头镰刀"); }
          });
          put("齟", new ArrayList<String>() {
            { add("齟"); }
          });
          put("方", new ArrayList<String>() {
            { add("方勵之"); }
          });
          put("齊", new ArrayList<String>() {
            {
              add("齊墨");
              add("齊諾");
            }
          });
          put("新", new ArrayList<String>() {
            {
              add("新纪元周刊387期");
              add("新唐人电视台网");
              add("新唐人电视台");
              add("新聞出版總署");
              add("新闻出版总署");
              add("新疆7.5事件");
              add("新台湾新闻");
              add("新手訓練營");
              add("新闻出版署");
              add("新華通論壇");
              add("新觀察論壇");
              add("新華舉報");
              add("新疆分裂");
              add("新疆独立");
              add("新报网站");
              add("新闻管制");
              add("新约教会");
              add("新華內情");
              add("新聞封鎖");
              add("新三才");
              add("新头壳");
              add("新語絲");
              add("新義安");
              add("新纪元");
              add("新疆国");
              add("新史记");
              add("新疆獨");
              add("新生網");
              add("新党");
              add("新疆");
            }
          });
          put("嗎", new ArrayList<String>() {
            {
              add("嗎的");
              add("嗎啡");
            }
          });
          put("齠", new ArrayList<String>() {
            { add("齠"); }
          });
          put("r", new ArrayList<String>() {
            {
              add("renmindahuitang");
              add("reddidi.com.cn");
              add("rrr.xaoh.cn");
              add("renmingbao");
              add("renminbao");
              add("rivals");
              add("renaya");
              add("rfa");
            }
          });
          put("嗑", new ArrayList<String>() {
            { add("嗑药"); }
          });
          put("齬", new ArrayList<String>() {
            { add("齬"); }
          });
          put("陬", new ArrayList<String>() {
            { add("陬"); }
          });
          put("陰", new ArrayList<String>() {
            {
              add("陰穴新玩法");
              add("陰蒂");
              add("陰莖");
              add("陰唇");
              add("陰門");
              add("陰水");
              add("陰囊");
              add("陰部");
              add("陰精");
              add("陰毛");
              add("陰道");
              add("陰戶");
            }
          });
          put("陳", new ArrayList<String>() {
            {
              add("陳宣良");
              add("陳一諮");
              add("陳良宇");
              add("陳總統");
              add("陳希同");
              add("陳學聖");
              add("陳破空");
              add("陳景俊");
              add("陳炳基");
              add("陳建銘");
              add("陳定南");
              add("陳小同");
              add("陳唐山");
              add("陳水扁");
              add("陳博志");
              add("陳軍");
              add("陳菊");
              add("陳蒙");
            }
          });
          put("陽", new ArrayList<String>() {
            {
              add("陽具");
              add("陽物");
              add("陽痿");
            }
          });
          put("陸", new ArrayList<String>() {
            { add("陸委會"); }
          });
          put("陆", new ArrayList<String>() {
            { add("陆雪琴"); }
          });
          put("降", new ArrayList<String>() {
            { add("降低命中的圖騰"); }
          });
          put("轉", new ArrayList<String>() {
            { add("轉化"); }
          });
          put("鯴", new ArrayList<String>() {
            { add("鯴"); }
          });
          put("轂", new ArrayList<String>() {
            { add("轂"); }
          });
          put("鯫", new ArrayList<String>() {
            { add("鯫"); }
          });
          put("鯢", new ArrayList<String>() {
            { add("鯢"); }
          });
          put("笨", new ArrayList<String>() {
            {
              add("笨逼");
              add("笨屄");
            }
          });
          put("鯝", new ArrayList<String>() {
            { add("鯝"); }
          });
          put("鯗", new ArrayList<String>() {
            { add("鯗"); }
          });
          put("车", new ArrayList<String>() {
            {
              add("车仑");
              add("车臣");
            }
          });
          put("鯔", new ArrayList<String>() {
            { add("鯔"); }
          });
          put("轡", new ArrayList<String>() {
            { add("轡"); }
          });
          put("轢", new ArrayList<String>() {
            { add("轢"); }
          });
          put("常", new ArrayList<String>() {
            {
              add("常受教");
              add("常勁");
            }
          });
          put("轴", new ArrayList<String>() {
            { add("轴永康"); }
          });
          put("鯁", new ArrayList<String>() {
            { add("鯁"); }
          });
          put("鯀", new ArrayList<String>() {
            { add("鯀"); }
          });
          put("鄺", new ArrayList<String>() {
            { add("鄺錦文"); }
          });
          put("鄹", new ArrayList<String>() {
            { add("鄹"); }
          });
          put("舂", new ArrayList<String>() {
            { add("舂"); }
          });
          put("鄭", new ArrayList<String>() {
            {
              add("鄭寶清");
              add("鄭運鵬");
              add("鄭餘鎮");
              add("鄭麗文");
              add("鄭源");
              add("鄭義");
            }
          });
          put("舆", new ArrayList<String>() {
            { add("舆论钳制"); }
          });
          put("舊", new ArrayList<String>() {
            { add("舊斗篷哨兵"); }
          });
          put("舌", new ArrayList<String>() {
            { add("舌头穴"); }
          });
          put("舳", new ArrayList<String>() {
            { add("舳"); }
          });
          put("峴", new ArrayList<String>() {
            { add("峴"); }
          });
          put("溷", new ArrayList<String>() {
            { add("溷"); }
          });
          put("溫", new ArrayList<String>() {
            {
              add("溫元凱");
              add("溫家寶");
              add("溫逼");
              add("溫比");
              add("溫B");
            }
          });
          put("鄄", new ArrayList<String>() {
            { add("鄄"); }
          });
          put("克", new ArrayList<String>() {
            {
              add("克林顿");
              add("克勞森");
              add("克萊特");
              add("克萊恩");
            }
          });
          put("H", new ArrayList<String>() {
            {
              add("Hong Kong Herald. 台湾报纸");
              add("Hong Kong Herald.");
              add("Huihuangtx.com");
              add("HYPERMART.NET");
              add("HRICHINA");
              add("H动漫");
              add("HACKING");
              add("HONGZHI");
              add("HUANET");
              add("HTTP");
            }
          });
          put("热", new ArrayList<String>() {
            {
              add("热比娅");
              add("热那亚");
            }
          });
          put("烂", new ArrayList<String>() {
            {
              add("烂游戏");
              add("烂屌");
              add("烂比");
              add("烂逼");
              add("烂鸟");
              add("烂屄");
              add("烂人");
              add("烂货");
              add("烂B");
            }
          });
          put("蚍", new ArrayList<String>() {
            { add("蚍"); }
          });
          put("塞", new ArrayList<String>() {
            {
              add("塞你老师");
              add("塞你老母");
              add("塞你公");
              add("塞你娘");
              add("塞你爸");
              add("塞你母");
              add("塞白");
            }
          });
          put("塔", new ArrayList<String>() {
            {
              add("塔利班");
              add("塔內");
              add("塔烏");
            }
          });
          put("烟", new ArrayList<String>() {
            { add("烟草局"); }
          });
          put("兽", new ArrayList<String>() {
            {
              add("兽兽门");
              add("兽交");
              add("兽奸");
              add("兽欲");
            }
          });
          put("兴", new ArrayList<String>() {
            { add("兴奋剂"); }
          });
          put("共", new ArrayList<String>() {
            {
              add("共産主義");
              add("共榮圈");
              add("共産黨");
              add("共黨");
              add("共党");
              add("共狗");
              add("共匪");
              add("共軍");
              add("共産");
            }
          });
          put("六", new ArrayList<String>() {
            {
              add("六月联盟");
              add("六四事件");
              add("六四真相");
              add("六四运动");
              add("六合彩");
              add("六。四");
              add("六?四");
              add("六-四");
              add("六.四");
              add("六四");
            }
          });
          put("公", new ArrayList<String>() {
            {
              add("公安局");
              add("公媳乱");
              add("公教报");
              add("公安部");
              add("公安");
            }
          });
          put("兩", new ArrayList<String>() {
            {
              add("兩岸三地論壇");
              add("兩個中國");
              add("兩會新聞");
              add("兩會報道");
              add("兩岸關係");
              add("兩會");
            }
          });
          put("全", new ArrayList<String>() {
            {
              add("全范围教会");
              add("全國兩會");
              add("全能神教");
              add("全國人大");
              add("全国人大");
              add("全能神");
              add("全裸");
            }
          });
          put("八", new ArrayList<String>() {
            {
              add("八方国际资讯");
              add("八九风波");
              add("八一东突");
              add("八九");
            }
          });
          put("入", new ArrayList<String>() {
            { add("入穴一游"); }
          });
          put("內", new ArrayList<String>() {
            {
              add("內褲");
              add("內衣");
            }
          });
          put("党", new ArrayList<String>() {
            {
              add("党主席");
              add("党中央");
            }
          });
          put("免", new ArrayList<String>() {
            {
              add("免费成人网站");
              add("免费偷窥网");
              add("免费A片");
            }
          });
          put("3", new ArrayList<String>() {
            {
              add("365tttyx.com");
              add("33bbb走光");
              add("3kwow.com");
              add("37447.cn");
              add("30杂志");
              add("3P炮图");
              add("365gn");
              add("3P");
            }
          });
          put("先", new ArrayList<String>() {
            { add("先奸后杀"); }
          });
          put("膏", new ArrayList<String>() {
            {
              add("膏藥旗");
              add("膏药旗");
            }
          });
          put("膁", new ArrayList<String>() {
            { add("膁"); }
          });
          put("膃", new ArrayList<String>() {
            { add("膃"); }
          });
          put("元", new ArrayList<String>() {
            { add("元老蘭提(沃德）"); }
          });
          put("㈠", new ArrayList<String>() {
            { add("㈠"); }
          });
          put("㈡", new ArrayList<String>() {
            { add("㈡"); }
          });
          put("㈢", new ArrayList<String>() {
            { add("㈢"); }
          });
          put("㈣", new ArrayList<String>() {
            { add("㈣"); }
          });
          put("㈥", new ArrayList<String>() {
            { add("㈥"); }
          });
          put("㈧", new ArrayList<String>() {
            { add("㈧"); }
          });
          put("㈨", new ArrayList<String>() {
            { add("㈨"); }
          });
          put("㈩", new ArrayList<String>() {
            { add("㈩"); }
          });
          put("㈱", new ArrayList<String>() {
            { add("㈱"); }
          });
          put("椠", new ArrayList<String>() {
            { add("椠"); }
          });
          put("鲰", new ArrayList<String>() {
            { add("鲰"); }
          });
          put("s", new ArrayList<String>() {
            {
              add("sewuyuetian.1274.net.cn");
              add("sewuyuetian.1174.net.cn");
              add("siwameitui.1274.net.cn");
              add("shanbenwushiliu");
              add("sex聊天室");
              add("sunzhongshan");
              add("showka8.com");
              add("shijiamouni");
              add("shenycs.co");
              add("stlmbbs.cn");
              add("sjlike.com");
              add("szlmgh.cn");
              add("sunyixian");
              add("songchuyu");
              add("samalanqi");
              add("sm调教");
              add("saqieer");
              add("sidalin");
              add("safeweb");
              add("simple");
              add("system");
              add("string");
              add("sucker");
              add("sunwen");
              add("sadamu");
              add("saobi");
              add("svdc");
              add("shit");
              add("sex");
              add("sm");
              add("sb");
              add("sf");
            }
          });
          put("篋", new ArrayList<String>() {
            { add("篋"); }
          });
          put("曹", new ArrayList<String>() {
            {
              add("曹長青");
              add("曹刚川");
              add("曹剛川");
              add("曹政");
            }
          });
          put("辛", new ArrayList<String>() {
            {
              add("辛灝年");
              add("辛子陵");
            }
          });
          put("鮮", new ArrayList<String>() {
            { add("鮮族"); }
          });
          put("辅", new ArrayList<String>() {
            { add("辅助程序"); }
          });
          put("鮪", new ArrayList<String>() {
            { add("鮪"); }
          });
          put("辏", new ArrayList<String>() {
            { add("辏"); }
          });
          put("鮞", new ArrayList<String>() {
            { add("鮞"); }
          });
          put("丬", new ArrayList<String>() {
            { add("丬"); }
          });
          put("鮐", new ArrayList<String>() {
            { add("鮐"); }
          });
          put("鮑", new ArrayList<String>() {
            {
              add("鮑戈");
              add("鮑彤");
              add("鮑伊");
            }
          });
          put("达", new ArrayList<String>() {
            {
              add("达赖喇嘛的智慧箴言");
              add("达米宣教会");
              add("达赖喇嘛");
              add("达菲鸡");
              add("达赖");
            }
          });
          put("曰", new ArrayList<String>() {
            {
              add("曰你");
              add("曰gM");
              add("曰gm");
              add("曰GM");
              add("曰Gm");
            }
          });
          put("机", new ArrayList<String>() {
            {
              add("机八");
              add("机巴");
              add("机芭");
              add("机掰");
              add("机叭");
              add("机Ｙ");
              add("机8");
              add("机Y");
            }
          });
          put("曲", new ArrayList<String>() {
            { add("曲线消魂"); }
          });
          put("繢", new ArrayList<String>() {
            { add("繢"); }
          });
          put("恝", new ArrayList<String>() {
            { add("恝"); }
          });
          put("恐", new ArrayList<String>() {
            {
              add("恐怖份子");
              add("恐怖组织");
              add("恐怖主义");
              add("恐怖主義");
            }
          });
          put("恋", new ArrayList<String>() {
            { add("恋孕"); }
          });
          put("繰", new ArrayList<String>() {
            { add("繰"); }
          });
          put("恩", new ArrayList<String>() {
            { add("恩格斯"); }
          });
          put("睾", new ArrayList<String>() {
            {
              add("睾丸");
              add("睾");
            }
          });
          put("銚", new ArrayList<String>() {
            { add("銚"); }
          });
          put("謦", new ArrayList<String>() {
            { add("謦"); }
          });
          put("銎", new ArrayList<String>() {
            { add("銎"); }
          });
          put("謝", new ArrayList<String>() {
            {
              add("謝深山");
              add("謝中之");
              add("謝長廷");
              add("謝選駿");
            }
          });
          put("講", new ArrayList<String>() {
            { add("講法"); }
          });
          put("謄", new ArrayList<String>() {
            { add("謄"); }
          });
          put("寂", new ArrayList<String>() {
            { add("寂寞自摸"); }
          });
          put("寇", new ArrayList<String>() {
            { add("寇晓伟"); }
          });
          put("密", new ArrayList<String>() {
            {
              add("密室淫行");
              add("密穴贴图");
              add("密宗");
              add("密穴");
              add("密洞");
            }
          });
          put("I", new ArrayList<String>() {
            {
              add("INCEST");
              add("ISIS");
              add("Isis");
              add("ITEM");
              add("ISIs");
              add("ISis");
              add("ISIL");
            }
          });
          put("富", new ArrayList<String>() {
            { add("富兰克林"); }
          });
          put("阿", new ArrayList<String>() {
            {
              add("阿鲁纳恰尔邦");
              add("阿波罗新闻网");
              add("阿拉法特");
              add("阿弥陀佛");
              add("阿扁萬歲");
              add("阿波罗网");
              add("阿扁万岁");
              add("阿萊娜");
              add("阿罗约");
              add("阿扁");
              add("阿拉");
            }
          });
          put("阼", new ArrayList<String>() {
            { add("阼"); }
          });
          put("防", new ArrayList<String>() {
            { add("防衛指揮官"); }
          });
          put("阳", new ArrayList<String>() {
            {
              add("阳光时务周刊");
              add("阳物");
              add("阳精");
              add("阳痿");
              add("阳具");
            }
          });
          put("阴", new ArrayList<String>() {
            {
              add("阴茎插小穴");
              add("阴阜高耸");
              add("阴道图片");
              add("阴小撕大");
              add("阴部特写");
              add("阴户");
              add("阴水");
              add("阴道");
              add("阴部");
              add("阴核");
              add("阴蒂");
              add("阴唇");
              add("阴阜");
              add("阴毛");
              add("阴缔");
              add("阴门");
              add("阴屄");
              add("阴茎");
              add("阴精");
            }
          });
          put("阎", new ArrayList<String>() {
            { add("阎王"); }
          });
          put("寶", new ArrayList<String>() {
            { add("寶石商人"); }
          });
          put("阝", new ArrayList<String>() {
            {
              add("阝月");
              add("阝");
            }
          });
          put("寻", new ArrayList<String>() {
            { add("寻欢"); }
          });
          put("导", new ArrayList<String>() {
            { add("导弹"); }
          });
          put("儂", new ArrayList<String>() {
            {
              add("儂著岡巒");
              add("儂著卵拋");
            }
          });
          put("楊", new ArrayList<String>() {
            {
              add("楊懷安");
              add("楊建利");
              add("楊月清");
              add("楊周");
              add("楊巍");
            }
          });
          put("4", new ArrayList<String>() {
            { add("47513.cn"); }
          });
          put("儺", new ArrayList<String>() {
            { add("儺"); }
          });
          put("儿", new ArrayList<String>() {
            { add("儿子"); }
          });
          put("滚", new ArrayList<String>() {
            { add("滚"); }
          });
          put("艟", new ArrayList<String>() {
            { add("艟"); }
          });
          put("岍", new ArrayList<String>() {
            { add("岍"); }
          });
          put("色", new ArrayList<String>() {
            {
              add("色情小电影");
              add("色狐狸网址");
              add("色色五月天");
              add("色色成人");
              add("色情服务");
              add("色色婷婷");
              add("色情电影");
              add("色狼小说");
              add("色情论坛");
              add("色狼论坛");
              add("色情工厂");
              add("色窝窝");
              add("色迷城");
              add("色书库");
              add("色97爱");
              add("色界");
              add("色色");
              add("色猫");
              add("色欲");
              add("色诱");
              add("色区");
              add("色情");
              add("色撸");
            }
          });
          put("艳", new ArrayList<String>() {
            {
              add("艳妇淫女");
              add("艳舞淫业");
              add("艳情小说");
              add("艳乳");
              add("艳照");
            }
          });
          put("滾", new ArrayList<String>() {
            {
              add("滾那嗎老比");
              add("滾那嗎錯比");
              add("滾那嗎瘟比");
              add("滾那嗎B");
              add("滾那嗎");
            }
          });
          put("艾", new ArrayList<String>() {
            {
              add("艾森豪威尔");
              add("艾麗絲");
              add("艾滋病");
            }
          });
          put("t", new ArrayList<String>() {
            {
              add("tb.hi.4024.net.cn");
              add("tianlong4f.cn");
              add("triangleboy");
              add("tangjiaxuan");
              add("taiwanduli");
              add("tiananmen");
              add("triangle");
              add("txwsWind");
              add("tibetalk");
              add("thec.cn");
              add("tont.cn");
              add("t9wg.cn");
              add("taobao");
              add("taidu");
              add("teen");
              add("tnnd");
              add("taip");
              add("test");
              add("tEST");
              add("tESt");
              add("tmd");
            }
          });
          put("滥", new ArrayList<String>() {
            {
              add("滥交");
              add("滥情");
            }
          });
          put("喬", new ArrayList<String>() {
            {
              add("喬伊");
              add("喬石");
            }
          });
          put("早", new ArrayList<String>() {
            { add("早泄"); }
          });
          put("１", new ArrayList<String>() {
            { add("１８歲淫亂"); }
          });
          put("日", new ArrayList<String>() {
            {
              add("日內瓦金融");
              add("日本AV女优");
              add("日本帝國");
              add("日本熟母");
              add("日本有码");
              add("日你爺爺");
              add("日本灌肠");
              add("日本素人");
              add("日本鬼子");
              add("日本骚货");
              add("日本RING");
              add("日他娘");
              add("日你媽");
              add("日死你");
              add("日朱駿");
              add("日你娘");
              add("日你妈");
              add("日X妈");
              add("日屄");
              add("日逼");
              add("日你");
              add("日死");
              add("日軍");
              add("日GM");
              add("日gm");
              add("日Gm");
              add("日gM");
              add("日");
            }
          });
          put("无", new ArrayList<String>() {
            {
              add("无界浏览器");
              add("无毛美少女");
              add("无码丝袜");
              add("无套自拍");
              add("无码淫漫");
              add("无码无套");
              add("无码彩图");
              add("无码淫女");
              add("无套内射");
              add("无码炮图");
              add("无码长片");
              add("无码体验");
              add("无码精选");
              add("无码做爱");
              add("无帮过");
              add("无修正");
              add("无帮国");
              add("无邦国");
              add("无码");
            }
          });
          put("鼙", new ArrayList<String>() {
            { add("鼙"); }
          });
          put("旺", new ArrayList<String>() {
            { add("旺报"); }
          });
          put("鼉", new ArrayList<String>() {
            { add("鼉"); }
          });
          put("旅", new ArrayList<String>() {
            {
              add("旅馆自拍");
              add("旅游局");
            }
          });
          put("善", new ArrayList<String>() {
            { add("善惡有報"); }
          });
          put("喟", new ArrayList<String>() {
            { add("喟"); }
          });
          put("臺", new ArrayList<String>() {
            {
              add("臺灣建國運動組織");
              add("臺灣青年獨立聯盟");
              add("臺灣自由聯盟");
              add("臺灣政論區");
              add("臺灣共産黨");
              add("臺灣帝國");
              add("臺灣民國");
              add("臺灣獨立");
              add("臺灣狗");
              add("臺灣獨");
              add("臺盟");
            }
          });
          put("臭", new ArrayList<String>() {
            {
              add("臭化西");
              add("臭七八");
              add("臭７８");
              add("臭鸡巴");
              add("臭ＧＹ");
              add("臭人也");
              add("臭女也");
              add("臭机八");
              add("臭你");
              add("臭机");
              add("臭她");
              add("臭鸡");
              add("臭妳");
              add("臭西");
              add("臭他");
              add("臭78");
              add("臭GY");
            }
          });
          put("自", new ArrayList<String>() {
            {
              add("自由民主論壇");
              add("自由欧洲电台");
              add("自由民主论坛");
              add("自由亚洲电台");
              add("自已的故事");
              add("自由新闻报");
              add("自杀指南");
              add("自拍写真");
              add("自制手枪");
              add("自立晚报");
              add("自插小穴");
              add("自慰抠穴");
              add("自杀手册");
              add("自治机关");
              add("自由时报");
              add("自拍美穴");
              add("自民黨");
              add("自民党");
              add("自慰");
              add("自焚");
            }
          });
          put("臀", new ArrayList<String>() {
            { add("臀浪"); }
          });
          put("臁", new ArrayList<String>() {
            { add("臁"); }
          });
          put("宄", new ArrayList<String>() {
            { add("宄"); }
          });
          put("宇", new ArrayList<String>() {
            { add("宇明網"); }
          });
          put("安", new ArrayList<String>() {
            {
              add("安非他命");
              add("安倍晋三");
              add("安全局");
              add("安南");
              add("安拉");
            }
          });
          put("宋", new ArrayList<String>() {
            {
              add("宋楚瑜");
              add("宋書元");
              add("宋祖英");
            }
          });
          put("完", new ArrayList<String>() {
            { add("完蛋操"); }
          });
          put("宗", new ArrayList<String>() {
            { add("宗教"); }
          });
          put("官", new ArrayList<String>() {
            {
              add("官逼民反");
              add("官商勾结");
              add("官方");
            }
          });
          put("实", new ArrayList<String>() {
            { add("实际神"); }
          });
          put("审", new ArrayList<String>() {
            { add("审查"); }
          });
          put("客", new ArrayList<String>() {
            {
              add("客家电视台");
              add("客户服务");
              add("客戶服務");
              add("客服");
            }
          });
          put("韉", new ArrayList<String>() {
            { add("韉"); }
          });
          put("韞", new ArrayList<String>() {
            { add("韞"); }
          });
          put("韝", new ArrayList<String>() {
            { add("韝"); }
          });
          put("害", new ArrayList<String>() {
            { add("害羞"); }
          });
          put("粗", new ArrayList<String>() {
            { add("粗制吗啡"); }
          });
          put("韓", new ArrayList<String>() {
            {
              add("韓聯潮");
              add("韓東方");
              add("韓正");
            }
          });
          put("宾", new ArrayList<String>() {
            {
              add("宾馆女郎");
              add("宾周");
            }
          });
          put("运", new ArrayList<String>() {
            {
              add("运营商");
              add("运营长");
              add("运营者");
              add("运营官");
              add("运营人");
              add("运营组");
              add("运营");
            }
          });
          put("近", new ArrayList<String>() {
            {
              add("近親相姦");
              add("近亲相奸");
            }
          });
          put("连", new ArrayList<String>() {
            {
              add("连続失禁");
              add("连战");
            }
          });
          put("迟", new ArrayList<String>() {
            { add("迟浩田"); }
          });
          put("远", new ArrayList<String>() {
            { add("远程偷拍"); }
          });
          put("J", new ArrayList<String>() {
            {
              add("JIANGDONGRIJI");
              add("JIAOCHUANG");
              add("JIAOCHUN");
              add("JIAN你");
              add("JI女");
              add("Ji女");
              add("JIBA");
              add("JINV");
              add("J8");
              add("JB");
            }
          });
          put("e", new ArrayList<String>() {
            {
              add("eee.xaoh.cn");
              add("ent365.com");
              add("e7sw.cn");
              add("eerdeni");
              add("engesi");
            }
          });
          put("迷", new ArrayList<String>() {
            {
              add("迷奸系列");
              add("迷幻药");
              add("迷奸药");
              add("迷魂药");
              add("迷歼药");
              add("迷信");
              add("迷奸");
              add("迷药");
            }
          });
          put("追", new ArrayList<String>() {
            { add("追查国际"); }
          });
          put("管", new ArrayList<String>() {
            {
              add("管理员");
              add("管里");
              add("管理");
            }
          });
          put("迪", new ArrayList<String>() {
            { add("迪裏夏提"); }
          });
          put("迫", new ArrayList<String>() {
            { add("迫奸"); }
          });
          put("蛩", new ArrayList<String>() {
            { add("蛩鼽"); }
          });
          put("炮", new ArrayList<String>() {
            {
              add("炮友之家");
              add("炮友");
            }
          });
          put("蛤", new ArrayList<String>() {
            { add("蛤蟆"); }
          });
          put("5", new ArrayList<String>() {
            {
              add("55sss偷拍区");
              add("51juezhan.com");
              add("58.253.67.74");
              add("51jiafen.cn");
              add("567567aa.cn");
              add("54hero.com");
              add("5m5m5m.com");
              add("52ppsa.cn");
              add("52sxhy.cn");
              add("597ft.com");
              add("51hdw.com");
              add("5d6d.com");
            }
          });
          put("堕", new ArrayList<String>() {
            { add("堕淫"); }
          });
          put("護", new ArrayList<String>() {
            { add("護法"); }
          });
          put("議", new ArrayList<String>() {
            {
              add("議員斯格文德");
              add("議長阿茵斯塔");
            }
          });
          put("警", new ArrayList<String>() {
            {
              add("警匪一家");
              add("警奴");
            }
          });
          put("譙", new ArrayList<String>() {
            { add("譙"); }
          });
          put("真", new ArrayList<String>() {
            {
              add("真主安拉");
              add("真理教");
              add("真善忍");
              add("真主");
            }
          });
          put("譖", new ArrayList<String>() {
            { add("譖"); }
          });
          put("譎", new ArrayList<String>() {
            { add("譎"); }
          });
          put("看", new ArrayList<String>() {
            {
              add("看中国专栏");
              add("看中國");
              add("看下");
            }
          });
          put("u", new ArrayList<String>() {
            {
              add("urban-rivals");
              add("uu1001.com");
              add("ul86.com");
              add("ustibet");
              add("unixbox");
              add("urban");
              add("u r");
              add("ur");
            }
          });
          put("㊣", new ArrayList<String>() {
            { add("㊣"); }
          });
          put("僉", new ArrayList<String>() {
            { add("僉"); }
          });
          put("檠", new ArrayList<String>() {
            { add("檠"); }
          });
          put("僵", new ArrayList<String>() {
            {
              add("僵賊民");
              add("僵贼民");
              add("僵賊");
            }
          });
          put("檾", new ArrayList<String>() {
            { add("檾"); }
          });
          put("魅", new ArrayList<String>() {
            { add("魅惑巨乳"); }
          });
          put("縶", new ArrayList<String>() {
            { add("縶"); }
          });
          put("總", new ArrayList<String>() {
            { add("總理"); }
          });
          put("憂", new ArrayList<String>() {
            { add("憂鬱的艾拉"); }
          });
          put("瘛", new ArrayList<String>() {
            { add("瘛"); }
          });
          put("鳓", new ArrayList<String>() {
            { add("鳓"); }
          });
          put("K", new ArrayList<String>() {
            {
              add("KISSMYASS");
              add("K他命");
              add("KEFU");
              add("K粉");
              add("KeFu");
              add("Kefu");
              add("KAO");
              add("Kao");
            }
          });
          put("政", new ArrayList<String>() {
            {
              add("政治反對派");
              add("政治反对派");
              add("政治犯");
              add("政權");
              add("政变");
              add("政府");
              add("政治");
            }
          });
          put("放", new ArrayList<String>() {
            {
              add("放荡少妇宾馆");
              add("放荡熟女");
              add("放荡少妇");
              add("放尿");
              add("放蕩");
            }
          });
          put("支", new ArrayList<String>() {
            { add("支那"); }
          });
          put("啊", new ArrayList<String>() {
            { add("啊無卵"); }
          });
          put("商", new ArrayList<String>() {
            { add("商务部"); }
          });
          put("攄", new ArrayList<String>() {
            { add("攄"); }
          });
          put("鳢", new ArrayList<String>() {
            { add("鳢"); }
          });
          put("鈐", new ArrayList<String>() {
            { add("鈐"); }
          });
          put("石", new ArrayList<String>() {
            {
              add("石拳戰鬥兵");
              add("石化圖騰");
            }
          });
          put("矯", new ArrayList<String>() {
            { add("矯健的馬努爾"); }
          });
          put("0", new ArrayList<String>() {
            {
              add("025game.cn");
              add("0571qq.com");
              add("09city.com");
              add("01gh.com");
              add("00sm.cn");
            }
          });
          put("6", new ArrayList<String>() {
            {
              add("6-4tianwang");
              add("661661.com");
              add("69nb.com");
            }
          });
          put("鞯", new ArrayList<String>() {
            { add("鞯"); }
          });
          put("鞽", new ArrayList<String>() {
            { add("鞽"); }
          });
          put("讓", new ArrayList<String>() {
            { add("讓你操"); }
          });
          put("子", new ArrayList<String>() {
            {
              add("子女任职名单");
              add("子宮");
              add("子宫");
            }
          });
          put("讖", new ArrayList<String>() {
            { add("讖"); }
          });
          put("鞴", new ArrayList<String>() {
            { add("鞴"); }
          });
          put("孙", new ArrayList<String>() {
            {
              add("孙逸仙");
              add("孙中山");
              add("孙文");
            }
          });
          put("讞", new ArrayList<String>() {
            { add("讞"); }
          });
          put("计", new ArrayList<String>() {
            {
              add("计牌软件");
              add("计生委");
            }
          });
          put("学", new ArrayList<String>() {
            {
              add("学生妹");
              add("学运");
              add("学潮");
            }
          });
          put("鞀", new ArrayList<String>() {
            { add("鞀"); }
          });
          put("孫", new ArrayList<String>() {
            {
              add("孫中山");
              add("孫大千");
            }
          });
          put("孱", new ArrayList<String>() {
            { add("孱"); }
          });
          put("v", new ArrayList<String>() {
            {
              add("vip886.com");
              add("voachinese");
              add("voa");
            }
          });
          put("學", new ArrayList<String>() {
            {
              add("學生妹");
              add("學自聯");
              add("學潮");
              add("學聯");
              add("學運");
            }
          });
          put("屙", new ArrayList<String>() {
            { add("屙"); }
          });
          put("威", new ArrayList<String>() {
            {
              add("威而钢");
              add("威而柔");
            }
          });
          put("届", new ArrayList<String>() {
            { add("届中央政治局委員"); }
          });
          put("屌", new ArrayList<String>() {
            {
              add("屌女也");
              add("屌人也");
              add("屌你");
              add("屌西");
              add("屌七");
              add("屌他");
              add("屌妳");
              add("屌鸠");
              add("屌她");
              add("屌毛");
              add("屌我");
              add("屌7");
              add("屌");
            }
          });
          put("屎", new ArrayList<String>() {
            {
              add("屎妳娘");
              add("屎你娘");
              add("屎");
            }
          });
          put("清", new ArrayList<String>() {
            { add("清晰内射"); }
          });
          put("屄", new ArrayList<String>() {
            {
              add("屄屄特写");
              add("屄毛");
              add("屄屄");
              add("屄");
            }
          });
          put("屺", new ArrayList<String>() {
            { add("屺"); }
          });
          put("花", new ArrayList<String>() {
            {
              add("花样性交");
              add("花花公子");
              add("花柳");
            }
          });
          put("游", new ArrayList<String>() {
            {
              add("游戲管理員");
              add("游蕩的僵尸");
              add("游戏管理员");
              add("游戏宫理员");
              add("游蕩的士兵");
              add("游戏发奖员");
              add("游蕩爪牙");
              add("游錫坤");
              add("游行");
            }
          });
          put("山", new ArrayList<String>() {
            {
              add("山本五十六");
              add("山口組");
            }
          });
          put("屨", new ArrayList<String>() {
            { add("屨"); }
          });
          put("港", new ArrayList<String>() {
            {
              add("港澳办");
              add("港独");
            }
          });
          put("温", new ArrayList<String>() {
            {
              add("温总理");
              add("温家宝");
              add("温假饱");
              add("温加宝");
              add("温家饱");
              add("温家保");
              add("温馨");
            }
          });
          put("屠", new ArrayList<String>() {
            {
              add("屠杀");
              add("屠殺");
            }
          });
          put("渣", new ArrayList<String>() {
            { add("渣波波"); }
          });
          put("屦", new ArrayList<String>() {
            { add("屦"); }
          });
          put("獫", new ArrayList<String>() {
            { add("獫"); }
          });
          put("獨", new ArrayList<String>() {
            {
              add("獨立臺灣會");
              add("獨裁政治");
              add("獨夫");
              add("獨裁");
            }
          });
          put("忽", new ArrayList<String>() {
            { add("忽然1周"); }
          });
          put("獻", new ArrayList<String>() {
            { add("獻祭的圖騰"); }
          });
          put("獸", new ArrayList<String>() {
            { add("獸交"); }
          });
          put("快", new ArrayList<String>() {
            { add("快乐AV"); }
          });
          put("a", new ArrayList<String>() {
            {
              add("aa.yazhousetu.hi.9705.net.cn");
              add("aaac.s.51524.com");
              add("aaad.s.59764.com");
              add("administrator");
              add("aisenhaoweier");
              add("asgardcn.com");
              add("anbeijinsan");
              add("any2000.com");
              add("a33.com");
              add("asshole");
              add("aomaer");
              add("aobama");
              add("annan");
              add("admin");
              add("ai滋");
              add("aids");
              add("a4u");
              add("a4y");
            }
          });
          put("蜆", new ArrayList<String>() {
            { add("蜆"); }
          });
          put("心", new ArrayList<String>() {
            { add("心灵法门“白话佛法”系列节目"); }
          });
          put("蜜", new ArrayList<String>() {
            {
              add("蜜洞");
              add("蜜穴");
            }
          });
          put("裸", new ArrayList<String>() {
            {
              add("裸露自拍");
              add("裸体少妇");
              add("裸陪");
              add("裸聊");
              add("裸欲");
              add("裸体");
            }
          });
          put("鲚", new ArrayList<String>() {
            { add("鲚"); }
          });
          put("鲜", new ArrayList<String>() {
            { add("鲜族"); }
          });
          put("鲕", new ArrayList<String>() {
            { add("鲕"); }
          });
          put("數", new ArrayList<String>() {
            { add("數據中國"); }
          });
          put("鲍", new ArrayList<String>() {
            {
              add("鲍威尔");
              add("鲍岳桥");
            }
          });
          put("敬", new ArrayList<String>() {
            { add("敬国神社"); }
          });
          put("售", new ArrayList<String>() {
            {
              add("售软件");
              add("售号");
              add("售ID");
            }
          });
          put("唐", new ArrayList<String>() {
            {
              add("唐柏橋");
              add("唐家璇");
            }
          });
          put("鲻", new ArrayList<String>() {
            { add("鲻"); }
          });
          put("鲼", new ArrayList<String>() {
            { add("鲼"); }
          });
          put("唚", new ArrayList<String>() {
            { add("唚"); }
          });
          put("鲴", new ArrayList<String>() {
            { add("鲴"); }
          });
          put("教", new ArrayList<String>() {
            {
              add("教養院");
              add("教育部");
            }
          });
          put("鲣", new ArrayList<String>() {
            { add("鲣"); }
          });
          put("L", new ArrayList<String>() {
            { add("LIHONGZHI"); }
          });
          put("絕", new ArrayList<String>() {
            { add("絕望之地"); }
          });
          put("統", new ArrayList<String>() {
            {
              add("統獨論壇");
              add("統戰");
              add("統獨");
            }
          });
          put("谋", new ArrayList<String>() {
            { add("谋杀"); }
          });
          put("穆", new ArrayList<String>() {
            {
              add("穆罕默德");
              add("穆斯林");
            }
          });
          put("调", new ArrayList<String>() {
            {
              add("调教虐待");
              add("调教性奴");
              add("调戏");
              add("调教");
            }
          });
          put("積", new ArrayList<String>() {
            { add("積克館"); }
          });
          put("穷", new ArrayList<String>() {
            { add("穷逼"); }
          });
          put("穴", new ArrayList<String>() {
            {
              add("穴海");
              add("穴图");
              add("穴淫");
              add("穴爽");
              add("穴");
            }
          });
          put("捏", new ArrayList<String>() {
            {
              add("捏你鶏巴");
              add("捏弄");
            }
          });
          put("穿", new ArrayList<String>() {
            { add("穿越浏览器"); }
          });
          put("谲", new ArrayList<String>() {
            { add("谲"); }
          });
          put("空", new ArrayList<String>() {
            {
              add("空虛的伊坤");
              add("空虛之地");
              add("空氣精靈");
              add("空姐性交");
            }
          });
          put("7", new ArrayList<String>() {
            {
              add("7mmo.com");
              add("71ka.com");
              add("77bbb");
              add("7GG");
            }
          });
          put("懒", new ArrayList<String>() {
            {
              add("懒教");
              add("懒叫");
            }
          });
          put("维", new ArrayList<String>() {
            { add("维护"); }
          });
          put("懂", new ArrayList<String>() {
            { add("懂文华"); }
          });
          put("鬧", new ArrayList<String>() {
            { add("鬧事"); }
          });
          put("鬥", new ArrayList<String>() {
            {
              add("鬥士哈夫拉蘇");
              add("鬥士霍克");
            }
          });
          put("经", new ArrayList<String>() {
            {
              add("经济社会理事会");
              add("经社理事会");
              add("经典炮图");
              add("经济日报");
            }
          });
          put("赵", new ArrayList<String>() {
            { add("赵紫阳"); }
          });
          put("懷", new ArrayList<String>() {
            { add("懷特"); }
          });
          put("给", new ArrayList<String>() {
            { add("给你爽"); }
          });
          put("w", new ArrayList<String>() {
            {
              add("www.50spcombaidu1828adyou97sace.co.cc");
              add("www.sanjidianying.com.cn");
              add("woyinwose.1243.net.cn");
              add("www.gamelifeclub.cn");
              add("www.91wangyou.com");
              add("www.23nice.com.cn");
              add("www.sf766.com.cn");
              add("www.maituan.com");
              add("www.neicehao.cn");
              add("www.wang567.com");
              add("www.99game.net");
              add("www.foyeye.com");
              add("www.gardcn.com");
              add("www.gw17173.cn");
              add("www.976543.com");
              add("www.978808.com");
              add("webgame.com.cn");
              add("www.sexwyt.com");
              add("www.wow366.cn");
              add("www.ylteam.cn");
              add("www.zy528.com");
              add("www.dy6789.cn");
              add("www.567yx.com");
              add("www.365jw.com");
              add("www.zsyxhd.cn");
              add("www.yxnpc.com");
              add("wangyoudl.com");
              add("www.zxkaku.cn");
              add("www.315ts.net");
              add("wgxiaowu.com");
              add("www.2q5q.com");
              add("www.yhzt.org");
              add("www.legu.com");
              add("www.70yx.com");
              add("www.anule.cn");
              add("www.bin5.cn");
              add("weijianxing");
              add("wuguanzheng");
              add("wanghongwen");
              add("www.xaoh.cn");
              add("www.766.com");
              add("wangzhaoguo");
              add("wangqishan");
              add("wanglequan");
              add("wy3868.com");
              add("woerkaixi");
              add("wgbobo.cn");
              add("wanglijun");
              add("wy724.com");
              add("wenjiabao");
              add("wg7766.cn");
              add("wubangguo");
              add("whoyo.com");
              add("wy33.com");
              add("wanggang");
              add("wangdan");
              add("wstaiji");
              add("wangce");
              add("weelaa");
              add("wuyi");
              add("wg");
            }
          });
          put("鬼", new ArrayList<String>() {
            {
              add("鬼畜轮奸");
              add("鬼輪姦");
              add("鬼村");
            }
          });
          put("猪", new ArrayList<String>() {
            {
              add("猪聋畸");
              add("猪聾畸");
              add("猪容基");
              add("猪毛1");
              add("猪猡");
              add("猪毛");
              add("猪操");
            }
          });
          put("德", new ArrayList<String>() {
            {
              add("德国之声中文网");
              add("德維爾");
            }
          });
          put("猥", new ArrayList<String>() {
            {
              add("猥亵");
              add("猥褻");
              add("猥琐");
            }
          });
          put("御", new ArrayList<String>() {
            { add("御の二代目"); }
          });
          put("蝴", new ArrayList<String>() {
            { add("蝴蝶逼"); }
          });
          put("徐", new ArrayList<String>() {
            {
              add("徐向前");
              add("徐光春");
              add("徐國舅");
              add("徐才厚");
            }
          });
          put("徒", new ArrayList<String>() {
            { add("徒弟会"); }
          });
          put("猛", new ArrayList<String>() {
            {
              add("猛操狂射");
              add("猛插");
            }
          });
          put("很", new ArrayList<String>() {
            { add("很黄"); }
          });
          put("猖", new ArrayList<String>() {
            { add("猖妓"); }
          });
          put("b", new ArrayList<String>() {
            {
              add("baijie.1249.net.cn");
              add("bcd.s.59764.com");
              add("blacksee.com.cn");
              add("bbs.butcn.com");
              add("bulaoge.com");
              add("bbs.766.com");
              add("bibidu.com");
              add("bazhuwg.cn");
              add("bbs.7gg.cn");
              add("bbs.pkmmo");
              add("bwowd.com");
              add("bz176.com");
              add("benladeng");
              add("bobaoping");
              add("bl62.com");
              add("baoweier");
              add("bulaier");
              add("banchan");
              add("bastard");
              add("boxilai");
              add("bignews");
              add("biaozi");
              add("bitch");
              add("boxun");
              add("bushi");
              add("bt");
            }
          });
          put("赌", new ArrayList<String>() {
            {
              add("赌马");
              add("赌球");
              add("赌博");
            }
          });
          put("鏨", new ArrayList<String>() {
            { add("鏨"); }
          });
          put("傅", new ArrayList<String>() {
            {
              add("傅作义");
              add("傅作義");
              add("傅鹏");
            }
          });
          put("伱", new ArrayList<String>() {
            { add("伱妈"); }
          });
          put("醬", new ArrayList<String>() {
            { add("醬猪媳"); }
          });
          put("櫬", new ArrayList<String>() {
            { add("櫬"); }
          });
          put("催", new ArrayList<String>() {
            { add("催情药"); }
          });
          put("伟", new ArrayList<String>() {
            { add("伟哥"); }
          });
          put("傴", new ArrayList<String>() {
            { add("傴"); }
          });
          put("傳", new ArrayList<String>() {
            { add("傳染性病"); }
          });
          put("伊", new ArrayList<String>() {
            {
              add("伊斯蘭亞格林尼斯");
              add("伊斯兰国");
              add("伊莎貝爾");
              add("伊斯蘭");
              add("伊斯兰");
              add("伊拉克");
              add("伊朗");
            }
          });
          put("傺", new ArrayList<String>() {
            { add("傺"); }
          });
          put("傻", new ArrayList<String>() {
            {
              add("傻屄");
              add("傻吊");
              add("傻卵");
              add("傻子");
              add("傻鳥");
              add("傻比");
              add("傻逼");
              add("傻B");
            }
          });
          put("湣", new ArrayList<String>() {
            { add("湣"); }
          });
          put("苘", new ArrayList<String>() {
            { add("苘"); }
          });
          put("尖", new ArrayList<String>() {
            { add("尖阁列岛"); }
          });
          put("M", new ArrayList<String>() {
            {
              add("MINGHUINEWS");
              add("MINGHUI");
              add("MyRadio");
              add("MAKING");
              add("Mai骚");
              add("MAI骚");
              add("MM屄");
              add("ML");
            }
          });
          put("湯", new ArrayList<String>() {
            { add("湯光中"); }
          });
          put("少", new ArrayList<String>() {
            {
              add("少妇偷情");
              add("少女被插");
              add("少修正");
            }
          });
          put("導", new ArrayList<String>() {
            { add("導師"); }
          });
          put("小", new ArrayList<String>() {
            {
              add("小泉纯一郎");
              add("小泉純一郎");
              add("小姐打飞机");
              add("小姐兼职");
              add("小姐裸聊");
              add("小肉粒");
              add("小参考");
              add("小乳头");
              add("小鶏鶏");
              add("小嫩鸡");
              add("小弟弟");
              add("小參考");
              add("小靈通");
              add("小比樣");
              add("小鸡鸡");
              add("小日本");
              add("小B樣");
              add("小姐");
              add("小便");
              add("小逼");
              add("小穴");
              add("小泉");
              add("小攻");
              add("小右");
              add("小受");
              add("小b");
            }
          });
          put("苏", new ArrayList<String>() {
            { add("苏家屯"); }
          });
          put("專", new ArrayList<String>() {
            {
              add("專制");
              add("專政");
            }
          });
          put("尉", new ArrayList<String>() {
            { add("尉健行"); }
          });
          put("將", new ArrayList<String>() {
            { add("將則民"); }
          });
          put("射", new ArrayList<String>() {
            {
              add("射了還說要");
              add("射精");
              add("射奶");
              add("射颜");
              add("射爽");
            }
          });
          put("湿", new ArrayList<String>() {
            {
              add("湿身诱惑");
              add("湿了");
              add("湿穴");
            }
          });
          put("封", new ArrayList<String>() {
            {
              add("封印的靈魂騎士");
              add("封從德");
              add("封殺");
            }
          });
          put("尾", new ArrayList<String>() {
            { add("尾申鲸"); }
          });
          put("尿", new ArrayList<String>() {
            { add("尿"); }
          });
          put("尼", new ArrayList<String>() {
            {
              add("尼克松");
              add("尼奧夫");
              add("尼玛");
            }
          });
          put("苹", new ArrayList<String>() {
            { add("苹果日报"); }
          });
          put("尻", new ArrayList<String>() {
            { add("尻"); }
          });
          put("尹", new ArrayList<String>() {
            { add("尹慶民"); }
          });
          put("英", new ArrayList<String>() {
            {
              add("英文中国邮报");
              add("英国金融时报");
              add("英雄纪念碑");
            }
          });
          put("就", new ArrayList<String>() {
            {
              add("就去色色");
              add("就去诱惑");
              add("就去日");
            }
          });
          put("湖", new ArrayList<String>() {
            {
              add("湖岸哨兵隊長");
              add("湖岸護衛兵");
              add("湖岸警衛兵");
            }
          });
          put("苯", new ArrayList<String>() {
            { add("苯比"); }
          });
          put("湔", new ArrayList<String>() {
            { add("湔"); }
          });
          put("弓", new ArrayList<String>() {
            {
              add("弓虽女干");
              add("弓雖");
            }
          });
          put("苣", new ArrayList<String>() {
            { add("苣"); }
          });
          put("尤", new ArrayList<String>() {
            { add("尤比亞"); }
          });
          put("生", new ArrayList<String>() {
            {
              add("生命力公益新闻网");
              add("生命分流的圖騰");
              add("生孩子沒屁眼");
              add("生徒胸触");
              add("生奸内射");
              add("生鸦片");
              add("生殖");
            }
          });
          put("苦", new ArrayList<String>() {
            { add("苦劳网"); }
          });
          put("纯", new ArrayList<String>() {
            { add("纯一郎"); }
          });
          put("风", new ArrayList<String>() {
            {
              add("风流岁月");
              add("风骚淫荡");
              add("风骚欲女");
              add("风艳阁");
              add("风骚");
              add("风水");
            }
          });
          put("约", new ArrayList<String>() {
            { add("约炮"); }
          });
          put("愨", new ArrayList<String>() {
            { add("愨"); }
          });
          put("红", new ArrayList<String>() {
            {
              add("红太阳的陨落");
              add("红卫兵");
            }
          });
          put("纠", new ArrayList<String>() {
            { add("纠察员"); }
          });
          put("飛", new ArrayList<String>() {
            { add("飛揚論壇"); }
          });
          put("纽", new ArrayList<String>() {
            { add("纽约时报"); }
          });
          put("食", new ArrayList<String>() {
            {
              add("食捻屎");
              add("食屎");
              add("食精");
            }
          });
          put("纵", new ArrayList<String>() {
            { add("纵览中国"); }
          });
          put("纳", new ArrayList<String>() {
            { add("纳粹"); }
          });
          put("飫", new ArrayList<String>() {
            { add("飫"); }
          });
          put("愆", new ArrayList<String>() {
            { add("愆"); }
          });
          put("意", new ArrayList<String>() {
            {
              add("意志不堅的圖騰");
              add("意淫");
            }
          });
          put("纛", new ArrayList<String>() {
            { add("纛"); }
          });
          put("纘", new ArrayList<String>() {
            { add("纘"); }
          });
          put("8", new ArrayList<String>() {
            {
              add("8188mu.com");
              add("888895.com");
              add("88mysf.com");
              add("89-64cdjp");
              add("88kx.com");
              add("8 仙");
              add("8仙");
            }
          });
          put("愛", new ArrayList<String>() {
            {
              add("愛滋病");
              add("愛滋");
            }
          });
          put("鱝", new ArrayList<String>() {
            { add("鱝"); }
          });
          put("鱟", new ArrayList<String>() {
            { add("鱟"); }
          });
          put("暴", new ArrayList<String>() {
            {
              add("暴躁的警衛兵靈魂");
              add("暴躁的城塔野獸");
              add("暴躁的馬杜克");
              add("暴熱的戰士");
              add("暴力虐待");
              add("暴風亡靈");
              add("暴徒");
              add("暴君");
              add("暴乱");
              add("暴亂");
              add("暴奸");
              add("暴淫");
              add("暴干");
              add("暴乳");
              add("暴动");
            }
          });
          put("哭", new ArrayList<String>() {
            {
              add("哭爸");
              add("哭母");
            }
          });
          put("暗", new ArrayList<String>() {
            { add("暗黑法師"); }
          });
          put("鱭", new ArrayList<String>() {
            { add("鱭"); }
          });
          put("品", new ArrayList<String>() {
            {
              add("品色堂");
              add("品香堂");
              add("品穴");
            }
          });
          put("x", new ArrayList<String>() {
            {
              add("xingqingzhongren.1174.net.cn");
              add("xiao77luntan.1249.net.cn");
              add("xiao77.1243.net.cn");
              add("xiaoqinzaixian.cn");
              add("xinwenguanzhi");
              add("xinjiangduli");
              add("xuxiangqian");
              add("xyq2sf.com");
              add("xizangduli");
              add("xl517.com");
              add("xyxgh.com");
              add("xijinping");
              add("xinsheng");
              add("xq-wl.cn");
              add("xihanuke");
              add("xucaihou");
              add("xiuau.cn");
              add("xiaoquan");
              add("xya3.cn");
              add("xdns.eu");
              add("xiejiao");
              add("xitele");
              add("xilake");
              add("xiao77");
              add("xtl");
            }
          });
          put("鱧", new ArrayList<String>() {
            { add("鱧"); }
          });
          put("哈", new ArrayList<String>() {
            { add("哈爾羅尼"); }
          });
          put("诎", new ArrayList<String>() {
            { add("诎"); }
          });
          put("嬀", new ArrayList<String>() {
            { add("嬀"); }
          });
          put("靠", new ArrayList<String>() {
            {
              add("靠你媽");
              add("靠你妈");
              add("靠腰");
              add("靠北");
              add("靠爸");
              add("靠母");
              add("靠");
            }
          });
          put("诔", new ArrayList<String>() {
            { add("诔"); }
          });
          put("革", new ArrayList<String>() {
            { add("革命"); }
          });
          put("瞘", new ArrayList<String>() {
            { add("瞘"); }
          });
          put("瞞", new ArrayList<String>() {
            { add("瞞報"); }
          });
          put("青", new ArrayList<String>() {
            {
              add("青天白日");
              add("青年日报");
            }
          });
          put("靖", new ArrayList<String>() {
            {
              add("靖國神社");
              add("靖国神社");
            }
          });
          put("静", new ArrayList<String>() {
            { add("静坐"); }
          });
          put("非", new ArrayList<String>() {
            { add("非常新闻通讯社"); }
          });
          put("诱", new ArrayList<String>() {
            {
              add("诱惑视频");
              add("诱色uu");
              add("诱惑");
              add("诱人");
            }
          });
          put("诼", new ArrayList<String>() {
            { add("诼"); }
          });
          put("诹", new ArrayList<String>() {
            { add("诹"); }
          });
          put("豐", new ArrayList<String>() {
            { add("豐饒的果實"); }
          });
          put("奥", new ArrayList<String>() {
            {
              add("奥巴马");
              add("奥马尔");
            }
          });
          put("税", new ArrayList<String>() {
            { add("税务总局"); }
          });
          put("程", new ArrayList<String>() {
            {
              add("程鐵軍");
              add("程凱");
            }
          });
          put("c", new ArrayList<String>() {
            {
              add("chengrenwangzhi.1242.net.cn");
              add("chengrenmanhua.1242.net.cn");
              add("cnaicheng.1174.net.cn");
              add("consignment5173");
              add("chongxianmu.cn");
              add("chinesenewsnet");
              add("caogangchuan");
              add("chinaliberal");
              add("chinavfx.net");
              add("cao你大爷");
              add("chenshuibian");
              add("cikcatv.2om");
              add("chenliangyu");
              add("canyaa.com");
              add("chunyilang");
              add("cdream.com");
              add("chenduxiu");
              add("cvceo.com");
              add("chenzhili");
              add("cao你妈");
              add("creaders");
              add("chailing");
              add("chinamz");
              add("c5c8.cn");
              add("chenyun");
              add("cao你");
              add("chenyi");
              add("cha你");
              add("c a o");
              add("caobi");
              add("caoB");
              add("cnd");
              add("cao");
              add("cs");
              add("cS");
            }
          });
          put("豪", new ArrayList<String>() {
            { add("豪乳"); }
          });
          put("口", new ArrayList<String>() {
            {
              add("口交放尿");
              add("口爆吞精");
              add("口内爆射");
              add("口淫");
              add("口爆");
              add("口交");
              add("口活");
              add("口射");
            }
          });
          put("澤", new ArrayList<String>() {
            {
              add("澤民");
              add("澤夫");
            }
          });
          put("澠", new ArrayList<String>() {
            { add("澠"); }
          });
          put("叫", new ArrayList<String>() {
            {
              add("叫小姐");
              add("叫春");
              add("叫床");
              add("叫鸡");
            }
          });
          put("可", new ArrayList<String>() {
            {
              add("可待因");
              add("可卡叶");
              add("可卡因");
            }
          });
          put("澩", new ArrayList<String>() {
            { add("澩"); }
          });
          put("右", new ArrayList<String>() {
            { add("右翼"); }
          });
          put("史", new ArrayList<String>() {
            {
              add("史萊姆王");
              add("史萊姆");
            }
          });
          put("台", new ArrayList<String>() {
            {
              add("台湾记者协会");
              add("台湾共产党");
              add("台湾共和国");
              add("台独分子");
              add("台湾报纸");
              add("台湾民国");
              add("台湾独立");
              add("台独万岁");
              add("台湾帝国");
              add("台湾旺报");
              add("台湾岛国");
              add("台湾党");
              add("台英社");
              add("台湾国");
              add("台湾狗");
              add("台独");
              add("台联");
              add("台办");
            }
          });
          put("澳", new ArrayList<String>() {
            {
              add("澳洲广播电台中文网");
              add("澳大利亚时报澳奇网");
              add("澳洲光明网");
            }
          });
          put("叶", new ArrayList<String>() {
            { add("叶剑英"); }
          });
          put("茉", new ArrayList<String>() {
            {
              add("茉莉花革命");
              add("茉莉花");
            }
          });
          put("司", new ArrayList<String>() {
            {
              add("司馬璐");
              add("司徒華");
              add("司馬晋");
              add("司法部");
            }
          });
          put("叼", new ArrayList<String>() {
            {
              add("叼你媽");
              add("叼你妈");
              add("叼你");
            }
          });
          put("参", new ArrayList<String>() {
            { add("参事室"); }
          });
          put("友", new ArrayList<String>() {
            { add("友好的魯德"); }
          });
          put("反", new ArrayList<String>() {
            {
              add("反封鎖技術");
              add("反恐委员会");
              add("反腐敗論壇");
              add("反革命");
              add("反政府");
              add("反社會");
              add("反人類");
              add("反封鎖");
              add("反华");
              add("反动");
              add("反党");
              add("反日");
              add("反共");
            }
          });
          put("双", new ArrayList<String>() {
            {
              add("双语学生邮报");
              add("双峰微颤");
              add("双龙入洞");
              add("双飞");
            }
          });
          put("发", new ArrayList<String>() {
            {
              add("发展与改革委员会");
              add("发展和改革委员会");
              add("发伦功");
              add("发抡功");
              add("发论功");
              add("发论公");
              add("发改委");
              add("发论");
              add("发伦");
              add("发票");
              add("发轮");
              add("发骚");
              add("发浪");
            }
          });
          put("受", new ArrayList<String>() {
            { add("受虐狂"); }
          });
          put("叔", new ArrayList<String>() {
            { add("叔嫂肉欲"); }
          });
          put("变", new ArrayList<String>() {
            { add("变态"); }
          });
          put("班", new ArrayList<String>() {
            {
              add("班禪");
              add("班禅");
            }
          });
          put("今", new ArrayList<String>() {
            { add("今日悉尼"); }
          });
          put("N", new ArrayList<String>() {
            {
              add("NECKROMANCER");
              add("Neckromancer");
              add("NAIVE");
              add("NACB");
              add("NMIS");
              add("NND");
              add("NPC");
              add("NMD");
            }
          });
          put("現", new ArrayList<String>() {
            {
              add("現金交易");
              add("現金");
            }
          });
          put("彭", new ArrayList<String>() {
            {
              add("彭博商业周刊");
              add("彭博新闻社");
              add("彭丽媛");
              add("彭德怀");
              add("彭真");
              add("彭博");
            }
          });
          put("螭", new ArrayList<String>() {
            { add("螭"); }
          });
          put("彐", new ArrayList<String>() {
            { add("彐"); }
          });
          put("彀", new ArrayList<String>() {
            { add("彀"); }
          });
          put("彈", new ArrayList<String>() {
            { add("彈劾"); }
          });
          put("紅", new ArrayList<String>() {
            {
              add("紅炎猛獸");
              add("紅色恐怖");
              add("紅燈區");
            }
          });
          put("納", new ArrayList<String>() {
            { add("納粹"); }
          });
          put("素", new ArrayList<String>() {
            { add("素人娘"); }
          });
          put("粮", new ArrayList<String>() {
            { add("粮食局"); }
          });
          put("紱", new ArrayList<String>() {
            { add("紱"); }
          });
          put("9", new ArrayList<String>() {
            {
              add("999日本妹");
              add("991game.com");
              add("92wydl.com");
              add("91bysd.cn");
              add("92klgh.cn");
              add("92ey.com");
              add("99sa.com");
              add("97sese");
            }
          });
          put("紿", new ArrayList<String>() {
            { add("紿"); }
          });
          put("肏", new ArrayList<String>() {
            {
              add("肏屄");
              add("肏");
            }
          });
          put("假", new ArrayList<String>() {
            {
              add("假阳具插穴");
              add("假青林");
              add("假庆林");
            }
          });
          put("你", new ArrayList<String>() {
            {
              add("你說我說論壇");
              add("你媽了妹");
              add("你二大爷");
              add("你姥姥的");
              add("你爷爷的");
              add("你妈了妹");
              add("你妈的");
              add("你爷爷");
              add("你大爷");
              add("你色吗");
              add("你媽逼");
              add("你奶奶");
              add("你老母");
              add("你媽的");
              add("你老味");
              add("你妈逼");
              add("你媽比");
              add("你媽");
              add("你爸");
              add("你妈");
              add("你娘");
              add("你爺");
              add("你姥");
            }
          });
          put("做", new ArrayList<String>() {
            {
              add("做爱图片");
              add("做爱自拍");
              add("做爱姿势");
              add("做爱电影");
              add("做愛");
              add("做爱");
            }
          });
          put("佥", new ArrayList<String>() {
            { add("佥"); }
          });
          put("体", new ArrayList<String>() {
            { add("体奸"); }
          });
          put("何", new ArrayList<String>() {
            { add("何候华"); }
          });
          put("余", new ArrayList<String>() {
            { add("余英時"); }
          });
          put("佛", new ArrayList<String>() {
            {
              add("佛展千手法");
              add("佛祖");
              add("佛教");
            }
          });
          put("作", new ArrayList<String>() {
            {
              add("作愛");
              add("作爱");
              add("作秀");
            }
          });
          put("y", new ArrayList<String>() {
            {
              add("yidangzhuanzheng");
              add("youxiji888.cn");
              add("yuzhengsheng");
              add("yaokong7.com");
              add("yuyongkang");
              add("yalishanda");
              add("yejianying");
              add("yujiankang");
              add("yaowenyuan");
              add("yeswm.com");
              add("ys168.com");
              add("yumuming");
              add("yisilan");
              add("yz55.cn");
              add("yuming");
            }
          });
          put("偷", new ArrayList<String>() {
            {
              add("偷窥视频");
              add("偷拍美穴");
              add("偷窥图片");
              add("偷拍");
              add("偷窥");
              add("偷欢");
              add("偷精");
            }
          });
          put("T", new ArrayList<String>() {
            {
              add("The Standard");
              add("Taipei Times");
              add("TRIANGLEBOY");
              add("TRIANGLE");
              add("THE9CITY");
              add("The9City");
              add("TIBETALK");
              add("TRINGEL");
              add("Tringel");
              add("T.M.D");
              add("THE9");
              add("The9");
              add("TNND");
              add("TAIP");
              add("TEST");
              add("Test");
              add("TEsT");
              add("TeSt");
              add("TMD");
              add("TD");
            }
          });
          put("貝", new ArrayList<String>() {
            { add("貝尤爾"); }
          });
          put("貞", new ArrayList<String>() {
            { add("貞操"); }
          });
          put("費", new ArrayList<String>() {
            {
              add("費良勇");
              add("費鴻泰");
            }
          });
          put("貺", new ArrayList<String>() {
            { add("貺"); }
          });
          put("貰", new ArrayList<String>() {
            { add("貰"); }
          });
          put("貫", new ArrayList<String>() {
            { add("貫通兩極法"); }
          });
          put("章", new ArrayList<String>() {
            { add("章孝嚴"); }
          });
          put("童", new ArrayList<String>() {
            { add("童颜巨乳"); }
          });
          put("仳", new ArrayList<String>() {
            { add("仳"); }
          });
          put("肛", new ArrayList<String>() {
            {
              add("肛门拳交");
              add("肛门喷水");
              add("肛交");
              add("肛门");
              add("肛門");
              add("肛屄");
              add("肛");
            }
          });
          put("慶", new ArrayList<String>() {
            { add("慶紅"); }
          });
          put("颛", new ArrayList<String>() {
            { add("颛"); }
          });
          put("颜", new ArrayList<String>() {
            {
              add("颜射自拍");
              add("颜射");
              add("颜骑");
            }
          });
          put("额", new ArrayList<String>() {
            { add("额尔德尼"); }
          });
          put("慰", new ArrayList<String>() {
            {
              add("慰安妇");
              add("慰春情");
            }
          });
          put("風", new ArrayList<String>() {
            {
              add("風雨神州論壇");
              add("風雨神州");
            }
          });
          put("d", new ArrayList<String>() {
            {
              add("dingxiang.1243.net.cn");
              add("digitallongking.com");
              add("dingxiang.1249.net");
              add("dongtiaoyingji");
              add("dengxiaoping");
              add("dolbbs.com");
              add("dadati.com");
              add("duowan.com");
              add("dalailama");
              add("d666.com");
              add("dajiyuan");
              add("dulumen");
              add("dl.com");
              add("duowan");
              add("dishun");
              add("dalai");
              add("dick");
              add("damn");
              add("dfdz");
              add("dafa");
              add("dang");
              add("dpp");
            }
          });
          put("颮", new ArrayList<String>() {
            { add("颮"); }
          });
          put("祚", new ArrayList<String>() {
            { add("祚"); }
          });
          put("神", new ArrayList<String>() {
            {
              add("神通加持法");
              add("神經病");
              add("神经病");
              add("神汉");
              add("神婆");
            }
          });
          put("祜", new ArrayList<String>() {
            { add("祜"); }
          });
          put("嘵", new ArrayList<String>() {
            { add("嘵"); }
          });
          put("龐", new ArrayList<String>() {
            { add("龐建國"); }
          });
          put("鎮", new ArrayList<String>() {
            { add("鎮壓"); }
          });
          put("岡", new ArrayList<String>() {
            { add("岡巒"); }
          });
          put("露", new ArrayList<String>() {
            {
              add("露阴照");
              add("露屄");
              add("露点");
              add("露逼");
              add("露穴");
              add("露毛");
              add("露B");
            }
          });
          put("嫖", new ArrayList<String>() {
            {
              add("嫖妓指南");
              add("嫖娼");
              add("嫖客");
            }
          });
          put("蠖", new ArrayList<String>() {
            { add("蠖"); }
          });
          put("嫐", new ArrayList<String>() {
            { add("嫐屄"); }
          });
          put("苊", new ArrayList<String>() {
            { add("苊"); }
          });
          put("癤", new ArrayList<String>() {
            { add("癤"); }
          });
          put("嫩", new ArrayList<String>() {
            {
              add("嫩穴肉缝");
              add("嫩鲍鱼");
              add("嫩奶");
              add("嫩穴");
              add("嫩鲍");
              add("嫩逼");
              add("嫩屄");
              add("嫩缝");
              add("嫩女");
              add("嫩BB");
              add("嫩b");
              add("嫩B");
            }
          });
          put("蠢", new ArrayList<String>() {
            { add("蠢猪"); }
          });
          put("蠡", new ArrayList<String>() {
            { add("蠡"); }
          });
          put("嫠", new ArrayList<String>() {
            { add("嫠"); }
          });
          put("蠼", new ArrayList<String>() {
            { add("蠼毵"); }
          });
          put("蠲", new ArrayList<String>() {
            { add("蠲"); }
          });
          put("白", new ArrayList<String>() {
            {
              add("白虎小穴");
              add("白浆四溅");
              add("白液四溅");
              add("白虎少妇");
              add("白虎阴穴");
              add("白嫩骚妇");
              add("白话佛法");
              add("白虎嫩B");
              add("白立樸");
              add("白莲教");
              add("白皮書");
              add("白痴");
              add("白粉");
              add("白夢");
            }
          });
          put("發", new ArrayList<String>() {
            {
              add("發掄功");
              add("發正念");
              add("發論功");
              add("發論公");
              add("發倫功");
              add("發論");
              add("發掄");
              add("發騷");
              add("發輪");
              add("發楞");
              add("發倫");
            }
          });
          put("登", new ArrayList<String>() {
            { add("登輝"); }
          });
          put("発", new ArrayList<String>() {
            {
              add("発妻");
              add("発射");
            }
          });
          put("倍", new ArrayList<String>() {
            { add("倍可亲"); }
          });
          put("侧", new ArrayList<String>() {
            { add("侧那"); }
          });
          put("侦", new ArrayList<String>() {
            { add("侦探设备"); }
          });
          put("橋", new ArrayList<String>() {
            { add("橋侵襲兵"); }
          });
          put("侨", new ArrayList<String>() {
            { add("侨办"); }
          });
          put("倬", new ArrayList<String>() {
            { add("倬"); }
          });
          put("倭", new ArrayList<String>() {
            {
              add("倭寇");
              add("倭国");
            }
          });
          put("倪", new ArrayList<String>() {
            { add("倪育賢"); }
          });
          put("倫", new ArrayList<String>() {
            { add("倫功"); }
          });
          put("侍", new ArrayList<String>() {
            {
              add("侍從貝赫爾特");
              add("侍從倫斯韋");
            }
          });
          put("濫", new ArrayList<String>() {
            {
              add("濫比");
              add("濫貨");
              add("濫交");
              add("濫逼");
              add("濫B");
            }
          });
          put("草", new ArrayList<String>() {
            {
              add("草的你妈");
              add("草你妈");
              add("草拟妈");
              add("草擬媽");
              add("草你媽");
              add("草泥马");
              add("草榴");
              add("草你");
              add("草妈");
              add("草");
            }
          });
          put("荊", new ArrayList<String>() {
            { add("荊棘護衛兵"); }
          });
          put("濼", new ArrayList<String>() {
            { add("濼"); }
          });
          put("去", new ArrayList<String>() {
            {
              add("去你媽的");
              add("去你妈的");
              add("去妳妈");
              add("去她妈");
              add("去妳的");
              add("去你的");
              add("去他妈");
              add("去你妈");
              add("去死");
            }
          });
          put("激", new ArrayList<String>() {
            {
              add("激情小电影");
              add("激情贴图");
              add("激情潮喷");
              add("激凸走光");
              add("激情小说");
              add("激情打炮");
              add("激情聊天");
              add("激情电影");
              add("激情裸体");
              add("激情交友");
              add("激插");
            }
          });
          put("荷", new ArrayList<String>() {
            { add("荷兰国际广播电台中文网"); }
          });
          put("濕", new ArrayList<String>() {
            {
              add("濕了還說不要");
              add("濕了還說要");
            }
          });
          put("厕", new ArrayList<String>() {
            {
              add("厕所盗摄");
              add("厕所偷拍");
              add("厕奴");
            }
          });
          put("厙", new ArrayList<String>() {
            { add("厙"); }
          });
          put("z", new ArrayList<String>() {
            {
              add("zhuanfalunADMIN");
              add("zuanshi1000.cn");
              add("zhonghuaminguo");
              add("zhangchunqiao");
              add("zhengjianwang");
              add("zhoujiankang");
              add("zhidian8.com");
              add("zhouyongkang");
              add("zhangdejiang");
              add("zengqinghong");
              add("zhouxiaokang");
              add("zxsj188.com");
              add("zhenshanren");
              add("zzz.xaoh.cn");
              add("zhaoziyang");
              add("zzmysf.com");
              add("zhanggaoli");
              add("zengpeiyan");
              add("zhurongji");
              add("zaixu.net");
              add("zhengjian");
              add("zy666.net");
              add("zx002.com");
              add("zhouenlai");
              add("zhenzhu");
              add("zhude");
            }
          });
          put("荡", new ArrayList<String>() {
            {
              add("荡妇");
              add("荡女");
            }
          });
          put("原", new ArrayList<String>() {
            {
              add("原住民族电视台");
              add("原子能机构");
              add("原味丝袜");
              add("原子弹");
            }
          });
          put("鰣", new ArrayList<String>() {
            { add("鰣"); }
          });
          put("曼", new ArrayList<String>() {
            { add("曼德拉"); }
          });
          put("曾", new ArrayList<String>() {
            {
              add("曾庆红");
              add("曾培炎");
            }
          });
          put("咼", new ArrayList<String>() {
            { add("咼"); }
          });
          put("鰨", new ArrayList<String>() {
            { add("鰨"); }
          });
          put("更", new ArrayList<String>() {
            { add("更生日报"); }
          });
          put("曷", new ArrayList<String>() {
            { add("曷"); }
          });
          put("鰳", new ArrayList<String>() {
            { add("鰳"); }
          });
          put("鰷", new ArrayList<String>() {
            { add("鰷"); }
          });
          put("鰵", new ArrayList<String>() {
            { add("鰵"); }
          });
          put("咬", new ArrayList<String>() {
            { add("咬着龟头"); }
          });
          put("鰹", new ArrayList<String>() {
            { add("鰹"); }
          });
          put("咪", new ArrayList<String>() {
            {
              add("咪咪图片");
              add("咪咪");
            }
          });
          put("%", new ArrayList<String>() {
            { add("%77%77%77%2E%39%37%38%38%30%38%2E%63%6F%6D"); }
          });
          put("和", new ArrayList<String>() {
            { add("和锅枪"); }
          });
          put("喷", new ArrayList<String>() {
            {
              add("喷你");
              add("喷精");
            }
          });
          put("緄", new ArrayList<String>() {
            { add("緄"); }
          });
          put("深", new ArrayList<String>() {
            { add("深爱色色"); }
          });
          put("緙", new ArrayList<String>() {
            { add("緙"); }
          });
          put("緡", new ArrayList<String>() {
            { add("緡"); }
          });
          put("Ｕ", new ArrayList<String>() {
            { add("ＵＲ"); }
          });
          put("緣", new ArrayList<String>() {
            { add("緣圈圈"); }
          });
          put("Ｘ", new ArrayList<String>() {
            { add("Ｘ到噴屎尿"); }
          });
          put("緱", new ArrayList<String>() {
            { add("緱"); }
          });
          put("Ｇ", new ArrayList<String>() {
            {
              add("Ｇ芭");
              add("Ｇ叭");
              add("Ｇ掰");
              add("Ｇ巴");
              add("Ｇ八");
            }
          });
          put("瘃", new ArrayList<String>() {
            { add("瘃"); }
          });
          put("血", new ArrayList<String>() {
            {
              add("血腥图片");
              add("血逼");
            }
          });
          put("银", new ArrayList<String>() {
            { add("银民吧"); }
          });
          put("枪", new ArrayList<String>() {
            {
              add("枪决现场");
              add("枪支弹药");
              add("枪决女犯");
            }
          });
          put("媚", new ArrayList<String>() {
            { add("媚药少年"); }
          });
          put("瘞", new ArrayList<String>() {
            { add("瘞"); }
          });
          put("瘟", new ArrayList<String>() {
            { add("瘟家宝"); }
          });
          put("媒", new ArrayList<String>() {
            { add("媒体公民行动网"); }
          });
          put("街", new ArrayList<String>() {
            {
              add("街头对抗");
              add("街头扒衣");
            }
          });
          put("瘥", new ArrayList<String>() {
            { add("瘥"); }
          });
          put("瘭", new ArrayList<String>() {
            { add("瘭"); }
          });
          put("衢", new ArrayList<String>() {
            { add("衢"); }
          });
          put("瘪", new ArrayList<String>() {
            { add("瘪三"); }
          });
          put("媽", new ArrayList<String>() {
            {
              add("媽的");
              add("媽批");
              add("媽比");
              add("媽B");
            }
          });
          put("瘳", new ArrayList<String>() {
            { add("瘳"); }
          });
          put("铀", new ArrayList<String>() {
            { add("铀"); }
          });
          put("瘸", new ArrayList<String>() {
            {
              add("瘸腿帮");
              add("瘸腿幫");
            }
          });
          put("賣", new ArrayList<String>() {
            {
              add("賣淫");
              add("賣國");
              add("賣逼");
              add("賣騷");
              add("賣比");
            }
          });
          put("窝", new ArrayList<String>() {
            { add("窝窝客"); }
          });
          put("賤", new ArrayList<String>() {
            {
              add("賤逼");
              add("賤人");
              add("賤比");
              add("賤種");
              add("賤貨");
              add("賤bi");
              add("賤B");
            }
          });
          put("賫", new ArrayList<String>() {
            { add("賫"); }
          });
          put("賬", new ArrayList<String>() {
            { add("賬號"); }
          });
          put("P", new ArrayList<String>() {
            {
              add("PEACEHALL");
              add("PLAYBOY");
              add("PAPER64");
              add("PENIS");
              add("PUSSY");
            }
          });
          put("賴", new ArrayList<String>() {
            { add("賴士葆"); }
          });
          put("賾", new ArrayList<String>() {
            { add("賾"); }
          });
          put("窃", new ArrayList<String>() {
            {
              add("窃听器材");
              add("窃听器");
            }
          });
          put("窀", new ArrayList<String>() {
            { add("窀"); }
          });
          put("突", new ArrayList<String>() {
            { add("突尼斯"); }
          });
          put("賀", new ArrayList<String>() {
            {
              add("賀國强");
              add("賀龍");
            }
          });
          put("資", new ArrayList<String>() {
            { add("資本主義"); }
          });
          put("賊", new ArrayList<String>() {
            { add("賊民"); }
          });
          put("賈", new ArrayList<String>() {
            {
              add("賈育台");
              add("賈廷安");
            }
          });
          put("f", new ArrayList<String>() {
            {
              add("feitengdl.com");
              add("falundafahao");
              add("fisonet.com");
              add("fxjsqc.com");
              add("fulankelin");
              add("freechina");
              add("falundafa");
              add("falungong");
              add("fy371.com");
              add("freedom");
              add("freenet");
              add("fa轮");
              add("falun");
              add("fuck");
              add("falu");
              add("flg");
            }
          });
          put("賓", new ArrayList<String>() {
            { add("賓周"); }
          });
          put("强", new ArrayList<String>() {
            {
              add("强效失意药");
              add("强暴幼女");
              add("强奸处女");
              add("强硬发言");
              add("强制浣肠");
              add("强奸犯");
              add("强卫");
              add("强暴");
              add("强奸");
            }
          });
          put("现", new ArrayList<String>() {
            { add("现代情色小说"); }
          });
          put("張", new ArrayList<String>() {
            {
              add("張伯笠");
              add("張昭富");
              add("張偉國");
              add("張溫鷹");
              add("張志清");
              add("張博雅");
              add("張清芳");
              add("張健");
              add("張林");
              add("張鋼");
            }
          });
          put("张", new ArrayList<String>() {
            {
              add("张小平");
              add("张朝阳");
              add("张立昌");
              add("张高丽");
              add("张春桥");
              add("张潮阳");
              add("张德江");
              add("张筱雨");
              add("张磊");
            }
          });
          put("玩", new ArrayList<String>() {
            {
              add("玩逼");
              add("玩穴");
            }
          });
          put("玛", new ArrayList<String>() {
            { add("玛雅网"); }
          });
          put("引", new ArrayList<String>() {
            { add("引導"); }
          });
          put("异", new ArrayList<String>() {
            {
              add("异型叛軍");
              add("异議人士");
              add("异見人士");
            }
          });
          put("王", new ArrayList<String>() {
            {
              add("王超華");
              add("王八蛋");
              add("王輔臣");
              add("王立军");
              add("王兆國");
              add("王秀麗");
              add("王洪文");
              add("王岐山");
              add("王世堅");
              add("王潤生");
              add("王樂泉");
              add("王乐泉");
              add("王滬寧");
              add("王世勛");
              add("王宝森");
              add("王涵萬");
              add("王兆国");
              add("王軍濤");
              add("王寶森");
              add("王太华");
              add("王丹");
              add("王刚");
              add("王昊");
              add("王震");
              add("王剛");
            }
          });
          put("开", new ArrayList<String>() {
            {
              add("开放杂志");
              add("开放网");
              add("开苞");
            }
          });
          put("玉", new ArrayList<String>() {
            {
              add("玉蒲团玉女心经");
              add("玉杵");
              add("玉乳");
              add("玉穴");
            }
          });
          put("黎", new ArrayList<String>() {
            { add("黎阳评"); }
          });
          put("鍰", new ArrayList<String>() {
            { add("鍰"); }
          });
          put("乳", new ArrayList<String>() {
            {
              add("乳此动人");
              add("乳此丝袜");
              add("乳波臀浪");
              add("乳此丰满");
              add("乳爆");
              add("乳沟");
              add("乳峰");
              add("乳头");
              add("乳交");
              add("乳晕");
              add("乳霸");
              add("乳房");
              add("乳尻");
              add("乳暈");
              add("乳射");
              add("乳波");
              add("乳頭");
              add("乳");
            }
          });
          put("鍃", new ArrayList<String>() {
            { add("鍃"); }
          });
          put("經", new ArrayList<String>() {
            {
              add("經文");
              add("經血");
            }
          });
          put("綮", new ArrayList<String>() {
            { add("綮"); }
          });
          put("綣", new ArrayList<String>() {
            { add("綣"); }
          });
          put("綿", new ArrayList<String>() {
            { add("綿恒"); }
          });
          put("網", new ArrayList<String>() {
            {
              add("網禪");
              add("網特");
            }
          });
          put("綰", new ArrayList<String>() {
            { add("綰"); }
          });
          put("殛", new ArrayList<String>() {
            { add("殛"); }
          });
          put("埃", new ArrayList<String>() {
            {
              add("埃裏克蘇特勤");
              add("埃斯萬");
            }
          });
          put("修", new ArrayList<String>() {
            { add("修煉"); }
          });
          put("殂", new ArrayList<String>() {
            { add("殂"); }
          });
          put("黧", new ArrayList<String>() {
            { add("黧"); }
          });
          put("信", new ArrayList<String>() {
            {
              add("信报财经新闻");
              add("信用危機");
              add("信报月刊");
              add("信徒");
            }
          });
          put("俞", new ArrayList<String>() {
            { add("俞正声"); }
          });
          put("保", new ArrayList<String>() {
            {
              add("保密局");
              add("保监会");
              add("保钓");
              add("保釣");
            }
          });
          put("基", new ArrayList<String>() {
            {
              add("基地组织");
              add("基督教");
              add("基督");
            }
          });
          put("㎜", new ArrayList<String>() {
            { add("㎜"); }
          });
          put("殺", new ArrayList<String>() {
            {
              add("殺你一家");
              add("殺你全家");
              add("殺人犯");
            }
          });
          put("俏", new ArrayList<String>() {
            { add("俏臀摄魄"); }
          });
          put("埤", new ArrayList<String>() {
            { add("埤"); }
          });
          put("㎎", new ArrayList<String>() {
            { add("㎎"); }
          });
          put("㎏", new ArrayList<String>() {
            { add("㎏"); }
          });
          put("俄", new ArrayList<String>() {
            { add("俄國"); }
          });
          put("抢", new ArrayList<String>() {
            { add("抢劫"); }
          });
          put("示", new ArrayList<String>() {
            { add("示威"); }
          });
          put("抠", new ArrayList<String>() {
            { add("抠穴"); }
          });
          put("社", new ArrayList<String>() {
            {
              add("社會主義");
              add("社科院");
            }
          });
          put("护", new ArrayList<String>() {
            { add("护士诱惑"); }
          });
          put("抽", new ArrayList<String>() {
            {
              add("抽你丫的");
              add("抽插");
              add("抽头");
            }
          });
          put("押", new ArrayList<String>() {
            {
              add("押小");
              add("押注");
              add("押大");
            }
          });
          put("顔", new ArrayList<String>() {
            {
              add("顔清標");
              add("顔慶章");
              add("顔射");
            }
          });
          put("顓", new ArrayList<String>() {
            { add("顓"); }
          });
          put("顬", new ArrayList<String>() {
            { add("顬"); }
          });
          put("顥", new ArrayList<String>() {
            { add("顥"); }
          });
          put("抑", new ArrayList<String>() {
            { add("抑制剂"); }
          });
          put("投", new ArrayList<String>() {
            { add("投毒杀人"); }
          });
          put("呻", new ArrayList<String>() {
            { add("呻吟"); }
          });
          put("呼", new ArrayList<String>() {
            { add("呼喊派"); }
          });
          put("罗", new ArrayList<String>() {
            {
              add("罗　干");
              add("罗荣桓");
              add("罗干");
            }
          });
          put("Q", new ArrayList<String>() {
            { add("QIANGJIAN"); }
          });
          put("周", new ArrayList<String>() {
            {
              add("周六性吧");
              add("周鋒鎖");
              add("周恩来");
              add("周永康");
              add("周健康");
              add("周小康");
              add("周恩來");
              add("周守訓");
              add("周总理");
            }
          });
          put("是", new ArrayList<String>() {
            { add("是鸡"); }
          });
          put("春", new ArrayList<String>() {
            {
              add("春夏自由論壇");
              add("春光外泻");
              add("春药");
            }
          });
          put("鷹", new ArrayList<String>() {
            { add("鷹眼派氏族"); }
          });
          put("星", new ArrayList<String>() {
            {
              add("星岛日报消息");
              add("星岛环球网");
              add("星岛日报");
            }
          });
          put("鷄", new ArrayList<String>() {
            { add("鷄巴"); }
          });
          put("呒", new ArrayList<String>() {
            { add("呒"); }
          });
          put("易", new ArrayList<String>() {
            { add("易丹軒"); }
          });
          put("昏", new ArrayList<String>() {
            {
              add("昏迷圖騰");
              add("昏药");
            }
          });
          put("明", new ArrayList<String>() {
            {
              add("明星新聞網");
              add("明镜新闻");
              add("明镜周刊");
              add("明报月刊");
              add("明星淫图");
              add("明慧网");
              add("明镜网");
              add("明报");
            }
          });
          put("欧", new ArrayList<String>() {
            {
              add("欧美无套");
              add("欧美大乳");
            }
          });
          put("昆", new ArrayList<String>() {
            {
              add("昆圖");
              add("昆");
            }
          });
          put("鷚", new ArrayList<String>() {
            { add("鷚"); }
          });
          put("呆", new ArrayList<String>() {
            { add("呆卵"); }
          });
          put("錦", new ArrayList<String>() {
            { add("錦濤"); }
          });
          put("錢", new ArrayList<String>() {
            {
              add("錢國梁");
              add("錢其琛");
              add("錢達");
            }
          });
          put("錯", new ArrayList<String>() {
            {
              add("錯那嗎比");
              add("錯那嗎逼");
              add("錯那嗎B");
              add("錯比");
              add("錯逼");
              add("錯B");
            }
          });
          put("沉", new ArrayList<String>() {
            { add("沉睡圖騰"); }
          });
          put("罂", new ArrayList<String>() {
            { add("罂粟"); }
          });
          put("錆", new ArrayList<String>() {
            { add("錆"); }
          });
          put("輿", new ArrayList<String>() {
            {
              add("輿論反制");
              add("輿論");
            }
          });
          put("河", new ArrayList<String>() {
            {
              add("河殇");
              add("河殤");
            }
          });
          put("钻", new ArrayList<String>() {
            { add("钻插"); }
          });
          put("钱", new ArrayList<String>() {
            { add("钱其琛"); }
          });
          put("盛", new ArrayList<String>() {
            { add("盛華仁"); }
          });
          put("盜", new ArrayList<String>() {
            { add("盜竊犯"); }
          });
          put("监", new ArrayList<String>() {
            {
              add("监禁陵辱");
              add("监督");
              add("监狱");
              add("监管");
            }
          });
          put("婊", new ArrayList<String>() {
            {
              add("婊子養的");
              add("婊子");
              add("婊");
            }
          });
          put("钤", new ArrayList<String>() {
            { add("钤"); }
          });
          put("盗", new ArrayList<String>() {
            {
              add("盗窃犯");
              add("盗撮");
            }
          });
          put("袭", new ArrayList<String>() {
            { add("袭近平"); }
          });
          put("被", new ArrayList<String>() {
            {
              add("被立王");
              add("被插");
              add("被干");
              add("被操");
            }
          });
          put("監", new ArrayList<String>() {
            {
              add("監視塔哨兵隊長");
              add("監視塔哨兵");
              add("監視塔");
            }
          });
          put("钓", new ArrayList<String>() {
            {
              add("钓鱼岛不属于中国");
              add("钓鱼台");
              add("钓鱼岛");
            }
          });
          put("盧", new ArrayList<String>() {
            {
              add("盧西德");
              add("盧卡");
            }
          });
          put("针", new ArrayList<String>() {
            { add("针孔偷拍"); }
          });
          put("相", new ArrayList<String>() {
            { add("相奸"); }
          });
          put("婬", new ArrayList<String>() {
            { add("婬乱军团"); }
          });
          put("卡", new ArrayList<String>() {
            { add("卡斯特罗"); }
          });
          put("占", new ArrayList<String>() {
            {
              add("占领中环");
              add("占中");
            }
          });
          put("莫", new ArrayList<String>() {
            {
              add("莫索里尼");
              add("莫偉强");
            }
          });
          put("莪", new ArrayList<String>() {
            { add("莪"); }
          });
          put("卫", new ArrayList<String>() {
            { add("卫生部"); }
          });
          put("卵", new ArrayList<String>() {
            {
              add("卵子");
              add("卵");
            }
          });
          put("莲", new ArrayList<String>() {
            { add("莲花逼"); }
          });
          put("足", new ArrayList<String>() {
            { add("足脚交"); }
          });
          put("升", new ArrayList<String>() {
            { add("升天"); }
          });
          put("十", new ArrayList<String>() {
            {
              add("十年动乱石进");
              add("十八摸");
            }
          });
          put("漏", new ArrayList<String>() {
            { add("漏逼"); }
          });
          put("漁", new ArrayList<String>() {
            { add("漁夫菲斯曼"); }
          });
          put("华", new ArrayList<String>() {
            {
              add("华国锋张德江");
              add("华人今日网");
              add("华尔街日报");
              add("华富财经");
              add("华建敏");
              add("华盛顿");
              add("华国锋");
            }
          });
          put("午", new ArrayList<String>() {
            { add("午夜"); }
          });
          put("南", new ArrayList<String>() {
            {
              add("南大自由論壇");
              add("南早中文网");
              add("南洋视界");
              add("南华早报");
              add("南蛮子");
              add("南蠻子");
              add("南联盟");
              add("南蛮");
            }
          });
          put("卖", new ArrayList<String>() {
            {
              add("卖国求荣");
              add("卖党求荣");
              add("卖财富");
              add("卖软件");
              add("卖毒品");
              add("卖.国");
              add("卖号");
              add("卖身");
              add("卖国");
              add("卖卡");
              add("卖淫");
              add("卖比");
              add("卖逼");
              add("卖ID");
              add("卖QQ");
              add("卖B");
            }
          });
          put("卓", new ArrayList<String>() {
            { add("卓伯源"); }
          });
          put("莖", new ArrayList<String>() {
            { add("莖候佳陰"); }
          });
          put("博", new ArrayList<String>() {
            {
              add("博客大赛网站");
              add("博讯网");
              add("博谈网");
              add("博訊");
              add("博讯");
            }
          });
          put("建", new ArrayList<String>() {
            { add("建國黨"); }
          });
          put("萨", new ArrayList<String>() {
            {
              add("萨马兰奇");
              add("萨达姆");
            }
          });
          put("萬", new ArrayList<String>() {
            {
              add("萬維讀者論壇");
              add("萬曉東");
              add("萬潤南");
              add("萬里");
            }
          });
          put("廣", new ArrayList<String>() {
            { add("廣聞"); }
          });
          put("廢", new ArrayList<String>() {
            { add("廢墟守護者"); }
          });
          put("廛", new ArrayList<String>() {
            { add("廛"); }
          });
          put("g", new ArrayList<String>() {
            {
              add("gangcunningci");
              add("gangcunxiushu");
              add("geocities.com");
              add("gongchandang");
              add("gigabyte.cn");
              add("gssmtt.com");
              add("guobaxiong");
              add("gaolipiao");
              add("g365.net");
              add("gogo.net");
              add("gan你");
              add("guojia");
              add("g点");
              add("game");
              add("g片");
              add("gcd");
              add("gm");
            }
          });
          put("廖", new ArrayList<String>() {
            { add("廖錫龍"); }
          });
          put("牛", new ArrayList<String>() {
            {
              add("牛B，牛比");
              add("牛头马面");
              add("牛博网");
              add("牛比");
              add("牛泪");
              add("牛逼");
              add("牛B");
            }
          });
          put("萊", new ArrayList<String>() {
            { add("萊特"); }
          });
          put("廉", new ArrayList<String>() {
            { add("廉政大論壇"); }
          });
          put("吸", new ArrayList<String>() {
            {
              add("吸收的圖騰");
              add("吸精少女");
              add("吸血獸");
              add("吸毒");
              add("吸精");
            }
          });
          put("吹", new ArrayList<String>() {
            {
              add("吹喇叭");
              add("吹箫");
              add("吹萧");
              add("吹簫");
            }
          });
          put("吴", new ArrayList<String>() {
            {
              add("吴帮国");
              add("吴邦国");
              add("吴官正");
              add("吴　仪");
              add("吴仪");
            }
          });
          put("吳", new ArrayList<String>() {
            {
              add("吳敦義");
              add("吳志芳");
              add("吳宏達");
              add("吳百益");
              add("吳學燦");
              add("吳方城");
              add("吳弘達");
              add("吳仁華");
              add("吳淑珍");
              add("吳學璨");
              add("吳育升");
            }
          });
          put("丘", new ArrayList<String>() {
            { add("丘垂貞"); }
          });
          put("含", new ArrayList<String>() {
            { add("含屌"); }
          });
          put("吞", new ArrayList<String>() {
            {
              add("吞精骚妹");
              add("吞精");
            }
          });
          put("吟", new ArrayList<String>() {
            { add("吟稻雁"); }
          });
          put("吗", new ArrayList<String>() {
            {
              add("吗啡片");
              add("吗啡碱");
              add("吗的");
              add("吗啡");
            }
          });
          put("鶏", new ArrayList<String>() {
            {
              add("鶏毛信文匯");
              add("鶏女");
              add("鶏鶏");
              add("鶏奸");
              add("鶏巴");
              add("鶏吧");
              add("鶏院");
              add("鶏八");
              add("鶏8");
            }
          });
          put("后", new ArrayList<String>() {
            { add("后庭"); }
          });
          put("同", new ArrayList<String>() {
            {
              add("同居万岁");
              add("同一教");
              add("同居");
            }
          });
          put("吉", new ArrayList<String>() {
            { add("吉跋猫"); }
          });
          put("吃", new ArrayList<String>() {
            {
              add("吃鸡巴");
              add("吃大便");
              add("吃屎");
              add("吃精");
            }
          });
          put("時", new ArrayList<String>() {
            {
              add("時代論壇");
              add("時事論壇");
            }
          });
          put("籀", new ArrayList<String>() {
            { add("籀"); }
          });
          put("R", new ArrayList<String>() {
            {
              add("RENMINGBAO");
              add("RENMINBAO");
              add("Rape");
              add("RAPE");
              add("RFA");
              add("RFI");
            }
          });
          put("晚", new ArrayList<String>() {
            {
              add("晚年周恩來");
              add("晚年周恩来");
            }
          });
          put("鐾", new ArrayList<String>() {
            { add("鐾"); }
          });
          put("普", new ArrayList<String>() {
            {
              add("普京");
              add("普贤");
            }
          });
          put("财", new ArrayList<String>() {
            { add("财政部"); }
          });
          put("贩", new ArrayList<String>() {
            { add("贩毒"); }
          });
          put("贶", new ArrayList<String>() {
            { add("贶"); }
          });
          put("贱", new ArrayList<String>() {
            {
              add("贱货");
              add("贱逼");
              add("贱人");
              add("贱");
            }
          });
          put("贾", new ArrayList<String>() {
            { add("贾庆林"); }
          });
          put("贺", new ArrayList<String>() {
            {
              add("贺过墙");
              add("贺国强");
              add("贺龙");
            }
          });
          put("留", new ArrayList<String>() {
            { add("留园网"); }
          });
          put("頇", new ArrayList<String>() {
            { add("頇"); }
          });
          put("拿", new ArrayList<String>() {
            { add("拿破仑"); }
          });
          put("秭", new ArrayList<String>() {
            { add("秭"); }
          });
          put("拳", new ArrayList<String>() {
            { add("拳交"); }
          });
          put("拉", new ArrayList<String>() {
            {
              add("拉姆斯菲爾德");
              add("拉姆斯菲尔德");
              add("拉皮條");
              add("拉登");
            }
          });
          put("呂", new ArrayList<String>() {
            {
              add("呂秀蓮");
              add("呂京花");
            }
          });
          put("科", new ArrayList<String>() {
            { add("科萊爾"); }
          });
          put("拍", new ArrayList<String>() {
            { add("拍肩神药"); }
          });
          put("秘", new ArrayList<String>() {
            {
              add("秘唇");
              add("秘裂");
            }
          });
          put("招", new ArrayList<String>() {
            {
              add("招鶏");
              add("招妓");
            }
          });
          put("私", new ArrayList<String>() {
            {
              add("私人服务器");
              add("私—服");
              add("私\\服");
              add("私?服");
              add("私/服");
              add("私-服");
              add("私處");
              add("私服");
            }
          });
          put("拐", new ArrayList<String>() {
            { add("拐卖"); }
          });
          put("拔", new ArrayList<String>() {
            {
              add("拔屄自拍");
              add("拔出来");
              add("拔屄");
            }
          });
          put("頭", new ArrayList<String>() {
            {
              add("頭領墳墓管理員");
              add("頭領奧馬");
            }
          });
          put("沃", new ArrayList<String>() {
            { add("沃尔开西"); }
          });
          put("爷", new ArrayList<String>() {
            {
              add("爷爷");
              add("爷");
            }
          });
          put("爱", new ArrayList<String>() {
            {
              add("爱妻淫穴");
              add("爱图公园");
              add("爱液横流");
              add("爱思想");
              add("爱女人");
              add("爱幼阁");
              add("爱色cc");
              add("爱液");
              add("爱爱");
              add("爱滋");
            }
          });
          put("爽", new ArrayList<String>() {
            {
              add("爽死我了");
              add("爽报");
              add("爽穴");
              add("爽你");
            }
          });
          put("爸", new ArrayList<String>() {
            { add("爸"); }
          });
          put("爹", new ArrayList<String>() {
            { add("爹"); }
          });
          put("庭", new ArrayList<String>() {
            { add("庭院警衛兵"); }
          });
          put("葭", new ArrayList<String>() {
            { add("葭"); }
          });
          put("爭", new ArrayList<String>() {
            { add("爭鳴論壇"); }
          });
          put("董", new ArrayList<String>() {
            {
              add("董文华");
              add("董贱华");
              add("董建华");
            }
          });
          put("爨", new ArrayList<String>() {
            { add("爨"); }
          });
          put("废", new ArrayList<String>() {
            { add("废物"); }
          });
          put("葜", new ArrayList<String>() {
            { add("葜"); }
          });
          put("应", new ArrayList<String>() {
            { add("应召"); }
          });
          put("爝", new ArrayList<String>() {
            { add("爝"); }
          });
          put("閶", new ArrayList<String>() {
            { add("閶"); }
          });
          put("爛", new ArrayList<String>() {
            {
              add("爛比");
              add("爛袋");
              add("爛貨");
              add("爛逼");
              add("爛B");
            }
          });
          put("葉", new ArrayList<String>() {
            { add("葉菊蘭"); }
          });
          put("爆", new ArrayList<String>() {
            {
              add("爆乳人妻");
              add("爆乳娘");
              add("爆草");
              add("爆乳");
              add("爆操");
            }
          });
          put("庋", new ArrayList<String>() {
            { add("庋"); }
          });
          put("庆", new ArrayList<String>() {
            { add("庆红"); }
          });
          put("庀", new ArrayList<String>() {
            { add("庀"); }
          });
          put("郭", new ArrayList<String>() {
            {
              add("郭岩華");
              add("郭俊銘");
              add("郭伯雄");
              add("郭羅基");
            }
          });
          put("郤", new ArrayList<String>() {
            { add("郤"); }
          });
          put("泐", new ArrayList<String>() {
            { add("泐"); }
          });
          put("法", new ArrayList<String>() {
            {
              add("法国国际广播电台");
              add("法广新闻网");
              add("法十輪十功");
              add("法广中文网");
              add("法轮大法");
              add("法輪大法");
              add("法(轮)功");
              add("法*轮*功");
              add("法謫功");
              add("法广网");
              add("法爾卡");
              add("法轮功");
              add("法西斯");
              add("法掄功");
              add("法輪功");
              add("法克鱿");
              add("法制办");
              add("法~綸");
              add("法~輪");
              add("法~論");
              add("法~倫");
              add("法~淪");
              add("法謫");
              add("法功");
              add("法侖");
              add("法淪");
              add("法仑");
              add("法愣");
              add("法轮");
              add("法掄");
              add("法綸");
            }
          });
          put("都", new ArrayList<String>() {
            { add("都市日报"); }
          });
          put("泡", new ArrayList<String>() {
            { add("泡沫經濟"); }
          });
          put("波", new ArrayList<String>() {
            { add("波霸"); }
          });
          put("h", new ArrayList<String>() {
            {
              add("huangsexiaoshuo.1242.net.cn");
              add("hypermart.net");
              add("huashengdun");
              add("haosilu.com");
              add("host800.com");
              add("heguoqiang");
              add("heluxiaofu");
              add("huajianmin");
              add("huiliangyu");
              add("hao1788.co");
              add("haouse.cn");
              add("huyaobang");
              add("huzhiming");
              add("hexun.com");
              add("homexf.cn");
              add("hrichina");
              add("hujintao");
              add("haog8.cn");
              add("hongzhi");
              add("huangju");
              add("huanet");
              add("huimin");
              add("helong");
              add("http");
              add("h站");
            }
          });
          put("郁", new ArrayList<String>() {
            { add("郁慕明"); }
          });
          put("泰", new ArrayList<String>() {
            {
              add("泰奴橋掠奪者");
              add("泰奴橋警衛兵");
            }
          });
          put("比", new ArrayList<String>() {
            {
              add("比樣");
              add("比毛");
            }
          });
          put("丽", new ArrayList<String>() {
            { add("丽春苑"); }
          });
          put("主", new ArrayList<String>() {
            {
              add("主攻指揮官");
              add("主场新闻");
              add("主神教");
              add("主義");
              add("主席");
              add("主佛");
            }
          });
          put("毒", new ArrayList<String>() {
            { add("毒龙舔脚"); }
          });
          put("丰", new ArrayList<String>() {
            {
              add("丰唇艳姬");
              add("丰乳");
            }
          });
          put("中", new ArrayList<String>() {
            {
              add("中华大陆行政执事站");
              add("中華人民實話實說");
              add("中国时报新竹分社");
              add("中華養生益智功");
              add("中国人权双周刊");
              add("中國社會進步黨");
              add("中央日报网络报");
              add("中国茉莉花革命");
              add("中央社新闻网");
              add("中华电视公司");
              add("中國移動通信");
              add("中國真實內容");
              add("中華人民正邪");
              add("中國社會論壇");
              add("中国成人论坛");
              add("中國問題論壇");
              add("中國復興論壇");
              add("中国恐怖组织");
              add("中華真實報道");
              add("中央電視臺");
              add("中国性爱城");
              add("中毒的圖騰");
              add("中央都是猪");
              add("中文搜性网");
              add("中國威脅論");
              add("中國孤兒院");
              add("中欧新闻网");
              add("中时电子报");
              add("中國共産黨");
              add("中國論壇");
              add("中华民国");
              add("中華時事");
              add("中国密报");
              add("中華民國");
              add("中央委员");
              add("中華大衆");
              add("中国之春");
              add("中年美妇");
              add("中華講清");
              add("中華大地");
              add("中國和平");
              add("中毒圖騰");
              add("中国时报");
              add("中俄邊界");
              add("中國之春");
              add("中华日报");
              add("中央社");
              add("中国猪");
              add("中國猪");
              add("中宣部");
              add("中南海");
              add("中央");
              add("中共");
            }
          });
          put("垃", new ArrayList<String>() {
            { add("垃圾游戏"); }
          });
          put("毀", new ArrayList<String>() {
            {
              add("毀滅步兵");
              add("毀滅騎士");
              add("毀滅射手");
            }
          });
          put("毂", new ArrayList<String>() {
            { add("毂"); }
          });
          put("母", new ArrayList<String>() {
            {
              add("母子奸情");
              add("母子交欢");
              add("母女双飞");
              add("母奸");
            }
          });
          put("严", new ArrayList<String>() {
            { add("严方军"); }
          });
          put("两", new ArrayList<String>() {
            {
              add("两腿之间");
              add("两个中国");
              add("两国论");
            }
          });
          put("丝", new ArrayList<String>() {
            {
              add("丝袜淫妇");
              add("丝袜写真");
              add("丝袜高跟");
              add("丝袜足交");
              add("丝袜");
              add("丝诱");
            }
          });
          put("东", new ArrayList<String>() {
            {
              add("东突暴动和独立");
              add("东突组织");
              add("东热空姐");
              add("东条英机");
              add("东亚日报");
              add("东方闪电");
              add("东北独立");
              add("东方日报");
              add("东亚病夫");
              add("东北xx网");
              add("东京热");
              add("东正教");
              add("东伊运");
              add("东升");
              add("东网");
              add("东突");
            }
          });
          put("垵", new ArrayList<String>() {
            { add("垵"); }
          });
          put("毳", new ArrayList<String>() {
            { add("毳"); }
          });
          put("世", new ArrayList<String>() {
            {
              add("世界以利亚福音宣教会");
              add("世界维吾尔大会四人帮");
              add("世界新闻媒体网");
              add("世界电影(台湾)");
              add("世界經濟導報");
              add("世界新闻网");
              add("世界报纸");
              add("世界日报");
              add("世维会");
            }
          });
          put("毿", new ArrayList<String>() {
            { add("毿"); }
          });
          put("专", new ArrayList<String>() {
            {
              add("专制");
              add("专宠");
              add("专政");
            }
          });
          put("不", new ArrayList<String>() {
            {
              add("不爽不要錢");
              add("不滅帝王");
              add("不玩了");
              add("不良");
            }
          });
          put("丌", new ArrayList<String>() {
            { add("丌"); }
          });
          put("下", new ArrayList<String>() {
            {
              add("下流地带");
              add("下注");
              add("下賤");
              add("下贱");
              add("下體");
            }
          });
          put("上", new ArrayList<String>() {
            {
              add("上海孤兒院");
              add("上海幫");
              add("上你");
              add("上妳");
              add("上訪");
            }
          });
          put("三", new ArrayList<String>() {
            {
              add("三去車侖工力");
              add("三班仆人派");
              add("三个呆婊");
              add("三個代表");
              add("三个代婊");
              add("三去车仑");
              add("三民主义");
              add("三級片");
              add("三陪女");
              add("三唑仑");
              add("三级片");
              add("三八淫");
              add("三K黨");
              add("三陪");
              add("三P");
            }
          });
          put("万", new ArrayList<String>() {
            {
              add("万维读者网");
              add("万维博客");
              add("万淫堂");
              add("万税");
            }
          });
          put("丁", new ArrayList<String>() {
            {
              add("丁字裤翘臀");
              add("丁香社区");
              add("丁关根");
              add("丁子霖");
              add("丁關根");
            }
          });
          put("一", new ArrayList<String>() {
            {
              add("一中一台");
              add("一边一国");
              add("一党专制");
              add("一党专政");
              add("一夜性网");
              add("一国两制");
              add("一夜情");
              add("一本道");
              add("一贯道");
              add("一夜欢");
              add("一陀糞");
            }
          });
          put("潮", new ArrayList<String>() {
            { add("潮喷"); }
          });
          put("華", new ArrayList<String>() {
            {
              add("華語世界論壇");
              add("華通時事論壇");
              add("華岳時事論壇");
              add("華夏文摘");
              add("華建敏");
            }
          });
          put("大", new ArrayList<String>() {
            {
              add("大众色情成人网");
              add("大纪元新闻网");
              add("大衆真人真事");
              add("大紀元新聞網");
              add("大中國論壇");
              add("大纪元时报");
              add("大東亞共榮");
              add("大中華論壇");
              add("大家論壇");
              add("大纪元网");
              add("大奶美逼");
              add("大学骚乱");
              add("大胆少女");
              add("大麻树脂");
              add("大波骚妇");
              add("大胆出位");
              add("大力抽送");
              add("大奶骚女");
              add("大波粉B");
              add("大衛教");
              add("大紀園");
              add("大史記");
              add("大鶏巴");
              add("大花逼");
              add("大麻油");
              add("大史紀");
              add("大鸡巴");
              add("大参考");
              add("大卫教");
              add("大奶媽");
              add("大事件");
              add("大東亞");
              add("大血比");
              add("大乱交");
              add("大參考");
              add("大纪元");
              add("大奶头");
              add("大紀元");
              add("大波波");
              add("大血B");
              add("大比");
              add("大师");
              add("大波");
              add("大便");
              add("大法");
              add("大麻");
              add("大乳");
              add("大逼");
              add("大b");
              add("大B");
            }
          });
          put("S", new ArrayList<String>() {
            {
              add("SM舔穴");
              add("SM援交");
              add("SM女王");
              add("SAFEWEB");
              add("SIMPLE");
              add("SYSTEM");
              add("System");
              add("SUCKER");
              add("Server");
              add("SAOBI");
              add("SUCK");
              add("SVDC");
              add("Suck");
              add("SHIT");
              add("SEX");
              add("SM");
              add("SF");
              add("SB");
            }
          });
          put("稅", new ArrayList<String>() {
            { add("稅力"); }
          });
          put("菊", new ArrayList<String>() {
            {
              add("菊花蚕");
              add("菊花蕾");
              add("菊花洞");
            }
          });
          put("包", new ArrayList<String>() {
            {
              add("包二奶");
              add("包皮");
            }
          });
          put("潜", new ArrayList<String>() {
            { add("潜烈蟹"); }
          });
          put("潘", new ArrayList<String>() {
            { add("潘國平"); }
          });
          put("菜", new ArrayList<String>() {
            { add("菜逼"); }
          });
          put("北", new ArrayList<String>() {
            {
              add("北大三角地論壇");
              add("北美自由論壇");
              add("北美中文网");
              add("北京當局");
              add("北京之春");
              add("北京xx网");
              add("北韓");
            }
          });
          put("潑", new ArrayList<String>() {
            { add("潑婦"); }
          });
          put("离", new ArrayList<String>() {
            { add("离长春"); }
          });
          put("禽", new ArrayList<String>() {
            { add("禽獸"); }
          });
          put("禰", new ArrayList<String>() {
            { add("禰"); }
          });
          put("戽", new ArrayList<String>() {
            { add("戽"); }
          });
          put("房", new ArrayList<String>() {
            { add("房事"); }
          });
          put("戴", new ArrayList<String>() {
            {
              add("戴维教");
              add("戴維教");
              add("戴海静");
              add("戴相龍");
              add("戴红");
              add("戴晶");
            }
          });
          put("戳", new ArrayList<String>() {
            {
              add("戳那嗎逼");
              add("戳那嗎比");
              add("戳那嗎B");
              add("戳你");
            }
          });
          put("戈", new ArrayList<String>() {
            {
              add("戈瑞爾德");
              add("戈揚");
            }
          });
          put("战", new ArrayList<String>() {
            { add("战牌"); }
          });
          put("戕", new ArrayList<String>() {
            { add("戕"); }
          });
          put("戔", new ArrayList<String>() {
            { add("戔"); }
          });
          put("禁", new ArrayList<String>() {
            {
              add("禁忌");
              add("禁书");
            }
          });
          put("我", new ArrayList<String>() {
            {
              add("我妳老爸");
              add("我就去色");
              add("我要性交");
              add("我就色");
              add("我咧干");
              add("我日你");
              add("我操你");
              add("我干");
              add("我操");
              add("我日");
              add("我奸");
            }
          });
          put("成", new ArrayList<String>() {
            {
              add("成人午夜场");
              add("成人自拍");
              add("成人文学");
              add("成人导航");
              add("成人论坛");
              add("成人小说");
              add("成人软件");
              add("成人社区");
              add("成人图片");
              add("成人电影");
              add("成人百强");
              add("成人网站");
              add("成人影片");
              add("成人A片");
              add("成人片");
              add("成人bt");
            }
          });
          put("鵒", new ArrayList<String>() {
            { add("鵒"); }
          });
          put("皇", new ArrayList<String>() {
            { add("皇軍"); }
          });
          put("裙", new ArrayList<String>() {
            {
              add("裙内偷拍");
              add("裙下风光");
            }
          });
          put("娘", new ArrayList<String>() {
            {
              add("娘餓比");
              add("娘的");
            }
          });
          put("鑫", new ArrayList<String>() {
            { add("鑫报e乐网"); }
          });
          put("装", new ArrayList<String>() {
            {
              add("装屄");
              add("装逼");
              add("装B");
            }
          });
          put("裏", new ArrayList<String>() {
            { add("裏菲斯"); }
          });
          put("鑣", new ArrayList<String>() {
            { add("鑣"); }
          });
          put("皮", new ArrayList<String>() {
            {
              add("皮條客");
              add("皮条");
            }
          });
          put("娼", new ArrayList<String>() {
            { add("娼"); }
          });
          put("裹", new ArrayList<String>() {
            { add("裹本"); }
          });
          put("毛", new ArrayList<String>() {
            {
              add("毛主席");
              add("毛賊東");
              add("毛泽东");
              add("毛厠洞");
              add("毛一鲜");
              add("毛厕洞");
              add("毛贼东");
              add("毛片");
              add("毛盘");
              add("毛賊");
              add("毛鲍");
              add("毛XX");
            }
          });
          put("裤", new ArrayList<String>() {
            { add("裤袜"); }
          });
          put("皴", new ArrayList<String>() {
            { add("皴"); }
          });
          put("赤", new ArrayList<String>() {
            {
              add("赤裸天使");
              add("赤色戰士");
              add("赤色騎士");
              add("赤匪");
              add("赤裸");
            }
          });
          put("赫", new ArrayList<String>() {
            { add("赫鲁晓夫"); }
          });
          put("田", new ArrayList<String>() {
            {
              add("田紀雲");
              add("田纪云");
            }
          });
          put("由", new ArrayList<String>() {
            { add("由喜貴"); }
          });
          put("甲", new ArrayList<String>() {
            { add("甲庆林"); }
          });
          put("起", new ArrayList<String>() {
            { add("起义"); }
          });
          put("走", new ArrayList<String>() {
            {
              add("走向圆满");
              add("走光偷拍");
              add("走私");
            }
          });
          put("电", new ArrayList<String>() {
            { add("电监会"); }
          });
          put("男", new ArrayList<String>() {
            {
              add("男女蒲典");
              add("男女交欢");
            }
          });
          put("i", new ArrayList<String>() {
            {
              add("itnongzhuang.com2y7v.cnhwxvote.cn");
              add("itnongzhuang.com");
              add("id666.uqc.cn");
              add("ising99.com");
              add("i9game.com");
              add("icpcn.com");
              add("ieboy.cn");
              add("incest");
              add("islam");
              add("isis");
              add("item");
              add("isil");
            }
          });
          put("赍", new ArrayList<String>() {
            { add("赍"); }
          });
          put("赖", new ArrayList<String>() {
            { add("赖昌星"); }
          });
          put("赛", new ArrayList<String>() {
            {
              add("赛妳娘");
              add("赛你娘");
              add("赛她娘");
              add("赛他娘");
            }
          });
          put("梵", new ArrayList<String>() {
            {
              add("梵蒂冈亚洲新闻通讯社");
              add("梵蒂冈广播电台");
            }
          });
          put("耠", new ArrayList<String>() {
            { add("耠"); }
          });
          put("耻", new ArrayList<String>() {
            { add("耻辱轮奸"); }
          });
          put("勾", new ArrayList<String>() {
            {
              add("勾魂少妇");
              add("勾引");
            }
          });
          put("梦", new ArrayList<String>() {
            { add("梦遗"); }
          });
          put("耶", new ArrayList<String>() {
            {
              add("耶和华");
              add("耶稣");
              add("耶穌");
              add("耶苏");
            }
          });
          put("勱", new ArrayList<String>() {
            { add("勱"); }
          });
          put("老", new ArrayList<String>() {
            {
              add("老少乱伦");
              add("老ＧＹ");
              add("老毛子");
              add("老虎机");
              add("老母");
              add("老土");
              add("老逼");
              add("老GY");
            }
          });
          put("古", new ArrayList<String>() {
            {
              add("古龍祭壇");
              add("古柯碱");
            }
          });
          put("勞", new ArrayList<String>() {
            {
              add("勞動教養所");
              add("勞教");
              add("勞改");
            }
          });
          put("邛", new ArrayList<String>() {
            { add("邛"); }
          });
          put("梅", new ArrayList<String>() {
            {
              add("梅德韦杰夫");
              add("梅花屄");
              add("梅花网");
              add("梅毒");
            }
          });
          put("動", new ArrayList<String>() {
            { add("動亂"); }
          });
          put("耐", new ArrayList<String>() {
            { add("耐艹"); }
          });
          put("狼", new ArrayList<String>() {
            { add("狼友"); }
          });
          put("独", new ArrayList<String>() {
            {
              add("独立台湾会");
              add("独立");
            }
          });
          put("狡", new ArrayList<String>() {
            { add("狡猾的達夫"); }
          });
          put("蒙", new ArrayList<String>() {
            {
              add("蒙古達子");
              add("蒙古鞑子");
              add("蒙古达子");
              add("蒙古独立");
              add("蒙古獨立");
              add("蒙尘药");
              add("蒙古獨");
              add("蒙独");
              add("蒙獨");
            }
          });
          put("茎", new ArrayList<String>() {
            { add("茎"); }
          });
          put("狗", new ArrayList<String>() {
            {
              add("狗娘养的");
              add("狗娘養的");
              add("狗操卖逼");
              add("狗狼養的");
              add("狗雜種");
              add("狗卵子");
              add("狗日的");
              add("狗屁");
              add("狗養");
              add("狗干");
              add("狗屎");
              add("狗卵");
              add("狗誠");
              add("狗娘");
              add("狗操");
              add("狗日");
              add("狗b");
            }
          });
          put("蒋", new ArrayList<String>() {
            {
              add("蒋中正");
              add("蒋经国");
              add("蒋介石");
            }
          });
          put("蒇", new ArrayList<String>() {
            { add("蒇"); }
          });
          put("狂", new ArrayList<String>() {
            {
              add("狂操你全家");
              add("狂乳激揺");
              add("狂操");
              add("狂插");
            }
          });
          put("蒂", new ArrayList<String>() {
            { add("蒂顺"); }
          });
          put("鋸", new ArrayList<String>() {
            {
              add("鋸齒通道被遺弃的骷髏");
              add("鋸齒通道骷髏");
            }
          });
          put("极", new ArrayList<String>() {
            {
              add("极品黑丝");
              add("极品奶妹");
              add("极品波神");
              add("极品波霸");
              add("极品白虎");
            }
          });
          put("林", new ArrayList<String>() {
            {
              add("林保華");
              add("林正勝");
              add("林信義");
              add("林長盛");
              add("林重謨");
              add("林佳龍");
              add("林业局");
              add("林彪");
              add("林肯");
            }
          });
          put("簡", new ArrayList<String>() {
            { add("簡肇棟"); }
          });
          put("黃", new ArrayList<String>() {
            {
              add("黃金幼龍");
              add("黃義交");
              add("黃慈萍");
              add("黃伯源");
              add("黃劍輝");
              add("黃仲生");
              add("黃菊");
              add("黃翔");
              add("黃禍");
              add("黃片");
            }
          });
          put("坐", new ArrayList<String>() {
            {
              add("坐台的");
              add("坐台");
              add("坐庄");
            }
          });
          put("黄", new ArrayList<String>() {
            {
              add("黄色小电影");
              add("黄金圣水");
              add("黄色电影");
              add("黄　菊");
              add("黄大仙");
              add("黄菊");
              add("黄盘");
              add("黄祸");
              add("黄片");
              add("黄色");
            }
          });
          put("买", new ArrayList<String>() {
            {
              add("买卖枪支");
              add("买春堂");
              add("买毒品");
              add("买财富");
              add("买春");
            }
          });
          put("乱", new ArrayList<String>() {
            {
              add("乱伦熟女网");
              add("乱交");
              add("乱伦");
            }
          });
          put("黌", new ArrayList<String>() {
            { add("黌"); }
          });
          put("黑", new ArrayList<String>() {
            {
              add("黑白无常");
              add("黑手黨");
              add("黑手党");
              add("黑社会");
              add("黑毛屄");
              add("黑社會");
              add("黑逼");
              add("黑屄");
            }
          });
          put("乩", new ArrayList<String>() {
            { add("乩"); }
          });
          put("默", new ArrayList<String>() {
            {
              add("默罕默德");
              add("默克尔");
            }
          });
          put("习", new ArrayList<String>() {
            {
              add("习仲勋");
              add("习近平");
              add("习远平");
              add("习大大");
            }
          });
          put("乡", new ArrayList<String>() {
            { add("乡巴佬"); }
          });
          put("黢", new ArrayList<String>() {
            { add("黢"); }
          });
          put("欽", new ArrayList<String>() {
            { add("欽本立"); }
          });
          put("乔", new ArrayList<String>() {
            { add("乔石"); }
          });
          put("黪", new ArrayList<String>() {
            { add("黪"); }
          });
          put("欲", new ArrayList<String>() {
            {
              add("欲火焚身");
              add("欲仙欲死");
              add("欲仙欲浪");
              add("欲火");
            }
          });
          put("乌", new ArrayList<String>() {
            { add("乌克兰分离"); }
          });
          put("黲", new ArrayList<String>() {
            { add("黲"); }
          });
          put("黻", new ArrayList<String>() {
            { add("黻"); }
          });
          put("欠", new ArrayList<String>() {
            {
              add("欠人骑");
              add("欠骑");
              add("欠干");
              add("欠操");
              add("欠日");
            }
          });
          put("欢", new ArrayList<String>() {
            {
              add("欢欢娱乐时空");
              add("欢乐性今宵");
            }
          });
          put("黽", new ArrayList<String>() {
            { add("黽"); }
          });
          put("黼", new ArrayList<String>() {
            { add("黼"); }
          });
          put("痴", new ArrayList<String>() {
            {
              add("痴乳");
              add("痴鳩");
              add("痴拈");
            }
          });
          put("趼", new ArrayList<String>() {
            { add("趼"); }
          });
          put("鐘", new ArrayList<String>() {
            { add("鐘山風雨論壇"); }
          });
          put("*", new ArrayList<String>() {
            {
              add("*李*洪*志*阿扁");
              add("*法*轮*功*");
              add("***");
            }
          });
          put("超", new ArrayList<String>() {
            {
              add("超毛大鲍");
              add("超ＧＹ");
              add("超７８");
              add("超她");
              add("超妳");
              add("超他");
              add("超你");
              add("超GY");
              add("超78");
            }
          });
          put("趕", new ArrayList<String>() {
            { add("趕你娘"); }
          });
          put("漢", new ArrayList<String>() {
            { add("漢奸"); }
          });
          put("馒", new ArrayList<String>() {
            { add("馒头屄"); }
          });
          put("馘", new ArrayList<String>() {
            { add("馘"); }
          });
          put("香", new ArrayList<String>() {
            {
              add("香港独立媒体");
              add("香港经济日报");
              add("香港商报");
              add("香港报纸");
              add("香港人网");
              add("香港电台");
            }
          });
          put("硬", new ArrayList<String>() {
            {
              add("硬直圖騰");
              add("硬挺");
            }
          });
          put("扒", new ArrayList<String>() {
            {
              add("扒穴");
              add("扒屄");
            }
          });
          put("打", new ArrayList<String>() {
            {
              add("打倒共产党");
              add("打野炮");
              add("打飛機");
              add("打飞机");
              add("打炮");
            }
          });
          put("j", new ArrayList<String>() {
            {
              add("jj.pangfangwuyuetian.hi.9705.net.cn");
              add("jiangzhongzheng");
              add("jiangdongriji");
              add("jinpaopao.com");
              add("jiangjingguo");
              add("jooplay.com");
              add("jiayyou.com");
              add("jiayyou.cn");
              add("jjdlw.com");
              add("jdyou.com");
              add("jiaochun");
              add("jian你");
              add("jinv");
              add("jiba");
              add("j8");
              add("jb");
            }
          });
          put("馬", new ArrayList<String>() {
            {
              add("馬英九");
              add("馬克思");
              add("馬良駿");
              add("馬三家");
              add("馬永成");
              add("馬時敏");
              add("馬特斯");
              add("馬大維");
            }
          });
          put("手", new ArrayList<String>() {
            { add("手淫"); }
          });
          put("扉", new ArrayList<String>() {
            { add("扉之阴"); }
          });
          put("邪", new ArrayList<String>() {
            { add("邪教"); }
          });
          put("邢", new ArrayList<String>() {
            { add("邢錚"); }
          });
          put("那", new ArrayList<String>() {
            {
              add("那嗎老比");
              add("那嗎錯比");
              add("那娘錯比");
              add("那嗎瘟比");
              add("那嗎逼");
              add("那嗎B");
            }
          });
          put("气", new ArrayList<String>() {
            { add("气象局"); }
          });
          put("邾", new ArrayList<String>() {
            { add("邾"); }
          });
          put("幞", new ArrayList<String>() {
            { add("幞"); }
          });
          put("氵", new ArrayList<String>() {
            {
              add("氵去車侖工力?");
              add("氵去車侖工力");
              add("氵去");
            }
          });
          put("干", new ArrayList<String>() {
            {
              add("干妳老母");
              add("干你妈逼");
              add("干你妈B");
              add("干你妈b");
              add("干死你");
              add("干他妈");
              add("干她妈");
              add("干七八");
              add("干７８");
              add("干你妈");
              add("干妳妈");
              add("干一干");
              add("干拎娘");
              add("干你娘");
              add("干妳娘");
              add("干ＧＹ");
              add("干的爽");
              add("干人也");
              add("干女也");
              add("干您");
              add("干死");
              add("干她");
              add("干穴");
              add("干牠");
              add("干机");
              add("干勒");
              add("干我");
              add("干到");
              add("干妳");
              add("干啦");
              add("干你");
              add("干干");
              add("干爆");
              add("干汝");
              add("干它");
              add("干鸡");
              add("干林");
              add("干他");
              add("干入");
              add("干尼");
              add("干爽");
              add("干78");
              add("干GM");
              add("干gM");
              add("干GY");
              add("干Gm");
              add("干gm");
              add("干X");
              add("干");
            }
          });
          put("幹", new ArrayList<String>() {
            {
              add("幹你老母");
              add("幹你老比");
              add("幹全家");
              add("幹你娘");
              add("幹死你");
              add("幹幹幹");
              add("幹一家");
              add("幹的你");
              add("幹你");
              add("幹逼");
              add("幹炮");
              add("幹比");
              add("幹砲");
              add("幹她");
              add("幹他");
              add("幹死");
              add("幹bi");
            }
          });
          put("氹", new ArrayList<String>() {
            { add("氹"); }
          });
          put("幼", new ArrayList<String>() {
            {
              add("幼香阁");
              add("幼交");
              add("幼妓");
              add("幼逼");
              add("幼圖");
            }
          });
          put("煞", new ArrayList<String>() {
            {
              add("煞逼");
              add("煞笔");
              add("煞筆");
            }
          });
          put("U", new ArrayList<String>() {
            {
              add("ULTRASURF");
              add("UltraSurf");
              add("USTIBET");
              add("UNIXBOX");
              add("U/R");
              add("U R");
              add("U-R");
              add("UR");
            }
          });
          put("煉", new ArrayList<String>() {
            { add("煉功"); }
          });
          put("姫", new ArrayList<String>() {
            { add("姫辱"); }
          });
          put("姬", new ArrayList<String>() {
            { add("姬勝德"); }
          });
          put("姣", new ArrayList<String>() {
            { add("姣西"); }
          });
          put("姦", new ArrayList<String>() {
            {
              add("姦染");
              add("姦淫");
            }
          });
        }
      };

  /**
   * 从SD卡中获取数据
   */
  public void getDirtyWordsFromFile() {
    YueJianAppTLog.info("从屏蔽字文件中读取数据");
    if (mKeyWords.size() > 0) {
      mKeyWords.clear();
    }
    final String fileName = YueJianAppAppContext.getInstance().getProperty("mask_file_name");
        YueJianAppTLog.error(" mask_file_name:%s", fileName);
    File file = new File(YueJianAppAppConfig.DEFAULT_SAVE_FILE_PATH + fileName);
    if (!file.exists() || file.length() == 0) { //如果屏蔽字有问题或者不可使用则重新下载
      String maskWordUrl = YueJianAppAppContext.getInstance().getProperty("mask_word_url");
      if (!TextUtils.isEmpty(maskWordUrl)) { //重新下载机制
        YueJianAppAppContext.getInstance().downLoadMaskWords(maskWordUrl);
      }
      return;
    }
    /**因为是耗时操作，故需要异步进行*/
    YueJianAppThreadManager.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        try {
          FileInputStream is = new FileInputStream(YueJianAppAppConfig.DEFAULT_SAVE_FILE_PATH + fileName);
          InputStreamReader inputReader = new InputStreamReader(is);
          //                    InputStreamReader inputReader = new
          //                    InputStreamReader(mContext.getResources().getAssets().open("key_words.txt"));
          BufferedReader bufReader = new BufferedReader(inputReader);
          String line = "";
          ArrayList<String> mValueList = null;
          String lineHeader = "";
          while ((line = bufReader.readLine()) != null) {
            mValueList = new ArrayList<String>();
            String[] mArrayFilter =
                line.split(","); // TODO 李建涛 暂时用","号进行分割 待修改截取规则
            /**将首字母相同的字符 添加到数组*/
            lineHeader = mArrayFilter[0];
            for (int i = 1; i < mArrayFilter.length; i++) {
              mValueList.add(mArrayFilter[i]); //
            }
            /**将行的首个字符 以及是个字符对应的value集合添加到map*/
            mKeyWords.put(lineHeader, mValueList);
          }
        } catch (FileNotFoundException e) {
          YueJianAppTLog.warn("文件未找到");
          e.printStackTrace();
        } catch (Exception e) {
          YueJianAppTLog.warn("文件操作有误");
          e.printStackTrace();
        }
        //        Log.e(TAG, res);
      }
    });
  }

  /**
   * 从Asserts中获取数据
   */
  public void getDirtyWordsFromAsserts() {
    if (mKeyWords.size() > 0) {
      mKeyWords.clear();
    }
    /**因为是耗时操作，故需要异步进行*/
    YueJianAppThreadManager.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        //                String res = "";
        try {
          /*FileInputStream is = new FileInputStream(YueJianAppAppConfig.DEFAULT_SAVE_FILE_PATH +
          "dirty_words.txt");
          InputStreamReader inputReader = new InputStreamReader(is);*/
          InputStreamReader inputReader =
              new InputStreamReader(mContext.getResources().getAssets().open("ban.csv"));
          BufferedReader bufReader = new BufferedReader(inputReader);
          String line;
          ArrayList<String> mValueList;
          String lineHeader;
          while ((line = bufReader.readLine()) != null) {
            mValueList = new ArrayList<>();
            String[] mArrayFilter =
                line.split(",");
            /**将首字母相同的字符 添加到数组*/
            lineHeader = mArrayFilter[0];
            for (int i = 1; i < mArrayFilter.length; i++) {
              mValueList.add(mArrayFilter[i]); //
            }
            /**将行的首个字符 以及是个字符对应的value集合添加到map*/
            mKeyWords.put(lineHeader, mValueList);
            //                        res += "\n" + line;
          }
        } catch (FileNotFoundException e) {
          YueJianAppTLog.warn("文件未找到");
          e.printStackTrace();
        } catch (Exception e) {
          YueJianAppTLog.warn("文件操作有误");
          e.printStackTrace();
        }
      }
    });
  }
}
