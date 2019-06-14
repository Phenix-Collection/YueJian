package com.mingquan.yuejian.vchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sun on 2018/10/26.
 * 设置我的视频聊天价格页面
 */

public class YueJianAppMyCallPriceDialogFragment extends YueJianAppBaseDialogFragment {

    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout;
    Unbinder unbinder;
    @BindView(R.id.wheel_picker)
    WheelPicker wheelPicker;
    @BindView(R.id.btn_conform)
    Button btnConform;

    private ArrayList<Integer> priceList;
    private int price;
    private Toast mToast;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_my_call_price, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        price = getArguments().getInt("PRICE");
        try {
            if (YueJianAppStringUtil.isEmpty(YueJianAppAppContext.getInstance().mBroadcastPriceJson)) {
                YueJianAppTLog.error("broadcast price json is empty");
                return;
            }
            priceList = new ArrayList<>();
            JSONArray priceArray = new JSONArray(YueJianAppAppContext.getInstance().mBroadcastPriceJson);
            for (int i = 0; i < priceArray.length(); i++) {
                priceList.add(priceArray.getInt(i));
            }
            initData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initData() {
        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        wheelPicker.setData(priceList);
        wheelPicker.setSelectedItemPosition(price <= priceList.get(0) ? 0 : priceList.indexOf(price));
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                price = (int) data;
            }
        });

        btnConform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YueJianAppApiProtoHelper.sendACSetBroadcastPriceReq(
                        getActivity(),
                        YueJianAppAppContext.getInstance().getLoginUid(),
                        YueJianAppAppContext.getInstance().getToken(),
                        price,
                        new YueJianAppApiProtoHelper.ACSetBroadcastPriceReqCallback() {
                            @Override
                            public void onError(int errCode, String errMessage) {
                                mToast.setText(errMessage);
                                mToast.show();
                            }

                            @Override
                            public void onResponse() {
                                mToast.setText("视频聊天价格设置成功!");
                                mToast.show();
                                dismiss();
                            }
                        });
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

