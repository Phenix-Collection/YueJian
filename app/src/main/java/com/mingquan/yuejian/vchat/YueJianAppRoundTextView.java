package com.mingquan.yuejian.vchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.utils.YueJianAppTDevice;

/**
 * 支持圆角的TextView
 * Created by stephen on 2017/12/18.
 */
public class YueJianAppRoundTextView extends AppCompatTextView {

    private static final String[] COLORS = {"#ed7d9e", "#71baf7", "#c7e1a0", "#cbaff4", "#ecb579", "#96e684", "#f1a1b8", "#a2dadd"};
    private int rtvBorderColor;
    private int rtvBgColor;
    private float rtvRadius;
    private int rtvBorderWidth;

    public YueJianAppRoundTextView(Context context) {
        this(context, null);
    }

    public YueJianAppRoundTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public YueJianAppRoundTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.YueJianAppRoundTextView, defStyleAttr, 0);
//
        if (attributes != null) {
            rtvBorderWidth = attributes.getDimensionPixelSize(R.styleable.YueJianAppRoundTextView_rtv_border_width, 0);
            rtvBorderColor = attributes.getColor(R.styleable.YueJianAppRoundTextView_rtv_border_color, Color.BLACK);
            rtvRadius = attributes.getDimension(R.styleable.YueJianAppRoundTextView_rtv_radius, 0);
            rtvBgColor = attributes.getColor(R.styleable.YueJianAppRoundTextView_rtv_bg_color, Color.RED);
            attributes.recycle();
        }

        init();
        invalidate();
    }

    private void init () {
        rtvBgColor = Color.parseColor(COLORS[(int) (Math.random() * COLORS.length)]);
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(rtvBgColor);
        gd.setCornerRadius(rtvRadius);

        if (rtvBorderWidth > 0) {
            gd.setStroke(rtvBorderWidth, rtvBorderColor);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(gd);
        }
        this.setPadding(((int)YueJianAppTDevice.dpToPixel(6)), ((int)YueJianAppTDevice.dpToPixel(3)), ((int)YueJianAppTDevice.dpToPixel(6)), ((int)YueJianAppTDevice.dpToPixel(3)));
        this.setTextColor(Color.WHITE);
        invalidate();
    }

    public int getRtvBorderColor() {
        return rtvBorderColor;
    }

    public void setRtvBorderColor(int rtvBorderColor) {
        this.rtvBorderColor = rtvBorderColor;

    }

    public int getRtvBgColor() {
        return rtvBgColor;
    }

    public void setRtvBgColor(int rtvBgColor) {
        this.rtvBgColor = rtvBgColor;
    }

    public float getRtvRadius() {
        return rtvRadius;
    }

    public void setRtvRadius(float rtvRadius) {
        this.rtvRadius = rtvRadius;
    }

    public int getRtvBorderWidth() {
        return rtvBorderWidth;
    }

    public void setRtvBorderWidth(int rtvBorderWidth) {
        this.rtvBorderWidth = rtvBorderWidth;
    }
}