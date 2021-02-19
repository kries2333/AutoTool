package com.kries.autotool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.Script;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kries.autotool.R;
import com.kries.autotool.autojs.AutoJs;
import com.kries.autotool.model.script.ScriptFile;
import com.kries.autotool.model.script.Scripts;
import com.kries.autotool.network.JWebsocketClient;
import com.stardust.app.GlobalAppContext;
import com.stardust.autojs.core.storage.LocalStorage;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class MainActivity extends Activity {

    private String clientId;
    private String scrpitName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URI uri = URI.create("ws://192.168.0.100:8081/ws?systemId=1221323131231");
        JWebsocketClient client = new JWebsocketClient(uri) {
            @Override
            public void onMessage(String message) {
                //message就是接收到的消息
                Log.d("JWebSClientService", message);

                //收到连接成功后
                try {
                    JSONObject root = new JSONObject(message);
                    int code = root.getInt("code");
                    if (code == 0) {
                        String msg = root.getString("Msg");
                        switch (msg) {
                            case "connected":   // 连接成功
                            {
                                JSONObject data = root.getJSONObject("data");
                                clientId = data.getString("clientId");

                                LocalStorage storage = new LocalStorage(getApplicationContext(), "auto_tool");
                                storage.put("client_id", clientId);
                                break;
                            }
                            case "start":   // 后台启动脚本
                            {
                                JSONObject data = root.getJSONObject("data");
                                scrpitName = data.getString("scrpitname");

                                Scripts.INSTANCE.run(new ScriptFile("/storage/emulated/0/脚本/" + scrpitName));
                                break;
                            }
                            case "stop":  // 后台停止脚本
                                break;

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Button btn = (Button) this.findViewById(R.id.start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
                try {
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
