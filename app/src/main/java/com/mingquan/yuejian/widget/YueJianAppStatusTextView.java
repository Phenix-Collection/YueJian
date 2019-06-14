package com.mingquan.yuejian.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.utils.YueJianAppTDevice;

import java.util.ArrayList;

public class YueJianAppStatusTextView extends AppCompatTextView {

    private ArrayList<Drawable> mDrawables;
    // 离线 在线 再聊 勿扰
    private static String[] broadcasterStatus; // 主播状态
    private static String[] broadcasterStatusColor; // 主播状态颜色
    private static String[] authStatus; // 审核状态

    public YueJianAppStatusTextView(Context context) {
        super(context);
        init();
    }

    public YueJianAppStatusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YueJianAppStatusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        broadcasterStatus = getResources().getStringArray(R.array.broadcaster_status);
        broadcasterStatusColor = getResources().getStringArray(R.array.broadcaster_status_color);
        authStatus = getResources().getStringArray(R.array.auth_status);
        mDrawables = new ArrayList<>();
        Drawable lixian = getResources().getDrawable(R.drawable.yue_jian_app_zt_lixian);
        Drawable zaixian = getResources().getDrawable(R.drawable.yue_jian_app_zt_zaixian);
        Drawable zailiao = getResources().getDrawable(R.drawable.yue_jian_app_zt_zailiao);
        Drawable wurao = getResources().getDrawable(R.drawable.yue_jian_app_zt_wurao);
        mDrawables.add(lixian);
        mDrawables.add(zaixian);
        mDrawables.add(zailiao);
        mDrawables.add(wurao);
        setGravity(Gravity.CENTER);
        setCompoundDrawablePadding((int) YueJianAppTDevice.dpToPixel(2));
        setPadding((int) YueJianAppTDevice.dpToPixel(4), (int) YueJianAppTDevice.dpToPixel(1.5f), (int) YueJianAppTDevice.dpToPixel(4), (int) YueJianAppTDevice.dpToPixel(1.5f));
//        setPadding((int) getResources().getDimension(R.dimen.space_8), 0, 0, 0);
    }

    /**
     * @param status 1在线
     */
    public void setStatus(int status, String statusBar) {
//        setBackground(mDrawables.get(status));
//        setText(broadcasterStatus[status]);
        setBackground(broadcasterStatusColor[status]);
        setText(statusBar);
        setTextColor(Color.parseColor(broadcasterStatusColor[status]));
    }

    /**
     * 设置视频审核状态
     *
     * @param authStatus
     */
    public void setVideoAuthStatus(int authStatus) {
        switch (authStatus) {
            case YueJianAppApiProtoHelper.AUTH_STATUS_NONE: // 待审
//                setBackground(mDrawables.get(3));
                setBackground(broadcasterStatusColor[3]);
                setTextColor(Color.parseColor(broadcasterStatusColor[3]));
                break;
            case YueJianAppApiProtoHelper.AUTH_STATUS_WAITING: // 在审
//                setBackground(mDrawables.get(0));
                setBackground(broadcasterStatusColor[0]);
                setTextColor(Color.parseColor(broadcasterStatusColor[0]));
                break;
            case YueJianAppApiProtoHelper.AUTH_STATUS_REJECTED: // 拒审
//                setBackground(mDrawables.get(2));
                setBackground(broadcasterStatusColor[2]);
                setTextColor(Color.parseColor(broadcasterStatusColor[2]));
                break;
            case YueJianAppApiProtoHelper.AUTH_STATUS_CERTIFIED: // 过审
//                setBackground(mDrawables.get(1));
                setBackground(broadcasterStatusColor[1]);
                setTextColor(Color.parseColor(broadcasterStatusColor[1]));
                break;
        }
        setText(this.authStatus[authStatus]);
    }

    /**
     * 设置背景颜色
     */
    public void setBackground(String color) {
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setStroke((int) YueJianAppTDevice.dpToPixel(0.5f), Color.parseColor(color));
        gd.setCornerRadius(YueJianAppTDevice.dpToPixel(15));
        setBackgroundDrawable(gd);
        GradientDrawable dot = new GradientDrawable();
        dot.setCornerRadius(YueJianAppTDevice.dpToPixel(1.5f));
        dot.setColor(Color.parseColor(color));
        dot.setSize((int) YueJianAppTDevice.dpToPixel(3), (int) YueJianAppTDevice.dpToPixel(3));
        dot.setBounds(0, 0, dot.getMinimumWidth(), dot.getMinimumHeight());
        setCompoundDrawables(dot, null, null, null);
    }
}
