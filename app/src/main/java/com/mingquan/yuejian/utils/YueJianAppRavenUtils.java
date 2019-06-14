package com.mingquan.yuejian.utils;

import android.content.Context;
import android.os.Build;

import com.getsentry.raven.android.Raven;
import com.getsentry.raven.event.Event;
import com.getsentry.raven.event.EventBuilder;
import com.getsentry.raven.event.interfaces.ExceptionInterface;
import com.getsentry.raven.event.interfaces.MessageInterface;
import com.mingquan.yuejian.YueJianAppAppContext;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by administrato on 2017/2/20.
 */

public class YueJianAppRavenUtils {
  public static final String ERROR = "Error";
  public static final String WARNING = "Warning";
  public static final String INFO = "Info";
  public static final String DEBUG = "debug";
  public static final String RELEASE = "release";

  /**
   * 方便外界调用
   *
   * @param context   Application context
   * @param e         Exception
   * @param message   messages
   * @param releseTag release of debug , 'Tag' instead
   * @param level     Raven  level , ex: Error Warning or Informations
   * @param ravenData user data can be modified.
   */
  public static void captureMessage(Context context, Exception e, String message, String releseTag,
      Event.Level level, Map<String, Object> ravenData) {
    EventBuilder eventBuilder = new EventBuilder();
    eventBuilder.withMessage(e.getMessage());
    eventBuilder.withRelease(releseTag);
    if (ravenData != null && !ravenData.isEmpty()) {
      Set<String> keySet = ravenData.keySet();
      if (!keySet.isEmpty()) {
        Iterator<String> iterator = keySet.iterator();
        if (iterator != null) {
          while (iterator.hasNext()) {
            String key = iterator.next();
            eventBuilder.withExtra(key, ravenData.get(key));
          }
        }
      }
    }
    eventBuilder.withExtra("userName", YueJianAppAppContext.getInstance().getLoginUser().getUser_nicename());
    eventBuilder.withExtra("device", Build.MODEL);
    eventBuilder.withExtra(
        "appVersion", YueJianAppAppUtil.getInstance().getAppVersionName(YueJianAppAppContext.getInstance().getApplicationContext()));
    eventBuilder.withExtra("userId", YueJianAppAppContext.getInstance().getLoginUid());
    eventBuilder.withSentryInterface(new ExceptionInterface(e), false);
    eventBuilder.withLevel(level);
    Raven.capture(eventBuilder.build());
  }

  public static void logException(String message, Exception e) {
    logException(message, e, null);
  }

  public static void logException(String message, Exception e, Map<String, Object> ravenData) {
    try {
      EventBuilder eventBuilder = new EventBuilder();
      eventBuilder.withMessage(message);
      if (ravenData != null && !ravenData.isEmpty()) {
        Set<String> keySet = ravenData.keySet();
        if (!keySet.isEmpty()) {
          Iterator<String> iterator = keySet.iterator();
          if (iterator != null) {
            while (iterator.hasNext()) {
              String key = iterator.next();
              eventBuilder.withExtra(key, ravenData.get(key));
            }
          }
        }
      }
      eventBuilder.withExtra(
          "userName", YueJianAppAppContext.getInstance().getLoginUser().getUser_nicename());
      eventBuilder.withExtra("device", Build.MODEL);
      eventBuilder.withExtra("appVersion",
          YueJianAppAppUtil.getInstance().getAppVersionName(YueJianAppAppContext.getInstance().getApplicationContext()));
      eventBuilder.withExtra("userId", YueJianAppAppContext.getInstance().getLoginUid());
      //        eventBuilder.withSentryInterface(new ExceptionInterface(e), false);
      eventBuilder.withSentryInterface(new MessageInterface(message), false);
      eventBuilder.withLevel(Event.Level.ERROR);
//      Raven.capture(eventBuilder.build());
      e.printStackTrace();
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }

  public static void logError(String message) {
    logError(message, null);
  }

  public static void logError(String message, Map<String, Object> ravenData) {
    EventBuilder eventBuilder = new EventBuilder();
    eventBuilder.withMessage(message);
    if (ravenData != null && !ravenData.isEmpty()) {
      Set<String> keySet = ravenData.keySet();
      if (!keySet.isEmpty()) {
        Iterator<String> iterator = keySet.iterator();
        if (iterator != null) {
          while (iterator.hasNext()) {
            String key = iterator.next();
            eventBuilder.withExtra(key, ravenData.get(key));
          }
        }
      }
    }
    eventBuilder.withExtra("userName", YueJianAppAppContext.getInstance().getLoginUser().getUser_nicename());
    eventBuilder.withExtra("device", Build.MODEL);
    eventBuilder.withExtra(
        "appVersion", YueJianAppAppUtil.getInstance().getAppVersionName(YueJianAppAppContext.getInstance().getApplicationContext()));
    eventBuilder.withExtra("userId", YueJianAppAppContext.getInstance().getLoginUid());
    eventBuilder.withSentryInterface(new MessageInterface(message), false);
    eventBuilder.withLevel(Event.Level.ERROR);
    Raven.capture(eventBuilder.build());
  }

  public static void logWarning(String message) {
    logWarning(message, null);
  }

  public static void logWarning(String message, Map<String, Object> ravenData) {
    EventBuilder eventBuilder = new EventBuilder();
    eventBuilder.withMessage(message);
    if (ravenData != null && !ravenData.isEmpty()) {
      Set<String> keySet = ravenData.keySet();
      if (!keySet.isEmpty()) {
        Iterator<String> iterator = keySet.iterator();
        if (iterator != null) {
          while (iterator.hasNext()) {
            String key = iterator.next();
            eventBuilder.withExtra(key, ravenData.get(key));
          }
        }
      }
    }
    eventBuilder.withExtra("userName", YueJianAppAppContext.getInstance().getLoginUser().getUser_nicename());
    eventBuilder.withExtra("device", Build.MODEL);
    eventBuilder.withExtra(
        "appVersion", YueJianAppAppUtil.getInstance().getAppVersionName(YueJianAppAppContext.getInstance().getApplicationContext()));
    eventBuilder.withExtra("userId", YueJianAppAppContext.getInstance().getLoginUid());
    eventBuilder.withLevel(Event.Level.WARNING);
    Raven.capture(eventBuilder.build());
  }
}
