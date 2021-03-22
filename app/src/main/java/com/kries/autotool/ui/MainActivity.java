package com.kries.autotool.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.Script;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.kries.autotool.R;
import com.kries.autotool.autojs.AutoJs;
import com.kries.autotool.common.uity;
import com.kries.autotool.database.DatabaseHelper;
import com.kries.autotool.model.script.ScriptFile;
import com.kries.autotool.model.script.Scripts;
import com.kries.autotool.network.JWebsocketClient;
import com.kries.autotool.network.MessageController;
import com.kries.autotool.tool.ActivityUtil;
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

    private String TAG = "MainActivity";

    private DatabaseHelper helper;
    private SharedPreferences sharedPreferences;
    private MessageController controller;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("data",Context.MODE_PRIVATE);

        this.helper = new DatabaseHelper(getApplicationContext());
        this.controller = new MessageController(getApplicationContext(), this.helper);

        EditText editToken = findViewById(R.id.editTextToken);
        EditText editDeviceName = findViewById(R.id.editTextDeviceName);

        editToken.setText(GetAccount());
        editDeviceName.setText(GetDeviceName());

        Button btn = (Button) this.findViewById(R.id.start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.Connent(editToken.getText().toString(), editDeviceName.getText().toString());
//                startService(intent);
            }
        });
    }

    private void SetAccoutData(String token, String device_name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.putString("device_name", device_name);
        editor.commit();
    }

    private String GetAccount() {
        return sharedPreferences.getString("token", "");
    }

    private String GetDeviceName() {
        return sharedPreferences.getString("device_name", "");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startFloatingService() {
        if (ActivityUtil.isServiceWork(this, "com.demon.suspensionbox.FloatingService")) {//防止重复启动
            Toast.makeText(this, "已启动！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        } else {
            startService(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(intent);
            }
        }
    }
}
