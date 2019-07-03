package com.mingquan.yuejian.ui;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseFullModeActivity;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUtils;
import com.mingquan.yuejian.widget.YueJianAppLoadUrlImageView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 头像上传
 */
public class YueJianAppSelectAvatarActivity extends YueJianAppBaseFullModeActivity {
    @BindView(R.id.av_edit_head)
    YueJianAppLoadUrlImageView mUHead;
    @BindView(R.id.btn_avator_from_album)
    Button mFromAlbum;//拍照
    @BindView(R.id.btn_avator_photograph)
    Button mFromPhotograph;//从相册
    private String AvatarUploadToken; // 头像上传token
    private Toast toast;

    public YueJianAppSelectAvatarActivity() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.yue_jian_app_activity_edit_head;
    }

    @Override
    public void initView() {
        toast = Toast.makeText(YueJianAppSelectAvatarActivity.this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) YueJianAppTDevice.getScreenWidth(), (int) YueJianAppTDevice.getScreenWidth());
        mUHead.setLayoutParams(layoutParams);
        mUHead.setImageLoadUrl(getIntent().getStringExtra("uhead"));
    }

    @Override
    public void initData() {
        getAvatarUploadToken();
    }

    @OnClick({R.id.iv_select_avatar_back, R.id.btn_avator_from_album, R.id.btn_avator_photograph})
    @Override
    public void onClick(View v) {
        if (YueJianAppUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_select_avatar_back:
                finish();
                break;
            case R.id.btn_avator_from_album: //相册
                PictureSelector.create(YueJianAppSelectAvatarActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片
                        .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .enableCrop(true)// 是否裁剪
                        .compress(true)// 是否压缩
                        .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .rotateEnabled(false) // 裁剪是否可旋转图片
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            case R.id.btn_avator_photograph:
                PictureSelector.create(YueJianAppSelectAvatarActivity.this)
                        .openCamera(PictureMimeType.ofImage())
                        .previewImage(true)// 是否可预览图片
                        .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .enableCrop(true)// 是否裁剪
                        .compress(true)// 是否压缩
                        .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .rotateEnabled(false) // 裁剪是否可旋转图片
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
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
                    YueJianAppAppContext.getInstance().getUploadManager().put(
                            images.get(0).getCutPath(),
                            YueJianAppAppContext.getInstance().getLoginUid() + "_" + System.currentTimeMillis(),
                            AvatarUploadToken,
                            completionHandler,
                            uploadOptions);
                    break;
            }
        }
    }

    private String avatarkey;
    private UpCompletionHandler completionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject response) {
            try {
                if (response == null) {
                    YueJianAppTLog.error("upload avatar response == null!!");
                    return;
                }
                avatarkey = (String) response.get("key");
                if (YueJianAppStringUtil.isEmpty(avatarkey)) {
                    YueJianAppTLog.error("upload avatar key is empty!!");
                    return;
                }
                if (info != null && info.isOK()) {
                    toast.setText("上传成功!");
                    toast.show();
                    YueJianAppApiProtoHelper.sendACUploadAvatarReq(
                            YueJianAppSelectAvatarActivity.this,
                            YueJianAppAppContext.getInstance().getLoginUid(),
                            YueJianAppAppContext.getInstance().getToken(),
                            avatarkey,
                            new YueJianAppApiProtoHelper.ACUploadAvatarReqCallback() {
                                @Override
                                public void onError(int errCode, String errMessage) {
                                    toast.setText(errMessage);
                                    toast.show();
                                }

                                @Override
                                public void onResponse(String avatarUrl) {
                                    mUHead.setImageLoadUrl(avatarUrl);
                                    YueJianAppACUserPublicInfoModel model = YueJianAppAppContext.getInstance().getAcPublicUser();
                                    model.setAvatarUrl(avatarUrl);
                                    YueJianAppAppContext.getInstance().updateAcPublicUser(model);
                                }
                            });
                } else {
                    toast.setText("upload failure!");
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

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    private void getAvatarUploadToken() {
        YueJianAppApiProtoHelper.sendACGetQiniuUploadTokenReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                YueJianAppApiProtoHelper.UPLOAD_FILE_AVATAR,
                new YueJianAppApiProtoHelper.ACGetQiniuUploadTokenReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse(String uploadToken) {
                        AvatarUploadToken = uploadToken;
                    }
                }
        );
    }
}
