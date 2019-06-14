package com.mingquan.yuejian.ui.other;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.getsentry.raven.event.Event;
import com.google.gson.Gson;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.bean.YueJianAppChatBean;
import com.mingquan.yuejian.bean.YueJianAppSendGiftBean;
import com.mingquan.yuejian.interf.YueJianAppIChatServer;
import com.mingquan.yuejian.interf.YueJianAppISocketCommon;
import com.mingquan.yuejian.proto.YueJianAppChatProtoBuilder;
import com.mingquan.yuejian.utils.YueJianAppRavenUtils;
import com.mingquan.yuejian.utils.YueJianAppTDevice;
import com.mingquan.yuejian.utils.YueJianAppTLog;
import com.mingquan.yuejian.utils.YueJianAppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.net.URL;

import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * 直播间业务逻辑处理
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class YueJianAppChatServer {
    public Socket mSocket;
    private Context context;
    private int showid;
    private String mUid;
    private String mToken;

    private YueJianAppIChatServer mChatServer;
    private static final int SYSTEMNOT = 1;
    private Gson gson;

    public YueJianAppChatServer(YueJianAppIChatServer chatServerInterface, Context context, int showid, String url) throws URISyntaxException {
        this.mChatServer = chatServerInterface;
        this.context = context;
        this.showid = showid;
        gson = new Gson();
        mSocket = YueJianAppAppContext.getInstance().getSocket(url);
    }

    //服务器连接结果监听
    private Emitter.Listener onConn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args[0].toString().equals("ok")) {
                YueJianAppTLog.info("socket connect successful ");
                mChatServer.onConnect(true);
            } else {
                mChatServer.onConnect(false);
            }
        }
    };

    //服务器连接关闭监听
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            YueJianAppTLog.warn("socket disconnect ");
        }
    };

    //服务器连接失败监听
    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String str = "";
            if (args != null) {
                str = String.valueOf(args[0]);
            }
            YueJianAppTLog.error("socket onError>>>  str:%s", str);
        }
    };

    private Emitter.Listener onReconnecting = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            YueJianAppTLog.warn("onReconnecting>>>>>>>>>>>>>>>>>>>");
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            YueJianAppTLog.info("onConnect>>>>>>>>>>>>>>>>>>>");
            try {
                YueJianAppChatProtoBuilder.sendCCLoginRpt(mSocket, mUid, mToken);
            } catch (Exception e) {
                e.printStackTrace();
                YueJianAppRavenUtils.captureMessage(
                        context, e, YueJianAppRavenUtils.ERROR, YueJianAppRavenUtils.DEBUG, Event.Level.ERROR, null);
            }
        }
    };

    private Emitter.Listener onReconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            YueJianAppTLog.info("onReconnect>>>>>>>>>>>>>>>>>>>");
        }
    };

    //服务器消息监听
    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                String res = args[0].toString();
                if (res.isEmpty() || YueJianAppUtils.isNumeric(res)) {
                    return;
                }
                JSONObject resJson = new JSONObject(res);
                JSONArray msgArrayJson = resJson.getJSONArray(YueJianAppISocketCommon.KEY_MSG);
                JSONObject contentJson = msgArrayJson.getJSONObject(0);
                int msgType = contentJson.has("msgtype") ? contentJson.getInt("msgtype") : 0;
                int action = contentJson.has("action") ? contentJson.getInt("action") : 0;
                switch (msgType) {
                    case SYSTEMNOT: //系统
                        if (action == 0) { // sendgift
                            YueJianAppChatBean c = new YueJianAppChatBean();
                            if (contentJson.has("uid")) {
                                c.setId(Integer.parseInt(contentJson.getString("uid")));
                            }
                            if (contentJson.has("usign")) {
                                c.setSignature(contentJson.getString("usign"));
                            }
                            if (contentJson.has("level")) {
                                c.setLevel(contentJson.getInt("level"));
                            }
                            if (contentJson.has("uname")) {
                                c.setUser_nicename(contentJson.getString("uname").trim());
                            }
                            if (contentJson.has("provinceBean")) {
                                c.setCity(contentJson.getString("provinceBean"));
                            }
                            if (contentJson.has("sex")) {
                                c.setSex(contentJson.getInt("sex"));
                            }
                            if (contentJson.has("uhead")) {
                                c.setAvatar(contentJson.getString("uhead"));
                            }
                            String evensend = "y";
                            if (contentJson.has("evensend")) {
                                evensend = contentJson.getString("evensend");
                            }

                            String broadCastVotes = "0";
                            JSONObject ob = contentJson.getJSONObject("ct").put("evensend", evensend);
                            if (ob.has("broadcast_votes")) {
                                broadCastVotes = ob.getString("broadcast_votes");
                            }
                            YueJianAppSendGiftBean mSendGiftInfo = gson.fromJson(
                                    contentJson.getJSONObject("ct").toString(), YueJianAppSendGiftBean.class); // gift info
                            String uname = c.getUser_nicename();
                            SpannableStringBuilder msg = new SpannableStringBuilder("我送了"
                                    + mSendGiftInfo.getGiftcount() + "个" + mSendGiftInfo.getGiftname() + " ");
                            SpannableStringBuilder name = new SpannableStringBuilder(uname);

                            Drawable drawable = Drawable.createFromStream(
                                    new URL(mSendGiftInfo.getGifticon()).openStream(), "src");
                            if (drawable != null) {
                                drawable.setBounds(0, 6, (int) YueJianAppTDevice.dpToPixel(18), (int) YueJianAppTDevice.dpToPixel(18));
                            }
                            ImageSpan hearImage = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                            msg.setSpan(
                                    hearImage, msg.length() - 1, msg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            int index = -1;
                            if (contentJson.has("index")) {
                                index = contentJson.getInt("index");
                            }
                            c.setSendChatMsg(msg);
                            c.setUserNick(name);
                            mChatServer.onShowSendGift(mSendGiftInfo, c, index, broadCastVotes);
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                YueJianAppRavenUtils.captureMessage(
                        context, e, YueJianAppRavenUtils.ERROR, YueJianAppRavenUtils.DEBUG, Event.Level.ERROR, null);
            }
        }
    };

    /**
     * @param
     * @dw 连接socket服务端
     */
    public void connectSocketServer(String id, String token) {
        mUid = id;
        mToken = token;
        try {
            if (null != mSocket) {
                Manager io = mSocket.io();
                io.reconnection(true);
                io.reconnectionDelay(3000);
                io.reconnectionAttempts(Integer.MAX_VALUE);
                mSocket.connect();
                mSocket.on("conn", onConn);
                mSocket.on(YueJianAppISocketCommon.BROADCAST, onMessage);
                mSocket.on(mSocket.EVENT_DISCONNECT, onDisconnect);
                mSocket.on(mSocket.EVENT_ERROR, onError);
                mSocket.on(mSocket.EVENT_RECONNECTING, onReconnecting);
                mSocket.on(mSocket.EVENT_RECONNECT, onReconnect);
                mSocket.on(mSocket.EVENT_CONNECT, onConnect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param handler 定时发送心跳包,在连接成功后调用
     */
    public void heartbeat(final Handler handler) {
        if (handler == null)
            return;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                YueJianAppTLog.info(">>>>heartbeat");
                if (mSocket == null) {
                    YueJianAppTLog.error("socket == null");
                    return;
                }
//                YueJianAppTLog.info("socket is connect:%s", mSocket.connected());
                if (!mSocket.connected()) {
                    YueJianAppTLog.warn("心跳包发送失败");
                    mSocket.disconnect();
                    mSocket.connect();
                    return;
                }
                YueJianAppChatProtoBuilder.sendCCSendHearBeatRpt(mSocket);
                handler.postDelayed(this, 4000);
            }
        }, 4000);
    }

    //释放资源
    public void close() {
        if (mSocket != null) {
            Manager io = mSocket.io();
            io.reconnection(false);
            mSocket.disconnect();
            mSocket.off();
            if (mSocket != null) {
                mSocket.close();
            }
            mSocket = null;
        }
    }

    public void error() {
        if (mSocket != null) {
            mSocket.emit(mSocket.EVENT_ERROR, new Object[]{});
        }
    }
}
