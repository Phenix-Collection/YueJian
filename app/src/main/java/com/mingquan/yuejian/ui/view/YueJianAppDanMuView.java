package com.mingquan.yuejian.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseApplication;
import com.mingquan.yuejian.interf.YueJianAppIImageLoaderImpl;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 弹幕组件
 */
public class YueJianAppDanMuView extends RelativeLayout {
    private Context mContext;
    private int mCurrMsgHeight; //当前弹幕消息的高度
    private int mDefaultHeight = (int) YueJianAppTDevice.dpToPixel(200);
    private List<View> mViews = new ArrayList<>();
    private YueJianAppIImageLoaderImpl mImageLoaderUtil;
    private Random mRandom;

    public YueJianAppDanMuView(Context context) {
        this(context, null);
    }

    public YueJianAppDanMuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YueJianAppDanMuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mRandom = new Random();
        mImageLoaderUtil = YueJianAppBaseApplication.getImageLoaderUtil();
        this.setMinimumHeight(mDefaultHeight);
    }

    /**
     * 添加弹幕 new
     *
     * @param mUser
     * @param text
     */
    public void addDanMu(YueJianAppACUserPublicInfoModel mUser, String text) {
        View view = View.inflate(mContext, R.layout.yue_jian_app_layout_danmu, null);
        TextView mPiaoPingText = (TextView) view.findViewById(R.id.tv_say);
        TextView mTextName = (TextView) view.findViewById(R.id.tv_name);
        YueJianAppAvatarView mAvatar = (YueJianAppAvatarView) view.findViewById(R.id.iv_avatar);
        mImageLoaderUtil.loadImageUserHead(mContext, mUser.getAvatarUrl(), mAvatar);
        mTextName.setText(mUser.getName());
        mPiaoPingText.setText(Html.fromHtml(text));
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mPiaoPingText.measure(width, height);
        mViews.add(view);
        this.addView(view);
        doTranslateAnim(view);
        requestLayout();
    }

    /**
     * 添加弹幕 new
     *
     * @param mUser
     * @param text
     */
    public void addDanMuHorn(YueJianAppACUserPublicInfoModel mUser, String text) {
        View view = View.inflate(mContext, R.layout.yue_jian_app_layout_danmu_new, null);
        TextView mPiaoPingText = (TextView) view.findViewById(R.id.tv_say);
        TextView mTextName = (TextView) view.findViewById(R.id.tv_name);
        YueJianAppAvatarView mAvatar = (YueJianAppAvatarView) view.findViewById(R.id.iv_avatar);
        mImageLoaderUtil.loadImageUserHead(mContext, mUser.getAvatarUrl(), mAvatar);
        mTextName.setText(mUser.getName());
        mPiaoPingText.setText(Html.fromHtml(text)); //设置Html的文字样式
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mPiaoPingText.measure(width, height);
        mViews.add(view);
        this.addView(view);
        doMidStopTranslateAnim(view);
        requestLayout();
    }

    public void doTranslateAnim(final View view) {
        if (this.getVisibility() == View.GONE || this.getVisibility() == View.INVISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
        int visible = view.getVisibility();
        if (visible == View.INVISIBLE || visible == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        mCurrMsgHeight = view.getMeasuredHeight();
        ObjectAnimator animX = ObjectAnimator.ofFloat(
                view, "translationX", getScreenWidth(), getScreenWidth() / 2, 0 - view.getMeasuredWidth());
        int startHeight = getRandomHeightStart();
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY", startHeight, startHeight);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animX, animY);
        animatorSet.setDuration(8000 + mRandom.nextInt(4000));
        animatorSet.start();

        // 根据情况，如果需要监听动画执行到何种“进度”，那么就监听之。
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                mViews.remove(view);
                /**当所有的view执行完动画完成时 移除整个DanMuView*/
                if (mViews == null || mViews.size() <= 0) {
                    YueJianAppDanMuView.this.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public void doMidStopTranslateAnim(final View view) {
        if (this.getVisibility() == View.GONE || this.getVisibility() == View.INVISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
        int visible = view.getVisibility();
        if (visible == View.INVISIBLE || visible == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        mCurrMsgHeight = view.getMeasuredHeight();
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX", getScreenWidth(),
                getScreenWidth() / 2, getScreenWidth() / 2 - 150, 0 - view.getMeasuredWidth());
        int startHeight = getRandomHeightStart();
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY", startHeight, startHeight);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animX, animY);
        animatorSet.setDuration(8000 + mRandom.nextInt(4000));
        animatorSet.start();

        // 根据情况，如果需要监听动画执行到何种“进度”，那么就监听之。
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                mViews.remove(view);
                /**当所有的view执行完动画完成时 移除整个DanMuView*/
                if (mViews == null || mViews.size() <= 0) {
                    YueJianAppDanMuView.this.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * 获得一个 随机的动画开始 时弹幕在view中高度
     *
     * @return
     */
    public int getRandomHeightStart() {
        getDanMuViewHeight();
        Random random = new Random();
        int mStartHeight = random.nextInt(getMeasuredHeight()) + 1 - mCurrMsgHeight;
        return mStartHeight < 0 ? 0 : mStartHeight;
    }

    /**
     * 获取整个view的高度
     */
    private void getDanMuViewHeight() {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        this.measure(width, height);
    }

    /**
     * 获取屏幕的宽高
     *
     * @return
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
}
