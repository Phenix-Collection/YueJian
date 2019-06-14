package com.mingquan.yuejian.vchat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppTagRecordAdapter;
import com.mingquan.yuejian.base.YueJianAppBaseFragment;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserTagMetaDataModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserTaggingModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppTimeFormater;
import com.mingquan.yuejian.xrecyclerview.YueJianAppRecycleViewDivider;
import com.lzy.widget.HeaderScrollHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 用户详情页里的资料页面
 */
public class YueJianAppVChatUserDataFragment extends YueJianAppBaseFragment implements HeaderScrollHelper.ScrollableContainer {

    @BindView(R.id.iv_label_more)
    ImageView ivLabelMore;
    @BindView(R.id.last_login)
    TextView tvLastLogin; // 最后一次登录
    @BindView(R.id.receive_rate)
    TextView receiveRate; // 接听率
    @BindView(R.id.height)
    TextView height; // 身高
    @BindView(R.id.weight)
    TextView weight; // 体重
    @BindView(R.id.constellation)
    TextView constellation; // 星座
    @BindView(R.id.city)
    TextView city; // 城市
    @BindView(R.id.love_num)
    TextView loveNum; // 爱心人量
    @BindView(R.id.not_love_num)
    TextView notLoveNum; // 不爱人数
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.rv_tags)
    RecyclerView rvTags; // 用户评价列表
    @BindView(R.id.ll_signature)
    LinearLayout llSignature;
    @BindView(R.id.rl_receive_rate)
    RelativeLayout rlReceiveRate;
    @BindView(R.id.tv_signature)
    TextView tvSignature; // 个性签名
    @BindView(R.id.user_label_container)
    YueJianAppLabelGroup userLabelContainer; // 用户印象label
    @BindView(R.id.self_label_container)
    YueJianAppLabelGroup selfLabelContainer; // 自己形象label
    Unbinder unbinder;

    private YueJianAppACUserPublicInfoModel publicInfoModel;
    private YueJianAppTagRecordAdapter tagRecordAdapter;

    private int mLastQueryId = 0; // 用户评价记录的最后一条id
    private ArrayList<YueJianAppACUserTagMetaDataModel> userTagMetaDataModels = new ArrayList<>(); // 用户标签
    private ArrayList<YueJianAppACUserTagMetaDataModel> selfTagMetaDataModels = new ArrayList<>();
    private List<YueJianAppACUserTaggingModel> userTaggingModels = new ArrayList<>(); // 用户评价集合

    public static YueJianAppVChatUserDataFragment newInstance() {
        YueJianAppVChatUserDataFragment fragment = new YueJianAppVChatUserDataFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yue_jian_app_fragment_vchat_user_data, container, false);
        Context mContext = getActivity();
        unbinder = ButterKnife.bind(this, view);
        ivLabelMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (publicInfoModel == null) {
                    return;
                }
                YueJianAppDialogHelper.showUserTagDialogFragment(getActivity().getSupportFragmentManager(), publicInfoModel.getUid());
            }
        });

        tagRecordAdapter = new YueJianAppTagRecordAdapter(mContext);
        rvTags.addItemDecoration(new YueJianAppRecycleViewDivider(mContext, RecyclerView.VERTICAL, (int) YueJianAppTDevice.dpToPixel(1), R.color.white));
        rvTags.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvTags.setAdapter(tagRecordAdapter);
        boolean canVideoChat = YueJianAppAppContext.getInstance().getPrivateInfoModel().getCanVideoChat();
        llSignature.setVisibility(canVideoChat ? View.VISIBLE : View.GONE);
        rlReceiveRate.setVisibility(canVideoChat ? View.VISIBLE : View.GONE);
        rvTags.setVisibility(canVideoChat ? View.VISIBLE : View.GONE);
        return view;
    }


    public void setData(YueJianAppACUserPublicInfoModel userPublicInfoModel) {
        this.publicInfoModel = userPublicInfoModel;
        if (publicInfoModel == null) {
            YueJianAppTLog.error("user data fragment info model == null !!");
            return;
        }
        tvSignature.setText(publicInfoModel.getSignature());
        tvLastLogin.setText(YueJianAppTimeFormater.getInterval2(String.valueOf(publicInfoModel.getLastLoginTime())));
        receiveRate.setText(String.format("%s %%", String.valueOf(publicInfoModel.getReceivingRate())));
        height.setText(String.format("%s cm", String.valueOf(publicInfoModel.getHeight())));
        weight.setText(String.format("%s kg", String.valueOf(publicInfoModel.getWeight())));
        constellation.setText(publicInfoModel.getSign());
        city.setText(publicInfoModel.getCity());

        if (publicInfoModel.getUserTags() > 0) {
            getUserTags(publicInfoModel.getUserTags());
            userLabelContainer.setLabels(userTagMetaDataModels);
        }
        if (publicInfoModel.getSelfTags() > 0) {
            getSelfTags(publicInfoModel.getSelfTags());
            selfLabelContainer.setLabels(selfTagMetaDataModels);
        }

        getUserTagsRecord();
    }

    /**
     * 获取用户评价标签
     *
     * @param tags
     * @return
     */
    private void getUserTags(int tags) {
        if ((tags & (tags - 1)) != 0) {
            for (int i = 0; i < 100; i++) {
                if (Math.pow(2, i) > tags) {
                    tags -= (int) Math.pow(2, i - 1);
                    getUserTags(tags);
                    userTagMetaDataModels.add(YueJianAppAppContext.getInstance().getUserTagMetaByTagId((int) Math.pow(2, i - 1)));
                    break;
                }
            }
        } else {
            userTagMetaDataModels.add(YueJianAppAppContext.getInstance().getUserTagMetaByTagId(tags));
        }
    }

    /**
     * 获取自己形象标签
     *
     * @param tags
     * @return
     */
    private void getSelfTags(int tags) {
        if ((tags & (tags - 1)) != 0) {
            for (int i = 0; i < 100; i++) {
                if (Math.pow(2, i) > tags) {
                    tags -= (int) Math.pow(2, i - 1);
                    getSelfTags(tags);
                    selfTagMetaDataModels.add(YueJianAppAppContext.getInstance().getUserTagMetaByTagId((int) Math.pow(2, i - 1)));
                    break;
                }
            }
        } else {
            selfTagMetaDataModels.add(YueJianAppAppContext.getInstance().getUserTagMetaByTagId(tags));
        }
    }

    /**
     * 获取用户评价记录
     */
    private void getUserTagsRecord() {
        YueJianAppApiProtoHelper.sendACGetUserTagsDetailReq(
                getActivity(),
                publicInfoModel.getUid(),
                20,
                mLastQueryId,
                new YueJianAppApiProtoHelper.ACGetUserTagsDetailReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACUserTaggingModel> tags, int lastQueryId) {
                        mLastQueryId = lastQueryId;
                        userTaggingModels.addAll(tags);
                        tagRecordAdapter.setList(userTaggingModels);
                    }
                }
        );
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
