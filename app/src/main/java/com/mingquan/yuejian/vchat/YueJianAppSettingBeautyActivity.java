package com.mingquan.yuejian.vchat;

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
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
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
import com.mingquan.yuejian.agora.YueJianAppEffectAndFilterSelectAdapter;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.utils.YueJianAppAppUtil;
import com.mingquan.yuejian.utils.YueJianAppSharedPreUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.agora.rtc.mediaio.IVideoFrameConsumer;
import io.agora.rtc.mediaio.MediaIO;
import io.agora.rtc.mediaio.SurfaceTextureHelper;

public class YueJianAppSettingBeautyActivity extends YueJianAppFullScreenModeActivity implements Camera.PreviewCallback {

    @BindView(R.id.gl_surface_view)
    GLSurfaceView glSurfaceView;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.btn_save)
    Button btnSave; // 保存按钮
    @BindView(R.id.btn_default)
    Button btnDefault; // 默认
    @BindView(R.id.seek_bar_01)
    DiscreteSeekBar seekBarColorLevel; // 美白
    @BindView(R.id.seek_bar_02)
    DiscreteSeekBar seekBarBlur; // 磨皮
    @BindView(R.id.seek_bar_03)
    DiscreteSeekBar seekBarThinFace; // 瘦脸
    @BindView(R.id.seek_bar_04)
    DiscreteSeekBar seekBarBigEye; // 大眼
    @BindView(R.id.seek_bar_05)
    DiscreteSeekBar seekBarRedLevel; // 红润
    Unbinder unbinder;

    private Camera mCamera;
    private int mCurrentCameraType = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int mCameraOrientation;
    private int mCameraWidth = 1280;
    private int mCameraHeight = 720;
    private Context mContext;
    private GLRenderer mGLRenderer;
    private int mViewWidth;
    private int mViewHeight;

    private SurfaceTextureHelper mSurfaceTextureHelper;

    private static final int PREVIEW_BUFFER_COUNT = 3;
    private byte[][] previewCallbackBuffer;

    private byte[] mCameraNV21Byte;
    private byte[] mFuImgNV21Bytes;

    private int mFrameId = 0;

    private int mFaceBeautyItem = 0; // Face beauty
    private int mEffectItem = 0; // Effect/Sticky

    private static String mFilterName = YueJianAppEffectAndFilterSelectAdapter.FILTERS_NAME[0];

    private boolean isNeedEffectItem = true;
    private String mEffectFileName = YueJianAppEffectAndFilterSelectAdapter.EFFECT_ITEM_FILE_NAME[1];

    private TextureMovieEncoder mTextureMovieEncoder;
    private String mVideoFileName;

    private HandlerThread mCreateItemThread;
    private Handler mCreateItemHandler;

    private boolean isInPause = true;

    private boolean isInAvatarMode = false;
    private IVideoFrameConsumer mIVideoFrameConsumer;
    private boolean mVideoFrameConsumerReady;

    public float mFilterLevel = 1.0f; // 滤镜0~1 默认为1
    public float mFaceBeautyColorLevel = 0.2f; //美白0~1 默认为1
    public float mFaceBeautyBlurLevel = 6.0f; // 磨皮0~6 默认为6
    public float mFaceBeautyALLBlurLevel = 1.0f; // 精准美肤0关闭 1开启，默认为1
    public float mFaceBeautyCheekThin = 1.0f; // 瘦脸0~1 默认为0
    public float mFaceBeautyEnlargeEye = 0.5f; // 大眼0~1 默认为0
    public float mFaceBeautyRedLevel = 0.5f; // 红润0~1 默认为1
    public int mFaceShape = 3;// 脸型 0：女神 1：网红 2：自然 3：默认 4：自定义（新版美型） SDK默认为 3
    public float mFaceShapeLevel = 0.5f;// 美型程度 范围0~1 SDK默认为1

    private boolean hasCameraPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yue_jian_app_activity_setting_beauty);
        unbinder = ButterKnife.bind(this);
        mContext = getBaseContext();
        YueJianAppAppUtil.getInstance().checkPermissions(
                YueJianAppSettingBeautyActivity.this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                new YueJianAppAppUtil.IPermissionsResult() {
                    @Override
                    public void passPermissons() {
                        hasCameraPermission = true;
                        openCamera(mCurrentCameraType, mCameraWidth, mCameraHeight);
                    }

                    @Override
                    public void forbitPermissons() {
                        hasCameraPermission = false;
                        YueJianAppAppUtil.getInstance().showSystemPermissionsSettingDialog(
                                YueJianAppSettingBeautyActivity.this,
                                "需要开启摄像头、音频权限,手动设置？",
                                false);
                    }
                });
        mCreateItemThread = new HandlerThread("CreateItemThread");
        mCreateItemThread.start();
        mCreateItemHandler = new CreateItemHandler(mCreateItemThread.getLooper());

        glSurfaceView.setEGLContextClientVersion(2);
        mGLRenderer = new GLRenderer();
        glSurfaceView.setRenderer(mGLRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mFilterLevel = YueJianAppAppContext.getInstance().mFilterLevel;

        mFaceBeautyColorLevel = YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_color_level", 0.2f);
        mFaceBeautyBlurLevel = YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_blur_level", 6.0f);
        mFaceBeautyCheekThin = YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_cheek_thin", 1.0f);
        mFaceBeautyEnlargeEye = YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_enlarge_eye", 0.5f);
        mFaceBeautyRedLevel = YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_red_level", 0.5f);

        mFaceBeautyALLBlurLevel = YueJianAppAppContext.getInstance().mFaceBeautyALLBlurLevel;
        mFaceShape = YueJianAppAppContext.getInstance().mFaceShape;
        mFaceShapeLevel = YueJianAppAppContext.getInstance().mFaceShapeLevel;

        seekBarColorLevel.setProgress((int) (mFaceBeautyColorLevel * 100));
        seekBarBigEye.setProgress((int) (mFaceBeautyEnlargeEye * 100));
        seekBarBlur.setProgress((int) (mFaceBeautyBlurLevel * 100 / 6));
        seekBarRedLevel.setProgress((int) (mFaceBeautyRedLevel * 100));
        seekBarThinFace.setProgress((int) (mFaceBeautyCheekThin * 100));
        initEvents();
    }

    private void initEvents() {

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFaceBeautyColorLevel = 0.2f;
                mFaceBeautyBlurLevel = 6.0f;
                mFaceBeautyCheekThin = 1.0f;
                mFaceBeautyEnlargeEye = 0.5f;
                mFaceBeautyRedLevel = 0.5f;

                YueJianAppSharedPreUtil.put(getApplicationContext(), "face_beauty_color_level", 0.2f);
                YueJianAppSharedPreUtil.put(getApplicationContext(), "face_beauty_blur_level", 6.0f);
                YueJianAppSharedPreUtil.put(getApplicationContext(), "face_beauty_cheek_thin", 1.0f);
                YueJianAppSharedPreUtil.put(getApplicationContext(), "face_beauty_enlarge_eye", 0.5f);
                YueJianAppSharedPreUtil.put(getApplicationContext(), "face_beauty_red_level", 0.5f);

                seekBarColorLevel.setProgress((int) (mFaceBeautyColorLevel * 100));
                seekBarBigEye.setProgress((int) (mFaceBeautyEnlargeEye * 100));
                seekBarBlur.setProgress((int) (mFaceBeautyBlurLevel * 100 / 6));
                seekBarRedLevel.setProgress((int) (mFaceBeautyRedLevel * 100));
                seekBarThinFace.setProgress((int) (mFaceBeautyCheekThin * 100));
                Toast.makeText(YueJianAppSettingBeautyActivity.this, "恢复默认成功！", Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YueJianAppSharedPreUtil.put(getApplicationContext(), "face_beauty_color_level", mFaceBeautyColorLevel);
                YueJianAppSharedPreUtil.put(getApplicationContext(), "face_beauty_blur_level", mFaceBeautyBlurLevel);
                YueJianAppSharedPreUtil.put(getApplicationContext(), "face_beauty_cheek_thin", mFaceBeautyCheekThin);
                YueJianAppSharedPreUtil.put(getApplicationContext(), "face_beauty_enlarge_eye", mFaceBeautyEnlargeEye);
                YueJianAppSharedPreUtil.put(getApplicationContext(), "face_beauty_red_level", mFaceBeautyRedLevel);
                Toast.makeText(YueJianAppSettingBeautyActivity.this, "保存设置成功！", Toast.LENGTH_SHORT).show();
            }
        });

        seekBarColorLevel.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mFaceBeautyColorLevel = ((float) value) / 100;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        seekBarBigEye.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mFaceBeautyEnlargeEye = ((float) value) / 100;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        seekBarBlur.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {

            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mFaceBeautyBlurLevel = ((float) value) / 100 * 6;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        seekBarRedLevel.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mFaceBeautyRedLevel = ((float) value) / 100;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        seekBarThinFace.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mFaceBeautyCheekThin = ((float) value) / 100;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        if (hasCameraPermission) {
            openCamera(mCurrentCameraType, mCameraWidth, mCameraHeight);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        YueJianAppAppUtil.getInstance().onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCreateItemHandler.removeMessages(CreateItemHandler.HANDLE_CREATE_ITEM);
        releaseCamera();
        glSurfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                mGLRenderer.notifyPause();
                mGLRenderer.destroySurfaceTexture();

                mEffectItem = 0;
                mFaceBeautyItem = 0;
                faceunity.fuDestroyAllItems();
                isNeedEffectItem = true;
                faceunity.fuOnDeviceLost();
                mFrameId = 0;
            }
        });

        glSurfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mEffectFileName = YueJianAppEffectAndFilterSelectAdapter.EFFECT_ITEM_FILE_NAME[1];

        mCreateItemThread.quitSafely();
        mCreateItemThread = null;
        mCreateItemHandler = null;
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
        if (mCamera != null) {
            /*throw new RuntimeException("camera already initialized");*/
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
                    Toast.makeText(YueJianAppSettingBeautyActivity.this,
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
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        mCameraNV21Byte = data;
        mCamera.addCallbackBuffer(data);
        isInPause = false;
    }

    private int draw(byte[] cameraNV21Byte, byte[] fuImgNV21Bytes, int cameraTextureId, int cameraWidth, int cameraHeight, int frameId, int[] arrayItems, int currentCameraType) {
        boolean isOESTexture = true; // Tip: camera texture 类型是默认的是 OES_Texture 的，和 Texture2D 不同
        int flags = isOESTexture ? faceunity.FU_ADM_FLAG_EXTERNAL_OES_TEXTURE : 0;
        boolean isNeedReadBack = true; // 是否需要写回，如果是，则入参的 byte array 会被修改为带有 fu 特效的；支持写回自定义大小的内存数组中
        flags = isNeedReadBack ? flags | faceunity.FU_ADM_FLAG_ENABLE_READBACK : flags;
        if (isNeedReadBack) {
            if (fuImgNV21Bytes == null) {
                fuImgNV21Bytes = new byte[cameraNV21Byte.length];
            }
            System.arraycopy(cameraNV21Byte, 0, fuImgNV21Bytes, 0, cameraNV21Byte.length);
        } else {
            fuImgNV21Bytes = cameraNV21Byte;
        }
        flags |= currentCameraType == Camera.CameraInfo.CAMERA_FACING_FRONT ? 0 : faceunity.FU_ADM_FLAG_FLIP_X;

        mFuImgNV21Bytes = fuImgNV21Bytes;

            /*
             * 这里拿到 fu 处理过后的 texture，可以对这个 texture 做后续操作，如硬编、预览。
             */
        return faceunity.fuDualInputToTexture(fuImgNV21Bytes, cameraTextureId, flags,
                cameraWidth, cameraHeight, frameId, arrayItems);
    }

    private byte[] getFuImgNV21Bytes() {
        return mFuImgNV21Bytes;
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
            mFullScreenFUDisplay = new FullFrameRect(new Texture2dProgram(
                    Texture2dProgram.ProgramType.TEXTURE_2D));
            mCameraDisplay = new FullFrameRect(new Texture2dProgram(
                    Texture2dProgram.ProgramType.TEXTURE_EXT));
            mCameraTextureId = mCameraDisplay.createTextureObject();

            landmarksPoints = new LandmarksPoints(); // Can calculate and draw face landmarks when certificate is valid

            switchCameraSurfaceTexture();
            mFaceBeautyItem = YueJianAppAppContext.getInstance().mFaceBeautyItem;
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            mViewWidth = width;
            mViewHeight = height;
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            FPSUtil.fps();
            if (isInPause) {
                mFullScreenFUDisplay.drawFrame(fuTex, mtx);
                glSurfaceView.requestRender();
                return;
            }

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
                        YueJianAppTLog.error("system error : %s %s", systemError, faceunity.fuGetSystemErrorString(systemError));
                    }
                });
            }

            if (isNeedEffectItem) {
                isNeedEffectItem = false;
                mCreateItemHandler.sendMessage(Message.obtain(mCreateItemHandler, CreateItemHandler.HANDLE_CREATE_ITEM, mEffectFileName));
            }

            faceunity.fuItemSetParam(mFaceBeautyItem, "filter_level", mFilterLevel);
            faceunity.fuItemSetParam(mFaceBeautyItem, "color_level", mFaceBeautyColorLevel);
            faceunity.fuItemSetParam(mFaceBeautyItem, "blur_level", mFaceBeautyBlurLevel);
            faceunity.fuItemSetParam(mFaceBeautyItem, "skin_detect", mFaceBeautyALLBlurLevel);
            faceunity.fuItemSetParam(mFaceBeautyItem, "filter_name", mFilterName);
            faceunity.fuItemSetParam(mFaceBeautyItem, "cheek_thinning", mFaceBeautyCheekThin);
            faceunity.fuItemSetParam(mFaceBeautyItem, "eye_enlarging", mFaceBeautyEnlargeEye);
            faceunity.fuItemSetParam(mFaceBeautyItem, "face_shape", mFaceShape);
            faceunity.fuItemSetParam(mFaceBeautyItem, "face_shape_level", mFaceShapeLevel);
            faceunity.fuItemSetParam(mFaceBeautyItem, "red_level", mFaceBeautyRedLevel);

            if (mCameraNV21Byte == null || mCameraNV21Byte.length == 0) {
                YueJianAppTLog.error("camera nv21 bytes null");
                glSurfaceView.requestRender();
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

            glSurfaceView.requestRender();

            if (mVideoFrameConsumerReady) {
                mIVideoFrameConsumer.consumeByteArrayFrame(
                        getFuImgNV21Bytes(),
                        MediaIO.PixelFormat.NV21.intValue(),
                        mCameraWidth,
                        mCameraHeight,
                        mCameraOrientation,
                        System.currentTimeMillis());
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
                            glSurfaceView.queueEvent(new Runnable() {
                                @Override
                                public void run() {
                                    faceunity.fuItemSetParam(newEffectItem, "isAndroid", 1.0);
                                    faceunity.fuItemSetParam(newEffectItem, "rotationAngle", 360 - mCameraOrientation);
                                }
                            });
                        }
                        glSurfaceView.queueEvent(new Runnable() {
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
}
