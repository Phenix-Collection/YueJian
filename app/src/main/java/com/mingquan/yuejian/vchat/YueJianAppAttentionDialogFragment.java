package com.mingquan.yuejian.vchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppUserBaseInfoAdapter;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserReleationInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by administrato on 2016/11/10.
 * <p>
 * 关注列表页面
 */

public class YueJianAppAttentionDialogFragment extends YueJianAppBaseDialogFragment {
    @BindView(R.id.lv_attentions)
    ListView mAttentionView;
    @BindView(R.id.sr_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.ll_attention_default)
    LinearLayout mLlAttentionNoDataView;
    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle mTemplateTitle;
    private List<YueJianAppACUserReleationInfoModel> mAttentionList = new ArrayList<>();
    YueJianAppUserBaseInfoAdapter mAdapter;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_attention, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();

        mTemplateTitle.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mAttentionView.setDividerHeight(0);

        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.actionbarbackground));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mAttentionList != null) {
                    mAttentionList.clear();
                }
                initData();
                mRefreshLayout.setRefreshing(false);
            }
        });
        mRefreshLayout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                registerReceiver();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (getActivity() != null)
                    getActivity().unregisterReceiver(closeReceiver);
            }
        });
    }

    protected BroadcastReceiver closeReceiver = null;

    //关闭的广播接收者
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(YueJianAppAppConfig.ACTION_CLOSE_ALL_DIALOG_FRAGMENT);
        closeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(YueJianAppAppConfig.ACTION_CLOSE_ALL_DIALOG_FRAGMENT)) {
                    dismiss();
                }
            }
        };
        if (getActivity() != null) {
            getActivity().registerReceiver(closeReceiver, intentFilter);
        }
    }

    public void initData() {
        YueJianAppApiProtoHelper.sendACGetFollowedUserListReq(
                getActivity(),
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getLoginUid(),
                new YueJianAppApiProtoHelper.ACGetFollowedUserListReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppAppContext.showToastAppMsg(getActivity(), errMessage);
                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACUserReleationInfoModel> users) {
                        mAttentionList = users;
                        fillUI();
                    }
                });
    }

    private void fillUI() {
        if (mLlAttentionNoDataView == null) {
            return;
        }
        if (mAttentionList == null || mAttentionList.size() <= 0) {
            mLlAttentionNoDataView.setVisibility(View.VISIBLE);
            return;
        }

        mLlAttentionNoDataView.setVisibility(View.GONE);
        mAdapter = new YueJianAppUserBaseInfoAdapter(mAttentionList, getActivity());
        mAttentionView.setAdapter(mAdapter);
        mAttentionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (YueJianAppUtils.isFastClick()) {
                    return;
                }
                YueJianAppDialogHelper.showVchatOtherInfoDialogFragment(
                        getActivity().getSupportFragmentManager(),
                        mAttentionList.get(position).getUser().getUid()
                );
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().post(new YueJianAppMessageEvent("REFRESH"));
    }
}
