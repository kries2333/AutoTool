package com.kries.autotool.network;

import android.util.Log;

import com.kries.autotool.common.uity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

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

    private JSONObject data;

    public JWebsocketClient(URI serverUri, JSONObject data) {
        super(serverUri, new Draft_6455());
        this.data = data;
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
        Log.e("JWebSocketClient", "onMessage()");
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
