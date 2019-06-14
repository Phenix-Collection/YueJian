package com.mingquan.yuejian.vchat;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingquan.yuejian.R;


/**
 * 标题控件
 */
public class YueJianAppXTemplateTitle extends RelativeLayout {
    private String titleText;
    private int titleColor;
    private RelativeLayout leftBtn;
    private TextView titleTv;


    public YueJianAppXTemplateTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.yue_jian_app_vchat_head_title_layout, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.YueJianAppXTemplateTitle, 0, 0);
        try {
            titleText = ta.getString(R.styleable.YueJianAppXTemplateTitle_title_text);
            titleColor = ta.getColor(R.styleable.YueJianAppXTemplateTitle_title_color, getResources().getColor(R.color.black));
            titleTv = (TextView) findViewById(R.id.header_title);
            titleTv.setText(titleText);
            titleTv.setTextColor(titleColor);
            leftBtn = (RelativeLayout) findViewById(R.id.left_btn);
        } finally {
            ta.recycle();
        }
    }

    /**
     * 标题控件
     *
     * @param titleText 设置标题文案
     */
    public void setTitleText(String titleText) {
        this.titleText = titleText;
        titleTv.setText(titleText);
    }

    /**
     * 设置标题颜色
     * @param color
     */
    public void setTitleColor(int color) {
        this.titleColor = color;
        titleTv.setTextColor(titleColor);
    }

    /**
     * 设置按钮事件
     *
     * @param listener 事件监听
     */
    public void setLeftBtnListener(OnClickListener listener) {
        leftBtn.setOnClickListener(listener);
    }
}
