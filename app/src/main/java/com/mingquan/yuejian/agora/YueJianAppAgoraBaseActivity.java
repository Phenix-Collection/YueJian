package com.mingquan.yuejian.agora;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mingquan.yuejian.YueJianAppAppContext;
import io.agora.rtc.RtcEngine;

public abstract class YueJianAppAgoraBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YueJianAppAppContext) getApplication()).initWorkerThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deInitUIandEvent();
    }

    protected abstract void initUIandEvent();

    protected abstract void deInitUIandEvent();

    protected RtcEngine rtcEngine() {
        return ((YueJianAppAppContext) getApplication()).getWorkerThread().getRtcEngine();
    }

    protected final YueJianAppWorkerThread worker() {
        return ((YueJianAppAppContext) getApplication()).getWorkerThread();
    }

    protected final YueJianAppEngineConfig config() {
        return ((YueJianAppAppContext) getApplication()).getWorkerThread().getEngineConfig();
    }

    protected final YueJianAppMyEngineEventHandler event() {
        return ((YueJianAppAppContext) getApplication()).getWorkerThread().eventHandler();
    }
}
