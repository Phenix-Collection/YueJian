package com.mingquan.yuejian.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPrivateInfoModel;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.vchat.YueJianAppXTemplateTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/10/24
 */
public class YueJianAppAuthActivity extends YueJianAppFullScreenModeActivity {

    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout;
    @BindView(R.id.btn_to_auth)
    Button btnToAuth;
    @BindView(R.id.tv_description)
    TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_auth);
        ButterKnife.bind(this);
        YueJianAppACUserPrivateInfoModel privateInfoModel = YueJianAppAppContext.getInstance().getPrivateInfoModel();
        if (privateInfoModel != null && !YueJianAppStringUtil.isEmpty(privateInfoModel.getAuthComment())) {
            tvDescription.setText(privateInfoModel.getAuthComment());
        }
        initEvent();
    }

    public void initEvent() {
        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnToAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YueJianAppUIHelper.showEditAuthinfoActivity(YueJianAppAuthActivity.this);
            }
        });
    }
}
