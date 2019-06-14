package com.mingquan.yuejian.huawei.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.huawei.hms.api.HuaweiApiAvailability;
import com.mingquan.yuejian.huawei.YueJianAppHMSAgent;

/**
 * 代理Activity，用于弹出解决问题的引导
 */
public class YueJianAppHMSAgentActivity extends YueJianAppBaseAgentActivity {

    /**
     * 参数标签，用于取得要解决的错误码
     */
    public static final String CONN_ERR_CODE_TAG = "HMSConnectionErrorCode";

    /**
     * 解决错误后结果的标签，用来取得解决的结果
     */
    public static final String EXTRA_RESULT = "intent.extra.RESULT";

    /**
     * 解决错误的requestCode
     */
    private static final int REQUEST_HMS_RESOLVE_ERROR = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YueJianAppApiClientMgr.INST.onActivityLunched();

        Intent intent = getIntent();
        if (intent != null) {
            int rstCode =  intent.getIntExtra(CONN_ERR_CODE_TAG, 0);
            YueJianAppHMSAgentLog.d("dispose code:" + rstCode);
            HuaweiApiAvailability.getInstance().resolveError(this, rstCode, REQUEST_HMS_RESOLVE_ERROR);
        } else {
            YueJianAppHMSAgentLog.e("intent is null");
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_HMS_RESOLVE_ERROR) {
            if(resultCode == Activity.RESULT_OK) {

                int result = data.getIntExtra(EXTRA_RESULT, -1);
                YueJianAppHMSAgentLog.d("dispose result:" + result);
                YueJianAppApiClientMgr.INST.onResolveErrorRst(result);
            } else {
                YueJianAppHMSAgentLog.e("dispose error:" + resultCode);
                YueJianAppApiClientMgr.INST.onResolveErrorRst(YueJianAppHMSAgent.AgentResultCode.ON_ACTIVITY_RESULT_ERROR);
            }
            finish();
        }
    }
}
