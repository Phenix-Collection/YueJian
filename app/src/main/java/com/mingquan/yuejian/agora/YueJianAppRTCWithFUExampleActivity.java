package com.mingquan.yuejian.agora;

import android.Manifest;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.faceunity.encoder.TextureMovieEncoder;
import com.faceunity.gles.FullFrameRect;
import com.faceunity.gles.LandmarksPoints;
import com.faceunity.gles.Texture2dProgram;
import com.faceunity.utils.CameraUtils;
import com.faceunity.utils.FPSUtil;
import com.faceunity.utils.MiscUtil;
import com.faceunity.wrapper.faceunity;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.utils.YueJianAppSharedPreUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.mediaio.IVideoFrameConsumer;
import io.agora.rtc.mediaio.IVideoSink;
import io.agora.rtc.mediaio.IVideoSource;
import io.agora.rtc.mediaio.MediaIO;
import io.agora.rtc.video.VideoCanvas;

/**
 * This activity demonstrates how to make FU and Agora RTC SDK work together
 * <p>
 */

@SuppressWarnings("deprecation")
public abstract class YueJianAppRTCWithFUExampleActivity extends YueJianAppFUBaseUIActivity implements Camera.PreviewCallback, YueJianAppIAGEventHandler {
    protected abstract int draw(byte[] cameraNV21Byte, byte[] fuImgNV21Bytes, int cameraTextureId, int cameraWidth, int cameraHeight, int frameId, int[] ints, int currentCameraType);

    protected abstract byte[] getFuImgNV21Bytes();

    private Context mContext;
    private GLRenderer mGLRenderer;
    private int mViewWidth;
    private int mViewHeight;

    private Camera mCamera;
    private int mCurrentCameraType = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int mCameraOrientation;
    private int mCameraWidth = 1280;
    private int mCameraHeight = 720;

    private static final int PREVIEW_BUFFER_COUNT = 3;
    private byte[][] previewCallbackBuffer;

    private byte[] mCameraNV21Byte;
    private byte[] mFuImgNV21Bytes;

    private int mFrameId = 0;

    private int mFaceBeautyItem = 0; // Face beauty
    private int mEffectItem = 0; // Effect/Sticky

    private String mFilterName = YueJianAppEffectAndFilterSelectAdapter.FILTERS_NAME[0];

    private TextureMovieEncoder mTextureMovieEncoder;
    private String mVideoFileName;

    private HandlerThread mCreateItemThread;
    private Handler mCreateItemHandler;

    private boolean isInPause = true;

    private boolean isInAvatarMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        YueJianAppTLog.info("onCreate");
        mContext = getBaseContext();
        super.onCreate(savedInstanceState);

