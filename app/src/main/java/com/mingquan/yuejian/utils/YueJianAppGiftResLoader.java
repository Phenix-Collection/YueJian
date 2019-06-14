package com.mingquan.yuejian.utils;

import android.content.Context;
import android.text.TextUtils;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.bean.YueJianAppSurfaceFrameBean;
import com.mingquan.yuejian.proto.model.YueJianAppACResourceModel;

/**
 * Created by Jiantao on 2017/5/18.
 */

public class YueJianAppGiftResLoader {
  /**
   * 生成下载文件名  resourceId.zip
   *
   * @param model
   * @return
   */
  public static String generateResName(YueJianAppACResourceModel model) {
    return model.getResourceId() + ".zip";
  }

  public static String generateResFolder(String folderName) {
    return YueJianAppAppConfig.DEFAULT_SAVE_GIFT_PATH + folderName;
  }

  public static String getDownloadFileUnZipFolderName(String fileName) {
    if (TextUtils.isEmpty(fileName))
      return null;
    //    String[] subFileNames=fileName.split(";");
    //保存的zipName == dog_fd600d6323538c6645400902f9bb75e9_35945.zip
    String upZipFileFolder = "";
    if (fileName.contains("_")) {
      upZipFileFolder = fileName.substring(0, fileName.indexOf("_"));
    } else {
      upZipFileFolder = fileName.replace(".zip", "");
    }
    return upZipFileFolder;
  }

  public static void loadResourceFiles(
          Context context, YueJianAppSurfaceFrameBean surfaceFrameBean, OnGiftResLoadedListener listener) {
    //        String fileFolderPath=generateResFolder(surfaceFrameBean.get)
  }

  /**
   * 监听接口
   */
  public interface OnGiftResLoadedListener { void onEquipLoaded(); }

  private OnGiftResLoadedListener mOnGiftResLoadedListener;
}
