package com.mingquan.yuejian.auth;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.interf.YueJianAppIBottomDialog;
import com.mingquan.yuejian.layoutmanager.YueJianAppFullyGridLayoutManager;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPrivateInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserTagMetaDataModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.ui.dialog.YueJianAppEditableActionSheetDialog;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.vchat.YueJianAppLabelGroup;
import com.mingquan.yuejian.vchat.YueJianAppXTemplateTitle;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YueJianAppEditAuthInfoActivity extends YueJianAppFullScreenModeActivity implements View.OnClickListener {

    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.ll_phone_num)
    LinearLayout llPhoneNum;
    @BindView(R.id.ll_height)
    LinearLayout llHeight;
    @BindView(R.id.ll_weight)
    LinearLayout llWeight;
    @BindView(R.id.ll_sign)
    LinearLayout llSign;
    @BindView(R.id.ll_city)
    LinearLayout llCity;
    @BindView(R.id.ll_my_intro)
    LinearLayout llMyIntro;
    @BindView(R.id.ll_labels)
    LinearLayout llLabels;
    @BindView(R.id.ll_signature)
    LinearLayout llSigns;
    @BindView(R.id.btn_auth)
    Button btnAuth;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_my_intro)
    TextView tvMyIntro;
    @BindView(R.id.tv_labels)
    TextView tvLabels;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.label_container)
    YueJianAppLabelGroup labelContainer;
    @BindView(R.id.rcv_picture)
    RecyclerView rcvPicture;
    @BindView(R.id.ll_id_card)
    LinearLayout llIdCard;
    @BindView(R.id.tv_id_card)
    TextView tvIdCard;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;

    private int maxSelectNum = 8;
    private List<LocalMedia> selectList = new ArrayList<>();
    private YueJianAppGridImageAdapter2 adapter;
    private PopupWindow pop;
    private String imageUploadToken;
    private JSONArray pictureKey = new JSONArray();
    private int sign_index;
    private String realName;
    private Toast toast;
    private YueJianAppACUserPrivateInfoModel privateInfoModel;
    private YueJianAppEditAuthInfoActivity mContext;
    private ArrayList<YueJianAppACUserTagMetaDataModel> selfTagMetaDataModels = new ArrayList<>();
    private String[] signs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_edit_auth_info);
        mContext = YueJianAppEditAuthInfoActivity.this;
        ButterKnife.bind(this);
        toast = Toast.makeText(YueJianAppEditAuthInfoActivity.this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        signs = getResources().getStringArray(R.array.sign);
        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getImageUploadToken();
        initData();
    }


    private void getImageUploadToken() {
        YueJianAppApiProtoHelper.sendACGetQiniuUploadTokenReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                YueJianAppApiProtoHelper.UPLOAD_FILE_AUTH_IMAGE,
                new YueJianAppApiProtoHelper.ACGetQiniuUploadTokenReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse(String uploadToken) {
                        imageUploadToken = uploadToken;
                    }
                }
        );
    }

    private void initData() {
        privateInfoModel = YueJianAppAppContext.getInstance().getPrivateInfoModel();
        if (privateInfoModel == null) {
            YueJianAppApiProtoHelper.sendACGetUserPrivateInfoReq(
                    this,
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    new YueJianAppApiProtoHelper.ACGetUserPrivateInfoReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            YueJianAppTLog.error(errMessage);
                        }

                        @Override
                        public void onResponse(YueJianAppACUserPrivateInfoModel user) {
                            YueJianAppAppContext.getInstance().setPrivateInfo(user);
                            privateInfoModel = user;
                            fillUI();
                            initPictureGroup();
                        }
                    });
        } else {
            fillUI();
            initPictureGroup();
        }

        YueJianAppApiProtoHelper.sendACGetUserPublicInfoReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                new YueJianAppApiProtoHelper.ACGetUserPublicInfoReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        toast.setText(errMessage);
                        toast.show();
                    }

                    @Override
                    public void onResponse(YueJianAppACUserPublicInfoModel user) {
                        tvName.setText(user.getName());
                        tvSignature.setText(user.getSignature());
                    }
                });
    }

    private void fillUI() {
        if (!YueJianAppStringUtil.isEmpty(privateInfoModel.getMobile())) {
            tvPhoneNum.setText(privateInfoModel.getMobile());
        }
        if (!YueJianAppStringUtil.isEmpty(privateInfoModel.getCerNo())) {
            tvIdCard.setText(privateInfoModel.getCerNo());
        }
        if (!YueJianAppStringUtil.isEmpty(privateInfoModel.getHeight())) {
            tvHeight.setText(String.format("%sCM", privateInfoModel.getHeight()));
        }
        if (!YueJianAppStringUtil.isEmpty(privateInfoModel.getWeight())) {
            tvWeight.setText(String.format("%sKG", privateInfoModel.getWeight()));
        }
        if (!YueJianAppStringUtil.isEmpty(privateInfoModel.getSign())) {
            tvSign.setText(privateInfoModel.getSign());
            for (int i = 0; i < signs.length; i++) {
                sign_index = signs[i].equals(privateInfoModel.getSign()) ? i : 0;
            }
        }

        if (!YueJianAppStringUtil.isEmpty(privateInfoModel.getCity())) {
            tvCity.setText(privateInfoModel.getCity());
        }
        if (!YueJianAppStringUtil.isEmpty(privateInfoModel.getIntroduction())) {
            tvMyIntro.setText(privateInfoModel.getIntroduction());
        }
        if (!YueJianAppStringUtil.isEmpty(privateInfoModel.getCity())) {
            tvCity.setText(privateInfoModel.getCity());
        }
        if (privateInfoModel.getTags() != 0) {
            getSelfTags(privateInfoModel.getTags());
            labelContainer.setLabels(selfTagMetaDataModels);
            tvLabels.setText("");
        }
    }

    /**
     * 获取自己的形象标签
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
                    selfTagMetaDataModels.add(YueJianAppAppContext.getInstance().getMyUserTagMetaByTagId((int) Math.pow(2, i - 1)));
                    break;
                }
            }
        } else {
            selfTagMetaDataModels.add(YueJianAppAppContext.getInstance().getMyUserTagMetaByTagId(tags));
        }
    }

    private void initPictureGroup() {
        String images = privateInfoModel.getImagesJson();
        if (!YueJianAppStringUtil.isEmpty(images)) {
            try {
                pictureKey = new JSONArray(images);
                for (int i = 0; i < pictureKey.length(); i++) {
                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setCutPath((String) pictureKey.get(i));
                    localMedia.setPictureType("image");
                    localMedia.setCut(true);
                    selectList.add(localMedia);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        YueJianAppFullyGridLayoutManager manager = new YueJianAppFullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        rcvPicture.setLayoutManager(manager);
        adapter = new YueJianAppGridImageAdapter2(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        rcvPicture.setAdapter(adapter);
        adapter.setOnItemClickListener(new YueJianAppGridImageAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(YueJianAppEditAuthInfoActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(mContext).externalPicturePreview(position, selectList);
                            break;
                        /*case 2:
                            // 预览视频
                            PictureSelector.create(mContext).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(mContext).externalPictureAudio(media.getPath());
                            break;*/
                    }
                }
            }
        });

        adapter.setOnItemLongClickListener(new YueJianAppGridImageAdapter2.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, View v) {
                showDeleteDialog(position);
            }
        });
    }

    private YueJianAppGridImageAdapter2.onAddPicClickListener onAddPicClickListener = new YueJianAppGridImageAdapter2.onAddPicClickListener() {

        @Override
        public void onAddPicClick() {

            //第一种方式，弹出选择和拍照的dialog
//            showPop();

            //第二种方式，直接进入相册，但是 是有拍照得按钮的
            //参数很多，根据需要添加

            PictureSelector.create(mContext)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                    .previewImage(true)// 是否可预览图片
                    .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .enableCrop(true)// 是否裁剪
                    .compress(true)// 是否压缩
                    .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .rotateEnabled(false) // 裁剪是否可旋转图片
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };

    /**
     * 弹出选择性别窗口
     */
    private void showDeleteDialog(final int position) {
        final LocalMedia localMedia = selectList.get(position);
        final YueJianAppEditableActionSheetDialog mDialog = new YueJianAppEditableActionSheetDialog(this).builder();
        TextView manTextView = new TextView(this);
        TextView delete = new TextView(this);
        mDialog.addSheetItem(manTextView, "设为封面", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        mDialog.cancel();
                        if (position == 0) {
                            return;
                        }

                        selectList.remove(localMedia);
                        selectList.add(0, localMedia);
                        adapter.notifyDataSetChanged();
                        sortPictureKey(position);
                    }
                });
        mDialog.addSheetItem(delete, "删除照片", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        selectList.remove(localMedia);
                        adapter.notifyDataSetChanged();
                        mDialog.cancel();
                        for (int i = 0; i < pictureKey.length(); i++) {
                            try {
                                if (localMedia.getCutPath().equals(pictureKey.get(i))) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        pictureKey.remove(i);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        mDialog.show();
    }

    private void sortPictureKey(int index) {
        List<String> list = new ArrayList<>();
        String str;
        for (int i = 0; i < pictureKey.length(); i++) {
            str = pictureKey.optString(i);
            list.add(str);
        }

        str = list.get(index);
        list.remove(index);
        list.add(0, str);

        //把数据放回去
        for (int i = 0; i < list.size(); i++) {
            str = list.get(i);
            try {
                pictureKey.put(i, str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showPop() {
        View bottomView = View.inflate(mContext, R.layout.yue_jian_app_layout_bottom_dialog, null);
        TextView mAlbum = (TextView) bottomView.findViewById(R.id.tv_album);
        TextView mCamera = (TextView) bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = (TextView) bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        PictureSelector.create(mContext)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum)
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_camera:
                        //拍照
                        PictureSelector.create(mContext)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调

                    images = PictureSelector.obtainMultipleResult(data);
                    selectList.addAll(images);

                    // selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    YueJianAppAppContext.getInstance().getUploadManager().put(
                            images.get(0).getCutPath(),
                            YueJianAppAppContext.getInstance().getLoginUid() + "_" + System.currentTimeMillis(),
                            imageUploadToken,
                            completionHandler,
                            uploadOptions);
                    break;
            }
        }
    }

    private UpCompletionHandler completionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject response) {
            try {
                if (response == null) {
                    YueJianAppTLog.error("upload auth picture response == null!!");
                    return;
                }
                pictureKey.put(response.get("key"));
                if (info != null && info.isOK()) {
                    toast.setText("上传成功!");
                    toast.show();
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                } else {
                    toast.setText("上传失败!");
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private UploadOptions uploadOptions = new UploadOptions(
            null,
            null,
            false,
            new UpProgressHandler() {
                @Override
                public void progress(String key, double percent) {
                    YueJianAppTLog.info("%s::::::::%s", key, percent);
                }
            }, null);

    @OnClick({
            R.id.ll_name, //昵称
            R.id.ll_phone_num, //手机号
            R.id.ll_id_card, // 身份证号
            R.id.ll_height, // 身高
            R.id.ll_weight, // 体重
            R.id.ll_sign, // 星座
            R.id.ll_city, // 城市
            R.id.ll_my_intro, // 个人简介
            R.id.ll_labels, // 形象标签
            R.id.ll_signature, // 签名
            R.id.btn_auth //提交认证
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_name:
                YueJianAppDialogHelp.showNameDialog(
                        mContext,
                        new YueJianAppIBottomDialog() {
                            @Override
                            public void cancelDialog(Dialog d) {

                            }

                            @Override
                            public void determineDialog(Dialog d, Object... value) {
                                d.dismiss();
                                if (value[0].toString().length() <= 0) {
                                    return;
                                }
                                tvName.setText(value[0].toString());
                            }
                        });
                break;
            case R.id.ll_phone_num:
                if (YueJianAppStringUtil.isEmpty(privateInfoModel.getMobile())) {
                    YueJianAppDialogHelper.showBindPhoneDialogFragment(getSupportFragmentManager());
                } else {
                    toast.setText("已经绑定手机号！");
                    toast.show();
                }
                break;
            case R.id.ll_id_card:
                YueJianAppDialogHelp.showIdCard(
                        mContext,
                        new YueJianAppIBottomDialog() {
                            @Override
                            public void cancelDialog(Dialog d) {

                            }

                            @Override
                            public void determineDialog(Dialog d, Object... value) {
                                d.dismiss();
                                if (value[0].toString().length() <= 0) {
                                    return;
                                }
                                realName = (String) value[0];
                                tvIdCard.setText(value[1].toString());
                            }
                        });
                break;
            case R.id.ll_height:
                YueJianAppDialogHelp.showHeightDialog(mContext, new YueJianAppIBottomDialog() {
                    @Override
                    public void cancelDialog(Dialog d) {

                    }

                    @Override
                    public void determineDialog(Dialog d, Object... value) {
                        tvHeight.setText(value[0].toString());
                        d.dismiss();
                    }
                });

                break;
            case R.id.ll_weight:
                YueJianAppDialogHelp.showWeightDialog(mContext, new YueJianAppIBottomDialog() {
                    @Override
                    public void cancelDialog(Dialog d) {

                    }

                    @Override
                    public void determineDialog(Dialog d, Object... value) {
                        tvWeight.setText(value[0].toString());
                        d.dismiss();
                    }
                });
                break;
            case R.id.ll_sign:
                YueJianAppDialogHelp.showSignDialog(mContext, new YueJianAppIBottomDialog() {
                    @Override
                    public void cancelDialog(Dialog d) {

                    }

                    @Override
                    public void determineDialog(Dialog d, Object... value) {
                        sign_index = (int) value[0];
                        tvSign.setText(signs[sign_index]);
                        d.dismiss();
                    }
                });

                break;
            case R.id.ll_city:
                YueJianAppDialogHelp.showCityDialog(mContext, new YueJianAppIBottomDialog() {
                    @Override
                    public void cancelDialog(Dialog d) {

                    }

                    @Override
                    public void determineDialog(Dialog d, Object... value) {
                        tvCity.setText(value[0].toString());
                        d.dismiss();
                    }
                });
                break;
            case R.id.ll_my_intro:
                YueJianAppDialogHelp.showMyIntroDialog(
                        mContext,
                        "可以介绍一下你自己",
                        "例：知名模特、演员",
                        new YueJianAppIBottomDialog() {
                            @Override
                            public void cancelDialog(Dialog d) {

                            }

                            @Override
                            public void determineDialog(Dialog d, Object... value) {
                                d.dismiss();
                                if (value[0].toString().length() <= 0) {
                                    return;
                                }
                                tvMyIntro.setText(value[0].toString());
                            }
                        });
                break;
            case R.id.ll_labels:
                YueJianAppDialogHelp.showMyLabelsDialog(
                        mContext,
                        new YueJianAppIBottomDialog() {
                            @Override
                            public void cancelDialog(Dialog d) {

                            }

                            @Override
                            public void determineDialog(Dialog d, Object... value) {
                                d.dismiss();
                                List<YueJianAppACUserTagMetaDataModel> labelsModel = (List<YueJianAppACUserTagMetaDataModel>) value[0];
                                if (labelsModel.size() > 0) {
                                    labelContainer.setLabels(labelsModel);
                                    tvLabels.setText("");
                                }
                            }
                        });
                break;
            case R.id.ll_signature:
                YueJianAppDialogHelp.showMyIntroDialog(
                        mContext,
                        "你希望大家和你面对面聊些什么？",
                        "随便什么话题都行",
                        new YueJianAppIBottomDialog() {
                            @Override
                            public void cancelDialog(Dialog d) {

                            }

                            @Override
                            public void determineDialog(Dialog d, Object... value) {
                                d.dismiss();
                                if (value[0].toString().length() <= 0) {
                                    return;
                                }
                                tvSignature.setText(value[0].toString());
                            }
                        });
                break;
            case R.id.btn_auth:
                if (pictureKey.length() <= 0) {
                    toast.setText("请上传照片！！！");
                    toast.show();
                    return;
                }
                if (YueJianAppStringUtil.isEmpty(tvName.getText().toString()) || tvName.getText().toString().equals("请输入昵称（必填）")) {
                    toast.setText("请输入昵称！！！");
                    toast.show();
                    return;
                }
                if (YueJianAppStringUtil.isEmpty(tvPhoneNum.getText().toString()) || tvPhoneNum.getText().toString().equals("请输入手机号（必填）")) {
                    toast.setText("请输入手机号！！！");
                    toast.show();
                    return;
                }
                /*if (YueJianAppStringUtil.isEmpty(tvIdCard.getText().toString()) || tvIdCard.getText().toString().equals("请输入身份证号（必填）")) {
                    toast.setText("请输入身份证号！！！");
                    toast.show();
                    return;
                }*/
                if (YueJianAppStringUtil.isEmpty(tvHeight.getText().toString()) || tvHeight.getText().toString().equals("请设置身高（必填）")) {
                    toast.setText("请输入身高！！！");
                    toast.show();
                    return;
                }
                if (YueJianAppStringUtil.isEmpty(tvWeight.getText().toString()) || tvWeight.getText().toString().equals("请设置体重（必填）")) {
                    toast.setText("请输入体重！！！");
                    toast.show();
                    return;
                }
                if (YueJianAppStringUtil.isEmpty(tvSign.getText().toString()) || tvSign.getText().toString().equals("请设置星座（必填）")) {
                    toast.setText("请输入星座！！！");
                    toast.show();
                    return;
                }
                if (YueJianAppStringUtil.isEmpty(tvCity.getText().toString()) || tvCity.getText().toString().equals("请设置城市（必填）")) {
                    toast.setText("请输入城市！！！");
                    toast.show();
                    return;
                }
                if (YueJianAppStringUtil.isEmpty(tvMyIntro.getText().toString()) || tvMyIntro.getText().toString().equals("请编辑个人介绍（必填）")) {
                    toast.setText("请输入个人介绍！！！");
                    toast.show();
                    return;
                }
                if (labelContainer.getLabels().size() <= 0) {
                    toast.setText("请设置形象标签！！！");
                    toast.show();
                    return;
                }
                int tags_num = 0;
                for (int i = 0; i < labelContainer.getLabels().size(); i++) {
                    tags_num += ((YueJianAppACUserTagMetaDataModel) labelContainer.getLabels().get(i)).getTagId();
                }

                String height = tvHeight.getText().toString().trim();
                String weight = tvWeight.getText().toString().trim();
                YueJianAppApiProtoHelper.sendACUploadAuthInfoReq(
                        mContext,
                        YueJianAppAppContext.getInstance().getLoginUid(),
                        YueJianAppAppContext.getInstance().getToken(),
                        YueJianAppStringUtil.encodeStr(tvName.getText().toString().trim()),
                        tvPhoneNum.getText().toString(),
                        Integer.parseInt(tvHeight.getText().toString().substring(0, height.length() - 2)),
                        Integer.parseInt(tvWeight.getText().toString().substring(0, weight.length() - 2)),
                        sign_index,
                        tvCity.getText().toString(),
                        YueJianAppStringUtil.encodeStr(tvMyIntro.getText().toString().trim()),
                        tags_num,
                        YueJianAppStringUtil.encodeStr(tvSignature.getText().toString().trim()),
                        "",
                        "",
                        pictureKey.toString(),
                        new YueJianAppApiProtoHelper.ACUploadAuthInfoReqCallback() {
                            @Override
                            public void onError(int errCode, String errMessage) {
                                toast.setText(errMessage);
                                toast.show();
                            }

                            @Override
                            public void onResponse() {
                                toast.setText("提交审核成功！！！");
                                toast.show();
                                finish();
                            }
                        });
                break;
        }
    }

    public void setPhoneNum(String mPhoneNum) {
        this.tvPhoneNum.setText(mPhoneNum);
    }
}
