package com.mingquan.yuejian.vchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sun on 2018/9/5.
 * <p>
 * 分成计划页面
 */

public class YueJianAppVChatCommissionDialogFragment extends YueJianAppBaseDialogFragment {
    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout;
    @BindView(R.id.btn_generalize)
    Button btnGeneralize; // 推广
    @BindView(R.id.tv_generalize_detail)
    TextView tvGeneralizeDetail; // 推广明细
    @BindView(R.id.tv_withdraw)
    TextView tvWithdraw; // 提现
    @BindView(R.id.tv_withdraw_num)
    TextView tvWithdrawNum; //可提现金额
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_commission, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    public void initData() {
        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
