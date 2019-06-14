package com.mingquan.yuejian.vchat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACConsumptionInfoModel;
import com.mingquan.yuejian.utils.YueJianAppTimeFormater;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sun on 2018/9/4.
 * <p>
 * 我的收支明细页面
 */

public class YueJianAppMyInOutListDialogFragment extends YueJianAppBaseDialogFragment {

    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout;
    @BindView(R.id.list_view)
    ListView listView;
    Unbinder unbinder;
    @BindView(R.id.sr_refresh)
    SwipeRefreshLayout srRefresh;
    private DiamondAdapter mAdapter;
    private Context mContext;
    private int mLastQueryId = 0;
    private List<YueJianAppACConsumptionInfoModel> mConsumptions;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_my_diamond_list, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        mConsumptions = new ArrayList<>();
        mAdapter = new DiamondAdapter();
        listView.setAdapter(mAdapter);
        initView();
        requestData();
    }

    public void initView() {
        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        srRefresh.setColorSchemeColors(getResources().getColor(R.color.home_pink));
        srRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLastQueryId = 0;
                requestData();
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == mConsumptions.size() - 1) {
                        requestData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 请求收支明细数据
     */
    private void requestData() {
        YueJianAppApiProtoHelper.sendACGetConsumptionInfoReq(
                (Activity) mContext,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                10,
                mLastQueryId,
                new YueJianAppApiProtoHelper.ACGetConsumptionInfoReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        if (srRefresh != null && srRefresh.isRefreshing()) {
                            srRefresh.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACConsumptionInfoModel> consumptions, int lastQueryId) {

                        if (srRefresh != null && srRefresh.isRefreshing()) {
                            srRefresh.setRefreshing(false);
                            mConsumptions.clear();
                        }
                        mLastQueryId = lastQueryId;
                        mConsumptions.addAll(consumptions);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class DiamondAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mConsumptions.size();
        }

        @Override
        public Object getItem(int i) {
            return mConsumptions.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.yue_jian_app_item_my_diamond_list, null);
                viewHolder = new ViewHolder();
                viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                viewHolder.tvPayNum = (TextView) convertView.findViewById(R.id.tv_pay_num);
                viewHolder.tvUnit = (TextView) convertView.findViewById(R.id.tv_unit);
                viewHolder.tvPayName = (TextView) convertView.findViewById(R.id.tv_pay_name);
                viewHolder.tvBroadcastName = (TextView) convertView.findViewById(R.id.tv_broadcast_name);
                viewHolder.broadcastAvatar = (YueJianAppAvatarView) convertView.findViewById(R.id.broadcast_avatar);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            YueJianAppACConsumptionInfoModel model = mConsumptions.get(position);
            if (model.getAmount() > 0) {
                viewHolder.tvPayNum.setText(String.format("+%d", model.getAmount()));
                viewHolder.tvPayNum.setTextColor(Color.parseColor("#ec6997"));
            } else {
                viewHolder.tvPayNum.setText(String.valueOf(model.getAmount()));
                viewHolder.tvPayNum.setTextColor(Color.parseColor("#4e80f7"));
            }
            switch (model.getConsumptionType()) {
                case YueJianAppApiProtoHelper.DIAMOND_INPUT_CHARGE:
                case YueJianAppApiProtoHelper.DIAMOND_INPUT_ACTIVITY_AGENT_INVITE_USER:
                case YueJianAppApiProtoHelper.DIAMOND_INPUT_ACTIVITY_BE_INVITED_BY_AGENT:
                case YueJianAppApiProtoHelper.DIAMOND_INPUT_TESTING:
                case YueJianAppApiProtoHelper.DIAMOND_OUTPUT_BUY_VIDEO:
                case YueJianAppApiProtoHelper.DIAMOND_OUTPUT_VIDEO_CHAT:
                case YueJianAppApiProtoHelper.DIAMOND_OUTPUT_SEND_GIFT:
                case YueJianAppApiProtoHelper.DIAMOND_OUTPUT_SEND_MESSAGE:
                case YueJianAppApiProtoHelper.DIAMOND_OUTPUT_TESTING:
                case YueJianAppApiProtoHelper.DIAMOND_INPUT_ACTIVITY_BE_INVITED_BY_PARTNER:
                case YueJianAppApiProtoHelper.DIAMOND_INPUT_ACTIVITY_FREE_TRIAL:
                case YueJianAppApiProtoHelper.DIAMOND_INPUT_ADMIN:
                    viewHolder.tvUnit.setText("钻石");
                    break;
                case YueJianAppApiProtoHelper.TICKET_INPUT_SELL_VIDEO:
                case YueJianAppApiProtoHelper.TICKET_INPUT_VIDEO_CHAT:
                case YueJianAppApiProtoHelper.TICKET_INPUT_RECEIVE_GIFT:
                case YueJianAppApiProtoHelper.TICKET_INPUT_RECEIVE_MESSAGE:
                case YueJianAppApiProtoHelper.TICKET_INPUT_TESTING:
                case YueJianAppApiProtoHelper.TICKET_OUTPUT_TESTING:
                    viewHolder.tvUnit.setText("趣票");
                    break;
            }
            viewHolder.tvPayName.setText(model.getDesp());
            viewHolder.tvTime.setText(YueJianAppTimeFormater.timeToStrLong((long) model.getConsumptionTime() * 1000));
            viewHolder.tvDate.setText(YueJianAppTimeFormater.timeToStrLong2((long) model.getConsumptionTime() * 1000));
            viewHolder.tvBroadcastName.setText(model.getTarget().getName());
            viewHolder.broadcastAvatar.setAvatarUrl(model.getTarget().getAvatarUrl());
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvTime; // 时间
        TextView tvDate; // 日期
        TextView tvPayNum; // 收支钻石数
        TextView tvUnit; // 收支单位（钻石或趣票）
        TextView tvPayName; // 收支类型
        TextView tvBroadcastName; // 主播昵称
        YueJianAppAvatarView broadcastAvatar; // 主播头像
    }
}

