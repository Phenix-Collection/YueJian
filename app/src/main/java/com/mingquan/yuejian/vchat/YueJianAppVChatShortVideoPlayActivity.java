package com.mingquan.yuejian.vchat;

import android.app.Dialog;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.interf.YueJianAppINomalDialog;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACVideoInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppDisplayUtils;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;
import com.mingquan.yuejian.utils.YueJianAppUtils;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;
import com.mingquan.yuejian.widget.YueJianAppStatusTextView;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YueJianAppVChatShortVideoPlayActivity extends YueJianAppFullScreenModeActivity implements View.OnClickListener {

    @BindView(R.id.root_view)
    RelativeLayout rootView;
    @BindView(R.id.video_view)
    KSYTextureView videoView;
    @BindView(R.id.iv_mask)
    ImageView ivMask;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.top_bar)
    RelativeLayout topBar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_status)
    YueJianAppStatusTextView tvStatus;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_to_video)
    TextView tvToVideo;
    @BindView(R.id.av_avatar)
    YueJianAppAvatarView avAvatar;
    @BindView(R.id.iv_heart)
    ImageView ivHeart;
    @BindView(R.id.tv_video_heart_num)
    TextView tvVideoHeartNum;
    @BindView(R.id.iv_lock)
    ImageView ivLock;
    @BindView(R.id.iv_follow)
    ImageView ivFollow;
    @BindView(R.id.ll_name)
    RelativeLayout llName;
    @BindView(R.id.tv_looker)
    TextView tvLooker;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.tv_send_gift)
    TextView tvSendGift;

    private YueJianAppACVideoInfoModel videoInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_short_video_play);
        ButterKnife.bind(this);

        videoInfoModel = (YueJianAppACVideoInfoModel) getIntent().getSerializableExtra("videoInfo");
        initView();
        initData();
        initEvents();
        initPlayer();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            topBar.setPadding(0,
                    YueJianAppDisplayUtils.getStatusBarHeight1(getApplicationContext()), 0, 0);
        }

        mToast = Toast.makeText(YueJianAppVChatShortVideoPlayActivity.this, "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
    }

    private void initData() {

        tvName.setText(videoInfoModel.getBroadcaster().getName());
        tvStatus.setStatus(videoInfoModel.getBroadcaster().getStatus(), videoInfoModel.getBroadcaster().getStatusTag());
        avAvatar.setAvatarUrl(videoInfoModel.getBroadcaster().getAvatarUrl());
        ivHeart.setBackgroundResource(videoInfoModel.getIsFollowing() ? R.drawable.yue_jian_app_icon_02006 : R.drawable.yue_jian_app_icon_02007);
        tvVideoHeartNum.setText("" + videoInfoModel.getFollowerCount());
        YueJianAppUiUtils.setVisibility(ivLock, videoInfoModel.getHasLocked() ? View.VISIBLE : View.GONE);
        YueJianAppUiUtils.setVisibility(ivMask, videoInfoModel.getHasLocked() ? View.VISIBLE : View.GONE);
        YueJianAppUiUtils.setVisibility(ivPlay, videoInfoModel.getHasLocked() ? View.VISIBLE : View.GONE);
    }

    private void initEvents() {
        /*ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    YueJianAppUiUtils.setVisibility(ivPlay, View.VISIBLE);
                } else {
                    videoView.start();
                    YueJianAppUiUtils.setVisibility(ivPlay, View.GONE);
                }
            }
        });
        avAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YueJianAppDialogHelper.showVchatOtherInfoDialogFragment(getSupportFragmentManager(), videoInfoModel.getBroadcaster().getUid());
            }
        });

        ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHeart.setClickable(false);
                followVideo();
            }
        });
        ivMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(YueJianAppAppContext.getInstance().getProperty("user.coin")) < videoInfoModel.getPrice()) {//跳转充值页面

                } else {
                    YueJianAppApiProtoHelper.sendACBuyVideoReq(YueJianAppVChatShortVideoPlayActivity.this,
                            YueJianAppAppContext.getInstance().getLoginUid(),
                            YueJianAppAppContext.getInstance().getToken(),
                            videoInfoModel.getVideoId(),
                            new YueJianAppApiProtoHelper.ACBuyVideoReqCallback() {
                                @Override
                                public void onError(int errCode, String errMessage) {
                                    YueJianAppTLog.error("购买视频失败：errCode:%s,errMessage:%s", errCode, errMessage);
                                }

                                @Override
                                public void onResponse() {
                                    YueJianAppUiUtils.setVisibility(ivMask, View.GONE);
                                    YueJianAppUiUtils.setVisibility(ivPlay, View.GONE);
                                    YueJianAppUiUtils.setVisibility(ivLock, View.GONE);
                                }
                            });
                }
            }
        });

        tvToVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YueJianAppUIHelper.showVideoChatActivity(YueJianAppVChatShortVideoPlayActivity.this, videoInfoModel.getBroadcaster(), true, "");
            }
        });*/
    }

    @OnClick({R.id.iv_back, R.id.root_view, R.id.av_avatar, R.id.iv_heart, R.id.iv_mask, R.id.tv_to_video, R.id.iv_follow})
    @Override
    public void onClick(View v) {
        if (YueJianAppUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_back: // 关闭当前页面
                finish();
                break;
            case R.id.root_view: // 播放或暂停
                if (videoView.isPlaying()) {
                    videoView.pause();
                    YueJianAppUiUtils.setVisibility(ivPlay, View.VISIBLE);
                } else {
                    videoView.start();
                    YueJianAppUiUtils.setVisibility(ivPlay, View.GONE);
                }
                break;
            case R.id.av_avatar: // 跳转用户详情页面
                YueJianAppDialogHelper.showVchatOtherInfoDialogFragment(getSupportFragmentManager(), videoInfoModel.getBroadcaster().getUid());
                break;
            case R.id.iv_heart: // 关注短视频
                followVideo();
                break;
            case R.id.iv_mask: // 购买短视频
                if (Integer.parseInt(YueJianAppAppContext.getInstance().getProperty("user.coin")) < videoInfoModel.getPrice()) {//跳转充值页面

                } else {
                    YueJianAppApiProtoHelper.sendACBuyVideoReq(YueJianAppVChatShortVideoPlayActivity.this,
                            YueJianAppAppContext.getInstance().getLoginUid(),
                            YueJianAppAppContext.getInstance().getToken(),
                            videoInfoModel.getVideoId(),
                            new YueJianAppApiProtoHelper.ACBuyVideoReqCallback() {
                                @Override
                                public void onError(int errCode, String errMessage) {
                                    YueJianAppTLog.error("购买视频失败：errcode:%s,errMessage:%s", errCode, errMessage);
                                }

                                @Override
                                public void onResponse() {
                                    YueJianAppUiUtils.setVisibility(ivMask, View.GONE);
                                    YueJianAppUiUtils.setVisibility(ivPlay, View.GONE);
                                    YueJianAppUiUtils.setVisibility(ivLock, View.GONE);
                                }
                            });
                }
                break;
            case R.id.tv_to_video:
                YueJianAppUIHelper.showVideoChatActivity(YueJianAppVChatShortVideoPlayActivity.this, videoInfoModel.getBroadcaster(), "");
                break;
            case R.id.iv_follow:
                YueJianAppApiProtoHelper.sendACFollowReq(
                        this,
                        YueJianAppAppContext.getInstance().getLoginUid(),
                        YueJianAppAppContext.getInstance().getToken(),
                        videoInfoModel.getBroadcaster().getUid(),
                        new YueJianAppApiProtoHelper.ACFollowReqCallback() {
                            @Override
                            public void onError(int errCode, String errMessage) {

                            }

                            @Override
                            public void onResponse() {
                                YueJianAppUiUtils.setVisibility(ivFollow, View.GONE);
                            }
                        }
                );
                break;
        }
    }

    /**
     * （取消）关注视频
     */
    private void followVideo() {
        ivHeart.setClickable(false);
        if (videoInfoModel.getIsFollowing()) { // 已经关注，取消关注视频
            YueJianAppApiProtoHelper.sendACUnfollowVideoReq(this,
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    videoInfoModel.getVideoId(),
                    new YueJianAppApiProtoHelper.ACUnfollowVideoReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            ivHeart.setClickable(true);
                        }

                        @Override
                        public void onResponse() {
                            mToast.setText("取消成功");
                            mToast.show();
                            videoInfoModel.setIsFollowing(false);
                            videoInfoModel.setFollowerCount(videoInfoModel.getFollowerCount() - 1);
                            ivHeart.setClickable(true);
                            ivHeart.setBackgroundResource(R.drawable.yue_jian_app_icon_02007);
                            tvVideoHeartNum.setText("" + (videoInfoModel.getFollowerCount()));
                        }
                    });
        } else { // 未关注，关注视频
            YueJianAppApiProtoHelper.sendACFollowVideoReq(this,
                    YueJianAppAppContext.getInstance().getLoginUid(),
                    YueJianAppAppContext.getInstance().getToken(),
                    videoInfoModel.getVideoId(),
                    new YueJianAppApiProtoHelper.ACFollowVideoReqCallback() {
                        @Override
                        public void onError(int errCode, String errMessage) {
                            ivHeart.setClickable(true);
                        }

                        @Override
                        public void onResponse() {
                            mToast.setText("点赞成功");
                            mToast.show();
//                            Toast.makeText(YueJianAppVChatShortVideoPlayActivity.this,"点赞成功",Toast.LENGTH_SHORT).setGravity(Gravity.CENTER,0,0);
                            videoInfoModel.setIsFollowing(true);
                            videoInfoModel.setFollowerCount(videoInfoModel.getFollowerCount() + 1);
                            ivHeart.setClickable(true);
                            ivHeart.setBackgroundResource(R.drawable.yue_jian_app_icon_02006);
                            tvVideoHeartNum.setText("" + (videoInfoModel.getFollowerCount()));
                        }
                    });
        }
    }

    Toast mToast;

    @Override
    protected void onRestart() {
        super.onRestart();
        if (videoView != null) {
            videoView.runInForeground();
            videoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.runInBackground(true);
            videoView.pause();
        }
    }

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        try {
            videoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            videoView.setKeepScreenOn(true);
            videoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            videoView.setOnCompletionListener(mOnCompletionListener);
            videoView.setOnPreparedListener(mOnPreparedListener);
            videoView.setOnInfoListener(mOnInfoListener);
            videoView.setOnVideoSizeChangedListener(mOnVideoSizeChangeListener);
            videoView.setOnErrorListener(mOnErrorListener);
            videoView.setOnSeekCompleteListener(mOnSeekCompletedListener);
            videoView.setOnMessageListener(mOnMessageListener);
            videoView.setScreenOnWhilePlaying(true);
            videoView.setTimeout(5, 30);
            videoView.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_AUTO);
            videoView.setBufferTimeMax(2);
            videoView.setBufferSize(15);
            videoView.setDataSource(videoInfoModel.getVideoUrl());


            if (videoInfoModel.getHasLocked()) {
                YueJianAppDialogHelp.showDialog(YueJianAppVChatShortVideoPlayActivity.this, "钻石不足，是否立即充值", new YueJianAppINomalDialog() {
                    @Override
                    public void cancelDialog(View v, Dialog d) {
                        d.dismiss();
                    }

                    @Override
                    public void determineDialog(View v, Dialog d) {
                        d.dismiss();
                    }
                });
            } else {
                videoView.prepareAsync();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            if (videoView != null) {

            }
        }
    };

    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            /*if (videoInfoModel.getHasLocked()) {
                YueJianAppTLog.debug("需要付费才能播放");
                YueJianAppDialogHelp.showConfirmDialog(getLayoutInflater(), YueJianAppVChatShortVideoPlayActivity.this, "钻石不足，是否立即充值", new YueJianAppINomalDialog() {
                    @Override
                    public void cancelDialog(View v, Dialog d) {
                        d.dismiss();
                    }

                    @Override
                    public void determineDialog(View v, Dialog d) {
                        d.dismiss();
                        // TODO: 2018/9/10 跳转充值页面
                    }
                });
                return;
            }
            // start player
            videoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            videoView.start();*/
        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangeListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
        }
    };

    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompletedListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer mp) {
        }
    };

    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
            videoView.reload(videoInfoModel.getVideoUrl(), false, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_FAST);
        }
    };

    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            YueJianAppTLog.error("OnErrorListener, Error:" + what + ",extra:" + extra);
            //      videoPlayEnd();
            return false;
        }
    };

    public IMediaPlayer.OnInfoListener mOnInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            switch (i) {
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    break;
                case KSYMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    break;
                case KSYMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    break;
                case KSYMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    break;
                case KSYMediaPlayer.MEDIA_INFO_SUGGEST_RELOAD:
                    // 播放SDK有做快速开播的优化，在流的音视频数据交织并不好时，可能只找到某一个流的信息
                    // 当播放器读到另一个流的数据时会发出此消息通知
                    // 请务必调用reload接口
                    if (videoView != null)
                        videoView.reload(videoInfoModel.getVideoUrl(), false);
                    break;
                case KSYMediaPlayer.MEDIA_INFO_RELOADED:
                    break;
            }
            return false;
        }
    };

    private IMediaPlayer.OnMessageListener mOnMessageListener = new IMediaPlayer.OnMessageListener() {
        @Override
        public void onMessage(IMediaPlayer iMediaPlayer, Bundle bundle) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.release();
            videoView = null;
        }
    }
}
