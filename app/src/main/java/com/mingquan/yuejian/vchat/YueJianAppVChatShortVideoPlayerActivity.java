package com.mingquan.yuejian.vchat;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppConfig;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.interf.YueJianAppIBottomDialog;
import com.mingquan.yuejian.interf.YueJianAppICallBack;
import com.mingquan.yuejian.interf.YueJianAppINomalDialog;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACGiftModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACVideoInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.utils.YueJianAppDialogHelp;
import com.mingquan.yuejian.utils.YueJianAppShowGiftManager;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUIHelper;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;
import com.mingquan.yuejian.widget.YueJianAppStatusTextView;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YueJianAppVChatShortVideoPlayerActivity extends YueJianAppFullScreenModeActivity {

    @BindView(R.id.vertical_viewpager)
    YueJianAppVerticalViewPager verticalViewpager;
    @BindView(R.id.gift_tips)
    LinearLayout giftTips;

    private YueJianAppShowGiftManager mShowGiftManager;
    private BroadcastReceiver broadcastReceiver;

    KSYTextureView videoView;
    private List<View> mViews = new ArrayList<>();
    private List<YueJianAppACVideoInfoModel> mVideoInfoModelList = new ArrayList<>();
    private YueJianAppACVideoInfoModel currVideoInfoModel;
    private int mPlayingPosition;
    private int mCurrentPosition;
    private YueJianAppVideoLayoutAdapter videoLayoutAdapter;
    private Toast mToast;
    private ArrayList<YueJianAppACGiftModel> giftModels;
    private boolean canVideoChat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_vchat_short_video_player);
        ButterKnife.bind(this);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        registerReceiver();
        mShowGiftManager = new YueJianAppShowGiftManager(this, giftTips);

        initData();
        getViews();
        initPlayer();
    }

    private void initData() {
        canVideoChat = YueJianAppAppContext.getInstance().getCanVideoChat();
        mCurrentPosition = getIntent().getIntExtra("POSITION", 0);
        mVideoInfoModelList = YueJianAppWeakDataHolder.getInstance().getData();
        if (mCurrentPosition < mVideoInfoModelList.size()) {
            currVideoInfoModel = mVideoInfoModelList.get(mCurrentPosition);
        } else {
            mCurrentPosition = mVideoInfoModelList.size() - 1;
            currVideoInfoModel = mVideoInfoModelList.get(mCurrentPosition);
        }

        mToast = Toast.makeText(YueJianAppVChatShortVideoPlayerActivity.this, "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);

        // 获取礼物列表
        YueJianAppApiProtoHelper.sendACGetGiftListReq(
                this,
                getPackageName(),
                new YueJianAppApiProtoHelper.ACGetGiftListReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error("sendACGetGiftListReq:%s", errMessage);
                    }

                    @Override
                    public void onResponse(ArrayList<YueJianAppACGiftModel> gifts) {
                        giftModels = gifts;
                    }
                });
    }

    private void getViews() {
        mViews.clear();
        for (final YueJianAppACVideoInfoModel videoInfoModel : mVideoInfoModelList) {
            View view = LayoutInflater.from(this).inflate(R.layout.yue_jian_app_video_layout_item, null);
            FrameLayout videoContainer = (FrameLayout) view.findViewById(R.id.video_container);
            RelativeLayout ivBack = (RelativeLayout) view.findViewById(R.id.top_bar);
            final ImageView ivCover = (ImageView) view.findViewById(R.id.iv_cover);
            final ImageView ivPlay = (ImageView) view.findViewById(R.id.iv_play);
            YueJianAppAvatarView avatar = (YueJianAppAvatarView) view.findViewById(R.id.avatar);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            final ImageView ivFollow = (ImageView) view.findViewById(R.id.iv_follow);
            YueJianAppStatusTextView tvStatus = (YueJianAppStatusTextView) view.findViewById(R.id.tv_status);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            ImageView ivLock = (ImageView) view.findViewById(R.id.iv_lock);
            final ImageView ivHeart = (ImageView) view.findViewById(R.id.iv_heart);
            final TextView tvHeartNum = (TextView) view.findViewById(R.id.tv_video_heart_num);
            TextView tvLooker = (TextView) view.findViewById(R.id.tv_looker);
            final TextView tvShare = (TextView) view.findViewById(R.id.tv_share);
            TextView tvToVideo = (TextView) view.findViewById(R.id.tv_to_video);
            TextView tvSendGift = (TextView) view.findViewById(R.id.tv_send_gift);

            final YueJianAppACUserPublicInfoModel userPublicInfoModel = videoInfoModel.getBroadcaster();

            YueJianAppAppContext.getImageLoaderUtil().loadImageHighDefinite(this, videoInfoModel.getCoverUrl(), ivCover, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (bitmap.getWidth() > bitmap.getHeight()) {
                        Matrix matrix = new Matrix();
                        matrix.setRotate(-90);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                        ivCover.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
            avatar.setAvatarUrl(userPublicInfoModel.getAvatarUrl());
            tvName.setText(userPublicInfoModel.getName());
            tvLooker.setText(String.valueOf(videoInfoModel.getWatchCount()));
            tvShare.setText(String.valueOf(videoInfoModel.getShareCount()));
            tvStatus.setStatus(userPublicInfoModel.getStatus(), userPublicInfoModel.getStatusTag());
            ivHeart.setBackgroundResource(videoInfoModel.getIsFollowing() ? R.drawable.yue_jian_app_icon_02006 : R.drawable.yue_jian_app_icon_02007);
            tvHeartNum.setText(String.valueOf(videoInfoModel.getFollowerCount()));
            tvTitle.setText(userPublicInfoModel.getSignature());
            tvToVideo.setVisibility(canVideoChat ? View.VISIBLE : View.GONE);
            if (videoInfoModel.getHasLocked() && !userPublicInfoModel.getUid().equals(YueJianAppAppContext.getInstance().getLoginUid())) {
                ivLock.setVisibility(View.VISIBLE);
                ivPlay.setVisibility(View.VISIBLE);
            } else {
                ivLock.setVisibility(View.GONE);
                ivPlay.setVisibility(View.GONE);
            }

            ivFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YueJianAppApiProtoHelper.sendACFollowReq(
                            YueJianAppVChatShortVideoPlayerActivity.this,
                            YueJianAppAppContext.getInstance().getLoginUid(),
                            YueJianAppAppContext.getInstance().getToken(),
                            userPublicInfoModel.getUid(),
                            new YueJianAppApiProtoHelper.ACFollowReqCallback() {
                                @Override
                                public void onError(int errCode, String errMessage) {

                                }

                                @Override
                                public void onResponse() {
                                    mToast.setText("关注成功");
                                    mToast.show();
                                    YueJianAppUiUtils.setVisibility(ivFollow, View.GONE);
                                }
                            });
                }
            });

            videoContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 视频没有加锁或者视频是我的， 直接播放
                    if (!videoInfoModel.getHasLocked() || videoInfoModel.getBroadcaster().getUid().equals(YueJianAppAppContext.getInstance().getLoginUid())) {
                        if (videoView.isPlaying()) {
                            videoView.pause();
                            YueJianAppUiUtils.setVisibility(ivPlay, View.VISIBLE);
                        } else {
                            videoView.start();
                            YueJianAppUiUtils.setVisibility(ivPlay, View.GONE);
                        }
                    } else {
                        String msg = String.format("观看此视频需要%s钻石", currVideoInfoModel.getPrice());
                        YueJianAppDialogHelp.showDialog(
                                YueJianAppVChatShortVideoPlayerActivity.this,
                                msg,
                                new YueJianAppINomalDialog() {
                                    @Override
                                    public void cancelDialog(View v, Dialog d) {
                                        mPlayingPosition = mCurrentPosition;
                                        d.dismiss();
                                    }

                                    @Override
                                    public void determineDialog(View v, Dialog d) {
                                        buyShortVideo();
                                        mPlayingPosition = mCurrentPosition;
                                        d.dismiss();
                                    }
                                });
                    }
                }
            });

            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YueJianAppDialogHelper.showVchatOtherInfoDialogFragment(
                            getSupportFragmentManager(),
                            videoInfoModel.getBroadcaster().getUid());
                }
            });

            ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followVideo(videoInfoModel, ivHeart, tvHeartNum);
                }
            });

            tvShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YueJianAppDialogHelp.showShareVideoDialog(
                            YueJianAppVChatShortVideoPlayerActivity.this,
                            videoInfoModel,
                            new YueJianAppICallBack() {
                                @Override
                                public void operate() {
                                    videoInfoModel.setShareCount(videoInfoModel.getShareCount() + 1);
                                    tvShare.setText(String.valueOf(videoInfoModel.getShareCount()));
                                }
                            });
                }
            });

            tvToVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YueJianAppUIHelper.showVideoChatActivity(YueJianAppVChatShortVideoPlayerActivity.this, videoInfoModel.getBroadcaster(), "");
                }
            });

            tvSendGift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (giftModels == null) {
                        mToast.setText("礼物资源正在加载，请稍后。。。");
                        mToast.show();
                        return;
                    }

                    YueJianAppDialogHelp.showGiftListDialog(
                            YueJianAppVChatShortVideoPlayerActivity.this,
                            giftModels,
                            videoInfoModel.getBroadcaster(),
                            videoInfoModel.getVideoId(),
                            new YueJianAppIBottomDialog() {
                                @Override
                                public void cancelDialog(Dialog d) {
                                    d.dismiss();
                                }

                                @Override
                                public void determineDialog(Dialog d, Object... value) {
                                    d.dismiss();
                                    // 点击充值按钮跳转充值页面
                                    YueJianAppDialogHelper.showRechargeDialogFragment(getSupportFragmentManager());
                                }
                            });
                }
            });

            mViews.add(view);
        }

        if (videoLayoutAdapter == null) {
            videoLayoutAdapter = new YueJianAppVideoLayoutAdapter(mViews);
            verticalViewpager.setAdapter(videoLayoutAdapter);
        } else {
            videoLayoutAdapter.notifyDataSetChanged();
        }
        if (mCurrentPosition != -1) {
            verticalViewpager.setCurrentItem(mCurrentPosition);
        }

        verticalViewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                if (mCurrentPosition >= mVideoInfoModelList.size()) {
                    mCurrentPosition = mVideoInfoModelList.size() - 1;
                }
                currVideoInfoModel = mVideoInfoModelList.get(mCurrentPosition);
                videoView.pause();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mPlayingPosition == mCurrentPosition) return;

                if (state == YueJianAppVerticalViewPager.SCROLL_STATE_IDLE) {
                    videoView.release();
                    ViewParent parent = videoView.getParent();
                    if (parent != null && parent instanceof FrameLayout) {
                        ((FrameLayout) parent).removeView(videoView);
                    }
                    initPlayer();
                }
            }
        });
    }

    /**
     * （取消）关注视频
     */
    private void followVideo(final YueJianAppACVideoInfoModel videoInfoModel, final ImageView ivHeart, final TextView tvVideoHeartNum) {
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
                            tvVideoHeartNum.setText(String.valueOf(videoInfoModel.getFollowerCount()));
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
                            videoInfoModel.setIsFollowing(true);
                            videoInfoModel.setFollowerCount(videoInfoModel.getFollowerCount() + 1);
                            ivHeart.setClickable(true);
                            ivHeart.setBackgroundResource(R.drawable.yue_jian_app_icon_02006);
                            tvVideoHeartNum.setText("" + (videoInfoModel.getFollowerCount()));
                        }
                    });
        }
    }

    /**
     * 初始化播放器
     */
    private void initPlayer() {
        try {
            videoView = new KSYTextureView(this);
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
            videoView.setDataSource(currVideoInfoModel.getVideoUrl());
            // 视频没有加锁或者视频是我的，直接播放
            if (!currVideoInfoModel.getHasLocked() || currVideoInfoModel.getBroadcaster().getUid().equals(YueJianAppAppContext.getInstance().getLoginUid())) {
                View view = mViews.get(mCurrentPosition);
                FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.video_container);
                frameLayout.addView(videoView);
                final ImageView ivFollow = (ImageView) view.findViewById(R.id.iv_follow);
                YueJianAppApiProtoHelper.sendACIsFollowingReq(
                        this,
                        YueJianAppAppContext.getInstance().getLoginUid(),
                        YueJianAppAppContext.getInstance().getToken(),
                        currVideoInfoModel.getBroadcaster().getUid(),
                        new YueJianAppApiProtoHelper.ACIsFollowingReqCallback() {
                            @Override
                            public void onError(int errCode, String errMessage) {

                            }

                            @Override
                            public void onResponse(boolean isFollowing) {
                                YueJianAppUiUtils.setVisibility(ivFollow, isFollowing ? View.GONE : View.VISIBLE);
                            }
                        });
                videoView.prepareAsync();
                videoView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                mPlayingPosition = mCurrentPosition;

            } else { // 视频不是我的，并且有加锁，需要购买后才能播放
                String msg = String.format("观看此视频需要%s钻石", currVideoInfoModel.getPrice());
                YueJianAppDialogHelp.showDialog(
                        YueJianAppVChatShortVideoPlayerActivity.this,
                        msg,
                        new YueJianAppINomalDialog() {
                            @Override
                            public void cancelDialog(View v, Dialog d) {
                                d.dismiss();
                                mPlayingPosition = mCurrentPosition;
                            }

                            @Override
                            public void determineDialog(View v, Dialog d) {
                                buyShortVideo();
                                mPlayingPosition = mCurrentPosition;
                                d.dismiss();
                            }
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 购买短视频
     */
    private void buyShortVideo() {
        YueJianAppApiProtoHelper.sendACBuyVideoReq(
                YueJianAppVChatShortVideoPlayerActivity.this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(), currVideoInfoModel.getVideoId(),
                new YueJianAppApiProtoHelper.ACBuyVideoReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                        YueJianAppDialogHelp.showDialog(
                                YueJianAppVChatShortVideoPlayerActivity.this,
                                errMessage,
                                new YueJianAppINomalDialog() {
                                    @Override
                                    public void cancelDialog(View v, Dialog d) {
                                        d.dismiss();
                                    }

                                    @Override
                                    public void determineDialog(View v, Dialog d) {
                                        d.dismiss();
                                        YueJianAppDialogHelper.showRechargeDialogFragment(getSupportFragmentManager());
                                    }
                                });
                    }

                    @Override
                    public void onResponse() {
                        YueJianAppTLog.error("currentPosition:%s", mCurrentPosition);
                        mToast.setText("购买成功!");
                        mToast.show();

                        mVideoInfoModelList.get(mCurrentPosition).setHasLocked(false);
                        YueJianAppUiUtils.setVisibility(mViews.get(mCurrentPosition).findViewById(R.id.iv_play), View.GONE);
                        YueJianAppUiUtils.setVisibility(mViews.get(mCurrentPosition).findViewById(R.id.iv_lock), View.GONE);
                        videoView.prepareAsync();
                        View view = mViews.get(mCurrentPosition);
                        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.video_container);
                        frameLayout.addView(videoView);
                        mPlayingPosition = mCurrentPosition;
                    }
                });
    }

    /**
     * 广播接收者
     */
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(YueJianAppAppConfig.ACTION_SEND_GIFT);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(YueJianAppAppConfig.ACTION_SEND_GIFT)) {
                    Bundle bundle = intent.getBundleExtra("GIFT_BUNDLE");
                    YueJianAppACGiftModel giftModel = (YueJianAppACGiftModel) bundle.getSerializable("GIFT_MODEL");
                    YueJianAppACUserPublicInfoModel senderModel = (YueJianAppACUserPublicInfoModel) bundle.getSerializable("SENDER_MODEL");
                    YueJianAppACUserPublicInfoModel receiverModel = (YueJianAppACUserPublicInfoModel) bundle.getSerializable("RECEIVER_MODEL");
                    mShowGiftManager.addGift(giftModel, senderModel, receiverModel);
                    mShowGiftManager.showGift();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {

        }
    };

    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            if (videoView != null) {
                videoView.start();
            }
        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangeListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(
                IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
            if (width > height) {
                if (videoView != null) {
                    videoView.setRotateDegree(90);
                }
            }
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
            videoView.reload(currVideoInfoModel.getVideoUrl(), false, KSYMediaPlayer.KSYReloadMode.KSY_RELOAD_MODE_FAST);
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
                    if (videoView != null) {
                        videoView.reload(currVideoInfoModel.getVideoUrl(), false);
                    }
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
            YueJianAppTLog.info("IMediaPlayer.OnMessageListener");
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        if (videoView != null) {
            videoView.runInForeground();
//            videoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.runInBackground(false);
            videoView.pause();
            YueJianAppUiUtils.setVisibility(mViews.get(mCurrentPosition).findViewById(R.id.iv_play), View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stop();
            videoView = null;
        }
        unregisterReceiver(broadcastReceiver);
    }
}
