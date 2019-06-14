package com.mingquan.yuejian.vchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by sun on 2018/9/4.
 * <p>
 * 我的钱包列表页面
 */

public class YueJianAppMyPackageDialogFragment extends YueJianAppBaseDialogFragment implements View.OnClickListener {

    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout;
    @BindView(R.id.tv_diamond)
    TextView tvDiamond; // 钻石数
    @BindView(R.id.tv_ticket)
    TextView tvTicket; // 趣票数
    @BindView(R.id.ll_recharge)
    LinearLayout llRecharge; //充值
    @BindView(R.id.ll_in_out_detail)
    LinearLayout llInOutDetail; // 收支明细
    private int mDiamond;
    private int mTicket;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_my_package, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDiamond = getArguments().getInt("DIAMOND");
        mTicket = getArguments().getInt("TICKET");
        initData();
    }

    public void initData() {
        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvDiamond.setText(String.valueOf(mDiamond));
        tvTicket.setText(String.valueOf(mTicket));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_recharge, R.id.ll_in_out_detail})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_recharge:
                YueJianAppDialogHelper.showRechargeDialogFragment(getActivity().getSupportFragmentManager());
                break;
            case R.id.ll_in_out_detail:
                YueJianAppDialogHelper.showMyDiamondListDialogFragment(getActivity().getSupportFragmentManager());
                break;
        }
    }
}

