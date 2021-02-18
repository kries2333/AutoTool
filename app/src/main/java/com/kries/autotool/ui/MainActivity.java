package com.kries.autotool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.Script;
import android.view.View;
import android.widget.Button;

import com.kries.autotool.R;
import com.kries.autotool.autojs.AutoJs;
import com.kries.autotool.model.script.ScriptFile;
import com.kries.autotool.model.script.Scripts;
import com.stardust.app.GlobalAppContext;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //连接服务器

        //加载脚本

        //获取参数



        Button btn = (Button) this.findViewById(R.id.start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
                Scripts.INSTANCE.run(new ScriptFile("/storage/emulated/0/脚本/随机养号.js"));
            }
        });
    }

    private void init() {
        AutoJs.initInstance(getApplication());
    }
}
