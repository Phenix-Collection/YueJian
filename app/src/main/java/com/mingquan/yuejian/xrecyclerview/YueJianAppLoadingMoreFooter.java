package com.mingquan.yuejian.xrecyclerview;

import android.content.Context;
import android.support.annotation.Dimension;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mingquan.yuejian.R;

import com.mingquan.yuejian.xrecyclerview.progressindicator.YueJianAppAVLoadingIndicatorView;

public class YueJianAppLoadingMoreFooter extends LinearLayout {

    private YueJianAppSimpleViewSwithcer progressCon;
    private Context mContext;
    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    public final static int STATE_SEARCHMORE = 3;
    private int mColor = 0xffB5B5B5;
    private TextView mText;
	public YueJianAppLoadingMoreFooter(Context context) {
		super(context);
		initView(context);
	}
	public YueJianAppLoadingMoreFooter(Context context,int color) {
		super(context);
        mColor = color;
		initView(context);
	}


    /**
	 * @param context
	 * @param attrs
	 */
	public YueJianAppLoadingMoreFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
    public void initView(Context context){
        mContext = context;
        setGravity(Gravity.CENTER);
        setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,HORIZONTAL));
        progressCon = new YueJianAppSimpleViewSwithcer(context);
        progressCon.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,HORIZONTAL));

        YueJianAppAVLoadingIndicatorView progressView = new  YueJianAppAVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(mColor);
        progressView.setIndicatorId(YueJianAppProgressStyle.LineSpinFadeLoader);
        progressCon.setView(progressView);

        addView(progressCon);
        mText = new TextView(context);
        mText.setText("正在加载...");
        mText.setTextColor(mColor);
        mText.setSingleLine();
        mText.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int)mContext.getResources().getDimension(R.dimen.space_50),HORIZONTAL);
        layoutParams.setMargins( (int)getResources().getDimension(R.dimen.space_10),0,0,0 );

        mText.setLayoutParams(layoutParams);
        addView(mText);
    }

    public void setProgressStyle(int style) {
        if(style == YueJianAppProgressStyle.SysProgress){
            progressCon.setView(new ProgressBar(mContext, null, android.R.attr.progressBarStyle));
        }else{
            YueJianAppAVLoadingIndicatorView progressView = new  YueJianAppAVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(mColor);
            progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }

    public void  setState(int state) {
        switch(state) {
            case STATE_LOADING:
                progressCon.setVisibility(View.VISIBLE);
                mText.setText(mContext.getText(R.string.listview_loading));
                mText.setTextColor(mColor);
                this.setVisibility(View.VISIBLE);
                    break;
            case STATE_COMPLETE:
                mText.setText(mContext.getText(R.string.listview_loading));
                mText.setTextColor(mColor);
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(mContext.getText(R.string.no_more));
                mText.setTextColor(mColor);
                mText.setTextSize(Dimension.SP,11);
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_SEARCHMORE:
                mText.setText(mContext.getText(R.string.search_more));
                mText.setTextColor(mColor);
                progressCon.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }

    }
}
