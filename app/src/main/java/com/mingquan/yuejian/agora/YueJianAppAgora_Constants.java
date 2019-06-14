package com.mingquan.yuejian.agora;

public class YueJianAppAgora_Constants {
    public static final String ACTION_KEY_ROOM_NAME = "ecHANEL";
    public static final int UID = 0;
    public static final int REQUEST_CODE_ALL_PERMISSIONS = 999;

    public static final int BASE_VALUE_PERMISSION = 0X0001;
    public static final int PERMISSION_REQ_ID_RECORD_AUDIO = BASE_VALUE_PERMISSION + 1;
    public static final int PERMISSION_REQ_ID_CAMERA = BASE_VALUE_PERMISSION + 2;
    public static final int PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = BASE_VALUE_PERMISSION + 3;
    public static final int PERMISSION_REQ_ID_READ_PHONE_STATE = BASE_VALUE_PERMISSION + 4;

    public static int[] VIDEO_PROFILES = new int[]{
            io.agora.rtc.Constants.VIDEO_PROFILE_120P,
            io.agora.rtc.Constants.VIDEO_PROFILE_180P,
            io.agora.rtc.Constants.VIDEO_PROFILE_240P,
            io.agora.rtc.Constants.VIDEO_PROFILE_360P,
            io.agora.rtc.Constants.VIDEO_PROFILE_480P,
            io.agora.rtc.Constants.VIDEO_PROFILE_720P};

    public static final int DEFAULT_PROFILE_IDX = 3;
}
