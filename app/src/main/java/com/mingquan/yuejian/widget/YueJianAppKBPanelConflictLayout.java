package com.mingquan.yuejian.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class YueJianAppKBPanelConflictLayout extends FrameLayout {
    private boolean mHide;

    public YueJianAppKBPanelConflictLayout(@NonNull Context context) {
        super(context);
    }

    public YueJianAppKBPanelConflictLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public YueJianAppKBPanelConflictLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHide) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void hide() {
        this.mHide = true;
        setVisibility(View.GONE);
    }

    public void show() {
        this.mHide = false;
        setVisibility(View.VISIBLE);
    }

}
