package com.mingquan.yuejian.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Constraints;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * 结合KBPanelConflictLayout，解决切换软键盘冲突的问题
 */
public class YueJianAppKBRootConflictLayout extends LinearLayout {

    private YueJianAppKBPanelConflictLayout mKBPanelConflictLayout;
    private int mOldHeight = -1;
    private boolean isTouchClose = false;

    public YueJianAppKBRootConflictLayout(@NonNull Context context) {
        super(context);
    }

    public YueJianAppKBRootConflictLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public YueJianAppKBRootConflictLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        preNotifyChild(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void preNotifyChild(int width, int height) {
        if (mOldHeight < 0) {
            mOldHeight = height;
            return;
        }
        int deltaY = height - mOldHeight;
        mOldHeight = height;
        int minKeyboardHeight = 180;
        if (Math.abs(deltaY) >= minKeyboardHeight) {
            if (deltaY < 0) {
                // 键盘弹起
                if (mKBPanelConflictLayout != null) {
                    // 隐藏面板
                    mKBPanelConflictLayout.hide();
                    Log.e(Constraints.TAG, "隐藏面板");
                }
            } else {
                // 键盘收起
                if (mKBPanelConflictLayout != null) {
                    // 显示面板
                    Log.e(Constraints.TAG, "显示面板");
                    if (isTouchClose) {
                        Log.e(Constraints.TAG, "set is touch close false");
                        isTouchClose = false;
                        mKBPanelConflictLayout.show();
                    }
                }
            }
        }
    }

    public void setKBPanelConflictLayout(YueJianAppKBPanelConflictLayout kBPanelConflictLayout) {
        mKBPanelConflictLayout = kBPanelConflictLayout;
    }

    public void setIsTouchClose(boolean isTouchClose) {
        this.isTouchClose = isTouchClose;
    }

}
