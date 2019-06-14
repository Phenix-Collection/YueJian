package com.mingquan.yuejian.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppBaseRecyclerMutilAdapter;
import com.mingquan.yuejian.adapter.YueJianAppVChatVideoAdapter;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACVideoInfoModel;
import com.mingquan.yuejian.ui.YueJianAppMainActivity;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;
import com.mingquan.yuejian.vchat.YueJianAppOneGoGridLayoutManager;
import com.lzy.widget.HeaderScrollHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 短视频列表子布局
 */

public class YueJianAppShortVideoChildFragment extends Fragment implements HeaderScrollHelper.ScrollableContainer {

    @BindView(R.id.hot_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.hot_refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.hot_empty_view)
    View emptyView;
    @BindView(R.id.load_view)
    View loadView;
    Unbinder unbinder;

    private View mRet;
    private Context mContext;
    private YueJianAppVChatVideoAdapter mAdapter;
    private YueJianAppOneGoGridLayoutManager mLayoutManager;

    private boolean isRefresh;
    private boolean isLoad;
    private boolean isNoMore;

    private int mLastId;

    private int videoType; // 视频类型
    private String targetUid; // 视频列表的uid


    /**
     * @param videoType // 视频类型
     * @param targetUid // 视频所属uid（个人页面专用）
     * @return
     */
    public static YueJianAppShortVideoChildFragment newInstance(int videoType, String targetUid) {
        YueJianAppShortVideoChildFragment fragment = new YueJianAppShortVideoChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("VIDEO_TYPE", videoType);
        bundle.putString("TARGET_UID", targetUid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 判断当前fragment是否显示
        if (getUserVisibleHint()) {
            mLastId = 0;
            getVideoList();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // 相当于Fragment的onResume
            mLastId = 0;
            getVideoList();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        videoType = getArguments().getInt("VIDEO_TYPE");
        targetUid = getArguments().getString("TARGET_UID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRet == null) {
            mRet = inflater.inflate(R.layout.yue_jian_app_new_hot_fragment_child, null, false);
            initView(mRet);
            initData();
            initEvents();
        }

        unbinder = ButterKnife.bind(this, mRet);
        return mRet;
    }

    public void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.hot_recyclerview);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.hot_refreshLayout);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.home_pink));
        emptyView = view.findViewById(R.id.hot_empty_view);
        loadView = view.findViewById(R.id.load_view);
    }

    public void initData() {
        mAdapter = new YueJianAppVChatVideoAdapter(mContext);
        if (videoType == 0 && targetUid.equals(YueJianAppAppContext.getInstance().getLoginUid())) {
            mAdapter.setShowAuthStatus(true);
        }

        mLayoutManager = new YueJianAppOneGoGridLayoutManager(mContext, 2);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                if (mAdapter.getItemViewType(itemPosition) == YueJianAppVChatVideoAdapter.TYPE_COMMON) {
                    if (itemPosition % 2 == 0) {
                        outRect.right = 2;
                        outRect.bottom = 4;
                    } else {
                        outRect.left = 2;
                        outRect.bottom = 4;
                    }
                }
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    YueJianAppMainActivity mainActivity;

    private void initEvents() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLastId = 0;
                isRefresh = true;
                isNoMore = false;
                getVideoList();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (getActivity() instanceof YueJianAppMainActivity) {
                    mainActivity = (YueJianAppMainActivity) getActivity();
                    if (Math.abs(dy) > Math.abs(dx) && dy > 20) {
                        mainActivity.hideTabHost();
                    } else if (Math.abs(dy) > Math.abs(dx) && dy < -20) {
                        mainActivity.showTabHost();
                    }
                }

                //加载更多，还剩8个的时候便开始请求
                if (mLayoutManager.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 2 && dy > 0) {
                    if (!isNoMore) {
                        isLoad = true;
                        isNoMore = true;
                        getVideoList();
                    }
                }
            }
        });

        mAdapter.setOnItemClickListener(new YueJianAppBaseRecyclerMutilAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (pos >= mAdapter.getList().size()) {
                    return;
                }

                YueJianAppUIHelper.showShortVideoPlayerActivity(getActivity(), mAdapter.getList(), pos);
            }
        });
    }

    /**
     * 发送关闭用户详情页的广播
     */
    private void sendCloseBroadcast() {
        Intent intent = new Intent();
        intent.setAction(YueJianAppAppConfig.ACTION_CLOSE_OTHER_INFO_DIALOG_FRAGMENT);
        if (mContext != null)
            mContext.sendBroadcast(intent);
    }

    /**
     * 获取短视频列表
     */
    private void getVideoList() {
        if (videoType == 0) {
            // 获取个人的短视频列表
            YueJianAppApiProtoHelper.sendACGetBroadcasterVideoListReq(
                    getActivity(),
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    targetUid,
                    8,
                    mLastId,
                    new YueJianAppApiProtoHelper.ACGetBroadcasterVideoListReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            YueJianAppAppContext.showToastAppMsg(getActivity(), errMessage);
                            YueJianAppUiUtils.setVisibility(loadView, View.GONE);
                            if (mAdapter != null && mRefreshLayout != null) {
                                if (isRefresh) {
                                    mRefreshLayout.setRefreshing(false);
                                    isRefresh = false;
                                } else if (isLoad) {
                                    isLoad = false;
                                }
                            }
                        }

                        @Override
                        public void onResponse(ArrayList<YueJianAppACVideoInfoModel> videos, int nextOffset) {
                            fillData(videos, nextOffset);
                        }
                    }
            );
        } else {
            YueJianAppApiProtoHelper.sendACGetVideoListReq(
                    getActivity(),
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    videoType,
                    8,
                    mLastId,
                    new YueJianAppApiProtoHelper.ACGetVideoListReqCallback() {

                        @Override
                        public void onError(int errCode, String errMessage) {
                            YueJianAppAppContext.showToastAppMsg(getActivity(), errMessage);
                            YueJianAppUiUtils.setVisibility(loadView, View.GONE);
                            if (mAdapter != null && mRefreshLayout != null) {
                                if (isRefresh) {
                                    mRefreshLayout.setRefreshing(false);
                                    isRefresh = false;
                                } else if (isLoad) {
                                    isLoad = false;
                                }
                            }
                        }

                        @Override
                        public void onResponse(ArrayList<YueJianAppACVideoInfoModel> videos, int nextOffset) {
                            fillData(videos, nextOffset);
                        }
                    }
            );
        }
    }

    private void fillData(ArrayList<YueJianAppACVideoInfoModel> videos, int nextOffset) {
        mLastId = nextOffset;
        YueJianAppUiUtils.setVisibility(loadView, View.GONE);
        if (mAdapter != null && mRefreshLayout != null) {
            if (isRefresh) {
                mRefreshLayout.setRefreshing(false);
                isRefresh = false;
                mAdapter.setList(videos);
            } else if (isLoad) {
                isLoad = false;
                mAdapter.appendList(videos);
            } else {
                mAdapter.setList(videos);
            }
            if (-1 == nextOffset) {
                isNoMore = true;
                mLastId = 0;
            } else {
                isNoMore = false;
            }
            if (mAdapter.getList().size() == 0) {
                YueJianAppUiUtils.setVisibility(emptyView, View.VISIBLE);
            } else {
                YueJianAppUiUtils.setVisibility(emptyView, View.GONE);
            }
        }
    }


    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
