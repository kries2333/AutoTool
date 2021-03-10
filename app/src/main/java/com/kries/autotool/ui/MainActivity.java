package com.kries.autotool.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.renderscript.Script;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kries.autotool.R;
import com.kries.autotool.autojs.AutoJs;
import com.kries.autotool.common.uity;
import com.kries.autotool.database.DatabaseHelper;
import com.kries.autotool.model.script.ScriptFile;
import com.kries.autotool.model.script.Scripts;
import com.kries.autotool.network.JWebsocketClient;
import com.stardust.app.GlobalAppContext;
import com.stardust.autojs.core.storage.LocalStorage;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.UUID;

public class MainActivity extends Activity {

    private String clientId;
    private String scrpitName;
    private URI    uri;
    private String TAG = "MainActivity";
    private String Url = "ws://192.168.0.107:8089/android";
    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.uri = URI.create(this.Url);
        this.helper = new DatabaseHelper(getApplicationContext());

        Button btn = (Button) this.findViewById(R.id.start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editToken = findViewById(R.id.editTextToken);
                EditText editDeviceName = findViewById(R.id.editTextDeviceName);



                Context ctx = getApplicationContext();
                String imei = uity.getIMEI(ctx);
                String androidOs = uity.getSystemVersion();
                String model = uity.getSystemModel();
                String memory = uity.getMemory(ctx);
                String display_x = uity.getDisplayX(ctx);
                String display_y = uity.getDisplayY(ctx);
                String token = editToken.getText().toString();
                String deviceName = editDeviceName.getText().toString();

                if (imei == "") return;

                String deviceId = uity.ComputeDeviceId(token, deviceName, androidOs, model, imei);

                try {
                    JSONObject data = new JSONObject();
                    data.putOpt("appid", 101);
                    data.putOpt("token", token);
                    data.putOpt("device_id", deviceId);
                    data.putOpt("device_name", deviceName);
                    data.putOpt("android_os", androidOs);
                    data.putOpt("model", model);
                    data.putOpt("imei", imei);
                    data.putOpt("memory", memory);
                    data.putOpt("display_x", display_x);
                    data.putOpt("display_y", display_y);

                    JWebsocketClient client = new JWebsocketClient(uri, data);
                    try {
                        client.connect();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
