package com.mingquan.yuejian.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.YueJianAppAppManager;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.WxPay.YueJianAppWChatPayDiamondForFragment;
import com.mingquan.yuejian.alipay.YueJianAppAliPayDiamondForFragment;
import com.mingquan.yuejian.base.YueJianAppBaseApplication;
import com.mingquan.yuejian.bean.YueJianAppUserBean;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACPayItemModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPrivateInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;
import com.mingquan.yuejian.utils.YueJianAppUtils;
import com.mingquan.yuejian.vchat.YueJianAppMessageEvent;
import com.mingquan.yuejian.vchat.YueJianAppXTemplateTitle;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class YueJianAppDiamondDialogFragment extends YueJianAppBaseDialogFragment {
    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle mTemplateTitle;
    @BindView(R.id.tv_contact_customer_service)
    TextView mContactCustomerService; // 联系客服
    @BindView(R.id.lv_pay)
    ListView mPayListView;
    @BindView(R.id.iv_recharge)
    ImageView ivRecharge;
    private ImageView mIvAliSelected; // 支付宝选中框
    private ImageView mIvWXSelected; // 微信选中框
    private TextView mPayName; // 支付方式
    private TextView mDiamond; // 钻石数
    private List<YueJianAppACPayItemModel> mRechargeList = new ArrayList<>();
    private int mSelectedIndex = 0;
    private int PAYMODE = YueJianAppApiProtoHelper.PAY_TYPE_ALI;
    private YueJianAppAliPayDiamondForFragment mAliPayUtils;
    private YueJianAppWChatPayDiamondForFragment mWChatPay;
    protected BroadcastReceiver logoutReceiver = null;
    private Unbinder unbinder;
    private Toast mToast;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_my_diamonds, container);
        unbinder = ButterKnife.bind(this, view);

        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        initView();
        initData();
        return view;
    }

    public void initView() {
        if (mTemplateTitle != null) {
            mTemplateTitle.setLeftBtnListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        View mHeadView = getActivity().getLayoutInflater().inflate(R.layout.yue_jian_app_view_diamonds_head, null);
        View mFootView = getActivity().getLayoutInflater().inflate(R.layout.yue_jian_app_view_diamonds_foot, null);
        mDiamond = (TextView) mHeadView.findViewById(R.id.tv_diamond);
        mPayName = (TextView) mHeadView.findViewById(R.id.tv_pay_name);
        RelativeLayout mAliPay = (RelativeLayout) mHeadView.findViewById(R.id.rl_ali_pay);
        RelativeLayout mWxPay = (RelativeLayout) mHeadView.findViewById(R.id.rl_wx_pay);
        mIvAliSelected = (ImageView) mHeadView.findViewById(R.id.iv_ali_selected);
        mIvWXSelected = (ImageView) mHeadView.findViewById(R.id.iv_wx_selected);
        mPayListView.addHeaderView(mHeadView);
        mPayListView.addFooterView(mFootView);
        selectPayMode();
        //微信支付
        mWxPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PAYMODE = YueJianAppApiProtoHelper.PAY_TYPE_WECHAT;
                selectPayMode();
            }
        });
        //支付宝
        mAliPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PAYMODE = YueJianAppApiProtoHelper.PAY_TYPE_ALI;
                selectPayMode();
            }
        });
        mPayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (YueJianAppUtils.isFastClick()) {
                    return;
                }
                YueJianAppTLog.error("item click %s", position);
                if (position == 0 || position == mRechargeList.size() + 1 || mSelectedIndex == position - 1) {
                    YueJianAppTLog.error("pos = 0 || size || selectIndex == pos - 1");
                    return;
                }
                if (position - mPayListView.getHeaderViewsCount() >= 0 && position - mPayListView.getHeaderViewsCount() < mRechargeList.size()) {
                    mSelectedIndex = position - mPayListView.getHeaderViewsCount();
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        ivRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRechargeList.size() <= 0) {
                    return;
                }
                YueJianAppACPayItemModel currItemModel = mRechargeList.get(mSelectedIndex);
                if (PAYMODE == YueJianAppApiProtoHelper.PAY_TYPE_ALI) {
                    mAliPayUtils.initPay(
                            currItemModel.getPrice(),
                            String.valueOf(currItemModel.getValue()),
                            currItemModel.getItemId());
                } else {
                    mWChatPay.initPay(
                            String.valueOf(currItemModel.getPrice()),
                            String.valueOf(currItemModel.getValue()),
                            currItemModel.getItemId());
                }
            }
        });

        mContactCustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YueJianAppApiProtoHelper.sendACGetUserPublicInfoReq(
                        getActivity(),
                        "120001",
                        new YueJianAppApiProtoHelper.ACGetUserPublicInfoReqCallback() {
                            @Override
                            public void onError(int errCode, String errMessage) {
                                YueJianAppTLog.error(errMessage);
                            }

                            @Override
                            public void onResponse(YueJianAppACUserPublicInfoModel user) {
                                YueJianAppUIHelper.showPrivateChatdetailFragment(getActivity(), user);
                            }
                        });
            }
        });
    }

    private void selectPayMode() {
        if (PAYMODE == YueJianAppApiProtoHelper.PAY_TYPE_ALI) {
            mPayName.setText(String.format("%s%s", getString(R.string.paymode), getString(R.string.alipay)));
            YueJianAppUiUtils.setVisibility(mIvAliSelected, View.VISIBLE);
            YueJianAppUiUtils.setVisibility(mIvWXSelected, View.GONE);
        } else {
            mPayName.setText(String.format("%s%s", getString(R.string.paymode), getString(R.string.wxpay)));
            YueJianAppUiUtils.setVisibility(mIvAliSelected, View.GONE);
            YueJianAppUiUtils.setVisibility(mIvWXSelected, View.VISIBLE);
        }
    }

    public void initData() {
        requestData();
        registerReceiver();
        mAliPayUtils = new YueJianAppAliPayDiamondForFragment(this);
        mWChatPay = new YueJianAppWChatPayDiamondForFragment(this);
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(YueJianAppAppConfig.ACTION_LOGOUT);
        intentFilter.addAction(YueJianAppAppConfig.ACTION_RECHARGE_DIAMOND);
        logoutReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(YueJianAppAppConfig.ACTION_LOGOUT)) {
                    getActivity().runOnUiThread(new Runnable() {
                        @SuppressLint("NewApi")
                        @Override
                        public void run() {
                            mToast.setText("您的账号已在其他设备登录，请重新登录!");
                            mToast.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    YueJianAppAppContext.getInstance().Logout();
                                    YueJianAppUIHelper.showLoginSelectActivity(getActivity());
                                    YueJianAppAppManager.getAppManager().finishAllActivity();
                                    dismiss();
                                }
                            }, 3000);
                        }
                    });
                } else if (action.equals(YueJianAppAppConfig.ACTION_RECHARGE_DIAMOND)) {
                    requestData();
                }
            }
        };
        getActivity().registerReceiver(logoutReceiver, intentFilter);
    }

    /**
     * 请求充值列表
     * 请求用户砖石数量
     */
    private void requestData() {
        requestDiamondNum();
        requestRechargeList();
    }

    /**
     * 请求用户钻石量
     */
    private void requestDiamondNum() {
        YueJianAppApiProtoHelper.sendACGetUserPrivateInfoReq(
                getActivity(),
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                new YueJianAppApiProtoHelper.ACGetUserPrivateInfoReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse(YueJianAppACUserPrivateInfoModel user) {
                        if (mDiamond != null) {
                            mDiamond.setText(String.valueOf(user.getDiamond()));
                        }
                        YueJianAppACUserPrivateInfoModel privateInfoModel = YueJianAppAppContext.getInstance().getPrivateInfoModel();
                        privateInfoModel.setDiamond(user.getDiamond());
                        YueJianAppAppContext.getInstance().setPrivateInfo(privateInfoModel);
                    }
                });
    }

    /**
     * 请求充值列表数据
     */
    private void requestRechargeList() {
        YueJianAppApiProtoHelper.sendACDiamondItemListReq(
                getActivity(),
                PAYMODE,
                YueJianAppAppContext.getInstance().getLoginUid(),
                new YueJianAppApiProtoHelper.ACDiamondItemListReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        mToast.setText(errMessage);
                        mToast.show();
                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACPayItemModel> items) {
                        mRechargeList.clear();
                        mRechargeList.addAll(items);
                        fillAdapter();
                    }
                });
    }

    MyAdapter mAdapter = new MyAdapter();

    private void fillAdapter() {
        mAdapter.setData(mRechargeList);
        mPayListView.setAdapter(mAdapter);
    }

    //充值结果
    public void rechargeResult(String rechargeMoney) {
        if (mDiamond != null) {
            mDiamond.setText(String.format("%d", Integer.parseInt(mDiamond.getText().toString()) + Integer.parseInt(rechargeMoney)));
        }
        Map<String, String> map = new HashMap<>();
        YueJianAppUserBean user = YueJianAppAppContext.getInstance().getLoginUser();
        map.put("fromName", user.getUser_nicename());
        map.put("rechargeMoney", rechargeMoney);
        map.put("rechargeUser", user.getId() + "");
        map.put("reportName", user.getUser_nicename());
        map.put("type", "alipay");
        MobclickAgent.onEvent(getActivity(), YueJianAppAppConfig.EVENT_RECHARGE, map);
        requestData();
    }

    private class MyAdapter extends BaseAdapter {
        private List<YueJianAppACPayItemModel> mChargeList = new ArrayList<>();

        public void setData(List<YueJianAppACPayItemModel> data) {
            this.mChargeList = data;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mChargeList.size() <= 0 ? 0 : mChargeList.size();
        }

        @Override
        public Object getItem(int position) {
            return mChargeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (mChargeList.size() <= 0) {
                return convertView;
            }
            YueJianAppACPayItemModel payItemModel = mChargeList.get(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.yue_jian_app_item_select_num, null);
                holder = new ViewHolder();
                holder.rlItem = (RelativeLayout) convertView.findViewById(R.id.rl_item);
                holder.tvDiamondNum = (TextView) convertView.findViewById(R.id.tv_item_diamond_num);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                holder.ivRibbon = (ImageView) convertView.findViewById(R.id.iv_ribbon);
                holder.tvExplain = (TextView) convertView.findViewById(R.id.tv_explain);
                holder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_item_selected);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvDiamondNum.setText(String.valueOf(payItemModel.getValue()));
            holder.tvExplain.setText(payItemModel.getDesp());
            holder.tvPrice.setText("￥" + payItemModel.getPrice());
            if (!YueJianAppStringUtil.isEmpty(payItemModel.getImageUrl())) {
                YueJianAppBaseApplication.getImageLoaderUtil().loadImageHighDefinite(
                        getActivity(),
                        payItemModel.getImageUrl(),
                        holder.ivRibbon);
            }
            if (position == mSelectedIndex) {
                holder.ivSelected.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.rlItem.setElevation(0);
                }
            } else {
                holder.ivSelected.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.rlItem.setElevation(YueJianAppTDevice.dpToPixel(2));
                }
            }
            return convertView;
        }

        private class ViewHolder {
            RelativeLayout rlItem;
            TextView tvDiamondNum;
            TextView tvExplain;
            TextView tvPrice;
            ImageView ivSelected; // 选中框
            ImageView ivRibbon; // 丝带
        }
    }

    @Override
    public void onDestroy() {
        if (null != logoutReceiver) {
            getActivity().unregisterReceiver(logoutReceiver);
        }
        unbinder.unbind();
        EventBus.getDefault().post(new YueJianAppMessageEvent("REFRESH"));
        super.onDestroy();
    }
}
