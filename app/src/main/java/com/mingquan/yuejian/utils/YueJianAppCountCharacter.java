package com.mingquan.yuejian.utils;

/**
 * Created by administrato on 2017/2/5.
 */

public class YueJianAppCountCharacter {
  public static void count(String str) {
    /**
     * 中文字符
     */
    int chCharacter = 0;

    /**
     * 英文字符
     */
    int enCharacter = 0;

    /**
     * 空格
     */
    int spaceCharacter = 0;

    /**
     * 数字
     */
    int numberCharacter = 0;

    /**
     * 其他字符
     */
    int otherCharacter = 0;

    if (null == str || str.equals("")) {
      System.out.println("字符串为空");
      return;
    }

    for (int i = 0; i < str.length(); i++) {
      char tmp = str.charAt(i);
      if ((tmp >= 'A' && tmp <= 'Z') || (tmp >= 'a' && tmp <= 'z')) {
        enCharacter++;
      } else if ((tmp >= '0') && (tmp <= '9')) {
        numberCharacter++;
      } else if (tmp == ' ') {
        spaceCharacter++;
      } else if (isChinese(tmp)) {
        chCharacter++;
      } else {
        otherCharacter++;
      }
    }
    System.out.println("字符串:" + str + "");
    System.out.println("中文字符有:" + chCharacter);
    System.out.println("英文字符有:" + enCharacter);
    System.out.println("数字有:" + numberCharacter);
    System.out.println("空格有:" + spaceCharacter);
    System.out.println("其他字符有:" + otherCharacter);
  }

  /**
   * 获取自定义的字数统计
   * 一个汉字相当于两个英文
   * 数字算为英文
   *
   * @return
   */
  public static int getCustomWordCount(CharSequence str) {
    /**
     * 中文字符
     */
    int chCharacter = 0;

    /**
     * 英文字符
     */
    int enCharacter = 0;

    /**
     * 空格
     */
    int spaceCharacter = 0;

    /**
     * 数字
     */
    int numberCharacter = 0;

    /**
     * 其他字符
     */
    int otherCharacter = 0;

    if (null == str || str.equals("")) {
      return 0;
    }

    for (int i = 0; i < str.length(); i++) {
      char tmp = str.charAt(i);
      if ((tmp >= 'A' && tmp <= 'Z') || (tmp >= 'a' && tmp <= 'z')) {
        enCharacter++;
      } else if ((tmp >= '0') && (tmp <= '9')) {
        numberCharacter++;
      } else if (tmp == ' ') {
        spaceCharacter++;
      } else if (isChinese(tmp)) {
        chCharacter++;
      } else {
        otherCharacter++;
      }
    }
    return (enCharacter + numberCharacter + 1) / 2 + chCharacter;
  }

  /***
   * 判断字符是否为中文
   *
   * @param ch 需要判断的字符
   * @return 中文返回true，非中文返回false
   */
  private static boolean isChinese(char ch) {
    //获取此字符的UniCodeBlock
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
    //  GENERAL_PUNCTUATION 判断中文的“号
    //  CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
    //  HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
        || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
      //      System.out.println(ch + " 是中文");
      return true;
    }
    return false;
  }
}
