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
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;
import com.mingquan.yuejian.widget.YueJianAppStatusTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sun on 2018/9/5.
 * <p>
 * 预约页面
 */

public class YueJianAppVChatAppointmentDialogFragment extends YueJianAppBaseDialogFragment {
    AppointmentAdapter mAdapter;
    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout;
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.sr_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.ll_default)
    LinearLayout llDefault;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_appointment, null);
        unbinder = ButterKnife.bind(this, view);
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

        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        fillUI();
    }

    private void fillUI() {
        llDefault.setVisibility(View.GONE);
        mAdapter = new AppointmentAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class AppointmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
//            return mDiamondList.size();
        }

        @Override
        public Object getItem(int i) {
//            return mDiamondList.get(i);
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.yue_jian_app_item_appointment, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.status.setStatus(1, "在线");
            return convertView;
        }
    }

    class ViewHolder {
        @BindView(R.id.av_user_head)
        YueJianAppAvatarView avUserHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.status)
        YueJianAppStatusTextView status;
        @BindView(R.id.tv_love_num)
        TextView tvLoveNum;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_appointment_status)
        TextView tvAppointmentStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
