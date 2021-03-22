package com.stardust.autojs.runtime.api;

import androidx.annotation.Nullable;

import com.stardust.autojs.annotation.ScriptInterface;

public interface Statistics {

    @ScriptInterface
    void set(Object key, Object value);

    @ScriptInterface
    void show();
}