        mCreateItemThread = new HandlerThread("CreateItemThread");
        mCreateItemThread.start();
        mCreateItemHandler = new CreateItemHandler(mCreateItemThread.getLooper());
        initUIandEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        YueJianAppTLog.info("onResume mCurrentCameraType:%s", mCurrentCameraType);
        worker().configEngine(io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER
                , YueJianAppAgora_Constants.VIDEO_PROFILES[YueJianAppAgora_Constants.DEFAULT_PROFILE_IDX]);
        openCamera(mCurrentCameraType, mCameraWidth, mCameraHeight);
        localVideoView.onResume();
        setConfig();
    }

    @Override
    protected void onPause() {
        YueJianAppTLog.info("onPause");

        super.onPause();

        mCreateItemHandler.removeMessages(CreateItemHandler.HANDLE_CREATE_ITEM);

        releaseCamera();

        localVideoView.queueEvent(new Runnable() {
            @Override
            public void run() {
                mGLRenderer.notifyPause();
                mGLRenderer.destroySurfaceTexture();

                mEffectItem = 0;
                mFaceBeautyItem = 0;
                // Note: Never use a destroyed item
                faceunity.fuDestroyAllItems();
                faceunity.fuOnDeviceLost();
                mFrameId = 0;
            }
        });

        localVideoView.onPause();
    }

    private void setConfig() {

        IVideoSource source = new IVideoSource() {

            @Override
            public boolean onInitialize(IVideoFrameConsumer iVideoFrameConsumer) {
                YueJianAppRTCWithFUExampleActivity.this.mIVideoFrameConsumer = iVideoFrameConsumer;
                return true;
            }

            @Override
            public boolean onStart() {
                YueJianAppRTCWithFUExampleActivity.this.mVideoFrameConsumerReady = true;
                return true;
            }

            @Override
            public void onStop() {
                YueJianAppRTCWithFUExampleActivity.this.mVideoFrameConsumerReady = false;
            }

            @Override
            public void onDispose() {
                YueJianAppRTCWithFUExampleActivity.this.mVideoFrameConsumerReady = false;
            }

            @Override
            public int getBufferType() {
                // Different PixelFormat should use different BufferType
                // If you choose TEXTURE_2D/TEXTURE_OES, you should use BufferType.TEXTURE
                return MediaIO.BufferType.BYTE_ARRAY.intValue();
            }
        };
        worker().setVideoSource(source);

        worker().setLocalVideoRenderer(new IVideoSink() {
            @Override
            public boolean onInitialize() {
                return true;
            }

            @Override
            public boolean onStart() {
                return true;
            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDispose() {

            }

            @Override
            public long getEGLContextHandle() {
                return 0;
            }

            @Override
            public int getBufferType() {
                return 0;
            }

            @Override
            public int getPixelFormat() {
                return 0;
            }

            @Override
            public void consumeByteBufferFrame(ByteBuffer byteBuffer, int i, int i1, int i2, int i3, long l) {

            }

            @Override
            public void consumeByteArrayFrame(byte[] bytes, int i, int i1, int i2, int i3, long l) {

            }

            @Override
            public void consumeTextureFrame(int i, int i1, int i2, int i3, int i4, long l, float[] floats) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCreateItemThread.quitSafely();
        mCreateItemThread = null;
        mCreateItemHandler = null;
    }

    private IVideoFrameConsumer mIVideoFrameConsumer;
    private boolean mVideoFrameConsumerReady;

    @Override
    protected void initUIandEvent() {
//        container.setOnTouchListener(this);// 添加小窗口触摸事件，实现拖拽效果

//        localVideoView = (GLSurfaceView) findViewById(R.id.local_video_view);
//        localVideoView.setZOrderMediaOverlay(true); // 显示在前面
        localVideoView.setEGLContextClientVersion(2);
        mGLRenderer = new GLRenderer();
        localVideoView.setRenderer(mGLRenderer);
        localVideoView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

//        worker().configEngine(io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER
//                , YueJianAppAgora_Constants.VIDEO_PROFILES[YueJianAppAgora_Constants.DEFAULT_PROFILE_IDX]);

//        GLSurfaceView container = (GLSurfaceView) findViewById(R.id.glsv);

        event().addEventHandler(this);
//        worker().joinChannel(mLiveId, config().mUid);
    }

    @Override
    protected void deInitUIandEvent() {
        event().removeEventHandler(this);
//        worker().leaveChannel(config().mChannel);
    }

    private void setupRemoteVideo(int uid) {
        if (remoteContainer == null) {
            remoteContainer = (RelativeLayout) findViewById(R.id.remote_video_view_container);
        }

        if (remoteContainer.getChildCount() >= 1) {
            return;
        }

        remoteSurfaceView = RtcEngine.CreateRendererView(getBaseContext());
        rtcEngine().setupRemoteVideo(new VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));
        remoteSurfaceView.setTag(uid); // for mark purpose
        remoteSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocalTop();
            }
        });
        localVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRemoteTop();
            }
        });
        // 主播显示自己全屏
        if (YueJianAppAppContext.getInstance().getPrivateInfoModel() != null &&
                YueJianAppAppContext.getInstance().getPrivateInfoModel().getAuthStatus() == YueJianAppApiProtoHelper.AUTH_STATUS_CERTIFIED) {
            setRemoteTop();
        } else {
            setLocalTop();
        }

        // 发送开始连麦的api，以开始计费
        YueJianAppApiProtoHelper.sendACVipChatStartReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                mLiveId,
                new YueJianAppApiProtoHelper.ACVipChatStartReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        Toast.makeText(mContext, errMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse() {

                    }
                });
    }

    private void onRemoteUserLeft() {
        if (remoteContainer == null) {
            remoteContainer = (RelativeLayout) findViewById(R.id.remote_video_view_container);
        }
        remoteContainer.removeAllViews();

        sendQuitVipChat();
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        YueJianAppTLog.info("on first remote video decoded");
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        YueJianAppTLog.info("on join channel success");
        isRemoteJoined = true;
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onRemoteUserLeft();
            }
        });
    }

    @Override
    public void onUserJoined(final int uid, int elapsed) {
        YueJianAppTLog.info("on user joined");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRemoteJoined = true;
                setupRemoteVideo(uid);
            }
        });
    }

    @Override
    public void onTokenPrivilegeWillExpire(String token) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateToken();
            }
        });
    }

    /**
     * 到token快过期时重新获取token
     */
    private void updateToken() {
        YueJianAppApiProtoHelper.sendACFetchAgoraTokenReq(
                this,
                YueJianAppAppContext.getInstance().getLoginUid(),
                YueJianAppAppContext.getInstance().getToken(),
                mLiveId,
                new YueJianAppApiProtoHelper.ACFetchAgoraTokenReqCallback() {
                    @Override
                    public void onError(int errCode, String errMessage) {
                        YueJianAppTLog.error(errMessage);
                    }

                    @Override
                    public void onResponse(String token) {
                        rtcEngine().renewToken(token);
                    }
                });
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        mCameraNV21Byte = data;
        mCamera.addCallbackBuffer(data);
        isInPause = false;
    }

    class GLRenderer implements GLSurfaceView.Renderer {

        FullFrameRect mFullScreenFUDisplay;
        FullFrameRect mCameraDisplay;

        int mCameraTextureId;
        SurfaceTexture mCameraSurfaceTexture;

        int faceTrackingStatus = 0;
        int systemErrorStatus = 0; // success number
        float[] isCalibrating = new float[1];

        LandmarksPoints landmarksPoints;
        float[] landmarksData = new float[150];
        float[] expressionData = new float[46];
        float[] rotationData = new float[4];
        float[] pupilPosData = new float[2];
        float[] rotationModeData = new float[1];

        int fuTex;
        final float[] mtx = new float[16];

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            YueJianAppTLog.info("onSurfaceCreated fu version :%s", faceunity.fuGetVersion());

            mFullScreenFUDisplay = new FullFrameRect(new Texture2dProgram(
                    Texture2dProgram.ProgramType.TEXTURE_2D));
            mCameraDisplay = new FullFrameRect(new Texture2dProgram(
                    Texture2dProgram.ProgramType.TEXTURE_EXT));
            mCameraTextureId = mCameraDisplay.createTextureObject();

            landmarksPoints = new LandmarksPoints(); // Can calculate and draw face landmarks when certificate is valid

            switchCameraSurfaceTexture();
            YueJianAppAppContext.getInstance().initFuRenderer();
            mFaceBeautyItem = YueJianAppAppContext.getInstance().mFaceBeautyItem;
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            YueJianAppTLog.info("onSurfaceChanged width:%s, height:%s", width, height);
            GLES20.glViewport(0, 0, width, height);
            mViewWidth = width;
            mViewHeight = height;
        }

        @Override
        public void onDrawFrame(GL10 gl) {

            FPSUtil.fps();

            if (isInPause) {
                mFullScreenFUDisplay.drawFrame(fuTex, mtx);
                localVideoView.requestRender();
                return;
            }

            /**
             * Update texture when Camera data available
             */
            try {
                mCameraSurfaceTexture.updateTexImage();
                mCameraSurfaceTexture.getTransformMatrix(mtx);
            } catch (Exception e) {
                e.printStackTrace();
            }

            final int isTracking = faceunity.fuIsTracking();
            if (isTracking != faceTrackingStatus) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isTracking == 0) {
                            YueJianAppTLog.info("未检测到人脸");
                            Arrays.fill(landmarksData, 0);
                        }
                    }
                });
                faceTrackingStatus = isTracking;
            }

            final int systemError = faceunity.fuGetSystemError();
            if (systemError != systemErrorStatus) {
                systemErrorStatus = systemError;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        YueJianAppTLog.error("system error : %s", systemError + " " + faceunity.fuGetSystemErrorString(systemError));
                    }
                });
            }
            if (setMysteryMode && !mUserInfo.getIsBroadcaster()) {
                YueJianAppTLog.info("设置显示马赛克贴纸");
                setMysteryMode = false;
                mCreateItemHandler.sendMessage(Message.obtain(mCreateItemHandler, CreateItemHandler.HANDLE_CREATE_ITEM, mEffectFileName));
            }

            faceunity.fuItemSetParam(mFaceBeautyItem, "color_level", YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_color_level", 0.2f));
            faceunity.fuItemSetParam(mFaceBeautyItem, "blur_level", YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_blur_level", 6.0f));
            faceunity.fuItemSetParam(mFaceBeautyItem, "cheek_thinning", YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_cheek_thin", 1.0f));
            faceunity.fuItemSetParam(mFaceBeautyItem, "eye_enlarging", YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_enlarge_eye", 0.5f));
            faceunity.fuItemSetParam(mFaceBeautyItem, "red_level", YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_red_level", 0.5f));
            faceunity.fuItemSetParam(mFaceBeautyItem, "filter_level", YueJianAppAppContext.getInstance().mFilterLevel);
            faceunity.fuItemSetParam(mFaceBeautyItem, "skin_detect", YueJianAppAppContext.getInstance().mFaceBeautyALLBlurLevel);
            faceunity.fuItemSetParam(mFaceBeautyItem, "filter_name", mFilterName);
            faceunity.fuItemSetParam(mFaceBeautyItem, "face_shape", YueJianAppAppContext.getInstance().mFaceShape);
            faceunity.fuItemSetParam(mFaceBeautyItem, "face_shape_level", YueJianAppAppContext.getInstance().mFaceShapeLevel);

            if (mCameraNV21Byte == null || mCameraNV21Byte.length == 0) {
                YueJianAppTLog.error("camera nv21 bytes null");
                localVideoView.requestRender();
                return;
            }

            if (isInAvatarMode) {
                fuTex = drawAvatar();
            } else {
                fuTex = draw(
                        mCameraNV21Byte,
                        mFuImgNV21Bytes,
                        mCameraTextureId,
                        mCameraWidth,
                        mCameraHeight,
                        mFrameId++,
                        new int[]{mFaceBeautyItem, mEffectItem}, mCurrentCameraType
                );
            }

            mFullScreenFUDisplay.drawFrame(fuTex, mtx);

            /**
             * Draw preview from camera and landmarks for Avatar sticky/effect
             **/
            if (isInAvatarMode) {
                int[] originalViewport = new int[4];
                GLES20.glGetIntegerv(GLES20.GL_VIEWPORT, originalViewport, 0);
                GLES20.glViewport(0, mViewHeight * 2 / 3, mViewWidth / 3, mViewHeight / 3);
                mCameraDisplay.drawFrame(mCameraTextureId, mtx);
                landmarksPoints.draw();
                GLES20.glViewport(originalViewport[0], originalViewport[1], originalViewport[2], originalViewport[3]);
            }

            final float[] isCalibratingTmp = new float[1];
            faceunity.fuGetFaceInfo(0, "is_calibrating", isCalibratingTmp);
            if (isCalibrating[0] != isCalibratingTmp[0]) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ((isCalibrating[0] = isCalibratingTmp[0]) > 0 && YueJianAppEffectAndFilterSelectAdapter.EFFECT_ITEM_FILE_NAME[6].equals(mEffectFileName)) {
                            YueJianAppTLog.info(String.valueOf(R.string.expression_calibrating));
//                            isCalibratingText.postDelayed(mCalibratingRunnable, 500);
                        } else {
//                            isCalibratingText.removeCallbacks(mCalibratingRunnable);
//                            isCalibratingText.setVisibility(View.GONE);
                        }
                    }
                });
            }

            if (mTextureMovieEncoder != null && mTextureMovieEncoder.checkRecordingStatus(TextureMovieEncoder.START_RECORDING)) {
                mVideoFileName = MiscUtil.createFileName() + "_camera.mp4";
                File outFile = new File(mVideoFileName);
                mTextureMovieEncoder.startRecording(new TextureMovieEncoder.EncoderConfig(
                        outFile, mCameraHeight, mCameraWidth,
                        3000000, EGL14.eglGetCurrentContext(), mCameraSurfaceTexture.getTimestamp()
                ));
                mTextureMovieEncoder.setTextureId(mFullScreenFUDisplay, fuTex, mtx);
                // forbid click until start or stop success
                mTextureMovieEncoder.setOnEncoderStatusUpdateListener(new TextureMovieEncoder.OnEncoderStatusUpdateListener() {
                    @Override
                    public void onStartSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                YueJianAppTLog.info("start encoder success");
//                                mRecordingBtn.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onStopSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                YueJianAppTLog.info("stop encoder success");
//                                mRecordingBtn.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "video file saved to "
                                + mVideoFileName, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (mTextureMovieEncoder != null && mTextureMovieEncoder.checkRecordingStatus(TextureMovieEncoder.IN_RECORDING)) {
                mTextureMovieEncoder.setTextureId(mFullScreenFUDisplay, fuTex, mtx);
                mTextureMovieEncoder.frameAvailable(mCameraSurfaceTexture);
            }

            localVideoView.requestRender();
            if (mVideoFrameConsumerReady) {
                // If data input type is OES Texture(always from camera)
                // mIVideoFrameConsumer.consumeTextureFrame(mCameraTextureId, MediaIO.PixelFormat.TEXTURE_OES.intValue(), mCameraWidth, mCameraHeight, 270, System.currentTimeMillis(), transformMatrix);

                // If data input type is Texture2D(always processed by face beatification sdk)
                // mIVideoFrameConsumer.consumeTextureFrame(fuTex, MediaIO.PixelFormat.TEXTURE_2D.intValue(), mCameraWidth, mCameraHeight, 270, System.currentTimeMillis(), transformMatrix);

                // If data input type is raw data(byte array, may processed by face beatification sdk)
                mIVideoFrameConsumer.consumeByteArrayFrame(getFuImgNV21Bytes(), MediaIO.PixelFormat.NV21.intValue(), mCameraWidth, mCameraHeight, mCameraOrientation, System.currentTimeMillis());

                // If data input type is raw data(byte array, may from camera)
                // mIVideoFrameConsumer.consumeByteArrayFrame(mCameraNV21Byte, MediaIO.PixelFormat.NV21.intValue(), mCameraWidth, mCameraHeight, 90, System.currentTimeMillis());
            }
        }

        int drawAvatar() {
            faceunity.fuTrackFace(mCameraNV21Byte, 0, mCameraWidth, mCameraHeight);

            /**
             * landmarks
             */
            Arrays.fill(landmarksData, 0.0f);
            faceunity.fuGetFaceInfo(0, "landmarks", landmarksData);
            if (landmarksPoints != null) {
                landmarksPoints.refresh(landmarksData, mCameraWidth, mCameraHeight, mCameraOrientation, mCurrentCameraType);
            }

            /**
             *rotation
             */
            Arrays.fill(rotationData, 0.0f);
            faceunity.fuGetFaceInfo(0, "rotation", rotationData);
            /**
             * expression
             */
            Arrays.fill(expressionData, 0.0f);
            faceunity.fuGetFaceInfo(0, "expression", expressionData);

            /**
             * pupil pos
             */
            Arrays.fill(pupilPosData, 0.0f);
            faceunity.fuGetFaceInfo(0, "pupil_pos", pupilPosData);

            /**
             * rotation mode
             */
            Arrays.fill(rotationModeData, 0.0f);
            faceunity.fuGetFaceInfo(0, "rotation_mode", rotationModeData);

            int isTracking = faceunity.fuIsTracking();

            // rotation is a 4-element unit, if not available, use 1,0,0,0
            if (isTracking <= 0) {
                rotationData[3] = 1.0f;
            }

            /**
             * adjust rotation mode
             */
            if (isTracking <= 0) {
                rotationModeData[0] = (360 - mCameraOrientation) / 90;
            }

            return faceunity.fuAvatarToTexture(pupilPosData,
                    expressionData,
                    rotationData,
                    rotationModeData,
                    /*flags*/0,
                    mCameraWidth,
                    mCameraHeight,
                    mFrameId++,
                    new int[]{mEffectItem},
                    isTracking);
        }

        public void switchCameraSurfaceTexture() {
            YueJianAppTLog.info("switchCameraSurfaceTexture mCameraSurfaceTexture:%s, mCameraTextureId:%s, mCamera:%s", mCameraSurfaceTexture, mCameraTextureId, mCamera);
            if (mCameraSurfaceTexture != null) {
                faceunity.fuOnCameraChange();
                destroySurfaceTexture();
            }

            if (mCameraTextureId == 0 || mCamera == null) {
                return;
            }

            mCameraSurfaceTexture = new SurfaceTexture(mCameraTextureId);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handleCameraStartPreview(mCameraSurfaceTexture);
                }
            });
        }

        public void notifyPause() {
            faceTrackingStatus = 0;

            if (mFullScreenFUDisplay != null) {
                mFullScreenFUDisplay.release(false);
                mFullScreenFUDisplay = null;
            }

            if (mCameraDisplay != null) {
                mCameraDisplay.release(false);
                mCameraDisplay = null;
            }
        }

        public void destroySurfaceTexture() {
            if (mCameraSurfaceTexture != null) {
                mCameraSurfaceTexture.release();
                mCameraSurfaceTexture = null;
            }
        }
    }

    class CreateItemHandler extends Handler {

        static final int HANDLE_CREATE_ITEM = 1;

        CreateItemHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_CREATE_ITEM:
                    try {
                        final String effectFileName = (String) msg.obj;
                        final int newEffectItem;
                        if (effectFileName.equals("none")) {
                            newEffectItem = 0;
                        } else {
                            InputStream is = mContext.getAssets().open(effectFileName);
                            byte[] itemData = new byte[is.available()];
                            int len = is.read(itemData);
                            YueJianAppTLog.info("effect len:%s", len);
                            is.close();
                            newEffectItem = faceunity.fuCreateItemFromPackage(itemData);
                            localVideoView.queueEvent(new Runnable() {
                                @Override
                                public void run() {
                                    faceunity.fuItemSetParam(newEffectItem, "isAndroid", 1.0);
                                    faceunity.fuItemSetParam(newEffectItem, "rotationAngle", 360 - mCameraOrientation);
                                }
                            });
                        }
                        localVideoView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                if (mEffectItem != 0 && mEffectItem != newEffectItem) {
                                    faceunity.fuDestroyItem(mEffectItem);
                                }
                                isInAvatarMode = Arrays.asList(YueJianAppEffectAndFilterSelectAdapter.AVATAR_EFFECT).contains(effectFileName);
                                mEffectItem = newEffectItem;
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    /**
     * 切换摄像头
     *
     * @param cameraType
     * @param desiredWidth
     * @param desiredHeight
     */
    @SuppressWarnings("deprecation")
    private void openCamera(int cameraType, int desiredWidth, int desiredHeight) {
        try {
            YueJianAppTLog.info("openCamera cameraType:%s", cameraType);

            if (mCamera != null) {
                YueJianAppTLog.error("camera already initialized");
                return;
            }

            Camera.CameraInfo info = new Camera.CameraInfo();
            int cameraId = 0;
            int numCameras = Camera.getNumberOfCameras();

            for (int camIdx = 0; camIdx < numCameras; camIdx++) {
                Camera.getCameraInfo(camIdx, info);
                if (info.facing == cameraType) {
                    cameraId = camIdx;
                    mCamera = Camera.open(camIdx);
                    mCurrentCameraType = cameraType;
                    break;
                }
            }

            if (mCamera == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(YueJianAppRTCWithFUExampleActivity.this,
                                "Open Camera Failed! Make sure it is not locked!", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
                throw new RuntimeException("unable to open camera");
            }
            mCameraOrientation = CameraUtils.getCameraOrientation(cameraId);
            CameraUtils.setCameraDisplayOrientation(this, cameraId, mCamera);

            Camera.Parameters parameters = mCamera.getParameters();

            CameraUtils.setFocusModes(parameters);

            int[] size = CameraUtils.choosePreviewSize(parameters, desiredWidth, desiredHeight);
            mCameraWidth = size[0];
            mCameraHeight = size[1];

            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 1000);
            YueJianAppTLog.error("开启摄像头失败，可能是没有权限，或者被别的app占用");
        }
    }

    /**
     * set preview and start preview after the surface created
     */
    private void handleCameraStartPreview(SurfaceTexture surfaceTexture) {
        try {
            if (previewCallbackBuffer == null) {
                previewCallbackBuffer = new byte[PREVIEW_BUFFER_COUNT][mCameraWidth * mCameraHeight * 3 / 2];
            }
            mCamera.setPreviewCallbackWithBuffer(this);
            for (int i = 0; i < PREVIEW_BUFFER_COUNT; i++) {
                mCamera.addCallbackBuffer(previewCallbackBuffer[i]);
            }

            mCamera.setPreviewTexture(surfaceTexture);

            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseCamera() {
        YueJianAppTLog.info("release camera");
        isInPause = true;

        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.setPreviewTexture(null);
                mCamera.setPreviewCallbackWithBuffer(null);
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        isInPause = true;
    }

    @Override
    protected void onCameraChange() {
        if (isInPause) {
            return;
        }

        YueJianAppTLog.info("onCameraChange:%s", mCurrentCameraType);

        releaseCamera();

        mCameraNV21Byte = null;
        mFrameId = 0;

        if (mCurrentCameraType == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            openCamera(Camera.CameraInfo.CAMERA_FACING_BACK, mCameraWidth, mCameraHeight);
        } else {
            openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT, mCameraWidth, mCameraHeight);
        }

        localVideoView.queueEvent(new Runnable() {
            @Override
            public void run() {
                mGLRenderer.switchCameraSurfaceTexture();
                faceunity.fuItemSetParam(mEffectItem, "isAndroid", 1.0);
                faceunity.fuItemSetParam(mEffectItem, "rotationAngle", 360 - mCameraOrientation);
            }
        });
    }
}
