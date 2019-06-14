package com.mingquan.yuejian.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/12/5.
 */
public class YueJianAppSquareImageView extends ImageView {
    public YueJianAppSquareImageView(Context context) {
        super(context);
    }

    public YueJianAppSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YueJianAppSquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
