package com.mingquan.yuejian.vchat;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.auth.YueJianAppGridVideoAdapter;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.interf.YueJianAppINomalDialog;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACVideoInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppEditableActionSheetDialog;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppStringUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YueJianAppUploadVideoActivity extends YueJianAppFullScreenModeActivity {

    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout;
    @BindView(R.id.rcv_video)
    RecyclerView rcvVideo;
    private Activity mContext;
    private String videoUploadToken;
    private YueJianAppGridVideoAdapter videoAdapter;
    private ArrayList<YueJianAppACVideoInfoModel> videoList = new ArrayList<>();
    private YueJianAppOneGoGridLayoutManager mLayoutManager;
    private int mlastId = 0;
    private Toast toast;
    private String path;
    String videoKey = "";
    String imageKey = "";
    private ProgressDialog waitDialog;
    private boolean isVisible = false;
    private int price;
    public static final int SET_ADD_VIDEO_PRICE = 100;
    public static final int SET_VIDEO_PRICE = 101;
    private boolean canVideoChat = false; // 是否能连麦

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_upload_video);
        ButterKnife.bind(this);
        mContext = this;
        canVideoChat = YueJianAppAppContext.getInstance().getCanVideoChat();
        titleLayout.setLeftBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getVideoUploadToken();
        toast = Toast.makeText(YueJianAppUploadVideoActivity.this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        initData();
    }

    private void getVideoUploadToken() {
        YueJianAppApiProtoHelper.sendACGetQiniuUploadTokenReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                YueJianAppApiProtoHelper.UPLOAD_FILE_VIDEO,
                new YueJianAppApiProtoHelper.ACGetQiniuUploadTokenReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse(String uploadToken) {
                        videoUploadToken = uploadToken;
                    }
                }
        );
    }

    private void initData() {
        videoAdapter = new YueJianAppGridVideoAdapter(this);
        videoAdapter.setList(videoList);
        videoAdapter.setOnItemClickListener(new YueJianAppGridVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position, View v) {

                if (position == 0) { // 打开相册
                    if (!canVideoChat) {
                        YueJianAppUIHelper.showSetVideoPriceActivity(mContext, -1);
                        return;
                    }
                    YueJianAppDialogHelp.showDialog(mContext, "是否要设定视频价格？", new YueJianAppINomalDialog() {
                        @Override
                        public void cancelDialog(View v, Dialog d) {
                            showPhotoAlbum();
                            price = 0;
                            d.dismiss();
                        }

                        @Override
                        public void determineDialog(View v, Dialog d) {
                            YueJianAppUIHelper.showSetVideoPriceActivity(mContext, -1);
                            d.dismiss();
                        }
                    });
                } else {
                    showVideoMenu(position - 1);
                }
            }
        });
        mLayoutManager = new YueJianAppOneGoGridLayoutManager(mContext, 2);
        rcvVideo.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                if (itemPosition % 2 == 0) {
                    outRect.right = 2;
                    outRect.bottom = 4;
                } else {
                    outRect.left = 2;
                    outRect.bottom = 4;
                }
            }
        });
        rcvVideo.setLayoutManager(mLayoutManager);
        rcvVideo.setAdapter(videoAdapter);
        requestBroadcasterVideoList();
    }

    private void requestBroadcasterVideoList() {
        YueJianAppApiProtoHelper.sendACGetBroadcasterVideoListReq(
                mContext,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                YueJianAppAppContext.getInstance().getLoginUid(),
                20,
                mlastId,
                new YueJianAppApiProtoHelper.ACGetBroadcasterVideoListReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {

                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACVideoInfoModel> videos, int nextOffset) {
//                        mlastId = nextOffset;
                        videoList.clear();
                        videoList.addAll(videos);
                        videoAdapter.notifyDataSetChanged();
                    }
                }
        );
    }

    private void showPhotoAlbum() {
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
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
                .recordVideoSecond(60)//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 播放，删除，设定价格的菜单
     *
     * @param position
     */
    private void showVideoMenu(final int position) {
        final YueJianAppEditableActionSheetDialog mDialog = new YueJianAppEditableActionSheetDialog(this).builder();
        TextView play = new TextView(this);
        TextView price = new TextView(this);
        TextView delete = new TextView(this);
        mDialog.addSheetItem(play, "播放视频", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        YueJianAppUIHelper.showShortVideoPlayerActivity(YueJianAppUploadVideoActivity.this, videoList, position);
                        mDialog.cancel();
                    }
                });
        if (canVideoChat) {
            mDialog.addSheetItem(price, "设定价格", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                    new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            YueJianAppUIHelper.showSetVideoPriceActivity(mContext, videoList.get(position).getVideoId());
                            mDialog.cancel();
                        }
                    });
        }

        mDialog.addSheetItem(delete, "删除视频", YueJianAppEditableActionSheetDialog.SheetItemColor.Blue,
                new YueJianAppEditableActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        YueJianAppApiProtoHelper.sendACDeleteVideoReq(
                                YueJianAppUploadVideoActivity.this,
                                YueJianAppAppContext.getInstance().getLoginUid(),
                                YueJianAppAppContext.getInstance().getToken(),
                                videoList.get(position).getVideoId(),
                                new YueJianAppApiProtoHelper.ACDeleteVideoReqCallback() {
                                    @Override
                                    public void onError(int errCode, String errMessage) {
                                        toast.setText(errMessage);
                                        toast.show();
                                    }

                                    @Override
                                    public void onResponse() {
                                        toast.setText("删除成功！");
                                        toast.show();
                                        videoList.remove(position);
                                        videoAdapter.notifyDataSetChanged();
                                    }
                                }
                        );
                        mDialog.cancel();
                    }
                });
        mDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                    // 图片选择结果回调
                    List<LocalMedia> videos = PictureSelector.obtainMultipleResult(data);
                    path = videos.get(0).getPath();
                    YueJianAppAppContext.getInstance().getUploadManager().put(
                            path,
                            YueJianAppAppContext.getInstance().getLoginUid() + System.currentTimeMillis(),
                            videoUploadToken,
                            videoCompletionHandler,
                            uploadOptions);
                }
                break;
            case SET_ADD_VIDEO_PRICE:
                price = data.getIntExtra("SET_ADD_VIDEO_PRICE", -1);
                showPhotoAlbum();
                break;
            case SET_VIDEO_PRICE:
                requestBroadcasterVideoList();
                break;
        }
    }

    private UpCompletionHandler videoCompletionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject response) {
            try {
                if (response == null) {
                    YueJianAppTLog.error("upload video response == null!!");
                    return;
                }
                videoKey = (String) response.get("key");
                if (YueJianAppStringUtil.isEmpty(videoKey)) {
                    YueJianAppTLog.error("upload video key is empty!!");
                    return;
                }

                if (info != null && info.isOK()) {
                    toast.setText("上传成功!");
                    toast.show();

                    try {
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(path);
                        Bitmap bitmap = retriever.getFrameAtTime();
                        FileOutputStream outStream;
                        final String localThumb = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
                        outStream = new FileOutputStream(new File(localThumb));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outStream);
                        outStream.close();
                        retriever.release();
                        YueJianAppAppContext.getInstance().getUploadManager().put(
                                localThumb,
                                YueJianAppAppContext.getInstance().getLoginUid() + "_" + System.currentTimeMillis(),
                                videoUploadToken,
                                imageCompletionHandler,
                                uploadOptions);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    toast.setText("video upload failure!");
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private UpCompletionHandler imageCompletionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject response) {
            try {
                if (response == null) {
                    YueJianAppTLog.error("upload image response == null!!");
                    return;
                }
                imageKey = (String) response.get("key");
                if (YueJianAppStringUtil.isEmpty(imageKey)) {
                    YueJianAppTLog.error("upload image key is empty!!");
                    return;
                }
                if (info != null && info.isOK()) {
                    hideWaitDialog();
                    toast.setText("短视频上传成功!");
                    toast.show();


                    YueJianAppApiProtoHelper.sendACUploadVideoReq(
                            mContext,
                            YueJianAppAppContext.getInstance().getLoginUid(),
                            YueJianAppAppContext.getInstance().getToken(),
                            videoKey,
                            imageKey,
                            price, new YueJianAppApiProtoHelper.ACUploadVideoReqCallback() {
                                @Override
                                public void onError(int errCode, String errMessage) {
                                    toast.setText(errMessage);
                                    toast.show();
                                }

                                @Override
                                public void onResponse(YueJianAppACVideoInfoModel video) {
                                    videoList.add(video);
                                    videoAdapter.notifyDataSetChanged();
                                }
                            });

                } else {
                    hideWaitDialog();
                    toast.setText("image upload failure!");
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
                    showWaitDialog();
                }
            }, null);

    private void showWaitDialog() {
        if (isVisible) {
            return;
        }
        if (waitDialog == null) {
            waitDialog = YueJianAppDialogHelp.getWaitDialog(this, "正在上传，请稍后。。。");
            waitDialog.setCanceledOnTouchOutside(false);
            isVisible = true;
            waitDialog.show();
        }
    }

    private void hideWaitDialog() {
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
            isVisible = false;
        }
    }
}
