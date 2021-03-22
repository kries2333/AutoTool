package com.kries.autotool.network;

import android.content.Context;
import android.util.Log;

import com.kries.autotool.common.uity;
import com.kries.autotool.database.DatabaseHelper;
import com.kries.autotool.model.script.Scripts;
import com.stardust.autojs.script.StringScriptSource;
import com.stardust.pio.PFiles;
import com.stardust.view.accessibility.AccessibilityNotificationObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

interface MessageCallback {
    void Open();
    void Start(String message);
    void Stop();
    void Download(byte[] message);
}

public class MessageController implements MessageCallback {
    private String TAG = "MessageController";
    private String Url = "ws://192.168.0.115:8089/android";
    private URI    uri;
    private Context ctx;
    private DatabaseHelper helper;
    private JWebsocketClient client;

    public MessageController(Context context, DatabaseHelper helper) {
        this.uri = URI.create(this.Url);
        this.ctx = context;
        this.helper = helper;
    }

    public void Connent(String token, String deviceName) {
        String imei = uity.getIMEI(ctx);
        String androidOs = uity.getSystemVersion();
        String model = uity.getSystemModel();
        String memory = uity.getMemory(ctx);
        String display_x = uity.getDisplayX(ctx);
        String display_y = uity.getDisplayY(ctx);

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

            client = new JWebsocketClient(uri, data, this);
            try {
                client.connect();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Open() {

    }

    @Override
    public void Start(String message) {
        Log.d(TAG, message);

        try {
            JSONObject root = new JSONObject(message);
            String path = root.getString("path");
            JSONObject data = new JSONObject();
            data.put("file_name", path);

            JSONObject sendObj = new JSONObject();
            sendObj.put("cmd", "download");
            sendObj.put("data", data);

            client.send(sendObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Stop() {

    }

    @Override
    public void Download(byte[] message) {
        InputStream stream = null;
        stream = new ByteArrayInputStream(message);
        Scripts.INSTANCE.run(new StringScriptSource(PFiles.read(stream)));
    }

    private ArrayList<TaskModel> parseTaskJson(String message) {
        ArrayList<TaskModel> TaskList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(message);
            for (int i = 0; i < array.length(); i++) {
                JSONObject root = (JSONObject) array.get(i);
                TaskModel taskInfo = new TaskModel();
                taskInfo.SetName(root.getString("name"));
                taskInfo.SetParams(root.getString("params"));

                TaskList.add(taskInfo);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return TaskList;
    }
}