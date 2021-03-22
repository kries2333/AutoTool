package com.kries.autotool.network;

import android.util.Log;

import com.kries.autotool.LogUtil;
import com.kries.autotool.autojs.AutoJs;
import com.kries.autotool.common.uity;
import com.kries.autotool.model.script.Scripts;
import com.stardust.autojs.script.StringScriptSource;
import com.stardust.pio.PFile;
import com.stardust.pio.PFiles;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.logging.Handler;

//"appid": 101,
//"device_name": "A01",
//"android_os": "7.0",
//"model": "NEW-AL10",
//"imei": "564895674894564156",
//"memory": "2048",
//"display_x": "1080",
//"display_y": "1920",
//"token": "286db2406c24",
//"device_id": "72e2869fad025f10"

public class JWebsocketClient extends WebSocketClient {
    private String TAG = "JWebsocketClient";
    private JSONObject data;
    private MessageCallback callBack;

    public JWebsocketClient(URI serverUri, JSONObject data, MessageCallback callback) {
        super(serverUri, new Draft_6455());
        this.data = data;
        this.callBack = callback;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("JWebSocketClient", "onOpen()");

        try {
            JSONObject root = new JSONObject();
            root.putOpt("cmd", "login");
            root.putOpt("data", this.data);

            this.send(root.toString());
        }catch (Exception e) {

        }
    }

    @Override
    public void onMessage(String message) {
        LogUtil.printJson(TAG, message, "");
        try {
            JSONObject messageObj = new JSONObject(message);
            String cmd = messageObj.getString("cmd");
            switch (cmd) {
                case "start":
                    // 首先解析参数
                    JSONObject responseObj = messageObj.getJSONObject("response");
                    JSONObject dataObj = responseObj.getJSONObject("data");
                    callBack.Start(dataObj.getString("msg"));
                    break;
                case "stop":
                    break;
                case "download":
                    JSONObject response = messageObj.getJSONObject("response");
                    String codeMsg = response.getString("codeMsg");
                    try {
                        callBack.Download(codeMsg.getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("JWebSocketClient", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        Log.e("JWebSocketClient", "error=" + ex);
    }

}
