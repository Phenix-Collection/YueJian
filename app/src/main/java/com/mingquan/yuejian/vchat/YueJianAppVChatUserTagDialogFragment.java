package com.mingquan.yuejian.vchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.fragment.YueJianAppBaseDialogFragment;
import com.mingquan.yuejian.model.YueJianAppUserTagAndCountModel;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserTagModel;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/9/29
 * <p>
 * 用户印象标签
 */

public class YueJianAppVChatUserTagDialogFragment extends YueJianAppBaseDialogFragment {

    private YueJianAppXTemplateTitle titleLayout;
    private YueJianAppLabelGroup labelContainer;
    private TextView tvEmptyTips;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_vchat_user_tag, null);
        titleLayout = (YueJianAppXTemplateTitle) view.findViewById(R.id.title_layout);
        labelContainer = (YueJianAppLabelGroup) view.findViewById(R.id.label_container);
        tvEmptyTips = (TextView) view.findViewById(R.id.tv_empty_tips);
        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        String target_uid = getArguments().getString("TARGET_UID");
        YueJianAppApiProtoHelper.sendACGetUserTagsBriefReq(getActivity(), target_uid, new YueJianAppApiProtoHelper.ACGetUserTagsBriefReqCallback() {
            @Override
            public void onError(int errCode, String errMessage) {

            }

            @Override
            public void onResponse(ArrayList<YueJianAppACUserTagModel> tags) {
                if (tags.size() == 0) {
                    tvEmptyTips.setText("暂时还没有用户标签");
                    return;
                }

                ArrayList<YueJianAppUserTagAndCountModel> userTagAndCountModels = new ArrayList<>();
                for (YueJianAppACUserTagModel model : tags) {
                    YueJianAppUserTagAndCountModel userTagAndCountModel = new YueJianAppUserTagAndCountModel();
                    userTagAndCountModel.setCount(model.getCount());
                    userTagAndCountModel.setMetaDataModel(YueJianAppAppContext.getInstance().getUserTagMetaByTagId(model.getTagId()));
                    userTagAndCountModels.add(userTagAndCountModel);
                }

                labelContainer.setLabels(userTagAndCountModels);
            }
        });
    }
}
