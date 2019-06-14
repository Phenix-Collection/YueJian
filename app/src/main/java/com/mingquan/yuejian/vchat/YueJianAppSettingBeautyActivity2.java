package com.mingquan.yuejian.vchat;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.agora.YueJianAppCameraRenderer;
import com.mingquan.yuejian.agora.YueJianAppEffectAndFilterSelectAdapter;
import com.mingquan.yuejian.agora.YueJianAppFURenderer;
import com.mingquan.yuejian.base.YueJianAppFullScreenModeActivity;
import com.mingquan.yuejian.utils.YueJianAppAppUtil;
import com.mingquan.yuejian.utils.YueJianAppNotchInScreenUtil;
import com.mingquan.yuejian.utils.YueJianAppSharedPreUtil;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class YueJianAppSettingBeautyActivity2 extends YueJianAppFullScreenModeActivity implements YueJianAppCameraRenderer.OnRendererStatusListener, SensorEventListener {

    @BindView(R.id.gl_surface_view2)
    GLSurfaceView glSurfaceView;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
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
    private YueJianAppCameraRenderer mCameraRenderer;
    public static String mFilterName = YueJianAppEffectAndFilterSelectAdapter.FILTERS_NAME[0];
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private YueJianAppFURenderer mFURenderer;

    public static float mFilterLevel = 1.0f; // 滤镜0~1 默认为1
    public static float mFaceBeautyColorLevel = 0.2f; //美白0~1 默认为1
    public static float mFaceBeautyBlurLevel = 6.0f; // 磨皮0~6 默认为6
    public static float mFaceBeautyALLBlurLevel = 1.0f; // 精准美肤0关闭 1开启，默认为1
    public static float mFaceBeautyCheekThin = 1.0f; // 瘦脸0~1 默认为0
    public static float mFaceBeautyEnlargeEye = 0.5f; // 大眼0~1 默认为0
    public static float mFaceBeautyRedLevel = 0.5f; // 红润0~1 默认为1
    public static int mFaceShape = 3;// 脸型 0：女神 1：网红 2：自然 3：默认 4：自定义（新版美型） SDK默认为 3
    public static float mFaceShapeLevel = 0.5f;// 美型程度 范围0~1 SDK默认为1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (YueJianAppNotchInScreenUtil.hasNotch(this)) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.yue_jian_app_activity_setting_beauty2);
        unbinder = ButterKnife.bind(this);
        glSurfaceView.setEGLContextClientVersion(2);
        mCameraRenderer = new YueJianAppCameraRenderer(this, glSurfaceView, this);
        glSurfaceView.setRenderer(mCameraRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //初始化FU相关 authpack 为证书文件
        mFURenderer = new YueJianAppFURenderer
                .Builder(this)
                .maxFaces(4)
                .inputTextureType(YueJianAppFURenderer.FU_ADM_FLAG_EXTERNAL_OES_TEXTURE)
                .createEGLContext(false)
                .needReadBackImage(false)
                .defaultEffect(null)
                .build();

        mFilterLevel = YueJianAppAppContext.getInstance().mFilterLevel;
        mFaceBeautyColorLevel = YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_color_level", 0.2f);
        mFaceBeautyBlurLevel = YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_blur_level", 6.0f);
        mFaceBeautyCheekThin = YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_cheek_thin", 1.0f);
        mFaceBeautyEnlargeEye = YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_enlarge_eye", 0.5f);
        mFaceBeautyRedLevel = YueJianAppSharedPreUtil.getFloat(getApplicationContext(), "face_beauty_red_level", 0.5f);
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

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri packageURI = Uri.parse("package:" + getPackageName());
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                startActivity(intent);
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
                Toast.makeText(YueJianAppSettingBeautyActivity2.this, "恢复默认成功！", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(YueJianAppSettingBeautyActivity2.this, "保存设置成功！", Toast.LENGTH_SHORT).show();
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
        mCameraRenderer.onCreate();
        mCameraRenderer.onResume();
//        int mCurrentCameraType = Camera.CameraInfo.CAMERA_FACING_FRONT;
//        boolean isOpen = mCameraRenderer.openCamera(mCurrentCameraType);
//        int cameraPermission = ActivityCompat.checkSelfPermission(this, "Manifest.permission.CAMERA");
//        YueJianAppTLog.info("setting beauty on resume is open:%s, cameraPermission:%s", isOpen, cameraPermission);
//        YueJianAppUiUtils.setVisibility(ivCamera, isOpen && cameraPermission == 0 ? View.GONE : View.VISIBLE);
        /*YueJianAppAppUtil.getInstance().checkPermissions(this, new String[]{Manifest.permission.CAMERA}, new YueJianAppAppUtil.IPermissionsResult() {
            @Override
            public void passPermissons() {
                mCameraRenderer.openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                YueJianAppUiUtils.setVisibility(ivCamera, View.GONE);
            }

            @Override
            public void forbitPermissons() {
                YueJianAppUiUtils.setVisibility(ivCamera, View.VISIBLE);
            }
        });*/
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        YueJianAppTLog.info("setting beauty on request permissions result");
        YueJianAppAppUtil.getInstance().onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        YueJianAppTLog.info("setting beauty onPause");
        mSensorManager.unregisterListener(this);
        mCameraRenderer.onPause();
        mCameraRenderer.onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YueJianAppTLog.info("setting beauty onDestroy");
        unbinder.unbind();
        mFURenderer.onSurfaceDestroyed();
    }

    // ---------------------------------- senderStatusListener method ------------------------------
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        YueJianAppTLog.info("setting beauty onSurfaceCreated");
        mFURenderer.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        YueJianAppTLog.info("setting beauty onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public int onDrawFrame(byte[] cameraNV21Byte, int cameraTextureId, int cameraWidth, int cameraHeight, float[] mtx, long timeStamp) {
        return mFURenderer.onDrawFrame(cameraNV21Byte, cameraTextureId, cameraWidth, cameraHeight);
    }

    @Override
    public void onSurfaceDestroy() {
    }

    @Override
    public void onCameraChange(int currentCameraType, int cameraOrientation) {
        mFURenderer.onCameraChange(currentCameraType, cameraOrientation);
    }

    // ------------------------------------senderStatusListener method------------------------------
    // ------------------------------------sensorEventListener method------------------------------
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (Math.abs(x) > 3 || Math.abs(y) > 3) {
                if (Math.abs(x) > Math.abs(y)) {
                    mFURenderer.setTrackOrientation(x > 0 ? 0 : 180);
                } else {
                    mFURenderer.setTrackOrientation(y > 0 ? 90 : 270);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // ------------------------------------sensorEventListener method------------------------------
}
