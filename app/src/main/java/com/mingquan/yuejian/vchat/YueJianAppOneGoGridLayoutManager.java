package com.mingquan.yuejian.vchat;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.mingquan.yuejian.utils.YueJianAppTLog;

/**
 * Created by Administrator on 2018/9/30
 */

public class YueJianAppOneGoGridLayoutManager extends GridLayoutManager {

    public YueJianAppOneGoGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public YueJianAppOneGoGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public YueJianAppOneGoGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            return super.scrollHorizontallyBy(dx, recycler, state);
        } catch (IndexOutOfBoundsException e) {
            YueJianAppTLog.info("hello", "scrollHorizontallyBy, IndexOutOfBoundsException");
            e.printStackTrace();
            return 0;
        }
    }
}