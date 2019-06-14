package com.mingquan.yuejian.ui.view;



import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * 全屏VideoView
 */
public class YueJianAppFullScreenVideoView extends VideoView {


    public YueJianAppFullScreenVideoView(Context context) {
        super(context);
// TODO Auto-generated constructor stub
    }
    public YueJianAppFullScreenVideoView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
    }
    public YueJianAppFullScreenVideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context,attrs,defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width , height);
    }


}
