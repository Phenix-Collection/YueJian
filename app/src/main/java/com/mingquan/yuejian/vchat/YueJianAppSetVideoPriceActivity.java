package com.mingquan.yuejian.vchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YueJianAppSetVideoPriceActivity extends YueJianAppFullScreenModeActivity {

    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    private int videoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_set_video_price);
        ButterKnife.bind(this);
        videoId = getIntent().getIntExtra("VIDEO_ID", -1);
        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String price = etPrice.getText().toString().trim();
                if (YueJianAppStringUtil.isEmpty(price)) {
                    Toast.makeText(YueJianAppSetVideoPriceActivity.this, "请输入视频价格！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.parseInt(price) > 30) {
                    Toast.makeText(YueJianAppSetVideoPriceActivity.this, "视频价格不应高于30钻！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (videoId == -1) { // 上传短视频时设定价格
                    Intent intent = new Intent();
                    intent.putExtra("SET_ADD_VIDEO_PRICE", Integer.parseInt(price));
                    YueJianAppSetVideoPriceActivity.this.setResult(YueJianAppUploadVideoActivity.SET_ADD_VIDEO_PRICE, intent);
                    finish();
                } else {
                    YueJianAppApiProtoHelper.sendACSetVideoPriceReq(
                            YueJianAppSetVideoPriceActivity.this,
                            YueJianAppAppContext.getInstance().getLoginUid(),
                            YueJianAppAppContext.getInstance().getToken(),
                            videoId,
                            Integer.parseInt(price),
                            new YueJianAppApiProtoHelper.ACSetVideoPriceReqCallback() {
                                @Override
                                public void onError(int errCode, String errMessage) {
                                    YueJianAppTLog.error("set video price :%s", errMessage);
                                }

                                @Override
                                public void onResponse() {
                                    YueJianAppSetVideoPriceActivity.this.setResult(YueJianAppUploadVideoActivity.SET_VIDEO_PRICE);
                                    finish();
                                }
                            });
                }
            }
        });
    }
}
