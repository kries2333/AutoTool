package com.stardust.autojs;

import com.stardust.autojs.engine.ScriptEngineManager;
import com.stardust.autojs.runtime.ScriptRuntime;
import com.stardust.autojs.runtime.api.Console;
import com.stardust.autojs.runtime.api.Statistics;
import com.stardust.util.Supplier;
import com.stardust.util.UiHandler;

/**
 * Created by Stardust on 2017/4/2.
 */

public class ScriptEngineServiceBuilder {

    ScriptEngineManager mScriptEngineManager;
    Console mGlobalConsole;
    Statistics mGlobalStatistics;
    UiHandler mUiHandler;

    public ScriptEngineServiceBuilder() {

    }

    public ScriptEngineServiceBuilder uiHandler(UiHandler uiHandler) {
        mUiHandler = uiHandler;
        return this;
    }

    public ScriptEngineServiceBuilder engineManger(ScriptEngineManager manager) {
        mScriptEngineManager = manager;
        return this;
    }

    public ScriptEngineServiceBuilder globalConsole(Console console) {
        mGlobalConsole = console;
        return this;
    }

    public ScriptEngineServiceBuilder globalStatistics(Statistics statistics) {
        mGlobalStatistics = statistics;
        return this;
    }

    public ScriptEngineService build() {
        return new ScriptEngineService(this);
    }


}
