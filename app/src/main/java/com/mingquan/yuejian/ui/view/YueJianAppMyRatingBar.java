package com.mingquan.yuejian.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.utils.YueJianAppTDevice;


/**
 * 自定义评分控件
 */
public class YueJianAppMyRatingBar extends LinearLayout {

    private boolean mClickable;
    private int starCount;
    private OnRatingChangeListener onRatingChangeListener;
    private float starImageSize;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;
    private Drawable halfFillDrawable;
    private float marginRightSize;

    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    public void setmClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }


    public YueJianAppMyRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(HORIZONTAL);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.YueJianAppMyRatingBar);
        this.starImageSize = mTypedArray.getDimension(R.styleable.YueJianAppMyRatingBar_starImageSize, 20.0f);
        this.starCount = mTypedArray.getInteger(R.styleable.YueJianAppMyRatingBar_starCount, 5);
        this.starEmptyDrawable = mTypedArray.getDrawable(R.styleable.YueJianAppMyRatingBar_starEmpty);
        this.starFillDrawable = mTypedArray.getDrawable(R.styleable.YueJianAppMyRatingBar_starFill);
        this.halfFillDrawable = mTypedArray.getDrawable(R.styleable.YueJianAppMyRatingBar_halfFill);
        this.mClickable = mTypedArray.getBoolean(R.styleable.YueJianAppMyRatingBar_clickable, false);
        this.marginRightSize = mTypedArray.getDimension(R.styleable.YueJianAppMyRatingBar_marginRightSize, 1f);

        mTypedArray.recycle();

        for (int i = 0; i < this.starCount; i++) {
            ImageView imageView = this.getStarImageView(context, attrs);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (YueJianAppMyRatingBar.this.mClickable) {  //判断星星可以点击
                        YueJianAppMyRatingBar.this.setStar(YueJianAppMyRatingBar.this.indexOfChild(v) + 1);  //设置当前评分
                        if (YueJianAppMyRatingBar.this.onRatingChangeListener != null) {
                            YueJianAppMyRatingBar.this.onRatingChangeListener.onRatingChange(YueJianAppMyRatingBar.this.indexOfChild(v) + 1);  //调用监听接口
                        }
                    }
                }
            });

            LayoutParams lp = new LayoutParams((int) starImageSize, (int) starImageSize);
            lp.setMargins(0, 0, (int) marginRightSize, 0);
            addView(imageView, lp);
        }

    }

    //初始化单个星星控件
    private ImageView getStarImageView(Context context, AttributeSet attrs) {
        ImageView imageView = new ImageView(context);
        imageView.setPadding((int) YueJianAppTDevice.dpToPixel(2), 0, (int) YueJianAppTDevice.dpToPixel(2), 0);
        imageView.setImageDrawable(this.starEmptyDrawable);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        return imageView;
    }

    //设置当前评分
    public void setStar(float mark) {

        //判断评分，不能大于星星总数，不能小于0
        mark = mark > this.starCount ? this.starCount : mark;
        mark = starCount < 0 ? 0 : mark;

//        float xiaoshu = mark - (int) (mark); //计算分数的小数部分
        int zhengshu = (int) mark; //计算分数的整数部分

        //显示整数部分的星星，全部是实心星星
        for (int i = 0; i < zhengshu; ++i) {
            ((ImageView) this.getChildAt(i)).setImageDrawable(this.starFillDrawable);
        }

        //剩余部分用全空星星显示
        for (int j = zhengshu; j < this.starCount; j++) {
            ((ImageView) this.getChildAt(j)).setImageDrawable(this.starEmptyDrawable);
        }

    }

    //定义星星点击的监听接口
    public interface OnRatingChangeListener {
        void onRatingChange(int var1);
    }

}
