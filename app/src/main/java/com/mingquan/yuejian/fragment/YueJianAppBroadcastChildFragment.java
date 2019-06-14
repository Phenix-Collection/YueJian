package com.mingquan.yuejian.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppBaseRecyclerMutilAdapter;
import com.mingquan.yuejian.adapter.YueJianAppIndexChildAdapter;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACBannerModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.ui.YueJianAppMainActivity;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;
import com.mingquan.yuejian.vchat.YueJianAppMessageEvent;
import com.mingquan.yuejian.xrecyclerview.YueJianAppLoadingMoreFooter;
import com.mingquan.yuejian.xrecyclerview.YueJianAppRecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 主播列表子布局
 */

public class YueJianAppBroadcastChildFragment extends Fragment {

    private View mRet;
    private YueJianAppMainActivity mContext;

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private YueJianAppIndexChildAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private View emptyView;
    private View loadView;

    private int index;
    private boolean isRefresh;
    private boolean isLoad;
    private boolean isNoMore;
    private boolean isInit;
    private int mLastId;
    private List<String> mRollImageList = new ArrayList<>();
    private List<String> mRollUrlList = new ArrayList<>();

    public static YueJianAppBroadcastChildFragment newInstance(int index) {
        YueJianAppBroadcastChildFragment fragment = new YueJianAppBroadcastChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX", index);
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
            showData();
        }
    }

    /**
     * @param isVisibleToUser true 相当于Fragment的onResume, false 相当于Fragment的onPause
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            showData();
        }
    }

    private void showData() {
        if (isInit) {
            isInit = false;
            // 加载各种数据
            if (mAdapter.getList().size() != 0) {
                return;
            }
            requestData();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (YueJianAppMainActivity) getActivity();
        index = getArguments().getInt("INDEX");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(Object event) {
        if (event instanceof YueJianAppMessageEvent
                && ((YueJianAppMessageEvent) event).getMessage().equals(YueJianAppAppConfig.ACTION_REFRESH_BROADCASTER_LIST)
                && index != 0
                && getUserVisibleHint()) {
            if (mRefreshLayout != null) {
                mRefreshLayout.autoRefresh();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRet == null) {
            mRet = inflater.inflate(R.layout.yue_jian_app_index_fragment_child, null, false);
            initView(mRet);
            initData();
            initEvents();
        }
        isInit = true;
        return mRet;
    }

    public void initView(View view) {
        mRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.hot_refreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.hot_recyclerview);
        emptyView = view.findViewById(R.id.hot_empty_view);
        loadView = view.findViewById(R.id.load_view);
    }

    public void initData() {
        mAdapter = new YueJianAppIndexChildAdapter(mContext);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.addItemDecoration(new YueJianAppRecycleViewDivider(mContext, RecyclerView.VERTICAL, (int) YueJianAppTDevice.dpToPixel(2), R.color.white));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initEvents() {
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                mLastId = 0;
                requestData();
                refreshLayout.finishRefresh();
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
                if (Math.abs(dy) > Math.abs(dx) && dy > 20) {
                    mContext.hideTabHost();
                } else if (Math.abs(dy) > Math.abs(dx) && dy < -20) {
                    mContext.showTabHost();
                }

                if (mLayoutManager.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 1 && dy > 0) {

                    if (isNoMore || mAdapter.getMoreFooter() == null) {
                        return;
                    }

                    if (index == 0) { // 关注列表不需要加载更多
                        return;
                    }

                    isLoad = true;
                    isNoMore = true;
                    mAdapter.getMoreFooter().setState(YueJianAppLoadingMoreFooter.STATE_LOADING);
                    requestData();
                }
            }
        });

        mAdapter.setOnItemClickListener(new YueJianAppBaseRecyclerMutilAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                YueJianAppTLog.info("position :%s", position);
                YueJianAppDialogHelper.showVchatOtherInfoDialogFragment(getChildFragmentManager(),
                        mAdapter.getList().get(position).getUid());
            }
        });
    }

    private void requestData() {
        YueJianAppApiProtoHelper.sendACGetBannerListReq(
                mContext,
                mContext.getPackageName(),
                YueJianAppAppContext.getInstance().getLoginUid(),
                new YueJianAppApiProtoHelper.ACGetBannerListReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error("加载banner数据失败:%s", errMessage);
                        if (index == 0) {
                            followData(); // 关注列表
                        } else {
                            otherData(index); // 推荐，新人，三星，四星，五星列表
                        }
                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACBannerModel> banners) {
                        YueJianAppTLog.info("banners:%s", banners);
                        if (mRollImageList != null) {
                            mRollImageList.clear();
                        }
                        if (mRollUrlList != null) {
                            mRollUrlList.clear();
                        }
                        for (int i = 0; i < banners.size(); i++) {
                            mRollImageList.add(banners.get(i).getImageUrl());
                            mRollUrlList.add(banners.get(i).getLinkUrl());
                        }
                        mAdapter.setImageList(mRollImageList);
                        mAdapter.setUrlList(mRollUrlList);

                        if (index == 0) {
                            followData(); // 关注列表
                        } else {
                            otherData(index); // 推荐，新人，三星，四星，五星列表
                        }
                    }
                });
    }

    private void followData() {
        YueJianAppApiProtoHelper.sendACGetFollowingBroadcastListReq(getActivity(),
                YueJianAppAppContext.getInstance().getLoginUid(),
                new YueJianAppApiProtoHelper.ACGetFollowingBroadcastListReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppAppContext.showToastAppMsg(getActivity(), errMessage);
                        YueJianAppUiUtils.setVisibility(loadView, View.GONE);
                        if (mRefreshLayout == null) {
                            YueJianAppTLog.error("follow data error mRefreshLayout == null !!");
                            return;
                        }
                        if (isRefresh) {
                            isRefresh = false;
                        }
                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACUserPublicInfoModel> broadcasts) {
                        YueJianAppUiUtils.setVisibility(loadView, View.GONE);
                        if (mAdapter == null) {
                            YueJianAppTLog.error("follow data response adapter == null !!");
                            return;
                        }

                        if (mRefreshLayout == null) {
                            YueJianAppTLog.error("follow data response mRefreshLayout == null !!");
                            return;
                        }
                        if (isRefresh) {
                            isRefresh = false;
                        }

                        mAdapter.setList(broadcasts);

                        if (mAdapter.getList().size() == 0) {
                            YueJianAppUiUtils.setVisibility(emptyView, View.VISIBLE);
                        } else {
                            YueJianAppUiUtils.setVisibility(emptyView, View.GONE);
                        }
                        mAdapter.getMoreFooter().setState(YueJianAppLoadingMoreFooter.STATE_NOMORE);
                    }
                }
        );
    }

    private void otherData(int index) {
        YueJianAppApiProtoHelper.sendACGetBroadcastListReq(
                getActivity(),
                index,
                10,
                mLastId,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppApiProtoHelper.GENDER_NONE,
                new YueJianAppApiProtoHelper.ACGetBroadcastListReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppAppContext.showToastAppMsg(getActivity(), errMessage);
                        YueJianAppUiUtils.setVisibility(loadView, View.GONE);
                        if (mAdapter == null) {
                            YueJianAppTLog.error("other data error adapter == null !!");
                            return;
                        }

                        if (mRefreshLayout == null) {
                            YueJianAppTLog.error("other data error mRefreshLayout == null !!");
                            return;
                        }
                        if (isRefresh) {
                            isRefresh = false;
                        } else if (isLoad) {
                            mAdapter.getMoreFooter().setState(YueJianAppLoadingMoreFooter.STATE_COMPLETE);
                            isLoad = false;
                        }
                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACUserPublicInfoModel> broadcasts, int nextOffset) {
                        mLastId = nextOffset;
                        YueJianAppUiUtils.setVisibility(loadView, View.GONE);
                        if (mAdapter == null) {
                            YueJianAppTLog.error("other data response adapter == null !!");
                            return;
                        }

                        if (mRefreshLayout == null) {
                            YueJianAppTLog.error("other data response mRefreshLayout == null !!");
                            return;
                        }
                        if (isRefresh) {
                            isRefresh = false;
                            mAdapter.setList(broadcasts);
                        } else if (isLoad) {
                            mAdapter.getMoreFooter().setState(YueJianAppLoadingMoreFooter.STATE_COMPLETE);
                            isLoad = false;
                            mAdapter.appendList(broadcasts);
                        } else {
                            mAdapter.setList(broadcasts);
                        }
                        if (-1 == nextOffset) {
                            isNoMore = true;
                            mAdapter.getMoreFooter().setState(YueJianAppLoadingMoreFooter.STATE_NOMORE);
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
        );
    }
}
