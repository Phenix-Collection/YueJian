package com.mingquan.yuejian.xrecyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

/*带上拉加载的RecyclerView*/
public class YueJianAppXRecyclerViewFooter extends RecyclerView {

    private Context mContext;
    private boolean isLoadingData = false;
    private boolean isnomore = false;
    private int mLoadingMoreProgressStyle = YueJianAppProgressStyle.LineSpinFadeLoader;
    private ArrayList<View> mFootViews = new ArrayList<>();
    private Adapter mAdapter;
    private Adapter mWrapAdapter;
    private float mLastY = -1;
    private LoadingListener mLoadingListener;


    private boolean loadingMoreEnabled = true;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = -3;
    //adapter没有数据的时候显示,类似于listView的emptyView
    private View mEmptyView;

    public YueJianAppXRecyclerViewFooter(Context context) {
        this(context, null);
    }

    public YueJianAppXRecyclerViewFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YueJianAppXRecyclerViewFooter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        YueJianAppLoadingMoreFooter footView = new YueJianAppLoadingMoreFooter(mContext);
        footView.setProgressStyle(mLoadingMoreProgressStyle);
        addFootView(footView);
        mFootViews.get(0).setVisibility(GONE);
//        View footerView = LayoutInflater.from(context).inflate(R.layout.yue_jian_app_agb_list_bottom_no_more_layout,null,false);
//        footerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        addFootView(footerView);
//        mFootViews.get(1).setVisibility(GONE);
    }


    public void addFootView(final View view) {
        mFootViews.clear();
        mFootViews.add(view);
    }

    public void loadMoreComplete() {
        isLoadingData = false;
        View footView = mFootViews.get(0);
        if (footView instanceof YueJianAppLoadingMoreFooter) {
            ((YueJianAppLoadingMoreFooter) footView).setState(YueJianAppLoadingMoreFooter.STATE_COMPLETE);
        } else {
            footView.setVisibility(View.GONE);
        }

    }

    public void setIsnomore(boolean isnomore){
        this.isnomore = isnomore;
        View footView = mFootViews.get(0);
        ((YueJianAppLoadingMoreFooter) footView).setState(this.isnomore ? YueJianAppLoadingMoreFooter.STATE_NOMORE:YueJianAppLoadingMoreFooter.STATE_COMPLETE);
    }
    public void reset(){
        setIsnomore(false);
        loadMoreComplete();
    }

    public void noMoreLoading() {
        isLoadingData = false;
        View footView = mFootViews.get(0);
        isnomore = true;
        if (footView instanceof YueJianAppLoadingMoreFooter) {
            ((YueJianAppLoadingMoreFooter) footView).setState(YueJianAppLoadingMoreFooter.STATE_NOMORE);
        } else {
            footView.setVisibility(View.GONE);
        }
    }

    public void setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        if (!enabled) {
            if (mFootViews.size() > 0) {
                mFootViews.get(0).setVisibility(GONE);
            }
        }
    }


    public void setLoadingMoreProgressStyle(int style) {
        mLoadingMoreProgressStyle = style;
        if (mFootViews.size() > 0 && mFootViews.get(0) instanceof YueJianAppLoadingMoreFooter) {
            ((YueJianAppLoadingMoreFooter) mFootViews.get(0)).setProgressStyle(style);
        }
    }


    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        mWrapAdapter = new WrapAdapter(mFootViews, adapter);
        super.setAdapter(mWrapAdapter);
        mAdapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        //state == RecyclerView.SCROLL_STATE_IDLE &&
        if ( mLoadingListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
//            if (layoutManager.getChildCount() > 0
//                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 4 && layoutManager.getItemCount() > layoutManager.getChildCount() && !isnomore&&!isLoadingData) {
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 6 && layoutManager.getItemCount() > layoutManager.getChildCount() && !isnomore&&!isLoadingData) {
                View footView = mFootViews.get(0);
                isLoadingData = true;
                if (footView instanceof YueJianAppLoadingMoreFooter) {
                    ((YueJianAppLoadingMoreFooter) footView).setState(YueJianAppLoadingMoreFooter.STATE_LOADING);
                } else {
                    footView.setVisibility(View.VISIBLE);
                }
                mLoadingListener.onLoadMore();
            }
        }
    }

    private static final String TAG = "XRecyclerViewFooterNew";

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
//                if (isOnTop() ) {
//                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
//                    if (mRefreshHeader.getVisiableHeight() > 0 && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
//                        Log.i("getVisiableHeight", "getVisiableHeight = " + mRefreshHeader.getVisiableHeight());
//                        Log.i("getVisiableHeight", " mRefreshHeader.getState() = " + mRefreshHeader.getState());
//                        return false;
//                    }
//                }
                break;
            default:
                mLastY = -1; // reset
//                if (isOnTop() && pullRefreshEnabled) {
//                    if (mRefreshHeader.releaseAction()) {
//                        if (mLoadingListener != null) {
//                            mLoadingListener.onRefresh();
//                        }
//                    }
//                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }


    private final AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                int emptyCount = 0;
                if (loadingMoreEnabled) {
                    emptyCount++;
                }
                if (adapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    YueJianAppXRecyclerViewFooter.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    YueJianAppXRecyclerViewFooter.this.setVisibility(View.VISIBLE);
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

//        @Override
//        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
//            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
//        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    private class WrapAdapter extends Adapter<ViewHolder> {

        private Adapter adapter;


        private ArrayList<View> mFootViews;


        public WrapAdapter(ArrayList<View> footViews, Adapter adapter) {
            this.adapter = adapter;
            this.mFootViews = footViews;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isFooter(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && ( isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }


        public boolean isFooter(int position) {
            return position < getItemCount() && position >= getItemCount() - mFootViews.size();
        }



        public int getFootersCount() {
            return mFootViews.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mFootViews.get(0));
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int adjPosition = position ;
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }
        }

        @Override
        public int getItemCount() {
            if (adapter != null) {
                return getFootersCount() + adapter.getItemCount();
            } else {
                return getFootersCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }
            int adjPosition = position;
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return TYPE_NORMAL;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= 0) {
                int adjPosition = position ;
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.unregisterAdapterDataObserver(observer);
            }
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.registerAdapterDataObserver(observer);
            }
        }

        private class SimpleViewHolder extends ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public interface LoadingListener {

        void onLoadMore();
    }

}
