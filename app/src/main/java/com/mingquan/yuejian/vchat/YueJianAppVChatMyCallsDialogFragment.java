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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACChatInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppTimeFormater;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sun on 2018/9/5.
 * <p>
 * 我的通话页面
 */

public class YueJianAppVChatMyCallsDialogFragment extends YueJianAppBaseDialogFragment {
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.sr_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle mTemplateTitle;
    @BindView(R.id.ll_my_call_default)
    LinearLayout llMyCallDefault;
    MyCallAdapter mAdapter;
    private Unbinder unbinder;
    private ArrayList<YueJianAppACChatInfoModel> mChatInfoModels;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_my_call, null);
        unbinder = ButterKnife.bind(this, view);
        mChatInfoModels = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView.setDividerHeight(0);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.actionbarbackground));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
        initData();
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
        mTemplateTitle.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        YueJianAppApiProtoHelper.sendACGetChatLogReq(
                getActivity(),
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                new YueJianAppApiProtoHelper.ACGetChatLogReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACChatInfoModel> logs) {
                        mChatInfoModels = logs;
                        fillUI();
                    }
                });
    }

    private void fillUI() {
        if (mChatInfoModels.size() <= 0) {
            YueJianAppTLog.info("没有通话记录！！");
            YueJianAppUiUtils.setVisibility(llMyCallDefault, View.VISIBLE);
            return;
        }

        YueJianAppUiUtils.setVisibility(llMyCallDefault, View.GONE);
        mAdapter = new MyCallAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                YueJianAppDialogHelper.showVchatOtherInfoDialogFragment(
                        getActivity().getSupportFragmentManager(),
                        mChatInfoModels.get(position).getTarget().getUid()
                );
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class MyCallAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mChatInfoModels.size();
        }

        @Override
        public Object getItem(int i) {
            return mChatInfoModels.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.yue_jian_app_item_my_call, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            YueJianAppACChatInfoModel model = mChatInfoModels.get(position);
            viewHolder.avUserHead.setAvatarUrl(model.getTarget().getAvatarUrl());
            viewHolder.tvName.setText(model.getTarget().getName());
            String chatTime = YueJianAppTimeFormater.timeToStrLong((long) model.getChatTime() * 1000);
            viewHolder.tvDate.setText(chatTime.substring(0, 6));
            viewHolder.tvTime.setText(chatTime.substring(6));
            switch (model.getChatState()) {
                case YueJianAppApiProtoHelper.CALL_STATUS_NONE: // 未接听
                case YueJianAppApiProtoHelper.CALL_STATUS_CALLING: // 通话成功
                    viewHolder.tvStatus.setText("无人接听");
                    viewHolder.tvStatus.setTextColor(getResources().getColor(R.color.vchat_ea4579));
                    break;
                case YueJianAppApiProtoHelper.CALL_STATUS_SHUTDOWN: // 系统关闭
                case YueJianAppApiProtoHelper.CALL_STATUS_END: // 通话结束
                    viewHolder.tvStatus.setText(
                            String.format(
                                    "通话时长：%s（支出%s钻石）",
                                    YueJianAppTimeFormater.secondToStrLong(model.getChatSeconds()),
                                    model.getExpense()
                            )
                    );
                    viewHolder.tvStatus.setTextColor(getResources().getColor(R.color.vchat_919191));
                    break;
                case YueJianAppApiProtoHelper.CALL_STATUS_CANCEL: //未接来电
                    viewHolder.tvStatus.setText("未接来电");
                    viewHolder.tvStatus.setTextColor(getResources().getColor(R.color.vchat_ea4579));
                    break;
                case YueJianAppApiProtoHelper.CALL_STATUS_DECLINE: // 无人接听
                    viewHolder.tvStatus.setText("无人接听");
                    viewHolder.tvStatus.setTextColor(getResources().getColor(R.color.vchat_ea4579));
                    break;
            }
            return convertView;
        }
    }

    class ViewHolder {
        @BindView(R.id.av_user_head)
        YueJianAppAvatarView avUserHead; // 主播头像
        @BindView(R.id.tv_name)
        TextView tvName; // 主播昵称
        @BindView(R.id.tv_status)
        TextView tvStatus; // 通话状态
        @BindView(R.id.tv_date)
        TextView tvDate; // 童话日期
        @BindView(R.id.tv_time)
        TextView tvTime; // 通话时间

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
